// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.auth;

import static com.apple.ads.auth.AuthorizationConstants.AUTHORIZATION_HEADER_NAME;
import static com.apple.ads.auth.AuthorizationConstants.BEARER_PREFIX;

import com.apple.ads.auth.exception.AccessTokenFetchException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class works to refresh the access token after receiving a 401 unauthorized response.
 * authenticate() is called when this happens, and is expected to return an updated request which
 * will be successfully authenticated (i.e. with a refreshed access token) N.B. if authenticate()
 * returns null, the request is not retried and the failed response is surfaced to the caller. See
 * the [Authenticator
 * Interface](https://square.github.io/okhttp/3.x/okhttp/okhttp3/Authenticator.html) for full
 * details.
 */
public class AppleAdsAuthenticator implements Authenticator {

  private static final Logger log = LoggerFactory.getLogger(AppleAdsAuthenticator.class);

  protected static final int MAX_REQUEST_ATTEMPTS = 2;

  private final AccessTokenProvider accessTokenProvider;

  public AppleAdsAuthenticator(@Nonnull AccessTokenProvider accessTokenProvider) {
    this.accessTokenProvider = accessTokenProvider;
  }

  @Override
  public Request authenticate(Route route, Response response) {
    //if the request has already been attempted the maximum number of times,
    //this means that a fresh token still did not result in successful authentication, so
    //we don't expect refreshing the token again to result in a success
    if (responseCount(response) >= MAX_REQUEST_ATTEMPTS) {
      return null;
    }

    String header = response.request().header(AUTHORIZATION_HEADER_NAME);
    String staleToken = header != null && header.startsWith(BEARER_PREFIX)
        ? header.substring(BEARER_PREFIX.length())
        : null;

    try {
      return newRequest(response.request(),
          accessTokenProvider.refreshIfStale(staleToken));
    } catch (AccessTokenFetchException e) {
      log.warn("Failed to refresh access token; request will not be retried", e);
      return null;
    }
  }

  /**
   * Creates a new request object identical to the provided one, except with the provided access
   * token as the Authorization header
   *
   * @param request     a Request that needs a new Authorization header
   * @param accessToken the access token to place in the updated Authorization header
   * @return the new Request
   */
  protected Request newRequest(Request request, String accessToken) {
    return request.newBuilder()
        .header(AUTHORIZATION_HEADER_NAME, BEARER_PREFIX + accessToken)
        .build();
  }

  /**
   * Returns the total number of times the request associated with the provided Response has been
   * attempted.
   *
   * @param response - a Response that resulted in a 401
   * @return the total number of times this request has been attempted
   */
  protected int responseCount(@Nonnull Response response) {
    int count = 1;
    while ((response = response.priorResponse()) != null) {
      count += 1;
    }
    return count;
  }

}
