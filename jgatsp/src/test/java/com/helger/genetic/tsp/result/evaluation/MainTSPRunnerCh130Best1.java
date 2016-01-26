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
package com.helger.genetic.tsp.result.evaluation;

import java.util.Random;

import com.helger.commons.debug.GlobalDebug;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.genetic.IContinuation;
import com.helger.genetic.ICrossover;
import com.helger.genetic.IEventHandler;
import com.helger.genetic.IMutation;
import com.helger.genetic.IPopulationCreator;
import com.helger.genetic.ISelector;
import com.helger.genetic.continuation.ContinuationKnownOptimum;
import com.helger.genetic.crossover.CrossoverPartiallyMapped;
import com.helger.genetic.eventhandler.EventHandlerDefault;
import com.helger.genetic.mutation.MutationRandomPartialReverse;
import com.helger.genetic.selector.SelectorAllSortedBest;
import com.helger.genetic.tsp.AbstractFileBasedTSPRunner;
import com.helger.genetic.tsp.TSPRunner;
import com.helger.genetic.tsp.eventhandler.TSPEventHandlerLogging;
import com.helger.genetic.tsp.model.TSPChromosomeValidator;
import com.helger.genetic.tsp.model.TSPFitnessFunction;
import com.helger.genetic.tsp.populationcreator.TSPPopulationCreatorRandom;
import com.helger.genetic.utils.decisionmaker.DecisionMakerPercentage;
import com.helger.genetic.utils.random.RandomGenerator;
import com.helger.genetic.utils.random.RandomGeneratorRandom;
import com.helger.math.matrix.Matrix;

/**
 * Best so far:
 *
 * <pre>
 * SelectorAllSortedBest(2); new DecisionMakerPercentage (0.0); new DecisionMakerPercentage (86.3); nPopulationSize=38
 * </pre>
 *
 * @author Philip Helger
 */
public final class MainTSPRunnerCh130Best1 extends AbstractFileBasedTSPRunner
{
  public static void main (final String [] args)
  {
    GlobalDebug.setDebugModeDirect (true);
    // Optimum length = 6110
    final Matrix aDistances = readTSPFromFile (new ClassPathResource ("tsp/ch130.tsp"), true);

    final double nOptimumDistance = 6110;
    final int nCities = aDistances.getRowDimension ();
    final TSPFitnessFunction ff = new TSPFitnessFunction (aDistances);
    final TSPChromosomeValidator cv = true ? null : new TSPChromosomeValidator (nCities);

    // Use fixed seed
    RandomGenerator.setRandomGenerator (new RandomGeneratorRandom (new Random (471147124713L)));
    final int nPopulationSize = 38;
    final IEventHandler eh = true ? new EventHandlerDefault () : new TSPEventHandlerLogging ();
    final IContinuation cont = new ContinuationKnownOptimum (ff.getFitness (nOptimumDistance), eh);
    final IPopulationCreator pc = new TSPPopulationCreatorRandom (nCities, nPopulationSize, ff, cv);
    final ISelector s = new SelectorAllSortedBest (2);
    final ICrossover c = new CrossoverPartiallyMapped (new DecisionMakerPercentage (0.0));
    final IMutation m = new MutationRandomPartialReverse (new DecisionMakerPercentage (86.3));

    new TSPRunner ("ch130").run (aDistances, nOptimumDistance, ff, eh, cont, pc, s, c, m);
  }
}
