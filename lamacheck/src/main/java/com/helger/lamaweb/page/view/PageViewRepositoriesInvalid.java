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

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsCollection;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.lama.updater.domain.MavenRepositoryInfo;
import com.helger.lama.updater.mgr.LamaMetaManager;

public final class PageViewRepositoriesInvalid extends AbstractPageViewRepositories
{
  public PageViewRepositoriesInvalid (@Nonnull @Nonempty final String sID)
  {
    super (sID, "Invalid Repositories");
  }

  @Override
  @Nonnull
  protected ICommonsCollection <MavenRepositoryInfo> getRelevantRepositories ()
  {
    final ICommonsList <MavenRepositoryInfo> ret = new CommonsArrayList<> ();
    for (final MavenRepositoryInfo aRepoInfo : LamaMetaManager.getRepoInfoMgr ().getAllRepositories ())
      if (aRepoInfo.isInvalid ())
        ret.add (aRepoInfo);
    return ret;
  }
}
