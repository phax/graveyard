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
package com.helger.lamaweb.jetty;

import java.io.IOException;

import com.helger.photon.jetty.JettyStopper;

public final class JettyStopLAMA
{
  public static void main (final String [] args) throws IOException
  {
    new JettyStopper ().run ();
  }
}
