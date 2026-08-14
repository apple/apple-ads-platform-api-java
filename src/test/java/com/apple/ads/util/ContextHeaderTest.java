// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ContextHeaderTest {

  @Test
  void fromAdAccountId_string_formatsHeader() {
    assertEquals("adAccountId=12345;", ContextHeader.fromAdAccountId("12345"));
  }

  @Test
  void fromAdAccountId_long_formatsHeader() {
    assertEquals("adAccountId=12345;", ContextHeader.fromAdAccountId(12345L));
  }
}