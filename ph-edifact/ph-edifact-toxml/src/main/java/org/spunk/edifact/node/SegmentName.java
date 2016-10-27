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
package org.spunk.edifact.node;

/**
 * Segments defined by the basic EDIFACT syntax rules.
 *
 * @see "http://www.unece.org/trade/untdid/texts/d423.htm"
 */
public enum SegmentName
{

  /**
   * Service string advice.
   */
  UNA ("UNA"),

  /**
   * Interchange control header.
   */
  UNB ("UNB"),

  /**
   * Message header.
   */
  UNH ("UNH"),

  /**
   * Message trailer.
   */
  UNT ("UNT"),

  /**
   * Interchange control trailer.
   */
  UNZ ("UNZ");

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString ()
  {
    return this.name;
  }

  /**
   * Convert SegmentName array to string array.
   * 
   * @param segmentNames
   *        Enums to convert to string.
   * @return String array corresponding to segment names in the input.
   */
  public static String [] toStringArray (final SegmentName... segmentNames)
  {
    String [] returnValue = {};

    if (segmentNames != null)
    {
      returnValue = new String [segmentNames.length];

      for (int i = 0; i < returnValue.length; i++)
      {
        returnValue[i] = segmentNames[i].name;
      }
    }

    return returnValue;
  }

  /**
   * Check if a string matches this segment name (ignoring case).
   * 
   * @param string
   *        to check.
   * @return <tt>true</tt> if string is not null and matches this segment name.
   */
  public boolean matches (final String string)
  {
    return string != null && this.name.equalsIgnoreCase (string);
  }

  private SegmentName (final String value)
  {
    this.name = value;
  }

  private String name;
}
