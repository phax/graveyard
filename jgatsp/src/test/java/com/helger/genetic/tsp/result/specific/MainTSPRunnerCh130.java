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

import com.helger.commons.CGlobal;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.genetic.IContinuation;
import com.helger.genetic.ICrossover;
import com.helger.genetic.IEventHandler;
import com.helger.genetic.IMutation;
import com.helger.genetic.IPopulationCreator;
import com.helger.genetic.ISelector;
import com.helger.genetic.continuation.ContinuationKnownOptimum;
import com.helger.genetic.continuation.ContinuationTimeBased;
import com.helger.genetic.continuation.ContinuationTotalGeneration;
import com.helger.genetic.crossover.CrossoverPartiallyMapped;
import com.helger.genetic.eventhandler.EventHandlerDefault;
import com.helger.genetic.model.IChromosome;
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
public final class MainTSPRunnerCh130 extends AbstractFileBasedTSPRunner
{
  public static void main (final String [] args)
  {
    // Optimum length = 6110
    final Matrix aDistances = readTSPFromFile (new ClassPathResource ("tsp/ch130.tsp"), true);

    final double nOptimumDistance = 6110;
    final int nCities = aDistances.getRowDimension ();
    final TSPFitnessFunction ff = new TSPFitnessFunction (aDistances);
    final TSPChromosomeValidator cv = true ? null : new TSPChromosomeValidator (nCities);

    long nMinGen = 105108;
    String sBest = "";
    for (int nBest = 2; nBest <= 4; ++nBest)
    {
      for (int nCPerc = 10; nCPerc <= 40; nCPerc += 10)
      {
        System.out.println ("nCPerc = " + nCPerc);
        for (int nMPerc = 600; nMPerc < 1000; ++nMPerc)
        {
          System.out.println ("  nMPerc = " + nMPerc);
          for (int nPopulationSize = 20; nPopulationSize <= 48; nPopulationSize += 4)
          {
            if (false)
              System.out.println ("    nPopulationSize = " + nPopulationSize);
            // Use fixed seed
            RandomGenerator.setRandomGenerator (new RandomGeneratorRandom (new Random (471147124713L)));

            final IEventHandler eh = true ? new EventHandlerDefault () : new TSPEventHandlerLogging ();
            IContinuation cont = null;
            cont = new ContinuationTotalGeneration (nMinGen + 1, cont);
            cont = new ContinuationKnownOptimum (ff.getFitness (nOptimumDistance), eh, cont);
            cont = new ContinuationTimeBased (20 * CGlobal.MILLISECONDS_PER_SECOND, cont);
            final IPopulationCreator pc = new TSPPopulationCreatorRandom (nCities, nPopulationSize, ff, cv);
            final ISelector s = new SelectorAllSortedBest (nBest);
            final ICrossover c = new CrossoverPartiallyMapped (new DecisionMakerPercentage (nCPerc / 10.0));
            final IMutation m = new MutationRandomPartialReverse (new DecisionMakerPercentage (nMPerc / 10.0));

            final IChromosome aBest = new TSPRunner ("ch130").run (aDistances,
                                                                   nOptimumDistance,
                                                                   ff,
                                                                   eh,
                                                                   cont,
                                                                   pc,
                                                                   s,
                                                                   c,
                                                                   m);
            if (ff.getDistance (aBest) == nOptimumDistance)
              if (eh.getLastGeneration () == nMinGen)
              {
                System.out.println ("PopulationSize=" +
                                    nPopulationSize +
                                    "; SelectorAllSortedBest(" +
                                    nBest +
                                    "); C:new DecisionMakerPercentage (" +
                                    (nCPerc / 10.0) +
                                    "); M:new DecisionMakerPercentage (" +
                                    (nMPerc / 10.0) +
                                    ") ==> nMinGen=" +
                                    nMinGen);
              }
              else
                if (eh.getLastGeneration () < nMinGen)
                {
                  nMinGen = eh.getLastGeneration ();
                  sBest = "PopulationSize=" +
                          nPopulationSize +
                          "; SelectorAllSortedBest(" +
                          nBest +
                          "); new DecisionMakerPercentage (" +
                          (nCPerc / 10.0) +
                          "); new DecisionMakerPercentage (" +
                          (nMPerc / 10.0) +
                          ") ==> " +
                          nMinGen;
                  System.out.println (sBest);
                }
          }
        }
      }
    }
    System.out.println ("Overall best: " + sBest);
  }
}
