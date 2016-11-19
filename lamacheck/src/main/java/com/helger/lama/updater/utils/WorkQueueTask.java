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

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.string.StringHelper;
import com.helger.web.scope.util.AbstractWebScopeAwareRunnable;

public final class WorkQueueTask extends AbstractWebScopeAwareRunnable
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (WorkQueueTask.class);

  private final EWorkQueueTaskType m_eTaskType;
  private final String m_sTaskID;
  private final Runnable m_aRunnable;
  private int m_nIndex;

  public WorkQueueTask (@Nonnull final EWorkQueueTaskType eTaskType, @Nonnull @Nonempty final String sTaskID, @Nonnull final Runnable aRunnable)
  {
    if (eTaskType == null)
      throw new NullPointerException ("TaskType");
    if (StringHelper.hasNoText (sTaskID))
      throw new IllegalArgumentException ("TaskID");
    if (aRunnable == null)
      throw new NullPointerException ("Runnable");
    m_eTaskType = eTaskType;
    m_sTaskID = sTaskID;
    m_aRunnable = aRunnable;
  }

  @Nonnull
  public WorkQueueTask setIndex (final int nIndex)
  {
    m_nIndex = nIndex;
    return this;
  }

  @Override
  protected void scopedRun ()
  {
    try
    {
      m_aRunnable.run ();
    }
    catch (final Throwable t)
    {
      s_aLogger.error ("Error executing workqueue task[" + m_nIndex + "] " + m_eTaskType.getID () + " " + m_sTaskID, t);
    }
  }
}
