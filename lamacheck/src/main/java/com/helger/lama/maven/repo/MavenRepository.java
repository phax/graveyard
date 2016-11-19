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

import java.net.URL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.annotation.MustImplementEqualsAndHashcode;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.id.IHasID;
import com.helger.commons.io.resource.URLResource;
import com.helger.commons.io.stream.StreamHelper;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.commons.url.URLHelper;

@NotThreadSafe
@MustImplementEqualsAndHashcode
public final class MavenRepository implements IHasID <String>
{
  public static final String ID_CENTRAL1 = "central1";
  public static final String ID_CENTRAL2 = "central2";
  public static final boolean DEFAULT_RELEASES = true;
  public static final boolean DEFAULT_SNAPSHOTS = true;

  private String m_sID;
  private final String m_sURL;
  private final EMavenRepositoryLayout m_eLayout;
  private final String m_sUserName;
  private final String m_sPassword;
  private final boolean m_bReleases;
  private final boolean m_bSnapshots;

  public MavenRepository (@Nonnull @Nonempty final String sID,
                          @Nonnull @Nonempty final String sURL,
                          @Nonnull final EMavenRepositoryLayout eLayout,
                          @Nullable final String sUserName,
                          @Nullable final String sPassword,
                          final boolean bReleases,
                          final boolean bSnapshots)
  {
    if (StringHelper.hasNoText (sID))
      throw new IllegalArgumentException ("ID may not be empty!");
    if (StringHelper.hasNoText (sURL))
      throw new IllegalArgumentException ("URL may not be empty!");
    if (eLayout == null)
      throw new NullPointerException ("layout");
    m_sID = StringHelper.trim (sID);
    final String sRealURL = StringHelper.trim (sURL);
    m_sURL = StringHelper.endsWith (sRealURL, '/') ? sRealURL : sRealURL + '/';
    m_eLayout = eLayout;
    m_sUserName = sUserName;
    m_sPassword = sPassword;
    m_bReleases = bReleases;
    m_bSnapshots = bSnapshots;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  public void setID (@Nonnull @Nonempty final String sID)
  {
    if (StringHelper.hasNoText (sID))
      throw new IllegalArgumentException ("ID may not be empty!");
    m_sID = sID;
  }

  public boolean isCentral ()
  {
    return m_sID.equals (MavenRepository.ID_CENTRAL1) || m_sID.equals (MavenRepository.ID_CENTRAL2);
  }

  /**
   * @return The base URL of the repository. Neither <code>null</code> nor
   *         empty.
   */
  @Nonnull
  @Nonempty
  public String getURL ()
  {
    return m_sURL;
  }

  /**
   * @return The base URL of the repository. Neither <code>null</code> nor
   *         empty.
   */
  @Nullable
  public URL getAsURL ()
  {
    return URLHelper.getAsURL (m_sURL);
  }

  /**
   * @return The layout of the repository. Never <code>null</code>.
   */
  @Nonnull
  public EMavenRepositoryLayout getLayout ()
  {
    return m_eLayout;
  }

  public boolean isLegacyLayout ()
  {
    return m_eLayout.equals (EMavenRepositoryLayout.LEGACY);
  }

  @Nullable
  public String getUserName ()
  {
    return m_sUserName;
  }

  @Nullable
  public String getPassword ()
  {
    return m_sPassword;
  }

  public boolean isReleasesEnabled ()
  {
    return m_bReleases;
  }

  public boolean isSnapshotsEnabled ()
  {
    return m_bSnapshots;
  }

  public boolean isReachable ()
  {
    return StreamHelper.getAllBytes (new URLResource (getAsURL ())) != null;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof MavenRepository))
      return false;
    final MavenRepository rhs = (MavenRepository) o;
    return m_sID.equals (rhs.m_sID) &&
           m_sURL.equals (rhs.m_sURL) &&
           m_eLayout.equals (rhs.m_eLayout) &&
           EqualsHelper.equals (m_sUserName, rhs.m_sUserName) &&
           EqualsHelper.equals (m_sPassword, rhs.m_sPassword) &&
           m_bReleases == rhs.m_bReleases &&
           m_bSnapshots == rhs.m_bSnapshots;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sID)
                                       .append (m_sURL)
                                       .append (m_eLayout)
                                       .append (m_sUserName)
                                       .append (m_sPassword)
                                       .append (m_bReleases)
                                       .append (m_bSnapshots)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    final ToStringGenerator ret = new ToStringGenerator (null).append ("id", m_sID).append ("URL", m_sURL).append ("layout", m_eLayout);
    if (m_sUserName != null)
      ret.append ("userName", m_sUserName).appendPassword (m_sPassword);
    return ret.append ("releases", m_bReleases).append ("snapshots", m_bSnapshots).toString ();
  }
}
