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
package com.helger.lamaweb.app.menu.config;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.lamaweb.app.CLama;
import com.helger.lamaweb.app.CLamaSecurity;
import com.helger.lamaweb.page.config.PageSavedStates;
import com.helger.photon.basic.app.menu.IMenuItemPage;
import com.helger.photon.basic.app.menu.IMenuObjectFilter;
import com.helger.photon.basic.app.menu.IMenuTree;
import com.helger.photon.bootstrap3.pages.BootstrapPagesMenuConfigurator;
import com.helger.photon.core.form.FormStateManager;
import com.helger.photon.security.menu.MenuObjectFilterUserAssignedToUserGroup;
import com.helger.photon.uicore.page.WebPageExecutionContext;
import com.helger.photon.uicore.page.system.BasePageShowChildren;

@Immutable
public final class MenuConfig
{
  private MenuConfig ()
  {}

  public static void init (@Nonnull final IMenuTree aMenuTree)
  {
    // We need this additional indirection layer, as the pages are initialized
    // statically!
    final MenuObjectFilterUserAssignedToUserGroup aFilterSuperUser = new MenuObjectFilterUserAssignedToUserGroup (CLamaSecurity.USERGROUPID_SUPERUSER);
    final IMenuObjectFilter aFilterSavedStates = aValue -> FormStateManager.getInstance ().containedOnceAFormState ();

    // Administrator
    {
      final IMenuItemPage aAdmin = aMenuTree.createRootItem (new BasePageShowChildren <WebPageExecutionContext> (CLamaMenuConfig.MENU_ADMIN,
                                                                                                                 "Administration",
                                                                                                                 aMenuTree))
                                            .setDisplayFilter (aFilterSuperUser);

      BootstrapPagesMenuConfigurator.addAllItems (aMenuTree, aAdmin, aFilterSuperUser, CLama.DEFAULT_LOCALE);
    }

    // Saved states
    aMenuTree.createRootSeparator ().setDisplayFilter (aFilterSavedStates);
    aMenuTree.createRootItem (new PageSavedStates (CLamaMenuConfig.MENU_SAVED_STATES, "Saved objects"))
             .setDisplayFilter (aFilterSavedStates);

    // Default menu item
    aMenuTree.setDefaultMenuItemID (CLamaMenuConfig.MENU_ADMIN);
  }
}
