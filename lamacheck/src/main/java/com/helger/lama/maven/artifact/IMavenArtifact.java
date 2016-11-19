/**
 * Copyright (C) 2011-2014 Philip Helger <philip[at]helger[dot]com>
 * All Rights Reserved
 *
 * This file is part of the LaMaCheck service.
 *
 * Proprietary and confidential.
 *
 * It can not be copied and/or distributed without the
 * express permission of Philip Helger.
 *
 * Unauthorized copying of this file, via any medium is
 * strictly prohibited.
 */
package com.helger.lama.maven.artifact;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.MustImplementEqualsAndHashcode;
import com.helger.commons.compare.CompareHelper;
import com.helger.commons.compare.IComparator;

/**
 * Interface for a single artifact. It it is not restricted to dependencies but
 * can also be used in other cases.
 *
 * @author Philip Helger
 */
@MustImplementEqualsAndHashcode
public interface IMavenArtifact
{
  @Nullable
  String getGroupID ();

  @Nonnull
  String getArtifactID ();

  @Nullable
  String getVersion ();

  @Nullable
  String getClassifier ();

  /**
   * @return <code>true</code> if any member contains "${" indicating an
   *         unresolved placeholder.
   */
  boolean containsUnresolvedVariable ();

  @Nonnull
  IMavenArtifact createWithNewGroupID (@Nullable String sGroupID);

  static IComparator <IMavenArtifact> comparator ()
  {
    return (aElement1, aElement2) -> {
      int ret = CompareHelper.compare (aElement1.getGroupID (), aElement2.getGroupID ());
      if (ret == 0)
      {
        ret = aElement1.getArtifactID ().compareTo (aElement2.getArtifactID ());
        if (ret == 0)
        {
          ret = CompareHelper.compare (aElement1.getVersion (), aElement2.getVersion ());
          if (ret == 0)
            ret = CompareHelper.compare (aElement1.getClassifier (), aElement2.getClassifier ());
        }
      }
      return ret;
    };
  }
}
