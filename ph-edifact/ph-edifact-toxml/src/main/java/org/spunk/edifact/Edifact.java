/**
 * Copyright 2012 A. Nonymous
 * Copyright (C) 2016 Philip Helger (www.helger.com)
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
package org.spunk.edifact;

import java.util.List;

import org.spunk.edifact.exception.EdifactException;
import org.spunk.edifact.exception.InterchangeHeaderException;
import org.spunk.edifact.exception.MessageHeaderException;
import org.spunk.edifact.node.Component;
import org.spunk.edifact.node.DataElement;
import org.spunk.edifact.node.Segment;
import org.spunk.edifact.node.SegmentName;
import org.spunk.edifact.node.SyntaxChars;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.helger.commons.collection.ext.ICommonsList;
import com.helger.xml.XMLFactory;

/**
 * EDIFACT.
 */
public final class Edifact
{
  /**
   * Convert current Segment and following nodes to EDIFACT.
   */
  public static final boolean CONVERT_SEGMENT_TO_EDIFACT_ALL = false;

  /**
   * Convert only the current Segment to EDIFACT.
   */
  public static final boolean CONVERT_SEGMENT_TO_EDIFACT_CURRENT_ONLY = true;

  private static final String XML_NODE_NAME_DOCUMENT = "document";
  private static final String XML_NODE_NAME_ELEMENT = "e";
  private static final String XML_NODE_NAME_COMPONENT = "c";

  /**
   * Constructor.
   */
  private Edifact ()
  {
    // Hidden constructor
  }

  /**
   * Convert this and all following nodes to the content of a DOM tree.
   *
   * @param segment
   *        Segment node from which to start the serialization.
   * @return DOM tree for this and all following nodes as children of an element
   *         with the name <tt>'document'</tt>.
   */
  public static Document newDocument (final Segment segment)
  {
    // Create a new document to hold all the converted EDIFACT
    final Document document = XMLFactory.newDocument ();

    // Root element of new document is simply called 'document'
    final Element documentElement = document.createElement (Edifact.XML_NODE_NAME_DOCUMENT);
    document.appendChild (documentElement);

    // Conversion starts at segment passed as input parameter
    Segment current = segment;

    // Iterate over all EDIFACT segments
    while (current != null)
    {
      Edifact.segmentToXml (documentElement, current);
      current = current.getNext ();
    }

    return document;
  }

  /**
   * Get the number of interchanges in the communication.
   *
   * @param segment
   *        First segment of a communication, for which to obtain an interchange
   *        count.
   * @return number of interchanges in the communication.
   */
  public static int getInterchangeCount (final Segment segment)
  {
    int interchangeCount = 0;

    Segment current = segment;
    String currentSegmentName;

    try
    {
      currentSegmentName = current.getSegmentName ();
    }
    catch (final NullPointerException cause)
    {
      throw new EdifactException (cause);
    }

    if (SegmentName.UNB.matches (currentSegmentName))
    {
      interchangeCount++;
    }

    do
    {
      final Segment unbSegment = Finder.find (current, SegmentName.UNB);

      if (unbSegment != null)
      {
        interchangeCount++;
      }

      current = unbSegment;
    } while (current != null);

    return interchangeCount;
  }

  /**
   * Get the number of messages in the communication.
   *
   * @param segment
   *        First segment of a communication, for which to obtain a message
   *        count.
   * @return number of messages in the communication.
   */
  public static int getMessageCount (final Segment segment)
  {
    int messageCount = 0;

    Segment current = segment;
    String currentSegmentName;

    try
    {
      currentSegmentName = current.getSegmentName ();
    }
    catch (final NullPointerException cause)
    {
      throw new EdifactException (cause);
    }

    if (SegmentName.UNH.matches (currentSegmentName))
    {
      messageCount++;
    }

    do
    {
      final Segment unhSegment = Finder.find (current, SegmentName.UNH);

      if (unhSegment != null)
      {
        messageCount++;
      }

      current = unhSegment;
    } while (current != null);

    return messageCount;
  }

  /**
   * Convert the binding back to an EDIFACT representation. All nodes starting
   * from the current node are converted.
   *
   * @param segment
   *        First segment starting at which to convert to EDIFACT.
   * @return EDIFACT representation for all nodes starting with the current one.
   */
  public static String toEdifact (final Segment segment)
  {
    String edifact;

    try
    {
      edifact = Edifact.toEdifact (segment, Edifact.CONVERT_SEGMENT_TO_EDIFACT_ALL);
    }
    catch (final NullPointerException cause)
    {
      throw new EdifactException (cause);
    }

    return edifact;
  }

  /**
   * Convert the binding back to an EDIFACT representation.
   *
   * @param segment
   *        First segment starting at which to convert to EDIFACT.
   * @param thisSegmentOnly
   *        Only generate EDIFACT for the current node if <tt>true</tt>.
   * @return EDIFACT representation.
   */
  public static String toEdifact (final Segment segment, final boolean thisSegmentOnly)
  {
    String returnValue = null;

    if (segment != null)
    {
      returnValue = Edifact.doToEdifact (segment, thisSegmentOnly);
    }

    return returnValue;
  }

  /**
   * Get the message type name from the message header.
   *
   * @param head
   *        Segment from which to start a search for a UNH segment, includin the
   *        specified segment.
   * @return Message type name.
   */
  public static String getMessageTypeName (final Segment head)
  {
    Segment unhSegment = head;

    // Message type name should be obtained from the UNH segment
    if (head != null && !SegmentName.UNH.matches (head.getSegmentName ()))
    {
      unhSegment = Finder.find (head, SegmentName.UNH);
    }

    // UNH segment should contain at least 2 elements (name+data)
    final String unhEdifact = Edifact.toEdifact (unhSegment);

    if (unhSegment == null || unhSegment.getDataElements ().size () < 2)
    {
      throw new InterchangeHeaderException (unhEdifact);
    }

    // Message type name should be stored in a single element
    final DataElement messageType = unhSegment.getDataElements ().get (1);
    final ICommonsList <Component> messageTypeComponents = messageType.getAllComponents ();

    // UNH should contains at least 2 components
    if (messageTypeComponents.size () < 2)
    {
      throw new MessageHeaderException ();
    }

    // Format Segment text to a message type string.
    final String key = Edifact.formatMessageTypeName (messageTypeComponents);

    return key;
  }

  private static String doToEdifact (final Segment segment, final boolean thisSegmentOnly)
  {
    String returnValue;

    // Convert Segment back to EDIFACT text
    final StringBuilder buffer = new StringBuilder ();
    Segment current = segment;

    do
    {
      // UNA segment needs special handling
      final String currentSegmentName = current.getSegmentName ();

      if (SegmentName.UNA.matches (currentSegmentName))
      {
        current = Edifact.emitEdifactSegmentUna (buffer, current);
      }
      else
      {
        current = Edifact.ediEdifactSegmentOther (buffer, current, currentSegmentName);
      }
    } while (current != null && !thisSegmentOnly);

    returnValue = buffer.toString ();

    return returnValue;
  }

  private static String formatMessageTypeName (final List <Component> components)
  {
    String returnValue = null;

    final StringBuilder buffer = new StringBuilder ();

    for (int i = 0; components != null && i < components.size (); i++)
    {
      Edifact.ediEdifactDataElementComponent (buffer, components, i);
    }

    returnValue = buffer.toString ();

    return returnValue;
  }

  private static void ediEdifactDataElementComponent (final StringBuilder buffer,
                                                      final List <Component> components,
                                                      final int i)
  {
    final Component component = components.get (i);

    if (i != 0)
    {
      buffer.append (SyntaxChars.COMPONENT_SEPARATOR.getChar ());
    }

    final char [] text = component.getText ().toCharArray ();

    for (int j = 0; j < text.length; j++)
    {
      Edifact.emitEdifactEscapeComponentChars (buffer, text, j);
    }
  }

  private static void emitEdifactEscapeComponentChars (final StringBuilder buffer, final char [] text, final int j)
  {
    final char character = text[j];

    if (SyntaxChars.isSyntaxChar (character))
    {
      buffer.append (SyntaxChars.RELEASE_CHARACTER);
    }

    buffer.append (character);
  }

  private static Segment emitEdifactSegmentUna (final StringBuilder buffer, final Segment current)
  {
    Segment returnValue;

    buffer.append (SegmentName.UNA);
    buffer.append (current.get (0, 0));
    returnValue = current.getNext ();

    return returnValue;
  }

  private static Segment ediEdifactSegmentOther (final StringBuilder buffer,
                                                 final Segment current,
                                                 final String currentSegmentName)
  {
    Segment returnValue;

    buffer.append (currentSegmentName);

    for (final DataElement EdifactDataElement : current.getDataElements ())
    {
      buffer.append (SyntaxChars.ELEMENT_SEPARATOR);

      final ICommonsList <Component> components = EdifactDataElement.getAllComponents ();

      for (int i = 0; i < components.size (); i++)
      {
        Edifact.ediEdifactDataElementComponent (buffer, components, i);
      }
    }

    returnValue = current.getNext ();
    buffer.append (SyntaxChars.SEGMENT_TERMINATOR);

    return returnValue;
  }

  private static void segmentToXml (final Element documentNode, final Segment current)
  {
    final Document document = documentNode.getOwnerDocument ();

    // Create/add a node for each segment
    final String segmentName = current.getSegmentName ();
    final Element element = document.createElement (segmentName);
    Edifact.elementToXml (element, current);

    documentNode.appendChild (element);
  }

  private static void elementToXml (final Element segmentNode, final Segment current)
  {
    final Document document = segmentNode.getOwnerDocument ();

    // Create/add a node for each element in the EDIFACT segment
    final List <DataElement> elements = current.getDataElements ();

    for (final DataElement dataElement : elements)
    {
      Element elementNode;

      elementNode = document.createElement (Edifact.XML_NODE_NAME_ELEMENT);
      Edifact.componentToXml (elementNode, dataElement);

      segmentNode.appendChild (elementNode);
    }
  }

  private static void componentToXml (final Element elementNode, final DataElement dataElement)
  {
    final Document document = elementNode.getOwnerDocument ();

    // Create/add a node for each component in the EDIFACT element
    final ICommonsList <Component> dataElementComponents = dataElement.getAllComponents ();

    for (final Component dataElementComponent : dataElementComponents)
    {
      Element componentNode;

      componentNode = document.createElement (Edifact.XML_NODE_NAME_COMPONENT);
      final String text = dataElementComponent.getText ();
      final Text textNode = document.createTextNode (text);
      componentNode.appendChild (textNode);

      elementNode.appendChild (componentNode);
    }
  }

}
