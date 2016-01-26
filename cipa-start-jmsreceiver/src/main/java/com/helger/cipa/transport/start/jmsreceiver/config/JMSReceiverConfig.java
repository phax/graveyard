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
package com.helger.cipa.transport.start.jmsreceiver.config;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.exceptions.InitializationException;
import com.helger.commons.string.StringHelper;

import eu.europa.ec.cipa.peppol.utils.ConfigFile;

/**
 * This class manages the configuration file for this component.
 *
 * @author Philip Helger
 */
@Immutable
public final class JMSReceiverConfig
{
  /**
   * The name of the configuration file. Must be within the classpath!
   */
  public static final String CONFIG_FILE_PATH = "config-start-jmsreceiver.properties";

  private static final ConfigFile s_aConfigFile;

  static
  {
    s_aConfigFile = new ConfigFile (CONFIG_FILE_PATH);
  }

  private JMSReceiverConfig ()
  {}

  /**
   * @return The ActiveMQ connection string to be used. No default value.
   */
  @Nullable
  public static String getConnectionString ()
  {
    return s_aConfigFile.getString ("connection-string");
  }

  /**
   * @return Name of the queue for incoming messages. Defaults to
   *         "FROM_PEPPOL_INBOX".
   */
  @Nullable
  public static String getInboxQueueName ()
  {
    return s_aConfigFile.getString ("inbox-queue-name", "FROM_PEPPOL_INBOX");
  }

  /**
   * @return Name of the queue for response messages. Defaults to
   *         "FROM_PEPPOL_RESPONSE".
   */
  @Nullable
  public static String getResponseQueueName ()
  {
    return s_aConfigFile.getString ("response-queue-name", "FROM_PEPPOL_RESPONSE");
  }

  /**
   * @return <code>true</code> to use persistent messaging (if possible).
   *         Defaults to <code>false</code>.
   */
  public static boolean isPersistentMessaging ()
  {
    return s_aConfigFile.getBoolean ("persistent-messaging", false);
  }

  /**
   * @return The timeout in milliseconds to wait for the response. Values &le; 0
   *         indicate infinity. Defaults to 5000 (=5 seconds).
   */
  public static int getResponseTimeoutMilliSeconds ()
  {
    return s_aConfigFile.getInt ("response-timeout", 5000);
  }

  /**
   * Validate that the mandatory elements of the configuration file are present.
   */
  public static void validateConfiguration ()
  {
    final String sConnectionString = getConnectionString ();
    if (StringHelper.hasNoText (sConnectionString))
      throw new InitializationException ("Configuration error: no connection string provided!");

    final String sInboxQueueName = getInboxQueueName ();
    if (StringHelper.hasNoText (sInboxQueueName))
      throw new InitializationException ("Configuration error: no inbox queue name provided!");

    final String sResponseQueueName = getResponseQueueName ();
    if (StringHelper.hasNoText (sResponseQueueName))
      throw new InitializationException ("Configuration error: no response queue name provided!");
  }
}
