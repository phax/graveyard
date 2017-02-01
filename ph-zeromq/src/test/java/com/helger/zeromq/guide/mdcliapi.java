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
public class mdcliapi
{

  private final String _broker;
  private final ZContext ctx;
  private ZMQ.Socket client;
  private long _timeout = 2500;
  private int _retries = 3;
  private final boolean _verbose;
  private final Formatter log = new Formatter (System.out);

  public long getTimeout ()
  {
    return _timeout;
  }

  public void setTimeout (final long timeout)
  {
    this._timeout = timeout;
  }

  public int getRetries ()
  {
    return _retries;
  }

  public void setRetries (final int retries)
  {
    this._retries = retries;
  }

  public mdcliapi (final String broker, final boolean verbose)
  {
    this._broker = broker;
    this._verbose = verbose;
    ctx = new ZContext ();
    reconnectToBroker ();
  }

  /**
   * Connect or reconnect to broker
   */
  void reconnectToBroker ()
  {
    if (client != null)
    {
      ctx.destroySocket (client);
    }
    client = ctx.createSocket (ZMQ.REQ);
    client.connect (_broker);
    if (_verbose)
      log.format ("I: connecting to broker at %s\n", _broker);
  }

  /**
   * Send request to broker and get reply by hook or crook Takes ownership of
   * request message and destroys it when sent. Returns the reply message or
   * NULL if there was no reply.
   *
   * @param service
   *        service
   * @param request
   *        request
   * @return msg
   */
  public ZMsg send (final String service, final ZMsg request)
  {
    request.push (new ZFrame (service));
    request.push (MDP.C_CLIENT.newFrame ());
    if (_verbose)
    {
      log.format ("I: send request to '%s' service: \n", service);
      request.dump (log.out ());
    }
    ZMsg reply = null;

    int retriesLeft = _retries;
    while (retriesLeft > 0 && !Thread.currentThread ().isInterrupted ())
    {

      request.duplicate ().send (client);

      // Poll socket for a reply, with timeout
      final ZMQ.Poller items = new ZMQ.Poller (1);
      items.register (client, ZMQ.Poller.POLLIN);
      if (items.poll (_timeout) == -1)
        break; // Interrupted

      if (items.pollin (0))
      {
        final ZMsg msg = ZMsg.recvMsg (client);
        if (_verbose)
        {
          log.format ("I: received reply: \n");
          msg.dump (log.out ());
        }
        // Don't try to handle errors, just assert noisily
        assert (msg.size () >= 3);

        final ZFrame header = msg.pop ();
        assert (MDP.C_CLIENT.getData ().equals (header.toString ()));
        header.destroy ();

        final ZFrame replyService = msg.pop ();
        assert (service.equals (replyService.toString ()));
        replyService.destroy ();

        reply = msg;
        break;
      }
      items.unregister (client);
      if (--retriesLeft == 0)
      {
        log.format ("W: permanent error, abandoning\n");
        break;
      }
      log.format ("W: no reply, reconnecting\n");
      reconnectToBroker ();
    }
    request.destroy ();
    return reply;
  }

  public void destroy ()
  {
    ctx.destroy ();
  }
}
