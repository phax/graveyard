/**
 * Copyright [2011] [Prasad Balan]
 * Copyright (C) 2016 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
   Copyright [2011] [Prasad Balan]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.pb.x12;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * The class represents methods used to translate a X12 transaction represented
 * as a file or string into an X12 object.
 *
 * @author Prasad Balan
 */
public class X12Parser implements IX12Parser
{
  private static final int SIZE = 106;
  public static final int POS_SEGMENT = 105;
  public static final int POS_ELEMENT = 3;
  public static final int POS_COMPOSITE_ELEMENT = 104;

  private final Cf m_aX12Cf;
  private Cf m_aCfMarker;
  private Loop m_aLoopMarker;

  public X12Parser (final Cf cf)
  {
    m_aX12Cf = cf;
  }

  /**
   * The method takes a X12 file and converts it into a X2 object. The X12 class
   * has methods to convert it into XML format as well as methods to modify the
   * contents.
   *
   * @param fileName
   *        a X12 file
   * @return the X12 object
   * @throws EDIFormatException
   *         In case of error
   * @throws IOException
   *         In case of error
   */
  public IEDI parse (final File fileName) throws EDIFormatException, IOException
  {
    final char [] buffer = new char [SIZE];
    try (final FileReader fr = new FileReader (fileName))
    {
      final int count = fr.read (buffer);
      if (count != SIZE)
        throw new EDIFormatException ();
    }
    final Context context = new Context ();
    context.setSegmentSeparator (buffer[POS_SEGMENT]);
    context.setElementSeparator (buffer[POS_ELEMENT]);
    context.setCompositeElementSeparator (buffer[POS_COMPOSITE_ELEMENT]);

    try (final Scanner scanner = new Scanner (fileName))
    {
      return scanSource (scanner, context);
    }
  }

  /**
   * private helper method
   *
   * @param scanner
   * @param context
   * @return
   */
  private X12 scanSource (final Scanner scanner, final Context context)
  {
    final char segmentSeparator = context.getSegmentSeparator ();
    final String quotedSegmentSeparator = Pattern.quote (Character.toString (segmentSeparator));

    scanner.useDelimiter (quotedSegmentSeparator + "\r\n|" + quotedSegmentSeparator + "\n|" + quotedSegmentSeparator);

    m_aCfMarker = m_aX12Cf;
    final X12 x12 = new X12 (context);
    m_aLoopMarker = x12;
    Loop loop = x12;

    while (scanner.hasNext ())
    {
      final String line = scanner.next ();
      final String [] tokens = line.split ("\\" + context.getElementSeparator ());
      if (doesChildLoopMatch (m_aCfMarker, tokens))
      {
        loop = loop.addChild (m_aCfMarker.getName ());
        loop.addSegment (line);
      }
      else
        if (doesParentLoopMatch (m_aCfMarker, tokens, loop))
        {
          loop = m_aLoopMarker.addChild (m_aCfMarker.getName ());
          loop.addSegment (line);
        }
        else
        {
          loop.addSegment (line);
        }
    }
    return x12;
  }

  /**
   * The method takes a InputStream and converts it into a X12 object. The X12
   * class has methods to convert it into XML format as well as methods to
   * modify the contents.
   *
   * @param source
   *        InputStream
   * @return the X12 object
   * @throws EDIFormatException
   *         In case of error
   * @throws IOException
   *         In case of error
   */
  public IEDI parse (final InputStream source) throws EDIFormatException, IOException
  {
    final StringBuilder strBuffer = new StringBuilder ();
    final char [] cbuf = new char [1024];
    int length = -1;

    final Reader reader = new BufferedReader (new InputStreamReader (source));

    while ((length = reader.read (cbuf)) != -1)
    {
      strBuffer.append (cbuf, 0, length);
    }

    final String strSource = strBuffer.toString ();
    return parse (strSource);
  }

  /**
   * The method takes a X12 string and converts it into a X2 object. The X12
   * class has methods to convert it into XML format as well as methods to
   * modify the contents.
   *
   * @param source
   *        String
   * @return the X12 object
   * @throws EDIFormatException
   *         In case of error
   */
  public IEDI parse (final String source) throws EDIFormatException
  {
    if (source.length () < SIZE)
    {
      throw new EDIFormatException ();
    }
    final Context context = new Context ();
    context.setSegmentSeparator (source.charAt (POS_SEGMENT));
    context.setElementSeparator (source.charAt (POS_ELEMENT));
    context.setCompositeElementSeparator (source.charAt (POS_COMPOSITE_ELEMENT));

    try (final Scanner scanner = new Scanner (source))
    {
      return scanSource (scanner, context);
    }
  }

  /**
   * Checks if the segment (or line read) matches to current loop
   *
   * @param cf
   *        Cf
   * @param tokens
   *        String[] represents the segment broken into elements
   * @return boolean
   */
  private boolean doesLoopMatch (final Cf cf, final String [] tokens)
  {
    if (cf.getSegment ().equals (tokens[0]))
    {
      if (cf.getSegmentQualPos () < 0)
        return true;
      for (final String qual : cf.getSegmentQuals ())
        if (qual.equals (tokens[cf.getSegmentQualPos ()]))
          return true;
    }
    return false;
  }

  /**
   * Checks if the segment (or line read) matches to any of the child loops
   * configuration.
   *
   * @param cf
   *        Cf
   * @param tokens
   *        String[] represents the segment broken into elements
   * @return boolean
   */
  boolean doesChildLoopMatch (final Cf parent, final String [] tokens)
  {
    for (final Cf cf : parent.getChildrenIter ())
      if (doesLoopMatch (cf, tokens))
      {
        m_aCfMarker = cf;
        return true;
      }
    return false;
  }

  /**
   * Checks if the segment (or line read) matches the parent loop configuration.
   *
   * @param cf
   *        Cf
   * @param tokens
   *        String[] represents the segment broken into elements
   * @param loop
   *        Loop
   * @return boolean
   */
  private boolean doesParentLoopMatch (final Cf child, final String [] tokens, final Loop loop)
  {
    final Cf parent = child.getParent ();
    if (parent == null)
      return false;

    m_aLoopMarker = loop.getParent ();
    for (final Cf cf : parent.getChildrenIter ())
      if (doesLoopMatch (cf, tokens))
      {
        m_aCfMarker = cf;
        return true;
      }

    if (doesParentLoopMatch (parent, tokens, m_aLoopMarker))
      return true;

    return false;
  }
}
