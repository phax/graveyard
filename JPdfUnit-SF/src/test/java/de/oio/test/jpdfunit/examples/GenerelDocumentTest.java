/*
* JPdfUnit- make your pdf green
* Copyright (C) 2005 Orientation in Objects GmbH
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the Apache License Version 2.0.
* There is a copy of this license along with this library.
* Otherwise see terms of license at apache.org
*
* Feel free to contact us:
*
* jpdfunit-users@lists.sourceforge.net
*
* $Id: GenerelDocumentTest.java,v 1.1 2009/12/14 15:58:48 s_schaefer Exp $
*/

package de.oio.test.jpdfunit.examples;

import de.oio.jpdfunit.AbstractDocumentTestCase;
import de.oio.jpdfunit.document.util.IDocumentDataSource;
import de.oio.jpdfunit.document.util.PdfDataSource;
import de.oio.jpdfunit.document.util.TextSearchType;

/**
 * @author bbratkus Yet another Example.
 */
public class GenerelDocumentTest extends AbstractDocumentTestCase
{

  /**
   * @param name
   */
  public GenerelDocumentTest (final String name)
  {
    super (name);
  }

  @Override
  protected IDocumentDataSource getDataSource ()
  {
    final IDocumentDataSource datasource = new PdfDataSource ("etc/testing-pdfs/oio-katalog-mit-logo-mit-farben.pdf");
    return datasource;
  }

  public void testAssertContentContainsText ()
  {
    assertContentContainsText ("Naja,", TextSearchType.STARTSWITH);
    assertContentContainsText ("OIO-Akademie", TextSearchType.CONTAINS);
    assertContentContainsText ("Schulungskatalog", TextSearchType.ENDSWITH);
    assertContentContainsText ("[\\S+\\s+]+?OIO-Akademie[\\s+\\S+]+?", TextSearchType.REGEXP);
  }

  public void testAssertDocumentInformation ()
  {
    assertCreatorEquals ("XEP 3.7.8 Client");
    assertAuthorsNameEquals ("Unknown");
  }

}
