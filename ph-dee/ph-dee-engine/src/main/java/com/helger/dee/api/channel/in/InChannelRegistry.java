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
package com.helger.dee.api.channel.in;

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
 * Global singleton containing all available in channels.
 *
 * @author Philip Helger
 */
public final class InChannelRegistry extends AbstractGlobalSingleton
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (InChannelRegistry.class);

  private final ICommonsMap <String, IInChannel> m_aMap = new CommonsHashMap <> ();

  @Deprecated
  @UsedViaReflection
  public InChannelRegistry ()
  {}

  @Override
  protected void onAfterInstantiation (@Nonnull final IScope aScope)
  {
    registerDefaultChannels ();
  }

  @Nonnull
  public static InChannelRegistry getInstance ()
  {
    return getGlobalSingleton (InChannelRegistry.class);
  }

  public void registerDefaultChannels ()
  {
    // Register all SPI implementations
    for (final IInChannelRegistrarSPI aSPI : ServiceLoader.load (IInChannelRegistrarSPI.class))
      aSPI.registerInChannels (this);
  }

  public void registerInChannel (@Nonnull final IInChannel aInChannel)
  {
    ValueEnforcer.notNull (aInChannel, "InChannel");

    final String sID = aInChannel.getID ();

    m_aRWLock.writeLocked ( () -> {
      if (m_aMap.containsKey (sID))
        throw new IllegalArgumentException ("InChannel ID '" + sID + "' already registered!");
      m_aMap.put (sID, aInChannel);
    });

    s_aLogger.info ("Registered InChannel '" + sID + "'");
  }

  @Nonnull
  public EChange unregisterAllInChannels ()
  {
    EChange eChange = EChange.UNCHANGED;
    for (final IInChannel aInChannel : getAllInChannels ())
      eChange = eChange.or (unregisterInChannel (aInChannel));
    return eChange;
  }

  @Nonnull
  public EChange unregisterInChannel (@Nonnull final IInChannel aInChannel)
  {
    ValueEnforcer.notNull (aInChannel, "InChannel");

    final String sID = aInChannel.getID ();

    return m_aRWLock.writeLocked ( () -> {
      if (m_aMap.remove (sID) == null)
        return EChange.UNCHANGED;
      s_aLogger.info ("Unregistered InChannel '" + sID + "'");
      return EChange.CHANGED;
    });
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <IInChannel> getAllInChannels ()
  {
    return m_aRWLock.readLocked ( () -> m_aMap.copyOfValues ());
  }

  @Nullable
  public IInChannel getInChannelOfID (@Nullable final String sID)
  {
    if (StringHelper.hasNoText (sID))
      return null;

    return m_aRWLock.readLocked ( () -> m_aMap.get (sID));
  }
}
