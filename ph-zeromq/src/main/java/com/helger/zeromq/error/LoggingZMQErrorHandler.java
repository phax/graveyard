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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQException;

import com.helger.commons.annotation.Nonempty;
import com.helger.zeromq.socket.ZMQSocket;

public class LoggingZMQErrorHandler implements IZMQErrorHandler
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LoggingZMQErrorHandler.class);

  public void handleException (@Nonnull final ZMQSocket aSocket,
                               @Nonnull @Nonempty final String sOperation,
                               @Nonnull final ZMQException ex)
  {
    s_aLogger.error ("Error in ZMQ operation " + sOperation + " on " + aSocket.getSocketType () + " socket", ex);
  }
}
