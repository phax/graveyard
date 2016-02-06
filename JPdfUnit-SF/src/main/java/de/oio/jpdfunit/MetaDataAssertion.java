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
* $Id: MetaDataAssertion.java,v 1.1 2009/12/14 15:58:49 s_schaefer Exp $
*/

package de.oio.jpdfunit;

import java.util.Calendar;

/**
 * This interface provides the user many methods for accessing the meta data of
 * a pdf document such as the author or the creation date. Even some general
 * informtions can be accessed via this interface.
 *
 * @author bbratkus
 */
public interface MetaDataAssertion
{

  /**
   * The method compares the supposed authors name to the authors name of the
   * pdf document.
   *
   * @param expected
   *        The supposed authors name.
   */
  void assertAuthorsNameEquals (String expected);

  /**
   * The method compares the supposed creator to the creator of the document.
   *
   * @param expected
   *        The supposed creator of the document.
   */
  void assertCreatorEquals (String expected);

  /**
   * The method comapares the supposed creation date to the creation date of the
   * document.
   *
   * @param expected
   *        The supposed creation date of the document
   */
  void assertCreationDateEquals (Calendar expected);

  /**
   * The method compares the supposed owner password with the owner password if
   * it exists of the document and trys to decrypt the document.
   *
   * @param expected
   *        The supposed owner password. Used for changing properties of a pdf
   *        file like printing or editing permissions.
   */
  void assertDecryptionWithOwnerPasswd (String expected);

  /**
   * The method compares the supposed user password with the user password if it
   * exists of the document and trys to decrypt the document.
   *
   * @param expected
   *        The supposed user password. Usually used for reading the pdf file.
   */
  void assertDecryptionWithUserPasswd (String expected);

  /**
   * The method comapares the supposed document title of the document
   * informations with the document title of the document.
   *
   * @param expected
   *        The supposed title of the pdf file
   */
  void assertDocumentTitleEquals (String expected);

  /**
   * The method compares the supposed encryption length with the encryption
   * length of the document if it is encrypted else it returns 0.
   *
   * @param expected
   *        The supposed encryption length of the pdf file.
   */
  void assertEncryptionLenghthEquals (int expected);

  /**
   * The method compares the supposed keywords to the keywords of the document
   * informations of the document.
   *
   * @param expected
   *        The supposed keywords of the pdf file.
   */
  void assertKeywordsEquals (String expected);

  /**
   * The method compares the supposed page number to the page number of the
   * document.
   *
   * @param expected
   *        The supposed page count of the pdf file. The page count is 1-based.
   */
  void assertPageCountEquals (int expected);

  /**
   * The method compares the supposed subject to the subject of the document.
   *
   * @param expected
   *        The supposed subject of the pdf file.
   */
  void assertSubjectEquals (String expected);

  /**
   * The method compares the supposed version to the version of the document.
   *
   * @param expected
   *        The supposed version of the pdf file.
   */
  void assertVersionEquals (float expected);

  /**
   * The method checks if the current document is encrypted and comapares it to
   * the parameter.
   *
   * @param expected
   *        The expected boolean. It has to be set to "true" if you suggest that
   *        the pdf file is encrypted.
   */
  void assertIsDocumentEncrypted (boolean expected);

  /**
   * The method compares the supposed owner password with the owner password if
   * it exists of the document.
   *
   * @param expected
   *        The supposed ownerpassword.
   */
  void assertIsOwnerPasswd (String expected);

  /**
   * The method compares the supposed user password with the user password if it
   * exists of the document.
   *
   * @param expected
   *        The supposed user password.
   */
  void assertIsUserPasswd (String expected);

}
