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

public final class POMResources implements IMavenSerializableObject
{
  private final ICommonsOrderedSet <POMResource> m_aResCont = new CommonsLinkedHashSet<> ();

  public boolean addResource (@Nonnull final POMResource aRes)
  {
    if (aRes == null)
      throw new NullPointerException ("res");
    return m_aResCont.add (aRes);
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <POMResource> getAll ()
  {
    return m_aResCont.getCopyAsList ();
  }

  public boolean isEmpty ()
  {
    return m_aResCont.isEmpty ();
  }

  public void appendToElement (final IMicroElement aElement)
  {
    if (!m_aResCont.isEmpty ())
    {
      final IMicroElement eResources = aElement.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_RESOURCES);
      for (final POMResource aRes : m_aResCont)
        aRes.appendToElement (eResources);
    }
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("ress", m_aResCont).toString ();
  }
}
