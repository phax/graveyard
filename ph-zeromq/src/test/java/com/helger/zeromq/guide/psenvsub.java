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
 * Pubsub envelope subscriber
 */

public class psenvsub
{
  public static void main (final String [] args)
  {
    // Prepare our context and subscriber
    try (final Context context = ZMQ.context (1))
    {
      try (final Socket subscriber = context.socket (ZMQ.SUB))
      {

        subscriber.connect ("tcp://localhost:5563");
        subscriber.subscribe ("B".getBytes (ZMQ.CHARSET));
        while (!Thread.currentThread ().isInterrupted ())
        {
          // Read envelope with address
          final String address = subscriber.recvStr ();
          // Read message contents
          final String contents = subscriber.recvStr ();
          System.out.println (address + " : " + contents);
        }
      }
    }
  }
}
