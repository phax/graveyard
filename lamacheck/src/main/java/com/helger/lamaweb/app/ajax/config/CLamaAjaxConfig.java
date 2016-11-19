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

import javax.annotation.concurrent.Immutable;

import com.helger.photon.core.ajax.IAjaxFunctionDeclaration;
import com.helger.photon.core.ajax.decl.SecureApplicationAjaxFunctionDeclaration;
import com.helger.photon.uicore.form.ajax.AjaxExecutorSaveFormState;

/**
 * This class defines the available ajax functions
 *
 * @author Philip Helger
 */
@Immutable
public final class CLamaAjaxConfig
{
  public static final IAjaxFunctionDeclaration CONFIG_SAVE_FORM_STATE = new SecureApplicationAjaxFunctionDeclaration ("saveFormState",
                                                                                                                      AjaxExecutorSaveFormState.class);
  public static final IAjaxFunctionDeclaration CONFIG_UPDATE_MENU_VIEW = new SecureApplicationAjaxFunctionDeclaration ("updateMenuView",
                                                                                                                       AjaxExecutorConfigUpdateMenuView.class);

  private CLamaAjaxConfig ()
  {}
}
