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
package com.helger.lama.config;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.IsSPIImplementation;
import com.helger.lama.maven.repo.MavenRepository;
import com.helger.lama.maven.repo.MavenRepositoryMicroTypeConverter;
import com.helger.lama.updater.domain.MavenArtifactInfo;
import com.helger.lama.updater.domain.MavenArtifactInfoMicroTypeConverter;
import com.helger.lama.updater.domain.MavenRepositoryInfo;
import com.helger.lama.updater.domain.MavenRepositoryInfoMicroTypeConverter;
import com.helger.lama.updater.mgr.IMavenRepositoryInfoResolver;
import com.helger.lama.updater.mgr.LamaMetaManager;
import com.helger.xml.microdom.convert.IMicroTypeConverterRegistrarSPI;
import com.helger.xml.microdom.convert.IMicroTypeConverterRegistry;

@IsSPIImplementation
public final class LamaCheckMicroTypeConverterRegistrySPI implements IMicroTypeConverterRegistrarSPI
{
  public void registerMicroTypeConverter (@Nonnull final IMicroTypeConverterRegistry aRegistry)
  {
    // Indirection level required, because otherwise the LamaMetaManager would
    // be instantiated in here!
    final IMavenRepositoryInfoResolver aRepoResolver = sID -> LamaMetaManager.getRepoInfoMgr ().getRepositoryOfID (sID);

    aRegistry.registerMicroElementTypeConverter (MavenRepository.class, new MavenRepositoryMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (MavenRepositoryInfo.class, new MavenRepositoryInfoMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (MavenArtifactInfo.class, new MavenArtifactInfoMicroTypeConverter (aRepoResolver));
  }
}
