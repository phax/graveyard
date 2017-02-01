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

//  Pathological publisher
//  Sends out 1,000 topics and then one random update per second
public class pathopub
{
  public static void main (final String [] args) throws Exception
  {
    try (final ZContext context = new ZContext ())
    {
      final Socket publisher = context.createSocket (ZMQ.PUB);
      if (args.length == 1)
        publisher.connect (args[0]);
      else
        publisher.bind ("tcp://*:5556");

      // Ensure subscriber connection has time to complete
      Thread.sleep (1000);

      // Send out all 1,000 topic messages
      int topicNbr;
      for (topicNbr = 0; topicNbr < 1000; topicNbr++)
      {
        publisher.send (StringHelper.getLeadingZero (topicNbr, 3), ZMQ.SNDMORE);
        publisher.send ("Save Roger");
      }
      // Send one random update per second
      final Random rand = new Random (System.currentTimeMillis ());
      while (!Thread.currentThread ().isInterrupted ())
      {
        Thread.sleep (1000);
        publisher.send (StringHelper.getLeadingZero (rand.nextInt (1000), 3), ZMQ.SNDMORE);
        publisher.send ("Off with his head!");
      }
    }
  }
}
