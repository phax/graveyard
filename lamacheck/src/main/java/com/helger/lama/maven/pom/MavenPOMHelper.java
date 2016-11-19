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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.string.StringHelper;
import com.helger.lama.maven.CMaven;
import com.helger.lama.maven.IMavenSerializableObject;
import com.helger.lama.maven.artifact.IMavenArtifact;
import com.helger.xml.microdom.IMicroElement;

@Immutable
public final class MavenPOMHelper
{
  private MavenPOMHelper ()
  {}

  /**
   * Append the fields of this object to the passed parent element.<br>
   * Note: this interface is not derived from {@link IMavenSerializableObject}
   * since the semantics are different. The serializable object is for self
   * contained objects, whereas an artifact is part of other objects!
   *
   * @param aArtifact
   *        The artifact to be serialized
   * @param eParent
   *        The parent element to append to.
   */
  public static void serializeArtifact (@Nonnull final IMavenArtifact aArtifact, @Nonnull final IMicroElement eParent)
  {
    if (aArtifact.getGroupID () != null)
      eParent.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_GROUPID).appendText (aArtifact.getGroupID ());

    eParent.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_ARTIFACTID).appendText (aArtifact.getArtifactID ());

    final String sVersion = aArtifact.getVersion ();
    if (StringHelper.hasText (sVersion))
      eParent.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_VERSION).appendText (sVersion);

    final String sClassifier = aArtifact.getClassifier ();
    if (StringHelper.hasText (sClassifier))
      eParent.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_CLASSIFIER).appendText (sClassifier);
  }
}
