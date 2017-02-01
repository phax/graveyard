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
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.microdom.IMicroDocument;
import com.helger.commons.microdom.IMicroElement;
import com.helger.commons.microdom.serialize.MicroReader;
import com.helger.commons.regex.RegExHelper;
import com.helger.commons.xml.EXMLParserFeature;
import com.helger.commons.xml.serialize.read.SAXReaderSettings;

public class MainReadCLDRCountryNames extends AbstractCLDRSupport
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MainReadCLDRCountryNames.class);

  public static void main (final String [] args)
  {
    final String sLocale = "sq_XK";
    final IMicroDocument aDoc = MicroReader.readMicroXML (new File (CLDR_BASE, "common/main/" + sLocale + ".xml"),
                                                          new SAXReaderSettings ().setFeatureValue (EXMLParserFeature.VALIDATION,
                                                                                                    false)
                                                                                  .setFeatureValue (EXMLParserFeature.LOAD_EXTERNAL_DTD,
                                                                                                    false));

    final Map <String, String> aData = new TreeMap <String, String> ();
    final IMicroElement eLocaleDisplayNames = aDoc.getDocumentElement ().getFirstChildElement ("localeDisplayNames");
    if (eLocaleDisplayNames != null)
      for (final IMicroElement e : eLocaleDisplayNames.getFirstChildElement ("territories")
                                                      .getAllChildElements ("territory"))
      {
        final String sType = e.getAttributeValue ("type");
        // Use only country codes
        if (RegExHelper.stringMatchesPattern ("[A-Z]+", sType))
        {
          // We don't care about alternatives
          if (!e.hasAttribute ("alt"))
            aData.put (sType, e.getTextContentTrimmed ());
        }
      }

    final StringBuilder aSB = new StringBuilder ();
    aSB.append ("final Map <String, String> aMap = new HashMap <String, String> (" + aData.size () + ");\n");
    for (final Map.Entry <String, String> aEntry : aData.entrySet ())
      aSB.append ("aMap.put (\"" + aEntry.getKey () + "\", \"" + maskForJava (aEntry.getValue ()) + "\");\n");
    s_aLogger.info (aSB.toString ());
  }
}
