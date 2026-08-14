// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.factory;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.apple.ads.api.AppleAdsApi;
import com.apple.ads.auth.AccessTokenProvider;
import com.apple.ads.auth.ClientSecretProvider;
import com.apple.ads.auth.exception.AccessTokenFetchException;
import com.apple.ads.auth.exception.ClientSecretException;
import com.apple.ads.factory.exception.ApiClientBuildException;
import com.apple.ads.factory.exception.InvalidBuilderConfigurationException;
import com.apple.ads.testutil.MockAuthorizationServer;
import com.apple.ads.testutil.TestKeys;
import com.apple.ads.util.UserAgentInterceptor;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppleAdsApiClientBuilderTest {

  @RegisterExtension
  MockAuthorizationServer mockAuthorizationServer = new MockAuthorizationServer();

  @TempDir
  static Path tempDir;

  private static Path TEST_PRIVATE_KEY_PATH;
  private static String TEST_PRIVATE_KEY_PEM;

  @BeforeAll
  static void generateTestPrivateKey() throws Exception {
    TEST_PRIVATE_KEY_PEM = TestKeys.generateEcPrivateKeyPem();
    TEST_PRIVATE_KEY_PATH = tempDir.resolve("test-private-key.pem");
    Files.writeString(TEST_PRIVATE_KEY_PATH, TEST_PRIVATE_KEY_PEM, StandardCharsets.UTF_8);
  }

  private static final String UNREACHABLE_AUTH_BASE_URL = "http://127.0.0.1:1/";

  private static final String CLIENT_ID = "test-client-id";
  private static final String TEAM_ID = "test-team-id";
  private static final String KEY_ID = "test-key-id";
  private static final String CLIENT_SECRET = "test-client-secret";
  private static final String SOME_PEM = "-----BEGIN EC PRIVATE KEY-----\nfake\n-----END EC PRIVATE KEY-----\n";

  @Mock
  private AccessTokenProvider mockAccessTokenProvider;
  @Mock
  private ClientSecretProvider mockClientSecretProvider;

  // -------------------------------------------------------------------------
  // withPrivateKey — argument validation
  // -------------------------------------------------------------------------

  @Test
  void withPrivateKey_nullClientId_throws() {
    assertThrows(InvalidBuilderConfigurationException.class, () ->
        AppleAdsApiClientBuilder.withPrivateKey(null, TEAM_ID, KEY_ID, SOME_PEM));
  }

  @Test
  void withPrivateKey_blankClientId_throws() {
    assertThrows(InvalidBuilderConfigurationException.class, () ->
        AppleAdsApiClientBuilder.withPrivateKey("  ", TEAM_ID, KEY_ID, SOME_PEM));
  }

  @Test
  void withPrivateKey_nullTeamId_throws() {
    assertThrows(InvalidBuilderConfigurationException.class, () ->
        AppleAdsApiClientBuilder.withPrivateKey(CLIENT_ID, null, KEY_ID, SOME_PEM));
  }

  @Test
  void withPrivateKey_blankTeamId_throws() {
    assertThrows(InvalidBuilderConfigurationException.class, () ->
        AppleAdsApiClientBuilder.withPrivateKey(CLIENT_ID, "", KEY_ID, SOME_PEM));
  }

  @Test
  void withPrivateKey_nullKeyId_throws() {
    assertThrows(InvalidBuilderConfigurationException.class, () ->
        AppleAdsApiClientBuilder.withPrivateKey(CLIENT_ID, TEAM_ID, null, SOME_PEM));
  }

  @Test
  void withPrivateKey_blankKeyId_throws() {
    assertThrows(InvalidBuilderConfigurationException.class, () ->
        AppleAdsApiClientBuilder.withPrivateKey(CLIENT_ID, TEAM_ID, "  ", SOME_PEM));
  }

  @Test
  void withPrivateKey_nullPrivateKey_throws() {
    assertThrows(InvalidBuilderConfigurationException.class, () ->
        AppleAdsApiClientBuilder.withPrivateKey(CLIENT_ID, TEAM_ID, KEY_ID, null));
  }

  @Test
  void withPrivateKey_blankPrivateKey_throws() {
    assertThrows(InvalidBuilderConfigurationException.class, () ->
        AppleAdsApiClientBuilder.withPrivateKey(CLIENT_ID, TEAM_ID, KEY_ID, ""));
  }

  // -------------------------------------------------------------------------
  // withPrivateKeyPath — argument validation
  // -------------------------------------------------------------------------

  @Test
  void withPrivateKeyPath_nullClientId_throws() {
    assertThrows(InvalidBuilderConfigurationException.class, () ->
        AppleAdsApiClientBuilder.withPrivateKeyPath(
            null, TEAM_ID, KEY_ID, TEST_PRIVATE_KEY_PATH));
  }

  @Test
  void withPrivateKeyPath_nullTeamId_throws() {
    assertThrows(InvalidBuilderConfigurationException.class, () ->
        AppleAdsApiClientBuilder.withPrivateKeyPath(
            CLIENT_ID, null, KEY_ID, TEST_PRIVATE_KEY_PATH));
  }

  @Test
  void withPrivateKeyPath_nullKeyId_throws() {
    assertThrows(InvalidBuilderConfigurationException.class, () ->
        AppleAdsApiClientBuilder.withPrivateKeyPath(
            CLIENT_ID, TEAM_ID, null, TEST_PRIVATE_KEY_PATH));
  }

  @Test
  void withPrivateKeyPath_nullPath_throws() {
    assertThrows(InvalidBuilderConfigurationException.class, () ->
        AppleAdsApiClientBuilder.withPrivateKeyPath(CLIENT_ID, TEAM_ID, KEY_ID, null));
  }

  @Test
  void withPrivateKeyPath_nonexistentFile_throws() {
    assertThrows(UncheckedIOException.class, () ->
        AppleAdsApiClientBuilder.withPrivateKeyPath(
            CLIENT_ID, TEAM_ID, KEY_ID, Paths.get("/nonexistent/path.pem")));
  }

  // -------------------------------------------------------------------------
  // withClientSecretProvider — argument validation
  // -------------------------------------------------------------------------

  @Test
  void withClientSecretProvider_nullClientId_throws() {
    assertThrows(InvalidBuilderConfigurationException.class, () ->
        AppleAdsApiClientBuilder.withClientSecretProvider(null, mockClientSecretProvider));
  }

  @Test
  void withClientSecretProvider_blankClientId_throws() {
    assertThrows(InvalidBuilderConfigurationException.class, () ->
        AppleAdsApiClientBuilder.withClientSecretProvider("  ", mockClientSecretProvider));
  }

  @Test
  void withClientSecretProvider_nullProvider_throws() {
    assertThrows(InvalidBuilderConfigurationException.class, () ->
        AppleAdsApiClientBuilder.withClientSecretProvider(CLIENT_ID, null));
  }

  // -------------------------------------------------------------------------
  // withAccessTokenProvider — argument validation
  // -------------------------------------------------------------------------

  @Test
  void withAccessTokenProvider_nullProvider_throws() {
    assertThrows(InvalidBuilderConfigurationException.class, () ->
        AppleAdsApiClientBuilder.withAccessTokenProvider(null));
  }

  // -------------------------------------------------------------------------
  // Mode-mismatch setter guards — auth* setters reject in ACCESS_TOKEN_PROVIDER mode,
  // and they fail-fast at the setter call (not at build time).
  // -------------------------------------------------------------------------

  @Test
  void authBaseUrl_onAccessTokenProviderBuilder_throwsImmediatelyInSetter() {
    AppleAdsApiClientBuilder builder =
        AppleAdsApiClientBuilder.withAccessTokenProvider(mockAccessTokenProvider);
    assertThrows(InvalidBuilderConfigurationException.class,
        () -> builder.authBaseUrl("http://custom-auth.example.com/"));
  }

  @Test
  void authLogLevel_onAccessTokenProviderBuilder_throwsImmediatelyInSetter() {
    AppleAdsApiClientBuilder builder =
        AppleAdsApiClientBuilder.withAccessTokenProvider(mockAccessTokenProvider);
    assertThrows(InvalidBuilderConfigurationException.class,
        () -> builder.authLogLevel(Level.NONE));
  }

  @Test
  void authLogLevel_null_onAccessTokenProviderBuilder_throwsImmediatelyInSetter() {
    AppleAdsApiClientBuilder builder =
        AppleAdsApiClientBuilder.withAccessTokenProvider(mockAccessTokenProvider);
    assertThrows(InvalidBuilderConfigurationException.class,
        () -> builder.authLogLevel(null));
  }

  @Test
  void authClientCustomizer_onAccessTokenProviderBuilder_throwsImmediatelyInSetter() {
    AppleAdsApiClientBuilder builder =
        AppleAdsApiClientBuilder.withAccessTokenProvider(mockAccessTokenProvider);
    assertThrows(InvalidBuilderConfigurationException.class,
        () -> builder.authClientCustomizer(b -> {
        }));
  }

  // -------------------------------------------------------------------------
  // auth* setters succeed on non-ACCESS_TOKEN_PROVIDER builders
  // -------------------------------------------------------------------------

  @Test
  void authBaseUrl_onClientSecretProviderBuilder_succeeds() {
    assertDoesNotThrow(() ->
        AppleAdsApiClientBuilder
            .withClientSecretProvider(CLIENT_ID, mockClientSecretProvider)
            .authBaseUrl("http://custom-auth.example.com/"));
  }

  @Test
  void authLogLevel_onPrivateKeyBuilder_succeeds() {
    assertDoesNotThrow(() ->
        AppleAdsApiClientBuilder
            .withPrivateKey(CLIENT_ID, TEAM_ID, KEY_ID, SOME_PEM)
            .authLogLevel(Level.NONE));
  }

  @Test
  void authClientCustomizer_onClientSecretProviderBuilder_succeeds() {
    assertDoesNotThrow(() ->
        AppleAdsApiClientBuilder
            .withClientSecretProvider(CLIENT_ID, mockClientSecretProvider)
            .authClientCustomizer(b -> {
            }));
  }

  // -------------------------------------------------------------------------
  // authLogLevel setter — BODY rejected immediately
  // -------------------------------------------------------------------------

  @Test
  void authLogLevel_BODY_throwsImmediatelyInSetter() {
    AppleAdsApiClientBuilder builder =
        AppleAdsApiClientBuilder.withClientSecretProvider(CLIENT_ID, mockClientSecretProvider);
    assertThrows(InvalidBuilderConfigurationException.class,
        () -> builder.authLogLevel(Level.BODY));
  }

  // -------------------------------------------------------------------------
  // baseUrl / authBaseUrl setters — null rejected immediately
  // -------------------------------------------------------------------------

  @Test
  void baseUrl_null_throwsImmediatelyInSetter() {
    AppleAdsApiClientBuilder builder =
        AppleAdsApiClientBuilder.withAccessTokenProvider(mockAccessTokenProvider);
    assertThrows(InvalidBuilderConfigurationException.class, () -> builder.baseUrl(null));
  }

  @Test
  void authBaseUrl_null_throwsImmediatelyInSetter() {
    AppleAdsApiClientBuilder builder =
        AppleAdsApiClientBuilder.withClientSecretProvider(CLIENT_ID, mockClientSecretProvider);
    assertThrows(InvalidBuilderConfigurationException.class, () -> builder.authBaseUrl(null));
  }

  // -------------------------------------------------------------------------
  // timeout setters — null and negative rejected immediately
  // -------------------------------------------------------------------------

  @Test
  void connectTimeout_null_throwsImmediatelyInSetter() {
    AppleAdsApiClientBuilder builder =
        AppleAdsApiClientBuilder.withAccessTokenProvider(mockAccessTokenProvider);
    assertThrows(InvalidBuilderConfigurationException.class, () -> builder.connectTimeout(null));
  }

  @Test
  void connectTimeout_negative_throwsImmediatelyInSetter() {
    AppleAdsApiClientBuilder builder =
        AppleAdsApiClientBuilder.withAccessTokenProvider(mockAccessTokenProvider);
    assertThrows(InvalidBuilderConfigurationException.class,
        () -> builder.connectTimeout(Duration.ofSeconds(-1)));
  }

  @Test
  void readTimeout_null_throwsImmediatelyInSetter() {
    AppleAdsApiClientBuilder builder =
        AppleAdsApiClientBuilder.withAccessTokenProvider(mockAccessTokenProvider);
    assertThrows(InvalidBuilderConfigurationException.class, () -> builder.readTimeout(null));
  }

  @Test
  void readTimeout_negative_throwsImmediatelyInSetter() {
    AppleAdsApiClientBuilder builder =
        AppleAdsApiClientBuilder.withAccessTokenProvider(mockAccessTokenProvider);
    assertThrows(InvalidBuilderConfigurationException.class,
        () -> builder.readTimeout(Duration.ofSeconds(-1)));
  }

  @Test
  void writeTimeout_null_throwsImmediatelyInSetter() {
    AppleAdsApiClientBuilder builder =
        AppleAdsApiClientBuilder.withAccessTokenProvider(mockAccessTokenProvider);
    assertThrows(InvalidBuilderConfigurationException.class, () -> builder.writeTimeout(null));
  }

  @Test
  void writeTimeout_negative_throwsImmediatelyInSetter() {
    AppleAdsApiClientBuilder builder =
        AppleAdsApiClientBuilder.withAccessTokenProvider(mockAccessTokenProvider);
    assertThrows(InvalidBuilderConfigurationException.class,
        () -> builder.writeTimeout(Duration.ofSeconds(-1)));
  }

  @Test
  void callTimeout_null_throwsImmediatelyInSetter() {
    AppleAdsApiClientBuilder builder =
        AppleAdsApiClientBuilder.withAccessTokenProvider(mockAccessTokenProvider);
    assertThrows(InvalidBuilderConfigurationException.class, () -> builder.callTimeout(null));
  }

  @Test
  void callTimeout_negative_throwsImmediatelyInSetter() {
    AppleAdsApiClientBuilder builder =
        AppleAdsApiClientBuilder.withAccessTokenProvider(mockAccessTokenProvider);
    assertThrows(InvalidBuilderConfigurationException.class,
        () -> builder.callTimeout(Duration.ofSeconds(-1)));
  }

  @Test
  void callTimeout_zero_isAccepted() {
    assertDoesNotThrow(() ->
        AppleAdsApiClientBuilder.withAccessTokenProvider(mockAccessTokenProvider)
            .callTimeout(Duration.ZERO));
  }

  // -------------------------------------------------------------------------
  // build() — success paths
  // -------------------------------------------------------------------------

  @Test
  void build_withAccessTokenProvider_returnsAppleAdsApi() {
    AppleAdsApi api = AppleAdsApiClientBuilder
        .withAccessTokenProvider(mockAccessTokenProvider)
        .build();

    assertNotNull(api);
    assertInstanceOf(AppleAdsApi.class, api);
  }

  @Test
  void build_withClientSecretProvider_succeeds() throws ClientSecretException {
    when(mockClientSecretProvider.getClientSecret()).thenReturn(CLIENT_SECRET);
    mockAuthorizationServer.enqueueTokenResponse();

    assertDoesNotThrow(() ->
        AppleAdsApiClientBuilder
            .withClientSecretProvider(CLIENT_ID, mockClientSecretProvider)
            .authBaseUrl(mockAuthorizationServer.url())
            .build());
  }

  @Test
  void build_withNullApiLogLevel_succeeds() {
    assertDoesNotThrow(() ->
        AppleAdsApiClientBuilder
            .withAccessTokenProvider(mockAccessTokenProvider)
            .apiLogLevel(null)
            .build());
  }

  @Test
  void build_withNullAuthLogLevel_succeeds() throws ClientSecretException {
    when(mockClientSecretProvider.getClientSecret()).thenReturn(CLIENT_SECRET);
    mockAuthorizationServer.enqueueTokenResponse();

    assertDoesNotThrow(() ->
        AppleAdsApiClientBuilder
            .withClientSecretProvider(CLIENT_ID, mockClientSecretProvider)
            .authBaseUrl(mockAuthorizationServer.url())
            .authLogLevel(null)
            .build());
  }

  // -------------------------------------------------------------------------
  // Customizers
  // -------------------------------------------------------------------------

  @Test
  void apiClientCustomizer_isApplied() {
    AtomicBoolean called = new AtomicBoolean(false);

    AppleAdsApiClientBuilder
        .withAccessTokenProvider(mockAccessTokenProvider)
        .apiClientCustomizer(builder -> called.set(true))
        .build();

    assertTrue(called.get());
  }

  @Test
  void authClientCustomizer_isApplied() {
    // The customizer is applied to the auth OkHttpClient.Builder before the token fetch.
    // Throwing a sentinel exception from the customizer lets us confirm it was reached
    // without making any real network calls.
    RuntimeException sentinel = new RuntimeException("authClientCustomizer was called");

    RuntimeException thrown = assertThrows(RuntimeException.class, () ->
        AppleAdsApiClientBuilder
            .withClientSecretProvider(CLIENT_ID, mockClientSecretProvider)
            .authClientCustomizer(builder -> {
              throw sentinel;
            })
            .build());

    assertSame(sentinel, thrown);
  }

  // -------------------------------------------------------------------------
  // UserAgentInterceptor — always installed on the API client, after the customizer
  // -------------------------------------------------------------------------

  @Test
  void build_installsUserAgentInterceptor_whenNoApiClientCustomizerIsSet() {
    AtomicReference<OkHttpClient.Builder> captured = new AtomicReference<>();

    // The only way to inspect the final API OkHttpClient.Builder is via a customizer.
    // This customizer does nothing but capture a reference; the UserAgentInterceptor
    // is added after the customizer returns, so inspecting the builder after build()
    // reflects whether the interceptor is installed
    AppleAdsApiClientBuilder
        .withAccessTokenProvider(mockAccessTokenProvider)
        .apiClientCustomizer(captured::set)
        .build();

    assertTrue(
        captured.get().interceptors().stream().anyMatch(i -> i instanceof UserAgentInterceptor),
        "UserAgentInterceptor should always be installed on the API OkHttpClient");
  }

  @Test
  void build_installsUserAgentInterceptor_afterApiClientCustomizerRuns() {
    AtomicReference<OkHttpClient.Builder> captured = new AtomicReference<>();

    AppleAdsApiClientBuilder
        .withAccessTokenProvider(mockAccessTokenProvider)
        .apiClientCustomizer(builder -> {
          // At this point the UserAgentInterceptor must not yet be present — the builder
          // adds it only after the customizer returns.
          assertTrue(builder.interceptors().stream()
                  .noneMatch(i -> i instanceof UserAgentInterceptor),
              "UserAgentInterceptor should not be present while customizer is running");
          captured.set(builder);
        })
        .build();

    // After build(), the User Agent interceptor should now be present (added after the customizer ran).
    assertTrue(
        captured.get().interceptors().stream().anyMatch(i -> i instanceof UserAgentInterceptor),
        "UserAgentInterceptor should be installed after the customizer runs");
  }

  // -------------------------------------------------------------------------
  // Logging interceptor — installed last so logs reflect the request's final state,
  // including any modifications made by interceptors added earlier (e.g. the
  // UserAgentInterceptor and any interceptors added by the customizer).
  // -------------------------------------------------------------------------

  @Test
  void build_installsLoggingInterceptor_lastWhenApiLogLevelIsSet() {
    AtomicReference<OkHttpClient.Builder> captured = new AtomicReference<>();

    AppleAdsApiClientBuilder
        .withAccessTokenProvider(mockAccessTokenProvider)
        .apiLogLevel(Level.BASIC)
        .apiClientCustomizer(captured::set)
        .build();

    List<Interceptor> interceptors = captured.get().interceptors();
    Interceptor last = interceptors.get(interceptors.size() - 1);
    assertInstanceOf(HttpLoggingInterceptor.class, last,
        "logging interceptor should be the last interceptor so it observes the request's "
            + "final state after all other interceptors have run");

    // Sanity check: the UA interceptor is still present, but earlier in the chain than logging.
    int uaIndex = -1;
    int logIndex = -1;
    for (int i = 0; i < interceptors.size(); i++) {
      if (interceptors.get(i) instanceof UserAgentInterceptor) {
        uaIndex = i;
      } else if (interceptors.get(i) instanceof HttpLoggingInterceptor) {
        logIndex = i;
      }
    }
    assertTrue(uaIndex >= 0, "UserAgentInterceptor should be present");
    assertTrue(logIndex > uaIndex,
        "logging interceptor should be installed after the UserAgentInterceptor");
  }

  @Test
  void build_doesNotInstallLoggingInterceptor_whenApiLogLevelIsNull() {
    AtomicReference<OkHttpClient.Builder> captured = new AtomicReference<>();

    AppleAdsApiClientBuilder
        .withAccessTokenProvider(mockAccessTokenProvider)
        .apiLogLevel(null)
        .apiClientCustomizer(captured::set)
        .build();

    assertTrue(captured.get().interceptors().stream()
            .noneMatch(i -> i instanceof HttpLoggingInterceptor),
        "logging interceptor should not be installed when apiLogLevel is null");
  }

  // -------------------------------------------------------------------------
  // Cryptography — real key parsing and JWT signing
  // -------------------------------------------------------------------------

  @Test
  void build_withPrivateKeyPath_parsesKeyAndSignsJwtBeforeFailingAtTokenFetch() {
    // Verifies that EphemeralClientSecretProvider successfully reads and parses
    // the EC private key and produces a signed JWT. The subsequent token fetch is
    // expected to fail fast because UNREACHABLE_AUTH_BASE_URL is not listening.
    // A failure at key parsing or JWT signing would wrap a JOSEException;
    // a failure at token fetch wraps an AccessTokenFetchException.
    ApiClientBuildException ex = assertThrows(ApiClientBuildException.class, () ->
        AppleAdsApiClientBuilder
            .withPrivateKeyPath(CLIENT_ID, TEAM_ID, KEY_ID, TEST_PRIVATE_KEY_PATH)
            .authBaseUrl(UNREACHABLE_AUTH_BASE_URL)
            .build());
    assertInstanceOf(AccessTokenFetchException.class, ex.getCause());
  }

  @Test
  void build_withPrivateKey_parsesKeyAndSignsJwtBeforeFailingAtTokenFetch() {
    // Same as above but exercising the content-based withPrivateKey factory directly,
    // confirming callers do not need to materialize the key on disk.
    ApiClientBuildException ex = assertThrows(ApiClientBuildException.class, () ->
        AppleAdsApiClientBuilder
            .withPrivateKey(CLIENT_ID, TEAM_ID, KEY_ID, TEST_PRIVATE_KEY_PEM)
            .authBaseUrl(UNREACHABLE_AUTH_BASE_URL)
            .build());
    assertInstanceOf(AccessTokenFetchException.class, ex.getCause());
  }

  // -------------------------------------------------------------------------
  // Token loading — default AccessTokenProvider eagerly fetches on build()
  // -------------------------------------------------------------------------

  @Test
  void build_withDefaultAccessTokenProvider_tokenIsEagerlyLoadedOnBuild()
      throws ClientSecretException {
    when(mockClientSecretProvider.getClientSecret()).thenReturn(CLIENT_SECRET);
    mockAuthorizationServer.enqueueTokenResponse();

    AppleAdsApiClientBuilder
        .withClientSecretProvider(CLIENT_ID, mockClientSecretProvider)
        .authBaseUrl(mockAuthorizationServer.url())
        .build();

    assertEquals(1, mockAuthorizationServer.getServer().getRequestCount(),
        "Expected exactly one token request during build()");
  }
}