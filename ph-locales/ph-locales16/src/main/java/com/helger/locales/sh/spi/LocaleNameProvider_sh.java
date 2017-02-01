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
package com.helger.locales.sh.spi;

import com.helger.locales.localedata.AbstractLocaleDataLocaleNameProvider;
import com.helger.locales.localedata.ILocaleData;
import com.helger.locales.sh.LocalDataSerbocroatian;

/**
 * A LocaleNameProvider for the Serbocroatian languages (primarily Bosnian
 * usage).
 *
 * @author Klaus Brunner
 */
public class LocaleNameProvider_sh extends AbstractLocaleDataLocaleNameProvider
{
  private static final ILocaleData s_aLocaleData = new LocalDataSerbocroatian ();

  @Override
  protected ILocaleData getLocaleData ()
  {
    return s_aLocaleData;
  }
}
