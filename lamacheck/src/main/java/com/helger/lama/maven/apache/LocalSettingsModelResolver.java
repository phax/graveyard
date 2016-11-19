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
package com.helger.lama.maven.apache;

import java.io.File;

import javax.annotation.Nonnull;

import org.apache.maven.settings.Settings;
import org.apache.maven.settings.io.DefaultSettingsReader;

import com.helger.commons.exception.InitializationException;
import com.helger.commons.system.SystemProperties;

public class LocalSettingsModelResolver
{
  private static final Settings s_aSettings;

  static
  {
    try
    {
      // Read main maven settings
      s_aSettings = new DefaultSettingsReader ().read (new File (SystemProperties.getUserHome () + "/.m2/settings.xml"), null);
    }
    catch (final Exception ex)
    {
      throw new InitializationException ("Failed to get local repo path", ex);
    }
  }

  public static String getLocalRepository ()
  {
    return s_aSettings.getLocalRepository ();
  }

  @Nonnull
  public static File resolveModel (final String sGroupID, final String sArtifactID, final String sVersion)
  {
    final String sFilename = sGroupID.replace ('.', '/') + '/' + sArtifactID + '/' + sVersion + '/' + sArtifactID + '-' + sVersion + ".pom";

    // Check main local repo is possible
    return new File (getLocalRepository (), sFilename);
  }
}
