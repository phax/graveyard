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
package com.helger.lamaweb.ui;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.id.factory.GlobalIDFactory;
import com.helger.html.hc.html.forms.HCEdit;
import com.helger.html.hc.html.forms.HCEditPassword;
import com.helger.html.hc.html.forms.HCFieldSet;
import com.helger.html.hc.html.forms.HCForm;
import com.helger.html.hc.html.forms.HCLabel;
import com.helger.html.hc.html.forms.HCLegend;
import com.helger.html.hc.html.grouping.HCDiv;
import com.helger.html.jquery.JQuery;
import com.helger.html.js.EJSEvent;
import com.helger.html.jscode.JSAssocArray;
import com.helger.html.jscode.JSPackage;
import com.helger.lamaweb.app.ajax.view.CLamaAjaxView;
import com.helger.photon.bootstrap3.button.BootstrapButtonToolbar;
import com.helger.photon.bootstrap3.button.BootstrapSubmitButton;
import com.helger.photon.core.EPhotonCoreText;
import com.helger.photon.core.app.context.ILayoutExecutionContext;
import com.helger.photon.core.form.RequestField;
import com.helger.photon.core.login.CLogin;

@Immutable
public final class LamaAccessUI
{
  private LamaAccessUI ()
  {}

  @Nonnull
  public static HCForm createViewLoginForm (@Nonnull final ILayoutExecutionContext aLEC,
                                            @Nullable final String sPreselectedUserName,
                                            final boolean bFullUI)
  {
    final Locale aDisplayLocale = aLEC.getDisplayLocale ();

    // Use new IDs for both fields, in case the login stuff is displayed more
    // than once!
    final String sIDUserName = GlobalIDFactory.getNewStringID ();
    final String sIDPassword = GlobalIDFactory.getNewStringID ();
    final String sIDErrorField = GlobalIDFactory.getNewStringID ();

    final String sTextUserName = EPhotonCoreText.EMAIL_ADDRESS.getDisplayText (aDisplayLocale);
    final String sTextPassword = EPhotonCoreText.LOGIN_FIELD_PASSWORD.getDisplayText (aDisplayLocale);

    final HCForm aForm = new HCForm (aLEC.getSelfHref ());
    final HCFieldSet aFieldSet = aForm.addAndReturnChild (new HCFieldSet ());
    if (bFullUI)
    {
      aForm.addClass (CLamaCSS.CSS_CLASS_FORM_GROUPED);
      aFieldSet.addChild (new HCLegend ().addChild ("Login"));
    }

    // User name field
    aFieldSet.addChild (new HCLabel ().setFor (sIDUserName).addChild (sTextUserName));
    aFieldSet.addChild (new HCEdit (new RequestField (CLogin.REQUEST_ATTR_USERID, sPreselectedUserName)).setID (sIDUserName)
                                                                                                        .setPlaceholder (sTextUserName));

    // Password field
    aFieldSet.addChild (new HCLabel ().setFor (sIDPassword).addChild (sTextPassword));
    aFieldSet.addChild (new HCEditPassword (CLogin.REQUEST_ATTR_PASSWORD).setID (sIDPassword).setPlaceholder (sTextPassword));

    // Placeholder for error message
    aFieldSet.addChild (new HCDiv ().setID (sIDErrorField));

    // Login button
    final BootstrapButtonToolbar aToolbar = aFieldSet.addAndReturnChild (new BootstrapButtonToolbar (aLEC));
    final JSPackage aOnClick = new JSPackage ();
    aOnClick.add (LamaJS.viewLogin ()
                        .arg (CLamaAjaxView.VIEW_LOGIN.getInvocationURI (aLEC.getRequestScope ()))
                        .arg (new JSAssocArray ().add (CLogin.REQUEST_ATTR_USERID, JQuery.idRef (sIDUserName).val ())
                                                 .add (CLogin.REQUEST_ATTR_PASSWORD, JQuery.idRef (sIDPassword).val ()))
                        .arg (sIDErrorField));
    aOnClick._return (false);
    aToolbar.addChild (new BootstrapSubmitButton ().addChild (EPhotonCoreText.LOGIN_BUTTON_SUBMIT.getDisplayText (aDisplayLocale))
                                                   .setEventHandler (EJSEvent.CLICK, aOnClick));
    return aForm;
  }
}
