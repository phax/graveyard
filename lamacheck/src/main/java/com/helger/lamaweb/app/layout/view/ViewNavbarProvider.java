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

import com.helger.photon.bootstrap3.base.BootstrapContainer;
import com.helger.photon.bootstrap3.breadcrumbs.BootstrapBreadcrumbs;
import com.helger.photon.bootstrap3.breadcrumbs.BootstrapBreadcrumbsProvider;
import com.helger.photon.bootstrap3.grid.BootstrapRow;
import com.helger.photon.core.app.context.LayoutExecutionContext;

/**
 * The content provider for the navigation bar area.
 *
 * @author Philip Helger
 */
final class ViewNavbarProvider
{
  private ViewNavbarProvider ()
  {}

  @Nonnull
  public static BootstrapContainer getContent (@Nonnull final LayoutExecutionContext aLEC)
  {
    final BootstrapBreadcrumbs aBreadcrumb = BootstrapBreadcrumbsProvider.createBreadcrumbs (aLEC);

    final BootstrapContainer aCont = new BootstrapContainer ();
    final BootstrapRow aRow = new BootstrapRow ();
    aRow.createColumn (12).addChild (aBreadcrumb);
    aCont.addChild (aRow);
    return aCont;
  }
}
