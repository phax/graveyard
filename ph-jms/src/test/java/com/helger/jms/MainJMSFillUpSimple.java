/**
 * Copyright (C) 2014-2015 Philip Helger (www.helger.com)
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
package com.helger.jms;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.helger.commons.concurrent.ExtendedDefaultThreadFactory;
import com.helger.commons.concurrent.ManagedExecutorService;
import com.helger.commons.timing.StopWatch;

/**
 * Hello world!
 */
public class MainJMSFillUpSimple
{
  private static final MockActiveMQJMSFactory s_aFactory = new MockActiveMQJMSFactory ();

  public static void main (final String [] args) throws Exception
  {
    final StopWatch aOSW = StopWatch.createdStarted ();

    final int nMax = 1024 * 2;
    final StopWatch aSW = StopWatch.createdStarted ();
    final ExecutorService aESProduce = Executors.newFixedThreadPool (10, new ExtendedDefaultThreadFactory ("producers"));
    for (int i = 0; i < nMax; ++i)
      aESProduce.submit (new MockProducer ());
    final long nMSP = aSW.stopAndGetMillis ();
    System.out.println ("Production of " + nMax + " messages took " + nMSP + "ms");

    aSW.restart ();
    final ExecutorService aESConsume = Executors.newFixedThreadPool (10, new ExtendedDefaultThreadFactory ("consumers"));
    for (int i = 0; i < nMax; ++i)
      aESConsume.submit (new MockConsumer ());
    final long nMSC = aSW.stopAndGetMillis ();
    System.out.println ("Consumption of " + nMax + " messages took " + nMSC + "ms");

    ManagedExecutorService.shutdownAndWaitUntilAllTasksAreFinished (aESProduce);
    ManagedExecutorService.shutdownAndWaitUntilAllTasksAreFinished (aESConsume);

    System.out.println ("Overall time " + aOSW.stopAndGetMillis () + "ms");

    s_aFactory.shutdown ();
  }

  public static class MockProducer implements Runnable
  {
    private static final AtomicInteger m_aCounter = new AtomicInteger (0);

    public void run ()
    {
      Connection connection = null;
      try
      {
        // Create a Connection
        connection = s_aFactory.createConnection ();

        // Create a Session
        final Session session = connection.createSession (false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
        final Queue destination = session.createQueue ("TEST.FOO");

        // Create a MessageProducer from the Session to the Topic or Queue
        final MessageProducer producer = session.createProducer (destination);
        producer.setDeliveryMode (DeliveryMode.NON_PERSISTENT);

        // Create a messages
        final TextMessage message = session.createTextMessage ("Hello world " + m_aCounter.incrementAndGet ());

        // Tell the producer to send the message
        producer.send (message);

        // No commit for non-transacted sessions
      }
      catch (final JMSException ex)
      {
        ex.printStackTrace ();
      }
      finally
      {
        JMSHelper.close (connection);
      }
    }
  }

  public static class MockConsumer implements Runnable
  {
    public void run ()
    {
      Connection connection = null;
      try
      {
        // Create a Connection
        connection = s_aFactory.createConnection ();

        // Create a Session
        final Session session = connection.createSession (false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
        final Destination destination = session.createQueue ("TEST.FOO");

        // Create a MessageConsumer from the Session to the Topic or Queue
        final MessageConsumer consumer = session.createConsumer (destination);

        // Wait for a message
        final Message message = consumer.receive ();
        assert message instanceof TextMessage;
        ((TextMessage) message).getText ();

        // No commit for non-transacted sessions
      }
      catch (final JMSException ex)
      {
        ex.printStackTrace ();
      }
      finally
      {
        JMSHelper.close (connection);
      }
    }
  }
}
