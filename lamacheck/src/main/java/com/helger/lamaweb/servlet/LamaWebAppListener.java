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
package com.helger.lamaweb.servlet;

import javax.annotation.Nonnull;

import org.slf4j.bridge.SLF4JBridgeHandler;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.ext.CommonsHashMap;
import com.helger.commons.collection.ext.ICommonsMap;
import com.helger.commons.debug.GlobalDebug;
import com.helger.html.jquery.JQueryAjaxBuilder;
import com.helger.html.jscode.JSAssocArray;
import com.helger.lama.updater.mgr.LamaMetaManager;
import com.helger.lamaweb.app.ajax.view.CLamaAjaxView;
import com.helger.lamaweb.app.init.DefaultSecurity;
import com.helger.lamaweb.app.init.InitializerConfig;
import com.helger.lamaweb.app.init.InitializerView;
import com.helger.photon.bootstrap3.servlet.AbstractWebAppListenerMultiAppBootstrap;
import com.helger.photon.bootstrap3.uictrls.datatables.BootstrapDataTables;
import com.helger.photon.core.app.CApplication;
import com.helger.photon.core.app.context.LayoutExecutionContext;
import com.helger.photon.core.app.init.IApplicationInitializer;
import com.helger.photon.uictrls.datatables.DataTablesLengthMenu;
import com.helger.photon.uictrls.datatables.EDataTablesFilterType;
import com.helger.photon.uictrls.datatables.ajax.AjaxExecutorDataTables;
import com.helger.photon.uictrls.datatables.ajax.AjaxExecutorDataTablesI18N;
import com.helger.photon.uictrls.famfam.EFamFamIcon;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;

/**
 * Callbacks for the application server
 *
 * @author Philip Helger
 */
public final class LamaWebAppListener extends AbstractWebAppListenerMultiAppBootstrap <LayoutExecutionContext>
{
  private static final DataTablesLengthMenu LENGTH_MENU = new DataTablesLengthMenu ().addItem (10)
                                                                                     .addItem (25)
                                                                                     .addItem (50)
                                                                                     .addItem (100)
                                                                                     .addItemAll ();

  @Override
  @Nonnull
  @Nonempty
  protected ICommonsMap <String, IApplicationInitializer <LayoutExecutionContext>> getAllInitializers ()
  {
    final ICommonsMap <String, IApplicationInitializer <LayoutExecutionContext>> ret = new CommonsHashMap<> ();
    ret.put (CApplication.APP_ID_SECURE, new InitializerConfig ());
    ret.put (CApplication.APP_ID_PUBLIC, new InitializerView ());
    return ret;
  }

  @Override
  protected void initGlobals ()
  {
    // Internal stuff:

    // JUL to SLF4J
    SLF4JBridgeHandler.removeHandlersForRootLogger ();
    SLF4JBridgeHandler.install ();

    super.initGlobals ();

    // set default UI properties
    EFamFamIcon.setAsDefault ();
    BootstrapDataTables.setConfigurator ( (aLEC, aTable, aDataTables) -> {
      final IRequestWebScopeWithoutResponse aRequestScope = aLEC.getRequestScope ();
      aDataTables.setAutoWidth (false)
                 .setLengthMenu (LENGTH_MENU)
                 .setAjaxBuilder (new JQueryAjaxBuilder ().url (CLamaAjaxView.DATATABLES.getInvocationURL (aRequestScope))
                                                          .data (new JSAssocArray ().add (AjaxExecutorDataTables.OBJECT_ID,
                                                                                          aTable.getID ())))
                 .setServerFilterType (EDataTablesFilterType.ALL_TERMS_PER_ROW)
                 .setTextLoadingURL (CLamaAjaxView.DATATABLES_I18N.getInvocationURL (aRequestScope),
                                     AjaxExecutorDataTablesI18N.LANGUAGE_ID);

      // Save cookie only in production mode
      if (GlobalDebug.isProductionMode ())
        aDataTables.setStateSave (true);
    });

    // Set all security related stuff
    DefaultSecurity.init ();

    // Initialize here
    LamaMetaManager.getInstance ();
  }
}
