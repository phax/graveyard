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
package com.helger.jms;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Misc JMS utilities.
 *
 * @author Philip Helger
 */
@ThreadSafe
public final class JMSHelper
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (JMSHelper.class);

  private JMSHelper ()
  {}

  /**
   * Build a JMS exception with a causing exception. This is because the
   * {@link JMSException} class is lacking a respective constructor.
   *
   * @param sMsg
   *        String message
   * @param aCause
   *        Causing exception
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static JMSException createException (@Nonnull final String sMsg, @Nullable final Throwable aCause)
  {
    final JMSException ret = new JMSException (sMsg);
    if (aCause instanceof Exception)
      ret.setLinkedException ((Exception) aCause);
    ret.initCause (aCause);
    return ret;
  }

  /**
   * Close the passed JMS {@link Connection}
   *
   * @param aConnection
   *        The connection to be closed. May be <code>null</code>.
   */
  public static void close (@Nullable final Connection aConnection)
  {
    if (aConnection != null)
      try
      {
        // There is no need to close the sessions, producers, and consumers of a
        // connection to be closed.
        aConnection.close ();
      }
      catch (final JMSException ex)
      {
        s_aLogger.error ("Failed to close JMS Connection " + aConnection.toString (), ex);
      }
  }

  /**
   * Close the passed JMS {@link ConnectionConsumer}
   *
   * @param aConnectionConsumer
   *        The connection to be closed. May be <code>null</code>.
   */
  public static void close (@Nullable final ConnectionConsumer aConnectionConsumer)
  {
    if (aConnectionConsumer != null)
      try
      {
        aConnectionConsumer.close ();
      }
      catch (final JMSException ex)
      {
        s_aLogger.error ("Failed to close JMS ConnectionConsumer " + aConnectionConsumer.toString (), ex);
      }
  }

  /**
   * Close the passed JMS {@link MessageConsumer}
   *
   * @param aMessageConsumer
   *        The connection to be closed. May be <code>null</code>.
   */
  public static void close (@Nullable final MessageConsumer aMessageConsumer)
  {
    if (aMessageConsumer != null)
      try
      {
        aMessageConsumer.close ();
      }
      catch (final JMSException ex)
      {
        s_aLogger.error ("Failed to close JMS MessageConsumer " + aMessageConsumer.toString (), ex);
      }
  }

  /**
   * Close the passed JMS {@link MessageProducer}
   *
   * @param aMessageProducer
   *        The connection to be closed. May be <code>null</code>.
   */
  public static void close (@Nullable final MessageProducer aMessageProducer)
  {
    if (aMessageProducer != null)
      try
      {
        aMessageProducer.close ();
      }
      catch (final JMSException ex)
      {
        s_aLogger.error ("Failed to close JMS MessageProducer " + aMessageProducer.toString (), ex);
      }
  }

  /**
   * Close the passed JMS {@link QueueBrowser}
   *
   * @param aQueueBrowser
   *        The connection to be closed. May be <code>null</code>.
   */
  public static void close (@Nullable final QueueBrowser aQueueBrowser)
  {
    if (aQueueBrowser != null)
      try
      {
        aQueueBrowser.close ();
      }
      catch (final JMSException ex)
      {
        s_aLogger.error ("Failed to close JMS QueueBrowser " + aQueueBrowser.toString (), ex);
      }
  }

  /**
   * Close the passed JMS {@link Session}
   *
   * @param aSession
   *        The connection to be closed. May be <code>null</code>.
   */
  public static void close (@Nullable final Session aSession)
  {
    if (aSession != null)
      try
      {
        aSession.close ();
      }
      catch (final JMSException ex)
      {
        s_aLogger.error ("Failed to close JMS Session " + aSession.toString (), ex);
      }
  }
}
