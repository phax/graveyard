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
 * $Id: DocumentTester.java,v 1.2 2011/12/09 14:03:50 s_schaefer Exp $
 */

package de.oio.jpdfunit;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;

import de.oio.jpdfunit.document.AbstractDocumentFactory;
import de.oio.jpdfunit.document.IContent;
import de.oio.jpdfunit.document.IDocument;
import de.oio.jpdfunit.document.IFont;
import de.oio.jpdfunit.document.util.TextSearchType;

/**
 * The PdfTesterClass is a core element of JPdfUnit. This class implements the
 * Assertion interfaces and has a Document and a Content for working with the
 * pdf files. The different assert methods are implemented right here.
 *
 * @author bbratkus
 */
public class DocumentTester implements IDocumentAssertion
{
  private static final String CONSTPARA = "The constructor parameter is not valild!";
  private static final String TESTEDDOC = "The tested pdf-document: ";
  private static final String FONTNAMES = "The font names: ";
  private static final String SUPTYPES = "The supposed types: ";

  private final transient IDocument m_aDocument;
  private final transient IContent m_aContent;
  private final transient IFont [] m_aFonts;
  private transient String m_sPFDFFile = null;

  private transient InputStream m_aPDFIS = null;

  private final transient AbstractDocumentFactory m_aFactory = AbstractDocumentFactory.newInstance ();

  /**
   * The constructor uses a String to get the document.
   *
   * @param file
   *        The path and the file as String. I.e. "/home/bbratkus/test.pdf", a
   *        relative path.
   */
  public DocumentTester (final String file)
  {
    if ((file == null) || (file == ""))
    {
      throw new IllegalArgumentException (CONSTPARA);
    }
    m_sPFDFFile = file;
    m_aDocument = m_aFactory.getDocument (file);
    m_aContent = m_aDocument.getContent ();
    m_aFonts = m_aDocument.getAllFontsInDocument ();
  }

  /**
   * The Constructor uses a InputStream to get the document.
   *
   * @param fileStream
   *        The Stream which the pdf file is within.
   */
  public DocumentTester (final InputStream fileStream)
  {
    if (fileStream == null)
    {
      throw new IllegalArgumentException (CONSTPARA);
    }
    m_aPDFIS = fileStream;
    m_aDocument = m_aFactory.getDocument (fileStream);
    m_aContent = m_aDocument.getContent ();
    m_aFonts = m_aDocument.getAllFontsInDocument ();
  }

  /**
   * The method compares the supposed authors name to the authors name of the
   * pdf document.
   *
   * @param expected
   *        The expected authors name, the methods checks if the expected value
   *        equals the authors name of the pdf file.
   */
  public void assertAuthorsNameEquals (final String expected)
  {
    checkAttributes ();
    if (checkConstructorParam ())
    {
      Assert.assertTrue (TESTEDDOC +
                         m_sPFDFFile +
                         " doesn't contain the expected author name: " +
                         expected,
                         m_aDocument.getAuthor ().equals (expected));
    }
    else
    {
      Assert.assertTrue ("The tested pdf-document doesn't contain the expected author name: " +
                         expected,
                         m_aDocument.getAuthor ().equals (expected));
    }

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
    checkAttributes ();
    if (checkConstructorParam ())
    {
      Assert.assertTrue (TESTEDDOC +
                         m_sPFDFFile +
                         " doesn't contain the expected parameter: " +
                         text,
                         m_aContent.isTextContent (text, type));
    }
    else
    {
      Assert.assertTrue ("The tested pdf-document doesn't contain the expected parameter: " +
                         text,
                         m_aContent.isTextContent (text, type));
    }

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
    checkAttributes ();
    if (checkConstructorParam ())
    {
      Assert.assertTrue (TESTEDDOC +
                         m_sPFDFFile +
                         " doesn't contain the expected parameter: " +
                         text +
                         " on the actual page (" +
                         page +
                         ") .",
                         m_aContent.isTextContentOnPage (text, type, page));
    }
    else
    {
      Assert.assertTrue ("The tested pdf-document doesn't contain the expected parameter: " +
                         text +
                         " on the actual page (" +
                         page +
                         ") .",
                         m_aContent.isTextContentOnPage (text, type, page));
    }

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
    List <?> pages;
    pages = m_aContent.getListOfPagesForContent (text, type);
    checkAttributes ();
    if (checkConstructorParam ())
    {
      Assert.assertTrue (TESTEDDOC +
                         m_sPFDFFile +
                         " doesn't contain the expected parameters:" +
                         text +
                         " in the right way.",
                         0 < pages.size ());
    }
    else
    {
      Assert.assertTrue ("The tested pdf-document doesn't contain the expected parameters:" +
                         text +
                         " in the right way.",
                         0 < pages.size ());
    }

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
    checkAttributes ();
    if (checkConstructorParam ())
    {
      Assert.assertFalse (TESTEDDOC +
                          m_sPFDFFile +
                          " contains the expected parameter: " +
                          text,
                          m_aContent.isTextContent (text, type));
    }
    else
    {
      Assert.assertFalse ("The tested pdf-document contain the expected parameter: " +
                          text,
                          m_aContent.isTextContent (text, type));
    }

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
    checkAttributes ();
    if (checkConstructorParam ())
    {
      Assert.assertFalse (TESTEDDOC +
                          m_sPFDFFile +
                          " contain the expected parameter: " +
                          text +
                          " on the actual page (" +
                          page +
                          ") .",
                          m_aContent.isTextContentOnPage (text, type, page));
    }
    else
    {
      Assert.assertFalse ("The tested pdf-document contains the expected parameter: " +
                          text +
                          " on the actual page (" +
                          page +
                          ") .",
                          m_aContent.isTextContentOnPage (text, type, page));
    }

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
    List <?> pages;
    pages = m_aContent.getListOfPagesForContent (text, type);
    checkAttributes ();
    if (checkConstructorParam ())
    {
      Assert.assertFalse (TESTEDDOC +
                          m_sPFDFFile +
                          " contains the expected parameters:" +
                          text +
                          " in the right way.",
                          0 < pages.size ());
      // or 1 < pages.length
    }
    else
    {
      Assert.assertFalse ("The tested pdf-document contains the expected parameters:" +
                          text +
                          " in the right way.",
                          0 < pages.size ());
      // or 1 < pages.length
    }
  }

  /**
   * The method compares the supposed creator to the creator of the document.
   *
   * @param expected
   *        The supposed creator of the document.
   */
  public void assertCreatorEquals (final String expected)
  {
    checkAttributes ();
    if (checkConstructorParam ())
    {
      Assert.assertTrue ("The supposed creator isn't equals to the creator: " +
                         expected +
                         " of the pdf file: " +
                         m_sPFDFFile,
                         m_aDocument.getCreator ().equals (expected));
    }
    else
    {
      Assert.assertTrue ("The supposed creator isn't equals to the creator: " +
                         expected +
                         " of the pdf file",
                         m_aDocument.getCreator ().equals (expected));
    }
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
    checkAttributes ();
    if (checkConstructorParam ())
    {
      Assert.assertTrue ("The supposed creation date: " +
                         expected +
                         " isn't equal to the creation date of the pdf file: " +
                         m_sPFDFFile,
                         m_aDocument.getCreationDate ().getTime ().equals (expected.getTime ()));
    }
    else
    {
      Assert.assertTrue ("The supposed creation date: " +
                         expected +
                         " isn't equal to the creation date of the pdf file.",
                         m_aDocument.getCreationDate ().getTime ().equals (expected.getTime ()));
    }
  }

  /**
   * The method compares the supposed owner password with the owner password if
   * it exists of the document and trys to decrypt the document.
   *
   * @param expected
   *        The supposed owner password. This method tries to decrypt the pdf
   *        file with this password.
   */
  public void assertDecryptionWithOwnerPasswd (final String expected)
  {
    checkAttributes ();
    if (checkConstructorParam ())
    {
      Assert.assertNotNull ("Can not decrypt the document: " +
                            m_sPFDFFile +
                            " with the supposed owner password: " +
                            expected,
                            m_aDocument.decryptDocument (expected));
    }
    else
    {
      Assert.assertNotNull ("Can not decrypt the document with the supposed owner password: " +
                            expected,
                            m_aDocument.decryptDocument (expected));
    }
  }

  /**
   * The method compares the supposed user password with the user password if it
   * exists of the document and trys to decrypt the document.
   *
   * @param expected
   *        The supposed user password. This method tries to decrypt the pdf
   *        file with this password.
   */
  public void assertDecryptionWithUserPasswd (final String expected)
  {
    checkAttributes ();
    if (checkConstructorParam ())
    {
      Assert.assertNotNull ("Can not decrypt the document: " +
                            m_sPFDFFile +
                            " with the supposed user password: " +
                            expected,
                            m_aDocument.decryptDocument (expected));
    }
    else
    {
      Assert.assertNotNull ("Can not decrypt the document with the supposed user password: " +
                            expected,
                            m_aDocument.decryptDocument (expected));
    }

  }

  /**
   * The method comapares the supposed document title of the document
   * informations with the document title of the document.
   *
   * @param expected
   *        The expected document title, the methods checks if the expected
   *        value equals the document title of the pdf file.
   */
  public void assertDocumentTitleEquals (final String expected)
  {
    checkAttributes ();
    if (checkConstructorParam ())
    {
      Assert.assertTrue ("The supposed title: " +
                         expected +
                         " is not equal to the title of the tested pdf file: " +
                         m_sPFDFFile,
                         m_aDocument.getTitle ().equals (expected));
    }
    else
    {
      Assert.assertTrue ("The supposed title: " +
                         expected +
                         " is not equal to the title of the tested pdf file.",
                         m_aDocument.getTitle ().equals (expected));

    }

  }

  /**
   * The method compares the supposed encryption length with the encryption
   * length of the document if it is encrypted else it returns 0.
   *
   * @param expected
   *        The expected encryption length, the method checks if the expected
   *        value equals the encryption length of the pdf file.
   */
  public void assertEncryptionLenghthEquals (final int expected)
  {
    checkAttributes ();
    if (checkConstructorParam ())
    {
      Assert.assertTrue ("The supposed encryption length: " +
                         expected +
                         "isn't equal to the encryption length of the pdf file: " +
                         m_sPFDFFile,
                         m_aDocument.getEnryptionLength () == expected);
    }
    else
    {
      Assert.assertTrue ("The supposed encryption length: " +
                         expected +
                         "isn't equal to the encryption length of the pdf file",
                         m_aDocument.getEnryptionLength () == expected);
    }

  }

  /**
   * The method compares the supposed keywords to the keywords of the document
   * informations of the document.
   *
   * @param expected
   *        The expected keywords, the method checks if the expected keywords
   *        equals the keywords of the pdf file.
   */
  public void assertKeywordsEquals (final String expected)
  {
    checkAttributes ();
    if (checkConstructorParam ())
    {
      Assert.assertTrue (TESTEDDOC +
                         m_sPFDFFile +
                         " doesn't contain the expected keywords: " +
                         expected,
                         m_aDocument.getKeywords ().equals (expected));
    }
    else
    {
      Assert.assertTrue ("The tested pdf-document doesn't contain the expected keywords: " +
                         expected,
                         m_aDocument.getKeywords ().equals (expected));

    }

  }

  /**
   * The method compares the supposed page number to the page number of the
   * document.
   *
   * @param expected
   *        The expected page count. The method checks if the expected page
   *        count equals the page count of the pdf file. The page count is
   *        1-based.
   */
  public void assertPageCountEquals (final int expected)
  {
    checkAttributes ();
    final int pages = m_aDocument.countPages ();
    if (checkConstructorParam ())
    {
      Assert.assertTrue (TESTEDDOC +
                         m_sPFDFFile +
                         " doesn't match with the expected parameter(page count): " +
                         expected,
                         expected == pages);
    }
    else
    {
      Assert.assertTrue ("The tested pdf-document doesn't match with" +
                         " the expected parameter(page count): " +
                         expected,
                         expected == pages);
    }
  }

  /**
   * The method compares the supposed subject to the subject of the document.
   *
   * @param expected
   *        The expected subject, the method checks if the expected subject
   *        equals the subject of the pdf file.
   */
  public void assertSubjectEquals (final String expected)
  {
    checkAttributes ();
    if (checkConstructorParam ())
    {
      Assert.assertTrue (TESTEDDOC +
                         m_sPFDFFile +
                         " doesn't match with the expected parameter(subject): " +
                         expected,
                         m_aDocument.getSubject ().equals (expected));
    }
    else
    {
      Assert.assertTrue ("The tested pdf-document doesn't match with" +
                         " the expected parameter(subject): " +
                         expected,
                         m_aDocument.getSubject ().equals (expected));
    }

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
    if (checkConstructorParam ())
    {
      Assert.assertTrue (TESTEDDOC +
                         m_sPFDFFile +
                         " doesn't match with the expected parameter: " +
                         expected,
                         m_aDocument.isDocumentEncrypted () == expected);
    }
    else
    {
      Assert.assertTrue ("The tested pdf-document doesn't match with the expected parameter: " +
                         expected +
                         " of the document.",
                         m_aDocument.isDocumentEncrypted () == expected);
    }

  }

  /**
   * The method compares the supposed owner password with the owner password if
   * it exists of the document.
   *
   * @param expected
   *        The expected owner password. The method compares the expected owner
   *        password to the owner password of the pdf file. This is necessary
   *        for changing the document attributes.
   */
  @Deprecated
  public void assertIsOwnerPasswd (final String expected)
  {
    throw new UnsupportedOperationException ();
    // checkAttributes();
    // if (checkConstructorParam()) {
    // Assert.assertTrue(TESTEDDOC + pdfFile
    // + " doesn't match with the expected owner password: " + expected,
    // document.isOwnerPasswd(expected));
    // } else {
    // Assert.assertTrue("The expected owner password: " + expected
    // + " doensn't match the owner password of the pdf file",
    // document.isOwnerPasswd(expected));
    // }
  }

  /**
   * The method compares the supposed user password with the user password if it
   * exists of the document.
   *
   * @param expected
   *        The expected user password. The method compares the expected user
   *        password to the user password of the pdf file. This is necessary for
   *        reading the pdf file.
   */
  @Deprecated
  public void assertIsUserPasswd (final String expected)
  {
    throw new UnsupportedOperationException ();
    // checkAttributes();
    // if (checkConstructorParam()) {
    // Assert.assertTrue(
    // TESTEDDOC + pdfFile
    // + " doens't match the expected userpassword: " + expected
    // , document.isUserPasswd(expected));
    // } else {
    // Assert.assertTrue("The expected user password: " + expected
    // + " doens't match the userpassword of the tested pdf file",
    // document.isUserPasswd(expected));
    // }
  }

  /**
   * The method compares the supposed version to the version of the document.
   *
   * @param expected
   *        The expected version of the pdf file. The method checks if the
   *        expected version equals the version of the pdf file.
   */
  public void assertVersionEquals (final float expected)
  {
    checkAttributes ();
    if (checkConstructorParam ())
    {
      Assert.assertTrue (TESTEDDOC +
                         m_sPFDFFile +
                         " do not match, the expected version: " +
                         expected,
                         expected == m_aDocument.getVersion ());
    }
    else
    {
      Assert.assertTrue ("The Versions of the document do not match, expected: " +
                         expected,
                         expected == m_aDocument.getVersion ());
    }
  }

  /**
   * File handling you have to close the document after finishing your tests.
   */
  public void close ()
  {
    m_aDocument.closeDocument ();
  }

  /**
   * The method returns the Content of the Document.
   *
   * @return Returns the content.
   */
  public IContent getContent ()
  {
    checkAttributes ();
    return m_aContent;
  }

  /**
   * The method returns the Document of the DocumentTester.
   *
   * @return Returns the document.
   */
  public IDocument getDocument ()
  {
    checkAttributes ();
    return m_aDocument;
  }

  /**
   * This method checks if one parameter is not valid.
   */
  private void checkAttributes ()
  {
    if ((this.m_aContent == null) || (this.m_aDocument == null))
    {
      throw new IllegalStateException ("A parameter is not valild !");
    }

  }

  /** {@inheritDoc} */
  public void assertFontNameInDocumentEquals (final String expected)
  {
    checkAttributes ();
    boolean found = false;
    int counter = 0;
    int position = -1;
    while ((!found) && (counter < m_aFonts.length))
    {

      position = m_aFonts[counter].getFontName ().indexOf (expected);
      counter++;
      if (0 <= position)
      {
        found = true;
      }
    }
    if (checkConstructorParam ())
    {
      Assert.assertTrue ("The font: " + expected + " does not match in the document: " + m_sPFDFFile + ".", found);
    }
    else
    {
      Assert.assertTrue ("The font: " + expected + " does not match.", found);
    }
  }

  /** {@inheritDoc} */
  public void assertAllFontNamesInDocument (final String [] expected)
  {
    checkAttributes ();
    boolean [] tested;
    tested = new boolean [expected.length];
    boolean found = false;
    int position = -1;
    for (int x = 0; x < expected.length; x++)
    {
      int counter = 0;
      while ((!tested[x]) && (counter < m_aFonts.length))
      {
        position = m_aFonts[counter].getFontName ().indexOf (expected[x]);
        counter++;
        if (0 <= position)
        {
          tested[x] = true;
        }
      }
      for (final boolean element : tested)
      {
        if (element)
        {
          found = true;
        }
        else
        {
          found = false;
        }
      }
    }
    if (checkConstructorParam ())
    {
      Assert.assertTrue (FONTNAMES + expected + " do not match in the document: " + m_sPFDFFile + ".", found);
    }
    else
    {
      Assert.assertTrue (FONTNAMES + expected + " do not match.", found);
    }

  }

  /** {@inheritDoc} */
  public void assertFontNameOnPage (final String expected, final int page)
  {
    checkAttributes ();
    boolean found = false;
    int counter = 0;
    int position = -1;
    while ((!found) && (counter < m_aFonts.length))
    {

      position = m_aFonts[counter].getFontName ().indexOf (expected);
      counter++;
      if (0 <= position)
      {
        found = true;
      }
    }
    if (checkConstructorParam ())
    {
      Assert.assertTrue ("The font name: " + expected + " does not match in the document: " + m_sPFDFFile + ".", found);
    }
    else
    {
      Assert.assertTrue ("The font name: " + expected + " does not match.", found);
    }
  }

  /** {@inheritDoc} */
  public void assertAllFontNamesOnPage (final String [] expected, final int page)
  {
    checkAttributes ();
    boolean [] tested;
    tested = new boolean [expected.length];
    boolean found = false;
    int position = -1;
    for (int x = 0; x < expected.length; x++)
    {
      int counter = 0;
      while ((!tested[x]) && (counter < m_aFonts.length))
      {
        position = m_aFonts[counter].getFontName ().indexOf (expected[x]);
        counter++;
        if (0 <= position)
        {
          tested[x] = true;
        }
      }
      for (final boolean element : tested)
      {
        if (element)
        {
          found = true;
        }
        else
        {
          found = false;
        }
      }
    }
    if (checkConstructorParam ())
    {
      Assert.assertTrue (FONTNAMES + expected + " do not match in the document: " + m_sPFDFFile + ".", found);
    }
    else
    {
      Assert.assertTrue (FONTNAMES + expected + " do not match.", found);
    }

  }

  /** {@inheritDoc} */
  public void assertFontTypeInDocument (final String expected)
  {
    checkAttributes ();
    boolean found = false;
    int counter = 0;
    int position = -1;
    while ((!found) && (counter < m_aFonts.length))
    {

      position = m_aFonts[counter].getFontType ().indexOf (expected);
      counter++;
      if (0 <= position)
      {
        found = true;
      }
    }
    if (checkConstructorParam ())
    {
      Assert.assertTrue ("The supposed type: " +
                         expected +
                         " does not match in the document: " +
                         m_sPFDFFile +
                         ".",
                         found);
    }
    else
    {
      Assert.assertTrue ("The supposed type: " + expected + " does not match.", found);
    }
  }

  /** {@inheritDoc} */
  public void assertAllFontTypesInDocument (final String [] expected)
  {
    checkAttributes ();
    boolean [] tested;
    tested = new boolean [expected.length];
    boolean found = false;
    int position = -1;
    for (int x = 0; x < expected.length; x++)
    {
      int counter = 0;
      while ((!tested[x]) && (counter < m_aFonts.length))
      {
        position = m_aFonts[counter].getFontType ().indexOf (expected[x]);
        counter++;
        if (0 <= position)
        {
          tested[x] = true;
        }
      }
      for (final boolean element : tested)
      {
        if (element)
        {
          found = true;
        }
        else
        {
          found = false;
        }
      }
    }
    if (checkConstructorParam ())
    {
      Assert.assertTrue (SUPTYPES + expected + " do not match the hole document: " + m_sPFDFFile + ".", found);
    }
    else
    {
      Assert.assertTrue (SUPTYPES + expected + " do not match the hole document.", found);
    }
  }

  /** {@inheritDoc} */
  public void assertFontTypeOnPage (final String expected, final int page)
  {
    checkAttributes ();
    boolean found = false;
    int counter = 0;
    int position = -1;
    while ((!found) && (counter < m_aFonts.length))
    {

      position = m_aFonts[counter].getFontType ().indexOf (expected);
      counter++;
      if (0 <= position)
      {
        found = true;
      }
    }
    if (checkConstructorParam ())
    {
      Assert.assertTrue ("The type: " +
                         expected +
                         " does not match on this Page of the document: " +
                         m_sPFDFFile +
                         ".",
                         found);
    }
    else
    {
      Assert.assertTrue ("The type: " + expected + " does not match on this Page.", found);
    }

  }

  /** {@inheritDoc} */
  public void assertAllFontTypesOnPage (final String [] expected, final int page)
  {
    checkAttributes ();
    boolean [] tested;
    tested = new boolean [expected.length];
    boolean found = false;
    int position = -1;
    for (int x = 0; x < expected.length; x++)
    {
      int counter = 0;
      while ((!tested[x]) && (counter < m_aFonts.length))
      {
        position = m_aFonts[counter].getFontName ().indexOf (expected[x]);
        counter++;
        if (0 <= position)
        {
          tested[x] = true;
        }
      }
      for (final boolean element : tested)
      {
        if (element)
        {
          found = true;
        }
        else
        {
          found = false;
        }
      }
    }
    if (checkConstructorParam ())
    {
      Assert.assertTrue (SUPTYPES +
                         expected +
                         " do not match on the supposed page of the document: " +
                         m_sPFDFFile +
                         ".",
                         found);
    }
    else
    {
      Assert.assertTrue (SUPTYPES + expected + " do not match on the supposed page of the document.", found);
    }
  }

  private boolean checkConstructorParam ()
  {
    boolean returnBool = false;
    if (m_aPDFIS == null && !(m_sPFDFFile == null))
    {
      returnBool = true;
    }
    return returnBool;
  }

  public void assertNumberOfBookmarks (final int numberOfBookmarks)
  {
    final List <?> list = m_aDocument.getAllBookmarks ();
    if (list == null)
    {
      if (numberOfBookmarks == 0)
      {
        Assert.assertTrue (true);
      }
      else
      {
        Assert.fail ("Not Same");
      }

    }
    else
    {
      Assert.assertEquals (numberOfBookmarks, list.size ());
    }

  }

  public void assertBookmarkExists (final String bookmarkName)
  {
    final List <?> list = m_aDocument.getAllBookmarks ();
    if ((list == null) && (bookmarkName == null))
    {
      Assert.assertTrue (true);
    }
    else
    {
      if (list == null)
      {
        Assert.fail ("Not Same");
      }
      else
      {
        if (bookmarkName == null)
        {
          Assert.fail ("Not Same");
        }
        else
        {
          final Iterator <?> i = list.iterator ();
          boolean found = false;
          while (i.hasNext ())
          {
            final String s = (String) i.next ();
            if (s.equals (bookmarkName))
            {
              Assert.assertTrue (true);
              found = true;
              break;
            }
          }
          if (!found)
          {
            Assert.fail ("Unable to find" + bookmarkName);
          }
        }
      }
    }
  }

  public void assertBookmarksAre (final String [] bookmarks)
  {
    int length = 0;
    if (bookmarks != null)
    {
      length = bookmarks.length;
    }
    assertNumberOfBookmarks (length);
    final List <?> list = m_aDocument.getAllBookmarks ();
    for (int i = 0; i < list.size (); i++)
    {
      Assert.assertEquals (bookmarks[i], list.get (i));
    }
  }
}
