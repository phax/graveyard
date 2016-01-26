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
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.string.ToStringGenerator;

/**
 * Wrapped class for a JMS {@link MessageConsumer}.
 *
 * @author Philip Helger
 */
public class MessageConsumerWrapper extends AbstractWrappedJMS implements MessageConsumer
{
  private final MessageConsumer m_aWrapped;

  public MessageConsumerWrapper (@Nonnull final JMSWrapper aWrapper, @Nonnull final MessageConsumer aWrapped)
  {
    super (aWrapper);
    ValueEnforcer.notNull (aWrapped, "Wrapped");
    m_aWrapped = aWrapped;
  }

  /**
   * @return The wrapped object. Never <code>null</code>.
   */
  @Nonnull
  protected MessageConsumer getWrapped ()
  {
    return m_aWrapped;
  }

  public String getMessageSelector () throws JMSException
  {
    return m_aWrapped.getMessageSelector ();
  }

  public MessageListener getMessageListener () throws JMSException
  {
    return m_aWrapped.getMessageListener ();
  }

  public void setMessageListener (final MessageListener listener) throws JMSException
  {
    m_aWrapped.setMessageListener (listener);
  }

  public Message receive () throws JMSException
  {
    return m_aWrapped.receive ();
  }

  public Message receive (final long timeout) throws JMSException
  {
    return m_aWrapped.receive (timeout);
  }

  public Message receiveNoWait () throws JMSException
  {
    return m_aWrapped.receiveNoWait ();
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
