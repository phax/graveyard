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
import com.helger.commons.hierarchy.IHasChildren;
import com.helger.commons.hierarchy.IHasParent;

/**
 * The Loop class is the representation of an Loop in a ANSI X12 transaction.
 * The building block of an X12 transaction is an element. Some elements may be
 * made of sub elements. Elements combine to form segments. Segments are grouped
 * as loops. And a set of loops form an X12 transaction.
 *
 * @author Prasad Balan
 */

public class Loop implements ICommonsIterable <Segment>, IHasParent <Loop>, IHasChildren <Loop>
{
  private Context m_aContext;
  private String m_sName;
  private final ICommonsList <Segment> m_aSegments = new CommonsArrayList<> ();
  private final ICommonsList <Loop> m_aLoops = new CommonsArrayList<> ();
  private Loop m_aParent;
  private int m_nDepth; // used to debug

  /**
   * The constructor takes a context object.
   *
   * @param aContext
   *        a Context object
   * @param name
   *        Name
   */
  public Loop (final Context aContext, final String name)
  {
    m_aContext = aContext;
    m_sName = name;
    m_aParent = null;
  }

  /**
   * Creates an empty instance of <code>Loop</code> and adds the loop as a child
   * to the current Loop. The returned instance can be used to add segments to
   * the child loop.
   *
   * @param name
   *        name of the loop
   * @return a new child Loop object
   */
  public Loop addChild (final String name)
  {
    final Loop l = new Loop (m_aContext, name);
    l._setParent (this);
    l.m_nDepth = m_nDepth + 1; // debug
    m_aLoops.add (l);
    return l;
  }

  /**
   * Inserts <code>Loop</code> as a child loop at the specified position.
   *
   * @param index
   *        position at which to add the loop.
   * @param loop
   *        Loop to be added
   */
  public void addChild (final int index, final Loop loop)
  {
    loop._setParent (this);
    loop.m_nDepth = m_nDepth + 1; // debug
    m_aLoops.add (index, loop);
  }

  /**
   * Creates an empty instance of <code>Segment</code> and adds the segment to
   * current Loop. The returned instance can be used to add elements to the
   * segment.
   *
   * @return a new Segment object
   */
  public Segment addSegment ()
  {
    final Segment s = new Segment (m_aContext);
    m_aSegments.add (s);
    return s;
  }

  /**
   * Takes a <code>String</code> representation of segment, creates a
   * <code>Segment</code> object and adds the segment to the current Loop.
   *
   * @param segment
   *        <code>String</code> representation of the Segment.
   * @return a new Segment object
   */
  public Segment addSegment (final String segment)
  {
    final Segment s = new Segment (m_aContext);
    final String [] elements = segment.split ("\\" + m_aContext.getElementSeparator ());
    s.addElements (elements);
    m_aSegments.add (s);
    return s;
  }

  /**
   * Adds <code>Segment</code> at the end of the current Loop
   *
   * @param segment
   *        <code>Segment</code>
   */
  public void addSegment (final Segment segment)
  {
    m_aSegments.add (segment);
  }

  /**
   * Creates an empty instance of <code>Segment</code> and adds the segment at
   * the specified position in the current Loop. The returned instance can be
   * used to add elements to the segment.
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
   * <code>Segment</code> object and adds the segment at the specified position
   * in the current Loop.
   *
   * @param index
   *        position to add the segment.
   * @param segment
   *        <code>String</code> representation of the segment.
   * @return a new Segment object
   */
  public Segment addSegment (final int index, final String segment)
  {
    final Segment s = new Segment (m_aContext);
    final String [] elements = segment.split ("\\" + m_aContext.getElementSeparator ());
    s.addElements (elements);
    m_aSegments.add (index, s);
    return s;
  }

  /**
   * Adds <code>Segment</code> at the specified position in current Loop.
   *
   * @param index
   *        position to add the segment.
   * @param segment
   *        <code>String</code> representation of the segment.
   */
  public void addSegment (final int index, final Segment segment)
  {
    m_aSegments.add (index, segment);
  }

  /**
   * Creates an empty instance of <code>Loop</code> and inserts the loop as a
   * child loop at the specified position. The returned instance can be used to
   * add segments to the child loop.
   *
   * @param index
   *        position at which to add the loop
   * @param name
   *        name of the loop
   * @return a new child Loop object
   */
  public Loop addChild (final int index, final String name)
  {
    final Loop l = new Loop (m_aContext, name);
    l._setParent (this);
    l.m_nDepth = m_nDepth + 1; // debug
    m_aLoops.add (index, l);
    return l;
  }

  /**
   * Checks if the Loop contains the specified child Loop. It will check the
   * complete child hierarchy.
   *
   * @param name
   *        name of a child loop
   * @return boolean
   */
  public boolean hasLoop (final String name)
  {
    for (final Loop l : m_aLoops)
    {
      if (name.equals (l.getName ()))
        return true;
      if (l.hasLoop (name))
        return true;
    }
    return false;
  }

  /**
   * Get the loop in the X12 transaction It will check the complete child
   * hierarchy.
   *
   * @param name
   *        name of a loop
   * @return List<Loop>
   */
  public ICommonsList <Loop> findLoop (final String name)
  {
    final ICommonsList <Loop> foundLoops = new CommonsArrayList<> ();
    for (final Loop l : m_aLoops)
    {
      if (name.equals (l.getName ()))
        foundLoops.add (l);

      final ICommonsList <Loop> moreLoops = l.findLoop (name);
      if (moreLoops.isNotEmpty ())
        foundLoops.addAll (moreLoops);
    }
    return foundLoops;
  }

  /**
   * Get the segment in the X12 transaction It will check the current loop and
   * the complete child hierarchy.
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

    for (final Loop l : m_aLoops)
    {
      final ICommonsList <Segment> moreSegments = l.findSegment (name);
      if (moreSegments.isNotEmpty ())
        foundSegments.addAll (moreSegments);
    }
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
   * Returns the <code>Loop<code> at the specified position.
   *
   * @param index
   *        index to get
   * @return Loop at the specified index
   */
  public Loop getLoop (final int index)
  {
    return m_aLoops.get (index);
  }

  /**
   * Returns the loops
   *
   * @return List<Loop>
   */
  public ICommonsList <Loop> getAllLoops ()
  {
    return m_aLoops.getClone ();
  }

  /**
   * @return Parent Loop
   */
  public Loop getParent ()
  {
    return m_aParent;
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
   * Returns the segments in the current loop.
   *
   * @return List of segments
   */
  public ICommonsList <Segment> getAllSegments ()
  {
    return m_aSegments.getClone ();
  }

  /**
   * Returns the name of the current Loop.
   *
   * @return String
   */
  public String getName ()
  {
    return m_sName;
  }

  /**
   * Returns and <code>Iterator</code> to the segments in the loop.
   *
   * @return Iterator<Segment>
   */
  public Iterator <Segment> iterator ()
  {
    return m_aSegments.iterator ();
  }

  /**
   * Removes the loop at the specified position in this list.
   *
   * @param index
   *        index to remove
   * @return removed object
   */
  public Loop removeLoop (final int index)
  {
    return m_aLoops.remove (index);
  }

  /**
   * Removes the segment at the specified position in this list.
   *
   * @param index
   *        index to remove
   * @return removed object
   */
  public Segment removeSegment (final int index)
  {
    return m_aSegments.remove (index);
  }

  /**
   * Returns <code>List<Loop></code> of child Loops
   *
   * @return List<Loop>
   */
  public ICommonsList <Loop> getAllChildren ()
  {
    return m_aLoops.getClone ();
  }

  public Iterable <Loop> getChildrenIter ()
  {
    return m_aLoops;
  }

  public int getChildCount ()
  {
    return m_aLoops.size ();
  }

  /**
   * Returns number of segments in Loop and child loops
   *
   * @return size
   */
  public int size ()
  {
    int size = m_aSegments.size ();
    for (final Loop l : m_aLoops)
      size += l.size ();
    return size;
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
   * Creates a new <code>Loop</code> and replaces the child loop at the
   * specified position. The returned instance can be used to add segments to
   * the child loop.
   *
   * @param name
   *        name of the loop
   * @param index
   *        position at which to add the loop.
   * @return a new child Loop object
   */
  public Loop setChild (final int index, final String name)
  {
    final Loop l = new Loop (m_aContext, name);
    l._setParent (this);
    l.m_nDepth = m_nDepth + 1; // debug
    m_aLoops.set (index, l);
    return l;
  }

  /**
   * Replaces child <code>Loop</code> at the specified position.
   *
   * @param index
   *        position at which to add the loop.
   * @param loop
   *        Loop to add
   */
  public void setChild (final int index, final Loop loop)
  {
    loop._setParent (this);
    loop.m_nDepth = m_nDepth + 1; // debug
    m_aLoops.set (index, loop);
  }

  /**
   * @param parent
   *        parent
   */
  private void _setParent (final Loop parent)
  {
    m_aParent = parent;
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
   */
  public void setSegment (final int index, final Segment segment)
  {
    m_aSegments.set (index, segment);
  }

  /**
   * Sets the name of the current Loop
   *
   * @param name
   *        <code>String</code>
   */
  public void setName (final String name)
  {
    m_sName = name;
  }

  /**
   * Returns the Loop in X12 <code>String</code> format. This method is used to
   * convert the X12 object into a X12 transaction.
   *
   * @return String
   */
  public String getAsString ()
  {
    return getAsString (false);
  }

  /**
   * Returns the Loop in X12 <code>String</code> format. This method is used to
   * convert the X12 object into a X12 transaction.
   *
   * @param bRemoveTrailingEmptyElements
   *        true to remove trailing empty elements
   * @return build string
   */
  public String getAsString (final boolean bRemoveTrailingEmptyElements)
  {
    final StringBuilder dump = new StringBuilder ();
    for (final Segment s : m_aSegments)
    {
      dump.append (s.getAsString (bRemoveTrailingEmptyElements));
      dump.append (m_aContext.getSegmentSeparator ());
    }
    for (final Loop l : m_aLoops)
      dump.append (l.getAsString (bRemoveTrailingEmptyElements));
    return dump.toString ();
  }

  /**
   * Returns the Loop in XML <code>String</code> format. This method is used to
   * convert the X12 object into a XML string.
   *
   * @return XML String
   */
  public String toXML ()
  {
    return toXML (false);
  }

  /**
   * Returns the Loop in XML <code>String</code> format. This method is used to
   * convert the X12 object into a XML string.
   *
   * @param bRemoveTrailingEmptyElements
   *        remove trailing empty
   * @return XML string
   */
  public String toXML (final boolean bRemoveTrailingEmptyElements)
  {
    final StringBuilder aSB = new StringBuilder ();
    aSB.append ("<LOOP NAME=\"").append (m_sName).append ("\">");
    for (final Segment s : m_aSegments)
      aSB.append (s.toXML (bRemoveTrailingEmptyElements));
    for (final Loop l : m_aLoops)
      aSB.append (l.toXML (bRemoveTrailingEmptyElements));
    aSB.append ("</LOOP>");
    return aSB.toString ();
  }

  /**
   * Generally not used. Mostly for debugging.
   *
   * @return depth
   */
  public int getDepth ()
  {
    return m_nDepth;
  }
}
