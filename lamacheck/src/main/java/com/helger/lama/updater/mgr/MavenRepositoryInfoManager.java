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
import com.helger.commons.collection.ext.ICommonsCollection;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.collection.ext.ICommonsMap;
import com.helger.commons.state.EChange;
import com.helger.commons.string.StringHelper;
import com.helger.commons.type.ObjectType;
import com.helger.commons.url.URLHelper;
import com.helger.lama.maven.repo.EMavenRepositoryLayout;
import com.helger.lama.maven.repo.MavenRepository;
import com.helger.lama.updater.domain.MavenRepositoryInfo;
import com.helger.photon.basic.app.dao.impl.AbstractSimpleDAO;
import com.helger.photon.basic.app.dao.impl.DAOException;
import com.helger.photon.basic.audit.AuditHelper;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.MicroDocument;
import com.helger.xml.microdom.convert.MicroTypeConverter;

public final class MavenRepositoryInfoManager extends AbstractSimpleDAO implements IMavenRepositoryInfoResolver
{
  public static final ObjectType OT_REPOSITORY = new ObjectType ("mavenrepository");

  private static final String ELEMENT_REPOS = "repos";
  private static final String ELEMENT_REPO = "repo";
  private static final Logger s_aLogger = LoggerFactory.getLogger (MavenRepositoryInfoManager.class);

  private final ICommonsMap <String, MavenRepositoryInfo> m_aRepoByID = new CommonsHashMap<> ();
  private final ICommonsMap <String, MavenRepositoryInfo> m_aRepoByURL = new CommonsHashMap<> ();

  public MavenRepositoryInfoManager (@Nonnull @Nonempty final String sFilename) throws DAOException
  {
    super (sFilename);
    initialRead ();
  }

  @Nonnull
  private EChange _addRepo (@Nonnull final MavenRepositoryInfo aRepo)
  {
    if (aRepo == null)
      throw new NullPointerException ("Repo");

    final String sID = aRepo.getID ();
    if (m_aRepoByID.containsKey (sID))
    {
      s_aLogger.warn ("Repository with ID '" + sID + "' is already contained! URL=" + aRepo.getURL ());
      return EChange.UNCHANGED;
    }

    final String sURL = aRepo.getURL ();
    if (m_aRepoByURL.containsKey (sURL))
    {
      s_aLogger.warn ("Repository with URL '" + sURL + "' is already contained! ID=" + aRepo.getID ());
      return EChange.UNCHANGED;
    }

    m_aRepoByID.put (sID, aRepo);
    m_aRepoByURL.put (sURL, aRepo);
    return EChange.CHANGED;
  }

  @Override
  @Nonnull
  protected EChange onRead (@Nonnull final IMicroDocument aDoc)
  {
    final IMicroElement eRoot = aDoc.getDocumentElement ();
    for (final IMicroElement eRepo : eRoot.getAllChildElements (ELEMENT_REPO))
      _addRepo (MicroTypeConverter.convertToNative (eRepo, MavenRepositoryInfo.class));
    return EChange.UNCHANGED;
  }

  @Override
  @Nonnull
  protected IMicroDocument createWriteData ()
  {
    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement eRepos = aDoc.appendElement (ELEMENT_REPOS);
    for (final MavenRepositoryInfo aRepo : CollectionHelper.getSortedByKey (m_aRepoByID).values ())
      eRepos.appendChild (MicroTypeConverter.convertToMicroElement (aRepo, ELEMENT_REPO));
    return aDoc;
  }

  @Nonnegative
  public int getRepositoryCount ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aRepoByID.size ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public MavenRepositoryInfo getRepositoryOfID (@Nullable final String sID)
  {
    if (StringHelper.hasNoText (sID))
      return null;

    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aRepoByID.get (sID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public boolean containsRepositorywithID (@Nullable final String sID)
  {
    if (StringHelper.hasNoText (sID))
      return false;

    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aRepoByID.containsKey (sID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsCollection <MavenRepositoryInfo> getAllRepositories ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aRepoByID.copyOfValues ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsCollection <MavenRepositoryInfo> getAllValidRepositories ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      final ICommonsList <MavenRepositoryInfo> ret = new CommonsArrayList<> ();
      for (final MavenRepositoryInfo aRepoInfo : m_aRepoByID.values ())
        if (aRepoInfo.isValid () && !aRepoInfo.isLegacyLayout ())
          ret.add (aRepoInfo);
      return ret;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public MavenRepositoryInfo getRepositoryOfURL (@Nullable final String sURL)
  {
    if (StringHelper.hasNoText (sURL))
      return null;

    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aRepoByURL.get (sURL);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public boolean containsRepositoryWithURL (@Nullable final String sURL)
  {
    if (StringHelper.hasNoText (sURL))
      return false;

    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aRepoByURL.containsKey (sURL);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public String getRepositoryIDOfURL (@Nullable final String sURL)
  {
    final MavenRepositoryInfo aRepo = getRepositoryOfURL (sURL);
    return aRepo == null ? null : aRepo.getID ();
  }

  @Nullable
  public MavenRepositoryInfo addRepository (@Nonnull @Nonempty final String sRepoID,
                                            @Nonnull @Nonempty final String sURL)
  {
    final MavenRepository aRepo = new MavenRepository (sRepoID,
                                                       sURL,
                                                       EMavenRepositoryLayout.MAVEN2,
                                                       (String) null,
                                                       (String) null,
                                                       MavenRepository.DEFAULT_RELEASES,
                                                       MavenRepository.DEFAULT_SNAPSHOTS);
    return addRepository (aRepo);
  }

  @Nullable
  public MavenRepositoryInfo addRepository (@Nonnull final MavenRepository aRepo)
  {
    // Invalid if:
    // - it is legacy layout
    // - if the URL is not valid
    final boolean bIsInvalid = aRepo.isLegacyLayout () || URLHelper.getAsURL (aRepo.getURL ()) == null;
    final MavenRepositoryInfo aRepoInfo = new MavenRepositoryInfo (aRepo, bIsInvalid);
    return addRepository (aRepoInfo).isChanged () ? aRepoInfo : null;
  }

  @Nonnull
  public EChange addRepository (@Nonnull final MavenRepositoryInfo aRepoInfo)
  {
    if (aRepoInfo == null)
      throw new NullPointerException ("repo");

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (_addRepo (aRepoInfo).isUnchanged ())
        return EChange.UNCHANGED;
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    AuditHelper.onAuditCreateSuccess (OT_REPOSITORY,
                                      aRepoInfo.getID (),
                                      aRepoInfo.getURL (),
                                      aRepoInfo.getRepo ().getLayout ().getID (),
                                      aRepoInfo.getRepo ().getUserName (),
                                      Boolean.toString (aRepoInfo.getRepo ().isReleasesEnabled ()),
                                      Boolean.toString (aRepoInfo.getRepo ().isSnapshotsEnabled ()));
    return EChange.CHANGED;
  }

  @Nonnull
  public EChange setInvalid (@Nonnull final MavenRepositoryInfo aRepoInfo, final boolean bIsValid, final String sNote)
  {
    if (aRepoInfo == null)
      throw new NullPointerException ("repo");

    m_aRWLock.writeLock ().lock ();
    try
    {
      EChange eChange = EChange.UNCHANGED;
      eChange = eChange.or (aRepoInfo.setInvalid (bIsValid));
      eChange = eChange.or (aRepoInfo.setNote (sNote));
      if (eChange.isUnchanged ())
        return EChange.UNCHANGED;
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    AuditHelper.onAuditModifySuccess (OT_REPOSITORY,
                                      aRepoInfo.getID (),
                                      "set-invalid",
                                      Boolean.toString (bIsValid),
                                      sNote);
    return EChange.CHANGED;
  }
}
