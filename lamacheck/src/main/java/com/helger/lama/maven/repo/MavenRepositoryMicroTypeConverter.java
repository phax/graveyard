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
import com.helger.commons.string.StringParser;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.MicroElement;
import com.helger.xml.microdom.convert.IMicroTypeConverter;

public final class MavenRepositoryMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ATTR_ID = "id";
  private static final String ATTR_URL = "url";
  private static final String ATTR_LAYOUT = "layout";
  private static final String ATTR_USERNAME = "username";
  private static final String ATTR_PASSWORD = "password";
  private static final String ATTR_RELEASES = "releases";
  private static final String ATTR_SNAPSHOTS = "snapshots";

  public MavenRepositoryMicroTypeConverter ()
  {}

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull @Nonempty final String sTagName)
  {
    final MavenRepository aRepo = (MavenRepository) aObject;
    final IMicroElement eRepo = new MicroElement (sNamespaceURI, sTagName);
    eRepo.setAttribute (ATTR_ID, aRepo.getID ());
    eRepo.setAttribute (ATTR_URL, aRepo.getURL ());
    eRepo.setAttribute (ATTR_LAYOUT, aRepo.getLayout ().getID ());
    eRepo.setAttribute (ATTR_USERNAME, aRepo.getUserName ());
    eRepo.setAttribute (ATTR_PASSWORD, aRepo.getPassword ());
    eRepo.setAttribute (ATTR_RELEASES, Boolean.toString (aRepo.isReleasesEnabled ()));
    eRepo.setAttribute (ATTR_SNAPSHOTS, Boolean.toString (aRepo.isSnapshotsEnabled ()));
    return eRepo;
  }

  @Nonnull
  public MavenRepository convertToNative (@Nonnull final IMicroElement eRepo)
  {
    final String sID = eRepo.getAttributeValue (ATTR_ID);
    final String sURL = eRepo.getAttributeValue (ATTR_URL);
    final String sLayout = eRepo.getAttributeValue (ATTR_LAYOUT);
    final EMavenRepositoryLayout eLayout = EMavenRepositoryLayout.getFromIDOrNull (sLayout);
    final String sUserName = eRepo.getAttributeValue (ATTR_USERNAME);
    final String sPassword = eRepo.getAttributeValue (ATTR_PASSWORD);
    final String sReleases = eRepo.getAttributeValue (ATTR_RELEASES);
    final boolean bReleases = StringParser.parseBool (sReleases);
    final String sSnapshots = eRepo.getAttributeValue (ATTR_SNAPSHOTS);
    final boolean bSnapshots = StringParser.parseBool (sSnapshots);

    return new MavenRepository (sID, sURL, eLayout, sUserName, sPassword, bReleases, bSnapshots);
  }
}
