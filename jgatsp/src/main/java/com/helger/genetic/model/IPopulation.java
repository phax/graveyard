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

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.annotation.ReturnsMutableObject;

public interface IPopulation
{
  /**
   * @return The generation. The initial generation has value 0.
   */
  @Nonnegative
  long getGeneration ();

  @Nonnegative
  int getChromosomeCount ();

  @Nonnull
  IChromosome getChromosome (@Nonnegative int nIndex);

  @Nonnull
  @ReturnsMutableObject ("speed")
  List <IChromosome> directGetAllChromosomes ();

  @Nonnull
  @ReturnsMutableCopy
  List <IChromosome> getAllChromosomes ();

  @Nonnull
  @ReturnsMutableCopy
  IChromosome [] getChromosomeArray ();

  @Nonnull
  IChromosome getFittestChromosome ();
}
