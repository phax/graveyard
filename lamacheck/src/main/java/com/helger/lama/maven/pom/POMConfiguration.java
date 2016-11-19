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
import com.helger.commons.collection.ext.CommonsLinkedHashMap;
import com.helger.commons.collection.ext.ICommonsOrderedMap;
import com.helger.commons.string.StringHelper;
import com.helger.lama.maven.CMaven;
import com.helger.lama.maven.IMavenSerializableObject;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.IMicroNode;

/**
 * The configuration inside a plugin
 *
 * @author Philip Helger
 */
public final class POMConfiguration implements IMavenSerializableObject
{
  private final ICommonsOrderedMap <String, Object> m_aConfiguration = new CommonsLinkedHashMap<> ();

  public POMConfiguration ()
  {}

  public void putAll (@Nonnull final POMConfiguration rhs)
  {
    m_aConfiguration.putAll (rhs.m_aConfiguration);
  }

  public void addItem (@Nonnull @Nonempty final String sName, @Nonnull final String sValue)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");
    if (sValue == null)
      throw new NullPointerException ("value");
    m_aConfiguration.put (sName, sValue);
  }

  public void addItem (@Nonnull @Nonempty final String sName, @Nonnull final IMicroNode aValue)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");
    if (aValue == null)
      throw new NullPointerException ("value");
    m_aConfiguration.put (sName, aValue);
  }

  public boolean isEmpty ()
  {
    return m_aConfiguration.isEmpty ();
  }

  public void appendToElement (@Nonnull final IMicroElement aElement)
  {
    if (!m_aConfiguration.isEmpty ())
    {
      final IMicroElement eConfiguration = aElement.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_CONFIGURATION);
      for (final Map.Entry <String, Object> aEntry : m_aConfiguration.entrySet ())
      {
        final IMicroElement eKey = eConfiguration.appendElement (CMaven.POM_XMLNS, aEntry.getKey ());
        final Object aValue = aEntry.getValue ();
        if (aValue instanceof String)
          eKey.appendText ((CharSequence) aValue);
        else
          if (aValue instanceof IMicroNode)
            eKey.appendChild ((IMicroNode) aValue);
          else
            throw new IllegalStateException ("Unknown value: " + aValue);
      }
    }
  }
}
