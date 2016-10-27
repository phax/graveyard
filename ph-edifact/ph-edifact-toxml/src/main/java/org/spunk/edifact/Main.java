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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.spunk.edifact.node.Segment;
import org.spunk.edifact.util.Streams;
import org.w3c.dom.Document;

/**
 * Main.
 */
public class Main
{
  /**
   * Transformer a file containing EDIFACT to XML.
   * 
   * @param arguments
   *        File name should be argument 1.
   * @throws FileNotFoundException
   *         Uncaught.
   * @throws IOException
   *         Uncaught.
   * @throws TransformerException
   *         Uncaught.
   */
  public static void main (final String [] arguments) throws FileNotFoundException, IOException, TransformerException
  {
    final Parser parser = new Parser ();
    final String fileContent = Streams.toString (new FileInputStream (arguments[0]));
    final Segment segment = parser.parse (fileContent);
    final Document document = Edifact.newDocument (segment);
    final DOMSource source = new DOMSource (document.getDocumentElement ());
    final Result target = new StreamResult (System.out);
    final TransformerFactory transformerFactory = TransformerFactory.newInstance ();
    final Transformer transformer = transformerFactory.newTransformer ();

    transformer.transform (source, target);
  }
}
