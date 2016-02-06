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
* $Id: DocumentFactoryException.java,v 1.1 2009/12/14 15:58:48 s_schaefer Exp $
*/

package de.oio.jpdfunit.document;

/**
 * The DocumentFactoryException is thrown if a error occures while getting a
 * factory class which is not the default implementation.
 *
 * @author bbratkus
 */
public class DocumentFactoryException extends RuntimeException
{
  public DocumentFactoryException (final String sMsg, final Throwable aCause)
  {
    super (sMsg, aCause);
  }
}
