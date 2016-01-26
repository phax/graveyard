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
import javax.jms.Queue;
import javax.jms.QueueBrowser;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.string.ToStringGenerator;

/**
 * Wrapped class for a JMS {@link QueueBrowser}.
 *
 * @author Philip Helger
 */
public class QueueBrowserWrapper extends AbstractWrappedJMS implements QueueBrowser
{
  private final QueueBrowser m_aWrapped;

  public QueueBrowserWrapper (@Nonnull final JMSWrapper aWrapper, @Nonnull final QueueBrowser aWrapped)
  {
    super (aWrapper);
    ValueEnforcer.notNull (aWrapped, "Wrapped");
    m_aWrapped = aWrapped;
  }

  /**
   * @return The wrapped object. Never <code>null</code>.
   */
  @Nonnull
  protected QueueBrowser getWrapped ()
  {
    return m_aWrapped;
  }

  public Queue getQueue () throws JMSException
  {
    return m_aWrapped.getQueue ();
  }

  public String getMessageSelector () throws JMSException
  {
    return m_aWrapped.getMessageSelector ();
  }

  public Enumeration <?> getEnumeration () throws JMSException
  {
    return m_aWrapped.getEnumeration ();
  }

  public void close () throws JMSException
  {
    m_aWrapped.close ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("wrapped", m_aWrapped).toString ();
  }
}
