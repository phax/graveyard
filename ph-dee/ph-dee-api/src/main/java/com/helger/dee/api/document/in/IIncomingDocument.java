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
package com.helger.dee.api.document.in;

import javax.annotation.Nonnull;

import com.helger.dee.api.channel.in.IInChannel;
import com.helger.dee.api.document.IDocument;
import com.helger.dee.api.partner.IPartner;

/**
 * Base interface for an incoming document.
 *
 * @author Philip Helger
 */
public interface IIncomingDocument extends IDocument
{
  /**
   * @return The channel via which the document came in. This may not change.
   *         Never <code>null</code>.
   */
  @Nonnull
  IInChannel getInChannel ();

  /**
   * @return The partner from which the document arrived. This may not change.
   *         Never <code>null</code>.
   */
  @Nonnull
  IPartner getSendingPartner ();
}
