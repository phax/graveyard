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
package org.pb.x12;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Test;

public class X12SimpleParserTest
{

  @Test
  public void testParseFile () throws EDIFormatException, IOException
  {
    final IX12Parser parser = new X12SimpleParser ();
    final URL url = this.getClass ().getResource ("/example/example835One.txt");
    final File f1 = new File (url.getFile ());

    final X12Simple x12 = (X12Simple) parser.parse (f1);

    assertEquals ("ISA*00*          *00*          *ZZ*SENDERID       *ZZ*RECEIVERID    *030409*0701*U*00401*0000000001*0*T*:~" +
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
                  "IEA*1*000000001~",
                  x12.getAsString ());
  }

  @Test
  public void testParseInputStream () throws EDIFormatException, IOException
  {
    final IX12Parser parser = new X12SimpleParser ();
    final InputStream is = this.getClass ().getResourceAsStream ("/example/example835One.txt");

    final X12Simple x12 = (X12Simple) parser.parse (is);

    assertEquals ("ISA*00*          *00*          *ZZ*SENDERID       *ZZ*RECEIVERID    *030409*0701*U*00401*0000000001*0*T*:~" +
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
                  "IEA*1*000000001~",
                  x12.getAsString ());
  }

  @Test
  public void testParseString () throws EDIFormatException
  {
    final String inputString = "ISA*00*          *00*          *ZZ*SENDERID       *ZZ*RECEIVERID    *030409*0701*U*00401*0000000001*0*T*:~" +
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
                               "IEA*1*000000001~";

    final IX12Parser parser = new X12SimpleParser ();
    final X12Simple x12 = (X12Simple) parser.parse (inputString);

    assertEquals (inputString, x12.getAsString ());
  }

}
