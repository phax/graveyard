/**
 * Copyright [2011] [Prasad Balan]
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
/*
   Copyright [2011] [Prasad Balan]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.pb.x12;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.hierarchy.IHasChildren;
import com.helger.commons.hierarchy.IHasParent;
import com.helger.commons.string.StringHelper;

/**
 * The {@link Cf} class represents a configuration element. Each {@link Cf}
 * instance represents items required to identify a Loop in a X12 transaction.
 * Some Loops can be identified by only the segment id. Others require segment
 * id and additional qualifiers to be able to identify the Loop. {@link Cf}
 * needs to be used in conjunction with {@link X12Parser}, to be able to parse a
 * X12 transaction into a loop hierarchy. A X12 {@link Cf} can be loaded using
 * many ways: custom code O/X mapping DI or any other way you may find
 * appropriate A Sample 835 hierarchy is shown below. Each row shows a
 * {@link Cf} element, in the format
 *
 * <pre>
 *    (A) - (B) - (C) - (D)
 *    (A) - Loop Name
 *    (B) - Segment id, that identifies the loop
 *    (C) - Segment qualifiers, that are needed to identify the loop. If there are multiple
 *          qualifiers they need to be separated by COMMA.
 *    (D) - Position in the segment where the qualifiers are present
 * </pre>
 *
 * e.g. In X12 835, Loops 1000A and 1000B have the same segment id (N1), to
 * differentiate them we need additional attributes. The N102 (index 1) element
 * has PR for 1000A loop and PE for 1000B loop.
 *
 * <pre>
 * +--X12
 * |  +--ISA - ISA
 * |  |  +--GS - GS
 * |  |  |  +--ST - ST - 835, - 1
 * |  |  |  |  +--1000A - N1 - PR, - 1
 * |  |  |  |  +--1000B - N1 - PE, - 1
 * |  |  |  |  +--2000 - LX
 * |  |  |  |  |  +--2100 - CLP
 * |  |  |  |  |  |  +--2110 - SVC
 * |  |  |  +--SE - SE
 * |  |  +--GE - GE
 * |  +--IEA - IEA
 * </pre>
 *
 * To parse a X12 835 in the above hierarchy, you need to create a Cf object
 * that represent the hierarchy. Here is the sample code to achieve
 *
 * <pre>
 * Cf cfX12 = new Cf("X12"); // root node
 * Cf cfISA = cfX12.addChild("ISA", "ISA"); // add as child of X12
 * Cf cfGS = cfISA.addChild("GS", "GS"); // add as child of ISA
 * Cf cfST = cfGS.addChild("ST", "ST", "835", 1); // add as child of GS
 * cfST.addChild("1000A", "N1", "PR", 1); // add as child of ST
 * cfST.addChild("1000B", "N1", "PE", 1); // add as child of ST
 * Cf cf2000 = cfST.addChild("2000", "LX")
 * Cf cf2100 = cf2000.addChild("2100", "CLP");
 * cf2100.addChild("2110", "SVC");
 * cfISA.addChild("GE", "GE");
 * cfX12.addChild("IEA", "IEA");
 * </pre>
 *
 * Alternate hierarchy for the same transaction. On most occasions a simple
 * hierarchy like below would work. Only when there is more that one loop that
 * is identified by the same segment id and additional qualifiers, you need to
 * put them under the appropriate parent Cf.
 *
 * <pre>
 *  +--X12
 *  |  +--ISA - ISA
 *  |  +--GS - GS
 *  |  +--ST - ST - 835, - 1
 *  |  +--1000A - N1 - PR, - 1
 *  |  +--1000B - N1 - PE, - 1
 *  |  +--2000 - LX
 *  |  +--2100 - CLP
 *  |  +--2110 - SVC
 *  |  +--SE - SE
 *  |  +--GE - GE
 *  |  +--IEA - IEA
 * </pre>
 *
 * @author Prasad Balan
 * @author Philip Helger
 */
public class Cf implements IHasParent <Cf>, IHasChildren <Cf>
{
  private String m_sName;
  private String m_sSegment;
  private String [] m_aSegmentQuals;
  private int m_nSegmentQualPos;
  private int m_nDepth;

  private ICommonsList <Cf> m_aChildren = new CommonsArrayList<> ();
  private Cf m_aParent;

  public Cf (@Nonnull final String name)
  {
    this (name, null);
  }

  public Cf (@Nonnull final String name, @Nullable final String segment)
  {
    this (name, segment, null, -1);
  }

  public Cf (@Nonnull final String name,
             @Nullable final String segment,
             @Nullable final String segmentQual,
             final int segmentQualPos)
  {
    m_sName = name;
    m_sSegment = segment;
    m_aSegmentQuals = StringHelper.getExplodedArray (',', segmentQual);
    m_nSegmentQualPos = segmentQualPos;
  }

  public void addChild (@Nonnull final Cf cf)
  {
    cf.m_nDepth = m_nDepth + 1;
    m_aChildren.add (cf);
    cf.setParent (this);
  }

  @Nonnull
  public Cf addChild (@Nonnull final String name, @Nullable final String segment)
  {
    final Cf cf = new Cf (name, segment);
    addChild (cf);
    return cf;
  }

  @Nonnull
  public Cf addChild (@Nonnull final String name,
                      @Nullable final String segment,
                      @Nullable final String segmentQual,
                      final int segmentQualPos)
  {
    final Cf cf = new Cf (name, segment, segmentQual, segmentQualPos);
    addChild (cf);
    return cf;
  }

  @Nonnull
  public ICommonsList <Cf> getAllChildren ()
  {
    return m_aChildren.getClone ();
  }

  @Nonnull
  public Iterable <Cf> getChildrenIter ()
  {
    return m_aChildren;
  }

  public int getChildCount ()
  {
    return m_aChildren.size ();
  }

  public boolean hasChildren ()
  {
    return m_aChildren.isNotEmpty ();
  }

  void setChildren (@Nonnull final ICommonsList <Cf> cfList)
  {
    m_aChildren = cfList;
    for (final Cf cf : cfList)
    {
      cf.m_nDepth = m_nDepth + 1;
      cf.setParent (this);
    }
  }

  public boolean hasParent ()
  {
    return m_aParent != null;
  }

  @Nullable
  public Cf getParent ()
  {
    return m_aParent;
  }

  void setParent (final Cf cf)
  {
    m_aParent = cf;
  }

  @Nonnull
  public String getName ()
  {
    return m_sName;
  }

  @Nullable
  public String getSegment ()
  {
    return m_sSegment;
  }

  @Nullable
  public String [] getSegmentQuals ()
  {
    return m_aSegmentQuals;
  }

  public int getSegmentQualPos ()
  {
    return m_nSegmentQualPos;
  }

  public void setName (@Nonnull final String name)
  {
    m_sName = name;
  }

  public void setSegment (@Nullable final String segment)
  {
    m_sSegment = segment;
  }

  public void setSegmentQuals (@Nullable final String [] segmentQuals)
  {
    m_aSegmentQuals = segmentQuals;
  }

  public void setSegmentQualPos (final int segmentQualPos)
  {
    m_nSegmentQualPos = segmentQualPos;
  }

  @Nonnull
  public String getAsString ()
  {
    final StringBuilder aSB = new StringBuilder ();
    for (int i = 0; i < m_nDepth; i++)
      aSB.append ("|  ");
    aSB.append ("+--").append (m_sName);
    if (m_sSegment != null)
      aSB.append (" - ").append (m_sSegment);
    if (m_aSegmentQuals != null)
    {
      aSB.append (" - ");
      for (final String s : m_aSegmentQuals)
        aSB.append (s).append (',');
    }
    if (m_nSegmentQualPos >= 0)
      aSB.append (" - ").append (m_nSegmentQualPos);

    aSB.append (System.getProperty ("line.separator"));
    for (final Cf cf : m_aChildren)
      aSB.append (cf.getAsString ());

    return aSB.toString ();
  }
}
