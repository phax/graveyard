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
 * Simplification wrapper for "Pub-Sub Synchronization" via TCP as described in
 * http://zguide.zeromq.org/page:all#Node-Coordination
 *
 * @author Philip Helger
 */
public class SyncedPubSubTCP extends PubSubTCP
{
  private final int m_nSyncPort;

  // Status vars
  private transient ZMQSocket m_aPublisherSyncSocket;
  private transient ZMQSocket m_aSubscriberSyncSocket;

  public SyncedPubSubTCP (@Nonnull final IZMQSocketProvider aSocketProvider,
                          @Nonnull @Nonempty final String sName,
                          @Nonnegative final int nDataPort,
                          @Nonnegative final int nSyncPort)
  {
    super (aSocketProvider, sName, nDataPort);
    m_nSyncPort = ValueEnforcer.isGT0 (nSyncPort, "SyncPort");
  }

  @Nonnegative
  public int getSyncPort ()
  {
    return m_nSyncPort;
  }

  @Nonnull
  public ZMQSocket createPublisherSyncSocket ()
  {
    ZMQSocket ret = m_aPublisherSyncSocket;
    if (ret != null && !ret.isClosed ())
      throw new IllegalStateException ("Publisher sync socket is already present!");

    ret = m_aSocketProvider.createSocket (ESocketType.REP);
    // Throws exception if port is in use
    ret.bind ("tcp://*:" + m_nSyncPort);
    m_aPublisherSyncSocket = ret;
    return ret;
  }

  @Nonnull
  public ZMQSocket createSubscriberSyncSocket ()
  {
    ZMQSocket ret = m_aSubscriberSyncSocket;
    if (ret != null && !ret.isClosed ())
      throw new IllegalStateException ("Subscriber sync socket is already present!");

    ret = m_aSocketProvider.createSocket (ESocketType.REQ);
    ret.connect ("tcp://localhost:" + m_nSyncPort);
    m_aSubscriberSyncSocket = ret;
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final SyncedPubSubTCP rhs = (SyncedPubSubTCP) o;
    return m_nSyncPort == rhs.m_nSyncPort;
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_nSyncPort).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("SyncPort", m_nSyncPort)
                            .appendIfNotNull ("PublisherSyncSocket", m_aPublisherSyncSocket)
                            .appendIfNotNull ("SubscriberSyncSocket", m_aSubscriberSyncSocket)
                            .toString ();
  }
}
