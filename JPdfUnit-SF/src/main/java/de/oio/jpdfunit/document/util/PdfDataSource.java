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
* $Id: PdfDataSource.java,v 1.1 2009/12/14 15:58:49 s_schaefer Exp $
*/

package de.oio.jpdfunit.document.util;

import java.io.InputStream;

/**
 * The pdf data source is for the choice of the datasource. Therefore there are
 * two elemental sources available on the on hand a String with the path to the
 * pdf document including the document i.e.
 * "etc/testing-pdfs/DocumentInformationTest.pdf" and on the other side a stream
 * which contains the pdf document.
 *
 * @author bbratkus
 */
public final class PdfDataSource implements IDocumentDataSource
{
  private transient String m_sFilename;
  private transient InputStream m_aPDFIS;
  private final transient int m_nDataSource;

  /**
   * @param pdf
   *        The pdf document i.e. "etc/testing-pdfs/DocumentInformationTest.pdf"
   */
  public PdfDataSource (final String pdf)
  {
    this.m_sFilename = pdf;
    m_nDataSource = 0;
  }

  /**
   * @param pdfStream
   *        The pdf file as stream
   */
  public PdfDataSource (final InputStream pdfStream)
  {
    this.m_aPDFIS = pdfStream;
    m_nDataSource = 1;
  }

  /**
   * @return Returns the file.
   */
  public String getFile ()
  {
    return m_sFilename;
  }

  /**
   * @return Returns the pdfStream.
   */
  public InputStream getStream ()
  {
    return m_aPDFIS;
  }

  /**
   * @return Returns the datasource type.
   */
  public int getDatasource ()
  {
    return m_nDataSource;
  }
}
