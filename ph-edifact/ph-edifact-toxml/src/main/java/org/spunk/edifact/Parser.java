/**
 * Copyright 2012 A. Nonymous
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
package org.spunk.edifact;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.CharBuffer;

import org.spunk.edifact.exception.EdifactException;
import org.spunk.edifact.exception.SegmentException;
import org.spunk.edifact.exception.ServiceStringAdviceException;
import org.spunk.edifact.node.Component;
import org.spunk.edifact.node.DataElement;
import org.spunk.edifact.node.Segment;
import org.spunk.edifact.node.SegmentName;
import org.spunk.edifact.node.SyntaxChars;

/**
 * Parser.
 */
public class Parser
{

  /**
   * IGNORED_CHARS.
   */
  public final static String IGNORED_CHARS = " \r\n\t";

  private final static int SEGMENT_TERMINATOR_INDEX = 5;
  private final static int RELEASE_CHARACTER_INDEX = 3;
  private final static int DATA_ELEMENT_SEPARATOR_INDEX = 1;
  private final static int COMPONENT_DATA_ELEMENT_SEPARATOR_INDEX = 0;
  private final static int UNA_SEGMENT_LENGTH = 6;
  private final static int SEGMENT_NAME_LENGTH = 3;

  private String filter;

  /**
   * Parse edi message as a list of edi segments.
   * 
   * @param content
   *        EDI message text.
   * @return Head node of EDI-segment linked list.
   */
  public Segment parse (final String content)
  {
    Segment returnValue = null;

    if (content != null)
    {
      final CharBuffer buffer = CharBuffer.wrap (content);
      returnValue = parse (buffer);
    }

    return returnValue;
  }

  private Segment parse (final CharBuffer buffer)
  {
    Segment returnValue = null;

    boolean isAtEndOfFile = false;
    Segment previous = null;

    while (!isAtEndOfFile)
    {
      final Segment edifactSegment = parseEdiSegment (buffer);

      if (isAccepted (edifactSegment))
      {
        if (edifactSegment != null && previous != null)
        {
          previous.setNext (edifactSegment);
          edifactSegment.setPrevious (previous);
        }

        previous = edifactSegment;

        if (edifactSegment != null && returnValue == null)
        {
          returnValue = edifactSegment;
        }
      }

      isAtEndOfFile = edifactSegment == null;
    }

    return returnValue;
  }

  /**
   * Performance tweak. Only emit XML-nodes that are contained in the filter
   * string. Very basic: tests any segmentName if it is contained in the filter
   * string.
   * 
   * @param value
   *        A string like <tt>"UNH,UNB,EQD,VDC"</tt>.
   */
  public void setFilter (final String value)
  {
    this.filter = value;
  }

  private boolean isAccepted (final Segment item)
  {
    boolean value;

    value = item != null && this.filter != null && this.filter.contains (item.getSegmentName ());
    value |= this.filter == null;

    return value;
  }

  private Segment parseEdiSegment (final CharBuffer buffer)
  {
    Segment edifactSegment = null;

    final StringBuilder builder = new StringBuilder ();
    final int offset = buffer.position ();
    final String segmentName = parseSegmentName (buffer, builder);

    if (segmentName != null)
    {
      edifactSegment = doParseEdiSegment (buffer, builder, segmentName, offset);
    }

    return edifactSegment;
  }

  private Segment doParseEdiSegment (final CharBuffer reader,
                                     final StringBuilder buffer,
                                     final String segmentName,
                                     final int offset)
  {
    final Segment edifactSegment = new Segment ();

    edifactSegment.setOffset (offset);
    edifactSegment.setSegmentName (segmentName);

    if (SegmentName.UNA.matches (segmentName))
    {
      parseUnaSegment (reader, edifactSegment);
    }
    else
    {
      parseSegmentChars (reader, edifactSegment, buffer);
    }

    return edifactSegment;
  }

  private void parseUnaSegment (final CharBuffer reader, final Segment edifactSegment)
  {
    final DataElement ediDataElement = new DataElement ();
    edifactSegment.addDataElement (ediDataElement);

    final char [] unaChars = new char [Parser.UNA_SEGMENT_LENGTH];

    for (int i = 0; i < Parser.UNA_SEGMENT_LENGTH; i++)
    {
      try
      {
        unaChars[i] = reader.get ();
      }
      catch (final BufferUnderflowException cause)
      {
        throw new EdifactException (cause);
      }
    }

    // Only defaults supported at the moment

    validateUNA (unaChars);

    final String unaString = String.valueOf (unaChars);
    final Component ediDataElementComponent = ediDataElement.nextComponent ();
    ediDataElementComponent.setText (unaString);
  }

  private String parseSegmentName (final CharBuffer buffer, final StringBuilder builder)
  {
    String segmentName = null;
    boolean isAtEndOfStream = false;
    int numberOfSegmentNameCharsParsed = 0;
    final char [] segmentNameChars = new char [Parser.SEGMENT_NAME_LENGTH];

    while (!isAtEndOfStream && numberOfSegmentNameCharsParsed < Parser.SEGMENT_NAME_LENGTH)
    {
      isAtEndOfStream = !buffer.hasRemaining ();

      if (!isAtEndOfStream)
      {
        final char c = buffer.get ();

        builder.append (c);

        if (!isIgnoredChar (c))
        {
          segmentNameChars[numberOfSegmentNameCharsParsed++] = c;
        }
      }
      else
        if (numberOfSegmentNameCharsParsed != 0)
        {
          throw new SegmentException ();
        }
    }

    if (!isAtEndOfStream)
    {
      segmentName = String.valueOf (segmentNameChars);
    }

    return segmentName;
  }

  private void parseSegmentChars (final CharBuffer reader, final Segment edifactSegment, final StringBuilder buffer)
  {
    try
    {
      doParseSegmentChars (reader, edifactSegment, buffer);
    }
    catch (final IOException cause)
    {
      throw new EdifactException (cause);
    }
  }

  private void doParseSegmentChars (final CharBuffer reader,
                                    final Segment edifactSegment,
                                    final StringBuilder buffer) throws IOException
  {
    boolean inRelease = false;
    boolean endOfSegment = false;

    while (!endOfSegment && reader.hasRemaining ())
    {
      final char c = reader.get ();

      if (inRelease)
      { // Do not check if this is a control character
        inRelease = false;
        buffer.append (c);
      }
      else
        if (!(inRelease = isReleaseChar (c)))
        {
          buffer.append (c);
          endOfSegment = SyntaxChars.SEGMENT_TERMINATOR.matches (c);

          if (SyntaxChars.ELEMENT_SEPARATOR.matches (c))
          {
            endOfSegment = parseElement (reader, edifactSegment, buffer);
          }
        }
    }
  }

  private boolean parseElement (final CharBuffer reader,
                                final Segment edifactSegment,
                                final StringBuilder buffer) throws IOException
  {
    boolean endOfSegment = false;
    boolean inRelease = false;

    DataElement ediDataElement = null;
    Component ediDataElementComponent = null;
    int n = buffer.length ();

    while (!endOfSegment && reader.hasRemaining ())
    {
      final char c = reader.get ();

      if (ediDataElement == null)
      {
        ediDataElement = new DataElement ();
        edifactSegment.addDataElement (ediDataElement);
      }

      if (ediDataElementComponent == null)
      {
        ediDataElementComponent = ediDataElement.nextComponent ();
      }

      if (inRelease)
      {
        // Do not check if this is a control character
        inRelease = false;
        buffer.append (c);
      }
      else
        if (!(inRelease = isReleaseChar (c)))
        {
          buffer.append (c);
          endOfSegment = SyntaxChars.SEGMENT_TERMINATOR.matches (c);

          if (endOfSegment)
          {
            final String text = buffer.substring (n, buffer.length () - 1);
            ediDataElementComponent.setText (text);
          }
          else
            if (SyntaxChars.ELEMENT_SEPARATOR.matches (c))
            {
              final String text = buffer.substring (n, buffer.length () - 1);
              ediDataElementComponent.setText (text);
              endOfSegment = parseElement (reader, edifactSegment, buffer);
            }
            else
              if (SyntaxChars.COMPONENT_SEPARATOR.matches (c))
              {
                final String text = buffer.substring (n, buffer.length () - 1);
                ediDataElementComponent.setText (text);
                n = buffer.length ();
                ediDataElementComponent = ediDataElement.nextComponent ();
              }
        }
    }

    return endOfSegment;
  }

  private boolean isReleaseChar (final char value)
  {
    boolean isReleaseChar;

    isReleaseChar = SyntaxChars.RELEASE_CHARACTER.matches (value);

    return isReleaseChar;
  }

  private boolean isIgnoredChar (final char value)
  {
    boolean isIgnoredChar;

    final String string = Character.toString (value);
    isIgnoredChar = Parser.IGNORED_CHARS.contains (string);

    return isIgnoredChar;
  }

  private void validateUNA (final char [] unaChars)
  {
    boolean isValidUNA;

    isValidUNA = SyntaxChars.COMPONENT_SEPARATOR.matches (unaChars[Parser.COMPONENT_DATA_ELEMENT_SEPARATOR_INDEX]);
    isValidUNA &= SyntaxChars.ELEMENT_SEPARATOR.matches (unaChars[Parser.DATA_ELEMENT_SEPARATOR_INDEX]);
    // Decimal separator is unused, no text to number conversion performed
    isValidUNA &= SyntaxChars.RELEASE_CHARACTER.matches (unaChars[Parser.RELEASE_CHARACTER_INDEX]);
    // 4th control char is reserved / repetition control char in v4
    isValidUNA &= SyntaxChars.SEGMENT_TERMINATOR.matches (unaChars[Parser.SEGMENT_TERMINATOR_INDEX]);

    if (!isValidUNA)
    {
      throw new ServiceStringAdviceException ();
    }
  }

}
