/*
 * JPdfUnit- Make your PFD green
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
 * $Id: PdfImplUtilResourceBundle.java,v 1.1 2009/12/14 15:58:48 s_schaefer Exp $
 */

package de.oio.jpdfunit.document.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * The class StringResourceBundle is for externalizing the Strings of the
 * project.
 *
 * @author bbratkus
 */
public final class PdfImplUtilResourceBundle
{
  private static final String BUNDLE = "de.oio.jpdfunit.document.util.PdfImplUtil"; //$NON-NLS-1$

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle (BUNDLE);

  private PdfImplUtilResourceBundle ()
  {}

  /**
   * @param key
   *        The key for the expected value of the StringResourceBundle.
   * @return The value of the key.
   */
  public static String getString (final String key)
  {
    try
    {
      return RESOURCE_BUNDLE.getString (key);
    }
    catch (final MissingResourceException e)
    {
      return '!' + key + '!';
    }
  }
}
