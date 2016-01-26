/**
 * Copyright (C) 2012-2015 Philip Helger
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
package com.helger.genetic.model;

import com.helger.commons.annotation.MustImplementEqualsAndHashcode;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;

@MustImplementEqualsAndHashcode
public final class GeneInt implements IGene
{
  private final int m_nValue;

  public GeneInt (final int nValue)
  {
    m_nValue = nValue;
  }

  public double doubleValue ()
  {
    return m_nValue;
  }

  public int intValue ()
  {
    return m_nValue;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof GeneInt))
      return false;
    final GeneInt rhs = (GeneInt) o;
    return EqualsHelper.equals (m_nValue, rhs.m_nValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_nValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("value", m_nValue).toString ();
  }
}
