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
package com.helger.genetic.tsp.model;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.helger.genetic.model.IChromosome;
import com.helger.genetic.model.IFitnessFunction;
import com.helger.math.matrix.Matrix;

public final class TSPFitnessFunction implements IFitnessFunction
{
  private final double [][] m_aDistances;
  private final int m_nCities;
  private final double m_dWorstCaseDistance;

  public TSPFitnessFunction (@Nonnull final Matrix aDistances)
  {
    this (aDistances, 1.0);
  }

  /**
   * Full ctor
   *
   * @param aDistances
   *        Distance matrix
   * @param dScalingFactor
   *        The scaling factor for the worst case. This may be relevant when not
   *        searching for the optimum but something like 110% of the optimum. In
   *        edge cases it may be possible that the 110% is worse than the worst
   *        case and so the scaling factor 1.1 must be passed instead.
   */
  public TSPFitnessFunction (@Nonnull final Matrix aDistances, @Nonnegative final double dScalingFactor)
  {
    if (aDistances == null)
      throw new NullPointerException ("distances");
    if (!aDistances.isSymmetrical ())
      throw new IllegalArgumentException ("Matrix must be symmetrical!");

    m_aDistances = aDistances.internalGetArray ();
    m_nCities = m_aDistances.length;
    double dMax = 0;
    for (int nRow = 0; nRow < m_nCities; ++nRow)
      for (int nCol = nRow + 1; nCol < m_nCities; ++nCol)
      {
        final double dValue = m_aDistances[nRow][nCol];
        dMax = Math.max (dMax, dValue);
      }

    // For all cities and back to the start
    m_dWorstCaseDistance = dMax * (m_nCities + 1) * dScalingFactor;
  }

  public double getDistance (@Nonnull final IChromosome aChromosome)
  {
    return getDistance (aChromosome.getGeneIntArray ());
  }

  public double getDistance (@Nonnull final int [] aGenes)
  {
    double ret = 0;
    for (int i = 1; i < m_nCities; ++i)
      ret += m_aDistances[aGenes[i - 1]][aGenes[i]];
    // And back to the start point
    ret += m_aDistances[aGenes[m_nCities - 1]][aGenes[0]];
    return ret;
  }

  /**
   * Get the fitness from the distance
   *
   * @param dDistance
   *        Distance. Always &ge; 0.
   * @return The fitness - the higher the better
   */
  public double getFitness (@Nonnegative final double dDistance)
  {
    // The higher the fitness value the better - as we have a minimization
    // problem here, invert the value
    return m_dWorstCaseDistance - dDistance;
  }

  public double getFitness (@Nonnull final IChromosome aChromosome)
  {
    final double dDistance = getDistance (aChromosome);
    return getFitness (dDistance);
  }

  public double getWorstCaseDistance ()
  {
    return m_dWorstCaseDistance;
  }
}
