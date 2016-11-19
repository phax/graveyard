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

import javax.annotation.Nonnull;

import com.helger.html.hc.IHCNode;
import com.helger.html.hc.html.IHCElement;
import com.helger.lamaweb.app.menu.view.CLamaMenuView;
import com.helger.photon.basic.app.menu.IMenuObject;
import com.helger.photon.basic.app.menu.IMenuTree;
import com.helger.photon.basic.app.menu.MenuItemDeterminatorCallback;
import com.helger.photon.bootstrap3.uictrls.ext.BootstrapMenuItemRenderer;
import com.helger.photon.core.app.context.LayoutExecutionContext;

/**
 * The content provider for the menu area.
 *
 * @author Philip Helger
 */
public final class ViewMenuProvider
{
  private ViewMenuProvider ()
  {}

  @Nonnull
  public static IHCNode getContent (@Nonnull final LayoutExecutionContext aLEC)
  {
    // Main menu
    final IMenuTree aMenuTree = aLEC.getMenuTree ();
    final MenuItemDeterminatorCallback aCallback = new MenuItemDeterminatorCallback (aMenuTree, aLEC.getSelectedMenuItemID ())
    {
      @Override
      protected boolean isMenuItemValidToBeDisplayed (@Nonnull final IMenuObject aMenuObj)
      {
        // Don't show items that belong to the footer
        if (aMenuObj.containsAttribute (CLamaMenuView.FLAG_FOOTER))
          return false;

        // Use default code
        return super.isMenuItemValidToBeDisplayed (aMenuObj);
      }
    };
    final IHCElement <?> aMenu = BootstrapMenuItemRenderer.createSideBarMenu (aLEC, aCallback);

    return aMenu;
  }
}
