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

import java.io.Serializable;
import java.util.List;

import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;

/**
 * EdiSegment.
 */
public class Segment implements Serializable
{
  private String m_sSegmentName;
  private final ICommonsList <DataElement> m_aDataElements = new CommonsArrayList<> ();
  private Segment previous;
  private Segment next;
  private int offset;

  /**
   * Constructor.
   */
  public Segment ()
  {}

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString ()
  {
    return this.m_sSegmentName + "=" + this.m_aDataElements;
  }

  /**
   * Getter.
   *
   * @return offset.
   */
  public int getOffset ()
  {
    return this.offset;
  }

  /**
   * Setter.
   *
   * @param value
   *        New value.
   */
  public void setOffset (final int value)
  {
    this.offset = value;
  }

  /**
   * EDI segment code.
   *
   * @param value
   *        segment code.
   */
  public void setSegmentName (final String value)
  {
    this.m_sSegmentName = value;
  }

  /**
   * EDI segment code.
   *
   * @return segmentName.
   */
  public String getSegmentName ()
  {
    return this.m_sSegmentName;
  }

  /**
   * Getter.
   *
   * @return previous.
   */
  public Segment getPrevious ()
  {
    return this.previous;
  }

  /**
   * Setter.
   *
   * @param value
   *        New value.
   */
  public void setPrevious (final Segment value)
  {
    this.previous = value;
  }

  /**
   * Getter.
   *
   * @return next.
   */
  public Segment getNext ()
  {
    return this.next;
  }

  /**
   * Setter.
   *
   * @param value
   *        New value.
   */
  public void setNext (final Segment value)
  {
    this.next = value;
  }

  /**
   * Add a new data element for this segment.
   *
   * @param value
   *        Data element to add.
   */
  public void addDataElement (final DataElement value)
  {
    this.m_aDataElements.add (value);
  }

  /**
   * All data elements for this segment.
   *
   * @return All data elements for this segment.
   */
  public List <DataElement> getDataElements ()
  {
    return this.m_aDataElements;
  }

  /**
   * Get the text content for a given element and component.
   *
   * @param i
   *        Element index.
   * @param j
   *        Component index.
   * @return Text for given element and component, <tt>null</tt> if no element
   *         or component exists for the given indices.
   */
  public String get (final int i, final int j)
  {
    String value = null;

    DataElement element = null;

    if (i >= 0 && i < this.m_aDataElements.size ())
    {
      element = this.m_aDataElements.get (i);
    }

    Component component = null;

    if (element != null && j >= 0 && j < element.getComponentCount ())
    {
      component = element.getComponentAtIndex (j);
    }

    if (component != null)
    {
      value = component.getText ();
    }

    return value;
  }

}
