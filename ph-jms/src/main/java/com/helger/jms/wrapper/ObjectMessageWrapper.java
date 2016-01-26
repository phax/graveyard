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

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;

/**
 * Wrapped class for a JMS {@link ObjectMessage}.
 * 
 * @author Philip Helger
 */
public class ObjectMessageWrapper extends MessageWrapper implements ObjectMessage
{
  public ObjectMessageWrapper (@Nonnull final JMSWrapper aWrapper, @Nonnull final ObjectMessage aWrapped)
  {
    super (aWrapper, aWrapped);
  }

  /**
   * @return The wrapped object. Never <code>null</code>.
   */
  @Override
  @Nonnull
  protected ObjectMessage getWrapped ()
  {
    return (ObjectMessage) super.getWrapped ();
  }

  public void setObject (final Serializable object) throws JMSException
  {
    getWrapped ().setObject (object);
  }

  public Serializable getObject () throws JMSException
  {
    return getWrapped ().getObject ();
  }
}
