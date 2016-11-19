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
package com.helger.lamaweb.app.ajax.view;

import javax.annotation.concurrent.Immutable;

import com.helger.lamaweb.app.CLama;
import com.helger.photon.core.ajax.IAjaxFunctionDeclaration;
import com.helger.photon.core.ajax.decl.PublicApplicationAjaxFunctionDeclaration;
import com.helger.photon.uictrls.datatables.ajax.AjaxExecutorDataTables;
import com.helger.photon.uictrls.datatables.ajax.AjaxExecutorDataTablesI18N;

/**
 * This class defines the available ajax functions
 *
 * @author Philip Helger
 */
@Immutable
public final class CLamaAjaxView
{
  public static final IAjaxFunctionDeclaration DATATABLES = new PublicApplicationAjaxFunctionDeclaration ("dataTables", AjaxExecutorDataTables.class);
  public static final IAjaxFunctionDeclaration DATATABLES_I18N = new PublicApplicationAjaxFunctionDeclaration ("datatables-i18n",
                                                                                                               new AjaxExecutorDataTablesI18N (CLama.DEFAULT_LOCALE));
  public static final IAjaxFunctionDeclaration VIEW_LOGIN = new PublicApplicationAjaxFunctionDeclaration ("login", AjaxExecutorViewLogin.class);
  public static final IAjaxFunctionDeclaration VIEW_UPDATE_MENU_VIEW = new PublicApplicationAjaxFunctionDeclaration ("updateMenuView",
                                                                                                                     AjaxExecutorViewUpdateMenuView.class);

  private CLamaAjaxView ()
  {}
}
