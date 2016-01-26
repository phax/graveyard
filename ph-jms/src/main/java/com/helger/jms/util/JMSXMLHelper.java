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
package com.helger.jms.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Session;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.helger.commons.microdom.IMicroDocument;
import com.helger.commons.microdom.IMicroNode;
import com.helger.commons.microdom.serialize.MicroReader;
import com.helger.commons.microdom.serialize.MicroWriter;
import com.helger.commons.xml.serialize.read.DOMReader;
import com.helger.commons.xml.serialize.write.EXMLSerializeIndent;
import com.helger.commons.xml.serialize.write.IXMLWriterSettings;
import com.helger.commons.xml.serialize.write.XMLWriter;
import com.helger.commons.xml.serialize.write.XMLWriterSettings;
import com.helger.jms.stream.BytesMessageInputStream;
import com.helger.jms.stream.BytesMessageOutputStream;

/**
 * Utility class to easily serialize the messages of this
 *
 * @author Philip Helger
 */
@Immutable
public final class JMSXMLHelper
{
  /** The XML writer settings to use. As small as possible. */
  private static final IXMLWriterSettings XWS = new XMLWriterSettings ().setIndent (EXMLSerializeIndent.NONE);

  private JMSXMLHelper ()
  {}

  /**
   * Create a message from the passed XML node
   *
   * @param aSession
   *        The JMS session to use. May not be <code>null</code>.
   * @param aNode
   *        The XML node to serialize. May not be <code>null</code>.
   * @return The created Message object and never <code>null</code>.
   * @throws JMSException
   *         In case some JMS stuff goes wrong
   */
  @Nonnull
  public static BytesMessage createMessageForXML (@Nonnull final Session aSession, @Nonnull final Node aNode) throws JMSException
  {
    // Create the message
    final BytesMessage aMsg = aSession.createBytesMessage ();
    // Serialize XML to BytesMessage
    XMLWriter.writeToStream (aNode, new BytesMessageOutputStream (aMsg), XWS);
    return aMsg;
  }

  /**
   * Create a message from the passed XML micro node
   *
   * @param aSession
   *        The JMS session to use. May not be <code>null</code>.
   * @param aNode
   *        The XML micro node to serialize. May not be <code>null</code>.
   * @return The created Message object and never <code>null</code>.
   * @throws JMSException
   *         In case some JMS stuff goes wrong
   */
  @Nonnull
  public static BytesMessage createMessageForXML (@Nonnull final Session aSession, @Nonnull final IMicroNode aNode) throws JMSException
  {
    // Create the message
    final BytesMessage aMsg = aSession.createBytesMessage ();
    // Serialize XML to BytesMessage
    MicroWriter.writeToStream (aNode, new BytesMessageOutputStream (aMsg), XWS);
    return aMsg;
  }

  /**
   * Read the {@link BytesMessage} and convert it to an XML {@link Document}.
   *
   * @param aMsg
   *        The message to read from. May not be <code>null</code>.
   * @return <code>null</code> if converting the message to an XML failed.
   */
  @Nullable
  public static Document createXMLFromMessage (@Nonnull final BytesMessage aMsg)
  {
    try
    {
      return DOMReader.readXMLDOM (new BytesMessageInputStream (aMsg));
    }
    catch (final SAXException ex)
    {
      return null;
    }
  }

  /**
   * Read the {@link BytesMessage} and convert it to an XML
   * {@link IMicroDocument}.
   *
   * @param aMsg
   *        The message to read from. May not be <code>null</code>.
   * @return <code>null</code> if converting the message to an XML failed.
   */
  @Nullable
  public static IMicroDocument createMicroXMLFromMessage (@Nonnull final BytesMessage aMsg)
  {
    return MicroReader.readMicroXML (new BytesMessageInputStream (aMsg));
  }
}
