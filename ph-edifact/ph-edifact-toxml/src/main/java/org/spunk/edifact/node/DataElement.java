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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;

/**
 * DataElement.
 */
public class DataElement implements Serializable
{
  private final ICommonsList <Component> m_aComponents = new CommonsArrayList<> ();

  /**
   * Constructor.
   */
  public DataElement ()
  {}

  /**
   * Getter.
   *
   * @return components.
   */
  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <Component> getAllComponents ()
  {
    return m_aComponents.getClone ();
  }

  @Nonnegative
  public int getComponentCount ()
  {
    return m_aComponents.size ();
  }

  @Nullable
  public Component getComponentAtIndex (final int nIndex)
  {
    return m_aComponents.get (nIndex);
  }

  /**
   * Append a new data element component.
   *
   * @return The newly appended component.
   */
  public Component nextComponent ()
  {
    final Component component = new Component ();
    m_aComponents.add (component);
    return component;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString ()
  {
    return m_aComponents.toString ();
  }
}
