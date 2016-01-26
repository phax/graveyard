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
package com.helger.jms.stream;

import java.io.OutputStream;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.jms.BytesMessage;
import javax.jms.JMSException;

import com.helger.commons.ValueEnforcer;

/**
 * {@link OutputStream} wrapper for a JMS {@link BytesMessage}.
 *
 * @author Philip Helger
 */
public class BytesMessageOutputStream extends OutputStream
{
  private final BytesMessage m_aMessage;

  public BytesMessageOutputStream (@Nonnull final BytesMessage aMessage)
  {
    ValueEnforcer.notNull (aMessage, "Message");
    m_aMessage = aMessage;
  }

  @Override
  public void write (final int nByte) throws JMSIOException
  {
    try
    {
      m_aMessage.writeByte ((byte) nByte);
    }
    catch (final JMSException ex)
    {
      throw new JMSIOException (ex);
    }
  }

  @Override
  public void write (@Nonnull final byte [] aBuf) throws JMSIOException
  {
    write (aBuf, 0, aBuf.length);
  }

  @Override
  public void write (@Nonnull final byte [] aBuf,
                     @Nonnegative final int nOfs,
                     @Nonnegative final int nLen) throws JMSIOException
  {
    try
    {
      m_aMessage.writeBytes (aBuf, nOfs, nLen);
    }
    catch (final JMSException ex)
    {
      throw new JMSIOException (ex);
    }
  }
}
