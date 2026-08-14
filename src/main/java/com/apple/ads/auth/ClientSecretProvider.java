// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.auth;

import com.apple.ads.auth.exception.ClientSecretException;
import jakarta.annotation.Nonnull;

/**
 * Interface for a simple provider of Oauth client secrets. Implementations may employ caching, or
 * make various assumptions about how the underlying parameters necessary for creation are
 * supplied.
 */
public interface ClientSecretProvider {

  @Nonnull
  String getClientSecret() throws ClientSecretException;
}
