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

import java.util.Formatter;

import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

/**
 * Majordomo Protocol Client API, Java version Implements the MDP/Worker spec at
 * http://rfc.zeromq.org/spec:7.
 */
public class mdwrkapi
{
  private static final int HEARTBEAT_LIVENESS = 3; // 3-5 is reasonable

  private final String _broker;
  private final ZContext ctx;
  private final String _service;

  private ZMQ.Socket worker; // Socket to broker
  private long heartbeatAt;// When to send HEARTBEAT
  private int liveness;// How many attempts left
  private int _heartbeat = 2500;// Heartbeat delay, msecs
  private int _reconnect = 2500; // Reconnect delay, msecs

  // Internal state
  private boolean expectReply = false; // false only at start

  private final long timeout = 2500;
  private final boolean _verbose;// Print activity to stdout
  private final Formatter log = new Formatter (System.out);

  // Return address, if any
  private ZFrame replyTo;

  public mdwrkapi (final String broker, final String service, final boolean verbose)
  {
    assert (broker != null);
    assert (service != null);
    this._broker = broker;
    this._service = service;
    this._verbose = verbose;
    ctx = new ZContext ();
    reconnectToBroker ();
  }

  /**
   * Send message to broker If no msg is provided, creates one internally
   *
   * @param command
   * @param option
   * @param aMsg
   */
  void sendToBroker (final MDP command, final String option, final ZMsg aMsg)
  {
    final ZMsg msg = aMsg != null ? aMsg.duplicate () : new ZMsg ();

    // Stack protocol envelope to start of message
    if (option != null)
      msg.addFirst (new ZFrame (option));

    msg.addFirst (command.newFrame ());
    msg.addFirst (MDP.W_WORKER.newFrame ());
    msg.addFirst (new ZFrame (ZMQ.MESSAGE_SEPARATOR));

    if (_verbose)
    {
      log.format ("I: sending %s to broker\n", command);
      msg.dump (log.out ());
    }
    msg.send (worker);
  }

  /**
   * Connect or reconnect to broker
   */
  void reconnectToBroker ()
  {
    if (worker != null)
    {
      ctx.destroySocket (worker);
    }
    worker = ctx.createSocket (ZMQ.DEALER);
    worker.connect (_broker);
    if (_verbose)
      log.format ("I: connecting to broker at %s\n", _broker);

    // Register service with broker
    sendToBroker (MDP.W_READY, _service, null);

    // If liveness hits zero, queue is considered disconnected
    liveness = HEARTBEAT_LIVENESS;
    heartbeatAt = System.currentTimeMillis () + _heartbeat;

  }

  /**
   * Send reply, if any, to broker and wait for next request.
   *
   * @param reply
   *        msg or null
   * @return null
   */
  public ZMsg receive (final ZMsg reply)
  {
    // Format and send the reply if we were provided one
    assert (reply != null || !expectReply);

    if (reply != null)
    {
      assert (replyTo != null);
      reply.wrap (replyTo);
      sendToBroker (MDP.W_REPLY, null, reply);
      reply.destroy ();
    }
    expectReply = true;

    while (!Thread.currentThread ().isInterrupted ())
    {
      // Poll socket for a reply, with timeout
      final ZMQ.Poller items = new ZMQ.Poller (1);
      items.register (worker, ZMQ.Poller.POLLIN);
      if (items.poll (timeout) == -1)
        break; // Interrupted

      if (items.pollin (0))
      {
        final ZMsg msg = ZMsg.recvMsg (worker);
        if (msg == null)
          break; // Interrupted
        if (_verbose)
        {
          log.format ("I: received message from broker: \n");
          msg.dump (log.out ());
        }
        liveness = HEARTBEAT_LIVENESS;
        // Don't try to handle errors, just assert noisily
        assert (msg != null && msg.size () >= 3);

        final ZFrame empty = msg.pop ();
        assert (empty.getData ().length == 0);
        empty.destroy ();

        final ZFrame header = msg.pop ();
        assert (MDP.W_WORKER.frameEquals (header));
        header.destroy ();

        final ZFrame command = msg.pop ();
        if (MDP.W_REQUEST.frameEquals (command))
        {
          // We should pop and save as many addresses as there are
          // up to a null part, but for now, just save one
          replyTo = msg.unwrap ();
          command.destroy ();
          return msg; // We have a request to process
        }
        else
          if (MDP.W_HEARTBEAT.frameEquals (command))
          {
            // Do nothing for heartbeats
          }
          else
            if (MDP.W_DISCONNECT.frameEquals (command))
            {
              reconnectToBroker ();
            }
            else
            {
              log.format ("E: invalid input message: \n");
              msg.dump (log.out ());
            }
        command.destroy ();
        msg.destroy ();
      }
      else
        if (--liveness == 0)
        {
          if (_verbose)
            log.format ("W: disconnected from broker - retrying\n");
          try
          {
            Thread.sleep (_reconnect);
          }
          catch (final InterruptedException e)
          {
            Thread.currentThread ().interrupt (); // Restore the
                                                  // interrupted status
            break;
          }
          reconnectToBroker ();

        }
      // Send HEARTBEAT if it's time
      if (System.currentTimeMillis () > heartbeatAt)
      {
        sendToBroker (MDP.W_HEARTBEAT, null, null);
        heartbeatAt = System.currentTimeMillis () + _heartbeat;
      }

    }
    if (Thread.currentThread ().isInterrupted ())
      log.format ("W: interrupt received, killing worker\n");
    return null;
  }

  public void destroy ()
  {
    ctx.destroy ();
  }

  // ============== getters and setters =================
  public int getHeartbeat ()
  {
    return _heartbeat;
  }

  public void setHeartbeat (final int heartbeat)
  {
    this._heartbeat = heartbeat;
  }

  public int getReconnect ()
  {
    return _reconnect;
  }

  public void setReconnect (final int reconnect)
  {
    this._reconnect = reconnect;
  }

}
