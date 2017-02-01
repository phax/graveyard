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
package com.helger.dee.api.document.status;

import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.dee.api.IDEEObject;

/**
 * Base interface for a single document status (instance).
 *
 * @author Philip Helger
 */
public interface IDocumentStatus extends IDEEObject
{
  /**
   * @return The respective status type. Never <code>null</code>.
   */
  @Nonnull
  IDocumentStatusType getStatusType ();

  /**
   * @return The date and time when the document status was entered. Never
   *         <code>null</code>.
   */
  @Nonnull
  LocalDateTime getStartDateTime ();

  /**
   * @return The date and time when the document status was left. May be
   *         <code>null</code> if this is the current document status.
   */
  @Nullable
  LocalDateTime getEndDateTime ();
}
