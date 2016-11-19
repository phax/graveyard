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

import javax.annotation.Nullable;

import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.lama.maven.version.MavenVersion;

public final class FindVersionResult
{
  private final MavenVersion m_aReleaseVersion;
  private final MavenVersion m_aBetaVersion;

  public FindVersionResult (@Nullable final MavenVersion aRelease, @Nullable final MavenVersion aBeta)
  {
    if (aRelease == null && aBeta == null)
      throw new IllegalArgumentException ("One must be set!");
    m_aReleaseVersion = aRelease;
    m_aBetaVersion = aBeta;
  }

  public boolean isLatestReleaseVersion (@Nullable final MavenVersion aVersion)
  {
    return m_aReleaseVersion != null && aVersion != null && m_aReleaseVersion.equals (aVersion);
  }

  public boolean hasReleaseVersion ()
  {
    return m_aReleaseVersion != null;
  }

  @Nullable
  public MavenVersion getReleaseVersion ()
  {
    return m_aReleaseVersion;
  }

  public boolean isLatestBetaVersion (@Nullable final MavenVersion aVersion)
  {
    return m_aBetaVersion != null && aVersion != null && m_aBetaVersion.equals (aVersion);
  }

  public boolean hasBetaVersion ()
  {
    return m_aBetaVersion != null;
  }

  @Nullable
  public MavenVersion getBetaVersion ()
  {
    return m_aBetaVersion;
  }

  public boolean isLatestBetaEqualToLatestRelease ()
  {
    return EqualsHelper.equals (m_aReleaseVersion, m_aBetaVersion);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).appendIfNotNull ("ReleaseVersion", m_aReleaseVersion == null ? null : m_aReleaseVersion.getOriginalVersion ())
                                       .appendIfNotNull ("BetaVersion", m_aBetaVersion == null ? null : m_aBetaVersion.getOriginalVersion ())
                                       .toString ();
  }
}
