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
* $Id: PdfBoxDocumentAdapter.java,v 1.1 2009/12/14 15:58:50 s_schaefer Exp $
*/

package de.oio.jpdfunit.document.pdflibimpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import de.oio.jpdfunit.document.IContent;
import de.oio.jpdfunit.document.IDocument;
import de.oio.jpdfunit.document.IFont;
import de.oio.jpdfunit.document.util.PdfImplUtilResourceBundle;

/**
 * This class implements the Document interface and provides the user the
 * methods for getting document relative informations of the pdf document.
 *
 * @author bbratkus
 */
class PdfBoxDocumentAdapter implements IDocument
{

  private final transient PdfBoxAnalyser analyser;

  private final transient IContent content;
  private transient IFont [] docFonts;

  PdfBoxDocumentAdapter (final String file)
  {
    try
    {
      analyser = new PdfBoxAnalyser (file);
      content = new PdfBoxContentAdapter (analyser);
      getAllFonts ();
      getAllBookmarks ();
    }
    catch (final IOException ioe)
    {
      throw new IllegalArgumentException (PdfImplUtilResourceBundle.getString ("PdfBoxDocumentAdapter.inital"));
    }
  }

  PdfBoxDocumentAdapter (final InputStream pdfStream)
  {
    try
    {
      analyser = new PdfBoxAnalyser (pdfStream);
      content = new PdfBoxContentAdapter (analyser);
      getAllFonts ();
      getAllBookmarks ();
    }
    catch (final IOException ioe)
    {
      throw new IllegalArgumentException (PdfImplUtilResourceBundle.getString ("PdfBoxDocumentAdapter.inital"));
    }
  }

  private void getAllFonts ()
  {
    final LinkedList converter = analyser.getAllFontsInDocument ();

    docFonts = new PdfBoxFontAdapter [converter.size ()];
    for (int i = 0; i < docFonts.length; i++)
    {
      docFonts[i] = (PdfBoxFontAdapter) converter.get (i);
    }
  }

  public List <String> getAllBookmarks ()
  {
    return analyser.getAllBookmarks ();
  }

  /** {@inheritDoc} */
  public void closeDocument ()
  {
    analyser.closeDocument ();
  }

  /** {@inheritDoc} */
  public int countPages ()
  {
    return analyser.countPages ();
  }

  /** {@inheritDoc} */
  public String decryptDocument (final String passwd)
  {
    analyser.decryptDocument (passwd);
    return this.content.getContent ();
  }

  /** {@inheritDoc} */
  public String getCreator ()
  {
    return analyser.getCreator ();
  }

  /** {@inheritDoc} */
  public String getAuthor ()
  {
    return analyser.getAuthor ();
  }

  /** {@inheritDoc} */
  public IContent getContent ()
  {
    return this.content;
  }

  /** {@inheritDoc} */
  public Calendar getCreationDate ()
  {
    return analyser.getCreationDate ();
  }

  /** {@inheritDoc} */
  public int getEnryptionLength ()
  {
    return analyser.getEncryptionLength ();
  }

  /** {@inheritDoc} */
  public String getKeywords ()
  {
    return analyser.getKeywords ();
  }

  /** {@inheritDoc} */
  public String getProducer ()
  {
    return analyser.getProducer ();
  }

  /** {@inheritDoc} */
  public String getSubject ()
  {
    return analyser.getSubject ();
  }

  /** {@inheritDoc} */
  public String getTitle ()
  {
    return analyser.getTitle ();
  }

  /** {@inheritDoc} */
  public float getVersion ()
  {
    return analyser.getVersion ();
  }

  /** {@inheritDoc} */
  public boolean isDocumentEncrypted ()
  {
    return analyser.isDocumentEncrypted ();
  }

  /** {@inheritDoc} */
  public boolean isOwnerPasswd (final String passwd)
  {
    return analyser.isOwnerPasswd (passwd);
  }

  /** {@inheritDoc} */
  public boolean isUserPasswd (final String passwd)
  {
    return analyser.isUserPasswd (passwd);
  }

  /** {@inheritDoc} */
  public IFont [] getAllFontsInDocument ()
  {
    final IFont [] returnArray = new IFont [docFonts.length];
    System.arraycopy (docFonts, 0, returnArray, 0, docFonts.length);
    return returnArray;
  }

  /** {@inheritDoc} */
  public IFont [] getAllFontsOnPage (final int page)
  {
    IFont [] pageArray;
    if (analyser.countPages () < page)
    {
      throw new IllegalArgumentException (PdfImplUtilResourceBundle.getString ("PdfBoxDocumentAdapter.parameter"));
    }
    final int startElement = findStartNumberForFonts (page);
    final int size = getNumberOfFonts (page);
    pageArray = new IFont [size];
    System.arraycopy (docFonts, startElement, pageArray, 0, size);
    return pageArray;
  }

  private int findStartNumberForFonts (final int page)
  {
    PdfBoxFontAdapter testingFont;
    int returnValue = -1;
    for (int i = 0; i < docFonts.length; i++)
    {
      testingFont = (PdfBoxFontAdapter) docFonts[i];
      if (testingFont.getPage () + 1 == page)
      {
        returnValue = i;
        i = docFonts.length + 1;
      }
    }
    return returnValue;

  }

  private int getNumberOfFonts (final int page)
  {
    PdfBoxFontAdapter testingFont;
    int fontNumber = 0;
    for (final IFont docFont : docFonts)
    {
      testingFont = (PdfBoxFontAdapter) docFont;
      if (testingFont.getPage () + 1 == page)
      {
        fontNumber++;
      }
      else
        if (testingFont.getPage () + 1 == page + 1)
        {
          return fontNumber;

        }
    }
    return fontNumber;
  }
}
