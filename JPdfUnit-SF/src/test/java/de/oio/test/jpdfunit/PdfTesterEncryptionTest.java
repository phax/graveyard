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
* $Id: PdfTesterEncryptionTest.java,v 1.2 2011/12/09 14:03:50 s_schaefer Exp $
*/

package de.oio.test.jpdfunit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import de.oio.jpdfunit.DocumentTester;
import junit.framework.TestCase;

/**
 * @author bbratkus Test decrypt and Informations.
 */
public class PdfTesterEncryptionTest extends TestCase
{

  private DocumentTester tester;

  public PdfTesterEncryptionTest (final String name)
  {
    super (name);
  }

  @Override
  protected void setUp () throws Exception
  {
    tester = getPdfTesterViaString ();
    // tester = getPdfTesterViaStream();
  }

  @Override
  protected void tearDown () throws Exception
  {
    tester.close ();
  }

  private DocumentTester getPdfTesterViaString ()
  {
    return new DocumentTester ("etc/testing-pdfs/DocumentEncryptionTest.pdf");
  }

  private DocumentTester getPdfTesterViaStream ()
  {
    FileInputStream file = null;
    final String data = "etc/testing-pdfs/DocumentEncryptionTest.pdf";
    try
    {
      file = new FileInputStream (data);
      return new DocumentTester (file);
    }
    catch (final FileNotFoundException e)
    {
      System.out.println ("the catch PdfTester");
      return new DocumentTester ("etc/testing-pdfs/DocumentEncryptionTest.pdf");
    }

  }

  public void testAssertEncryptionLenghthEquals ()
  {
    tester.assertEncryptionLenghthEquals (40);
    // tester.assertEncryptionLenghthEquals(0);
    // tester.assertEncryptionLenghthEquals(-1);
  }

  public void testAssertIsDocumentEncrypted ()
  {
    tester.assertIsDocumentEncrypted (true);
    // tester.assertIsDocumentEncrypted(false);
  }

  public void testAssertIsOwnerPasswd ()
  {
    tester.getDocument ().decryptDocument ("O1O-jpdfunit");
    // tester.assertIsOwnerPasswd(null);
    // tester.assertIsOwnerPasswd("");
  }

  public void testAssertIsUserPasswd ()
  {
    tester.getDocument ().decryptDocument ("jpdfunit");
    // tester.assertIsUserPasswd(null);
    // tester.assertIsUserPasswd("");
  }

  public void testAssertDecryptionWithOwnerPasswd ()
  {
    tester.assertDecryptionWithOwnerPasswd ("O1O-jpdfunit");
    // tester.assertDecryptionWithOwnerPasswd(null);
    // tester.assertDecryptionWithOwnerPasswd("");
  }

  public void testAssertDecryptionWithUserPasswd ()
  {
    tester.assertDecryptionWithUserPasswd ("jpdfunit");
    // tester.assertDecryptionWithUserPasswd(null);
    // tester.assertDecryptionWithUserPasswd("");
  }

}
