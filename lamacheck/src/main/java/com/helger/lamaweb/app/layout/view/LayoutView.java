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

import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.html.css.DefaultCSSClassProvider;
import com.helger.html.css.ICSSClassProvider;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.html.grouping.HCDiv;
import com.helger.html.hc.html.grouping.HCP;
import com.helger.html.hc.html.grouping.HCUL;
import com.helger.html.hc.html.metadata.HCHead;
import com.helger.html.hc.html.textlevel.HCSpan;
import com.helger.html.hc.impl.HCNodeList;
import com.helger.lamaweb.app.menu.view.CLamaMenuView;
import com.helger.photon.basic.app.menu.ApplicationMenuTree;
import com.helger.photon.basic.app.menu.IMenuItemExternal;
import com.helger.photon.basic.app.menu.IMenuItemPage;
import com.helger.photon.basic.app.menu.IMenuObject;
import com.helger.photon.basic.app.menu.IMenuSeparator;
import com.helger.photon.bootstrap3.CBootstrapCSS;
import com.helger.photon.bootstrap3.base.BootstrapContainer;
import com.helger.photon.bootstrap3.grid.BootstrapRow;
import com.helger.photon.bootstrap3.uictrls.ext.BootstrapMenuItemRendererHorz;
import com.helger.photon.core.app.context.LayoutExecutionContext;
import com.helger.photon.core.app.layout.CLayout;
import com.helger.photon.core.app.layout.ILayoutAreaContentProvider;
import com.helger.photon.core.app.layout.ILayoutManager;

/**
 * This class registers the renderer for the layout areas.
 *
 * @author Philip Helger
 */
public final class LayoutView
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
      return ViewHeaderProvider.getContent (aLEC);
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
      return ViewNavbarProvider.getContent (aLEC);
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
      aLeft.addChild (new HCSpan ().addChild (ViewMenuProvider.getContent (aLEC)).setID (CLayout.LAYOUT_AREAID_MENU));
      aLeft.addChild (new HCDiv ().setID (CLayout.LAYOUT_AREAID_SPECIAL));
      aRow.createColumn (12, 4, 3, 3).addChild (aLeft);

      // content
      aRow.createColumn (12, 8, 9, 9).addChild (ViewContentProvider.getContent (aLEC));

      final BootstrapContainer aContentLayout = new BootstrapContainer ();
      aContentLayout.addChild (aRow);
      return aContentLayout;
    }
  }

  private static final class AreaFooter implements ILayoutAreaContentProvider <LayoutExecutionContext>
  {
    private static final ICSSClassProvider CSS_CLASS_FOOTER_LINKS = DefaultCSSClassProvider.create ("footer-links");

    private ICommonsList <IMenuObject> m_aFooterObjects;

    /**
     * Retrieve and cache all menu objects to be displayed in the footer. It can
     * be cached, as the menu cannot be modified at runtime.
     *
     * @return Never <code>null</code>
     */
    @Nonnull
    private List <IMenuObject> _getFooterObjects ()
    {
      if (m_aFooterObjects == null)
      {
        m_aFooterObjects = new CommonsArrayList<> ();
        ApplicationMenuTree.getTree ().iterateAllMenuObjects (aCurrentObject -> {
          if (aCurrentObject.containsAttribute (CLamaMenuView.FLAG_FOOTER))
            m_aFooterObjects.add (aCurrentObject);
        });
      }
      return m_aFooterObjects;
    }

    @Nonnull
    public IHCNode getContent (@Nonnull final LayoutExecutionContext aLEC, @Nonnull final HCHead aHead)
    {
      final HCDiv aDiv = new HCDiv ().addClass (CBootstrapCSS.CONTAINER);

      aDiv.addChild (new HCP ().addChild ("LaMaCheck - Latest Maven artifact check"));
      aDiv.addChild (new HCP ().addChild ("Copyright 2013-2015 P. Helger, philip[at]helger[dot]com"));

      final BootstrapMenuItemRendererHorz aRenderer = new BootstrapMenuItemRendererHorz (aLEC.getDisplayLocale ());
      final HCUL aUL = aDiv.addAndReturnChild (new HCUL ().addClass (CSS_CLASS_FOOTER_LINKS));
      for (final IMenuObject aMenuObj : _getFooterObjects ())
      {
        if (aMenuObj instanceof IMenuSeparator)
          aUL.addItem (aRenderer.renderSeparator (aLEC, (IMenuSeparator) aMenuObj));
        else
          if (aMenuObj instanceof IMenuItemPage)
            aUL.addItem (aRenderer.renderMenuItemPage (aLEC, (IMenuItemPage) aMenuObj, false, false, false));
          else
            if (aMenuObj instanceof IMenuItemExternal)
              aUL.addItem (aRenderer.renderMenuItemExternal (aLEC, (IMenuItemExternal) aMenuObj, false, false, false));
            else
              throw new IllegalStateException ("Unsupported menu object type!");
      }

      return aDiv;
    }
  }

  private LayoutView ()
  {}

  public static void init (@Nonnull final ILayoutManager <LayoutExecutionContext> aLayoutMgr)
  {
    // Register all layout area handler (order is important for SEO!)
    aLayoutMgr.registerAreaContentProvider (CLayout.LAYOUT_AREAID_HEADER, new AreaHeader ());
    aLayoutMgr.registerAreaContentProvider (CLayout.LAYOUT_AREAID_NAVBAR, new AreaNavBar ());
    aLayoutMgr.registerAreaContentProvider (CLayout.LAYOUT_AREAID_VIEWPORT, new AreaViewPort ());
    aLayoutMgr.registerAreaContentProvider (CLayout.LAYOUT_AREAID_FOOTER, new AreaFooter ());
  }
}
