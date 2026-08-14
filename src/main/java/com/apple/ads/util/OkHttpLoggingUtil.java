// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.util;

import com.apple.ads.auth.AuthorizationConstants;
import jakarta.annotation.Nonnull;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import okhttp3.logging.HttpLoggingInterceptor.Logger;

/**
 * Utility class to assist with constructing HttpLoggingInterceptor instances with desired settings
 * and which safely redact sensitive header(s) (e.g. Authorization)
 */
public class OkHttpLoggingUtil {

  private OkHttpLoggingUtil() {
  }

  /**
   * Creates a safe HttpLoggingInterceptor with the specified level and the default logger
   *
   * @param level - the desired HttpLoggingInterceptor.Level at which logs for HTTP request will be
   *              sent
   * @return an HttpLoggingInterceptor
   */
  public static HttpLoggingInterceptor safeOkHttpLoggingInterceptor(@Nonnull Level level) {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    setUpInterceptor(interceptor, level);

    return interceptor;
  }

  /**
   * Creates a safe HttpLoggingInterceptor with the specified level and logger
   *
   * @param level  - the desired HttpLoggingInterceptor.Level at which logs for HTTP request will be
   *               sent
   * @param logger - a custom HttpLoggingInterceptor.Logger to use for logging
   * @return an HttpLoggingInterceptor
   */
  public static HttpLoggingInterceptor safeOkHttpLoggingInterceptor(
      @Nonnull Logger logger, @Nonnull Level level) {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(logger);
    setUpInterceptor(interceptor, level);

    return interceptor;
  }

  private static void setUpInterceptor(HttpLoggingInterceptor interceptor, Level level) {
    interceptor.setLevel(level);
    interceptor.redactHeader(AuthorizationConstants.AUTHORIZATION_HEADER_NAME);
  }
}
