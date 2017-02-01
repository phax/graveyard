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
package com.helger.locales.gl;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

public final class GalicianLocales
{
  /** gl */
  public static final Locale GALICIAN = new Locale ("gl");
  /** gl_ES */
  public static final Locale GALICIAN_ES = new Locale ("gl", "ES");
  /** gl and gl_ES */
  public static final Set <Locale> GALICIAN_LIST;

  static
  {
    final Set <Locale> aSet = new LinkedHashSet <Locale> ();
    aSet.add (GALICIAN);
    aSet.add (GALICIAN_ES);
    GALICIAN_LIST = Collections.unmodifiableSet (aSet);
  }

  /** es_ES */
  public static final Locale CASTILIAN = new Locale ("es", "ES");

  private GalicianLocales ()
  {}

  /**
   * @return "gl" and "gl_ES"s
   */
  public static Locale [] getLocaleArray ()
  {
    return new Locale [] { GALICIAN, GALICIAN_ES };
  }
}
