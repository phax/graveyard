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
package com.helger.zeromq.singleton;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZContext;

import com.helger.commons.annotation.UsedViaReflection;
import com.helger.commons.scope.IScope;
import com.helger.commons.scope.singleton.AbstractGlobalSingleton;
import com.helger.zeromq.socket.ESocketType;
import com.helger.zeromq.socket.IZMQSocketProvider;
import com.helger.zeromq.socket.ZMQSocket;

public class ZMQGlobalSingleton extends AbstractGlobalSingleton implements IZMQSocketProvider
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ZMQGlobalSingleton.class);

  private final ZContext m_aContext;

  @Deprecated
  @UsedViaReflection
  public ZMQGlobalSingleton ()
  {
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("Global ZMQ context about to be initialized");
    // param == no of IO threads
    m_aContext = new ZContext (3);
    s_aLogger.info ("Global ZMQ context initialized!");
  }

  @Nonnull
  public static ZMQGlobalSingleton getInstance ()
  {
    return getGlobalSingleton (ZMQGlobalSingleton.class);
  }

  @Override
  protected void onDestroy (@Nonnull final IScope aScopeInDestruction) throws Exception
  {
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("Global ZMQ context about to be closed");
    m_aContext.close ();
    s_aLogger.info ("Global ZMQ context closed!");
  }

  @Nonnull
  public ZContext getContext ()
  {
    return m_aContext;
  }

  /**
   * Creates a new managed socket within this instance. Use this to get
   * automatic management of the socket at shutdown
   *
   * @param eType
   *        socket type. May not be <code>null</code>.
   * @return Newly created Socket object
   */
  @Nonnull
  public ZMQSocket createSocket (@Nonnull final ESocketType eType)
  {
    return ZMQSocket.create (getContext (), eType);
  }
}
