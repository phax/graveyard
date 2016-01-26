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
package com.helger.genetic.tsp;

import java.text.NumberFormat;
import java.util.Locale;

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.CGlobal;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.debug.GlobalDebug;
import com.helger.commons.locale.LocaleCache;
import com.helger.commons.math.MathHelper;
import com.helger.commons.string.StringHelper;
import com.helger.commons.timing.StopWatch;
import com.helger.genetic.GARunner;
import com.helger.genetic.IContinuation;
import com.helger.genetic.ICrossover;
import com.helger.genetic.IEventHandler;
import com.helger.genetic.IMutation;
import com.helger.genetic.IPopulationCreator;
import com.helger.genetic.ISelector;
import com.helger.genetic.continuation.ContinuationInfinite;
import com.helger.genetic.continuation.ContinuationKnownOptimum;
import com.helger.genetic.continuation.ContinuationTimeBased;
import com.helger.genetic.crossover.CrossoverOnePointInt;
import com.helger.genetic.crossover.CrossoverPartiallyMapped;
import com.helger.genetic.eventhandler.EventHandlerDefault;
import com.helger.genetic.model.IChromosome;
import com.helger.genetic.mutation.MutationRandomMoveMultiple;
import com.helger.genetic.selector.SelectorAllSortedBest;
import com.helger.genetic.selector.SelectorAlternating;
import com.helger.genetic.selector.SelectorTournament;
import com.helger.genetic.tsp.eventhandler.TSPEventHandlerLogging;
import com.helger.genetic.tsp.model.TSPChromosomeValidator;
import com.helger.genetic.tsp.model.TSPFitnessFunction;
import com.helger.genetic.tsp.mutation.TSPMutationGreedy;
import com.helger.genetic.tsp.populationcreator.TSPPopulationCreatorRandom;
import com.helger.genetic.utils.decisionmaker.AbstractDecisionMakerRandom;
import com.helger.genetic.utils.decisionmaker.DecisionMakerPercentage;
import com.helger.genetic.utils.decisionmaker.DecisionMakerPercentageDecreasing;
import com.helger.math.matrix.Matrix;

public class TSPRunner
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (TSPRunner.class);
  private static final Locale LOCALE = LocaleCache.getInstance ().getLocale ("de", "AT");

  private final String m_sID;

  private static String _asPerc (final int n1, final int n2)
  {
    return _asPerc (MathHelper.getDividedDouble (n1, n2));
  }

  /**
   * @param d
   *        A value between 0 and 1
   * @return The percentage string
   */
  @Nonnull
  private static String _asPerc (final double d)
  {
    final NumberFormat aNF = NumberFormat.getPercentInstance (LOCALE);
    aNF.setMaximumFractionDigits (2);
    return aNF.format (d);
  }

  public TSPRunner (@Nonnull @Nonempty final String sID)
  {
    if (StringHelper.hasNoText (sID))
      throw new IllegalArgumentException ("ID");
    m_sID = sID;
  }

  @Nonnull
  public IChromosome runWithDefaultSettings (@Nonnull final Matrix aDistances,
                                             @Nonnegative final double dOptimumDistance)
  {
    final int nCities = aDistances.getRowDimension ();

    // Build input parameters
    final TSPFitnessFunction ff = new TSPFitnessFunction (aDistances);
    final TSPChromosomeValidator cv = true ? null : new TSPChromosomeValidator (nCities);

    // Limit population size, to city size
    final int nPopulationSize = Math.min (nCities, 32);

    final IEventHandler eh = false ? new EventHandlerDefault () : new TSPEventHandlerLogging ();
    IContinuation aNestedCont = null;
    if (dOptimumDistance >= 0)
    {
      // Handle known optimum
      aNestedCont = new ContinuationKnownOptimum (ff.getFitness (dOptimumDistance), eh, aNestedCont);
    }
    if (true)
      aNestedCont = new ContinuationTimeBased (20 * CGlobal.MILLISECONDS_PER_SECOND, aNestedCont);
    final IContinuation cont = new ContinuationInfinite (aNestedCont);
    final IPopulationCreator pc = new TSPPopulationCreatorRandom (nCities, nPopulationSize, ff, cv);
    final ISelector s = true ? new SelectorAllSortedBest (2)
                             : new SelectorAlternating (new SelectorAllSortedBest (4),
                                                        new SelectorTournament (),
                                                        10000,
                                                        eh)
                             {
                               @Override
                               protected void onCrossoverSelectionAlternation (@SuppressWarnings ("unused") @Nonnull final ISelector aNewCS)
                               {
                                 if (GlobalDebug.isDebugMode ())
                                   s_aLogger.info ("Switching crossover selector to " + aNewCS.getClass ().getName ());
                               }
                             };
    final DecisionMakerPercentage cdm = true ? new DecisionMakerPercentage (2)
                                             : new DecisionMakerPercentageDecreasing (12, 0.5, 1, 5000)
                                             {
                                               @Override
                                               protected void onPercentageChange ()
                                               {
                                                 if (false)
                                                   s_aLogger.info ("New cdm% of " + _asPerc (getPercentage () / 100));
                                               }
                                             };
    final ICrossover c = true ? new CrossoverPartiallyMapped (cdm) : new CrossoverOnePointInt (cdm);
    final AbstractDecisionMakerRandom mdm = false ? new DecisionMakerPercentage (75)
                                                  : new DecisionMakerPercentageDecreasing (50, 2, 1, 5000)
                                                  {
                                                    @Override
                                                    protected void onPercentageChange ()
                                                    {
                                                      if (false)
                                                        s_aLogger.info ("New mdm% of " +
                                                                        _asPerc (getPercentage () / 100));
                                                    }
                                                  };
    final IMutation m = true ? new TSPMutationGreedy (mdm, aDistances) : new MutationRandomMoveMultiple (mdm);
    return run (aDistances, dOptimumDistance, ff, eh, cont, pc, s, c, m);
  }

  /**
   * Run the TSP with the given matrix
   *
   * @param aDistances
   *        Symmetric distance matrix
   * @param dOptimumDistance
   *        The optimum, known length. May be -1 to indicate unknown.
   * @param ff
   *        fitness function
   * @param aEventHandler
   *        event handler
   * @param aContinuation
   *        continuation condition
   * @param aPopulationCreator
   *        population creator
   * @param aSelector
   *        chromosome selector
   * @param aCrossover
   *        crossover algorithm
   * @param aMutation
   *        mutation algorithm
   * @return Best matching {@link IChromosome}
   */
  @Nonnull
  public IChromosome run (@Nonnull final Matrix aDistances,
                          @CheckForSigned final double dOptimumDistance,
                          @Nonnull final TSPFitnessFunction ff,
                          @Nonnull final IEventHandler aEventHandler,
                          @Nonnull final IContinuation aContinuation,
                          @Nonnull final IPopulationCreator aPopulationCreator,
                          @Nonnull final ISelector aSelector,
                          @Nonnull final ICrossover aCrossover,
                          @Nonnull final IMutation aMutation)
  {
    if (aDistances == null)
      throw new NullPointerException ("distances");
    if (aDistances.getRowDimension () != aDistances.getColumnDimension ())
      throw new IllegalArgumentException ("Passed Matrix is not symmetrical!");

    final NumberFormat aNF = NumberFormat.getInstance (LOCALE);
    aNF.setMaximumFractionDigits (2);

    final int nCities = aDistances.getRowDimension ();

    if (GlobalDebug.isDebugMode ())
      s_aLogger.info ("Trying to solve TSP '" +
                      m_sID +
                      "' with " +
                      aNF.format (nCities) +
                      " cities" +
                      (dOptimumDistance >= 0 ? " with known optimum of " + aNF.format (dOptimumDistance) : ""));

    // Solve TSP
    final StopWatch aSW = StopWatch.createdStarted ();
    final IChromosome aBest = new GARunner (aEventHandler,
                                            aContinuation,
                                            aPopulationCreator,
                                            aSelector,
                                            aCrossover,
                                            aMutation).run ();
    aSW.stop ();

    // Show results
    final double dBestFoundDistance = ff.getDistance (aBest);
    String sComparedToOpt = "";
    if (dOptimumDistance >= 0)
      sComparedToOpt = " (" + _asPerc (dBestFoundDistance / dOptimumDistance) + " of optimum)";

    if (GlobalDebug.isDebugMode ())
      s_aLogger.info ("Shortest path has length " +
                      aNF.format (dBestFoundDistance) +
                      sComparedToOpt +
                      " after " +
                      aNF.format (aEventHandler.getLastGeneration ()) +
                      " generations (in " +
                      aSW.getMillis () +
                      "ms which is =" +
                      aNF.format ((double) aEventHandler.getLastGeneration () /
                                  aSW.getMillis () *
                                  CGlobal.MILLISECONDS_PER_SECOND) +
                      " generations per second)");
    if (GlobalDebug.isDebugMode ())
      s_aLogger.info ("Crossovers executed: " +
                      aNF.format (aCrossover.getExecutionCount ()) +
                      " out of " +
                      aNF.format (aCrossover.getTryCount ()) +
                      " = " +
                      _asPerc (aCrossover.getExecutionCount (), aCrossover.getTryCount ()));
    if (GlobalDebug.isDebugMode ())
      s_aLogger.info ("Mutations executed: " +
                      aNF.format (aMutation.getExecutionCount ()) +
                      " out of " +
                      aNF.format (aMutation.getTryCount ()) +
                      " = " +
                      _asPerc (aMutation.getExecutionCount (), aMutation.getTryCount ()));
    return aBest;
  }
}
