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
package com.helger.locales.sh;

import java.util.Arrays;
import java.util.Locale;

/**
 * Various locale settings for the Serbocroatian languages, Bosnia variant.
 *
 * @author Klaus Brunner
 */
public class LocalDataSerbocroatian_BA extends LocalDataSerbocroatian
{
  private static final String BOSNIA_AND_HERZEGOVINA = "BA";
  private static final String CURRENCY_SYMBOL = "BAM";

  private static final Locale [] SUPPORTED_LOCALES = { new Locale (BOSNIAN_LANGUAGE, BOSNIA_AND_HERZEGOVINA),
                                                       new Locale (SERBOCROATIAN_LANGUAGE, BOSNIA_AND_HERZEGOVINA) };

  @Override
  public Locale [] getAllSupportedLocales ()
  {
    return Arrays.copyOf (SUPPORTED_LOCALES, SUPPORTED_LOCALES.length);
  }

  @Override
  public String getCurrencySymbol ()
  {
    return CURRENCY_SYMBOL;
  }
}
