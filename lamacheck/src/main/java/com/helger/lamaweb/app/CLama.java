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

import java.util.Locale;

import javax.annotation.concurrent.Immutable;

import com.helger.commons.locale.LocaleCache;

@Immutable
public final class CLama
{
  public static final Locale DEFAULT_LOCALE = LocaleCache.getInstance ().getLocale ("en", "US");

  private CLama ()
  {}
}
