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

public class X12SimpleTest
{

  @Test
  public void testX12Simple ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    assertNotNull (x12);
  }

  @Test
  public void testAddSegment ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    final Segment s = x12.addSegment ();
    assertEquals ("", s.getAsString ());
  }

  @Test
  public void testAddSegmentString ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    x12.addSegment ("ISA*ISA01*ISA02*ISA03");
    assertEquals ("ISA*ISA01*ISA02*ISA03", x12.getSegment (0).getAsString ());
  }

  @Test
  public void testAddSegmentSegment ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    final Segment s = new Segment (new Context ('~', '*', ':'));
    s.addElements ("ISA", "ISA01", "ISA02", "ISA03");
    x12.addSegment (s);
    assertEquals ("ISA*ISA01*ISA02*ISA03", x12.getSegment (0).getAsString ());
  }

  @Test
  public void testAddSegmentInt ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    x12.addSegment ("ISA*ISA01*ISA02*ISA03");
    x12.addSegment ("ST*ST01*ST02*ST03");
    final Segment s = x12.addSegment (1); // test
    s.addElements ("GS*GS01*GS02*GS03");
    assertEquals ("GS*GS01*GS02*GS03", x12.getSegment (1).getAsString ());
  }

  @Test
  public void testAddSegmentIntString ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    x12.addSegment ("ISA*ISA01*ISA02*ISA03");
    x12.addSegment ("ST*ST01*ST02*ST03");
    x12.addSegment (1, "GS*GS01*GS02*GS03"); // test
    assertEquals ("GS*GS01*GS02*GS03", x12.getSegment (1).getAsString ());
  }

  @Test
  public void testAddSegmentIntSegment ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    x12.addSegment ("ISA*ISA01*ISA02*ISA03");
    x12.addSegment ("ST*ST01*ST02*ST03");
    final Segment s = new Segment (new Context ('~', '*', ':'));
    s.addElements ("GS*GS01*GS02*GS03");
    x12.addSegment (1, s); // test
    assertEquals ("GS*GS01*GS02*GS03", x12.getSegment (1).getAsString ());
  }

  @Test
  public void testFindSegment ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    x12.addSegment ("ISA*ISA01*ISA02*ISA03");
    x12.addSegment ("GS*GS01*GS02*GS03");
    x12.addSegment ("ST*ST01*ST02*ST03");
    x12.addSegment ("REF*REF01*REF02*REF03");
    x12.addSegment ("REF*REF01*REF02*REF03");
    x12.addSegment ("REF*REF01*REF02*REF03");
    x12.addSegment ("SE*SE01*SE02*SE03");
    x12.addSegment ("GE*GE01*GE02*GE03");
    x12.addSegment ("IEA*IEA01*IEA02*IEA03");
    final List <Segment> results = x12.findSegment ("REF");
    assertEquals ("REF*REF01*REF02*REF03", results.get (0).getAsString ());
    assertEquals ("REF*REF01*REF02*REF03", results.get (1).getAsString ());
    assertEquals ("REF*REF01*REF02*REF03", results.get (2).getAsString ());
  }

  @Test
  public void testGetContext ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    assertEquals ("[~,*,:]", x12.getContext ().getAsString ());
  }

  @Test
  public void testGetSegment ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    x12.addSegment ("ISA*ISA01*ISA02*ISA03");
    x12.addSegment ("GS*GS01*GS02*GS03");
    x12.addSegment ("ST*ST01*ST02*ST03");
    assertEquals ("GS*GS01*GS02*GS03", x12.getSegment (1).getAsString ());
  }

  @Test
  public void testIterator ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    assertNotNull (x12.iterator ());
  }

  @Test
  public void testRemoveSegment ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    x12.addSegment ("ISA*ISA01*ISA02*ISA03");
    x12.addSegment ("ST1*ST101*ST102*ST103");
    x12.addSegment ("ST2*ST201*ST202*ST203");
    x12.addSegment ("ST2*ST301*ST302*ST303");

    final Segment s1 = x12.removeSegment (2); // test
    assertEquals ("ST2*ST201*ST202*ST203", s1.getAsString ());
    assertEquals (3, x12.size ());
    assertEquals ("ISA*ISA01*ISA02*ISA03~ST1*ST101*ST102*ST103~ST2*ST301*ST302*ST303~", x12.getAsString ());

    final Segment s2 = x12.removeSegment (0); // test
    assertEquals ("ISA*ISA01*ISA02*ISA03", s2.getAsString ());
    assertEquals (2, x12.size ());
    assertEquals ("ST1*ST101*ST102*ST103~ST2*ST301*ST302*ST303~", x12.getAsString ());
  }

  @Test
  public void testSetContext ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    x12.setContext (new Context ('s', 'e', 'c'));
    assertEquals ("[s,e,c]", x12.getContext ().getAsString ());
  }

  @Test
  public void testSetSegmentInt ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    x12.addSegment ("ISA*ISA01*ISA02*ISA03");
    x12.addSegment ("ST*ST01*ST02*ST03");
    x12.addSegment ("ST*ST01*ST02*ST03");
    final Segment s = x12.setSegment (1); // test
    s.addElements ("GS*GS01*GS02*GS03");
    assertEquals ("GS*GS01*GS02*GS03", x12.getSegment (1).getAsString ());
  }

  @Test
  public void testSetSegmentIntString ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    x12.addSegment ("ISA*ISA01*ISA02*ISA03");
    x12.addSegment ("ST*ST01*ST02*ST03");
    x12.addSegment ("ST*ST01*ST02*ST03");
    x12.setSegment (1, "GS*GS01*GS02*GS03"); // test
    assertEquals ("GS*GS01*GS02*GS03", x12.getSegment (1).getAsString ());
  }

  @Test
  public void testSetSegmentIntSegment ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    x12.addSegment ("ISA*ISA01*ISA02*ISA03");
    x12.addSegment ("ST*ST01*ST02*ST03");
    x12.addSegment ("ST*ST01*ST02*ST03");
    final Segment s = new Segment (new Context ('~', '*', ':'));
    s.addElements ("GS*GS01*GS02*GS03");
    x12.setSegment (1, s); // test
    assertEquals ("GS*GS01*GS02*GS03", x12.getSegment (1).getAsString ());
  }

  @Test
  public void testSize ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    x12.addSegment ("ISA*ISA01*ISA02*ISA03");
    x12.addSegment ("GS*GS01*GS02*GS03");
    x12.addSegment ("ST*ST01*ST02*ST03");
    x12.addSegment ("REF*REF01*REF02*REF03");
    x12.addSegment ("REF*REF01*REF02*REF03");
    x12.addSegment ("REF*REF01*REF02*REF03");
    x12.addSegment ("SE*SE01*SE02*SE03");
    x12.addSegment ("GE*GE01*GE02*GE03");
    x12.addSegment ("IEA*IEA01*IEA02*IEA03");
    assertEquals (9, x12.size ());
  }

  @Test
  public void testToString ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    x12.addSegment ("ISA*ISA01*ISA02*ISA03");
    x12.addSegment ("GS*GS01*GS02*GS03");
    assertEquals ("ISA*ISA01*ISA02*ISA03~GS*GS01*GS02*GS03~", x12.getAsString ());
  }

  @Test
  public void testToStringRemoveTrailingEmptyElements ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    x12.addSegment ("ISA*ISA01*ISA02*ISA03***");
    x12.addSegment ("GS*GS01*GS02*GS03***");
    assertEquals ("ISA*ISA01*ISA02*ISA03~GS*GS01*GS02*GS03~", x12.getAsString (true));
  }

  @Test
  public void testToStringRemoveTrailingEmptyElementsTwo ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    x12.addSegment ("ISA*ISA01*ISA02*ISA03***ISA06");
    x12.addSegment ("GS*GS01*GS02*GS03***");
    assertEquals ("ISA*ISA01*ISA02*ISA03***ISA06~GS*GS01*GS02*GS03~", x12.getAsString (true));
  }

  @Test
  public void testToStringRemoveTrailingEmptyElementsThree ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    x12.addSegment ("ISA*ISA01*ISA02*ISA03***ISA06");
    x12.addSegment ("GS*GS01*GS02*GS03**GS05**");
    assertEquals ("ISA*ISA01*ISA02*ISA03***ISA06~GS*GS01*GS02*GS03**GS05~", x12.getAsString (true));
  }

  @Test
  public void testToXML ()
  {
    final X12Simple x12 = new X12Simple (new Context ('~', '*', ':'));
    x12.addSegment ("ISA*ISA01*ISA02*ISA03");
    x12.addSegment ("GS*GS01*GS02*GS03");
    assertEquals ("<X12><ISA><ISA01><![CDATA[ISA01]]></ISA01><ISA02><![CDATA[ISA02]]></ISA02><ISA03><![CDATA[ISA03]]></ISA03></ISA><GS><GS01><![CDATA[GS01]]></GS01><GS02><![CDATA[GS02]]></GS02><GS03><![CDATA[GS03]]></GS03></GS></X12>",
                  x12.toXML ());
  }

}
