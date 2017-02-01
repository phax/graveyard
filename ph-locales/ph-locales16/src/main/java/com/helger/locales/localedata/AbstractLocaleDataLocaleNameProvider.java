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

import java.util.Locale;
import java.util.spi.LocaleNameProvider;

public abstract class AbstractLocaleDataLocaleNameProvider extends LocaleNameProvider
{
  /**
   * Get {@link ILocaleData} for this instance. Must be provided by concrete
   * implementations.
   *
   * @return LocaleData Never <code>null</code>.
   */
  protected abstract ILocaleData getLocaleData ();

  private static boolean _badCountryCodeFormat (final String sCountryCode)
  {
    return sCountryCode.length () != 2 ||
           Character.isLowerCase (sCountryCode.charAt (0)) ||
           Character.isLowerCase (sCountryCode.charAt (1));
  }

  @Override
  public final String getDisplayCountry (final String sCountryCode, final Locale aLocale)
  {
    getLocaleData ().checkLocaleSupported (aLocale);

    if (_badCountryCodeFormat (sCountryCode))
      throw new IllegalArgumentException ("country code not in required format");

    return getLocaleData ().getDisplayCountry (sCountryCode);
  }

  private static boolean _badLanguageCodeFormat (final String sLanguageCode)
  {
    return sLanguageCode.length () != 2 ||
           Character.isUpperCase (sLanguageCode.charAt (0)) ||
           Character.isUpperCase (sLanguageCode.charAt (1));
  }

  @Override
  public final String getDisplayLanguage (final String sLanguageCode, final Locale aLocale)
  {
    getLocaleData ().checkLocaleSupported (aLocale);

    if (_badLanguageCodeFormat (sLanguageCode))
      throw new IllegalArgumentException ("language code not in required format");

    return getLocaleData ().getDisplayLanguage (sLanguageCode);
  }

  @Override
  public final String getDisplayVariant (final String sVariant, final Locale aLocale)
  {
    getLocaleData ().checkLocaleSupported (aLocale);

    return sVariant;
  }

  @Override
  public final Locale [] getAvailableLocales ()
  {
    return getLocaleData ().getAllSupportedLocales ();
  }
}
