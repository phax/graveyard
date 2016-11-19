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
package com.helger.lama.maven.pom;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.id.IHasID;
import com.helger.commons.string.ToStringGenerator;
import com.helger.lama.maven.CMaven;
import com.helger.lama.maven.IMavenSerializableObject;
import com.helger.lama.maven.repo.MavenRepository;
import com.helger.xml.microdom.IMicroElement;

public class POMRepository implements IHasID <String>, IMavenSerializableObject
{
  private final boolean m_bPluginRepo;
  private final MavenRepository m_aRepo;

  public POMRepository (final boolean bPluginRepo, @Nonnull final MavenRepository aRepo)
  {
    if (aRepo == null)
      throw new NullPointerException ("repo");
    m_bPluginRepo = bPluginRepo;
    m_aRepo = aRepo;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_aRepo.getID ();
  }

  public boolean isPluginRepo ()
  {
    return m_bPluginRepo;
  }

  @Nonnull
  public MavenRepository getRepo ()
  {
    return m_aRepo;
  }

  public void appendToElement (@Nonnull final IMicroElement aElement)
  {
    final IMicroElement eRepository = aElement.appendElement (CMaven.POM_XMLNS,
                                                              m_bPluginRepo ? CMavenXML.ELEMENT_PLUGINREPOSITORY : CMavenXML.ELEMENT_REPOSITORY);
    eRepository.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_ID).appendText (m_aRepo.getID ());
    eRepository.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_URL).appendText (m_aRepo.getURL ());
    if (!m_aRepo.getLayout ().isDefault ())
      eRepository.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_LAYOUT).appendText (m_aRepo.getLayout ().getID ());
    eRepository.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_RELEASES)
               .appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_ENABLED)
               .appendText (Boolean.toString (m_aRepo.isReleasesEnabled ()));
    eRepository.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_SNAPSHOTS)
               .appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_ENABLED)
               .appendText (Boolean.toString (m_aRepo.isSnapshotsEnabled ()));
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("pluginRepo", m_bPluginRepo).append ("repo", m_aRepo).toString ();
  }
}
