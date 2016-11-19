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
package com.helger.lama.maven.repo;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.PresentForCodeCoverage;
import com.helger.commons.string.StringHelper;
import com.helger.commons.system.SystemProperties;
import com.helger.lama.maven.EMavenPackaging;
import com.helger.lama.maven.artifact.IMavenArtifact;
import com.helger.lama.maven.artifact.IMavenArtifactWithPackaging;

@Immutable
public final class MavenRepositoryHelper
{
  @PresentForCodeCoverage
  private static final MavenRepositoryHelper s_aInstance = new MavenRepositoryHelper ();

  private MavenRepositoryHelper ()
  {}

  @Nonnull
  @Nonempty
  public static String getLocalRepoBaseDir ()
  {
    return SystemProperties.getUserHome () + File.separatorChar + ".m2" + File.separatorChar + "repository" + File.separatorChar;
  }

  @Nonnull
  @Nonempty
  public static String getAbsolutePathInLocalRepo (@Nonnull final IMavenArtifactWithPackaging aArtifact)
  {
    return getAbsolutePathInLocalRepo (aArtifact, aArtifact.getPackaging ());
  }

  @Nonnull
  @Nonempty
  public static String getAbsolutePathInLocalRepo (@Nonnull final IMavenArtifact aArtifact, @Nonnull final EMavenPackaging aPackaging)
  {
    final StringBuilder aSB = new StringBuilder (getLocalRepoBaseDir ());
    aSB.append (aArtifact.getGroupID ().replace ('.', File.separatorChar))
       .append (File.separatorChar)
       .append (aArtifact.getArtifactID ())
       .append (File.separatorChar)
       .append (aArtifact.getVersion ())
       .append (File.separatorChar)
       .append (aArtifact.getArtifactID ())
       .append ('-')
       .append (aArtifact.getVersion ());
    if (StringHelper.hasText (aArtifact.getClassifier ()))
      aSB.append ('-').append (aArtifact.getClassifier ());
    return aSB.append (aPackaging.getFileExtension ()).toString ();
  }
}
