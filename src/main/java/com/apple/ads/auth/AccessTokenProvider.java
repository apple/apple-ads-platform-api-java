// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.auth;

import com.apple.ads.auth.exception.AccessTokenFetchException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Interface which models the maintenance lifecycle of an OAuth access token. Assumes some caching
 * is employed, but the exact strategy is left up to specific implementations.
 */
public interface AccessTokenProvider {

  /**
   * Returns the current access token without blocking. Called on every outgoing API request, so
   * implementations ought not perform I/O here.
   *
   * <p>Implementations must guarantee a non-null return value, typically by eagerly fetching a
   * token before this method becomes reachable (e.g. during construction).
   *
   * @return the current access token
   */
  @Nonnull
  String getAccessToken();

  /**
   * Returns a valid access token, refreshing only if {@code staleToken} matches the currently
   * cached token. If another caller already refreshed the token (i.e. {@code staleToken} no longer
   * matches the cached token), the current token is returned without making a new request. Pass
   * {@code null} to unconditionally force a refresh.
   *
   * <p>This method is invoked by the 401 retry path to coordinate refresh across concurrent
   * requests that fail with the same stale token. Implementations should serialize concurrent
   * callers so that only one refresh is performed per stale token.
   *
   * @param staleToken the token that was rejected; pass {@code null} to always force a refresh
   * @return a valid access token
   * @throws AccessTokenFetchException if a refresh was needed but could not be completed
   */
  @Nonnull
  String refreshIfStale(@Nullable String staleToken) throws AccessTokenFetchException;
}