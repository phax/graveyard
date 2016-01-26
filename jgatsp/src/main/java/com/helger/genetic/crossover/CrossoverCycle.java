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
import com.helger.commons.annotation.UnsupportedOperation;
import com.helger.genetic.model.Chromosome;
import com.helger.genetic.model.IChromosome;
import com.helger.genetic.utils.decisionmaker.IDecisionMaker;

/**
 * Crossover cycle Operator. <a
 * href="http://ecet.ecs.ru.acad.bg/cst05/Docs/cp/SIII/IIIA.20.pdf">Source</a>
 * Crossover for 2 int-chromosomes, having unique genes only. The Cycle
 * Crossover operator identifies a number of so-called cycles between two parent
 * chromosomes. Then, to form Child 1, cycle one is copied from parent 1, cycle
 * 2 from parent 2, cycle 3 from parent 1, and so on.:
 * 
 * <pre>
 *     E1 = (0 - 1 - 2 - 3 - 4 - 5 - 6 - 7)
 *     E2 = (7 - 4 - 1 - 0 - 2 - 5 - 3 - 6)
 * </pre>
 * 
 * Start at index 0
 * 
 * <pre>
 *     N1 = (0 - x - x - x - x - x - x - x)
 *     N2 = (7 - x - x - x - x - x - x - x)
 * </pre>
 * 
 * Now goto value 7 in E1 and continue
 * 
 * <pre>
 *     N1 = (0 - x - x - x - x - x - x - 7)
 *     N2 = (7 - x - x - x - x - x - x - 6)
 * </pre>
 * 
 * Now goto value 6 in E1 and continue
 * 
 * <pre>
 *     N1 = (0 - x - x - x - x - x - 6 - 7)
 *     N2 = (7 - x - x - x - x - x - 3 - 6)
 * </pre>
 * 
 * Now goto value 3 in E1 and continue
 * 
 * <pre>
 *     N1 = (0 - x - x - 3 - x - x - 6 - 7)
 *     N2 = (7 - x - x - 0 - x - x - 3 - 6)
 * </pre>
 * 
 * As value 0 was already used in N1 stop cycling and copy the remaining genes
 * from E2 to N1 and from E1 to N2 (main crossover)
 * 
 * <pre>
 *     N1 = (0 - 4 - 1 - 3 - 2 - 5 - 6 - 7)
 *     N2 = (7 - 1 - 2 - 0 - 4 - 5 - 3 - 6)
 * </pre>
 * 
 * @author Philip Helger
 */
public class CrossoverCycle extends AbstractCrossover
{
  public CrossoverCycle (@Nonnull final IDecisionMaker aDescisionMaker)
  {
    super (2, aDescisionMaker);
  }

  @Override
  @UnsupportedOperation
  public IChromosome [] executeCrossover (@Nonnull @Nonempty final IChromosome [] aChromosomes)
  {
    final int nGenes = aChromosomes[0].getGeneCount ();

    final CCWWithIndex aGenes0 = new CCWWithIndex (aChromosomes[0]);
    final CCWWithIndex aGenes1 = new CCWWithIndex (aChromosomes[1]);

    // Cycle all values that should stay
    int nLastValue = aGenes0.getOldValue (0);
    while (!aGenes0.isNewValueUsed (nLastValue))
    {
      final int nIndex = aGenes0.getIndexOfOldValue (nLastValue);
      final int nOld0 = aGenes0.getOldValue (nIndex);
      final int nOld1 = aGenes1.getOldValue (nIndex);
      aGenes0.setNewValue (nIndex, nOld0);
      aGenes1.setNewValue (nIndex, nOld1);
      nLastValue = nOld1;
    }

    // Copy all remaining genes from 1 to 0 and vice versa
    for (int i = aGenes0.getNextNewUnusedIndex (0); i < nGenes; i = aGenes0.getNextNewUnusedIndex (i + 1))
    {
      aGenes0.setNewValue (i, aGenes1.getOldValue (i));
      aGenes1.setNewValue (i, aGenes0.getOldValue (i));
    }

    final IChromosome aNew0 = Chromosome.createGenesInt (aChromosomes[0], aGenes0.getAllNewGenes ());
    final IChromosome aNew1 = Chromosome.createGenesInt (aChromosomes[1], aGenes1.getAllNewGenes ());
    return new IChromosome [] { aNew0, aNew1 };
  }
}
