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
package com.helger.locales.gl.spi;

import java.util.TimeZone;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.locales.gl.GalicianLocales;

/**
 * Test class for class {@link GalicianTimeZoneNameProvider}.
 */
public final class GalicianTimeZoneNameProviderTest
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (GalicianTimeZoneNameProviderTest.class);

  @Test
  public void testTimeZoneNames () throws Exception
  {
    final TimeZone aTimeZone = TimeZone.getTimeZone ("CEST");
    s_aLogger.info (aTimeZone.getDisplayName (GalicianLocales.GALICIAN_ES));
    s_aLogger.info (aTimeZone.getDisplayName (GalicianLocales.CASTILIAN));
    final GalicianTimeZoneNameProvider prov = new GalicianTimeZoneNameProvider ();
    s_aLogger.info ("-" + prov.getDisplayName ("CEST", false, TimeZone.LONG, GalicianLocales.GALICIAN_ES) + "-");
  }
}
