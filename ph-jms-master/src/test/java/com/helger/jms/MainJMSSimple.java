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

import com.helger.commons.concurrent.ManagedExecutorService;

/**
 * Hello world!
 */
public class MainJMSSimple
{
  private static final MockActiveMQJMSFactory s_aFactory = new MockActiveMQJMSFactory ();

  public static void main (final String [] args) throws Exception
  {
    final ExecutorService aESProduce = Executors.newCachedThreadPool ();
    final ExecutorService aESConsume = Executors.newCachedThreadPool ();
    aESProduce.submit (new MockProducer ());
    aESProduce.submit (new MockProducer ());
    aESConsume.submit (new MockConsumer ());
    Thread.sleep (1000);
    aESConsume.submit (new MockConsumer ());
    aESProduce.submit (new MockProducer ());
    aESConsume.submit (new MockConsumer ());
    aESProduce.submit (new MockProducer ());
    Thread.sleep (1000);
    aESConsume.submit (new MockConsumer ());
    aESProduce.submit (new MockProducer ());
    aESConsume.submit (new MockConsumer ());
    aESConsume.submit (new MockConsumer ());
    aESProduce.submit (new MockProducer ());
    aESProduce.submit (new MockProducer ());
    Thread.sleep (1000);
    aESProduce.submit (new MockProducer ());
    aESConsume.submit (new MockConsumer ());
    aESConsume.submit (new MockConsumer ());
    aESProduce.submit (new MockProducer ());
    aESConsume.submit (new MockConsumer ());
    aESProduce.submit (new MockProducer ());
    aESConsume.submit (new MockConsumer ());
    aESProduce.submit (new MockProducer ());
    aESConsume.submit (new MockConsumer ());
    aESConsume.submit (new MockConsumer ());
    aESProduce.submit (new MockProducer ());
    ManagedExecutorService.shutdownAndWaitUntilAllTasksAreFinished (aESProduce);
    ManagedExecutorService.shutdownAndWaitUntilAllTasksAreFinished (aESConsume);
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
        final Session session = connection.createSession (true, -1);

        // Create the destination (Topic or Queue)
        final Queue destination = session.createQueue ("TEST.FOO");

        // Create a MessageProducer from the Session to the Topic or Queue
        final MessageProducer producer = session.createProducer (destination);
        producer.setDeliveryMode (DeliveryMode.NON_PERSISTENT);

        // Create a messages
        final TextMessage message = session.createTextMessage ("Hello world " + m_aCounter.incrementAndGet ());

        // Tell the producer to send the message
        System.out.println ("Sent message " + message.getText ());
        producer.send (message);
        session.commit ();

        // Clean up
        // Do not close the producer as it is reused
        if (false)
          producer.close ();
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
        final Session session = connection.createSession (true, -1);

        // Create the destination (Topic or Queue)
        final Destination destination = session.createQueue ("TEST.FOO");

        // Create a MessageConsumer from the Session to the Topic or Queue
        final MessageConsumer consumer = session.createConsumer (destination);

        // Wait for a message
        final Message message = consumer.receive ();

        if (message instanceof TextMessage)
        {
          final TextMessage textMessage = (TextMessage) message;
          final String text = textMessage.getText ();
          System.out.println ("Received (TM): " + text);
        }
        else
        {
          System.out.println ("Received (SE): " + message);
        }

        session.commit ();
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
