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

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

/**
 * Clone server model 1
 * 
 * @author Danish Shrestha <dshrestha06@gmail.com>
 */
public class clonesrv1
{
  private static AtomicLong sequence = new AtomicLong ();

  public void run ()
  {
    final Context ctx = ZMQ.context (1);
    final Socket publisher = ctx.socket (ZMQ.PUB);
    publisher.bind ("tcp://*:5556");

    try
    {
      Thread.sleep (200);
    }
    catch (final InterruptedException e)
    {
      e.printStackTrace ();
    }

    final Random random = new Random ();

    while (true)
    {
      final long currentSequenceNumber = sequence.incrementAndGet ();
      final int key = random.nextInt (10000);
      final int body = random.nextInt (1000000);

      final ByteBuffer b = ByteBuffer.allocate (4);
      b.asIntBuffer ().put (body);

      final kvsimple kvMsg = new kvsimple (key + "", currentSequenceNumber, b.array ());
      kvMsg.send (publisher);
      System.out.println ("sending " + kvMsg);

    }
  }

  public static void main (final String [] args)
  {
    new clonesrv1 ().run ();
  }
}
