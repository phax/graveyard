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
* $Id: DocumentInformationTest.java,v 1.1 2009/12/14 15:58:48 s_schaefer Exp $
*/
package de.oio.test.jpdfunit.examples;

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.oio.jpdfunit.AbstractDocumentTestCase;
import de.oio.jpdfunit.document.util.IDocumentDataSource;
import de.oio.jpdfunit.document.util.PdfDataSource;

/**
 * @author bbratkus A Example DocumentInformationTest.
 */
public class DocumentInformationTest extends AbstractDocumentTestCase
{
  public DocumentInformationTest (final String name)
  {
    super (name);
  }

  @Override
  protected IDocumentDataSource getDataSource ()
  {
    final IDocumentDataSource datasource = new PdfDataSource ("etc/testing-pdfs/DocumentInformationTest.pdf");
    return datasource;
  }

  public void testAssertAuthorsNameEquals ()
  {
    assertAuthorsNameEquals ("Benjamin Bratkus");
  }

  public void testAssertCreatorEquals ()
  {
    assertCreatorEquals ("jpdfunit");
  }

  public void testAssertCreationDateEquals ()
  {
    final GregorianCalendar testDate = new GregorianCalendar (2005, Calendar.JUNE, 29, 16, 33, 02);
    assertCreationDateEquals (testDate);
  }

  public void testAssertDocumentTitleEquals ()
  {
    assertDocumentTitleEquals ("Testing the document information");
  }

  public void testAssertEncryptionLenghthEquals ()
  {
    assertEncryptionLenghthEquals (0);
  }

  public void testAssertKeywordsEquals ()
  {
    assertKeywordsEquals ("pdfbox, junit, java, testing, jpdfbox");
  }

  public void testAssertPageCount ()
  {
    assertPageCount (1);

  }

  public void testAssertSubjectEquals ()
  {
    assertSubjectEquals ("document information test");
  }

  public void testAssertVersionEquals ()
  {
    assertVersionEquals (1.4f);
  }

  public void testAssertIsDocumentEncrypted ()
  {
    assertIsDocumentEncrypted (false);
  }

}
