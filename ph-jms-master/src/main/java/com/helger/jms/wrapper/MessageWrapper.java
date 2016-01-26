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
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.string.ToStringGenerator;

/**
 * Wrapped class for a JMS {@link Message}.
 *
 * @author Philip Helger
 */
public class MessageWrapper extends AbstractWrappedJMS implements Message
{
  private final Message m_aWrapped;

  public MessageWrapper (@Nonnull final JMSWrapper aWrapper, @Nonnull final Message aWrapped)
  {
    super (aWrapper);
    ValueEnforcer.notNull (aWrapped, "Wrapped");
    m_aWrapped = aWrapped;
  }

  /**
   * @return The wrapped object. Never <code>null</code>.
   */
  @Nonnull
  protected Message getWrapped ()
  {
    return m_aWrapped;
  }

  public String getJMSMessageID () throws JMSException
  {
    return m_aWrapped.getJMSMessageID ();
  }

  public void setJMSMessageID (final String id) throws JMSException
  {
    m_aWrapped.setJMSMessageID (id);
  }

  public long getJMSTimestamp () throws JMSException
  {
    return m_aWrapped.getJMSTimestamp ();
  }

  public void setJMSTimestamp (final long timestamp) throws JMSException
  {
    m_aWrapped.setJMSTimestamp (timestamp);
  }

  public byte [] getJMSCorrelationIDAsBytes () throws JMSException
  {
    return m_aWrapped.getJMSCorrelationIDAsBytes ();
  }

  public void setJMSCorrelationIDAsBytes (final byte [] correlationID) throws JMSException
  {
    m_aWrapped.setJMSCorrelationIDAsBytes (correlationID);
  }

  public void setJMSCorrelationID (final String correlationID) throws JMSException
  {
    m_aWrapped.setJMSCorrelationID (correlationID);
  }

  public String getJMSCorrelationID () throws JMSException
  {
    return m_aWrapped.getJMSCorrelationID ();
  }

  public Destination getJMSReplyTo () throws JMSException
  {
    return m_aWrapped.getJMSReplyTo ();
  }

  public void setJMSReplyTo (final Destination replyTo) throws JMSException
  {
    m_aWrapped.setJMSReplyTo (replyTo);
  }

  public Destination getJMSDestination () throws JMSException
  {
    return m_aWrapped.getJMSDestination ();
  }

  public void setJMSDestination (final Destination destination) throws JMSException
  {
    m_aWrapped.setJMSDestination (destination);
  }

  public int getJMSDeliveryMode () throws JMSException
  {
    return m_aWrapped.getJMSDeliveryMode ();
  }

  public void setJMSDeliveryMode (final int deliveryMode) throws JMSException
  {
    m_aWrapped.setJMSDeliveryMode (deliveryMode);
  }

  public boolean getJMSRedelivered () throws JMSException
  {
    return m_aWrapped.getJMSRedelivered ();
  }

  public void setJMSRedelivered (final boolean redelivered) throws JMSException
  {
    m_aWrapped.setJMSRedelivered (redelivered);
  }

  public String getJMSType () throws JMSException
  {
    return m_aWrapped.getJMSType ();
  }

  public void setJMSType (final String type) throws JMSException
  {
    m_aWrapped.setJMSType (type);
  }

  public long getJMSExpiration () throws JMSException
  {
    return m_aWrapped.getJMSExpiration ();
  }

  public void setJMSExpiration (final long expiration) throws JMSException
  {
    m_aWrapped.setJMSExpiration (expiration);
  }

  public int getJMSPriority () throws JMSException
  {
    return m_aWrapped.getJMSPriority ();
  }

  public void setJMSPriority (final int priority) throws JMSException
  {
    m_aWrapped.setJMSPriority (priority);
  }

  public void clearProperties () throws JMSException
  {
    m_aWrapped.clearProperties ();
  }

  public boolean propertyExists (final String name) throws JMSException
  {
    return m_aWrapped.propertyExists (name);
  }

  public boolean getBooleanProperty (final String name) throws JMSException
  {
    return m_aWrapped.getBooleanProperty (name);
  }

  public byte getByteProperty (final String name) throws JMSException
  {
    return m_aWrapped.getByteProperty (name);
  }

  public short getShortProperty (final String name) throws JMSException
  {
    return m_aWrapped.getShortProperty (name);
  }

  public int getIntProperty (final String name) throws JMSException
  {
    return m_aWrapped.getIntProperty (name);
  }

  public long getLongProperty (final String name) throws JMSException
  {
    return m_aWrapped.getLongProperty (name);
  }

  public float getFloatProperty (final String name) throws JMSException
  {
    return m_aWrapped.getFloatProperty (name);
  }

  public double getDoubleProperty (final String name) throws JMSException
  {
    return m_aWrapped.getDoubleProperty (name);
  }

  public String getStringProperty (final String name) throws JMSException
  {
    return m_aWrapped.getStringProperty (name);
  }

  public Object getObjectProperty (final String name) throws JMSException
  {
    return m_aWrapped.getObjectProperty (name);
  }

  public Enumeration <?> getPropertyNames () throws JMSException
  {
    return m_aWrapped.getPropertyNames ();
  }

  public void setBooleanProperty (final String name, final boolean value) throws JMSException
  {
    m_aWrapped.setBooleanProperty (name, value);
  }

  public void setByteProperty (final String name, final byte value) throws JMSException
  {
    m_aWrapped.setByteProperty (name, value);
  }

  public void setShortProperty (final String name, final short value) throws JMSException
  {
    m_aWrapped.setShortProperty (name, value);
  }

  public void setIntProperty (final String name, final int value) throws JMSException
  {
    m_aWrapped.setIntProperty (name, value);
  }

  public void setLongProperty (final String name, final long value) throws JMSException
  {
    m_aWrapped.setLongProperty (name, value);
  }

  public void setFloatProperty (final String name, final float value) throws JMSException
  {
    m_aWrapped.setFloatProperty (name, value);
  }

  public void setDoubleProperty (final String name, final double value) throws JMSException
  {
    m_aWrapped.setDoubleProperty (name, value);
  }

  public void setStringProperty (final String name, final String value) throws JMSException
  {
    m_aWrapped.setStringProperty (name, value);
  }

  public void setObjectProperty (final String name, final Object value) throws JMSException
  {
    m_aWrapped.setObjectProperty (name, value);
  }

  public void acknowledge () throws JMSException
  {
    m_aWrapped.acknowledge ();
  }

  public void clearBody () throws JMSException
  {
    m_aWrapped.clearBody ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("wrapped", m_aWrapped).toString ();
  }
}
