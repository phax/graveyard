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

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.helger.commons.CGlobal;
import com.helger.commons.charset.CCharset;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.commons.io.stream.StreamHelper;
import com.helger.commons.regex.RegExHelper;
import com.helger.commons.string.StringParser;
import com.helger.math.matrix.Matrix;

public abstract class AbstractFileBasedTSPRunner
{
  private static double _getDistance (final double dOrigValue, final boolean bRoundDistances)
  {
    return bRoundDistances ? (int) (dOrigValue + 0.5) : dOrigValue;
  }

  @Nonnull
  public static Matrix readTSPFromFile (@Nonnull final IReadableResource aRes, final boolean bRoundDistances)
  {
    // Read the file content
    final List <String> aLines = StreamHelper.readStreamLines (aRes, CCharset.CHARSET_ISO_8859_1_OBJ);

    // Read all params
    final Map <String, String> aParams = new HashMap <String, String> ();
    int nIndex = 0;
    while (nIndex < aLines.size ())
    {
      final String sLine = aLines.get (nIndex);
      final String [] aParts = RegExHelper.getAllMatchingGroupValues ("([A-Z0-9_]+)\\s*:\\s(.+)", sLine);
      if (aParts == null)
        break;
      aParams.put (aParts[0], aParts[1]);
      ++nIndex;
    }

    // Node count
    final int nNodes = StringParser.parseInt (aParams.get ("DIMENSION"), CGlobal.ILLEGAL_UINT);
    if (nNodes == CGlobal.ILLEGAL_UINT)
      throw new IllegalStateException ("Failed to get node count from " + aParams);
    if (nNodes > 16000)
      throw new IllegalStateException ("TSP has too many nodes (" + nNodes + ") - too much heap would be required!");
    final Matrix ret = new Matrix (nNodes, nNodes);

    // Find starting position of coordinates
    for (; nIndex < aLines.size (); ++nIndex)
    {
      final String sLine = aLines.get (nIndex).trim ();
      if (sLine.equals ("NODE_COORD_SECTION") || sLine.equals ("EDGE_WEIGHT_SECTION"))
      {
        ++nIndex;
        break;
      }
    }
    if (nIndex == aLines.size ())
      throw new IllegalStateException ("Failed to find NODE_COORD_SECTION/EDGE_WEIGHT_SECTION");

    final String sEdgeWeightType = aParams.get ("EDGE_WEIGHT_TYPE");
    if ("EUC_2D".equals (sEdgeWeightType))
    {
      final List <Point2D> aPoints = new ArrayList <Point2D> ();
      for (int i = 0; i < nNodes; ++i)
      {
        final String sLine = aLines.get (nIndex + i).trim ();
        final String [] aParts = RegExHelper.getSplitToArray (sLine, "\\s+", 3);
        final int nX = StringParser.parseDoubleObj (aParts[1]).intValue ();
        final int nY = StringParser.parseDoubleObj (aParts[2]).intValue ();
        final Point2D aPoint = new Point2D.Double (nX, nY);
        aPoints.add (aPoint);
      }

      for (int i = 0; i < nNodes; ++i)
      {
        final Point2D aPointi = aPoints.get (i);
        for (int j = i + 1; j < nNodes; ++j)
        {
          final double dDistance = _getDistance (aPointi.distance (aPoints.get (j)), bRoundDistances);
          ret.set (i, j, dDistance);
          ret.set (j, i, dDistance);
        }
      }
    }
    else
      throw new IllegalStateException ("Cannot handle edge weight type '" + sEdgeWeightType + "'!");

    if (false)
      ret.print (6, 1);
    return ret;
  }
}
