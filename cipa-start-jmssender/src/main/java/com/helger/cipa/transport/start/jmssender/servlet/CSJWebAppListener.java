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
package com.helger.cipa.transport.start.jmssender.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.slf4j.bridge.SLF4JBridgeHandler;

import com.helger.cipa.transport.start.jmssender.config.CSJConfig;
import com.helger.cipa.transport.start.jmssender.jms.CSJMessageListener;
import com.helger.cipa.transport.start.jmssender.jms.CSJMessageListenerPool;
import com.helger.commons.annotations.Nonempty;
import com.helger.webbasics.app.init.DefaultApplicationInitializer;
import com.helger.webbasics.app.init.IApplicationInitializer;
import com.helger.webbasics.app.layout.LayoutExecutionContext;
import com.helger.webbasics.servlet.WebAppListenerMultiApp;

/**
 * Callbacks for the application server
 *
 * @author Philip Helger
 */
public final class CSJWebAppListener extends WebAppListenerMultiApp <LayoutExecutionContext>
{
  public static final String APP_ID = "jmssender";

  @Override
  @Nonnull
  @Nonempty
  protected Map <String, IApplicationInitializer <LayoutExecutionContext>> getAllInitializers ()
  {
    final Map <String, IApplicationInitializer <LayoutExecutionContext>> ret = new HashMap <String, IApplicationInitializer <LayoutExecutionContext>> ();
    ret.put (APP_ID, new DefaultApplicationInitializer <LayoutExecutionContext> ());
    return ret;
  }

  @Override
  protected void initGlobals ()
  {
    // JUL to SLF4J
    SLF4JBridgeHandler.removeHandlersForRootLogger ();
    SLF4JBridgeHandler.install ();

    super.initGlobals ();

    // Register JMS listener
    CSJMessageListenerPool.getInstance ().registerMessageListener (CSJConfig.getToPeppolInboxQueueName (),
                                                                   new CSJMessageListener ());
  }
}
