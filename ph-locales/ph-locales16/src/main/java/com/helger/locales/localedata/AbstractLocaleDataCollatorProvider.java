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

import java.text.Collator;
import java.text.spi.CollatorProvider;
import java.util.Locale;

public abstract class AbstractLocaleDataCollatorProvider extends CollatorProvider
{
  /**
   * Get {@link ILocaleData} for this instance. Must be provided by concrete
   * implementations.
   *
   * @return LocaleData Never <code>null</code>.
   */
  protected abstract ILocaleData getLocaleData ();

  @Override
  public final Collator getInstance (final Locale aLocale)
  {
    getLocaleData ().checkLocaleSupported (aLocale);
    return getLocaleData ().getCollator ();
  }

  @Override
  public final Locale [] getAvailableLocales ()
  {
    return getLocaleData ().getAllSupportedLocales ();
  }
}
