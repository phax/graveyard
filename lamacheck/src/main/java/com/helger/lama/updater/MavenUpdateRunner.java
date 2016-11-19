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

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.PresentForCodeCoverage;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.CommonsHashMap;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.collection.ext.ICommonsMap;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.mutable.MutableInt;
import com.helger.commons.state.EChange;
import com.helger.commons.statistics.IMutableStatisticsHandlerTimer;
import com.helger.commons.statistics.StatisticsManager;
import com.helger.commons.string.StringHelper;
import com.helger.commons.timing.StopWatch;
import com.helger.lama.maven.metadata.MavenMetaData;
import com.helger.lama.maven.pom.MavenPOM;
import com.helger.lama.maven.version.MavenVersion;
import com.helger.lama.updater.FindMetaDataResult.Item;
import com.helger.lama.updater.domain.MavenArtifactInfo;
import com.helger.lama.updater.domain.MavenRepositoryInfo;
import com.helger.lama.updater.mgr.LamaMetaManager;
import com.helger.lama.updater.mgr.MavenArtifactInfoManager;
import com.helger.lama.updater.mgr.MavenRepositoryInfoManager;
import com.helger.lama.updater.utils.UpdateBlacklist;
import com.helger.lama.updater.utils.WorkQueue;
import com.helger.photon.basic.audit.AuditHelper;

@Immutable
public final class MavenUpdateRunner
{
  private static final IMutableStatisticsHandlerTimer s_aStatsTimer = StatisticsManager.getTimerHandler (MavenUpdateRunner.class);
  private static final Logger s_aLogger = LoggerFactory.getLogger (MavenUpdateRunner.class);
  private static final Duration MIN_DURATION_BETWEEN_CHECK_NEW_ARTIFACT = Duration.ofDays (3);
  private static final Duration MIN_DURATION_BETWEEN_CHECK_UPDATE_ARTIFACT = Duration.ofDays (2);
  private static final boolean DEBUG_MODE = false;

  @PresentForCodeCoverage
  private static final MavenUpdateRunner s_aInstance = new MavenUpdateRunner ();

  private MavenUpdateRunner ()
  {}

  @Nonnull
  private static EChange _updateLatestVersion (@Nonnull final MavenArtifactInfo aArtifactInfo,
                                               @Nonnull final MavenMetaData aMetaData,
                                               final boolean bReleaseVersion)
  {
    final MavenVersion aCurrentLatestVersion = bReleaseVersion ? aArtifactInfo.getLatestReleaseVersion ()
                                                               : aArtifactInfo.getLatestBetaVersion ();
    // aCurrentLatestVersion may be null!

    final MavenVersion aRelevantVersion = bReleaseVersion ? aMetaData.getLatestReleaseVersion ()
                                                          : aMetaData.getLatestVersion ();
    if (aRelevantVersion == null)
      throw new IllegalStateException ("oops");

    if (EqualsHelper.equals (aCurrentLatestVersion, aRelevantVersion))
      return EChange.UNCHANGED;

    // Allow for a downgrade!
    final boolean bIsUpgrade = aCurrentLatestVersion == null || aRelevantVersion.isGreaterThan (aCurrentLatestVersion);

    // Determine all versions greater than the currently known one
    final ICommonsList <String> aAllGreaterVersions = new CommonsArrayList<> ();
    for (final MavenVersion aGreaterVersion : aMetaData.getAllVersionsGreaterThan (aCurrentLatestVersion,
                                                                                   bReleaseVersion))
      aAllGreaterVersions.add (aGreaterVersion.getOriginalVersion ());

    s_aLogger.info (aArtifactInfo.getID () +
                    (bIsUpgrade ? " can be upgraded" : "must be downgraded") +
                    (aCurrentLatestVersion == null ? ""
                                                   : " from version " + aCurrentLatestVersion.getOriginalVersion ()) +
                    " to " +
                    aRelevantVersion.getOriginalVersion () +
                    (aAllGreaterVersions.size () == 1 ? ""
                                                      : " as the greatest of (" +
                                                        StringHelper.getImploded (" or ", aAllGreaterVersions) +
                                                        ")"));
    if (bReleaseVersion)
      LamaMetaManager.getArtifactInfoMgr ().setLatestReleaseVersion (aArtifactInfo, aRelevantVersion);
    else
      LamaMetaManager.getArtifactInfoMgr ().setLatestBetaVersion (aArtifactInfo, aRelevantVersion);
    return EChange.CHANGED;
  }

  /**
   * Determine all actions to take.
   *
   * @param aArtifactInfos
   *        Artifact list to handle.
   * @param aNewArtifacts
   *        Destination list for all new artifacts (artifacts without a repo)
   * @param aUpdateArtifacts
   *        Destination list for all artifacts to be checked for updates
   * @param aNewRepos
   *        Destination list for all new repositories (repositories without an
   *        artifact)
   */
  private static void _determineUpdateActionsToTake (@Nonnull final Collection <MavenArtifactInfo> aArtifactInfos,
                                                     @Nonnull final List <MavenArtifactInfo> aNewArtifacts,
                                                     @Nonnull final List <MavenArtifactInfo> aUpdateArtifacts,
                                                     @Nonnull final List <MavenRepositoryInfo> aNewRepos)
  {
    // Init artifacts per repo
    final MavenRepositoryInfoManager aRepoInfoMgr = LamaMetaManager.getRepoInfoMgr ();
    final ICommonsMap <String, MutableInt> aArtifactsPerRepo = new CommonsHashMap<> ();
    for (final MavenRepositoryInfo aRepoInfo : aRepoInfoMgr.getAllRepositories ())
      aArtifactsPerRepo.put (aRepoInfo.getID (), new MutableInt (0));

    for (final MavenArtifactInfo aCurArtifactInfo : aArtifactInfos)
    {
      if (aCurArtifactInfo.getAllValidDesiredRepoCount () == 0)
      {
        if (aCurArtifactInfo.getLastUpdateMetaData () == null ||
            aCurArtifactInfo.getDurationSinceLastUpdateErrorRepositories ()
                            .compareTo (MIN_DURATION_BETWEEN_CHECK_NEW_ARTIFACT) > 0)
        {
          // Search for artifact in all repos
          aNewArtifacts.add (aCurArtifactInfo);
        }
      }
      else
        if (DEBUG_MODE ||
            aCurArtifactInfo.getDurationSinceLastUpdateMetaData ()
                            .compareTo (MIN_DURATION_BETWEEN_CHECK_UPDATE_ARTIFACT) > 0)
        {
          // Check for update on this artifact
          aUpdateArtifacts.add (aCurArtifactInfo);
        }

      // Increment how many artifacts are in a specified repo
      for (final MavenRepositoryInfo aInfo : aCurArtifactInfo.getAllDesiredRepos ())
        aArtifactsPerRepo.get (aInfo.getID ()).inc ();
    }

    // Search all repos that contain no artifacts
    for (final MavenRepositoryInfo aRepoInfo : aRepoInfoMgr.getAllRepositories ())
      if (aRepoInfo.isValid () && aArtifactsPerRepo.get (aRepoInfo.getID ()).is0 ())
        aNewRepos.add (aRepoInfo);

    if (!aNewArtifacts.isEmpty ())
      s_aLogger.info ("Searching repositories for " +
                      aNewArtifacts.size () +
                      " of " +
                      aArtifactInfos.size () +
                      " artifacts");
    if (!aUpdateArtifacts.isEmpty ())
      s_aLogger.info ("Updating versions for " +
                      aUpdateArtifacts.size () +
                      " of " +
                      aArtifactInfos.size () +
                      " artifacts");
    if (!aNewRepos.isEmpty ())
      s_aLogger.info ("Searching artifacts for " + aNewRepos.size () + " repositories");
  }

  /**
   * Update the passed artifacts. Everything in here works with the
   * ArtifactInfoMgr auto-save being disabled.
   *
   * @param aArtifactInfos
   *        Non-<code>null</code> collection of artifacts to be updated.
   * @param aPOMCache
   * @return The number of updates found. Always &ge; 0.
   */
  @Nonnegative
  private static int _updateAllArtifacts (@Nonnull final Collection <MavenArtifactInfo> aArtifactInfos,
                                          @Nonnull final UpdateBlacklist aBlacklist,
                                          @Nonnull final UpdatedPOMHandler aPOMCache)
  {
    final MavenArtifactInfoManager aArtifactInfoMgr = LamaMetaManager.getArtifactInfoMgr ();

    // Determine all artifacts that are to be updated
    final List <MavenArtifactInfo> aNewArtifacts = new ArrayList<> ();
    final List <MavenArtifactInfo> aUpdateArtifacts = new ArrayList<> ();
    final List <MavenRepositoryInfo> aNewRepos = new ArrayList<> ();
    _determineUpdateActionsToTake (aArtifactInfos, aNewArtifacts, aUpdateArtifacts, aNewRepos);

    final MutableInt aUpdatedVersions = new MutableInt (0);

    // Run all checks in parallel
    final WorkQueue aWQ = new WorkQueue ();

    // For all artifacts to add
    if (!DEBUG_MODE)
      for (final MavenArtifactInfo aCurArtifactInfo : aNewArtifacts)
        aWQ.addTaskNewArtifact (aCurArtifactInfo, () -> UpdateHandler.onNewArtifact (aCurArtifactInfo, aBlacklist));

    // For all artifacts to update
    for (final MavenArtifactInfo aCurArtifactInfo : aUpdateArtifacts)
    {
      aWQ.addTaskUpdateArtifact (aCurArtifactInfo, () -> {
        // No valid repositories to search?
        final List <MavenRepositoryInfo> aAllValidDesiredRepos = aCurArtifactInfo.getAllValidDesiredRepos ();
        if (aAllValidDesiredRepos.isEmpty ())
          return;

        // Read the metadata xml file in all matching repositories
        final FindMetaDataResult aResult = UpdateReaderMetaData.findMetaDataWithLatestVersion (aCurArtifactInfo,
                                                                                               aAllValidDesiredRepos,
                                                                                               aBlacklist);
        if (aResult == null)
        {
          // No maven-metadata.xml contained a version (release or beta)
          return;
        }

        if (DEBUG_MODE)
          s_aLogger.info (aResult.toString ());
        else
        {
          // Release version update
          if (aResult.hasReleaseItems ())
          {
            boolean bReadReleasePOM = false;
            for (final Item aItem1 : aResult.getAllReleaseItems ())
            {
              if (_updateLatestVersion (aCurArtifactInfo, aItem1.getMetaData (), true).isChanged ())
              {
                // Only read the POM if something was updated, because
                // otherwise the POM is not supposed to be changed
                aUpdatedVersions.inc ();
                if (!aBlacklist.containsRepo (aItem1.getRepoInfo ()))
                {
                  // Try to read the POM file of the release version
                  final MavenPOM aPOM1 = UpdateReaderPOM.readPOM (aItem1.getRepoInfo (),
                                                                  aCurArtifactInfo,
                                                                  aResult.getReleaseVersion (),
                                                                  aBlacklist);
                  if (aPOM1 != null)
                  {
                    aPOMCache.add (aPOM1);
                    aArtifactInfoMgr.setPackaging (aCurArtifactInfo, aPOM1.getPackagingOrDefault ());
                    bReadReleasePOM = true;
                    break;
                  }
                }
              }
            }
            if (!bReadReleasePOM)
            {
              s_aLogger.warn ("Failed to resolve pom.xml for (release) Maven Artifact " +
                              aCurArtifactInfo.getID () +
                              " version " +
                              aResult.getReleaseVersion ().getOriginalVersion ());
            }
          }

          if (aResult.isBetaVersionSetAndDifferentFromReleaseVersion () && aResult.hasBetaItems ())
          {
            // Beta version update
            boolean bReadBetaPOM = false;
            for (final Item aItem2 : aResult.getAllBetaItems ())
            {
              final EChange eUpdated = _updateLatestVersion (aCurArtifactInfo, aItem2.getMetaData (), false);
              if (eUpdated.isChanged ())
                aUpdatedVersions.inc ();

              // Only read the POM if something was updated, because otherwise
              // the POM is not supposed to be changed for non-SNAPSHOT
              // versions
              if (eUpdated.isChanged () || aResult.getBetaVersion ().isSnapshotVersion ())
              {
                // Try to read the POM file of the beta version
                final MavenPOM aPOM2 = UpdateReaderPOM.readPOM (aItem2.getRepoInfo (),
                                                                aCurArtifactInfo,
                                                                aResult.getBetaVersion (),
                                                                aBlacklist);
                if (aPOM2 != null)
                {
                  aPOMCache.add (aPOM2);
                  // Update only if no packaging is defined yet
                  if (aCurArtifactInfo.getPackaging () == null)
                    aArtifactInfoMgr.setPackaging (aCurArtifactInfo, aPOM2.getPackagingOrDefault ());
                  bReadBetaPOM = true;
                  break;
                }
              }
            }
            if (!bReadBetaPOM)
            {
              s_aLogger.warn ("Failed to resolve pom.xml for (beta) Maven Artifact " +
                              aCurArtifactInfo.getID () +
                              " version " +
                              aResult.getBetaVersion ().getOriginalVersion ());
            }
          }
          s_aLogger.info ("Finished checking " + aCurArtifactInfo.getID ());
        }
      });
    }

    // Scan repos for artifacts
    if (!DEBUG_MODE)
      for (final MavenRepositoryInfo aCurRepoInfo : aNewRepos)
        aWQ.addTaskNewRepo (aCurRepoInfo, () -> UpdateHandler.onNewRepo (aCurRepoInfo, aBlacklist));

    aWQ.waitUntilDone ();

    if (aUpdatedVersions.intValue () == 0)
      s_aLogger.info ("SUMMARY: All " + aArtifactInfos.size () + " artifacts are up-to-date!");
    else
      s_aLogger.info ("SUMMARY: " + aUpdatedVersions.intValue () + " artifact(s) were updated");

    final MavenRepositoryInfoManager aRepoInfoMgr = LamaMetaManager.getRepoInfoMgr ();
    for (final String sRepo : aBlacklist.getAllIDs ())
    {
      s_aLogger.warn ("Blacklisted repo: " + sRepo);

      // Remember blacklisting
      final MavenRepositoryInfo aRepoInfo = aRepoInfoMgr.getRepositoryOfID (sRepo);
      if (aRepoInfo != null)
        aRepoInfoMgr.setInvalid (aRepoInfo,
                                 true,
                                 "Was added to blacklist around " + PDTFactory.getCurrentLocalDateTime ().toString ());
    }

    return aUpdatedVersions.intValue ();
  }

  @Nonnegative
  public static int updateAllMavenArtifacts ()
  {
    final MavenArtifactInfoManager aArtifactMgr = LamaMetaManager.getArtifactInfoMgr ();

    // get all artifacts ordered
    final Collection <MavenArtifactInfo> aAllArtifacts = aArtifactMgr.getAllArtifactsSorted ();
    final UpdateBlacklist aBlacklist = new UpdateBlacklist ();
    final UpdatedPOMHandler aPOMCache = new UpdatedPOMHandler ();
    int nUpdatedArtifacts;

    aArtifactMgr.beginWithoutAutoSave ();
    try
    {
      final StopWatch aSW = StopWatch.createdStarted ();

      // read all metadata.xml files
      nUpdatedArtifacts = _updateAllArtifacts (aAllArtifacts, aBlacklist, aPOMCache);

      s_aStatsTimer.addTime (aSW.stopAndGetMillis ());

      AuditHelper.onAuditExecuteSuccess ("update-maven-artifact-list",
                                         Integer.toString (aAllArtifacts.size ()),
                                         Long.toString (aSW.getMillis ()),
                                         Integer.toString (nUpdatedArtifacts));
    }
    finally
    {
      aArtifactMgr.endWithoutAutoSave ();
    }

    aArtifactMgr.beginWithoutAutoSave ();
    try
    {
      // Read all identified POMs and new repos and artifacts
      aPOMCache.registerNewArtifacts (aBlacklist);
    }
    finally
    {
      aArtifactMgr.endWithoutAutoSave ();
    }

    return nUpdatedArtifacts;
  }
}
