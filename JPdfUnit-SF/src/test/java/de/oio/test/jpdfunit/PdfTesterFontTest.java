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
* $Id: PdfTesterFontTest.java,v 1.1 2009/12/14 15:58:51 s_schaefer Exp $
*/

package de.oio.test.jpdfunit;

import de.oio.jpdfunit.DocumentTester;
import junit.framework.TestCase;

/**
 * @author bbratkus TODO To change the template for this generated type comment
 *         go to Window - Preferences - Java - Code Style - Code Templates
 */
public class PdfTesterFontTest extends TestCase
{
  private DocumentTester tester;

  public PdfTesterFontTest ()
  {}

  public PdfTesterFontTest (final String name)
  {
    super (name);
  }

  @Override
  protected void setUp () throws Exception
  {
    tester = new DocumentTester ("etc/testing-pdfs/FontTest.pdf");
  }

  @Override
  protected void tearDown () throws Exception
  {
    tester.close ();
  }

  public void testAssertFontNameInDocumentEquals ()
  {
    tester.assertFontNameInDocumentEquals ("Verdana-BoldItalic");
  }

  public void testAssertAllFontNamesInDocument ()
  {
    final String [] fontz = { "INFKCJ+Verdana-BoldItalic",
                              "INFINM+BookAntiqua",
                              "INFKIH+Tahoma",
                              "INFJBB+Batang",
                              "INFJNP+Verdana-Italic",
                              "INFKNL+LucidaSansUnicode",
                              "INFKFN+Georgia",
                              "INFKBD+Verdana-Bold",
                              "INFHPB+TimesNewRoman",
                              "INFIBM+Arial",
                              "INFJAG+BookmanOldStyle",
                              "INFJMJ+Verdana",
                              "INFKII+Marlett",
                              "INFJIB+CourierNewPSMT" };
    tester.assertAllFontNamesInDocument (fontz);
  }

  public void testAssertFontNameOnPage ()
  {
    tester.assertFontNameOnPage ("Verdana-BoldItalic", 1);
  }

  public void testAssertFontNamesOnPage ()
  {
    final String [] fontz = { "INFKCJ+Verdana-BoldItalic",
                              "INFINM+BookAntiqua",
                              "INFKIH+Tahoma",
                              "INFJBB+Batang",
                              "INFJNP+Verdana-Italic",
                              "INFKNL+LucidaSansUnicode",
                              "INFKFN+Georgia",
                              "INFKBD+Verdana-Bold",
                              "INFHPB+TimesNewRoman",
                              "INFIBM+Arial",
                              "INFJAG+BookmanOldStyle",
                              "INFJMJ+Verdana",
                              "INFKII+Marlett",
                              "INFJIB+CourierNewPSMT" };
    tester.assertAllFontNamesOnPage (fontz, 1);
  }

  public void testAssertFontTypeInDocument ()
  {
    tester.assertFontTypeInDocument ("TrueType");
  }

  public void testAssertAllFontTypesInDocument ()
  {
    final String [] types = { "TrueType", "Type0" };
    tester.assertAllFontTypesInDocument (types);
  }

  public void testAssertFontTypeOnPage ()
  {
    tester.assertFontTypeOnPage ("TrueType", 1);
  }
}
