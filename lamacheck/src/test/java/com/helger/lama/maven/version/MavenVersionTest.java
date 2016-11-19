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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link MavenVersion}.
 *
 * @author Philip Helger
 */
public final class MavenVersionTest
{
  @Test
  public void testIsRelease ()
  {
    assertTrue (new MavenVersion ("2.1.2").isReleaseVersion ());
    assertFalse (new MavenVersion ("2.1.2-b01").isReleaseVersion ());
    assertFalse (new MavenVersion ("2.1.2-b57").isReleaseVersion ());
    assertFalse (new MavenVersion ("2.1.2-b99999999999").isReleaseVersion ());
    assertFalse (new MavenVersion ("7.0.0.pre5").isReleaseVersion ());
    assertFalse (new MavenVersion ("1.0-incubating-M2.1").isReleaseVersion ());
  }
}
