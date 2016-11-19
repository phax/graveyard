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
package com.helger.lama.maven.artifact;

import javax.annotation.Nonnull;

import com.helger.lama.maven.EMavenPackaging;

/**
 * Interface for a single artifact that has a specific packaging.
 *
 * @author Philip Helger
 */
public interface IMavenArtifactWithPackaging extends IMavenArtifact
{
  @Nonnull
  EMavenPackaging getPackaging ();
}
