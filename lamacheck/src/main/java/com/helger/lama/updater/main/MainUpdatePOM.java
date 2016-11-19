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
package com.helger.lama.updater.main;

import java.io.File;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.io.file.SimpleFileIO;
import com.helger.commons.io.file.filter.IFileFilter;
import com.helger.commons.io.file.iterate.FileSystemRecursiveIterator;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.lama.maven.artifact.IMavenArtifact;
import com.helger.lama.maven.pom.MavenPOM;
import com.helger.lama.maven.pom.MavenPOMFile;
import com.helger.lama.maven.pom.POMDependency;
import com.helger.lama.maven.pom.POMExtension;
import com.helger.lama.maven.pom.POMPlugin;
import com.helger.lama.maven.repo.MavenRepository;
import com.helger.lama.maven.version.MavenVersion;
import com.helger.lama.updater.domain.MavenArtifactInfo;
import com.helger.lama.updater.mgr.LamaMetaManager;
import com.helger.xml.microdom.serialize.MicroWriter;
import com.helger.xml.serialize.write.XMLWriterSettings;

public final class MainUpdatePOM
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MainUpdatePOM.class);

  private static void _checkForUpdate (@Nonnull final String sType,
                                       @Nonnull final IMavenArtifact aArtifact,
                                       final boolean bCheckOnlyCentral)
  {
    final String sCurrentVersion = aArtifact.getVersion ();
    if (sCurrentVersion != null)
    {
      final String sInternalArtifactID = MavenArtifactInfo.createArtifactID (aArtifact);
      final MavenArtifactInfo aArtifactInfo = LamaMetaManager.getArtifactInfoMgr ()
                                                             .getArtifactOfID (sInternalArtifactID);
      if (aArtifactInfo != null)
      {
        final MavenVersion aCurrentVersion = new MavenVersion (sCurrentVersion);
        final MavenVersion aLatestVersion = bCheckOnlyCentral ? aArtifactInfo.getLatestReleaseVersionOfRepository (MavenRepository.ID_CENTRAL2)
                                                              : aArtifactInfo.getLatestReleaseVersion ();
        if (aLatestVersion != null && aLatestVersion.isGreaterThan (aCurrentVersion))
          s_aLogger.info ("  " +
                          sType +
                          " " +
                          sInternalArtifactID +
                          " can be updated from " +
                          sCurrentVersion +
                          " to " +
                          aLatestVersion.getOriginalVersion ());
      }
      else
      {
        final String sGroupID = aArtifact.getGroupID ();
        final String sArtifactID = aArtifact.getArtifactID ();
        if (!"org.eclipse.m2e".equals (sGroupID) || !"lifecycle-mapping".equals (sArtifactID))
          LamaMetaManager.getArtifactInfoMgr ().addArtifact (sGroupID, sArtifactID);
      }
    }
  }

  public static void findUpdatesOfPOM (@Nonnull final MavenPOM aPOM, final boolean bCheckOnlyCentral)
  {
    if (aPOM.getParentArtifact () != null)
      _checkForUpdate ("ParentPOM", aPOM.getParentArtifact (), bCheckOnlyCentral);
    for (final POMDependency aDep : aPOM.getDependencyManagement ().getAll ())
      _checkForUpdate ("DependencyManagement", aDep.getArtifact (), bCheckOnlyCentral);
    for (final POMDependency aDep : aPOM.getDependencies ().getAll ())
      _checkForUpdate ("Dependency", aDep.getArtifact (), bCheckOnlyCentral);
    for (final POMExtension aExtension : aPOM.getBuildExtensions ().getAll ())
      _checkForUpdate ("Extension", aExtension.getArtifact (), bCheckOnlyCentral);
    for (final POMPlugin aPlugin : aPOM.getBuildPluginManagement ().getAll ())
    {
      _checkForUpdate ("PluginManagement", aPlugin.getArtifact (), bCheckOnlyCentral);
      for (final POMDependency aDependency : aPlugin.getDependencies ().getAll ())
        _checkForUpdate ("PluginManagement-Dependency", aDependency.getArtifact (), bCheckOnlyCentral);
    }
    for (final POMPlugin aPlugin : aPOM.getBuildPlugins ().getAll ())
    {
      _checkForUpdate ("Plugin", aPlugin.getArtifact (), bCheckOnlyCentral);
      for (final POMDependency aDependency : aPlugin.getDependencies ().getAll ())
        _checkForUpdate ("Plugin-Dependency", aDependency.getArtifact (), bCheckOnlyCentral);
    }
  }

  public static void main (final String [] args) throws Exception
  {
    final IFileFilter aIgnore = IFileFilter.filenameMatchNone ("src",
                                                               "tags",
                                                               "branches",
                                                               "target",
                                                               "Archive",
                                                               "checkout");
    LamaRunner.run ( () -> {
      LamaMetaManager.getArtifactInfoMgr ().beginWithoutAutoSave ();
      try
      {
        File aBaseDir = new File ("p:\\git");
        if (false)
          aBaseDir = new File ("P:\\svn-joinup-cipa\\trunk\\");

        for (final File aFile : new FileSystemRecursiveIterator (aBaseDir, aIgnore))
          if (aFile.getName ().equals ("pom.xml"))
          {
            s_aLogger.info (aFile.getAbsolutePath ());
            final IReadableResource aRes = new FileSystemResource (aFile);
            final MavenPOM aPOM = new MavenPOMFile (aRes).getPOM ().resolveProperties ();
            findUpdatesOfPOM (aPOM, true);
            if (false)
              SimpleFileIO.writeFile (new File (aFile.getParentFile (), aFile.getName () + "-gen.xml"),
                                      MicroWriter.getXMLString (aPOM.getAsDOMTree ()),
                                      XMLWriterSettings.DEFAULT_XML_CHARSET_OBJ);
          }
      }
      finally
      {
        LamaMetaManager.getArtifactInfoMgr ().endWithoutAutoSave ();
      }
    });
  }
}
