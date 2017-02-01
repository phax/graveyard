/**
 * Copyright (C) 2015-2017 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.locales.xk;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

public final class KosovoLocales
{
  /** sq_XK */
  public static final Locale ALBANIAN_KOSOVO = new Locale ("sq", "XK");
  /** sr_XK */
  public static final Locale SERBIAN_KOSOVO = new Locale ("sr", "XK");
  /** sq_XK and sr_XK */
  public static final Set <Locale> KOSOVO_LIST;

  static
  {
    final Set <Locale> aSet = new LinkedHashSet <Locale> ();
    aSet.add (ALBANIAN_KOSOVO);
    aSet.add (SERBIAN_KOSOVO);
    KOSOVO_LIST = Collections.unmodifiableSet (aSet);
  }

  private KosovoLocales ()
  {}

  // Albanian
  public static final Locale PROXY_LOCALE = new Locale ("sq", "AL");
  // Serbian
  public static final Locale PROXY_LOCALE2 = new Locale ("sr", "RS");

  /**
   * @return sq_XK and sr_XK
   */
  public static Locale [] getLocaleArray ()
  {
    return new Locale [] { ALBANIAN_KOSOVO, SERBIAN_KOSOVO };
  }
}
