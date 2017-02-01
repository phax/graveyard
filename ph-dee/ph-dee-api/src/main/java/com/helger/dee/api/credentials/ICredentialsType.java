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
package com.helger.dee.api.credentials;

import com.helger.commons.text.display.IHasDisplayText;
import com.helger.dee.api.IDEEObject;

/**
 * Base interface for a credentials type. This is e.g. "username + password",
 * "key pair", ...
 *
 * @author Philip Helger
 */
public interface ICredentialsType extends IDEEObject, IHasDisplayText
{
  /* empty */
}
