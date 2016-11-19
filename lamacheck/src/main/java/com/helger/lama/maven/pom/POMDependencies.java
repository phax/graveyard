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

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.collection.ext.CommonsLinkedHashSet;
import com.helger.commons.collection.ext.ICommonsOrderedSet;
import com.helger.commons.string.ToStringGenerator;
import com.helger.lama.maven.CMaven;
import com.helger.lama.maven.IMavenSerializableObject;
import com.helger.lama.maven.artifact.IMavenArtifact;
import com.helger.xml.microdom.IMicroElement;

public final class POMDependencies implements IMavenSerializableObject
{
  private final boolean m_bIsDependencyManagement;
  private final ICommonsOrderedSet <POMDependency> m_aDeps = new CommonsLinkedHashSet<> ();

  public POMDependencies (final boolean bIsDependencyManagement)
  {
    m_bIsDependencyManagement = bIsDependencyManagement;
  }

  public boolean addDependency (@Nonnull final POMDependency aDep)
  {
    if (aDep == null)
      throw new NullPointerException ("dependency");
    return m_aDeps.add (aDep);
  }

  public void addAll (@Nonnull final POMDependencies aDependencies)
  {
    m_aDeps.addAll (aDependencies.m_aDeps);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <POMDependency> getAll ()
  {
    return CollectionHelper.newList (m_aDeps);
  }

  public boolean isEmpty ()
  {
    return m_aDeps.isEmpty ();
  }

  @Nullable
  public IMavenArtifact findDependency (@Nonnull final IMavenArtifact aArtifact)
  {
    if (aArtifact.getGroupID () == null || aArtifact.getArtifactID () == null)
      throw new IllegalArgumentException ("Cannot resolve artifact: " + aArtifact);
    for (final POMDependency aDep : m_aDeps)
    {
      final IMavenArtifact aDepArtifact = aDep.getArtifact ();
      if (aArtifact.getGroupID ().equals (aDepArtifact.getGroupID ()) &&
          aArtifact.getArtifactID ().equals (aDepArtifact.getArtifactID ()))
        return aDepArtifact;
    }
    return null;
  }

  public void appendToElement (@Nonnull final IMicroElement aElement)
  {
    if (!m_aDeps.isEmpty ())
    {
      IMicroElement eParent = aElement;
      if (m_bIsDependencyManagement)
        eParent = aElement.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_DEPENDENCY_MANAGEMENT);

      final IMicroElement eDependencies = eParent.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_DEPENDENCIES);
      for (final POMDependency aDep : m_aDeps)
        aDep.appendToElement (eDependencies);
    }
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("deps", m_aDeps).toString ();
  }
}
