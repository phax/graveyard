/**
 * Copyright (C) 2014-2015 Philip Helger (www.helger.com)
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
package com.helger.jms.exceptionlistener;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;

/**
 * A special JMS exception listener, that encapsulates several
 * {@link ExceptionListener} instances.
 *
 * @author Philip Helger
 */
public class MultiJMSExceptionListener implements ExceptionListener
{
  private final List <ExceptionListener> m_aWrappedExceptionListeners = new ArrayList <ExceptionListener> ();

  public MultiJMSExceptionListener (@Nullable final ExceptionListener... aWrappedExceptionListeners)
  {
    if (aWrappedExceptionListeners != null)
      for (final ExceptionListener aEL : aWrappedExceptionListeners)
        if (aEL != null)
          m_aWrappedExceptionListeners.add (aEL);
  }

  public MultiJMSExceptionListener (@Nullable final Iterable <? extends ExceptionListener> aWrappedExceptionListeners)
  {
    if (aWrappedExceptionListeners != null)
      for (final ExceptionListener aEL : aWrappedExceptionListeners)
        if (aEL != null)
          m_aWrappedExceptionListeners.add (aEL);
  }

  public void onException (final JMSException aException)
  {
    for (final ExceptionListener aEL : m_aWrappedExceptionListeners)
      aEL.onException (aException);
  }
}
