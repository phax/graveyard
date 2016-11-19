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

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.lama.maven.CMaven;
import com.helger.lama.maven.IMavenSerializableObject;
import com.helger.lama.maven.artifact.IMavenArtifact;
import com.helger.xml.microdom.IMicroElement;

public final class POMPlugins implements IMavenSerializableObject
{
  private final ICommonsList <POMPlugin> m_aPlugins = new CommonsArrayList<> ();

  public void addPlugin (final POMPlugin aPlugin)
  {
    if (aPlugin == null)
      throw new NullPointerException ("plugin");
    m_aPlugins.add (aPlugin);
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <POMPlugin> getAll ()
  {
    return m_aPlugins.getClone ();
  }

  public boolean isEmpty ()
  {
    return m_aPlugins.isEmpty ();
  }

  @Nullable
  public IMavenArtifact findPlugin (@Nonnull final IMavenArtifact aArtifact)
  {
    if (aArtifact.getGroupID () == null || aArtifact.getArtifactID () == null)
      throw new IllegalArgumentException ("Cannot resolve artifact: " + aArtifact);
    for (final POMPlugin aPlugin : m_aPlugins)
    {
      final IMavenArtifact aDepArtifact = aPlugin.getArtifact ();
      if (aArtifact.getGroupID ().equals (aDepArtifact.getGroupID ()) &&
          aArtifact.getArtifactID ().equals (aDepArtifact.getArtifactID ()))
        return aDepArtifact;
    }
    return null;
  }

  public void appendToElement (final IMicroElement aElement)
  {
    if (!m_aPlugins.isEmpty ())
    {
      final IMicroElement eDependencies = aElement.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_PLUGINS);
      for (final POMPlugin aPlugin : m_aPlugins)
        aPlugin.appendToElement (eDependencies);
    }
  }
}
