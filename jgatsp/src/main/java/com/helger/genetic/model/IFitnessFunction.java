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
package com.helger.genetic.model;

import javax.annotation.Nonnull;

/**
 * Abstract fitness function declaration.
 * 
 * @author Philip Helger
 */
public interface IFitnessFunction
{
  /**
   * Calculate the fitness of a chromosome. A higher fitness always indicates a
   * better solution!
   * 
   * @param aChromosome
   *        The chromosome to calculate the fitness from. May not be
   *        <code>null</code>.
   * @return The fitness value. The higher the better
   */
  double getFitness (@Nonnull IChromosome aChromosome);
}
