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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;

import com.helger.commons.string.StringHelper;

/**
 * Clone server Model Three
 *
 * @author Danish Shrestha <dshrestha06@gmail.com>
 */
public class clonesrv3
{
  private static Map <String, kvsimple> kvMap = new LinkedHashMap<> ();

  public void run ()
  {
    try (final ZContext ctx = new ZContext ())
    {

      final Socket snapshot = ctx.createSocket (ZMQ.ROUTER);
      snapshot.bind ("tcp://*:5556");

      final Socket publisher = ctx.createSocket (ZMQ.PUB);
      publisher.bind ("tcp://*:5557");

      final Socket collector = ctx.createSocket (ZMQ.PULL);
      collector.bind ("tcp://*:5558");

      final Poller poller = new Poller (2);
      poller.register (collector, Poller.POLLIN);
      poller.register (snapshot, Poller.POLLIN);

      long sequence = 0;
      while (!Thread.currentThread ().isInterrupted ())
      {
        if (poller.poll (1000) < 0)
          break; // Context has been shut down

        // apply state updates from main thread
        if (poller.pollin (0))
        {
          final kvsimple kvMsg = kvsimple.recv (collector);
          if (kvMsg == null) // Interrupted
            break;
          kvMsg.setSequence (++sequence);
          kvMsg.send (publisher);
          clonesrv3.kvMap.put (kvMsg.getKey (), kvMsg);
          System.out.println ("I: publishing update " + StringHelper.getWithLeading (Long.toString (sequence), 5, ' '));
        }

        // execute state snapshot request
        if (poller.pollin (1))
        {
          final byte [] identity = snapshot.recv (0);
          if (identity == null)
            break; // Interrupted
          final String request = snapshot.recvStr ();

          if (!request.equals ("ICANHAZ?"))
          {
            System.out.println ("E: bad request, aborting");
            break;
          }

          final Iterator <Entry <String, kvsimple>> iter = kvMap.entrySet ().iterator ();
          while (iter.hasNext ())
          {
            final Entry <String, kvsimple> entry = iter.next ();
            final kvsimple msg = entry.getValue ();
            System.out.println ("Sending message " + entry.getValue ().getSequence ());
            this.sendMessage (msg, identity, snapshot);
          }

          // now send end message with getSequence number
          System.out.println ("Sending state snapshot = " + sequence);
          snapshot.send (identity, ZMQ.SNDMORE);
          final kvsimple message = new kvsimple ("KTHXBAI", sequence, ZMQ.SUBSCRIPTION_ALL);
          message.send (snapshot);
        }
      }
      System.out.println (" Interrupted\n" + sequence + " messages handled");
    }
  }

  private void sendMessage (final kvsimple msg, final byte [] identity, final Socket snapshot)
  {
    snapshot.send (identity, ZMQ.SNDMORE);
    msg.send (snapshot);
  }

  public static void main (final String [] args)
  {
    new clonesrv3 ().run ();
  }
}
