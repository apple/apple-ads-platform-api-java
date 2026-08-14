// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.auth.model;

/**
 * Record to encapsulate a newly retrieved OAuth bearer token and the number of seconds until which
 * it expires.
 *
 * @param token            - a freshly retrieved OAuth bearer token
 * @param expiresInSeconds - the number of seconds until the token expires
 */
public record FetchedToken(String token, long expiresInSeconds) {

}