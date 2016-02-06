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
* $Id: PdfBoxFactory.java,v 1.1 2009/12/14 15:58:50 s_schaefer Exp $
*/

package de.oio.jpdfunit.document.pdflibimpl;

import java.io.InputStream;

import de.oio.jpdfunit.document.IDocument;
import de.oio.jpdfunit.document.AbstractDocumentFactory;
import de.oio.jpdfunit.document.util.PdfImplUtilResourceBundle;

/**
 * The PdfBoxFactory is the default factory class which is used for getting a
 * Document and the matching content.
 *
 * @author bbratkus
 */
public final class PdfBoxFactory extends AbstractDocumentFactory
{

  private PdfBoxFactory ()
  {}

  /**
   * @return Returns an instance of the PdfBoxFactory.
   */
  public static AbstractDocumentFactory newInstance ()
  {
    return new PdfBoxFactory ();
  }

  /** {@inheritDoc} */
  @Override
  public IDocument getDocument (final String pdfFile)
  {

    if ((pdfFile != null) && (pdfFile != ""))
    {
      return new PdfBoxDocumentAdapter (pdfFile);
    }
    throw new IllegalArgumentException (PdfImplUtilResourceBundle.getString ("PdfBoxFactory.unsupported")); //$NON-NLS-1$

  }

  /** {@inheritDoc} */
  @Override
  public IDocument getDocument (final InputStream pdfStream)
  {

    if (pdfStream != null)
    {
      return new PdfBoxDocumentAdapter (pdfStream);
    }
    throw new IllegalArgumentException (PdfImplUtilResourceBundle.getString ("PdfBoxFactory.unsupported")); //$NON-NLS-1$

  }
}
