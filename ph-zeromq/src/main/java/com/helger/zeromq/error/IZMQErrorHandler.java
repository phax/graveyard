/**
 * Copyright (C) 2016-2017 Philip Helger (www.helger.com)
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
package com.helger.zeromq.error;

import javax.annotation.Nonnull;

import org.zeromq.ZMQException;

import com.helger.commons.annotation.Nonempty;
import com.helger.zeromq.socket.ZMQSocket;

/**
 * Error handler for ZMQ socket operations
 *
 * @author Philip Helger
 */
@FunctionalInterface
public interface IZMQErrorHandler
{
  /**
   * Exception handling method
   *
   * @param aSocket
   *        The socket on which the exception occurred.
   * @param sOperation
   *        The operation that was performed
   * @param ex
   *        The exception that occurred.
   */
  void handleException (@Nonnull ZMQSocket aSocket, @Nonnull @Nonempty String sOperation, @Nonnull ZMQException ex);
}
