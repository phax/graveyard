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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.UsedViaReflection;
import com.helger.commons.callback.IThrowingRunnable;
import com.helger.commons.concurrent.BasicThreadFactory;
import com.helger.commons.concurrent.ManagedExecutorService;
import com.helger.commons.scope.IScope;
import com.helger.commons.scope.singleton.AbstractGlobalSingleton;

public class ZMQWorkerPool extends AbstractGlobalSingleton
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ZMQWorkerPool.class);

  private final ExecutorService m_aES;

  @Deprecated
  @UsedViaReflection
  public ZMQWorkerPool ()
  {
    m_aES = Executors.newFixedThreadPool (3,
                                          new BasicThreadFactory.Builder ().setDaemon (true)
                                                                           .setNamingPattern ("zmq-worker-%d")
                                                                           .build ());
  }

  @Nonnull
  public static ZMQWorkerPool getInstance ()
  {
    return getGlobalSingleton (ZMQWorkerPool.class);
  }

  @Override
  protected void onDestroy (@Nonnull final IScope aScopeInDestruction) throws Exception
  {
    s_aLogger.info ("Global ZMQ worker queue about to be closed");
    ManagedExecutorService.shutdownAndWaitUntilAllTasksAreFinished (m_aES);
    s_aLogger.info ("Global ZMQ worker queue closed!");
  }

  @Nonnull
  public CompletableFuture <Void> run (@Nonnull final IThrowingRunnable <? extends Throwable> aRunnable)
  {
    return CompletableFuture.runAsync ( () -> {
      try
      {
        aRunnable.run ();
      }
      catch (final Throwable t)
      {
        s_aLogger.error ("Error running ZMQ runner " + aRunnable, t);
      }
    }, m_aES);
  }

  @Nonnull
  public <T> CompletableFuture <T> supply (@Nonnull final Supplier <T> aSupplier)
  {
    return CompletableFuture.supplyAsync ( () -> {
      try
      {
        return aSupplier.get ();
      }
      catch (final Exception ex)
      {
        s_aLogger.error ("Error running ZMQ supplier " + aSupplier, ex);
        return null;
      }
    }, m_aES);
  }
}
