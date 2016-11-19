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
package com.helger.lamaweb.app.menu.view;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class CLamaMenuView
{
  public static final String MENU_LOGIN = "login";

  public static final String MENU_ARTIFACTS_ALL = "artifacts_all";
  public static final String MENU_ARTIFACTS_PLUGINS = "artifacts_plugins";
  public static final String MENU_REPOSITORIES_ALL = "repositories_all";
  public static final String MENU_REPOSITORIES_EMPTY = "repositories_empty";
  public static final String MENU_REPOSITORIES_INVALID = "repositories_invalid";

  public static final String MENU_SITENOTICE = "sitenotice";
  public static final String MENU_GTC = "gtc";

  // menu item flags
  public static final String FLAG_FOOTER = "footer";

  private CLamaMenuView ()
  {}
}
