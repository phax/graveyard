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
package com.helger.lamaweb.page.view;

import java.util.Collection;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.lama.updater.domain.MavenArtifactInfo;
import com.helger.lama.updater.mgr.LamaMetaManager;

public final class PageViewArtifactsAll extends AbstractPageViewArtifacts
{
  public PageViewArtifactsAll (@Nonnull @Nonempty final String sID)
  {
    super (sID, "Maven Artifacts");
  }

  @Override
  @Nonnull
  protected Collection <MavenArtifactInfo> getRelevantArtifacts ()
  {
    return LamaMetaManager.getArtifactInfoMgr ().getAllArtifacts ();
  }
}
