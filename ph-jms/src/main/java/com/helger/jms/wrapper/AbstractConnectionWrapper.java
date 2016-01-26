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
import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.string.ToStringGenerator;

/**
 * Wrapped class for a JMS {@link Connection}.
 *
 * @author Philip Helger
 */
public abstract class AbstractConnectionWrapper extends AbstractWrappedJMS implements Connection
{
  private final Connection m_aWrapped;

  public AbstractConnectionWrapper (@Nonnull final JMSWrapper aWrapper, @Nonnull final Connection aWrapped)
  {
    super (aWrapper);
    ValueEnforcer.notNull (aWrapped, "Wrapped");
    m_aWrapped = aWrapped;
  }

  /**
   * @return The wrapped object. Never <code>null</code>.
   */
  @Nonnull
  protected Connection getWrapped ()
  {
    return m_aWrapped;
  }

  @Nonnull
  public Session createSession (final boolean transacted, final int acknowledgeMode) throws JMSException
  {
    final Session aSession = m_aWrapped.createSession (transacted, acknowledgeMode);
    return getWrapper ().wrapSession (aSession);
  }

  public String getClientID () throws JMSException
  {
    return m_aWrapped.getClientID ();
  }

  public void setClientID (final String clientID) throws JMSException
  {
    m_aWrapped.setClientID (clientID);
  }

  public ConnectionMetaData getMetaData () throws JMSException
  {
    return m_aWrapped.getMetaData ();
  }

  public ExceptionListener getExceptionListener () throws JMSException
  {
    return m_aWrapped.getExceptionListener ();
  }

  public void setExceptionListener (final ExceptionListener listener) throws JMSException
  {
    m_aWrapped.setExceptionListener (listener);
  }

  public void start () throws JMSException
  {
    m_aWrapped.start ();
  }

  public void stop () throws JMSException
  {
    m_aWrapped.stop ();
  }

  public void close () throws JMSException
  {
    m_aWrapped.close ();
  }

  @Nonnull
  public ConnectionConsumer createConnectionConsumer (final Destination destination,
                                                      final String messageSelector,
                                                      final ServerSessionPool sessionPool,
                                                      final int maxMessages) throws JMSException
  {
    final ConnectionConsumer aConnectionConsumer = m_aWrapped.createConnectionConsumer (destination,
                                                                                        messageSelector,
                                                                                        sessionPool,
                                                                                        maxMessages);
    return getWrapper ().wrapConnectionConsumer (aConnectionConsumer);
  }

  @Nonnull
  public ConnectionConsumer createDurableConnectionConsumer (final Topic topic,
                                                             final String subscriptionName,
                                                             final String messageSelector,
                                                             final ServerSessionPool sessionPool,
                                                             final int maxMessages) throws JMSException
  {
    final ConnectionConsumer aConnectionConsumer = m_aWrapped.createDurableConnectionConsumer (topic,
                                                                                               subscriptionName,
                                                                                               messageSelector,
                                                                                               sessionPool,
                                                                                               maxMessages);
    return getWrapper ().wrapConnectionConsumer (aConnectionConsumer);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("wrapped", m_aWrapped).toString ();
  }
}
