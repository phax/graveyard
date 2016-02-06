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
* $Id: OioTest.java,v 1.1 2009/12/14 15:58:48 s_schaefer Exp $
*/

package de.oio.test.jpdfunit.examples;

import de.oio.jpdfunit.AbstractDocumentTestCase;
import de.oio.jpdfunit.document.util.IDocumentDataSource;
import de.oio.jpdfunit.document.util.PdfDataSource;

/**
 * @author bbratkus Startpage example.
 */
public class OioTest extends AbstractDocumentTestCase
{

  /**
   * @param name
   */
  public OioTest (final String name)
  {
    super (name);

  }

  @Override
  protected IDocumentDataSource getDataSource ()
  {
    final IDocumentDataSource datasource = new PdfDataSource ("etc/testing-pdfs/DocumentEncryptionTest.pdf");
    return datasource;
  }

  public void testAssertIsDocumentEncrypted ()
  {
    assertIsDocumentEncrypted (true);
  }
}
