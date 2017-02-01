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
package com.helger.dee.api.channel.out;

import java.util.ServiceLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.annotation.UsedViaReflection;
import com.helger.commons.collection.ext.CommonsHashMap;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.collection.ext.ICommonsMap;
import com.helger.commons.scope.IScope;
import com.helger.commons.scope.singleton.AbstractGlobalSingleton;
import com.helger.commons.state.EChange;
import com.helger.commons.string.StringHelper;

/**
 * Global singleton containing all available out channels.
 *
 * @author Philip Helger
 */
public final class OutChannelRegistry extends AbstractGlobalSingleton
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (OutChannelRegistry.class);

  private final ICommonsMap <String, IOutChannel> m_aMap = new CommonsHashMap <> ();

  @Deprecated
  @UsedViaReflection
  public OutChannelRegistry ()
  {}

  @Override
  protected void onAfterInstantiation (@Nonnull final IScope aScope)
  {
    registerDefaultChannels ();
  }

  @Nonnull
  public static OutChannelRegistry getInstance ()
  {
    return getGlobalSingleton (OutChannelRegistry.class);
  }

  public void registerDefaultChannels ()
  {
    // Register all SPI implementations
    for (final IOutChannelRegistrarSPI aSPI : ServiceLoader.load (IOutChannelRegistrarSPI.class))
      aSPI.registerOutChannels (this);
  }

  public void registerOutChannel (@Nonnull final IOutChannel aOutChannel)
  {
    ValueEnforcer.notNull (aOutChannel, "OutChannel");

    final String sID = aOutChannel.getID ();

    m_aRWLock.writeLocked ( () -> {
      if (m_aMap.containsKey (sID))
        throw new IllegalArgumentException ("OutChannel ID '" + sID + "' alreay registered!");
      m_aMap.put (sID, aOutChannel);
    });

    s_aLogger.info ("Registered OutChannel '" + sID + "'");
  }

  @Nonnull
  public EChange unregisterAllOutChannels ()
  {
    EChange eChange = EChange.UNCHANGED;
    for (final IOutChannel aOutChannel : getAllOutChannels ())
      eChange = eChange.or (unregisterOutChannel (aOutChannel));
    return eChange;
  }

  @Nonnull
  public EChange unregisterOutChannel (@Nonnull final IOutChannel aOutChannel)
  {
    ValueEnforcer.notNull (aOutChannel, "OutChannel");

    final String sID = aOutChannel.getID ();

    return m_aRWLock.writeLocked ( () -> {
      if (m_aMap.remove (sID) == null)
        return EChange.UNCHANGED;
      s_aLogger.info ("Unregistered OutChannel '" + sID + "'");
      return EChange.CHANGED;
    });
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <IOutChannel> getAllOutChannels ()
  {
    return m_aRWLock.readLocked ( () -> m_aMap.copyOfValues ());
  }

  @Nullable
  public IOutChannel getOutChannelOfID (@Nullable final String sID)
  {
    if (StringHelper.hasNoText (sID))
      return null;

    return m_aRWLock.readLocked ( () -> m_aMap.get (sID));
  }
}
