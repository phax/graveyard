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

import javax.annotation.concurrent.Immutable;

import com.helger.photon.jetty.JettyStarter;

/**
 * Run ebiz4all as a standalone web application in Jetty on port 8080.<br>
 * http://localhost:8080/kb
 *
 * @author Philip Helger
 */
@Immutable
public final class RunInJettyLAMA
{
  public static void main (final String [] args) throws Exception
  {
    new JettyStarter (RunInJettyLAMA.class).run ();
  }
}
