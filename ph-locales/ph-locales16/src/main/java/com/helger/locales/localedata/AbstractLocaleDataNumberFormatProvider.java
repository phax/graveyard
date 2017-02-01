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

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.spi.NumberFormatProvider;
import java.util.Locale;

public abstract class AbstractLocaleDataNumberFormatProvider extends NumberFormatProvider
{
  /**
   * Get {@link ILocaleData} for this instance. Must be provided by concrete
   * implementations.
   *
   * @return LocaleData Never <code>null</code>.
   */
  protected abstract ILocaleData getLocaleData ();

  @Override
  public final NumberFormat getCurrencyInstance (final Locale aLocale)
  {
    getLocaleData ().checkLocaleSupported (aLocale);

    final DecimalFormat aDF = new DecimalFormat (getLocaleData ().getCurrencyFormat (),
                                                 new LocaleDataDecimalFormatSymbols (getLocaleData ()));
    aDF.setRoundingMode (RoundingMode.HALF_UP);

    return aDF;
  }

  @Override
  public final NumberFormat getIntegerInstance (final Locale aLocale)
  {
    getLocaleData ().checkLocaleSupported (aLocale);

    final DecimalFormat aDF = new DecimalFormat (getLocaleData ().getIntegerFormat (),
                                                 new LocaleDataDecimalFormatSymbols (getLocaleData ()));

    // following settings are required by contract for this method
    aDF.setParseIntegerOnly (true);
    aDF.setRoundingMode (RoundingMode.HALF_EVEN);

    return aDF;
  }

  @Override
  public final NumberFormat getNumberInstance (final Locale aLocale)
  {
    getLocaleData ().checkLocaleSupported (aLocale);

    final DecimalFormat aDF = new DecimalFormat (getLocaleData ().getNumberFormat (),
                                                 new LocaleDataDecimalFormatSymbols (getLocaleData ()));
    aDF.setRoundingMode (RoundingMode.HALF_UP);

    return aDF;
  }

  @Override
  public final NumberFormat getPercentInstance (final Locale aLocale)
  {
    getLocaleData ().checkLocaleSupported (aLocale);

    final DecimalFormat aDF = new DecimalFormat (getLocaleData ().getPercentFormat (),
                                                 new LocaleDataDecimalFormatSymbols (getLocaleData ()));
    aDF.setRoundingMode (RoundingMode.HALF_UP);

    return aDF;
  }

  @Override
  public final Locale [] getAvailableLocales ()
  {
    return getLocaleData ().getAllSupportedLocales ();
  }
}
