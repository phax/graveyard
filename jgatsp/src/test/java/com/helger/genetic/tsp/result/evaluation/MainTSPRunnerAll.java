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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;

import org.apache.poi.ss.usermodel.Font;
import org.joda.time.Duration;

import com.helger.commons.CGlobal;
import com.helger.commons.io.file.FilenameHelper;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.timing.StopWatch;
import com.helger.datetime.PDTFactory;
import com.helger.genetic.IContinuation;
import com.helger.genetic.ICrossover;
import com.helger.genetic.IEventHandler;
import com.helger.genetic.IMutation;
import com.helger.genetic.IPopulationCreator;
import com.helger.genetic.ISelector;
import com.helger.genetic.continuation.ContinuationKnownOptimum;
import com.helger.genetic.continuation.ContinuationTimeBased;
import com.helger.genetic.crossover.CrossoverCycle;
import com.helger.genetic.crossover.CrossoverEdgeRecombination;
import com.helger.genetic.crossover.CrossoverOnePointInt;
import com.helger.genetic.crossover.CrossoverPartiallyMapped;
import com.helger.genetic.eventhandler.EventHandlerDefault;
import com.helger.genetic.mutation.MutationRandomExchange;
import com.helger.genetic.mutation.MutationRandomMoveMultiple;
import com.helger.genetic.mutation.MutationRandomMoveSingle;
import com.helger.genetic.mutation.MutationRandomPartialReverse;
import com.helger.genetic.selector.SelectorAllSortedBest;
import com.helger.genetic.tsp.AbstractFileBasedTSPRunner;
import com.helger.genetic.tsp.TSPRunner;
import com.helger.genetic.tsp.model.TSPChromosomeValidator;
import com.helger.genetic.tsp.model.TSPFitnessFunction;
import com.helger.genetic.tsp.mutation.TSPMutationGreedy;
import com.helger.genetic.tsp.mutation.TSPMutationGreedyBeginning;
import com.helger.genetic.tsp.populationcreator.TSPPopulationCreatorRandom;
import com.helger.genetic.utils.decisionmaker.DecisionMakerPercentage;
import com.helger.genetic.utils.decisionmaker.IDecisionMaker;
import com.helger.genetic.utils.random.RandomGenerator;
import com.helger.genetic.utils.random.RandomGeneratorRandom;
import com.helger.math.matrix.Matrix;
import com.helger.poi.excel.EExcelVersion;
import com.helger.poi.excel.WorkbookCreationHelper;
import com.helger.poi.excel.style.ExcelStyle;

public final class MainTSPRunnerAll extends AbstractFileBasedTSPRunner
{
  private static final int REPEATS = 10;
  private static final Map <String, Integer> TSPLIST = new LinkedHashMap <String, Integer> ();

  static
  {
    TSPLIST.put ("tsp/a280.tsp", Integer.valueOf (2579));
    TSPLIST.put ("tsp/berlin52.tsp", Integer.valueOf (7542));
    TSPLIST.put ("tsp/bier127.tsp", Integer.valueOf (118282));
    TSPLIST.put ("tsp/brd14051.tsp", Integer.valueOf (469385));
    TSPLIST.put ("tsp/ch130.tsp", Integer.valueOf (6110));
    TSPLIST.put ("tsp/ch150.tsp", Integer.valueOf (6528));
    TSPLIST.put ("tsp/d1291.tsp", Integer.valueOf (50801));
    if (false)
      TSPLIST.put ("tsp/d15112.tsp", Integer.valueOf (1573084));
    TSPLIST.put ("tsp/d1655.tsp", Integer.valueOf (62128));
    TSPLIST.put ("tsp/d198.tsp", Integer.valueOf (15780));
    TSPLIST.put ("tsp/d2103.tsp", Integer.valueOf (80450));
    TSPLIST.put ("tsp/d493.tsp", Integer.valueOf (35002));
    TSPLIST.put ("tsp/d657.tsp", Integer.valueOf (48912));
    TSPLIST.put ("tsp/eil101.tsp", Integer.valueOf (629));
    TSPLIST.put ("tsp/eil51.tsp", Integer.valueOf (426));
    TSPLIST.put ("tsp/eil76.tsp", Integer.valueOf (538));
    TSPLIST.put ("tsp/fl1400.tsp", Integer.valueOf (20127));
    TSPLIST.put ("tsp/fl1577.tsp", Integer.valueOf (22249));
    TSPLIST.put ("tsp/fl3795.tsp", Integer.valueOf (28772));
    TSPLIST.put ("tsp/fl417.tsp", Integer.valueOf (11861));
    TSPLIST.put ("tsp/fnl4461.tsp", Integer.valueOf (182566));
    TSPLIST.put ("tsp/gil262.tsp", Integer.valueOf (2378));
    TSPLIST.put ("tsp/kroA100.tsp", Integer.valueOf (21282));
    TSPLIST.put ("tsp/kroA150.tsp", Integer.valueOf (26524));
    TSPLIST.put ("tsp/kroA200.tsp", Integer.valueOf (29368));
    TSPLIST.put ("tsp/kroB100.tsp", Integer.valueOf (22141));
    TSPLIST.put ("tsp/kroB150.tsp", Integer.valueOf (26130));
    TSPLIST.put ("tsp/kroB200.tsp", Integer.valueOf (29437));
    TSPLIST.put ("tsp/kroC100.tsp", Integer.valueOf (20749));
    TSPLIST.put ("tsp/kroD100.tsp", Integer.valueOf (21294));
    TSPLIST.put ("tsp/kroE100.tsp", Integer.valueOf (22068));
    TSPLIST.put ("tsp/lin105.tsp", Integer.valueOf (14379));
    TSPLIST.put ("tsp/lin318.tsp", Integer.valueOf (42029));
    TSPLIST.put ("tsp/linhp318.tsp", Integer.valueOf (41345));
    TSPLIST.put ("tsp/nrw1379.tsp", Integer.valueOf (56638));
    TSPLIST.put ("tsp/p654.tsp", Integer.valueOf (34643));
    TSPLIST.put ("tsp/pcb1173.tsp", Integer.valueOf (56892));
    TSPLIST.put ("tsp/pcb3038.tsp", Integer.valueOf (137694));
    TSPLIST.put ("tsp/pcb442.tsp", Integer.valueOf (50778));
    TSPLIST.put ("tsp/pr1002.tsp", Integer.valueOf (259045));
    TSPLIST.put ("tsp/pr107.tsp", Integer.valueOf (44303));
    TSPLIST.put ("tsp/pr124.tsp", Integer.valueOf (59030));
    TSPLIST.put ("tsp/pr136.tsp", Integer.valueOf (96772));
    TSPLIST.put ("tsp/pr144.tsp", Integer.valueOf (58537));
    TSPLIST.put ("tsp/pr152.tsp", Integer.valueOf (73682));
    TSPLIST.put ("tsp/pr226.tsp", Integer.valueOf (80369));
    TSPLIST.put ("tsp/pr2392.tsp", Integer.valueOf (378032));
    TSPLIST.put ("tsp/pr264.tsp", Integer.valueOf (49135));
    TSPLIST.put ("tsp/pr299.tsp", Integer.valueOf (48191));
    TSPLIST.put ("tsp/pr439.tsp", Integer.valueOf (107217));
    TSPLIST.put ("tsp/pr76.tsp", Integer.valueOf (108159));
    TSPLIST.put ("tsp/rat195.tsp", Integer.valueOf (2323));
    TSPLIST.put ("tsp/rat575.tsp", Integer.valueOf (6773));
    TSPLIST.put ("tsp/rat783.tsp", Integer.valueOf (8806));
    TSPLIST.put ("tsp/rat99.tsp", Integer.valueOf (1211));
    TSPLIST.put ("tsp/rd100.tsp", Integer.valueOf (7910));
    TSPLIST.put ("tsp/rd400.tsp", Integer.valueOf (15281));
    TSPLIST.put ("tsp/rl11849.tsp", Integer.valueOf (923288));
    TSPLIST.put ("tsp/rl1304.tsp", Integer.valueOf (252948));
    TSPLIST.put ("tsp/rl1323.tsp", Integer.valueOf (270199));
    TSPLIST.put ("tsp/rl1889.tsp", Integer.valueOf (316536));
    TSPLIST.put ("tsp/rl5915.tsp", Integer.valueOf (565530));
    TSPLIST.put ("tsp/rl5934.tsp", Integer.valueOf (556045));
    TSPLIST.put ("tsp/st70.tsp", Integer.valueOf (675));
    TSPLIST.put ("tsp/ts225.tsp", Integer.valueOf (126643));
    TSPLIST.put ("tsp/tsp225.tsp", Integer.valueOf (3916));
    TSPLIST.put ("tsp/u1060.tsp", Integer.valueOf (224094));
    TSPLIST.put ("tsp/u1432.tsp", Integer.valueOf (152970));
    TSPLIST.put ("tsp/u159.tsp", Integer.valueOf (42080));
    TSPLIST.put ("tsp/u1817.tsp", Integer.valueOf (57201));
    TSPLIST.put ("tsp/u2152.tsp", Integer.valueOf (64253));
    TSPLIST.put ("tsp/u2319.tsp", Integer.valueOf (234256));
    TSPLIST.put ("tsp/u574.tsp", Integer.valueOf (36905));
    TSPLIST.put ("tsp/u724.tsp", Integer.valueOf (41910));
    TSPLIST.put ("tsp/usa13509.tsp", Integer.valueOf (19982859));
    TSPLIST.put ("tsp/vm1084.tsp", Integer.valueOf (239297));
    TSPLIST.put ("tsp/vm1748.tsp", Integer.valueOf (336556));
  }

  private static void _runTSP (@Nonnull final String sFilename,
                               final int nOptimumDistance,
                               final WorkbookCreationHelper aWCH,
                               final boolean bUseMaxPopulationSize,
                               final int nSeconds,
                               final ICrossover c,
                               final Class <? extends IMutation> cm,
                               final int nMutationPerc) throws Exception
  {
    final String sTSPID = FilenameHelper.getBaseName (sFilename);
    final Matrix aDistances = readTSPFromFile (new ClassPathResource (sFilename), true);
    final IEventHandler eh = new EventHandlerDefault ();

    IMutation m;
    final IDecisionMaker dm = new DecisionMakerPercentage (nMutationPerc);
    if (cm == TSPMutationGreedy.class)
      m = new TSPMutationGreedy (dm, aDistances);
    else
      if (cm == TSPMutationGreedyBeginning.class)
        m = new TSPMutationGreedyBeginning (aDistances, eh, new MutationRandomPartialReverse (dm));
      else
        m = cm.getConstructor (IDecisionMaker.class).newInstance (dm);

    final int nCities = aDistances.getRowDimension ();
    final TSPFitnessFunction ff = new TSPFitnessFunction (aDistances);
    final TSPChromosomeValidator cv = true ? null : new TSPChromosomeValidator (nCities);
    final int nPopulationSize = bUseMaxPopulationSize ? nCities : Math.min (32, nCities);

    if (nCities > 700)
      return;

    System.out.print ("    " + sTSPID);

    // Use fixed seed
    RandomGenerator.setRandomGenerator (new RandomGeneratorRandom (new Random (43278234789L)));

    aWCH.addRow ();
    aWCH.addCell (sTSPID);
    aWCH.addCell (nCities);
    aWCH.addCell (nOptimumDistance);
    aWCH.addCell (nPopulationSize);

    for (int i = 0; i < REPEATS; ++i)
    {
      System.out.print (" " + i);
      IContinuation cont = null;
      cont = new ContinuationKnownOptimum (ff.getFitness (nOptimumDistance), eh, cont);
      cont = new ContinuationTimeBased (nSeconds * CGlobal.MILLISECONDS_PER_SECOND, cont);
      final IPopulationCreator pc = new TSPPopulationCreatorRandom (nCities, nPopulationSize, ff, cv);
      final ISelector s = new SelectorAllSortedBest (2);

      new TSPRunner (sTSPID).run (aDistances, nOptimumDistance, ff, eh, cont, pc, s, c, m);
      final double dBestDistance = ff.getDistance (eh.getFittestChromosome ());

      aWCH.addCell (eh.getLastGeneration ());
      aWCH.addCell (dBestDistance);
      aWCH.addCell (dBestDistance / nOptimumDistance * 100.0);
    }

    // First font has index 1...
    final ExcelStyle aStyle = new ExcelStyle ();
    aStyle.setFontIndex ((short) 1);

    final int nRowNum = aWCH.getRowCount ();
    final String sGenerations = "E" +
                                nRowNum +
                                ",H" +
                                nRowNum +
                                ",K" +
                                nRowNum +
                                ",N" +
                                nRowNum +
                                ",Q" +
                                nRowNum +
                                ",T" +
                                nRowNum +
                                ",W" +
                                nRowNum +
                                ",Z" +
                                nRowNum +
                                ",AC" +
                                nRowNum +
                                ",AF" +
                                nRowNum;
    aWCH.addCellFormula ("AVERAGE(" + sGenerations + ")");
    aWCH.addCellStyle (aStyle);
    aWCH.addCellFormula ("STDEV(" + sGenerations + ")");
    aWCH.addCellStyle (aStyle);
    final String sDistance = "F" +
                             nRowNum +
                             ",I" +
                             nRowNum +
                             ",L" +
                             nRowNum +
                             ",P" +
                             nRowNum +
                             ",R" +
                             nRowNum +
                             ",U" +
                             nRowNum +
                             ",X" +
                             nRowNum +
                             ",AA" +
                             nRowNum +
                             ",AD" +
                             nRowNum +
                             ",AG" +
                             nRowNum;
    aWCH.addCellFormula ("AVERAGE(" + sDistance + ")");
    aWCH.addCellStyle (aStyle);
    aWCH.addCellFormula ("STDEV(" + sDistance + ")");
    aWCH.addCellStyle (aStyle);
    final String sPercentage = "G" +
                               nRowNum +
                               ",J" +
                               nRowNum +
                               ",M" +
                               nRowNum +
                               ",P" +
                               nRowNum +
                               ",S" +
                               nRowNum +
                               ",V" +
                               nRowNum +
                               ",Y" +
                               nRowNum +
                               ",AB" +
                               nRowNum +
                               ",AE" +
                               nRowNum +
                               ",AH" +
                               nRowNum;
    aWCH.addCellFormula ("AVERAGE(" + sPercentage + ")");
    aWCH.addCellStyle (aStyle);
    aWCH.addCellFormula ("STDEV(" + sPercentage + ")");
    aWCH.addCellStyle (aStyle);
    System.out.println ();
  }

  private static void _runAll (final int nCrossoverPerc,
                               final int nMutationPerc,
                               final int nSeconds,
                               final boolean bUseMaxPopulationSize) throws Exception
  {
    final Map <String, ICrossover> cm = new LinkedHashMap <String, ICrossover> ();
    cm.put ("Cycle", new CrossoverCycle (new DecisionMakerPercentage (nCrossoverPerc)));
    cm.put ("EdgeRecombination", new CrossoverEdgeRecombination (new DecisionMakerPercentage (nCrossoverPerc)));
    cm.put ("OnePointInt", new CrossoverOnePointInt (new DecisionMakerPercentage (nCrossoverPerc)));
    cm.put ("PartiallyMapped", new CrossoverPartiallyMapped (new DecisionMakerPercentage (nCrossoverPerc)));

    final Map <String, Class <? extends IMutation>> mm = new LinkedHashMap <String, Class <? extends IMutation>> ();
    if (false)
      mm.put ("RandomExchange", MutationRandomExchange.class);
    if (false)
      mm.put ("RandomMoveMultiple", MutationRandomMoveMultiple.class);
    if (false)
      mm.put ("RandomMoveSingle", MutationRandomMoveSingle.class);
    if (false)
      mm.put ("RandomPartialReverse", MutationRandomPartialReverse.class);
    if (false)
      mm.put ("TSPMutationGreedy", TSPMutationGreedy.class);
    mm.put ("TSPMutationGreedyBeginning", TSPMutationGreedyBeginning.class);

    System.out.println ("Running " +
                        nCrossoverPerc +
                        "-" +
                        cm.keySet () +
                        "-" +
                        nMutationPerc +
                        "-" +
                        mm.keySet () +
                        "-" +
                        nSeconds +
                        (bUseMaxPopulationSize ? "-maxpop" : ""));
    final int nEstimatedSeconds = 47 * REPEATS * nSeconds * cm.size () * mm.size ();
    System.out.println ("  Estimated end time: " + PDTFactory.getCurrentDateTime ().plusSeconds (nEstimatedSeconds));

    for (final Map.Entry <String, ICrossover> aEntryC : cm.entrySet ())
    {
      final ICrossover aCrossover = aEntryC.getValue ();
      System.out.println (aEntryC.getKey ());

      for (final Map.Entry <String, Class <? extends IMutation>> aEntryM : mm.entrySet ())
      {
        final StopWatch aSW = StopWatch.createdStarted ();
        System.out.println ("  " + aEntryM.getKey ());
        final Class <? extends IMutation> aMutationClass = aEntryM.getValue ();

        final WorkbookCreationHelper aWCH = new WorkbookCreationHelper (EExcelVersion.XLSX);
        aWCH.createNewSheet ("STW CT");
        aWCH.addRow ();
        aWCH.addCell ("TSP");
        aWCH.addCell ("Städte");
        aWCH.addCell ("Optimum");
        aWCH.addCell ("Population");
        for (int i = 0; i < REPEATS; ++i)
        {
          aWCH.addCell ("Generation " + (i + 1));
          aWCH.addCell ("Distanz " + (i + 1));
          aWCH.addCell ("Prozent " + (i + 1));
        }

        final Font aFont = aWCH.getWorkbook ().createFont ();
        aFont.setFontName ("Calibri");
        aFont.setFontHeightInPoints ((short) 11);
        aFont.setBoldweight (Font.BOLDWEIGHT_BOLD);

        for (final Map.Entry <String, Integer> aEntry : TSPLIST.entrySet ())
          _runTSP (aEntry.getKey (),
                   aEntry.getValue ().intValue (),
                   aWCH,
                   bUseMaxPopulationSize,
                   nSeconds,
                   aCrossover,
                   aMutationClass,
                   nMutationPerc);

        aWCH.autoFilterAllColumns ();
        aWCH.autoSizeAllColumns ();
        aWCH.write ("data/all/search-" +
                    aEntryC.getKey () +
                    "-" +
                    nCrossoverPerc +
                    "-" +
                    aEntryM.getKey () +
                    "-" +
                    nMutationPerc +
                    "-" +
                    nSeconds +
                    "secs" +
                    (bUseMaxPopulationSize ? "-maxpop" : "") +
                    ".xlsx");
        System.out.println ("    Took " + new Duration (aSW.stopAndGetMillis ()).toString ());
      }
    }
  }

  public static void main (final String [] args) throws Exception
  {
    _runAll (3, 80, 15, false);
  }
}
