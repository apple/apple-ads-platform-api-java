// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to access the project version at runtime. The version is stored as an instance variable
 * and only loaded from resource files at construction.
 */
public class ProjectVersionUtil {

  private static final String UNKNOWN_PROJECT_VERSION = "UNKNOWN";
  private static final String VERSION_PROPERTIES_PATH = "/version.properties";
  private static final String VERSION_PROPERTY_NAME = "version";

  public static final ProjectVersionUtil INSTANCE = new ProjectVersionUtil();
  private static final Logger log =
      LoggerFactory.getLogger(ProjectVersionUtil.class);

  private final String projectVersion;

  private ProjectVersionUtil() {
    this.projectVersion = loadProjectVersion();
  }

  /**
   * Loads the project version from resources and returns it.
   *
   * @return the project version as a String
   */
  protected String loadProjectVersion() {
    Properties props = new Properties();
    try (InputStream is = getClass().getResourceAsStream(VERSION_PROPERTIES_PATH)) {
      props.load(is);
      return props.getProperty(VERSION_PROPERTY_NAME);
    } catch (IOException e) {
      log.warn("Failed to load gradle project version from process properties", e);
      return UNKNOWN_PROJECT_VERSION;
    }
  }

  /**
   * Returns this instance's project version, accessed via an instance variable.
   *
   * @return the project version as a String
   */
  public String getProjectVersion() {
    return this.projectVersion;
  }
}
