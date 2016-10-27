/**
 * Copyright 2012 A. Nonymous
 * Copyright (C) 2016 Philip Helger (www.helger.com)
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
package org.spunk.edifact.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;

/**
 * Streams.
 */
public final class Streams
{

  private static final int KILOBYTE = 1024;
  private static final int COPY_BUFFER_SIZE_BYTES = 64 * Streams.KILOBYTE;

  /**
   * Hidden constructor.
   */
  private Streams ()
  {
    // empty constructor
  }

  /**
   * Fully read a stream into a byte array.
   *
   * @param inputStream
   *        Stream to buffer into a byte array.
   * @return Contents of stream.
   * @throws IOException
   *         Failed to copy.
   */
  public static byte [] copy (final InputStream inputStream) throws IOException
  {
    byte [] bytes = null;

    if (inputStream != null)
    {
      try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream ())
      {
        Streams.copy (inputStream, outputStream);
        bytes = outputStream.toByteArray ();
      }
    }

    return bytes;
  }

  /**
   * Copy input stream to output stream. Streams are closed after the copy.
   *
   * @param inputStream
   *        Stream to copy from starting from the current offset.
   * @param outputStream
   *        Stream to copy.
   * @throws IOException
   *         Failed to copy.
   */
  public static void copy (final InputStream inputStream, final OutputStream outputStream) throws IOException
  {
    try (ReadableByteChannel input = Channels.newChannel (inputStream))
    {
      copy (outputStream, input);
    }
  }

  /**
   * Buffer the content of a stream into string. The input stream is guaranteed
   * to have close invoked on them if the method returns without throwing an
   * exception.
   *
   * @param inputStream
   *        Stream to buffer into a byte array converted to a utf-8 string.
   * @return Stream contents starting from the current offset.
   * @throws IOException
   *         Failed to copy.
   */
  public static String toString (final InputStream inputStream) throws IOException
  {
    final byte [] bytes = Streams.copy (inputStream);
    return new String (bytes, StandardCharsets.UTF_8);
  }

  /**
   * Buffer the content of a resource into string.
   *
   * @param resourceName
   *        Resource stream to buffer into a byte array converted to a utf-8
   *        string.
   * @return Stream contents starting from the current offset, <tt>null</tt> if
   *         the resource or the stream for it is <tt>null</tt>.
   * @throws IOException
   *         Failed to copy.
   */
  public static String toString (final String resourceName) throws IOException
  {
    String value;

    try (InputStream stream = Streams.class.getResourceAsStream (resourceName))
    {
      value = Streams.toString (stream);
    }

    return value;
  }

  private static void copy (final OutputStream outputStream, final ReadableByteChannel input) throws IOException
  {
    try (WritableByteChannel output = Channels.newChannel (outputStream))
    {
      _doCopy (input, output);
      outputStream.flush ();
    }
  }

  private static void _doCopy (final ReadableByteChannel input, final WritableByteChannel output) throws IOException
  {
    final ByteBuffer buffer = ByteBuffer.allocate (Streams.COPY_BUFFER_SIZE_BYTES);

    while (input.read (buffer) != -1)
    {
      buffer.flip ();

      while (buffer.hasRemaining ())
      {
        output.write (buffer);
      }

      buffer.clear ();
    }
  }

}
