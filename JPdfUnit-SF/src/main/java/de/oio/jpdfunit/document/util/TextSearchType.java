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
* $Id: TextSearchType.java,v 1.1 2009/12/14 15:58:49 s_schaefer Exp $
*/

package de.oio.jpdfunit.document.util;

/**
 * The basic class which realizes the different TextSearcher classes.
 *
 * @author Benjamin Bratkus
 */
public final class TextSearchType
{
  private final transient String m_sName;
  private final transient ITextSearcher m_aSearcher;

  private TextSearchType (final String name, final ITextSearcher searcher)
  {
    this.m_aSearcher = searcher;
    this.m_sName = name;
  }

  /**
   * The ContainsTextSearcher is used for searching a text within the content of
   * a page of the pdf document.
   */
  public static final TextSearchType CONTAINS = new TextSearchType (PdfImplUtilResourceBundle.getString ("TextSearchType.contains"),
                                                                    new ContainsTextSearcher ());
  /**
   * The WordTextSearcher is used for searching a word means a String in the
   * content of a page of the pdf document. Searchs a string as substring of the
   * content. It uses the default delimiter set, which is " \t\n\r\f": the space
   * character, the tab character, the newline character, the carriage-return
   * character, and the form-feed character. Delimiter characters themselves
   * will not be treated as tokens.
   */
  public static final TextSearchType WORD = new TextSearchType (PdfImplUtilResourceBundle.getString ("TextSearchType.word"),
                                                                new WordTextSearcher ());
  /**
   * The EqualsTextSearcher is used for searching on a page of the pdf document
   * which content is equal to the expected text.
   */
  public static final TextSearchType EQUALS = new TextSearchType (PdfImplUtilResourceBundle.getString ("TextSearchType.equals"),
                                                                  new EqualsTextSearcher ());
  /**
   * The StartsWithTextSearcher is used for searching a text at the beginnig of
   * the content of a page of the pdf document. Searchs a string as substring of
   * the content. It uses the default delimiter set, which is " \t\n\r\f": the
   * space character, the tab character, the newline character, the
   * carriage-return character, and the form-feed character. Delimiter
   * characters themselves will not be treated as tokens.
   */
  public static final TextSearchType STARTSWITH = new TextSearchType (PdfImplUtilResourceBundle.getString ("TextSearchType.startswith"),
                                                                      new StartsWithTextSearcher ());
  /**
   * The EndswithTextSearcher is used for searching a text at the end of the
   * content of a page of the pdf document. Searchs a string as substring of the
   * content. It uses the default delimiter set, which is " \t\n\r\f": the space
   * character, the tab character, the newline character, the carriage-return
   * character, and the form-feed character. Delimiter characters themselves
   * will not be treated as tokens.
   */
  public static final TextSearchType ENDSWITH = new TextSearchType (PdfImplUtilResourceBundle.getString ("TextSearchType.endswith"),
                                                                    new EndsWithTextSearcher ());
  /**
   * The RegexpTextSearcher is used for searching a text within the content of a
   * page of the pdf document.
   */
  public static final TextSearchType REGEXP = new TextSearchType (PdfImplUtilResourceBundle.getString ("TextSearchType.regexp"),
                                                                  new RegexpTextSearcher ());

  /**
   * @return The name of the Textsearchtype.
   */
  @Override
  public String toString ()
  {
    return m_sName;
  }

  /**
   * @return The TextSearcher for searching the text in the content.
   */
  public ITextSearcher getSearcher ()
  {
    return m_aSearcher;
  }

}
