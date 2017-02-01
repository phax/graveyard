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

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

/**
 * Multi threaded Hello World server
 */
public class mtserver
{

  private static class Worker extends Thread
  {
    private final Context _context;

    private Worker (final Context context)
    {
      this._context = context;
    }

    @Override
    public void run ()
    {
      final ZMQ.Socket socket = _context.socket (ZMQ.REP);
      socket.connect ("inproc://workers");

      while (true)
      {

        // Wait for next request from client (C string)
        final String request = socket.recvStr (0);
        System.out.println (Thread.currentThread ().getName () + " Received request: [" + request + "]");

        // Do some 'work'
        try
        {
          Thread.sleep (1000);
        }
        catch (final InterruptedException e)
        {}

        // Send reply back to client (C string)
        socket.send ("world", 0);
      }
    }
  }

  public static void main (final String [] args)
  {
    try (final Context context = ZMQ.context (1))
    {
      try (final Socket clients = context.socket (ZMQ.ROUTER))
      {
        clients.bind ("tcp://*:5555");

        try (final Socket workers = context.socket (ZMQ.DEALER))
        {
          workers.bind ("inproc://workers");

          for (int thread_nbr = 0; thread_nbr < 5; thread_nbr++)
          {
            final Thread worker = new Worker (context);
            worker.start ();
          }
          // Connect work threads to client threads via a queue
          ZMQ.proxy (clients, workers, null);

          // We never get here but clean up anyhow
        }
      }
    }
  }
}
