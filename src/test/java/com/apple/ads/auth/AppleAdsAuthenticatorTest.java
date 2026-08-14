// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.auth;

import static com.apple.ads.auth.AuthorizationConstants.AUTHORIZATION_HEADER_NAME;
import static com.apple.ads.auth.AuthorizationConstants.BEARER_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.apple.ads.auth.exception.AccessTokenFetchException;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppleAdsAuthenticatorTest {

  @Mock
  private AccessTokenProvider mockProvider;

  private static final String BASE_URL = "http://example.com";
  private static final String OLD_TOKEN = "old-token";
  private static final String NEW_TOKEN = "new-token";
  private static final String CURRENT_TOKEN = "current-token";
  private static final String STALE_TOKEN = "stale-token";

  private static Request requestWithToken(String token) {
    return new Request.Builder()
        .url(BASE_URL)
        .header(AUTHORIZATION_HEADER_NAME, BEARER_PREFIX + token)
        .build();
  }

  private static Request requestWithNoAuthHeader() {
    return new Request.Builder().url(BASE_URL).build();
  }

  private static Response unauthorizedResponse(Request request) {
    return new Response.Builder()
        .request(request)
        .protocol(Protocol.HTTP_1_1)
        .code(401)
        .message("Unauthorized")
        .build();
  }

  /**
   * Returns a response that has one prior response, giving responseCount == 2.
   */
  private static Response responseWithPrior(Request request) {
    Response prior = unauthorizedResponse(request);
    return unauthorizedResponse(request).newBuilder().priorResponse(prior).build();
  }

  @Test
  void authenticate_returnsNull_whenMaxRetryAttemptsExceeded() {
    AppleAdsAuthenticator authenticator = new AppleAdsAuthenticator(mockProvider);

    Request request = requestWithToken(OLD_TOKEN);
    Response response = responseWithPrior(request);

    assertNull(authenticator.authenticate(null, response));
  }

  @Test
  void authenticate_returnsRequestWithNewToken_onNormalRefresh() throws Exception {
    when(mockProvider.refreshIfStale(OLD_TOKEN)).thenReturn(NEW_TOKEN);

    AppleAdsAuthenticator authenticator = new AppleAdsAuthenticator(mockProvider);
    Request result = authenticator.authenticate(null,
        unauthorizedResponse(requestWithToken(OLD_TOKEN)));

    assertNotNull(result);
    assertEquals(BEARER_PREFIX + NEW_TOKEN, result.header(AUTHORIZATION_HEADER_NAME));
  }

  @Test
  void authenticate_returnsRequestWithNewToken_whenRequestHasNoAuthHeader() throws Exception {
    when(mockProvider.refreshIfStale(null)).thenReturn(NEW_TOKEN);

    AppleAdsAuthenticator authenticator = new AppleAdsAuthenticator(mockProvider);
    Request result = authenticator.authenticate(null,
        unauthorizedResponse(requestWithNoAuthHeader()));

    assertNotNull(result);
    assertEquals(BEARER_PREFIX + NEW_TOKEN, result.header(AUTHORIZATION_HEADER_NAME));
  }

  @Test
  void authenticate_returnsNull_whenTokenRefreshFails() throws Exception {
    when(mockProvider.refreshIfStale(OLD_TOKEN))
        .thenThrow(new AccessTokenFetchException("fetch failed"));

    AppleAdsAuthenticator authenticator = new AppleAdsAuthenticator(mockProvider);
    Request result = authenticator.authenticate(null,
        unauthorizedResponse(requestWithToken(OLD_TOKEN)));

    assertNull(result);
  }

  @Test
  void authenticate_passesStaleTokenToProvider() throws Exception {
    when(mockProvider.refreshIfStale(STALE_TOKEN)).thenReturn(CURRENT_TOKEN);

    AppleAdsAuthenticator authenticator = new AppleAdsAuthenticator(mockProvider);
    authenticator.authenticate(null, unauthorizedResponse(requestWithToken(STALE_TOKEN)));

    verify(mockProvider).refreshIfStale(STALE_TOKEN);
  }
}