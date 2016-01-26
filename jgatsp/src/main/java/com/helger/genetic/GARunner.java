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
package com.helger.genetic;

import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.genetic.model.IChromosome;
import com.helger.genetic.model.IMutablePopulation;
import com.helger.genetic.model.IPopulation;

/**
 * <pre>
 *     [Start] Generate random population of n chromosomes (suitable solutions for the problem)
 *     [Fitness] Evaluate the fitness f(x) of each chromosome x in the population
 *     [New population] Create a new population by repeating following steps until the new population is complete
 *         [Selection] Select two parent chromosomes from a population
 *         [Crossover] With a crossover probability cross over the parents to form a new offspring (children). If no crossover was performed, offspring is an exact copy of parents.
 *         [Mutation] With a mutation probability mutate new offspring at each locus (position in chromosome).
 *         [Accepting] Place new offspring in a new population
 *     [Replace] Use new generated population for a further run of algorithm
 *     [Test] If the end condition is satisfied, stop, and return the best solution in current population
 *     [Loop] Go to step 2
 * </pre>
 *
 * @author Philip Helger
 */
public class GARunner
{
  private final IEventHandler m_aEventHandler;
  private final IContinuation m_aContinuation;
  private final IPopulationCreator m_aPopulationCreator;
  private final ISelector m_aSelector;
  private final ICrossover m_aCrossover;
  private final IMutation m_aMutation;

  public GARunner (@Nonnull final IEventHandler aEventHandler,
                   @Nonnull final IContinuation aContinuation,
                   @Nonnull final IPopulationCreator aPopulationCreator,
                   @Nonnull final ISelector aSelector,
                   @Nonnull final ICrossover aCrossover,
                   @Nonnull final IMutation aMutation)
  {
    ValueEnforcer.notNull (aEventHandler, "EventHandler");
    ValueEnforcer.notNull (aContinuation, "Continuation");
    ValueEnforcer.notNull (aPopulationCreator, "PopulationCreator");
    ValueEnforcer.notNull (aSelector, "Selector");
    ValueEnforcer.notNull (aCrossover, "Crossover");
    ValueEnforcer.notNull (aMutation, "Mutation");

    m_aEventHandler = aEventHandler;
    m_aContinuation = aContinuation;
    m_aPopulationCreator = aPopulationCreator;
    m_aSelector = aSelector;
    m_aCrossover = aCrossover;
    m_aMutation = aMutation;
  }

  @Nonnull
  public IChromosome run ()
  {
    // Invoke callback
    m_aContinuation.onStart ();

    // Start
    IPopulation aPrevPopulation = m_aPopulationCreator.createInitialPopulation ();

    // Check all created chromosomes for validity
    for (final IChromosome aChromosome : aPrevPopulation.directGetAllChromosomes ())
      if (!aChromosome.isValid ())
        throw new IllegalStateException ("Created illegal initial chromosome!");

    // Invoke event handler
    m_aEventHandler.onNewPopulation (aPrevPopulation);

    // Get fittest of initial population
    IChromosome aOverallBest = aPrevPopulation.getFittestChromosome ();

    // Invoke event handler
    m_aEventHandler.onNewFittestChromosome (aOverallBest);

    do
    {
      // Get all chromosomes from the previous population
      List <IChromosome> aChromosomes = aPrevPopulation.getAllChromosomes ();

      // Selection and consistency checks
      aChromosomes = m_aSelector.selectSurvivingChromosomes (aChromosomes);

      // Crossover and consistency checks
      aChromosomes = m_aCrossover.crossover (aChromosomes);
      int nChromosomeIndex = 0;
      for (final IChromosome aChromosome : aChromosomes)
      {
        if (!aChromosome.isValid ())
          throw new IllegalStateException ("Crossover created illegal chromosome at index " +
                                           nChromosomeIndex +
                                           ": " +
                                           aChromosome);
        ++nChromosomeIndex;
      }

      // Mutation and consistency checks
      aChromosomes = m_aMutation.mutate (aChromosomes);
      int nMutationIndex = 0;
      for (final IChromosome aChromosome : aChromosomes)
      {
        if (!aChromosome.isValid ())
          throw new IllegalStateException ("Mutation created illegal chromosome at index " +
                                           nMutationIndex +
                                           ": " +
                                           aChromosome);
        ++nMutationIndex;
      }

      // Start building next population with the new chromosomes
      final IMutablePopulation aNextPopulation = m_aPopulationCreator.createEmptyPopulation ();
      aNextPopulation.addChromosomes (aChromosomes);

      // Invoke event handler
      m_aEventHandler.onNewPopulation (aNextPopulation);

      // Check fittest of newly created population
      final IChromosome aCurrentFittest = aNextPopulation.getFittestChromosome ();
      if (aCurrentFittest.isFitterThan (aOverallBest))
      {
        aOverallBest = aCurrentFittest;

        // Invoke event handler
        m_aEventHandler.onNewFittestChromosome (aCurrentFittest);
      }

      // Finally replace population
      aPrevPopulation = aNextPopulation;
    } while (m_aContinuation.shouldContinue (aPrevPopulation));

    // Finally
    return aOverallBest;
  }
}
