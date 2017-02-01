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

import java.net.URL;
import java.text.DateFormatSymbols;
import java.text.spi.DateFormatSymbolsProvider;
import java.util.Locale;

import org.junit.Test;

import com.helger.locales.sh.spi.DateFormatSymbolsProvider_sh;
import com.helger.locales.sh.spi.DateFormatSymbolsProvider_sh_RS;

public final class DateFormatSymbolsFuncTest
{
  private static final String BOSNIAN_LANGUAGE = "bs";
  private static final String SERBIA = "RS";
  private static final String SERBOCROATIAN_LANGUAGE = "sh";

  @Test (expected = IllegalArgumentException.class)
  public void providerChecksInvalidLocale ()
  {
    final DateFormatSymbolsProvider provider = new DateFormatSymbolsProvider_sh ();
    provider.getInstance (new Locale ("??", "!!"));
  }

  @Test (expected = NullPointerException.class)
  public void providerChecksNullLocale ()
  {
    final DateFormatSymbolsProvider provider = new DateFormatSymbolsProvider_sh ();
    provider.getInstance (null);
  }

  @Test
  public void providerReturnsInstance ()
  {
    final DateFormatSymbolsProvider provider = new DateFormatSymbolsProvider_sh ();
    assertNotNull (provider.getInstance (new Locale (BOSNIAN_LANGUAGE)));
  }

  @Test
  public void monthNames ()
  {
    final DateFormatSymbolsProvider provider = new DateFormatSymbolsProvider_sh ();
    final DateFormatSymbols symbols = provider.getInstance (new Locale (BOSNIAN_LANGUAGE));

    assertEquals (12, symbols.getMonths ().length);
    assertEquals ("januar", symbols.getMonths ()[0]);
    assertEquals ("decembar", symbols.getMonths ()[11]);
  }

  @Test
  public void weekdays ()
  {
    final DateFormatSymbolsProvider provider = new DateFormatSymbolsProvider_sh ();
    final DateFormatSymbols symbols = provider.getInstance (new Locale (BOSNIAN_LANGUAGE));

    assertEquals (8, symbols.getWeekdays ().length);
    assertEquals (8, symbols.getShortWeekdays ().length);
    assertEquals ("", symbols.getWeekdays ()[0]);
    assertEquals ("", symbols.getShortWeekdays ()[0]);

    assertEquals ("četvrtak", symbols.getWeekdays ()[5]);
    assertEquals ("čet", symbols.getShortWeekdays ()[5]);
  }

  @Test
  public void serbianWeekdays ()
  {
    final DateFormatSymbolsProvider provider = new DateFormatSymbolsProvider_sh_RS ();
    final DateFormatSymbols symbols = provider.getInstance (new Locale (SERBOCROATIAN_LANGUAGE, SERBIA));

    assertEquals (8, symbols.getWeekdays ().length);
    assertEquals (8, symbols.getShortWeekdays ().length);
    assertEquals ("", symbols.getWeekdays ()[0]);
    assertEquals ("", symbols.getShortWeekdays ()[0]);

    assertEquals ("sreda", symbols.getWeekdays ()[4]);
    assertEquals ("sre", symbols.getShortWeekdays ()[4]);

    assertEquals ("nedelja", symbols.getWeekdays ()[1]);
    assertEquals ("ned", symbols.getShortWeekdays ()[1]);

  }

  @Test
  public void checkSpiDeclaration ()
  {
    // check that SPI "declaration file" exists
    final URL spifile = Thread.currentThread ()
                              .getContextClassLoader ()
                              .getResource ("META-INF/services/java.text.spi.DateFormatSymbolsProvider");
    assertNotNull (spifile);
  }

  @Test
  public void checkEnglishWeekdays ()
  {
    final DateFormatSymbols symbols = DateFormatSymbols.getInstance (new Locale ("en"));
    assertEquals (8, symbols.getWeekdays ().length);
    assertEquals (8, symbols.getShortWeekdays ().length);

    assertEquals ("", symbols.getWeekdays ()[0]);
    assertEquals ("Sunday", symbols.getWeekdays ()[1]);
    assertEquals ("Saturday", symbols.getWeekdays ()[7]);
  }
}
