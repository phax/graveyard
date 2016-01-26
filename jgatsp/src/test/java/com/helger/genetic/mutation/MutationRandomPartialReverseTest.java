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
package com.helger.genetic.mutation;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.genetic.model.Chromosome;
import com.helger.genetic.model.IChromosome;
import com.helger.genetic.model.IFitnessFunction;
import com.helger.genetic.model.MockFitnessFunction;
import com.helger.genetic.utils.decisionmaker.DecisionMakerPercentage;

public final class MutationRandomPartialReverseTest
{
  @Test
  public void testBasic ()
  {
    final IFitnessFunction ff = new MockFitnessFunction ();
    IChromosome cOld = Chromosome.createGenesInt (ff, null, 1, 2, 3, 4, 5, 6, 7, 8);
    final MutationRandomPartialReverse aMRE = new MutationRandomPartialReverse (new DecisionMakerPercentage (100));
    for (int i = 1; i <= 2000; ++i)
    {
      final IChromosome cNew = aMRE.executeMutation (cOld);
      assertTrue ("No change: " + cOld.toString (), !cOld.equals (cNew));
      cOld = cNew;
    }
  }
}
