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

import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;

public class CfTest
{

  @Test
  public void testCfString ()
  {
    final Cf cf = new Cf ("ISA");
    assertNotNull (cf);
  }

  @Test
  public void testCfStringString ()
  {
    final Cf cf = new Cf ("2300", "CLM");
    assertNotNull (cf);
  }

  @Test
  public void testCfStringStringStringInteger ()
  {
    final Cf cf = new Cf ("1000A", "NM1", "41", 1);
    assertNotNull (cf);
  }

  @Test
  public void testAddChildCf ()
  {
    final Cf cf1 = new Cf ("ISA");
    final Cf cf2 = new Cf ("GS");
    cf1.addChild (cf2);
    assertEquals (cf2, cf1.getAllChildren ().get (0));
    assertEquals ("GS", cf1.getAllChildren ().get (0).getName ());
  }

  @Test
  public void testAddChildStringString ()
  {
    final Cf cf1 = new Cf ("GS");
    final Cf cf2 = new Cf ("ST", "ST01");
    cf1.addChild (cf2);
    assertEquals (cf2, cf1.getAllChildren ().get (0));
    assertEquals ("ST", cf1.getAllChildren ().get (0).getName ());
  }

  @Test
  public void testAddChildStringStringStringInteger ()
  {
    final Cf cf1 = new Cf ("ST");
    final Cf cf2 = new Cf ("1000A", "NM1", "41", 1);
    cf1.addChild (cf2);
    assertEquals (cf2, cf1.getAllChildren ().get (0));
    assertEquals ("1000A", cf1.getAllChildren ().get (0).getName ());
    assertEquals ("NM1", cf1.getAllChildren ().get (0).getSegment ());
    assertEquals ("41", cf1.getAllChildren ().get (0).getSegmentQuals ()[0]);
    assertEquals (1, cf1.getAllChildren ().get (0).getSegmentQualPos ());
  }

  @Test
  public void testChildList ()
  {
    final Cf cf1 = new Cf ("ST");
    cf1.addChild (new Cf ("1000A", "NM1", "41", 1));
    cf1.addChild (new Cf ("1000B", "NM1", "40", 1));
    assertEquals (2, cf1.getAllChildren ().size ());
    assertEquals ("1000A", cf1.getAllChildren ().get (0).getName ());
    assertEquals ("1000B", cf1.getAllChildren ().get (1).getName ());
  }

  @Test
  public void testHasChildren ()
  {
    final Cf cf1 = new Cf ("ST");
    cf1.addChild (new Cf ("1000A", "NM1", "41", 1));
    cf1.addChild (new Cf ("1000B", "NM1", "40", 1));
    assertTrue (cf1.hasChildren ());
  }

  @Test
  public void testHasParent ()
  {
    final Cf cf1 = new Cf ("ST");
    final Cf cf2 = cf1.addChild ("1000A", "NM1", "41", 1);
    final Cf cf3 = cf1.addChild ("1000B", "NM1", "40", 1);
    assertTrue (cf2.hasParent ());
    assertTrue (cf3.hasParent ());
  }

  @Test
  public void testGetParent ()
  {
    final Cf cf1 = new Cf ("ST");
    final Cf cf2 = cf1.addChild ("1000A", "NM1", "41", 1);
    final Cf cf3 = cf1.addChild ("1000B", "NM1", "40", 1);
    assertEquals (cf1, cf2.getParent ());
    assertEquals (cf1, cf3.getParent ());
  }

  @Test
  public void testGetName ()
  {
    final Cf cf = new Cf ("ISA");
    assertEquals ("ISA", cf.getName ());
  }

  @Test
  public void testGetSegment ()
  {
    final Cf cf = new Cf ("2300", "CLM");
    assertEquals ("CLM", cf.getSegment ());
  }

  @Test
  public void testGetSegmentQuals ()
  {
    final Cf cf = new Cf ("1000A", "NM1", "41", 1);
    assertEquals ("41", cf.getSegmentQuals ()[0]);
  }

  @Test
  public void testGetSegmentQualPos ()
  {
    final Cf cf = new Cf ("1000A", "NM1", "41", 1);
    assertEquals (1, cf.getSegmentQualPos ());
  }

  @Test
  public void testSetParent ()
  {
    final Cf cf1 = new Cf ("ST");
    final Cf cf2 = new Cf ("1000A", "NM1", "41", 1);
    cf2.setParent (cf1);
    assertEquals (cf1, cf2.getParent ());
  }

  @Test
  public void testSetChildren ()
  {
    final Cf cf1 = new Cf ("ST");
    final Cf cf2 = cf1.addChild ("1000A", "NM1", "41", 1);
    final Cf cf3 = cf1.addChild ("1000B", "NM1", "40", 1);
    final ICommonsList <Cf> kids = new CommonsArrayList<> ();
    kids.add (cf2);
    kids.add (cf3);
    cf1.setChildren (kids);
    assertEquals (2, cf1.getChildCount ());
  }

  @Test
  public void testSetName ()
  {
    final Cf cf = new Cf ("XXX");
    cf.setName ("ISA");
    assertEquals ("ISA", cf.getName ());
  }

  @Test
  public void testSetSegment ()
  {
    final Cf cf = new Cf ("XXXX", "XXX");
    cf.setSegment ("CLM");
    assertEquals ("CLM", cf.getSegment ());
  }

  @Test
  public void testSetSegmentQuals ()
  {
    final Cf cf = new Cf ("1000A", "NM1");
    final String [] quals = { "41" };
    cf.setSegmentQuals (quals);
    assertEquals ("41", cf.getSegmentQuals ()[0]);
  }

  @Test
  public void testSetSegmentQualPos ()
  {
    final Cf cf = new Cf ("1000A", "NM1");
    final String [] quals = { "41" };
    cf.setSegmentQuals (quals);
    cf.setSegmentQualPos (1);
    assertEquals (1, cf.getSegmentQualPos ());
  }

  @Test
  public void testGetAsString ()
  {
    final Cf cf = new Cf ("1000A", "NM1", "41", 1);
    assertEquals ("+--1000A - NM1 - 41, - 1" + System.getProperty ("line.separator"), cf.getAsString ());
  }
}
