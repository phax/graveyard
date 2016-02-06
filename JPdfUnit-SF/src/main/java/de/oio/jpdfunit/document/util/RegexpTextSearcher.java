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
* $Id: RegexpTextSearcher.java,v 1.1 2009/12/14 15:58:49 s_schaefer Exp $
*/

package de.oio.jpdfunit.document.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The RegexpTextSearcher is used for searching a text within the content of a
 * page of the pdf document.
 *
 * @author bbratkus
 */
class RegexpTextSearcher implements ITextSearcher
{

  /** {@inheritDoc} */
  public boolean isTextContent (final String text, final String content)
  {
    final Pattern expression = Pattern.compile (text);
    final Matcher m = expression.matcher (content);
    if (m.find ())
    {
      return true;
    }
    return false;
  }

}
