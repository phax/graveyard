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

import java.util.List;

import javax.annotation.Nonnull;

import com.helger.genetic.model.Chromosome;
import com.helger.genetic.model.IChromosome;
import com.helger.genetic.model.IGene;
import com.helger.genetic.utils.decisionmaker.IDecisionMaker;
import com.helger.genetic.utils.random.RandomGenerator;

/**
 * Mutation that randomly exchanges 2 genes. Original:
 * 
 * <pre>
 * 1 2 3 4 5 6 7
 *    +-+   +-+
 * </pre>
 * 
 * After the mutation:
 * 
 * <pre>
 * 1 2 6 4 5 3 7
 *    +-+   +-+
 * </pre>
 * 
 * Also called "Swap".
 * 
 * @author Philip Helger
 */
public class MutationRandomExchange extends AbstractMutation
{
  public MutationRandomExchange (@Nonnull final IDecisionMaker aDescisionMaker)
  {
    super (aDescisionMaker);
  }

  @Override
  @Nonnull
  public IChromosome executeMutation (@Nonnull final IChromosome aChromosome)
  {
    final int nGenes = aChromosome.getGeneCount ();
    if (nGenes < 2)
    {
      // Avoid endless loop in index selection below
      // If only 1 element is present there is nothing to be exchanged
      throw new IllegalArgumentException ("You need to have at least 2 genes, but you only have " + nGenes + " genes!");
    }

    final int [] aIndices = RandomGenerator.getMultipleUniqueIntsInRange (2, nGenes);
    final int nIndex1 = aIndices[0];
    final int nIndex2 = aIndices[1];

    // Create a copy of the genes
    final List <IGene> aGenes = aChromosome.getAllGenes ();
    // And exchange gene 1 and 2
    final IGene aGene1 = aGenes.get (nIndex1);
    final IGene aGene2 = aGenes.get (nIndex2);
    aGenes.set (nIndex1, aGene2);
    aGenes.set (nIndex2, aGene1);
    // Return the new chromosome
    return new Chromosome (aChromosome, aGenes);
  }
}
