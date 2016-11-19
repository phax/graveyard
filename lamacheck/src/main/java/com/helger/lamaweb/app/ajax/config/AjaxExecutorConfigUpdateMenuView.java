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
package com.helger.lamaweb.app.ajax.config;

import javax.annotation.Nonnull;

import com.helger.html.hc.IHCNode;
import com.helger.lamaweb.app.layout.config.ConfigMenuProvider;
import com.helger.photon.core.ajax.executor.AbstractAjaxExecutor;
import com.helger.photon.core.ajax.response.AjaxHtmlResponse;
import com.helger.photon.core.app.context.LayoutExecutionContext;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;

public final class AjaxExecutorConfigUpdateMenuView extends AbstractAjaxExecutor
{
  @Override
  @Nonnull
  protected AjaxHtmlResponse mainHandleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope) throws Exception
  {
    final LayoutExecutionContext aLEC = LayoutExecutionContext.createForAjaxOrAction (aRequestScope);

    // Get the rendered content of the menu area
    final IHCNode aRoot = ConfigMenuProvider.getContent (aLEC);

    // Set as result property
    return AjaxHtmlResponse.createSuccess (aRequestScope, aRoot);
  }
}
