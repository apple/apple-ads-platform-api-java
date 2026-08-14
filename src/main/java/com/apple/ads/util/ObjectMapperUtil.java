// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Value;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.openapitools.jackson.nullable.JsonNullableModule;

/**
 * Utility class to assist with constructing ObjectMapper instances configured for the Apple Ads API
 * and for Apple's authorization server. These mappers are paired with the respective
 * {@link retrofit2.converter.jackson.JacksonConverterFactory} instances used by the API and auth
 * clients.
 */
public class ObjectMapperUtil {

  /**
   * Outbound serialization format for {@link LocalDateTime} fields. Pinned to exactly three
   * fractional-second digits so the wire format is stable regardless of the underlying nano
   * precision (e.g. {@code 12:30:45} serializes as {@code 12:30:45.000}; sub-millisecond nanos are
   * truncated).
   */
  static final DateTimeFormatter LOCAL_DATE_TIME_SERIALIZER_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

  private ObjectMapperUtil() {
  }

  /**
   * Creates an {@link ObjectMapper} configured for (de)serializing Apple Ads API request and
   * response bodies. The returned mapper:
   *
   * <ul>
   *   <li>registers {@link JavaTimeModule} to support the {@code java.time} types used by the
   *   generated model classes (e.g. {@code LocalDateTime}, {@code LocalDate});</li>
   *   <li>serializes {@link LocalDateTime} values with exactly three fractional-second digits
   *   (e.g. {@code 2024-05-01T12:30:45.000}) so request bodies always present a fixed-width
   *   millisecond format;</li>
   *   <li>registers {@link JsonNullableModule} to support {@code JsonNullable<T>} fields, which
   *   the generated model classes use to distinguish "absent" from "explicitly null";</li>
   *   <li>emits dates as ISO-8601 strings rather than epoch timestamps;</li>
   *   <li>tolerates unknown response properties so that additive changes to the API do not break
   *   deserialization;</li>
   *   <li>tolerates unknown enum values by deserializing them to {@code null} for the same
   *   reason;</li>
   *   <li>uses {@link JsonInclude.Include#NON_ABSENT} so that unset fields are omitted from
   *   request bodies while explicitly-null {@code JsonNullable} values are preserved (required
   *   for PATCH-style requests where {@code null} means "clear this field").</li>
   * </ul>
   */
  public static ObjectMapper forAdsApi() {
    JavaTimeModule javaTimeModule = new JavaTimeModule();
    javaTimeModule.addSerializer(LocalDateTime.class,
        new LocalDateTimeSerializer(LOCAL_DATE_TIME_SERIALIZER_FORMAT));

    return JsonMapper.builder()
        .addModule(javaTimeModule)
        .addModule(new JsonNullableModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)
        .defaultPropertyInclusion(Value.ALL_NON_ABSENT)
        .build();
  }

  /**
   * Creates an {@link ObjectMapper} configured for (de)serializing requests and responses to
   * Apple's authorization server. The returned mapper tolerates unknown response properties so
   * that additive changes to the token response do not break deserialization.
   */
  public static ObjectMapper forAuthApi() {
    return JsonMapper.builder()
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .build();
  }
}