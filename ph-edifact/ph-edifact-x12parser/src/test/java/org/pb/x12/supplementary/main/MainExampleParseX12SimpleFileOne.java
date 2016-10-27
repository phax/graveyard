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

import org.pb.x12.IX12Parser;
import org.pb.x12.Segment;
import org.pb.x12.X12Simple;
import org.pb.x12.X12SimpleParser;

/**
 * Example showing X12Simple Parser reading a X12 file and looping over the
 * segments.
 *
 * @author Prasad Balan
 *
 *         <pre>
 * Example of parsing a X12 file
 *
 * X12Simple x12 = (X12Simple) new X12SimpleParser().parse(new File("C:\\test\\835.txt"));
 * for (Segment s : x12) {
 *     if (s.getElement(0).equals("CLP")) {
 *         System.out.println("Total Change Amount " + s.getElement(3));
 *     }
 * }
 *         </pre>
 */
public class MainExampleParseX12SimpleFileOne
{

  public static void main (final String [] args)
  {
    X12Simple x12 = null;
    final IX12Parser parser = new X12SimpleParser ();
    double totalChargeAmount = 0.0;

    final URL url = MainExampleParseX12FileOne.class.getResource ("/example/example835One.txt");
    final File f1 = new File (url.getFile ());

    try
    {
      x12 = (X12Simple) parser.parse (f1);
      for (final Segment s : x12)
      {
        if (s.getElement (0).equals ("CLP"))
        {
          totalChargeAmount = totalChargeAmount + Double.parseDouble (s.getElement (3));
        }
      }
      System.out.println ("Total Charged Amount = " + totalChargeAmount);

    }
    catch (final Exception e1)
    {
      e1.printStackTrace ();
    }
  }
}
