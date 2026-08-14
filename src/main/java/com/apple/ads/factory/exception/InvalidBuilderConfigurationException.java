// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.factory.exception;

/**
 * Thrown when invalid combinations of options are provided to @AppleAdsApiClientBuilder. The message
 * should always explain what the problem is.
 */
public class InvalidBuilderConfigurationException extends IllegalArgumentException {

  public InvalidBuilderConfigurationException(String message) {
    super(message);
  }
}