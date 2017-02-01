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
 * Various locale settings for the Serbocroatian languages, Croatian variant.
 *
 * @author Klaus Brunner
 */
public class LocalDataSerbocroatian_HR extends LocalDataSerbocroatian
{
  private static final String CURRENCY_SYMBOL = "HRK";

  private static final Locale [] SUPPORTED_LOCALES = { new Locale (SERBOCROATIAN_LANGUAGE, CROATIA_COUNTRY) };

  private static final String [] MONTHS = { "siječanj",
                                            "veljača",
                                            "ožujak",
                                            "travanj",
                                            "svibanj",
                                            "lipanj",
                                            "srpanj",
                                            "kolovoz",
                                            "rujan",
                                            "listopad",
                                            "studeni",
                                            "prosinac" };

  private static final String [] SHORT_MONTHS = { "sij",
                                                  "vel",
                                                  "ožu",
                                                  "tra",
                                                  "svi",
                                                  "lip",
                                                  "srp",
                                                  "kol",
                                                  "ruj",
                                                  "lis",
                                                  "stu",
                                                  "pro" };

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
  public String [] getShortMonths ()
  {
    return Arrays.copyOf (SHORT_MONTHS, SHORT_MONTHS.length);
  }
}
