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

import org.pb.x12.IX12Parser;
import org.pb.x12.Segment;
import org.pb.x12.X12Simple;
import org.pb.x12.X12SimpleParser;

/**
 * Example showing X12Simple Parser reading a X12 String and looping over the
 * segments.
 *
 * @author Prasad Balan
 *
 *         <pre>
 * Example of parsing a X12 String
 *
 * X12Simple x12 = new X12SimpleParser().parse("ISA*00*          *00*          *ZZ*SENDERID ......... "));
 * for (Segment s : x12) {
 *     if (s.getElement(0).equals("CLP")) {
 *         System.out.println("Total Change Amount " + s.getElement(3));
 *     }
 * }
 *         </pre>
 */

public class MainExampleParseX12SimpleStringOne
{

  public static void main (final String [] args)
  {
    X12Simple x12 = null;
    final IX12Parser parser = new X12SimpleParser ();
    double totalChargeAmount = 0.0;
    final String x12Data = new String ("ISA*00*          *00*          *ZZ*SENDERID       *ZZ*RECEIVERID    *030409*0701*U*00401*0000000001*0*T*:~" +
                                       "GS*1212*SENDERID*RECEIVERID*0701*000000001*X*00401~" +
                                       "ST*835*000000001~" +
                                       "BPR*DATA*NOT*VALID*RANDOM*TEXT~" +
                                       "TRN*1*0000000000*1999999999~" +
                                       "DTM*111*20090915~" +
                                       "N1*PR*ALWAYS INSURANCE COMPANY~" +
                                       "N7*AROUND THE CORNER~" +
                                       "N4*SHINE CITY*GREEN STATE*ZIP~" +
                                       "REF*DT*435864864~" +
                                       "N1*PE*FI*888888888*P.O.BOX 456*SHINE CITY*GREEN STATE*ZIP*EARTH~" +
                                       "LX*1~" +
                                       "CLP*PCN123456789**5555.55**CCN987654321~" +
                                       "CAS*PR*909099*100.00~" +
                                       "NM1*QC*1*PATIENT*TREATED*ONE***34*333333333~" +
                                       "DTM*273*20020824~" +
                                       "AMT*A1*10.10~" +
                                       "AMT*A2*20.20~" +
                                       "LX*2~" +
                                       "CLP*PCN123456789**4444.44**CCN987654321~" +
                                       "CAS*PR*909099*200.00~" +
                                       "NM1*QC*1*PATIENT*TREATED*TWO***34*444444444~" +
                                       "DTM*273*20020824~" +
                                       "AMT*A1*30.30~" +
                                       "AMT*A2*40.40~" +
                                       "SE*24*000000001~" +
                                       "GE*1*000000001~" +
                                       "IEA*1*000000001~");
    try
    {
      x12 = (X12Simple) parser.parse (x12Data);
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
