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

import java.util.BitSet;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.helger.genetic.model.IChromosome;

/**
 * Special CCW with additional index map. Separated to save memory in cases
 * where the index is not needed.
 * 
 * @author Philip Helger
 */
final class CCWWithIndex extends CCW
{
  private final BitSet m_aUsedNewInidices;
  private final int [] m_aOldGenesValueToIndex;

  public CCWWithIndex (@Nonnull final IChromosome aChromosome)
  {
    super (aChromosome);

    final int nGeneCount = aChromosome.getGeneCount ();
    m_aUsedNewInidices = new BitSet ();

    // Create map from value to index of old genes
    m_aOldGenesValueToIndex = new int [nGeneCount];
    for (int i = 0; i < nGeneCount; ++i)
      m_aOldGenesValueToIndex[m_aOldGenes[i]] = i;
  }

  @Nonnegative
  public int getIndexOfOldValue (@Nonnegative final int nValue)
  {
    return m_aOldGenesValueToIndex[nValue];
  }

  @Override
  public void setNewValue (@Nonnegative final int nIndex, @Nonnegative final int nValue)
  {
    super.setNewValue (nIndex, nValue);
    m_aUsedNewInidices.set (nIndex);
  }

  public boolean isNewIndexUsed (@Nonnegative final int nIndex)
  {
    return m_aUsedNewInidices.get (nIndex);
  }

  public int getNextNewUnusedIndex (@Nonnegative final int nIndex)
  {
    return m_aUsedNewInidices.nextClearBit (nIndex);
  }
}
