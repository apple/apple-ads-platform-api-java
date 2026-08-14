// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.util;

import java.io.IOException;
import jakarta.annotation.Nonnull;
import retrofit2.Response;

/**
 * Utility class to assist with safely handling Retrofit Response objects
 */
public class RetrofitUtil {

  private RetrofitUtil() {
  }

  /**
   *
   * @param response - an unsuccesful Retrofit Response object
   * @param <T>      - unused type of the provided Response
   * @return a String containing the response body
   * @throws IOException if there is an IOException reading the Response body
   */
  public static <T> String getErrorBodyString(@Nonnull Response<T> response) throws IOException {
    if (response.isSuccessful()) {
      throw new IllegalArgumentException(
          "cannot generate error body string for a successful response");
    }

    return response.errorBody() == null ?
        "(no body in response)" : response.errorBody().string();
  }
}
