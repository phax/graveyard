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
import javax.jms.JMSException;
import javax.jms.MessageListener;

import com.helger.commons.annotations.Nonempty;
import com.helger.commons.annotations.UsedViaReflection;
import com.helger.commons.scopes.IScope;
import com.helger.commons.scopes.singleton.GlobalSingleton;
import com.helger.jms.simple.JMSDestinationAndConsumer;
import com.helger.jms.simple.JMSMessageListenerPool;

public class CSJMessageListenerPool extends GlobalSingleton
{
  private final JMSMessageListenerPool m_aPool;

  @Deprecated
  @UsedViaReflection
  public CSJMessageListenerPool () throws JMSException
  {
    m_aPool = new JMSMessageListenerPool (ActiveMQJMSFactorySingleton.getInstance ().getFactory ());
  }

  @Nonnull
  public static CSJMessageListenerPool getInstance ()
  {
    return getGlobalSingleton (CSJMessageListenerPool.class);
  }

  @Override
  protected void onDestroy (@Nonnull final IScope aScopeInDestruction)
  {
    m_aPool.close ();
  }

  @Nonnull
  public JMSDestinationAndConsumer registerMessageListener (@Nonnull @Nonempty final String sQueueName,
                                                            @Nonnull final MessageListener aListener)
  {
    return m_aPool.registerMessageListener (sQueueName, aListener);
  }
}
