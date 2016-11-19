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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.io.file.SimpleFileIO;
import com.helger.commons.io.file.filter.IFileFilter;
import com.helger.commons.io.file.iterate.FileSystemRecursiveIterator;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.commons.io.resource.URLResource;
import com.helger.commons.io.stream.StreamHelper;
import com.helger.lama.maven.pom.MavenPOM;
import com.helger.lama.maven.pom.MavenPOMFile;
import com.helger.lama.updater.MavenUpdateRunner;
import com.helger.lama.updater.domain.MavenArtifactInfo;
import com.helger.lama.updater.domain.MavenRepositoryInfo;
import com.helger.lama.updater.mgr.LamaMetaManager;
import com.helger.lama.updater.mgr.MavenArtifactInfoManager;
import com.helger.lama.updater.mgr.MavenRepositoryInfoManager;
import com.helger.lama.updater.utils.OfflineCache;
import com.helger.photon.basic.app.io.WebFileIO;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.serialize.MicroReader;
import com.helger.xml.sax.DoNothingSAXErrorHandler;
import com.helger.xml.sax.InputSourceFactory;
import com.helger.xml.serialize.read.SAXReaderSettings;

public final class MainMavenMetaDataUpdater
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MainMavenMetaDataUpdater.class);

  public static void main (final String [] args) throws Exception
  {
    LamaRunner.run ( () -> {
      final FileSystemResource aLock = WebFileIO.getDataIO ().getResource ("lama.running");
      if (aLock.exists ())
        throw new IllegalStateException ("LaMa already running!");
      SimpleFileIO.writeFile (aLock.getAsFile (), new byte [0]);

      try
      {
        // Check if we're online
        final boolean bOffline = StreamHelper.getAllBytes (new URLResource ("http://www.google.com")) == null;
        if (bOffline)
          throw new IllegalStateException ("No Internet connectivity!");

        final MavenRepositoryInfoManager aRepoInfoMgr = LamaMetaManager.getRepoInfoMgr ();
        final MavenArtifactInfoManager aArtifactInfoMgr = LamaMetaManager.getArtifactInfoMgr ();

        if (false)
          aRepoInfoMgr.addRepository ("restlet", "http://maven.restlet.org/");
        if (false)
          aArtifactInfoMgr.addArtifact ("com.ximpleware", "vtd-xml");

        if (false)
        {
          s_aLogger.info ("Checking all " + aRepoInfoMgr.getRepositoryCount () + " repos if they are reachable!");
          for (final MavenRepositoryInfo aRepo : aRepoInfoMgr.getAllValidRepositories ())
            if (!aRepo.getRepo ().isReachable ())
              s_aLogger.error ("Repo not reachable: " + aRepo);
        }

        if (false)
        {
          // Fix packaging of all artifacts by reading all stored POMs
          final File x = WebFileIO.getDataIO ().getFile (OfflineCache.DIR_OFFLINE_CACHE);
          for (final File aFile : new FileSystemRecursiveIterator (x).withFilter (IFileFilter.filenameEndsWith (".pom")))
          {
            final FileSystemResource aRes = new FileSystemResource (aFile);
            final IMicroDocument aDoc = MicroReader.readMicroXML (InputSourceFactory.create (aRes),
                                                                  new SAXReaderSettings ().setErrorHandler (new DoNothingSAXErrorHandler ()));
            if (aDoc != null &&
                aDoc.getDocumentElement () != null &&
                aDoc.getDocumentElement ().getTagName ().equals ("project"))
            {
              final MavenPOM aPOM = new MavenPOMFile (aRes).getPOM ().resolveProperties ();
              final String sArtifactID = MavenArtifactInfo.createArtifactID (aPOM.getEffectiveArtifact ());
              final MavenArtifactInfo aArtifactInfo = aArtifactInfoMgr.getArtifactOfID (sArtifactID);
              if (aArtifactInfo != null)
                aArtifactInfoMgr.setPackaging (aArtifactInfo, aPOM.getPackagingOrDefault ());
            }
          }
          return;
        }

        // Main update runner
        if (MavenUpdateRunner.updateAllMavenArtifacts () > 0)
          MainMavenMetaDataProjectUpdate.writePOM ();
      }
      finally
      {
        WebFileIO.getFileOpMgr ().deleteFile (aLock.getAsFile ());
      }
    });
  }
}
