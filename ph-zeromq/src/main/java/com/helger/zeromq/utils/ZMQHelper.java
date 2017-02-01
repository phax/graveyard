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
package com.helger.zeromq.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;
import com.helger.commons.lang.GenericReflection;

/**
 * Global helper method for ZMQ
 *
 * @author Philip Helger
 */
@Immutable
public final class ZMQHelper
{
  private ZMQHelper ()
  {}

  // TODO SerializationHelper in ph-commons > 8.5.4
  @Nonnull
  public static byte [] getSerializedByteArray (@Nonnull final Serializable aData)
  {
    try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ())
    {
      // Convert to byte array
      try (final ObjectOutputStream aOOS = new ObjectOutputStream (aBAOS))
      {
        aOOS.writeObject (aData);
      }

      // Main sending
      return aBAOS.toByteArray ();
    }
    catch (final IOException ex)
    {
      throw new IllegalStateException ("Failed to write serializable object " + aData, ex);
    }
  }

  // TODO SerializationHelper in ph-commons > 8.5.4
  @Nonnull
  public static <T> T getDeserializedObject (@Nonnull final byte [] aData)
  {
    // Read new object from byte array
    try (final ObjectInputStream aOIS = new ObjectInputStream (new NonBlockingByteArrayInputStream (aData)))
    {
      return GenericReflection.uncheckedCast (aOIS.readObject ());
    }
    catch (final Exception ex)
    {
      throw new IllegalStateException ("Failed to read serializable object", ex);
    }
  }

}
