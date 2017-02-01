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

import com.helger.commons.collection.ArrayHelper;

/**
 * Synchronized publisher.
 */
public class syncpub
{
  /**
   * We wait for 10 subscribers
   */
  private static int SUBSCRIBERS_EXPECTED = 10;

  public static void main (final String [] args)
  {
    try (final Context context = ZMQ.context (1))
    {
      // Socket to talk to clients
      try (final Socket publisher = context.socket (ZMQ.PUB))
      {
        publisher.setLinger (5000);
        // In 0MQ 3.x pub socket could drop messages if sub can follow the
        // generation of pub messages
        publisher.setSndHWM (0);
        publisher.bind ("tcp://*:5561");

        // Socket to receive signals
        try (final Socket syncservice = context.socket (ZMQ.REP))
        {
          syncservice.bind ("tcp://*:5562");

          System.out.println ("Waiting subscribers");
          // Get synchronization from subscribers
          int subscribers = 0;
          while (subscribers < SUBSCRIBERS_EXPECTED)
          {
            // - wait for synchronization request
            syncservice.recv (0);

            // - send synchronization reply
            syncservice.send (ArrayHelper.EMPTY_BYTE_ARRAY, 0);
            subscribers++;

            System.out.println ("Having " + subscribers + " subscribers");
          }
          // Now broadcast exactly 1M updates followed by END
          System.out.println ("Broadcasting messages");

          int update_nbr;
          for (update_nbr = 0; update_nbr < 1_000_000; update_nbr++)
          {
            publisher.send ("Rhubarb", 0);
          }

          publisher.send ("END", 0);
          System.out.println ("Done broadcasting messages");
        }
      }
    }
  }
}
