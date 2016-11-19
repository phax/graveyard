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
import javax.annotation.Nullable;

import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;
import com.helger.lama.maven.CMaven;
import com.helger.lama.maven.IMavenSerializableObject;
import com.helger.lama.maven.artifact.IMavenArtifact;
import com.helger.lama.maven.artifact.MavenArtifact;
import com.helger.xml.microdom.IMicroElement;

public final class POMExtension implements IMavenSerializableObject
{
  private final IMavenArtifact m_aArtifact;

  public POMExtension (@Nullable final String sGroupID, @Nonnull final String sArtifactID)
  {
    this (new MavenArtifact (sGroupID, sArtifactID));
  }

  public POMExtension (@Nullable final String sGroupID, @Nonnull final String sArtifactID, @Nullable final String sVersion)
  {
    this (new MavenArtifact (sGroupID, sArtifactID, sVersion));
  }

  public POMExtension (@Nullable final String sGroupID,
                       @Nonnull final String sArtifactID,
                       @Nullable final String sVersion,
                       @Nullable final String sClassifier)
  {
    this (new MavenArtifact (sGroupID, sArtifactID, sVersion, sClassifier));
  }

  public POMExtension (@Nonnull final IMavenArtifact aArtifact)
  {
    if (aArtifact == null)
      throw new NullPointerException ("artifact");
    m_aArtifact = aArtifact;
  }

  @Nonnull
  public IMavenArtifact getArtifact ()
  {
    return m_aArtifact;
  }

  public void appendToElement (final IMicroElement aElement)
  {
    final IMicroElement eDependency = aElement.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_EXTENSION);
    MavenPOMHelper.serializeArtifact (m_aArtifact, eDependency);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof POMExtension))
      return false;
    final POMExtension rhs = (POMExtension) o;
    return m_aArtifact.equals (rhs.m_aArtifact);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aArtifact).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("artifact", m_aArtifact).toString ();
  }
}
