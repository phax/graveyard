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
package com.helger.zeromq.socket;

import java.io.Closeable;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.charset.Charset;

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.ArrayHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.zeromq.error.IZMQErrorHandler;
import com.helger.zeromq.error.LoggingZMQErrorHandler;

import zmq.DecoderBase;
import zmq.EncoderBase;

/**
 * Wrapper around a {@link org.zeromq.ZMQ.Socket} for simpler error handling.
 *
 * @author Philip Helger
 */
public class ZMQSocket implements Closeable
{
  public static final Charset DEFAULT_CHARSET = ZMQ.CHARSET;

  private IZMQErrorHandler m_aErrorHandler = new LoggingZMQErrorHandler ();
  private final ESocketType m_eType;
  private final ZMQ.Socket m_aSocket;
  private boolean m_bClosed = false;

  public ZMQSocket (@Nonnull final ESocketType eType, @Nonnull final ZMQ.Socket aSocket)
  {
    m_eType = ValueEnforcer.notNull (eType, "SocketType");
    m_aSocket = ValueEnforcer.notNull (aSocket, "ZMQ.Socket");
  }

  /**
   * @return The existing error handler. Never <code>null</code>.
   */
  @Nonnull
  public IZMQErrorHandler getErrorHandler ()
  {
    return m_aErrorHandler;
  }

  /**
   * Set a different error handler.
   *
   * @param aErrorHandler
   *        The new error handler. May not be <code>null</code>.
   * @return this for chaining
   */
  @Nonnull
  public ZMQSocket setErrorHandler (@Nonnull final IZMQErrorHandler aErrorHandler)
  {
    m_aErrorHandler = ValueEnforcer.notNull (aErrorHandler, "ErrorHandler");
    return this;
  }

  /**
   * @return The socket type as passed in the constructor. Never
   *         <code>null</code>.
   */
  @Nonnull
  public ESocketType getSocketType ()
  {
    return m_eType;
  }

  /**
   * @return The underlying ZMQ socket as passed in the constructor. Never
   *         <code>null</code>.
   */
  @Nonnull
  public ZMQ.Socket getSocket ()
  {
    return m_aSocket;
  }

  public void close ()
  {
    if (!m_bClosed)
    {
      m_aSocket.close ();
      m_bClosed = true;
    }
  }

  public boolean isClosed ()
  {
    return m_bClosed;
  }

  private void _handleException (@Nonnull @Nonempty final String sOperation, @Nonnull final ZMQException ex)
  {
    m_aErrorHandler.handleException (this, sOperation, ex);
  }

  /**
   * @return the linger period.
   * @see #setLinger(long)
   */
  public long getLinger ()
  {
    return m_aSocket.getLinger ();
  }

  /**
   * The 'ZMQ_LINGER' option shall retrieve the period for pending outbound
   * messages to linger in memory after closing the socket. Value of -1 means
   * infinite. Pending messages will be kept until they are fully transferred to
   * the peer. Value of 0 means that all the pending messages are dropped
   * immediately when socket is closed. Positive value means number of
   * milliseconds to keep trying to send the pending messages before discarding
   * them.
   *
   * @param nLinger
   *        the linger period in milliseconds.
   */
  public void setLinger (final long nLinger)
  {
    m_aSocket.setLinger (nLinger);
  }

  /**
   * @return the reconnectIVL.
   * @see #setReconnectIVL(long)
   */
  public long getReconnectIVL ()
  {
    return m_aSocket.getReconnectIVL ();
  }

  /**
   * Set reconnectIVL
   *
   * @param value
   *        value
   */
  public void setReconnectIVL (final long value)
  {
    m_aSocket.setReconnectIVL (value);
  }

  /**
   * @return the backlog.
   * @see #setBacklog(long)
   */
  public long getBacklog ()
  {
    return m_aSocket.getBacklog ();
  }

  public void setBacklog (final long value)
  {
    m_aSocket.setBacklog (value);
  }

  /**
   * @return the reconnectIVLMax.
   * @see #setReconnectIVLMax(long)
   */
  public long getReconnectIVLMax ()
  {
    return m_aSocket.getReconnectIVLMax ();
  }

  public void setReconnectIVLMax (final long value)
  {
    m_aSocket.setReconnectIVLMax (value);
  }

  /**
   * @return the maxMsgSize.
   * @see #setMaxMsgSize(long)
   */
  public long getMaxMsgSize ()
  {
    return m_aSocket.getMaxMsgSize ();
  }

  public void setMaxMsgSize (final long value)
  {
    m_aSocket.setMaxMsgSize (value);
  }

  /**
   * @return the SndHWM.
   * @see #setSndHWM(long)
   */
  public long getSndHWM ()
  {
    return m_aSocket.getSndHWM ();
  }

  /**
   * Set send high water mark
   *
   * @param nHWM
   *        High water mark
   */
  public void setSndHWM (final long nHWM)
  {
    m_aSocket.setSndHWM (nHWM);
  }

  /**
   * @return the recvHWM period.
   * @see #setRcvHWM(long)
   */
  public long getRcvHWM ()
  {
    return m_aSocket.getRcvHWM ();
  }

  /**
   * Set receive high water mark
   *
   * @param nHWM
   *        High water mark
   */
  public void setRcvHWM (final long nHWM)
  {
    m_aSocket.setRcvHWM (nHWM);
  }

  /**
   * The 'ZMQ_HWM' option shall set the high water mark for the specified
   * 'socket'. The high water mark is a hard limit on the maximum number of
   * outstanding messages 0MQ shall queue in memory for any single peer that the
   * specified 'socket' is communicating with. If this limit has been reached
   * the socket shall enter an exceptional state and depending on the socket
   * type, 0MQ shall take appropriate action such as blocking or dropping sent
   * messages. Refer to the individual socket descriptions in the man page of
   * zmq_socket[3] for details on the exact action taken for each socket type.
   *
   * @param hwm
   *        the number of messages to queue.
   */
  public void setHWM (final long hwm)
  {
    setSndHWM (hwm);
    setRcvHWM (hwm);
  }

  /**
   * @see #setAffinity(long)
   * @return the affinity.
   */
  public long getAffinity ()
  {
    return m_aSocket.getAffinity ();
  }

  /**
   * Get the Affinity. The 'ZMQ_AFFINITY' option shall set the I/O thread
   * affinity for newly created connections on the specified 'socket'. Affinity
   * determines which threads from the 0MQ I/O thread pool associated with the
   * socket's _context_ shall handle newly created connections. A value of zero
   * specifies no affinity, meaning that work shall be distributed fairly among
   * all 0MQ I/O threads in the thread pool. For non-zero values, the lowest bit
   * corresponds to thread 1, second lowest bit to thread 2 and so on. For
   * example, a value of 3 specifies that subsequent connections on 'socket'
   * shall be handled exclusively by I/O threads 1 and 2. See also in the man
   * page of init[3] for details on allocating the number of I/O threads for a
   * specific _context_.
   *
   * @param value
   *        the io_thread affinity.
   */
  public void setAffinity (final long value)
  {
    m_aSocket.setAffinity (value);
  }

  /**
   * @see #setIdentity(byte[])
   * @return the Identity.
   */
  public byte [] getIdentity ()
  {
    return m_aSocket.getIdentity ();
  }

  /**
   * The 'ZMQ_IDENTITY' option shall set the identity of the specified 'socket'.
   * Socket identity determines if existing 0MQ infrastructure (_message
   * queues_, _forwarding devices_) shall be identified with a specific
   * application and persist across multiple runs of the application. If the
   * socket has no identity, each run of an application is completely separate
   * from other runs. However, with identity set the socket shall re-use any
   * existing 0MQ infrastructure configured by the previous run(s). Thus the
   * application may receive messages that were sent in the meantime, _message
   * queue_ limits shall be shared with previous run(s) and so on. Identity
   * should be at least one byte and at most 255 bytes long. Identities starting
   * with binary zero are reserved for use by 0MQ infrastructure.
   *
   * @param identity
   *        identity
   */
  public void setIdentity (final byte [] identity)
  {
    m_aSocket.setIdentity (identity);
  }

  /**
   * @see #setReceiveTimeOut(int)
   * @return the Receive Timeout in milliseconds.
   */
  public int getReceiveTimeOut ()
  {
    return m_aSocket.getReceiveTimeOut ();
  }

  /**
   * Sets the timeout for receive operation on the socket. If the value is 0,
   * recv will return immediately, with null if there is no message to receive.
   * If the value is -1, it will block until a message is available. For all
   * other values, it will wait for a message for that amount of time before
   * returning with a null and an EAGAIN error.
   *
   * @param value
   *        Timeout for receive operation in milliseconds. Default -1 (infinite)
   */
  public void setReceiveTimeOut (final int value)
  {
    m_aSocket.setReceiveTimeOut (value);
  }

  /**
   * @see #setSendTimeOut(int)
   * @return the Send Timeout in milliseconds.
   */
  public int getSendTimeOut ()
  {
    return m_aSocket.getSendTimeOut ();
  }

  /**
   * Sets the timeout for send operation on the socket. If the value is 0, send
   * will return immediately, with a false if the message cannot be sent. If the
   * value is -1, it will block until the message is sent. For all other values,
   * it will try to send the message for that amount of time before returning
   * with false and an EAGAIN error.
   *
   * @param value
   *        Timeout for send operation in milliseconds. Default -1 (infinite)
   */
  public void setSendTimeOut (final int value)
  {
    m_aSocket.setSendTimeOut (value);
  }

  /**
   * Override SO_KEEPALIVE socket option (where supported by OS) to enable
   * keep-alive packets for a socket connection. Possible values are -1, 0, 1.
   * The default value -1 will skip all overrides and do the OS default.
   *
   * @param value
   *        The value of 'ZMQ_TCP_KEEPALIVE' to turn TCP keepalives on (1) or
   *        off (0).
   */
  public void setTCPKeepAlive (final long value)
  {
    m_aSocket.setTCPKeepAlive (value);
  }

  /**
   * @see #setTCPKeepAlive(long)
   * @return the keep alive setting.
   */
  public long getTCPKeepAliveSetting ()
  {
    return m_aSocket.getTCPKeepAliveSetting ();
  }

  /**
   * Override TCP_KEEPCNT socket option (where supported by OS). The default
   * value -1 will skip all overrides and do the OS default.
   *
   * @param value
   *        The value of 'ZMQ_TCP_KEEPALIVE_CNT' defines the number of
   *        keepalives before death.
   */
  public void setTCPKeepAliveCount (final long value)
  {
    m_aSocket.setTCPKeepAliveCount (value);
  }

  /**
   * @see #setTCPKeepAliveCount(long)
   * @return the keep alive count.
   */
  public long getTCPKeepAliveCount ()
  {
    return m_aSocket.getTCPKeepAliveCount ();
  }

  /**
   * Override TCP_KEEPINTVL socket option (where supported by OS). The default
   * value -1 will skip all overrides and do the OS default.
   *
   * @param value
   *        The value of 'ZMQ_TCP_KEEPALIVE_INTVL' defines the interval between
   *        keepalives. Unit is OS dependant.
   */
  public void setTCPKeepAliveInterval (final long value)
  {
    m_aSocket.setTCPKeepAliveInterval (value);
  }

  /**
   * @see #setTCPKeepAliveInterval(long)
   * @return the keep alive interval.
   */
  public long getTCPKeepAliveInterval ()
  {
    return m_aSocket.getTCPKeepAliveInterval ();
  }

  /**
   * Override TCP_KEEPCNT (or TCP_KEEPALIVE on some OS) socket option (where
   * supported by OS). The default value -1 will skip all overrides and do the
   * OS default.
   *
   * @param value
   *        The value of 'ZMQ_TCP_KEEPALIVE_IDLE' defines the interval between
   *        the last data packet sent over the socket and the first keepalive
   *        probe. Unit is OS dependant.
   */
  public void setTCPKeepAliveIdle (final long value)
  {
    m_aSocket.setTCPKeepAliveIdle (value);
  }

  /**
   * @see #setTCPKeepAliveIdle(long)
   * @return the keep alive idle value.
   */
  public long getTCPKeepAliveIdle ()
  {
    return m_aSocket.getTCPKeepAliveIdle ();
  }

  /**
   * @see #setSendBufferSize(long)
   * @return the kernel send buffer size.
   */
  public long getSendBufferSize ()
  {
    return m_aSocket.getSendBufferSize ();
  }

  /**
   * The 'ZMQ_SNDBUF' option shall set the underlying kernel transmit buffer
   * size for the 'socket' to the specified size in bytes. A value of zero means
   * leave the OS default unchanged. For details please refer to your operating
   * system documentation for the 'SO_SNDBUF' socket option.
   *
   * @param value
   *        underlying kernel transmit buffer size for the 'socket' in bytes A
   *        value of zero means leave the OS default unchanged.
   */
  public void setSendBufferSize (final long value)
  {
    m_aSocket.setSendBufferSize (value);
  }

  /**
   * @see #setReceiveBufferSize(long)
   * @return the kernel receive buffer size.
   */
  public long getReceiveBufferSize ()
  {
    return m_aSocket.getReceiveBufferSize ();
  }

  /**
   * The 'ZMQ_RCVBUF' option shall set the underlying kernel receive buffer size
   * for the 'socket' to the specified size in bytes. For details refer to your
   * operating system documentation for the 'SO_RCVBUF' socket option.
   *
   * @param value
   *        Underlying kernel receive buffer size for the 'socket' in bytes. A
   *        value of zero means leave the OS default unchanged.
   */
  public void setReceiveBufferSize (final long value)
  {
    m_aSocket.setReceiveBufferSize (value);
  }

  /**
   * The 'ZMQ_RCVMORE' option shall return a boolean value indicating if the
   * multi-part message currently being read from the specified 'socket' has
   * more message parts to follow. If there are no message parts to follow or if
   * the message currently being read is not a multi-part message a value of
   * zero shall be returned. Otherwise, a value of 1 shall be returned.
   *
   * @return true if there are more messages to receive.
   */
  public boolean hasReceiveMore ()
  {
    return m_aSocket.hasReceiveMore ();
  }

  /**
   * The 'ZMQ_FD' option shall retrieve file descriptor associated with the 0MQ
   * socket. The descriptor can be used to integrate 0MQ socket into an existing
   * event loop. It should never be used for anything else than polling -- such
   * as reading or writing. The descriptor signals edge-triggered IN event when
   * something has happened within the 0MQ socket. It does not necessarily mean
   * that the messages can be read or written. Check ZMQ_EVENTS option to find
   * out whether the 0MQ socket is readable or writeable.
   *
   * @return the underlying file descriptor.
   */
  public SelectableChannel getFD ()
  {
    return m_aSocket.getFD ();
  }

  /**
   * The 'ZMQ_EVENTS' option shall retrieve event flags for the specified
   * socket. If a message can be read from the socket ZMQ_POLLIN flag is set. If
   * message can be written to the socket ZMQ_POLLOUT flag is set.
   *
   * @return the mask of outstanding events.
   */
  public int getEvents ()
  {
    return m_aSocket.getEvents ();
  }

  /**
   * The 'ZMQ_SUBSCRIBE' option shall establish a new message filter on a
   * 'ZMQ_SUB' socket. Newly created 'ZMQ_SUB' sockets shall filter out all
   * incoming messages, therefore you should call this option to establish an
   * initial message filter. An empty 'option_value' of length zero shall
   * subscribe to all incoming messages. A non-empty 'option_value' shall
   * subscribe to all messages beginning with the specified prefix. Mutiple
   * filters may be attached to a single 'ZMQ_SUB' socket, in which case a
   * message shall be accepted if it matches at least one filter.
   *
   * @param topic
   *        topic
   */
  public void subscribe (final byte [] topic)
  {
    m_aSocket.subscribe (topic);
  }

  /**
   * Subscribe to all messages.
   * 
   * @see #subscribe(byte[])
   */
  public void subscribeToAll ()
  {
    subscribe (ArrayHelper.EMPTY_BYTE_ARRAY);
  }

  /**
   * The 'ZMQ_UNSUBSCRIBE' option shall remove an existing message filter on a
   * 'ZMQ_SUB' socket. The filter specified must match an existing filter
   * previously established with the 'ZMQ_SUBSCRIBE' option. If the socket has
   * several instances of the same filter attached the 'ZMQ_UNSUBSCRIBE' option
   * shall remove only one instance, leaving the rest in place and functional.
   *
   * @param topic
   *        topic
   */
  public void unsubscribe (final byte [] topic)
  {
    m_aSocket.unsubscribe (topic);
  }

  /**
   * Set custom Encoder
   *
   * @param cls
   *        class
   */
  public void setEncoder (final Class <? extends EncoderBase> cls)
  {
    m_aSocket.setEncoder (cls);
  }

  /**
   * Set custom Decoder
   *
   * @param cls
   *        class
   */
  public void setDecoder (final Class <? extends DecoderBase> cls)
  {
    m_aSocket.setDecoder (cls);
  }

  /**
   * Sets the ROUTER socket behavior when an unroutable message is encountered.
   *
   * @param mandatory
   *        A value of false is the default and discards the message silently
   *        when it cannot be routed. A value of true returns an EHOSTUNREACH
   *        error code if the message cannot be routed.
   */
  public void setRouterMandatory (final boolean mandatory)
  {
    m_aSocket.setRouterMandatory (mandatory);
  }

  /**
   * If two clients use the same identity when connecting to a ROUTER, the
   * results shall depend on the ZMQ_ROUTER_HANDOVER option setting
   *
   * @param handover
   *        A value of false, (default) the ROUTER socket shall reject clients
   *        trying to connect with an already-used identity A value of true, the
   *        ROUTER socket shall hand-over the connection to the new client and
   *        disconnect the existing one
   */
  public void setRouterHandover (final boolean handover)
  {
    m_aSocket.setRouterHandover (handover);
  }

  /**
   * Sets the XPUB socket behavior on new subscriptions and unsubscriptions.
   *
   * @param verbose
   *        A value of false is the default and passes only new subscription
   *        messages to upstream. A value of true passes all subscription
   *        messages upstream.
   */
  public void setXpubVerbose (final boolean verbose)
  {
    m_aSocket.setXpubVerbose (verbose);
  }

  /**
   * @see #setIPv4Only (boolean)
   * @return the IPV4ONLY
   */
  public boolean getIPv4Only ()
  {
    return m_aSocket.getIPv4Only ();
  }

  /**
   * The 'ZMQ_IPV4ONLY' option shall set the underlying native socket type. An
   * IPv6 socket lets applications connect to and accept connections from both
   * IPv4 and IPv6 hosts.
   *
   * @param v4only
   *        A value of true will use IPv4 sockets, while the value of false will
   *        use IPv6 sockets
   */
  public void setIPv4Only (final boolean v4only)
  {
    m_aSocket.setIPv4Only (v4only);
  }

  /**
   * @see #setTCPKeepAlive(int)
   * @return the keep alive setting.
   */
  public int getTCPKeepAlive ()
  {
    return m_aSocket.getTCPKeepAlive ();
  }

  /**
   * Override SO_KEEPALIVE socket option (where supported by OS) to enable
   * keep-alive packets for a socket connection. Possible values are -1, 0, 1.
   * The default value -1 will skip all overrides and do the OS default.
   *
   * @param optVal
   *        The value of 'ZMQ_TCP_KEEPALIVE' to turn TCP keepalives on (1) or
   *        off (0).
   */
  public void setTCPKeepAlive (final int optVal)
  {
    m_aSocket.setTCPKeepAlive (optVal);
  }

  /**
   * @see #setDelayAttachOnConnect(boolean)
   * @return the keep alive setting.
   */
  public boolean getDelayAttachOnConnect ()
  {
    return m_aSocket.getDelayAttachOnConnect ();
  }

  /**
   * Accept messages only when connections are made If set to true, will delay
   * the attachment of a pipe on connect until the underlying connection has
   * completed. This will cause the socket to block if there are no other
   * connections, but will prevent queues from filling on pipes awaiting
   * connection
   *
   * @param value
   *        The value of 'ZMQ_DELAY_ATTACH_ON_CONNECT'. Default false.
   */
  public void setDelayAttachOnConnect (final boolean value)
  {
    m_aSocket.setDelayAttachOnConnect (value);
  }

  /**
   * Bind to network interface. Start listening for new connections.
   *
   * @param sAddr
   *        the endpoint to bind to.
   */
  public void bind (@Nonnull final String sAddr)
  {
    try
    {
      m_aSocket.bind (sAddr);
    }
    catch (final ZMQException ex)
    {
      _handleException ("bind(" + sAddr + ")", ex);
      throw ex;
    }
  }

  /**
   * Bind to network interface to a random port. Start listening for new
   * connections.
   *
   * @param sAddr
   *        the endpoint to bind to.
   * @return The port bound to
   */
  public int bindToRandomPort (final String sAddr)
  {
    try
    {
      return m_aSocket.bindToRandomPort (sAddr);
    }
    catch (final ZMQException ex)
    {
      _handleException ("bindToRandomPort(" + sAddr + ")", ex);
      throw ex;
    }
  }

  /**
   * Bind to network interface to a random port. Start listening for new
   * connections.
   *
   * @param sAddr
   *        the endpoint to bind to.
   * @param nMin
   *        The minimum port in the range of ports to try.
   * @param nMax
   *        The maximum port in the range of ports to try.
   * @return The port bound to
   */
  public int bindToRandomPort (final String sAddr, final int nMin, final int nMax)
  {
    try
    {
      return m_aSocket.bindToRandomPort (sAddr, nMin, nMax);
    }
    catch (final ZMQException ex)
    {
      _handleException ("bindToRandomPort(" + sAddr + "," + nMin + "," + nMax + ")", ex);
      throw ex;
    }
  }

  /**
   * Connect to remote application.
   *
   * @param sAddr
   *        the endpoint to connect to.
   */
  public void connect (@Nonnull final String sAddr)
  {
    try
    {
      m_aSocket.connect (sAddr);
    }
    catch (final ZMQException ex)
    {
      _handleException ("connect(" + sAddr + ")", ex);
      throw ex;
    }
  }

  /**
   * Stop accepting connections on a socket.
   *
   * @param sAddr
   *        the endpoint to unbind from.
   * @return <code>true</code> if successful.
   */
  public boolean unbind (final String sAddr)
  {
    return m_aSocket.unbind (sAddr);
  }

  public boolean send (final String data)
  {
    try
    {
      return m_aSocket.send (data);
    }
    catch (final ZMQException ex)
    {
      _handleException ("send(" + data + " bytes)", ex);
      throw ex;
    }
  }

  public boolean sendMore (final String data)
  {
    try
    {
      return m_aSocket.sendMore (data);
    }
    catch (final ZMQException ex)
    {
      _handleException ("sendMore(" + data + " bytes)", ex);
      throw ex;
    }
  }

  public boolean send (final String data, final int flags)
  {
    try
    {
      return m_aSocket.send (data, flags);
    }
    catch (final ZMQException ex)
    {
      _handleException ("send(" + data + " bytes, " + flags + ")", ex);
      throw ex;
    }
  }

  public boolean send (final byte [] data)
  {
    try
    {
      return m_aSocket.send (data);
    }
    catch (final ZMQException ex)
    {
      _handleException ("send(" + ArrayHelper.getSize (data) + " bytes)", ex);
      throw ex;
    }
  }

  public boolean sendMore (final byte [] data)
  {
    try
    {
      return m_aSocket.sendMore (data);
    }
    catch (final ZMQException ex)
    {
      _handleException ("sendMore(" + ArrayHelper.getSize (data) + " bytes)", ex);
      throw ex;
    }
  }

  public boolean send (final byte [] data, final int flags)
  {
    try
    {
      return m_aSocket.send (data, flags);
    }
    catch (final ZMQException ex)
    {
      _handleException ("send(" + ArrayHelper.getSize (data) + " bytes, " + flags + ")", ex);
      throw ex;
    }
  }

  public boolean send (final byte [] data, final int off, final int length, final int flags)
  {
    try
    {
      return m_aSocket.send (data, off, length, flags);
    }
    catch (final ZMQException ex)
    {
      _handleException ("send(" +
                        ArrayHelper.getSize (data) +
                        " bytes, " +
                        off +
                        ", " +
                        length +
                        ", " +
                        flags +
                        ")",
                        ex);
      throw ex;
    }
  }

  /**
   * Send a message
   *
   * @param data
   *        ByteBuffer payload
   * @param flags
   *        the flags to apply to the send operation
   * @return the number of bytes sent, -1 on error
   */
  public int sendByteBuffer (final ByteBuffer data, final int flags)
  {
    try
    {
      return m_aSocket.sendByteBuffer (data, flags);
    }
    catch (final ZMQException ex)
    {
      _handleException ("sendByteBuffer(" + (data == null ? 0 : data.remaining ()) + " bytes, " + flags + ")", ex);
      throw ex;
    }
  }

  /**
   * Receive a message.
   *
   * @return the message received, as an array of bytes; null on error.
   */
  @Nullable
  public byte [] recv ()
  {
    try
    {
      return m_aSocket.recv ();
    }
    catch (final ZMQException ex)
    {
      _handleException ("recv()", ex);
      throw ex;
    }
  }

  /**
   * Receive a message.
   *
   * @param flags
   *        the flags to apply to the receive operation.
   * @return the message received, as an array of bytes; null on error.
   */
  @Nullable
  public byte [] recv (final int flags)
  {
    try
    {
      return m_aSocket.recv (flags);
    }
    catch (final ZMQException ex)
    {
      _handleException ("recv(" + flags + ")", ex);
      throw ex;
    }
  }

  /**
   * Receive a message in to a specified buffer.
   *
   * @param buffer
   *        byte[] to copy zmq message payload in to.
   * @param offset
   *        offset in buffer to write data
   * @param len
   *        max bytes to write to buffer. If len is smaller than the incoming
   *        message size, the message will be truncated.
   * @param flags
   *        the flags to apply to the receive operation.
   * @return the number of bytes read, -1 on error
   */
  @CheckForSigned
  public int recv (@Nonnull final byte [] buffer, final int offset, final int len, final int flags)
  {
    try
    {
      return m_aSocket.recv (buffer, offset, len, flags);
    }
    catch (final ZMQException ex)
    {
      _handleException ("recv(" + ArrayHelper.getSize (buffer) + ", " + offset + ", " + len + ", " + flags + ")", ex);
      throw ex;
    }
  }

  /**
   * Receive a message into the specified ByteBuffer
   *
   * @param buffer
   *        the buffer to copy the zmq message payload into
   * @param flags
   *        the flags to apply to the receive operation
   * @return the number of bytes read, -1 on error
   */
  @CheckForSigned
  public int recvByteBuffer (@Nonnull final ByteBuffer buffer, final int flags)
  {
    try
    {
      return m_aSocket.recvByteBuffer (buffer, flags);
    }
    catch (final ZMQException ex)
    {
      _handleException ("recvByteBuffer(" + buffer.limit () + " bytes, " + flags + ")", ex);
      throw ex;
    }
  }

  /**
   * @return the message received, as a String object; null on no message.
   */
  @Nullable
  public String recvStr ()
  {
    try
    {
      return m_aSocket.recvStr ();
    }
    catch (final ZMQException ex)
    {
      _handleException ("recvStr()", ex);
      throw ex;
    }
  }

  /**
   * @param flags
   *        the flags to apply to the receive operation.
   * @return the message received, as a String object; null on no message.
   */
  @Nullable
  public String recvStr (final int flags)
  {
    try
    {
      return m_aSocket.recvStr (flags);
    }
    catch (final ZMQException ex)
    {
      _handleException ("recvStr(" + flags + ")", ex);
      throw ex;
    }
  }

  /**
   * Start a monitoring socket where events can be received.
   *
   * @param sAddr
   *        the endpoint to receive events from. (must be inproc transport)
   * @param events
   *        the events of interest.
   * @return true if monitor socket setup is successful
   */
  public boolean monitor (final String sAddr, final int events)
  {
    return m_aSocket.monitor (sAddr, events);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("Type", m_eType).append ("ZMQ.Socket", m_aSocket).toString ();
  }

  @Nonnull
  public static ZMQSocket create (@Nonnull final ZContext aContext, @Nonnull final ESocketType eType)
  {
    ValueEnforcer.notNull (aContext, "Context");
    ValueEnforcer.notNull (eType, "SocketType");

    // Create
    final ZMQ.Socket aSocket = aContext.createSocket (eType.getType ());

    // Wrap
    return new ZMQSocket (eType, aSocket);
  }
}
