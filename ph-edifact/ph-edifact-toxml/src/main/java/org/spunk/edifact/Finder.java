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

import java.util.HashSet;
import java.util.Set;

import org.spunk.edifact.node.Segment;
import org.spunk.edifact.node.SegmentName;
import org.spunk.edifact.util.Strings;

/**
 * Finder.
 */
public class Finder
{
  /**
   * Find the next segment that matches one of the given names. Case sensitive.
   *
   * @param segment
   *        First segment from which to commence searching.
   * @param value
   *        EDI-segment name, first occurrence after the current node. These are
   *        trimmed to null values. Resulting null values are ignored.
   * @return First {@link Segment} with a segment name matching one given in
   *         <tt>value</tt>, or <tt>null</tt> if none found.
   */
  public static Segment find (final Segment segment, final SegmentName... value)
  {
    Segment returnValue;

    final String [] segmentNames = SegmentName.toStringArray (value);
    returnValue = Finder.find (segment, segmentNames);

    return returnValue;
  }

  /**
   * Find the next segment that matches one of the given names. Case sensitive.
   *
   * @param segment
   *        First segment from which to commence searching.
   * @param value
   *        EDI-segment name, first occurrence after the current node. These are
   *        trimmed to null values. Resulting null values are ignored.
   * @return First {@link Segment} with a segment name matching one given in
   *         <tt>value</tt>, or <tt>null</tt> if none found.
   */
  public static Segment find (final Segment segment, final String... value)
  {
    Segment result = null;

    final Set <String> criteria = new HashSet<> ();
    String [] values = {};

    // Ensure varargs element is non-null
    if (value != null)
    {
      values = value;
    }

    // Trim input arguments to null
    for (final String string : values)
    {
      final String criterion = Strings.trimToNull (string);

      if (criterion != null)
      {
        criteria.add (criterion);
      }
    }

    // Use the trimmed criteria to perform the search
    if (value != null && segment != null)
    {
      result = Finder.doFind (segment, criteria);
    }

    return result;
  }

  private static Segment doFind (final Segment segment, final Set <String> criteria)
  {
    Segment result = null;

    // Skip current node, and start searching at next one
    Segment current = segment.getNext ();

    // Search the linked list for segment with name in criteria
    while (current != null && !criteria.contains (current.getSegmentName ()))
    {
      current = current.getNext ();
    }

    // If the current node is not null, it should be returned as result
    if (current != null && criteria.contains (current.getSegmentName ()))
    {
      result = current;
    }

    return result;
  }
}
