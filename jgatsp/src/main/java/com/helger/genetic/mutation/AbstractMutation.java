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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.genetic.IMutation;
import com.helger.genetic.model.IChromosome;
import com.helger.genetic.utils.decisionmaker.IDecisionMaker;

public abstract class AbstractMutation implements IMutation
{
  private IDecisionMaker m_aDescisionMaker;
  private int m_nTryCount = 0;
  private int m_nExecutionCount = 0;

  public AbstractMutation (@Nonnull final IDecisionMaker aDecisionMaker)
  {
    setDecisionMaker (aDecisionMaker);
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

  @Nonnull
  public abstract IChromosome executeMutation (@Nonnull final IChromosome aChromosome);

  @Nonnull
  public List <IChromosome> mutate (@Nonnull final List <IChromosome> aChromosomes)
  {
    int nIndex = 0;
    for (final IChromosome aChromosome : aChromosomes)
    {
      m_nTryCount++;
      if (m_aDescisionMaker.shouldDoIt ())
      {
        m_nExecutionCount++;
        final IChromosome aMutatedChromosome = executeMutation (aChromosome);
        aChromosomes.set (nIndex, aMutatedChromosome);
      }
      ++nIndex;
    }
    return aChromosomes;
  }
}
