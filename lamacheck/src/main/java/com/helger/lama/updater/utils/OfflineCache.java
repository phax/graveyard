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

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.lama.updater.domain.MavenRepositoryInfo;
import com.helger.photon.basic.app.io.WebFileIO;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.serialize.MicroWriter;

@Immutable
public final class OfflineCache
{
  public static final String DIR_OFFLINE_CACHE = "offline-cache/";
  private static final Logger s_aLogger = LoggerFactory.getLogger (OfflineCache.class);

  private OfflineCache ()
  {}

  @Nonnull
  public static File getOfflineCacheFile (@Nonnull final MavenRepositoryInfo aRepoInfo, @Nonnull @Nonempty final String sRelativeFilename)
  {
    ValueEnforcer.notNull (aRepoInfo, "RepoInfo");
    ValueEnforcer.notEmpty (sRelativeFilename, "RelativeFilename");

    final String sPath = DIR_OFFLINE_CACHE + aRepoInfo.getID () + "/" + sRelativeFilename;
    return WebFileIO.getDataIO ().getFile (sPath);
  }

  public static void putInOfflineCache (@Nonnull final MavenRepositoryInfo aRepoInfo,
                                        @Nonnull @Nonempty final String sRelativeFilename,
                                        @Nonnull final IMicroDocument aDoc)
  {
    ValueEnforcer.notNull (aDoc, "Doc");

    final File aPath = getOfflineCacheFile (aRepoInfo, sRelativeFilename);
    if (MicroWriter.writeToFile (aDoc, aPath).isFailure ())
      s_aLogger.error ("Failed to write to offline-cache: " + aPath.toString ());
  }
}
