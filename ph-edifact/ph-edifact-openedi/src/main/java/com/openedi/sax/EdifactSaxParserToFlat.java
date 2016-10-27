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

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;
import com.openedi.sax.abs.AbstractEdifactParser;

/**
 * <BR>
 * <BR>
 * <B>Class/Interface Description: </B><BR>
 * Instance class for SAX parsing of EDIFACT-XML (ISO TS 20625) to
 * UN/EDIFACT<BR>
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
 * 28.09.05   fricke         1.0      created
 * 19.06.14   fricke         2.0      Adapted to the XML/EDIFACT standard  (ISO TS 20625)
 * -------------------------------------------------------------------------------------------------
 *         </PRE>
 ****************************************************************/
public class EdifactSaxParserToFlat extends AbstractEdifactParser
{
  public static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
  public static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
  public static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

  private final SAXParser saxParser;
  private final DefaultHandler handler;
  private final SAXParserFactory factory;

  private StringBuilder buffy;

  private boolean isUNA01 = false;

  private boolean isUNA02 = false;

  private boolean isUNA03 = false;

  private boolean isUNA04 = false;

  private boolean isUNA05 = false;

  private boolean isUNA06 = false;
  private boolean isComposite = false;

  private StringBuilder sbUNA = new StringBuilder ("");
  private String segmentDelimiter = "'";
  private String fieldDelimiter = "+";
  private String subFieldDelimiter = ":";
  private String releaseChar = "?";
  private String decimalSep = ".";
  // Needed for regex evaluation
  private final String fieldDelimiterEscape = "\\+";
  private final String releaseCharEscape = "\\?";

  private String releaseCharSegmentDelimiter = releaseChar + segmentDelimiter;
  private String releaseCharFieldDelimiter = releaseChar + fieldDelimiter;
  private String releaseCharSubFieldDelimiter = releaseChar + subFieldDelimiter;
  private final String releaseCharReleaseChar = releaseChar + releaseChar;
  private String releaseCharDecimalSeparator = releaseChar + decimalSep;

  private boolean isStartSegment;
  private boolean isLastComposite;

  /**
   * The constructor to be used for this SAX parser instance
   */
  public EdifactSaxParserToFlat (final boolean isValidate) throws Exception
  {
    super ();

    isStartSegment = false;
    factory = SAXParserFactory.newInstance ();
    factory.setNamespaceAware (true);
    factory.setValidating (isValidate);
    saxParser = factory.newSAXParser ();
    // mi.println(new java.util.Date(System.currentTimeMillis()).toString()+":
    // "+"EdifactSaxParserToFlat( )--> obtained the following parser:
    // "+saxParser.getClass() );
    // TRACE.infoT( "EdifactSaxParserToFlat() obtained the following parser:
    // "+saxParser.getClass()+" Factory: "+factory.getClass(),
    // XIAdapterCategories.APP_AF_MP, "");
    handler = this;
    // mi.println(new java.util.Date(System.currentTimeMillis()).toString()+":
    // "+"EdifactSaxParserToFlat()--> Initialization finished" );

  }

  @SuppressWarnings ("unused")
  public String parse (final String sinputXML) throws Exception
  {
    // mi.println(new java.util.Date(System.currentTimeMillis()).toString()+":
    // "+"EdifactSaxParserToFlat.parse( "+inputXML+" )" );
    initGlobalVariables ();
    final String inputXML = sinputXML.replaceAll ("&apos;", "'");
    int index1 = inputXML.indexOf ("<D_0065>") + 8;
    int index2 = inputXML.indexOf ("</D_0065>");
    // 01ottfro should add code to handle string out of bounds
    // if <D_0065> not found
    final String edifactMessageName = inputXML.substring (index1, index2);
    index1 = inputXML.indexOf ("<D_0054>") + 8;
    index2 = inputXML.indexOf ("</D_0054>");
    // 01ottfro should add code to handle string out of bounds
    // if <S00903> not found
    final String messageVersion = inputXML.substring (index1, index2);
    index1 = inputXML.indexOf ("<D_0051>") + 8;
    index2 = inputXML.indexOf ("</D_0051>");
    // 01ottfro should add code to handle string out of bounds
    // if <S00904> not found
    final String messageType = inputXML.substring (index1, index2);

    final StringBuilder buffyXsdName = new StringBuilder ();

    buffyXsdName.append ("EDIFACTINTERCHANGE");
    buffyXsdName.append ("_");
    buffyXsdName.append (edifactMessageName);
    buffyXsdName.append ("_");
    buffyXsdName.append (messageVersion);
    final String messageName = buffyXsdName.toString ();
    buffyXsdName.append (".xsd");
    final String xsdName = buffyXsdName.toString ();

    if (saxParser.isValidating ())
    {
      saxParser.setProperty (JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
      saxParser.setProperty (JAXP_SCHEMA_SOURCE,
                             Thread.currentThread ().getContextClassLoader ().getResourceAsStream (xsdName));
    }

    buffy = new StringBuilder ();
    saxParser.parse (new NonBlockingByteArrayInputStream (inputXML.getBytes ()), handler);
    return processReleaseChar (buffy.toString ());
  }

  /**
   * Initialize global variables
   */
  private void initGlobalVariables ()
  {
    // TODO Auto-generated method stub

    isUNA01 = false;
    isUNA02 = false;
    isUNA03 = false;
    // isUNA04 = false;
    isUNA05 = false;
    isUNA06 = false;
    isComposite = false;

    sbUNA = new StringBuilder ("");
    segmentDelimiter = "'";
    fieldDelimiter = "+";
    subFieldDelimiter = ":";
    releaseChar = "?";
    decimalSep = ".";
    isStartSegment = false;
    isLastComposite = false;

  }

  public void emit (final String s)
  {
    // mi.println(new java.util.Date(System.currentTimeMillis()).toString()+":
    // "+"EdifactSaxParser.emit( "+s+" )" );
    buffy.append (s);
  }

  public void nl ()
  {
    buffy.append ("\n");
  }

  @Override
  public void startDocument () throws SAXException
  {
    // mi.println(new java.util.Date(System.currentTimeMillis()).toString()+":
    // "+"EdifactSaxParser.startDocument()" );
  }

  @Override
  public void endDocument () throws SAXException
  {
    nl ();
  }

  @Override
  public void startElement (final String namespaceURI,
                            final String sName, // simple name (localName)
                            final String qName, // qualified name
                            final Attributes attrs) throws SAXException
  {
    // mi.println(new java.util.Date(System.currentTimeMillis()).toString()+":
    // "+"EdifactSaxParser.startElement( "+sName+", "+qName+" )" );
    // System.err.println("EdifactSaxParser.startElement( " + sName + ", " +
    // qName + " )");
    String eName = sName; // element name
    if ("".equals (eName))
      eName = qName; // namespaceAware = false

    if (eName.equals ("D_UNA1"))
    {
      isUNA01 = true;
      isUNA02 = isUNA03 = isUNA04 = isUNA05 = isUNA06 = false;
      return;
    }
    if (eName.equals ("D_UNA2"))
    {
      isUNA02 = true;
      isUNA01 = isUNA03 = isUNA04 = isUNA05 = isUNA06 = false;
      return;
    }
    if (eName.equals ("D_UNA3"))
    {
      isUNA03 = true;
      isUNA01 = isUNA02 = isUNA04 = isUNA05 = isUNA06 = false;
      return;
    }
    if (eName.equals ("D_UNA4"))
    {
      isUNA04 = true;
      isUNA01 = isUNA02 = isUNA03 = isUNA05 = isUNA06 = false;
      return;
    }
    if (eName.equals ("D_UNA5"))
    {
      isUNA05 = true;
      isUNA01 = isUNA02 = isUNA03 = isUNA04 = isUNA06 = false;
      return;
    }
    if (eName.equals ("D_UNA6"))
    {
      isUNA06 = true;
      isUNA01 = isUNA02 = isUNA03 = isUNA04 = isUNA05 = false;
      return;
    }

    isUNA01 = isUNA02 = isUNA03 = isUNA04 = isUNA05 = isUNA06 = false;
    if (eName.startsWith ("S_"))
    {
      isStartSegment = true;
      isLastComposite = false;
      emit (eName.substring (2));
      return;
    }

    if (eName.startsWith ("C_"))
    {
      isComposite = true;
      return;
    }

    if (eName.startsWith ("D_"))
    {
      if (isComposite)
        if (!isStartSegment)
          if (!isLastComposite)
            emit (subFieldDelimiter);
          else
          {
            isLastComposite = false;
            emit (fieldDelimiter);
          }
        else
        {
          isStartSegment = false;
          emit (fieldDelimiter);
        }
      else
        emit (fieldDelimiter);
      return;
    }
    isLastComposite = false;

  }

  @Override
  public void endElement (final String namespaceURI,
                          final String sName, // simple name
                          final String qName // qualified name
  ) throws SAXException
  {
    String eName = sName; // element name
    if (eName.equals ("D_UNA1"))
    {
      isUNA01 = false;
    }
    if (eName.equals ("D_UNA2"))
    {
      isUNA02 = false;
    }
    if (eName.equals ("D_UNA3"))
    {
      isUNA03 = false;
    }
    if (eName.equals ("D_UNA4"))
    {
      isUNA04 = false;
    }
    if (eName.equals ("D_UNA5"))
    {
      isUNA05 = false;
    }
    if (eName.equals ("D_UNA6"))
    {
      isUNA06 = false;
      final String sUNA = sbUNA.toString ();
      if (sUNA.contains (releaseChar + segmentDelimiter))
        sUNA.replace (releaseChar + segmentDelimiter, releaseChar + " " + segmentDelimiter);
      emit (sUNA);
    }
    if ("".equals (eName))
      eName = qName; // namespaceAware = false
    if (eName.startsWith ("C_"))
    {
      isLastComposite = true;
      isComposite = false;
      return;
    }
    if (eName.startsWith ("S_"))
    {
      emit (segmentDelimiter);
      return;
    }

  }

  /**
   * Contains the real contents between the XML tags
   */
  @Override
  public void characters (final char buf[], final int offset, final int len) throws SAXException
  {
    final String s = new String (buf, offset, len);
    if (isUNA01)
    // We know that we are now dealing with UNA segment contents
    // We may not get all chars in one call...
    {
      sbUNA.append (buf, offset, len);
      subFieldDelimiter = s;
      releaseCharSubFieldDelimiter = releaseChar + s;
      return;
    }
    if (isUNA02)
    // We know that we are now dealing with UNA segment contents
    // We may not get all chars in one call...
    {
      sbUNA.append (buf, offset, len);
      fieldDelimiter = s;
      releaseCharFieldDelimiter = releaseChar + s;

      return;
    }
    if (isUNA03)
    // We know that we are now dealing with UNA segment contents
    // We may not get all chars in one call...
    {
      sbUNA.append (buf, offset, len);
      decimalSep = s;
      releaseCharDecimalSeparator = releaseChar + s;
      return;
    }
    if (isUNA04)
    // We know that we are now dealing with UNA segment contents
    // We may not get all chars in one call...
    {
      sbUNA.append (buf, offset, len);
      releaseChar = s;
      return;
    }
    if (isUNA05)
    // We know that we are now dealing with UNA segment contents
    // We may not get all chars in one call...
    {
      sbUNA.append (" ");
      return;
    }
    if (isUNA06)
    // We know that we are now dealing with UNA segment contents
    // We may not get all chars in one call...
    {
      // sbUNA.append(buf,offset,len);
      segmentDelimiter = s;
      releaseCharSegmentDelimiter = releaseChar + s;
      return;
    }

    if (!s.trim ().equals (""))
    {
      // System.err.println("AQ"+s.trim()+"Q");
      // s = s.replaceAll("\\"+releaseChar,releaseChar+releaseChar);
      // s = s.replaceAll("\\"+subFieldDelimiter,releaseChar+subFieldDelimiter);
      // s = s.replaceAll("\\"+fieldDelimiter,releaseChar+fieldDelimiter);
      // s = s.replaceAll("\\"+segmentDelimiter,releaseChar+segmentDelimiter);
      emit (s.trim ());
      // System.err.println("BQ"+s.trim()+"Q");
    }

  }

  @Override
  public void ignorableWhitespace (final char buf[], final int offset, final int Len) throws SAXException
  {
    // nl ();
  }

  @Override
  public void endPrefixMapping (final String arg0) throws SAXException
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void processingInstruction (final String arg0, final String arg1) throws SAXException
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void setDocumentLocator (final Locator arg0)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void skippedEntity (final String arg0) throws SAXException
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void startPrefixMapping (final String arg0, final String arg1) throws SAXException
  {
    // TODO Auto-generated method stub

  }

  /**
   * Insert release char if needed
   *
   * @param content
   * @return
   */
  public String processReleaseChar (final String scontent)
  {
    String content = scontent.replaceAll (segmentDelimiter, releaseCharSegmentDelimiter);
    if (fieldDelimiter.equals ("+"))
      content = content.replaceAll (fieldDelimiterEscape, releaseCharFieldDelimiter);
    else
      content = content.replaceAll (fieldDelimiter, releaseCharFieldDelimiter);

    content = content.replaceAll (subFieldDelimiter, releaseCharSubFieldDelimiter);
    content = content.replaceAll (decimalSep, releaseCharDecimalSeparator);

    if (releaseChar.equals ("?"))
      content = content.replaceAll (releaseCharEscape, releaseCharReleaseChar);
    else
      content = content.replaceAll (releaseChar, releaseCharReleaseChar);
    return content;

  }
}
