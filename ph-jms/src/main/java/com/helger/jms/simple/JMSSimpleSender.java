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

import javax.annotation.Nonnull;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.OverrideOnDemand;
import com.helger.commons.state.ESuccess;
import com.helger.jms.IJMSFactory;
import com.helger.jms.JMSHelper;

/**
 * A simple sender for JMS messages.
 *
 * @author Philip Helger
 */
public class JMSSimpleSender
{
  public static final boolean DEFAULT_PERSISTENT = false;

  private static final Logger s_aLogger = LoggerFactory.getLogger (JMSSimpleSender.class);

  private final IJMSFactory m_aJMSFactory;
  private final boolean m_bPersistent;

  public JMSSimpleSender (@Nonnull final IJMSFactory aJMSFactory)
  {
    this (aJMSFactory, DEFAULT_PERSISTENT);
  }

  public JMSSimpleSender (@Nonnull final IJMSFactory aJMSFactory, final boolean bPersistent)
  {
    ValueEnforcer.notNull (aJMSFactory, "JMSFactory");
    m_aJMSFactory = aJMSFactory;
    m_bPersistent = bPersistent;
  }

  /**
   * @return The JMS factory from the constructor.
   */
  @Nonnull
  protected final IJMSFactory getJMSFactory ()
  {
    return m_aJMSFactory;
  }

  /**
   * @return <code>true</code> if this sender sends persistent messages,
   *         <code>false</code> if not.
   */
  public final boolean isPersistent ()
  {
    return m_bPersistent;
  }

  /**
   * Overridable method that is invoked for JMS exceptions.
   *
   * @param ex
   *        The exception. Never <code>null</code>.
   */
  @OverrideOnDemand
  protected void onException (@Nonnull final JMSException ex)
  {
    s_aLogger.error (ex.getMessage (), ex.getCause ());
  }

  @Nonnull
  public ESuccess sendNonTransactional (@Nonnull @Nonempty final String sQueueName,
                                        @Nonnull final IJMSMessageCreator aMsgCreator)
  {
    ValueEnforcer.notEmpty (sQueueName, "QueueName");
    ValueEnforcer.notNull (aMsgCreator, "MsgCreator");

    Connection aConnection = null;
    try
    {
      // Create a Connection
      aConnection = m_aJMSFactory.createConnection ();

      // Create a Session
      final Session aSession = aConnection.createSession (false, Session.AUTO_ACKNOWLEDGE);

      // Create the destination (Topic or Queue)
      final Queue aDestination = aSession.createQueue (sQueueName);

      // Create a MessageProducer from the Session to the Topic or Queue
      final MessageProducer aProducer = aSession.createProducer (aDestination);
      aProducer.setDeliveryMode (m_bPersistent ? DeliveryMode.PERSISTENT : DeliveryMode.NON_PERSISTENT);

      // Create a messages
      final Message aMessage = aMsgCreator.createMessage (aSession);
      if (aMessage == null)
        throw new IllegalStateException ("Failed to create message");

      // Tell the producer to send the message
      aProducer.send (aMessage);

      // No commit for non-transacted sessions
      return ESuccess.SUCCESS;
    }
    catch (final JMSException ex)
    {
      onException (ex);
      return ESuccess.FAILURE;
    }
    finally
    {
      JMSHelper.close (aConnection);
    }
  }

  @Nonnull
  public ESuccess sendTransactional (@Nonnull @Nonempty final String sQueueName,
                                     @Nonnull final IJMSMessageCreator aMsgCreator)
  {
    ValueEnforcer.notEmpty (sQueueName, "QueueName");
    ValueEnforcer.notNull (aMsgCreator, "MsgCreator");

    Connection aConnection = null;
    try
    {
      // Create a Connection
      aConnection = m_aJMSFactory.createConnection ();

      // Create a Session
      final Session aSession = aConnection.createSession (true, -1);

      // Create the destination (Topic or Queue)
      final Queue aDestination = aSession.createQueue (sQueueName);

      // Create a MessageProducer from the Session to the Topic or Queue
      final MessageProducer aProducer = aSession.createProducer (aDestination);
      aProducer.setDeliveryMode (m_bPersistent ? DeliveryMode.PERSISTENT : DeliveryMode.NON_PERSISTENT);

      // Create a messages
      final Message aMessage = aMsgCreator.createMessage (aSession);
      if (aMessage == null)
        throw new IllegalStateException ("Failed to create message");

      // Tell the producer to send the message
      aProducer.send (aMessage);

      // commit for transacted sessions
      aSession.commit ();
      return ESuccess.SUCCESS;
    }
    catch (final JMSException ex)
    {
      onException (ex);
      return ESuccess.FAILURE;
    }
    finally
    {
      JMSHelper.close (aConnection);
    }
  }
}
