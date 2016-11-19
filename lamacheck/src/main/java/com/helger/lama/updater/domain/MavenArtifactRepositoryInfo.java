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

import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.id.IHasID;
import com.helger.commons.state.EChange;
import com.helger.commons.string.ToStringGenerator;
import com.helger.lama.maven.version.MavenVersion;

public final class MavenArtifactRepositoryInfo implements IHasID <String>
{
  private final MavenRepositoryInfo m_aRepoInfo;
  private MavenVersion m_aLatestReleaseVersion;
  private MavenVersion m_aLatestBetaVersion;
  private LocalDateTime m_aUpdateSuccessDT;
  private LocalDateTime m_aUpdateErrorDT;

  MavenArtifactRepositoryInfo (@Nonnull final MavenRepositoryInfo aRepoInfo)
  {
    ValueEnforcer.notNull (aRepoInfo, "RepoInfo");
    m_aRepoInfo = aRepoInfo;
  }

  MavenArtifactRepositoryInfo (@Nonnull final MavenRepositoryInfo aRepoInfo,
                               @Nullable final String sLatestReleaseVersion,
                               @Nullable final String sLatestBetaVersion,
                               @Nullable final LocalDateTime aUpdateSuccessDT,
                               @Nullable final LocalDateTime aUpdateErrorDT)
  {
    ValueEnforcer.notNull (aRepoInfo, "RepoInfo");
    m_aRepoInfo = aRepoInfo;
    m_aLatestReleaseVersion = sLatestReleaseVersion == null ? null : new MavenVersion (sLatestReleaseVersion);
    m_aLatestBetaVersion = sLatestBetaVersion == null ? null : new MavenVersion (sLatestBetaVersion);
    m_aUpdateSuccessDT = aUpdateSuccessDT;
    m_aUpdateErrorDT = aUpdateErrorDT;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_aRepoInfo.getID ();
  }

  @Nonnull
  public MavenRepositoryInfo getRepoInfo ()
  {
    return m_aRepoInfo;
  }

  @Nullable
  public MavenVersion getLatestReleaseVersion ()
  {
    return m_aLatestReleaseVersion;
  }

  @Nonnull
  public EChange setLatestReleaseVersion (@Nullable final MavenVersion aLatestReleaseVersion,
                                          @Nonnull final LocalDateTime aUpdateDT)
  {
    if (aLatestReleaseVersion == null)
      m_aUpdateErrorDT = aUpdateDT;
    else
      m_aUpdateSuccessDT = aUpdateDT;

    if (EqualsHelper.equals (m_aLatestReleaseVersion, aLatestReleaseVersion))
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
    if (EqualsHelper.equals (m_aLatestBetaVersion, aLatestBetaVersion))
      return EChange.UNCHANGED;
    m_aLatestBetaVersion = aLatestBetaVersion;
    return EChange.CHANGED;
  }

  @Nullable
  public LocalDateTime getUpdateSuccessDT ()
  {
    return m_aUpdateSuccessDT;
  }

  @Nullable
  public LocalDateTime getUpdateErrorDT ()
  {
    return m_aUpdateErrorDT;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("repoInfo", m_aRepoInfo)
                                       .append ("latestReleaseVersion", m_aLatestReleaseVersion)
                                       .append ("latestBetaVersion", m_aLatestBetaVersion)
                                       .append ("updateSuccessDT", m_aUpdateSuccessDT)
                                       .append ("updateErrorDT", m_aUpdateErrorDT)
                                       .toString ();
  }
}
