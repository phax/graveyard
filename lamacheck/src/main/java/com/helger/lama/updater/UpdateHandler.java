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
package com.helger.lama.updater;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.datetime.PDTFactory;
import com.helger.lama.updater.domain.MavenArtifactInfo;
import com.helger.lama.updater.domain.MavenRepositoryInfo;
import com.helger.lama.updater.mgr.LamaMetaManager;
import com.helger.lama.updater.mgr.MavenArtifactInfoManager;
import com.helger.lama.updater.mgr.MavenRepositoryInfoManager;
import com.helger.lama.updater.utils.UpdateBlacklist;

final class UpdateHandler
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (UpdateHandler.class);

  public UpdateHandler ()
  {}

  public static void onNewRepo (@Nonnull final MavenRepositoryInfo aRepoInfo, @Nonnull final UpdateBlacklist aBlacklist)
  {
    if (aRepoInfo.isValid () && !aRepoInfo.isLegacyLayout ())
    {
      final MavenArtifactInfoManager aArtifactInfoMgr = LamaMetaManager.getArtifactInfoMgr ();
      final List <MavenArtifactInfo> aAllArtifactInfos = aArtifactInfoMgr.getAllArtifactsSorted ();
      s_aLogger.info ("Search for " + aAllArtifactInfos.size () + " artifacts in new repo " + aRepoInfo.getURL ());

      int nArtifactCount = 0;
      for (final MavenArtifactInfo aCurArtifactInfo : aAllArtifactInfos)
        if (UpdateReaderMetaData.readMavenMetadata (aRepoInfo, aCurArtifactInfo, aBlacklist) != null)
        {
          // Remember that we found the current artifact
          if (aArtifactInfoMgr.addDesiredRepository (aCurArtifactInfo, aRepoInfo).isChanged ())
          {
            ++nArtifactCount;
            if (s_aLogger.isDebugEnabled ())
              s_aLogger.debug ("Found ArtifactInfo " +
                               aCurArtifactInfo.getID () +
                               " in repository " +
                               aRepoInfo.getID ());
          }
        }
      s_aLogger.info ("Finished search for " +
                      aAllArtifactInfos.size () +
                      " artifacts in new repo " +
                      aRepoInfo.getURL () +
                      ". Found " +
                      nArtifactCount +
                      " artifacts.");
      if (nArtifactCount == 0)
      {
        // When no artifacts are found, mark the repo as invalid to avoid
        // further hammering the system
        LamaMetaManager.getRepoInfoMgr ().setInvalid (aRepoInfo,
                                                      true,
                                                      "Found no artifacts. Last update " +
                                                            PDTFactory.getCurrentLocalDateTime ().toString ());
      }
    }
  }

  public static void onNewArtifact (@Nonnull final MavenArtifactInfo aArtifactInfo,
                                    @Nonnull final UpdateBlacklist aBlacklist)
  {
    final MavenArtifactInfoManager aArtifactInfoMgr = LamaMetaManager.getArtifactInfoMgr ();
    final MavenRepositoryInfoManager aRepoMgr = LamaMetaManager.getRepoInfoMgr ();
    final Collection <MavenRepositoryInfo> aAllValidRepos = aRepoMgr.getAllValidRepositories ();

    s_aLogger.info ("Searching metadata for Maven Artifact " +
                    aArtifactInfo.getID () +
                    " in " +
                    aAllValidRepos.size () +
                    " repos (" +
                    aBlacklist.getCount () +
                    " black listed)");

    for (final MavenRepositoryInfo aRepoInfo : aAllValidRepos)
      if (!aBlacklist.containsRepo (aRepoInfo))
      {
        // No need for black-listing, as each repo is queried exactly once
        if (UpdateReaderMetaData.readMavenMetadata (aRepoInfo, aArtifactInfo, aBlacklist) != null)
        {
          // Remember that this repository can be used for the artifact
          if (aArtifactInfoMgr.addDesiredRepository (aArtifactInfo, aRepoInfo).isChanged ())
            s_aLogger.info ("Found repository " + aRepoInfo.getID () + " for ArtifactInfo " + aArtifactInfo.getID ());
        }
      }

    s_aLogger.info ("Finished searching metadata for Maven Artifact " +
                    aArtifactInfo.getID () +
                    " in " +
                    aAllValidRepos.size () +
                    " repos");
    if (aArtifactInfo.getDesiredRepoCount () == 0)
    {
      // Found no repositories
      aArtifactInfoMgr.setLastUpdateErrorRepositories (aArtifactInfo, PDTFactory.getCurrentLocalDateTime ());
    }
  }
}
