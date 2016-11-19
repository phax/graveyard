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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.ext.CommonsHashMap;
import com.helger.commons.collection.ext.ICommonsMap;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.lama.maven.artifact.IMavenArtifact;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.util.MicroHelper;

/**
 * Represents the maven-metadata.xml for a single version (mainly for SNAPSHOT
 * versions)
 *
 * @author Philip Helger
 */
public final class MavenMetaDataSnapshots
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MavenMetaDataSnapshots.class);

  private final IMavenArtifact m_aArtifact;
  private String m_sReadVersion;
  private String m_sLastUpdated;
  private final ICommonsMap <String, String> m_aAllSnapshots = new CommonsHashMap<> ();

  /**
   * Ctor.
   *
   * @param aArtifact
   *        The artifact ID. May not be <code>null</code>.
   */
  private MavenMetaDataSnapshots (@Nonnull final IMavenArtifact aArtifact)
  {
    if (aArtifact == null)
      throw new NullPointerException ("artifact");
    m_aArtifact = aArtifact;
  }

  @Nonnull
  public IMavenArtifact getArtifact ()
  {
    return m_aArtifact;
  }

  @Nullable
  public static String getID (@Nullable final String sClassifier, @Nullable final String sExtension)
  {
    return StringHelper.getConcatenatedOnDemand (sClassifier, ':', sExtension);
  }

  @Nullable
  public String getReadVersion ()
  {
    return m_sReadVersion;
  }

  void setReadVersion (@Nullable final String sReadVersion)
  {
    m_sReadVersion = sReadVersion;
  }

  @Nullable
  public String getLastUpdated ()
  {
    return m_sLastUpdated;
  }

  void setLastUpdated (@Nullable final String sLastUpdated)
  {
    m_sLastUpdated = sLastUpdated;
  }

  private void _addSnapshotVersion (@Nullable final String sClassifier,
                                    @Nonnull @Nonempty final String sExtension,
                                    @Nonnull @Nonempty final String sValue)
  {
    if (StringHelper.hasNoText (sExtension))
      throw new IllegalArgumentException ("Extension");
    if (StringHelper.hasNoText (sValue))
      throw new IllegalArgumentException ("Value");

    final String sID = getID (sClassifier, sExtension);
    if (m_aAllSnapshots.containsKey (sID))
      throw new IllegalArgumentException ("A snapshot version with ID '" + sID + "' is already contained!");
    m_aAllSnapshots.put (sID, sValue);
  }

  @Nonnegative
  public int getSnapshotVersionCount ()
  {
    return m_aAllSnapshots.size ();
  }

  @Nullable
  public String getSnapshotVersionFilename (@Nullable final String sClassifier,
                                            @Nonnull @Nonempty final String sExtension)
  {
    if (StringHelper.hasNoText (sExtension))
      throw new IllegalArgumentException ("Extension");

    final String sID = getID (sClassifier, sExtension);
    final String sValue = m_aAllSnapshots.get (sID);
    if (sValue == null)
    {
      // No explicit SNAPSHOT version present - use version read from file.
      // E.g. in
      // https://repository.apache.org/content/groups/snapshots/org/apache/directory/buildtools/directory-checkstyle/0.1-SNAPSHOT/
      return m_aArtifact.getArtifactID () + "-" + m_sReadVersion + '.' + sExtension;
    }

    String ret = m_aArtifact.getArtifactID () + "-" + sValue;
    if (StringHelper.hasText (sClassifier))
      ret += '-' + sClassifier;
    return ret + '.' + sExtension;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("artifact", m_aArtifact)
                                       .append ("snapshots", m_aAllSnapshots)
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
   * @return <code>null</code> in case of an error
   */
  @Nullable
  public static MavenMetaDataSnapshots readXML (@Nonnull final IMavenArtifact aArtifact,
                                                @Nonnull final IMicroDocument aDoc)
  {
    final IMicroElement eRoot = aDoc.getDocumentElement ();
    if (!eRoot.getTagName ().equals ("metadata"))
    {
      s_aLogger.warn ("SNAPSHOT Metadata XML has the wrong root element '" + eRoot.getTagName () + "'");
      return null;
    }

    try
    {
      final MavenMetaDataSnapshots ret = new MavenMetaDataSnapshots (aArtifact);
      final String sReadGroupID = MicroHelper.getChildTextContentTrimmed (eRoot, "groupId");
      if (!EqualsHelper.equals (aArtifact.getGroupID (), sReadGroupID))
        s_aLogger.warn ("GroupId mismatch: '" + aArtifact.getGroupID () + "' vs '" + sReadGroupID + "'");
      final String sReadArtifactID = MicroHelper.getChildTextContentTrimmed (eRoot, "artifactId");
      if (!EqualsHelper.equals (aArtifact.getArtifactID (), sReadArtifactID))
        s_aLogger.warn ("ArtifactId mismatch: '" + aArtifact.getArtifactID () + "' vs. '" + sReadArtifactID + "'");
      ret.setReadVersion (MicroHelper.getChildTextContentTrimmed (eRoot, "version"));

      final IMicroElement eVersioning = eRoot.getFirstChildElement ("versioning");
      if (eVersioning == null)
      {
        s_aLogger.warn ("SNAPSHOT Metadata XML is missing the versioning element");
        return null;
      }

      ret.setLastUpdated (MicroHelper.getChildTextContentTrimmed (eVersioning, "lastUpdated"));

      // snapshotVersions element - is optional
      final IMicroElement eSnapshotVersions = eVersioning.getFirstChildElement ("snapshotVersions");
      if (eSnapshotVersions != null)
      {
        for (final IMicroElement eSnapshotVersion : eSnapshotVersions.getAllChildElements ("snapshotVersion"))
        {
          final String sClassifier = MicroHelper.getChildTextContentTrimmed (eSnapshotVersion, "classifier");
          final String sExtension = MicroHelper.getChildTextContentTrimmed (eSnapshotVersion, "extension");
          final String sValue = MicroHelper.getChildTextContentTrimmed (eSnapshotVersion, "value");
          ret._addSnapshotVersion (sClassifier, sExtension, sValue);
        }
      }

      return ret;
    }
    catch (final RuntimeException ex)
    {
      s_aLogger.warn ("Error initializing SNAPSHOT Metadata for " +
                      aArtifact +
                      ": " +
                      ex.getClass ().getName () +
                      " - " +
                      ex.getMessage ());
    }
    return null;
  }
}
