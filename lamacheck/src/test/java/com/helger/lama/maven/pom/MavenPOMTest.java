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
import com.helger.lama.maven.CMaven;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.serialize.MicroReader;

public final class MavenPOMTest
{
  @Test
  public void testReadPOM ()
  {
    // Read the pom.xml of this project :)
    final IMicroDocument aDoc = MicroReader.readMicroXML (new FileSystemResource ("pom.xml"));
    assertNotNull (aDoc);

    // read project
    final IMicroElement eProject = aDoc.getDocumentElement ();
    assertNotNull (eProject);
    assertEquals (CMaven.POM_XMLNS, eProject.getNamespaceURI ());
    assertEquals (CMavenXML.ELEMENT_PROJECT, eProject.getTagName ());
  }
}
