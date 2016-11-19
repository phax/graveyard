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
package com.helger.lama.updater;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.PresentForCodeCoverage;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.lama.maven.metadata.MavenMetaDataSnapshots;
import com.helger.lama.maven.pom.MavenPOM;
import com.helger.lama.maven.pom.MavenPOMFile;
import com.helger.lama.maven.version.MavenVersion;
import com.helger.lama.updater.domain.MavenArtifactInfo;
import com.helger.lama.updater.domain.MavenRepositoryInfo;
import com.helger.lama.updater.utils.HTTPReader;
import com.helger.lama.updater.utils.OfflineCache;
import com.helger.lama.updater.utils.UpdateBlacklist;
import com.helger.xml.microdom.IMicroDocument;

@Immutable
final class UpdateReaderPOM
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (UpdateReaderPOM.class);

  @PresentForCodeCoverage
  private static final UpdateReaderPOM s_aInstance = new UpdateReaderPOM ();

  private UpdateReaderPOM ()
  {}

  @Nullable
  public static MavenMetaDataSnapshots readMavenMetadataSnapshots (@Nonnull final MavenRepositoryInfo aRepoInfo,
                                                                   @Nonnull final MavenArtifactInfo aArtifactInfo,
                                                                   @Nonnull final MavenVersion aVersion,
                                                                   @Nonnull final UpdateBlacklist aBlacklist)
  {
    final String sMetadataPath = aArtifactInfo.getVersionMetadataPath (aVersion);
    final IMicroDocument aMetaDataDoc = HTTPReader.readXML (aRepoInfo, sMetadataPath, aBlacklist);
    if (aMetaDataDoc == null)
    {
      // Warning already emitted
      return null;
    }

    // Interpret the maven-metadata.xml file
    return MavenMetaDataSnapshots.readXML (aArtifactInfo.getAsArtifact (), aMetaDataDoc);
    // In case of an error, the warning is already emitted
  }

  @Nullable
  public static MavenPOM readPOM (@Nonnull final MavenRepositoryInfo aRepoInfo,
                                  @Nonnull final MavenArtifactInfo aArtifactInfo,
                                  @Nonnull final MavenVersion aVersion,
                                  @Nonnull final UpdateBlacklist aBlacklist)
  {
    // Build path to pom.xml
    String sPOMFilename;
    if (aVersion.isSnapshotVersion ())
    {
      // Read snapshot metadata
      final MavenMetaDataSnapshots aSnapshots = readMavenMetadataSnapshots (aRepoInfo, aArtifactInfo, aVersion, aBlacklist);
      if (aSnapshots == null)
      {
        // Warning was already emitted
        return null;
      }
      final String sSnapshotPOMFilename = aSnapshots.getSnapshotVersionFilename (null, "pom");
      if (sSnapshotPOMFilename == null)
      {
        s_aLogger.warn ("Failed to determined SNAPSHOT POM version of " +
                        aArtifactInfo.getID () +
                        " for version " +
                        aVersion.getOriginalVersion () +
                        " in " +
                        aRepoInfo.getURL ());
        return null;
      }
      sPOMFilename = aArtifactInfo.getVersionPath (aVersion) + sSnapshotPOMFilename;
    }
    else
    {
      sPOMFilename = aArtifactInfo.getPOMPathAndFilename (aVersion);
    }

    final IMicroDocument aPOMDoc = HTTPReader.readXML (aRepoInfo, sPOMFilename, aBlacklist);
    if (aPOMDoc == null)
    {
      // Warning already emitted
      return null;
    }

    try
    {
      // Read POM from offline cache so that some file system properties can be
      // resolved correctly
      final FileSystemResource aRes = new FileSystemResource (OfflineCache.getOfflineCacheFile (aRepoInfo, sPOMFilename));
      return new MavenPOMFile (aPOMDoc, aRes, false).getPOM ().resolveProperties ();
    }
    catch (final Exception ex)
    {
      s_aLogger.error ("Failed to read POM of " +
                       aArtifactInfo.getID () +
                       " in " +
                       aRepoInfo.getURL () +
                       ": " +
                       ex.getClass ().getName () +
                       " - " +
                       ex.getMessage ());
    }
    return null;
  }
}
