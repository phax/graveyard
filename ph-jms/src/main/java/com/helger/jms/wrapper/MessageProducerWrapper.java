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
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.string.ToStringGenerator;

/**
 * Wrapped class for a JMS {@link MessageProducer}.
 *
 * @author Philip Helger
 */
public class MessageProducerWrapper extends AbstractWrappedJMS implements MessageProducer
{
  private final MessageProducer m_aWrapped;

  public MessageProducerWrapper (@Nonnull final JMSWrapper aWrapper, @Nonnull final MessageProducer aWrapped)
  {
    super (aWrapper);
    ValueEnforcer.notNull (aWrapped, "Wrapped");
    m_aWrapped = aWrapped;
  }

  /**
   * @return The wrapped object. Never <code>null</code>.
   */
  @Nonnull
  protected MessageProducer getWrapped ()
  {
    return m_aWrapped;
  }

  public void setDisableMessageID (final boolean value) throws JMSException
  {
    m_aWrapped.setDisableMessageID (value);
  }

  public boolean getDisableMessageID () throws JMSException
  {
    return m_aWrapped.getDisableMessageID ();
  }

  public void setDisableMessageTimestamp (final boolean value) throws JMSException
  {
    m_aWrapped.setDisableMessageTimestamp (value);
  }

  public boolean getDisableMessageTimestamp () throws JMSException
  {
    return m_aWrapped.getDisableMessageTimestamp ();
  }

  public void setDeliveryMode (final int deliveryMode) throws JMSException
  {
    m_aWrapped.setDeliveryMode (deliveryMode);
  }

  public int getDeliveryMode () throws JMSException
  {
    return m_aWrapped.getDeliveryMode ();
  }

  public void setPriority (final int defaultPriority) throws JMSException
  {
    m_aWrapped.setPriority (defaultPriority);
  }

  public int getPriority () throws JMSException
  {
    return m_aWrapped.getPriority ();
  }

  public void setTimeToLive (final long timeToLive) throws JMSException
  {
    m_aWrapped.setTimeToLive (timeToLive);
  }

  public long getTimeToLive () throws JMSException
  {
    return m_aWrapped.getTimeToLive ();
  }

  public Destination getDestination () throws JMSException
  {
    return m_aWrapped.getDestination ();
  }

  public void close () throws JMSException
  {
    m_aWrapped.close ();
  }

  public void send (final Message message) throws JMSException
  {
    m_aWrapped.send (message);
  }

  public void send (final Message message, final int deliveryMode, final int priority, final long timeToLive) throws JMSException
  {
    m_aWrapped.send (message, deliveryMode, priority, timeToLive);
  }

  public void send (final Destination destination, final Message message) throws JMSException
  {
    m_aWrapped.send (destination, message);
  }

  public void send (final Destination destination,
                    final Message message,
                    final int deliveryMode,
                    final int priority,
                    final long timeToLive) throws JMSException
  {
    m_aWrapped.send (destination, message, deliveryMode, priority, timeToLive);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("wrapped", m_aWrapped).toString ();
  }
}
