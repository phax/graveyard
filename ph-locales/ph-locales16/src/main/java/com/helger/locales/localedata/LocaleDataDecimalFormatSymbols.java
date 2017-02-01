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
package com.helger.locales.localedata;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class LocaleDataDecimalFormatSymbols extends DecimalFormatSymbols
{
  /**
   * @param aLocaleData
   *        Never <code>null</code>
   */
  public LocaleDataDecimalFormatSymbols (final ILocaleData aLocaleData)
  {
    super (Locale.getDefault ());
    setDecimalSeparator (aLocaleData.getDecimalSeparator ());
    setGroupingSeparator (aLocaleData.getGroupingSeparator ());
    setPatternSeparator (aLocaleData.getPatternSeparator ());
    setPercent (aLocaleData.getPercent ());
    setZeroDigit (aLocaleData.getZeroDigit ());
    setDigit (aLocaleData.getDigit ());
    setMinusSign (aLocaleData.getMinusSign ());
    setExponentSeparator (aLocaleData.getExponentSeparator ());
    setPerMill (aLocaleData.getPerMille ());
    setInfinity (aLocaleData.getInfinity ());
    setNaN (aLocaleData.getNaN ());
    setCurrencySymbol (aLocaleData.getCurrencySymbol ());
  }
}
