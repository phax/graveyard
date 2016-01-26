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

import com.helger.commons.csv.CSVWriter;
import com.helger.genetic.eventhandler.EventHandlerDefault;
import com.helger.genetic.model.IChromosome;
import com.helger.genetic.model.IPopulation;
import com.helger.genetic.tsp.model.TSPFitnessFunction;

public class TSPEventHandlerCSV extends EventHandlerDefault
{
  private final CSVWriter m_aWriter;
  private final TSPFitnessFunction m_aFF;

  public TSPEventHandlerCSV (@Nonnull final CSVWriter aWriter, @Nonnull final TSPFitnessFunction ff)
  {
    m_aWriter = aWriter;
    m_aFF = ff;
    m_aWriter.writeNext (new String [] { "Generation", "Distance" });
  }

  @Override
  protected void internalOnNewPopulation (@Nonnull final IPopulation aPopulation)
  {
    final IChromosome aFittest = aPopulation.getFittestChromosome ();
    final int nDistance = (int) m_aFF.getDistance (aFittest);
    m_aWriter.writeNext (new String [] { Long.toString (aPopulation.getGeneration ()), Integer.toString (nDistance) });
  }
}
