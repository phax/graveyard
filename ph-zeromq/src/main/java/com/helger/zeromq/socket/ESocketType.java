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
package com.helger.zeromq.socket;

import org.zeromq.ZMQ;

/**
 * Type safe socket types.
 *
 * @author Philip Helger
 */
public enum ESocketType
{
  /**
   * Flag to specify a exclusive pair of sockets.
   */
  PAIR (ZMQ.PAIR),
  /**
   * Flag to specify a PUB socket, receiving side must be a SUB or XSUB.
   */
  PUB (ZMQ.PUB),
  /**
   * Flag to specify the receiving part of the PUB or XPUB socket.
   */
  SUB (ZMQ.SUB),
  /**
   * Flag to specify a REQ socket, receiving side must be a REP.
   */
  REQ (ZMQ.REQ),
  /**
   * Flag to specify the receiving part of a REQ socket.
   */
  REP (ZMQ.REP),
  /**
   * Flag to specify a DEALER socket (aka XREQ). DEALER is really a combined
   * ventilator / sink that does load-balancing on output and fair-queuing on
   * input with no other semantics. It is the only socket type that lets you
   * shuffle messages out to N nodes and shuffle the replies back, in a raw
   * bidirectional async pattern.
   */
  DEALER (ZMQ.DEALER),
  /**
   * Flag to specify ROUTER socket (aka XREP). ROUTER is the socket that creates
   * and consumes request-reply routing envelopes. It is the only socket type
   * that lets you route messages to specific connections if you know their
   * identities.
   */
  ROUTER (ZMQ.ROUTER),
  /**
   * Flag to specify the receiving part of a PUSH socket.
   */
  PULL (ZMQ.PULL),
  /**
   * Flag to specify a PUSH socket, receiving side must be a PULL.
   */
  PUSH (ZMQ.PUSH),
  /**
   * Flag to specify a XPUB socket, receiving side must be a SUB or XSUB.
   * Subscriptions can be received as a message. Subscriptions start with a '1'
   * byte. Unsubscriptions start with a '0' byte.
   */
  XPUB (ZMQ.XPUB),
  /**
   * Flag to specify the receiving part of the PUB or XPUB socket. Allows
   */
  XSUB (ZMQ.XSUB);

  private final int m_nType;

  private ESocketType (final int nType)
  {
    m_nType = nType;
  }

  public int getType ()
  {
    return m_nType;
  }
}
