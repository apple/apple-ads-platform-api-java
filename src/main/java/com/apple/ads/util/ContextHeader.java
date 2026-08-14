// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.util;

import jakarta.annotation.Nonnull;

public class ContextHeader {

  private static final String TEMPLATE = "adAccountId=%s;";

  private ContextHeader() {}

  public static String fromAdAccountId(@Nonnull String adAccountId) {
    return TEMPLATE.formatted(adAccountId);
  }

  public static String fromAdAccountId(@Nonnull Long adAccountId) {
    return fromAdAccountId(Long.toString(adAccountId));
  }
}
