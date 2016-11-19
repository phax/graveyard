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
package com.helger.lama.maven.pom;

import javax.annotation.concurrent.Immutable;

import com.helger.commons.annotation.PresentForCodeCoverage;

@Immutable
public final class CMavenXML
{
  public static final String MODEL_VERSION = "4.0.0";
  public static final String ELEMENT_ARTIFACTID = "artifactId";
  public static final String ELEMENT_BUILD = "build";
  public static final String ELEMENT_CLASSIFIER = "classifier";
  public static final String ELEMENT_CONFIGURATION = "configuration";
  public static final String ELEMENT_CONNECTION = "connection";
  public static final String ELEMENT_DEPENDENCIES = "dependencies";
  public static final String ELEMENT_DEPENDENCY = "dependency";
  public static final String ELEMENT_DEPENDENCY_MANAGEMENT = "dependencyManagement";
  public static final String ELEMENT_DESCRIPTION = "description";
  public static final String ELEMENT_DEVELOPER_CONNECTION = "developerConnection";
  public static final String ELEMENT_DIRECTORY = "directory";
  public static final String ELEMENT_DISTRIBUTION = "distribution";
  public static final String ELEMENT_ENABLED = "enabled";
  public static final String ELEMENT_EXCLUSION = "exclusion";
  public static final String ELEMENT_EXCLUSIONS = "exclusions";
  public static final String ELEMENT_EXECUTION = "execution";
  public static final String ELEMENT_EXECUTIONS = "executions";
  public static final String ELEMENT_EXTENSION = "extension";
  public static final String ELEMENT_EXTENSIONS = "extensions";
  public static final String ELEMENT_GOAL = "goal";
  public static final String ELEMENT_GOALS = "goals";
  public static final String ELEMENT_GROUPID = "groupId";
  public static final String ELEMENT_ID = "id";
  public static final String ELEMENT_LAYOUT = "layout";
  public static final String ELEMENT_LICENSE = "license";
  public static final String ELEMENT_LICENSES = "licenses";
  public static final String ELEMENT_MODEL_VERSION = "modelVersion";
  public static final String ELEMENT_MODULE = "module";
  public static final String ELEMENT_MODULES = "modules";
  public static final String ELEMENT_NAME = "name";
  public static final String ELEMENT_OPTIONAL = "optional";
  public static final String ELEMENT_PACKAGING = "packaging";
  public static final String ELEMENT_PARENT = "parent";
  public static final String ELEMENT_PHASE = "phase";
  public static final String ELEMENT_PLUGIN = "plugin";
  public static final String ELEMENT_PLUGINREPOSITORIES = "pluginRepositories";
  public static final String ELEMENT_PLUGINREPOSITORY = "pluginRepository";
  public static final String ELEMENT_PLUGINS = "plugins";
  public static final String ELEMENT_PLUGIN_MANAGEMENT = "pluginManagement";
  public static final String ELEMENT_PREREQUISITES = "prerequisites";
  public static final String ELEMENT_PROJECT = "project";
  public static final String ELEMENT_PROPERTIES = "properties";
  public static final String ELEMENT_RELATIVE_PATH = "relativePath";
  public static final String ELEMENT_RELEASES = "releases";
  public static final String ELEMENT_REPOSITORIES = "repositories";
  public static final String ELEMENT_REPOSITORY = "repository";
  public static final String ELEMENT_RESOURCE = "resource";
  public static final String ELEMENT_RESOURCES = "resources";
  public static final String ELEMENT_SCM = "scm";
  public static final String ELEMENT_SCOPE = "scope";
  public static final String ELEMENT_SNAPSHOTS = "snapshots";
  public static final String ELEMENT_TARGETPATH = "targetPath";
  public static final String ELEMENT_TYPE = "type";
  public static final String ELEMENT_URL = "url";
  public static final String ELEMENT_VERSION = "version";

  @PresentForCodeCoverage
  private static final CMavenXML s_aInstance = new CMavenXML ();

  private CMavenXML ()
  {}
}
