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
* $Id: DocumentDataSource.java,v 1.1 2009/12/14 15:58:49 s_schaefer Exp $
*/

package de.oio.jpdfunit.document.util;

import java.io.InputStream;

/**
 * The Interface provides the general functionality to access the different
 * DataSources.
 *
 * @author bbratkus
 */
public interface IDocumentDataSource
{

  /**
   * @return Returns the datasource type.
   */
  int getDatasource ();

  /**
   * @return Returns the Stream.
   */
  InputStream getStream ();

  /**
   * @return Returns the file.
   */
  String getFile ();

}
