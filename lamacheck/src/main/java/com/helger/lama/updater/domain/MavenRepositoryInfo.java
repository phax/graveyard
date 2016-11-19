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
package com.helger.lama.updater.domain;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.id.IHasID;
import com.helger.commons.state.EChange;
import com.helger.commons.state.IValidityIndicator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.lama.maven.repo.MavenRepository;

public class MavenRepositoryInfo implements IValidityIndicator, IHasID <String>
{
  private final MavenRepository m_aRepo;
  private boolean m_bIsInvalid;
  private String m_sNote;

  public MavenRepositoryInfo (@Nonnull final MavenRepository aRepo, final boolean bIsInvalid)
  {
    ValueEnforcer.notNull (aRepo, "Repo");
    m_aRepo = aRepo;
    m_bIsInvalid = bIsInvalid;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_aRepo.getID ();
  }

  /**
   * @return The base URL of the repository. Neither <code>null</code> nor
   *         empty.
   */
  @Nonnull
  @Nonempty
  public String getURL ()
  {
    return m_aRepo.getURL ();
  }

  @Nonnull
  public MavenRepository getRepo ()
  {
    return m_aRepo;
  }

  public boolean isCentral ()
  {
    return m_aRepo.isCentral ();
  }

  public boolean isLegacyLayout ()
  {
    return m_aRepo.isLegacyLayout ();
  }

  /**
   * @return <code>true</code> if this repository is considered valid,
   *         <code>false</code> if not
   */
  public boolean isValid ()
  {
    return !m_bIsInvalid;
  }

  /**
   * @return <code>true</code> if this repository is considered invalid,
   *         <code>false</code> if not
   */
  public boolean isInvalid ()
  {
    return m_bIsInvalid;
  }

  @Nonnull
  public EChange setInvalid (final boolean bIsInvalid)
  {
    if (m_bIsInvalid == bIsInvalid)
      return EChange.UNCHANGED;
    m_bIsInvalid = bIsInvalid;
    return EChange.CHANGED;
  }

  @Nullable
  public EChange setNote (@Nullable final String sNote)
  {
    if (EqualsHelper.equals (sNote, m_sNote))
      return EChange.UNCHANGED;
    m_sNote = sNote;
    return EChange.CHANGED;
  }

  @Nullable
  public String getNote ()
  {
    return m_sNote;
  }

  public boolean hasNote ()
  {
    return StringHelper.hasText (m_sNote);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("repo", m_aRepo).append ("isInvalid", m_bIsInvalid).appendIfNotNull ("note", m_sNote).toString ();
  }
}
