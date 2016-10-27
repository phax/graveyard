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
import java.util.LinkedList;
import java.util.List;

public class EdifactField extends AbstractEdifactField implements Serializable
{
  // Constructor for non composite field
  public EdifactField (final String fieldTagName, final String fieldValue)
  {
    super ();
    this.m_sFieldTagName = fieldTagName;
    this.m_sFieldValue = fieldValue;
    isComposite = false;

  }

  // Constructor for non composite field
  public EdifactField (final String fieldTagName, final String fieldValue, final char releaseChar)
  {
    super ();
    this.m_sFieldTagName = fieldTagName;
    this.m_sFieldValue = removeReleaseChar (fieldValue, releaseChar);
    isComposite = false;

  }

  // Constructor for composite field
  public EdifactField (final String fieldTagName)
  {
    super ();
    this.m_sFieldTagName = fieldTagName;
    isComposite = true;
    subFields = new LinkedList<> ();

  }

  public String m_sFieldValue = null;
  public String m_sFieldTagName = null;
  public List <EdifactSubField> subFields = null;
  public boolean isComposite;
}
