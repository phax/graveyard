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
package com.openedi.object;

import java.util.Stack;

public class SegmentObject extends AdvancedObject
{
  public String m_sTheParentSegmentGroupName;
  public boolean m_bIsFirstInSegmentGroup;

  public SegmentObject (final String theName,
                        final String theParentSegmentGroupName,
                        final boolean isFirstInSegmentGroup,
                        final Stack <String> theStack)
  {
    super (theName, EObjectType.TYPE_SEGMENT_OBJECT, theStack);
    this.m_sTheParentSegmentGroupName = theParentSegmentGroupName;
    this.m_bIsFirstInSegmentGroup = isFirstInSegmentGroup;
  }

  public SegmentObject (final String theName, final Stack <String> theStack)
  {
    this (theName, null, false, theStack);
  }

}
