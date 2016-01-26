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
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

/**
 * Wrapped class for a JMS {@link TopicSession}.
 * 
 * @author Philip Helger
 */
public class TopicSessionWrapper extends AbstractSessionWrapper implements TopicSession
{
  public TopicSessionWrapper (@Nonnull final JMSWrapper aWrapper, @Nonnull final TopicSession aWrapped)
  {
    super (aWrapper, aWrapped);
  }

  /**
   * @return The wrapped object. Never <code>null</code>.
   */
  @Override
  @Nonnull
  protected TopicSession getWrapped ()
  {
    return (TopicSession) super.getWrapped ();
  }

  @Nonnull
  public TopicSubscriber createSubscriber (final Topic topic) throws JMSException
  {
    final TopicSubscriber aTopicSubscriber = getWrapped ().createSubscriber (topic);
    return getWrapper ().wrapTopicSubscriber (aTopicSubscriber);
  }

  @Nonnull
  public TopicSubscriber createSubscriber (final Topic topic, final String messageSelector, final boolean noLocal) throws JMSException
  {
    final TopicSubscriber aTopicSubscriber = getWrapped ().createSubscriber (topic, messageSelector, noLocal);
    return getWrapper ().wrapTopicSubscriber (aTopicSubscriber);
  }

  @Nonnull
  public TopicPublisher createPublisher (final Topic topic) throws JMSException
  {
    final TopicPublisher aTopicPublisher = getWrapped ().createPublisher (topic);
    return getWrapper ().wrapTopicPublisher (aTopicPublisher);
  }
}
