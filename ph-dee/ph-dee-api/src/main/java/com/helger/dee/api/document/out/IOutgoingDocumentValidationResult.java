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
package com.helger.dee.api.document.out;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.helger.dee.api.document.in.IIncomingDocument;
import com.helger.dee.api.document.in.IIncomingDocumentValidationResult;
import com.helger.dee.api.transform.ITransformationResult;

public interface IOutgoingDocumentValidationResult
{
  @Nonnull
  IIncomingDocument getIncomingDocument ();

  @Nonnull
  IIncomingDocumentValidationResult getIncomingDocumentValidationResult ();

  @Nonnull
  ITransformationResult getConversionResult ();

  @Nonnegative
  long getOutgoingDocumentValidationProcessingMilliseconds ();
}
