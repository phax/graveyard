/*
    Copyright (c) 2007-2014 Contributors as noted in the AUTHORS file

    This file is part of 0MQ.

    0MQ is free software; you can redistribute it and/or modify it under
    the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    0MQ is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.helger.zeromq.guide;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZLoop;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.PollItem;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMsg;
import org.zeromq.ZThread;

/**
 * Load-balancing broker Demonstrates use of the ZLoop API and reactor style The
 * client and worker tasks are identical from the previous example.
 */
public class lbbroker3
{
  private static final int NBR_CLIENTS = 10;
  private static final int NBR_WORKERS = 3;
  private static byte [] WORKER_READY = { '\001' };

  /**
   * Basic request-reply client using REQ socket
   */
  private static class ClientTask implements ZThread.IDetachedRunnable
  {
    @Override
    public void run (final Object [] args)
    {
      try (final ZContext context = new ZContext ())
      {
        // Prepare our context and sockets
        final Socket client = context.createSocket (ZMQ.REQ);
        ZHelper.setId (client); // Set a printable identity

        client.connect ("ipc://frontend.ipc");

        // Send request, get reply
        client.send ("HELLO");
        final String reply = client.recvStr ();
        System.out.println ("Client: " + reply);
      }
    }
  }

  /**
   * Worker using REQ socket to do load-balancing
   */
  private static class WorkerTask implements ZThread.IDetachedRunnable
  {
    @Override
    public void run (final Object [] args)
    {
      try (final ZContext context = new ZContext ())
      {
        // Prepare our context and sockets
        final Socket worker = context.createSocket (ZMQ.REQ);
        ZHelper.setId (worker); // Set a printable identity

        worker.connect ("ipc://backend.ipc");

        // Tell backend we're ready for work
        final ZFrame frame = new ZFrame (WORKER_READY);
        frame.send (worker, 0);

        while (true)
        {
          final ZMsg msg = ZMsg.recvMsg (worker);
          if (msg == null)
            break;

          msg.getLast ().reset ("OK");
          msg.send (worker);
        }
      }
    }
  }

  // Our load-balancer structure, passed to reactor handlers
  private static class LBBroker
  {
    Socket frontend; // Listen to clients
    Socket backend; // Listen to workers
    Queue <ZFrame> workers; // List of ready workers
  }

  /**
   * In the reactor design, each time a message arrives on a socket, the reactor
   * passes it to a handler function. We have two handlers; one for the
   * frontend, one for the backend:
   */
  private static class FrontendHandler implements ZLoop.IZLoopHandler
  {

    @Override
    public int handle (final ZLoop loop, final PollItem item, final Object arg_)
    {

      final LBBroker arg = (LBBroker) arg_;
      final ZMsg msg = ZMsg.recvMsg (arg.frontend);
      if (msg != null)
      {
        msg.wrap (arg.workers.poll ());
        msg.send (arg.backend);

        // Cancel reader on frontend if we went from 1 to 0 workers
        if (arg.workers.size () == 0)
        {
          loop.removePoller (new PollItem (arg.frontend, 0));
        }
      }
      return 0;
    }

  }

  private static class BackendHandler implements ZLoop.IZLoopHandler
  {

    @Override
    public int handle (final ZLoop loop, final PollItem item, final Object arg_)
    {

      final LBBroker arg = (LBBroker) arg_;
      final ZMsg msg = ZMsg.recvMsg (arg.backend);
      if (msg != null)
      {
        final ZFrame address = msg.unwrap ();
        // Queue worker address for load-balancing
        arg.workers.add (address);

        // Enable reader on frontend if we went from 0 to 1 workers
        if (arg.workers.size () == 1)
        {
          final PollItem newItem = new PollItem (arg.frontend, ZMQ.Poller.POLLIN);
          loop.addPoller (newItem, frontendHandler, arg);
        }

        // Forward message to client if it's not a READY
        final ZFrame frame = msg.getFirst ();
        if (Arrays.equals (frame.getData (), WORKER_READY))
          msg.destroy ();
        else
          msg.send (arg.frontend);
      }
      return 0;
    }
  }

  private final static FrontendHandler frontendHandler = new FrontendHandler ();
  private final static BackendHandler backendHandler = new BackendHandler ();

  /**
   * And the main task now sets-up child tasks, then starts its reactor. If you
   * press Ctrl-C, the reactor exits and the main task shuts down.
   * 
   * @param args
   *        ignored
   */
  public static void main (final String [] args)
  {
    try (final ZContext context = new ZContext ())
    {
      final LBBroker arg = new LBBroker ();
      // Prepare our context and sockets
      arg.frontend = context.createSocket (ZMQ.ROUTER);
      arg.backend = context.createSocket (ZMQ.ROUTER);
      arg.frontend.bind ("ipc://frontend.ipc");
      arg.backend.bind ("ipc://backend.ipc");

      int clientNbr;
      for (clientNbr = 0; clientNbr < NBR_CLIENTS; clientNbr++)
        ZThread.start (new ClientTask ());

      for (int workerNbr = 0; workerNbr < NBR_WORKERS; workerNbr++)
        ZThread.start (new WorkerTask ());

      // Queue of available workers
      arg.workers = new LinkedList<> ();

      // Prepare reactor and fire it up
      final ZLoop reactor = new ZLoop ();
      final PollItem item = new PollItem (arg.backend, ZMQ.Poller.POLLIN);
      reactor.addPoller (item, backendHandler, arg);
      reactor.start ();
    }
  }

}
