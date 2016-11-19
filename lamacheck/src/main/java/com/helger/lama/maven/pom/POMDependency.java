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

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.ReturnsImmutableObject;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;
import com.helger.lama.maven.CMaven;
import com.helger.lama.maven.EMavenPackaging;
import com.helger.lama.maven.EMavenScope;
import com.helger.lama.maven.IMavenSerializableObject;
import com.helger.lama.maven.artifact.IMavenArtifact;
import com.helger.lama.maven.artifact.MavenArtifact;
import com.helger.xml.microdom.IMicroElement;

public final class POMDependency implements IMavenSerializableObject
{
  private final IMavenArtifact m_aArtifact;
  private EMavenPackaging m_eType;
  private EMavenScope m_eScope;
  private boolean m_bOptional = false;
  private ICommonsList <IMavenArtifact> m_aExclusions;

  public POMDependency (@Nullable final String sGroupID, @Nonnull final String sArtifactID)
  {
    this (new MavenArtifact (sGroupID, sArtifactID));
  }

  public POMDependency (@Nullable final String sGroupID,
                        @Nonnull final String sArtifactID,
                        @Nullable final String sVersion)
  {
    this (new MavenArtifact (sGroupID, sArtifactID, sVersion));
  }

  public POMDependency (@Nullable final String sGroupID,
                        @Nonnull final String sArtifactID,
                        @Nullable final String sVersion,
                        @Nullable final String sClassifier)
  {
    this (new MavenArtifact (sGroupID, sArtifactID, sVersion, sClassifier));
  }

  public POMDependency (@Nonnull final IMavenArtifact aArtifact)
  {
    if (aArtifact == null)
      throw new NullPointerException ("artifact");
    m_aArtifact = aArtifact;
  }

  public POMDependency (@Nonnull final IMavenArtifact aArtifact, @Nonnull final POMDependency aDep)
  {
    this (aArtifact);
    m_eType = aDep.m_eType;
    m_eScope = aDep.m_eScope;
    m_bOptional = aDep.m_bOptional;
    if (aDep.m_aExclusions != null)
      m_aExclusions = new CommonsArrayList<> (aDep.m_aExclusions);
  }

  @Nonnull
  public IMavenArtifact getArtifact ()
  {
    return m_aArtifact;
  }

  @Nonnull
  public POMDependency setType (@Nullable final EMavenPackaging eType)
  {
    m_eType = eType;
    return this;
  }

  public EMavenPackaging getType ()
  {
    return m_eType;
  }

  @Nonnull
  public POMDependency setScope (@Nullable final EMavenScope eScope)
  {
    m_eScope = eScope;
    return this;
  }

  @Nullable
  public EMavenScope getScope ()
  {
    return m_eScope;
  }

  @Nonnull
  public POMDependency setOptional (final boolean bOptional)
  {
    m_bOptional = bOptional;
    return this;
  }

  public boolean isOptional ()
  {
    return m_bOptional;
  }

  public void addExclusion (@Nonnull final IMavenArtifact aArtifact)
  {
    if (aArtifact == null)
      throw new NullPointerException ("artifact");
    if (m_aExclusions == null)
      m_aExclusions = new CommonsArrayList<> ();
    m_aExclusions.add (aArtifact);
  }

  @Nonnull
  @ReturnsImmutableObject
  public List <IMavenArtifact> getExclusions ()
  {
    return CollectionHelper.makeUnmodifiableNotNull (m_aExclusions);
  }

  public void appendToElement (final IMicroElement aElement)
  {
    final IMicroElement eDependency = aElement.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_DEPENDENCY);
    MavenPOMHelper.serializeArtifact (m_aArtifact, eDependency);
    if (m_eType != null)
      eDependency.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_TYPE).appendText (m_eType.getID ());
    if (m_eScope != null)
      eDependency.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_SCOPE).appendText (m_eScope.getID ());
    if (m_bOptional)
      eDependency.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_OPTIONAL).appendText (Boolean.TRUE.toString ());

    if (m_aExclusions != null)
    {
      final IMicroElement eExclusions = eDependency.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_EXCLUSIONS);
      for (final IMavenArtifact aExclusion : m_aExclusions)
      {
        final IMicroElement eExclusion = eExclusions.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_EXCLUSION);
        MavenPOMHelper.serializeArtifact (aExclusion, eExclusion);
      }
    }
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof POMDependency))
      return false;
    final POMDependency rhs = (POMDependency) o;
    return m_aArtifact.equals (rhs.m_aArtifact) &&
           EqualsHelper.equals (m_eType, rhs.m_eType) &&
           EqualsHelper.equals (m_eScope, rhs.m_eScope) &&
           EqualsHelper.equals (m_aExclusions, rhs.m_aExclusions);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aArtifact)
                                       .append (m_eType)
                                       .append (m_eScope)
                                       .append (m_aExclusions)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("artifact", m_aArtifact)
                                       .appendIfNotNull ("packaging", m_eType)
                                       .appendIfNotNull ("scope", m_eScope)
                                       .appendIfNotNull ("exclusions", m_aExclusions)
                                       .toString ();
  }
}
