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

import javax.annotation.Nonnull;

import com.helger.commons.annotation.UsedViaReflection;
import com.helger.commons.exception.InitializationException;
import com.helger.commons.scope.IScope;
import com.helger.commons.scope.singleton.AbstractGlobalSingleton;
import com.helger.photon.basic.app.dao.impl.DAOException;

public final class LamaMetaManager extends AbstractGlobalSingleton
{
  public static final String MAVEN_REPO_XML = "maven-repos.xml";
  public static final String MAVEN_ARTIFACT_XML = "maven-artifacts.xml";

  private MavenRepositoryInfoManager m_aRepoInfoMgr;
  private MavenArtifactInfoManager m_aArtifactInfoMgr;

  @Deprecated
  @UsedViaReflection
  public LamaMetaManager ()
  {}

  @Override
  protected void onAfterInstantiation (@Nonnull final IScope aScope)
  {
    try
    {
      m_aRepoInfoMgr = new MavenRepositoryInfoManager (MAVEN_REPO_XML);
      // Must be after m_aRepoInfoMgr
      m_aArtifactInfoMgr = new MavenArtifactInfoManager (MAVEN_ARTIFACT_XML);
    }
    catch (final DAOException ex)
    {
      throw new InitializationException ("Failed to init managers", ex);
    }
  }

  @Nonnull
  public static LamaMetaManager getInstance ()
  {
    return getGlobalSingleton (LamaMetaManager.class);
  }

  @Nonnull
  public static MavenArtifactInfoManager getArtifactInfoMgr ()
  {
    return getInstance ().m_aArtifactInfoMgr;
  }

  @Nonnull
  public static MavenRepositoryInfoManager getRepoInfoMgr ()
  {
    return getInstance ().m_aRepoInfoMgr;
  }
}
