/**
 * Copyright (C) 2015-2017 Philip Helger (www.helger.com)
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
package com.helger.dee.api.history;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.ext.ICommonsList;

/**
 * Generic interface to handle objects with a history
 *
 * @author Philip Helger
 * @param <T>
 *        data type to handle
 */
public interface IHistory <T> extends Iterable <T>
{
  /**
   * @return The complete history. Never <code>null</code> but maybe empty.
   */
  @Nonnull
  @Nonempty
  ICommonsList <T> getAll ();

  /**
   * @return The current object.
   */
  @Nullable
  T getCurrent ();
}
