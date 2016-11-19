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
package com.helger.lamaweb.app.ajax.view;

import java.util.Locale;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.debug.GlobalDebug;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.render.HCRenderer;
import com.helger.json.JsonObject;
import com.helger.lamaweb.app.CLamaSecurity;
import com.helger.photon.basic.app.request.ApplicationRequestManager;
import com.helger.photon.bootstrap3.alert.BootstrapErrorBox;
import com.helger.photon.core.EPhotonCoreText;
import com.helger.photon.core.ajax.executor.AbstractAjaxExecutor;
import com.helger.photon.core.ajax.response.AjaxJsonResponse;
import com.helger.photon.core.login.CLogin;
import com.helger.photon.security.login.ELoginResult;
import com.helger.photon.security.login.LoggedInUserManager;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;

public final class AjaxExecutorViewLogin extends AbstractAjaxExecutor
{
  public static final String JSON_LOGGEDIN = "loggedin";
  public static final String JSON_HTML = "html";
  private static final Logger s_aLogger = LoggerFactory.getLogger (AjaxExecutorViewLogin.class);

  @Override
  @Nonnull
  protected AjaxJsonResponse mainHandleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope) throws Exception
  {
    final String sLoginName = aRequestScope.getAttributeAsString (CLogin.REQUEST_ATTR_USERID);
    final String sPassword = aRequestScope.getAttributeAsString (CLogin.REQUEST_ATTR_PASSWORD);

    // Main login
    final ELoginResult eLoginResult = LoggedInUserManager.getInstance ().loginUser (sLoginName, sPassword, CLamaSecurity.REQUIRED_ROLE_IDS_VIEW);
    if (eLoginResult.isSuccess ())
      return AjaxJsonResponse.createSuccess (new JsonObject ().add (JSON_LOGGEDIN, true));

    // Get the rendered content of the menu area
    if (GlobalDebug.isDebugMode ())
      s_aLogger.warn ("Login of '" + sLoginName + "' failed because " + eLoginResult);

    final Locale aDisplayLocale = ApplicationRequestManager.getRequestMgr ().getRequestDisplayLocale ();
    final IHCNode aRoot = new BootstrapErrorBox ().addChild (EPhotonCoreText.LOGIN_ERROR_MSG.getDisplayText (aDisplayLocale) +
                                                             " " +
                                                             eLoginResult.getDisplayText (aDisplayLocale));

    // Set as result property
    return AjaxJsonResponse.createSuccess (new JsonObject ().add (JSON_LOGGEDIN, false).add (JSON_HTML,
                                                                                             HCRenderer.getAsHTMLStringWithoutNamespaces (aRoot)));
  }
}
