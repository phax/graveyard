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
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.compare.ESortOrder;
import com.helger.genetic.model.ComparatorChromosomeFitness;
import com.helger.genetic.model.IChromosome;
import com.helger.genetic.utils.random.RandomGenerator;

/**
 * Cross over selector:
 * <ul>
 * <li>Take all chromosomes in best fitting order</li>
 * <li>Choose every chromosome exactly once, independent of the fitness</li>
 * </ul>
 *
 * @author Philip Helger
 */
public class SelectorTournament extends AbstractSelector
{
  public SelectorTournament ()
  {}

  @Nonnull
  public List <IChromosome> selectSurvivingChromosomes (@Nonnull final List <IChromosome> aChromosomes)
  {
    Collections.sort (aChromosomes, new ComparatorChromosomeFitness ().setSortOrder (ESortOrder.ASCENDING));

    final int nChromosomes = aChromosomes.size ();
    double dTotalFitness = 0;
    int nIndex = 0;
    final double [] aFitnesses = new double [1 + nChromosomes];
    aFitnesses[0] = 0;
    for (final IChromosome aChromosome : aChromosomes)
    {
      dTotalFitness += aChromosome.getFitness ();
      aFitnesses[++nIndex] = dTotalFitness;
    }

    // Unify fitness value between 0 and 1
    for (int i = 0; i < aFitnesses.length; ++i)
      aFitnesses[i] /= dTotalFitness;

    final List <IChromosome> ret = new ArrayList <IChromosome> (nChromosomes);
    for (int i = 0; i < nChromosomes; ++i)
    {
      final double dRandomFitness = RandomGenerator.getDouble ();
      for (int k = 0; k < nChromosomes; ++k)
        if (dRandomFitness > aFitnesses[k] && dRandomFitness <= aFitnesses[k + 1])
        {
          ret.add (aChromosomes.get (k));
          break;
        }
    }

    if (ret.size () != nChromosomes)
      throw new IllegalStateException ("Mismatch. Having " + ret.size () + " but expected " + nChromosomes);

    return ret;
  }
}
