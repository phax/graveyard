/**
 * Copyright (C) 2016-2017 Philip Helger (www.helger.com)
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
package com.helger.zeromq;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnegative;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.string.ToStringGenerator;

/**
 * Dynamic port manager with port range.
 *
 * @author Philip Helger
 */
@ThreadSafe
public class ZMQPortManager
{
  // This port range is defined by IANA for dynamic or private ports
  // We use this when choosing a port for dynamic binding.
  public static final int DYNAMIC_PORT_FROM = 0xc000;
  public static final int DYNAMIC_PORT_TO = 0xffff;

  /**
   * Default instance using ports from {@link #DYNAMIC_PORT_FROM} to
   * {@link #DYNAMIC_PORT_TO}
   */
  public static final ZMQPortManager DEFAULT_INSTANCE = new ZMQPortManager (DYNAMIC_PORT_FROM, DYNAMIC_PORT_TO);

  private static final Logger s_aLogger = LoggerFactory.getLogger (ZMQPortManager.class);
  private final int m_nStartPort;
  private final int m_nEndPort;
  private final AtomicInteger m_aPort;

  /**
   * Constructor.
   *
   * @param nStartPort
   *        Start port (inclusive). Must be &gt; 1024 and &le; 0xffff.
   * @param nEndPort
   *        End port (inclusive). Must be &ge; start port and &le; 0xffff.
   */
  public ZMQPortManager (@Nonnegative final int nStartPort, @Nonnegative final int nEndPort)
  {
    ValueEnforcer.isTrue (nStartPort > 1024, "StartPort must be > 1024!");
    ValueEnforcer.isTrue (nStartPort <= 0xffff, "StartPort must <= 0xffff!");
    ValueEnforcer.isTrue (nEndPort <= 0xffff, "EndPort must be <= 0xffff!");
    ValueEnforcer.isTrue (nEndPort >= nStartPort, "EndPort must >= StartPort!");
    m_nStartPort = nStartPort;
    m_nEndPort = nEndPort;
    m_aPort = new AtomicInteger (nStartPort);
  }

  /**
   * @return The start port (inclusive) as provided in the constructor.
   */
  @Nonnegative
  public int getStartPort ()
  {
    return m_nStartPort;
  }

  /**
   * @return The end port (inclusive) as provided in the constructor.
   */
  @Nonnegative
  public int getEndPort ()
  {
    return m_nEndPort;
  }

  /**
   * Get the next port number in the range of {@link #getStartPort()} to
   * {@link #getEndPort()}. The first call will deliver the same value as
   * {@link #getStartPort()}, the second call will delivery the value of
   * {@link #getStartPort()} + 1 etc.
   *
   * @return The next port number to use.
   * @throws IllegalStateException
   *         if all ports are already used.
   */
  @Nonnegative
  public int getNextPort ()
  {
    final int ret = m_aPort.getAndIncrement ();
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("ZMQ port manager provides next port number " + ret);
    if (ret > m_nEndPort)
      throw new IllegalStateException ("All available ports (" +
                                       m_nStartPort +
                                       " - " +
                                       m_nEndPort +
                                       ") are already used!");
    return ret;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("StartPort", m_nStartPort)
                                       .append ("EndPort", m_nEndPort)
                                       .append ("Port", m_aPort)
                                       .toString ();
  }
}
