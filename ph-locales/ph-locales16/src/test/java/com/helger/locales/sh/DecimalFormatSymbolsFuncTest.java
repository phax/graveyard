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
import java.text.DecimalFormatSymbols;
import java.text.spi.DecimalFormatSymbolsProvider;
import java.util.Locale;

import org.junit.Test;

import com.helger.locales.sh.spi.DecimalFormatSymbolsProvider_sh;

public final class DecimalFormatSymbolsFuncTest
{
  private static final Locale BOSNIAN_LOCALE = new Locale ("bs");

  @Test
  public void getLegalSymbolsProviderInstance ()
  {
    final DecimalFormatSymbolsProvider provider = new DecimalFormatSymbolsProvider_sh ();
    assertNotNull (provider.getInstance (BOSNIAN_LOCALE));
  }

  @Test (expected = IllegalArgumentException.class)
  public void getIllegalDateInstance ()
  {
    final DecimalFormatSymbolsProvider provider = new DecimalFormatSymbolsProvider_sh ();
    provider.getInstance (new Locale ("??"));
  }

  @Test
  public void testSymbols ()
  {
    final DecimalFormatSymbolsProvider provider = new DecimalFormatSymbolsProvider_sh ();
    final DecimalFormatSymbols symbols = provider.getInstance (BOSNIAN_LOCALE);
    assertEquals (',', symbols.getDecimalSeparator ());
    assertEquals ('.', symbols.getGroupingSeparator ());
  }

  @Test
  public void checkSpiDeclaration ()
  {
    // check that SPI "declaration file" exists
    final URL spifile = Thread.currentThread ()
                              .getContextClassLoader ()
                              .getResource ("META-INF/services/java.text.spi.DecimalFormatSymbolsProvider");
    assertNotNull (spifile);
  }
}
