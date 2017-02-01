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
package com.helger.dee.api.document;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.dee.api.IDEEObject;
import com.helger.dee.api.document.payload.IPayload;
import com.helger.dee.api.document.status.IDocumentStatus;
import com.helger.dee.api.history.IHistory;
import com.helger.dee.api.metadata.IMetadata;

/**
 * Base interface for incoming and outgoing documents
 *
 * @author Philip Helger
 */
public interface IDocument extends IDEEObject
{
  /**
   * @return The metadata for this document.
   */
  @Nonnull
  IMetadata getMetadata ();

  /**
   * @return The payload. Never <code>null</code>.
   */
  @Nonnull
  IHistory <IPayload> getPayload ();

  /**
   * @return The current payload. May be <code>null</code>.
   */
  @Nullable
  default IPayload getCurrentPayload ()
  {
    return getPayload ().getCurrent ();
  }

  /**
   * @return The current status of the document. May not be <code>null</code>.
   */
  @Nonnull
  IHistory <IDocumentStatus> getStatus ();

  /**
   * @return The current status. May be <code>null</code>.
   */
  @Nullable
  default IDocumentStatus getCurrentStatus ()
  {
    return getStatus ().getCurrent ();
  }
}
