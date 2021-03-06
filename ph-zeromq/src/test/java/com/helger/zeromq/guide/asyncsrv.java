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

import java.util.Random;

import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.PollItem;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMsg;

import com.helger.commons.string.StringHelper;

//
//Asynchronous client-to-server (DEALER to ROUTER)
//
//While this example runs in a single process, that is just to make
//it easier to start and stop the example. Each task has its own
//context and conceptually acts as a separate process.

public class asyncsrv
{
  // ---------------------------------------------------------------------
  // This is our client task
  // It connects to the server, and then sends a request once per second
  // It collects responses as they arrive, and it prints them out. We will
  // run several client tasks in parallel, each with a different random ID.

  private static final Random rand = new Random (System.nanoTime ());

  private static class client_task implements Runnable
  {
    public void run ()
    {
      try (final ZContext ctx = new ZContext ())
      {
        final Socket client = ctx.createSocket (ZMQ.DEALER);

        // Set random identity to make tracing easier
        final String identity = StringHelper.getHexStringLeadingZero (rand.nextInt (0x10000), 4) +
                                "-" +
                                StringHelper.getHexStringLeadingZero (rand.nextInt (0x10000), 4);
        client.setIdentity (identity.getBytes (ZMQ.CHARSET));
        client.connect ("tcp://localhost:5570");

        final PollItem [] items = new PollItem [] { new PollItem (client, Poller.POLLIN) };

        int requestNbr = 0;
        while (!Thread.currentThread ().isInterrupted ())
        {
          // Tick once per second, pulling in arriving messages
          for (int centitick = 0; centitick < 100; centitick++)
          {
            ZMQ.poll (items, 10);
            if (items[0].isReadable ())
            {
              final ZMsg msg = ZMsg.recvMsg (client);
              msg.getLast ().print (identity);
              msg.destroy ();
            }
          }
          client.send ("request #" + (++requestNbr), 0);
        }
      }
    }
  }

  // This is our server task.
  // It uses the multithreaded server model to deal requests out to a pool
  // of workers and route replies back to clients. One worker can handle
  // one request at a time but one client can talk to multiple workers at
  // once.

  private static class server_task implements Runnable
  {
    public void run ()
    {
      try (final ZContext ctx = new ZContext ())
      {
        // Frontend socket talks to clients over TCP
        final Socket frontend = ctx.createSocket (ZMQ.ROUTER);
        frontend.bind ("tcp://*:5570");

        // Backend socket talks to workers over inproc
        final Socket backend = ctx.createSocket (ZMQ.DEALER);
        backend.bind ("inproc://backend");

        // Launch pool of worker threads, precise number is not critical
        for (int threadNbr = 0; threadNbr < 5; threadNbr++)
          new Thread (new server_worker (ctx)).start ();

        // Connect backend to frontend via a proxy
        ZMQ.proxy (frontend, backend, null);
      }
    }
  }

  // Each worker task works on one request at a time and sends a random number
  // of replies back, with random delays between replies:

  private static class server_worker implements Runnable
  {
    private final ZContext m_aCtx;

    public server_worker (final ZContext ctx)
    {
      this.m_aCtx = ctx;
    }

    public void run ()
    {
      final Socket worker = m_aCtx.createSocket (ZMQ.DEALER);
      worker.connect ("inproc://backend");

      while (!Thread.currentThread ().isInterrupted ())
      {
        // The DEALER socket gives us the address envelope and message
        final ZMsg msg = ZMsg.recvMsg (worker);
        final ZFrame address = msg.pop ();
        final ZFrame content = msg.pop ();
        assert (content != null);
        msg.destroy ();

        // Send 0..4 replies back
        final int replies = rand.nextInt (5);
        for (int reply = 0; reply < replies; reply++)
        {
          // Sleep for some fraction of a second
          try
          {
            Thread.sleep (rand.nextInt (1000) + 1);
          }
          catch (final InterruptedException e)
          {}
          address.send (worker, ZFrame.REUSE + ZFrame.MORE);
          content.send (worker, ZFrame.REUSE);
        }
        address.destroy ();
        content.destroy ();
      }
      m_aCtx.destroy ();
    }
  }

  // The main thread simply starts several clients, and a server, and then
  // waits for the server to finish.

  public static void main (final String [] args) throws Exception
  {
    try (final ZContext ctx = new ZContext ())
    {
      new Thread (new client_task ()).start ();
      new Thread (new client_task ()).start ();
      new Thread (new client_task ()).start ();
      new Thread (new server_task ()).start ();

      // Run for 5 seconds then quit
      Thread.sleep (5 * 1000);
    }
  }
}
