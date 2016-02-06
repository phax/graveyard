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
* $Id: EqualsTextSearcher.java,v 1.1 2009/12/14 15:58:49 s_schaefer Exp $
*/

package de.oio.jpdfunit.document.util;

/**
 * The EqualsTextSearcher is used for searching on a page of the pdf document
 * which content is equal to the expected text.
 *
 * @author bbratkus
 */
class EqualsTextSearcher implements ITextSearcher
{

  /** {@inheritDoc} */
  public boolean isTextContent (final String text, final String content)
  {
    if (text.equals (content))
    {
      return true;
    }
    return false;
  }

}
