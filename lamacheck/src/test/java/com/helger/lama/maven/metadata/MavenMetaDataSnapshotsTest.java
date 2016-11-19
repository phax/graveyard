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
package com.helger.lama.maven.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.helger.commons.io.resource.ClassPathResource;
import com.helger.lama.maven.artifact.IMavenArtifact;
import com.helger.lama.maven.artifact.MavenArtifact;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.serialize.MicroReader;

public final class MavenMetaDataSnapshotsTest
{
  @Test
  public void testBasic1 ()
  {
    final IMavenArtifact aArtifact = new MavenArtifact ("org.apache.camel", "camel-ahc");
    final IMicroDocument aDoc = MicroReader.readMicroXML (new ClassPathResource ("metadata/metadata-snapshots01.xml"));
    assertNotNull (aDoc);
    final MavenMetaDataSnapshots aMetadata = MavenMetaDataSnapshots.readXML (aArtifact, aDoc);
    assertNotNull (aMetadata);
    assertEquals ("camel-ahc-2.12-20130408.025924-4.pom", aMetadata.getSnapshotVersionFilename (null, "pom"));
    assertEquals ("camel-ahc-2.12-20130408.025924-4-camelComponent.properties",
                  aMetadata.getSnapshotVersionFilename ("camelComponent", "properties"));
  }

  @Test
  public void testBasic2 ()
  {
    final IMavenArtifact aArtifact = new MavenArtifact ("com.sun.faces", "jsf-impl");
    final IMicroDocument aDoc = MicroReader.readMicroXML (new ClassPathResource ("metadata/metadata-snapshots02.xml"));
    assertNotNull (aDoc);
    final MavenMetaDataSnapshots aMetadata = MavenMetaDataSnapshots.readXML (aArtifact, aDoc);
    assertNotNull (aMetadata);
    assertEquals ("jsf-impl-2.2.1-20130406.074538-8.pom", aMetadata.getSnapshotVersionFilename (null, "pom"));
  }

  @Test
  public void testBasic3 ()
  {
    final IMavenArtifact aArtifact = new MavenArtifact ("org.apache.directory.buildtools", "directory-checkstyle");
    final IMicroDocument aDoc = MicroReader.readMicroXML (new ClassPathResource ("metadata/metadata-snapshots03.xml"));
    assertNotNull (aDoc);
    final MavenMetaDataSnapshots aMetadata = MavenMetaDataSnapshots.readXML (aArtifact, aDoc);
    assertNotNull (aMetadata);
    assertEquals ("directory-checkstyle-0.1-SNAPSHOT.pom", aMetadata.getSnapshotVersionFilename (null, "pom"));
  }
}
