// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.util;

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
class UserAgentInterceptorTest {

  @Mock
  private Interceptor.Chain mockChain;
  @Mock
  private Response mockResponse;

  private static final String USER_AGENT_HEADER_NAME = "User-Agent";
  private static final String EXPECTED_USER_AGENT =
      "apple-ads-java-" + ProjectVersionUtil.INSTANCE.getProjectVersion();

  private static final Request BASE_REQUEST =
      new Request.Builder().url("http://example.com").build();

  @Test
  void intercept_setsUserAgentHeader() throws IOException {
    when(mockChain.request()).thenReturn(BASE_REQUEST);
    when(mockChain.proceed(any())).thenReturn(mockResponse);

    new UserAgentInterceptor().intercept(mockChain);

    ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
    verify(mockChain).proceed(captor.capture());
    assertEquals(EXPECTED_USER_AGENT, captor.getValue().header(USER_AGENT_HEADER_NAME));
  }

  @Test
  void intercept_overwritesExistingUserAgentHeader() throws IOException {
    Request requestWithExistingUserAgent = BASE_REQUEST.newBuilder()
        .header(USER_AGENT_HEADER_NAME, "some-other-user-agent/1.2.3")
        .build();
    when(mockChain.request()).thenReturn(requestWithExistingUserAgent);
    when(mockChain.proceed(any())).thenReturn(mockResponse);

    new UserAgentInterceptor().intercept(mockChain);

    ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
    verify(mockChain).proceed(captor.capture());
    assertEquals(EXPECTED_USER_AGENT, captor.getValue().header(USER_AGENT_HEADER_NAME));
  }

  @Test
  void intercept_returnsResponseFromChain() throws IOException {
    when(mockChain.request()).thenReturn(BASE_REQUEST);
    when(mockChain.proceed(any())).thenReturn(mockResponse);

    Response result = new UserAgentInterceptor().intercept(mockChain);

    assertSame(mockResponse, result);
  }
}