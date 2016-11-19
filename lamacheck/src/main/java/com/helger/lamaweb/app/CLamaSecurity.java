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
package com.helger.lamaweb.app;

import java.util.List;

import javax.annotation.concurrent.Immutable;

import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.photon.security.CSecurity;

@Immutable
public final class CLamaSecurity
{
  // Security role IDs
  public static final String ROLEID_CONFIG = "config";
  public static final String ROLEID_VIEW = "view";

  // User group IDs
  public static final String USERGROUPID_SUPERUSER = CSecurity.USERGROUP_ADMINISTRATORS_ID;
  public static final String USERGROUPID_CONFIG = "ugconfig";
  public static final String USERGROUPID_VIEW = "ugview";

  public static final List <String> REQUIRED_ROLE_IDS_CONFIG = new CommonsArrayList<> (ROLEID_CONFIG).getAsUnmodifiable ();
  public static final List <String> REQUIRED_ROLE_IDS_VIEW = new CommonsArrayList<> (ROLEID_VIEW).getAsUnmodifiable ();

  private CLamaSecurity ()
  {}
}
