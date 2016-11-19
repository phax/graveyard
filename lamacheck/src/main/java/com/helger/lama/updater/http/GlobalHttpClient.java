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
package com.helger.lama.updater.http;

import javax.annotation.Nonnull;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.helger.commons.annotation.UsedViaReflection;
import com.helger.commons.scope.IScope;
import com.helger.commons.scope.singleton.AbstractGlobalSingleton;

public final class GlobalHttpClient extends AbstractGlobalSingleton
{
  private final CloseableHttpClient m_aHttpClient;
  private final PoolingHttpClientConnectionManager m_aConnMgr;
  private final IdleConnectionMonitorThread m_aIdleThread;

  @Deprecated
  @UsedViaReflection
  public GlobalHttpClient ()
  {
    final RegistryBuilder <ConnectionSocketFactory> aCSFReg = RegistryBuilder.<ConnectionSocketFactory> create ();
    aCSFReg.register ("http", PlainConnectionSocketFactory.getSocketFactory ()).register ("https", SSLConnectionSocketFactory.getSocketFactory ());

    m_aConnMgr = new PoolingHttpClientConnectionManager (aCSFReg.build ());
    // Increase max total connection to 200
    m_aConnMgr.setMaxTotal (200);
    // Increase default max connection per route to 20
    m_aConnMgr.setDefaultMaxPerRoute (10);
    // Increase max connections for localhost:81 to 50
    m_aConnMgr.setMaxPerRoute (new HttpRoute (new HttpHost ("locahost", 81)), 50);

    // Start the idle connection monitor thread
    m_aIdleThread = new IdleConnectionMonitorThread (m_aConnMgr);
    m_aIdleThread.start ();

    final HttpClientBuilder aHCB = HttpClients.custom ().setConnectionManager (m_aConnMgr);
    if (false)
      aHCB.setUserAgent ("useragent");
    // Allow gzip,compress
    aHCB.addInterceptorLast (new RequestAcceptEncoding ());
    // Add cookies
    aHCB.addInterceptorLast (new RequestAddCookies ());
    // Un-gzip or uncompress
    aHCB.addInterceptorLast (new ResponseContentEncoding ());

    // Set cookie store (defaults to BasicCookieStore)
    // aHCB.setDefaultCookieStore (cookieStore);

    // Some credentials
    final CredentialsProvider aCP = new BasicCredentialsProvider ();
    aCP.setCredentials (new AuthScope ("localhost", 81), new UsernamePasswordCredentials ("philip", "phloc2"));
    aHCB.setDefaultCredentialsProvider (aCP);

    // Default cookie acceptance policy - most interoperable one
    final RequestConfig aDefaultRequestConfig = RequestConfig.custom ()
                                                             .setCookieSpec (CookieSpecs.DEFAULT)
                                                             .setSocketTimeout (5000)
                                                             .setConnectTimeout (5000)
                                                             .build ();
    aHCB.setDefaultRequestConfig (aDefaultRequestConfig);

    m_aHttpClient = aHCB.build ();
  }

  @Override
  protected void onDestroy (@Nonnull final IScope aScopeInDestruction)
  {
    m_aConnMgr.shutdown ();
    if (m_aIdleThread.isAlive ())
    {
      m_aIdleThread.shutdown ();
      try
      {
        m_aIdleThread.join ();
      }
      catch (final InterruptedException ex)
      {
        throw new RuntimeException ("Failed to join idle connection handling thread", ex);
      }
    }
  }

  @Nonnull
  public static GlobalHttpClient getInstance ()
  {
    return getGlobalSingleton (GlobalHttpClient.class);
  }

  @Nonnull
  public static CloseableHttpClient getHttpClient ()
  {
    return getInstance ().m_aHttpClient;
  }
}
