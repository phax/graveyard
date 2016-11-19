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

import java.util.Map;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.collection.ext.CommonsLinkedHashMap;
import com.helger.commons.collection.ext.ICommonsOrderedMap;
import com.helger.commons.string.StringHelper;
import com.helger.lama.maven.CMaven;
import com.helger.lama.maven.IMavenSerializableObject;
import com.helger.lama.maven.artifact.IMavenArtifact;
import com.helger.xml.microdom.IMicroElement;

public class POMProperties implements IMavenSerializableObject
{
  public static final String PREFIX_PROJECT = "project.";

  private final ICommonsOrderedMap <String, String> m_aSystemProperties = new CommonsLinkedHashMap<> ();
  private final ICommonsOrderedMap <String, String> m_aProperties = new CommonsLinkedHashMap<> ();

  public POMProperties (@Nonnull final IMavenArtifact aArtifact)
  {
    // set artifact system properties
    if (StringHelper.hasText (aArtifact.getGroupID ()))
      addSystemProperty (PREFIX_PROJECT + "groupId", aArtifact.getGroupID ());
    addSystemProperty (PREFIX_PROJECT + "artifactId", aArtifact.getArtifactID ());
    if (StringHelper.hasText (aArtifact.getVersion ()))
      addSystemProperty (PREFIX_PROJECT + "version", aArtifact.getVersion ());
  }

  public void addProperty (@Nonnull @Nonempty final String sName, @Nonnull final String sValue)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("Name");
    if (sValue == null)
      throw new NullPointerException ("Value");
    m_aProperties.put (sName, sValue);
  }

  public void addAllProperties (@Nonnull final POMProperties aProperties)
  {
    m_aProperties.putAll (aProperties.m_aProperties);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, String> getAll ()
  {
    return CollectionHelper.newMap (m_aProperties);
  }

  public void addSystemProperty (@Nonnull @Nonempty final String sName, @Nonnull final String sValue)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("Name");
    if (sValue == null)
      throw new NullPointerException ("Value");
    m_aSystemProperties.put (sName, sValue);

    // Set without "project." as well!
    if (sName.startsWith (PREFIX_PROJECT))
      m_aSystemProperties.put (sName.substring (PREFIX_PROJECT.length ()), sValue);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, String> getAllSystemProperties ()
  {
    return CollectionHelper.newMap (m_aSystemProperties);
  }

  public void addAllSystemProperties (@Nonnull final POMProperties aProperties)
  {
    m_aSystemProperties.putAll (aProperties.m_aSystemProperties);
  }

  public void appendToElement (@Nonnull final IMicroElement aElement)
  {
    // Do not emit system properties
    if (!m_aProperties.isEmpty ())
    {
      final IMicroElement eProperties = aElement.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_PROPERTIES);
      for (final Map.Entry <String, String> aEntry : m_aProperties.entrySet ())
        eProperties.appendElement (CMaven.POM_XMLNS, aEntry.getKey ()).appendText (aEntry.getValue ());
    }
  }
}
