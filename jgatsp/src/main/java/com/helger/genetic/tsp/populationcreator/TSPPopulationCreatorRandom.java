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
package com.helger.genetic.tsp.populationcreator;

import java.math.BigInteger;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.math.FactorialHelper;
import com.helger.genetic.model.Chromosome;
import com.helger.genetic.model.IChromosome;
import com.helger.genetic.model.IChromsomeValidator;
import com.helger.genetic.model.IFitnessFunction;
import com.helger.genetic.model.IMutablePopulation;
import com.helger.genetic.model.IPopulation;
import com.helger.genetic.populationcreator.AbstractPopulationCreator;
import com.helger.genetic.utils.random.RandomGenerator;

public final class TSPPopulationCreatorRandom extends AbstractPopulationCreator
{
  private final int m_nCities;
  private final int m_nPopulationSize;
  private final IFitnessFunction m_aFitnessFunction;
  private final IChromsomeValidator m_aChromosomeValidator;

  public TSPPopulationCreatorRandom (@Nonnegative final int nCities,
                                     @Nonnegative final int nPopulationSize,
                                     @Nonnull final IFitnessFunction aFitnessFunction,
                                     @Nullable final IChromsomeValidator aChromosomeValidator)
  {
    if (nCities < 2)
      throw new IllegalArgumentException ("too few cities");
    if (nPopulationSize < 2)
      throw new IllegalArgumentException ("too few populations!");
    if (aFitnessFunction == null)
      throw new NullPointerException ("fitnessFunction");
    final BigInteger aPossiblePermutations = FactorialHelper.getAnyFactorialLinear (nCities);
    if (BigInteger.valueOf (nPopulationSize).compareTo (aPossiblePermutations) > 0)
      throw new IllegalArgumentException ("Cannot generate " +
                                          nPopulationSize +
                                          " different random populations but only " +
                                          aPossiblePermutations.toString () +
                                          "!");
    m_nCities = nCities;
    m_nPopulationSize = nPopulationSize;
    m_aFitnessFunction = aFitnessFunction;
    m_aChromosomeValidator = aChromosomeValidator;
  }

  private static void _swapRandom (@Nonnull final int [] aCities)
  {
    final int [] aIndices = RandomGenerator.getMultipleUniqueIntsInRange (2, aCities.length);
    final int nIndex1 = aIndices[0];
    final int nIndex2 = aIndices[1];

    final int nOld = aCities[nIndex1];
    aCities[nIndex1] = aCities[nIndex2];
    aCities[nIndex2] = nOld;
  }

  @Nonnull
  private IChromosome _createRandomChromosome ()
  {
    // Initial linear create
    final int [] aCities = new int [m_nCities];
    for (int i = 0; i < m_nCities; ++i)
      aCities[i] = i;

    // And some random swapping
    for (int i = 0; i < m_nCities; ++i)
      _swapRandom (aCities);

    // We're done
    return Chromosome.createGenesInt (m_aFitnessFunction, m_aChromosomeValidator, aCities);
  }

  @Nonnull
  public IPopulation createInitialPopulation ()
  {
    final Set <IChromosome> cs = new LinkedHashSet <IChromosome> ();
    while (cs.size () < m_nPopulationSize)
    {
      final IChromosome aChromosome = _createRandomChromosome ();
      cs.add (aChromosome);
    }

    // Create a generation 0 object and add them in arbitrary order
    final IMutablePopulation ret = createEmptyPopulation ();
    for (final IChromosome aChromosome : cs)
      ret.addChromosome (aChromosome);
    return ret;
  }
}
