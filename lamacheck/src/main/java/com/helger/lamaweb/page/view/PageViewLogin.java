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
package com.helger.lamaweb.page.view;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.html.hc.impl.HCNodeList;
import com.helger.lamaweb.ui.LamaAccessUI;
import com.helger.photon.bootstrap3.pages.AbstractBootstrapWebPage;
import com.helger.photon.uicore.page.WebPageExecutionContext;

public final class PageViewLogin extends AbstractBootstrapWebPage <WebPageExecutionContext>
{
  public PageViewLogin (@Nonnull @Nonempty final String sID)
  {
    super (sID, "Login");
  }

  @Override
  @Nullable
  public String getHeaderText (@Nonnull final WebPageExecutionContext aWPEC)
  {
    return null;
  }

  @Override
  protected void fillContent (final WebPageExecutionContext aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();

    aNodeList.addChild (LamaAccessUI.createViewLoginForm (aWPEC, null, true));
  }
}
