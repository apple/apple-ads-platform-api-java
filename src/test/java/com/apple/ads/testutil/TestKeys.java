// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.testutil;

import java.io.StringWriter;
import java.security.KeyPairGenerator;
import java.security.spec.ECGenParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

/**
 * Generates throwaway private keys as PEM strings for tests.
 *
 * <p>Keys are generated fresh on each call so that no key material lives in the repository (which
 * would otherwise trip credential-scanning tools). The emitted PEM formats match the output of the
 * corresponding OpenSSL commands:
 *
 * <ul>
 *   <li>{@link #generateEcPrivateKeyPem()} — SEC1 {@code EC PRIVATE KEY}, equivalent to
 *       {@code openssl ecparam -genkey -name prime256v1 -noout}
 *   <li>{@link #generateRsaPrivateKeyPem()} — PKCS#1 {@code RSA PRIVATE KEY}, equivalent to
 *       {@code openssl genrsa 2048}
 * </ul>
 */
public final class TestKeys {

  private static final BouncyCastleProvider BC = new BouncyCastleProvider();

  private static final String EC_ALGORITHM = "EC";
  private static final String EC_CURVE = "secp256r1";
  private static final String RSA_ALGORITHM = "RSA";
  private static final int RSA_KEY_SIZE = 2048;

  private TestKeys() {
  }

  /**
   * Generates a fresh P-256 (prime256v1 / secp256r1) EC private key in SEC1 PEM form. This is the
   * key type expected by {@code EphemeralClientSecretProvider} for ES256 signing.
   *
   * <p>The key is generated with the BouncyCastle provider so the emitted SEC1 structure carries
   * the named-curve OID; the JDK's default SunEC provider omits it, which downstream PEM parsers
   * (e.g. Nimbus JOSE) reject.
   */
  public static String generateEcPrivateKeyPem() {
    try {
      KeyPairGenerator generator = KeyPairGenerator.getInstance(EC_ALGORITHM, BC);
      generator.initialize(new ECGenParameterSpec(EC_CURVE));
      return toPem(generator.generateKeyPair().getPrivate());
    } catch (Exception e) {
      throw new IllegalStateException("failed to generate EC private key", e);
    }
  }

  /**
   * Generates a fresh 2048-bit RSA private key in PKCS#1 PEM form. Used to exercise the negative
   * path where a non-EC key must be rejected.
   */
  public static String generateRsaPrivateKeyPem() {
    try {
      KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA_ALGORITHM, BC);
      generator.initialize(RSA_KEY_SIZE);
      return toPem(generator.generateKeyPair().getPrivate());
    } catch (Exception e) {
      throw new IllegalStateException("failed to generate RSA private key", e);
    }
  }

  private static String toPem(java.security.PrivateKey privateKey) throws Exception {
    StringWriter out = new StringWriter();
    try (JcaPEMWriter pemWriter = new JcaPEMWriter(out)) {
      pemWriter.writeObject(privateKey);
    }
    return out.toString();
  }
}