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

import de.oio.jpdfunit.DocumentTester;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * @author bbratkus TODO To change the template for this generated type comment
 *         go to Window - Preferences - Java - Code Style - Code Templates
 */
public class PdfTesterBookmarkTest extends TestCase
{
  DocumentTester tester;

  public PdfTesterBookmarkTest ()
  {}

  public PdfTesterBookmarkTest (final String name)
  {
    super (name);
  }

  @Override
  protected void setUp () throws Exception
  {
    tester = new DocumentTester ("etc/testing-pdfs/bookMarkTest.pdf");
  }

  @Override
  protected void tearDown () throws Exception
  {
    tester.close ();
  }

  public void test_numberOfBookmarks ()
  {
    tester.assertNumberOfBookmarks (1);
  }

  public void test_bookmarkExists ()
  {
    tester.assertBookmarkExists ("CID");
  }

  public void test_bookmarkNotExists ()
  {
    try
    {
      tester.assertBookmarkExists ("UNKNOWN");
      fail ("Bookmark should have failed");
    }
    catch (final AssertionFailedError e)
    {
      assertTrue (true);
    }
  }

  public void test_bookmarksAre ()
  {
    tester.assertBookmarksAre (new String [] { "CID" });
  }

  public void test_bookmarksArePartial ()
  {
    try
    {
      tester.assertBookmarksAre (new String [] { "CID", "PAGE2" });
      fail ("Should Fail two many bookmarks");
    }
    catch (final AssertionFailedError e)
    {
      assertTrue (true);
    }
  }

  public void test_bookmarksAreNull ()
  {
    try
    {
      tester.assertBookmarksAre (null);
      fail ("Should Fail null");
    }
    catch (final AssertionFailedError e)
    {
      assertTrue (true);
    }
  }

  public void test_bookmarksAreNotMatch ()
  {
    try
    {
      tester.assertBookmarksAre (new String [] { "PAGE2" });
      fail ("Should Fail not correct bookmarks");
    }
    catch (final AssertionFailedError e)
    {
      assertTrue (true);
    }
  }

}
