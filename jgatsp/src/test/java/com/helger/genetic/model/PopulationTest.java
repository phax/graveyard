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
package com.helger.genetic.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test class for class {@link Population}.
 * 
 * @author Philip Helger
 */
public final class PopulationTest
{
  @Test
  public void testBasic ()
  {
    final Population p = new Population (0);
    assertEquals (0, p.getGeneration ());
    assertEquals (0, p.getChromosomeCount ());

    final IFitnessFunction ff = new MockFitnessFunction ();
    final Chromosome c = Chromosome.createGenesInt (ff, null, 0, 1);
    p.addChromosome (c);
    assertEquals (1, p.getChromosomeCount ());
  }
}
