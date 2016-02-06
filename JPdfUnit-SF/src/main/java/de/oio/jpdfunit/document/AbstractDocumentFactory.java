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
* $Id: DocumentFactory.java,v 1.1 2009/12/14 15:58:48 s_schaefer Exp $
*/

package de.oio.jpdfunit.document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import de.oio.jpdfunit.document.pdflibimpl.PdfBoxFactory;
import de.oio.jpdfunit.document.util.PdfImplUtilResourceBundle;

/**
 * Defines a factory API that enables the application to configure own factories
 * and to parse pdf documents.
 *
 * @author bbratkus
 */
public abstract class AbstractDocumentFactory
{
  private static final String NOINSTANCE = PdfImplUtilResourceBundle.getString ("DocumentFactory.noInstance");

  private static final int MAGICTRHREE = 3;

  /**
   * For working with a Document first you have to get the Document. In this
   * method a String ist used to get the document.
   *
   * @param pdfFile
   *        The document i.e.
   *        "etc/testing-pdfs/oio-katalog-mit-logo-mit-farben.pdf"
   * @return The document created via the pdfFile.
   */
  public abstract IDocument getDocument (String pdfFile);

  /**
   * For working with a Document first you have to get the Document. In this
   * method a InputStream ist used to get the document.
   *
   * @param pdfStream
   *        A InputStream which contains the document.
   * @return The document created via the stream.
   */
  public abstract IDocument getDocument (InputStream pdfStream);

  /**
   * This factory method works like the SaxParserFactory. Creates a new factory
   * instance. The implementation class to load is the first found in the
   * following locations:
   * <ol>
   * <li>the <code>de.oio.document.DocumentFactory</code> system property</li>
   * <li>the above named property value in the
   * <code><i>$JAVA_HOME</i>/lib/DocumentFactory.properties</code> file</li>
   * <li>the class name specified in the
   * <code>META-INF/services/de.oio.document.DocumentFactory</code> system
   * resource</li>
   * <li>the default factory class</li>
   * </ol>
   *
   * @return A concrete DocumentFactory by default the PdfBoxFactory.
   * @throws DocumentFactoryException
   *         The exception will be thrown if the instantation fails.
   */
  public static AbstractDocumentFactory newInstance ()
  {
    ClassLoader loader = Thread.currentThread ().getContextClassLoader ();
    if (loader == null)
    {
      loader = IDocument.class.getClassLoader ();
    }
    String className = null;
    int count = 0;
    do
    {
      className = getDocumentClassName (loader, count++);
      if (className != null)
      {
        try
        {
          final Class <?> t = (loader != null) ? loader.loadClass (className) : Class.forName (className);
          return (AbstractDocumentFactory) t.newInstance ();
        }
        catch (final ClassNotFoundException e)
        {
          throw new DocumentFactoryException (NOINSTANCE + " " + className, e);
        }
        catch (final Exception e)
        {
          throw new DocumentFactoryException (NOINSTANCE + " " + className, e);
        }
      }
    } while (count < MAGICTRHREE);

    return PdfBoxFactory.newInstance ();

  }

  private static String getDocumentClassName (final ClassLoader loader, final int attempt)
  {
    final String propertyName = PdfImplUtilResourceBundle.getString ("DocumentFactory.propertyName");
    switch (attempt)
    { // NOPMD
      case 0:
        return System.getProperty (propertyName);
      case 1:
        try
        {
          File file = new File (System.getProperty (PdfImplUtilResourceBundle.getString ("DocumentFactory.javaHome")));
          file = new File (file, PdfImplUtilResourceBundle.getString ("DocumentFactory.lib"));
          file = new File (file, PdfImplUtilResourceBundle.getString ("DocumentFactory.DocFacProp"));
          final InputStream in = new FileInputStream (file);
          final Properties props = new Properties ();
          props.load (in);
          in.close ();
          return props.getProperty (propertyName);
        }
        catch (final IOException e)
        {
          return null;
        }
      case 2:
        try
        {
          final String serviceKey = PdfImplUtilResourceBundle.getString ("DocumentFactory.meta") + propertyName;
          final InputStream in = (loader != null) ? loader.getResourceAsStream (serviceKey)
                                                  : IDocument.class.getResourceAsStream (serviceKey);
          if (in != null)
          {
            final BufferedReader r = new BufferedReader (new InputStreamReader (in));
            final String ret = r.readLine ();
            r.close ();
            return ret;
          }
        }
        catch (final IOException e)
        {
          throw new IllegalArgumentException (PdfImplUtilResourceBundle.getString ("DocumentFactory.noStream"));
        }
        return null;
      default:
        return null;
    }
  }

}
