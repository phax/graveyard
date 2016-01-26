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
package com.helger.genetic.tsp.result.specific;

import java.util.Random;

import com.helger.commons.io.resource.ClassPathResource;
import com.helger.genetic.tsp.AbstractFileBasedTSPRunner;
import com.helger.genetic.tsp.TSPRunner;
import com.helger.genetic.utils.random.RandomGenerator;
import com.helger.genetic.utils.random.RandomGeneratorRandom;
import com.helger.math.matrix.Matrix;

public final class MainTSPRunnerBrd14051 extends AbstractFileBasedTSPRunner
{
  public static void main (final String [] args)
  {
    // Use fixed seed
    RandomGenerator.setRandomGenerator (new RandomGeneratorRandom (new Random (471147124713L)));
    final Matrix m = readTSPFromFile (new ClassPathResource ("tsp/brd14051.tsp"), true);
    new TSPRunner ("brd14051").runWithDefaultSettings (m, 469385);
  }
}
