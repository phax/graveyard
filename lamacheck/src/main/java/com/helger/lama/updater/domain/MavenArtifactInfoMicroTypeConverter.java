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

import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.collection.ext.CommonsHashSet;
import com.helger.commons.collection.ext.ICommonsSet;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.string.StringParser;
import com.helger.lama.maven.EMavenPackaging;
import com.helger.lama.maven.version.MavenVersion;
import com.helger.lama.updater.mgr.IMavenRepositoryInfoResolver;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.MicroElement;
import com.helger.xml.microdom.convert.IMicroTypeConverter;

public final class MavenArtifactInfoMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ATTR_GROUPID = "groupid";
  private static final String ATTR_ARTIFACTID = "artifactid";
  private static final String ATTR_PACKAGING = "packaging";
  private static final String ELEMENT_REPO = "repo";
  private static final String ATTR_ID = "id";
  private static final String ATTR_LATESTVERSION = "latestversion";
  private static final String ATTR_LATESTBETAVERSION = "latestbetaversion";
  private static final String ELEMENT_EXCLUDEDVERSION = "excludedversion";
  private static final String ATTR_LASTUPDATE_METADATA = "lastupdatemetadata";
  private static final String ATTR_LASTUPDATEERROR_METADATA = "lastupdateerrormetadata";
  private static final String ATTR_LASTUPDATEERROR_REPOSITORIES = "lastupdateerrorrepositories";
  private static final String ATTR_LASTUPDATESUCCESSDT = "lastupdatesuccessdt";
  private static final String ATTR_LASTUPDATEERRORDT = "lastupdateerrordt";
  private static final Logger s_aLogger = LoggerFactory.getLogger (MavenArtifactInfoMicroTypeConverter.class);

  private final IMavenRepositoryInfoResolver m_aRepoResolver;

  public MavenArtifactInfoMicroTypeConverter (@Nonnull final IMavenRepositoryInfoResolver aRepoResolver)
  {
    m_aRepoResolver = aRepoResolver;
  }

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull @Nonempty final String sTagName)
  {
    final MavenArtifactInfo aInfo = (MavenArtifactInfo) aObject;
    final IMicroElement eArtifact = new MicroElement (sNamespaceURI, sTagName);
    eArtifact.setAttribute (ATTR_GROUPID, aInfo.getGroupID ());
    eArtifact.setAttribute (ATTR_ARTIFACTID, aInfo.getArtifactID ());
    if (aInfo.getPackaging () != null)
      eArtifact.setAttribute (ATTR_PACKAGING, aInfo.getPackaging ().getID ());
    // Order is important
    for (final MavenArtifactRepositoryInfo aRepoData : aInfo.getAllArtifactRepositoryInfos ())
    {
      final IMicroElement eRepo = eArtifact.appendElement (ELEMENT_REPO);
      eRepo.setAttribute (ATTR_ID, aRepoData.getID ());
      if (aRepoData.getLatestReleaseVersion () != null)
        eRepo.setAttribute (ATTR_LATESTVERSION, aRepoData.getLatestReleaseVersion ().getOriginalVersion ());
      if (aRepoData.getLatestBetaVersion () != null)
        eRepo.setAttribute (ATTR_LATESTBETAVERSION, aRepoData.getLatestBetaVersion ().getOriginalVersion ());
      eRepo.setAttributeWithConversion (ATTR_LASTUPDATESUCCESSDT, aRepoData.getUpdateSuccessDT ());
      eRepo.setAttributeWithConversion (ATTR_LASTUPDATEERRORDT, aRepoData.getUpdateErrorDT ());
    }
    if (aInfo.getLatestReleaseVersion () != null)
      eArtifact.setAttribute (ATTR_LATESTVERSION, aInfo.getLatestReleaseVersion ().getOriginalVersion ());
    if (aInfo.getLatestBetaVersion () != null)
      eArtifact.setAttribute (ATTR_LATESTBETAVERSION, aInfo.getLatestBetaVersion ().getOriginalVersion ());
    for (final MavenVersion aExcludedVersion : CollectionHelper.getSorted (aInfo.getAllExcludedVersions ()))
      eArtifact.appendElement (ELEMENT_EXCLUDEDVERSION).setAttribute (ATTR_ID, aExcludedVersion.getOriginalVersion ());
    eArtifact.setAttributeWithConversion (ATTR_LASTUPDATE_METADATA, aInfo.getLastUpdateMetaData ());
    eArtifact.setAttributeWithConversion (ATTR_LASTUPDATEERROR_METADATA, aInfo.getLastUpdateErrorMetaData ());
    eArtifact.setAttributeWithConversion (ATTR_LASTUPDATEERROR_REPOSITORIES, aInfo.getLastUpdateErrorRepositories ());
    return eArtifact;
  }

  @Nullable
  private static LocalDateTime _readDT (final String s)
  {
    if (s == null)
      return null;
    // Old stuff?
    final long nMillis = StringParser.parseLong (s, -1L);
    if (nMillis > 0)
      return PDTFactory.createLocalDateTime (nMillis);

    // TODO read from s
    throw new IllegalStateException ("TODO");
  }

  @Nonnull
  public MavenArtifactInfo convertToNative (@Nonnull final IMicroElement eArtifact)
  {
    final String sGroupID = eArtifact.getAttributeValue (ATTR_GROUPID);
    final String sArtifactID = eArtifact.getAttributeValue (ATTR_ARTIFACTID);
    final String sPackaging = eArtifact.getAttributeValue (ATTR_PACKAGING);
    final EMavenPackaging ePackaging = EMavenPackaging.getFromIDOrNull (sPackaging);
    final String sLatestVersion = eArtifact.getAttributeValue (ATTR_LATESTVERSION);
    final String sLatestBetaVersion = eArtifact.getAttributeValue (ATTR_LATESTBETAVERSION);
    final ICommonsSet <MavenVersion> aExcludedVersions = new CommonsHashSet<> ();
    for (final IMicroElement eExcludedVersion : eArtifact.getAllChildElements (ELEMENT_EXCLUDEDVERSION))
      aExcludedVersions.add (new MavenVersion (eExcludedVersion.getAttributeValue (ATTR_ID)));
    final String sLastUpdateMetaData = eArtifact.getAttributeValue (ATTR_LASTUPDATE_METADATA);
    final LocalDateTime aLastUpdateMetaData = _readDT (sLastUpdateMetaData);
    final String sLastUpdateErrorMetaData = eArtifact.getAttributeValue (ATTR_LASTUPDATEERROR_METADATA);
    final LocalDateTime aLastUpdateErrorMetaData = _readDT (sLastUpdateErrorMetaData);
    final String sLastUpdateErrorRepositories = eArtifact.getAttributeValue (ATTR_LASTUPDATEERROR_REPOSITORIES);
    final LocalDateTime aLastUpdateErrorRepositories = _readDT (sLastUpdateErrorRepositories);
    final MavenArtifactInfo ret = new MavenArtifactInfo (sGroupID,
                                                         sArtifactID,
                                                         ePackaging,
                                                         sLatestVersion,
                                                         sLatestBetaVersion,
                                                         aExcludedVersions,
                                                         aLastUpdateMetaData,
                                                         aLastUpdateErrorMetaData,
                                                         aLastUpdateErrorRepositories);
    for (final IMicroElement eRepo : eArtifact.getAllChildElements (ELEMENT_REPO))
    {
      final String sRepoID = eRepo.getAttributeValue (ATTR_ID);
      final MavenRepositoryInfo aRepo = m_aRepoResolver.getRepositoryOfID (sRepoID);
      if (aRepo == null)
      {
        s_aLogger.warn ("Failed to resolve referenced repository '" + sRepoID + "'");
        continue;
      }

      final String sRepoLatestVersion = eRepo.getAttributeValue (ATTR_LATESTVERSION);
      final String sRepoLatestBetaVersion = eRepo.getAttributeValue (ATTR_LATESTBETAVERSION);
      final String sRepoLastUpdateSuccessDT = eRepo.getAttributeValue (ATTR_LASTUPDATESUCCESSDT);
      final LocalDateTime aRepoLastUpdateSuccessDT = _readDT (sRepoLastUpdateSuccessDT);
      final String sRepoLastUpdateErrorDT = eRepo.getAttributeValue (ATTR_LASTUPDATEERRORDT);
      final LocalDateTime aRepoLastUpdateErrorDT = _readDT (sRepoLastUpdateErrorDT);

      ret.addDesiredRepo (new MavenArtifactRepositoryInfo (aRepo,
                                                           sRepoLatestVersion,
                                                           sRepoLatestBetaVersion,
                                                           aRepoLastUpdateSuccessDT,
                                                           aRepoLastUpdateErrorDT));
    }
    return ret;
  }
}
