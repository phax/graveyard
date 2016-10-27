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
package com.openedi.unece.xsd.sax;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;

import com.openedi.unece.xsd.data.CompositeDataDefinition;
import com.openedi.unece.xsd.data.CompositeDefinition;
import com.openedi.unece.xsd.data.SegmentData;
import com.openedi.unece.xsd.data.SegmentStructureData;
import com.openedi.unece.xsd.data.SegmentStructureElement;
import com.openedi.unece.xsd.data.TypeDef;

/**
 * <BR>
 * <BR>
 * <B>Class/Interface Description: </B><BR>
 * Instance class for SAX parsing of UN/ECE EDIFACT message description files to
 * to XSD's Was originally uses for parsing SEF files<BR>
 * <BR>
 * <BR>
 *
 * @author Matthias Fricke
 *         <DT><B>Known Bugs:</B><BR>
 *         <!-- Keep in mind to update method bug lists --> none <BR>
 *         <BR>
 *         <DT><B>History:</B>
 *
 *         <PRE>
 * <!-- Do not use tabs in the history table! Do not extend table width! rel.inc defines release and increment no -->
 * date       name           rel.inc  changes
 * -------------------------------------------------------------------------------------------------
 * 20.02.07   fricke         1.0      created
 * 01.10.13	  fricke		 2.0      swapped from proprietary SEF format to UN/ECE website data descriptions
 * -------------------------------------------------------------------------------------------------
 *         </PRE>
 ****************************************************************/
public class SaxParserUNECEToXSD implements XMLReader
{

  public static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
  public static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
  public static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

  private Transformer transformer;

  private ContentHandler contentHandler;

  private static AttributesImpl attribs = new AttributesImpl ();
  private static AttributesImpl uneceParseAttribs;
  private static String namespaceURI = "";
  private List <SegmentData> m_theSegmentList;
  private List <SegmentStructureData> m_theSegmentStructureList;
  // MFRI bei Aufbau ControlSegments abfragen
  private Map <String, Map <String, String>> m_theDataElementTab;
  private Map <String, CompositeDefinition> m_theCompositeTab;
  // MFRI bei Aufbau ControlSegments abfragen
  private Map <String, SegmentStructureData> m_theSegmentTab;

  private Map <String, Hashtable <String, String>> theElementTypeTab;

  private String m_messageName;
  private String m_messageType;

  static
  {
    attribs.addAttribute (namespaceURI, "", "composite", "CDATA", "true");
  }

  /**
   * The constructor to be used for this SAX parser instance
   */
  public SaxParserUNECEToXSD (final String messageName) throws SAXException
  {
    super ();

    try
    {
      final SAXTransformerFactory saxTransformerFactory = (SAXTransformerFactory) TransformerFactory.newInstance ();

      final TransformerHandler handler = saxTransformerFactory.newTransformerHandler ();
      transformer = handler.getTransformer ();
      transformer.setOutputProperty (OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty (OutputKeys.INDENT, "yes");
      transformer.setOutputProperty (OutputKeys.METHOD, "xml");
      transformer.setOutputProperty (OutputKeys.MEDIA_TYPE, "text/xml");

      uneceParseAttribs = new AttributesImpl ();
      uneceParseAttribs.addAttribute ("http://www.w3.org/2001/XMLSchema",
                                      "xmlns:xsd",
                                      "xmlns:xsd",
                                      "CDATA",
                                      "http://www.w3.org/2001/XMLSchema");
      uneceParseAttribs.addAttribute ("urn:sapstern.com:EDIFACTINTERCHANGE_" +
                                      messageName,
                                      "targetNamespace",
                                      "targetNamespace",
                                      "CDATA",
                                      "urn:sapstern.com:EDIFACTINTERCHANGE_" + messageName);
      uneceParseAttribs.addAttribute ("urn:sapstern.com:EDIFACTINTERCHANGE_" +
                                      messageName,
                                      "xmlns",
                                      "xmlns",
                                      "CDATA",
                                      "urn:sapstern.com:EDIFACTINTERCHANGE_" + messageName);
      theElementTypeTab = new Hashtable<> ();

    }
    catch (final TransformerConfigurationException tce)
    {
      throw new SAXException (tce);
    }
  }

  // ===========================================================
  // XML Reader Interface Implementation
  // ===========================================================
  public void parse (final InputSource source) throws IOException, SAXException
  {
    m_messageName = m_messageName.replace ("D.", "");
    contentHandler.startDocument ();
    contentHandler.startElement (namespaceURI, "xsd:schema", "xsd:schema", uneceParseAttribs);
    final AttributesImpl messageTypeAttribs = new AttributesImpl ();
    messageTypeAttribs.addAttribute ("", "name", "name", "CDATA", m_messageType);
    contentHandler.startElement (namespaceURI, "xsd:element", "xsd:element", messageTypeAttribs);
    contentHandler.startElement (namespaceURI, "xsd:complexType", "xsd:complexType", new AttributesImpl ());
    contentHandler.startElement (namespaceURI, "xsd:sequence", "xsd:sequence", new AttributesImpl ());
    final String theMsegment = "M_" + m_messageName;
    setupSegmentDeclaration (contentHandler, "ref", "ref", "S_UNA", "0", null, true);
    setupSegmentDeclaration (contentHandler, "ref", "ref", "S_UNB", "0", null, true);
    setupSegmentDeclaration (contentHandler, "ref", "ref", "" + theMsegment, null, "unbounded", true);
    setupSegmentDeclaration (contentHandler, "ref", "ref", "S_UNZ", "0", null, true);
    contentHandler.endElement (namespaceURI, "xsd:sequence", "xsd:sequence");
    contentHandler.endElement (namespaceURI, "xsd:complexType", "xsd:complexType");
    contentHandler.endElement (namespaceURI, "xsd:element", "xsd:element");
    // Top level Segment start
    setupSegment (theMsegment, 0);
    setupSegmentStructure ();
    setupContorlSegments ("S_UNA");
    setupContorlSegments ("S_UNB");
    setupContorlSegments ("S_UNZ");
    setupComposite ();
    setupElements ();
    setupElementTypes ();

    contentHandler.endElement (namespaceURI, "xsd:schema", "xsd:schema");
    contentHandler.endDocument ();
  }

  private void setupContorlSegments (final String theSegmentName) throws SAXException
  {
    final AttributesImpl unaTypeAttribs = new AttributesImpl ();
    unaTypeAttribs.addAttribute ("", "name", "name", "CDATA", theSegmentName);
    contentHandler.startElement (namespaceURI, "xsd:element", "xsd:element", unaTypeAttribs);
    contentHandler.startElement (namespaceURI, "xsd:complexType", "xsd:complexType", new AttributesImpl ());
    contentHandler.startElement (namespaceURI, "xsd:sequence", "xsd:sequence", new AttributesImpl ());
    final SegmentStructureData theSegmentStructureData = m_theSegmentTab.get (theSegmentName);

    if (theSegmentStructureData != null)
    {
      for (int i = 0; i < theSegmentStructureData.theMemberList.size (); i++)
      {
        final SegmentStructureElement currentElement = theSegmentStructureData.theMemberList.get (i);
        final AttributesImpl memberTypeAttribs = new AttributesImpl ();
        memberTypeAttribs.addAttribute ("", "ref", "ref", "CDATA", "" + currentElement.theName);
        if (currentElement.isMandatory.equals ("0"))
          memberTypeAttribs.addAttribute ("", "minOccurs", "minOccurs", "CDATA", "0");
        contentHandler.startElement (namespaceURI, "xsd:element", "xsd:element", memberTypeAttribs);

        contentHandler.endElement (namespaceURI, "xsd:element", "xsd:element");
      }
    }

    contentHandler.endElement (namespaceURI, "xsd:sequence", "xsd:sequence");
    contentHandler.endElement (namespaceURI, "xsd:complexType", "xsd:complexType");
    contentHandler.endElement (namespaceURI, "xsd:element", "xsd:element");
    // Composite und Datenelemente, die zum UNA / UNB / und UNZ gehoehren ind
    // die entsprechenden Listen anhaengen
    if (theSegmentStructureData != null)
    {
      for (int i = 0; i < theSegmentStructureData.theMemberList.size (); i++)
      {
        final SegmentStructureElement currentElement = theSegmentStructureData.theMemberList.get (i);

        if (currentElement.theType == TypeDef.TYPE_COMPOSITE)
        {
          final CompositeDefinition theCompositeDef = m_theCompositeTab.get (currentElement.theName);
          if (theCompositeDef != null)
          {
            m_theCompositeTab.put (theCompositeDef.theName, theCompositeDef);
            final Iterator <CompositeDataDefinition> theIterator = theCompositeDef.theDataList.iterator ();
            while (theIterator.hasNext ())
            {
              final CompositeDataDefinition theCompositeDataDef = theIterator.next ();
              final Map <String, String> theDataElementDef = m_theDataElementTab.get (theCompositeDataDef.theName);

              if (theDataElementDef != null)
                m_theDataElementTab.put (theDataElementDef.get ("name"), theDataElementDef);
            }
          }
        }
        if (currentElement.theType == TypeDef.TYPE_DATAELEMENT)
        {
          final Map <String, String> theDataElementDef = m_theDataElementTab.get (currentElement.theName);

          if (theDataElementDef != null)
            m_theDataElementTab.put (theDataElementDef.get ("name"), theDataElementDef);
        }
      }
    }

  }

  private void setupSegmentStructure () throws SAXException
  {
    final Iterator <SegmentStructureData> theSegmentStructureIterator = m_theSegmentStructureList.iterator ();
    while (theSegmentStructureIterator.hasNext ())
    {
      final SegmentStructureData theData = theSegmentStructureIterator.next ();
      final AttributesImpl theAttribs = new AttributesImpl ();
      theAttribs.addAttribute ("", "name", "name", "CDATA", theData.theNameOfSegment);
      contentHandler.startElement (namespaceURI, "xsd:element", "xsd:element", theAttribs);
      if (theData.theDescription != null)
      {
        contentHandler.startElement (namespaceURI, "xsd:annotation", "xsd:annotation", new AttributesImpl ());
        contentHandler.startElement (namespaceURI, "xsd:documentation", "xsd:documentation", new AttributesImpl ());
        contentHandler.characters (theData.theDescription.toCharArray (), 0, theData.theDescription.length ());
        contentHandler.endElement (namespaceURI, "xsd:documentation", "xsd:documentation");
        contentHandler.endElement (namespaceURI, "xsd:annotation", "xsd:annotation");
      }

      contentHandler.startElement (namespaceURI, "xsd:complexType", "xsd:complexType", new AttributesImpl ());
      contentHandler.startElement (namespaceURI, "xsd:sequence", "xsd:sequence", new AttributesImpl ());
      final Iterator <SegmentStructureElement> theListIterator = theData.theMemberList.iterator ();

      while (theListIterator.hasNext ())
      {

        final SegmentStructureElement theCDataDef = theListIterator.next ();

        final AttributesImpl theElemAttribs = new AttributesImpl ();
        theElemAttribs.addAttribute ("", "ref", "ref", "CDATA", "" + theCDataDef.theName);
        if (theCDataDef.isMandatory.equals ("0"))
          theElemAttribs.addAttribute ("", "minOccurs", "minOccurs", "CDATA", "0");
        contentHandler.startElement (namespaceURI, "xsd:element", "xsd:element", theElemAttribs);
        contentHandler.endElement (namespaceURI, "xsd:element", "xsd:element");

      }
      contentHandler.endElement (namespaceURI, "xsd:sequence", "xsd:sequence");
      contentHandler.endElement (namespaceURI, "xsd:complexType", "xsd:complexType");
      contentHandler.endElement (namespaceURI, "xsd:element", "xsd:element");

    }
  }

  private void setupComposite () throws SAXException
  {
    for (final CompositeDefinition theDef : m_theCompositeTab.values ())
    {
      final AttributesImpl theAttribs = new AttributesImpl ();
      theAttribs.addAttribute ("", "name", "name", "CDATA", theDef.theName);
      contentHandler.startElement (namespaceURI, "xsd:element", "xsd:element", theAttribs);
      if (theDef.theDescription != null)
      {
        contentHandler.startElement (namespaceURI, "xsd:annotation", "xsd:annotation", new AttributesImpl ());
        contentHandler.startElement (namespaceURI, "xsd:documentation", "xsd:documentation", new AttributesImpl ());
        contentHandler.characters (theDef.theDescription.toCharArray (), 0, theDef.theDescription.length ());
        contentHandler.endElement (namespaceURI, "xsd:documentation", "xsd:documentation");
        contentHandler.endElement (namespaceURI, "xsd:annotation", "xsd:annotation");
      }
      contentHandler.startElement (namespaceURI, "xsd:complexType", "xsd:complexType", new AttributesImpl ());
      contentHandler.startElement (namespaceURI, "xsd:sequence", "xsd:sequence", new AttributesImpl ());
      final Iterator <CompositeDataDefinition> theListIterator = theDef.theDataList.iterator ();
      while (theListIterator.hasNext ())
      {
        final CompositeDataDefinition theCDataDef = theListIterator.next ();
        final AttributesImpl theElemAttribs = new AttributesImpl ();
        theElemAttribs.addAttribute ("", "ref", "ref", "CDATA", "" + theCDataDef.theName);
        if (theCDataDef.isManadatory.equals ("0"))
          theElemAttribs.addAttribute ("", "minOccurs", "minOccurs", "CDATA", "0");
        contentHandler.startElement (namespaceURI, "xsd:element", "xsd:element", theElemAttribs);
        contentHandler.endElement (namespaceURI, "xsd:element", "xsd:element");

      }
      contentHandler.endElement (namespaceURI, "xsd:sequence", "xsd:sequence");
      contentHandler.endElement (namespaceURI, "xsd:complexType", "xsd:complexType");
      contentHandler.endElement (namespaceURI, "xsd:element", "xsd:element");
    }
  }

  private void setupElements () throws SAXException
  {
    for (final Map <String, String> theCurrentDataElemTab : m_theDataElementTab.values ())
    {
      final AttributesImpl theAttribs = new AttributesImpl ();
      theAttribs.addAttribute ("", "name", "name", "CDATA", theCurrentDataElemTab.get ("name"));

      final String theType = theCurrentDataElemTab.get ("Repr");
      String typeOfAttrib = null;
      String theTypeDefString = null;
      if (theType.startsWith ("an"))
        typeOfAttrib = theTypeDefString = "string";
      else
      {
        typeOfAttrib = "decimal";
        theTypeDefString = "numeric";
      }
      String theLengthDescription = theType.replaceAll ("a", "");
      theLengthDescription = theLengthDescription.replaceAll ("n", "");
      theTypeDefString = theTypeDefString + "1" + theLengthDescription;
      theAttribs.addAttribute ("", "type", "type", "CDATA", "" + theTypeDefString);
      contentHandler.startElement (namespaceURI, "xsd:element", "xsd:element", theAttribs);

      contentHandler.startElement (namespaceURI, "xsd:annotation", "xsd:annotation", new AttributesImpl ());
      contentHandler.startElement (namespaceURI, "xsd:documentation", "xsd:documentation", new AttributesImpl ());
      contentHandler.characters (theCurrentDataElemTab.get ("Desc").toCharArray (),
                                 0,
                                 theCurrentDataElemTab.get ("Desc").length ());
      contentHandler.endElement (namespaceURI, "xsd:documentation", "xsd:documentation");
      contentHandler.endElement (namespaceURI, "xsd:annotation", "xsd:annotation");

      final Hashtable <String, String> theTab = new Hashtable<> ();
      theTab.put ("typedef", theTypeDefString);
      theTab.put ("type", typeOfAttrib);
      String maxLength = theType.replaceAll ("a", "");
      maxLength = maxLength.replaceAll ("n", "");
      maxLength = maxLength.replaceAll ("\\.", "");
      theTab.put ("length", maxLength);

      theElementTypeTab.put (theTypeDefString, theTab);

      contentHandler.endElement (namespaceURI, "xsd:element", "xsd:element");
    }
  }

  private void setupElementTypes () throws SAXException
  {
    for (final Map <String, String> tab : theElementTypeTab.values ())
    {
      setupRestriction (tab.get ("type"), tab.get ("typedef"), tab.get ("length"));
    }
  }

  /**
   * @param theType
   * @param theAttribsRestri
   * @throws SAXException
   */
  private void setupRestriction (final String theType,
                                 final String typeOfAttrib,
                                 final String theLength) throws SAXException
  {
    final AttributesImpl theSimpleTypeAttr = new AttributesImpl ();
    theSimpleTypeAttr.addAttribute ("", "name", "name", "CDATA", typeOfAttrib);
    contentHandler.startElement (namespaceURI, "xsd:simpleType", "xsd:simpleType", theSimpleTypeAttr);
    final AttributesImpl theAttribsRestri = new AttributesImpl ();
    theAttribsRestri.addAttribute ("", "base", "base", "CDATA", "xsd:" + theType);
    contentHandler.startElement (namespaceURI, "xsd:restriction", "xsd:restriction", theAttribsRestri);
    final AttributesImpl theAttribsValueMin = new AttributesImpl ();
    if (theType.equals ("string"))
    {
      theAttribsValueMin.addAttribute ("", "value", "value", "CDATA", "1");
      contentHandler.startElement (namespaceURI, "xsd:minLength", "xsd:minLength", theAttribsValueMin);
      contentHandler.endElement (namespaceURI, "xsd:minLength", "xsd:minLength");
      final AttributesImpl theAttribsValueMax = new AttributesImpl ();
      theAttribsValueMax.addAttribute ("", "value", "value", "CDATA", theLength);
      contentHandler.startElement (namespaceURI, "xsd:maxLength", "xsd:maxLength", theAttribsValueMax);
      contentHandler.endElement (namespaceURI, "xsd:maxLength", "xsd:maxLength");
    } // xsd:totalDigits
    else
    {
      final AttributesImpl theAttribsTotalDigits = new AttributesImpl ();
      theAttribsTotalDigits.addAttribute ("", "value", "value", "CDATA", theLength);
      contentHandler.startElement (namespaceURI, "xsd:totalDigits", "xsd:totalDigits", theAttribsTotalDigits);
      contentHandler.endElement (namespaceURI, "xsd:totalDigits", "xsd:totalDigits");
    }

    contentHandler.endElement (namespaceURI, "xsd:restriction", "xsd:restriction");
    contentHandler.endElement (namespaceURI, "xsd:simpleType", "xsd:simpleType");
  }

  /**
   * @param theSegmentGroupName
   * @param theLevel
   * @throws SAXException
   */
  private void setupSegment (final String theSegmentGroupName, final int theLevel) throws SAXException
  {

    setupSegmentDeclaration (contentHandler, "name", "name", theSegmentGroupName, null, null, false);
    contentHandler.startElement (namespaceURI, "xsd:complexType", "xsd:complexType", new AttributesImpl ());
    contentHandler.startElement (namespaceURI, "xsd:sequence", "xsd:sequence", new AttributesImpl ());

    final List <String> sGlist = new LinkedList<> ();
    for (int i = 0; i < m_theSegmentList.size (); i++)
    {
      final SegmentData theData = m_theSegmentList.get (i);
      if (theData.theLevel == theLevel && theData.theNameOfSegmentGroup.equals (theSegmentGroupName))
      {
        if (theData.theSegmentName.startsWith ("G_"))
          sGlist.add (theData.theSegmentName);

        theData.theLevel = -1;
        m_theSegmentList.set (i, theData);

        if (theData.isMandatory == true)
          setupSegmentDeclaration (contentHandler,
                                   "ref",
                                   "ref",
                                   "" + theData.theSegmentName,
                                   null,
                                   String.valueOf (theData.theCardinality),
                                   true);
        else
          setupSegmentDeclaration (contentHandler,
                                   "ref",
                                   "ref",
                                   "" + theData.theSegmentName,
                                   "0",
                                   String.valueOf (theData.theCardinality),
                                   true);
        //
      }

    }
    contentHandler.endElement (namespaceURI, "xsd:sequence", "xsd:sequence");
    contentHandler.endElement (namespaceURI, "xsd:complexType", "xsd:complexType");
    contentHandler.endElement (namespaceURI, "xsd:element", "xsd:element");
    for (int i = 0; i < sGlist.size (); i++)
    {
      final int theLocalLevel = theLevel + 1;
      setupSegment (sGlist.get (i), theLocalLevel);
    }

  }

  /**
   * @param theContentHandler
   * @param theSegmentName
   * @param theMinOccurence
   * @param theMaxOccurence
   * @param isEndElement
   * @throws SAXException
   */
  private void setupSegmentDeclaration (final ContentHandler theContentHandler,
                                        final String attribName,
                                        final String attribNameQ,
                                        final String theSegmentName,
                                        final String theMinOccurence,
                                        final String theMaxOccurence,
                                        final boolean isEndElement) throws SAXException
  {
    final AttributesImpl theAttribs = new AttributesImpl ();
    theAttribs.addAttribute ("", attribName, attribNameQ, "CDATA", theSegmentName);
    if (theMinOccurence != null)
      theAttribs.addAttribute ("", "minOccurs", "minOccurs", "CDATA", theMinOccurence);
    if (theMaxOccurence != null && !theMaxOccurence.equals ("1"))
      theAttribs.addAttribute ("", "minOccurs", "maxOccurs", "CDATA", theMaxOccurence);
    theContentHandler.startElement (namespaceURI, "xsd:element", "xsd:element", theAttribs);
    if (isEndElement)
      theContentHandler.endElement (namespaceURI, "xsd:element", "xsd:element");
  }

  public void parse (final String uri)
  {}

  public void setContentHandler (final ContentHandler handler)
  {
    contentHandler = handler;
  }

  public ContentHandler getContentHandler ()
  {
    return contentHandler;
  }

  public boolean getFeature (final String s)
  {
    return false;
  }

  public void setFeature (final String s, final boolean b)
  {}

  public Object getProperty (final String s)
  {
    return null;
  }

  public void setProperty (final String s, final Object o)
  {}

  public void setEntityResolver (final EntityResolver e)
  {}

  public EntityResolver getEntityResolver ()
  {
    return null;
  }

  public void setDTDHandler (final DTDHandler d)
  {}

  public DTDHandler getDTDHandler ()
  {
    return null;
  }

  public void setErrorHandler (final ErrorHandler handler)
  {}

  public ErrorHandler getErrorHandler ()
  {
    return null;
  }

  /**
   * @return
   * @throws TransformerException
   * @throws UnsupportedEncodingException
   */
  public String parseUNECE () throws TransformerException, UnsupportedEncodingException
  {
    // mi.println(new java.util.Date(System.currentTimeMillis()).toString()+":
    // "+"EdifactSaxParserToXML().parseEifact( "+inputEDIFACT+" )" );
    // construct SAXSource with our custom XMLReader
    final InputSource inputSource = new InputSource ();
    inputSource.setEncoding ("ISO-8859-1");
    final XMLReader parser = this;
    final SAXSource saxSource = new SAXSource (parser, inputSource);
    final ByteArrayOutputStream barryOut = new ByteArrayOutputStream ();
    final StreamResult streamResult = new StreamResult (barryOut);
    transformer.transform (saxSource, streamResult);
    final String result = new String (barryOut.toByteArray (), "ISO-8859-1");
    // mi.println(new java.util.Date(System.currentTimeMillis()).toString()+":
    // "+"EdifactSaxParserToXML().parseEifact()==> Result: "+result );
    return result;
  }

  public String getMessageType ()
  {
    return m_messageType;
  }

  public void setMessageType (final String messageType)
  {
    this.m_messageType = messageType;
  }

  public String getMessageName ()
  {
    return m_messageName;
  }

  public void setMessageName (final String messageName)
  {
    this.m_messageName = messageName;
  }

  public void setTheSegmentList (final List <SegmentData> theSegmentList)
  {
    this.m_theSegmentList = theSegmentList;
  }

  public void setTheSegmentStructureList (final List <SegmentStructureData> theSegmentStructureList)
  {
    this.m_theSegmentStructureList = theSegmentStructureList;
  }

  public void setTheSegmentTab (final Map <String, SegmentStructureData> theSgmentTab)
  {
    this.m_theSegmentTab = theSgmentTab;
  }

  public void setTheDataElementTab (final Map <String, Map <String, String>> theDataElementTab)
  {
    this.m_theDataElementTab = theDataElementTab;
  }

  public void setTheCompositeTab (final Map <String, CompositeDefinition> theCompositeTab)
  {
    this.m_theCompositeTab = theCompositeTab;
  }
}
