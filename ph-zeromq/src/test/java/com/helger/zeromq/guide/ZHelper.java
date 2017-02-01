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

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.helger.commons.string.StringHelper;

public class ZHelper
{
  private static Random rand = new Random (System.currentTimeMillis ());

  /**
   * Receives all message parts from socket, prints neatly
   *
   * @param sock
   *        socket
   */
  public static void dump (final Socket sock)
  {
    System.out.println ("----------------------------------------");
    while (true)
    {
      final byte [] msg = sock.recv (0);
      boolean isText = true;
      String data = "";
      for (final byte element : msg)
      {
        final int n = element & 0xff;
        if (n < 32 || n > 127)
          isText = false;
        data += StringHelper.getHexStringLeadingZero2 (element);
      }
      if (isText)
        data = new String (msg, ZMQ.CHARSET);

      System.out.println ("[" + StringHelper.getLeadingZero (msg.length, 3) + "] " + data);
      if (!sock.hasReceiveMore ())
        break;
    }
  }

  public static void setId (final Socket sock)
  {
    final String identity = StringHelper.getHexStringLeadingZero (rand.nextInt (), 4) +
                            "-" +
                            StringHelper.getHexStringLeadingZero (rand.nextInt (), 4);

    sock.setIdentity (identity.getBytes (ZMQ.CHARSET));
  }

  public static List <Socket> buildZPipe (final Context ctx)
  {
    final Socket socket1 = ctx.socket (ZMQ.PAIR);
    socket1.setLinger (0);
    socket1.setHWM (1);

    final Socket socket2 = ctx.socket (ZMQ.PAIR);
    socket2.setLinger (0);
    socket2.setHWM (1);

    final String iface = "inproc://" + new BigInteger (130, rand).toString (32);
    socket1.bind (iface);
    socket2.connect (iface);

    return Arrays.asList (socket1, socket2);
  }
}
