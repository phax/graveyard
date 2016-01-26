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
package com.helger.genetic.utils.decisionmaker;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class DescisionMakerFixedRateTest
{
  @Test
  public void testBasic_1_1 ()
  {
    final DecisionMakerFixedRate dm = new DecisionMakerFixedRate (1, 1);
    assertTrue (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
  }

  @Test
  public void testBasic_1_2 ()
  {
    final DecisionMakerFixedRate dm = new DecisionMakerFixedRate (1, 2);
    assertTrue (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
  }

  @Test
  public void testBasic_1_3 ()
  {
    final DecisionMakerFixedRate dm = new DecisionMakerFixedRate (1, 3);
    assertTrue (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
  }

  @Test
  public void testBasic_1_4 ()
  {
    final DecisionMakerFixedRate dm = new DecisionMakerFixedRate (1, 4);
    assertTrue (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
  }

  @Test
  public void testBasic_2_4 ()
  {
    final DecisionMakerFixedRate dm = new DecisionMakerFixedRate (2, 4);
    assertFalse (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertFalse (dm.shouldDoIt ());
    assertTrue (dm.shouldDoIt ());
  }
}
