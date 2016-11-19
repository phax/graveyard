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
package com.helger.lama.updater.utils;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.ext.CommonsConcurrentHashMap;
import com.helger.commons.collection.ext.ICommonsMap;
import com.helger.commons.collection.ext.ICommonsSet;
import com.helger.commons.string.ToStringGenerator;
import com.helger.lama.updater.domain.MavenRepositoryInfo;

/**
 * In-memory black list with all prohibited repos.
 *
 * @author Philip Helger
 */
public final class UpdateBlacklist
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (UpdateBlacklist.class);
  private final ICommonsMap <String, MavenRepositoryInfo> m_aMap = new CommonsConcurrentHashMap<> ();

  public UpdateBlacklist ()
  {}

  public boolean containsRepo (@Nonnull final MavenRepositoryInfo aRepoInfo)
  {
    ValueEnforcer.notNull (aRepoInfo, "RepoInfo");

    return m_aMap.containsKey (aRepoInfo.getID ());
  }

  public void addRepo (@Nonnull final MavenRepositoryInfo aRepoInfo)
  {
    ValueEnforcer.notNull (aRepoInfo, "RepoInfo");

    final String sID = aRepoInfo.getID ();
    m_aMap.computeIfAbsent (sID, k -> {
      s_aLogger.warn ("Added repository " + aRepoInfo.getURL () + " (" + sID + ") to blacklist");
      return aRepoInfo;
    });
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsSet <String> getAllIDs ()
  {
    return m_aMap.copyOfKeySet ();
  }

  @Nonnegative
  public int getCount ()
  {
    return m_aMap.size ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("Map", m_aMap).toString ();
  }
}
