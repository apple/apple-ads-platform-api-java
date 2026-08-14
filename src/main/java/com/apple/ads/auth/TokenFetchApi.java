// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.auth;

import com.apple.ads.auth.model.AccessTokenResponse;
import jakarta.annotation.Nonnull;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Retrofit API describing Apple's authorization server
 */
public interface TokenFetchApi {

  @FormUrlEncoded
  @POST("auth/oauth2/token")
  Call<AccessTokenResponse> getAccessToken(
      @Field("grant_type") @Nonnull String grantType,
      @Field("client_id") @Nonnull String clientId,
      @Field("client_secret") @Nonnull String clientSecret,
      @Field("scope") @Nonnull String scope
  );
}
