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
* $Id: FontAssertion.java,v 1.1 2009/12/14 15:58:49 s_schaefer Exp $
*/

package de.oio.jpdfunit;

/**
 * @author bbratkus TODO To change the template for this generated type comment
 *         go to Window - Preferences - Java - Code Style - Code Templates
 */
public interface FontAssertion
{
  /**
   * @param expected
   *        A expected font name like "Verdana-BoldItalic".
   */

  void assertFontNameInDocumentEquals (String expected);

  /**
   * @param expected
   *        Expected font names like "Verdana-BoldItalic".
   */
  void assertAllFontNamesInDocument (String [] expected);

  /**
   * @param expected
   *        A expected font name like "Verdana-BoldItalic".
   * @param page
   *        The supposed page number, notice it is 1-based.
   */
  void assertFontNameOnPage (String expected, int page);

  /**
   * @param expected
   *        Expected font names like "Verdana-BoldItalic".
   * @param page
   *        The supposed page number, notice it is 1-based.
   */
  void assertAllFontNamesOnPage (String [] expected, int page);

  /**
   * @param expected
   *        A supposed font type, i.e. "TrueType" or "Type1".
   */
  void assertFontTypeInDocument (String expected);

  /**
   * @param expected
   *        All supposed font types in the document. Be carefull and list all of
   *        them. A supposed font type is i.e. "TrueType" or "Type1".
   */
  void assertAllFontTypesInDocument (String [] expected);

  /**
   * @param expected
   *        A supposed font type, i.e "TrueType" or "Type1".
   * @param page
   *        The supposed page to search for the font type.
   */
  void assertFontTypeOnPage (String expected, int page);

  /**
   * @param expected
   *        All supposed font types in the document. Be carefull and list all of
   *        them. A supposed font type is i.e. "TrueType" or "Type1".
   * @param page
   *        The supposed page to search for the font types.
   */
  void assertAllFontTypesOnPage (String [] expected, int page);

}
