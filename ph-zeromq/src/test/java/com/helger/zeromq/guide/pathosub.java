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
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import com.helger.commons.string.StringHelper;

//  Pathological subscriber
//  Subscribes to one random topic and prints received messages
public class pathosub
{
  public static void main (final String [] args)
  {
    try (final ZContext context = new ZContext ())
    {
      final Socket subscriber = context.createSocket (ZMQ.SUB);
      if (args.length == 1)
        subscriber.connect (args[0]);
      else
        subscriber.connect ("tcp://localhost:5556");

      final Random rand = new Random (System.currentTimeMillis ());
      final String subscription = StringHelper.getLeadingZero (rand.nextInt (1000), 3);
      subscriber.subscribe (subscription.getBytes (ZMQ.CHARSET));

      while (true)
      {
        final String topic = subscriber.recvStr ();
        if (topic == null)
          break;
        final String data = subscriber.recvStr ();
        assert (topic.equals (subscription));
        System.out.println (data);
      }
    }
  }
}
