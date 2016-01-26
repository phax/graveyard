/**
 * Copyright (C) 2012-2015 Philip Helger
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.genetic.tsp.eventhandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.genetic.IEventHandler;
import com.helger.genetic.eventhandler.EventHandlerDefault;
import com.helger.genetic.model.IChromosome;
import com.helger.genetic.model.IPopulation;
import com.helger.genetic.tsp.model.TSPFitnessFunction;

public class TSPEventHandlerLogging extends EventHandlerDefault
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (TSPEventHandlerLogging.class);
  private final long m_nStartTime;

  public TSPEventHandlerLogging ()
  {
    this (null);
  }

  public TSPEventHandlerLogging (@Nullable final IEventHandler aNestedEventHandler)
  {
    super (aNestedEventHandler);
    m_nStartTime = System.currentTimeMillis ();
  }

  @Override
  protected void internalOnNewFittestChromosome (@Nonnull final IChromosome aCurrentFittest)
  {
    final long nElaspedMS = System.currentTimeMillis () - m_nStartTime;
    final double dWorstCase = ((TSPFitnessFunction) aCurrentFittest.getFitnessFunction ()).getWorstCaseDistance ();
    s_aLogger.info ("After " +
                    nElaspedMS +
                    " ms [Gen " +
                    getLastGeneration () +
                    "]: New shortest distance [" +
                    (dWorstCase - aCurrentFittest.getFitness ()) +
                    "]");
  }

  @Override
  protected void internalOnNewPopulation (@Nonnull final IPopulation aPopulation)
  {
    if ((aPopulation.getGeneration () % 1000000) == 0)
    {
      final long nElaspedMS = System.currentTimeMillis () - m_nStartTime;
      s_aLogger.info ("After " + nElaspedMS + "ms at generation " + aPopulation.getGeneration ());
    }
  }
}
