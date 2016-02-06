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
* $Id: WordTextSearcher.java,v 1.1 2009/12/14 15:58:49 s_schaefer Exp $
*/

package de.oio.jpdfunit.document.util;

import java.util.StringTokenizer;

/**
 * The WordTextSearcher is used for searching a word means a String in the
 * content of a page of the pdf document. Searchs a string as substring of the
 * content. It uses the default delimiter set, which is " \t\n\r\f": the space
 * character, the tab character, the newline character, the carriage-return
 * character, and the form-feed character. Delimiter characters themselves will
 * not be treated as tokens.
 *
 * @author bbratkus
 */
class WordTextSearcher implements ITextSearcher
{

  private transient StringTokenizer tokenizer;

  /** {@inheritDoc} */
  public boolean isTextContent (final String text, final String content)
  {
    boolean returnBool = false;
    tokenizer = new StringTokenizer (content);
    while (tokenizer.hasMoreTokens ())
    {
      if (text.equals (tokenizer.nextToken ()))
      {
        returnBool = true;
      }
    }
    return returnBool;
  }

}
