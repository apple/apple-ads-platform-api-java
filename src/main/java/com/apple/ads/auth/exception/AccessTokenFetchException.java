// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.auth.exception;

/**
 * Exception thrown when a failure occurs in fetching an access token from Apple's authorization
 * server.
 */
public class AccessTokenFetchException extends Exception {

  public AccessTokenFetchException(String message, Throwable cause) {
    super(message, cause);
  }

  public AccessTokenFetchException(String message) {
    super(message);
  }
}
