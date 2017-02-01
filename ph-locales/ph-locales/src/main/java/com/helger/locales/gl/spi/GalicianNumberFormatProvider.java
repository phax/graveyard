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
package com.helger.locales.gl.spi;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.spi.NumberFormatProvider;
import java.util.Locale;

import com.helger.locales.gl.GalicianDecimalFormatSymbols;
import com.helger.locales.gl.GalicianLocales;

public final class GalicianNumberFormatProvider extends NumberFormatProvider
{
  public GalicianNumberFormatProvider ()
  {}

  @Override
  public NumberFormat getCurrencyInstance (final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ();

    if (GalicianLocales.GALICIAN_LIST.contains (aLocale))
    {
      final NumberFormat aNF = NumberFormat.getCurrencyInstance (GalicianLocales.CASTILIAN);
      if (aNF instanceof DecimalFormat)
      {
        ((DecimalFormat) aNF).setDecimalFormatSymbols (new GalicianDecimalFormatSymbols ());
      }
      return aNF;
    }

    throw new IllegalArgumentException ("Locale \"" +
                                        aLocale +
                                        "\" " +
                                        "is not one of the supported locales (" +
                                        GalicianLocales.GALICIAN_LIST +
                                        ")");
  }

  @Override
  public NumberFormat getIntegerInstance (final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ();

    if (GalicianLocales.GALICIAN_LIST.contains (aLocale))
    {
      final NumberFormat aNF = NumberFormat.getIntegerInstance (GalicianLocales.CASTILIAN);
      if (aNF instanceof DecimalFormat)
      {
        ((DecimalFormat) aNF).setDecimalFormatSymbols (new GalicianDecimalFormatSymbols ());
      }
      return aNF;
    }

    throw new IllegalArgumentException ("Locale \"" +
                                        aLocale +
                                        "\" " +
                                        "is not one of the supported locales (" +
                                        GalicianLocales.GALICIAN_LIST +
                                        ")");
  }

  @Override
  public NumberFormat getNumberInstance (final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ();

    if (GalicianLocales.GALICIAN_LIST.contains (aLocale))
    {
      final NumberFormat aNF = NumberFormat.getNumberInstance (GalicianLocales.CASTILIAN);
      if (aNF instanceof DecimalFormat)
      {
        ((DecimalFormat) aNF).setDecimalFormatSymbols (new GalicianDecimalFormatSymbols ());
      }
      return aNF;
    }

    throw new IllegalArgumentException ("Locale \"" +
                                        aLocale +
                                        "\" " +
                                        "is not one of the supported locales (" +
                                        GalicianLocales.GALICIAN_LIST +
                                        ")");
  }

  @Override
  public NumberFormat getPercentInstance (final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ();

    if (GalicianLocales.GALICIAN_LIST.contains (aLocale))
    {
      final NumberFormat aNF = NumberFormat.getPercentInstance (GalicianLocales.CASTILIAN);
      if (aNF instanceof DecimalFormat)
      {
        ((DecimalFormat) aNF).setDecimalFormatSymbols (new GalicianDecimalFormatSymbols ());
      }
      return aNF;
    }

    throw new IllegalArgumentException ("Locale \"" +
                                        aLocale +
                                        "\" " +
                                        "is not one of the supported locales (" +
                                        GalicianLocales.GALICIAN_LIST +
                                        ")");
  }

  @Override
  public Locale [] getAvailableLocales ()
  {
    return GalicianLocales.getLocaleArray ();
  }
}
