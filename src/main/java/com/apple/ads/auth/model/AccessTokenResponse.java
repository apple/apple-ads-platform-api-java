// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO representing the response from Apple's authorization server
 */
public class AccessTokenResponse {

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("token_type")
  private String tokenType;

  @JsonProperty("expires_in")
  private int expiresIn;

  @JsonProperty("scope")
  private String scope;

  public String getAccessToken() {
    return accessToken;
  }

  public String getTokenType() {
    return tokenType;
  }

  public int getExpiresIn() {
    return expiresIn;
  }

  public String getScope() {
    return scope;
  }
}