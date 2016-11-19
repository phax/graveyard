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
package com.helger.lama.maven.metadata;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.ReturnsImmutableObject;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.CommonsLinkedHashSet;
import com.helger.commons.collection.ext.CommonsTreeSet;
import com.helger.commons.collection.ext.ICommonsCollection;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.collection.ext.ICommonsOrderedSet;
import com.helger.commons.collection.ext.ICommonsSortedSet;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.filter.IFilter;
import com.helger.commons.string.ToStringGenerator;
import com.helger.lama.maven.artifact.IMavenArtifact;
import com.helger.lama.maven.version.MavenVersion;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.serialize.MicroWriter;
import com.helger.xml.microdom.util.MicroHelper;

/**
 * Represents the maven-metadata.xml for a whole artifact
 *
 * @author Philip Helger
 */
public final class MavenMetaData
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MavenMetaData.class);

  private final IMavenArtifact m_aArtifact;
  private final IFilter <MavenVersion> m_aVersionFilter;
  private MavenVersion m_aLatestReleaseVersion;
  private MavenVersion m_aLatestVersion;
  private final ICommonsOrderedSet <MavenVersion> m_aAllVersions = new CommonsLinkedHashSet<> ();

  /**
   * Ctor.
   *
   * @param aArtifact
   *        The artifact ID. May not be <code>null</code>.
   * @param aVersionFilter
   *        An optional version filter. May be <code>null</code>.
   */
  private MavenMetaData (@Nonnull final IMavenArtifact aArtifact, @Nullable final IFilter <MavenVersion> aVersionFilter)
  {
    if (aArtifact == null)
      throw new NullPointerException ("artifact");
    m_aArtifact = aArtifact;
    m_aVersionFilter = aVersionFilter;
  }

  @Nonnull
  public IMavenArtifact getArtifact ()
  {
    return m_aArtifact;
  }

  private void _addVersion (@Nullable final MavenVersion aVersion)
  {
    if (aVersion != null)
    {
      // Does the version match the filter?
      if (m_aVersionFilter == null || m_aVersionFilter.test (aVersion))
      {
        // Is the current version suitable for a release version?
        if (aVersion.isReleaseVersion ())
        {
          if (m_aLatestReleaseVersion == null || aVersion.isGreaterThan (m_aLatestReleaseVersion))
            m_aLatestReleaseVersion = aVersion;
        }

        // Is it the latest version?
        if (m_aLatestVersion == null || aVersion.isGreaterThan (m_aLatestVersion))
          m_aLatestVersion = aVersion;
      }

      // Remember in all versions set anyway!
      m_aAllVersions.add (aVersion);
    }
  }

  @Nullable
  public MavenVersion getLatestReleaseVersion ()
  {
    return m_aLatestReleaseVersion;
  }

  @Nullable
  public MavenVersion getLatestVersion ()
  {
    return m_aLatestVersion;
  }

  @Nonnull
  @ReturnsImmutableObject
  public Collection <MavenVersion> getAllVersions ()
  {
    return getAllVersions (false);
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsCollection <MavenVersion> getAllVersions (final boolean bUseOnlyReleaseVersions)
  {
    if (!bUseOnlyReleaseVersions)
      return CollectionHelper.newList (m_aAllVersions);

    final ICommonsList <MavenVersion> ret = new CommonsArrayList<> ();
    for (final MavenVersion aVersion : m_aAllVersions)
      if (aVersion.isReleaseVersion ())
        ret.add (aVersion);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsSortedSet <MavenVersion> getAllVersionsGreaterThan (@Nullable final MavenVersion aReferenceVersion,
                                                                     final boolean bUseOnlyReleaseVersions)
  {
    final ICommonsSortedSet <MavenVersion> ret = new CommonsTreeSet<> ();
    for (final MavenVersion aVersion : m_aAllVersions)
      if (aReferenceVersion == null || aVersion.isGreaterThan (aReferenceVersion))
        if (!bUseOnlyReleaseVersions || aVersion.isReleaseVersion ())
          ret.add (aVersion);
    return ret;
  }

  @Override
  public String toString ()
  {
    final ICommonsList <String> aAllVersionStrings = new CommonsArrayList<> (m_aAllVersions,
                                                                             MavenVersion::getOriginalVersion);
    return new ToStringGenerator (null).append ("Artifact", m_aArtifact)
                                       .appendIfNotNull ("VersionFilter", m_aVersionFilter)
                                       .append ("LatestReleaseVersion",
                                                m_aLatestReleaseVersion == null ? null
                                                                                : m_aLatestReleaseVersion.getOriginalVersion ())
                                       .append ("LatestVersion",
                                                m_aLatestVersion == null ? null
                                                                         : m_aLatestVersion.getOriginalVersion ())
                                       .append ("AllVersions", aAllVersionStrings)
                                       .toString ();
  }

  /**
   * Read the Maven metadata from an XML file.
   *
   * @param aArtifact
   *        The artifact for which the meta data should be read. May not be
   *        <code>null</code>.
   * @param aDoc
   *        The Maven artifact metadata XML document
   * @param aExcludeVersions
   *        In case the versioning is inconsistent, this optional Set can
   *        contain versions to be ignored as the "latest" or as the "released"
   *        versions.
   * @return <code>null</code> in case of an error
   */
  @Nullable
  public static MavenMetaData readXML (@Nonnull final IMavenArtifact aArtifact,
                                       @Nonnull final IMicroDocument aDoc,
                                       @Nullable final Set <MavenVersion> aExcludeVersions)
  {
    final IMicroElement eRoot = aDoc.getDocumentElement ();
    if (!eRoot.getTagName ().equals ("metadata"))
    {
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Metadata XML has the wrong root element '" + eRoot.getTagName () + "'");
      return null;
    }

    final IFilter <MavenVersion> aFilter = CollectionHelper.isEmpty (aExcludeVersions) ? null
                                                                                       : new MavenVersionFilterExcludeVersions (aExcludeVersions);
    try
    {
      MavenMetaData ret = new MavenMetaData (aArtifact, aFilter);
      if (false)
      {
        final String sReadGroupID = MicroHelper.getChildTextContentTrimmed (eRoot, "groupId");
        if (!EqualsHelper.equals (aArtifact.getGroupID (), sReadGroupID))
          s_aLogger.warn ("GroupId mismatch: '" + aArtifact.getGroupID () + "' vs '" + sReadGroupID + "'");
        final String sReadArtifactID = MicroHelper.getChildTextContentTrimmed (eRoot, "artifactId");
        if (!EqualsHelper.equals (aArtifact.getArtifactID (), sReadArtifactID))
          s_aLogger.warn ("ArtifactId mismatch: '" + aArtifact.getArtifactID () + "' vs. '" + sReadArtifactID + "'");
      }

      final IMicroElement eVersioning = eRoot.getFirstChildElement ("versioning");
      if (eVersioning != null)
      {
        // Release element
        final IMicroElement eRelease = eVersioning.getFirstChildElement ("release");
        if (eRelease != null)
          ret._addVersion (MavenVersion.create (eRelease.getTextContentTrimmed ()));

        // Latest element
        final IMicroElement eLatest = eVersioning.getFirstChildElement ("latest");
        if (eLatest != null)
          ret._addVersion (MavenVersion.create (eLatest.getTextContentTrimmed ()));

        // Scan all versions
        final IMicroElement eVersions = eVersioning.getFirstChildElement ("versions");
        if (eVersions != null)
          for (final IMicroElement eVersion : eVersions.getAllChildElements ("version"))
            ret._addVersion (MavenVersion.create (eVersion.getTextContentTrimmed ()));
      }

      if (ret.getLatestVersion () == null)
      {
        s_aLogger.warn ("Unable to determine a version from the Metadata!");
        if (eVersioning != null)
          s_aLogger.warn (MicroWriter.getXMLString (aDoc));
        ret = null;
      }
      return ret;
    }
    catch (final RuntimeException ex)
    {
      s_aLogger.warn ("Error initializing Metadata for " +
                      aArtifact +
                      ": " +
                      ex.getClass ().getName () +
                      " - " +
                      ex.getMessage ());
    }
    return null;
  }
}
