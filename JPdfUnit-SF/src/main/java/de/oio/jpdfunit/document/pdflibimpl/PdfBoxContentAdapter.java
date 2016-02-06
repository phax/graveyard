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
* $Id: PdfBoxContentAdapter.java,v 1.1 2009/12/14 15:58:50 s_schaefer Exp $
*/

package de.oio.jpdfunit.document.pdflibimpl;

import java.util.List;

import de.oio.jpdfunit.document.IContent;
import de.oio.jpdfunit.document.util.TextSearchType;

/**
 * This class implements the Content interface and provides the user the methods
 * for getting content relative informations of the pdf document.
 *
 * @author bbratkus
 */
class PdfBoxContentAdapter implements IContent
{

  private final transient PdfBoxAnalyser analyser;

  PdfBoxContentAdapter (final PdfBoxAnalyser initAnalyser)
  {
    analyser = initAnalyser;
  }

  /** {@inheritDoc} */
  public String getContent ()
  {
    return analyser.getContent ();
  }

  /** {@inheritDoc} */
  public String getContentOnPage (final int page)
  {
    return analyser.getContentOnPage (page);
  }

  /** {@inheritDoc} */
  public int getFirstPageForContent (final String text, final TextSearchType type)
  {
    return analyser.getFirstPageForContent (text, type);
  }

  /** {@inheritDoc} */
  public List <Integer> getListOfPagesForContent (final String text, final TextSearchType type)
  {
    return analyser.getListOfPagesForContent (text, type);
  }

  /** {@inheritDoc} */
  public int [] getPagesForContent (final String text, final TextSearchType type)
  {
    return analyser.getPagesForContent (text, type);
  }

  /** {@inheritDoc} */
  public boolean isTextContent (final String text, final TextSearchType type)
  {
    return analyser.isTextContent (text, type);
  }

  /** {@inheritDoc} */
  public boolean isTextContentOnPage (final String text, final TextSearchType type, final int page)
  {
    return analyser.isTextContentOnPage (text, type, page);
  }

}
