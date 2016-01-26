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

import java.util.BitSet;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.UnsupportedOperation;
import com.helger.genetic.model.Chromosome;
import com.helger.genetic.model.IChromosome;
import com.helger.genetic.utils.decisionmaker.IDecisionMaker;

/**
 * Edge Recombination Crossover Operator. <a href=
 * "http://www.rubicite.com/Tutorials/GeneticAlgorithms/CrossoverOperators/EdgeRecombinationCrossoverOperator.aspx"
 * >Source</a> and <a
 * href="http://en.wikipedia.org/wiki/Edge_recombination_operator">Source</a> <br>
 * Crossover for 2 int-chromosomes, having unique genes only. This is a
 * crossover techniques for permutation (ordered) chromosomes. It strives to
 * introduce the fewest paths possible. In problems such as the travelling
 * salesman, introducing a stray edge between two nodes is usually very bad for
 * a chromosome's fitness. The idea here is to use as many existing edges, or
 * node-connections, as possible to generate children. Edge Recombination
 * typically out performs PMX and Ordered crossover, but usually takes longer to
 * compute. The Algorithm:
 * 
 * <pre>
 * Parent 1: A B F E D G C
 * Parent 2: G F A B C D E
 * 
 * Generate Neighbor List:
 * 
 * A: B C F
 * B: A F C
 * C: A G B D
 * D: E G C E
 * E: F D G
 * F: B E G A
 * G: D C E F
 * 
 * CHILD = Empty Chromosome
 * 
 * 1. X = the first node from a random parent. 
 * 2. While the CHILD chromosome isn't full, Loop:
 *    - Append X to CHILD
 *    - Remove X from Neighbor Lists
 * 
 *    if X's neighbor list is empty:
 *       - Z = random node not already in CHILD
 *    else
 *       - Determine neighbor of X that has fewest neighbors
 *       - If there is a tie, randomly choose 1
 *       - Z = chosen node
 *    X = Z
 * </pre>
 * 
 * @author Philip Helger
 */
public class CrossoverEdgeRecombination extends AbstractCrossover
{
  public CrossoverEdgeRecombination (@Nonnull final IDecisionMaker aDescisionMaker)
  {
    super (2, aDescisionMaker);
  }

  @Override
  @UnsupportedOperation
  public IChromosome [] executeCrossover (@Nonnull @Nonempty final IChromosome [] aChromosomes)
  {
    final int nGenes = aChromosomes[0].getGeneCount ();
    final int nLastIdx = nGenes - 1;

    final CCW aGenes0 = new CCW (aChromosomes[0]);
    final CCW aGenes1 = new CCW (aChromosomes[1]);

    // Init adjacency matrices
    final BitSet [] aAdjacencyMatrices = new BitSet [nGenes];
    for (int i = 0; i < nGenes; ++i)
      aAdjacencyMatrices[i] = new BitSet (nGenes);

    // Fill adjacency matrices
    {
      final int nFirst = aGenes0.getOldValue (0);
      final int nLast = aGenes0.getOldValue (nLastIdx);
      int nPrev = nLast;
      int nCur = nFirst;
      for (int i = 1; i < nGenes; ++i)
      {
        final int nNext = aGenes0.getOldValue (i);
        aAdjacencyMatrices[nCur].set (nPrev);
        aAdjacencyMatrices[nCur].set (nNext);
        nPrev = nCur;
        nCur = nNext;
      }
      aAdjacencyMatrices[nCur].set (nPrev);
      aAdjacencyMatrices[nCur].set (nFirst);
    }
    {
      final int nFirst = aGenes1.getOldValue (0);
      final int nLast = aGenes1.getOldValue (nLastIdx);
      int nPrev = nLast;
      int nCur = nFirst;
      for (int i = 1; i < nGenes; ++i)
      {
        final int nNext = aGenes1.getOldValue (i);
        aAdjacencyMatrices[nCur].set (nPrev);
        aAdjacencyMatrices[nCur].set (nNext);
        nPrev = nCur;
        nCur = nNext;
      }
      aAdjacencyMatrices[nCur].set (nPrev);
      aAdjacencyMatrices[nCur].set (nFirst);
    }

    int nDstIndex = 0;
    // Our x
    int nCurValue = aGenes0.getOldValue (nDstIndex);
    while (nDstIndex < nGenes)
    {
      // Append x
      aGenes0.setNewValue (nDstIndex, nCurValue);

      // remove x from neighbor list
      for (final BitSet aAdjacencyMatrice : aAdjacencyMatrices)
        aAdjacencyMatrice.set (nCurValue, false);

      // Get neighbors of x
      final BitSet aNeighbors = aAdjacencyMatrices[nCurValue];
      // Our z
      int nNextValue;
      if (aNeighbors.cardinality () == 0)
      {
        // No neighbors present
        nNextValue = aGenes0.getNextUnusedNewValue (0);
      }
      else
      {
        // Determine neighbor of x that has fewest neighbors
        int nShortestCount = Integer.MAX_VALUE;
        int nShortestIndex = -1;
        for (int i = aNeighbors.nextSetBit (0); i >= 0; i = aNeighbors.nextSetBit (i + 1))
        {
          final BitSet aBitSet = aAdjacencyMatrices[i];
          // BitSet had x as neighbor
          final int nCount = aBitSet.cardinality ();
          if (nCount < nShortestCount)
          {
            nShortestCount = nCount;
            nShortestIndex = i;
          }
        }
        nNextValue = nShortestIndex;
      }
      // x = z
      nCurValue = nNextValue;
      nDstIndex++;
    }

    final IChromosome aNew0 = Chromosome.createGenesInt (aChromosomes[0], aGenes0.getAllNewGenes ());
    final IChromosome aNew1 = Chromosome.createGenesInt (aChromosomes[1], aGenes0.getAllNewGenes ());
    return new IChromosome [] { aNew0, aNew1 };
  }
}
