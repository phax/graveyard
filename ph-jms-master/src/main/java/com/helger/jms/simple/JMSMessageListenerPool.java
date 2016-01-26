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
package com.helger.jms.simple;

import java.io.Closeable;

import javax.annotation.Nonnull;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.jms.IJMSFactory;
import com.helger.jms.JMSHelper;

public class JMSMessageListenerPool implements Closeable
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (JMSMessageListenerPool.class);

  private final Connection m_aConnection;
  private final Session m_aSession;

  public JMSMessageListenerPool (@Nonnull final IJMSFactory aJMSFactory) throws JMSException
  {
    ValueEnforcer.notNull (aJMSFactory, "JMSFactory");

    // Create a Connection
    m_aConnection = aJMSFactory.createConnection ();

    // Create a Session
    m_aSession = m_aConnection.createSession (false, Session.AUTO_ACKNOWLEDGE);
    if (m_aSession == null)
      throw new IllegalStateException ("Failed to create JMS session from " + m_aConnection);
  }

  public void close ()
  {
    JMSHelper.close (m_aConnection);
    s_aLogger.info ("Closed MessageListener pool");
  }

  /**
   * @return The underlying JMS connection created in the constructor. Never
   *         <code>null</code>.
   */
  @Nonnull
  public Connection getConnection ()
  {
    return m_aConnection;
  }

  /**
   * @return The underlying JMS session created in the constructor. Never
   *         <code>null</code>.
   */
  @Nonnull
  public Session getSession ()
  {
    return m_aSession;
  }

  /**
   * Register a new message listener to a certain JMS queue.
   *
   * @param sQueueName
   *        JMS Queue name to listen. May neither be <code>null</code> nor
   *        empty.
   * @param aListener
   *        The main listener to register. May not be <code>null</code>.
   * @return The destination and the message consumer used. Never
   *         <code>null</code>.
   */
  @Nonnull
  public JMSDestinationAndConsumer registerMessageListener (@Nonnull @Nonempty final String sQueueName,
                                                            @Nonnull final MessageListener aListener)
  {
    ValueEnforcer.notEmpty (sQueueName, "QueueName");
    ValueEnforcer.notNull (aListener, "Listener");

    try
    {
      // Create the destination queue
      final Destination aDestination = m_aSession.createQueue (sQueueName);

      // Create a MessageConsumer from the Session to the Queue
      final MessageConsumer aConsumer = m_aSession.createConsumer (aDestination);
      aConsumer.setMessageListener (aListener);
      s_aLogger.info ("Successfully registered listener for queue '" + sQueueName + "'");

      return new JMSDestinationAndConsumer (aDestination, aConsumer);
    }
    catch (final JMSException ex)
    {
      throw new IllegalStateException ("Failed to register listener " +
                                       aListener +
                                       " for queue '" +
                                       sQueueName +
                                       "'",
                                       ex);
    }
  }

  /**
   * Register a new message listener to a certain JMS queue but with a special
   * JMS correlation ID.
   *
   * @param sQueueName
   *        JMS Queue name to listen. May neither be <code>null</code> nor
   *        empty.
   * @param sCorrelationID
   *        JMS correlation ID to match. May neither be <code>null</code> nor
   *        empty.
   * @param aListener
   *        The main listener to register. May not be <code>null</code>.
   * @param bOnce
   *        <code>true</code> if this is a one-time action in which case the
   *        created destination can be closed after the first retrieval.
   * @return The destination and the message consumer used. Never
   *         <code>null</code>.
   */
  @Nonnull
  public JMSDestinationAndConsumer registerMessageListenerForCorrelationID (@Nonnull @Nonempty final String sQueueName,
                                                                            @Nonnull @Nonempty final String sCorrelationID,
                                                                            @Nonnull final MessageListener aListener,
                                                                            final boolean bOnce)
  {
    ValueEnforcer.notEmpty (sQueueName, "QueueName");
    ValueEnforcer.notEmpty (sCorrelationID, "CorrelationID");
    ValueEnforcer.notNull (aListener, "Listener");

    try
    {
      // Create the destination queue
      final Destination aDestination = m_aSession.createQueue (sQueueName);

      // Create a MessageConsumer from the Session to the Queue
      final MessageConsumer aConsumer = m_aSession.createConsumer (aDestination,
                                                                   "JMSCorrelationID = '" + sCorrelationID + "'");
      if (bOnce)
      {
        aConsumer.setMessageListener (new MessageListener ()
        {
          public void onMessage (final Message aMessage)
          {
            try
            {
              // Invoke the passed listener
              aListener.onMessage (aMessage);
            }
            finally
            {
              // Close this specific consumer
              JMSHelper.close (aConsumer);
            }
          }
        });
      }
      else
      {
        // Use listener as is
        aConsumer.setMessageListener (aListener);
      }

      s_aLogger.info ("Successfully registered listener for queue '" +
                      sQueueName +
                      "' and correlation ID '" +
                      sCorrelationID +
                      "'");
      return new JMSDestinationAndConsumer (aDestination, aConsumer);
    }
    catch (final JMSException ex)
    {
      throw new IllegalStateException ("Failed to register listener " +
                                       aListener +
                                       " for queue '" +
                                       sQueueName +
                                       "' and correlation ID '" +
                                       sCorrelationID +
                                       "'",
                                       ex);
    }
  }
}
