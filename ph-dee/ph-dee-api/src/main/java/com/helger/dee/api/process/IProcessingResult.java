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
package com.helger.dee.api.process;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.state.ISuccessIndicator;
import com.helger.dee.api.document.payload.IPayload;

/**
 * Base interface for the result of a single processing step.
 *
 * @author Philip Helger
 */
public interface IProcessingResult extends ISuccessIndicator, Serializable
{
  /**
   * @return The new payload. May be identical to the original payload.
   */
  @Nonnull
  IPayload getNewPayload ();

  /**
   * @return The number milliseconds the process step took. Always &ge; 0.
   */
  @Nonnull
  long getProcessingMilliseconds ();

  /**
   * @return The exception that occurred during processing. May be
   *         <code>null</code> if processing was successful.
   */
  @Nullable
  Throwable getThrowable ();
}
