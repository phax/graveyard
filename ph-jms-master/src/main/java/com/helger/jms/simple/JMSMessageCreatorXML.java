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
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Session;

import org.w3c.dom.Node;

import com.helger.commons.ValueEnforcer;
import com.helger.jms.util.JMSXMLHelper;

/**
 * Special {@link IJMSMessageCreator} for easily sending DOM nodes as
 * {@link BytesMessage}
 *
 * @author Philip Helger
 */
public class JMSMessageCreatorXML implements IJMSMessageCreator
{
  private final Node m_aNode;

  public JMSMessageCreatorXML (@Nonnull final Node aNode)
  {
    ValueEnforcer.notNull (aNode, "Node");
    m_aNode = aNode;
  }

  /**
   * @return The node passed in the constructor. Never <code>null</code>.
   */
  @Nonnull
  public Node getNode ()
  {
    return m_aNode;
  }

  @Nonnull
  public BytesMessage createMessage (@Nonnull final Session aSession) throws JMSException
  {
    return JMSXMLHelper.createMessageForXML (aSession, m_aNode);
  }
}
