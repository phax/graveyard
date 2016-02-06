package de.oio.test.jpdfunit;

import java.util.List;

import de.oio.jpdfunit.DocumentTester;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

public class PdfTesterBookmarkCatalogTest extends TestCase
{

  DocumentTester tester;

  public PdfTesterBookmarkCatalogTest ()
  {}

  public PdfTesterBookmarkCatalogTest (final String name)
  {
    super (name);
  }

  @Override
  protected void setUp () throws Exception
  {
    tester = new DocumentTester ("etc/testing-pdfs/oio-katalog-mit-logo-mit-farben.pdf");
  }

  @Override
  protected void tearDown () throws Exception
  {
    tester.close ();
  }

  public void test_numberOfBookmarks ()
  {
    final List <String> list = tester.getDocument ().getAllBookmarks ();
    tester.assertNumberOfBookmarks (121);
  }

  public void test_bookmarkExists ()
  {
    tester.assertBookmarkExists ("Impressum");
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
