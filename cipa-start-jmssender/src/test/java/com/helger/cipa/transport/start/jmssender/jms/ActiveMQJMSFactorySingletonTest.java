/**
 * Copyright (C) 2013-2015 Philip Helger (www.helger.com)
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
package com.helger.cipa.transport.start.jmssender.jms;

import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.helger.commons.scopes.mock.ScopeTestRule;

/**
 * Unit test class of class {@link ActiveMQJMSFactorySingleton}.
 *
 * @author Philip Helger
 */
public final class ActiveMQJMSFactorySingletonTest
{
  @Rule
  public final TestRule m_aRule = new ScopeTestRule ();

  @Test
  public void testBasic ()
  {
    assertNotNull (ActiveMQJMSFactorySingleton.getInstance ());
  }
}
