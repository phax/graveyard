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
package com.helger.lama.maven.version;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.MustImplementEqualsAndHashcode;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.regex.RegExHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.commons.version.Version;

@MustImplementEqualsAndHashcode
public final class MavenVersion implements Comparable <MavenVersion>
{
  /**
   * A list of constant all-lowercase identifiers, that identify pre-release
   * artifacts
   */
  private static final ICommonsList <String> PRERELEASE_QUALIFIER_CONST = new CommonsArrayList<> ();

  /** A list of regular expressions, that identify pre-release artifacts */
  private static final ICommonsList <String> PRERELEASE_QUALIFIER_REGEX = new CommonsArrayList<> ();

  static
  {
    PRERELEASE_QUALIFIER_CONST.add ("snapshot");
    PRERELEASE_QUALIFIER_CONST.add ("alpha");
    PRERELEASE_QUALIFIER_CONST.add ("beta");
    PRERELEASE_QUALIFIER_CONST.add ("incubator");
    PRERELEASE_QUALIFIER_CONST.add ("dev");
    // For things like "b197" or "M5" or "pre7a"
    PRERELEASE_QUALIFIER_REGEX.add (".*(b|ea|m|rc|pre)[0-9]+[a-zA-Z]*");
    // For things like "M2.1"
    PRERELEASE_QUALIFIER_REGEX.add (".*(b|ea|m|rc|pre)[0-9]+(?:\\.[0-9]+)");
  }

  private final Version m_aVersion;
  private final String m_sOriginalVersion;
  private final boolean m_bIsSnapshotVersion;
  private final boolean m_bIsReleaseVersion;

  public static boolean isReleaseVersion (@Nonnull final Version aVersion)
  {
    final String sQualifier = aVersion.getQualifier ();
    if (sQualifier != null)
    {
      final String sRealQualifier = sQualifier.toLowerCase (Locale.US);
      for (final String sPart : PRERELEASE_QUALIFIER_CONST)
        if (sRealQualifier.contains (sPart))
          return false;
      for (final String sRegEx : PRERELEASE_QUALIFIER_REGEX)
        if (RegExHelper.stringMatchesPattern (sRegEx, sRealQualifier))
          return false;
    }
    return true;
  }

  public MavenVersion (@Nonnull final String sOriginalVersion)
  {
    m_aVersion = MavenVersionParser.parse (sOriginalVersion);
    m_sOriginalVersion = sOriginalVersion;
    m_bIsSnapshotVersion = sOriginalVersion.endsWith ("-SNAPSHOT");
    m_bIsReleaseVersion = isReleaseVersion (m_aVersion);
  }

  @Nonnull
  public String getAsString ()
  {
    return MavenVersionParser.asString (m_aVersion);
  }

  @Nonnull
  public String getOriginalVersion ()
  {
    return m_sOriginalVersion;
  }

  public boolean isSnapshotVersion ()
  {
    return m_bIsSnapshotVersion;
  }

  public boolean isReleaseVersion ()
  {
    return m_bIsReleaseVersion;
  }

  public boolean isGreaterThan (@Nonnull final MavenVersion aVersion)
  {
    return m_aVersion.isGreaterThan (aVersion.m_aVersion);
  }

  public int compareTo (@Nonnull final MavenVersion rhs)
  {
    return m_aVersion.compareTo (rhs.m_aVersion);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MavenVersion))
      return false;
    final MavenVersion rhs = (MavenVersion) o;
    return m_sOriginalVersion.equals (rhs.m_sOriginalVersion);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sOriginalVersion).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("version", m_aVersion)
                                       .append ("orig", m_sOriginalVersion)
                                       .append ("isRelease", m_bIsReleaseVersion)
                                       .toString ();
  }

  @Nullable
  public static MavenVersion create (@Nullable final String sVersion)
  {
    return sVersion == null ? null : new MavenVersion (sVersion);
  }
}
