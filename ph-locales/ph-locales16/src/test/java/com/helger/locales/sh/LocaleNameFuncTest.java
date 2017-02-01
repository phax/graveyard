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
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.util.Locale;
import java.util.spi.LocaleNameProvider;

import org.junit.Test;

import com.helger.locales.sh.spi.LocaleNameProvider_sh;

public final class LocaleNameFuncTest
{
  private static final Locale BOSNIAN_LOCALE = new Locale ("bs");

  @Test (expected = IllegalArgumentException.class)
  public void providerChecksWrongLocale ()
  {
    final LocaleNameProvider provider = new LocaleNameProvider_sh ();
    assertNull (provider.getDisplayCountry ("BA", new Locale ("hr")));
  }

  @Test
  public void providerChecksBogusCountry ()
  {
    final LocaleNameProvider provider = new LocaleNameProvider_sh ();
    assertNull (provider.getDisplayCountry ("??", BOSNIAN_LOCALE));
  }

  @Test
  public void providerChecksBogusLanguage ()
  {
    final LocaleNameProvider provider = new LocaleNameProvider_sh ();
    assertNull (provider.getDisplayLanguage ("??", BOSNIAN_LOCALE));
  }

  @Test (expected = IllegalArgumentException.class)
  public void providerChecksBadLanguageFormat ()
  {
    final LocaleNameProvider provider = new LocaleNameProvider_sh ();
    provider.getDisplayLanguage ("srp", BOSNIAN_LOCALE);
  }

  @Test (expected = IllegalArgumentException.class)
  public void providerChecksBadCountryFormat ()
  {
    final LocaleNameProvider provider = new LocaleNameProvider_sh ();
    provider.getDisplayLanguage ("AUT", BOSNIAN_LOCALE);
  }

  @Test
  public void providerKnowsLanguages ()
  {
    final LocaleNameProvider provider = new LocaleNameProvider_sh ();

    assertEquals ("bosanski", provider.getDisplayLanguage ("bs", BOSNIAN_LOCALE));
    assertEquals ("engleski", provider.getDisplayLanguage ("en", BOSNIAN_LOCALE));
    assertEquals ("hrvatski", provider.getDisplayLanguage ("hr", BOSNIAN_LOCALE));
    assertEquals ("srpski", provider.getDisplayLanguage ("sr", BOSNIAN_LOCALE));
  }

  @Test
  public void providerKnowsCountries ()
  {
    final LocaleNameProvider provider = new LocaleNameProvider_sh ();

    assertEquals ("Bosna i Hercegovina", provider.getDisplayCountry ("BA", BOSNIAN_LOCALE));
    assertEquals ("Hrvatska", provider.getDisplayCountry ("HR", BOSNIAN_LOCALE));
    assertEquals ("Srbija", provider.getDisplayCountry ("RS", BOSNIAN_LOCALE));
    assertEquals ("Slovenija", provider.getDisplayCountry ("SI", BOSNIAN_LOCALE));
    assertEquals ("Crna Gora", provider.getDisplayCountry ("ME", BOSNIAN_LOCALE));
  }

  @Test
  public void checkSpiDeclaration ()
  {
    // check that SPI "declaration file" exists
    final URL spifile = Thread.currentThread ()
                              .getContextClassLoader ()
                              .getResource ("META-INF/services/java.util.spi.LocaleNameProvider");
    assertNotNull (spifile);
  }
}
