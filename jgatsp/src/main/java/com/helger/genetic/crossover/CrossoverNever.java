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
import com.helger.genetic.model.IChromosome;
import com.helger.genetic.utils.decisionmaker.DecisionMakerNever;

public final class CrossoverNever extends AbstractCrossover
{
  private static final CrossoverNever s_aInstance = new CrossoverNever ();

  private CrossoverNever ()
  {
    super (2, DecisionMakerNever.getInstance ());
  }

  @Nonnull
  public static CrossoverNever getInstance ()
  {
    return s_aInstance;
  }

  @Override
  @UnsupportedOperation
  public IChromosome [] executeCrossover (@Nonnull @Nonempty final IChromosome [] aChromosomes)
  {
    throw new UnsupportedOperationException ();
  }
}
