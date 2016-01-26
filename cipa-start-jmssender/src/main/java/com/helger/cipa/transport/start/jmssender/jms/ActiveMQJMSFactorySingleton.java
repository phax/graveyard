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
package com.helger.cipa.transport.start.jmssender.jms;

import javax.annotation.Nonnull;

import com.helger.commons.annotations.UsedViaReflection;
import com.helger.commons.scopes.IScope;
import com.helger.commons.scopes.singleton.GlobalSingleton;
import com.helger.jms.IJMSFactory;

/**
 * This is the singleton accessor to get the JMS factory. It ensures that the
 * underlying {@link ActiveMQJMSFactory} is correctly closed, when the web
 * application is shutdown.
 *
 * @author Philip Helger
 */
public final class ActiveMQJMSFactorySingleton extends GlobalSingleton
{
  private final ActiveMQJMSFactory m_aFactory;

  @Deprecated
  @UsedViaReflection
  public ActiveMQJMSFactorySingleton ()
  {
    m_aFactory = new ActiveMQJMSFactory ();
  }

  @Nonnull
  public static ActiveMQJMSFactorySingleton getInstance ()
  {
    return getGlobalSingleton (ActiveMQJMSFactorySingleton.class);
  }

  @Override
  protected void onDestroy (@Nonnull final IScope aScopeInDestruction)
  {
    m_aFactory.shutdown ();
  }

  @Nonnull
  public IJMSFactory getFactory ()
  {
    return m_aFactory;
  }
}
