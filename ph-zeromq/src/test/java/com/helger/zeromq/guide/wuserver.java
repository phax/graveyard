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

import com.helger.commons.string.StringHelper;

//
//  Weather update server in Java
//  Binds PUB socket to tcp://*:5556
//  Publishes random weather updates
//
public class wuserver
{

  public static void main (final String [] args) throws Exception
  {
    // Prepare our context and publisher
    try (final ZMQ.Context context = ZMQ.context (1))
    {
      try (final ZMQ.Socket publisher = context.socket (ZMQ.PUB))
      {
        publisher.bind ("tcp://*:5556");
        publisher.bind ("ipc://weather");

        // Initialize random number generator
        final Random srandom = new Random (System.currentTimeMillis ());
        while (!Thread.currentThread ().isInterrupted ())
        {
          // Get values that will fool the boss
          final int zipcode = 10000 + srandom.nextInt (10000);
          final int temperature = srandom.nextInt (215) - 80 + 1;
          final int relhumidity = srandom.nextInt (50) + 10 + 1;

          // Send message to all subscribers
          final String update = StringHelper.getLeadingZero (zipcode, 5) + " " + temperature + " " + relhumidity;
          publisher.send (update, 0);
        }
      }
    }
  }
}
