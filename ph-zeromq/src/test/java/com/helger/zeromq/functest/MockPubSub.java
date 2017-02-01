/**
 * Copyright (C) 2016-2017 Philip Helger (www.helger.com)
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
package com.helger.zeromq.functest;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.zeromq.model.SyncedPubSubTCP;
import com.helger.zeromq.singleton.ZMQGlobalSingleton;

public final class MockPubSub extends SyncedPubSubTCP
{
  public static final MockPubSub PUB1_TO_SUB = new MockPubSub ("pub1-to-sub");
  public static final MockPubSub PUB2_TO_SUB = new MockPubSub ("pub2-to-sub");

  public MockPubSub (@Nonnull @Nonempty final String sName)
  {
    super (ZMQGlobalSingleton.getInstance (), sName, CMockZMQ.getNextFreePort (), CMockZMQ.getNextFreePort ());
  }
}
