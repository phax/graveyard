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

public class POMPrerequisites implements IMavenSerializableObject
{
  private final ICommonsOrderedMap <String, String> m_aObjs = new CommonsLinkedHashMap<> ();

  public POMPrerequisites ()
  {}

  public void addItem (@Nonnull @Nonempty final String sKey, @Nonnull final String sValue)
  {
    if (StringHelper.hasNoText (sKey))
      throw new IllegalArgumentException ("key");
    if (sValue == null)
      throw new NullPointerException ("value");
    m_aObjs.put (sKey, sValue);
  }

  public boolean isEmpty ()
  {
    return m_aObjs.isEmpty ();
  }

  public void appendToElement (@Nonnull final IMicroElement aElement)
  {
    if (!isEmpty ())
    {
      final IMicroElement eConfiguration = aElement.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_PREREQUISITES);
      for (final Map.Entry <String, String> aEntry : m_aObjs.entrySet ())
      {
        final IMicroElement eKey = eConfiguration.appendElement (CMaven.POM_XMLNS, aEntry.getKey ());
        eKey.appendText (aEntry.getValue ());
      }
    }
  }
}
