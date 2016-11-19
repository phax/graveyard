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
package com.helger.lamaweb.app.menu.view;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.io.resource.ClassPathResource;
import com.helger.lamaweb.page.view.PageViewArtifactsAll;
import com.helger.lamaweb.page.view.PageViewArtifactsPlugins;
import com.helger.lamaweb.page.view.PageViewLogin;
import com.helger.lamaweb.page.view.PageViewRepositoriesAll;
import com.helger.lamaweb.page.view.PageViewRepositoriesEmpty;
import com.helger.lamaweb.page.view.PageViewRepositoriesInvalid;
import com.helger.photon.basic.app.menu.IMenuItemPage;
import com.helger.photon.basic.app.menu.IMenuTree;
import com.helger.photon.security.menu.MenuObjectFilterNoUserLoggedIn;
import com.helger.photon.uicore.page.WebPageExecutionContext;
import com.helger.photon.uicore.page.external.BasePageViewExternal;

@Immutable
public final class MenuView
{
  private MenuView ()
  {}

  public static void init (@Nonnull final IMenuTree aMenuTree)
  {
    final MenuObjectFilterNoUserLoggedIn aFilterNoUserLoggedIn = new MenuObjectFilterNoUserLoggedIn ();

    // Not logged in
    aMenuTree.createRootItem (new PageViewLogin (CLamaMenuView.MENU_LOGIN)).setDisplayFilter (aFilterNoUserLoggedIn);
    aMenuTree.createRootSeparator ().setDisplayFilter (aFilterNoUserLoggedIn);

    // Common stuff
    final IMenuItemPage aArtifacts = aMenuTree.createRootItem (new PageViewArtifactsAll (CLamaMenuView.MENU_ARTIFACTS_ALL));
    aMenuTree.createItem (aArtifacts, new PageViewArtifactsPlugins (CLamaMenuView.MENU_ARTIFACTS_PLUGINS));
    final IMenuItemPage aRepos = aMenuTree.createRootItem (new PageViewRepositoriesAll (CLamaMenuView.MENU_REPOSITORIES_ALL));
    aMenuTree.createItem (aRepos, new PageViewRepositoriesEmpty (CLamaMenuView.MENU_REPOSITORIES_EMPTY));
    aMenuTree.createItem (aRepos, new PageViewRepositoriesInvalid (CLamaMenuView.MENU_REPOSITORIES_INVALID));

    aMenuTree.createRootSeparator ();
    aMenuTree.createRootItem (new BasePageViewExternal <WebPageExecutionContext> (CLamaMenuView.MENU_SITENOTICE,
                                                                                  "Site notice",
                                                                                  new ClassPathResource ("viewpages/en/site-notice.xml")));

    aMenuTree.createRootItem (new BasePageViewExternal <WebPageExecutionContext> (CLamaMenuView.MENU_GTC,
                                                                                  "GTC",
                                                                                  new ClassPathResource ("viewpages/en/gtc.xml")))
             .setAttribute (CLamaMenuView.FLAG_FOOTER, Boolean.TRUE);

    // Set default
    aMenuTree.setDefaultMenuItemID (CLamaMenuView.MENU_ARTIFACTS_ALL);
  }
}
