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
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * Interface for creating arbitrary JMS messages for sending.
 * 
 * @author Philip Helger
 */
public interface IJMSMessageCreator
{
  /**
   * Create a new JMS message.
   * 
   * @param aSession
   *        The JMS session to use. Never <code>null</code>.
   * @return Never <code>null</code>.
   * @throws JMSException
   *         In case of an error
   */
  @Nonnull
  Message createMessage (@Nonnull Session aSession) throws JMSException;
}
