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
package com.helger.locales.proxy;

import java.text.NumberFormat;
import java.text.spi.NumberFormatProvider;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * An implementation of {@link NumberFormatProvider} that proxies the methods to
 * the implementations of another aLocale.
 *
 * @author Philip Helger
 */
public abstract class AbstractProxyNumberFormatProvider extends NumberFormatProvider
{
  private final Set <Locale> m_aSupportedLocales = new LinkedHashSet <Locale> ();
  private final Locale m_aProxyLocale;

  public AbstractProxyNumberFormatProvider (final Collection <Locale> aSupportedLocales, final Locale aProxyLocale)
  {
    if (aSupportedLocales == null || aSupportedLocales.isEmpty ())
      throw new IllegalArgumentException ("SupportedLocale");
    if (aProxyLocale == null)
      throw new NullPointerException ("ProxyLocale");

    m_aSupportedLocales.addAll (aSupportedLocales);
    m_aProxyLocale = aProxyLocale;
  }

  protected final Set <Locale> getAllSupportedLocales ()
  {
    return new LinkedHashSet <Locale> (m_aSupportedLocales);
  }

  protected final Locale getProxyLocale ()
  {
    return m_aProxyLocale;
  }

  @Override
  public Locale [] getAvailableLocales ()
  {
    // Always return a new array
    return m_aSupportedLocales.toArray (new Locale [m_aSupportedLocales.size ()]);
  }

  @Override
  public NumberFormat getCurrencyInstance (final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ();

    if (m_aSupportedLocales.contains (aLocale))
      return NumberFormat.getCurrencyInstance (m_aProxyLocale);

    throw new IllegalArgumentException ("Locale \"" +
                                        aLocale +
                                        "\" " +
                                        "is not one of the supported locales (" +
                                        m_aSupportedLocales +
                                        ")");
  }

  @Override
  public NumberFormat getIntegerInstance (final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ();

    if (m_aSupportedLocales.contains (aLocale))
      return NumberFormat.getIntegerInstance (m_aProxyLocale);

    throw new IllegalArgumentException ("Locale \"" +
                                        aLocale +
                                        "\" " +
                                        "is not one of the supported locales (" +
                                        m_aSupportedLocales +
                                        ")");
  }

  @Override
  public NumberFormat getNumberInstance (final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ();

    if (m_aSupportedLocales.contains (aLocale))
      return NumberFormat.getNumberInstance (m_aProxyLocale);

    throw new IllegalArgumentException ("Locale \"" +
                                        aLocale +
                                        "\" " +
                                        "is not one of the supported locales (" +
                                        m_aSupportedLocales +
                                        ")");
  }

  @Override
  public NumberFormat getPercentInstance (final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ();

    if (m_aSupportedLocales.contains (aLocale))
      return NumberFormat.getPercentInstance (m_aProxyLocale);

    throw new IllegalArgumentException ("Locale \"" +
                                        aLocale +
                                        "\" " +
                                        "is not one of the supported locales (" +
                                        m_aSupportedLocales +
                                        ")");
  }
}
