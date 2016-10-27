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

public class EdifactSubField extends AbstractEdifactField implements Serializable
{
  /** Creates a new instance of EdifactSubField */
  public EdifactSubField (final String subFieldTagName, final String subFieldValue, final char releaseChar)
  {
    this.m_subFieldTagName = subFieldTagName;
    this.m_subFieldValue = super.removeReleaseChar (subFieldValue, releaseChar);
  }

  public String m_subFieldValue;
  public String m_subFieldTagName;
}
