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
package com.helger.cipa.transport.start.jmssender.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.exceptions.InitializationException;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.microdom.reader.XMLMapHandler;
import com.helger.commons.string.StringParser;

/**
 * Central config file.
 *
 * @author Philip Helger
 */
public final class CSJConfig
{
  public static final String CONFIG_FILENAME = "config-start-jms-sender.xml";
  private static final CSJConfig s_aInstance = new CSJConfig ();

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final Map <String, String> m_aMap = new HashMap <String, String> ();

  private CSJConfig ()
  {
    reinit ();
  }

  @Nonnull
  public static CSJConfig getInstance ()
  {
    return s_aInstance;
  }

  public void reinit ()
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      m_aMap.clear ();
      if (XMLMapHandler.readMap (new ClassPathResource (CONFIG_FILENAME), m_aMap).isFailure ())
        throw new InitializationException ("Failed to read config file " + CONFIG_FILENAME);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  public String getString (@Nullable final String sKey, @Nullable final String sDefault)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      final String ret = m_aMap.get (sKey);
      return ret != null ? ret : sDefault;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  public String getStringRequired (@Nullable final String sKey, @Nullable final String sDefault)
  {
    final String ret = getString (sKey, sDefault);
    if (ret == null)
      throw new IllegalArgumentException ("ConfigFile is missing an entry for '" + sKey + "'");
    return ret;
  }

  @Nonnull
  public String getStringRequired (@Nullable final String sKey)
  {
    return getStringRequired (sKey, null);
  }

  public boolean getBooleanRequired (@Nullable final String sKey, final boolean bDefault)
  {
    final String ret = getString (sKey, Boolean.toString (bDefault));
    return StringParser.parseBool (ret);
  }

  @Nonnull
  public static String getConnectionString ()
  {
    return getInstance ().getStringRequired ("connectionstring");
  }

  public static boolean getPersistentMessaging ()
  {
    return getInstance ().getBooleanRequired ("persistent-messaging", false);
  }

  // TO_PEPPOL_INBOX
  @Nonnull
  public static String getToPeppolInboxQueueName ()
  {
    return getInstance ().getStringRequired ("to-peppol-inbox-queue-name");
  }

  // TO_PEPPOL_RESPONSE
  @Nonnull
  public static String getToPeppolResponseQueueName ()
  {
    return getInstance ().getStringRequired ("to-peppol-response-queue-name");
  }
}
