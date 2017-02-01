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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.text.Collator;
import java.text.spi.CollatorProvider;
import java.util.Locale;

import org.junit.Test;

import com.helger.locales.sh.spi.CollatorProvider_sh;

public final class CollatorFuncTest
{
  private static final Locale BOSNIAN_LOCALE = new Locale ("bs");

  @Test (expected = IllegalArgumentException.class)
  public void providerChecksInvalidLocale ()
  {
    final CollatorProvider provider = new CollatorProvider_sh ();
    provider.getInstance (new Locale ("??", "!!"));
  }

  @Test (expected = NullPointerException.class)
  public void providerChecksNullLocale ()
  {
    final CollatorProvider provider = new CollatorProvider_sh ();
    provider.getInstance (null);
  }

  @Test
  public void providerValidLocale ()
  {
    final CollatorProvider provider = new CollatorProvider_sh ();
    final Collator collator = provider.getInstance (BOSNIAN_LOCALE);
    assertNotNull (collator);
  }

  @Test
  public void testSimpleComparisons ()
  {
    final CollatorProvider provider = new CollatorProvider_sh ();
    final Collator collator = provider.getInstance (BOSNIAN_LOCALE);

    // simple ASCII - if this fails, things are seriously wrong
    assertTrue (collator.compare ("a", "a") == 0);
    assertTrue (collator.compare ("a", "b") < 0);
    assertTrue (collator.compare ("a", "z") < 0);
    assertTrue (collator.compare ("A", "Z") < 0);

    // ASCII - same char, different cases
    assertTrue (collator.compare ("a", "A") < 0);
  }

  @Test
  public void testLocaleSpecificComparisons ()
  {
    final CollatorProvider provider = new CollatorProvider_sh ();

    final Collator collator = provider.getInstance (BOSNIAN_LOCALE);

    assertTrue (collator.compare ("c", "č") < 0);
    assertTrue (collator.compare ("č", "ć") < 0);
    assertTrue (collator.compare ("d", "dž") < 0); // decomposed dž
    assertTrue (collator.compare ("d", "\u01c6") < 0); // dž digraph
    assertTrue (collator.compare ("d", "đ") < 0);
    assertTrue (collator.compare ("lz", "lj") < 0); // decomposed lj
    assertTrue (collator.compare ("lz", "\u01c9") < 0); // lj digraph
    assertTrue (collator.compare ("nz", "nj") < 0); // decomposed nj
    assertTrue (collator.compare ("nz", "\u01cc") < 0); // nj digraph

    assertTrue (collator.compare ("s", "š") < 0);
    assertTrue (collator.compare ("z", "ž") < 0);

    // TODO: more tests with all sorts of digraph/non-digraph situations,
    // different cases etc.
  }

  @Test
  public void checkSpiDeclaration ()
  {
    // check that SPI "declaration file" exists
    final URL spifile = Thread.currentThread ()
                              .getContextClassLoader ()
                              .getResource ("META-INF/services/java.text.spi.CollatorProvider");
    assertNotNull (spifile);
  }
}
