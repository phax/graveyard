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

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

/**
 * ROUTER-TO-REQ example
 */
public class rtreq
{
  private static Random rand = new Random ();
  private static final int NBR_WORKERS = 10;

  private static class Worker extends Thread
  {

    @Override
    public void run ()
    {
      try (final Context context = ZMQ.context (1))
      {
        try (final Socket worker = context.socket (ZMQ.REQ))
        {
          ZHelper.setId (worker); // Set a printable identity

          worker.connect ("tcp://localhost:5671");

          int total = 0;
          while (true)
          {
            // Tell the broker we're ready for work
            worker.send ("Hi Boss");

            // Get workload from broker, until finished
            final String workload = worker.recvStr ();
            final boolean finished = workload.equals ("Fired!");
            if (finished)
            {
              System.out.println ("Completed: " + total + " tasks");
              break;
            }
            total++;

            // Do some random work
            try
            {
              Thread.sleep (rand.nextInt (500) + 1);
            }
            catch (final InterruptedException e)
            {}
          }
        }
      }
    }
  }

  /*
   * While this example runs in a single process, that is just to make it easier
   * to start and stop the example. Each thread has its own context and
   * conceptually acts as a separate process.
   */
  public static void main (final String [] args) throws Exception
  {
    try (final Context context = ZMQ.context (1); final Socket broker = context.socket (ZMQ.ROUTER))
    {
      broker.bind ("tcp://*:5671");

      for (int workerNbr = 0; workerNbr < NBR_WORKERS; workerNbr++)
      {
        final Thread worker = new Worker ();
        worker.start ();
      }

      // Run for five seconds and then tell workers to end
      final long endTime = System.currentTimeMillis () + 5000;
      int workersFired = 0;
      while (true)
      {
        // Next message gives us least recently used worker
        final String identity = broker.recvStr ();
        broker.sendMore (identity);
        broker.recvStr (); // Envelope delimiter
        broker.recvStr (); // Response from worker
        broker.sendMore ("");

        // Encourage workers until it's time to fire them
        if (System.currentTimeMillis () < endTime)
          broker.send ("Work harder");
        else
        {
          broker.send ("Fired!");
          if (++workersFired == NBR_WORKERS)
            break;
        }
      }
    }
  }
}
