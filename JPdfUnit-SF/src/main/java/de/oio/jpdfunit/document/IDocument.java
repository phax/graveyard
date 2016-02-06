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
* $Id: Document.java,v 1.1 2009/12/14 15:58:48 s_schaefer Exp $
*/

package de.oio.jpdfunit.document;

import java.util.Calendar;
import java.util.List;

/**
 * The Document interface provides a lot of methods to get basic information
 * about the document.
 *
 * @author bbratkus
 */
public interface IDocument
{
  /**
   * Please close the file after finishing your tests.
   */
  void closeDocument ();

  /**
   * The method gets the page count of the document.
   *
   * @return the number of pages it is 1-based.
   */
  int countPages ();

  /**
   * The method trys to decrypt the document with a given password and returns
   * the content if it is possible.
   *
   * @param passwd
   *        The owner or the user password of the pdf file to decrypt the
   *        document.
   * @return The hole content of the document.
   */
  String decryptDocument (String passwd);

  /**
   * The method gets the cretor of the document if it was set in the document
   * information.
   *
   * @return The creator of the document.
   */
  String getCreator ();

  /**
   * The method gets the author of the document if it was set in the document
   * information.
   *
   * @return The author of the document a document information.
   */
  String getAuthor ();

  /**
   * The method gets the content of the document if it is not decrypted.
   *
   * @return The Content of the document a document.
   */
  IContent getContent ();

  /**
   * The method gets the creation date of the document but be carefull it is a
   * very specific Calendari.e. "GregorianCalendar testDate = new
   * GregorianCalendar(2005, Calendar.JUNE, 29, 16, 33, 02);"
   *
   * @return The creation date of the document a document information.
   */
  Calendar getCreationDate ();

  /**
   * The method gets the encryption length of the document if it is encrypted.
   *
   * @return If the document is encrypted the method returns the encryption
   *         length else 0.
   */
  int getEnryptionLength ();

  /**
   * The method gets the keywords of a document if they are set in the document
   * information.
   *
   * @return The keywords of the document which were set in the document
   *         information.
   */
  String getKeywords ();

  /**
   * The method gets the producer of the document if this is set in the document
   * information.
   *
   * @return The producer of the document which was set in the document
   *         information.
   */
  String getProducer ();

  /**
   * The method gets the subject of the document if it is set in the document
   * information.
   *
   * @return The subject of the document which can be found in the document
   *         information.
   */
  String getSubject ();

  /**
   * The method gets the title of the document if it is set in the document
   * information.
   *
   * @return The title of the document which is set in the document information.
   */
  String getTitle ();

  /**
   * The method gets the version of the document if it is set in the document
   * information.
   *
   * @return The version of the document.
   */
  float getVersion ();

  /**
   * The method checks if the document is encrypted.
   *
   * @return Returns true if the document is encrypted.
   */
  boolean isDocumentEncrypted ();

  /**
   * The method checks if the supposed owner password is the owner password of
   * the document.
   *
   * @param passwd
   *        The supposed owner password.
   * @return Returns true if the parameter is the owner password of the
   *         document.
   */
  boolean isOwnerPasswd (String passwd);

  /**
   * The method checks if the supposed user password is the user password of the
   * document.
   *
   * @param passwd
   *        The supposed user password.
   * @return Returns true if the parameter is the user password of the document.
   */
  boolean isUserPasswd (String passwd);

  /**
   * For testing the different fonts of a document you can get the hole fonts as
   * an array.
   *
   * @return Returns a font array, which contains all the fonts of the document.
   *         This fonts are specified by their type and font name.
   */
  IFont [] getAllFontsInDocument ();

  /**
   * For testing the different fonts of a document you can get the fonts for a
   * selected page by using this method. Be carefull it is 1-based.
   *
   * @param page
   *        The supposed page to get the fonts from.
   * @return Returns a font array, which contains all the fonts of the page.
   *         This fonts are specified by their type and font name.
   */
  IFont [] getAllFontsOnPage (int page);

  /**
   * This method returns all the bookmarks of the testing document.
   *
   * @return The list containing the bookmarks.
   */
  List <String> getAllBookmarks ();
}
