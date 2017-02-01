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
 * Various locale settings for the Serbocroatian languages, Serbian variant.
 *
 * @author Klaus Brunner
 */
public class LocalDataSerbocroatian_RS extends LocalDataSerbocroatian
{
  private static final String SERBIA = "RS";

  private static final String CURRENCY_SYMBOL = "RSD";

  private static final String [] MONTHS = { "januar",
                                            "februar",
                                            "mart",
                                            "april",
                                            "maj",
                                            "jun",
                                            "jul",
                                            "avgust",
                                            "septembar",
                                            "oktobar",
                                            "novembar",
                                            "decembar" };

  private static final String [] WEEKDAYS = { "",
                                              "nedelja",
                                              "ponedeljak",
                                              "utorak",
                                              "sreda",
                                              "četvrtak",
                                              "petak",
                                              "subota" };

  private static final String [] SHORT_WEEKDAYS = { "", "ned", "pon", "uto", "sre", "čet", "pet", "sub" };

  private static final Locale [] SUPPORTED_LOCALES = { new Locale (SERBOCROATIAN_LANGUAGE, SERBIA) };

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

  @Override
  public String [] getMonths ()
  {
    return Arrays.copyOf (MONTHS, MONTHS.length);
  }

  @Override
  public String [] getShortWeekdays ()
  {
    return Arrays.copyOf (SHORT_WEEKDAYS, SHORT_WEEKDAYS.length);
  }

  @Override
  public String [] getWeekdays ()
  {
    return Arrays.copyOf (WEEKDAYS, WEEKDAYS.length);
  }
}
