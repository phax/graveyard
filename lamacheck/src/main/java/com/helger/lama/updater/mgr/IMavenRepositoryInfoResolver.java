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
package com.helger.lama.updater.mgr;

import javax.annotation.Nullable;

import com.helger.lama.updater.domain.MavenRepositoryInfo;

public interface IMavenRepositoryInfoResolver
{
  @Nullable
  MavenRepositoryInfo getRepositoryOfID (@Nullable String sID);
}
