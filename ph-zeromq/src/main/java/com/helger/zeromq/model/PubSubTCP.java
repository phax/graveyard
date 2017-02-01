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
package com.helger.zeromq.model;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;
import com.helger.zeromq.socket.ESocketType;
import com.helger.zeromq.socket.IZMQSocketProvider;
import com.helger.zeromq.socket.ZMQSocket;

/**
 * Simplification wrapper for "Pub-Sub" via TCP
 *
 * @author Philip Helger
 */
public class PubSubTCP extends AbstractZMQModel
{
  private final int m_nDataPort;

  // Status vars
  private transient ZMQSocket m_aPublisherDataSocket;
  private transient ZMQSocket m_aSubscriberDataSocket;

  public PubSubTCP (@Nonnull final IZMQSocketProvider aSocketProvider,
                    @Nonnull @Nonempty final String sName,
                    @Nonnegative final int nDataPort)
  {
    super (aSocketProvider, sName);
    m_nDataPort = ValueEnforcer.isGT0 (nDataPort, "DataPort");
  }

  @Nonnegative
  public int getDataPort ()
  {
    return m_nDataPort;
  }

  @Nonnull
  public ZMQSocket createPublisherDataSocket ()
  {
    ZMQSocket ret = m_aPublisherDataSocket;
    if (ret != null && !ret.isClosed ())
      throw new IllegalStateException ("Publisher data socket is already present!");

    ret = m_aSocketProvider.createSocket (ESocketType.PUB);
    ret.setLinger (-1);
    // In 0MQ 3.x pub socket could drop messages if sub can follow the
    // generation of pub messages
    ret.setSndHWM (0);
    // Throws exception if port is in use
    ret.bind ("tcp://*:" + m_nDataPort);
    m_aPublisherDataSocket = ret;
    return ret;
  }

  @Nonnull
  public ZMQSocket getExistingPublisherDataSocket ()
  {
    final ZMQSocket ret = m_aPublisherDataSocket;
    if (ret == null)
      throw new IllegalStateException ("Publisher data socket was not yet created!");
    if (ret.isClosed ())
      throw new IllegalStateException ("Publisher data socket is already closed!");
    return ret;
  }

  @Nonnull
  public ZMQSocket createSubscriberDataSocket ()
  {
    ZMQSocket ret = m_aSubscriberDataSocket;
    if (ret != null && !ret.isClosed ())
      throw new IllegalStateException ("Subscriber data socket is already present!");

    ret = m_aSocketProvider.createSocket (ESocketType.SUB);
    ret.setRcvHWM (0);
    ret.connect ("tcp://localhost:" + m_nDataPort);
    // Subscribe to everything
    ret.subscribeToAll ();
    m_aSubscriberDataSocket = ret;
    return ret;
  }

  @Nonnull
  public ZMQSocket getExistingSubscriberDataSocket ()
  {
    final ZMQSocket ret = m_aSubscriberDataSocket;
    if (ret == null)
      throw new IllegalStateException ("Subscriber data socket was not yet created!");
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final PubSubTCP rhs = (PubSubTCP) o;
    return m_nDataPort == rhs.m_nDataPort;
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_nDataPort).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("DataPort", m_nDataPort)
                            .appendIfNotNull ("PublisherDataSocket", m_aPublisherDataSocket)
                            .appendIfNotNull ("SubscriberDataSocket", m_aSubscriberDataSocket)
                            .toString ();
  }
}
