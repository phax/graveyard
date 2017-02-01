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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.spi.DateFormatProvider;
import java.util.Locale;

public abstract class AbstractLocaleDataDateFormatProvider extends DateFormatProvider
{
  /**
   * Get {@link ILocaleData} for this instance. Must be provided by concrete
   * implementations.
   *
   * @return LocaleData Never <code>null</code>.
   */
  protected abstract ILocaleData getLocaleData ();

  @Override
  public final DateFormat getDateInstance (final int nStyle, final Locale aLocale)
  {
    getLocaleData ().checkLocaleSupported (aLocale);

    final String sFormat = _selectDateFormatForStyle (nStyle);
    return new SimpleDateFormat (sFormat, new LocaleDataDateFormatSymbols (getLocaleData ()));
  }

  @Override
  public final DateFormat getDateTimeInstance (final int nDateStyle, final int nTimeStyle, final Locale aLocale)
  {
    getLocaleData ().checkLocaleSupported (aLocale);

    final String sFormat = _selectDateFormatForStyle (nDateStyle) + " " + _selectTimeFormatForStyle (nTimeStyle);
    return new SimpleDateFormat (sFormat, new LocaleDataDateFormatSymbols (getLocaleData ()));
  }

  @Override
  public final DateFormat getTimeInstance (final int nStyle, final Locale aLocale)
  {
    getLocaleData ().checkLocaleSupported (aLocale);

    final String sFormat = _selectTimeFormatForStyle (nStyle);
    return new SimpleDateFormat (sFormat, new LocaleDataDateFormatSymbols (getLocaleData ()));
  }

  private String _selectDateFormatForStyle (final int nStyle)
  {
    String sFormat = null;
    switch (nStyle)
    {
      case DateFormat.FULL:
        sFormat = getLocaleData ().getDateFull ();
        break;
      case DateFormat.LONG:
        sFormat = getLocaleData ().getDateLong ();
        break;
      case DateFormat.MEDIUM:
        sFormat = getLocaleData ().getDateMedium ();
        break;
      case DateFormat.SHORT:
        sFormat = getLocaleData ().getDateShort ();
        break;
      default:
        throw new IllegalArgumentException ("unsupported style " + nStyle);
    }
    return sFormat;
  }

  private String _selectTimeFormatForStyle (final int nStyle)
  {
    String sFormat = null;
    switch (nStyle)
    {
      case DateFormat.FULL:
        sFormat = getLocaleData ().getTimeFull ();
        break;
      case DateFormat.LONG:
        sFormat = getLocaleData ().getTimeLong ();
        break;
      case DateFormat.MEDIUM:
        sFormat = getLocaleData ().getTimeMedium ();
        break;
      case DateFormat.SHORT:
        sFormat = getLocaleData ().getTimeShort ();
        break;
      default:
        throw new IllegalArgumentException ("unsupported style " + nStyle);
    }
    return sFormat;
  }

  @Override
  public final Locale [] getAvailableLocales ()
  {
    return getLocaleData ().getAllSupportedLocales ();
  }
}
