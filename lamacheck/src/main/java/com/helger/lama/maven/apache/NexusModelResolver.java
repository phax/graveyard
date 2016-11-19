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
package com.helger.lama.maven.apache;

import java.io.File;

import javax.annotation.Nonnull;

import org.apache.maven.model.Parent;
import org.apache.maven.model.Repository;
import org.apache.maven.model.building.FileModelSource;
import org.apache.maven.model.building.ModelSource2;
import org.apache.maven.model.resolution.InvalidRepositoryException;
import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.model.resolution.UnresolvableModelException;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.providers.http.HttpWagon;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.connector.wagon.WagonProvider;
import org.eclipse.aether.internal.impl.DefaultRepositorySystem;
import org.eclipse.aether.internal.impl.SimpleLocalRepositoryManagerFactory;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.NoLocalRepositoryManagerException;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.eclipse.aether.util.repository.SimpleResolutionErrorPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.CommonsHashMap;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.collection.ext.ICommonsMap;
import com.helger.commons.concurrent.SimpleReadWriteLock;

public final class NexusModelResolver implements ModelResolver
{
  public static final class MyWagonProvider implements WagonProvider
  {
    public Wagon lookup (final String roleHint) throws Exception
    {
      if ("http".equals (roleHint))
        return new HttpWagon ();
      throw new IllegalStateException ("No Wagon Provider for '" + roleHint + "'");
    }

    public void release (final Wagon wagon)
    {
      try
      {
        if (wagon instanceof HttpWagon)
          ((HttpWagon) wagon).closeConnection ();
      }
      catch (final Exception ex)
      {
        throw new IllegalStateException ("Failed to release " + wagon, ex);
      }
    }
  }

  private static final Logger s_aLogger = LoggerFactory.getLogger (NexusModelResolver.class);
  private final ICommonsList <RemoteRepository> m_aRepos = new CommonsArrayList<> ();
  private final DefaultRepositorySystem m_aRepoSystem = new DefaultRepositorySystem ();
  private final DefaultRepositorySystemSession m_aRepoSession = MavenRepositorySystemUtils.newSession ();
  private final SimpleReadWriteLock m_aRWLock = new SimpleReadWriteLock ();
  private final ICommonsMap <String, File> m_aCache = new CommonsHashMap<> ();

  public NexusModelResolver ()
  {
    final RemoteRepository aRepo = new RemoteRepository.Builder ("repo.ph.nexus",
                                                                 "default",
                                                                 "http://localhost:81/nexus/content/groups/public").setAuthentication (new AuthenticationBuilder ().addUsername ("philip")
                                                                                                                                                                   .addPassword ("phloc2")
                                                                                                                                                                   .build ())
                                                                                                                   .build ();
    m_aRepos.add (aRepo);

    m_aRepoSystem.initService (MavenRepositorySystemUtils.newServiceLocator ());

    try
    {
      m_aRepoSession.setLocalRepositoryManager (new SimpleLocalRepositoryManagerFactory ().newInstance (m_aRepoSession,
                                                                                                        new LocalRepository (LocalSettingsModelResolver.getLocalRepository ())));
    }
    catch (final NoLocalRepositoryManagerException ex)
    {
      throw new IllegalStateException (ex);
    }
    m_aRepoSession.setResolutionErrorPolicy (new SimpleResolutionErrorPolicy (false, true));
  }

  @Nonnull
  public ModelSource2 resolveModel (final String sGroupID,
                                    final String sArtifactID,
                                    final String sVersion) throws UnresolvableModelException
  {
    final String sID = sGroupID + ":" + sArtifactID + "::" + sVersion;
    m_aRWLock.readLock ().lock ();
    try
    {
      final File aFile = m_aCache.get (sID);
      if (aFile != null)
        return new FileModelSource (aFile);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }

    s_aLogger.info ("Resolving " + sID);
    try
    {
      Artifact aPomArtifact = new DefaultArtifact (sGroupID, sArtifactID, "pom", sVersion);
      final ArtifactRequest aArtifactRequest = new ArtifactRequest (aPomArtifact, m_aRepos, null);
      aPomArtifact = m_aRepoSystem.resolveArtifact (m_aRepoSession, aArtifactRequest).getArtifact ();
      final File aFile = aPomArtifact.getFile ();
      m_aRWLock.writeLock ().lock ();
      try
      {
        m_aCache.put (sID, aFile);
      }
      finally
      {
        m_aRWLock.writeLock ().unlock ();
      }
      return new FileModelSource (aFile);
    }
    catch (final ArtifactResolutionException e)
    {
      throw new UnresolvableModelException ("Failed to resolve POM for " +
                                            sGroupID +
                                            ":" +
                                            sArtifactID +
                                            ":" +
                                            sVersion +
                                            " due to " +
                                            e.getMessage (),
                                            sGroupID,
                                            sArtifactID,
                                            sVersion,
                                            e);
    }
  }

  public ModelSource2 resolveModel (final Parent parent) throws UnresolvableModelException
  {
    // TODO
    return null;
  }

  public void addRepository (final Repository aRepository) throws InvalidRepositoryException
  {
    addRepository (aRepository, false);
  }

  public void addRepository (final Repository aRepository, final boolean bReplace) throws InvalidRepositoryException
  {}

  public ModelResolver newCopy ()
  {
    return new NexusModelResolver ();
  }
}
