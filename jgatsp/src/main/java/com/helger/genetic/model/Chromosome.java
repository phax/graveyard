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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;

public class Chromosome implements IChromosome
{
  private final IFitnessFunction m_aFitnessFunction;
  private final IChromsomeValidator m_aChromsomeValidator;
  private final List <? extends IGene> m_aGenes;
  // Status cache
  private final int m_nGeneCount;
  private Double m_aFitness;

  public Chromosome (@Nonnull final IChromosome aChromsome, @Nonnull @Nonempty final IGene... aGenes)
  {
    this (aChromsome.getFitnessFunction (), aChromsome.getValidator (), aGenes);
  }

  public Chromosome (@Nonnull final IFitnessFunction aFitnessFunction,
                     @Nullable final IChromsomeValidator aChromsomeValidator,
                     @Nonnull @Nonempty final IGene... aGenes)
  {
    this (aFitnessFunction, aChromsomeValidator, CollectionHelper.newList (aGenes));
  }

  public Chromosome (@Nonnull final IChromosome aChromsome, @Nonnull @Nonempty final List <? extends IGene> aGenes)
  {
    this (aChromsome.getFitnessFunction (), aChromsome.getValidator (), aGenes);
  }

  public Chromosome (@Nonnull final IFitnessFunction aFitnessFunction,
                     @Nullable final IChromsomeValidator aChromsomeValidator,
                     @Nonnull @Nonempty final List <? extends IGene> aGenes)
  {
    if (aFitnessFunction == null)
      throw new NullPointerException ("fitnessFunction");
    if (CollectionHelper.isEmpty (aGenes))
      throw new IllegalArgumentException ("No genes provided!");
    m_aFitnessFunction = aFitnessFunction;
    m_aChromsomeValidator = aChromsomeValidator;
    m_aGenes = aGenes;
    m_nGeneCount = aGenes.size ();
  }

  @Nonnegative
  public int getGeneCount ()
  {
    return m_nGeneCount;
  }

  @Nonnull
  public IGene getGene (@Nonnegative final int nIndex)
  {
    return m_aGenes.get (nIndex);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <IGene> getAllGenes ()
  {
    return CollectionHelper.newList (m_aGenes);
  }

  @Nonnull
  @ReturnsMutableCopy
  public IGene [] getGeneArray ()
  {
    return m_aGenes.toArray (new IGene [m_nGeneCount]);
  }

  @Nonnull
  @ReturnsMutableCopy
  public int [] getGeneIntArray ()
  {
    final int nGenes = m_nGeneCount;
    final IGene [] aGenes = m_aGenes.toArray (new IGene [nGenes]);
    final int [] ret = new int [nGenes];
    for (int i = 0; i < nGenes; ++i)
      ret[i] = aGenes[i].intValue ();
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public double [] getGeneDoubleArray ()
  {
    final int nGenes = m_nGeneCount;
    final IGene [] aGenes = m_aGenes.toArray (new IGene [nGenes]);
    final double [] ret = new double [nGenes];
    for (int i = 0; i < nGenes; ++i)
      ret[i] = aGenes[i].doubleValue ();
    return ret;
  }

  @Nonnull
  public IFitnessFunction getFitnessFunction ()
  {
    return m_aFitnessFunction;
  }

  @Nonnull
  public Double getFitnessObj ()
  {
    // Lazy calculation with caching
    if (m_aFitness == null)
      m_aFitness = Double.valueOf (m_aFitnessFunction.getFitness (this));
    return m_aFitness;
  }

  public double getFitness ()
  {
    return getFitnessObj ().doubleValue ();
  }

  public boolean isFitterThan (@Nonnull final IChromosome aChromosome)
  {
    return getFitnessObj ().compareTo (aChromosome.getFitnessObj ()) > 0;
  }

  @Nullable
  public IChromsomeValidator getValidator ()
  {
    return m_aChromsomeValidator;
  }

  public boolean isValid ()
  {
    return m_aChromsomeValidator == null || m_aChromsomeValidator.isValidChromosome (this);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof Chromosome))
      return false;
    final Chromosome rhs = (Chromosome) o;
    // fitness function and fitness are irrelevant
    return EqualsHelper.equals (m_aGenes, rhs.m_aGenes);
  }

  @Override
  public int hashCode ()
  {
    // fitness function and fitness are irrelevant
    return new HashCodeGenerator (this).append (m_aGenes).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("genes", m_aGenes).append ("fitness", m_aFitness).toString ();
  }

  @Nonnull
  public static Chromosome createGenesInt (@Nonnull final IChromosome aChromosome,
                                           @Nonnull @Nonempty final int... aGeneValues)
  {
    return createGenesInt (aChromosome.getFitnessFunction (), aChromosome.getValidator (), aGeneValues);
  }

  @Nonnull
  public static Chromosome createGenesInt (@Nonnull final IFitnessFunction aFitnessFunction,
                                           @Nullable final IChromsomeValidator aChromsomeValidator,
                                           @Nonnull @Nonempty final int... aGeneValues)
  {
    final List <GeneInt> aGenes = new ArrayList <GeneInt> (aGeneValues.length);
    for (final int nValue : aGeneValues)
      aGenes.add (new GeneInt (nValue));
    return new Chromosome (aFitnessFunction, aChromsomeValidator, aGenes);
  }

  @Nonnull
  public static Chromosome createGenesDouble (@Nonnull final IChromosome aChromosome,
                                              @Nonnull @Nonempty final double... aGeneValues)
  {
    return createGenesDouble (aChromosome.getFitnessFunction (), aChromosome.getValidator (), aGeneValues);
  }

  @Nonnull
  public static Chromosome createGenesDouble (@Nonnull final IFitnessFunction aFitnessFunction,
                                              @Nullable final IChromsomeValidator aChromsomeValidator,
                                              @Nonnull @Nonempty final double... aGeneValues)
  {
    final List <GeneDouble> aGenes = new ArrayList <GeneDouble> (aGeneValues.length);
    for (final double dValue : aGeneValues)
      aGenes.add (new GeneDouble (dValue));
    return new Chromosome (aFitnessFunction, aChromsomeValidator, aGenes);
  }
}
