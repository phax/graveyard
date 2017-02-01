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
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZThread;
import org.zeromq.ZThread.IAttachedRunnable;

import com.helger.commons.string.StringHelper;

//  Espresso Pattern
//  This shows how to capture data using a pub-sub proxy
public class espresso
{
  // The subscriber thread requests messages starting with
  // A and B, then reads and counts incoming messages.
  private static class Subscriber implements IAttachedRunnable
  {

    @Override
    public void run (final Object [] args, final ZContext ctx, final Socket pipe)
    {
      // Subscribe to "A" and "B"
      final Socket subscriber = ctx.createSocket (ZMQ.SUB);
      subscriber.connect ("tcp://localhost:6001");
      subscriber.subscribe ("A".getBytes (ZMQ.CHARSET));
      subscriber.subscribe ("B".getBytes (ZMQ.CHARSET));

      int count = 0;
      while (count < 5)
      {
        final String string = subscriber.recvStr ();
        if (string == null)
          break; // Interrupted
        count++;
      }
      ctx.destroySocket (subscriber);
    }
  }

  // .split publisher thread
  // The publisher sends random messages starting with A-J:
  private static class Publisher implements IAttachedRunnable
  {
    @Override
    public void run (final Object [] args, final ZContext ctx, final Socket pipe)
    {
      final Socket publisher = ctx.createSocket (ZMQ.PUB);
      publisher.bind ("tcp://*:6000");
      final Random rand = new Random (System.currentTimeMillis ());

      while (!Thread.currentThread ().isInterrupted ())
      {
        final String string = (char) ('A' + rand.nextInt (10)) +
                              "-" +
                              StringHelper.getLeadingZero (rand.nextInt (100000), 5);
        if (!publisher.send (string))
          break; // Interrupted
        try
        {
          Thread.sleep (100); // Wait for 1/10th second
        }
        catch (final InterruptedException e)
        {}
      }
      ctx.destroySocket (publisher);
    }
  }

  // .split listener thread
  // The listener receives all messages flowing through the proxy, on its
  // pipe. In CZMQ, the pipe is a pair of ZMQ_PAIR sockets that connect
  // attached child threads. In other languages your mileage may vary:
  private static class Listener implements IAttachedRunnable
  {
    @Override
    public void run (final Object [] args, final ZContext ctx, final Socket pipe)
    {
      // Print everything that arrives on pipe
      while (true)
      {
        final ZFrame frame = ZFrame.recvFrame (pipe);
        if (frame == null)
          break; // Interrupted
        frame.print (null);
        frame.destroy ();
      }
    }
  }

  // .split main thread
  // The main task starts the subscriber and publisher, and then sets
  // itself up as a listening proxy. The listener runs as a child thread:
  public static void main (final String [] argv)
  {
    // Start child threads
    try (final ZContext ctx = new ZContext ())
    {
      ZThread.fork (ctx, new Publisher ());
      ZThread.fork (ctx, new Subscriber ());

      final Socket subscriber = ctx.createSocket (ZMQ.XSUB);
      subscriber.connect ("tcp://localhost:6000");
      final Socket publisher = ctx.createSocket (ZMQ.XPUB);
      publisher.bind ("tcp://*:6001");
      final Socket listener = ZThread.fork (ctx, new Listener ());
      ZMQ.proxy (subscriber, publisher, listener);

      System.out.println (" interrupted");
      // Tell attached threads to exit
    }
  }
}
