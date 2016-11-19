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
package com.helger.lama.updater.http;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.HttpClientConnectionManager;

import com.helger.commons.CGlobal;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class IdleConnectionMonitorThread extends Thread
{
  private final HttpClientConnectionManager m_aConnMgr;
  private volatile boolean m_bShutdown = false;

  public IdleConnectionMonitorThread (final HttpClientConnectionManager aConnMgr)
  {
    super ("IdleConnectionMonitorThread");
    m_aConnMgr = aConnMgr;
  }

  @Override
  public void run ()
  {
    try
    {
      while (!m_bShutdown)
      {
        synchronized (this)
        {
          wait (5 * CGlobal.MILLISECONDS_PER_SECOND);
          // Close expired connections
          m_aConnMgr.closeExpiredConnections ();
          // Optionally, close connections
          // that have been idle longer than 30 sec
          m_aConnMgr.closeIdleConnections (30, TimeUnit.SECONDS);
        }
      }
    }
    catch (final InterruptedException ex)
    {
      // terminate
    }
  }

  @SuppressFBWarnings ("NN_NAKED_NOTIFY")
  public void shutdown ()
  {
    m_bShutdown = true;
    synchronized (this)
    {
      notifyAll ();
    }
  }
}
