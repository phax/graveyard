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

import javax.annotation.Nonnull;

import com.helger.html.hc.IHCNode;
import com.helger.html.hc.impl.HCNodeList;
import com.helger.photon.basic.app.menu.ApplicationMenuTree;
import com.helger.photon.basic.app.menu.IMenuItemPage;
import com.helger.photon.basic.app.request.ApplicationRequestManager;
import com.helger.photon.basic.app.request.IRequestManager;
import com.helger.photon.bootstrap3.pages.BootstrapWebPageUIHandler;
import com.helger.photon.core.app.context.LayoutExecutionContext;
import com.helger.photon.uicore.page.IWebPage;
import com.helger.photon.uicore.page.WebPageExecutionContext;

/**
 * The content provider for the content area.
 *
 * @author Philip Helger
 */
public final class ConfigContentProvider
{
  private ConfigContentProvider ()
  {}

  @SuppressWarnings ("unchecked")
  @Nonnull
  public static IHCNode getContent (@Nonnull final LayoutExecutionContext aLEC)
  {
    final IRequestManager aRequestMgr = ApplicationRequestManager.getRequestMgr ();

    // Get the requested menu item
    final IMenuItemPage aSelectedMenuItem = aRequestMgr.getRequestMenuItem ();

    // Resolve the page of the selected menu item (if found)
    IWebPage <WebPageExecutionContext> aDisplayPage;
    // Only if we have display rights!
    if (aSelectedMenuItem.matchesDisplayFilter ())
      aDisplayPage = (IWebPage <WebPageExecutionContext>) aSelectedMenuItem.getPage ();
    else
    {
      // No rights -> goto start page
      aDisplayPage = (IWebPage <WebPageExecutionContext>) ApplicationMenuTree.getTree ()
                                                                             .getDefaultMenuItem ()
                                                                             .getPage ();
    }

    final WebPageExecutionContext aWPEC = new WebPageExecutionContext (aLEC, aDisplayPage);

    // Build page content: header + content
    final HCNodeList aPageContainer = new HCNodeList ();
    final String sHeaderText = aDisplayPage.getHeaderText (aWPEC);
    aPageContainer.addChild (BootstrapWebPageUIHandler.INSTANCE.createPageHeader (sHeaderText));
    // Main fill content
    aDisplayPage.getContent (aWPEC);
    // Add result
    aPageContainer.addChild (aWPEC.getNodeList ());
    return aPageContainer;
  }
}
