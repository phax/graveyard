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

import java.util.List;

import javax.annotation.Nonnull;

import com.helger.genetic.IEventHandler;
import com.helger.genetic.model.IChromosome;
import com.helger.genetic.mutation.AbstractMutation;
import com.helger.genetic.utils.decisionmaker.DecisionMakerAlways;
import com.helger.math.matrix.Matrix;

/**
 * A mutation that always performs a greedy optimization for the first 20
 * generations, and than uses the supplied mutation.
 *
 * @author Philip Helger
 */
public class TSPMutationGreedyBeginning extends AbstractMutation
{
  private final IEventHandler m_aEventHandler;
  private final TSPMutationGreedy m_aFirst;
  private final AbstractMutation m_aLast;
  private boolean m_bGreedyMode = true;

  public TSPMutationGreedyBeginning (@Nonnull final Matrix aDistanceMatrix,
                                     @Nonnull final IEventHandler aEventHandler,
                                     @Nonnull final AbstractMutation aMutation)
  {
    super (DecisionMakerAlways.getInstance ());
    if (aDistanceMatrix == null)
      throw new NullPointerException ("DistanceMatrix");
    if (aEventHandler == null)
      throw new NullPointerException ("EventHandler");
    if (aMutation == null)
      throw new NullPointerException ("Mutation");

    m_aEventHandler = aEventHandler;
    m_aFirst = new TSPMutationGreedy (DecisionMakerAlways.getInstance (), aDistanceMatrix);
    m_aLast = aMutation;
  }

  @Override
  @Nonnull
  public IChromosome executeMutation (@Nonnull final IChromosome aChromosome)
  {
    return m_bGreedyMode ? m_aFirst.executeMutation (aChromosome) : m_aLast.executeMutation (aChromosome);
  }

  @Override
  @Nonnull
  public List <IChromosome> mutate (@Nonnull final List <IChromosome> aChromosomes)
  {
    if (m_bGreedyMode && m_aEventHandler.getLastGeneration () >= 15)
    {
      m_bGreedyMode = false;
      // Set the new decision maker
      setDecisionMaker (m_aLast.getDecisionMaker ());
    }

    return super.mutate (aChromosomes);
  }
}
