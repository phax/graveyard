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
package com.helger.locales.supplementary.tools;

import java.io.File;

import com.helger.commons.string.StringHelper;

public abstract class AbstractCLDRSupport
{
  protected static final File CLDR_BASE = new File ("src/test/resources/cldr/27.0.1/");

  protected static final String maskForJava (final String s)
  {
    final StringBuilder aSB = new StringBuilder (s.length () * 2);
    for (final char c : s.toCharArray ())
      if (c < 32 || c > 127)
        aSB.append ("\\u").append (StringHelper.getHexStringLeadingZero (c, 4));
      else
      {
        if (c == '"' || c == '\\')
          aSB.append ('\\');
        aSB.append (c);
      }
    return aSB.toString ();
  }
}