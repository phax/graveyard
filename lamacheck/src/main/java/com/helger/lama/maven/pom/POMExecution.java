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

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.string.StringHelper;
import com.helger.lama.maven.CMaven;
import com.helger.lama.maven.IMavenSerializableObject;
import com.helger.xml.microdom.IMicroElement;

public class POMExecution implements IMavenSerializableObject
{
  private String m_sID;
  private String m_sPhase;
  private final ICommonsList <String> m_aGoals = new CommonsArrayList<> ();
  private final POMConfiguration m_aConfiguration = new POMConfiguration ();

  public POMExecution ()
  {}

  public void setID (@Nullable final String sID)
  {
    m_sID = sID;
  }

  @Nullable
  public String getID ()
  {
    return m_sID;
  }

  public void setPhase (@Nullable final String sPhase)
  {
    m_sPhase = sPhase;
  }

  @Nullable
  public String getPhase ()
  {
    return m_sPhase;
  }

  public void addGoal (final String sGoal)
  {
    if (StringHelper.hasNoText (sGoal))
      throw new IllegalArgumentException ("goal");
    m_aGoals.add (sGoal);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllGoals ()
  {
    return CollectionHelper.newList (m_aGoals);
  }

  @Nonnull
  public POMConfiguration getConfiguration ()
  {
    return m_aConfiguration;
  }

  @Override
  public void appendToElement (@Nonnull final IMicroElement aElement)
  {
    final IMicroElement eExecution = aElement.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_EXECUTION);

    if (StringHelper.hasText (m_sID))
      eExecution.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_ID).appendText (m_sID);

    if (StringHelper.hasText (m_sPhase))
      eExecution.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_PHASE).appendText (m_sPhase);

    if (!m_aGoals.isEmpty ())
    {
      final IMicroElement eGoals = eExecution.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_GOALS);
      for (final String sGoal : m_aGoals)
        eGoals.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_GOAL).appendText (sGoal);
    }

    if (!m_aConfiguration.isEmpty ())
      m_aConfiguration.appendToElement (eExecution);
  }
}
