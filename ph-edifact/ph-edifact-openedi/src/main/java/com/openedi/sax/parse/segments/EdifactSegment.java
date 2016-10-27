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
package com.openedi.sax.parse.segments;

import java.io.Serializable;

import org.w3c.dom.Element;

import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;

public class EdifactSegment implements Serializable
{
  /** Creates a new instance of EdifactSegment */
  public EdifactSegment (final String name, final boolean isGroupSegment)
  {
    this.m_segmentName = name;
    this.m_isGroupSegment = isGroupSegment;

  }

  public Element m_theElement;
  public boolean m_isGroupSegment = false;
  public String m_segmentName;
  public final ICommonsList <EdifactField> m_segmentFields = new CommonsArrayList<> ();
  public final ICommonsList <EdifactSegment> m_childSegments = new CommonsArrayList<> ();
  public String m_segmentString;
  // public List<String> theFieldValueList ;
}
