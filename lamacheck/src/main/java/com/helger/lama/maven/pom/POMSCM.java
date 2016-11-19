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

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.string.StringHelper;
import com.helger.lama.maven.CMaven;
import com.helger.lama.maven.IMavenSerializableObject;
import com.helger.xml.microdom.IMicroElement;

@NotThreadSafe
public class POMSCM implements IMavenSerializableObject
{
  private String m_sUrl;
  private String m_sConnection;
  private String m_sDeveloperConnection;

  public POMSCM ()
  {}

  public void setUrl (@Nullable final String sURL)
  {
    m_sUrl = sURL;
  }

  @Nullable
  public String getUrl ()
  {
    return m_sUrl;
  }

  public void setConnection (@Nullable final String sURL)
  {
    m_sConnection = sURL;
  }

  @Nullable
  public String getConnection ()
  {
    return m_sConnection;
  }

  public void setDeveloperConnection (@Nullable final String sURL)
  {
    m_sDeveloperConnection = sURL;
  }

  @Nullable
  public String getDeveloperConnection ()
  {
    return m_sDeveloperConnection;
  }

  public boolean isAnyConnectionDefined ()
  {
    return StringHelper.hasText (m_sUrl) || StringHelper.hasText (m_sConnection) || StringHelper.hasText (m_sDeveloperConnection);
  }

  public void appendToElement (final IMicroElement aElement)
  {
    if (isAnyConnectionDefined ())
    {
      final IMicroElement eSCM = aElement.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_SCM);
      if (StringHelper.hasText (m_sUrl))
        eSCM.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_URL).appendText (m_sUrl);
      if (StringHelper.hasText (m_sConnection))
        eSCM.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_CONNECTION).appendText (m_sConnection);
      if (StringHelper.hasText (m_sDeveloperConnection))
        eSCM.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_DEVELOPER_CONNECTION).appendText (m_sDeveloperConnection);
    }
  }
}
