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

import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;
import com.helger.lama.maven.CMaven;
import com.helger.lama.maven.IMavenSerializableObject;
import com.helger.xml.microdom.IMicroElement;

public final class POMResource implements IMavenSerializableObject
{
  private final String m_sDirectory;
  private String m_sTargetPath;

  public POMResource (@Nonnull final String sDirectory)
  {
    m_sDirectory = sDirectory;
  }

  public String getDirectory ()
  {
    return m_sDirectory;
  }

  public POMResource setTargetPath (@Nullable final String sTargetPath)
  {
    m_sTargetPath = sTargetPath;
    return this;
  }

  public String getTargetPath ()
  {
    return m_sTargetPath;
  }

  public void appendToElement (final IMicroElement aElement)
  {
    final IMicroElement eRes = aElement.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_RESOURCE);
    if (m_sDirectory != null)
      eRes.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_DIRECTORY).appendText (m_sDirectory);
    if (m_sTargetPath != null)
      eRes.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_TARGETPATH).appendText (m_sTargetPath);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof POMResource))
      return false;
    final POMResource rhs = (POMResource) o;
    return EqualsHelper.equals (m_sDirectory, rhs.m_sDirectory) && EqualsHelper.equals (m_sTargetPath, rhs.m_sTargetPath);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sDirectory).append (m_sTargetPath).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("dir", m_sDirectory).appendIfNotNull ("targetPath", m_sTargetPath).toString ();
  }
}
