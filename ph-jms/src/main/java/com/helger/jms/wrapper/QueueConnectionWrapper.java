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
package com.helger.jms.wrapper;

import javax.annotation.Nonnull;
import javax.jms.ConnectionConsumer;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.ServerSessionPool;

/**
 * Wrapped class for a JMS {@link QueueConnection}.
 * 
 * @author Philip Helger
 */
public class QueueConnectionWrapper extends AbstractConnectionWrapper implements QueueConnection
{
  public QueueConnectionWrapper (@Nonnull final JMSWrapper aWrapper, @Nonnull final QueueConnection aWrapped)
  {
    super (aWrapper, aWrapped);
  }

  /**
   * @return The wrapped object. Never <code>null</code>.
   */
  @Override
  @Nonnull
  protected QueueConnection getWrapped ()
  {
    return (QueueConnection) super.getWrapped ();
  }

  @Nonnull
  public QueueSession createQueueSession (final boolean transacted, final int acknowledgeMode) throws JMSException
  {
    final QueueSession aQueueSession = getWrapped ().createQueueSession (transacted, acknowledgeMode);
    return getWrapper ().wrapQueueSession (aQueueSession);
  }

  @Nonnull
  public ConnectionConsumer createConnectionConsumer (final Queue queue,
                                                      final String messageSelector,
                                                      final ServerSessionPool sessionPool,
                                                      final int maxMessages) throws JMSException
  {
    final ConnectionConsumer aConnectionConsumer = getWrapped ().createConnectionConsumer (queue,
                                                                                           messageSelector,
                                                                                           sessionPool,
                                                                                           maxMessages);
    return getWrapper ().wrapConnectionConsumer (aConnectionConsumer);
  }
}
