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
* $Id: Content.java,v 1.1 2009/12/14 15:58:48 s_schaefer Exp $
*/

package de.oio.jpdfunit.document;

import java.util.List;

import de.oio.jpdfunit.document.util.TextSearchType;

/**
 * The interface Content provides the user the possibilities to get content
 * relative informations of the pdf document.
 *
 * @author bbratkus
 */
public interface IContent
{

  /**
   * The method returns the content of the document.
   *
   * @return The content of the pdf file.
   */
  String getContent ();

  /**
   * The method returns the content of the document for a certain page.
   *
   * @param page
   *        The page to get the content for. The page count is 1-based.
   * @return The content of the pdf file on the selected page.
   */
  String getContentOnPage (int page);

  /**
   * The method returns the page number for a certain text and type.
   *
   * @param text
   *        The text to search within the document for. Even a regular
   *        expression is a valid parameter.
   * @param type
   *        The TextSearchtype but remember that this is relative to the single
   *        pages of the document.
   * @return The page number for the input parameters.
   */
  int getFirstPageForContent (String text, TextSearchType type);

  /**
   * The method returns the page numbers for a certain text and type.
   *
   * @param text
   *        The text to search within the document for. Even a regular
   *        expression is a valid parameter.
   * @param type
   *        The TextSearchtype but remember that this is relative to the single
   *        pages of the document.
   * @return The page numbers for the input parameters. The page count is
   *         1-based.
   */
  List <Integer> getListOfPagesForContent (String text, TextSearchType type);

  /**
   * The method returns the page numbers for a certain text and type.
   *
   * @param text
   *        The text to search within the document for. Even a regular
   *        expression is a valid parameter.
   * @param type
   *        The TextSearchtype but remember that this is relative to the single
   *        pages of the document.
   * @return The page numbers for the input parameters. The page count is
   *         1-based.
   * @see de.oio.jpdfunit.document.IContent
   * @deprecated As of version 0.93, replaced by
   *             <code>Content.getListOfPagesForContent(String text, TextSearchType type)</code>
   *             .
   */
  @Deprecated
  int [] getPagesForContent (String text, TextSearchType type);

  /**
   * The method returns true for a certain text and type if it is content.
   *
   * @param text
   *        The text to search within the document for. Even a regular
   *        expression is a valid parameter.
   * @param type
   *        The TextSearchtype but remember that this is relative to the single
   *        pages of the document.
   * @return Returns true if the parameters matches.
   */
  boolean isTextContent (String text, TextSearchType type);

  /**
   * The method returns true for a certain text, type and if it is content.
   *
   * @param text
   *        The text to search within the document for. Even a regular
   *        expression is a valid parameter.
   * @param type
   *        The TextSearchtype but remember that this is relative to the single
   *        pages of the document.
   * @param page
   *        The supposed page for the text. The page count is 1-based.
   * @return Returns true if the parameters matches.
   */
  boolean isTextContentOnPage (String text, TextSearchType type, int page);

}
