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
* $Id: TextSearcher.java,v 1.1 2009/12/14 15:58:49 s_schaefer Exp $
*/

package de.oio.jpdfunit.document.util;

/**
 * The interface TextSearcher provides the methods for the different
 * TextSearcher.
 *
 * @author bbratkus
 */
public interface ITextSearcher
{
  /**
   * The method returns true if the strint was found in the content.
   *
   * @param text
   *        The text to search for in the content.
   * @param content
   *        The content to look for the text.
   * @return Returns true if the text was found in the content.
   */
  boolean isTextContent (String text, String content);

}
