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
package com.helger.genetic.eventhandler;

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.CGlobal;
import com.helger.commons.annotation.OverrideOnDemand;
import com.helger.genetic.IEventHandler;
import com.helger.genetic.model.IChromosome;
import com.helger.genetic.model.IPopulation;

public class EventHandlerDefault implements IEventHandler
{
  private final IEventHandler m_aNestedEventHandler;
  private IPopulation m_aLastPopulation;
  private IChromosome m_aFittestChromosome;

  public EventHandlerDefault ()
  {
    this ((IEventHandler) null);
  }

  public EventHandlerDefault (@Nullable final IEventHandler aNestedEventHandler)
  {
    m_aNestedEventHandler = aNestedEventHandler;
  }

  /**
   * @param aPopulation
   *        The new population
   */
  @OverrideOnDemand
  protected void internalOnNewPopulation (@SuppressWarnings ("unused") @Nonnull final IPopulation aPopulation)
  {}

  public final void onNewPopulation (@Nonnull final IPopulation aPopulation)
  {
    m_aLastPopulation = aPopulation;
    internalOnNewPopulation (aPopulation);
    if (m_aNestedEventHandler != null)
      m_aNestedEventHandler.onNewPopulation (aPopulation);
  }

  @Nullable
  public final IPopulation getLastPopulation ()
  {
    return m_aLastPopulation;
  }

  @CheckForSigned
  public final long getLastGeneration ()
  {
    return m_aLastPopulation == null ? CGlobal.ILLEGAL_ULONG : m_aLastPopulation.getGeneration ();
  }

  /**
   * Called when a new overall fittest chromosome was detected
   * 
   * @param aCurrentFittest
   *        The currently fittest chromosome. Never <code>null</code>.
   */
  @OverrideOnDemand
  protected void internalOnNewFittestChromosome (@SuppressWarnings ("unused") @Nonnull final IChromosome aCurrentFittest)
  {}

  public final void onNewFittestChromosome (@Nonnull final IChromosome aCurrentFittest)
  {
    m_aFittestChromosome = aCurrentFittest;
    internalOnNewFittestChromosome (aCurrentFittest);
    if (m_aNestedEventHandler != null)
      m_aNestedEventHandler.onNewFittestChromosome (aCurrentFittest);
  }

  @Nullable
  public final IChromosome getFittestChromosome ()
  {
    return m_aFittestChromosome;
  }
}
