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
package org.spunk.edifact.bhun.edifact;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Before;
import org.junit.Test;
import org.spunk.edifact.Edifact;
import org.spunk.edifact.Parser;
import org.spunk.edifact.node.Segment;
import org.spunk.edifact.util.Streams;
import org.w3c.dom.Document;

/**
 * EdifactTest.
 */
public class EdifactTest
{

  private Parser parser;
  private Transformer m_aTransformer;

  /**
   * Before.
   *
   * @throws TransformerConfigurationException
   *         Not expected.
   */
  @Before
  public void before () throws TransformerConfigurationException
  {
    this.parser = new Parser ();
    final TransformerFactory newInstance = TransformerFactory.newInstance ();
    this.m_aTransformer = newInstance.newTransformer ();
  }

  /**
   * Stupidity test.
   *
   * @throws IOException
   *         Not expected.
   * @throws TransformerFactoryConfigurationError
   *         Not expected.
   * @throws TransformerException
   *         Not expected.
   */
  @Test
  public void parse00000001 () throws IOException, TransformerException, TransformerFactoryConfigurationError
  {
    final String resource = Streams.toString ("/0000001.edi");

    assertNotNull (resource);

    final Segment segment = this.parser.parse (resource);
    final Document document = Edifact.newDocument (segment);
    final DOMSource source = new DOMSource (document.getDocumentElement ());
    final Result target = new StreamResult (System.err);

    this.m_aTransformer.transform (source, target);
  }
}
