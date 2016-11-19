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

import com.helger.commons.annotation.Nonempty;
import com.helger.lamaweb.app.layout.LamaHTMLProvider;
import com.helger.photon.core.app.CApplication;
import com.helger.photon.core.app.html.IHTMLProvider;
import com.helger.photon.core.servlet.AbstractApplicationServlet;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;

public final class LamaViewApplicationServlet extends AbstractApplicationServlet
{
  @Override
  @Nonnull
  @Nonempty
  protected String getApplicationID ()
  {
    return CApplication.APP_ID_PUBLIC;
  }

  @Override
  @Nonnull
  protected IHTMLProvider createHTMLProvider (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    return new LamaHTMLProvider ();
  }
}
