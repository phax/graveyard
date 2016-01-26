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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.genetic.IContinuation;
import com.helger.genetic.model.IPopulation;

/**
 * Run infinitely!
 * 
 * @author Philip Helger
 */
public class ContinuationInfinite extends AbstractContinuation
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ContinuationInfinite.class);

  public ContinuationInfinite ()
  {
    this (null);
  }

  public ContinuationInfinite (@Nullable final IContinuation aNestedGACallback)
  {
    super (aNestedGACallback);
  }

  @Override
  protected void internalOnStart ()
  {
    s_aLogger.info ("Running algorithm for infinite time!");
  }

  @Override
  protected boolean internalShouldContinue (@Nonnull final IPopulation aPopulation)
  {
    return true;
  }
}
