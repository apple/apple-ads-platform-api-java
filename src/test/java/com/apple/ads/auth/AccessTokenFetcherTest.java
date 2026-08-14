// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.auth;

import static com.apple.ads.auth.AccessTokenFetcher.GRANT_TYPE;
import static com.apple.ads.auth.AccessTokenFetcher.SCOPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.apple.ads.auth.exception.AccessTokenFetchException;
import com.apple.ads.auth.exception.ClientSecretException;
import com.apple.ads.auth.model.AccessTokenResponse;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;
import okio.Timeout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import retrofit2.Call;
import retrofit2.Response;

@ExtendWith(MockitoExtension.class)
class AccessTokenFetcherTest {

  private static final String CLIENT_ID = "test-client-id";
  private static final String CLIENT_SECRET = "test-client-secret";
  private static final String ACCESS_TOKEN = "test-access-token";

  private static final MediaType PLAIN_TEXT = MediaType.parse("text/plain");

  @Mock
  private TokenFetchApi mockApi;
  @Mock
  private ClientSecretProvider mockClientSecretProvider;
  @Mock
  private Call<AccessTokenResponse> mockCall;
  @Mock
  private AccessTokenResponse mockTokenResponse;

  private AccessTokenFetcher fetcher;

  @BeforeEach
  void setUp() throws Exception {
    when(mockClientSecretProvider.getClientSecret()).thenReturn(CLIENT_SECRET);
    lenient().when(mockApi.getAccessToken(GRANT_TYPE, CLIENT_ID, CLIENT_SECRET, SCOPE))
        .thenReturn(mockCall);
    fetcher = new AccessTokenFetcher(mockApi, CLIENT_ID, mockClientSecretProvider);
  }

  @Test
  void getAccessToken_returnsTokenOnSuccess() throws Exception {
    when(mockTokenResponse.getAccessToken()).thenReturn(ACCESS_TOKEN);
    when(mockCall.execute()).thenReturn(Response.success(mockTokenResponse));

    assertEquals(ACCESS_TOKEN, fetcher.getAccessToken().token());
  }

  @Test
  void getAccessToken_passesCorrectFieldsToApi() throws Exception {
    when(mockTokenResponse.getAccessToken()).thenReturn(ACCESS_TOKEN);
    when(mockCall.execute()).thenReturn(Response.success(mockTokenResponse));

    fetcher.getAccessToken();

    verify(mockApi).getAccessToken(GRANT_TYPE, CLIENT_ID, CLIENT_SECRET, SCOPE);
  }

  @Test
  void getAccessToken_errorResponse_exceptionMessageContainsStatusCode() throws Exception {
    when(mockCall.execute()).thenReturn(
        Response.error(403, ResponseBody.create(PLAIN_TEXT, "Forbidden")));

    AccessTokenFetchException ex =
        assertThrows(AccessTokenFetchException.class, () -> fetcher.getAccessToken());

    assertTrue(ex.getMessage().contains("403"),
        "Exception message should include the HTTP status code");
  }

  @Test
  void getAccessToken_UnsuccessfulResponse_throwsAccessTokenFetchException() throws Exception {
    when(mockCall.execute()).thenReturn(
        Response.error(500, ResponseBody.create(PLAIN_TEXT, "Internal Server Error")));

    assertThrows(AccessTokenFetchException.class, () -> fetcher.getAccessToken());
  }

  @Test
  void getAccessToken_errorResponse_hasNoCause_whenBodyIsReadable() throws Exception {
    when(mockCall.execute()).thenReturn(
        Response.error(401, ResponseBody.create(PLAIN_TEXT, "Unauthorized")));

    AccessTokenFetchException ex =
        assertThrows(AccessTokenFetchException.class, () -> fetcher.getAccessToken());

    assertNull(ex.getCause());
  }

  @Test
  void getAccessToken_errorResponse_preservesCause_whenBodyReadFails() throws Exception {
    IOException bodyReadException = new IOException("stream closed");
    when(mockCall.execute()).thenReturn(
        Response.error(401, bodyThatThrowsOnRead(bodyReadException)));

    AccessTokenFetchException ex =
        assertThrows(AccessTokenFetchException.class, () -> fetcher.getAccessToken());

    assertSame(bodyReadException, ex.getCause());
  }

  @Test
  void getAccessToken_errorResponse_messageContainsStatusCode_whenBodyReadFails() throws Exception {
    when(mockCall.execute()).thenReturn(
        Response.error(403, bodyThatThrowsOnRead(new IOException("stream closed"))));

    AccessTokenFetchException ex =
        assertThrows(AccessTokenFetchException.class, () -> fetcher.getAccessToken());

    assertTrue(ex.getMessage().contains("403"));
  }

  @Test
  void getAccessToken_ioException_throwsAccessTokenFetchExceptionWithCause() throws Exception {
    IOException cause = new IOException("network failure");
    when(mockCall.execute()).thenThrow(cause);

    AccessTokenFetchException ex =
        assertThrows(AccessTokenFetchException.class, () -> fetcher.getAccessToken());

    assertSame(cause, ex.getCause());
  }

  @Test
  void getAccessToken_nullResponseBody_throwsAccessTokenFetchException() throws Exception {
    when(mockCall.execute()).thenReturn(Response.success(null));

    assertThrows(AccessTokenFetchException.class, () -> fetcher.getAccessToken());
  }

  @Test
  void getAccessToken_nullAccessTokenInBody_throwsAccessTokenFetchException() throws Exception {
    when(mockTokenResponse.getAccessToken()).thenReturn(null);
    when(mockCall.execute()).thenReturn(Response.success(mockTokenResponse));

    assertThrows(AccessTokenFetchException.class, () -> fetcher.getAccessToken());
  }

  @Test
  void getAccessToken_clientSecretException_wrapsInAccessTokenFetchException() throws Exception {
    when(mockClientSecretProvider.getClientSecret())
        .thenThrow(new ClientSecretException("signing failed"));

    AccessTokenFetchException ex =
        assertThrows(AccessTokenFetchException.class, () -> fetcher.getAccessToken());

    assertInstanceOf(ClientSecretException.class, ex.getCause());
  }

  /**
   * Returns a ResponseBody whose source() throws the given IOException. Since ResponseBody.string()
   * is final and delegates to source(), this causes string() to throw without needing to mock a
   * final method.
   */
  private static ResponseBody bodyThatThrowsOnRead(IOException toThrow) {
    return new ResponseBody() {
      @Override
      public MediaType contentType() {
        return PLAIN_TEXT;
      }

      @Override
      public long contentLength() {
        return -1;
      }

      @Override
      public BufferedSource source() {
        return Okio.buffer(new Source() {
          @Override
          public long read(okio.Buffer sink, long byteCount) throws IOException {
            throw toThrow;
          }

          @Override
          public Timeout timeout() {
            return Timeout.NONE;
          }

          @Override
          public void close() {
          }
        });
      }
    };
  }
}
