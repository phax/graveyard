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
package com.helger.genetic.selector;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.genetic.model.IChromosome;
import com.helger.genetic.utils.random.RandomGenerator;

/**
 * Cross over selector:
 * <ul>
 * <li>Take a random subset of chromosomes to choose from</li>
 * <li>Pick random chromosomes in each run</li>
 * </ul>
 *
 * @author Philip Helger
 */
public class SelectorRandomUnsortedRandom extends AbstractSelector
{
  private final int m_nTournamentSize;

  public SelectorRandomUnsortedRandom (@Nonnegative final int nTournamentSize)
  {
    ValueEnforcer.isGT0 (nTournamentSize, "TournamentSize");
    m_nTournamentSize = nTournamentSize;
  }

  @Nonnull
  public List <IChromosome> selectSurvivingChromosomes (@Nonnull final List <IChromosome> aChromosomes)
  {
    final int nChromosomeCount = aChromosomes.size ();
    final IChromosome [] aChosen = new IChromosome [m_nTournamentSize];
    for (int i = 0; i < m_nTournamentSize; ++i)
      aChosen[i] = aChromosomes.get (RandomGenerator.getIntInRange (nChromosomeCount));

    final List <IChromosome> ret = new ArrayList <IChromosome> (nChromosomeCount);
    for (int i = 0; i < nChromosomeCount; ++i)
      ret.add (aChosen[RandomGenerator.getIntInRange (m_nTournamentSize)]);
    return ret;
  }
}
