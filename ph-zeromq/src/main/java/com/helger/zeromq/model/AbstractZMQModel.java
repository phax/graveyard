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

import java.io.Serializable;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.name.IHasName;
import com.helger.commons.string.ToStringGenerator;
import com.helger.zeromq.socket.IZMQSocketProvider;

/**
 * Abstract base class for ZMQ model abstractions
 *
 * @author Philip Helger
 */
public abstract class AbstractZMQModel implements Serializable, IHasName
{
  protected final IZMQSocketProvider m_aSocketProvider;
  private final String m_sName;

  public AbstractZMQModel (@Nonnull final IZMQSocketProvider aSocketProvider, @Nonnull @Nonempty final String sName)
  {
    m_aSocketProvider = ValueEnforcer.notNull (aSocketProvider, "SocketProvider");
    m_sName = ValueEnforcer.notEmpty (sName, "Name");
  }

  @Nonnull
  public IZMQSocketProvider getSocketProvider ()
  {
    return m_aSocketProvider;
  }

  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final AbstractZMQModel rhs = (AbstractZMQModel) o;
    // SocketProvider may not implement equals/hashCode
    return m_sName.equals (rhs.m_sName);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sName).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("SocketProvider", m_aSocketProvider)
                                       .append ("Name", m_sName)
                                       .toString ();
  }
}
