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

/**
 * The class represents an X12 context. A X12 context consists of a segment
 * separator, element separator and a composite element separator.
 *
 * @author Prasad Balan
 */
public class Context
{
  private char m_cSegmentSep;
  private char m_cElementSep;
  private char m_cCompositeElementSep;

  /**
   * Default constructor.
   */
  public Context ()
  {
    this ('~', '*', ':');
  }

  /**
   * Constructor which takes the segment separator, element separator and
   * composite element separator as input.
   *
   * @param s
   *        segment separator
   * @param e
   *        element separator
   * @param c
   *        composite element separator
   */
  public Context (final char s, final char e, final char c)
  {
    m_cSegmentSep = s;
    m_cElementSep = e;
    m_cCompositeElementSep = c;
  }

  /**
   * Returns the composite element separator.
   *
   * @return composite element separator
   */
  public char getCompositeElementSeparator ()
  {
    return m_cCompositeElementSep;
  }

  /**
   * Returns the element separator.
   *
   * @return an element separator
   */
  public char getElementSeparator ()
  {
    return m_cElementSep;
  }

  /**
   * Returns the segment separator.
   *
   * @return a segment separator
   */
  public char getSegmentSeparator ()
  {
    return m_cSegmentSep;
  }

  /**
   * Sets the composite element separator.
   *
   * @param c
   *        the composite element separator.
   */
  public void setCompositeElementSeparator (final char c)
  {
    m_cCompositeElementSep = c;
  }

  /**
   * Sets the element separator.
   *
   * @param e
   *        the element separator.
   */
  public void setElementSeparator (final char e)
  {
    m_cElementSep = e;
  }

  /**
   * Sets the segment separator.
   *
   * @param s
   *        the segment separator
   */
  public void setSegmentSeparator (final char s)
  {
    m_cSegmentSep = s;
  }

  /**
   * @return a <code>String</code> consisting of segment, element and composite
   *         element separator.
   */
  public String getAsString ()
  {
    return "[" + m_cSegmentSep + "," + m_cElementSep + "," + m_cCompositeElementSep + "]";
  }
}
