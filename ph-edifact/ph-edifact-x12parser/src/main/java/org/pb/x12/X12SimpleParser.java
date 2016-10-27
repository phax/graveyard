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
public class X12SimpleParser implements IX12Parser
{
  static final int SIZE = 106;
  static final int POS_SEGMENT = 105;
  static final int POS_ELEMENT = 3;
  static final int POS_COMPOSITE_ELEMENT = 104;

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
      fr.close ();
      if (count != SIZE)
        throw new EDIFormatException ();
    }
    final Context context = new Context ();
    context.setSegmentSeparator (buffer[POS_SEGMENT]);
    context.setElementSeparator (buffer[POS_ELEMENT]);
    context.setCompositeElementSeparator (buffer[POS_COMPOSITE_ELEMENT]);

    final char segmentSeparator = context.getSegmentSeparator ();
    final String quotedSegmentSeparator = Pattern.quote (Character.toString (segmentSeparator));

    try (final Scanner scanner = new Scanner (fileName))
    {
      scanner.useDelimiter (quotedSegmentSeparator + "\r\n|" + quotedSegmentSeparator + "\n|" + quotedSegmentSeparator);

      final X12Simple x12 = new X12Simple (context);
      while (scanner.hasNext ())
      {
        final String line = scanner.next ();
        x12.addSegment (line);
      }
      return x12;
    }
  }

  /**
   * The method takes a InputStream and converts it into a X2 object. The X12
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

    final char segmentSeparator = context.getSegmentSeparator ();
    final String quotedSegmentSeparator = Pattern.quote (Character.toString (segmentSeparator));

    try (final Scanner scanner = new Scanner (source))
    {
      scanner.useDelimiter (quotedSegmentSeparator + "\r\n|" + quotedSegmentSeparator + "\n|" + quotedSegmentSeparator);

      final X12Simple x12 = new X12Simple (context);
      while (scanner.hasNext ())
      {
        final String line = scanner.next ();
        final Segment s = x12.addSegment ();
        final String [] tokens = line.split ("\\" + context.getElementSeparator ());
        s.addElements (tokens);
      }
      return x12;
    }
  }
}
