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
package com.helger.lamaweb.app.layout;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.OverrideOnDemand;
import com.helger.html.hc.html.root.HCHtml;
import com.helger.photon.basic.app.request.IRequestManager;
import com.helger.photon.core.app.context.ISimpleWebExecutionContext;
import com.helger.photon.core.app.context.LayoutExecutionContext;
import com.helger.photon.core.app.layout.AbstractLayoutManagerBasedLayoutHTMLProvider;
import com.helger.photon.core.app.layout.ApplicationLayoutManager;

/**
 * Main class for creating HTML output
 *
 * @author Philip Helger
 */
public class LamaHTMLProvider extends AbstractLayoutManagerBasedLayoutHTMLProvider <LayoutExecutionContext>
{
  public LamaHTMLProvider ()
  {
    super (ApplicationLayoutManager.<LayoutExecutionContext> getInstance ());
  }

  @Override
  @Nonnull
  protected LayoutExecutionContext createLayoutExecutionContext (@Nonnull final ISimpleWebExecutionContext aSWEC,
                                                                 @Nonnull final IRequestManager aRequestManager)
  {
    return new LayoutExecutionContext (aSWEC, aRequestManager.getRequestMenuItem ());
  }

  /**
   * Fill the HTML HEAD element.
   *
   * @param aHtml
   *        The HTML object to be filled.
   */
  @Override
  @OverrideOnDemand
  protected void fillHead (@Nonnull final ISimpleWebExecutionContext aSWEC, @Nonnull final HCHtml aHtml)
  {
    super.fillHead (aSWEC, aHtml);
    aHtml.getHead ().setPageTitle ("LaMaCheck");
  }
}
