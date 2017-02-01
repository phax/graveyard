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
* $Id: PdfTesterBookmarkTest.java,v 1.1 2009/12/14 15:58:51 s_schaefer Exp $
*/

package de.oio.test.jpdfunit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.oio.jpdfunit.DocumentTester;

/**
 * @author bbratkus TODO To change the template for this generated type comment
 *         go to Window - Preferences - Java - Code Style - Code Templates
 */
public class PdfTesterBookmarkTest
{
  DocumentTester tester;

  @Before
  public void setUp ()
  {
    tester = new DocumentTester ("etc/testing-pdfs/bookMarkTest.pdf");
  }

  @After
  public void tearDown ()
  {
    tester.close ();
  }

  @Test
  public void test_numberOfBookmarks ()
  {
    tester.assertNumberOfBookmarks (1);
  }

  @Test
  public void test_bookmarkExists ()
  {
    tester.assertBookmarkExists ("CID");
  }

  @Test
  public void test_bookmarkNotExists ()
  {
    try
    {
      tester.assertBookmarkExists ("UNKNOWN");
      fail ("Bookmark should have failed");
    }
    catch (final AssertionError e)
    {
      assertTrue (true);
    }
  }

  @Test
  public void test_bookmarksAre ()
  {
    tester.assertBookmarksAre (new String [] { "CID" });
  }

  @Test
  public void test_bookmarksArePartial ()
  {
    try
    {
      tester.assertBookmarksAre (new String [] { "CID", "PAGE2" });
      fail ("Should Fail two many bookmarks");
    }
    catch (final AssertionError e)
    {
      assertTrue (true);
    }
  }

  @Test
  public void test_bookmarksAreNull ()
  {
    try
    {
      tester.assertBookmarksAre (null);
      fail ("Should Fail null");
    }
    catch (final AssertionError e)
    {
      assertTrue (true);
    }
  }

  @Test
  public void test_bookmarksAreNotMatch ()
  {
    try
    {
      tester.assertBookmarksAre (new String [] { "PAGE2" });
      fail ("Should Fail not correct bookmarks");
    }
    catch (final AssertionError e)
    {
      assertTrue (true);
    }
  }
}
