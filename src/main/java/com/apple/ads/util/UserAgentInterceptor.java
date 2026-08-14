// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.util;

import jakarta.annotation.Nonnull;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * OkHttp interceptor that attaches a {@code User-Agent} header identifying this library and its
 * version to every outgoing request.
 */
public class UserAgentInterceptor implements Interceptor {

  private static final String USER_AGENT_HEADER_NAME = "User-Agent";
  private static final String USER_AGENT_PREFIX = "apple-ads-java-";

  private final String userAgent;

  public UserAgentInterceptor() {
    this.userAgent = USER_AGENT_PREFIX + ProjectVersionUtil.INSTANCE.getProjectVersion();
  }

  @Override
  @Nonnull
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request().newBuilder()
        .header(USER_AGENT_HEADER_NAME, userAgent)
        .build();
    return chain.proceed(request);
  }
}