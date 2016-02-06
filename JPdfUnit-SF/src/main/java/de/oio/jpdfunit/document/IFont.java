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
* $Id: Font.java,v 1.2 2011/12/09 14:03:49 s_schaefer Exp $
*/

package de.oio.jpdfunit.document;

/**
 * The interface for accessing the fonts of the document.
 *
 * @author bbratkus
 */
public interface IFont
{
  /**
   * For a font subset, the PostScript name of the font the value of the font's
   * BaseFont entry and the font descriptor's FontName entry begins with a tag
   * followed by a plus sign (+). The tag consists of exactly six uppercase
   * letters; the choice of letters is arbitrary, but different subsets in the
   * same PDF file must have different tags. For example, EOODIA+Poetica is the
   * name of a subset of Poetica, a Type 1 font. (See implementation note 62 in
   * Appendix H.)
   *
   * @return The name of the font.
   */
  String getFontName ();

  /**
   * Fonts are seperated in different font types. The methods returns the type
   * of this font.
   *
   * @return The type of this font. I.e. the TrueType or the Type0
   */
  String getFontType ();

  /**
   * For getting the page of a font you can use this method. Caution it is
   * 1-based.
   *
   * @return The page number the font appears.
   */
  int getPage ();
}
