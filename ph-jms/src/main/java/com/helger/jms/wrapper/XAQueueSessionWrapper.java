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
import javax.jms.QueueSession;
import javax.jms.XAQueueSession;

/**
 * Wrapped class for a JMS {@link XAQueueSession}.
 * 
 * @author Philip Helger
 */
public class XAQueueSessionWrapper extends AbstractXASessionWrapper implements XAQueueSession
{
  public XAQueueSessionWrapper (@Nonnull final JMSWrapper aWrapper, @Nonnull final XAQueueSession aWrapped)
  {
    super (aWrapper, aWrapped);
  }

  /**
   * @return The wrapped object. Never <code>null</code>.
   */
  @Override
  @Nonnull
  protected XAQueueSession getWrapped ()
  {
    return (XAQueueSession) super.getWrapped ();
  }

  public QueueSession getQueueSession () throws JMSException
  {
    return getWrapped ().getQueueSession ();
  }
}
