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
package com.helger.jms.wrapper;

import javax.annotation.Nonnull;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.jms.TopicPublisher;

/**
 * Wrapped class for a JMS {@link TopicPublisher}.
 * 
 * @author Philip Helger
 */
public class TopicPublisherWrapper extends MessageProducerWrapper implements TopicPublisher
{
  public TopicPublisherWrapper (@Nonnull final JMSWrapper aWrapper, @Nonnull final TopicPublisher aWrapped)
  {
    super (aWrapper, aWrapped);
  }

  /**
   * @return The wrapped object. Never <code>null</code>.
   */
  @Override
  @Nonnull
  protected TopicPublisher getWrapped ()
  {
    return (TopicPublisher) super.getWrapped ();
  }

  public Topic getTopic () throws JMSException
  {
    return getWrapped ().getTopic ();
  }

  public void publish (final Message message) throws JMSException
  {
    getWrapped ().publish (message);
  }

  public void publish (final Message message, final int deliveryMode, final int priority, final long timeToLive) throws JMSException
  {
    getWrapped ().publish (message, deliveryMode, priority, timeToLive);
  }

  public void publish (final Topic topic, final Message message) throws JMSException
  {
    getWrapped ().publish (topic, message);
  }

  public void publish (final Topic topic,
                       final Message message,
                       final int deliveryMode,
                       final int priority,
                       final long timeToLive) throws JMSException
  {
    getWrapped ().publish (topic, message, deliveryMode, priority, timeToLive);
  }
}
