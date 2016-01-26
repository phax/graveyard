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
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.OverrideOnDemand;
import com.helger.jms.IJMSFactory;
import com.helger.jms.JMSHelper;

/**
 * A simple receiver for JMS messages
 *
 * @author Philip Helger
 */
public class JMSSimpleReceiver
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (JMSSimpleReceiver.class);

  private final IJMSFactory m_aJMSFactory;

  public JMSSimpleReceiver (@Nonnull final IJMSFactory aJMSFactory)
  {
    ValueEnforcer.notNull (aJMSFactory, "JMSFactory");
    m_aJMSFactory = aJMSFactory;
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

  public void receiveNonTransactional (@Nonnull @Nonempty final String sQueueName,
                                       @Nonnull final IJMSMessageHandler aMsgHandler)
  {
    ValueEnforcer.notEmpty (sQueueName, "QueueName");
    ValueEnforcer.notNull (aMsgHandler, "MsgHandler");

    Connection aConnection = null;
    try
    {
      // Create a Connection
      aConnection = m_aJMSFactory.createConnection ();

      // Create a Session
      final Session aSession = aConnection.createSession (false, Session.AUTO_ACKNOWLEDGE);

      // Create the destination (Topic or Queue)
      final Destination aDestination = aSession.createQueue (sQueueName);

      // Create a MessageConsumer from the Session to the Topic or Queue
      final MessageConsumer aConsumer = aSession.createConsumer (aDestination);

      // Wait for a message
      final Message aMessage = aConsumer.receive ();
      aMsgHandler.handleMessage (aMessage);

      // No commit for non-transacted sessions
    }
    catch (final JMSException ex)
    {
      onException (ex);
    }
    finally
    {
      JMSHelper.close (aConnection);
    }
  }

  public void receiveTransactional (@Nonnull @Nonempty final String sQueueName,
                                    @Nonnull final IJMSMessageHandler aMsgHandler)
  {
    ValueEnforcer.notEmpty (sQueueName, "QueueName");
    ValueEnforcer.notNull (aMsgHandler, "MsgHandler");

    Connection aConnection = null;
    try
    {
      // Create a Connection
      aConnection = m_aJMSFactory.createConnection ();

      // Create a Session
      final Session aSession = aConnection.createSession (true, -1);

      // Create the destination (Topic or Queue)
      final Destination aDestination = aSession.createQueue (sQueueName);

      // Create a MessageConsumer from the Session to the Topic or Queue
      final MessageConsumer aConsumer = aSession.createConsumer (aDestination);

      // Wait for a message
      final Message aMessage = aConsumer.receive ();
      aMsgHandler.handleMessage (aMessage);

      // commit for transacted sessions
      aSession.commit ();
    }
    catch (final JMSException ex)
    {
      onException (ex);
    }
    finally
    {
      JMSHelper.close (aConnection);
    }
  }
}
