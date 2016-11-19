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

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.ext.CommonsLinkedHashSet;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.collection.ext.ICommonsOrderedSet;
import com.helger.commons.string.ToStringGenerator;
import com.helger.lama.maven.CMaven;
import com.helger.lama.maven.IMavenSerializableObject;
import com.helger.xml.microdom.IMicroElement;

public final class POMExtensions implements IMavenSerializableObject
{
  private final ICommonsOrderedSet <POMExtension> m_aExtensions = new CommonsLinkedHashSet<> ();

  public boolean addExtension (@Nonnull final POMExtension aExtension)
  {
    if (aExtension == null)
      throw new NullPointerException ("Extension");
    return m_aExtensions.add (aExtension);
  }

  public void addAll (@Nonnull final POMExtensions aExtensionendencies)
  {
    m_aExtensions.addAll (aExtensionendencies.m_aExtensions);
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <POMExtension> getAll ()
  {
    return m_aExtensions.getCopyAsList ();
  }

  public boolean isEmpty ()
  {
    return m_aExtensions.isEmpty ();
  }

  public void appendToElement (@Nonnull final IMicroElement aElement)
  {
    if (!m_aExtensions.isEmpty ())
    {
      final IMicroElement eExtensionendencies = aElement.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_EXTENSIONS);
      for (final POMExtension aExtension : m_aExtensions)
        aExtension.appendToElement (eExtensionendencies);
    }
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("extensions", m_aExtensions).toString ();
  }
}
