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

import com.helger.commons.id.IHasID;
import com.helger.commons.lang.EnumHelper;

/**
 * Predefined Maven2 scopes
 *
 * @author Philip Helger
 */
public enum EMavenScope implements IHasID <String>
{
 /**
  * This is the default scope, used if none is specified. Compile dependencies
  * are available in all classpaths of a project. Furthermore, those
  * dependencies are propagated to dependent projects.
  */
  COMPILE ("compile"),
 /**
  * This is much like compile, but indicates you expect the JDK or a container
  * to provide the dependency at runtime. For example, when building a web
  * application for the Java Enterprise Edition, you would set the dependency on
  * the Servlet API and related Java EE APIs to scope provided because the web
  * container provides those classes. This scope is only available on the
  * compilation and test classpath, and is not transitive.
  */
  PROVIDED ("provided"),
 /**
  * This scope indicates that the dependency is not required for compilation,
  * but is for execution. It is in the runtime and test classpaths, but not the
  * compile classpath.
  */
  RUNTIME ("runtime"),
 /**
  * This scope indicates that the dependency is not required for normal use of
  * the application, and is only available for the test compilation and
  * execution phases.
  */
  TEST ("test"),
 /**
  * This scope is similar to provided except that you have to provide the JAR
  * which contains it explicitly. The artifact is always available and is not
  * looked up in a repository.
  */
  SYSTEM ("system"),
 /**
  * (only available in Maven 2.0.9 or later)<br>
  * This scope is only used on a dependency of type pom in the
  * &lt;dependencyManagement> section. It indicates that the specified POM
  * should be replaced with the dependencies in that POM's
  * &lt;dependencyManagement> section. Since they are replaced, dependencies
  * with a scope of import do not actually participate in limiting the
  * transitivity of a dependency.
  */
  IMPORT ("import");

  @Nonnull
  public static final EMavenScope DEFAULT_SCOPE = COMPILE;

  private final String m_sValue;

  private EMavenScope (final String sValue)
  {
    m_sValue = sValue;
  }

  public String getID ()
  {
    return m_sValue;
  }

  @Nullable
  public static EMavenScope getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EMavenScope.class, sID);
  }

  @Nonnull
  public static EMavenScope getFromIDOrThrow (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrThrow (EMavenScope.class, sID);
  }
}
