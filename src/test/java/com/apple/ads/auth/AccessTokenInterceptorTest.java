// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.auth;

import static com.apple.ads.auth.AuthorizationConstants.AUTHORIZATION_HEADER_NAME;
import static com.apple.ads.auth.AuthorizationConstants.BEARER_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccessTokenInterceptorTest {

  @Mock
  private AccessTokenProvider mockProvider;
  @Mock
  private Interceptor.Chain mockChain;
  @Mock
  private Response mockResponse;

  private static final Request BASE_REQUEST =
      new Request.Builder().url("http://example.com").build();

  private static final String TOKEN = "my-token";
  private static final String OLD_TOKEN = "old-token";
  private static final String NEW_TOKEN = "new-token";

  @Test
  void intercept_setsBearerAuthorizationHeader() throws IOException {
    when(mockProvider.getAccessToken()).thenReturn(TOKEN);
    when(mockChain.request()).thenReturn(BASE_REQUEST);
    when(mockChain.proceed(any())).thenReturn(mockResponse);

    new AccessTokenInterceptor(mockProvider).intercept(mockChain);

    ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
    verify(mockChain).proceed(captor.capture());
    assertEquals(BEARER_PREFIX + TOKEN, captor.getValue().header(AUTHORIZATION_HEADER_NAME));
  }

  @Test
  void intercept_overwritesExistingAuthorizationHeader() throws IOException {
    when(mockProvider.getAccessToken()).thenReturn(NEW_TOKEN);
    Request requestWithOldToken = BASE_REQUEST.newBuilder()
        .header(AUTHORIZATION_HEADER_NAME, BEARER_PREFIX + OLD_TOKEN)
        .build();
    when(mockChain.request()).thenReturn(requestWithOldToken);
    when(mockChain.proceed(any())).thenReturn(mockResponse);

    new AccessTokenInterceptor(mockProvider).intercept(mockChain);

    ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
    verify(mockChain).proceed(captor.capture());
    assertEquals(BEARER_PREFIX + NEW_TOKEN, captor.getValue().header(AUTHORIZATION_HEADER_NAME));
  }

  @Test
  void intercept_returnsResponseFromChain() throws IOException {
    when(mockProvider.getAccessToken()).thenReturn(TOKEN);
    when(mockChain.request()).thenReturn(BASE_REQUEST);
    when(mockChain.proceed(any())).thenReturn(mockResponse);

    Response result = new AccessTokenInterceptor(mockProvider).intercept(mockChain);

    assertSame(mockResponse, result);
  }
}