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

import javax.annotation.Nonnull;

import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsIterable;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.string.StringHelper;

/**
 * The X12 class is the object representation of an ANSI X12 transaction. The
 * building block of an X12 transaction is an element. Some elements may be made
 * of sub elements. Elements combine to form segments. Segments are grouped as
 * loops. And a set of loops form an X12 transaction.
 *
 * @author Prasad Balan
 */

public class X12Simple implements IEDI, ICommonsIterable <Segment>
{
  private Context m_aContext;
  private final ICommonsList <Segment> m_aSegments;

  /**
   * The constructor takes a context object.
   *
   * @param c
   *        a Context object
   */
  public X12Simple (final Context c)
  {
    m_aContext = c;
    m_aSegments = new CommonsArrayList<> ();
  }

  /**
   * Creates an empty instance of <code>Segment</code> and adds the segment to
   * the end of the X12 transaction. The returned instance can be used to add
   * elements to the segment.
   *
   * @return a new Segment object
   */
  @Nonnull
  public Segment addSegment ()
  {
    final Segment s = new Segment (m_aContext);
    m_aSegments.add (s);
    return s;
  }

  /**
   * Takes a <code>String</code> representation of segment, creates a
   * <code>Segment</code> object and adds the segment to the end of the X12
   * transaction.
   *
   * @param segment
   *        <code>String</code> representation of the Segment.
   * @return a new Segment object
   */
  public Segment addSegment (final String segment)
  {
    final Segment s = new Segment (m_aContext);
    final String [] elements = StringHelper.getExplodedArray (m_aContext.getElementSeparator (), segment);
    s.addElements (elements);
    m_aSegments.add (s);
    return s;
  }

  /**
   * Takes a <code>Segment</code> and adds the segment to the end of the X12
   * transaction.
   *
   * @param segment
   *        <code>Segment</code> representation of the Segment.
   * @return a new Segment object
   */
  public Segment addSegment (final Segment segment)
  {
    m_aSegments.add (segment);
    return segment;
  }

  /**
   * Creates an empty instance of <code>Segment</code> and inserts the segment
   * at the specified position in the X12 transaction. The returned instance can
   * be used to add elements to the segment.
   *
   * @param index
   *        position at which to add the segment.
   * @return a new Segment object
   */
  public Segment addSegment (final int index)
  {
    final Segment s = new Segment (m_aContext);
    m_aSegments.add (index, s);
    return s;
  }

  /**
   * Takes a <code>String</code> representation of segment, creates a
   * <code>Segment</code> object and inserts the segment at the specified
   * position
   *
   * @param index
   *        to add
   * @param segment
   *        <code>String</code> representation of the Segment.
   * @return a new Segment object
   */
  public Segment addSegment (final int index, final String segment)
  {
    final Segment s = new Segment (m_aContext);
    final String [] elements = StringHelper.getExplodedArray (m_aContext.getElementSeparator (), segment);
    s.addElements (elements);
    m_aSegments.add (index, s);
    return s;
  }

  /**
   * Takes a <code>String</code> representation of segment, creates a
   * <code>Segment</code> object and inserts the segment at the specified
   * position
   *
   * @param index
   *        to get
   * @param segment
   *        <code>String</code> representation of the Segment.
   * @return a new Segment object
   */
  public Segment addSegment (final int index, final Segment segment)
  {
    m_aSegments.add (index, segment);
    return segment;
  }

  /**
   * Get the segments in the X12 transaction.
   *
   * @param name
   *        name of a segment
   * @return List<Segment>
   */
  public ICommonsList <Segment> findSegment (final String name)
  {
    final ICommonsList <Segment> foundSegments = new CommonsArrayList<> ();
    for (final Segment s : m_aSegments)
      if (name.equals (s.getElement (0)))
        foundSegments.add (s);
    return foundSegments;
  }

  /**
   * Returns the context of the X12 transaction.
   *
   * @return Context object
   */
  public Context getContext ()
  {
    return m_aContext;
  }

  /**
   * Returns the <code>Segment<code> at the specified position.
   *
   * @param index
   *        index to get
   * @return Segment at the specified index
   */
  public Segment getSegment (final int index)
  {
    return m_aSegments.get (index);
  }

  /**
   * @return List of segments
   */
  public ICommonsList <Segment> getAllSegments ()
  {
    return m_aSegments.getClone ();
  }

  /**
   * Returns and <code>Iterator</code> to the elements in the segment.
   *
   * @return Iterator<String>
   */
  public Iterator <Segment> iterator ()
  {
    return m_aSegments.iterator ();
  }

  /**
   * Removes the segment at the specified position in this list.
   *
   * @param index
   *        index
   * @return removed object
   */
  public Segment removeSegment (final int index)
  {
    return m_aSegments.remove (index);
  }

  /**
   * Sets the context of the current transaction.
   *
   * @param context
   *        context
   */
  public void setContext (final Context context)
  {
    m_aContext = context;
  }

  /**
   * Creates an empty instance of <code>Segment</code> and replaces the segment
   * at specified position in the X12 transaction. The returned instance can be
   * used to add elements to the segment.
   *
   * @param index
   *        position at which to add the segment.
   * @return a new Segment object
   */
  public Segment setSegment (final int index)
  {
    final Segment s = new Segment (m_aContext);
    m_aSegments.set (index, s);
    return s;
  }

  /**
   * Takes a <code>String</code> representation of segment, creates a
   * <code>Segment</code> object and replaces the segment at the specified
   * position in the X12 transaction.
   *
   * @param index
   *        position of the segment to be replaced.
   * @param segment
   *        <code>String</code> representation of the Segment.
   * @return a new Segment object
   */
  public Segment setSegment (final int index, final String segment)
  {
    final Segment s = new Segment (m_aContext);
    final String [] elements = segment.split ("\\" + m_aContext.getElementSeparator ());
    s.addElements (elements);
    m_aSegments.set (index, s);
    return s;
  }

  /**
   * Replaces <code>Segment</code> at the specified position in X12 transaction.
   *
   * @param index
   *        position of the segment to be replaced.
   * @param segment
   *        <code>Segment</code>
   * @return a new Segment object
   */
  public Segment setSegment (final int index, final Segment segment)
  {
    m_aSegments.set (index, segment);
    return segment;
  }

  /**
   * Returns number of segments in the transaction
   *
   * @return size
   */
  public int size ()
  {
    return m_aSegments.size ();
  }

  /**
   * @return the X12 transaction in <code>String</code> format. This method is
   *         used to convert the X12 object into a X12 transaction.
   */
  public String getAsString ()
  {
    return getAsString (false);
  }

  /**
   * Returns the X12 representation of the segment.
   *
   * @param bRemoveTrailingEmptyElements
   *        remove trailing empty elements
   * @return string
   */
  public String getAsString (final boolean bRemoveTrailingEmptyElements)
  {
    final StringBuilder aSB = new StringBuilder ();
    final char c = m_aContext.getSegmentSeparator ();
    for (final Segment s : m_aSegments)
      aSB.append (s.getAsString (bRemoveTrailingEmptyElements)).append (c);
    return aSB.toString ();
  }

  /**
   * Returns the X12 transaction in XML format. This method translates the X12
   * object into XML format.
   *
   * @return XML string
   */
  public String toXML ()
  {
    return toXML (false);
  }

  /**
   * Returns the X12 transaction in XML format. This method translates the X12
   * object into XML format.
   *
   * @param bRemoveTrailingEmptyElements
   *        remove trailing empty elements
   * @return XML
   */
  public String toXML (final boolean bRemoveTrailingEmptyElements)
  {
    final StringBuilder aSB = new StringBuilder ();
    aSB.append ("<X12>");
    for (final Segment s : m_aSegments)
      aSB.append (s.toXML (bRemoveTrailingEmptyElements));
    aSB.append ("</X12>");
    return aSB.toString ();
  }
}
