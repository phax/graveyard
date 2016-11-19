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

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.lama.maven.CMaven;
import com.helger.lama.maven.IMavenSerializableObject;
import com.helger.xml.microdom.IMicroElement;

public final class POMExecutions implements IMavenSerializableObject
{
  private final ICommonsList <POMExecution> m_aExecutions = new CommonsArrayList<> ();

  public void addExecution (final POMExecution aExecution)
  {
    if (aExecution == null)
      throw new NullPointerException ("Execution");
    m_aExecutions.add (aExecution);
  }

  public void addAll (@Nonnull final POMExecutions aExecutions)
  {
    m_aExecutions.addAll (aExecutions.m_aExecutions);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <POMExecution> getAll ()
  {
    return CollectionHelper.newList (m_aExecutions);
  }

  public boolean isEmpty ()
  {
    return m_aExecutions.isEmpty ();
  }

  public void appendToElement (final IMicroElement aElement)
  {
    if (!m_aExecutions.isEmpty ())
    {
      final IMicroElement eDependencies = aElement.appendElement (CMaven.POM_XMLNS, CMavenXML.ELEMENT_EXECUTIONS);
      for (final POMExecution aExecution : m_aExecutions)
        aExecution.appendToElement (eDependencies);
    }
  }
}
