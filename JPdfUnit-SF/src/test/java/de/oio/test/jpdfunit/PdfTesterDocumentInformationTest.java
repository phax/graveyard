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
* $Id: PdfTesterDocumentInformationTest.java,v 1.1 2009/12/14 15:58:51 s_schaefer Exp $
*/

package de.oio.test.jpdfunit;

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.oio.jpdfunit.DocumentTester;
import junit.framework.TestCase;

/**
 * @author bbratkus Get some Document Informations.
 */
public class PdfTesterDocumentInformationTest extends TestCase
{
  private DocumentTester tester;

  public PdfTesterDocumentInformationTest (final String name)
  {
    super (name);
  }

  @Override
  protected void setUp () throws Exception
  {
    tester = getPdfTesterViaString ();
  }

  @Override
  protected void tearDown () throws Exception
  {
    tester.close ();
  }

  private DocumentTester getPdfTesterViaString ()
  {
    return new DocumentTester ("etc/testing-pdfs/DocumentInformationTest.pdf");
  }

  public void testAssertAuthorsNameEquals ()
  {
    tester.assertAuthorsNameEquals ("Benjamin Bratkus");
  }

  public void testAssertCreatorEquals ()
  {
    tester.assertCreatorEquals ("jpdfunit");
  }

  public void testAssertCreationDateEquals ()
  {
    // GregorianCalendar testDate = new
    // GregorianCalendar(2005,Calendar.JUNE, 29,16,33,02); valid
    // GregorianCalendar testDate = new GregorianCalendar(2005,
    // Calendar.JUNE, 29);
    final GregorianCalendar testDate = new GregorianCalendar (2005, Calendar.JUNE, 29, 16, 33, 02);
    tester.assertCreationDateEquals (testDate);
  }

  public void testAssertDocumentTitleEquals ()
  {
    tester.assertDocumentTitleEquals ("Testing the document information");
  }

  public void testAssertEncryptionLenghthEquals ()
  {
    tester.assertEncryptionLenghthEquals (0);
  }

  public void testAssertKeywordsEquals ()
  {
    tester.assertKeywordsEquals ("pdfbox, junit, java, testing, jpdfbox");
  }

  public void testAssertPageCountEquals ()
  {
    tester.assertPageCountEquals (1);
  }

  public void testAssertSubjectEquals ()
  {
    tester.assertSubjectEquals ("document information test");
  }

  public void testAssertVersionEquals ()
  {
    tester.assertVersionEquals (1.4f);
  }

  public void testAssertIsDocumentEncrypted ()
  {
    tester.assertIsDocumentEncrypted (false);
  }

}
