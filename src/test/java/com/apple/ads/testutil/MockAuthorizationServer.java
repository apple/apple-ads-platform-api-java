// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.testutil;

import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * JUnit 5 extension that manages a {@link MockWebServer} instance for tests that need a fake token
 * endpoint. Register it with {@code @RegisterExtension} and use {@link #url()} as the
 * {@code authBaseUrl} for {@code AppleAdsApiClientBuilder}.
 *
 * <pre>{@code
 * @RegisterExtension
 * MockTokenServer mockTokenServer = new MockTokenServer();
 * }</pre>
 */
public class MockAuthorizationServer implements BeforeEachCallback, AfterEachCallback {

  private MockWebServer server;

  @Override
  public void beforeEach(ExtensionContext context) throws IOException {
    server = new MockWebServer();
    server.start();
  }

  @Override
  public void afterEach(ExtensionContext context) throws IOException {
    server.shutdown();
  }

  /**
   * Returns the base URL of the mock server, suitable for use as {@code authBaseUrl}.
   */
  public String url() {
    return server.url("/").toString();
  }

  /**
   * Returns the underlying {@link MockWebServer}, e.g. to call {@code getRequestCount()} or
   * {@code takeRequest()}.
   */
  public MockWebServer getServer() {
    return server;
  }

  /**
   * Enqueues a standard 200 token response using {@code "test-access-token"} as the token value.
   */
  public void enqueueTokenResponse() {
    enqueueTokenResponse("test-access-token");
  }

  /**
   * Enqueues a 200 token response with the given {@code accessToken} value.
   */
  public void enqueueTokenResponse(String accessToken) {
    server.enqueue(new MockResponse()
        .setResponseCode(200)
        .addHeader("Content-Type", "application/json")
        .setBody("""
            {"access_token":"%s","token_type":"Bearer","expires_in":3600,"scope":"searchadsorg"}
            """.formatted(accessToken)));
  }

  /**
   * Enqueues an error response with the given HTTP status code and empty body.
   */
  public void enqueueErrorResponse(int statusCode) {
    server.enqueue(new MockResponse().setResponseCode(statusCode));
  }

  /**
   * Convenience wrapper around {@link MockWebServer#takeRequest()}.
   */
  public RecordedRequest takeRequest() throws InterruptedException {
    return server.takeRequest();
  }
}