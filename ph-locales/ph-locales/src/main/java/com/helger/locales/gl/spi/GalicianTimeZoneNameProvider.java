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

import java.text.DateFormatSymbols;
import java.util.Locale;
import java.util.TimeZone;
import java.util.spi.TimeZoneNameProvider;

import com.helger.locales.gl.GalicianLocales;

public final class GalicianTimeZoneNameProvider extends TimeZoneNameProvider
{
  public GalicianTimeZoneNameProvider ()
  {}

  private static boolean _isStyleValid (final int nStyle)
  {
    return nStyle == TimeZone.SHORT || nStyle == TimeZone.LONG;
  }

  @Override
  public String getDisplayName (final String sID, final boolean bDaylight, final int nStyle, final Locale aLocale)
  {
    if (sID == null)
      throw new NullPointerException ();
    if (aLocale == null)
      throw new NullPointerException ();
    if (!_isStyleValid (nStyle))
      throw new IllegalArgumentException ("Style \"" + nStyle + "\" is not valid");

    if (GalicianLocales.GALICIAN_LIST.contains (aLocale))
    {
      final DateFormatSymbols aDFS = DateFormatSymbols.getInstance (GalicianLocales.GALICIAN_ES);
      final String [] [] aZoneStrings = aDFS.getZoneStrings ();

      /*
       * First, try to retrieve a name using the specified ID as the main
       * timezone ID
       */
      for (final String [] aZoneString : aZoneStrings)
        if (sID.equalsIgnoreCase (aZoneString[0]))
        {
          switch (nStyle)
          {
            case TimeZone.LONG:
              return bDaylight ? aZoneString[3] : aZoneString[1];
            case TimeZone.SHORT:
              return bDaylight ? aZoneString[4] : aZoneString[2];
          }
        }

      /*
       * Then try to retrieve a name using the specified ID as a short name
       * (first, non-daylight saving - then, daylight-saving).
       */
      if (!bDaylight)
      {
        for (final String [] aZoneString : aZoneStrings)
          if (sID.equalsIgnoreCase (aZoneString[2]))
          {
            switch (nStyle)
            {
              case TimeZone.LONG:
                return bDaylight ? aZoneString[3] : aZoneString[1];
              case TimeZone.SHORT:
                return bDaylight ? aZoneString[4] : aZoneString[2];
            }
          }
      }
      else
      {
        for (final String [] aZoneString : aZoneStrings)
          if (sID.equalsIgnoreCase (aZoneString[4]))
          {
            switch (nStyle)
            {
              case TimeZone.LONG:
                return bDaylight ? aZoneString[3] : aZoneString[1];
              case TimeZone.SHORT:
                return bDaylight ? aZoneString[4] : aZoneString[2];
            }
          }
      }

      /*
       * If we don't have a name yet, default to en_US
       */
      final TimeZone aTimeZone = TimeZone.getTimeZone (sID);
      return aTimeZone.getDisplayName (new Locale ("en", "US"));
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
