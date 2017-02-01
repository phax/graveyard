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

import org.zeromq.ZMQ;

//
//Custom routing Router to Mama (ROUTER to REQ)
//
public class rtmama
{

  private static final int NBR_WORKERS = 10;

  public static class Worker implements Runnable
  {
    private final byte [] END = "END".getBytes (ZMQ.CHARSET);

    public void run ()
    {
      try (final ZMQ.Context context = ZMQ.context (1))
      {
        try (final ZMQ.Socket worker = context.socket (ZMQ.REQ))
        {
          // worker.setIdentity(); will set a random id automatically
          worker.connect ("ipc://routing.ipc");

          int total = 0;
          while (true)
          {
            worker.send ("ready", 0);
            final byte [] workerload = worker.recv (0);
            if (Arrays.equals (workerload, END))
            {
              System.out.println ("Processs " + total + " tasks.");
              break;
            }
            total += 1;
          }
        }
      }
    }
  }

  @SuppressWarnings ("unused")
  public static void main (final String [] args)
  {
    try (final ZMQ.Context context = ZMQ.context (1))
    {
      try (final ZMQ.Socket client = context.socket (ZMQ.ROUTER))
      {
        client.bind ("ipc://routing.ipc");

        for (int i = 0; i != NBR_WORKERS; i++)
        {
          new Thread (new Worker ()).start ();
        }

        for (int i = 0; i != NBR_WORKERS; i++)
        {
          // LRU worker is next waiting in queue
          final byte [] address = client.recv (0);
          final byte [] empty = client.recv (0);
          final byte [] ready = client.recv (0);

          client.send (address, ZMQ.SNDMORE);
          client.send ("", ZMQ.SNDMORE);
          client.send ("This is the workload", 0);
        }

        for (int i = 0; i != NBR_WORKERS; i++)
        {
          // LRU worker is next waiting in queue
          final byte [] address = client.recv (0);
          final byte [] empty = client.recv (0);
          final byte [] ready = client.recv (0);

          client.send (address, ZMQ.SNDMORE);
          client.send ("", ZMQ.SNDMORE);
          client.send ("END", 0);
        }

        // Now ask mamas to shut down and report their results
      }
    }
  }

}
