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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.ext.CommonsLinkedHashMap;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.collection.ext.ICommonsOrderedMap;
import com.helger.lama.maven.CMaven;
import com.helger.lama.maven.IMavenSerializableObject;
import com.helger.xml.microdom.IMicroElement;

@NotThreadSafe
public class POMRepositories implements IMavenSerializableObject
{
  private final boolean m_bPluginRepo;
  private final ICommonsOrderedMap <String, POMRepository> m_aMap = new CommonsLinkedHashMap<> ();

  public POMRepositories (final boolean bPluginRepo)
  {
    m_bPluginRepo = bPluginRepo;
  }

  public void addRepository (@Nullable final POMRepository aPOMRepository)
  {
    if (aPOMRepository != null)
    {
      m_aMap.put (aPOMRepository.getID (), aPOMRepository);
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <POMRepository> getAllRepositories ()
  {
    return m_aMap.copyOfValues ();
  }

  @Nonnegative
  public int getRepositoryCount ()
  {
    return m_aMap.size ();
  }

  public void appendToElement (final IMicroElement aElement)
  {
    if (!m_aMap.isEmpty ())
    {
      final IMicroElement eRepositories = aElement.appendElement (CMaven.POM_XMLNS,
                                                                  m_bPluginRepo ? CMavenXML.ELEMENT_PLUGINREPOSITORIES
                                                                                : CMavenXML.ELEMENT_REPOSITORIES);
      for (final POMRepository aRepo : m_aMap.values ())
        aRepo.appendToElement (eRepositories);
    }
  }
}
