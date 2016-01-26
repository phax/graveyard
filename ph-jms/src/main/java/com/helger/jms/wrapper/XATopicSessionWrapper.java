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
import javax.jms.TopicSession;
import javax.jms.XATopicSession;

/**
 * Wrapped class for a JMS {@link XATopicSession}.
 * 
 * @author Philip Helger
 */
public class XATopicSessionWrapper extends AbstractXASessionWrapper implements XATopicSession
{
  public XATopicSessionWrapper (@Nonnull final JMSWrapper aWrapper, @Nonnull final XATopicSession aWrapped)
  {
    super (aWrapper, aWrapped);
  }

  /**
   * @return The wrapped object. Never <code>null</code>.
   */
  @Override
  @Nonnull
  protected XATopicSession getWrapped ()
  {
    return (XATopicSession) super.getWrapped ();
  }

  public TopicSession getTopicSession () throws JMSException
  {
    return getWrapped ().getTopicSession ();
  }
}
