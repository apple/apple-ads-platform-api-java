# Apple Ads Platform API Java

A Java client library for the Apple Ads Platform API

## Model and Endpoint Documentation

This README serves as the primary documentation for installation and usage of this library. For
information on data models and API endpoints, see the
[Apple Ads Platform API documentation](https://developer.apple.com/documentation/apple-ads-platform-api)
found on Apple's developer website.

## Installation

Install this library from the Central Repository ("Maven Central"). The coordinates
are below. Like nearly any software dependency, you should pin a specific version
and update it only when you explicitly intend to do so.

Maven (`pom.xml`):

```xml
<dependency>
    <groupId>com.apple.ads</groupId>
    <artifactId>apple-ads-platform</artifactId>
    <version>VERSION</version>
</dependency>
```

Gradle (`build.gradle`):

```groovy
implementation "com.apple.ads:apple-ads-platform:VERSION"
```
## Getting Started

This library makes it easy to create a [`Retrofit2`](https://square.github.io/retrofit/)
instance that is ready to call the [Apple Ads Platform API](src/main/java/com/apple/ads/api/AppleAdsApi.java).
The [`AppleAdsApiClientBuilder`](src/main/java/com/apple/ads/factory/AppleAdsApiClientBuilder.java)
accepts necessary information for authentication as well as various optional settings. The resulting
client performs the OAuth flow transparently.

### Client Construction

You can instantiate a client in three ways.

#### Using Your Private Key

The first way to create the client is to provide your private key along with the rest of the
associated metadata. The library creates a client secret using your private key every time
a new access token is needed.

```java
String clientId = "...";
String teamId = "...";
String keyId = "...";
String privateKey = getPrivateKeyFromSecurePlace();

AppleAdsApi api = AppleAdsApiClientBuilder
    .withPrivateKey(clientId, teamId, keyId, privateKey)
    .build();
```

You can also provide a `Path` to a file containing your private key. This route is otherwise the
same as above.

```java
String clientId = "...";
String teamId = "...";
String keyId = "...";
Path privateKeyPath = Path.of("...path/to/your/private/key/file");

AppleAdsApi api = AppleAdsApiClientBuilder
    .withPrivateKeyPath(clientId, teamId, keyId, privateKeyPath)
    .build();
```

#### Using a Custom ClientSecretProvider

If you wish to generate client secrets in a different way (for example generating
them offline and using a fixed one at runtime, or using a separate service for
signing), you can use the builder factory method which accepts an instance of
[`ClientSecretProvider`](src/main/java/com/apple/ads/auth/ClientSecretProvider.java). The library
calls this class whenever a client secret is needed for fetching a new access token.

```java
String clientId = "...";
String clientSecret = "...";

ClientSecretProvider clientSecretProvider = 
    new FixedClientSecretProvider(clientSecret);

AppleAdsApi api = AppleAdsApiClientBuilder
    .withClientSecretProvider(clientId, clientSecretProvider)
    .build();
```

#### Implementing OAuth Yourself

We recommend letting the library handle the OAuth flow. If you have specific requirements, you can
implement it yourself by constructing the client with an
[`AccessTokenProvider`](src/main/java/com/apple/ads/auth/AccessTokenProvider.java).
In this case, the library calls this class before every API request in order to attach an access
token as an HTTP header.

```java
// MyAccessTokenProvider must implement AccessTokenProvider
AccessTokenProvider myTokenProvider = new MyAccessTokenProvider(...);

AppleAdsApi api = AppleAdsApiClientBuilder
    .withAccessTokenProvider(myTokenProvider)
    .build();
```

### Optional Settings

The builder provides the following chainable configuration methods. All have sensible defaults
and can be omitted.

| Method                 | Argument Type                       | Description                                                                                                                                                                                                                                                                                                      | Default                              |
|------------------------|-------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------|
| `apiClientCustomizer`  | `Consumer<OkHttpClient.Builder>`    | Consumer applied last to the main API client's `OkHttpClient.Builder`, allowing additional customization (proxy setup, additional interceptors, etc.). See the [OkHttp](https://square.github.io/okhttp/5.x/okhttp/okhttp3/-ok-http-client/-builder/index.html) documentation to see everything that's possible. | None applied                         |
| `authClientCustomizer` | `Consumer<OkHttpClient.Builder>`    | Same as `apiClientCustomizer`, but for the auth client. Not applicable when the builder was created via `withAccessTokenProvider`.                                                                                                                                                                               | None applied                         |
| `apiLogLevel`          | `HttpLoggingInterceptor.Level`      | Logging level for the main API client. Pass `null` to disable logging entirely for these calls.                                                                                                                                                                                                                  | `Level.BASIC`                        |
| `authLogLevel`         | `HttpLoggingInterceptor.Level`      | Logging level for the auth client. Pass `null` to disable logging. `Level.BODY` is rejected because auth bodies contain credentials. Not applicable when the builder was created via `withAccessTokenProvider`.                                                                                                  | `Level.BASIC`                        |
| `logger`               | `HttpLoggingInterceptor.Logger`     | Custom logger shared by the API and auth logging interceptors.                                                                                                                                                                                                                                                   | OkHttp's default logger              |
| `connectTimeout`       | `Duration`                          | Connect timeout for the main API client.                                                                                                                                                                                                                                                                         | 5 seconds                            |
| `readTimeout`          | `Duration`                          | Read timeout for the main API client.                                                                                                                                                                                                                                                                            | 5 seconds                            |
| `writeTimeout`         | `Duration`                          | Write timeout for the main API client.                                                                                                                                                                                                                                                                           | 5 seconds                            |
| `callTimeout`          | `Duration`                          | Overall call timeout for the main API client. Zero means no timeout is enforced.                                                                                                                                                                                                                                 | `Duration.ZERO` (no timeout)         |

#### Example with Optional Settings

```java
AppleAdsApi api = AppleAdsApiClientBuilder
    .withPrivateKey(clientId, teamId, keyId, privateKey)
    .apiLogLevel(Level.BODY)
    .build();
```

## Examples

#### Query Running Campaigns
```java
AppleAdsApi api = AppleAdsApiClientBuilder
    .withPrivateKey(clientId, teamId, keyId, privateKey)
    .build();

QueryRequest runningCampaignsRequest = new QueryRequest()
    .addFiltersItem(new QueryFilter()
        .field("systemStatus")
        .operator("EQUALS")
        .value("RUNNING"));

String contextHeader = ContextHeader.fromAdAccountId(...);

CampaignQueryResponse response = api.campaignsQueryPost(contextHeader, runningCampaignsRequest)
      .execute();
```
#### Get a Business Brand by ID
```java
AppleAdsApi api = AppleAdsApiClientBuilder
    .withPrivateKey(clientId, teamId, keyId, privateKey)
    .build();

String brandId = "...";
String contextHeader = ContextHeader.fromAdAccountId(...);

BrandsResponse response = api.getBrand(contextHeader, brandId)
    .execute();
```

#### Update a Keyword Bid
```java
AppleAdsApi api = AppleAdsApiClientBuilder
    .withPrivateKey(clientId, teamId, keyId, privateKey)
    .build();

String keywordId = "...";
String contextHeader = ContextHeader.fromAdAccountId(...);

KeywordUpdate keywordUpdate = new KeywordUpdate()
    .bid(new Money()
        .amount("1.00")
        .currency("USD"));

KeywordResponse response = api.keywordsIdPut(keywordId, contextHeader, keywordUpdate)
    .execute();
```

## Keeping Your Credentials Secure

Your private key and client secrets are sensitive credentials. Don't store them as plain text.
Treat access tokens as secrets too. The library does not log any of these values. Do the same if you
choose to add any additional logging or observability through additional customization of the
client.

## Thread Safety

When constructed with a private key or `ClientSecretProvider`, the client is thread-safe. Create a
single instance and share it across your entire application. This maximizes the
benefit of connection pooling and minimizes calls to the OAuth server. If you provide your own
`AccessTokenProvider`, thread-safety depends on the implementation.

## Enum Classes

This library uses enum classes throughout the API model. As the API itself evolves over time, new
enum cases may appear. To prevent deserialization errors when this happens, every enum class contains
a special case `UNKNOWN_DEFAULT_OPEN_API`, which will be selected when deserializing API responses
that contain a string value that doesn't match an existing enum case. You can compare against this
case to detect when this has happened. Keep your library up to date to ensure you have model classes
that match the latest version of the API.

## Development setup

After cloning, install the project's git hooks:

```
./scripts/install-hooks.sh
```

This installs a `pre-commit` hook that auto-formats staged Java files via Spotless and re-stages them. The hook re-stages whole files, so if you use `git add -p` to stage only part of a file, formatting fixes to the unstaged portion will be swept into the commit — stage whole files to avoid surprises.

Spotless also runs as part of `./gradlew build`. To check or fix formatting manually:

```
./gradlew spotlessCheck    # verify
./gradlew spotlessApply    # auto-fix
```

## License

This project is released under the MIT License. See [LICENSE](LICENSE) for details.

This project includes third-party software components; see [ACKNOWLEDGEMENTS](ACKNOWLEDGEMENTS) for attribution.