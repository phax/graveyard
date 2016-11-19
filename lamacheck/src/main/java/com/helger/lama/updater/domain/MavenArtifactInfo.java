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
package com.helger.lama.updater.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.CommonsLinkedHashMap;
import com.helger.commons.collection.ext.ICommonsCollection;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.collection.ext.ICommonsOrderedMap;
import com.helger.commons.collection.ext.ICommonsSet;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.id.IHasID;
import com.helger.commons.state.EChange;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.lama.maven.EMavenPackaging;
import com.helger.lama.maven.artifact.IMavenArtifact;
import com.helger.lama.maven.artifact.MavenArtifact;
import com.helger.lama.maven.version.MavenVersion;

@NotThreadSafe
public final class MavenArtifactInfo implements IHasID <String>
{
  private final String m_sID;
  private final String m_sGroupID;
  private final String m_sArtifactID;
  private EMavenPackaging m_ePackaging;
  private final ICommonsOrderedMap <String, MavenArtifactRepositoryInfo> m_aDesiredRepos = new CommonsLinkedHashMap<> ();
  private MavenVersion m_aLatestReleaseVersion;
  private MavenVersion m_aLatestBetaVersion;
  private final ICommonsSet <MavenVersion> m_aExcludedVersions;
  private LocalDateTime m_aLastUpdateMetaData;
  private LocalDateTime m_aLastUpdateErrorMetaData;
  private LocalDateTime m_aLastUpdateErrorRepositories;

  @Nonnull
  @Nonempty
  public static String createArtifactID (@Nonnull final IMavenArtifact aArtifact)
  {
    return createArtifactID (aArtifact.getGroupID (), aArtifact.getArtifactID ());
  }

  @Nonnull
  @Nonempty
  public static String createArtifactID (@Nonnull @Nonempty final String sGroupID,
                                         @Nonnull @Nonempty final String sArtifactID)
  {
    return sGroupID + ":" + sArtifactID;
  }

  public MavenArtifactInfo (@Nonnull @Nonempty final String sGroupID,
                            @Nonnull @Nonempty final String sArtifactID,
                            @Nullable final EMavenPackaging ePackaging,
                            @Nullable final String sLatestReleaseVersion,
                            @Nullable final String sLatestBetaVersion,
                            @Nullable final Collection <MavenVersion> aExcludedVersions,
                            @Nullable final LocalDateTime aLastUpdateMetaData,
                            @Nullable final LocalDateTime aLastUpdateErrorMetaData,
                            @Nullable final LocalDateTime aLastUpdateErrorRepositories)
  {
    ValueEnforcer.notEmpty (sGroupID, "GroupID");
    ValueEnforcer.notEmpty (sArtifactID, "ArtifactID");

    m_sID = createArtifactID (sGroupID, sArtifactID);
    m_sGroupID = sGroupID;
    m_sArtifactID = sArtifactID;
    m_ePackaging = ePackaging;

    // Check in case the MavenVersion algorithm was improved!
    MavenVersion aLatestReleaseVersion = sLatestReleaseVersion == null ? null
                                                                       : new MavenVersion (sLatestReleaseVersion);
    if (aLatestReleaseVersion != null && !aLatestReleaseVersion.isReleaseVersion ())
      aLatestReleaseVersion = null;
    m_aLatestReleaseVersion = aLatestReleaseVersion;
    m_aLatestBetaVersion = sLatestBetaVersion == null ? null : new MavenVersion (sLatestBetaVersion);
    m_aExcludedVersions = CollectionHelper.newOrderedSet (aExcludedVersions);
    setLastUpdateMetaData (aLastUpdateMetaData);
    setLastUpdateErrorMetaData (aLastUpdateErrorMetaData);
    setLastUpdateErrorRepositories (aLastUpdateErrorRepositories);
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  @Nonempty
  public String getGroupID ()
  {
    return m_sGroupID;
  }

  @Nonnull
  @Nonempty
  public String getArtifactID ()
  {
    return m_sArtifactID;
  }

  public boolean isStandardPluginArtifactID ()
  {
    return m_sArtifactID.endsWith ("-maven-plugin") ||
           (m_sArtifactID.startsWith ("maven-") && m_sArtifactID.endsWith ("-plugin"));
  }

  public boolean isPluginArtifact ()
  {
    return isStandardPluginArtifactID () || EMavenPackaging.MAVEN_PLUGIN.equals (m_ePackaging);
  }

  @Nonnull
  public IMavenArtifact getAsArtifact ()
  {
    return new MavenArtifact (m_sGroupID, m_sArtifactID);
  }

  @Nullable
  public EMavenPackaging getPackaging ()
  {
    return m_ePackaging;
  }

  @Nonnull
  public EChange setPackaging (@Nullable final EMavenPackaging ePackaging)
  {
    if (EqualsHelper.equals (m_ePackaging, ePackaging))
      return EChange.UNCHANGED;
    m_ePackaging = ePackaging;
    return EChange.CHANGED;
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <MavenArtifactRepositoryInfo> getAllArtifactRepositoryInfos ()
  {
    return m_aDesiredRepos.copyOfValues ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsCollection <MavenRepositoryInfo> getAllDesiredRepos ()
  {
    final ICommonsList <MavenRepositoryInfo> ret = new CommonsArrayList<> ();
    for (final MavenArtifactRepositoryInfo aRepoData : m_aDesiredRepos.values ())
      ret.add (aRepoData.getRepoInfo ());
    return ret;
  }

  @Nonnegative
  public int getDesiredRepoCount ()
  {
    return m_aDesiredRepos.size ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <MavenRepositoryInfo> getAllValidDesiredRepos ()
  {
    final ICommonsList <MavenRepositoryInfo> ret = new CommonsArrayList<> ();
    for (final MavenArtifactRepositoryInfo aRepoData : m_aDesiredRepos.values ())
    {
      final MavenRepositoryInfo aRepoInfo = aRepoData.getRepoInfo ();
      if (aRepoInfo.isValid () && !aRepoInfo.isLegacyLayout ())
      {
        // Put central on top
        if (aRepoInfo.isCentral ())
          ret.add (0, aRepoInfo);
        else
          ret.add (aRepoInfo);
      }
    }
    return ret;
  }

  @Nonnegative
  public int getAllValidDesiredRepoCount ()
  {
    int ret = 0;
    for (final MavenArtifactRepositoryInfo aRepoData : m_aDesiredRepos.values ())
    {
      final MavenRepositoryInfo aRepoInfo = aRepoData.getRepoInfo ();
      if (aRepoInfo.isValid () && !aRepoInfo.isLegacyLayout ())
        ++ret;
    }
    return ret;
  }

  @Nonnull
  EChange addDesiredRepo (@Nonnull final MavenArtifactRepositoryInfo aDesiredRepo)
  {
    ValueEnforcer.notNull (aDesiredRepo, "DesiredRepo");

    final String sID = aDesiredRepo.getID ();
    if (m_aDesiredRepos.containsKey (sID))
      return EChange.UNCHANGED;
    m_aDesiredRepos.put (sID, aDesiredRepo);
    return EChange.CHANGED;
  }

  @Nonnull
  public EChange addDesiredRepo (@Nonnull final MavenRepositoryInfo aDesiredRepo)
  {
    ValueEnforcer.notNull (aDesiredRepo, "DesiredRepo");

    return addDesiredRepo (new MavenArtifactRepositoryInfo (aDesiredRepo));
  }

  public boolean hasDesiredRepo (@Nullable final String sRepoID)
  {
    return m_aDesiredRepos.containsKey (sRepoID);
  }

  @Nullable
  public MavenVersion getLatestReleaseVersionOfRepository (@Nullable final MavenRepositoryInfo aDesiredRepo)
  {
    if (aDesiredRepo == null)
      return null;

    return getLatestReleaseVersionOfRepository (aDesiredRepo.getID ());
  }

  @Nullable
  public MavenVersion getLatestReleaseVersionOfRepository (@Nullable final String sDesiredRepoID)
  {
    if (StringHelper.hasNoText (sDesiredRepoID))
      return null;

    final MavenArtifactRepositoryInfo aRepoData = m_aDesiredRepos.get (sDesiredRepoID);
    return aRepoData == null ? null : aRepoData.getLatestReleaseVersion ();
  }

  @Nonnull
  public EChange setLatestReleaseVersionOfRepository (@Nonnull final MavenRepositoryInfo aDesiredRepo,
                                                      @Nullable final MavenVersion aRelevantReleaseVersionOfThisRepo,
                                                      @Nonnull final LocalDateTime aUpdateDT)
  {
    ValueEnforcer.notNull (aDesiredRepo, "DesiredRepo");
    ValueEnforcer.notNull (aUpdateDT, "UpdateDT");

    final String sID = aDesiredRepo.getID ();
    final MavenArtifactRepositoryInfo aRepoData = m_aDesiredRepos.get (sID);
    if (aRepoData == null)
      return EChange.UNCHANGED;

    return aRepoData.setLatestReleaseVersion (aRelevantReleaseVersionOfThisRepo, aUpdateDT);
  }

  @Nullable
  public MavenVersion getLatestBetaVersionOfRepository (@Nullable final MavenRepositoryInfo aDesiredRepo)
  {
    if (aDesiredRepo == null)
      return null;

    final MavenArtifactRepositoryInfo aRepoData = m_aDesiredRepos.get (aDesiredRepo.getID ());
    return aRepoData == null ? null : aRepoData.getLatestBetaVersion ();
  }

  @Nonnull
  public EChange setLatestBetaVersionOfRepository (@Nonnull final MavenRepositoryInfo aDesiredRepo,
                                                   @Nullable final MavenVersion aRelevantBetaVersionOfThisRepo)
  {
    ValueEnforcer.notNull (aDesiredRepo, "DesiredRepo");

    final String sID = aDesiredRepo.getID ();
    final MavenArtifactRepositoryInfo aRepoData = m_aDesiredRepos.get (sID);
    if (aRepoData == null)
      return EChange.UNCHANGED;

    return aRepoData.setLatestBetaVersion (aRelevantBetaVersionOfThisRepo);
  }

  @Nullable
  public MavenVersion getLatestReleaseVersion ()
  {
    return m_aLatestReleaseVersion;
  }

  @Nonnull
  public EChange setLatestReleaseVersion (@Nullable final MavenVersion aLatestReleaseVersion)
  {
    if (m_aLatestReleaseVersion != null)
    {
      if (aLatestReleaseVersion == null)
        throw new IllegalArgumentException ("Cannot set the latest release version to null because a latest release version is already present!");

      // Allow for downgrade
      if (false)
        if (m_aLatestReleaseVersion.isGreaterThan (aLatestReleaseVersion))
          throw new IllegalArgumentException ("New latest release version " +
                                              aLatestReleaseVersion +
                                              " is not greater than " +
                                              m_aLatestReleaseVersion +
                                              " for artifact " +
                                              getID ());
    }
    if (EqualsHelper.equals (aLatestReleaseVersion, m_aLatestReleaseVersion))
      return EChange.UNCHANGED;
    m_aLatestReleaseVersion = aLatestReleaseVersion;
    return EChange.CHANGED;
  }

  @Nullable
  public MavenVersion getLatestBetaVersion ()
  {
    return m_aLatestBetaVersion;
  }

  @Nonnull
  public EChange setLatestBetaVersion (@Nullable final MavenVersion aLatestBetaVersion)
  {
    if (m_aLatestBetaVersion != null)
    {
      if (aLatestBetaVersion == null)
        throw new IllegalArgumentException ("Cannot set the latest beta version to null because a latest version is already present!");

      // Allow for downgrade
      if (false)
        if (m_aLatestBetaVersion.isGreaterThan (aLatestBetaVersion))
          throw new IllegalArgumentException ("New latest beta version " +
                                              aLatestBetaVersion +
                                              " is not greater than " +
                                              m_aLatestBetaVersion +
                                              " for artifact " +
                                              getID ());
    }
    if (EqualsHelper.equals (aLatestBetaVersion, m_aLatestBetaVersion))
      return EChange.UNCHANGED;
    m_aLatestBetaVersion = aLatestBetaVersion;
    return EChange.CHANGED;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <MavenVersion> getAllExcludedVersions ()
  {
    return CollectionHelper.newSet (m_aExcludedVersions);
  }

  @Nonnull
  EChange addExcludedVersion (@Nonnull final MavenVersion aExcludedVersion)
  {
    ValueEnforcer.notNull (aExcludedVersion, "ExcludedVersion");

    return EChange.valueOf (m_aExcludedVersions.add (aExcludedVersion));
  }

  /**
   * Get the repository-relative path to this artifact.
   *
   * @return The path.
   */
  @Nonnull
  @Nonempty
  public String getArtifactPath ()
  {
    return m_sGroupID.replace ('.', '/') + '/' + m_sArtifactID + '/';
  }

  /**
   * Get the repository-relative path to the specified version of this artifact.
   *
   * @param aVersion
   *        The version to use. May not be <code>null</code>.
   * @return The path.
   */
  @Nonnull
  @Nonempty
  public String getVersionPath (@Nonnull final MavenVersion aVersion)
  {
    ValueEnforcer.notNull (aVersion, "Version");

    return getArtifactPath () + aVersion.getOriginalVersion () + '/';
  }

  /**
   * @return The repository-relative path to the maven-metadata.xml file of this
   *         artifact.
   */
  @Nonnull
  @Nonempty
  public String getMetadataPath ()
  {
    return getArtifactPath () + "maven-metadata.xml";
  }

  /**
   * @param aVersion
   *        The version to use. May not be <code>null</code>.
   * @return The repository-relative path to the maven-metadata.xml file of this
   *         artifact in the specified version.
   */
  @Nonnull
  @Nonempty
  public String getVersionMetadataPath (@Nonnull final MavenVersion aVersion)
  {
    return getVersionPath (aVersion) + "maven-metadata.xml";
  }

  /**
   * Get the filename of the pom.xml of this artifact in the passed version.
   * Note: this will return the incorrect version for SNAPSHOT version!
   *
   * @param aVersion
   *        The version to use. May not be <code>null</code>.
   * @return The filename.
   */
  @Nonnull
  @Nonempty
  public String getPOMFilename (@Nonnull final MavenVersion aVersion)
  {
    ValueEnforcer.notNull (aVersion, "Version");

    return m_sArtifactID + '-' + aVersion.getOriginalVersion () + ".pom";
  }

  /**
   * Get the repository-relative path to the pom.xml of this artifact in the
   * passed version. Note: this will return the incorrect version for SNAPSHOT
   * version!
   *
   * @param aVersion
   *        The version to use. May not be <code>null</code>.
   * @return The path.
   */
  @Nonnull
  @Nonempty
  public String getPOMPathAndFilename (@Nonnull final MavenVersion aVersion)
  {
    return getVersionPath (aVersion) + getPOMFilename (aVersion);
  }

  @Nonnull
  private static Duration _getDurationSince (@Nullable final LocalDateTime aDT)
  {
    return aDT == null ? Duration.ofHours (25) : Duration.between (aDT, PDTFactory.getCurrentLocalDateTime ());
  }

  /**
   * @return The date time when the artifact was fetched the last time. May be
   *         <code>null</code> for newly added artifacts.
   */
  @Nullable
  public LocalDateTime getLastUpdateMetaData ()
  {
    return m_aLastUpdateMetaData;
  }

  @Nonnull
  public Duration getDurationSinceLastUpdateMetaData ()
  {
    return _getDurationSince (m_aLastUpdateMetaData);
  }

  @Nonnull
  public EChange setLastUpdateMetaData (@Nullable final LocalDateTime aLastUpdateMetaData)
  {
    if (EqualsHelper.equals (m_aLastUpdateMetaData, aLastUpdateMetaData))
      return EChange.UNCHANGED;
    m_aLastUpdateMetaData = aLastUpdateMetaData;
    return EChange.CHANGED;
  }

  @Nullable
  public LocalDateTime getLastUpdateErrorMetaData ()
  {
    return m_aLastUpdateErrorMetaData;
  }

  @Nonnull
  public Duration getDurationSinceLastUpdateErrorMetaData ()
  {
    return _getDurationSince (m_aLastUpdateErrorMetaData);
  }

  @Nonnull
  public EChange setLastUpdateErrorMetaData (@Nullable final LocalDateTime aLastUpdateErrorMetaData)
  {
    if (EqualsHelper.equals (m_aLastUpdateErrorMetaData, aLastUpdateErrorMetaData))
      return EChange.UNCHANGED;
    m_aLastUpdateErrorMetaData = aLastUpdateErrorMetaData;
    return EChange.CHANGED;
  }

  @Nullable
  public LocalDateTime getLastUpdateErrorRepositories ()
  {
    return m_aLastUpdateErrorRepositories;
  }

  @Nonnull
  public Duration getDurationSinceLastUpdateErrorRepositories ()
  {
    return _getDurationSince (m_aLastUpdateErrorRepositories);
  }

  @Nonnull
  public EChange setLastUpdateErrorRepositories (@Nullable final LocalDateTime aLastUpdateErrorRepositories)
  {
    if (EqualsHelper.equals (m_aLastUpdateErrorRepositories, aLastUpdateErrorRepositories))
      return EChange.UNCHANGED;
    m_aLastUpdateErrorRepositories = aLastUpdateErrorRepositories;
    return EChange.CHANGED;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MavenArtifactInfo))
      return false;
    final MavenArtifactInfo rhs = (MavenArtifactInfo) o;
    return m_sID.equals (rhs.m_sID);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sID).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ID", m_sID)
                                       .append ("groupId", m_sGroupID)
                                       .append ("artifactId", m_sArtifactID)
                                       .append ("packaging", m_ePackaging)
                                       .append ("desiredRepos", m_aDesiredRepos)
                                       .append ("latestReleaseVersion", m_aLatestReleaseVersion)
                                       .append ("latestBetaVersion", m_aLatestBetaVersion)
                                       .append ("excludedVersions", m_aExcludedVersions)
                                       .append ("lastUpdateMetaData", m_aLastUpdateMetaData)
                                       .append ("lastUpdateErrorMetaData", m_aLastUpdateErrorMetaData)
                                       .append ("lastUpdateErrorRepositories", m_aLastUpdateErrorRepositories)
                                       .toString ();
  }
}
