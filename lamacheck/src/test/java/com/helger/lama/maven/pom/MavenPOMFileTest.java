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
package com.helger.lama.maven.pom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.helger.commons.io.resource.FileSystemResource;
import com.helger.lama.maven.EMavenPackaging;
import com.helger.lama.maven.artifact.IMavenArtifact;

/**
 * Test class for class {@link MavenPOMFile}.
 *
 * @author Philip Helger
 */
public final class MavenPOMFileTest
{
  @Test
  public void testRead ()
  {
    final MavenPOMFile f = new MavenPOMFile (new FileSystemResource ("pom.xml"));
    assertNotNull (f);
    assertNotNull (f.getPOM ());

    assertEquals (EMavenPackaging.WAR, f.getPOM ().getPackagingOrDefault ());

    IMavenArtifact a = f.getPOM ().getArtifact ();
    assertNotNull (a);
    assertEquals (null, a.getGroupID ());
    assertEquals ("lamacheck", a.getArtifactID ());

    a = f.getPOM ().getEffectiveArtifact ();
    assertNotNull (a);
    assertEquals ("com.helger", a.getGroupID ());
    assertEquals ("lamacheck", a.getArtifactID ());
  }
}
