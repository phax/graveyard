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

import java.util.Enumeration;

import javax.annotation.Nonnull;
import javax.jms.JMSException;
import javax.jms.MapMessage;

/**
 * Wrapped class for a JMS {@link MapMessage}.
 * 
 * @author Philip Helger
 */
public class MapMessageWrapper extends MessageWrapper implements MapMessage
{
  public MapMessageWrapper (@Nonnull final JMSWrapper aWrapper, @Nonnull final MapMessage aWrapped)
  {
    super (aWrapper, aWrapped);
  }

  /**
   * @return The wrapped object. Never <code>null</code>.
   */
  @Override
  @Nonnull
  protected MapMessage getWrapped ()
  {
    return (MapMessage) super.getWrapped ();
  }

  public boolean getBoolean (final String name) throws JMSException
  {
    return getWrapped ().getBoolean (name);
  }

  public byte getByte (final String name) throws JMSException
  {
    return getWrapped ().getByte (name);
  }

  public short getShort (final String name) throws JMSException
  {
    return getWrapped ().getShort (name);
  }

  public char getChar (final String name) throws JMSException
  {
    return getWrapped ().getChar (name);
  }

  public int getInt (final String name) throws JMSException
  {
    return getWrapped ().getInt (name);
  }

  public long getLong (final String name) throws JMSException
  {
    return getWrapped ().getLong (name);
  }

  public float getFloat (final String name) throws JMSException
  {
    return getWrapped ().getFloat (name);
  }

  public double getDouble (final String name) throws JMSException
  {
    return getWrapped ().getDouble (name);
  }

  public String getString (final String name) throws JMSException
  {
    return getWrapped ().getString (name);
  }

  public byte [] getBytes (final String name) throws JMSException
  {
    return getWrapped ().getBytes (name);
  }

  public Object getObject (final String name) throws JMSException
  {
    return getWrapped ().getObject (name);
  }

  public Enumeration <?> getMapNames () throws JMSException
  {
    return getWrapped ().getMapNames ();
  }

  public void setBoolean (final String name, final boolean value) throws JMSException
  {
    getWrapped ().setBoolean (name, value);
  }

  public void setByte (final String name, final byte value) throws JMSException
  {
    getWrapped ().setByte (name, value);
  }

  public void setShort (final String name, final short value) throws JMSException
  {
    getWrapped ().setShort (name, value);
  }

  public void setChar (final String name, final char value) throws JMSException
  {
    getWrapped ().setChar (name, value);
  }

  public void setInt (final String name, final int value) throws JMSException
  {
    getWrapped ().setInt (name, value);
  }

  public void setLong (final String name, final long value) throws JMSException
  {
    getWrapped ().setLong (name, value);
  }

  public void setFloat (final String name, final float value) throws JMSException
  {
    getWrapped ().setFloat (name, value);
  }

  public void setDouble (final String name, final double value) throws JMSException
  {
    getWrapped ().setDouble (name, value);
  }

  public void setString (final String name, final String value) throws JMSException
  {
    getWrapped ().setString (name, value);
  }

  public void setBytes (final String name, final byte [] value) throws JMSException
  {
    getWrapped ().setBytes (name, value);
  }

  public void setBytes (final String name, final byte [] value, final int offset, final int length) throws JMSException
  {
    getWrapped ().setBytes (name, value, offset, length);
  }

  public void setObject (final String name, final Object value) throws JMSException
  {
    getWrapped ().setObject (name, value);
  }

  public boolean itemExists (final String name) throws JMSException
  {
    return getWrapped ().itemExists (name);
  }
}
