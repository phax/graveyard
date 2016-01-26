/**
 * Copyright (C) 2013-2015 Philip Helger (www.helger.com)
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
package com.helger.cipa.transport.start.jmsreceiver.jms;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.cipa.transport.start.jmsreceiver.config.JMSReceiverConfig;
import com.helger.commons.annotations.UsedViaReflection;
import com.helger.commons.scopes.IScope;
import com.helger.jms.JMSUtils;
import com.helger.web.scopes.singleton.GlobalWebSingleton;

/**
 * This is the global JMS response handler for this project. It waits for
 * responses to messages with a certain JMS correlation ID on the queue
 * specified by {@link JMSReceiverConfig#getResponseQueueName ()}.
 *
 * @author Philip Helger
 */
public class JMSResponseHandler extends GlobalWebSingleton
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (JMSResponseHandler.class);

  private final Connection m_aConnection;
  private final Session m_aSession;
  private final Queue m_aDestination;
  private final long m_nTimeoutMilliseconds;

  @Deprecated
  @UsedViaReflection
  public JMSResponseHandler () throws JMSException
  {
    // Create a Connection
    m_aConnection = ActiveMQJMSFactorySingleton.getInstance ().getFactory ().createConnection ();

    // Create a non-transactional Session
    m_aSession = m_aConnection.createSession (false, Session.AUTO_ACKNOWLEDGE);

    // Create the destination (Topic or Queue)
    final String sQueueName = JMSReceiverConfig.getResponseQueueName ();
    m_aDestination = m_aSession.createQueue (sQueueName);

    // Response timeout
    m_nTimeoutMilliseconds = JMSReceiverConfig.getResponseTimeoutMilliSeconds ();
    s_aLogger.info ("Using a timeout of " + m_nTimeoutMilliseconds + " ms to wait for answers in " + sQueueName);
  }

  /**
   * @return The global singleton instance of this class.
   */
  @Nonnull
  public static JMSResponseHandler getInstance ()
  {
    return getGlobalSingleton (JMSResponseHandler.class);
  }

  /**
   * Internally called when this singleton gets destroyed.
   */
  @Override
  protected void onDestroy (@Nonnull final IScope aScopeInDestruction)
  {
    JMSUtils.close (m_aConnection);
  }

  /**
   * @return The JMS destination to be used as the "replyTo" when sending
   *         messages.
   */
  @Nonnull
  public Destination getJMSDestination ()
  {
    return m_aDestination;
  }

  /**
   * Wait for the response message until the timeout. If a timeout occurs, it's
   * the responsibility of the caller to ensure that the message gets deleted.
   *
   * @param sCorrelationID
   *        The JMS correlation ID to filter the message
   * @return The retrieved message or <code>null</code> in case of a timeout. If
   *         <code>null</code> is returned, the message will stay in the JMS
   *         queue if it arrives at some time later. It is the responsibility of
   *         the caller to handle this case. If a timeout of "0" is specified,
   *         this messages waits (blocking) forever.
   */
  @Nullable
  public Message waitForResponse (@Nonnull final String sCorrelationID)
  {
    try
    {
      // Create a MessageConsumer for the specified correlation ID
      final MessageConsumer aConsumer = m_aSession.createConsumer (m_aDestination, "JMSCorrelationID = '" +
                                                                                   sCorrelationID +
                                                                                   "'");
      try
      {
        // Wait for the specified timeout
        return aConsumer.receive (m_nTimeoutMilliseconds);
      }
      finally
      {
        // Close the consumer to avoid resource leaks
        JMSUtils.close (aConsumer);
      }
    }
    catch (final JMSException ex)
    {
      s_aLogger.error ("Error waiting for JMS response", ex);
      return null;
    }
  }
}
