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
package com.helger.lamaweb.page;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.id.IHasID;
import com.helger.commons.name.IHasDisplayName;
import com.helger.html.hc.IHCNode;
import com.helger.html.jscode.JSArray;
import com.helger.lamaweb.app.ajax.config.CLamaAjaxConfig;
import com.helger.photon.bootstrap3.alert.BootstrapErrorBox;
import com.helger.photon.bootstrap3.alert.BootstrapInfoBox;
import com.helger.photon.bootstrap3.button.BootstrapButtonToolbar;
import com.helger.photon.bootstrap3.pages.AbstractBootstrapWebPageForm;
import com.helger.photon.core.app.layout.CLayout;
import com.helger.photon.uicore.form.ajax.AjaxExecutorSaveFormState;
import com.helger.photon.uicore.icon.EDefaultIcon;
import com.helger.photon.uicore.js.JSFormHelper;
import com.helger.photon.uicore.page.WebPageExecutionContext;

public abstract class AbstractLamaFormPage <DATATYPE extends IHasID <String>> extends AbstractBootstrapWebPageForm <DATATYPE, WebPageExecutionContext>
{
  public AbstractLamaFormPage (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }

  @Override
  @Nullable
  public String getHeaderText (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final DATATYPE aSelectedObject = getSelectedObject (aWPEC, getSelectedObjectID (aWPEC));
    if (aSelectedObject instanceof IHasDisplayName)
      return ((IHasDisplayName) aSelectedObject).getDisplayName ();
    return super.getHeaderText (aWPEC);
  }

  @Override
  protected void modifyCreateToolbar (@Nonnull final WebPageExecutionContext aWPEC, @Nonnull final BootstrapButtonToolbar aToolbar)
  {
    final JSArray aSuccessUpdates = new JSArray ();
    // Update menu via Ajax
    aSuccessUpdates.add (JSFormHelper.createUpdateParam (aWPEC.getRequestScope (),
                                                         CLayout.LAYOUT_AREAID_MENU,
                                                         CLamaAjaxConfig.CONFIG_UPDATE_MENU_VIEW));

    // Update special area directly with code
    IHCNode aSpecialNode = new BootstrapInfoBox ().addChild ("Data was successfully saved!");
    aSuccessUpdates.add (JSFormHelper.createUpdateParam (CLayout.LAYOUT_AREAID_SPECIAL, aSpecialNode));
    final JSArray aFailureUpdates = new JSArray ();
    // Update special area directly with code
    aSpecialNode = new BootstrapErrorBox ().addChild ("Error saving the data!");
    aFailureUpdates.add (JSFormHelper.createUpdateParam (CLayout.LAYOUT_AREAID_SPECIAL, aSpecialNode));
    aToolbar.addButton ("Merken",
                        JSFormHelper.saveFormData (aWPEC.getRequestScope (),
                                                   FORM_ID_INPUT,
                                                   AjaxExecutorSaveFormState.PREFIX_FIELD,
                                                   getID (),
                                                   CLamaAjaxConfig.CONFIG_SAVE_FORM_STATE,
                                                   aSuccessUpdates,
                                                   aFailureUpdates),
                        EDefaultIcon.SAVE);
  }
}
