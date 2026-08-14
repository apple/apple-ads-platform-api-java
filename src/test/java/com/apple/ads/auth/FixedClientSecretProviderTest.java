// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class FixedClientSecretProviderTest {

  private static final String SECRET = "test-client-secret";

  @Test
  void getClientSecret_returnsValueProvidedAtConstruction() {
    FixedClientSecretProvider provider = new FixedClientSecretProvider(SECRET);

    assertEquals(SECRET, provider.getClientSecret());
  }

  @Test
  void getClientSecret_returnsSameValueOnRepeatedCalls() {
    FixedClientSecretProvider provider = new FixedClientSecretProvider(SECRET);

    assertEquals(SECRET, provider.getClientSecret());
    assertEquals(SECRET, provider.getClientSecret());
    assertEquals(SECRET, provider.getClientSecret());
  }

  @Test
  void constructor_nullSecret_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new FixedClientSecretProvider(null));
  }
}