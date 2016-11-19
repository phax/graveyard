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
package com.helger.lamaweb.servlet;

import javax.annotation.Nonnull;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.debug.GlobalDebug;
import com.helger.commons.state.EContinue;
import com.helger.lamaweb.app.CLamaSecurity;
import com.helger.photon.core.app.CApplication;
import com.helger.photon.core.login.AbstractLoginManager;
import com.helger.photon.core.servlet.AbstractUnifiedResponseFilter;
import com.helger.photon.security.login.LoggedInUserManager;
import com.helger.photon.security.util.SecurityHelper;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;
import com.helger.web.servlet.response.UnifiedResponse;

public final class LamaConfigLoginFilter extends AbstractUnifiedResponseFilter
{
  private AbstractLoginManager m_aLogin;

  @Override
  @Nonnull
  @Nonempty
  protected String getApplicationID (@Nonnull final FilterConfig aFilterConfig)
  {
    return CApplication.APP_ID_SECURE;
  }

  @Override
  protected void onInit (@Nonnull final FilterConfig aFilterConfig) throws ServletException
  {
    // Make the application login configurable if you like
    m_aLogin = new LamaLoginManager ();
  }

  @Override
  @Nonnull
  protected EContinue handleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                     @Nonnull final UnifiedResponse aUnifiedResponse) throws ServletException
  {
    // FIXME always login default user
    if (GlobalDebug.isDebugMode ())
    {
      final LoggedInUserManager aLIUM = LoggedInUserManager.getInstance ();
      if (!aLIUM.isUserLoggedInInCurrentSession ())
      {
        aLIUM.loginUser ("admin@phloc.com", "password").isSuccess ();
      }
    }

    if (m_aLogin.checkUserAndShowLogin (aRequestScope, aUnifiedResponse).isBreak ())
    {
      // Show login screen
      return EContinue.BREAK;
    }

    // Check if the currently logged in user has the required roles
    final String sCurrentUserID = LoggedInUserManager.getInstance ().getCurrentUserID ();
    if (!SecurityHelper.hasUserAllRoles (sCurrentUserID, CLamaSecurity.REQUIRED_ROLE_IDS_CONFIG))
    {
      aUnifiedResponse.setStatus (HttpServletResponse.SC_FORBIDDEN);
      return EContinue.BREAK;
    }

    return EContinue.CONTINUE;
  }
}
