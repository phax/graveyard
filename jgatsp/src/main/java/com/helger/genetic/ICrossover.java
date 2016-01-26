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
package com.helger.genetic;

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.helger.genetic.model.IChromosome;

public interface ICrossover extends IDecidingAction
{
  /**
   * @return The number of chromosomes, for which the crossover is used.
   */
  @Nonnegative
  int getCrossoverChromosomeCount ();

  /**
   * Perform a crossover with a certain percentage on certain chromosomes within
   * the population.
   *
   * @param aChromosomes
   *        The chromosomes in which the crossover should be performed.
   * @return The crossed-over chromosomes
   */
  @Nonnull
  List <IChromosome> crossover (@Nonnull List <IChromosome> aChromosomes);
}
