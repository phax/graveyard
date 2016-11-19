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
package com.helger.lama.updater.main;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

import com.helger.commons.io.file.FileOperations;
import com.helger.commons.io.file.FilenameHelper;
import com.helger.commons.io.file.SimpleFileIO;
import com.helger.commons.io.file.iterate.FileSystemRecursiveIterator;
import com.helger.commons.io.stream.StreamHelper;
import com.helger.lama.updater.domain.MavenRepositoryInfo;
import com.helger.lama.updater.http.GlobalHttpClient;
import com.helger.lama.updater.mgr.LamaMetaManager;
import com.helger.lama.updater.utils.OfflineCache;
import com.helger.photon.basic.app.io.WebFileIO;
import com.helger.xml.sax.DoNothingSAXErrorHandler;
import com.helger.xml.serialize.read.DOMReader;
import com.helger.xml.serialize.read.DOMReaderSettings;

public final class MainOfflineCacheFixPOMXML
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MainOfflineCacheFixPOMXML.class);

  public static void main (final String [] args) throws Exception
  {
    LamaRunner.run ( () -> {
      final File aBaseDir = WebFileIO.getDataIO ().getFile (OfflineCache.DIR_OFFLINE_CACHE);
      final int nBaseDirLen = aBaseDir.getAbsolutePath ().length () + 1;
      for (final File aFile : new FileSystemRecursiveIterator (aBaseDir))
        if (aFile.getName ().endsWith (".pom"))
        {
          Document aDoc = null;
          try
          {
            aDoc = DOMReader.readXMLDOM (aFile, new DOMReaderSettings ().setErrorHandler (new DoNothingSAXErrorHandler ()));
          }
          catch (final SAXParseException ex1)
          {
            if (ex1.getMessage ().startsWith ("The prefix "))
              continue;
          }
          if (aDoc == null)
          {
            String sPath = FilenameHelper.getPathUsingUnixSeparator (aFile.getPath ().substring (nBaseDirLen));
            final int i = sPath.indexOf ('/');
            final String sRepoID = sPath.substring (0, i);
            final MavenRepositoryInfo aRepoInfo = LamaMetaManager.getRepoInfoMgr ().getRepositoryOfID (sRepoID);
            sPath = sPath.substring (i + 1);

            s_aLogger.info ("Downloading " + aRepoInfo.getURL () + sPath);
            final HttpGet aHttpGet = new HttpGet (aRepoInfo.getURL () + sPath);
            try
            {
              // Perform the HTTP GET
              final HttpResponse aResponse = GlobalHttpClient.getHttpClient ().execute (aHttpGet);
              final HttpEntity aResponseEntity = aResponse.getEntity ();
              if (aResponseEntity != null && (aResponse.getStatusLine () == null || aResponse.getStatusLine ().getStatusCode () == 200))
              {
                // getContent never returns null!
                final byte [] x = StreamHelper.getAllBytes (aResponseEntity.getContent ());
                if (x != null)
                {
                  s_aLogger.info ("Found " + sPath + " -> " + x.length + " bytes");
                  SimpleFileIO.writeFile (aFile, x);
                }
              }
              else
                FileOperations.deleteFile (aFile);
              // ensure the connection gets released to the manager
              EntityUtils.consume (aResponseEntity);
            }
            catch (final Exception ex2)
            {
              aHttpGet.abort ();
            }
          }
        }
    });
  }
}
