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

import java.util.Iterator;

import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsIterable;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.string.StringHelper;

/**
 * This class represents an X12 segment.
 *
 * @author Prasad Balan
 */
public class Segment implements ICommonsIterable <String>
{
  private Context m_aContext;
  private final ICommonsList <String> m_aElements = new CommonsArrayList<> ();

  /**
   * The constructor takes a <code>Context</code> object as input. The context
   * object represents the delimiters in a X12 transaction.
   *
   * @param aContext
   *        the context object
   */
  public Segment (final Context aContext)
  {
    m_aContext = aContext;
  }

  /**
   * Adds <code>String</code> element to the segment. The element is added at
   * the end of the elements in the current segment.
   *
   * @param e
   *        the element to be added
   * @return boolean
   */
  public boolean addElement (final String e)
  {
    return m_aElements.add (e);
  }

  /**
   * Adds <code>String</code> with elements to the segment. The elements are
   * added at the end of the elements in the current segment. e.g.
   * <code>addElements("ISA*ISA01*ISA02");</code>
   *
   * @param s
   *        elements to be split by the element separator
   * @return boolean if all were added
   */
  public boolean addElements (final String s)
  {
    return addElements (StringHelper.getExploded (m_aContext.getElementSeparator (), s));
  }

  /**
   * Adds <code>String</code> elements to the segment. The elements are added at
   * the end of the elements in the current segment. e.g.
   * <code> addElements("ISA", "ISA01", "ISA02");</code>
   *
   * @param es
   *        elements to add
   * @return boolean <code>true</code> if all were added
   */
  public boolean addElements (final String... es)
  {
    for (final String s : es)
      if (!m_aElements.add (s))
        return false;
    return true;
  }

  /**
   * Adds <code>String</code> elements to the segment. The elements are added at
   * the end of the elements in the current segment. e.g.
   * <code> addElements("ISA", "ISA01", "ISA02");</code>
   *
   * @param es
   *        elements to add
   * @return boolean <code>true</code> if all were added
   */
  public boolean addElements (final Iterable <String> es)
  {
    for (final String s : es)
      if (!m_aElements.add (s))
        return false;
    return true;
  }

  /**
   * Adds strings as a composite element to the end of the segment.
   *
   * @param ces
   *        sub-elements of a composite element
   * @return boolean
   */
  public boolean addCompositeElement (final String... ces)
  {
    final StringBuilder aSB = new StringBuilder ();
    final char c = m_aContext.getCompositeElementSeparator ();
    for (final String s : ces)
    {
      if (aSB.length () > 0)
        aSB.append (c);
      aSB.append (s);
    }
    return m_aElements.add (aSB.toString ());
  }

  /**
   * Inserts <code>String</code> element to the segment at the specified
   * position
   *
   * @param index
   *        Index to add
   * @param e
   *        the element to be added
   * @return boolean
   */
  public boolean addElementAt (final int index, final String e)
  {
    return m_aElements.add (e);
  }

  /**
   * Inserts strings as a composite element to segment at specified position
   *
   * @param index
   *        index to add
   * @param ces
   *        sub-elements of a composite element
   */
  public void addCompositeElement (final int index, final String... ces)
  {
    final StringBuilder aSB = new StringBuilder ();
    final char c = m_aContext.getCompositeElementSeparator ();
    for (final String s : ces)
    {
      if (aSB.length () > 0)
        aSB.append (c);
      aSB.append (s);
    }
    m_aElements.add (index, aSB.toString ());
  }

  /**
   * Returns the context object
   *
   * @return Context object
   */
  public Context getContext ()
  {
    return m_aContext;
  }

  /**
   * Returns the <code>String<code> element at the specified position.
   *
   * @param index
   *        position
   * @return the element at the specified position.
   */
  public String getElement (final int index)
  {
    return m_aElements.get (index);
  }

  /**
   * @return List of elements
   */
  public ICommonsList <String> getAllElements ()
  {
    return m_aElements.getClone ();
  }

  /**
   * Returns and <code>Iterator</code> to the elements in the segment.
   *
   * @return Iterator<String>
   */

  public Iterator <String> iterator ()
  {
    return m_aElements.iterator ();
  }

  /**
   * Removes the element at the specified position in this list.
   *
   * @param index
   *        index
   * @return removed element
   */
  public String removeElement (final int index)
  {
    return m_aElements.remove (index);
  }

  /**
   * Removes empty and null elements at the end of segment
   */
  private void _removeTrailingEmptyElements ()
  {
    for (int i = m_aElements.size () - 1; i >= 0; i--)
    {
      if (StringHelper.hasNoText (m_aElements.get (i)))
        m_aElements.remove (i);
      else
        break;
    }
  }

  /**
   * Sets the context of the segment
   *
   * @param context
   *        context object
   */
  public void setContext (final Context context)
  {
    m_aContext = context;
  }

  /**
   * Replaces element at the specified position with the specified
   * <code>String</code>
   *
   * @param index
   *        position of the element to be replaced
   * @param s
   *        new element with which to replace
   */
  public void setElement (final int index, final String s)
  {
    m_aElements.set (index, s);
  }

  /**
   * Replaces composite element at the specified position in segment.
   *
   * @param index
   *        index to overwrite
   * @param ces
   *        sub-elements of a composite element
   */
  public void setCompositeElement (final int index, final String... ces)
  {
    final StringBuilder dump = new StringBuilder ();
    for (final String s : ces)
    {
      dump.append (s);
      dump.append (m_aContext.getCompositeElementSeparator ());
    }
    m_aElements.set (index, dump.substring (0, dump.length () - 1));
  }

  /**
   * Returns number of elements in the segment.
   *
   * @return size
   */
  public int size ()
  {
    return m_aElements.size ();
  }

  /**
   * @return the X12 representation of the segment.
   */
  public String getAsString ()
  {
    final StringBuilder aSB = new StringBuilder ();
    final char cSep = m_aContext.getElementSeparator ();
    for (final String s : m_aElements)
    {
      if (aSB.length () > 0)
        aSB.append (cSep);
      aSB.append (s);
    }
    return aSB.toString ();
  }

  /**
   * Returns the X12 representation of the segment.
   *
   * @param bRemoveTrailingEmptyElements
   *        remove all trailing empty?
   * @return <code>String</code>
   */
  public String getAsString (final boolean bRemoveTrailingEmptyElements)
  {
    if (bRemoveTrailingEmptyElements)
      _removeTrailingEmptyElements ();
    return getAsString ();
  }

  /**
   * Returns the XML representation of the segment.
   *
   * @return <code>String</code>
   */
  public String toXML ()
  {
    final StringBuilder aSB = new StringBuilder ();
    final String sFirst = m_aElements.get (0);
    aSB.append ("<" + sFirst + ">");
    for (int i = 1; i < m_aElements.size (); i++)
    {
      final String sTag = sFirst + StringHelper.getLeadingZero (i, 2);
      aSB.append ("<" + sTag + "><![CDATA[");
      aSB.append (m_aElements.get (i));
      aSB.append ("]]></" + sTag + ">");
    }
    aSB.append ("</" + sFirst + ">");
    return aSB.toString ();
  }

  /**
   * Returns the XML representation of the segment.
   *
   * @param bRemoveTrailingEmptyElements
   *        remove trailing empty elements
   * @return <code>String</code>
   */
  public String toXML (final boolean bRemoveTrailingEmptyElements)
  {
    if (bRemoveTrailingEmptyElements)
      _removeTrailingEmptyElements ();
    return toXML ();
  }
}
