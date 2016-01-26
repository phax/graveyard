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
package com.helger.genetic.mutation;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.helger.genetic.model.Chromosome;
import com.helger.genetic.model.IChromosome;
import com.helger.genetic.model.IGene;
import com.helger.genetic.utils.decisionmaker.IDecisionMaker;
import com.helger.genetic.utils.random.RandomGenerator;

/**
 * Mutation that randomly moves one or more genes. Original:
 * 
 * <pre>
 * 1 2 3 4 5 6 7
 *  +---+   ^
 * </pre>
 * 
 * After the mutation:
 * 
 * <pre>
 * 1 4 5 2 3 6 7
 *      +---+
 * </pre>
 * 
 * Also called "Swap".
 * 
 * @author Philip Helger
 */
public class MutationRandomMoveMultiple extends AbstractMutation
{
  public MutationRandomMoveMultiple (@Nonnull final IDecisionMaker aDescisionMaker)
  {
    super (aDescisionMaker);
  }

  @Override
  @Nonnull
  public IChromosome executeMutation (@Nonnull final IChromosome aChromosome)
  {
    final int nGenes = aChromosome.getGeneCount ();
    if (nGenes < 3)
    {
      // At least 3 genes must be present because it is prohibited to move all
      // or none genes
      throw new IllegalArgumentException ("You need to have at least 3 genes, but you only have " + nGenes + " genes!");
    }

    // Never move none or all elements
    final int nElementsToMove = 1 + RandomGenerator.getIntInRange (nGenes - 2);

    // Select start and end index the remaining range
    final int [] aIndices = RandomGenerator.getMultipleUniqueIntsInRange (2, nGenes - nElementsToMove);
    final int nIndexSrc = aIndices[0];
    final int nIndexDst = aIndices[1];

    // Create a copy of the genes
    final List <IGene> aGenes = aChromosome.getAllGenes ();
    // Remove all from the source position
    final List <IGene> aGenesToMove = new ArrayList <IGene> (nElementsToMove);
    for (int i = 0; i < nElementsToMove; ++i)
      aGenesToMove.add (aGenes.remove (nIndexSrc));
    // and insert at the new position
    aGenes.addAll (nIndexDst, aGenesToMove);
    // Return the new chromosome
    return new Chromosome (aChromosome, aGenes);
  }
}
