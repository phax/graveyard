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
* $Id: AllTests.java,v 1.1 2009/12/14 15:58:51 s_schaefer Exp $
*/
package de.oio.test.jpdfunit;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author bbratkus Some Tests unifyed in a Suite
 */
public final class AllTests
{
  /**
   * @return The testsuite
   */
  public static Test suite ()
  {
    final TestSuite suite = new TestSuite ();
    suite.addTest (new TestSuite (PdfTesterDocumentInformationTest.class));
    suite.addTest (new TestSuite (PdfTesterEncryptionTest.class));
    suite.addTest (new TestSuite (PdfTesterContentTest.class));
    // suite.addTest(new TestSuite(EvilDocumentTest.class));
    suite.addTest (new TestSuite (PdfTesterFontTest.class));
    suite.addTest (new TestSuite (PdfTesterBookmarkTest.class));
    suite.addTest (new TestSuite (PdfTesterBookmarkCatalogTest.class));
    return suite;
  }

  private AllTests ()
  {

  }
}
