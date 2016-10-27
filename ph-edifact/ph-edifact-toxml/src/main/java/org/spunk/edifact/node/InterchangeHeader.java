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
package org.spunk.edifact.node;

import java.io.Serializable;

import org.spunk.edifact.Edifact;
import org.spunk.edifact.exception.InterchangeHeaderException;

/**
 * InterchangeHeader.
 */
public class InterchangeHeader implements Serializable
{

  private static final long serialVersionUID = 7440786450660936889L;

  private String sender;
  private String receiver;
  private String date;
  private String time;

  /**
   * Constructor.
   */
  public InterchangeHeader ()
  {
    // empty constructor
  }

  /**
   * Constructor.
   * 
   * @param segment
   *        UNB segment.
   */
  public InterchangeHeader (final Segment segment)
  {
    String segmentName;

    try
    {
      segmentName = segment.getSegmentName ();
    }
    catch (final NullPointerException cause)
    {
      throw new InterchangeHeaderException (cause);
    }

    if (!SegmentName.UNB.matches (segmentName))
    {
      throw new InterchangeHeaderException (Edifact.toEdifact (segment));
    }

    this.sender = segment.get (1, 0);
    this.receiver = segment.get (2, 0);
    this.date = segment.get (3, 0);
    this.time = segment.get (3, 1);
  }

  /**
   * Getter.
   * 
   * @return sender.
   */
  public String getSender ()
  {
    return this.sender;
  }

  /**
   * Setter.
   * 
   * @param value
   *        New value.
   */
  public void setSender (final String value)
  {
    this.sender = value;
  }

  /**
   * Getter.
   * 
   * @return receiver.
   */
  public String getReceiver ()
  {
    return this.receiver;
  }

  /**
   * Setter.
   * 
   * @param value
   *        New value.
   */
  public void setReceiver (final String value)
  {
    this.receiver = value;
  }

  /**
   * Getter.
   * 
   * @return date.
   */
  public String getDate ()
  {
    return this.date;
  }

  /**
   * Setter.
   * 
   * @param value
   *        New value.
   */
  public void setDate (final String value)
  {
    this.date = value;
  }

  /**
   * Getter.
   * 
   * @return time.
   */
  public String getTime ()
  {
    return this.time;
  }

  /**
   * Setter.
   * 
   * @param value
   *        New value.
   */
  public void setTime (final String value)
  {
    this.time = value;
  }
}
