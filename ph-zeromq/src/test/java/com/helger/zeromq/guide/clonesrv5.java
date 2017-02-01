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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.zeromq.ZContext;
import org.zeromq.ZLoop;
import org.zeromq.ZLoop.IZLoopHandler;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.PollItem;
import org.zeromq.ZMQ.Socket;

//  Clone server - Model Five
public class clonesrv5
{
  private final ZContext ctx; // Context wrapper
  private final Map <String, kvmsg> kvmap; // Key-value store
  private final ZLoop loop; // zloop reactor
  private final int port; // Main port we're working on
  private long sequence; // How many updates we're at
  private final Socket snapshot; // Handle snapshot requests
  private final Socket publisher; // Publish updates to clients
  private final Socket collector; // Collect updates from clients

  // .split snapshot handler
  // This is the reactor handler for the snapshot socket; it accepts
  // just the ICANHAZ? request and replies with a state snapshot ending
  // with a KTHXBAI message:
  private static class Snapshots implements IZLoopHandler
  {
    @Override
    public int handle (final ZLoop loop, final PollItem item, final Object arg)
    {
      final clonesrv5 srv = (clonesrv5) arg;
      final Socket socket = item.getSocket ();

      final byte [] identity = socket.recv ();
      if (identity != null)
      {
        // Request is in second frame of message
        final String request = socket.recvStr ();
        String subtree = null;
        if (request.equals ("ICANHAZ?"))
        {
          subtree = socket.recvStr ();
        }
        else
          System.out.printf ("E: bad request, aborting\n");

        if (subtree != null)
        {
          // Send state socket to client
          for (final Entry <String, kvmsg> entry : srv.kvmap.entrySet ())
          {
            sendSingle (entry.getValue (), identity, subtree, socket);
          }

          // Now send END message with getSequence number
          System.out.println ("I: sending shapshot=" + srv.sequence);
          socket.send (identity, ZMQ.SNDMORE);
          final kvmsg kvmsg = new kvmsg (srv.sequence);
          kvmsg.setKey ("KTHXBAI");
          kvmsg.setBody (subtree.getBytes (ZMQ.CHARSET));
          kvmsg.send (socket);
          kvmsg.destroy ();
        }
      }
      return 0;
    }
  }

  // .split collect updates
  // We store each update with a new getSequence number, and if necessary, a
  // time-to-live. We publish updates immediately on our publisher socket:
  private static class Collector implements IZLoopHandler
  {
    @Override
    public int handle (final ZLoop loop, final PollItem item, final Object arg)
    {
      final clonesrv5 srv = (clonesrv5) arg;
      final Socket socket = item.getSocket ();

      final kvmsg msg = kvmsg.recv (socket);
      if (msg != null)
      {
        msg.setSequence (++srv.sequence);
        msg.send (srv.publisher);
        final int ttl = Integer.parseInt (msg.getProp ("ttl"));
        if (ttl > 0)
          msg.setProp ("ttl", Long.toString (System.currentTimeMillis () + ttl * 1000));
        msg.store (srv.kvmap);
        System.out.println ("I: publishing update=" + srv.sequence);
      }

      return 0;
    }
  }

  private static class FlushTTL implements IZLoopHandler
  {
    @Override
    public int handle (final ZLoop loop, final PollItem item, final Object arg)
    {
      final clonesrv5 srv = (clonesrv5) arg;
      if (srv.kvmap != null)
      {
        for (final kvmsg msg : new ArrayList<> (srv.kvmap.values ()))
        {
          srv.flushSingle (msg);
        }
      }
      return 0;
    }
  }

  public clonesrv5 ()
  {
    port = 5556;
    ctx = new ZContext ();
    kvmap = new HashMap<> ();
    loop = new ZLoop ();
    loop.verbose (false);

    // Set up our clone server sockets
    snapshot = ctx.createSocket (ZMQ.ROUTER);
    snapshot.bind ("tcp://*:" + port);
    publisher = ctx.createSocket (ZMQ.PUB);
    publisher.bind ("tcp://*:" + (port + 1));
    collector = ctx.createSocket (ZMQ.PULL);
    collector.bind ("tcp://*:" + (port + 2));
  }

  public void run ()
  {
    // Register our handlers with reactor
    PollItem poller = new PollItem (snapshot, ZMQ.Poller.POLLIN);
    loop.addPoller (poller, new Snapshots (), this);
    poller = new PollItem (collector, ZMQ.Poller.POLLIN);
    loop.addPoller (poller, new Collector (), this);
    loop.addTimer (1000, 0, new FlushTTL (), this);

    loop.start ();
    loop.destroy ();
    ctx.destroy ();
  }

  // We call this function for each getKey-value pair in our hash table
  private static void sendSingle (final kvmsg msg, final byte [] identity, final String subtree, final Socket socket)
  {
    if (msg.getKey ().startsWith (subtree))
    {
      socket.send (identity, // Choose recipient
                   ZMQ.SNDMORE);
      msg.send (socket);
    }
  }

  // .split flush ephemeral values
  // At regular intervals, we flush ephemeral values that have expired. This
  // could be slow on very large data sets:

  // If getKey-value pair has expired, delete it and publish the
  // fact to listening clients.
  private void flushSingle (final kvmsg msg)
  {
    final long ttl = Long.parseLong (msg.getProp ("ttl"));
    if (ttl > 0 && System.currentTimeMillis () >= ttl)
    {
      msg.setSequence (++sequence);
      msg.setBody (ZMQ.MESSAGE_SEPARATOR);
      msg.send (publisher);
      msg.store (kvmap);
      System.out.println ("I: publishing delete=" + sequence);
    }
  }

  public static void main (final String [] args)
  {
    final clonesrv5 srv = new clonesrv5 ();
    srv.run ();
  }
}
