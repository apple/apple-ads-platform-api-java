// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.factory.exception;

import com.apple.ads.factory.AppleAdsApiClientBuilder;

/**
 * Thrown when {@link AppleAdsApiClientBuilder#build()} fails due to an error
 * during key parsing, JWT signing setup, or the initial access token fetch. The underlying cause
 * is available via {@link #getCause()}.
 */
public class ApiClientBuildException extends RuntimeException {

  public ApiClientBuildException(String message, Throwable cause) {
    super(message, cause);
  }
}