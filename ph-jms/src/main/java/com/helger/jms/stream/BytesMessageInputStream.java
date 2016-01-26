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

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MessageEOFException;

import com.helger.commons.ValueEnforcer;

/**
 * {@link InputStream} wrapper for a JMS {@link BytesMessage}.
 *
 * @author Philip Helger
 */
public class BytesMessageInputStream extends InputStream
{
  private final BytesMessage m_aMessage;

  public BytesMessageInputStream (@Nonnull final BytesMessage aMessage)
  {
    ValueEnforcer.notNull (aMessage, "Message");
    m_aMessage = aMessage;
  }

  @Override
  public int read (@Nonnull final byte [] aBuf) throws JMSIOException
  {
    try
    {
      return m_aMessage.readBytes (aBuf);
    }
    catch (final JMSException ex)
    {
      throw new JMSIOException (ex);
    }
  }

  @Override
  public int read (@Nonnull final byte [] aBuf,
                   @Nonnegative final int nOfs,
                   @Nonnegative final int nLen) throws IOException
  {
    if (nOfs == 0)
    {
      try
      {
        return m_aMessage.readBytes (aBuf, nLen);
      }
      catch (final JMSException ex)
      {
        throw new JMSIOException (ex);
      }
    }
    return super.read (aBuf, nOfs, nLen);
  }

  @Override
  public int read () throws JMSIOException
  {
    try
    {
      return m_aMessage.readByte ();
    }
    catch (final MessageEOFException ex)
    {
      // -1 == EOF
      return -1;
    }
    catch (final JMSException ex)
    {
      throw new JMSIOException (ex);
    }
  }
}
