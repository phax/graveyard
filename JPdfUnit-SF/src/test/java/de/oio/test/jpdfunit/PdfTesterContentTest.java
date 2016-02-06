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
* $Id: PdfTesterContentTest.java,v 1.1 2009/12/14 15:58:51 s_schaefer Exp $
*/

package de.oio.test.jpdfunit;

import de.oio.jpdfunit.DocumentTester;
import de.oio.jpdfunit.document.util.TextSearchType;
import junit.framework.TestCase;

/**
 * @author bbratkus Test for content.
 */
public class PdfTesterContentTest extends TestCase
{

  private DocumentTester tester;

  public PdfTesterContentTest (final String name)
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
    return new DocumentTester ("etc/testing-pdfs/oio-katalog-mit-logo-mit-farben.pdf");
    // return new PdfTester("");
  }

  public void testAssertContentContainsTextAtBeginnig ()
  {
    tester.assertContentContainsText ("Naja,", TextSearchType.STARTSWITH);
    // tester.assertContentContainsText("", TextSearchType.STARTSWITH);
    // tester.assertContentContainsText(null, TextSearchType.STARTSWITH);
  }

  public void testAssertContentContainsTextWithin ()
  {
    tester.assertContentContainsText ("OIO-Akademie", TextSearchType.CONTAINS);
    // tester.assertContentContainsText("", TextSearchType.CONTAINS);
    // tester.assertContentContainsText(null, TextSearchType.CONTAINS);
  }

  public void testAssertContentContainsTextAtEnd ()
  {
    tester.assertContentContainsText ("Schulungskatalog", TextSearchType.ENDSWITH);
    // tester.assertContentContainsText("Schulungskatalog",
    // TextSearchType.ENDSWITH);
    // tester.assertContentContainsText(null, TextSearchType.ENDSWITH);
  }

  public void testAssertContentContainsTextThroughRegex ()
  {

    tester.assertContentContainsText ("[\\S+\\s+]+?OIO-Akademie[\\s+\\S+]+?", TextSearchType.REGEXP);
    // tester.assertContentContainsText("", TextSearchType.REGEXP);
    // tester.assertContentContainsText(null, TextSearchType.REGEXP);

  }

  public void testAssertContentContainsTextOnPage ()
  {
    tester.assertContentContainsTextOnPage ("OIO-Akademie", 1, TextSearchType.CONTAINS);
    // tester.assertContentContainsTextOnPage("",1,TextSearchType.CONTAINS);
    // tester.assertContentContainsTextOnPage(null,1,TextSearchType.CONTAINS);

  }

  public void testAssertContentContainsTextMultipleTimes ()
  {
    tester.assertContentContainsTextMultipleTimes ("OIO-Akademie", TextSearchType.CONTAINS);
    // tester.assertContentContainsTextMultipleTimes("",
    // TextSearchType.CONTAINS);
    // tester.assertContentContainsTextMultipleTimes(null,
    // TextSearchType.CONTAINS);
  }

  public void testAssertContentNotContainsTextAtBeginnig ()
  {
    tester.assertContentDoesNotContainText ("Naaaaaja,", TextSearchType.STARTSWITH);
    // tester.assertContentContainsText("", TextSearchType.STARTSWITH);
    // tester.assertContentContainsText(null, TextSearchType.STARTSWITH);
  }

  public void testAssertContentNotContainsTextWithin ()
  {
    tester.assertContentDoesNotContainText ("OIIIIIO-Akademie", TextSearchType.CONTAINS);
    // tester.assertContentContainsText("", TextSearchType.CONTAINS);
    // tester.assertContentContainsText(null, TextSearchType.CONTAINS);
  }

  public void testAssertContentNotContainsTextAtEnd ()
  {
    tester.assertContentDoesNotContainText ("Schulungskataloooog", TextSearchType.ENDSWITH);
    // tester.assertContentContainsText("Schulungskatalog",
    // TextSearchType.ENDSWITH);
    // tester.assertContentContainsText(null, TextSearchType.ENDSWITH);
  }

  public void testAssertContentNotContainsTextThroughRegex ()
  {

    tester.assertContentDoesNotContainText ("[\\S+\\s+]+?OIIIIIO-Akademie[\\s+\\S+]+?", TextSearchType.REGEXP);
    // tester.assertContentContainsText("", TextSearchType.REGEXP);
    // tester.assertContentContainsText(null, TextSearchType.REGEXP);

  }

  public void testAssertContentNotContainsTextOnPage ()
  {
    tester.assertContentDoesNotContainTextOnPage ("OIIIIO-Akademie", 1, TextSearchType.CONTAINS);
    // tester.assertContentContainsTextOnPage("",1,TextSearchType.CONTAINS);
    // tester.assertContentContainsTextOnPage(null,1,TextSearchType.CONTAINS);

  }

  public void testAssertContentNotContainsTextMultipleTimes ()
  {
    tester.assertContentDoesNotContainTextMultipleTimes ("OIIIIIO-Akademie", TextSearchType.CONTAINS);
    // tester.assertContentContainsTextMultipleTimes("",
    // TextSearchType.CONTAINS);
    // tester.assertContentContainsTextMultipleTimes(null,
    // TextSearchType.CONTAINS);
  }

}
