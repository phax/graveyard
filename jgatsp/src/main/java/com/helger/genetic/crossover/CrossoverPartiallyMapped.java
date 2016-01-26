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
 * Crossover PMX Operator. <a
 * href="http://rosin-online.de/ki/partiallymappedCrossover.html">Source</a>
 * Crossover for 2 int-chromosomes at a single point, having unique genes only.
 * Select a part of the parent chromosomes that is directly copied to its
 * children:
 * 
 * <pre>
 *     E1 = (2 - 3 - 5 - 7 - 1 - 6 - 4)
 *     E2 = (4 - 5 - 6 - 3 - 2 - 7 - 1)
 *                      +---------+
 *     N1 = (x - x - x - 3 - 2 - 7 - x)
 *     N2 = (x - x - x - 7 - 1 - 6 - x)
 * </pre>
 * 
 * Copy as many genes from the parent (keeping the position)
 * 
 * <pre>
 *     N1 = (x - x - 5 - 3 - 2 - 7 - 4)
 *     N2 = (4 - 5 - x - 7 - 1 - 6 - x)
 * </pre>
 * 
 * The remaining genes are filled from left to right
 * 
 * <pre>
 *     N1 = (1 - 6 - 5 - 3 - 2 - 7 - 4)
 *     N2 = (4 - 5 - 2 - 7 - 1 - 6 - 3)
 * </pre>
 * 
 * @author Philip Helger
 */
public class CrossoverPartiallyMapped extends AbstractCrossover
{
  public CrossoverPartiallyMapped (@Nonnull final IDecisionMaker aDescisionMaker)
  {
    super (2, aDescisionMaker);
  }

  /**
   * This method determines the 2 indices where the values should be swapped.
   * This can be overridden for tests, to avoid the randomness
   * 
   * @param nGenes
   *        The maximum index (exclusive)
   * @return An array of 2 ints, where the first one must be lower than the
   *         second one
   */
  @VisibleForTesting
  @OverrideOnDemand
  protected int [] getCrossoverIndices (final int nGenes)
  {
    return RandomGenerator.getMultipleUniqueIntsInRange (2, nGenes);
  }

  @Override
  @UnsupportedOperation
  public IChromosome [] executeCrossover (@Nonnull @Nonempty final IChromosome [] aChromosomes)
  {
    final int nGenes = aChromosomes[0].getGeneCount ();
    final int [] aCrossoverIndeces = getCrossoverIndices (nGenes);

    final CCWWithIndex aGenes0 = new CCWWithIndex (aChromosomes[0]);
    final CCWWithIndex aGenes1 = new CCWWithIndex (aChromosomes[1]);

    // Swap between crossovers
    for (int i = aCrossoverIndeces[0]; i < aCrossoverIndeces[1]; ++i)
    {
      aGenes0.setNewValue (i, aGenes1.getOldValue (i));
      aGenes1.setNewValue (i, aGenes0.getOldValue (i));
    }

    // use stuff from parent as good as possible (before crossover start and
    // after crossover end)
    for (int i = 0; i < nGenes; ++i)
      if (i < aCrossoverIndeces[0] || i >= aCrossoverIndeces[1])
      {
        final int nOldGene0 = aGenes0.getOldValue (i);
        if (!aGenes0.isNewValueUsed (nOldGene0))
          aGenes0.setNewValue (i, nOldGene0);

        final int nOldGene1 = aGenes1.getOldValue (i);
        if (!aGenes1.isNewValueUsed (nOldGene1))
          aGenes1.setNewValue (i, nOldGene1);
      }

    // Fill missing elements
    for (int i = aGenes0.getNextNewUnusedIndex (0); i < nGenes; i = aGenes0.getNextNewUnusedIndex (i + 1))
      aGenes0.setNewValue (i, aGenes0.getNextUnusedNewValue (0));
    for (int i = aGenes1.getNextNewUnusedIndex (0); i < nGenes; i = aGenes1.getNextNewUnusedIndex (i + 1))
      aGenes1.setNewValue (i, aGenes1.getNextUnusedNewValue (0));

    final IChromosome aNew0 = Chromosome.createGenesInt (aChromosomes[0], aGenes0.getAllNewGenes ());
    final IChromosome aNew1 = Chromosome.createGenesInt (aChromosomes[1], aGenes1.getAllNewGenes ());
    return new IChromosome [] { aNew0, aNew1 };
  }
}
