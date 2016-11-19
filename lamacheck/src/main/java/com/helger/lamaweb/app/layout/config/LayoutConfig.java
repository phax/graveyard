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
import com.helger.html.hc.html.grouping.HCDiv;
import com.helger.html.hc.html.metadata.HCHead;
import com.helger.html.hc.html.textlevel.HCSpan;
import com.helger.html.hc.impl.HCNodeList;
import com.helger.photon.bootstrap3.base.BootstrapContainer;
import com.helger.photon.bootstrap3.grid.BootstrapRow;
import com.helger.photon.core.app.context.LayoutExecutionContext;
import com.helger.photon.core.app.layout.CLayout;
import com.helger.photon.core.app.layout.ILayoutAreaContentProvider;
import com.helger.photon.core.app.layout.ILayoutManager;

/**
 * This class registers the renderer for the layout areas.
 *
 * @author Philip Helger
 */
public final class LayoutConfig
{
  /**
   * The header renderer.
   *
   * @author Philip Helger
   */
  private static final class AreaHeader implements ILayoutAreaContentProvider <LayoutExecutionContext>
  {
    public IHCNode getContent (@Nonnull final LayoutExecutionContext aLEC, @Nonnull final HCHead aHead)
    {
      return ConfigHeaderProvider.getContent (aLEC);
    }
  }

  /**
   * The navigation bar renderer.
   *
   * @author Philip Helger
   */
  private static final class AreaNavBar implements ILayoutAreaContentProvider <LayoutExecutionContext>
  {
    public IHCNode getContent (@Nonnull final LayoutExecutionContext aLEC, @Nonnull final HCHead aHead)
    {
      return ConfigNavbarProvider.getContent (aLEC);
    }
  }

  /**
   * The viewport renderer (menu + content area)
   *
   * @author Philip Helger
   */
  private static final class AreaViewPort implements ILayoutAreaContentProvider <LayoutExecutionContext>
  {
    @Nonnull
    public IHCNode getContent (@Nonnull final LayoutExecutionContext aLEC, @Nonnull final HCHead aHead)
    {
      final BootstrapRow aRow = new BootstrapRow ();
      // left
      final HCNodeList aLeft = new HCNodeList ();
      // We need a wrapper span for easy AJAX content replacement
      aLeft.addChild (new HCSpan ().addChild (ConfigMenuProvider.getContent (aLEC)).setID (CLayout.LAYOUT_AREAID_MENU));
      aLeft.addChild (new HCDiv ().setID (CLayout.LAYOUT_AREAID_SPECIAL));
      aRow.createColumn (12, 4, 3, 3).addChild (aLeft);

      // content
      aRow.createColumn (12, 8, 9, 9).addChild (ConfigContentProvider.getContent (aLEC));

      final BootstrapContainer aContentLayout = new BootstrapContainer ();
      aContentLayout.addChild (aRow);
      return aContentLayout;
    }
  }

  private LayoutConfig ()
  {}

  public static void init (@Nonnull final ILayoutManager <LayoutExecutionContext> aLayoutMgr)
  {
    // Register all layout area handler (order is important for SEO!)
    aLayoutMgr.registerAreaContentProvider (CLayout.LAYOUT_AREAID_HEADER, new AreaHeader ());
    aLayoutMgr.registerAreaContentProvider (CLayout.LAYOUT_AREAID_NAVBAR, new AreaNavBar ());
    aLayoutMgr.registerAreaContentProvider (CLayout.LAYOUT_AREAID_VIEWPORT, new AreaViewPort ());
  }
}
