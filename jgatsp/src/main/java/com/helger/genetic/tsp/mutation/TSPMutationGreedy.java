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
package com.helger.genetic.tsp.mutation;

import java.util.BitSet;
import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.math.MathHelper;
import com.helger.genetic.model.Chromosome;
import com.helger.genetic.model.GeneInt;
import com.helger.genetic.model.IChromosome;
import com.helger.genetic.model.IGene;
import com.helger.genetic.mutation.AbstractMutation;
import com.helger.genetic.utils.decisionmaker.IDecisionMaker;
import com.helger.genetic.utils.random.RandomGenerator;
import com.helger.math.matrix.Matrix;

/**
 * Mutation that sorts a sub-sequence with the greedy TSP algorithm:
 *
 * @author Philip Helger
 */
public class TSPMutationGreedy extends AbstractMutation
{
  private final Matrix m_aDistanceMatrix;

  public TSPMutationGreedy (@Nonnull final IDecisionMaker aDescisionMaker, @Nonnull final Matrix aDistanceMatrix)
  {
    super (aDescisionMaker);
    if (aDistanceMatrix == null)
      throw new NullPointerException ("DistanceMatrix");
    m_aDistanceMatrix = aDistanceMatrix;
  }

  @Nonnull
  @Nonempty
  public static int [] getGreedyOrder (@Nonnull final Matrix aDistanceMatrix)
  {
    final double [][] aDistances = aDistanceMatrix.internalGetArray ();
    final int nCities = aDistanceMatrix.getColumnDimension ();
    final int [] ret = new int [nCities];
    final BitSet aUsedCities = new BitSet (nCities);
    int nUsedCities = 0;
    int nCurrentCity = 0;
    while (true)
    {
      aUsedCities.set (nCurrentCity);
      ret[nUsedCities++] = nCurrentCity;
      if (nUsedCities == nCities)
        break;

      double dMinimum = Double.MAX_VALUE;
      int nMinimumRow = -1;
      for (int nRow = 0; nRow < nCities; ++nRow)
        if (!aUsedCities.get (nRow))
        {
          final double dDistance = aDistances[nRow][nCurrentCity];
          if (dDistance < dMinimum)
          {
            dMinimum = dDistance;
            nMinimumRow = nRow;
          }
        }

      nCurrentCity = nMinimumRow;
    }
    return ret;
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
      // There must be at least 2 difference, so that sublist delivers at
      // least 2 elements
    } while (MathHelper.abs (nIndex1 - nIndex2) < 2);

    // Create a copy of all genes
    final List <IGene> aGenes = aChromosome.getAllGenes ();
    final int nStartIndex = Math.min (nIndex1, nIndex2);
    final int nEndIndex = Math.max (nIndex1, nIndex2);
    // Get sublist
    final int [] aSelectedIndices = new int [nEndIndex - nStartIndex];
    for (int i = 0; i < aSelectedIndices.length; ++i)
      aSelectedIndices[i] = aGenes.get (nStartIndex + i).intValue ();

    final int [] aOrderedIndices = getGreedyOrder (m_aDistanceMatrix.getMatrix (aSelectedIndices, aSelectedIndices));

    for (int i = 0; i < aOrderedIndices.length; ++i)
      aGenes.set (nStartIndex + i, new GeneInt (aSelectedIndices[aOrderedIndices[i]]));

    return new Chromosome (aChromosome, aGenes);
  }
}
