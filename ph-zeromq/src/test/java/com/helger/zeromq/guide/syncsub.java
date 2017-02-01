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
 * Synchronized subscriber.
 */
public class syncsub
{

  public static void main (final String [] args)
  {
    try (final Context context = ZMQ.context (1))
    {
      // First, connect our subscriber socket
      try (final Socket subscriber = context.socket (ZMQ.SUB))
      {
        subscriber.setRcvHWM (0);
        subscriber.connect ("tcp://localhost:5561");
        subscriber.subscribe (ZMQ.SUBSCRIPTION_ALL);

        // Second, synchronize with publisher
        try (final Socket syncclient = context.socket (ZMQ.REQ))
        {
          syncclient.connect ("tcp://localhost:5562");

          // - send a synchronization request
          syncclient.send (ArrayHelper.EMPTY_BYTE_ARRAY, 0);

          // - wait for synchronization reply
          syncclient.recv (0);

          System.out.println ("subscriber registered");

          // Third, get our updates and report how many we got
          int update_nbr = 0;
          while (true)
          {
            final String string = subscriber.recvStr (0);
            if (string.equals ("END"))
            {
              break;
            }
            update_nbr++;

            if (false)
              if ((update_nbr % 50_000) == 0)
                System.out.println ("So far received " + update_nbr + " updates.");
          }
          System.out.println ("Received " + update_nbr + " updates.");
        }
      }
    }
  }
}
