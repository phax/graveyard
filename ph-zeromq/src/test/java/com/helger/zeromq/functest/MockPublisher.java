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

import java.io.Serializable;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.collection.ArrayHelper;
import com.helger.zeromq.ZMQWorkerPool;
import com.helger.zeromq.socket.ZMQSocket;
import com.helger.zeromq.utils.ZMQHelper;

/**
 * Publisher specific code
 *
 * @author Philip Helger
 */
public class MockPublisher
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MockPublisher.class);

  private MockPublisher ()
  {}

  /**
   * Must be called by publisher upon startup
   *
   * @param aSPS
   *        mode. May not be <code>null</code>.
   */
  public static void init (@Nonnull final MockPubSub aSPS)
  {
    // Asynchronous
    ZMQWorkerPool.getInstance ().run ( () -> {
      aSPS.createPublisherDataSocket ();
      s_aLogger.info ("Publisher " + aSPS.getName () + " waiting for subscriber!");
      try (ZMQSocket aSocket = aSPS.createPublisherSyncSocket ())
      {
        // - wait for synchronization request
        aSocket.recv (0);

        // - send synchronization reply
        aSocket.send (ArrayHelper.EMPTY_BYTE_ARRAY, 0);
      }
      s_aLogger.info ("Subscriber for publisher " + aSPS.getName () + " present now!");
    });
  }

  /**
   * Send invoice data to subscriber.
   *
   * @param aSPS
   *        mode. May not be <code>null</code>.
   * @param aData
   *        The data to be published.
   */
  public static void publishData (@Nonnull final MockPubSub aSPS, @Nonnull final Serializable aData)
  {
    // Asynchronous
    ZMQWorkerPool.getInstance ().run ( () -> {
      s_aLogger.info ("Publishing data from " + aSPS.getName ());
      final byte [] aBytes = ZMQHelper.getSerializedByteArray (aData);
      aSPS.getExistingPublisherDataSocket ().send (aBytes, 0);
    });
  }

  /**
   * publisher sends this message to indicate subscriber that no more messages
   * are coming in.
   *
   * @param aSPS
   *        mode. May not be <code>null</code>.
   */
  public static void shutdown (@Nonnull final MockPubSub aSPS)
  {
    aSPS.getExistingPublisherDataSocket ().send (CMockZMQ.SHUTDOWN, 0);
  }
}
