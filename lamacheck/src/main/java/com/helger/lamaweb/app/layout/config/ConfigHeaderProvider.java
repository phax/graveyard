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
package com.helger.lamaweb.app.layout.config;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.helger.commons.url.ISimpleURL;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.html.textlevel.HCA;
import com.helger.html.hc.html.textlevel.HCSpan;
import com.helger.html.hc.html.textlevel.HCStrong;
import com.helger.html.hc.impl.HCNodeList;
import com.helger.lamaweb.ui.CLamaCSS;
import com.helger.photon.bootstrap3.CBootstrapCSS;
import com.helger.photon.bootstrap3.nav.BootstrapNav;
import com.helger.photon.bootstrap3.navbar.BootstrapNavbar;
import com.helger.photon.bootstrap3.navbar.EBootstrapNavbarPosition;
import com.helger.photon.bootstrap3.navbar.EBootstrapNavbarType;
import com.helger.photon.core.EPhotonCoreText;
import com.helger.photon.core.app.context.LayoutExecutionContext;
import com.helger.photon.core.servlet.LogoutServlet;
import com.helger.photon.core.url.LinkHelper;
import com.helger.photon.security.login.LoggedInUserManager;
import com.helger.photon.security.util.SecurityHelper;

/**
 * The content provider for the header area.
 *
 * @author Philip Helger
 */
public final class ConfigHeaderProvider
{
  private ConfigHeaderProvider ()
  {}

  @Nonnull
  public static IHCNode getContent (@Nonnull final LayoutExecutionContext aLEC)
  {
    final Locale aDisplayLocale = aLEC.getDisplayLocale ();
    final ISimpleURL aLinkToStartPage = aLEC.getLinkToMenuItem (aLEC.getMenuTree ().getDefaultMenuItemID ());

    final BootstrapNavbar aNavbar = new BootstrapNavbar (EBootstrapNavbarType.STATIC_TOP, true, aDisplayLocale);
    aNavbar.addBrand (new HCNodeList ().addChildren (new HCSpan ().addChild ("LaMaCheck").addClass (CLamaCSS.CSS_CLASS_LOGO1),
                                                     new HCSpan ().addChild (" Administration").addClass (CLamaCSS.CSS_CLASS_LOGO2)),
                      aLinkToStartPage);

    final BootstrapNav aNav = new BootstrapNav ();
    aNav.addItem (new HCSpan ().addChild ("Logged in as ")
                               .addClass (CBootstrapCSS.NAVBAR_TEXT)
                               .addChild (new HCStrong ().addChild (SecurityHelper.getUserDisplayName (LoggedInUserManager.getInstance ()
                                                                                                                          .getCurrentUser (),
                                                                                                       aDisplayLocale))));
    aNav.addItem (new HCA (LinkHelper.getURLWithContext (aLEC.getRequestScope (),
                                                         LogoutServlet.SERVLET_DEFAULT_PATH)).addChild (EPhotonCoreText.LOGIN_LOGOUT.getDisplayText (aDisplayLocale)));
    aNavbar.addNav (EBootstrapNavbarPosition.COLLAPSIBLE_RIGHT, aNav);
    return aNavbar;
  }
}
