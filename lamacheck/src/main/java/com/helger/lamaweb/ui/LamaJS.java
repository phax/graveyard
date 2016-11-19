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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.html.jscode.JSExpr;
import com.helger.html.jscode.JSInvocation;
import com.helger.html.jscode.JSRef;

@Immutable
public final class LamaJS
{
  private LamaJS ()
  {}

  @Nonnull
  public static JSRef getLamaApp ()
  {
    // Match the JS file in src/main/webapp/js
    return JSExpr.ref ("Lama");
  }

  @Nonnull
  public static JSInvocation viewLogin ()
  {
    // Invoke the JS function "viewLogin" on the object
    return getLamaApp ().invoke ("viewLogin");
  }
}
