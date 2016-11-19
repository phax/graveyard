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

import com.helger.html.hc.html.IHCElement;
import com.helger.photon.bootstrap3.CBootstrapCSS;
import com.helger.photon.bootstrap3.uictrls.ext.BootstrapMenuItemRenderer;
import com.helger.photon.core.app.context.LayoutExecutionContext;

/**
 * The content provider for the menu area.
 *
 * @author Philip Helger
 */
public final class ConfigMenuProvider
{
  private ConfigMenuProvider ()
  {}

  @Nonnull
  public static IHCElement <?> getContent (@Nonnull final LayoutExecutionContext aLEC)
  {
    final IHCElement <?> ret = BootstrapMenuItemRenderer.createSideBarMenu (aLEC);
    if (false)
      ret.addClass (CBootstrapCSS.AFFIX);
    return ret;
  }
}
