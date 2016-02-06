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
 * $Id: PdfBoxAnalyser.java,v 1.2 2011/12/09 14:03:49 s_schaefer Exp $
 */

package de.oio.jpdfunit.document.pdflibimpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.encryption.PDEncryptionDictionary;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.util.PDFTextStripper;

import de.oio.jpdfunit.document.util.PdfImplUtilResourceBundle;
import de.oio.jpdfunit.document.util.TextSearchType;
import de.oio.jpdfunit.document.util.ITextSearcher;

/**
 * The PdfBoxAnalyser class is the adaptor class to the pdf library PDFBox. It
 * extends the PDFTextStripper class of the PDFBox and provides via the
 * PDDocument of PDFBox and the PDFTextStripper the user the functionalities to
 * get the different informations and content of the pdf document.
 *
 * @author bbratkus
 */
class PdfBoxAnalyser extends PDFTextStripper
{
  private static final String PARAMETER = PdfImplUtilResourceBundle.getString ("PdfBoxAnalyser.inital");

  private static final String CANTDECRYPT = PdfImplUtilResourceBundle.getString ("PdfBoxAnalyser.decrypt");

  private static final String NOCONTENT = PdfImplUtilResourceBundle.getString ("PdfBoxAnalyser.content");

  private static final String NODOCINIT = PdfImplUtilResourceBundle.getString ("PdfBoxAnalyser.docInital");

  private final transient PDDocument pdDocument;

  private transient StringBuffer textbuffer = null;

  private transient LinkedList <PdfBoxFontAdapter> fonts;

  private transient ArrayList <String> bookMarkList;

  /**
   * This constructor uses a String parameter to instanciate the PDDocument.
   *
   * @param file
   *        The path and the file as String. I.e. "/home/bbratkus/test.pdf".
   * @throws IOException
   */
  public PdfBoxAnalyser (final String file) throws IOException
  {
    if ((file.equals ("") || (file == null))) //$NON-NLS-1$
    {
      throw new IllegalArgumentException (PARAMETER);
    }
    try
    {
      pdDocument = PDDocument.load (file);
    }
    catch (final IOException ioe)
    {
      throw new IllegalArgumentException (NODOCINIT);
    }
    setContentAsStringBuffer ();
    getDocumentFonts ();
  }

  /**
   * This constructor uses a InputStream as parameter to instanciate the
   * PDDocument.
   *
   * @param pdfFileStream
   *        The Stream which the pdf file is within.
   * @throws IOException
   */
  public PdfBoxAnalyser (final InputStream pdfStream) throws IOException
  {
    if (pdfStream == null)
    {
      throw new IllegalArgumentException (PARAMETER);
    }
    try
    {
      pdDocument = PDDocument.load (pdfStream);
    }
    catch (final IOException ioe)
    {
      throw new IllegalArgumentException (NODOCINIT);
    }
    setContentAsStringBuffer ();
    getDocumentFonts ();
  }

  /**
   * This method sets the Content of the the PDDocument to a StringBuffer.
   *
   * @throws IllegalArgumentException
   *         The method throws an IllegalArgumentException if if is not possible
   *         to get the content as StringBuffer of the PDDocument.
   */
  private void setContentAsStringBuffer ()
  {
    if (!pdDocument.isEncrypted ())
    {
      try
      {
        this.textbuffer = new StringBuffer (getText (pdDocument));
      }
      catch (final IOException e)
      {
        throw new IllegalArgumentException (NOCONTENT);
      }
    }
  }

  /**
   * Close the document.
   *
   * @throws IllegalStateException
   *         The method throws an IllegalStateException if the framework is not
   *         able to close the document.
   */
  public void closeDocument ()
  {
    try
    {
      pdDocument.close ();
    }
    catch (final IOException ioe)
    {
      throw new IllegalStateException (PdfImplUtilResourceBundle.getString ("PdfBoxAnalyser.close"));
    }
  }

  /**
   * This method gets the number of pages of the PDDocument.
   *
   * @return The page number of the document.
   * @throws IllegalStateException
   *         The method throws an IllegalStateException if it is not possible to
   *         get the page count.
   */
  public int countPages ()
  {

    return pdDocument.getNumberOfPages ();
  }

  /**
   * This method tries to decrypt the document via the user or the owner
   * password.
   *
   * @param passwd
   *        The supposed owner or user password of the document.
   * @throws IllegalStateException
   *         The method throws an IllegalStateException if it is not possible to
   *         decrypt the document.
   * @throws IllegalArgumentException
   *         if the supposed password do not match the password of the pdf
   *         document.
   */
  public void decryptDocument (final String passwd)
  {
    if (passwd == null)
      throw new IllegalArgumentException (PARAMETER);

    try
    {
      pdDocument.decrypt (passwd);
      if (pdDocument.isEncrypted ())
        throw new IllegalStateException (PdfImplUtilResourceBundle.getString ("PdfBoxAnalyser.encrypted"));
    }
    catch (final CryptographyException e)
    {
      throw new IllegalStateException (CANTDECRYPT);
    }
    catch (final IOException e)
    {
      throw new IllegalStateException (CANTDECRYPT);
    }
  }

  /**
   * The method gets the authors name of the PDDocumentInformation of the
   * PDDocument.
   *
   * @return The authors name which is set in the document information.
   */
  public String getAuthor ()
  {
    return pdDocument.getDocumentInformation ().getAuthor ();
  }

  /**
   * This methods files a String with the StringBuffer which holds the content.
   *
   * @return The hole content of a pdf document.
   */
  public String getContent ()
  {
    setContentAsStringBuffer ();
    return textbuffer.toString ();
  }

  /**
   * This method get the content for a certain page.
   *
   * @param page
   *        The page number of the page which content should be returned. The
   *        page count is 1-based.
   * @return The content of the selected page.
   * @throws IllegalArgumentException
   *         The method throws an IllegalArgumentException if the page number is
   *         smaller or equals 0.
   */
  public String getContentOnPage (final int page)
  {

    if ((page == 0) || (page < 0))
    {
      throw new IllegalArgumentException (PARAMETER);
    }
    StringBuffer tmpBuffer = null;
    super.setStartPage (page);
    super.setEndPage (page);
    try
    {
      tmpBuffer = new StringBuffer (super.getText (pdDocument));
    }
    catch (final IOException ioe)
    {
      throw new IllegalArgumentException (NOCONTENT);
    }
    return tmpBuffer.toString ();
  }

  /**
   * The method gets the creator of the PDDocumentInformation of the PDDocument.
   *
   * @return The creator of the document.
   */
  public String getCreator ()
  {
    return pdDocument.getDocumentInformation ().getCreator ();
  }

  /**
   * The method gets the creation date of the PDDocumentInformation of the
   * PDDocument.
   *
   * @return The creation date which is set in the document information.
   */
  public Calendar getCreationDate ()
  {
    try
    {
      return pdDocument.getDocumentInformation ().getCreationDate ();
    }
    catch (final IOException e)
    {
      throw new IllegalStateException ();
    }
  }

  /**
   * The method gets the encryption length of the PDEncryptionDictionary of the
   * PDDocument.
   *
   * @return The actual length of the choosen encryption.
   */
  public int getEncryptionLength ()
  {
    int length = 0;
    try
    {
      if (pdDocument.isEncrypted ())
      {
        final PDEncryptionDictionary dicen = pdDocument.getEncryptionDictionary ();
        length = dicen.getLength ();
      }
    }
    catch (final IOException ioe)
    {
      length = 0;
    }
    return length;
  }

  /**
   * The method gets the first page where the text appears.
   *
   * @param text
   *        The string or regular expression (constructs a regular expression
   *        matcher from a String by compiling it using a new instance of
   *        RECompiler) to search for in the document. Be carefull regex are
   *        greedy.
   * @param type
   *        The kind of search.
   * @return The first page the text was found. Returns -1 if the text can not
   *         be found
   */
  public int getFirstPageForContent (final String text, final TextSearchType type)
  {
    boolean isContent = false;
    if ((text == null) || (type == null))
    {
      throw new IllegalArgumentException (PARAMETER);
    }

    String docContent = null;
    int value = -1;
    final ITextSearcher textsearcher = type.getSearcher ();
    final int pageCount = pdDocument.getNumberOfPages ();
    for (int i = 1; i <= pageCount; i++)
    {
      docContent = this.getContentOnPage (i);
      isContent = textsearcher.isTextContent (text, docContent);
      if (isContent)
      {
        value = i;
      }
    }

    return value;
  }

  /**
   * The method gets the keywords of the PDDocumentInformation of the
   * PDDocument.
   *
   * @return The keywords which are set in the document information.
   */
  public String getKeywords ()
  {
    return pdDocument.getDocumentInformation ().getKeywords ();
  }

  /**
   * The method gets all the pages where the text appears.
   *
   * @param text
   *        The string or regular expression (Constructs a regular expression
   *        matcher from a String by compiling it using a new instance of
   *        RECompiler.) to search for in the document. Be carefull regex are
   *        greedy.
   * @param type
   *        The kind of search. The type is related to the pages of the
   *        document.
   * @return The hole page numbers where the text was found in the document. If
   *         the text was found one time returns -1.
   * @see de.oio.jpdfunit.document.IContent
   * @deprecated As of version 0.93, replaced by
   *             <code>Content.getListOfPagesForContent(String text, TextSearchType type)</code>
   *             .
   */
  @Deprecated
  public int [] getPagesForContent (final String text, final TextSearchType type)
  {
    int [] result;
    boolean isContent = false;
    if ((text == null) || (type == null))
    {
      throw new IllegalArgumentException (PARAMETER);
    }
    String docContent = null;
    final ITextSearcher textsearcher = type.getSearcher ();
    final int pageCount = pdDocument.getNumberOfPages ();
    result = new int [pageCount];
    boolean positive = false;
    for (int i = 1; i <= pageCount; i++)
    {
      docContent = this.getContentOnPage (i);
      isContent = textsearcher.isTextContent (text, docContent);
      if (isContent)
      {
        result[i] = i;
        positive = true;
      }
    }
    if (!positive)
    {
      result = new int [0];
    }
    return result;
  }

  /**
   * The method gets all the pages where the text appears.
   *
   * @param text
   *        The string or regular expression (Constructs a regular expression
   *        matcher from a String by compiling it using a new instance of
   *        RECompiler.) to search for in the document. Be carefull regex are
   *        greedy.
   * @param type
   *        The kind of search. The type is related to the pages of the
   *        document.
   * @return The hole page numbers where the text was found in the document. If
   *         the text was found one time returns -1.
   */
  public List <Integer> getListOfPagesForContent (final String text, final TextSearchType type)
  {
    boolean isContent = false;
    if ((text == null) || (type == null))
    {
      throw new IllegalArgumentException (PARAMETER);
    }
    String docContent = null;
    final ITextSearcher textsearcher = type.getSearcher ();
    final int pageCount = pdDocument.getNumberOfPages ();
    final List <Integer> pages = new ArrayList <Integer> ();
    for (int i = 1; i <= pageCount; i++)
    {
      docContent = this.getContentOnPage (i);
      isContent = textsearcher.isTextContent (text, docContent);
      if (isContent)
      {
        pages.add (new Integer (i));
      }
    }
    return pages;
  }

  /**
   * The method gets the producer of the PDDocumentInformation of the
   * PDDocument.
   *
   * @return The producer which is set in the in information of the document.
   */
  public String getProducer ()
  {
    return pdDocument.getDocumentInformation ().getProducer ();
  }

  /**
   * The method gets the subject of the PDDocumentInformation of the PDDocument.
   *
   * @return The subject of the document which is set in the document
   *         information.
   */
  public String getSubject ()
  {
    return pdDocument.getDocumentInformation ().getSubject ();

  }

  /**
   * The method gets the title of the PDDocumentInformation of the PDDocument.
   *
   * @return The title of the document which is set in the document information.
   */
  public String getTitle ()
  {
    return pdDocument.getDocumentInformation ().getTitle ();
  }

  /**
   * The method gets the version of the COSDocument of the PDDocument.
   *
   * @return The version of the tested pdf file.
   */
  public float getVersion ()
  {
    final COSDocument cosDoc = pdDocument.getDocument ();
    return cosDoc.getVersion ();
  }

  /**
   * The method ckecks if the text is content.
   *
   * @param text
   *        The expected text which should be content of the document, even here
   *        you can use a regular expression .
   * @param type
   *        The kind of search. The type is related to the pages of the
   *        document.
   * @return The method returns true if the text is found the first time.
   */
  public boolean isTextContent (final String text, final TextSearchType type)
  {
    boolean isContent = false;
    if ((text == null) || (type == null))
    {
      throw new IllegalArgumentException (PARAMETER);
    }

    String docContent = null;

    final ITextSearcher textsearcher = type.getSearcher ();
    final int pageCount = pdDocument.getNumberOfPages ();
    boolean returnBool = false;
    for (int i = 1; i <= pageCount; i++)
    {
      docContent = this.getContentOnPage (i);
      isContent = textsearcher.isTextContent (text, docContent);
      if (isContent)
      {
        returnBool = true;
      }
    }

    return returnBool;
  }

  /**
   * The method ckecks if the text is content on a certain page.
   *
   * @param text
   *        The expected text or regular expression which should be content of
   *        the document.
   * @param type
   *        The kind of search. The type is related to the pages of the
   *        document.
   * @param page
   *        The page of the document which should be searched for the text. The
   *        page count is 1-based.
   * @return The method returns true if the text is found on the suggested page.
   */
  public boolean isTextContentOnPage (final String text, final TextSearchType type, final int page)
  {
    boolean isContent = false;
    if ((text == null) || (type == null) || (page == 0) || (page < 0))
    {
      throw new IllegalArgumentException (PARAMETER);
    }

    String docContent = null;
    final ITextSearcher textsearcher = type.getSearcher ();
    docContent = this.getContentOnPage (page);
    isContent = textsearcher.isTextContent (text, docContent);
    return isContent;
  }

  /**
   * The method checks if the PDDocument is encrypted.
   *
   * @return the method returns true if the document is encrypted.
   */
  public boolean isDocumentEncrypted ()
  {
    return pdDocument.isEncrypted ();
  }

  /**
   * The method checks if the supposed password is a password of the PDDocument.
   *
   * @param expected
   *        The expected owner password. The owner password is required for
   *        changing the attributes of the document.
   * @return The method returns true, if the expected password matches to the
   *         owner password of the document.
   */
  public boolean isOwnerPasswd (final String expected)
  {
    if (expected == null)
    {
      throw new IllegalArgumentException (PARAMETER);
    }
    boolean isPasswd = false;
    try
    {
      if (pdDocument.isEncrypted ())
      {
        isPasswd = pdDocument.isOwnerPassword (expected);
      }
    }
    catch (final IOException e)
    {
      throw new IllegalStateException (CANTDECRYPT);
    }
    catch (final CryptographyException e)
    {
      throw new IllegalStateException (CANTDECRYPT);
    }
    return isPasswd;
  }

  /**
   * The method checks if the supposed password is a password of the PDDocument.
   *
   * @param expected
   *        The expected user password. The user password is required for i.e.
   *        reading the document.
   * @return The method returns true, if the expected password matches to the
   *         user password of the document.
   */
  public boolean isUserPasswd (final String expected)
  {
    if (expected == null)
    {
      throw new IllegalArgumentException (PARAMETER);
    }
    boolean isPasswd = false;
    try
    {
      if (pdDocument.isEncrypted ())
      {
        isPasswd = pdDocument.isUserPassword (expected);
      }
    }
    catch (final IOException e)
    {
      throw new IllegalStateException (CANTDECRYPT);
    }
    catch (final CryptographyException e)
    {
      throw new IllegalStateException (CANTDECRYPT);
    }
    return isPasswd;
  }

  /**
   * @return A LinkedList with containing all Fonts of the Document, means the
   *         "names" and the types of a font relative to the page.
   */
  public LinkedList <PdfBoxFontAdapter> getAllFontsInDocument ()
  {
    getDocumentFonts ();
    return fonts;
  }

  private void getDocumentFonts ()
  {
    fonts = new LinkedList <PdfBoxFontAdapter> ();
    PDResources ress;
    LinkedList myFontList = null;
    if (!pdDocument.isEncrypted ())
    {
      for (int page = 0; page < pdDocument.getNumberOfPages (); page++)
      {
        ress = ((PDPage) (pdDocument.getDocumentCatalog ().getAllPages ().get (page))).findResources ();
        myFontList = new LinkedList (ress.getFonts ().values ());
        for (int i = 0; i < myFontList.size (); i++)
        {
          fonts.add (new PdfBoxFontAdapter (((PDFont) (myFontList.get (i))).getBaseFont (),
                                            ((PDFont) (myFontList.get (i))).getSubType (),
                                            page));
        }
      }
    }
  }

  /**
   * This method returns all bookmarks of a pdf document.
   *
   * @return The llst containing the bookmarks.
   */
  public List <String> getAllBookmarks ()
  {
    getBookmarks ();
    return bookMarkList;
  }

  private void getBookmarks ()
  {
    bookMarkList = new ArrayList <String> ();
    final PDDocumentOutline root = pdDocument.getDocumentCatalog ().getDocumentOutline ();
    if (root != null)
    {
      final PDOutlineItem item = root.getFirstChild ();
      rekursionBookmarks (item);
    }
  }

  private void rekursionBookmarks (PDOutlineItem bla)
  {
    while (bla != null)
    {
      bookMarkList.add (bla.getTitle ());
      final PDOutlineItem child = bla.getFirstChild ();
      rekursionBookmarks (child);
      bla = bla.getNextSibling ();
    }
  }
}
