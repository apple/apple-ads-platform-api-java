// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.auth;

import static com.apple.ads.util.RetrofitUtil.getErrorBodyString;

import com.apple.ads.auth.exception.AccessTokenFetchException;
import com.apple.ads.auth.exception.ClientSecretException;
import com.apple.ads.auth.model.AccessTokenResponse;
import com.apple.ads.auth.model.FetchedToken;
import jakarta.annotation.Nonnull;
import java.io.IOException;
import retrofit2.Response;

/**
 * Class for issuing HTTP requests to Apple's OAuth server to retrieve access (bearer) tokens
 */
public class AccessTokenFetcher {

  protected static final String GRANT_TYPE = "client_credentials";
  protected static final String SCOPE = "searchadsorg";

  private final TokenFetchApi api;
  private final String clientId;
  private final ClientSecretProvider clientSecretProvider;

  public AccessTokenFetcher(@Nonnull TokenFetchApi api, @Nonnull String clientId,
      @Nonnull ClientSecretProvider clientSecretProvider) {
    this.api = api;
    this.clientId = clientId;
    this.clientSecretProvider = clientSecretProvider;
  }

  /**
   * Fetch an access token and return the token plus information about its expiration
   *
   * @return a fresh access token and the number of seconds in which it expires
   * @throws AccessTokenFetchException
   */
  public FetchedToken getAccessToken() throws AccessTokenFetchException {
    String clientSecret;
    try {
      clientSecret = clientSecretProvider.getClientSecret();
    } catch (ClientSecretException e) {
      throw new AccessTokenFetchException("Failed to obtain client secret", e);
    }

    AccessTokenResponse responseBody;
    try {
      Response<AccessTokenResponse> response = api.getAccessToken(GRANT_TYPE,
              clientId,
              clientSecret,
              SCOPE)
          .execute();

      if (response.isSuccessful()) {
        responseBody = response.body();
      } else {
        String errorBody;
        IOException bodyReadException = null;
        try {
          errorBody = getErrorBodyString(response);
        } catch (IOException e) {
          errorBody = "(could not read error body)";
          bodyReadException = e;
        }
        // null cause is equivalent to no cause (see Exception(String, Throwable))
        throw new AccessTokenFetchException(
            "Fetching access token failed with response code=%d, body=[%s]"
                .formatted(response.code(), errorBody),
            bodyReadException);
      }
    } catch (IOException e) {
      throw new AccessTokenFetchException("IOException while fetching access token", e);
    }

    if (responseBody == null || responseBody.getAccessToken() == null) {
      throw new AccessTokenFetchException(
          "Unexpected response body received when fetching access token");
    }

    return new FetchedToken(responseBody.getAccessToken(), responseBody.getExpiresIn());
  }
}
