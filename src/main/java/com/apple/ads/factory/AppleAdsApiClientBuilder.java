// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.factory;

import com.apple.ads.api.AppleAdsApi;
import com.apple.ads.auth.AccessTokenFetcher;
import com.apple.ads.auth.AccessTokenInterceptor;
import com.apple.ads.auth.AccessTokenProvider;
import com.apple.ads.auth.AppleAdsAuthenticator;
import com.apple.ads.auth.ClientSecretProvider;
import com.apple.ads.auth.EphemeralClientSecretProvider;
import com.apple.ads.auth.InMemoryCachingAccessTokenProvider;
import com.apple.ads.auth.TokenFetchApi;
import com.apple.ads.auth.exception.AccessTokenFetchException;
import com.apple.ads.factory.exception.ApiClientBuildException;
import com.apple.ads.factory.exception.InvalidBuilderConfigurationException;
import com.apple.ads.util.ObjectMapperUtil;
import com.apple.ads.util.OkHttpLoggingUtil;
import com.apple.ads.util.UserAgentInterceptor;
import com.nimbusds.jose.JOSEException;
import jakarta.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * A builder class to assist with constructing an AppleAdsApi instance. Sensible defaults are provided for
 * most options, but a high degree of customization is possible. The resulting client is thread-safe
 * and users should plan to share a single instance across their entire application in order to
 * benefit from connection pooling and minimize calls to the authorization server.
 *
 * <p>Construct a builder by choosing one of the four static entry points based on what
 * authentication material you have available:
 *
 * <ul>
 *   <li>{@link #withPrivateKey(String, String, String, String)} — caller supplies a PEM-encoded EC
 *       private key as a string, plus the client/team/key identifiers. The library handles JWT
 *       client-secret signing and access-token fetching.</li>
 *   <li>{@link #withPrivateKeyPath(String, String, String, Path)} — same as above, but the private
 *       key is read from a filesystem path.</li>
 *   <li>{@link #withClientSecretProvider(String, ClientSecretProvider)} — caller supplies a custom
 *       {@link ClientSecretProvider} (e.g. a class providing a hardcoded client secret). The library
 *       still handles access-token fetching against Apple's authorization server.</li>
 *   <li>{@link #withAccessTokenProvider(AccessTokenProvider)} — caller supplies a fully custom
 *       {@link AccessTokenProvider}. The library does not contact Apple's authorization server at
 *       all; the {@code auth*} configuration setters on this builder are not applicable.</li>
 * </ul>
 *
 * <pre>{@code
 * AppleAdsApiClientBuilder
 *     .withPrivateKey(clientId, teamId, keyId, privateKey)
 *     .build()
 * }</pre>
 */
public class AppleAdsApiClientBuilder {

  private static final String DEFAULT_BASE_URL = "https://api.apple.ads.com/v1/";
  private static final String DEFAULT_AUTH_BASE_URL = "https://appleid.apple.com/";

  private enum AuthMode {PRIVATE_KEY, CLIENT_SECRET_PROVIDER, ACCESS_TOKEN_PROVIDER }

  private final AuthMode mode;

  // populated by the factories based on the chosen mode
  private String clientId;
  private String teamId;
  private String keyId;
  private String privateKeyPem;
  private ClientSecretProvider clientSecretProvider;
  private AccessTokenProvider accessTokenProvider;

  // default base urls for the authorization server and the ads api
  private String baseUrl = DEFAULT_BASE_URL;
  private String authBaseUrl = DEFAULT_AUTH_BASE_URL;

  // optional customization applied last to the respective OkHttpClient.Builder
  private Consumer<OkHttpClient.Builder> apiClientCustomizer;
  private Consumer<OkHttpClient.Builder> authClientCustomizer;

  // optional logging configuration
  private Level apiLogLevel = Level.BASIC;
  private Level authLogLevel = Level.BASIC;
  private HttpLoggingInterceptor.Logger logger;

  // default timeout configuration for the auth client (fixed; not configurable)
  private static final Duration AUTH_CONNECT_TIMEOUT = Duration.ofSeconds(5);
  private static final Duration AUTH_READ_TIMEOUT = Duration.ofSeconds(5);
  private static final Duration AUTH_WRITE_TIMEOUT = Duration.ofSeconds(5);
  private static final Duration AUTH_CALL_TIMEOUT = Duration.ZERO;

  // default timeout configuration for the main API client (configurable)
  private Duration connectTimeout = Duration.ofSeconds(5);
  private Duration readTimeout = Duration.ofSeconds(5);
  private Duration writeTimeout = Duration.ofSeconds(5);
  private Duration callTimeout = Duration.ZERO;

  private AppleAdsApiClientBuilder(AuthMode mode) {
    this.mode = mode;
  }

  /**
   * Creates a builder that authenticates using a PEM-encoded EC private key  along with the
   * identifiers needed to sign and identify the client-secret JWT. The library will produce client
   * secrets internally and exchange them for access tokens against Apple's authorization server.
   *
   * <p>Callers whose key material lives on disk may prefer
   * {@link #withPrivateKeyPath(String, String, String, Path)}.
   *
   * @throws InvalidBuilderConfigurationException if any argument is null or blank
   */
  public static AppleAdsApiClientBuilder withPrivateKey(
      @Nonnull String clientId, @Nonnull String teamId, @Nonnull String keyId,
      @Nonnull String privateKey) {
    require(clientId, "clientId");
    require(teamId, "teamId");
    require(keyId, "keyId");
    require(privateKey, "privateKey");
    AppleAdsApiClientBuilder b = new AppleAdsApiClientBuilder(AuthMode.PRIVATE_KEY);
    b.clientId = clientId;
    b.teamId = teamId;
    b.keyId = keyId;
    b.privateKeyPem = privateKey;
    return b;
  }

  /**
   * Like {@link #withPrivateKey(String, String, String, String)}, but eagerly reads the
   * PEM-encoded EC private key from the given filesystem path (as UTF-8).
   *
   * @throws InvalidBuilderConfigurationException if any argument is null or blank
   * @throws UncheckedIOException                 if the private key file cannot be read
   */
  public static AppleAdsApiClientBuilder withPrivateKeyPath(
      @Nonnull String clientId, @Nonnull String teamId, @Nonnull String keyId,
      @Nonnull Path privateKeyPath) {
    if (privateKeyPath == null) {
      throw new InvalidBuilderConfigurationException("'privateKeyPath' must not be null");
    }
    String pem;
    try {
      pem = Files.readString(privateKeyPath, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new UncheckedIOException(
          "failed to read private key from path: " + privateKeyPath, e);
    }
    return withPrivateKey(clientId, teamId, keyId, pem);
  }

  /**
   * Creates a builder that authenticates using a caller-supplied {@link ClientSecretProvider}
   * instead of the default {@link EphemeralClientSecretProvider}. The library still handles
   * access-token fetching against Apple's authorization server.
   *
   * @throws InvalidBuilderConfigurationException if {@code clientId} is null or blank, or if
   *                                              {@code clientSecretProvider} is null
   */
  public static AppleAdsApiClientBuilder withClientSecretProvider(
      @Nonnull String clientId, @Nonnull ClientSecretProvider clientSecretProvider) {
    require(clientId, "clientId");
    if (clientSecretProvider == null) {
      throw new InvalidBuilderConfigurationException("'clientSecretProvider' must not be null");
    }
    AppleAdsApiClientBuilder b = new AppleAdsApiClientBuilder(AuthMode.CLIENT_SECRET_PROVIDER);
    b.clientId = clientId;
    b.clientSecretProvider = clientSecretProvider;
    return b;
  }

  /**
   * Creates a builder that delegates all access-token acquisition to a caller-supplied
   * {@link AccessTokenProvider}. The library will not contact Apple's authorization server, and
   * the {@code authBaseUrl}, {@code authLogLevel}, and {@code authClientCustomizer} setters on
   * this builder are not applicable — calling any of them will throw.
   *
   * @throws InvalidBuilderConfigurationException if {@code accessTokenProvider} is null
   */
  public static AppleAdsApiClientBuilder withAccessTokenProvider(
      @Nonnull AccessTokenProvider accessTokenProvider) {
    if (accessTokenProvider == null) {
      throw new InvalidBuilderConfigurationException("'accessTokenProvider' must not be null");
    }
    AppleAdsApiClientBuilder b = new AppleAdsApiClientBuilder(AuthMode.ACCESS_TOKEN_PROVIDER);
    b.accessTokenProvider = accessTokenProvider;
    return b;
  }

  /**
   * Base URL for the Apple Ads API. Defaults to {@value DEFAULT_BASE_URL}.
   */
  public AppleAdsApiClientBuilder baseUrl(@Nonnull String baseUrl) {
    if (baseUrl == null) {
      throw new InvalidBuilderConfigurationException("'baseUrl' must not be null");
    }
    this.baseUrl = baseUrl;
    return this;
  }

  /**
   * Base URL for the Apple authorization server used to fetch access tokens. Defaults to
   * {@value DEFAULT_AUTH_BASE_URL}. Not applicable when the builder was created via
   * {@link #withAccessTokenProvider(AccessTokenProvider)}.
   */
  public AppleAdsApiClientBuilder authBaseUrl(@Nonnull String authBaseUrl) {
    rejectIfAccessTokenProviderMode("authBaseUrl");
    if (authBaseUrl == null) {
      throw new InvalidBuilderConfigurationException("'authBaseUrl' must not be null");
    }
    this.authBaseUrl = authBaseUrl;
    return this;
  }

  /**
   * Consumer applied last to the {@link OkHttpClient.Builder} for the main API client, allowing
   * additional customization (e.g. timeouts, proxy setup, additional interceptors) after all
   * defaults have been configured.
   */
  public AppleAdsApiClientBuilder apiClientCustomizer(
      Consumer<OkHttpClient.Builder> apiClientCustomizer) {
    this.apiClientCustomizer = apiClientCustomizer;
    return this;
  }

  /**
   * Consumer applied last to the {@link OkHttpClient.Builder} for the auth client, allowing
   * additional customization (e.g. timeouts, proxy setup, additional interceptors) after all
   * defaults have been configured. Not applicable when the builder was created via
   * {@link #withAccessTokenProvider(AccessTokenProvider)}.
   */
  public AppleAdsApiClientBuilder authClientCustomizer(
      Consumer<OkHttpClient.Builder> authClientCustomizer) {
    rejectIfAccessTokenProviderMode("authClientCustomizer");
    this.authClientCustomizer = authClientCustomizer;
    return this;
  }

  /**
   * Logging level for the main API client. Defaults to {@link Level#BASIC}. Pass {@code null} to
   * disable logging entirely for these API calls.
   */
  public AppleAdsApiClientBuilder apiLogLevel(Level apiLogLevel) {
    this.apiLogLevel = apiLogLevel;
    return this;
  }

  /**
   * Logging level for the authorization server client. Defaults to {@link Level#BASIC}. Pass
   * {@code null} to disable logging entirely for these API calls. {@link Level#BODY} is not
   * permitted as auth request/response bodies may contain sensitive credential data. Not
   * applicable when the builder was created via
   * {@link #withAccessTokenProvider(AccessTokenProvider)}.
   */
  public AppleAdsApiClientBuilder authLogLevel(Level authLogLevel) {
    rejectIfAccessTokenProviderMode("authLogLevel");
    if (authLogLevel == Level.BODY) {
      throw new InvalidBuilderConfigurationException(
          "'authLogLevel' does not permit Level.BODY: request/response bodies on the auth client "
              + "contain sensitive credential data");
    }
    this.authLogLevel = authLogLevel;
    return this;
  }

  /**
   * Custom {@link HttpLoggingInterceptor.Logger} used by both the API and auth logging
   * interceptors. If not set, the default OkHttp logger is used.
   */
  public AppleAdsApiClientBuilder logger(HttpLoggingInterceptor.Logger logger) {
    this.logger = logger;
    return this;
  }

  /**
   * Connect timeout for the main API client. Defaults to 5 seconds.
   */
  public AppleAdsApiClientBuilder connectTimeout(@Nonnull Duration connectTimeout) {
    this.connectTimeout = requireValidTimeout(connectTimeout, "connectTimeout");
    return this;
  }

  /**
   * Read timeout for the main API client. Defaults to 5 seconds.
   */
  public AppleAdsApiClientBuilder readTimeout(@Nonnull Duration readTimeout) {
    this.readTimeout = requireValidTimeout(readTimeout, "readTimeout");
    return this;
  }

  /**
   * Write timeout for the main API client. Defaults to 5 seconds.
   */
  public AppleAdsApiClientBuilder writeTimeout(@Nonnull Duration writeTimeout) {
    this.writeTimeout = requireValidTimeout(writeTimeout, "writeTimeout");
    return this;
  }

  /**
   * Call timeout for the main API client. Defaults to 0 (no timeout). A value of zero means no
   * timeout is enforced.
   */
  public AppleAdsApiClientBuilder callTimeout(@Nonnull Duration callTimeout) {
    this.callTimeout = requireValidTimeout(callTimeout, "callTimeout");
    return this;
  }

  /**
   * Builds and returns a configured {@link AppleAdsApi} instance. When using the default
   * {@link com.apple.ads.auth.AccessTokenProvider}, this method eagerly fetches an access token
   * before returning.
   *
   * @throws ApiClientBuildException if key parsing, JWT signing setup, or the initial access
   *                                 token fetch fails
   */
  public AppleAdsApi build() {
    try {
      OkHttpClient client = resolveOkHttpClient();
      return new Retrofit.Builder()
          .baseUrl(baseUrl)
          .client(client)
          .addConverterFactory(JacksonConverterFactory.create(ObjectMapperUtil.forAdsApi()))
          .build()
          .create(AppleAdsApi.class);
    } catch (JOSEException | AccessTokenFetchException e) {
      throw new ApiClientBuildException("Failed to build API client: " + e.getMessage(), e);
    }
  }

  private OkHttpClient resolveOkHttpClient()
      throws AccessTokenFetchException, JOSEException {
    AccessTokenProvider provider = resolveAccessTokenProvider();

    OkHttpClient.Builder builder = new OkHttpClient.Builder()
        .addInterceptor(new AccessTokenInterceptor(provider))
        .authenticator(new AppleAdsAuthenticator(provider));

    applyTimeouts(builder);

    if (apiClientCustomizer != null) {
      apiClientCustomizer.accept(builder);
    }

    builder.addInterceptor(new UserAgentInterceptor());

    //add logging last so logs reflect the final state of the request
    if (apiLogLevel != null) {
      builder.addInterceptor(logger != null
          ? OkHttpLoggingUtil.safeOkHttpLoggingInterceptor(logger, apiLogLevel)
          : OkHttpLoggingUtil.safeOkHttpLoggingInterceptor(apiLogLevel));
    }

    return builder.build();
  }

  private AccessTokenProvider resolveAccessTokenProvider()
      throws JOSEException, AccessTokenFetchException {
    if (mode == AuthMode.ACCESS_TOKEN_PROVIDER) {
      return accessTokenProvider;
    }

    OkHttpClient.Builder authClientBuilder = new OkHttpClient.Builder();

    if (authLogLevel != null) {
      authClientBuilder.addInterceptor(logger != null
          ? OkHttpLoggingUtil.safeOkHttpLoggingInterceptor(logger, authLogLevel)
          : OkHttpLoggingUtil.safeOkHttpLoggingInterceptor(authLogLevel));
    }

    applyAuthTimeouts(authClientBuilder);

    if (authClientCustomizer != null) {
      authClientCustomizer.accept(authClientBuilder);
    }

    TokenFetchApi tokenFetchApi = new Retrofit.Builder()
        .baseUrl(authBaseUrl)
        .client(authClientBuilder.build())
        .addConverterFactory(JacksonConverterFactory.create(ObjectMapperUtil.forAuthApi()))
        .build()
        .create(TokenFetchApi.class);

    AccessTokenFetcher fetcher = new AccessTokenFetcher(
        tokenFetchApi, clientId, resolveClientSecretProvider());

    return new InMemoryCachingAccessTokenProvider(fetcher);
  }

  private ClientSecretProvider resolveClientSecretProvider() throws JOSEException {
    if (mode == AuthMode.CLIENT_SECRET_PROVIDER) {
      return clientSecretProvider;
    }
    return new EphemeralClientSecretProvider(privateKeyPem, clientId, teamId, keyId);
  }

  private void applyTimeouts(OkHttpClient.Builder builder) {
    builder
        .connectTimeout(connectTimeout.toMillis(), TimeUnit.MILLISECONDS)
        .readTimeout(readTimeout.toMillis(), TimeUnit.MILLISECONDS)
        .writeTimeout(writeTimeout.toMillis(), TimeUnit.MILLISECONDS)
        .callTimeout(callTimeout.toMillis(), TimeUnit.MILLISECONDS);
  }

  private static void applyAuthTimeouts(OkHttpClient.Builder builder) {
    builder
        .connectTimeout(AUTH_CONNECT_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS)
        .readTimeout(AUTH_READ_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS)
        .writeTimeout(AUTH_WRITE_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS)
        .callTimeout(AUTH_CALL_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
  }

  private void rejectIfAccessTokenProviderMode(String field) {
    if (mode == AuthMode.ACCESS_TOKEN_PROVIDER) {
      throw new InvalidBuilderConfigurationException(
          "'" + field + "' is not applicable when the builder is created via "
              + "withAccessTokenProvider: " + field + " is only used to build the default "
              + "AccessTokenProvider, which is bypassed when an AccessTokenProvider is provided");
    }
  }

  private static void require(String value, String field) {
    if (value == null || value.isBlank()) {
      throw new InvalidBuilderConfigurationException("'" + field + "' is required");
    }
  }

  private static Duration requireValidTimeout(Duration value, String field) {
    if (value == null) {
      throw new InvalidBuilderConfigurationException("'" + field + "' must not be null");
    }
    if (value.isNegative()) {
      throw new InvalidBuilderConfigurationException("'" + field + "' must not be negative");
    }
    return value;
  }
}