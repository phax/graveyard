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
package com.helger.genetic.tsp.model;

import java.util.BitSet;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.genetic.model.IChromosome;
import com.helger.genetic.model.IChromsomeValidator;

@NotThreadSafe
public class TSPChromosomeValidator implements IChromsomeValidator
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (TSPChromosomeValidator.class);

  private final int m_nCities;
  private final BitSet m_aBits;

  public TSPChromosomeValidator (@Nonnegative final int nCities)
  {
    if (nCities < 2)
      throw new IllegalArgumentException ("City count must at least be 2!");
    m_nCities = nCities;
    m_aBits = new BitSet (nCities);
  }

  public boolean isValidChromosome (@Nonnull final IChromosome aChromosome)
  {
    m_aBits.clear ();
    for (final int nCity : aChromosome.getGeneIntArray ())
      m_aBits.set (nCity);
    // If the next clear bit is the one "after" the last one, we're fine
    final int nMissingCity = m_aBits.nextClearBit (0);
    if (nMissingCity == m_nCities)
      return true;
    s_aLogger.warn ("Chromosome misses gene " + nMissingCity + ": " + aChromosome.getAllGenes ());
    return false;
  }
}
