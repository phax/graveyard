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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;

/**
 * Clone client Model Four
 */
public class clonecli4
{
  // This client is identical to clonecli3 except for where we
  // handles subtrees.
  private final static String SUBTREE = "/client/";

  private static Map <String, kvsimple> kvMap = new HashMap<> ();

  public void run ()
  {
    try (final ZContext ctx = new ZContext ())
    {
      final Socket snapshot = ctx.createSocket (ZMQ.DEALER);
      snapshot.connect ("tcp://localhost:5556");

      final Socket subscriber = ctx.createSocket (ZMQ.SUB);
      subscriber.connect ("tcp://localhost:5557");
      subscriber.subscribe (SUBTREE.getBytes (ZMQ.CHARSET));

      final Socket push = ctx.createSocket (ZMQ.PUSH);
      push.connect ("tcp://localhost:5558");

      // get state snapshot
      snapshot.sendMore ("ICANHAZ?");
      snapshot.send (SUBTREE);
      long sequence = 0;

      while (true)
      {
        final kvsimple kvMsg = kvsimple.recv (snapshot);
        if (kvMsg == null)
          break; // Interrupted

        sequence = kvMsg.getSequence ();
        if ("KTHXBAI".equalsIgnoreCase (kvMsg.getKey ()))
        {
          System.out.println ("Received snapshot = " + kvMsg.getSequence ());
          break; // done
        }

        System.out.println ("receiving " + kvMsg.getSequence ());
        clonecli4.kvMap.put (kvMsg.getKey (), kvMsg);
      }

      final Poller poller = new Poller (1);
      poller.register (subscriber);

      final Random random = new Random ();

      // now apply pending updates, discard out-of-getSequence messages
      long alarm = System.currentTimeMillis () + 5000;
      while (true)
      {
        final int rc = poller.poll (Math.max (0, alarm - System.currentTimeMillis ()));
        if (rc == -1)
          break; // Context has been shut down

        if (poller.pollin (0))
        {
          final kvsimple kvMsg = kvsimple.recv (subscriber);
          if (kvMsg == null)
            break; // Interrupted
          if (kvMsg.getSequence () > sequence)
          {
            sequence = kvMsg.getSequence ();
            System.out.println ("receiving " + sequence);
            clonecli4.kvMap.put (kvMsg.getKey (), kvMsg);
          }
        }

        if (System.currentTimeMillis () >= alarm)
        {
          final String key = SUBTREE + random.nextInt (10000);
          final int body = random.nextInt (1000000);

          final ByteBuffer b = ByteBuffer.allocate (4);
          b.asIntBuffer ().put (body);

          final kvsimple kvUpdateMsg = new kvsimple (key, 0, b.array ());
          kvUpdateMsg.send (push);
          alarm = System.currentTimeMillis () + 1000;
        }
      }
    }
  }

  public static void main (final String [] args)
  {
    new clonecli4 ().run ();
  }
}
