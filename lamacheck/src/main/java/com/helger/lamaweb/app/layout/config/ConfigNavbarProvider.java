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

import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.photon.basic.app.menu.IMenuItem;
import com.helger.photon.basic.app.menu.IMenuObject;
import com.helger.photon.bootstrap3.base.BootstrapContainer;
import com.helger.photon.bootstrap3.breadcrumbs.BootstrapBreadcrumbs;
import com.helger.photon.bootstrap3.grid.BootstrapRow;
import com.helger.photon.core.app.context.LayoutExecutionContext;
import com.helger.tree.withid.DefaultTreeItemWithID;

/**
 * The content provider for the navigation bar area.
 *
 * @author Philip Helger
 */
public final class ConfigNavbarProvider
{
  private ConfigNavbarProvider ()
  {}

  private static void _getBreadCrumbs (final BootstrapBreadcrumbs aBreadcrumb, final LayoutExecutionContext aLEC)
  {
    final Locale aDisplayLocale = aLEC.getDisplayLocale ();
    final ICommonsList <IMenuItem> aItems = new CommonsArrayList<> ();
    IMenuItem aCurrent = aLEC.getSelectedMenuItem ();
    while (aCurrent != null)
    {
      aItems.add (0, aCurrent);
      final DefaultTreeItemWithID <String, IMenuObject> aTreeItem = aLEC.getMenuTree ()
                                                                        .getItemWithID (aCurrent.getID ());
      aCurrent = aTreeItem.isRootItem () ? null : (IMenuItem) aTreeItem.getParent ().getData ();
    }

    final int nItems = aItems.size ();
    if (nItems > 0)
    {
      for (int i = 0; i < nItems; ++i)
      {
        final IMenuItem aItem = aItems.get (i);

        // Create link on all but the last item
        if (i < nItems - 1)
          aBreadcrumb.addLink (aLEC.getLinkToMenuItem (aItem.getID ()), aItem.getDisplayText (aDisplayLocale));
        else
          aBreadcrumb.addActive (aItem.getDisplayText (aDisplayLocale));
      }
    }
  }

  @Nonnull
  public static BootstrapContainer getContent (final LayoutExecutionContext aLEC)
  {
    final BootstrapBreadcrumbs aBreadcrumb = new BootstrapBreadcrumbs ();
    _getBreadCrumbs (aBreadcrumb, aLEC);

    final BootstrapContainer aCont = new BootstrapContainer ();
    final BootstrapRow aRow = new BootstrapRow ();
    aRow.createColumn (12).addChild (aBreadcrumb);
    aCont.addChild (aRow);
    return aCont;
  }
}
