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

import java.util.List;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.io.file.FilenameHelper;
import com.helger.commons.string.StringHelper;
import com.helger.lama.maven.artifact.IMavenArtifact;
import com.helger.lama.maven.pom.MavenPOM;
import com.helger.lama.maven.pom.POMDependency;
import com.helger.lama.maven.pom.POMExtension;
import com.helger.lama.maven.pom.POMPlugin;
import com.helger.lama.maven.pom.POMRepository;
import com.helger.lama.maven.repo.MavenRepository;
import com.helger.lama.updater.domain.MavenArtifactInfo;
import com.helger.lama.updater.domain.MavenRepositoryInfo;
import com.helger.lama.updater.mgr.LamaMetaManager;
import com.helger.lama.updater.mgr.MavenArtifactInfoManager;
import com.helger.lama.updater.mgr.MavenRepositoryInfoManager;
import com.helger.lama.updater.utils.UpdateBlacklist;
import com.helger.lama.updater.utils.WorkQueue;

final class UpdatedPOMHandler
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (UpdatedPOMHandler.class);

  private final ICommonsList <MavenPOM> m_aPOMs = new CommonsArrayList<> ();

  public UpdatedPOMHandler ()
  {}

  public void add (@Nonnull final MavenPOM aPOM)
  {
    if (aPOM == null)
      throw new NullPointerException ("POM");
    m_aPOMs.add (aPOM);
  }

  private static void _addArtifact (@Nonnull final IMavenArtifact aArtifact,
                                    @Nonnull final List <MavenArtifactInfo> aAddedArtifacts)
  {
    if (!aArtifact.containsUnresolvedVariable () && StringHelper.hasText (aArtifact.getGroupID ()))
    {
      final MavenArtifactInfoManager aArtifactInfoMgr = LamaMetaManager.getArtifactInfoMgr ();
      if (!aArtifactInfoMgr.containsArtifactWithID (aArtifact))
        if (aArtifactInfoMgr.addArtifact (aArtifact.getGroupID (), aArtifact.getArtifactID ()).isUnchanged ())
          s_aLogger.error ("Failed to add artifact " + aArtifact);
        else
          aAddedArtifacts.add (aArtifactInfoMgr.getArtifactOfID (MavenArtifactInfo.createArtifactID (aArtifact)));
    }
  }

  private static void _addRepo (@Nonnull final MavenRepository aRepo,
                                @Nonnull final List <MavenRepositoryInfo> aAddedRepos)
  {
    if (!aRepo.getURL ().contains ("${"))
    {
      final MavenRepositoryInfoManager aRepoMgr = LamaMetaManager.getRepoInfoMgr ();
      if (!aRepoMgr.containsRepositoryWithURL (aRepo.getURL ()))
      {
        // Create a unique ID as the URL is already unique
        String sUniqueID = FilenameHelper.getAsSecureValidFilename (aRepo.getID ());
        int nIndex = 0;
        while (aRepoMgr.containsRepositorywithID (sUniqueID))
          sUniqueID = aRepo.getID () + nIndex++;
        aRepo.setID (sUniqueID);

        final MavenRepositoryInfo aAddedRepoInfo = aRepoMgr.addRepository (aRepo);
        if (aAddedRepoInfo == null)
          s_aLogger.error ("Failed to add repo " + aRepo);
        else
          aAddedRepos.add (aAddedRepoInfo);
      }
    }
  }

  public void registerNewArtifacts (@Nonnull final UpdateBlacklist aBlacklist)
  {
    final ICommonsList <MavenArtifactInfo> aAddedArtifacts = new CommonsArrayList<> ();
    final ICommonsList <MavenRepositoryInfo> aAddedRepos = new CommonsArrayList<> ();
    for (final MavenPOM aPOM : m_aPOMs)
    {
      final IMavenArtifact aParentArtifact = aPOM.getParentArtifact ();
      if (aParentArtifact != null)
        _addArtifact (aParentArtifact, aAddedArtifacts);
      for (final POMDependency aDependency : aPOM.getEffectiveDependencies ().getAll ())
        _addArtifact (aDependency.getArtifact (), aAddedArtifacts);
      for (final POMDependency aDependency : aPOM.getEffectiveDependencyManagement ().getAll ())
        _addArtifact (aDependency.getArtifact (), aAddedArtifacts);
      for (final POMExtension aExtension : aPOM.getBuildExtensions ().getAll ())
        _addArtifact (aExtension.getArtifact (), aAddedArtifacts);
      for (final POMPlugin aPlugin : aPOM.getBuildPluginManagement ().getAll ())
      {
        _addArtifact (aPlugin.getArtifact (), aAddedArtifacts);
        for (final POMDependency aDependency : aPlugin.getDependencies ().getAll ())
          _addArtifact (aDependency.getArtifact (), aAddedArtifacts);
      }
      for (final POMPlugin aPlugin : aPOM.getBuildPlugins ().getAll ())
      {
        _addArtifact (aPlugin.getArtifact (), aAddedArtifacts);
        for (final POMDependency aDependency : aPlugin.getDependencies ().getAll ())
          _addArtifact (aDependency.getArtifact (), aAddedArtifacts);
      }
      for (final POMRepository aRepository : aPOM.getRepositories ().getAllRepositories ())
        _addRepo (aRepository.getRepo (), aAddedRepos);
      for (final POMRepository aPluginRepository : aPOM.getPluginRepositories ().getAllRepositories ())
        _addRepo (aPluginRepository.getRepo (), aAddedRepos);
    }

    if (aAddedRepos.isEmpty () && aAddedArtifacts.isEmpty ())
      return;

    if (!aAddedRepos.isEmpty ())
      s_aLogger.info ("Found " + aAddedRepos.size () + " new repositories");
    if (!aAddedArtifacts.isEmpty ())
      s_aLogger.info ("Found " + aAddedArtifacts.size () + " new artifacts");

    // Run all checks in parallel
    final WorkQueue aWQ = new WorkQueue ();

    // Search all artifacts in the new repositories
    for (final MavenRepositoryInfo aAddedRepoInfo : aAddedRepos)
      aWQ.addTaskNewRepo (aAddedRepoInfo, () -> UpdateHandler.onNewRepo (aAddedRepoInfo, aBlacklist));

    // Search all repositories for new artifacts
    for (final MavenArtifactInfo aAddedArtifactInfo : aAddedArtifacts)
      aWQ.addTaskNewArtifact (aAddedArtifactInfo, () -> UpdateHandler.onNewArtifact (aAddedArtifactInfo, aBlacklist));

    aWQ.waitUntilDone ();
  }
}
