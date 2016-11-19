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

import javax.annotation.concurrent.Immutable;

import com.helger.commons.annotation.PresentForCodeCoverage;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.resource.IReadableResource;

/**
 * Contains some maven specific paths.
 *
 * @author Philip Helger
 */
@Immutable
public final class CMaven
{
  // Source directory names
  public static final String SRC_DIR = "src/";
  public static final String SRC_MAIN_DIR = SRC_DIR + "main/";
  public static final String SRC_MAIN_JAVA_DIR = SRC_MAIN_DIR + "java/";
  public static final String SRC_MAIN_RESOURCES_DIR = SRC_MAIN_DIR + "resources/";
  public static final String SRC_MAIN_WEBAPP_DIR = SRC_MAIN_DIR + "webapp/";
  public static final String SRC_TEST_DIR = SRC_DIR + "test/";
  public static final String SRC_TEST_JAVA_DIR = SRC_TEST_DIR + "java/";
  public static final String SRC_TEST_RESOURCES_DIR = SRC_TEST_DIR + "resources/";

  // Target directory names
  public static final String TARGET_DIR = "target/";
  public static final String TARGET_CLASSES_DIR = TARGET_DIR + "classes/";
  public static final String TARGET_TEST_CLASSES_DIR = TARGET_DIR + "test-classes/";
  public static final String TARGET_WEBAPP_CLASSES_DIR = TARGET_DIR + "webapp-classes/";
  public static final String TARGET_GENERATED_SOURCES_DIR = TARGET_DIR + "generated-sources/";

  // POM related stuff
  public static final String POM_XML = "pom.xml";
  public static final String POM_XMLNS = "http://maven.apache.org/POM/4.0.0";
  public static final String POM_XMLNS_URL = "http://maven.apache.org/maven-v4_0_0.xsd";
  public static final IReadableResource POM_XMLNS_RES = new ClassPathResource ("schemas/maven-4.0.0.xsd");

  @PresentForCodeCoverage
  private static final CMaven s_aInstance = new CMaven ();

  private CMaven ()
  {}
}
