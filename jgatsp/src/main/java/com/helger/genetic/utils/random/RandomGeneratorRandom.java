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

import java.util.BitSet;
import java.util.Random;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;

/**
 * Implementation of {@link IRandomGenerator} based on {@link java.util.Random}
 * 
 * @author Philip Helger
 */
public class RandomGeneratorRandom implements IRandomGenerator
{
  private final Random m_aRandom;

  public RandomGeneratorRandom (@Nonnull final Random aRandom)
  {
    if (aRandom == null)
      throw new NullPointerException ("random");
    m_aRandom = aRandom;
  }

  @Nonnegative
  public double getDouble ()
  {
    return m_aRandom.nextDouble ();
  }

  @Nonnegative
  public int getIntInRange (@Nonnegative final int nMaxValueExcl)
  {
    return m_aRandom.nextInt (nMaxValueExcl);
  }

  @Nonnull
  @Nonempty
  public int [] getMultipleUniqueIntsInRange (@Nonnegative final int nCount, @Nonnegative final int nMaxValueExcl)
  {
    if (nCount < 2)
      throw new IllegalArgumentException ("Must create at least 2 values!");
    if (nMaxValueExcl < nCount)
      throw new IllegalArgumentException ("Must create at least " + nCount + " different value!");

    // Select unique ints
    final BitSet aBitSet = new BitSet (nMaxValueExcl);
    while (aBitSet.cardinality () < nCount)
      aBitSet.set (m_aRandom.nextInt (nMaxValueExcl));

    // Get all selected bits
    final int [] ret = new int [nCount];
    int nRetIndex = 0;
    for (int i = aBitSet.nextSetBit (0); i >= 0; i = aBitSet.nextSetBit (i + 1))
      ret[nRetIndex++] = i;

    // consistency checks
    if (nRetIndex != nCount)
      throw new IllegalStateException ("Selection did not work. Having " + nRetIndex + " but expected " + nCount);
    return ret;
  }
}
