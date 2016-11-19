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
package com.helger.lama.updater.utils;

import java.io.InputStream;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.UnknownHostException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.io.file.FilenameHelper;
import com.helger.commons.lang.ClassHelper;
import com.helger.commons.url.URLHelper;
import com.helger.lama.updater.domain.MavenRepositoryInfo;
import com.helger.lama.updater.http.GlobalHttpClient;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.serialize.MicroReader;
import com.helger.xml.sax.DoNothingSAXErrorHandler;
import com.helger.xml.sax.EmptyEntityResolver;
import com.helger.xml.sax.InputSourceFactory;
import com.helger.xml.serialize.read.SAXReaderSettings;

public final class HTTPReader
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (HTTPReader.class);

  private HTTPReader ()
  {}

  @Nullable
  public static IMicroDocument readXML (@Nonnull final MavenRepositoryInfo aRepoInfo,
                                        @Nonnull final String sRelativeFilename,
                                        @Nonnull final UpdateBlacklist aBlacklist)
  {
    final String sURL = FilenameHelper.getCleanConcatenatedUrlPath (aRepoInfo.getURL (), sRelativeFilename);

    if (aBlacklist.containsRepo (aRepoInfo))
    {
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Ignoring '" + sURL + "' because repo '" + aRepoInfo.getID () + "' is in blacklist!");
      return null;
    }

    // http-client
    final URI aURI = URLHelper.getAsURI (sURL);
    if (aURI == null)
    {
      s_aLogger.error ("Failed to parse URL '" + sURL + "'");
      return null;
    }

    final HttpGet aHttpGet = new HttpGet (aURI);
    aHttpGet.setConfig (RequestConfig.custom ().setConnectTimeout (5000).setMaxRedirects (5).build ());
    IMicroDocument aDoc = null;
    try
    {
      // Perform the HTTP GET
      final HttpResponse aResponse = GlobalHttpClient.getHttpClient ().execute (aHttpGet);
      final HttpEntity aResponseEntity = aResponse.getEntity ();
      try
      {
        if (aResponseEntity != null &&
            (aResponse.getStatusLine () == null || aResponse.getStatusLine ().getStatusCode () == 200))
        {
          // getContent never returns null!
          final InputStream aIS = aResponseEntity.getContent ();

          // Remote XML reading - so easy ;-)
          aDoc = MicroReader.readMicroXML (InputSourceFactory.create (aIS),
                                           new SAXReaderSettings ().setEntityResolver (new EmptyEntityResolver ())
                                                                   .setErrorHandler (new DoNothingSAXErrorHandler ()));
          if (aDoc == null)
          {
            // Failed to parse it
            if (false)
              s_aLogger.error ("Illegal XML for " + sURL);
            return null;
          }

          // Remember :)
          OfflineCache.putInOfflineCache (aRepoInfo, sRelativeFilename, aDoc);
        }
        else
          if (false)
            s_aLogger.error ("Failed to read " + sURL);
      }
      finally
      {
        // ensure the connection gets released to the manager
        EntityUtils.consume (aResponseEntity);
      }
    }
    catch (final HttpHostConnectException | ConnectTimeoutException | NoRouteToHostException | UnknownHostException
        | SocketTimeoutException | ClientProtocolException ex)
    {
      s_aLogger.error ("Failed to get content from URL '" +
                       sURL +
                       "': " +
                       ClassHelper.getClassLocalName (ex) +
                       " - " +
                       ex.getMessage ());
      aBlacklist.addRepo (aRepoInfo);
      aHttpGet.abort ();
    }
    catch (final Exception ex)
    {
      s_aLogger.error ("Failed to get content from URL '" + sURL + "'", ex);
      aHttpGet.abort ();
    }

    return aDoc;
  }
}
