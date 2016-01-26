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
package com.helger.cipa.transport.start.jmsreceiver.jms;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.helger.cipa.transport.start.jmsreceiver.config.JMSReceiverConfig;
import com.helger.commons.factory.IFactory;
import com.helger.jms.JMSFactory;

/**
 * This is the special ActiveMQ JMS factory that reads the connection string
 * from the configuration file.
 *
 * @author Philip Helger
 */
@ThreadSafe
public final class ActiveMQJMSFactory extends JMSFactory
{
  public ActiveMQJMSFactory ()
  {
    // Factory for ConnectionFactory :)
    super (new IFactory <ConnectionFactory> ()
    {
      @Nonnull
      public ConnectionFactory create ()
      {
        final String sConnectionString = JMSReceiverConfig.getConnectionString ();
        return new ActiveMQConnectionFactory (sConnectionString);
      }
    });
  }
}
