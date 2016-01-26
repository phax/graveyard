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

import java.util.Arrays;
import java.util.BitSet;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.genetic.model.IChromosome;

/**
 * Crossover chromosome wrapper (CCW) implementation
 *
 * @author Philip Helger
 */
class CCW
{
  protected final int [] m_aOldGenes;
  private final int [] m_aNewGenes;
  private final BitSet m_aUsedNewValues;

  public CCW (@Nonnull final IChromosome aChromosome)
  {
    ValueEnforcer.notNull (aChromosome, "Chromosome");

    m_aOldGenes = aChromosome.getGeneIntArray ();
    m_aNewGenes = new int [m_aOldGenes.length];
    Arrays.fill (m_aNewGenes, -1);
    m_aUsedNewValues = new BitSet (m_aOldGenes.length);
  }

  @Nonnull
  @ReturnsMutableObject ("Design")
  public final int [] getAllOldGenes ()
  {
    // ESCA-JAVA0259:
    return m_aOldGenes;
  }

  @Nonnegative
  public final int getOldValue (@Nonnegative final int nIndex)
  {
    return m_aOldGenes[nIndex];
  }

  public void setNewValue (@Nonnegative final int nIndex, @Nonnegative final int nValue)
  {
    m_aNewGenes[nIndex] = nValue;
    m_aUsedNewValues.set (nValue);
  }

  public final boolean isNewValueUsed (@Nonnegative final int nValue)
  {
    return m_aUsedNewValues.get (nValue);
  }

  @Nonnegative
  public final int getNextUnusedNewValue (@Nonnegative final int nIndex)
  {
    return m_aUsedNewValues.nextClearBit (nIndex);
  }

  @Nonnegative
  public final int getNewGeneCount ()
  {
    return m_aUsedNewValues.cardinality ();
  }

  @Nonnull
  @ReturnsMutableObject ("Design")
  public final int [] getAllNewGenes ()
  {
    return m_aNewGenes;
  }
}
