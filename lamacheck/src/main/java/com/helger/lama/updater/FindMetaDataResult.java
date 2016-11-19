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

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.string.ToStringGenerator;
import com.helger.lama.maven.metadata.MavenMetaData;
import com.helger.lama.maven.version.MavenVersion;
import com.helger.lama.updater.domain.MavenRepositoryInfo;

/**
 * This class is basically a map from Repo to MavenMetadata. It only contains
 * the items with the latest version!
 *
 * @author Philip Helger
 */
public final class FindMetaDataResult
{
  public static final class Item
  {
    private final MavenRepositoryInfo m_aRepoInfo;
    private final MavenMetaData m_aMetaData;

    Item (@Nonnull final MavenRepositoryInfo aRepoInfo, @Nonnull final MavenMetaData aMetaData)
    {
      ValueEnforcer.notNull (aRepoInfo, "RepoInfo");
      ValueEnforcer.notNull (aMetaData, "MetaData");
      m_aRepoInfo = aRepoInfo;
      m_aMetaData = aMetaData;
    }

    @Nonnull
    public MavenRepositoryInfo getRepoInfo ()
    {
      return m_aRepoInfo;
    }

    @Nonnull
    public MavenMetaData getMetaData ()
    {
      return m_aMetaData;
    }

    @Override
    public String toString ()
    {
      final MavenVersion aLatestReleaseVersion = m_aMetaData.getLatestReleaseVersion ();
      final MavenVersion aLatestVersion = m_aMetaData.getLatestVersion ();
      final ICommonsList <String> aAllVersionStrings = new CommonsArrayList<> (m_aMetaData.getAllVersions (),
                                                                               MavenVersion::getOriginalVersion);
      return new ToStringGenerator (null).append ("RepoID", m_aRepoInfo.getID ())
                                         .append ("LatestReleaseVersion",
                                                  aLatestReleaseVersion == null ? null
                                                                                : aLatestReleaseVersion.getOriginalVersion ())
                                         .append ("LatestVersion",
                                                  aLatestVersion == null ? null : aLatestVersion.getOriginalVersion ())
                                         .append ("AllVersions", aAllVersionStrings)
                                         .toString ();
    }
  }

  private final FindVersionResult m_aLatestVersions;
  private final ICommonsList <Item> m_aReleaseItems = new CommonsArrayList<> ();
  private final ICommonsList <Item> m_aBetaItems = new CommonsArrayList<> ();

  public FindMetaDataResult (@Nonnull final FindVersionResult aLatestVersions)
  {
    if (aLatestVersions == null)
      throw new NullPointerException ("LatestVersions");
    m_aLatestVersions = aLatestVersions;
  }

  public void addReleaseVersion (@Nonnull final MavenRepositoryInfo aRepoInfo,
                                 @Nonnull final MavenMetaData aMetaDataOfThisRepo)
  {
    m_aReleaseItems.add (new Item (aRepoInfo, aMetaDataOfThisRepo));
  }

  public boolean hasReleaseItems ()
  {
    return !m_aReleaseItems.isEmpty ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <Item> getAllReleaseItems ()
  {
    return CollectionHelper.newList (m_aReleaseItems);
  }

  @Nullable
  public MavenVersion getReleaseVersion ()
  {
    return m_aLatestVersions.getReleaseVersion ();
  }

  public void addBetaVersion (@Nonnull final MavenRepositoryInfo aRepoInfo,
                              @Nonnull final MavenMetaData aMetaDataOfThisRepo)
  {
    m_aBetaItems.add (new Item (aRepoInfo, aMetaDataOfThisRepo));
  }

  public boolean hasBetaItems ()
  {
    return !m_aBetaItems.isEmpty ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <Item> getAllBetaItems ()
  {
    return CollectionHelper.newList (m_aBetaItems);
  }

  @Nullable
  public MavenVersion getBetaVersion ()
  {
    return m_aLatestVersions.getBetaVersion ();
  }

  public boolean isBetaVersionSetAndDifferentFromReleaseVersion ()
  {
    return m_aLatestVersions.hasBetaVersion () &&
           (!m_aLatestVersions.hasReleaseVersion () ||
            !m_aLatestVersions.getReleaseVersion ().equals (m_aLatestVersions.getBetaVersion ()));
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("LatestVersions", m_aLatestVersions)
                                       .append ("ReleaseItems", m_aReleaseItems)
                                       .append ("BetaItems", m_aBetaItems)
                                       .toString ();
  }
}
