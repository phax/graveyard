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

import org.apache.maven.model.Parent;
import org.apache.maven.model.Repository;
import org.apache.maven.model.building.FileModelSource;
import org.apache.maven.model.building.ModelSource2;
import org.apache.maven.model.resolution.InvalidRepositoryException;
import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.model.resolution.UnresolvableModelException;

import com.helger.lama.maven.apache.LocalSettingsModelResolver;
import com.helger.lama.maven.apache.NexusModelResolver;
import com.helger.lama.maven.version.MavenVersion;
import com.helger.lama.updater.domain.MavenArtifactInfo;
import com.helger.lama.updater.domain.MavenRepositoryInfo;
import com.helger.lama.updater.mgr.LamaMetaManager;
import com.helger.lama.updater.utils.OfflineCache;

public final class OfflineCacheModelResolver implements ModelResolver
{
  private final NexusModelResolver m_aFallback;

  public OfflineCacheModelResolver (final boolean bFallback)
  {
    m_aFallback = bFallback ? new NexusModelResolver () : null;
  }

  public ModelSource2 resolveModel (final String sGroupID, final String sArtifactID, final String sVersion) throws UnresolvableModelException
  {
    final MavenArtifactInfo aArtifactInfo = LamaMetaManager.getArtifactInfoMgr ()
                                                           .getArtifactOfID (MavenArtifactInfo.createArtifactID (sGroupID, sArtifactID));
    if (aArtifactInfo != null)
    {
      final MavenVersion aVersion = new MavenVersion (sVersion);
      final String sFilename = aArtifactInfo.getPOMPathAndFilename (aVersion);

      // Use offline cache where applicable
      for (final MavenRepositoryInfo aRepoInfo : aArtifactInfo.getAllDesiredRepos ())
      {
        final File aFile = OfflineCache.getOfflineCacheFile (aRepoInfo, sFilename);
        if (aFile.exists ())
          return new FileModelSource (aFile);
      }

      // Check main local repo is possible
      final File aFile = LocalSettingsModelResolver.resolveModel (sGroupID, sArtifactID, sVersion);
      if (aFile.exists ())
        return new FileModelSource (aFile);

      if (m_aFallback != null)
        return m_aFallback.resolveModel (sGroupID, sArtifactID, sVersion);
    }
    throw new UnresolvableModelException ("Failed to resolve " + sGroupID + ":" + sArtifactID + ":" + sVersion, sGroupID, sArtifactID, sVersion);
  }

  public ModelSource2 resolveModel (final Parent aParent) throws UnresolvableModelException
  {
    return resolveModel (aParent.getGroupId (), aParent.getArtifactId (), aParent.getVersion ());
  }

  public void addRepository (final Repository aRepository) throws InvalidRepositoryException
  {
    addRepository (aRepository, false);
  }

  public void addRepository (final Repository aRepository, final boolean bReplace) throws InvalidRepositoryException
  {}

  public ModelResolver newCopy ()
  {
    return new OfflineCacheModelResolver (m_aFallback != null);
  }
}
