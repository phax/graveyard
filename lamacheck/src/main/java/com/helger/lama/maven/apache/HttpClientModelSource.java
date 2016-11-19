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
package com.helger.lama.maven.apache;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.annotation.Nonnull;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.maven.model.building.ModelSource2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.lama.updater.http.GlobalHttpClient;

public class HttpClientModelSource implements ModelSource2
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (HttpClientModelSource.class);
  private final URI m_aURI;

  /**
   * Creates a new model source backed by the specified URI.
   *
   * @param pomUrl
   *        The POM file, must not be {@code null}.
   */
  public HttpClientModelSource (@Nonnull final URI pomUrl)
  {
    ValueEnforcer.notNull (pomUrl, "pomUrl");
    m_aURI = pomUrl;
  }

  public InputStream getInputStream () throws IOException
  {
    final HttpGet aHttpGet = new HttpGet (m_aURI);
    try
    {
      // Perform the HTTP GET
      final HttpResponse aResponse = GlobalHttpClient.getHttpClient ().execute (aHttpGet);
      final HttpEntity aResponseEntity = aResponse.getEntity ();
      if (aResponseEntity != null && (aResponse.getStatusLine () == null || aResponse.getStatusLine ().getStatusCode () == 200))
      {
        // getContent never returns null!
        s_aLogger.info ("Downloading " + m_aURI);
        return aResponseEntity.getContent ();
      }
      // ensure the connection gets released to the manager
      EntityUtils.consume (aResponseEntity);
    }
    catch (final Exception ex)
    {
      aHttpGet.abort ();
    }
    return null;
  }

  public String getLocation ()
  {
    return m_aURI.toString ();
  }

  /**
   * Gets the POM URI of this model source.
   *
   * @return The underlying POM URI, never {@code null}.
   */
  public URI getLocationURI ()
  {
    return m_aURI;
  }

  public ModelSource2 getRelatedSource (final String relPath)
  {
    return null;
  }

  @Override
  public String toString ()
  {
    return getLocation ();
  }
}
