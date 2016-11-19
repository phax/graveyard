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
package com.helger.lama.updater.mgr;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.CommonsHashMap;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.collection.ext.ICommonsMap;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.id.IHasID;
import com.helger.commons.state.EChange;
import com.helger.commons.string.StringHelper;
import com.helger.commons.type.ObjectType;
import com.helger.lama.maven.EMavenPackaging;
import com.helger.lama.maven.artifact.IMavenArtifact;
import com.helger.lama.maven.version.MavenVersion;
import com.helger.lama.updater.domain.MavenArtifactInfo;
import com.helger.lama.updater.domain.MavenRepositoryInfo;
import com.helger.photon.basic.app.dao.impl.AbstractSimpleDAO;
import com.helger.photon.basic.app.dao.impl.DAOException;
import com.helger.photon.basic.audit.AuditHelper;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.MicroDocument;
import com.helger.xml.microdom.convert.MicroTypeConverter;

public final class MavenArtifactInfoManager extends AbstractSimpleDAO
{
  public static final ObjectType OT_ARTIFACT = new ObjectType ("mavenartifact");

  private static final String ELEMENT_ARTIFACTS = "artifacts";
  private static final String ELEMENT_ARTIFACT = "artifact";
  private static final Logger s_aLogger = LoggerFactory.getLogger (MavenArtifactInfoManager.class);

  private final ICommonsMap <String, MavenArtifactInfo> m_aItems = new CommonsHashMap<> ();

  public MavenArtifactInfoManager (@Nonnull @Nonempty final String sFilename) throws DAOException
  {
    super (sFilename);
    initialRead ();
  }

  @Nonnull
  private EChange _addArtifact (@Nonnull final MavenArtifactInfo aArtifact)
  {
    if (aArtifact == null)
      throw new NullPointerException ("Artifact");
    if (aArtifact.getAsArtifact ().containsUnresolvedVariable ())
      throw new IllegalArgumentException ("Oops we have a problem here!");

    final String sID = aArtifact.getID ();
    if (m_aItems.containsKey (sID))
    {
      s_aLogger.warn ("Artifact with ID '" + sID + "' is already contained!");
      return EChange.UNCHANGED;
    }

    m_aItems.put (sID, aArtifact);
    return EChange.CHANGED;
  }

  @Override
  @Nonnull
  protected EChange onRead (@Nonnull final IMicroDocument aDoc)
  {
    final IMicroElement eRoot = aDoc.getDocumentElement ();
    for (final IMicroElement eArtifact : eRoot.getAllChildElements (ELEMENT_ARTIFACT))
      _addArtifact (MicroTypeConverter.convertToNative (eArtifact, MavenArtifactInfo.class));

    return EChange.UNCHANGED;
  }

  @Override
  @Nonnull
  protected IMicroDocument createWriteData ()
  {
    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement eRoot = aDoc.appendElement (ELEMENT_ARTIFACTS);
    for (final MavenArtifactInfo aInfo : CollectionHelper.getSortedByKey (m_aItems).values ())
      eRoot.appendChild (MicroTypeConverter.convertToMicroElement (aInfo, ELEMENT_ARTIFACT));
    return aDoc;
  }

  @Nonnegative
  public int getArtifactCount ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aItems.size ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public MavenArtifactInfo getArtifactOfID (@Nullable final String sID)
  {
    if (StringHelper.hasNoText (sID))
      return null;

    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aItems.get (sID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public boolean containsArtifactWithID (@Nullable final IMavenArtifact aArtifact)
  {
    if (aArtifact == null)
      return false;
    return containsArtifactWithID (MavenArtifactInfo.createArtifactID (aArtifact));
  }

  public boolean containsArtifactWithID (@Nullable final String sID)
  {
    if (StringHelper.hasNoText (sID))
      return false;

    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aItems.containsKey (sID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <MavenArtifactInfo> getAllArtifacts ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return CollectionHelper.newList (m_aItems.values ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <MavenArtifactInfo> getAllArtifactsSorted ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return CollectionHelper.getSorted (m_aItems.values (), IHasID.getComparatorID ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnegative
  public int getArtifactCountWithDesiredRepoID (@Nullable final String sRepoID)
  {
    int ret = 0;
    if (StringHelper.hasText (sRepoID))
    {
      m_aRWLock.readLock ().lock ();
      try
      {
        for (final MavenArtifactInfo aArtifactInfo : m_aItems.values ())
          if (aArtifactInfo.hasDesiredRepo (sRepoID))
            ret++;
      }
      finally
      {
        m_aRWLock.readLock ().unlock ();
      }
    }
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <MavenArtifactInfo> getAllArtifactsWithDesiredRepoID (@Nullable final String sRepoID)
  {
    final ICommonsList <MavenArtifactInfo> ret = new CommonsArrayList<> ();
    if (StringHelper.hasText (sRepoID))
    {
      m_aRWLock.readLock ().lock ();
      try
      {
        for (final MavenArtifactInfo aArtifactInfo : m_aItems.values ())
          if (aArtifactInfo.hasDesiredRepo (sRepoID))
            ret.add (aArtifactInfo);
      }
      finally
      {
        m_aRWLock.readLock ().unlock ();
      }
    }
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <MavenArtifactInfo> getAllArtifactsWithDesiredRepoIDSorted (@Nullable final String sRepoID)
  {
    return CollectionHelper.getSortedInline (getAllArtifactsWithDesiredRepoID (sRepoID), IHasID.getComparatorID ());
  }

  @Nonnull
  public EChange addArtifact (@Nonnull @Nonempty final String sGroupID, @Nonnull @Nonempty final String sArtifactID)
  {
    return addArtifact (sGroupID, sArtifactID, (List <MavenRepositoryInfo>) null);
  }

  @Nonnull
  public EChange addArtifact (@Nonnull @Nonempty final String sGroupID,
                              @Nonnull @Nonempty final String sArtifactID,
                              @Nullable final List <MavenRepositoryInfo> aDesiredRepos)
  {
    return addArtifact (sGroupID,
                        sArtifactID,
                        aDesiredRepos,
                        (String) null,
                        (String) null,
                        (Collection <MavenVersion>) null);
  }

  @Nonnull
  public EChange addArtifact (@Nonnull @Nonempty final String sGroupID,
                              @Nonnull @Nonempty final String sArtifactID,
                              @Nullable final MavenRepositoryInfo aDesiredRepo,
                              @Nullable final String sLatestVersion)
  {
    return addArtifact (sGroupID,
                        sArtifactID,
                        aDesiredRepo == null ? null : CollectionHelper.newList (aDesiredRepo),
                        sLatestVersion,
                        (String) null,
                        (Collection <MavenVersion>) null);
  }

  @Nonnull
  public EChange addArtifact (@Nonnull @Nonempty final String sGroupID,
                              @Nonnull @Nonempty final String sArtifactID,
                              @Nullable final String sLatestVersion)
  {
    return addArtifact (sGroupID,
                        sArtifactID,
                        (List <MavenRepositoryInfo>) null,
                        sLatestVersion,
                        (String) null,
                        (Collection <MavenVersion>) null);
  }

  @Nonnull
  public EChange addArtifact (@Nonnull @Nonempty final String sGroupID,
                              @Nonnull @Nonempty final String sArtifactID,
                              @Nullable final String sLatestVersion,
                              @Nullable final Collection <MavenVersion> aExcludedVersions)
  {
    return addArtifact (sGroupID,
                        sArtifactID,
                        (List <MavenRepositoryInfo>) null,
                        sLatestVersion,
                        (String) null,
                        aExcludedVersions);
  }

  @Nonnull
  public EChange addArtifact (@Nonnull @Nonempty final String sGroupID,
                              @Nonnull @Nonempty final String sArtifactID,
                              @Nullable final List <MavenRepositoryInfo> aDesiredRepos,
                              @Nullable final String sLatestVersion,
                              @Nullable final String sLatestBetaVersion,
                              @Nullable final Collection <MavenVersion> aExcludedVersions)
  {
    final MavenArtifactInfo aArtifact = new MavenArtifactInfo (sGroupID,
                                                               sArtifactID,
                                                               (EMavenPackaging) null,
                                                               sLatestVersion,
                                                               sLatestBetaVersion,
                                                               aExcludedVersions,
                                                               (LocalDateTime) null,
                                                               (LocalDateTime) null,
                                                               (LocalDateTime) null);
    if (aDesiredRepos != null)
      for (final MavenRepositoryInfo aDesiredRepo : aDesiredRepos)
        aArtifact.addDesiredRepo (aDesiredRepo);

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (_addArtifact (aArtifact).isUnchanged ())
        return EChange.UNCHANGED;
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    AuditHelper.onAuditCreateSuccess (OT_ARTIFACT, aArtifact.getID ());
    return EChange.CHANGED;
  }

  @Nonnull
  public EChange setPackaging (@Nonnull final MavenArtifactInfo aArtifactInfo,
                               @Nullable final EMavenPackaging ePackaging)
  {
    if (aArtifactInfo == null)
      throw new NullPointerException ("ArtifactInfo");
    if (!containsArtifactWithID (aArtifactInfo.getID ()))
      throw new IllegalArgumentException ("No such artifact: " + aArtifactInfo);

    // Avoid setting null instead of an existing packaging
    if (ePackaging != null || aArtifactInfo.getPackaging () == null)
    {
      m_aRWLock.writeLock ().lock ();
      try
      {
        if (aArtifactInfo.setPackaging (ePackaging).isChanged ())
        {
          markAsChanged ();
          return EChange.CHANGED;
        }
      }
      finally
      {
        m_aRWLock.writeLock ().unlock ();
      }
    }
    return EChange.UNCHANGED;
  }

  public void setLastUpdateMetaData (@Nonnull final MavenArtifactInfo aArtifactInfo,
                                     @Nonnull final LocalDateTime aDateTime)
  {
    if (aArtifactInfo == null)
      throw new NullPointerException ("ArtifactInfo");
    if (aDateTime == null)
      throw new NullPointerException ("LocalDateTime");
    if (!containsArtifactWithID (aArtifactInfo.getID ()))
      throw new IllegalArgumentException ("No such artifact: " + aArtifactInfo);

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (aArtifactInfo.setLastUpdateMetaData (aDateTime).isChanged ())
        markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  public void setLastUpdateErrorMetaData (@Nonnull final MavenArtifactInfo aArtifactInfo,
                                          @Nonnull final LocalDateTime aDateTime)
  {
    if (aArtifactInfo == null)
      throw new NullPointerException ("ArtifactInfo");
    if (aDateTime == null)
      throw new NullPointerException ("LocalDateTime");
    if (!containsArtifactWithID (aArtifactInfo.getID ()))
      throw new IllegalArgumentException ("No such artifact: " + aArtifactInfo);

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (aArtifactInfo.setLastUpdateErrorMetaData (aDateTime).isChanged ())
        markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  public void setLastUpdateErrorRepositories (@Nonnull final MavenArtifactInfo aArtifactInfo,
                                              @Nonnull final LocalDateTime aDateTime)
  {
    if (aArtifactInfo == null)
      throw new NullPointerException ("ArtifactInfo");
    if (aDateTime == null)
      throw new NullPointerException ("LocalDateTime");
    if (!containsArtifactWithID (aArtifactInfo.getID ()))
      throw new IllegalArgumentException ("No such artifact: " + aArtifactInfo);

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (aArtifactInfo.setLastUpdateErrorRepositories (aDateTime).isChanged ())
        markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  public void setLatestReleaseVersion (@Nonnull final MavenArtifactInfo aArtifactInfo,
                                       @Nonnull final MavenVersion aLatestReleaseVersion)
  {
    if (aArtifactInfo == null)
      throw new NullPointerException ("ArtifactInfo");
    if (aLatestReleaseVersion == null)
      throw new NullPointerException ("LatestVersion");
    if (!containsArtifactWithID (aArtifactInfo.getID ()))
      throw new IllegalArgumentException ("No such artifact: " + aArtifactInfo);

    final MavenVersion aOldLatestVersion = aArtifactInfo.getLatestReleaseVersion ();
    boolean bUpdated = false;
    m_aRWLock.writeLock ().lock ();
    try
    {
      if (aArtifactInfo.setLatestReleaseVersion (aLatestReleaseVersion).isChanged ())
      {
        markAsChanged ();
        bUpdated = true;
      }
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }

    if (bUpdated)
      AuditHelper.onAuditModifySuccess (OT_ARTIFACT,
                                        aArtifactInfo.getID (),
                                        "latestversion",
                                        aOldLatestVersion == null ? "null" : aOldLatestVersion.getOriginalVersion (),
                                        aLatestReleaseVersion.getAsString ());
  }

  public void setLatestBetaVersion (@Nonnull final MavenArtifactInfo aArtifactInfo,
                                    @Nonnull final MavenVersion aLatestBetaVersion)
  {
    if (aArtifactInfo == null)
      throw new NullPointerException ("ArtifactInfo");
    if (aLatestBetaVersion == null)
      throw new NullPointerException ("LatestBetaVersion");
    if (!containsArtifactWithID (aArtifactInfo.getID ()))
      throw new IllegalArgumentException ("No such artifact: " + aArtifactInfo);

    final MavenVersion aOldLatestBetaVersion = aArtifactInfo.getLatestBetaVersion ();
    boolean bUpdated = false;
    m_aRWLock.writeLock ().lock ();
    try
    {
      if (aArtifactInfo.setLatestBetaVersion (aLatestBetaVersion).isChanged ())
      {
        markAsChanged ();
        bUpdated = true;
      }
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }

    if (bUpdated)
      AuditHelper.onAuditModifySuccess (OT_ARTIFACT,
                                        aArtifactInfo.getID (),
                                        "latestbetaversion",
                                        aOldLatestBetaVersion == null ? "null"
                                                                      : aOldLatestBetaVersion.getOriginalVersion (),
                                        aLatestBetaVersion.getAsString ());
  }

  @Nonnull
  public EChange addDesiredRepository (@Nonnull final MavenArtifactInfo aArtifactInfo,
                                       @Nonnull final MavenRepositoryInfo aDesiredRepo)
  {
    if (aArtifactInfo == null)
      throw new NullPointerException ("ArtifactInfo");
    if (aDesiredRepo == null)
      throw new NullPointerException ("DesiredRepository");
    if (!containsArtifactWithID (aArtifactInfo.getID ()))
      throw new IllegalArgumentException ("No such artifact: " + aArtifactInfo);

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (aArtifactInfo.addDesiredRepo (aDesiredRepo).isUnchanged ())
        return EChange.UNCHANGED;
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }

    AuditHelper.onAuditModifySuccess (OT_ARTIFACT, aArtifactInfo.getID (), "add-desiredrepo", aDesiredRepo.getID ());
    return EChange.CHANGED;
  }

  @Nonnull
  public EChange setLatestReleaseVersionOfRepository (@Nonnull final MavenArtifactInfo aArtifactInfo,
                                                      @Nonnull final MavenRepositoryInfo aDesiredRepo,
                                                      @Nullable final MavenVersion aRelevantReleaseVersionOfThisRepo)
  {
    if (aArtifactInfo == null)
      throw new NullPointerException ("ArtifactInfo");
    if (aDesiredRepo == null)
      throw new NullPointerException ("DesiredRepository");
    if (!containsArtifactWithID (aArtifactInfo.getID ()))
      throw new IllegalArgumentException ("No such artifact: " + aArtifactInfo);

    EChange eVersionChange;
    m_aRWLock.writeLock ().lock ();
    try
    {
      eVersionChange = aArtifactInfo.setLatestReleaseVersionOfRepository (aDesiredRepo,
                                                                          aRelevantReleaseVersionOfThisRepo,
                                                                          PDTFactory.getCurrentLocalDateTime ());
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }

    if (eVersionChange.isUnchanged ())
      return EChange.UNCHANGED;

    AuditHelper.onAuditModifySuccess (OT_ARTIFACT,
                                      aArtifactInfo.getID (),
                                      "update-latest-release-version",
                                      aDesiredRepo.getID (),
                                      aRelevantReleaseVersionOfThisRepo == null ? null
                                                                                : aRelevantReleaseVersionOfThisRepo.getOriginalVersion ());
    return EChange.CHANGED;
  }

  @Nonnull
  public EChange setLatestBetaVersionOfRepository (@Nonnull final MavenArtifactInfo aArtifactInfo,
                                                   @Nonnull final MavenRepositoryInfo aDesiredRepo,
                                                   @Nullable final MavenVersion aRelevantBetaVersionOfThisRepo)
  {
    if (aArtifactInfo == null)
      throw new NullPointerException ("ArtifactInfo");
    if (aDesiredRepo == null)
      throw new NullPointerException ("DesiredRepository");
    if (!containsArtifactWithID (aArtifactInfo.getID ()))
      throw new IllegalArgumentException ("No such artifact: " + aArtifactInfo);

    EChange eVersionChange;
    m_aRWLock.writeLock ().lock ();
    try
    {
      eVersionChange = aArtifactInfo.setLatestBetaVersionOfRepository (aDesiredRepo, aRelevantBetaVersionOfThisRepo);
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }

    if (eVersionChange.isUnchanged ())
      return EChange.UNCHANGED;

    AuditHelper.onAuditModifySuccess (OT_ARTIFACT,
                                      aArtifactInfo.getID (),
                                      "update-latest-beta-version",
                                      aDesiredRepo.getID (),
                                      aRelevantBetaVersionOfThisRepo == null ? null
                                                                             : aRelevantBetaVersionOfThisRepo.getOriginalVersion ());
    return EChange.CHANGED;
  }
}
