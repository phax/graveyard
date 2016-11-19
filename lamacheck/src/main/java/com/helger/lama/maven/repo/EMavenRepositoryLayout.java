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
package com.helger.lama.maven.repo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.id.IHasID;
import com.helger.commons.lang.EnumHelper;

public enum EMavenRepositoryLayout implements IHasID <String>
{
 MAVEN2 ("default"),
 LEGACY ("legacy");

  public static final EMavenRepositoryLayout DEFAULT = MAVEN2;

  private final String m_sID;

  private EMavenRepositoryLayout (@Nonnull @Nonempty final String sID)
  {
    m_sID = sID;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  public boolean isDefault ()
  {
    return this == DEFAULT;
  }

  @Nullable
  public static EMavenRepositoryLayout getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EMavenRepositoryLayout.class, sID);
  }

  @Nullable
  public static EMavenRepositoryLayout getFromIDOrDefault (@Nullable final String sID, @Nullable final EMavenRepositoryLayout eDefault)
  {
    return EnumHelper.getFromIDOrDefault (EMavenRepositoryLayout.class, sID, eDefault);
  }
}
