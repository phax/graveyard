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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.genetic.ICrossover;
import com.helger.genetic.model.IChromosome;
import com.helger.genetic.utils.decisionmaker.IDecisionMaker;

public abstract class AbstractCrossover implements ICrossover
{
  private final int m_nCrossoverChromosomeCount;
  private IDecisionMaker m_aDescisionMaker;
  private int m_nTryCount = 0;
  private int m_nExecutionCount = 0;

  public AbstractCrossover (@Nonnegative final int nCrossoverChromosomeCount,
                            @Nonnull final IDecisionMaker aDescisionMaker)
  {
    if (nCrossoverChromosomeCount < 2)
      throw new IllegalArgumentException ("At least 2 chromosomes are required for a crossover (" +
                                          nCrossoverChromosomeCount +
                                          ")!");
    m_nCrossoverChromosomeCount = nCrossoverChromosomeCount;
    setDecisionMaker (aDescisionMaker);
  }

  @Nonnegative
  public final int getCrossoverChromosomeCount ()
  {
    return m_nCrossoverChromosomeCount;
  }

  @Nonnull
  public final IDecisionMaker getDecisionMaker ()
  {
    return m_aDescisionMaker;
  }

  public final void setDecisionMaker (@Nonnull final IDecisionMaker aDecisionMaker)
  {
    ValueEnforcer.notNull (aDecisionMaker, "DecisionMaker");
    m_aDescisionMaker = aDecisionMaker;
  }

  @Nonnegative
  public final int getTryCount ()
  {
    return m_nTryCount;
  }

  @Nonnegative
  public final int getExecutionCount ()
  {
    return m_nExecutionCount;
  }

  /**
   * Execute the crossover on the passed chromosomes.
   *
   * @param aChromosomes
   *        The chromosomes to crossover. The length of the array equals the
   *        result of {@link #getExecutionCount()}.
   * @return The modified chromosomes. The array must have the same length as
   *         the input array!
   */
  @Nonnull
  @Nonempty
  public abstract IChromosome [] executeCrossover (@Nonnull @Nonempty final IChromosome [] aChromosomes);

  @Nonnull
  @Nonempty
  public final List <IChromosome> crossover (@Nonnull final List <IChromosome> aChromosomes)
  {
    m_nTryCount++;
    if (!m_aDescisionMaker.shouldDoIt ())
    {
      // Return unchanged
      return aChromosomes;
    }

    m_nExecutionCount++;

    final int nChromosomes = aChromosomes.size ();
    final IChromosome [] aSelected = new IChromosome [m_nCrossoverChromosomeCount];
    final List <IChromosome> ret = new ArrayList <IChromosome> (nChromosomes);

    int nTotalIndex = 0;
    outer: while (true)
    {
      for (int i = 0; i < m_nCrossoverChromosomeCount; ++i)
        aSelected[i] = aChromosomes.get (nTotalIndex++ % nChromosomes);
      final IChromosome [] aNewOnes = executeCrossover (aSelected);
      for (int i = 0; i < m_nCrossoverChromosomeCount; ++i)
      {
        ret.add (aNewOnes[i]);
        if (ret.size () >= nChromosomes)
          break outer;
      }
    }
    return ret;
  }
}
