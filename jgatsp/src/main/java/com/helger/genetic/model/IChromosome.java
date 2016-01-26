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
import javax.annotation.Nullable;

import com.helger.commons.annotation.MustImplementEqualsAndHashcode;
import com.helger.commons.annotation.ReturnsMutableCopy;

/**
 * Unmodifiable collection of {@link IGene}. The fitness function should be
 * called only once per instance, so the object should be immutable.<br>
 * Assumption: chromosome is ordered!
 * 
 * @author Philip Helger
 */
@MustImplementEqualsAndHashcode
public interface IChromosome
{
  /**
   * @return The number of contained genes. Always &ge; 0.
   */
  @Nonnegative
  int getGeneCount ();

  /**
   * Get the gene at the specified index.
   * 
   * @param nIndex
   *        The index to use. Must be &ge; 0.
   * @return The gene at the specified index
   * @throws ArrayIndexOutOfBoundsException
   *         in case the index is invalid
   */
  @Nonnull
  IGene getGene (@Nonnegative int nIndex);

  /**
   * @return A new list with all contained genes
   */
  @Nonnull
  @ReturnsMutableCopy
  List <IGene> getAllGenes ();

  /**
   * @return A new array with all contained genes
   */
  @Nonnull
  @ReturnsMutableCopy
  IGene [] getGeneArray ();

  /**
   * @return A new array with all contained gene values as int.
   */
  @Nonnull
  @ReturnsMutableCopy
  int [] getGeneIntArray ();

  /**
   * @return A new array with all contained gene values as double.
   */
  @Nonnull
  @ReturnsMutableCopy
  double [] getGeneDoubleArray ();

  /**
   * @return The fitness function to be used to calculate the fitness.
   */
  @Nonnull
  IFitnessFunction getFitnessFunction ();

  /**
   * @return The fitness of this chromosome. The higher, the better.
   */
  @Nonnull
  Double getFitnessObj ();

  /**
   * @return The fitness of this chromosome. The higher, the better.
   */
  double getFitness ();

  /**
   * @param aChromosome
   *        Chromosome to compare to
   * @return <code>true</code> if this chromosome is fitter than the passed
   *         chromosome
   */
  boolean isFitterThan (@Nonnull IChromosome aChromosome);

  /**
   * @return The chromosome validator to be used. May be <code>null</code>.
   */
  @Nullable
  IChromsomeValidator getValidator ();

  /**
   * @return <code>true</code> if the chromosome is valid or if no validator is
   *         defined.
   */
  boolean isValid ();
}
