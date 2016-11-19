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

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.string.StringParser;
import com.helger.lama.maven.repo.MavenRepository;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.convert.IMicroTypeConverter;
import com.helger.xml.microdom.convert.MicroTypeConverter;
import com.helger.xml.microdom.util.MicroHelper;

public final class MavenRepositoryInfoMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ATTR_INVALID = "invalid";
  private static final String ELEMENT_NOTE = "note";

  public MavenRepositoryInfoMicroTypeConverter ()
  {}

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull @Nonempty final String sTagName)
  {
    final MavenRepositoryInfo aRepo = (MavenRepositoryInfo) aObject;
    final IMicroElement eRepo = MicroTypeConverter.convertToMicroElement (aRepo.getRepo (), sNamespaceURI, sTagName);
    eRepo.setAttribute (ATTR_INVALID, Boolean.toString (aRepo.isInvalid ()));
    if (aRepo.hasNote ())
      eRepo.appendElement (sNamespaceURI, ELEMENT_NOTE).appendText (aRepo.getNote ());
    return eRepo;
  }

  @Nonnull
  public MavenRepositoryInfo convertToNative (@Nonnull final IMicroElement eRepo)
  {
    final MavenRepository aRepo = MicroTypeConverter.convertToNative (eRepo, MavenRepository.class);
    final String sIsInvalid = eRepo.getAttributeValue (ATTR_INVALID);
    final boolean bIsInvalid = StringParser.parseBool (sIsInvalid);
    final String sNote = MicroHelper.getChildTextContent (eRepo, ELEMENT_NOTE);

    final MavenRepositoryInfo ret = new MavenRepositoryInfo (aRepo, bIsInvalid);
    ret.setNote (sNote);
    return ret;
  }
}
