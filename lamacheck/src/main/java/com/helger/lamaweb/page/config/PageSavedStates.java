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
package com.helger.lamaweb.page.config;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.photon.bootstrap3.pages.form.BasePageFormSavedStates;
import com.helger.photon.uicore.page.WebPageExecutionContext;

public final class PageSavedStates extends BasePageFormSavedStates <WebPageExecutionContext>
{
  public PageSavedStates (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }
}
