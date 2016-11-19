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
package com.helger.lamaweb.app.layout.view;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.helger.commons.url.ISimpleURL;
import com.helger.css.property.CCSSProperties;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.html.grouping.HCDiv;
import com.helger.html.hc.html.textlevel.HCA;
import com.helger.html.hc.html.textlevel.HCSpan;
import com.helger.html.hc.html.textlevel.HCStrong;
import com.helger.lamaweb.ui.CLamaCSS;
import com.helger.lamaweb.ui.LamaAccessUI;
import com.helger.photon.bootstrap3.CBootstrapCSS;
import com.helger.photon.bootstrap3.dropdown.BootstrapDropdownMenu;
import com.helger.photon.bootstrap3.nav.BootstrapNav;
import com.helger.photon.bootstrap3.navbar.BootstrapNavbar;
import com.helger.photon.bootstrap3.navbar.EBootstrapNavbarPosition;
import com.helger.photon.bootstrap3.navbar.EBootstrapNavbarType;
import com.helger.photon.core.EPhotonCoreText;
import com.helger.photon.core.app.context.LayoutExecutionContext;
import com.helger.photon.core.servlet.LogoutServlet;
import com.helger.photon.core.url.LinkHelper;
import com.helger.photon.security.login.LoggedInUserManager;
import com.helger.photon.security.user.IUser;
import com.helger.photon.security.util.SecurityHelper;

/**
 * The content provider for the header area.
 *
 * @author Philip Helger
 */
final class ViewHeaderProvider
{
  private ViewHeaderProvider ()
  {}

  private static void _addLoginLogout (@Nonnull final BootstrapNavbar aNavbar, @Nonnull final LayoutExecutionContext aLEC)
  {
    final Locale aDisplayLocale = aLEC.getDisplayLocale ();
    final IUser aUser = LoggedInUserManager.getInstance ().getCurrentUser ();
    if (aUser != null)
    {
      final BootstrapNav aNav = new BootstrapNav ();
      aNav.addItem (new HCSpan ().addChild ("Logged in as ")
                                 .addClass (CBootstrapCSS.NAVBAR_TEXT)
                                 .addChild (new HCStrong ().addChild (SecurityHelper.getUserDisplayName (LoggedInUserManager.getInstance ()
                                                                                                                            .getCurrentUser (),
                                                                                                         aDisplayLocale))));
      aNav.addItem (new HCA (LinkHelper.getURLWithContext (aLEC.getRequestScope (),
                                                           LogoutServlet.SERVLET_DEFAULT_PATH)).addChild (EPhotonCoreText.LOGIN_LOGOUT.getDisplayText (aDisplayLocale)));
      aNavbar.addNav (EBootstrapNavbarPosition.COLLAPSIBLE_RIGHT, aNav);
    }
    else
    {
      final BootstrapNav aNav = new BootstrapNav ();
      final BootstrapDropdownMenu aDropDown = aNav.addDropdownMenu ("Login");
      {
        final HCDiv aDiv = new HCDiv ().addClass (CBootstrapCSS.DROPDOWN_MENU).addStyle (CCSSProperties.PADDING.newValue ("15px"));
        aDiv.addChild (LamaAccessUI.createViewLoginForm (aLEC, null, false).addClass (CBootstrapCSS.NAVBAR_FORM));
        aDropDown.addItem (aDiv);
      }
      aNavbar.addNav (EBootstrapNavbarPosition.COLLAPSIBLE_RIGHT, aNav);
    }
  }

  @Nonnull
  public static IHCNode getContent (@Nonnull final LayoutExecutionContext aLEC)
  {
    final ISimpleURL aLinkToStartPage = aLEC.getLinkToMenuItem (aLEC.getMenuTree ().getDefaultMenuItemID ());

    final BootstrapNavbar aNavbar = new BootstrapNavbar (EBootstrapNavbarType.STATIC_TOP, true, aLEC.getDisplayLocale ());
    aNavbar.addBrand (new HCSpan ().addChild ("LaMaCheck").addClass (CLamaCSS.CSS_CLASS_LOGO1), aLinkToStartPage);

    _addLoginLogout (aNavbar, aLEC);
    return aNavbar;
  }
}
