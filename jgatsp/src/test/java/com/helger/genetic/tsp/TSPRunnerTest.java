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

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.helger.commons.collection.CollectionHelper;
import com.helger.genetic.tsp.model.TSPFitnessFunction;
import com.helger.genetic.tsp.mutation.TSPMutationGreedy;
import com.helger.math.graph.IMutableGraph;
import com.helger.math.graph.IMutableGraphNode;
import com.helger.math.graph.IMutableGraphRelation;
import com.helger.math.graph.simple.SimpleGraph;
import com.helger.math.matrix.Matrix;

public final class TSPRunnerTest
{
  private static final String ATTR_COST = "distance";

  @Nonnull
  private static Matrix _createDistanceMatrix (@Nonnull final IMutableGraph aGraph)
  {
    final List <IMutableGraphNode> aNodes = CollectionHelper.newList (aGraph.getAllNodes ().values ());
    final int nCities = aNodes.size ();
    final Matrix aMatrix = new Matrix (nCities, nCities);
    for (int nRow = 0; nRow < nCities; ++nRow)
    {
      final IMutableGraphNode aRowNode = aNodes.get (nRow);
      for (int nCol = 0; nCol < nCities; ++nCol)
        if (nRow != nCol)
        {
          final IMutableGraphRelation aRel = aRowNode.getRelation (aNodes.get (nCol));
          final int nDistance = aRel.getAttributeAsInt (ATTR_COST);
          aMatrix.set (nRow, nCol, nDistance);
          aMatrix.set (nCol, nRow, nDistance);
        }
    }
    return aMatrix;
  }

  @Test
  public void testSimple ()
  {
    final SimpleGraph aGraph = new SimpleGraph ();
    aGraph.createNode ("A");
    aGraph.createNode ("B");
    aGraph.createNode ("C");
    aGraph.createNode ("D");

    aGraph.createRelation ("A", "B").setAttribute (ATTR_COST, 2);
    aGraph.createRelation ("A", "C").setAttribute (ATTR_COST, 4);
    aGraph.createRelation ("A", "D").setAttribute (ATTR_COST, 5);
    aGraph.createRelation ("B", "C").setAttribute (ATTR_COST, 2);
    aGraph.createRelation ("B", "D").setAttribute (ATTR_COST, 4);
    aGraph.createRelation ("C", "D").setAttribute (ATTR_COST, 2);

    final Matrix m = _createDistanceMatrix (aGraph);
    new TSPRunner ("simple").runWithDefaultSettings (m, 11);
  }

  @Test
  public void testGreedy ()
  {
    final SimpleGraph aGraph = new SimpleGraph ();
    aGraph.createNode ("A");
    aGraph.createNode ("B");
    aGraph.createNode ("C");
    aGraph.createNode ("D");

    aGraph.createRelation ("A", "B").setAttribute (ATTR_COST, 5);
    aGraph.createRelation ("A", "C").setAttribute (ATTR_COST, 4);
    aGraph.createRelation ("A", "D").setAttribute (ATTR_COST, 3);
    aGraph.createRelation ("B", "C").setAttribute (ATTR_COST, 2);
    aGraph.createRelation ("B", "D").setAttribute (ATTR_COST, 5);
    aGraph.createRelation ("C", "D").setAttribute (ATTR_COST, 4);

    final Matrix m = _createDistanceMatrix (aGraph);

    final int [] aGreedy = TSPMutationGreedy.getGreedyOrder (m);
    System.out.println (Arrays.toString (aGreedy));
    final TSPFitnessFunction ff = new TSPFitnessFunction (m);
    System.out.println (ff.getDistance (aGreedy));

    new TSPRunner ("simple").runWithDefaultSettings (m, 14);
  }
}
