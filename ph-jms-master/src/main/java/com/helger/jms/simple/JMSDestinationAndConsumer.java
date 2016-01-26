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
package com.helger.jms.simple;

import javax.annotation.Nonnull;
import javax.jms.Destination;
import javax.jms.MessageConsumer;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.string.ToStringGenerator;

/**
 * A pair of JMS destination and JMS message consumer.
 *
 * @author Philip Helger
 */
public class JMSDestinationAndConsumer
{
  private final Destination m_aDestination;
  private final MessageConsumer m_aConsumer;

  /**
   * @param aDestination
   *        Destination. May not be <code>null</code>.
   * @param aConsumer
   *        Message consumer. May not be <code>null</code>.
   */
  public JMSDestinationAndConsumer (@Nonnull final Destination aDestination, @Nonnull final MessageConsumer aConsumer)
  {
    ValueEnforcer.notNull (aDestination, "Destination");
    ValueEnforcer.notNull (aConsumer, "Consumer");

    m_aDestination = aDestination;
    m_aConsumer = aConsumer;
  }

  /**
   * @return The JMS destination. Never <code>null</code>.
   */
  @Nonnull
  public Destination getDestination ()
  {
    return m_aDestination;
  }

  /**
   * @return The JMS message consumer. Never <code>null</code>.
   */
  @Nonnull
  public MessageConsumer getConsumer ()
  {
    return m_aConsumer;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("destination", m_aDestination)
                                       .append ("consumer", m_aConsumer)
                                       .toString ();
  }
}
