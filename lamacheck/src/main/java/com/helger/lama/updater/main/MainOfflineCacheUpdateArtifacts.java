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
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.concurrent.BasicThreadFactory;
import com.helger.commons.concurrent.ManagedExecutorService;
import com.helger.commons.io.file.iterate.FileSystemRecursiveIterator;
import com.helger.commons.random.RandomHelper;
import com.helger.lama.maven.apache.GetAllDependencies;
import com.helger.lama.maven.apache.MavenArtifactWithType;
import com.helger.lama.maven.apache.NexusModelResolver;
import com.helger.lama.maven.artifact.IMavenArtifact;
import com.helger.lama.updater.domain.MavenArtifactInfo;
import com.helger.lama.updater.mgr.LamaMetaManager;
import com.helger.lama.updater.utils.OfflineCache;
import com.helger.photon.basic.app.io.WebFileIO;

public final class MainOfflineCacheUpdateArtifacts
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MainOfflineCacheUpdateArtifacts.class);

  private static void _checkForUpdate (@Nonnull final IMavenArtifact aArtifact)
  {
    if (!aArtifact.containsUnresolvedVariable ())
    {
      final String sCurrentVersion = aArtifact.getVersion ();
      if (sCurrentVersion != null)
      {
        final String sInternalArtifactID = MavenArtifactInfo.createArtifactID (aArtifact);
        final MavenArtifactInfo aArtifactInfo = LamaMetaManager.getArtifactInfoMgr ()
                                                               .getArtifactOfID (sInternalArtifactID);
        if (aArtifactInfo == null)
        {
          final String sGroupID = aArtifact.getGroupID ();
          final String sArtifactID = aArtifact.getArtifactID ();
          if (!"org.eclipse.m2e".equals (sGroupID) || !"lifecycle-mapping".equals (sArtifactID))
          {
            if (LamaMetaManager.getArtifactInfoMgr ().addArtifact (sGroupID, sArtifactID).isChanged ())
              s_aLogger.info ("Added " + sInternalArtifactID);
          }
        }
      }
    }
  }

  public static void main (final String [] args) throws Exception
  {
    LamaRunner.run ( () -> {
      s_aLogger.info ("Searching files");
      final File aBaseDir = WebFileIO.getDataIO ().getFile (OfflineCache.DIR_OFFLINE_CACHE);
      final ICommonsList <File> aFiles = new CommonsArrayList<> ();
      for (final File aFile1 : new FileSystemRecursiveIterator (aBaseDir))
        if (aFile1.getName ().endsWith (".pom"))
          aFiles.add (aFile1);
      s_aLogger.info ("Found " + aFiles.size () + " files");

      LamaMetaManager.getArtifactInfoMgr ().beginWithoutAutoSave ();
      try
      {
        final ThreadPoolExecutor aExecService = new ThreadPoolExecutor (20,
                                                                        20,
                                                                        0L,
                                                                        TimeUnit.SECONDS,
                                                                        new LinkedBlockingQueue <Runnable> (),
                                                                        new BasicThreadFactory.Builder ().setNamingPattern ("lama-updater-%d")
                                                                                                         .setDaemon (true)
                                                                                                         .build ());
        final NexusModelResolver aResolver = new NexusModelResolver ();

        while (!aFiles.isEmpty ())
        {
          // Put in random order, so that the artifact resolution can happen
          // better in parallel!
          final File aFile2 = aFiles.remove (RandomHelper.getRandom ().nextInt (aFiles.size ()));
          aExecService.submit ( () -> {
            final ICommonsList <MavenArtifactWithType> aDeps = GetAllDependencies.getAllDependencies (aFile2,
                                                                                                      aResolver);
            if (aDeps != null)
              for (final MavenArtifactWithType aAWT : aDeps)
                _checkForUpdate (aAWT.getArtifact ());
          });
        }
        ManagedExecutorService.shutdownAndWaitUntilAllTasksAreFinished (aExecService);
      }
      finally
      {
        LamaMetaManager.getArtifactInfoMgr ().endWithoutAutoSave ();
      }
    });
  }
}
