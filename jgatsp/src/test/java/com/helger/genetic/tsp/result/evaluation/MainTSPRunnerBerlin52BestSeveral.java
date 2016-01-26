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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.helger.commons.CGlobal;
import com.helger.commons.debug.GlobalDebug;
import com.helger.commons.io.file.FileHelper;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.genetic.IContinuation;
import com.helger.genetic.ICrossover;
import com.helger.genetic.IMutation;
import com.helger.genetic.IPopulationCreator;
import com.helger.genetic.ISelector;
import com.helger.genetic.continuation.ContinuationKnownOptimum;
import com.helger.genetic.continuation.ContinuationTimeBased;
import com.helger.genetic.continuation.ContinuationTotalGeneration;
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

import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TIntList;

public final class MainTSPRunnerBerlin52BestSeveral extends AbstractFileBasedTSPRunner
{
  public static void main (final String [] args) throws IOException
  {
    GlobalDebug.setDebugModeDirect (true);
    final Matrix aDistances = readTSPFromFile (new ClassPathResource ("tsp/berlin52.tsp"), true);

    final int nOptimumDistance = 7542;
    final int nCities = aDistances.getRowDimension ();
    final TSPFitnessFunction ff = new TSPFitnessFunction (aDistances);
    final TSPChromosomeValidator cv = true ? null : new TSPChromosomeValidator (nCities);

    // Use fixed seed only once
    RandomGenerator.setRandomGenerator (new RandomGeneratorRandom (new Random (789234789989L)));

    final int nRepeats = 5;
    final List <TIntList> aDistanceListPerPopulations = new ArrayList <TIntList> (nRepeats);
    final List <TIntList> aDistanceListBest = new ArrayList <TIntList> (nRepeats);
    for (int i = 0; i < nRepeats; ++i)
    {
      final int nPopulationSize = nCities;
      final TSPEventHandlerGenerationTracker eh = new TSPEventHandlerGenerationTracker (ff);
      IContinuation cont = null;
      cont = new ContinuationTotalGeneration (1000, cont);
      cont = new ContinuationKnownOptimum (ff.getFitness (nOptimumDistance), eh, cont);
      if (false)
        cont = new ContinuationTimeBased (20 * CGlobal.MILLISECONDS_PER_SECOND, cont);
      final IPopulationCreator pc = new TSPPopulationCreatorRandom (nCities, nPopulationSize, ff, cv);
      final ISelector s = new SelectorAllSortedBest (2);
      final ICrossover c = new CrossoverEdgeRecombination (new DecisionMakerPercentage (2));
      final IMutation m = new TSPMutationGreedyBeginning (aDistances,
                                                          eh,
                                                          new MutationRandomPartialReverse (new DecisionMakerPercentage (80)));

      new TSPRunner ("berlin52").run (aDistances, nOptimumDistance, ff, eh, cont, pc, s, c, m);
      aDistanceListPerPopulations.add (eh.getDistanceListPerPopulation ());
      aDistanceListBest.add (eh.getDistanceListBest ());
    }

    final boolean bWithBest = false;
    final Workbook aWB = EExcelVersion.XLSX.createWorkbook ();
    final Sheet aSheet = aWB.createSheet ("STW CT");
    int nColumn = 0;
    Row aRow = aSheet.createRow (0);
    aRow.createCell (nColumn++).setCellValue ("Generation");
    aRow.createCell (nColumn++).setCellValue ("100%");
    aRow.createCell (nColumn++).setCellValue ("110%");
    aRow.createCell (nColumn++).setCellValue ("125%");
    for (int i = 0; i < nRepeats; ++i)
    {
      aRow.createCell (nColumn++).setCellValue ("Run " + (i + 1));
      if (bWithBest)
        aRow.createCell (nColumn++).setCellValue ("Best " + (i + 1));
    }

    // Ensure all rows are present
    int nMaxRows = 0;
    for (final TIntList aIL : aDistanceListPerPopulations)
      nMaxRows = Math.max (nMaxRows, aIL.size ());

    // Create rows and set generation and optimum
    final double dOptimumDistance110 = nOptimumDistance * 1.1;
    final double dOptimumDistance125 = nOptimumDistance * 1.25;
    for (int i = 0; i < nMaxRows; ++i)
    {
      aRow = aSheet.createRow (1 + i);
      aRow.createCell (0).setCellValue (i + 1);
      aRow.createCell (1).setCellValue (nOptimumDistance);
      aRow.createCell (2).setCellValue (dOptimumDistance110);
      aRow.createCell (3).setCellValue (dOptimumDistance125);
    }

    // Set all values
    nColumn = 4;
    for (int i = 0; i < nRepeats; ++i)
    {
      int nRow = 1;
      final TIntIterator itPerPop = aDistanceListPerPopulations.get (i).iterator ();
      while (itPerPop.hasNext ())
        aSheet.getRow (nRow++).createCell (nColumn).setCellValue (itPerPop.next ());
      ++nColumn;

      if (bWithBest)
      {
        nRow = 1;
        final TIntIterator itBest = aDistanceListBest.get (i).iterator ();
        while (itBest.hasNext ())
          aSheet.getRow (nRow++).createCell (nColumn).setCellValue (itBest.next ());
        ++nColumn;
      }
    }
    aWB.write (FileHelper.getOutputStream (new File ("data/berlin52/results-best-multiple.xlsx")));
  }
}
