// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.auth;

import static com.apple.ads.auth.AuthorizationConstants.AUTHORIZATION_HEADER_NAME;
import static com.apple.ads.auth.AuthorizationConstants.BEARER_PREFIX;

import jakarta.annotation.Nonnull;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * OkHttp interceptor that attaches the current Bearer access token to every outgoing request. This
 * runs before {@link AppleAdsAuthenticator}, which handles token refresh on 401 responses.
 */
public class AccessTokenInterceptor implements Interceptor {

  private final AccessTokenProvider accessTokenProvider;

  public AccessTokenInterceptor(@Nonnull AccessTokenProvider accessTokenProvider) {
    this.accessTokenProvider = accessTokenProvider;
  }

  @Override
  @Nonnull
  public Response intercept(@Nonnull Chain chain) throws IOException {
    Request request = chain.request().newBuilder()
        .header(AUTHORIZATION_HEADER_NAME,
            BEARER_PREFIX + accessTokenProvider.getAccessToken())
        .build();
    return chain.proceed(request);
  }
}