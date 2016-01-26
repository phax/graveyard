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
import javax.jms.Session;
import javax.jms.XASession;
import javax.transaction.xa.XAResource;

/**
 * Wrapped class for a JMS {@link XASession}.
 * 
 * @author Philip Helger
 */
public abstract class AbstractXASessionWrapper extends AbstractSessionWrapper implements XASession
{
  public AbstractXASessionWrapper (@Nonnull final JMSWrapper aWrapper, @Nonnull final XASession aWrapped)
  {
    super (aWrapper, aWrapped);
  }

  /**
   * @return The wrapped object. Never <code>null</code>.
   */
  @Override
  @Nonnull
  protected XASession getWrapped ()
  {
    return (XASession) super.getWrapped ();
  }

  public Session getSession () throws JMSException
  {
    return getWrapped ().getSession ();
  }

  public XAResource getXAResource ()
  {
    return getWrapped ().getXAResource ();
  }
}
