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
* $Id: PdfBoxFontAdapter.java,v 1.1 2009/12/14 15:58:50 s_schaefer Exp $
*/

package de.oio.jpdfunit.document.pdflibimpl;

import de.oio.jpdfunit.document.IFont;

/**
 * A real simple class for representing the fonts of a document. This fonts
 * contains three attributes. One is the font name and another the font type and
 * the page where this font appears.
 *
 * @author bbratkus
 */
class PdfBoxFontAdapter implements IFont
{
  private final transient String m_sName;
  private final transient String m_sType;
  private final transient int m_nPage;

  /**
   * The default constructor using the three parameters name, type and page.
   *
   * @param name
   *        The name of this font.
   * @param type
   *        The type of this font.
   * @param page
   *        The page number where this font appears.
   */
  PdfBoxFontAdapter (final String name, final String type, final int page)
  {
    m_sName = name;
    m_sType = type;
    m_nPage = page;
  }

  /** {@inheritDoc} */
  public String getFontType ()
  {
    return m_sType;
  }

  /** {@inheritDoc} */
  public String getFontName ()
  {
    return m_sName;
  }

  /** {@inheritDoc} */
  public int getPage ()
  {
    return m_nPage;
  }
}
