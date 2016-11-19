/**
 * Copyright (C) 2011-2014 Philip Helger <philip[at]helger[dot]com>
 * All Rights Reserved
 *
 * This file is part of the LaMaCheck service.
 *
 * Proprietary and confidential.
 *
 * It can not be copied and/or distributed without the
 * express permission of Philip Helger.
 *
 * Unauthorized copying of this file, via any medium is
 * strictly prohibited.
 */
package com.helger.lama.updater.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.concurrent.BasicThreadFactory;
import com.helger.commons.concurrent.ManagedExecutorService;
import com.helger.lama.updater.domain.MavenArtifactInfo;
import com.helger.lama.updater.domain.MavenRepositoryInfo;

public class WorkQueue
{
  private final LinkedBlockingQueue <Runnable> m_aQueue;
  private final ExecutorService m_aExecService;
  private int m_nTaskIndex = 0;

  public WorkQueue ()
  {
    this (10 * Runtime.getRuntime ().availableProcessors ());
  }

  public WorkQueue (@Nonnegative final int nMaxThreads)
  {
    m_aQueue = new LinkedBlockingQueue<> ();
    m_aExecService = new ThreadPoolExecutor (nMaxThreads,
                                             nMaxThreads,
                                             0L,
                                             TimeUnit.SECONDS,
                                             m_aQueue,
                                             new BasicThreadFactory.Builder ().setNamingPattern ("lama-worker-%d")
                                                                              .setDaemon (true)
                                                                              .build ());
  }

  public void addTaskUpdateArtifact (@Nonnull final MavenArtifactInfo aArtifactInfo, @Nonnull final Runnable aRunnable)
  {
    ValueEnforcer.notNull (aArtifactInfo, "ArtifactInfo");

    addTask (new WorkQueueTask (EWorkQueueTaskType.UPDATE_ARTIFACT, aArtifactInfo.getID (), aRunnable));
  }

  public void addTaskNewArtifact (@Nonnull final MavenArtifactInfo aArtifactInfo, @Nonnull final Runnable aRunnable)
  {
    ValueEnforcer.notNull (aArtifactInfo, "ArtifactInfo");

    addTask (new WorkQueueTask (EWorkQueueTaskType.NEW_ARTIFACT, aArtifactInfo.getID (), aRunnable));
  }

  public void addTaskNewRepo (@Nonnull final MavenRepositoryInfo aRepoInfo, @Nonnull final Runnable aRunnable)
  {
    ValueEnforcer.notNull (aRepoInfo, "RepoInfo");

    addTask (new WorkQueueTask (EWorkQueueTaskType.NEW_REPO, aRepoInfo.getID (), aRunnable));
  }

  public void addTask (@Nonnull final WorkQueueTask aTask)
  {
    ValueEnforcer.notNull (aTask, "Task");

    m_aExecService.submit (aTask.setIndex (m_nTaskIndex++));
  }

  public void waitUntilDone ()
  {
    ManagedExecutorService.shutdownAndWaitUntilAllTasksAreFinished (m_aExecService);
  }
}
