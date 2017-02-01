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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.spi.DateFormatProvider;
import java.util.Locale;

import com.helger.locales.gl.GalicianLocales;

public final class GalicianDateFormatProvider extends DateFormatProvider
{
  private static final String PATTERN_DATE_SHORT = "d'/'MM'/'yy";
  private static final String PATTERN_DATE_MEDIUM = "dd'-'MMM'-'yyyy";
  private static final String PATTERN_DATE_LONG = "d 'de' MMMM 'de' yyyy";
  private static final String PATTERN_DATE_FULL = "EEEE d 'de' MMMM 'de' yyyy";

  private static final String PATTERN_TIME_SHORT = "H':'mm";
  private static final String PATTERN_TIME_MEDIUM = "H':'mm':'ss";
  private static final String PATTERN_TIME_LONG = "H':'mm':'ss z";
  private static final String PATTERN_TIME_FULL = "HH'H'mm'' z";

  public GalicianDateFormatProvider ()
  {}

  private static boolean _isStyleValid (final int nStyle)
  {
    return nStyle == DateFormat.SHORT ||
           nStyle == DateFormat.MEDIUM ||
           nStyle == DateFormat.LONG ||
           nStyle == DateFormat.FULL;
  }

  @Override
  public DateFormat getDateInstance (final int nStyle, final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ();

    if (!_isStyleValid (nStyle))
      throw new IllegalArgumentException ("Style \"" + nStyle + "\" is not valid");

    if (GalicianLocales.GALICIAN_LIST.contains (aLocale))
    {
      switch (nStyle)
      {
        case DateFormat.FULL:
          return new SimpleDateFormat (PATTERN_DATE_FULL, aLocale);
        case DateFormat.LONG:
          return new SimpleDateFormat (PATTERN_DATE_LONG, aLocale);
        case DateFormat.MEDIUM:
          return new SimpleDateFormat (PATTERN_DATE_MEDIUM, aLocale);
        case DateFormat.SHORT:
          return new SimpleDateFormat (PATTERN_DATE_SHORT, aLocale);
      }
    }

    throw new IllegalArgumentException ("Locale \"" +
                                        aLocale +
                                        "\" " +
                                        "is not one of the supported locales (" +
                                        GalicianLocales.GALICIAN_LIST +
                                        ")");
  }

  @Override
  public DateFormat getDateTimeInstance (final int nDateStyle, final int nTimeStyle, final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ();
    if (!_isStyleValid (nDateStyle))
      throw new IllegalArgumentException ("Style \"" + nDateStyle + "\" is not valid");
    if (!_isStyleValid (nTimeStyle))
      throw new IllegalArgumentException ("Style \"" + nTimeStyle + "\" is not valid");

    if (GalicianLocales.GALICIAN_LIST.contains (aLocale))
    {
      final StringBuilder aPattern = new StringBuilder ();
      switch (nDateStyle)
      {
        case DateFormat.FULL:
          aPattern.append (PATTERN_DATE_FULL);
          break;
        case DateFormat.LONG:
          aPattern.append (PATTERN_DATE_LONG);
          break;
        case DateFormat.MEDIUM:
          aPattern.append (PATTERN_DATE_MEDIUM);
          break;
        case DateFormat.SHORT:
          aPattern.append (PATTERN_DATE_SHORT);
          break;
        default:
          throw new IllegalArgumentException ("Unsupported date style");
      }
      aPattern.append (" ");
      switch (nTimeStyle)
      {
        case DateFormat.FULL:
          aPattern.append (PATTERN_TIME_FULL);
          break;
        case DateFormat.LONG:
          aPattern.append (PATTERN_TIME_LONG);
          break;
        case DateFormat.MEDIUM:
          aPattern.append (PATTERN_TIME_MEDIUM);
          break;
        case DateFormat.SHORT:
          aPattern.append (PATTERN_TIME_SHORT);
          break;
        default:
          throw new IllegalArgumentException ("Unsupported time style");
      }

      return new SimpleDateFormat (aPattern.toString (), aLocale);
    }

    throw new IllegalArgumentException ("Locale \"" +
                                        aLocale +
                                        "\" " +
                                        "is not one of the supported locales (" +
                                        GalicianLocales.GALICIAN_LIST +
                                        ")");
  }

  @Override
  public DateFormat getTimeInstance (final int nStyle, final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ();
    if (!_isStyleValid (nStyle))
      throw new IllegalArgumentException ("Style \"" + nStyle + "\" is not valid");

    if (GalicianLocales.GALICIAN_LIST.contains (aLocale))
    {
      switch (nStyle)
      {
        case DateFormat.FULL:
          return new SimpleDateFormat (PATTERN_TIME_FULL, aLocale);
        case DateFormat.LONG:
          return new SimpleDateFormat (PATTERN_TIME_LONG, aLocale);
        case DateFormat.MEDIUM:
          return new SimpleDateFormat (PATTERN_TIME_MEDIUM, aLocale);
        case DateFormat.SHORT:
          return new SimpleDateFormat (PATTERN_TIME_SHORT, aLocale);
      }
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
