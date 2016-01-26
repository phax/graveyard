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

import java.io.File;
import java.util.Random;

import com.helger.commons.io.file.FileHelper;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.genetic.IContinuation;
import com.helger.genetic.ICrossover;
import com.helger.genetic.IMutation;
import com.helger.genetic.IPopulationCreator;
import com.helger.genetic.ISelector;
import com.helger.genetic.continuation.ContinuationKnownOptimum;
import com.helger.genetic.crossover.CrossoverEdgeRecombination;
import com.helger.genetic.mutation.MutationRandomPartialReverse;
import com.helger.genetic.selector.SelectorAllSortedBest;
import com.helger.genetic.tsp.AbstractFileBasedTSPRunner;
import com.helger.genetic.tsp.TSPRunner;
import com.helger.genetic.tsp.eventhandler.TSPEventHandlerGenerationTracker;
import com.helger.genetic.tsp.model.TSPChromosomeValidator;
import com.helger.genetic.tsp.model.TSPFitnessFunction;
import com.helger.genetic.tsp.mutation.TSPMutationGreedyBeginning;
import com.helger.genetic.tsp.populationcreator.TSPPopulationCreatorRandom;
import com.helger.genetic.utils.decisionmaker.DecisionMakerPercentage;
import com.helger.genetic.utils.random.RandomGenerator;
import com.helger.genetic.utils.random.RandomGeneratorRandom;
import com.helger.math.matrix.Matrix;
import com.helger.poi.excel.EExcelVersion;
import com.helger.poi.excel.WorkbookCreationHelper;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TIntList;

public final class MainTSPRunnerBerlin52Best1 extends AbstractFileBasedTSPRunner
{
  public static void main (final String [] args)
  {
    final Matrix aDistances = readTSPFromFile (new ClassPathResource ("tsp/berlin52.tsp"), true);

    final int nOptimumDistance = 7542;
    final int nCities = aDistances.getRowDimension ();
    final TSPFitnessFunction ff = new TSPFitnessFunction (aDistances);
    final TSPChromosomeValidator cv = true ? null : new TSPChromosomeValidator (nCities);

    // Use fixed seed
    RandomGenerator.setRandomGenerator (new RandomGeneratorRandom (new Random (471147124713L)));

    final int nPopulationSize = 32;
    final TSPEventHandlerGenerationTracker eh = new TSPEventHandlerGenerationTracker (ff);
    IContinuation cont = null;
    cont = new ContinuationKnownOptimum (ff.getFitness (nOptimumDistance), eh, cont);
    final IPopulationCreator pc = new TSPPopulationCreatorRandom (nCities, nPopulationSize, ff, cv);
    final ISelector s = new SelectorAllSortedBest (2);
    final ICrossover c = new CrossoverEdgeRecombination (new DecisionMakerPercentage (0.2));
    final IMutation m = new TSPMutationGreedyBeginning (aDistances,
                                                        eh,
                                                        new MutationRandomPartialReverse (new DecisionMakerPercentage (80)));

    new TSPRunner ("berlin52").run (aDistances, nOptimumDistance, ff, eh, cont, pc, s, c, m);
    final TIntList aList = eh.getDistanceListPerPopulation ();

    final File aFile = new File ("data/berlin52/results-best.xlsx");
    final WorkbookCreationHelper aWCH = new WorkbookCreationHelper (EExcelVersion.XLSX);
    aWCH.createNewSheet ("STW CT");
    aWCH.addRow ();
    aWCH.addCell ("Generation");
    aWCH.addCell ("Optimum");
    aWCH.addCell ("Distance");
    final TIntIterator it = aList.iterator ();
    int nGeneration = 1;
    while (it.hasNext ())
    {
      aWCH.addRow ();
      aWCH.addCell (nGeneration);
      aWCH.addCell (nOptimumDistance);
      aWCH.addCell (it.next ());
      ++nGeneration;
    }
    aWCH.write (FileHelper.getOutputStream (aFile));
  }
}
