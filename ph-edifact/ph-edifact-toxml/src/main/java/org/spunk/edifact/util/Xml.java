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
package org.spunk.edifact.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

/**
 * Utility methods to handle XML.
 */
public final class Xml
{

  /**
   * Creates a new document using the default JAXP settings.
   * 
   * @return New updatable document instance.
   * @throws ParserConfigurationException
   *         Failed to instantiate a new document builder.
   */
  public static Document newDocument () throws ParserConfigurationException
  {
    Document document;

    final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance ();
    final DocumentBuilder db = dbf.newDocumentBuilder ();
    document = db.newDocument ();

    return document;
  }

  private Xml ()
  {
    // HIDDEN CONSTRUCTOR
  }
}
