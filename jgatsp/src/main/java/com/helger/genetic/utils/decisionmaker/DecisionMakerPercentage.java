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

public class DecisionMakerPercentage extends AbstractDecisionMakerRandom
{
  private double m_dPercentage;

  public DecisionMakerPercentage (@Nonnegative final double dPercentage)
  {
    setPercentage (dPercentage);
  }

  /**
   * @return The current percentage value. Between 0 and 100.
   */
  public final double getPercentage ()
  {
    return m_dPercentage;
  }

  protected static boolean isValidPercentage (final double dPercentage)
  {
    return dPercentage >= 0 && dPercentage <= 100;
  }

  public final void setPercentage (@Nonnegative final double dPercentage)
  {
    if (!isValidPercentage (dPercentage))
      throw new IllegalArgumentException ("percentage is illegal: " + dPercentage);
    m_dPercentage = dPercentage;
  }

  @Override
  public boolean useRandomNumber (final double dRandom)
  {
    final boolean bUse = dRandom <= m_dPercentage;
    return bUse;
  }
}
