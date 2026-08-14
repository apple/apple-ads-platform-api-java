// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.auth.exception;

/**
 * Exception thrown when a client secret cannot be generated or retrieved.
 */
public class ClientSecretException extends Exception {

  public ClientSecretException(String message) {
    super(message);
  }

  public ClientSecretException(String message, Throwable cause) {
    super(message, cause);
  }
}
