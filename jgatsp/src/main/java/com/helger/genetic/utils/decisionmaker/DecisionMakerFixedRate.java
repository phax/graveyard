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
package com.helger.genetic.utils.decisionmaker;

import javax.annotation.Nonnegative;

public final class DecisionMakerFixedRate implements IDecisionMaker
{
  private final int m_nEvery;
  private final int m_nOf;
  private int m_nCall = 0;

  /**
   * Constructor. With parameters (1, 3) it should be done at the 1st, 4th, 7th
   * etc. call. With parameters (1, 4) it should be done at the 1st, 5th, 9th
   * etc. call. With parameters (2, 4) it should be done at the 2nd, 6th, 10th
   * etc. call.
   * 
   * @param nEvery
   *        The index for which sthg. should be done
   * @param nOf
   *        The round to be done
   */
  public DecisionMakerFixedRate (@Nonnegative final int nEvery, @Nonnegative final int nOf)
  {
    if (nEvery <= 0)
      throw new IllegalArgumentException ("every may not be negative");
    if (nOf <= 0)
      throw new IllegalArgumentException ("of may not be negative");
    if (nEvery > nOf)
      throw new IllegalArgumentException ("every must be <= than of");
    // -1 because of modulo to work correctly
    m_nEvery = nEvery - 1;
    m_nOf = nOf;
  }

  public boolean shouldDoIt ()
  {
    final boolean bShouldDoIt = (m_nCall % m_nOf) == m_nEvery;
    m_nCall++;
    return bShouldDoIt;
  }
}
