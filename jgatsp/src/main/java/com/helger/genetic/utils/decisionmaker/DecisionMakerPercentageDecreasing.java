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

import com.helger.commons.annotation.OverrideOnDemand;

/**
 * A percentage based decision maker with decreasing percentages.
 * 
 * @author Philip Helger
 */
public class DecisionMakerPercentageDecreasing extends DecisionMakerPercentage
{
  private final double m_dMinPercentage;
  private final double m_dDeltaPercentage;
  private long m_nStep = 0;
  private final long m_nChangeStep;

  public DecisionMakerPercentageDecreasing (@Nonnegative final double dInitialPercentage,
                                            @Nonnegative final double dMinPercentage,
                                            @Nonnegative final double dDeltaPercentage,
                                            @Nonnegative final long nChangeStep)
  {
    super (dInitialPercentage);
    if (!isValidPercentage (dMinPercentage) || dMinPercentage > dInitialPercentage)
      throw new IllegalArgumentException ("Min percentage is illegal: " + dMinPercentage);
    if (!isValidPercentage (dDeltaPercentage))
      throw new IllegalArgumentException ("Delta percentage is illegal: " + dDeltaPercentage);
    if (nChangeStep <= 0)
      throw new IllegalArgumentException ("Step is invalid: " + nChangeStep);
    m_dMinPercentage = dMinPercentage;
    m_dDeltaPercentage = dDeltaPercentage;
    m_nChangeStep = nChangeStep;
  }

  @Nonnegative
  public final double getMinPercentage ()
  {
    return m_dMinPercentage;
  }

  @Nonnegative
  public final double getDeltaPercentage ()
  {
    return m_dDeltaPercentage;
  }

  @Nonnegative
  public final long getChangeStep ()
  {
    return m_nChangeStep;
  }

  @OverrideOnDemand
  protected void onPercentageChange ()
  {}

  @Override
  public boolean useRandomNumber (final double dRandom)
  {
    if ((++m_nStep % m_nChangeStep) == 0)
    {
      final double dCurPerc = getPercentage ();
      if (dCurPerc > m_dMinPercentage)
      {
        final double dNewPerc = Math.max (dCurPerc - m_dDeltaPercentage, m_dMinPercentage);
        setPercentage (dNewPerc);
        onPercentageChange ();
      }
    }

    return super.useRandomNumber (dRandom);
  }
}
