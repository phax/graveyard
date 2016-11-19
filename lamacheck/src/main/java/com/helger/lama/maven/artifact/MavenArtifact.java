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
package com.helger.lama.maven.artifact;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;

/**
 * Represents a single Maven2 artifact.
 *
 * @author Philip Helger
 */
@Immutable
public final class MavenArtifact implements IMavenArtifact
{
  private final String m_sGroupID;
  private final String m_sArtifactID;
  private final String m_sVersion;
  private final String m_sClassifier;

  public MavenArtifact (@Nullable final String sGroupID, @Nonnull final String sArtifactID)
  {
    this (sGroupID, sArtifactID, null, null);
  }

  public MavenArtifact (@Nullable final String sGroupID, @Nonnull final String sArtifactID, @Nullable final String sVersion)
  {
    this (sGroupID, sArtifactID, sVersion, null);
  }

  public MavenArtifact (@Nullable final String sGroupID,
                        @Nonnull final String sArtifactID,
                        @Nullable final String sVersion,
                        @Nullable final String sClassifier)
  {
    // Group ID may be empty, if derived from a parent POM
    if (StringHelper.hasNoTextAfterTrim (sArtifactID))
      throw new IllegalArgumentException ("artifactId may not be empty");
    m_sGroupID = StringHelper.trim (sGroupID);
    m_sArtifactID = sArtifactID.trim ();
    m_sVersion = StringHelper.trim (sVersion);
    m_sClassifier = StringHelper.trim (sClassifier);
  }

  @Nullable
  public String getGroupID ()
  {
    return m_sGroupID;
  }

  @Nonnull
  public String getArtifactID ()
  {
    return m_sArtifactID;
  }

  @Nullable
  public String getVersion ()
  {
    return m_sVersion;
  }

  @Nullable
  public String getClassifier ()
  {
    return m_sClassifier;
  }

  public boolean containsUnresolvedVariable ()
  {
    if (m_sGroupID != null && m_sGroupID.contains ("${"))
      return true;
    if (m_sArtifactID.contains ("${"))
      return true;
    if (m_sVersion != null && m_sVersion.contains ("${"))
      return true;
    if (m_sClassifier != null && m_sClassifier.contains ("${"))
      return true;
    return false;
  }

  @Nonnull
  public MavenArtifact createWithNewGroupID (@Nullable final String sGroupID)
  {
    return new MavenArtifact (sGroupID, m_sArtifactID, m_sVersion, m_sClassifier);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MavenArtifact))
      return false;
    final MavenArtifact rhs = (MavenArtifact) o;
    return EqualsHelper.equals (m_sGroupID, rhs.m_sGroupID) &&
           m_sArtifactID.equals (rhs.m_sArtifactID) &&
           EqualsHelper.equals (m_sVersion, rhs.m_sVersion) &&
           EqualsHelper.equals (m_sClassifier, rhs.m_sClassifier);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sGroupID).append (m_sArtifactID).append (m_sVersion).append (m_sClassifier).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).appendIfNotNull ("groupID", m_sGroupID)
                                       .append ("artifactID", m_sArtifactID)
                                       .appendIfNotNull ("version", m_sVersion)
                                       .appendIfNotNull ("classifier", m_sClassifier)
                                       .toString ();
  }
}
