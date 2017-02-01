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

import java.util.HashMap;
import java.util.Map;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.PollItem;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMsg;
import org.zeromq.ZThread;
import org.zeromq.ZThread.IAttachedRunnable;

public class clone
{
  private final ZContext ctx; // Our context wrapper
  private final Socket pipe; // Pipe through to clone agent

  // .split constructor and destructor
  // Here are the constructor and destructor for the clone class. Note that
  // we create a context specifically for the pipe that connects our
  // frontend to the backend agent:
  public clone ()
  {
    ctx = new ZContext ();
    pipe = ZThread.fork (ctx, new CloneAgent ());
  }

  public void destroy ()
  {
    ctx.destroy ();
  }

  // .split subtree method
  // Specify subtree for snapshot and updates, which we must do before
  // connecting to a server as the subtree specification is sent as the
  // first command to the server. Sends a [SUBTREE][subtree] command to
  // the agent:
  public void subtree (final String subtree)
  {
    final ZMsg msg = new ZMsg ();
    msg.add ("SUBTREE");
    msg.add (subtree);
    msg.send (pipe);
  }

  // .split connect method
  // Connect to a new server endpoint. We can connect to at most two
  // servers. Sends [CONNECT][endpoint][service] to the agent:
  public void connect (final String address, final String service)
  {
    final ZMsg msg = new ZMsg ();
    msg.add ("CONNECT");
    msg.add (address);
    msg.add (service);
    msg.send (pipe);
  }

  // .split set method
  // Set a new value in the shared hashmap. Sends a [SET][key][value][ttl]
  // command through to the agent which does the actual work:
  public void set (final String key, final String value, final int ttl)
  {
    final ZMsg msg = new ZMsg ();
    msg.add ("SET");
    msg.add (key);
    msg.add (value);
    msg.add (Integer.toString (ttl));
    msg.send (pipe);
  }

  // .split get method
  // Look up value in distributed hash table. Sends [GET][key] to the agent and
  // waits for a value response. If there is no value available, will eventually
  // return NULL:
  public String get (final String key)
  {
    final ZMsg msg = new ZMsg ();
    msg.add ("GET");
    msg.add (key);
    msg.send (pipe);

    final ZMsg reply = ZMsg.recvMsg (pipe);
    if (reply != null)
    {
      final String value = reply.popString ();
      reply.destroy ();
      return value;
    }
    return null;
  }

  // .split working with servers
  // The backend agent manages a set of servers, which we implement using
  // our simple class model:
  private static class Server
  {
    private final String _address; // Server address
    private final int _port; // Server port
    private final Socket snapshot; // Snapshot socket
    private final Socket subscriber; // Incoming updates
    private long expiry; // When server expires
    private int requests; // How many snapshot requests made?

    protected Server (final ZContext ctx, final String address, final int port, final String subtree)
    {
      System.out.println ("I: adding server " + address + ":" + port + "...");
      this._address = address;
      this._port = port;

      snapshot = ctx.createSocket (ZMQ.DEALER);
      snapshot.connect (address + ":" + port);
      subscriber = ctx.createSocket (ZMQ.SUB);
      subscriber.connect (address + ":" + (port + 1));
      subscriber.subscribe (subtree.getBytes (ZMQ.CHARSET));
    }

    protected void destroy ()
    {}
  }

  // .split backend agent class
  // Here is the implementation of the backend agent itself:

  // Number of servers to which we will talk to
  private final static int SERVER_MAX = 2;

  // Server considered dead if silent for this long
  private final static int SERVER_TTL = 5000; // msecs

  // States we can be in
  private final static int STATE_INITIAL = 0; // Before asking server for state
  private final static int STATE_SYNCING = 1; // Getting state from server
  private final static int STATE_ACTIVE = 2; // Getting new updates from server

  private static class Agent
  {
    private final ZContext _ctx; // Context wrapper
    private final Socket _pipe; // Pipe back to application
    private final Map <String, String> kvmap; // Actual key/value table
    private String subtree; // Subtree specification, if any
    private final Server [] server;
    private int nbrServers; // 0 to SERVER_MAX
    private int state; // Current state
    private int curServer; // If active, server 0 or 1
    private long sequence; // Last kvmsg processed
    private final Socket publisher; // Outgoing updates

    protected Agent (final ZContext ctx, final Socket pipe)
    {
      this._ctx = ctx;
      this._pipe = pipe;
      kvmap = new HashMap<> ();
      subtree = "";
      state = STATE_INITIAL;
      publisher = ctx.createSocket (ZMQ.PUB);

      server = new Server [SERVER_MAX];
    }

    protected void destroy ()
    {
      for (int serverNbr = 0; serverNbr < nbrServers; serverNbr++)
        server[serverNbr].destroy ();
    }

    // .split handling a control message
    // Here we handle the different control messages from the frontend;
    // SUBTREE, CONNECT, SET, and GET:
    private boolean controlMessage ()
    {
      final ZMsg msg = ZMsg.recvMsg (_pipe);
      final String command = msg.popString ();
      if (command == null)
        return false; // Interrupted

      if (command.equals ("SUBTREE"))
      {
        subtree = msg.popString ();
      }
      else
        if (command.equals ("CONNECT"))
        {
          final String address = msg.popString ();
          final String service = msg.popString ();
          if (nbrServers < SERVER_MAX)
          {
            server[nbrServers++] = new Server (_ctx, address, Integer.parseInt (service), subtree);
            // We broadcast updates to all known servers
            publisher.connect (address + ":" + (Integer.parseInt (service) + 2));
          }
          else
            System.out.println ("E: too many servers (max. " + SERVER_MAX + ")");
        }
        else
          // .split set and get commands
          // When we set a property, we push the new key-value pair onto
          // all our connected servers:
          if (command.equals ("SET"))
          {
            final String key = msg.popString ();
            final String value = msg.popString ();
            final String ttl = msg.popString ();
            kvmap.put (key, value);

            // Send key-value pair on to server
            final kvmsg kvmsg = new kvmsg (0);
            kvmsg.setKey (key);
            kvmsg.setUUID ();
            kvmsg.setBody (value);
            kvmsg.setProp ("ttl", ttl);
            kvmsg.send (publisher);
            kvmsg.destroy ();
          }
          else
            if (command.equals ("GET"))
            {
              final String key = msg.popString ();
              final String value = kvmap.get (key);
              if (value != null)
                _pipe.send (value);
              else
                _pipe.send ("");
            }
      msg.destroy ();

      return true;
    }
  }

  private static class CloneAgent implements IAttachedRunnable
  {

    @Override
    public void run (final Object [] args, final ZContext ctx, final Socket pipe)
    {
      final Agent self = new Agent (ctx, pipe);

      while (!Thread.currentThread ().isInterrupted ())
      {
        final PollItem [] pollItems = { new PollItem (pipe, Poller.POLLIN), null };
        long pollTimer = -1;
        int pollSize = 2;
        final Server server = self.server[self.curServer];
        switch (self.state)
        {
          case STATE_INITIAL:
            // In this state we ask the server for a snapshot,
            // if we have a server to talk to...
            if (self.nbrServers > 0)
            {
              System.out.println ("I: waiting for server at " + server._address + ":" + server._port + "...");
              if (server.requests < 2)
              {
                server.snapshot.sendMore ("ICANHAZ?");
                server.snapshot.send (self.subtree);
                server.requests++;
              }
              server.expiry = System.currentTimeMillis () + SERVER_TTL;
              self.state = STATE_SYNCING;
              pollItems[1] = new PollItem (server.snapshot, Poller.POLLIN);
            }
            else
              pollSize = 1;
            break;

          case STATE_SYNCING:
            // In this state we read from snapshot and we expect
            // the server to respond, else we fail over.
            pollItems[1] = new PollItem (server.snapshot, Poller.POLLIN);
            break;

          case STATE_ACTIVE:
            // In this state we read from subscriber and we expect
            // the server to give hugz, else we fail over.
            pollItems[1] = new PollItem (server.subscriber, Poller.POLLIN);
            break;
        }
        if (server != null)
        {
          pollTimer = server.expiry - System.currentTimeMillis ();
          if (pollTimer < 0)
            pollTimer = 0;
        }
        // .split client poll loop
        // We're ready to process incoming messages; if nothing at all
        // comes from our server within the timeout, that means the
        // server is dead:
        final int rc = ZMQ.poll (pollItems, pollSize, pollTimer);
        if (rc == -1)
          break; // Context has been shut down

        if (pollItems[0].isReadable ())
        {
          if (!self.controlMessage ())
            break; // Interrupted
        }
        else
          if (pollItems[1].isReadable ())
          {
            final kvmsg msg = kvmsg.recv (pollItems[1].getSocket ());
            if (msg == null)
              break; // Interrupted

            // Anything from server resets its expiry time
            server.expiry = System.currentTimeMillis () + SERVER_TTL;
            if (self.state == STATE_SYNCING)
            {
              // Store in snapshot until we're finished
              server.requests = 0;
              if (msg.getKey ().equals ("KTHXBAI"))
              {
                self.sequence = msg.getSequence ();
                self.state = STATE_ACTIVE;
                System.out.println ("I: received from " +
                                    server._address +
                                    ":" +
                                    server._port +
                                    " snapshot=" +
                                    self.sequence);
                msg.destroy ();
              }
            }
            else
              if (self.state == STATE_ACTIVE)
              {
                // Discard out-of-sequence updates, incl. hugz
                if (msg.getSequence () > self.sequence)
                {
                  self.sequence = msg.getSequence ();
                  System.out.println ("I: received from " +
                                      server._address +
                                      ":" +
                                      server._port +
                                      " update=" +
                                      self.sequence);
                }
                else
                  msg.destroy ();
              }
          }
          else
          {
            // Server has died, failover to next
            System.out.println ("I: server at " + server._address + ":" + server._port + " didn't give hugz");
            self.curServer = (self.curServer + 1) % self.nbrServers;
            self.state = STATE_INITIAL;
          }
      }
      self.destroy ();
    }
  }

}
