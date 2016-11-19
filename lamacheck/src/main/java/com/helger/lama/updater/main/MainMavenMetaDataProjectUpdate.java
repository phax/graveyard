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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.id.IHasID;
import com.helger.commons.io.file.FileHelper;
import com.helger.lama.maven.EMavenPackaging;
import com.helger.lama.maven.artifact.MavenArtifact;
import com.helger.lama.maven.pom.MavenPOM;
import com.helger.lama.maven.pom.POMDependency;
import com.helger.lama.maven.pom.POMPlugin;
import com.helger.lama.maven.repo.MavenRepository;
import com.helger.lama.updater.domain.MavenArtifactInfo;
import com.helger.lama.updater.domain.MavenRepositoryInfo;
import com.helger.lama.updater.mgr.LamaMetaManager;
import com.helger.lama.updater.mgr.MavenRepositoryInfoManager;

public final class MainMavenMetaDataProjectUpdate
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MainMavenMetaDataProjectUpdate.class);

  public static void writePOM ()
  {
    final MavenPOM aPOM = new MavenPOM (new MavenArtifact ("com.phloc.maven",
                                                           "phloc-maven-localupdater",
                                                           "1.0.0-SNAPSHOT"));
    if (false)
      aPOM.setParentArtifact (new MavenArtifact ("com.phloc", "phloc-devsupport-parent-pom", "1"));
    aPOM.setName (aPOM.getArtifact ().getArtifactID ());

    final MavenRepositoryInfoManager aRepoInfoMgr = LamaMetaManager.getRepoInfoMgr ();

    // As they are identical, use only one repo. And repo2 is faster than repo1!
    aPOM.addRepository (aRepoInfoMgr.getRepositoryOfID (MavenRepository.ID_CENTRAL2).getRepo ());
    aPOM.addPluginRepository (aRepoInfoMgr.getRepositoryOfID (MavenRepository.ID_CENTRAL2).getRepo ());

    // Add all repos - not necessary because we have the Nexus
    if (false)
      for (final MavenRepositoryInfo aRepoInfo : CollectionHelper.getSorted (aRepoInfoMgr.getAllValidRepositories (),
                                                                             IHasID.getComparatorID ()))
        if (!aRepoInfo.isCentral ())
        {
          aPOM.addRepository (aRepoInfo.getRepo ());
          if (false)
            aPOM.addPluginRepository (aRepoInfo.getRepo ());
        }

    // Add all artifacts
    for (final MavenArtifactInfo aArtifactInfo : LamaMetaManager.getArtifactInfoMgr ().getAllArtifactsSorted ())
      if (aArtifactInfo.getLatestReleaseVersion () != null)
      {
        final EMavenPackaging ePackaging = aArtifactInfo.getPackaging ();
        if (ePackaging == null || !ePackaging.equals (EMavenPackaging.POM))
        {
          final POMDependency aDependency = new POMDependency (aArtifactInfo.getGroupID (),
                                                               aArtifactInfo.getArtifactID (),
                                                               aArtifactInfo.getLatestReleaseVersion ()
                                                                            .getOriginalVersion ());
          if (!aDependency.getArtifact ().containsUnresolvedVariable ())
          {
            if (aArtifactInfo.isPluginArtifact ())
            {
              if (false)
                aPOM.addBuildPlugin (new POMPlugin (aDependency.getArtifact ()));
            }
            else
              aPOM.addDependency (aDependency);
          }
        }
      }

    final POMPlugin aCompilerPlugin = new POMPlugin ("org.apache.maven.plugins", "maven-compiler-plugin");
    aCompilerPlugin.getConfiguration ().addItem ("source", "1.6");
    // Jetty 9 requires target 1.7
    aCompilerPlugin.getConfiguration ().addItem ("target", "1.7");
    aPOM.addBuildPlugin (aCompilerPlugin);

    File f = new File ("").getAbsoluteFile ();
    if (f.getName ().equals ("trunk"))
      f = new File (f.getParentFile ().getParentFile (), "phloc-maven-localupdater/trunk/pom.xml");
    else
      f = new File (f.getParentFile (), "phloc-maven-localupdater/pom.xml");
    aPOM.writeToStream (FileHelper.getOutputStream (f));
    s_aLogger.info ("Successfully created pom.xml");
  }

  public static void main (final String [] args) throws Exception
  {
    LamaRunner.run ( () -> writePOM ());
  }
}
