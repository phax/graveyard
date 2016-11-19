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

public final class POMPlugin implements IMavenSerializableObject
{
  public static final boolean DEFAULT_EXTENSIONS = false;

  private final IMavenArtifact m_aArtifact;
  private boolean m_bExtensions = DEFAULT_EXTENSIONS;
  private final POMExecutions m_aExecutions = new POMExecutions ();
  private final POMConfiguration m_aConfiguration = new POMConfiguration ();
  private final POMDependencies m_aDependencies = new POMDependencies (false);

  public POMPlugin (@Nonnull final String sGroupID, @Nonnull final String sArtifactID)
  {
    this (new MavenArtifact (sGroupID, sArtifactID));
  }

  public POMPlugin (@Nonnull final String sGroupID, @Nonnull final String sArtifactID, @Nullable final String sVersion)
  {
    this (new MavenArtifact (sGroupID, sArtifactID, sVersion));
  }

  public POMPlugin (@Nonnull final String sGroupID,
                    @Nonnull final String sArtifactID,
                    @Nullable final String sVersion,
                    @Nullable final String sClassifier)
  {
    this (new MavenArtifact (sGroupID, sArtifactID, sVersion, sClassifier));
  }

  public POMPlugin (@Nonnull final IMavenArtifact aArtifact)
  {
    if (aArtifact == null)
      throw new NullPointerException ("artifact");
    // Plugins may not have a groupID - in this case use the default one!
    m_aArtifact = aArtifact.getGroupID () == null ? aArtifact.createWithNewGroupID ("org.apache.maven.plugins") : aArtifact;
  }

  /**
   * For property resolving.
   *
   * @param aArtifact
   *        Relevant artefact
   * @param aSourcePlugin
   *        Plugin to copy from
   */
  public POMPlugin (@Nonnull final IMavenArtifact aArtifact, @Nonnull final POMPlugin aSourcePlugin)
  {
    this (aArtifact);
    m_bExtensions = aSourcePlugin.m_bExtensions;
    m_aExecutions.addAll (aSourcePlugin.m_aExecutions);
    m_aConfiguration.putAll (aSourcePlugin.m_aConfiguration);
    m_aDependencies.addAll (aSourcePlugin.m_aDependencies);
  }

  @Nonnull
  public IMavenArtifact getArtifact ()
  {
    return m_aArtifact;
  }

  @Nullable
  public String getGroupID ()
  {
    return m_aArtifact.getGroupID ();
  }

  @Nonnull
  public String getArtifactID ()
  {
    return m_aArtifact.getArtifactID ();
  }

  @Nullable
  public String getVersion ()
  {
    return m_aArtifact.getVersion ();
  }

  @Nullable
  public String getClassifier ()
  {
    return m_aArtifact.getClassifier ();
  }

  public void setExtensions (final boolean bExtensions)
  {
    m_bExtensions = bExtensions;
  }

  @Nonnull
  public POMExecutions getExecutions ()
  {
    return m_aExecutions;
  }

  @Nonnull
  public POMConfiguration getConfiguration ()
  {
    return m_aConfiguration;
  }

  @Nonnull
  public POMDependencies getDependencies ()
  {
    return m_aDependencies;
  }

  public void appendToElement (@Nonnull final IMicroElement aElement)
  {
    final IMicroElement ePlugin = aElement.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_PLUGIN);
    MavenPOMHelper.serializeArtifact (m_aArtifact, ePlugin);
    if (m_bExtensions != DEFAULT_EXTENSIONS)
      ePlugin.appendElement (CMaven.POM_XMLNS, "extensions").appendText (Boolean.toString (m_bExtensions));

    m_aExecutions.appendToElement (ePlugin);
    m_aConfiguration.appendToElement (ePlugin);
    m_aDependencies.appendToElement (ePlugin);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof POMPlugin))
      return false;
    final POMPlugin rhs = (POMPlugin) o;
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
    return new ToStringGenerator (this).append ("groupID", getGroupID ())
                                       .append ("artifactID", getArtifactID ())
                                       .appendIfNotNull ("version", getVersion ())
                                       .appendIfNotNull ("classifier", getClassifier ())
                                       .toString ();
  }
}
