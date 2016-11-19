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
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.PresentForCodeCoverage;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.datetime.PDTFactory;
import com.helger.lama.maven.metadata.MavenMetaData;
import com.helger.lama.maven.version.MavenVersion;
import com.helger.lama.updater.domain.MavenArtifactInfo;
import com.helger.lama.updater.domain.MavenRepositoryInfo;
import com.helger.lama.updater.mgr.LamaMetaManager;
import com.helger.lama.updater.mgr.MavenArtifactInfoManager;
import com.helger.lama.updater.utils.HTTPReader;
import com.helger.lama.updater.utils.UpdateBlacklist;
import com.helger.xml.microdom.IMicroDocument;

@Immutable
final class UpdateReaderMetaData
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (UpdateReaderMetaData.class);

  @PresentForCodeCoverage
  private static final UpdateReaderMetaData s_aInstance = new UpdateReaderMetaData ();

  private UpdateReaderMetaData ()
  {}

  @Nullable
  public static MavenMetaData readMavenMetadata (@Nonnull final MavenRepositoryInfo aRepoInfo,
                                                 @Nonnull final MavenArtifactInfo aArtifactInfo,
                                                 @Nonnull final UpdateBlacklist aBlacklist)
  {
    final String sMetadataPath = aArtifactInfo.getMetadataPath ();
    final IMicroDocument aMetaDataDoc = HTTPReader.readXML (aRepoInfo, sMetadataPath, aBlacklist);
    if (aMetaDataDoc == null)
    {
      // Warning already emitted
      return null;
    }

    // Interpret the maven-metadata.xml file
    final MavenMetaData ret = MavenMetaData.readXML (aArtifactInfo.getAsArtifact (),
                                                     aMetaDataDoc,
                                                     aArtifactInfo.getAllExcludedVersions ());
    if (ret == null)
    {
      // Happens quote often when accessing a non-existing element
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Failed to parse maven-metadata.xml of " +
                         aArtifactInfo.getID () +
                         " in " +
                         aRepoInfo.getURL ());
    }
    return ret;
  }

  /**
   * Read all metadata.xml files
   *
   * @param aArtifactInfo
   *        Artifact to retrieve metadata.xml
   * @param aAllValidDesiredRepos
   *        All valid repositories for this artifact
   * @param aBlacklist
   *        Global blacklist
   * @return A list with same size as the repositories, and the order matches
   *         the repository list. Entries may be <code>null</code> if reading
   *         failed.
   */
  @Nonnull
  @ReturnsMutableCopy
  private static List <MavenMetaData> _readAllMavenMetaData (@Nonnull final MavenArtifactInfo aArtifactInfo,
                                                             @Nonnull final List <MavenRepositoryInfo> aAllValidDesiredRepos,
                                                             @Nonnull final UpdateBlacklist aBlacklist)
  {
    final ICommonsList <MavenMetaData> aMetadataList = new CommonsArrayList<> (aAllValidDesiredRepos.size ());
    for (final MavenRepositoryInfo aRepoInfo : aAllValidDesiredRepos)
    {
      MavenMetaData aMetaDataOfThisRepo = null;
      if (!aBlacklist.containsRepo (aRepoInfo))
      {
        // Read and Interpret the maven-metadata.xml file
        aMetaDataOfThisRepo = readMavenMetadata (aRepoInfo, aArtifactInfo, aBlacklist);
      }
      // May be null
      aMetadataList.add (aMetaDataOfThisRepo);
    }

    assert aMetadataList.size () == aAllValidDesiredRepos.size ();
    return aMetadataList;
  }

  @Nullable
  private static FindVersionResult _determineLatestVersions (@Nonnull final MavenArtifactInfo aArtifactInfo,
                                                             @Nonnull final List <MavenRepositoryInfo> aAllValidDesiredRepos,
                                                             @Nonnull final List <MavenMetaData> aMetadataList)
  {
    ValueEnforcer.notNull (aArtifactInfo, "ArtifactInfo");
    ValueEnforcer.notNull (aAllValidDesiredRepos, "AllValidDesiredRepos");
    ValueEnforcer.notNull (aMetadataList, "MetadataList");
    if (aAllValidDesiredRepos.size () != aMetadataList.size ())
      throw new IllegalArgumentException ("Size mismatch");

    MavenVersion aLatestRelease = null;
    MavenVersion aLatestBeta = null;
    final MavenArtifactInfoManager aArtifactInfoMgr = LamaMetaManager.getArtifactInfoMgr ();

    // For all read metadata
    for (int i = 0; i < aMetadataList.size (); ++i)
    {
      // Get current metadata - may be null
      final MavenMetaData aMetaDataOfThisRepo = aMetadataList.get (i);

      // Get the matching repo - same index!
      final MavenRepositoryInfo aRepoInfo = aAllValidDesiredRepos.get (i);

      // Latest release of this repo
      final MavenVersion aReleaseVersionOfThisRepo = aMetaDataOfThisRepo == null ? null
                                                                                 : aMetaDataOfThisRepo.getLatestReleaseVersion ();
      aArtifactInfoMgr.setLatestReleaseVersionOfRepository (aArtifactInfo, aRepoInfo, aReleaseVersionOfThisRepo);

      // Latest beta version of this repo
      final MavenVersion aBetaVersionOfThisRepo = aMetaDataOfThisRepo == null ? null
                                                                              : aMetaDataOfThisRepo.getLatestVersion ();
      aArtifactInfoMgr.setLatestBetaVersionOfRepository (aArtifactInfo, aRepoInfo, aBetaVersionOfThisRepo);

      if (aMetaDataOfThisRepo != null)
      {
        if (aReleaseVersionOfThisRepo == null && aBetaVersionOfThisRepo == null)
        {
          if (s_aLogger.isDebugEnabled ())
            s_aLogger.debug ("Found no relevant version of " + aArtifactInfo.getID () + " in " + aRepoInfo.getURL ());
        }
        else
        {
          // Determine overall latest release and beta versions of this artifact
          if (aLatestRelease == null ||
              (aReleaseVersionOfThisRepo != null && aReleaseVersionOfThisRepo.isGreaterThan (aLatestRelease)))
            aLatestRelease = aReleaseVersionOfThisRepo;
          if (aLatestBeta == null ||
              (aBetaVersionOfThisRepo != null && aBetaVersionOfThisRepo.isGreaterThan (aLatestBeta)))
            aLatestBeta = aBetaVersionOfThisRepo;
        }
      }
    }

    if (aLatestRelease == null && aLatestBeta == null)
    {
      // Remember error reading maven-metadata.xml
      aArtifactInfoMgr.setLastUpdateErrorMetaData (aArtifactInfo, PDTFactory.getCurrentLocalDateTime ());
      return null;
    }

    // Remember that we read the maven-metadata.xml
    aArtifactInfoMgr.setLastUpdateMetaData (aArtifactInfo, PDTFactory.getCurrentLocalDateTime ());
    return new FindVersionResult (aLatestRelease, aLatestBeta);
  }

  @Nullable
  public static FindMetaDataResult findMetaDataWithLatestVersion (@Nonnull final MavenArtifactInfo aArtifactInfo,
                                                                  @Nonnull final List <MavenRepositoryInfo> aAllValidDesiredRepos,
                                                                  @Nonnull final UpdateBlacklist aBlacklist)
  {
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("Searching for " +
                       aArtifactInfo.getID () +
                       " in " +
                       aAllValidDesiredRepos.size () +
                       " repos (" +
                       aBlacklist.getCount () +
                       " black listed)");

    // Search all repositories for this artifact
    // The list indices are identical to the ones of the repositories (and
    // therefore may contain null elements)
    final List <MavenMetaData> aMetadataList = _readAllMavenMetaData (aArtifactInfo, aAllValidDesiredRepos, aBlacklist);

    // Determine overall latest versions (release and beta) by interpreting all
    // read metadata files
    final FindVersionResult aLatestVersions = _determineLatestVersions (aArtifactInfo,
                                                                        aAllValidDesiredRepos,
                                                                        aMetadataList);
    if (aLatestVersions == null)
    {
      // Found no versions at all :(
      return null;
    }

    final FindMetaDataResult ret = new FindMetaDataResult (aLatestVersions);

    // Search all repositories for this artifact
    for (int i = 0; i < aMetadataList.size (); ++i)
    {
      final MavenMetaData aMetaDataOfThisRepo = aMetadataList.get (i);
      if (aMetaDataOfThisRepo != null)
      {
        final MavenRepositoryInfo aRepoInfo = aAllValidDesiredRepos.get (i);

        if (aLatestVersions.isLatestReleaseVersion (aMetaDataOfThisRepo.getLatestReleaseVersion ()))
          ret.addReleaseVersion (aRepoInfo, aMetaDataOfThisRepo);

        if (aLatestVersions.isLatestBetaVersion (aMetaDataOfThisRepo.getLatestVersion ()))
          ret.addBetaVersion (aRepoInfo, aMetaDataOfThisRepo);
      }
    }
    return ret;
  }
}
