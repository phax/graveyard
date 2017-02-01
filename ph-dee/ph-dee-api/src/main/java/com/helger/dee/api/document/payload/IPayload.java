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
package com.helger.dee.api.document.payload;

import java.io.OutputStream;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.WillClose;

import com.helger.commons.io.IHasInputStream;
import com.helger.commons.mime.IMimeType;
import com.helger.commons.state.ESuccess;
import com.helger.dee.api.IDEEObject;

/**
 * Base interface for a single payload.
 *
 * @author Philip Helger
 */
public interface IPayload extends IDEEObject, IHasInputStream
{
  /**
   * @return Length of the payload. Must be &ge; 0.
   */
  @Nonnegative
  long getPayloadLength ();

  /**
   * @return The MIME type of the payload. May not be <code>null</code>.
   */
  @Nonnull
  IMimeType getPayloadMimeType ();

  /**
   * Write the payload to the specified {@link OutputStream}.
   *
   * @param aOS
   *        The output stream to write to. May not be <code>null</code> and will
   *        be closed automatically.
   * @return {@link ESuccess#SUCCESS} in case of success.
   */
  @Nonnull
  ESuccess writePayloadTo (@Nonnull @WillClose OutputStream aOS);
}
