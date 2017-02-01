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

import javax.annotation.Nonnegative;

import com.helger.zeromq.ZMQPortManager;
import com.helger.zeromq.socket.ZMQSocket;

final class CMockZMQ
{
  private static final ZMQPortManager s_aPortMgr = new ZMQPortManager (5200, 5299);
  public static final byte [] SHUTDOWN = "stop".getBytes (ZMQSocket.DEFAULT_CHARSET);

  private CMockZMQ ()
  {}

  @Nonnegative
  public static int getNextFreePort ()
  {
    return s_aPortMgr.getNextPort ();
  }
}
