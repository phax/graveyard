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
package com.helger.genetic.selector;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.compare.ESortOrder;
import com.helger.genetic.model.ComparatorChromosomeFitness;
import com.helger.genetic.model.IChromosome;

/**
 * Cross over selector:
 * <ul>
 * <li>Take all chromosomes in best fitting order</li>
 * <li>Always choose the n best fitting chromosomes</li>
 * </ul>
 *
 * @author Philip Helger
 */
public class SelectorAllSortedBest extends AbstractSelector
{
  private final int m_nBestCount;

  public SelectorAllSortedBest (@Nonnegative final int nBestCount)
  {
    ValueEnforcer.isGT0 (nBestCount, "BestCount");
    m_nBestCount = nBestCount;
  }

  @Nonnull
  public List <IChromosome> selectSurvivingChromosomes (@Nonnull final List <IChromosome> aChromosomes)
  {
    // Sort all chromosomes by descending fitness
    final List <IChromosome> aSortedChromosome = CollectionHelper.getSortedInline (aChromosomes,
                                                                                   new ComparatorChromosomeFitness ().setSortOrder (ESortOrder.DESCENDING));

    final int nChromosomes = aChromosomes.size ();
    final List <IChromosome> ret = new ArrayList <IChromosome> (nChromosomes);
    for (int i = 0; i < nChromosomes; ++i)
      ret.add (aSortedChromosome.get (i % m_nBestCount));
    return ret;
  }
}
