// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.apple.ads.auth.exception.AccessTokenFetchException;
import com.apple.ads.auth.model.FetchedToken;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InMemoryCachingAccessTokenProviderTest {

  @Mock
  private AccessTokenFetcher mockFetcher;
  @Mock
  private ScheduledExecutorService mockScheduler;

  private static final String INITIAL_TOKEN = "initial-token";
  private static final String NEW_TOKEN = "new-token";

  // Large enough that no background refresh fires during any test.
  private static final long EXPIRES_IN = 3600L;

  @Test
  void constructor_fetchesTokenEagerly() throws Exception {
    when(mockFetcher.getAccessToken()).thenReturn(new FetchedToken(INITIAL_TOKEN, EXPIRES_IN));

    new InMemoryCachingAccessTokenProvider(mockFetcher);

    verify(mockFetcher).getAccessToken();
  }

  @Test
  void getAccessToken_returnsTokenFetchedDuringConstruction() throws Exception {
    when(mockFetcher.getAccessToken()).thenReturn(new FetchedToken(INITIAL_TOKEN, EXPIRES_IN));

    InMemoryCachingAccessTokenProvider provider =
        new InMemoryCachingAccessTokenProvider(mockFetcher);

    assertEquals(INITIAL_TOKEN, provider.getAccessToken());
  }

  @Test
  void constructor_propagatesAccessTokenFetchException() throws Exception {
    when(mockFetcher.getAccessToken()).thenThrow(new AccessTokenFetchException("failed"));

    assertThrows(AccessTokenFetchException.class,
        () -> new InMemoryCachingAccessTokenProvider(mockFetcher));
  }

  @Test
  void refreshIfStale_withNull_returnsNewToken() throws Exception {
    when(mockFetcher.getAccessToken())
        .thenReturn(new FetchedToken(INITIAL_TOKEN, EXPIRES_IN),
                    new FetchedToken(NEW_TOKEN, EXPIRES_IN));
    InMemoryCachingAccessTokenProvider provider =
        new InMemoryCachingAccessTokenProvider(mockFetcher);

    assertEquals(NEW_TOKEN, provider.refreshIfStale(null));
  }

  @Test
  void refreshIfStale_withNull_updatesCurrentToken() throws Exception {
    when(mockFetcher.getAccessToken())
        .thenReturn(new FetchedToken(INITIAL_TOKEN, EXPIRES_IN),
                    new FetchedToken(NEW_TOKEN, EXPIRES_IN));
    InMemoryCachingAccessTokenProvider provider =
        new InMemoryCachingAccessTokenProvider(mockFetcher);

    provider.refreshIfStale(null);

    assertEquals(NEW_TOKEN, provider.getAccessToken());
  }

  @Test
  void refreshIfStale_propagatesAccessTokenFetchException() throws Exception {
    when(mockFetcher.getAccessToken())
        .thenReturn(new FetchedToken(INITIAL_TOKEN, EXPIRES_IN))
        .thenThrow(new AccessTokenFetchException("refresh failed"));
    InMemoryCachingAccessTokenProvider provider =
        new InMemoryCachingAccessTokenProvider(mockFetcher);

    assertThrows(AccessTokenFetchException.class, () -> provider.refreshIfStale(null));
  }

  @Test
  void refreshIfStale_doesNotModifyCachedToken_whenFetchFails() throws Exception {
    when(mockFetcher.getAccessToken())
        .thenReturn(new FetchedToken(INITIAL_TOKEN, EXPIRES_IN))
        .thenThrow(new AccessTokenFetchException("refresh failed"));
    InMemoryCachingAccessTokenProvider provider =
        new InMemoryCachingAccessTokenProvider(mockFetcher);

    assertThrows(AccessTokenFetchException.class, () -> provider.refreshIfStale(null));
    assertEquals(INITIAL_TOKEN, provider.getAccessToken());
  }

  @Test
  void refreshIfStale_withNull_callsFetcherEachTime() throws Exception {
    when(mockFetcher.getAccessToken())
        .thenReturn(new FetchedToken("token-1", EXPIRES_IN),
                    new FetchedToken("token-2", EXPIRES_IN),
                    new FetchedToken("token-3", EXPIRES_IN));
    InMemoryCachingAccessTokenProvider provider =
        new InMemoryCachingAccessTokenProvider(mockFetcher);

    provider.refreshIfStale(null);
    provider.refreshIfStale(null);

    // 1 eager + 2 explicit calls
    verify(mockFetcher, times(3)).getAccessToken();
  }

  @Test
  void refreshIfStale_refreshes_whenStaleTokenMatchesCachedToken() throws Exception {
    when(mockFetcher.getAccessToken())
        .thenReturn(new FetchedToken(INITIAL_TOKEN, EXPIRES_IN),
                    new FetchedToken(NEW_TOKEN, EXPIRES_IN));
    InMemoryCachingAccessTokenProvider provider =
        new InMemoryCachingAccessTokenProvider(mockFetcher);

    String result = provider.refreshIfStale(INITIAL_TOKEN);

    assertEquals(NEW_TOKEN, result);
    assertEquals(NEW_TOKEN, provider.getAccessToken());
  }

  @Test
  void refreshIfStale_returnsCachedToken_whenStaleTokenNoLongerMatchesCachedToken() throws Exception {
    when(mockFetcher.getAccessToken()).thenReturn(new FetchedToken(NEW_TOKEN, EXPIRES_IN));
    InMemoryCachingAccessTokenProvider provider =
        new InMemoryCachingAccessTokenProvider(mockFetcher);

    // Pass a stale token that differs from the cached token
    String result = provider.refreshIfStale(INITIAL_TOKEN);

    assertEquals(NEW_TOKEN, result);
    // Fetcher called only once (during construction), not again
    verify(mockFetcher, times(1)).getAccessToken();
  }

  @Test
  void scheduleRefresh_usesMinimumDelay_whenExpiresInIsSmallerThanBuffer() throws Exception {
    long expiresInSmallerThanBuffer = InMemoryCachingAccessTokenProvider.EXPIRY_BUFFER_SECONDS - 1;
    when(mockFetcher.getAccessToken())
        .thenReturn(new FetchedToken(INITIAL_TOKEN, expiresInSmallerThanBuffer));

    new InMemoryCachingAccessTokenProvider(mockFetcher, mockScheduler);

    verify(mockScheduler).schedule(any(Runnable.class),
        eq(InMemoryCachingAccessTokenProvider.MIN_REFRESH_DELAY_SECONDS), eq(TimeUnit.SECONDS));
  }

  @Test
  void scheduleRefresh_usesMinimumDelay_whenExpiresInIsZero() throws Exception {
    when(mockFetcher.getAccessToken())
        .thenReturn(new FetchedToken(INITIAL_TOKEN, 0L));

    new InMemoryCachingAccessTokenProvider(mockFetcher, mockScheduler);

    verify(mockScheduler).schedule(any(Runnable.class),
        eq(InMemoryCachingAccessTokenProvider.MIN_REFRESH_DELAY_SECONDS), eq(TimeUnit.SECONDS));
  }

  @Test
  void close_shutsDownScheduler() throws Exception {
    when(mockFetcher.getAccessToken()).thenReturn(new FetchedToken(INITIAL_TOKEN, EXPIRES_IN));
    InMemoryCachingAccessTokenProvider provider =
        new InMemoryCachingAccessTokenProvider(mockFetcher, mockScheduler);

    provider.close();

    verify(mockScheduler).shutdown();
  }

  @Test
  void scheduleRefresh_doesNotScheduleAfterClose() throws Exception {
    when(mockFetcher.getAccessToken()).thenReturn(new FetchedToken(INITIAL_TOKEN, EXPIRES_IN));
    when(mockScheduler.isShutdown()).thenReturn(false).thenReturn(true);
    InMemoryCachingAccessTokenProvider provider =
        new InMemoryCachingAccessTokenProvider(mockFetcher, mockScheduler);

    provider.close();
    provider.refreshIfStale(null);

    // schedule() called once during construction, but not again after close
    verify(mockScheduler, times(1)).schedule(any(Runnable.class), anyLong(), any(TimeUnit.class));
  }
}