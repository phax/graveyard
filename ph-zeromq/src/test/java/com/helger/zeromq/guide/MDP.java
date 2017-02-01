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

import java.util.Arrays;

import org.zeromq.ZFrame;
import org.zeromq.ZMQ;

/**
 * Majordomo Protocol definitions, Java version
 */
public enum MDP
{
  /**
   * This is the version of MDP/Client we implement
   */
  C_CLIENT ("MDPC01"),

  /**
   * This is the version of MDP/Worker we implement
   */
  W_WORKER ("MDPW01"),

  // MDP/Server commands, as byte values
  W_READY (1),
  W_REQUEST (2),
  W_REPLY (3),
  W_HEARTBEAT (4),
  W_DISCONNECT (5);

  private final byte [] data;

  MDP (final String value)
  {
    this.data = value.getBytes (ZMQ.CHARSET);
  }

  MDP (final int value)
  { // watch for ints>255, will be truncated
    final byte b = (byte) (value & 0xFF);
    this.data = new byte [] { b };
  }

  public String getData ()
  {
    return new String (data, ZMQ.CHARSET);
  }

  public ZFrame newFrame ()
  {
    return new ZFrame (data);
  }

  public boolean frameEquals (final ZFrame frame)
  {
    return Arrays.equals (data, frame.getData ());
  }
}
