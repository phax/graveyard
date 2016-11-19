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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.io.file.SimpleFileIO;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.lama.updater.domain.MavenRepositoryInfo;
import com.helger.lama.updater.mgr.LamaMetaManager;
import com.helger.lama.updater.mgr.MavenArtifactInfoManager;
import com.helger.lama.updater.mgr.MavenRepositoryInfoManager;
import com.helger.lama.updater.utils.HTTPReader;
import com.helger.lama.updater.utils.UpdateBlacklist;
import com.helger.photon.basic.app.io.WebFileIO;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.util.MicroHelper;

/**
 * Read the global Maven archetype catalog and add all contained artifacts
 *
 * @author Philip Helger
 */
public final class MainArchetypeCatalogUpdater
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MainArchetypeCatalogUpdater.class);

  public static void main (final String [] args) throws Exception
  {
    LamaRunner.run ( () -> {
      final FileSystemResource aLock = WebFileIO.getDataIO ().getResource ("lama.running");
      if (aLock.exists ())
        throw new IllegalStateException ("LaMa already running!");
      SimpleFileIO.writeFile (aLock.getAsFile (), new byte [0]);

      final MavenArtifactInfoManager aAIM = LamaMetaManager.getArtifactInfoMgr ();
      final MavenRepositoryInfoManager aRIM = LamaMetaManager.getRepoInfoMgr ();
      aAIM.beginWithoutAutoSave ();
      try
      {
        final UpdateBlacklist aBlacklist = new UpdateBlacklist ();
        for (final MavenRepositoryInfo aRepoInfo : aRIM.getAllRepositories ())
          if (aRepoInfo.isValid ())
          {
            final IMicroDocument aDoc = HTTPReader.readXML (aRepoInfo, "archetype-catalog.xml", aBlacklist);
            if (aDoc != null)
            {
              s_aLogger.info ("Found archetype catalog in " + aRepoInfo.getURL ());
              final IMicroElement eArchetypes = aDoc.getDocumentElement ().getFirstChildElement ("archetypes");
              if (eArchetypes != null)
                for (final IMicroElement eArchetype : eArchetypes.getAllChildElements ("archetype"))
                  aAIM.addArtifact (MicroHelper.getChildTextContent (eArchetype, "groupId"),
                                    MicroHelper.getChildTextContent (eArchetype, "artifactId"));
            }
          }
      }
      finally
      {
        aAIM.endWithoutAutoSave ();
        WebFileIO.getFileOpMgr ().deleteFile (aLock.getAsFile ());
      }
    });
  }
}
