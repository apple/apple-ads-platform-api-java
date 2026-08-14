// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apple.ads.testutil.TestKeys;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EphemeralClientSecretProviderTest {

  private static final String CLIENT_ID = "test-client-id";
  private static final String TEAM_ID = "test-team-id";
  private static final String KEY_ID = "test-key-id";
  private static final String AUDIENCE = "https://appleid.apple.com";

  private static String privateKeyPem;
  private static String rsaPrivateKeyPem;

  @BeforeAll
  static void setup() {
    privateKeyPem = TestKeys.generateEcPrivateKeyPem();
    rsaPrivateKeyPem = TestKeys.generateRsaPrivateKeyPem();
  }

  @Test
  void constructor_withNullPrivateKeyPem_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () ->
        new EphemeralClientSecretProvider(null, CLIENT_ID, TEAM_ID, KEY_ID));
  }

  @Test
  void constructor_withNullClientId_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () ->
        new EphemeralClientSecretProvider(privateKeyPem, null, TEAM_ID, KEY_ID));
  }

  @Test
  void constructor_withNullTeamId_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () ->
        new EphemeralClientSecretProvider(privateKeyPem, CLIENT_ID, null, KEY_ID));
  }

  @Test
  void constructor_withNullKeyId_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () ->
        new EphemeralClientSecretProvider(privateKeyPem, CLIENT_ID, TEAM_ID, null));
  }

  @Test
  void constructor_withNonEcKey_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () ->
        new EphemeralClientSecretProvider(
            rsaPrivateKeyPem, CLIENT_ID, TEAM_ID, KEY_ID));
  }

  @Test
  void getClientSecret_returnsWellFormedThreePartJwt() throws Exception {
    EphemeralClientSecretProvider provider =
        new EphemeralClientSecretProvider(privateKeyPem, CLIENT_ID, TEAM_ID, KEY_ID);

    String jwt = provider.getClientSecret();

    assertNotNull(jwt);
    assertEquals(3, jwt.split("\\.").length, "JWT must have exactly three dot-separated parts");
  }

  @Test
  void getClientSecret_jwtHeaderContainsCorrectAlgorithmAndKeyId() throws Exception {
    EphemeralClientSecretProvider provider =
        new EphemeralClientSecretProvider(privateKeyPem, CLIENT_ID, TEAM_ID, KEY_ID);

    SignedJWT jwt = SignedJWT.parse(provider.getClientSecret());

    assertEquals(JWSAlgorithm.ES256, jwt.getHeader().getAlgorithm());
    assertEquals(KEY_ID, jwt.getHeader().getKeyID());
  }

  @Test
  void getClientSecret_jwtClaimsContainCorrectValues() throws Exception {
    EphemeralClientSecretProvider provider =
        new EphemeralClientSecretProvider(privateKeyPem, CLIENT_ID, TEAM_ID, KEY_ID);

    JWTClaimsSet claims = SignedJWT.parse(provider.getClientSecret()).getJWTClaimsSet();

    assertEquals(CLIENT_ID, claims.getSubject());
    assertEquals(TEAM_ID, claims.getIssuer());
    assertEquals(List.of(AUDIENCE), claims.getAudience());
    assertNotNull(claims.getIssueTime());
    assertTrue(claims.getExpirationTime().after(new Date()),
        "Expiration time must be in the future");
  }

  @Test
  void getClientSecret_jwtSignatureVerifiesWithPublicKey() throws Exception {
    EphemeralClientSecretProvider provider =
        new EphemeralClientSecretProvider(privateKeyPem, CLIENT_ID, TEAM_ID, KEY_ID);

    SignedJWT jwt = SignedJWT.parse(provider.getClientSecret());

    ECKey privateKey = (ECKey) JWK.parseFromPEMEncodedObjects(privateKeyPem);
    JWSVerifier verifier = new ECDSAVerifier(privateKey.toPublicJWK());

    assertTrue(jwt.verify(verifier),
        "JWT signature must verify against the corresponding public key");
  }

  @Test
  void getClientSecret_eachCallProducesANewJwt() throws Exception {
    EphemeralClientSecretProvider provider =
        new EphemeralClientSecretProvider(privateKeyPem, CLIENT_ID, TEAM_ID, KEY_ID);

    String first = provider.getClientSecret();
    // Small sleep so issue times differ
    Thread.sleep(10);
    String second = provider.getClientSecret();

    assertNotEquals(first, second, "Each call to getClientSecret() should produce a distinct JWT");
  }
}
