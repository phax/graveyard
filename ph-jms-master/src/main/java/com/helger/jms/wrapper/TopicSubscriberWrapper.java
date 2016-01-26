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
import javax.jms.TopicSubscriber;

/**
 * Wrapped class for a JMS {@link TopicSubscriber}.
 * 
 * @author Philip Helger
 */
public class TopicSubscriberWrapper extends MessageConsumerWrapper implements TopicSubscriber
{
  public TopicSubscriberWrapper (@Nonnull final JMSWrapper aWrapper, @Nonnull final TopicSubscriber aWrapped)
  {
    super (aWrapper, aWrapped);
  }

  /**
   * @return The wrapped object. Never <code>null</code>.
   */
  @Override
  @Nonnull
  protected TopicSubscriber getWrapped ()
  {
    return (TopicSubscriber) super.getWrapped ();
  }

  public Topic getTopic () throws JMSException
  {
    return getWrapped ().getTopic ();
  }

  public boolean getNoLocal () throws JMSException
  {
    return getWrapped ().getNoLocal ();
  }
}
