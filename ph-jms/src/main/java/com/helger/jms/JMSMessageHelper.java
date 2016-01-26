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
package com.helger.jms;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageEOFException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import com.helger.commons.CGlobal;
import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.io.IHasInputStream;
import com.helger.commons.io.stream.StreamHelper;

/**
 * Misc JMS utilities concerning the handling of messages.
 *
 * @author Philip Helger
 */
@ThreadSafe
public final class JMSMessageHelper
{
  private JMSMessageHelper ()
  {}

  /**
   * Check if the passed object can be put into
   * {@link StreamMessage#writeObject(Object)}
   *
   * @param aObject
   *        The object to check. May be <code>null</code> which is valid.
   * @return <code>true</code> if the object can be used, <code>false</code>
   *         otherwise.
   */
  public static boolean isValidStreamMessageObject (@Nullable final Object aObject)
  {
    return aObject == null ||
           aObject instanceof Boolean ||
           aObject instanceof Byte ||
           aObject instanceof Character ||
           aObject instanceof Double ||
           aObject instanceof Float ||
           aObject instanceof Integer ||
           aObject instanceof Long ||
           aObject instanceof Short ||
           aObject instanceof String ||
           aObject instanceof byte [];
  }

  /**
   * Check if the passed object can be used as the source for a
   * {@link MapMessage}
   *
   * @param aMap
   *        The map to check. May not be <code>null</code>.
   * @return <code>true</code> if the map can be used, <code>false</code>
   *         otherwise.
   */
  public static boolean isValidMapMessageObject (@Nonnull final Map <?, ?> aMap)
  {
    ValueEnforcer.notNull (aMap, "Map");
    for (final Object aValue : aMap.values ())
      if (!isValidStreamMessageObject (aValue))
        return false;
    return true;
  }

  @Nonnull
  private static BytesMessage _createBytesMessage (@Nonnull final Session aSession,
                                                   @Nonnull final InputStream aIS) throws JMSException
  {
    final BytesMessage aStreamMsg = aSession.createBytesMessage ();
    try
    {
      final byte [] aBuffer = new byte [4 * CGlobal.BYTES_PER_KILOBYTE];
      int nLen = 0;
      while ((nLen = aIS.read (aBuffer)) >= 0)
        aStreamMsg.writeBytes (aBuffer, 0, nLen);
    }
    catch (final IOException ex)
    {
      throw JMSHelper.createException ("Failed to read input stream to create a BytesMessage", ex);
    }
    return aStreamMsg;
  }

  /**
   * Try to create a JMS {@link Message} from the passed object. The following
   * object types are handled in this order:
   * <ul>
   * <li>{@link Message} - used as is</li>
   * <li>{@link String} - {@link TextMessage}</li>
   * <li>{@link CharSequence} - {@link TextMessage}</li>
   * <li>{@link Map} - {@link MapMessage} - the Map must conform to
   * {@link #isValidMapMessageObject(Map)}</li>
   * <li>{@link List} - {@link StreamMessage} - the List items must conform to
   * {@link #isValidStreamMessageObject(Object)}</li>
   * <li>{@link InputStream} - {@link BytesMessage} (not closed!)</li>
   * <li>{@link IHasInputStream} - {@link BytesMessage}</li>
   * <li>byte[] - {@link BytesMessage}</li>
   * <li>{@link Serializable} - {@link ObjectMessage}</li>
   * </ul>
   *
   * @param aSession
   *        The JMS session to use. May not be <code>null</code>.
   * @param aObject
   *        The object to be converted. May be <code>null</code>.
   * @return <code>null</code> if the input object was <code>null</code> or if
   *         it was of no applicable type.
   * @throws JMSException
   *         In case of a JMS error.
   */
  @Nullable
  public static Message createMessageForObject (@Nonnull final Session aSession,
                                                @Nullable final Object aObject) throws JMSException
  {
    ValueEnforcer.notNull (aSession, "Session");

    if (aObject instanceof Message)
      return (Message) aObject;

    if (aObject instanceof String)
      return aSession.createTextMessage ((String) aObject);

    if (aObject instanceof CharSequence)
      return aSession.createTextMessage (aObject.toString ());

    if (aObject instanceof Map <?, ?>)
    {
      final MapMessage aMapMsg = aSession.createMapMessage ();
      final Map <?, ?> aMap = (Map <?, ?>) aObject;
      if (!isValidMapMessageObject (aMap))
        throw new JMSException ("The Map contains objects that cannot be used in a JMS MapMessage!");
      for (final Map.Entry <?, ?> aEntry : aMap.entrySet ())
        aMapMsg.setObject (aEntry.getKey ().toString (), aEntry.getValue ());
      return aMapMsg;
    }

    if (aObject instanceof List <?>)
    {
      final StreamMessage aStreamMsg = aSession.createStreamMessage ();
      final List <?> aList = (List <?>) aObject;
      for (final Object aItem : aList)
      {
        if (!isValidStreamMessageObject (aItem))
          throw new JMSException ("The List contains at least one object that cannot be used in a JMS StreamMessage: " +
                                  aItem);
        aStreamMsg.writeObject (aItem);
      }
      return aStreamMsg;
    }

    if (aObject instanceof InputStream)
    {
      final InputStream aIS = (InputStream) aObject;
      return _createBytesMessage (aSession, aIS);
    }

    if (aObject instanceof IHasInputStream)
    {
      final InputStream aIS = ((IHasInputStream) aObject).getInputStream ();
      if (aIS == null)
        return null;

      try
      {
        return _createBytesMessage (aSession, aIS);
      }
      finally
      {
        StreamHelper.close (aIS);
      }
    }

    if (aObject instanceof byte [])
    {
      final BytesMessage aBytesMsg = aSession.createBytesMessage ();
      aBytesMsg.writeBytes ((byte []) aObject);
      return aBytesMsg;
    }

    if (aObject instanceof Serializable)
    {
      final ObjectMessage aObjectMsg = aSession.createObjectMessage ();
      aObjectMsg.setObject ((Serializable) aObject);
      return aObjectMsg;
    }

    return null;
  }

  /**
   * Get all bytes from the passed {@link BytesMessage}
   *
   * @param aMsg
   *        The message. May not be <code>null</code>.
   * @return A newly created byte array with all bytes of the message. Never
   *         <code>null</code>.
   * @throws JMSException
   *         In case of a JMS error
   */
  @Nonnull
  @ReturnsMutableCopy
  public static byte [] getObjectOfMessage (@Nonnull final BytesMessage aMsg) throws JMSException
  {
    // Puts the message body in read-only mode and repositions the stream of
    // bytes to the beginning.
    aMsg.reset ();

    // Start reading
    final long nResponseBodyLength = aMsg.getBodyLength ();
    if (nResponseBodyLength > Integer.MAX_VALUE)
      throw new JMSException ("BytesMessage is too large: " + nResponseBodyLength + " bytes");

    final byte [] aBytes = new byte [(int) nResponseBodyLength];
    aMsg.readBytes (aBytes);
    return aBytes;
  }

  /**
   * Get a Map from the passed {@link MapMessage}
   *
   * @param aMsg
   *        The message. May not be <code>null</code>.
   * @return A newly created Map with all objects of the message. Never
   *         <code>null</code>.
   * @throws JMSException
   *         In case of a JMS error
   */
  @Nonnull
  @ReturnsMutableCopy
  public static Map <String, Object> getObjectOfMessage (@Nonnull final MapMessage aMsg) throws JMSException
  {
    final Map <String, Object> aResult = new HashMap <String, Object> ();
    for (final Enumeration <?> aEnum = aMsg.getMapNames (); aEnum.hasMoreElements ();)
    {
      final String sName = (String) aEnum.nextElement ();
      final Object aValue = aMsg.getObject (sName);
      aResult.put (sName, aValue);
    }
    return aResult;
  }

  /**
   * Get a object from the passed {@link ObjectMessage}
   *
   * @param aMsg
   *        The message. May not be <code>null</code>.
   * @return The object of the message. May be <code>null</code>.
   * @throws JMSException
   *         In case of a JMS error
   */
  @Nullable
  public static Serializable getObjectOfMessage (@Nonnull final ObjectMessage aMsg) throws JMSException
  {
    return aMsg.getObject ();
  }

  /**
   * Get a List from the passed {@link StreamMessage}
   *
   * @param aMsg
   *        The message. May not be <code>null</code>.
   * @return A newly created List with all objects of the message. Never
   *         <code>null</code>.
   * @throws JMSException
   *         In case of a JMS error
   */
  @Nonnull
  @ReturnsMutableCopy
  public static List <Object> getObjectOfMessage (@Nonnull final StreamMessage aMsg) throws JMSException
  {
    // Puts the message body in read-only mode and repositions the stream of
    // bytes to the beginning.
    aMsg.reset ();

    try
    {
      final List <Object> aResult = new ArrayList <Object> ();
      Object aItem = null;
      while ((aItem = aMsg.readObject ()) != null)
        aResult.add (aItem);
      return aResult;
    }
    catch (final MessageEOFException ex)
    {
      throw ex;
    }
    catch (final Exception ex)
    {
      throw JMSHelper.createException ("Failed to extract information from JMS StreamMessage", ex);
    }
  }

  /**
   * Get a String from the passed {@link TextMessage}
   *
   * @param aMsg
   *        The message. May not be <code>null</code>.
   * @return The text of the message. May be <code>null</code>.
   * @throws JMSException
   *         In case of a JMS error
   */
  @Nullable
  public static String getObjectOfMessage (@Nonnull final TextMessage aMsg) throws JMSException
  {
    return aMsg.getText ();
  }

  /**
   * Try to extract the native object from a JMS {@link Message}. The following
   * object types are handled in this order:
   * <ul>
   * <li>{@link BytesMessage} - byte[]</li>
   * <li>{@link MapMessage} - {@link Map}&lt;String,Object&gt;</li>
   * <li>{@link ObjectMessage} - {@link Serializable}</li>
   * <li>{@link StreamMessage} - {@link List}&lt;Object&gt;</li>
   * <li>{@link TextMessage} - {@link String}</li>
   * </ul>
   *
   * @param aMsg
   *        The JMS message to use. May not be <code>null</code>.
   * @return <code>null</code> if the content of the {@link Message} is
   *         <code>null</code>.
   * @throws JMSException
   *         In case of a JMS error or if the {@link Message} is of an
   *         unsupported type.
   * @see #getObjectOfMessage(BytesMessage)
   * @see #getObjectOfMessage(MapMessage)
   * @see #getObjectOfMessage(ObjectMessage)
   * @see #getObjectOfMessage(StreamMessage)
   * @see #getObjectOfMessage(TextMessage)
   */
  @Nullable
  public static Object getObjectOfMessage (@Nonnull final Message aMsg) throws JMSException
  {
    ValueEnforcer.notNull (aMsg, "Message");

    try
    {
      if (aMsg instanceof BytesMessage)
        return getObjectOfMessage ((BytesMessage) aMsg);

      if (aMsg instanceof MapMessage)
        return getObjectOfMessage ((MapMessage) aMsg);

      if (aMsg instanceof ObjectMessage)
        return getObjectOfMessage ((ObjectMessage) aMsg);

      if (aMsg instanceof StreamMessage)
        return getObjectOfMessage ((StreamMessage) aMsg);

      if (aMsg instanceof TextMessage)
        return getObjectOfMessage ((TextMessage) aMsg);
    }
    catch (final Throwable t)
    {
      throw JMSHelper.createException ("Failed to extract object from JMS message " + aMsg.toString (), t);
    }
    throw new JMSException ("Unsupported JMS message type: " + aMsg.getClass ().getName ());
  }
}
