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

import com.helger.commons.string.StringHelper;
import com.helger.lama.maven.CMaven;
import com.helger.lama.maven.IMavenSerializableObject;
import com.helger.xml.microdom.IMicroElement;

public class POMLicense implements IMavenSerializableObject
{
  private String m_sName;
  private String m_sUrl;
  private String m_sDistribution;

  public POMLicense ()
  {}

  public void setName (@Nullable final String sName)
  {
    m_sName = sName;
  }

  @Nullable
  public String getName ()
  {
    return m_sName;
  }

  public void setUrl (@Nullable final String sUrl)
  {
    m_sUrl = sUrl;
  }

  @Nullable
  public String getUrl ()
  {
    return m_sUrl;
  }

  public void setDistribution (@Nullable final String sDistribution)
  {
    m_sDistribution = sDistribution;
  }

  @Nullable
  public String getDistribution ()
  {
    return m_sDistribution;
  }

  @Override
  public void appendToElement (@Nonnull final IMicroElement aElement)
  {
    final IMicroElement eLicense = aElement.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_LICENSE);
    if (StringHelper.hasText (m_sName))
      eLicense.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_NAME).appendText (m_sName);
    if (StringHelper.hasText (m_sUrl))
      eLicense.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_URL).appendText (m_sUrl);
    if (StringHelper.hasText (m_sDistribution))
      eLicense.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_DISTRIBUTION).appendText (m_sDistribution);
  }
}
