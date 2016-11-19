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
package com.helger.lama.maven.metadata;

import java.util.Set;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.filter.IFilter;
import com.helger.commons.string.ToStringGenerator;
import com.helger.lama.maven.version.MavenVersion;

public final class MavenVersionFilterExcludeVersions implements IFilter <MavenVersion>
{
  private final Set <MavenVersion> m_aExcludeVersions;

  public MavenVersionFilterExcludeVersions (@Nonnull final Set <MavenVersion> aExcludeVersions)
  {
    ValueEnforcer.notNull (aExcludeVersions, "EWxcludeVersions");
    m_aExcludeVersions = aExcludeVersions;
  }

  public boolean test (final MavenVersion aReadVersion)
  {
    return !m_aExcludeVersions.contains (aReadVersion);
  }

  @Override
  public String toString ()
  {
    final ICommonsList <String> aAllVersionStrings = new CommonsArrayList<> (m_aExcludeVersions,
                                                                             MavenVersion::getOriginalVersion);
    return new ToStringGenerator (null).append ("ExcludeVersions", aAllVersionStrings).toString ();
  }
}
