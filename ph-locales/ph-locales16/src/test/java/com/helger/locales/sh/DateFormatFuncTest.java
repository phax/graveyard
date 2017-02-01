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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.text.DateFormat;
import java.text.spi.DateFormatProvider;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.helger.locales.sh.spi.DateFormatProvider_sh;
import com.helger.locales.sh.spi.DateFormatProvider_sh_HR;
import com.helger.locales.sh.spi.DateFormatProvider_sh_RS;

public final class DateFormatFuncTest
{
  private static final int DAYS_RANGE = 400;
  private static final Locale BOSNIAN_LOCALE = new Locale ("bs");
  private static final Locale SH_RS_LOCALE = new Locale ("sh", "RS");
  private static final Locale SH_HR_LOCALE = new Locale ("sh", "HR");
  private static final int SECOND = 19;
  private static final int MINUTE = 25;
  private static final int HOUR = 22;
  private static final int DAY = 10;
  private static final int MONTH = 7;
  private static final int YEAR = 2008;

  private static final Date TEST_DATE;
  static
  {
    final GregorianCalendar aCal = new GregorianCalendar (TimeZone.getDefault (), Locale.getDefault ());
    aCal.set (YEAR, MONTH - 1, DAY, HOUR, MINUTE, SECOND);
    TEST_DATE = aCal.getTime ();
  }

  @Test
  public void getLegalDateInstance ()
  {
    final DateFormatProvider provider = new DateFormatProvider_sh ();
    assertNotNull (provider.getDateInstance (DateFormat.SHORT, BOSNIAN_LOCALE));
  }

  @Test (expected = IllegalArgumentException.class)
  public void getIllegalDateInstance ()
  {
    final DateFormatProvider provider = new DateFormatProvider_sh ();
    provider.getDateInstance (DateFormat.SHORT * 2, BOSNIAN_LOCALE);
  }

  @Test
  public void dateFormats ()
  {
    final DateFormatProvider provider = new DateFormatProvider_sh ();

    {
      final DateFormat shortDate = provider.getDateInstance (DateFormat.SHORT, BOSNIAN_LOCALE);
      final String shortDateString = shortDate.format (TEST_DATE);
      final String expected = String.format (BOSNIAN_LOCALE,
                                             "%02d.%02d.%02d.",
                                             Integer.valueOf (DAY),
                                             Integer.valueOf (MONTH),
                                             Integer.valueOf (YEAR - 2000));
      assertEquals (expected, shortDateString);
    }

    {
      final DateFormat mediumDate = provider.getDateInstance (DateFormat.MEDIUM, BOSNIAN_LOCALE);
      final String mediumDateString = mediumDate.format (TEST_DATE);
      final String expected = String.format (BOSNIAN_LOCALE,
                                             "%02d.%02d.%04d.",
                                             Integer.valueOf (DAY),
                                             Integer.valueOf (MONTH),
                                             Integer.valueOf (YEAR));
      assertEquals (expected, mediumDateString);
    }

    {
      final DateFormat longDate = provider.getDateInstance (DateFormat.LONG, BOSNIAN_LOCALE);
      final String longDateString = longDate.format (TEST_DATE);
      final String expected = String.format (BOSNIAN_LOCALE,
                                             "%d. juli %04d.",
                                             Integer.valueOf (DAY),
                                             Integer.valueOf (YEAR));
      assertEquals (expected, longDateString);
    }

    {
      final DateFormat fullDate = provider.getDateInstance (DateFormat.FULL, BOSNIAN_LOCALE);
      final String fullDateString = fullDate.format (TEST_DATE);
      final String expected = String.format (BOSNIAN_LOCALE,
                                             "četvrtak, %d. juli %04d.",
                                             Integer.valueOf (DAY),
                                             Integer.valueOf (YEAR));
      assertEquals (expected, fullDateString);
    }
  }

  @Test
  public void dateFormatsSerbia ()
  {
    final DateFormatProvider provider = new DateFormatProvider_sh_RS ();
    {
      final DateFormat fullDate = provider.getDateInstance (DateFormat.FULL, SH_RS_LOCALE);
      final String fullDateString = fullDate.format (TEST_DATE);
      final String expected = String.format (SH_RS_LOCALE,
                                             "četvrtak, %d. jul %04d.",
                                             Integer.valueOf (DAY),
                                             Integer.valueOf (YEAR));
      assertEquals (expected, fullDateString);
    }
  }

  @Test
  public void dayRangesWithoutCrashing ()
  {
    // see if it doesn't crash on a range of days
    {
      final DateFormatProvider provider = new DateFormatProvider_sh ();
      final DateFormat fullDate = provider.getDateInstance (DateFormat.FULL, BOSNIAN_LOCALE);
      formatDayRange (fullDate);
    }

    {
      final DateFormatProvider provider = new DateFormatProvider_sh_RS ();
      final DateFormat fullDate = provider.getDateInstance (DateFormat.FULL, SH_RS_LOCALE);
      formatDayRange (fullDate);
    }

    {
      final DateFormatProvider provider = new DateFormatProvider_sh_HR ();
      final DateFormat fullDate = provider.getDateInstance (DateFormat.FULL, SH_HR_LOCALE);
      formatDayRange (fullDate);
    }
  }

  private void formatDayRange (final DateFormat fullDate)
  {
    Date currentDate = TEST_DATE;
    for (int i = 0; i < DAYS_RANGE; i++)
    {
      assertNotNull (fullDate.format (currentDate));
      currentDate = new Date (currentDate.getTime () + 24 * 60 * 60 * 1000);
    }
  }

  @Test
  public void dateFormatsCroatia ()
  {
    final DateFormatProvider provider = new DateFormatProvider_sh_HR ();
    {
      final DateFormat fullDate = provider.getDateInstance (DateFormat.FULL, SH_HR_LOCALE);
      final String fullDateString = fullDate.format (TEST_DATE);

      final String expected = String.format (SH_HR_LOCALE,
                                             "četvrtak, %d. srpanj %04d.",
                                             Integer.valueOf (DAY),
                                             Integer.valueOf (YEAR));
      assertEquals (expected, fullDateString);
    }
  }

  @Test
  public void timeFormats ()
  {
    final DateFormatProvider provider = new DateFormatProvider_sh ();

    {
      final DateFormat shortTime = provider.getTimeInstance (DateFormat.SHORT, BOSNIAN_LOCALE);
      final String shortTimeString = shortTime.format (TEST_DATE);
      final String expected = String.format (BOSNIAN_LOCALE,
                                             "%02d:%02d",
                                             Integer.valueOf (HOUR),
                                             Integer.valueOf (MINUTE));
      assertEquals (expected, shortTimeString);
    }

    {
      final DateFormat mediumTime = provider.getTimeInstance (DateFormat.MEDIUM, BOSNIAN_LOCALE);
      final String mediumDateString = mediumTime.format (TEST_DATE);
      final String expected = String.format (BOSNIAN_LOCALE,
                                             "%02d:%02d:%02d",
                                             Integer.valueOf (HOUR),
                                             Integer.valueOf (MINUTE),
                                             Integer.valueOf (SECOND));
      assertEquals (expected, mediumDateString);
    }

    {
      final DateFormat longTime = provider.getTimeInstance (DateFormat.LONG, BOSNIAN_LOCALE);
      final String longDateString = longTime.format (TEST_DATE);
      final String expected = String.format (BOSNIAN_LOCALE,
                                             "%02d:%02d:%02d",
                                             Integer.valueOf (HOUR),
                                             Integer.valueOf (MINUTE),
                                             Integer.valueOf (SECOND));
      assertTrue (longDateString.startsWith (expected));
      // TODO: complete check
    }

    {
      final DateFormat fullTime = provider.getTimeInstance (DateFormat.LONG, BOSNIAN_LOCALE);
      final String fullDateString = fullTime.format (TEST_DATE);
      final String expected = String.format (BOSNIAN_LOCALE,
                                             "%02d:%02d:%02d",
                                             Integer.valueOf (HOUR),
                                             Integer.valueOf (MINUTE),
                                             Integer.valueOf (SECOND));
      assertTrue (fullDateString.startsWith (expected));
      // TODO: complete check
    }
  }

  @Test
  public void checkSpiDeclaration ()
  {
    // check that SPI "declaration file" exists
    final URL spifile = Thread.currentThread ()
                              .getContextClassLoader ()
                              .getResource ("META-INF/services/java.text.spi.DateFormatProvider");
    assertNotNull (spifile);
  }
}
