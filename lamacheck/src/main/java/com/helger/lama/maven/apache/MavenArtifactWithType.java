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
package com.helger.lama.maven.apache;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.lama.maven.artifact.IMavenArtifact;

public class MavenArtifactWithType
{
  private final String m_sLocation;
  private final IMavenArtifact m_aArtifact;

  public MavenArtifactWithType (@Nonnull @Nonempty final String sLocation, @Nonnull final IMavenArtifact aArtifact)
  {
    m_sLocation = sLocation;
    m_aArtifact = aArtifact;
  }

  @Nonnull
  @Nonempty
  public String getLocation ()
  {
    return m_sLocation;
  }

  @Nonnull
  public IMavenArtifact getArtifact ()
  {
    return m_aArtifact;
  }
}
