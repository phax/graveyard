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
package com.helger.lama.updater.main;

import java.io.File;

import com.helger.commons.callback.IThrowingRunnable;
import com.helger.network.proxy.EHttpProxyType;
import com.helger.network.proxy.HttpProxyConfig;
import com.helger.network.proxy.UseSystemProxyConfig;
import com.helger.photon.basic.app.io.WebFileIO;
import com.helger.photon.security.mgr.PhotonSecurityManager;
import com.helger.web.mock.MockHttpServletRequest;
import com.helger.web.mock.MockHttpServletResponse;
import com.helger.web.mock.MockServletContext;
import com.helger.web.scope.mgr.WebScopeManager;

public final class LamaRunner
{
  public static <EXTYPE extends Throwable> void run (final IThrowingRunnable <EXTYPE> r) throws EXTYPE
  {
    if (false)
    {
      new HttpProxyConfig (EHttpProxyType.HTTP, "172.30.9.12", 8080).activateGlobally ();
      new HttpProxyConfig (EHttpProxyType.HTTPS, "172.30.9.12", 8080).activateGlobally ();
    }
    else
      new UseSystemProxyConfig ().activateGlobally ();
    final File aBasePath = new File (new File ("").getAbsoluteFile ().getParentFile (), "lamadata");
    WebFileIO.initPaths (aBasePath, aBasePath, false);
    final MockServletContext aSC = MockServletContext.create ();
    WebScopeManager.onRequestBegin ("lama", new MockHttpServletRequest (aSC), new MockHttpServletResponse ());
    PhotonSecurityManager.getAuditMgr ();

    try
    {
      r.run ();
    }
    finally
    {
      WebScopeManager.onRequestEnd ();
      aSC.invalidate ();
    }
  }
}
