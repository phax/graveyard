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

import java.util.Arrays;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.collection.ArrayHelper;
import com.helger.zeromq.ZMQWorkerPool;
import com.helger.zeromq.socket.ZMQSocket;
import com.helger.zeromq.utils.ZMQHelper;

/**
 * Subscriber specific code
 *
 * @author Philip Helger
 */
public class MockSubscriber
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MockSubscriber.class);

  private MockSubscriber ()
  {}

  /**
   * Wait asynchronously for incoming messages and return them. Stop waiting if
   * <code>null</code> is returned.
   *
   * @param aSPS
   *        The mode to use. May not be <code>null</code>.
   * @return <code>null</code> if the socket is closed
   * @param <DATATYPE>
   *        The data type to receive
   */
  @Nullable
  private static <DATATYPE> DATATYPE _receiveMessage (@Nonnull final MockPubSub aSPS)
  {
    @SuppressWarnings ("resource")
    final ZMQSocket aSubscriberDataSocket = aSPS.getExistingSubscriberDataSocket ();
    if (aSubscriberDataSocket.isClosed ())
    {
      s_aLogger.info ("Subscriber socket for " + aSPS.getName () + " was already closed!");
      return null;
    }

    // Read data from socket
    final byte [] aData = aSubscriberDataSocket.recv (0);
    if (aData == null)
    {
      s_aLogger.info ("Subscriber socket for " + aSPS.getName () + " received an empty paket!");
      return null;
    }

    if (Arrays.equals (CMockZMQ.SHUTDOWN, aData))
    {
      s_aLogger.info ("Shutdown of subscriber for " + aSPS);
      aSubscriberDataSocket.close ();
      return null;
    }

    s_aLogger.info ("Subscriber socket for " + aSPS.getName () + " received " + aData.length + " bytes");
    return ZMQHelper.getDeserializedObject (aData);
  }

  /**
   * Must be called by subscriber upon startup
   *
   * @param aSPS
   *        Mode. May not be <code>null</code>.
   * @param aConsumer
   *        The consumer to handle incoming data.
   * @param <DATATYPE>
   *        The data type to subscribe to
   */
  public static <DATATYPE> void init (@Nonnull final MockPubSub aSPS,
                                      @Nonnull final Consumer <? super DATATYPE> aConsumer)
  {
    ValueEnforcer.notNull (aConsumer, "Consumer");

    // Asynchronous for all modes
    ZMQWorkerPool.getInstance ().run ( () -> {
      aSPS.createSubscriberDataSocket ();

      s_aLogger.info ("Notifying publisher " + aSPS.getName ());
      try (ZMQSocket aSocket = aSPS.createSubscriberSyncSocket ())
      {
        // - send a synchronization request
        aSocket.send (ArrayHelper.EMPTY_BYTE_ARRAY, 0);

        // - wait for synchronization reply
        aSocket.recv (0);
      }
      s_aLogger.info ("Subscriber notified publisher " + aSPS.getName () + "!");
    }).thenRun ( () -> {
      // Receive also asynchronous
      ZMQWorkerPool.getInstance ().run ( () -> {
        while (true)
        {
          final DATATYPE aInvoiceData = _receiveMessage (aSPS);
          if (aInvoiceData == null)
            break;

          // Call provided consumer
          aConsumer.accept (aInvoiceData);
        }
      });
    });
  }
}
