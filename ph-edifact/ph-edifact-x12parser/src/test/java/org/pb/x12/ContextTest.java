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

import org.junit.Test;

public class ContextTest
{

  @Test
  public void testContext ()
  {
    final Context ctxt = new Context ();
    assertNotNull (ctxt);
  }

  @Test
  public void testContextCharacterCharacterCharacter ()
  {
    final Context ctxt = new Context ('a', 'b', 'c');
    assertNotNull (ctxt);
  }

  @Test
  public void testGetCompositeElementSeparator ()
  {
    final Context ctxt = new Context ('a', 'b', 'c');
    assertEquals ('c', ctxt.getCompositeElementSeparator ());
  }

  @Test
  public void testGetElementSeparator ()
  {
    final Context ctxt = new Context ('a', 'b', 'c');
    assertEquals ('b', ctxt.getElementSeparator ());
  }

  @Test
  public void testGetSegmentSeparator ()
  {
    final Context ctxt = new Context ('a', 'b', 'c');
    assertEquals ('a', ctxt.getSegmentSeparator ());
  }

  @Test
  public void testSetCompositeElementSeparator ()
  {
    final Context ctxt = new Context ();
    ctxt.setCompositeElementSeparator ('c');
    assertEquals ('c', ctxt.getCompositeElementSeparator ());

  }

  @Test
  public void testSetElementSeparator ()
  {
    final Context ctxt = new Context ();
    ctxt.setElementSeparator ('b');
    assertEquals ('b', ctxt.getElementSeparator ());
  }

  @Test
  public void testSetSegmentSeparator ()
  {
    final Context ctxt = new Context ();
    ctxt.setSegmentSeparator ('b');
    assertEquals ('b', ctxt.getSegmentSeparator ());
  }

  @Test
  public void testToString ()
  {
    final Context ctxt = new Context ('a', 'b', 'c');
    assertEquals ("[a,b,c]", ctxt.getAsString ());
  }

}
