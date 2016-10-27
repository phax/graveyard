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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SegmentTest
{

  @Test
  public void testSegmentEmpty ()
  {
    final Segment s = new Segment (new Context ('~', '*', ':'));
    assertNotNull (s);
  }

  @Test
  public void testAddElementString ()
  {
    final Segment s = new Segment (new Context ('~', '*', ':'));
    assertTrue (s.addElement ("ISA"));
  }

  @Test
  public void testAddElements ()
  {
    final Segment s = new Segment (new Context ('~', '*', ':'));
    assertTrue (s.addElements ("ISA", "ISA01", "ISA02"));
  }

  @Test
  public void testAddCompositeElementStringArray ()
  {
    final Segment s = new Segment (new Context ('~', '*', ':'));
    assertTrue (s.addCompositeElement ("AB", "CD", "EF"));
  }

  @Test
  public void testAddElementIntString ()
  {
    final Segment s = new Segment (new Context ('~', '*', ':'));
    s.addElements ("ISA", "ISA01", "ISA02");
    assertTrue (s.addCompositeElement ("ISA03_1", "ISA03_2", "ISA03_3"));
  }

  @Test
  public void testAddCompositeElementIntStringArray ()
  {
    final Segment s = new Segment (new Context ('~', '*', ':'));
    s.addElements ("ISA", "ISA01", "ISA02", "ISA04");
    s.addCompositeElement (3, "ISA03_1", "ISA03_2", "ISA03_3");
    assertEquals ("ISA03_1:ISA03_2:ISA03_3", s.getElement (3));
  }

  @Test
  public void testGetContext ()
  {
    final Segment s = new Segment (new Context ('~', '*', ':'));
    assertEquals ("[~,*,:]", s.getContext ().getAsString ());
  }

  @Test
  public void testGetElement ()
  {
    final Segment s = new Segment (new Context ('~', '*', ':'));
    s.addElements ("ISA", "ISA01", "ISA02", "ISA03");
    assertEquals ("ISA02", s.getElement (2));
  }

  @Test
  public void testIterator ()
  {
    final Segment s = new Segment (new Context ('~', '*', ':'));
    s.addElements ("ISA", "ISA01", "ISA02", "ISA03");
    assertNotNull (s.iterator ());
  }

  @Test
  public void testRemoveElement ()
  {
    final Segment s = new Segment (new Context ('~', '*', ':'));
    s.addElements ("ISA", "ISA01", "ISA02", "ISA03");
    s.removeElement (2);
    assertEquals ("ISA*ISA01*ISA03", s.getAsString ());
  }

  @Test
  public void testRemoveElementTwo ()
  {
    final Segment s = new Segment (new Context ('~', '*', ':'));
    s.addElements ("ISA", "ISA01", "ISA02", "ISA03");
    s.removeElement (3);
    assertEquals ("ISA*ISA01*ISA02", s.getAsString ());
  }

  @Test
  public void testSetContext ()
  {
    final Segment s = new Segment (new Context ('~', '*', ':'));
    s.setContext (new Context ('s', 'e', 'c'));
    assertEquals ("[s,e,c]", s.getContext ().getAsString ());
  }

  @Test
  public void testSetElement ()
  {
    final Segment s = new Segment (new Context ('~', '*', ':'));
    s.addElements ("ISA", "ISA01", "ISA02", "ISA04", "ISA04");
    s.setElement (3, "ISA03");
    assertEquals ("ISA03", s.getElement (3));
  }

  @Test
  public void testSetCompositeElement ()
  {
    final Segment s = new Segment (new Context ('~', '*', ':'));
    s.addElements ("ISA", "ISA01", "ISA02", "ISA04", "ISA04");
    s.setCompositeElement (3, "ISA03_1", "ISA03_2", "ISA03_3");
    assertEquals ("ISA03_1:ISA03_2:ISA03_3", s.getElement (3));
  }

  @Test
  public void testSize ()
  {
    final Segment s = new Segment (new Context ('~', '*', ':'));
    s.addElements ("ISA", "ISA01", "ISA02", "ISA03", "ISA04");
    assertEquals (5, s.size ());
  }

  @Test
  public void testToString ()
  {
    final Segment s = new Segment (new Context ('~', '*', ':'));
    s.addElements ("ISA", "ISA01", "ISA02", "ISA03", "ISA04");
    s.setCompositeElement (3, "ISA03_1", "ISA03_2", "ISA03_3");
    assertEquals ("ISA*ISA01*ISA02*ISA03_1:ISA03_2:ISA03_3*ISA04", s.getAsString ());

  }

  @Test
  public void testToStringRemoveTrailingEmptyElements ()
  {
    final Segment s = new Segment (new Context ('~', '*', ':'));
    s.addElements ("ISA", "ISA01", "ISA02", "ISA03", "ISA04", "", "", "");
    assertEquals ("ISA*ISA01*ISA02*ISA03*ISA04", s.getAsString (true));
  }

  @Test
  public void testToStringRemoveTrailingNullElements ()
  {
    final Segment s = new Segment (new Context ('~', '*', ':'));
    s.addElements ("ISA", "ISA01", "ISA02", "ISA03", "ISA04", null, null, null);
    assertEquals ("ISA*ISA01*ISA02*ISA03*ISA04", s.getAsString (true));
  }

  @Test
  public void testToXML ()
  {
    final Segment s = new Segment (new Context ('~', '*', ':'));
    s.addElements ("ISA", "01", "02", "03", "04");
    s.setCompositeElement (3, "03_1", "03_2", "03_3");
    assertEquals ("<ISA><ISA01><![CDATA[01]]></ISA01><ISA02><![CDATA[02]]></ISA02><ISA03><![CDATA[03_1:03_2:03_3]]></ISA03><ISA04><![CDATA[04]]></ISA04></ISA>",
                  s.toXML ());
  }

}
