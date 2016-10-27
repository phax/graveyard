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
package com.openedi.sax;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;

import com.openedi.sax.abs.AbstractEdifactParser;
import com.openedi.sax.parse.segments.EdifactField;
import com.openedi.sax.parse.segments.EdifactSegment;
import com.openedi.sax.parse.segments.EdifactSubField;
import com.openedi.transform.StringTokenizerEscape;

/**
 * <BR>
 * <BR>
 * <B>Class/Interface Description: </B><BR>
 * Instance class for SAX parsing of UN/EDIFACT to EDIFACT-XML (ISO TS
 * 20625)<BR>
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
 * 20.02.06   fricke         1.0 created
 * 2008       frost          1.1 bugfixing
 * 19.04.14   fricke         2.0 Adapted to the XML/EDIFACT standard  (ISO TS 20625)
 * -------------------------------------------------------------------------------------------------
 *         </PRE>
 ****************************************************************/
public class EdifactSaxParserToXML extends AbstractEdifactParser implements XMLReader
{

  public static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
  public static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
  public static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
  private static final String EDIFACT_ROOT_SEGMENT_NAME = "EDIFACTINTERCHANGE";

  private Transformer m_transformer;

  private ContentHandler m_contentHandler;
  private final AttributesImpl m_attribs = new AttributesImpl ();
  private static AttributesImpl s_compositeAttribs = new AttributesImpl ();
  private final AttributesImpl m_rootAttribs = new AttributesImpl ();
  private static String s_namespaceURI = "";

  private String m_sEncoding = "UTF-8";
  private char m_segmentDelimiter = '\'';
  private char m_fieldDelimiter = '+';
  private char m_subFieldDelimiter = ':';
  private char m_releaseChar = '?';
  private char m_decimalSep = '.';

  private final boolean m_edielSwedish = false;

  private java.util.logging.Logger m_aLogger;

  private List <EdifactSegment> m_rawListOfAllEdifactSegments;
  private Hashtable <String, Node> m_theElementTab;

  // private NodeList theDOMElementList ;
  @SuppressWarnings ("unused")
  private String m_theMessageName;

  static
  {
    s_compositeAttribs.addAttribute (s_namespaceURI, "", "composite", "CDATA", "true");
  }

  /**
   * @param encoding
   * @throws SAXException
   */
  public EdifactSaxParserToXML (final String encoding) throws SAXException
  {
    super ();
    init (encoding, null);
  }

  /**
   * @param encoding
   * @param logger
   * @throws SAXException
   */
  public EdifactSaxParserToXML (final String encoding, final java.util.logging.Logger logger) throws SAXException
  {
    super ();
    this.m_aLogger = logger;
    this.m_aLogger.setLevel (Level.FINEST);
    init (encoding, logger);
  }

  private void init (final String encoding, final java.util.logging.Logger alogger) throws SAXException
  {
    java.util.logging.Logger logger = alogger;
    if (alogger == null)
    {
      this.m_aLogger = java.util.logging.Logger.getAnonymousLogger ();
      logger = this.m_aLogger;
    }

    this.m_aLogger.setLevel (Level.FINEST);
    this.m_aLogger.entering ("EdifactSaxParserToXML", "init");
    this.m_sEncoding = encoding;
    try
    {
      final SAXTransformerFactory saxTransformerFactory = (SAXTransformerFactory) TransformerFactory.newInstance ();
      logger.finest ("got saxTransformerFactory");
      final TransformerHandler handler = saxTransformerFactory.newTransformerHandler ();
      logger.finest ("got TransformerHandler");
      m_transformer = handler.getTransformer ();
      logger.finest ("got transformer");
      m_transformer.setOutputProperty (OutputKeys.ENCODING, encoding);
      m_transformer.setOutputProperty (OutputKeys.INDENT, "yes");
      m_transformer.setOutputProperty (OutputKeys.METHOD, "xml");
    }
    catch (final TransformerConfigurationException tce)
    {
      logger.throwing ("EdifactSaxParserToXML", "init", tce);
      throw new SAXException (tce);
    }
  }

  /**
   * SAX parser processing for each EdifactSegment object
   *
   * @param EdifactSegment
   *        currentSegment: The current EdifactSegment to be processed by our
   *        reverse SAX parser
   * @throws SAXException
   */
  private void parseCurrentSegment (final EdifactSegment currentSegment) throws SAXException
  {
    m_aLogger.entering ("EdifactSaxParserToXML", "parseCurrentSegment");
    final List <EdifactField> eFields = currentSegment.m_segmentFields;

    for (int count = 0; count < eFields.size (); count++)
    {
      final EdifactField fieldObject = eFields.get (count);
      final String value = fieldObject.m_sFieldValue;

      if (fieldObject.isComposite)
      {
        m_contentHandler.startElement (s_namespaceURI, "", fieldObject.m_sFieldTagName, null);
        final List <EdifactSubField> subFieldList = fieldObject.subFields;
        for (int j = 0; j < subFieldList.size (); j++)
        {
          final EdifactSubField subFieldObject = subFieldList.get (j);
          if (subFieldObject.m_subFieldTagName != null)
          {
            m_contentHandler.startElement (s_namespaceURI, "", subFieldObject.m_subFieldTagName, m_attribs);
            m_contentHandler.characters (subFieldObject.m_subFieldValue.toCharArray (),
                                         0,
                                         subFieldObject.m_subFieldValue.length ());
            m_contentHandler.endElement (s_namespaceURI, "", subFieldObject.m_subFieldTagName);
          }
        }
      }
      else
      {
        m_contentHandler.startElement (s_namespaceURI, "", fieldObject.m_sFieldTagName, m_attribs);
        m_contentHandler.characters (value.toCharArray (), 0, value.length ());
      }
      m_contentHandler.endElement (s_namespaceURI, "", fieldObject.m_sFieldTagName);
    }
    m_aLogger.exiting ("EdifactSaxParserToXML", "parseCurrentSegment");
  }

  /**
   * Recursive traversal of the tree of EdifactSegment objects mehtod calls the
   * SAX XML parser for the current EdifactSegment object to be processed
   *
   * @param EdifactSegment
   *        segment: The EdifactSegment object to be processed
   * @throws SAXException
   */
  private void traverseEdifactSegment (final EdifactSegment segment) throws SAXException
  {
    m_aLogger.entering ("EdifactSaxParserToXML", "traverseEdifactSegment");
    m_aLogger.log (Level.FINE, "segment " + segment);
    m_contentHandler.startElement (s_namespaceURI, "", segment.m_segmentName, m_attribs);
    parseCurrentSegment (segment);
    if (segment.m_childSegments.isEmpty ())
    {
      m_contentHandler.endElement (s_namespaceURI, "", segment.m_segmentName);
      return;
    }
    for (int i = 0; i < segment.m_childSegments.size (); i++)
    {
      traverseEdifactSegment (segment.m_childSegments.get (i));
    }
    m_contentHandler.endElement (s_namespaceURI, "", segment.m_segmentName);
    m_aLogger.exiting ("EdifactSaxParserToXML", "traverseEdifactSegment");
  }

  // ===========================================================
  // XML Reader Interface Implementation
  // ===========================================================
  public void parse (final InputSource source) throws IOException, SAXException
  {
    // reading one char at a time is very bad performance...
    // we want a byte array so that we can read it several times
    // and check charset encoding before turning the bytes into string
    // most of the time we will be happy with ISO-8859-1 = UNOC
    // but sometimes we need ISO-646 = UNOB = ASCII
    //
    // Swedish characters are represented as follows
    //
    // r = r.replaceAll("\\É", "@");
    // r = r.replaceAll("\\Å", "]");
    // r = r.replace("Ö".charAt(0), "\\".charAt(0));
    // r = r.replaceAll("\\Ä", "[");
    // r = r.replaceAll("\\Ü", "^");
    //
    // r = r.replaceAll("\\é", "`");
    // r = r.replaceAll("\\ä", "{");
    // r = r.replaceAll("\\ö", "|");
    // r = r.replaceAll("\\å", "}");
    // r = r.replaceAll("\\ü", "~");

    // microsoft has some information "EDI Character Sets"
    // http://msdn.microsoft.com/en-us/library/bb246115.aspx
    //

    m_aLogger.entering ("EdifactSaxParserToXML", "parse");

    final java.io.BufferedInputStream bis = new java.io.BufferedInputStream (source.getByteStream ());
    final ByteArrayOutputStream out = new ByteArrayOutputStream ();
    final byte [] bytebuf = new byte [4096];
    int len;
    while ((len = bis.read (bytebuf)) != -1)
    {
      out.write (bytebuf, 0, len);
    }
    out.flush ();
    final byte [] byteEdifact = out.toByteArray ();
    String rawEdifact = null;
    // rawEdifact = new String(byteEdifact, "ISO-8859-1");
    rawEdifact = new String (byteEdifact, "ISO-8859-1");
    m_aLogger.log (Level.FINE, "rawEdifact ok");
    if (rawEdifact.matches (".*UNB\\+UNOC.*"))
    {
      // we are fine do nothing
      m_aLogger.log (Level.FINE, "match .*UNB\\+UNOC.*");
    }
    else
      if (rawEdifact.matches (".*UNB\\+UNOB.*|.*UNB\\+UNOA.*"))
      {
        m_aLogger.log (Level.FINE, "match .*UNB\\+UNOB.*|.*UNB\\+UNOA.*");
        rawEdifact = new String (byteEdifact, "ASCII");

        // EDIEL swedish specials
        if (m_edielSwedish)
        {
          m_aLogger.log (Level.FINE, "ediel Swedish");
          String r = rawEdifact;
          r = r.replaceAll ("\\@", "É"); // do not konvert email
          // addresses
          r = r.replaceAll ("\\]", "Å");
          r = r.replaceAll ("\\\\", "Ö");
          r = r.replaceAll ("\\[", "\\Ä");
          r = r.replaceAll ("\\^", "Ü");
          r = r.replaceAll ("\\`", "é");
          r = r.replaceAll ("\\{", "\\ä");
          r = r.replaceAll ("\\|", "ö");
          r = r.replaceAll ("\\}", "\\å");
          r = r.replaceAll ("\\~", "ü");
          rawEdifact = r;
        }
      }
      else
      { // default to UNOB
        m_aLogger.log (Level.FINE, "default UNOB");
        rawEdifact = new String (byteEdifact, "ASCII");
        // EDIEL swedish specials
        if (m_edielSwedish)
        {
          m_aLogger.log (Level.FINE, "ediel Swedish");
          String r = rawEdifact;
          r = r.replaceAll ("\\@", "É"); // do not konvert email
          // addresses
          r = r.replaceAll ("\\]", "Å");
          r = r.replaceAll ("\\\\", "Ö");
          r = r.replaceAll ("\\[", "\\Ä");
          r = r.replaceAll ("\\^", "Ü");
          r = r.replaceAll ("\\`", "é");
          r = r.replaceAll ("\\{", "\\ä");
          r = r.replaceAll ("\\|", "ö");
          r = r.replaceAll ("\\}", "\\å");
          r = r.replaceAll ("\\~", "ü");
          rawEdifact = r;
        }
      }

    rawEdifact = rawEdifact.toString ().trim ();
    // rawEdifact = rawEdifact.replaceAll("\n", ""); //01ottfro
    // rawEdifact = rawEdifact.replaceAll("\r", ""); //01ottfro
    final int indexOfSegmentDelimiter = rawEdifact.indexOf ("UNB+");
    if (indexOfSegmentDelimiter == -1)
    {
      m_aLogger.log (Level.FINE, "throw Can not find UNB segment for:\n" + rawEdifact);
      throw new SAXException ("Can not find UNB segment for:\n " + rawEdifact);
    }
    if (rawEdifact.indexOf ("UNA+") != -1)
    {
      m_segmentDelimiter = rawEdifact.charAt (indexOfSegmentDelimiter - 1);
      // System.out.println("Index segment delimiter:
      // "+indexOfSegmentDelimiter+" SegmentDelimiter
      // :"+segmentDelimiter+"\n"+rawEdifact);
      m_subFieldDelimiter = rawEdifact.charAt (3);
      m_fieldDelimiter = rawEdifact.charAt (4);
      m_decimalSep = rawEdifact.charAt (5);
      m_releaseChar = rawEdifact.charAt (6);
    }
    final Hashtable <String, String> initialValues = getInitialValues (rawEdifact,
                                                                       m_segmentDelimiter,
                                                                       m_subFieldDelimiter);
    // String messageName = (String)initialValues.get("messageName");
    final StringBuffer buffyRootTagName = new StringBuffer ();
    buffyRootTagName.append (initialValues.get ("messageOrganization"));
    buffyRootTagName.append ("_");
    buffyRootTagName.append (initialValues.get ("messageName"));
    buffyRootTagName.append ("_");
    buffyRootTagName.append (initialValues.get ("messageVersion"));
    final String xsdFileName = buffyRootTagName.toString ();
    m_aLogger.log (Level.INFO, "xsdFileName " + xsdFileName);

    m_rootAttribs.addAttribute ("http://www.w3.org/2001/XMLSchema-instance",
                                "xmlns:xsi",
                                "xmlns:xsi",
                                "CDATA",
                                "http://www.w3.org/2001/XMLSchema-instance");
    m_rootAttribs.addAttribute ("http://www.w3.org/2001/XMLSchema-instance",
                                "",
                                "xsi:noNamespaceSchemaLocation",
                                "CDATA",
                                xsdFileName + ".xsd");
    m_rootAttribs.addAttribute ("http://www.w3.org/2001/XMLSchema-instance",
                                "",
                                "xmlns:ns0",
                                "CDATA",
                                "urn:sapstern.com:" + xsdFileName);

    m_contentHandler.startDocument ();

    EdifactSegment tree;
    try
    {
      tree = setupTreeOfEdifactSegments (rawEdifact, xsdFileName);
    }
    catch (final ParserConfigurationException e)
    {
      // TODO Auto-generated catch block
      throw new SAXException (e);
    }
    traverseEdifactSegment (tree);
    m_contentHandler.endDocument ();
    m_aLogger.exiting ("EdifactSaxParserToXML", "parse");
  }

  public void setContentHandler (final ContentHandler handler)
  {
    m_contentHandler = handler;
  }

  public ContentHandler getContentHandler ()
  {
    return m_contentHandler;
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
   * @param flatEdifact
   * @param messageName
   * @return
   * @throws IOException
   * @throws SAXException
   * @throws ParserConfigurationException
   */
  private EdifactSegment setupTreeOfEdifactSegments (final String flatEdifact,
                                                     final String messageName) throws IOException,
                                                                               SAXException,
                                                                               ParserConfigurationException
  {
    m_aLogger.entering ("EdifactSaxParserToXML", "setupTreeOfEdifactSegments");
    m_aLogger.finest ("messageName " + messageName);
    EdifactSegment rootSegment = new EdifactSegment (EDIFACT_ROOT_SEGMENT_NAME, true);
    this.m_theMessageName = messageName;
    super.xsdDOM = super.setupXSDDOM (messageName + ".xsd");
    // this.theDOMElementList =
    // super.xsdDOM.getElementsByTagName("xsd:element");
    // setup Hashtable of DOM Elements
    this.m_theElementTab = getElementTable (super.xsdDOM);

    this.m_rawListOfAllEdifactSegments = setupRawSegmentList (flatEdifact);
    final Element theRootElement = super.xsdDOM.getDocumentElement ();
    // rootSegment contains all the other segments in correct order
    System.out.println (new java.util.Date (System.currentTimeMillis ()).toString () +
                        ": " +
                        "EdifactSaxParserToXML().setupTreeOfEdifactSegments()==> start setup tree");
    rootSegment = setupSegment (rootSegment, theRootElement);
    System.out.println (new java.util.Date (System.currentTimeMillis ()).toString () +
                        ": " +
                        "EdifactSaxParserToXML().setupTreeOfEdifactSegments()==> finish setup tree");
    System.out.println (new java.util.Date (System.currentTimeMillis ()).toString () +
                        ": " +
                        "EdifactSaxParserToXML().setupTreeOfEdifactSegments()==> start refining tree");
    walkSegmentTreeFillSegmentFields (rootSegment);
    System.out.println (new java.util.Date (System.currentTimeMillis ()).toString () +
                        ": " +
                        "EdifactSaxParserToXML().setupTreeOfEdifactSegments()==> finish refining tree");
    // printSegmentTree(rootSegment, "");
    // CKE
    m_aLogger.finest ("resultList ok");

    m_aLogger.finest ("rootSegment ok");
    m_aLogger.exiting ("EdifactSaxParserToXML", "setupTreeOfEdifactSegments");

    return rootSegment;
  }

  // /**
  // * Output of the XML hierarcy
  // * @param rootSegment
  // * @param currentDepth
  // */
  // private void printSegmentTree(EdifactSegment rootSegment, String
  // currentDepth)
  // {
  // // TODO Auto-generated method stub
  // if ( currentDepth.length() > 0 )
  // System.out.println(currentDepth+">"+rootSegment.segmentName);
  // else
  // System.out.println(rootSegment.segmentName);
  // for ( int i=0;i<rootSegment.segmentFields.size();i++ )
  // {
  //
  // if (rootSegment.segmentFields.get(i).isComposite)
  // {
  // System.out.println(currentDepth+"->"+rootSegment.segmentFields.get(i).fieldTagName);
  // for (int j=0;j<rootSegment.segmentFields.get(i).subFields.size();j++)
  // {
  // System.out.println(currentDepth+"-->"+rootSegment.segmentFields.get(i).subFields.get(j).subFieldTagName+"
  // Value "+rootSegment.segmentFields.get(i).subFields.get(j).subFieldValue);
  // }
  // }
  // else
  // System.out.println(currentDepth+">"+rootSegment.segmentFields.get(i).fieldTagName+"
  // Value "+rootSegment.segmentFields.get(i).fieldValue);
  // }
  // for (int i = 0; i < rootSegment.childSegments.size(); i++)
  // {
  // printSegmentTree(rootSegment.childSegments.get(i), currentDepth);
  // }
  // currentDepth = currentDepth+"-";
  // }

  /**
   * @param rootSegment
   */
  private void walkSegmentTreeFillSegmentFields (final EdifactSegment rootSegment)
  {
    // Here we implement the split of the raw segment string in the segment
    // fields
    refineEdifactSegment (rootSegment);
    for (int i = 0; i < rootSegment.m_childSegments.size (); i++)
    {
      final EdifactSegment theChildSegment = rootSegment.m_childSegments.get (i);
      walkSegmentTreeFillSegmentFields (theChildSegment);
    }

  }

  /**
   * @param theResultSegment
   * @param currentDomElement
   * @return
   */
  private EdifactSegment setupSegment (final EdifactSegment theResultSegment, final Element currentDomElement)
  {

    // Sind noch weitere Kinder vorhanden
    if (currentDomElement.hasChildNodes ())
    {
      final NodeList nodes = currentDomElement.getChildNodes ();
      // Iteriere ueber die Kinder des aktuellen DOM Elementes
      for (int i = 0; i < nodes.getLength (); i++)
      {
        final Node node = nodes.item (i);

        if (node instanceof Element)
        {
          // a child element to process
          final Element child = (Element) node;

          // Hier kommt ggf eine Liste zurueck, weil wir mehrere Edifact
          // Segmente des gleichen Typs hintereinander haben koennen
          final List <EdifactSegment> theSegmentList = findEdifactSegmentForElement (child);
          if (theSegmentList != null)
          {
            // rekursiver Aufruf fuer das, bzw die Kindersegment
            // (schauen, ob da noch weitere Kinder drunter sind
            for (int k = 0; k < theSegmentList.size (); k++)
            {
              EdifactSegment currentSegment = theSegmentList.get (k);
              currentSegment = setupSegment (currentSegment, child);
              theResultSegment.m_childSegments.add (currentSegment);
            }
          }
        }
      }
      return theResultSegment;
    }
    return theResultSegment;
  }

  /**
   * Schaut in der Liste der Edifact Segmente nach, ob fuer das xsd Segment
   * Element ein EDIFACT Segment vorhanden ist Es koennen ggf mehrere Segmente
   * sein, deshalb wird eine Liste zurueckgegeben
   *
   * @param currentElement
   * @return
   */
  /**
   * @param currentElement
   * @return
   */
  private List <EdifactSegment> findEdifactSegmentForElement (final Element currentElement)
  {
    final List <EdifactSegment> resultList = new LinkedList<> ();

    final NodeList theCildNodesOfComplexType = getNodeListOfComplexType (currentElement.getChildNodes ());

    if (theCildNodesOfComplexType == null)
      return resultList;
    for (int j = 0; j < theCildNodesOfComplexType.getLength (); j++)
    {
      final Node theNode = theCildNodesOfComplexType.item (j);
      if (theNode instanceof Element)
      {
        final String nameOfAttrib = ((Element) theNode).getAttribute ("ref");
        // Handle group and meta elements

        if (nameOfAttrib.startsWith ("G_") | nameOfAttrib.startsWith ("M_"))
        {
          // First: get the cardinality
          final long maxOccurs = getMaxOccurs (theNode);
          // Element theCheckElement = getElementByAttributeValueFromDOM(
          // nameOfAttrib, this.theDOMElementList );
          final Element theCheckElement = (Element) this.m_theElementTab.get (nameOfAttrib);
          final NodeList theCheckCildNodesOfComplexType = getNodeListOfComplexType (theCheckElement.getChildNodes ());
          for (int i = 0; i < maxOccurs; i++)
          {
            // check whether we have valid segments for this segment group
            // if
            // (globalIndexOfNextEdifactSegment>=rawListOfAllEdifactSegments.size())
            // break;
            if (theCheckCildNodesOfComplexType == null)
              break;
            boolean foundSegment = false;
            for (int z = 0; z < theCheckCildNodesOfComplexType.getLength (); z++)
            {
              final Node theCheckNode = theCheckCildNodesOfComplexType.item (z);
              if (theCheckNode instanceof Element)
              {
                final String nameOfAttribOfCheckNode = ((Element) theCheckNode).getAttribute ("ref");
                final EdifactSegment checkEdifactSegment = m_rawListOfAllEdifactSegments.get (0);
                if (checkEdifactSegment.m_segmentName.equals (nameOfAttribOfCheckNode))
                  foundSegment = true;
                break;
              }
            }
            if (!foundSegment)
              break;
            // success, we have found a Edifact segment in the string of raw
            // Edifact which
            // belongs to the current xsd child G_ or M_ element
            EdifactSegment theCurrentSegment = new EdifactSegment (nameOfAttrib, true);
            // Element theElement =
            // getElementByAttributeValueFromDOM(nameOfAttrib,
            // this.theDOMElementList);
            final Element theElement = (Element) this.m_theElementTab.get (nameOfAttrib);
            // recursive call of setupSegment for this segment group
            theCurrentSegment = setupSegment (theCurrentSegment, theElement);

            if (theCurrentSegment.m_childSegments != null && !theCurrentSegment.m_childSegments.isEmpty ())
              resultList.add (theCurrentSegment);

          }
          continue;
          // end of G_ or M_ processing
        }
        if (m_rawListOfAllEdifactSegments.isEmpty ())
          break;
        // Lookup of segment inside UN/EDIFCT string

        while (!m_rawListOfAllEdifactSegments.isEmpty ())
        {

          final EdifactSegment currentSegment = m_rawListOfAllEdifactSegments.get (0);

          if (currentSegment.m_segmentName.equals (nameOfAttrib))
          {

            // success, we have found a Edifact segment in the string of raw
            // Edifact which
            // belongs to the current xsd child element

            // Assign associated DOM Element to segment object we need it later
            // while setting up the fields of the segment
            // currentSegment.theElement =
            // getElementByAttributeValueFromDOM(nameOfAttrib,
            // this.theDOMElementList);
            currentSegment.m_theElement = (Element) this.m_theElementTab.get (nameOfAttrib);
            resultList.add (currentSegment);
            m_rawListOfAllEdifactSegments.remove (0);
            // lookup of next segment,if it has a different segment name we get
            // out of the loop

            if (!m_rawListOfAllEdifactSegments.isEmpty ())
            {
              final EdifactSegment nextSegment = m_rawListOfAllEdifactSegments.get (0);
              if (!nextSegment.m_segmentName.equals (nameOfAttrib))
                break;
              // Check the cardinality
              if (getMaxOccurs (theNode) <= 1)
                break;
            }

          }
          else
            break;
        }

      }
    }
    return resultList;
  }

  /**
   * @param theNode
   * @return
   */
  private long getMaxOccurs (final Node theNode)
  {
    long result = 1;
    final String stringMaxOccurs = ((Element) theNode).getAttribute ("maxOccurs");
    if (stringMaxOccurs != null && !stringMaxOccurs.equals (""))
    {
      if (stringMaxOccurs.equalsIgnoreCase ("unbounded"))
        result = 9999999;
      else
        result = Long.parseLong (stringMaxOccurs);
    }
    return result;
  }

  /**
   * @param childNodes
   * @return
   */
  private NodeList getNodeListOfComplexType (final NodeList childNodes)
  {
    // TODO Auto-generated method stub
    for (int i = 0; i < childNodes.getLength (); i++)
    {
      final Node node = childNodes.item (i);
      // System.out.println("getNodeListOfComplexType()"+node.getNodeName());
      if (node.getNodeName ().equals ("xsd:complexType"))
        return getNodeListOfComplexType (node.getChildNodes ());

      if (node.getNodeName ().equals ("xsd:sequence"))
        return node.getChildNodes ();
    }
    return null;
  }

  /**
   * retrieves a Hashtable of initial values from UNB/UNH segment
   *
   * @param flatEdifactMessage
   *        the input message string
   * @param segmentDelimiter
   *        most cases '
   * @param subFieldDelimiter
   *        most cases :
   * @return the message name / the message version / the UNA segment content
   * @throws SAXException
   *         thrown if we can't find message name and / or version
   */
  private Hashtable <String, String> getInitialValues (final String flatEdifactMessage,
                                                       final char segmentDelimiter,
                                                       final char subFieldDelimiter) throws SAXException
  {
    Hashtable <String, String> resultTab = new Hashtable<> ();
    final StringTokenizer toki = new StringTokenizer (flatEdifactMessage, Character.toString (segmentDelimiter));

    while (toki.hasMoreTokens ())
    {
      final String edifactSegment = toki.nextToken ().trim ();
      if (edifactSegment.startsWith ("UNH"))
      {
        resultTab = getMessageNameAndVersion (subFieldDelimiter, edifactSegment);
        break;
      }
    }
    resultTab.put ("messageOrganization", EDIFACT_ROOT_SEGMENT_NAME);
    return resultTab;
  }

  /**
   * @param subFieldDelimiter
   * @param theUnhSegment
   * @return
   */
  private Hashtable <String, String> getMessageNameAndVersion (final char subFieldDelimiter, final String theUnhSegment)
  {
    final Hashtable <String, String> resultTab = new Hashtable<> ();
    String theParseString = null;
    final StringTokenizer theFieldTokenizer = new StringTokenizer (theUnhSegment,
                                                                   Character.toString (m_fieldDelimiter));
    while (theFieldTokenizer.hasMoreElements ())
      theParseString = theFieldTokenizer.nextToken ();
    final StringTokenizer theTokenizer = new StringTokenizer (theParseString, Character.toString (subFieldDelimiter));
    final int theNumberOfTokens = theTokenizer.countTokens ();
    for (int theIndex = 0; theIndex < theNumberOfTokens; theIndex++)
    {
      final String theLocalToken = theTokenizer.nextToken ();
      switch (theIndex)
      {
        case 0:
          resultTab.put ("messageName", theLocalToken);
          break;
        case 2:
          resultTab.put ("messageVersion", theLocalToken);
          break;
      }
    }
    return resultTab;
  }

  /**
   * Setup of all the segment fields and subfields for all the EdifactSegment
   * objects<br>
   *
   * @param EdifactSegment
   *        segmentObject: The root EdifactSegment object containing<br>
   *        all the other children EdifactSegment segment objects<br>
   * @return EdifactSegment : The properly setup root segment<br>
   */
  private EdifactSegment refineEdifactSegment (final EdifactSegment segmentObject)
  {
    m_aLogger.entering ("EdifactSaxParserToXML", "refineEdifactSegment");
    m_aLogger.log (Level.FINE, "segment " + segmentObject.m_segmentName);
    // Spezialbehandlung UNA Segment notwendig CKE
    if (segmentObject.m_segmentName.equals ("S_UNA"))
      return processUNASegmentObject (segmentObject);
    // obtain a StringTokenizer including the separator tokens (+ in most of the
    // cases)
    final StringTokenizerEscape loopToki = new StringTokenizerEscape (segmentObject.m_segmentString,
                                                                      m_fieldDelimiter,
                                                                      true,
                                                                      m_releaseChar);
    final List <String> tokenList = loopToki.getAllTokens ();

    if (tokenList.isEmpty ())
      return segmentObject;
    segmentObject.m_segmentFields.clear ();
    // Hier muss ueber den aktuellen DOM Subtree geloopt werden und nicht ueber
    // den Edifact String
    final Element domElement = segmentObject.m_theElement;
    final NodeList childNodes = getNodeListOfComplexType (domElement.getChildNodes ());

    // start with 1 as first token holds the segment name
    int index = 0;
    // Iteriere ueber die Kinder des aktuellen DOM Elementes
    for (int i = 0; i < childNodes.getLength (); i++)
    {
      final Node node = childNodes.item (i);

      if (node instanceof Element)
      {
        // a child element to process
        final String nameOfAttrib = ((Element) node).getAttribute ("ref");
        // Handle data elements
        if (nameOfAttrib.startsWith ("D_"))
        {
          if (tokenList.size () <= index)
            break;
          index = processDOMElementToken (tokenList, index, nameOfAttrib, segmentObject.m_segmentFields);
          continue;
        }
        // Handle elements of complex type eg.: UNOC:3
        if (nameOfAttrib.startsWith ("C_"))
        {
          final EdifactField currentCompositeField = new EdifactField (nameOfAttrib);
          if (tokenList.size () <= index)
            break;
          // get token
          String currentToken = tokenList.get (index);
          if (currentToken.equals (Character.valueOf (m_fieldDelimiter)))
          {
            index++;
            if (tokenList.size () <= index)
              break;
            // This should always be as the tokenizer produces the delimiters as
            // tokens, except for the beginning
            currentToken = tokenList.get (index);
            if (currentToken.equals (Character.valueOf (m_fieldDelimiter)))
            {
              // empty Field like BGM+++
              continue;
            }
          }
          // split composite field
          final StringTokenizerEscape fieldTokenizerSubFields = new StringTokenizerEscape (currentToken,
                                                                                           m_subFieldDelimiter,
                                                                                           true,
                                                                                           m_releaseChar);
          final List <String> subFieldTokenList = fieldTokenizerSubFields.getAllTokens ();
          int indexSubfields = 0;
          // Lookup the DOM Element holding the definition of the current
          // composite field
          final Node theCurrentChildNodeDef = this.m_theElementTab.get (nameOfAttrib);
          // Node theCurrentChildNodeDef =
          // getElementByAttributeValueFromDOM(nameOfAttrib,
          // this.theDOMElementList);
          NodeList childNodesOfChild = null;
          for (int p = 0; p < theCurrentChildNodeDef.getChildNodes ().getLength (); p++)
          {
            final Node nodeOfChildDef = theCurrentChildNodeDef.getChildNodes ().item (p);

            if (nodeOfChildDef instanceof Element)
            {
              if (nodeOfChildDef.getNodeName ().contains ("xsd:complexType"))
              {
                childNodesOfChild = getNodeListOfComplexType (nodeOfChildDef.getChildNodes ());
                break;
              }
            }
          }

          for (int j = 0; j < childNodesOfChild.getLength (); j++)
          {
            final Node nodeOfChild = childNodesOfChild.item (j);
            //
            if (nodeOfChild instanceof Element)
            {
              final Element theCurrentElement = (Element) nodeOfChild;
              if (subFieldTokenList.size () <= indexSubfields)
                break;
              String currentSubFieldToken = subFieldTokenList.get (indexSubfields);
              if (currentSubFieldToken.equals (Character.valueOf (m_subFieldDelimiter)))
              {
                if (subFieldTokenList.size () <= indexSubfields)
                  break;
                indexSubfields++;
                // This should always be as the tokenizer produces the
                // delimiters as tokens, except for the beginning
                currentSubFieldToken = subFieldTokenList.get (indexSubfields);
                if (currentSubFieldToken.equals (Character.valueOf (m_subFieldDelimiter)))
                {
                  // empty subFields like ::
                  continue;
                }
              }
              final EdifactSubField currentSubField = new EdifactSubField (theCurrentElement.getAttribute ("ref"),
                                                                           currentSubFieldToken,
                                                                           m_releaseChar);
              currentCompositeField.subFields.add (currentSubField);
              indexSubfields++;
            }
          }
          // Remove the composite field token
          tokenList.remove (index);
          // increase the index
          index++;
          segmentObject.m_segmentFields.add (currentCompositeField);
        }

      }
    }

    m_aLogger.exiting ("EdifactSaxParserToXML", "refineEdifactSegment");
    return segmentObject;
  }

  /**
   * @param tokenList
   * @param nindex
   * @param nameOfAttrib
   * @return
   */
  private int processDOMElementToken (final List <String> tokenList,
                                      final int nindex,
                                      final String nameOfAttrib,
                                      final List <EdifactField> fieldList)
  {
    int index = nindex;
    // get token
    String currentToken = tokenList.get (index);

    if (!currentToken.equals (Character.valueOf (m_fieldDelimiter)))
    {
      // found a value for the current field from DOM so we setup a field and
      // attach it to list of fields
      final EdifactField currendField = new EdifactField (nameOfAttrib, currentToken, m_releaseChar);
      fieldList.add (currendField);
    }
    else
    {
      index++;
      if (tokenList.size () <= index)
        return index;
      currentToken = tokenList.get (index);
      if (!currentToken.equals (Character.valueOf (m_fieldDelimiter)))
      {
        // found a value for the current field from DOM so we setup a field and
        // attach it to list of fields
        final EdifactField currendField = new EdifactField (nameOfAttrib, currentToken, m_releaseChar);
        fieldList.add (currendField);
      }

    }
    index++;

    return index;
  }

  private EdifactSegment processUNASegmentObject (final EdifactSegment segmentObject)
  {
    // TODO Auto-generated method stub

    final List <EdifactField> theFields = new LinkedList<> ();
    theFields.add (new EdifactField ("D_UNA1", Character.toString (m_subFieldDelimiter)));
    theFields.add (new EdifactField ("D_UNA2", Character.toString (m_fieldDelimiter)));
    theFields.add (new EdifactField ("D_UNA3", Character.toString (m_decimalSep)));
    theFields.add (new EdifactField ("D_UNA4", Character.toString (m_releaseChar)));
    theFields.add (new EdifactField ("D_UNA5", ""));

    theFields.add (new EdifactField ("D_UNA6", Character.toString (m_segmentDelimiter)));
    segmentObject.m_segmentFields.setAll (theFields);
    return segmentObject;
  }

  /**
   * @param flatEdifact
   * @return
   */
  public List <EdifactSegment> setupRawSegmentList (final String flatEdifact)
  {
    m_aLogger.entering ("EdifactSaxParserToXML", "setupRawSegmentList");

    final List <EdifactSegment> resultList = new LinkedList<> ();
    // Setup a tokenizer holding all the segments
    // StringTokenizer toki = new StringTokenizer(flatEdifact,
    // segmentDelimiter);
    final StringTokenizerEscape toki = new StringTokenizerEscape (flatEdifact,
                                                                  m_segmentDelimiter,
                                                                  false,
                                                                  m_releaseChar);
    // System.out.println("Edifact Segment: "+flatEdifact);
    while (toki.hasMoreTokens ())
    {
      final String edifactSegment = toki.nextToken ().trim ();

      final EdifactSegment segmentObject = getEdifactSegment (edifactSegment);
      resultList.add (segmentObject);
    }
    m_aLogger.exiting ("EdifactSaxParserToXML", "setupRawSegmentList");
    return resultList;
  }

  /**
   * @param edifactSegment
   * @return
   */
  private EdifactSegment getEdifactSegment (final String edifactSegment)
  {
    m_aLogger.entering ("EdifactSaxParserToXML", "getEdifactSegment");
    // Split the current segment into field tokens
    final StringTokenizerEscape fieldTokens = new StringTokenizerEscape (edifactSegment,
                                                                         m_fieldDelimiter,
                                                                         true,
                                                                         m_releaseChar);

    // Setup of SegmentObject (segment name is first token of the
    // fieldtokenizer)
    String nextToken = fieldTokens.nextToken ().trim ();
    if (nextToken.endsWith (":"))
      nextToken = nextToken.replace (":", "");
    final EdifactSegment segmentObject = new EdifactSegment ("S_" + nextToken, false);
    if (edifactSegment.startsWith ("UNA"))
    {
      segmentObject.m_segmentString = edifactSegment.substring (3);
      return segmentObject;
    }
    // Setup the raw segment string except the segment name
    final StringBuffer localBuffy = new StringBuffer ();
    // get rid of the segment delimiter
    fieldTokens.nextToken ();
    while (fieldTokens.hasMoreTokens ())
    {
      final String theField = fieldTokens.nextToken ();
      localBuffy.append (theField);
    }
    // Store the raw segment inside this segment object
    segmentObject.m_segmentString = localBuffy.toString ();

    m_aLogger.exiting ("EdifactSaxParserToXML", "getEdifactSegment");
    return segmentObject;
  }

  /**
   * @param fieldTagName
   * @param subFieldValue
   * @param fieldObject
   */
  public void addSubField (final String fieldTagName, final String subFieldValue, final EdifactField fieldObject)
  {

    final EdifactSubField subField = new EdifactSubField (fieldTagName, subFieldValue, m_releaseChar);
    fieldObject.subFields.add (subField);
  }

  public void parse (final String uri)
  {}

  /**
   * @param inputEDIFACT
   * @return
   * @throws TransformerException
   * @throws UnsupportedEncodingException
   */
  public String parseEdifact (final String inputEDIFACT) throws TransformerException, UnsupportedEncodingException
  {
    System.out.println (new java.util.Date (System.currentTimeMillis ()).toString () +
                        ": " +
                        "EdifactSaxParserToXML().parseEifact( )==> start transformation");
    final byte [] textBytes = inputEDIFACT.getBytes (m_sEncoding);
    final ByteArrayInputStream barryIn = new ByteArrayInputStream (textBytes);
    final InputSource inputSource = new InputSource (barryIn);
    inputSource.setEncoding (m_sEncoding);
    final XMLReader parser = this;
    final SAXSource saxSource = new SAXSource (parser, inputSource);
    final ByteArrayOutputStream barryOut = new ByteArrayOutputStream ();
    final StreamResult streamResult = new StreamResult (barryOut);
    m_transformer.transform (saxSource, streamResult);
    final String result = new String (barryOut.toByteArray (), m_sEncoding);
    System.out.println (new java.util.Date (System.currentTimeMillis ()).toString () +
                        ": " +
                        "EdifactSaxParserToXML().parseEifact()==> finished transformation");
    return result;
  }

  public ByteArrayOutputStream parseEdifact (final InputSource inputEDIFACT) throws TransformerException,
                                                                             UnsupportedEncodingException,
                                                                             IOException
  {
    System.out.println (new java.util.Date (System.currentTimeMillis ()).toString () +
                        ": " +
                        "EdifactSaxParserToXML().parseEifact( )==> start transformation");
    m_aLogger.entering ("EdifactSaxParserToXML", "parseEdifact");
    final XMLReader parser = this;
    m_aLogger.log (Level.FINE, "saxSource next");
    final SAXSource saxSource = new SAXSource (parser, inputEDIFACT);
    m_aLogger.log (Level.FINE, "saxSource ok");
    final ByteArrayOutputStream barryOut = new ByteArrayOutputStream ();
    final StreamResult streamResult = new StreamResult (barryOut);
    m_aLogger.log (Level.FINE, "streamResult ok");
    m_transformer.transform (saxSource, streamResult);
    m_aLogger.log (Level.FINE, "transform ok");

    barryOut.flush ();
    System.out.println (new java.util.Date (System.currentTimeMillis ()).toString () +
                        ": " +
                        "EdifactSaxParserToXML().parseEifact()==> finished transformation");
    m_aLogger.exiting ("EdifactSaxParserToXML", "parseEdifact");
    return barryOut;
  }

  /**
   * Search for a specific node inside a Nodelist the search can be done using a
   * tag name or the name of an attribute
   *
   * @param String
   *        name : The name fo tag/attribute
   * @param NodeList
   *        nodes : The nodelist we want to evaluate
   * @param boolean
   *        isAttribute: Tell the method whether we look for an attribute or a
   *        tagname
   * @return Node : The node we are interested in
   */
  public static Node getNode (final String name, final NodeList nodes, final boolean isAttribute)
  {
    Node child = null;
    // System.out.println(nodes.toString());
    for (int i = 0; i < nodes.getLength (); i++)
    {
      child = nodes.item (i);

      switch (child.getNodeType ())
      {
        case Node.DOCUMENT_NODE:
        case Node.ELEMENT_NODE:
          if (!isAttribute)
          {
            if (child.getNodeName () != null && child.getNodeName ().equals (name))
              return child;
          }
          else
          {
            if (child.getNodeName () != null)
              if (child.getAttributes ().getNamedItem ("name").getNodeValue ().equals (name))
              {
                return child;
              }

          }
      }
    }
    return child;
  }

  /**
   * @param xsdDOM
   * @return
   * @throws SAXException
   */
  public static Hashtable <String, Node> getElementTable (final Document xsdDOM) throws SAXException
  {

    final Hashtable <String, Node> tab = new Hashtable<> ();
    final NodeList theList = xsdDOM.getElementsByTagName ("xsd:element");
    for (int i = 0; i < theList.getLength (); i++)
    {
      if (theList.item (i) instanceof Element &&
          theList.item (i).getAttributes ().getNamedItem ("name") != null &&
          (theList.item (i).getAttributes ().getNamedItem ("name").getNodeValue ().startsWith ("G_") ||
           theList.item (i).getAttributes ().getNamedItem ("name").getNodeValue ().startsWith ("C_") ||
           theList.item (i)
                  .getAttributes ()
                  .getNamedItem ("name")
                  .getNodeValue ()
                  .startsWith ("M_") ||
           theList.item (i).getAttributes ().getNamedItem ("name").getNodeValue ().startsWith ("S_") ||
           theList.item (i).getAttributes ().getNamedItem ("name").getNodeValue ().startsWith ("EDIFACT")))
      {

        tab.put (theList.item (i).getAttributes ().getNamedItem ("name").getNodeValue (), theList.item (i));
      }
    }

    return tab;
  }

}
