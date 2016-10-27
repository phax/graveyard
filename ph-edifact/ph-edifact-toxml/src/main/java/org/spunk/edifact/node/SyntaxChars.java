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
 * SyntaxChars.
 */
public enum SyntaxChars
{

  /**
   * SEGMENT_TERMINATOR.
   */
  SEGMENT_TERMINATOR ('\''),

  /**
   * ELEMENT_SEPARATOR.
   */
  ELEMENT_SEPARATOR ('+'),

  /**
   * COMPONENT_SEPARATOR.
   */
  COMPONENT_SEPARATOR (':'),

  /**
   * DECIMAL_SEPARATOR.
   */
  DECIMAL_SEPARATOR ('.'),

  /**
   * RELEASE_CHARACTER.
   */
  RELEASE_CHARACTER ('?');

  private final char m_cChar;

  private SyntaxChars (final char value)
  {
    m_cChar = value;
  }

  /**
   * Check if character matches input.
   *
   * @param c
   *        Character to check against.
   * @return <tt>true</tt> if this enum constant's value matches the input.
   */
  public boolean matches (final char c)
  {
    return m_cChar == c;
  }

  public char getChar ()
  {
    return m_cChar;
  }

  /**
   * Check if character is a separator, terminator or release char.
   *
   * @param character
   *        Character to check.
   * @return <tt>true</tt> if character is a separator, terminator or release
   *         char.
   */
  public static boolean isSyntaxChar (final char character)
  {
    return SyntaxChars.ELEMENT_SEPARATOR.matches (character) ||
           SyntaxChars.COMPONENT_SEPARATOR.matches (character) ||
           SyntaxChars.COMPONENT_SEPARATOR.matches (character) ||
           SyntaxChars.RELEASE_CHARACTER.matches (character);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString ()
  {
    return Character.toString (this.m_cChar);
  }
}
