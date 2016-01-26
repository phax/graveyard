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

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.math.MathHelper;
import com.helger.genetic.model.Chromosome;
import com.helger.genetic.model.IChromosome;
import com.helger.genetic.model.IGene;
import com.helger.genetic.utils.decisionmaker.IDecisionMaker;
import com.helger.genetic.utils.random.RandomGenerator;

/**
 * Mutation that randomly reverses a range of genes. Original:
 * 
 * <pre>
 * 1 2 3 4 5 6 7
 *    +-----+
 * </pre>
 * 
 * After the mutation:
 * 
 * <pre>
 * 1 2 5 4 3 6 7
 *    +-----+
 * </pre>
 * 
 * Also called "Inversion".
 * 
 * @author Philip Helger
 */
public class MutationRandomPartialReverse extends AbstractMutation
{
  public MutationRandomPartialReverse (@Nonnull final IDecisionMaker aDescisionMaker)
  {
    super (aDescisionMaker);
  }

  @Override
  @Nonnull
  public IChromosome executeMutation (@Nonnull final IChromosome aChromosome)
  {
    final int nGenes = aChromosome.getGeneCount ();
    if (nGenes < 4)
    {
      // Avoid endless loop in index selection below
      // If only 3 elements are present ("a" "b" and "c") and index1 represents
      // "b" there exists no index2 which has a minimum distance of 2!
      throw new IllegalArgumentException ("You need to have at least 4 genes, but you only have " + nGenes + " genes!");
    }

    final int nIndex1 = RandomGenerator.getIntInRange (nGenes);
    int nIndex2;
    do
    {
      nIndex2 = RandomGenerator.getIntInRange (nGenes);
      // There must be at least 2 difference, so that sublist delivers at least
      // 2 elements
    } while (MathHelper.abs (nIndex1 - nIndex2) < 2);

    // Create a copy of all genes
    final List <IGene> aGenes = aChromosome.getAllGenes ();
    // Partially reverse stuff
    if (nIndex1 < nIndex2)
      Collections.reverse (aGenes.subList (nIndex1, nIndex2));
    else
      Collections.reverse (aGenes.subList (nIndex2, nIndex1));
    return new Chromosome (aChromosome, aGenes);
  }
}
