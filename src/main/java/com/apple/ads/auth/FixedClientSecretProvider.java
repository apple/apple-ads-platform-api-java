// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.auth;

import jakarta.annotation.Nonnull;

/**
 * A {@link ClientSecretProvider} that returns a fixed client secret supplied at
 * construction time. Useful when callers manage client-secret signing themselves or obtain a
 * pre-signed secret from an external system and simply need to use that value for authentication.
 */
public class FixedClientSecretProvider implements ClientSecretProvider {

  private final String clientSecret;

  public FixedClientSecretProvider(@Nonnull String clientSecret) {
    if (clientSecret == null) {
      throw new IllegalArgumentException("clientSecret must not be null");
    }
    this.clientSecret = clientSecret;
  }

  @Override
  public String getClientSecret() {
    return clientSecret;
  }
}
