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

import javax.annotation.concurrent.Immutable;

import com.helger.html.css.DefaultCSSClassProvider;
import com.helger.html.css.ICSSClassProvider;

/**
 * Contains constant CSS classes
 *
 * @author Philip Helger
 */
@Immutable
public final class CLamaCSS
{
  // For forms, to create a border around it
  public static final ICSSClassProvider CSS_CLASS_FORM_GROUPED = DefaultCSSClassProvider.create ("grouped");

  // Logo parts
  public static final ICSSClassProvider CSS_CLASS_LOGO1 = DefaultCSSClassProvider.create ("logo1");
  public static final ICSSClassProvider CSS_CLASS_LOGO2 = DefaultCSSClassProvider.create ("logo2");

  private CLamaCSS ()
  {}
}
