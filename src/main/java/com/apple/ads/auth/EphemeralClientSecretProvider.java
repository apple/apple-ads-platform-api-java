// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.auth;

import com.apple.ads.auth.exception.ClientSecretException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.Nonnull;
import java.util.Date;

/**
 * A ClientSecretGenerator that simply creates a new client secret each time one is requested.
 */
public class EphemeralClientSecretProvider implements ClientSecretProvider {

  private static final String AUDIENCE = "https://appleid.apple.com";
  private static final long EXPIRY_SECONDS = 86400L * 180;

  private final ECDSASigner signer;
  private final JWSHeader jwsHeader;
  private final String clientId;
  private final String teamId;

  public EphemeralClientSecretProvider(@Nonnull String privateKeyPem, @Nonnull String clientId,
      @Nonnull String teamId, @Nonnull String keyId) throws JOSEException {
    if (privateKeyPem == null) {
      throw new IllegalArgumentException("privateKeyPem must not be null");
    }
    if (clientId == null) {
      throw new IllegalArgumentException("clientId must not be null");
    }
    if (teamId == null) {
      throw new IllegalArgumentException("teamId must not be null");
    }
    if (keyId == null) {
      throw new IllegalArgumentException("keyId must not be null");
    }
    this.clientId = clientId;
    this.teamId = teamId;
    JWK jwk = JWK.parseFromPEMEncodedObjects(privateKeyPem);
    if (!(jwk instanceof ECKey)) {
      throw new IllegalArgumentException(
          "Expected an EC private key but got " + jwk.getKeyType().getValue());
    }
    this.signer = new ECDSASigner((ECKey) jwk);
    this.jwsHeader = new JWSHeader.Builder(JWSAlgorithm.ES256)
        .keyID(keyId)
        .build();
  }

  @Override
  public String getClientSecret() throws ClientSecretException {
    long now = System.currentTimeMillis();

    JWTClaimsSet claims = new JWTClaimsSet.Builder()
        .subject(clientId)
        .audience(AUDIENCE)
        .issueTime(new Date(now))
        .expirationTime(new Date(now + EXPIRY_SECONDS * 1000))
        .issuer(teamId)
        .build();

    SignedJWT jwt = new SignedJWT(jwsHeader, claims);
    try {
      jwt.sign(signer);
    } catch (JOSEException e) {
      throw new ClientSecretException("Failed to sign client secret JWT", e);
    }

    return jwt.serialize();
  }
}

