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
import java.util.Arrays;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

/**
 * A simple getKey value message class
 *
 * @author Danish Shrestha <dshrestha06@gmail.com>
 */
public class kvsimple
{
  private final String _key;
  private long _sequence;
  private final byte [] _body;

  public kvsimple (final String key, final long sequence, final byte [] body)
  {
    this._key = key;
    this._sequence = sequence;
    this._body = body; // clone if needed
  }

  public String getKey ()
  {
    return _key;
  }

  public long getSequence ()
  {
    return _sequence;
  }

  public void setSequence (final long sequence)
  {
    this._sequence = sequence;
  }

  public byte [] getBody ()
  {
    return _body;
  }

  public void send (final Socket publisher)
  {
    publisher.send (_key.getBytes (ZMQ.CHARSET), ZMQ.SNDMORE);

    final ByteBuffer bb = ByteBuffer.allocate (8);
    bb.asLongBuffer ().put (_sequence);
    publisher.send (bb.array (), ZMQ.SNDMORE);

    publisher.send (_body, 0);
  }

  public static kvsimple recv (final Socket updates)
  {
    byte [] data = updates.recv (0);
    if (data == null || !updates.hasReceiveMore ())
      return null;
    final String key = new String (data, ZMQ.CHARSET);
    data = updates.recv (0);
    if (data == null || !updates.hasReceiveMore ())
      return null;
    final long sequence = ByteBuffer.wrap (data).getLong ();
    final byte [] body = updates.recv (0);
    if (body == null || updates.hasReceiveMore ())
      return null;

    return new kvsimple (key, sequence, body);
  }

  @Override
  public String toString ()
  {
    return "kvsimple [getKey=" + _key + ", getSequence=" + _sequence + ", body=" + Arrays.toString (_body) + "]";
  }

  @Override
  public int hashCode ()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode (_body);
    result = prime * result + ((_key == null) ? 0 : _key.hashCode ());
    result = prime * result + (int) (_sequence ^ (_sequence >>> 32));
    return result;
  }

  @Override
  public boolean equals (final Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass () != obj.getClass ())
      return false;
    final kvsimple other = (kvsimple) obj;
    if (!Arrays.equals (_body, other._body))
      return false;
    if (_key == null)
    {
      if (other._key != null)
        return false;
    }
    else
      if (!_key.equals (other._key))
        return false;
    if (_sequence != other._sequence)
      return false;
    return true;
  }

}
