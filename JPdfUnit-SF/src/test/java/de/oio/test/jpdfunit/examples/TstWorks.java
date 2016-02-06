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
* $Id: TstWorks.java,v 1.1 2009/12/14 15:58:48 s_schaefer Exp $
*/
package de.oio.test.jpdfunit.examples;

import de.oio.jpdfunit.DocumentTester;

/**
 * @author bbratkus Work with the Tester in an application.
 */
public class TstWorks
{

  private static DocumentTester tester = new DocumentTester ("etc/testing-pdfs/DocumentEncryptionTest.pdf");
  private static DocumentTester myTester = new DocumentTester ("etc/testing-pdfs/DocumentInformationTest.pdf");

  public static void main (final String [] args) throws Exception
  {

    System.out.println ("Is document encrypted? " + tester.getDocument ().isDocumentEncrypted ());
    System.out.println ("Page Count ? " + myTester.getDocument ().countPages ());
    tester.close ();
    myTester.close ();

  }
}
