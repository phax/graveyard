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
package com.helger.genetic.utils.random;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;

public interface IRandomGenerator
{
  /**
   * @return A random double value between 0 and 1
   */
  @Nonnegative
  double getDouble ();

  /**
   * Get a single random int value in the range from [0 - max_value[
   * 
   * @param nMaxValueExcl
   *        The maximum value (exclusive) to be created
   * @return The random value
   */
  @Nonnegative
  int getIntInRange (@Nonnegative int nMaxValueExcl);

  /**
   * Get multiple unique random int values in a range from [0 - max_value[
   * 
   * @param nCount
   *        Number of random values to create
   * @param nMaxValueExcl
   *        The maximum value (exclusive) to be created
   * @return The int array, in value ascending order. Never <code>null</code>.
   */
  @Nonnull
  @Nonempty
  int [] getMultipleUniqueIntsInRange (@Nonnegative int nCount, @Nonnegative int nMaxValueExcl);
}
