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
* $Id: EvilDocumentTest.java,v 1.1 2009/12/14 15:58:51 s_schaefer Exp $
*/
package de.oio.test.jpdfunit;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;

import org.junit.Test;

import de.oio.jpdfunit.DocumentTester;

/**
 * @author bbratkus A really bad bad Test.
 */
public class EvilDocumentTest
{
  @Test
  public void testInvalidDocuments ()
  {
    try
    {
      new DocumentTester ("");
      fail ();
    }
    catch (final IllegalArgumentException e)
    {}

    try
    {
      new DocumentTester ("etc/testing-pdfs/invalidpdf.pdf");
      fail ();
    }
    catch (final IllegalArgumentException e)
    {}

    try
    {
      new DocumentTester ("etc/testing-pdfs/invalidpdf2.pdf");
      fail ();
    }
    catch (final IllegalArgumentException e)
    {}

    try
    {
      new DocumentTester (new ByteArrayInputStream (new byte [] { 1, 2 }));
      fail ();
    }
    catch (final IllegalArgumentException e)
    {}
  }
}
