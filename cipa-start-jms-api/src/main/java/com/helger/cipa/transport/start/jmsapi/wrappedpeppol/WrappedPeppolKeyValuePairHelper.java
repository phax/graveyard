/**
 * Copyright (C) 2013-2015 Philip Helger (www.helger.com)
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
package com.helger.cipa.transport.start.jmsapi.wrappedpeppol;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotations.ReturnsMutableCopy;
import com.helger.commons.collections.pair.KeyValuePairList;

/**
 * A utility class that simplifies the handling of Key-Value-Pairs within a
 * {@link WrappedPeppolType} object.
 * 
 * @author Philip Helger
 */
public class WrappedPeppolKeyValuePairHelper
{
  private final WrappedPeppolType m_aWrappedPeppol;

  public WrappedPeppolKeyValuePairHelper (@Nonnull final WrappedPeppolType aWrappedPeppol)
  {
    if (aWrappedPeppol == null)
      throw new NullPointerException ("WrappedPeppol");
    m_aWrappedPeppol = aWrappedPeppol;
  }

  /**
   * Check if the passed key is contained or not.
   * 
   * @param sKey
   *        Key name. May be <code>null</code>.
   * @return <code>true</code> if the key is contained, <code>false</code> if
   *         not.
   */
  public boolean containsKey (@Nullable final String sKey)
  {
    return getValueOfKey (sKey) != null;
  }

  /**
   * Get the value of the passed key.
   * 
   * @param sKey
   *        Key name. May be <code>null</code>.
   * @return The value of the passed key or <code>null</code> if no such key is
   *         contained.
   */
  @Nullable
  public String getValueOfKey (@Nullable final String sKey)
  {
    if (sKey != null)
      for (final KeyValuePairType aKVP : m_aWrappedPeppol.getKeyValuePair ())
        if (sKey.equals (aKVP.getKey ()))
          return aKVP.getValue ();
    return null;
  }

  /**
   * Check if the passed key is contained or not. The name of the key is checked
   * case insensitive.
   * 
   * @param sKey
   *        Key name. May be <code>null</code>.
   * @return <code>true</code> if the key is contained, <code>false</code> if
   *         not.
   */
  public boolean containsCaseInsensitiveKey (@Nullable final String sKey)
  {
    return getValueOfCaseInsensitiveKey (sKey) != null;
  }

  /**
   * Get the value of the passed key using case insensitive key name matching.
   * 
   * @param sKey
   *        Key name. May be <code>null</code>.
   * @return The value of the passed key or <code>null</code> if no such key is
   *         contained.
   */
  @Nullable
  public String getValueOfCaseInsensitiveKey (@Nullable final String sKey)
  {
    if (sKey != null)
      for (final KeyValuePairType aKVP : m_aWrappedPeppol.getKeyValuePair ())
        if (sKey.equalsIgnoreCase (aKVP.getKey ()))
          return aKVP.getValue ();
    return null;
  }

  /**
   * Check if the passed key with the passed value is contained.
   * 
   * @param sKey
   *        The key to check. May be <code>null</code>.
   * @param sSearchValue
   *        The value to check. May be <code>null</code>.
   * @return <code>true</code> if the passed key is contained and the value
   *         matches the passe value
   */
  public boolean hasKeyValuePair (@Nullable final String sKey, @Nullable final String sSearchValue)
  {
    final String sKeyValue = getValueOfKey (sKey);
    return sKeyValue != null && sKeyValue.equals (sSearchValue);
  }

  /**
   * @return A non-<code>null</code> list with all keys.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllKeysList ()
  {
    final List <String> ret = new ArrayList <String> ();
    for (final KeyValuePairType aKVP : m_aWrappedPeppol.getKeyValuePair ())
      ret.add (aKVP.getKey ());
    return ret;
  }

  /**
   * @return A non-<code>null</code> set with all keys.
   */
  @Nonnull
  @ReturnsMutableCopy
  public Set <String> getAllKeysSet ()
  {
    final Set <String> ret = new HashSet <String> ();
    for (final KeyValuePairType aKVP : m_aWrappedPeppol.getKeyValuePair ())
      ret.add (aKVP.getKey ());
    return ret;
  }

  /**
   * @return A non-<code>null</code> list with all values.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllValuesList ()
  {
    final List <String> ret = new ArrayList <String> ();
    for (final KeyValuePairType aKVP : m_aWrappedPeppol.getKeyValuePair ())
      ret.add (aKVP.getValue ());
    return ret;
  }

  /**
   * @return A non-<code>null</code> map from key to value.
   */
  @Nonnull
  @ReturnsMutableCopy
  public Map <String, String> getAsKeyValueMap ()
  {
    final Map <String, String> ret = new LinkedHashMap <String, String> ();
    for (final KeyValuePairType aKVP : m_aWrappedPeppol.getKeyValuePair ())
      ret.put (aKVP.getKey (), aKVP.getValue ());
    return ret;
  }

  /**
   * @return A non-<code>null</code> {@link KeyValuePairList}.
   */
  @Nonnull
  @ReturnsMutableCopy
  public KeyValuePairList <String, String> getAsKeyValuePairList ()
  {
    final KeyValuePairList <String, String> ret = new KeyValuePairList <String, String> ();
    for (final KeyValuePairType aKVP : m_aWrappedPeppol.getKeyValuePair ())
      ret.add (aKVP.getKey (), aKVP.getValue ());
    return ret;
  }
}
