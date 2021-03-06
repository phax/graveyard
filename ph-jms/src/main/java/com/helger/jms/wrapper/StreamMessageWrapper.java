/**
 * Copyright (C) 2014-2015 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.jms.wrapper;

import javax.annotation.Nonnull;
import javax.jms.JMSException;
import javax.jms.StreamMessage;

/**
 * Wrapped class for a JMS {@link StreamMessage}.
 * 
 * @author Philip Helger
 */
public class StreamMessageWrapper extends MessageWrapper implements StreamMessage
{
  public StreamMessageWrapper (@Nonnull final JMSWrapper aWrapper, @Nonnull final StreamMessage aWrapped)
  {
    super (aWrapper, aWrapped);
  }

  /**
   * @return The wrapped object. Never <code>null</code>.
   */
  @Override
  @Nonnull
  protected StreamMessage getWrapped ()
  {
    return (StreamMessage) super.getWrapped ();
  }

  public boolean readBoolean () throws JMSException
  {
    return getWrapped ().readBoolean ();
  }

  public byte readByte () throws JMSException
  {
    return getWrapped ().readByte ();
  }

  public short readShort () throws JMSException
  {
    return getWrapped ().readShort ();
  }

  public char readChar () throws JMSException
  {
    return getWrapped ().readChar ();
  }

  public int readInt () throws JMSException
  {
    return getWrapped ().readInt ();
  }

  public long readLong () throws JMSException
  {
    return getWrapped ().readLong ();
  }

  public float readFloat () throws JMSException
  {
    return getWrapped ().readFloat ();
  }

  public double readDouble () throws JMSException
  {
    return getWrapped ().readDouble ();
  }

  public String readString () throws JMSException
  {
    return getWrapped ().readString ();
  }

  public int readBytes (final byte [] value) throws JMSException
  {
    return getWrapped ().readBytes (value);
  }

  public Object readObject () throws JMSException
  {
    return getWrapped ().readObject ();
  }

  public void writeBoolean (final boolean value) throws JMSException
  {
    getWrapped ().writeBoolean (value);
  }

  public void writeByte (final byte value) throws JMSException
  {
    getWrapped ().writeByte (value);
  }

  public void writeShort (final short value) throws JMSException
  {
    getWrapped ().writeShort (value);
  }

  public void writeChar (final char value) throws JMSException
  {
    getWrapped ().writeChar (value);
  }

  public void writeInt (final int value) throws JMSException
  {
    getWrapped ().writeInt (value);
  }

  public void writeLong (final long value) throws JMSException
  {
    getWrapped ().writeLong (value);
  }

  public void writeFloat (final float value) throws JMSException
  {
    getWrapped ().writeFloat (value);
  }

  public void writeDouble (final double value) throws JMSException
  {
    getWrapped ().writeDouble (value);
  }

  public void writeString (final String value) throws JMSException
  {
    getWrapped ().writeString (value);
  }

  public void writeBytes (final byte [] value) throws JMSException
  {
    getWrapped ().writeBytes (value);
  }

  public void writeBytes (final byte [] value, final int offset, final int length) throws JMSException
  {
    getWrapped ().writeBytes (value, offset, length);
  }

  public void writeObject (final Object value) throws JMSException
  {
    getWrapped ().writeObject (value);
  }

  public void reset () throws JMSException
  {
    getWrapped ().reset ();
  }
}
