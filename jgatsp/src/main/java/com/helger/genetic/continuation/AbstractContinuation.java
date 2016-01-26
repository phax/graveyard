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
package com.helger.genetic.continuation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.OverrideOnDemand;
import com.helger.genetic.IContinuation;
import com.helger.genetic.model.IPopulation;

public abstract class AbstractContinuation implements IContinuation
{
  private final IContinuation m_aNestedGACallback;

  public AbstractContinuation (@Nullable final IContinuation aNestedGACallback)
  {
    m_aNestedGACallback = aNestedGACallback;
  }

  /**
   * Called when algorithm starts running.
   */
  @OverrideOnDemand
  protected void internalOnStart ()
  {}

  public final void onStart ()
  {
    internalOnStart ();
    if (m_aNestedGACallback != null)
      m_aNestedGACallback.onStart ();
  }

  /**
   * @param aPopulation
   *        The last created population for which the algorithm will be
   *        re-applied
   * @return <code>true</code> if the algorithm should continue,
   *         <code>false</code> if it should stop immediately
   */
  @OverrideOnDemand
  protected boolean internalShouldContinue (@SuppressWarnings ("unused") @Nonnull final IPopulation aPopulation)
  {
    return true;
  }

  public final boolean shouldContinue (@Nonnull final IPopulation aPopulation)
  {
    if (!internalShouldContinue (aPopulation))
      return false;
    if (m_aNestedGACallback != null)
      return m_aNestedGACallback.shouldContinue (aPopulation);
    // No nested callback present
    return true;
  }
}
