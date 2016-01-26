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
package com.helger.genetic.crossover;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.OverrideOnDemand;
import com.helger.commons.annotation.UnsupportedOperation;
import com.helger.commons.annotation.VisibleForTesting;
import com.helger.genetic.model.Chromosome;
import com.helger.genetic.model.IChromosome;
import com.helger.genetic.utils.decisionmaker.IDecisionMaker;
import com.helger.genetic.utils.random.RandomGenerator;

/**
 * Crossover for 2 int-chromosomes at a single point, having unique genes only.
 * Example cut at index 2:
 * 
 * <pre>
 * Chromosome1: 1 2 | 3 4 5 6
 * Chromosome2: 4 2 | 6 5 1 3
 * </pre>
 * 
 * Fill chromosome 1 with all available genes from chromosome 2 that are
 * available (and vice versa)
 * 
 * <pre>
 * Chromosome1: 1 2 | 6 5 3 x
 * Chromosome2: 4 2 | 3 5 6 x
 * </pre>
 * 
 * Fill both chromosomes with the remaining free elements, in ascending order
 * 
 * <pre>
 * Chromosome1: 1 2 | 6 5 3 4
 * Chromosome2: 4 2 | 3 5 6 1
 * </pre>
 * 
 * @author Philip Helger
 */
public class CrossoverOnePointInt extends AbstractCrossover
{
  public CrossoverOnePointInt (@Nonnull final IDecisionMaker aDescisionMaker)
  {
    super (2, aDescisionMaker);
  }

  /**
   * This method determines the index where the crossover should be performed.
   * This can be overridden for tests, to avoid the randomness
   * 
   * @param nGenes
   *        The maximum index (exclusive)
   * @return A non-negative index of the crossover
   */
  @VisibleForTesting
  @OverrideOnDemand
  protected int getCrossoverIndex (final int nGenes)
  {
    return RandomGenerator.getIntInRange (nGenes);
  }

  @Override
  @UnsupportedOperation
  public IChromosome [] executeCrossover (@Nonnull @Nonempty final IChromosome [] aChromosomes)
  {
    final int nGenes = aChromosomes[0].getGeneCount ();
    final int nCrossoverIndex = getCrossoverIndex (nGenes);

    final CCW aGenes0 = new CCW (aChromosomes[0]);
    final CCW aGenes1 = new CCW (aChromosomes[1]);
    int nIndex0 = 0;
    int nIndex1 = 0;

    // Copy as-is until crossover point
    for (int i = 0; i < nCrossoverIndex; ++i)
    {
      aGenes0.setNewValue (nIndex0++, aGenes0.getOldValue (i));
      aGenes1.setNewValue (nIndex1++, aGenes1.getOldValue (i));
    }

    // perform cross over if possible
    for (int i = nCrossoverIndex; i < nGenes; ++i)
    {
      final int nOldGene0 = aGenes0.getOldValue (i);
      final int nOldGene1 = aGenes1.getOldValue (i);
      if (!aGenes0.isNewValueUsed (nOldGene1))
        aGenes0.setNewValue (nIndex0++, nOldGene1);
      if (!aGenes1.isNewValueUsed (nOldGene0))
        aGenes1.setNewValue (nIndex1++, nOldGene0);
    }

    // Fill missing elements
    // Take all clear bits
    for (int i = aGenes0.getNextUnusedNewValue (0); i < nGenes; i = aGenes0.getNextUnusedNewValue (i + 1))
      aGenes0.setNewValue (nIndex0++, i);
    for (int i = aGenes1.getNextUnusedNewValue (0); i < nGenes; i = aGenes1.getNextUnusedNewValue (i + 1))
      aGenes1.setNewValue (nIndex1++, i);

    if (nIndex0 != nGenes)
      throw new IllegalArgumentException ("Gene mismatch for chromosome 0");
    if (nIndex1 != nGenes)
      throw new IllegalArgumentException ("Gene mismatch for chromosome 1");

    final IChromosome aNew0 = Chromosome.createGenesInt (aChromosomes[0], aGenes0.getAllNewGenes ());
    final IChromosome aNew1 = Chromosome.createGenesInt (aChromosomes[1], aGenes1.getAllNewGenes ());
    return new IChromosome [] { aNew0, aNew1 };
  }
}
