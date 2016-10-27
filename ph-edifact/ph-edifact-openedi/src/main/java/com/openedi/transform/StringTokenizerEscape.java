/**
 * Free Message Converter Copyleft 2007 - 2014 Matthias Fricke mf@sapstern.com
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
package com.openedi.transform;

import java.util.LinkedList;
import java.util.List;

import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;

/**
 * @author O1706
 */
public class StringTokenizerEscape
{
  private int pos = 0;
  private final String m_buffer;
  private final char m_delimiter; // ' segment + field : subfield
  @SuppressWarnings ("unused")
  private boolean m_returnDelims = false;
  private final char m_escChar; // ? in EDIFACT

  private final ICommonsList <String> m_ll;
  private final ICommonsList <String> m_ll1; // MFRI

  public StringTokenizerEscape (final String str, final char delim, final boolean returnDelims, final char escapeChar)
  {
    // System.err.println("str == " + str);
    this.m_buffer = str;
    this.m_delimiter = delim;
    this.m_returnDelims = returnDelims;
    this.m_escChar = escapeChar;
    m_ll = new CommonsArrayList<> ();
    while (_hasMoreTokens2 ())
      _nextToken2 ();

    final List <String> lltmp = new LinkedList<> ();
    while (m_ll.isNotEmpty ())
    {
      final String s = m_ll.removeFirst ();
      // remove empty strings
      if (s.length () > 0)
      {
        // Remove delimiters
        if (returnDelims || (s.length () != 1 || s.charAt (0) != m_delimiter))
          lltmp.add (s);
      }
    }
    m_ll.setAll (lltmp);
    lltmp.clear ();
    m_ll1 = new CommonsArrayList<> (m_ll);
  }

  private String _nextToken2 ()
  {
    final StringBuilder bufferDynamic = new StringBuilder ();
    boolean delimExit = false;
    int i = this.pos;
    if (m_buffer == null)
    {
      return null;
    }
    while (true)
    {
      delimExit = false;
      if (i > this.m_buffer.length () - 1)
      {
        this.pos = i;
        break;
      }

      if (this.m_buffer.charAt (i) == this.m_escChar)
      {
        // found escape character: add next character and move to next next
        // character
        // add the escape char
        bufferDynamic.append (this.m_buffer.charAt (i));
        i++;
        if (i > this.m_buffer.length () - 1)
        {
          this.pos = m_buffer.length ();
          break;
        }
        // add the escaped character
        bufferDynamic.append (this.m_buffer.charAt (i));
        // move to next character
        i++;
      }
      else
        if (this.m_buffer.charAt (i) == this.m_delimiter)
        {
          // delimiter --> return token
          i++; // skip delimiter and move to next char
          this.pos = i;
          delimExit = true;
          break;
        }
        else
        {
          // append current char and move to next char
          bufferDynamic.append (this.m_buffer.charAt (i));
          i++;
        }

    }
    m_ll.add (bufferDynamic.toString ());
    if (delimExit)
    {
      m_ll.add (Character.toString (m_delimiter));
    }
    return (null);
  }

  private boolean _hasMoreTokens2 ()
  {
    if (m_buffer == null)
    {
      return false;
    }
    return (!(this.pos > this.m_buffer.length () - 1));
  }

  public boolean hasMoreTokens ()
  {
    return m_ll.isNotEmpty ();

  }

  public String nextToken ()
  {
    return m_ll.removeFirst ();
  }

  public ICommonsList <String> getAllTokens ()
  {
    return m_ll1;
  }
}
