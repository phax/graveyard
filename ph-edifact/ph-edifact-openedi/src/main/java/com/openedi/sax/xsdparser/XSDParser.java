/**
 * Free Message Converter Copyleft 2007 - 2014 Matthias Fricke mf@sapstern.com
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
package com.openedi.sax.xsdparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.openedi.object.AbstractObject;
import com.openedi.object.AdvancedObject;
import com.openedi.object.ComplexObject;
import com.openedi.object.DataObject;
import com.openedi.object.MetaObject;
import com.openedi.object.SegmentGroupObject;
import com.openedi.object.SegmentObject;

public class XSDParser extends DefaultHandler
{
  private SAXParser saxParser = null;
  private DefaultHandler handler = null;
  private SAXParserFactory theFactory = null;
  // private Hashtable<String, AbstractObject> theTab = null;
  private AbstractObject theCurrentObject = null;
  private boolean isGroupStart;
  private String theNameOfCurrentSegmentGroup;
  private Stack <String> theStack = null;
  private List <String> theRefList = null;
  private List <AbstractObject> theXSDHierarcyList = null;

  public XSDParser () throws ParserConfigurationException, SAXException
  {
    super ();
    theXSDHierarcyList = new LinkedList<> ();
    theFactory = SAXParserFactory.newInstance ();
    theFactory.setNamespaceAware (true);
    theFactory.setValidating (false);
    saxParser = theFactory.newSAXParser ();
    // theTab = new Hashtable<String, AbstractObject>();
    theStack = new Stack<> ();
    handler = this;

  }

  public List <AbstractObject> parse (final InputStream in) throws SAXException, IOException
  {
    saxParser.parse (in, handler);
    return theXSDHierarcyList;
  }

  @Override
  public void startDocument () throws SAXException
  {
    // emit("<?xml version='1.0' encoding='"+encoding+"'?>");
  }

  @Override
  public void endDocument () throws SAXException
  {
    // nl ();
  }

  @Override
  public void startElement (final String namespaceURI,
                            final String sName, // simple name (localName)
                            final String qName, // qualified name
                            final Attributes attrs) throws SAXException
  {
    String eName = sName; // element name
    if ("".equals (eName))
      eName = qName; // namespaceAware = false
    if (eName.equals ("element"))
    {
      for (int i = 0; i < attrs.getLength (); i++)
      {
        final String theName = attrs.getValue (i);
        if (attrs.getLocalName (i).equals ("name") &&
            (theName.startsWith ("EDI") || theName.startsWith ("M_") || theName.startsWith ("G_")))
        {

          System.out.println (attrs.getLocalName (i) + ": " + theName);
          if (theCurrentObject != null)
          {
            // theTab.put(theCurrentObject.theName, theCurrentObject);
            theXSDHierarcyList.add (theCurrentObject);
          }
          theCurrentObject = setupCurrentObject (theName);

        }
        if (attrs.getLocalName (i).equals ("ref"))
        {
          System.out.println (attrs.getLocalName (i) + ": " + theName);
          final AdvancedObject theAdvancedObject = (AdvancedObject) theCurrentObject;
          theAdvancedObject.theMemberList.add (setupCurrentObject (theName));
          // Referenzlste der aktuellen Segmentgruppe fortschreiben
          theRefList.add (theName);
        }

      }

    }
  }

  /**
   * @param theName
   * @return
   */
  private AbstractObject setupCurrentObject (final String theName)
  {
    AbstractObject theObject = null;

    if (theName.startsWith ("EDI") || theName.startsWith ("M_"))
    {
      theObject = new MetaObject (theName, theStack);
      isGroupStart = true;
      theNameOfCurrentSegmentGroup = theName;
      theStack.push (theName);

      // Referenzliste zuruecksetzen
      theRefList = new LinkedList<> ();

    }
    if (theName.startsWith ("G_"))
    {
      theObject = new SegmentGroupObject (theName, theStack);
      isGroupStart = true;
      theNameOfCurrentSegmentGroup = theName;
      // Die Refliste der vorigen Segmentgruppe abloopen und schauen, ob diese
      // Segmentgruppe ein Kind der vorigen Segmentgruppe ist
      // Wenn nicht, so muss die vorige Segmentgruppe vorher vom Stack gepoppt
      // werden
      boolean isFound = false;
      final Iterator <String> theRefListIterator = theRefList.iterator ();
      while (theRefListIterator.hasNext ())
      {
        final String theCurrentName = theRefListIterator.next ();
        if (theCurrentName.equals (theName))
        {
          isFound = true;
          break;
        }
      }
      if (!isFound)
        theStack.pop ();
      // aktuelle segmentgruppe auf den Stack pushen
      theStack.push (theName);

    }
    if (theName.startsWith ("S_"))
    {
      theObject = new SegmentObject (theName, theNameOfCurrentSegmentGroup, isGroupStart, theStack);
      isGroupStart = false;
    }
    if (theName.startsWith ("C_"))
    {
      theObject = new ComplexObject (theName, theStack);
    }
    if (theName.startsWith ("D_"))
    {
      theObject = new DataObject (theName, theStack);
    }
    return theObject;
  }

  @Override
  public void endElement (final String namespaceURI,
                          final String sName, // simple name
                          final String qName // qualified name
  ) throws SAXException
  {
    String eName = sName; // element name
    if ("".equals (eName))
      eName = qName; // namespaceAware = false

  }

  /**
   * Contains the real contents between the XML tags
   */
  @Override
  public void characters (final char buf[], final int offset, final int len) throws SAXException
  {
    // String s = new String (buf, offset, len);

  }

  @Override
  public void ignorableWhitespace (final char buf[], final int offset, final int Len) throws SAXException
  {
    // nl ();
  }

}
