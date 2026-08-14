// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.auth;

import com.apple.ads.auth.exception.AccessTokenFetchException;
import com.apple.ads.auth.model.FetchedToken;
import jakarta.annotation.Nonnull;
import java.io.Closeable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AccessTokenProvider that caches the token in memory and proactively refreshes it on a background
 * thread before it expires. API calls always read the cached token without blocking.
 */
public final class InMemoryCachingAccessTokenProvider implements AccessTokenProvider, Closeable {
  private static final String TOKEN_REFRESH_THREAD_NAME = "apple-ads-token-refresh";
  private static final Logger log =
      LoggerFactory.getLogger(InMemoryCachingAccessTokenProvider.class);

  static final long EXPIRY_BUFFER_SECONDS = 60;
  static final long MIN_REFRESH_DELAY_SECONDS = 30;

  private final AccessTokenFetcher tokenFetcher;
  private final ScheduledExecutorService scheduler;

  private volatile String cachedToken;

  public InMemoryCachingAccessTokenProvider(@Nonnull AccessTokenFetcher tokenFetcher)
      throws AccessTokenFetchException {
    this(tokenFetcher, Executors.newSingleThreadScheduledExecutor(r -> {
      Thread t = new Thread(r, TOKEN_REFRESH_THREAD_NAME);
      t.setDaemon(true);
      return t;
    }));
  }

  InMemoryCachingAccessTokenProvider(@Nonnull AccessTokenFetcher tokenFetcher,
      @Nonnull ScheduledExecutorService scheduler) throws AccessTokenFetchException {
    this.tokenFetcher = tokenFetcher;
    this.scheduler = scheduler;
    fetchAndCache();
  }

  @Override
  public String getAccessToken() {
    return cachedToken;
  }

  @Override
  public synchronized String refreshIfStale(String staleToken)
      throws AccessTokenFetchException {
    if (staleToken != null && !staleToken.equals(cachedToken)) {
      // Another thread already refreshed the token — return the current one without fetching
      return cachedToken;
    }
    return fetchAndCache();
  }

  private synchronized String fetchAndCache() throws AccessTokenFetchException {
    FetchedToken fetched = tokenFetcher.getAccessToken();
    cachedToken = fetched.token();
    scheduleRefresh(fetched.expiresInSeconds());
    return fetched.token();
  }

  private void scheduleRefresh(long expiresInSeconds) {
    if (scheduler.isShutdown()) {
      return;
    }
    // Clamp to MIN_REFRESH_DELAY_SECONDS to prevent a tight refresh loop if the server
    // consistently returns tokens with a very short (or zero) expiresIn.
    long delay = Math.max(MIN_REFRESH_DELAY_SECONDS, expiresInSeconds - EXPIRY_BUFFER_SECONDS);
    scheduler.schedule(() -> {
      try {
        fetchAndCache();
      } catch (AccessTokenFetchException e) {
        log.warn("Proactive token refresh failed; relying on reactive 401 handling as fallback", e);
      }
    }, delay, TimeUnit.SECONDS);
  }

  /**
   * Shuts down the background token refresh scheduler. After this call, no further proactive
   * refreshes will be scheduled. Any refresh task already in progress is allowed to complete.
   *
   * <p>This should be called when the provider is no longer needed, for example in a
   * {@code @PreDestroy} method or a JVM shutdown hook.
   */
  @Override
  public void close() {
    scheduler.shutdown();
  }
}