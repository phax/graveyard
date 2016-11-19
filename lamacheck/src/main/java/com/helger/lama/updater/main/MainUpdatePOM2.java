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
import java.util.List;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.io.file.filter.IFileFilter;
import com.helger.commons.io.file.iterate.FileSystemRecursiveIterator;
import com.helger.lama.maven.apache.GetAllDependencies;
import com.helger.lama.maven.apache.MavenArtifactWithType;
import com.helger.lama.maven.artifact.IMavenArtifact;
import com.helger.lama.maven.repo.MavenRepository;
import com.helger.lama.maven.version.MavenVersion;
import com.helger.lama.updater.domain.MavenArtifactInfo;
import com.helger.lama.updater.mgr.LamaMetaManager;

public final class MainUpdatePOM2
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MainUpdatePOM2.class);

  private static void _checkForUpdate (@Nonnull final String sType,
                                       @Nonnull final IMavenArtifact aArtifact,
                                       final boolean bCheckOnlyCentral)
  {
    final String sCurrentVersion = aArtifact.getVersion ();
    if (sCurrentVersion != null)
    {
      final String sInternalArtifactID = MavenArtifactInfo.createArtifactID (aArtifact);
      final MavenArtifactInfo aArtifactInfo = LamaMetaManager.getArtifactInfoMgr ()
                                                             .getArtifactOfID (sInternalArtifactID);
      if (aArtifactInfo != null)
      {
        final MavenVersion aCurrentVersion = new MavenVersion (sCurrentVersion);
        final MavenVersion aLatestVersion = bCheckOnlyCentral ? aArtifactInfo.getLatestReleaseVersionOfRepository (MavenRepository.ID_CENTRAL2)
                                                              : aArtifactInfo.getLatestReleaseVersion ();
        if (aLatestVersion != null && aLatestVersion.isGreaterThan (aCurrentVersion))
          s_aLogger.info ("  " +
                          sType +
                          " " +
                          sInternalArtifactID +
                          " can be updated from " +
                          sCurrentVersion +
                          " to " +
                          aLatestVersion.getOriginalVersion ());
      }
      else
      {
        final String sGroupID = aArtifact.getGroupID ();
        final String sArtifactID = aArtifact.getArtifactID ();
        if (!"org.eclipse.m2e".equals (sGroupID) || !"lifecycle-mapping".equals (sArtifactID))
          LamaMetaManager.getArtifactInfoMgr ().addArtifact (sGroupID, sArtifactID);
      }
    }
  }

  public static void main (final String [] args) throws Exception
  {
    final IFileFilter aIgnore = IFileFilter.filenameMatchNone ("src",
                                                               "tags",
                                                               "branches",
                                                               "target",
                                                               "Archive",
                                                               "checkout");
    LamaRunner.run ( () -> {
      LamaMetaManager.getArtifactInfoMgr ().beginWithoutAutoSave ();
      try
      {
        File aBaseDir = new File ("f:\\git");
        if (false)
          aBaseDir = new File ("P:\\svn-joinup-cipa\\trunk\\");

        for (final File aFile : new FileSystemRecursiveIterator (aBaseDir, aIgnore))
          if (aFile.getName ().equals ("pom.xml"))
          {
            s_aLogger.info (aFile.getAbsolutePath ());

            final List <MavenArtifactWithType> a = GetAllDependencies.getAllDependencies (aFile,
                                                                                          new OfflineCacheModelResolver (true));
            if (a != null)
              for (final MavenArtifactWithType aAWT : a)
                _checkForUpdate (aAWT.getLocation (), aAWT.getArtifact (), true);
          }
      }
      finally
      {
        LamaMetaManager.getArtifactInfoMgr ().endWithoutAutoSave ();
      }
    });
  }
}
