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
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

public class LoopTest
{

  @Test
  public void testLoop ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ISA");
    assertNotNull (loop);
  }

  @Test
  public void testAddChildString ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ISA");
    final Loop child = loop.addChild ("GS");
    assertNotNull (child);
  }

  @Test
  public void testAddChildIntLoop ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ISA");
    final Loop gs = new Loop (new Context ('~', '*', ':'), "GS");
    final Loop st = new Loop (new Context ('~', '*', ':'), "ST");
    loop.addChild (0, gs);
    loop.addChild (1, st);
    assertEquals ("ST", loop.getLoop (1).getName ());
  }

  @Test
  public void testAddSegment ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ST");
    final Segment s = loop.addSegment ();
    assertNotNull (s);
  }

  @Test
  public void testAddSegmentString ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ST");
    loop.addSegment ("ST*835*000000001");
    assertEquals ("ST", loop.getSegment (0).getElement (0));
  }

  @Test
  public void testAddSegmentSegment ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ST");
    final Segment segment = new Segment (new Context ('~', '*', ':'));
    segment.addElements ("ST*835*000000001");
    loop.addSegment (segment);
    assertEquals ("ST", loop.getSegment (0).getElement (0));
  }

  @Test
  public void testAddSegmentInt ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ST");
    loop.addSegment ("BPR*DATA*NOT*VALID*RANDOM*TEXT");
    loop.addSegment ("TRN*1*0000000000*1999999999");
    loop.addSegment ("DTM*111*20090915");
    final Segment segment = new Segment (new Context ('~', '*', ':'));
    segment.addElements ("ST*835*000000001");
    loop.addSegment (0, segment);
    assertEquals ("ST", loop.getSegment (0).getElement (0));
  }

  @Test
  public void testAddSegmentIntString ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ST");
    loop.addSegment ("BPR*DATA*NOT*VALID*RANDOM*TEXT");
    loop.addSegment ("TRN*1*0000000000*1999999999");
    loop.addSegment ("DTM*111*20090915");
    loop.addSegment (0, "ST*835*000000001");
    assertEquals ("ST", loop.getSegment (0).getElement (0));
  }

  @Test
  public void testAddSegmentIntSegment ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ST");
    loop.addSegment ("ST*835*000000001");
    loop.addSegment ("BPR*DATA*NOT*VALID*RANDOM*TEXT");
    loop.addSegment ("DTM*111*20090915");
    final Segment segment = new Segment (new Context ('~', '*', ':'));
    segment.addElements ("ST*835*000000001");
    loop.addSegment (2, "TRN*1*0000000000*1999999999");
    assertEquals ("TRN", loop.getSegment (2).getElement (0));
  }

  @Test
  public void testAddChildIntString ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ISA");
    loop.addChild ("GS");
    loop.addChild (1, "ST");
    assertEquals ("ST", loop.getLoop (1).getName ());
  }

  @Test
  public void testHasLoop ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ISA");
    loop.addChild ("GS");
    loop.addChild ("ST");
    assertEquals (new Boolean (true), new Boolean (loop.hasLoop ("ST")));
  }

  @Test
  public void testFindLoop ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ISA");
    loop.addChild ("GS");
    loop.addChild ("ST");
    loop.addChild ("1000A");
    loop.addChild ("1000B");
    loop.addChild ("2000");
    loop.addChild ("2100");
    loop.addChild ("2110");
    loop.addChild ("GE");
    loop.addChild ("IEA");
    final List <Loop> loops = loop.findLoop ("2000");
    assertEquals (Integer.valueOf (1), Integer.valueOf (loops.size ()));
  }

  @Test
  public void testFindSegment ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ISA");
    loop.addChild ("GS");
    loop.addChild ("ST");
    loop.addChild ("1000A");
    loop.addChild ("1000B");
    final Loop child1 = loop.addChild ("2000");
    child1.addSegment ("LX*1");
    final Loop child2 = loop.addChild ("2000");
    child2.addSegment ("LX*2");
    loop.addChild ("2100");
    loop.addChild ("2110");
    loop.addChild ("GE");
    loop.addChild ("IEA");
    final List <Segment> segments = loop.findSegment ("LX");
    assertEquals (Integer.valueOf (2), Integer.valueOf (segments.size ()));
  }

  @Test
  public void testGetContext ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ISA");
    assertEquals ("[~,*,:]", loop.getContext ().getAsString ());
  }

  @Test
  public void testGetLoop ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "X12");
    loop.addChild ("ISA");
    loop.addChild ("GS");
    loop.addChild ("ST");
    loop.addChild ("1000A");
    loop.addChild ("1000B");
    loop.addChild ("2000");
    loop.addChild ("2100");
    loop.addChild ("2110");
    loop.addChild ("GE");
    loop.addChild ("IEA");
    assertEquals ("1000A", loop.getLoop (3).getName ());
  }

  @Test
  public void testGetSegment ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ST");
    loop.addSegment ("ST*835*000000001");
    loop.addSegment ("BPR*DATA*NOT*VALID*RANDOM*TEXT");
    loop.addSegment ("DTM*111*20090915");
    assertEquals ("DTM", loop.getSegment (2).getElement (0));
  }

  @Test
  public void testGetName ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ST");
    assertEquals ("ST", loop.getName ());
  }

  @Test
  public void testIterator ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ST");
    assertNotNull (loop.iterator ());
  }

  @Test
  public void testRemoveLoop ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "X12");
    loop.addChild ("ISA");
    loop.addChild ("GS");
    loop.addChild ("ST");
    loop.addChild ("1000A");
    loop.addChild ("1000B");
    loop.addChild ("2000");
    loop.addChild ("2100");
    loop.addChild ("2110");
    loop.addChild ("SE");
    loop.addChild ("GE");
    loop.addChild ("IEA");

    final Loop l1 = loop.removeLoop (3);
    assertEquals ("1000A", l1.getName ());

    final Loop l2 = loop.removeLoop (0);
    assertEquals ("ISA", l2.getName ());
  }

  @Test
  public void testRemoveSegment ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ST");
    loop.addSegment ("BPR*DATA*NOT*VALID*RANDOM*TEXT");
    loop.addSegment ("TRN*1*0000000000*1999999999");
    loop.addSegment ("DTM*111*20090915");
    loop.addSegment (0, "ST*835*000000001");

    final Segment s = loop.removeSegment (2);
    assertEquals ("TRN*1*0000000000*1999999999", s.getAsString ());
    assertEquals (3, loop.size ());
  }

  @Test
  public void testChildList ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "X12");
    loop.addChild ("ISA");
    loop.addChild ("GS");
    loop.addChild ("ST");
    loop.addChild ("1000A");
    loop.addChild ("1000B");
    loop.addChild ("2000");
    loop.addChild ("2100");
    loop.addChild ("2110");
    loop.addChild ("SE");
    loop.addChild ("GE");
    loop.addChild ("IEA");
    assertEquals (11, loop.getChildCount ());
  }

  @Test
  public void testSize ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ST");
    loop.addSegment ("ST*835*000000001");
    loop.addSegment ("BPR*DATA*NOT*VALID*RANDOM*TEXT");
    loop.addSegment ("DTM*111*20090915");
    assertEquals (3, loop.size ());
  }

  @Test
  public void testSetContext ()
  {
    final Loop loop = new Loop (new Context ('a', 'b', 'c'), "ST");
    final Context context = new Context ('~', '*', ':');
    loop.setContext (context);
    assertEquals ("[~,*,:]", loop.getContext ().getAsString ());
  }

  @Test
  public void testSetChildIntString ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "X12");
    loop.addChild ("ISA");
    loop.addChild ("GS");
    loop.addChild ("XX");
    loop.addChild ("1000A");
    loop.addChild ("1000B");
    loop.addChild ("2000");
    loop.addChild ("2100");
    loop.addChild ("2110");
    loop.addChild ("GE");
    loop.addChild ("IEA");
    loop.setChild (2, "ST"); // test
    assertEquals ("ST", loop.getLoop (2).getName ());
  }

  @Test
  public void testSetChildIntLoop ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "X12");
    loop.addChild ("ISA");
    loop.addChild ("GS");
    loop.addChild ("XX");
    loop.addChild ("1000A");
    loop.addChild ("1000B");
    loop.addChild ("2000");
    loop.addChild ("2100");
    loop.addChild ("2110");
    loop.addChild ("GE");
    loop.addChild ("IEA");
    loop.setChild (2, new Loop (new Context ('~', '*', ':'), "ST"));
    assertEquals ("ST", loop.getLoop (2).getName ());
  }

  @Test
  public void testSetSegmentInt ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ST");
    loop.addSegment ("NOT*THE*RIGHT*SEGMENT");
    loop.addSegment ("BPR*DATA*NOT*VALID*RANDOM*TEXT");
    loop.addSegment ("TRN*1*0000000000*1999999999");
    loop.addSegment ("DTM*111*20090915");
    final Segment segment = new Segment (new Context ('~', '*', ':'));
    segment.addElements ("ST*835*000000001");
    loop.setSegment (0, segment);
    assertEquals ("ST", loop.getSegment (0).getElement (0));
  }

  @Test
  public void testSetSegmentIntString ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ST");
    loop.addSegment ("NOT*THE*RIGHT*SEGMENT");
    loop.addSegment ("BPR*DATA*NOT*VALID*RANDOM*TEXT");
    loop.addSegment ("TRN*1*0000000000*1999999999");
    loop.addSegment ("DTM*111*20090915");
    final Segment segment = new Segment (new Context ('~', '*', ':'));
    segment.addElements ("ST*835*000000001");
    loop.setSegment (0, segment);
    assertEquals ("ST", loop.getSegment (0).getElement (0));
  }

  @Test
  public void testSetSegmentIntSegment ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ST");
    loop.addSegment ("ST*835*000000001");
    loop.addSegment ("BPR*DATA*NOT*VALID*RANDOM*TEXT");
    loop.addSegment ("DTM*111*20090915");
    loop.addSegment ("NOT*THE*RIGHT*SEGMENT");
    loop.setSegment (2, "TRN*1*0000000000*1999999999");
    assertEquals ("TRN", loop.getSegment (2).getElement (0));
  }

  @Test
  public void testSetName ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "AB");
    loop.setName ("ST");
    assertEquals ("ST", loop.getName ());
  }

  @Test
  public void testToString ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ST");
    loop.addSegment ("ST*835*000000001");
    assertEquals ("ST*835*000000001~", loop.getAsString ());
  }

  @Test
  public void testToStringRemoveTrailingEmptyElements ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ST");
    final Segment s = loop.addSegment ("ST*835*000000001");
    s.addElement ("");
    s.addElement ("");
    assertEquals ("ST*835*000000001~", loop.getAsString (true));
  }

  @Test
  public void testToStringRemoveTrailingEmptyElementsTwo ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ST");
    final Segment s = loop.addSegment ("ST*835*000000001***ST05");
    s.addElement (null);
    s.addElement (null);
    assertEquals ("ST*835*000000001***ST05~", loop.getAsString (true));
  }

  @Test
  public void testToStringRemoveTrailingEmptyElementsThree ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ST");
    final Segment s1 = loop.addSegment ("ST1*ST101*ST102***ST105");
    s1.addElement (null);
    s1.addElement (null);
    final Segment s2 = loop.addSegment ("ST2*ST201*ST202***ST205");
    s2.addElement ("");
    s2.addElement ("");
    assertEquals ("ST1*ST101*ST102***ST105~ST2*ST201*ST202***ST205~", loop.getAsString (true));
  }

  @Test
  public void testToXML ()
  {
    final Loop loop = new Loop (new Context ('~', '*', ':'), "ST");
    loop.addSegment ("ST*835*000000001");
    assertEquals ("<LOOP NAME=\"ST\"><ST><ST01><![CDATA[835]]></ST01><ST02><![CDATA[000000001]]></ST02></ST></LOOP>",
                  loop.toXML ());
  }

}
