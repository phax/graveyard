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
* $Id: DocumentTestCase.java,v 1.1 2009/12/14 15:58:49 s_schaefer Exp $
*/

package de.oio.jpdfunit;

import java.util.Calendar;

import de.oio.jpdfunit.document.util.IDocumentDataSource;
import de.oio.jpdfunit.document.util.TextSearchType;
import junit.framework.TestCase;

/**
 * The DocumentTestCase class is the basic class for your own tests. For
 * implementing own tests extending the DocumentTestCase class you have to
 * provide a getDataSource() method where you return your DocumentDataSource ie.
 * a "PdfDataSource".
 *
 * @author bbratkus
 */
public abstract class AbstractDocumentTestCase extends TestCase
{
  private static DocumentTester tester;

  /**
   * @param name
   *        Constructs a test case with the given name.
   */
  public AbstractDocumentTestCase (final String name)
  {
    super (name);
  }

  /**
   * The method compares the supposed authors name to the authors name of the
   * pdf document.
   *
   * @param expected
   *        The expected authors name, the methods checks if the expected value
   *        equals the authors name of the file.
   */
  public void assertAuthorsNameEquals (final String expected)
  {
    tester.assertAuthorsNameEquals (expected);
  }

  /**
   * This assert method checks if the supposed parameters are contained in the
   * document. The method matches the first time the text was found in the
   * document.
   *
   * @param text
   *        The text to search within the document for. Even a regular
   *        expression is a valid parameter.
   * @param type
   *        The TextSearchtype but remember that this is relative to the single
   *        pages of the document.
   */
  public void assertContentContainsText (final String text, final TextSearchType type)
  {
    tester.assertContentContainsText (text, type);
  }

  /**
   * This assert method checks if the supposed parameters are contained in the
   * document. The method trys to match the text on a certain page in the
   * document.
   *
   * @param text
   *        The text to search within the document for. Even a regular
   *        expression is a valid parameter.
   * @param page
   *        The supposed page to search for the text. The page count is 1-based.
   * @param type
   *        The TextSearchtype but remember that this is relative to the single
   *        pages of the document.
   */
  public void assertContentContainsTextOnPage (final String text, final int page, final TextSearchType type)
  {
    tester.assertContentContainsTextOnPage (text, page, type);
  }

  /**
   * This assert method checks if the supposed parameters are contained in the
   * document. The method trys to match all the time the text was found in the
   * document.
   *
   * @param text
   *        The text to search within the document for. Even a regular
   *        expression is a valid parameter.
   * @param type
   *        The TextSearchtype but remember that this is relative to the single
   *        pages of the document.
   */
  public void assertContentContainsTextMultipleTimes (final String text, final TextSearchType type)
  {
    tester.assertContentContainsTextMultipleTimes (text, type);
  }

  /**
   * This assert method checks if the supposed parameters are not contained in
   * the document. The method matches the first time the text was found in the
   * document.
   *
   * @param text
   *        The text to search within the document for. Even a regular
   *        expression is a valid parameter.
   * @param type
   *        The TextSearchtype but remember that this is relative to the single
   *        pages of the document.
   */
  public void assertContentDoesNotContainText (final String text, final TextSearchType type)
  {
    tester.assertContentDoesNotContainText (text, type);
  }

  /**
   * This assert method checks if the supposed parameters are not contained in
   * the document. The method trys to match the text on a certain page in the
   * document.
   *
   * @param text
   *        The text to search within the document for. Even a regular
   *        expression is a valid parameter.
   * @param page
   *        The supposed page to search for the text. The page count is 1-based.
   * @param type
   *        The TextSearchtype but remember that this is relative to the single
   *        pages of the document.
   */
  public void assertContentDoesNotContainTextOnPage (final String text, final int page, final TextSearchType type)
  {
    tester.assertContentDoesNotContainTextOnPage (text, page, type);
  }

  /**
   * This assert method checks if the supposed parameters are not contained in
   * the document. The method trys to match all the time the text was found in
   * the document.
   *
   * @param text
   *        The text to search within the document for. Even a regular
   *        expression is a valid parameter.
   * @param type
   *        The TextSearchtype but remember that this is relative to the single
   *        pages of the document.
   */
  public void assertContentDoesNotContainTextMultipleTimes (final String text, final TextSearchType type)
  {
    tester.assertContentDoesNotContainTextMultipleTimes (text, type);
  }

  /**
   * The method compares the supposed creator to the creator of the document.
   *
   * @param expected
   *        The supposed creator of the document.
   */
  public void assertCreatorEquals (final String expected)
  {
    tester.assertCreatorEquals (expected);
  }

  /**
   * The method comapares the supposed creation date to the creation date of the
   * document.
   *
   * @param expected
   *        The expected creation date of the document. It will be compared to
   *        the creation date of the document.
   */

  public void assertCreationDateEquals (final Calendar expected)
  {
    tester.assertCreationDateEquals (expected);
  }

  /**
   * The method compares the supposed owner password with the owner password if
   * it exists of the document and trys to decrypt the document.
   *
   * @param expected
   *        The supposed owner password. This method tries to decrypt the file
   *        with this password.
   */
  public void assertDecryptWithOwnerPasswd (final String expected)
  {
    tester.assertDecryptionWithOwnerPasswd (expected);
  }

  /**
   * The method compares the supposed user password with the user password if it
   * exists of the document and trys to decrypt the document.
   *
   * @param expected
   *        The supposed user password. This method tries to decrypt the file
   *        with this password.
   */
  public void assertDecryptWithUserPasswd (final String expected)
  {
    tester.assertDecryptionWithUserPasswd (expected);
  }

  /**
   * The method comapares the supposed document title of the document
   * informations with the document title of the document.
   *
   * @param exspected
   *        The expected document title, the methods checks if the expected
   *        value equals the document title of the file.
   */
  public void assertDocumentTitleEquals (final String exspected)
  {
    tester.assertDocumentTitleEquals (exspected);
  }

  /**
   * The method compares the supposed encryption length with the encryption
   * length of the document if it is encrypted else it returns 0.
   *
   * @param exspected
   *        The expected encryption length, the method checks if the expected
   *        value equals the encryption length of the file.
   */
  public void assertEncryptionLenghthEquals (final int exspected)
  {
    tester.assertEncryptionLenghthEquals (exspected);
  }

  /**
   * The method compares the supposed keywords to the keywords of the document
   * informations of the document.
   *
   * @param exspected
   *        The expected keywords, the method checks if the expected keywords
   *        equals the keywords of the file.
   */
  public void assertKeywordsEquals (final String exspected)
  {
    tester.assertKeywordsEquals (exspected);
  }

  /**
   * The method compares the supposed page number to the page number of the
   * document.
   *
   * @param exspected
   *        The expected page count. The method checks if the expected page
   *        count equals the page count of the file. The page count is 1-based.
   */

  public void assertPageCount (final int exspected)
  {
    tester.assertPageCountEquals (exspected);
  }

  /**
   * The method compares the supposed subject to the subject of the document.
   *
   * @param expected
   *        The expected subject, the method checks if the expected subject
   *        equals the subject of the file.
   */
  public void assertSubjectEquals (final String expected)
  {
    tester.assertSubjectEquals (expected);
  }

  /**
   * The method checks if the current document is encrypted and comapares it to
   * the parameter.
   *
   * @param expected
   *        Expected has to be set to "true" if you suppose that the document is
   *        encrypted.
   */
  public void assertIsDocumentEncrypted (final boolean expected)
  {
    tester.assertIsDocumentEncrypted (expected);
  }

  /**
   * The method compares the supposed owner password with the owner password if
   * it exists of the document.
   *
   * @param expected
   *        The expected owner password. The method compares the expected owner
   *        password to the owner password of the file. This is necessary for
   *        changing the document attributes.
   */
  public void assertIsOwnerPasswd (final String expected)
  {
    tester.assertIsOwnerPasswd (expected);
  }

  /**
   * The method compares the supposed user password with the user password if it
   * exists of the document.
   *
   * @param expected
   *        The expected user password. The method compares the expected user
   *        password to the user password of the file. This is necessary for
   *        reading the file.
   */
  public void assertIsUserPasswd (final String expected)
  {
    tester.assertIsUserPasswd (expected);
  }

  /**
   * The method compares the supposed version to the version of the document.
   *
   * @param expected
   *        The expected version of the file. The method checks if the expected
   *        version equals the version of the file.
   */
  public void assertVersionEquals (final float expected)
  {
    tester.assertVersionEquals (expected);
  }

  /**
   * @param expected
   *        A expected font name like "Verdana-BoldItalic".
   */
  public void assertFontNameInDocument (final String expected)
  {
    tester.assertFontNameInDocumentEquals (expected);
  }

  /**
   * @param expected
   *        Expected font names like "Verdana-BoldItalic".
   */
  public void assertAllFontNamesInDocument (final String [] expected)
  {
    tester.assertAllFontNamesInDocument (expected);
  }

  /**
   * @param expected
   *        A expected font name like "Verdana-BoldItalic".
   * @param page
   *        The supposed page number, notice it is 1-based.
   */
  public void assertFontNameOnPage (final String expected, final int page)
  {
    tester.assertFontNameOnPage (expected, page);
  }

  /**
   * @param expected
   *        Expected font names like "Verdana-BoldItalic".
   * @param page
   *        The supposed page number, notice it is 1-based.
   */
  public void assertAllFontNamesOnPage (final String [] expected, final int page)
  {
    tester.assertAllFontNamesOnPage (expected, page);
  }

  /**
   * @param expected
   *        A supposed font type, i.e. "TrueType" or "Type1".
   */
  public void assertFontTypeInDocument (final String expected)
  {
    tester.assertFontTypeInDocument (expected);
  }

  /**
   * @param expected
   *        All supposed font types in the document. Be carefull and list all of
   *        them. A supposed font type is i.e. "TrueType" or "Type1".
   */
  public void assertAllFontTypesInDocument (final String [] expected)
  {
    tester.assertAllFontTypesInDocument (expected);
  }

  /**
   * @param expected
   *        A supposed font type, i.e "TrueType" or "Type1".
   * @param page
   *        The supposed page to search for the font type.
   */
  public void assertFontTypeOnPage (final String expected, final int page)
  {
    tester.assertFontTypeOnPage (expected, page);
  }

  /**
   * @param expected
   *        All supposed font types in the document. Be carefull and list all of
   *        them. A supposed font type is i.e. "TrueType" or "Type1".
   * @param page
   *        The supposed page to search for the font types.
   */
  public void assertAllFontTypesOnPage (final String [] expected, final int page)
  {
    tester.assertAllFontTypesOnPage (expected, page);
  }

  /**
   * If you would like to perform some additional steps. You can use this method
   * by overwriting it.
   */
  protected void onSetup ()
  {

  }

  /**
   * The setUp method of the JUnit framework. This extension sets up the testing
   * document and provides the user the possiblity of an individual extension
   * via the onSetup method.
   *
   * @throws Exception
   */
  @Override
  protected final void setUp () throws Exception
  {
    super.setUp ();
    setUpTestingDocument (getDataSource ());
    onSetup ();

  }

  /**
   * A must have if you want to extend the DocumentTestCase therefore you need a
   * DocumentDataSource.
   *
   * @return A DocumentDataSource this data source is necessary for starting the
   *         test.
   */
  protected abstract IDocumentDataSource getDataSource ();

  /**
   * The method instanciates the DocumentTester for the DocumentTestCase.
   *
   * @param source
   *        This method sets up the inital tester for handling the file. The
   *        kind of tester depends on the DocumentDataSource.
   */
  protected void setUpTestingDocument (final IDocumentDataSource source)
  {
    switch (source.getDatasource ())
    {
      case 0:
        tester = new DocumentTester (source.getFile ());
        break;
      case 1:
        tester = new DocumentTester (source.getStream ());
        break;
      default:
        throw new IllegalArgumentException ("Unsupported type");
    }

  }

  /**
   * A must have for a testcase. This tearDown method closes the DocumentTesters
   * Document.
   *
   * @throws Exception
   */

  @Override
  protected void tearDown () throws Exception
  {
    tester.close ();
    super.tearDown ();
  }

  /**
   * The method returns the used DocumentTester of the DocumentTestCase.
   *
   * @return Returns the tester.
   */
  public DocumentTester getTester ()
  {
    return tester;
  }

  public void assertNumberOfBookmarks (final int numberOfBookmarks)
  {
    tester.assertNumberOfBookmarks (numberOfBookmarks);
  }

  public void assertBookmarkExists (final String bookmarkName)
  {
    tester.assertBookmarkExists (bookmarkName);
  }

  public void assertBookmarksAre (final String [] bookmarks)
  {
    tester.assertBookmarksAre (bookmarks);
  }

}
