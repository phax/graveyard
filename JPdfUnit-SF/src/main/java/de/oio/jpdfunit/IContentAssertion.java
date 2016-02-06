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
* $Id: ContentAssertion.java,v 1.1 2009/12/14 15:58:49 s_schaefer Exp $
*/

package de.oio.jpdfunit;

import de.oio.jpdfunit.document.util.TextSearchType;

/**
 * This interface provides the user the different content assertions.
 *
 * @author bbratkus
 */
public interface IContentAssertion
{
  /**
   * This assert method checks if the supposed parameters are contained in the
   * pdf document. The method matches the first time the text was found in the
   * pdf document.
   *
   * @param text
   *        The string or regular expression (constructs a regular expression
   *        matcher from a String by compiling it using a new instance of
   *        RECompiler) to search for in the document. Be carefull regex are
   *        greedy.
   * @param type
   *        The kind of search. The type is related to the pages of the
   *        document.
   */
  void assertContentContainsText (String text, TextSearchType type);

  /**
   * This assert method checks if the supposed parameters are contained in the
   * document. The method trys to match all the time the text was found in the
   * document.
   *
   * @param text
   *        The string or regular expression to search for in the document. Be
   *        carefull regex are greedy.
   * @param type
   *        The kind of search. The type is related to the pages of the
   *        document.
   */
  void assertContentContainsTextMultipleTimes (String text, TextSearchType type);

  /**
   * This assert method checks if the supposed parameters are contained in the
   * document. The method trys to match the text on a certain page in the
   * document.
   *
   * @param text
   *        The string or regular expression to search for in the document. Be
   *        carefull regex are greedy.
   * @param page
   *        The page of the document which should be passed trough. The page
   *        count equals to a real document its 1-based.
   * @param type
   *        The kind of search. The type is related to the pages of the
   *        document.
   */
  void assertContentContainsTextOnPage (String text, int page, TextSearchType type);

  /**
   * This assert method checks if the supposed parameters are not contained in
   * the pdf document. The method matches the first time the text was found in
   * the pdf document.
   *
   * @param text
   *        The string or regular expression (constructs a regular expression
   *        matcher from a String by compiling it using a new instance of
   *        RECompiler) to search for in the document. Be carefull regex are
   *        greedy.
   * @param type
   *        The kind of search. The type is related to the pages of the
   *        document.
   */
  void assertContentDoesNotContainText (String text, TextSearchType type);

  /**
   * This assert method checks if the supposed parameters are contained in the
   * document. The method trys to match all the time the text was found in the
   * document.
   *
   * @param text
   *        The string or regular expression to search for in the document. Be
   *        carefull regex are greedy.
   * @param type
   *        The kind of search. The type is related to the pages of the
   *        document.
   */
  void assertContentDoesNotContainTextMultipleTimes (String text, TextSearchType type);

  /**
   * This assert method checks if the supposed parameters are not contained in
   * the document. The method trys to match the text on a certain page in the
   * document.
   *
   * @param text
   *        The string or regular expression to search for in the document. Be
   *        carefull regex are greedy.
   * @param page
   *        The page of the document which should be passed trough. The page
   *        count equals to a real document its 1-based.
   * @param type
   *        The kind of search. The type is related to the pages of the
   *        document.
   */
  void assertContentDoesNotContainTextOnPage (String text, int page, TextSearchType type);
}
