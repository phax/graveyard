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
package com.helger.lamaweb.app.init;

import javax.annotation.Nonnull;

import com.helger.lamaweb.app.CLama;
import com.helger.lamaweb.app.ajax.config.CLamaAjaxConfig;
import com.helger.lamaweb.app.layout.config.LayoutConfig;
import com.helger.lamaweb.app.menu.config.MenuConfig;
import com.helger.photon.basic.app.locale.ILocaleManager;
import com.helger.photon.basic.app.menu.IMenuTree;
import com.helger.photon.core.ajax.IAjaxInvoker;
import com.helger.photon.core.app.context.LayoutExecutionContext;
import com.helger.photon.core.app.init.IApplicationInitializer;
import com.helger.photon.core.app.layout.ILayoutManager;

public class InitializerConfig implements IApplicationInitializer <LayoutExecutionContext>
{
  @Override
  public void initLocales (@Nonnull final ILocaleManager aLocaleMgr)
  {
    aLocaleMgr.registerLocale (CLama.DEFAULT_LOCALE);
    aLocaleMgr.setDefaultLocale (CLama.DEFAULT_LOCALE);
  }

  @Override
  public void initLayout (@Nonnull final ILayoutManager <LayoutExecutionContext> aLayoutMgr)
  {
    LayoutConfig.init (aLayoutMgr);
  }

  @Override
  public void initMenu (@Nonnull final IMenuTree aMenuTree)
  {
    MenuConfig.init (aMenuTree);
  }

  @Override
  public void initAjax (@Nonnull final IAjaxInvoker aAjaxInvoker)
  {
    aAjaxInvoker.registerFunction (CLamaAjaxConfig.CONFIG_SAVE_FORM_STATE);
    aAjaxInvoker.registerFunction (CLamaAjaxConfig.CONFIG_UPDATE_MENU_VIEW);
  }

  @Override
  public void initRest ()
  {}
}