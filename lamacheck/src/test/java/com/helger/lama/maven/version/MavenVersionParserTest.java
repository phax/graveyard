/**
 * Copyright (C) 2011-2014 Philip Helger <philip[at]helger[dot]com>
 * All Rights Reserved
 *
 * This file is part of the LaMaCheck service.
 *
 * Proprietary and confidential.
 *
 * It can not be copied and/or distributed without the
 * express permission of Philip Helger.
 *
 * Unauthorized copying of this file, via any medium is
 * strictly prohibited.
 */
package com.helger.lama.maven.version;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.commons.version.Version;

/**
 * Test class for class {@link MavenVersionParser}.
 *
 * @author Philip Helger
 */
public final class MavenVersionParserTest
{
  @Test
  public void testParse ()
  {
    assertNotNull (MavenVersionParser.parse ("2.1.2"));
    assertNotNull (MavenVersionParser.parse ("2.2-beta-5"));
    assertNotNull (MavenVersionParser.parse ("2.0-beta-2"));
    assertNotNull (MavenVersionParser.parse ("1.0.0-beta-1"));
    assertNotNull (MavenVersionParser.parse ("2.0.0-M2.1"));
    assertNotNull (MavenVersionParser.parse ("1.7R2"));
    assertNotNull (MavenVersionParser.parse ("1.0-dev-2.20021231.045254"));
    assertNotNull (MavenVersionParser.parse ("1.5R4.1"));
    assertNotNull (MavenVersionParser.parse ("9.1.1.B60.25.p2"));
    assertEquals (MavenVersionParser.parse ("9.1.1.B60.25.p2"), MavenVersionParser.parse ("9.1.1.B60.25.p2"));
    assertFalse (MavenVersionParser.parse ("9.1.1.B60.25.p2").equals (MavenVersionParser.parse ("9.1.1.B60.25.p1")));
    assertEquals (new Version (2, 2, 3, "1-promoted-b01"), MavenVersionParser.parse ("2.2.3-1-promoted-b01"));
    assertEquals (new Version (2, 2, 3, "1.2"), MavenVersionParser.parse ("2.2.3.1.2"));
    assertEquals (new Version (9, 4, 6, MavenVersionParser.RELEASE_QUALIFIER), MavenVersionParser.parse ("r946"));
    assertEquals (new Version (15, 5, 4, MavenVersionParser.RELEASE_QUALIFIER), MavenVersionParser.parse ("r1554"));
    assertEquals (new Version (15, 5, 4, "M"), MavenVersionParser.parse ("r1554M"));
    assertEquals (new Version (2004, 5, 19, MavenVersionParser.RELEASE_QUALIFIER), MavenVersionParser.parse ("20040519"));
    assertEquals (new Version (2004, 5, 19, MavenVersionParser.RELEASE_QUALIFIER), MavenVersionParser.parse ("v20040519"));
    assertEquals (new Version (1, 0, 0, "incubating-M2.1"), MavenVersionParser.parse ("1.0-incubating-M2.1"));
    assertEquals (new Version (1, 0, 0, "incubating-M2.1"), MavenVersionParser.parse ("00000001.0000000-incubating-M2.1"));
    assertEquals (new Version (1, 0, 0, "incubating-M2.1"), MavenVersionParser.parse ("00000001-incubating-M2.1"));
  }

  @Test
  public void testCompare ()
  {
    assertTrue (MavenVersionParser.parse ("1.7R2").isGreaterThan (MavenVersionParser.parse ("1.7R1")));
    assertTrue (MavenVersionParser.parse ("2.2.3-1-promoted-b01").isGreaterThan (MavenVersionParser.parse ("2.2.3")));
    assertTrue (MavenVersionParser.parse ("0.1.44-1").isGreaterThan (MavenVersionParser.parse ("0.1.44")));
  }
}
