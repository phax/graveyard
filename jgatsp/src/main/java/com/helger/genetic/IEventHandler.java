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

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.genetic.model.IChromosome;
import com.helger.genetic.model.IPopulation;

public interface IEventHandler
{
  /**
   * Called whenever a new population is being processed. This method is always
   * called before the fittest chromosome is determined within this population!
   * 
   * @param aPopulation
   *        The new population. Never <code>null</code>.
   */
  void onNewPopulation (@Nonnull IPopulation aPopulation);

  /**
   * @return The last population known so far. May be <code>null</code> if none
   *         was processed.
   */
  @Nullable
  IPopulation getLastPopulation ();

  /**
   * @return The last handled population number of -1 in case no population was
   *         handled so far.
   */
  @CheckForSigned
  long getLastGeneration ();

  /**
   * Called when a new overall fittest chromosome was detected
   * 
   * @param aCurrentFittest
   *        The currently fittest chromosome. Never <code>null</code>.
   */
  void onNewFittestChromosome (@Nonnull IChromosome aCurrentFittest);

  /**
   * @return The current fittest chromosome or <code>null</code> if no such
   *         chromosome was determined yet.
   */
  @Nullable
  IChromosome getFittestChromosome ();
}
