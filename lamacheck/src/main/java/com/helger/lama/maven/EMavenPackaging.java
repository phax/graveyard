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
package com.helger.lama.maven;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.id.IHasID;
import com.helger.commons.lang.EnumHelper;

/**
 * Predefined Maven2 packaging.
 *
 * @author Philip Helger
 */
public enum EMavenPackaging implements IHasID <String>
{
 EAR ("ear", ".ear"),
 JAR ("jar", ".jar"),
 POM ("pom", ".pom"),
 RAR ("rar", ".rar"),
 WAR ("war", ".war"),
 ZIP ("zip", ".zip"),
 AMI ("ami"),
 APK ("apk"),
 APKLIB ("apklib"),
 ATLASSIAN_PLUGIN ("atlassian-plugin"),
 AWS ("aws"),
 BUNDLE ("bundle"),
 DISTRIBUTION_BASE_ZIP ("distribution-base-zip"),
 DISTRIBUTION_FRAGMENT ("distribution-fragment"),
 ECLIPSE_PLUGIN ("eclipse-plugin"),
 ECLIPSE_REPOSITORY ("eclipse-repository"),
 EJB ("ejb"),
 GLASSFISH_JAR ("glassfish-jar"),
 HK2_JAR ("hk2-jar"),
 HPI ("hpi"),
 JAVA_SOURCE ("java-source"),
 JBOSS_SAR ("jboss-sar"),
 JDOCBOOK ("jdocbook"),
 JENKINS_MODULE ("jenkins-module"),
 MAVEN_PLUGIN ("maven-plugin"),
 OSGI_BUNDLE ("osgi-bundle"),
 STAPLER_JAR ("stapler-jar"),
 SWC ("swc");

  @Nonnull
  public static final EMavenPackaging DEFAULT_PACKAGING = JAR;

  private final String m_sValue;
  private final String m_sExt;

  private EMavenPackaging (@Nonnull @Nonempty final String sValue)
  {
    this (sValue, ".jar");
  }

  private EMavenPackaging (@Nonnull @Nonempty final String sValue, @Nonnull @Nonempty final String sExt)
  {
    m_sValue = sValue;
    m_sExt = sExt;
  }

  @Nonnull
  public String getID ()
  {
    return m_sValue;
  }

  @Nonnull
  public String getFileExtension ()
  {
    return m_sExt;
  }

  public boolean isOneOfTheStandardPackagings ()
  {
    return this == JAR || this == WAR || this == EAR || this == POM || this == MAVEN_PLUGIN;
  }

  @Nullable
  public static EMavenPackaging getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EMavenPackaging.class, sID);
  }
}
