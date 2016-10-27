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
package com.openedi.sax.abs;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.openedi.transform.ElementProperties;

public abstract class AbstractEdifactParser extends DefaultHandler
{
  protected Document xsdDOM = null;

  protected static Hashtable <String, ElementProperties> elementTypesTab = null;

  protected static List <String> messageNames = null;

  protected static Hashtable <String, Hashtable <String, List <ElementProperties>>> elementTables = null;

  protected static Hashtable <String, NodeList> tabOfcomplexType = null;

  protected AbstractEdifactParser ()
  {
    if (messageNames == null)
      messageNames = new LinkedList<> ();

    if (tabOfcomplexType == null) // MFRI
      tabOfcomplexType = new Hashtable<> (); // MFRI

    if (elementTables == null)
      elementTables = new Hashtable<> ();
  }

  /**
   * MFRI Rreturns a DOM object for the XSD String of a given message type
   *
   * @return
   * @throws SAXException
   */
  protected Document setupXSDDOM (final String messageName) throws SAXException
  {

    try
    {

      final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
      // Turn off validation
      factory.setValidating (false);

      // Create a validating DOM parser
      final DocumentBuilder builder = factory.newDocumentBuilder ();
      final InputStream is = Thread.currentThread ().getContextClassLoader ().getResourceAsStream (messageName);
      return builder.parse (is);

    }
    catch (final Exception e)
    {
      e.printStackTrace ();
      throw new SAXException (e);
    }
  }

}
