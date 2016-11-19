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
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.lama.maven.CMaven;
import com.helger.lama.maven.IMavenSerializableObject;
import com.helger.xml.microdom.IMicroElement;

public final class POMLicenses implements IMavenSerializableObject
{
  private final ICommonsList <POMLicense> m_aLicenses = new CommonsArrayList<> ();

  public void addLicense (final POMLicense aLicense)
  {
    if (aLicense == null)
      throw new NullPointerException ("License");
    m_aLicenses.add (aLicense);
  }

  public void addAll (@Nonnull final POMLicenses aLicenses)
  {
    m_aLicenses.addAll (aLicenses.m_aLicenses);
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <POMLicense> getAll ()
  {
    return m_aLicenses.getClone ();
  }

  public boolean isEmpty ()
  {
    return m_aLicenses.isEmpty ();
  }

  public void appendToElement (final IMicroElement aElement)
  {
    if (!m_aLicenses.isEmpty ())
    {
      final IMicroElement eDependencies = aElement.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_LICENSES);
      for (final POMLicense aLicense : m_aLicenses)
        aLicense.appendToElement (eDependencies);
    }
  }
}
