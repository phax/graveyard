/**
 * Copyright [2011] [Prasad Balan]
 * Copyright (C) 2016 Philip Helger (www.helger.com)
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
/*
   Copyright [2011] [Prasad Balan]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.pb.x12.supplementary.main;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.pb.x12.Cf;
import org.pb.x12.IX12Parser;
import org.pb.x12.Loop;
import org.pb.x12.Segment;
import org.pb.x12.X12;
import org.pb.x12.X12Parser;

/**
 * Example showing X12 Parser reading a X12 file and looping over the segments.
 *
 * @author Prasad Balan
 *
 *         <pre>
 * Example of parsing a X12 file
 *
 * This is the modified loop hierarchy of a 835 transaction used in this example.
 * The original/actual hierarchy is in example exampleParseX12FileTwo.
 * This just illustrates different ways you can setup the hierarchy to achieve
 * the desired results.
 * Note: Such a hierarchy change will work only when there is not more than
 * one Loop with the same identifiers. For e.g. in an 837 transaction both
 * Loop 2010BA (Subscriber) and 2330A (Other Subscriber) have the same identifiers,
 * which are segment id NM1 and IL at position NM102 (or index 1). This might
 * cause the parser to identify both elements as the same loop. In such cases
 * it is advisable to maintain the hierarchy.
 *
 *  +--X12
 *  |  +--ISA - ISA
 *  |  +--GS - GS
 *  |  +--ST - ST - 835, - 1
 *  |  +--1000A - N1 - PR, - 1
 *  |  +--1000B - N1 - PE, - 1
 *  |  +--2000 - LX
 *  |  +--2100 - CLP
 *  |  +--2110 - SVC
 *  |  +--SE - SE
 *  |  +--GE - GE
 *  |  +--IEA - IEA
 *
 * Cf cf835 = loadCf();
 * Parser parser = new X12Parser(cf835);
 * // The configuration Cf can be loaded using DI framework.
 * // Check the sample spring application context file provided.
 *
 * Double totalChargeAmount = 0.0;
 * X12 x12 = (X12) parser.parse(new File("C:\\test\\835.txt"));
 * List<Segment> segments = x12.findSegment("CLP");
 * for (Segment s : segments) {
 *     totalChargeAmount = totalChargeAmount + Double.parseDouble(s.getElement(3));
 * }
 * System.out.println("Total Change Amount " + s.getElement(3));
 *         </pre>
 */

public class MainExampleParseX12FileTwo
{

  public static void main (final String [] args)
  {
    X12 x12 = null;
    final Cf cf835 = loadCf (); // candidate for dependency injection
    final IX12Parser parser = new X12Parser (cf835);
    double totalChargeAmount = 0.0;

    final URL url = MainExampleParseX12FileOne.class.getClass ().getResource ("/example/example835One.txt");
    final File f1 = new File (url.getFile ());

    try
    {
      x12 = (X12) parser.parse (f1);

      // Calculate the total charge amount
      final List <Loop> loops = x12.findLoop ("2100");
      for (final Loop loop : loops)
      {
        for (final Segment s : loop)
        {
          if (s.getElement (0).equals ("CLP"))
          {
            totalChargeAmount = totalChargeAmount + Double.parseDouble (s.getElement (3));
          }
        }
      }
      System.out.println ("Total Charged Amount = " + totalChargeAmount);

      // Calculate the total charge amount - alternate method
      totalChargeAmount = 0.0;
      final List <Segment> segments = x12.findSegment ("CLP");
      for (final Segment s : segments)
      {
        totalChargeAmount = totalChargeAmount + Double.parseDouble (s.getElement (3));
      }
      System.out.println ("Total Charged Amount = " + totalChargeAmount);

    }
    catch (final Exception e1)
    {
      e1.printStackTrace ();
    }
  }

  // Alternately can be loaded using Spring/DI
  private static Cf loadCf ()
  {
    final Cf cfX12 = new Cf ("X12");
    cfX12.addChild ("ISA", "ISA");
    cfX12.addChild ("GS", "GS");
    cfX12.addChild ("ST", "ST", "835", 1);
    cfX12.addChild ("1000A", "N1", "PR", 1);
    cfX12.addChild ("1000B", "N1", "PE", 1);
    cfX12.addChild ("2000", "LX");
    cfX12.addChild ("2100", "CLP");
    cfX12.addChild ("2110", "SVC");
    cfX12.addChild ("GE", "GE");
    cfX12.addChild ("IEA", "IEA");
    // System.out.println(cfX12);
    return cfX12;
  }
}
