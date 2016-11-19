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

import com.helger.lamaweb.app.CLamaSecurity;
import com.helger.photon.bootstrap3.uictrls.ext.BootstrapLoginManager;

public final class LamaLoginManager extends BootstrapLoginManager
{
  public LamaLoginManager ()
  {
    super ("LamaConfig Administration - Login");
    setRequiredRoleIDs (CLamaSecurity.REQUIRED_ROLE_IDS_CONFIG);
  }
}
