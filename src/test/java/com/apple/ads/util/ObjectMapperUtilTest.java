// Copyright (c) 2026 Apple Inc. Licensed under MIT License.
package com.apple.ads.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Tests for {@link ObjectMapperUtil#forAdsApi()}. Sample POJOs defined in this file intentionally
 * mirror the <em>shape</em> of generated model classes (Jackson annotations, {@code java.time}
 * types, {@link JsonNullable} fields, enums with {@code @JsonValue}/{@code @JsonCreator}) so that
 * these tests verify end-to-end behavior without depending on any actual model class.
 */
class ObjectMapperUtilTest {

  private final ObjectMapper mapper = ObjectMapperUtil.forAdsApi();

  // ---- java.time serialization ----

  @Test
  void forAdsApi_serializesLocalDateAsIsoString() throws Exception {
    Sample sample = new Sample();
    sample.date = LocalDate.of(2024, 5, 1);

    String json = mapper.writeValueAsString(sample);

    assertTrue(json.contains("\"date\":\"2024-05-01\""), json);
  }

  @Test
  void forAdsApi_serializesLocalDateTimeWithMillisecondPrecision() throws Exception {
    Sample sample = new Sample();
    sample.localTimestamp = LocalDateTime.of(2024, 5, 1, 12, 30, 45, 123_000_000);

    String json = mapper.writeValueAsString(sample);

    assertTrue(json.contains("\"localTimestamp\":\"2024-05-01T12:30:45.123\""), json);
  }

  @Test
  void forAdsApi_serializesLocalDateTimeWithZeroSubsecondsAsThreeZeroDigits() throws Exception {
    Sample sample = new Sample();
    sample.localTimestamp = LocalDateTime.of(2024, 5, 1, 12, 30, 45);

    String json = mapper.writeValueAsString(sample);

    assertTrue(json.contains("\"localTimestamp\":\"2024-05-01T12:30:45.000\""), json);
  }

  @Test
  void forAdsApi_serializesLocalDateTimeWithNanoPrecisionTruncatedToMilliseconds() throws Exception {
    Sample sample = new Sample();
    sample.localTimestamp = LocalDateTime.of(2024, 5, 1, 12, 30, 45, 123_456_789);

    String json = mapper.writeValueAsString(sample);

    assertTrue(json.contains("\"localTimestamp\":\"2024-05-01T12:30:45.123\""), json);
  }

  @Test
  void forAdsApi_deserializesLocalDateFromIsoString() throws Exception {
    Sample sample = mapper.readValue("{\"date\":\"2024-05-01\"}", Sample.class);

    assertEquals(LocalDate.of(2024, 5, 1), sample.date);
  }

  // ---- unknown properties ----

  @Test
  void forAdsApi_ignoresUnknownProperties() throws Exception {
    String json = "{\"name\":\"foo\",\"unknownField\":\"bar\",\"anotherUnknown\":42}";

    Sample sample = mapper.readValue(json, Sample.class);

    assertEquals("foo", sample.name);
  }

  // ---- enums ----

  @Test
  void forAdsApi_serializesEnumViaJsonValue() throws Exception {
    Sample sample = new Sample();
    sample.status = SampleEnum.ACTIVE;

    String json = mapper.writeValueAsString(sample);

    assertTrue(json.contains("\"status\":\"ACTIVE\""), json);
  }

  @Test
  void forAdsApi_deserializesKnownEnumValue() throws Exception {
    Sample sample = mapper.readValue("{\"status\":\"ACTIVE\"}", Sample.class);

    assertEquals(SampleEnum.ACTIVE, sample.status);
  }

  @Test
  void forAdsApi_deserializesUnknownEnumValueAsNull() throws Exception {
    Sample sample = mapper.readValue("{\"status\":\"NOT_A_REAL_VALUE\"}", Sample.class);

    assertNull(sample.status);
  }

  // ---- JsonNullable (serialization) ----

  @Test
  void forAdsApi_omitsUndefinedJsonNullableFromSerialization() throws Exception {
    Sample sample = new Sample();
    // nullableField stays as JsonNullable.undefined()

    String json = mapper.writeValueAsString(sample);

    assertFalse(json.contains("nullableField"), json);
  }

  @Test
  void forAdsApi_serializesExplicitNullJsonNullableAsJsonNull() throws Exception {
    Sample sample = new Sample();
    sample.nullableField = JsonNullable.of(null);

    String json = mapper.writeValueAsString(sample);

    assertTrue(json.contains("\"nullableField\":null"), json);
  }

  @Test
  void forAdsApi_serializesPresentJsonNullableAsValue() throws Exception {
    Sample sample = new Sample();
    sample.nullableField = JsonNullable.of("hello");

    String json = mapper.writeValueAsString(sample);

    assertTrue(json.contains("\"nullableField\":\"hello\""), json);
  }

  // ---- JsonNullable (deserialization) ----

  @Test
  void forAdsApi_deserializesMissingFieldAsUndefinedJsonNullable() throws Exception {
    Sample sample = mapper.readValue("{}", Sample.class);

    assertNotNull(sample.nullableField);
    assertFalse(sample.nullableField.isPresent());
  }

  @Test
  void forAdsApi_deserializesExplicitJsonNullAsPresentNullJsonNullable() throws Exception {
    Sample sample = mapper.readValue("{\"nullableField\":null}", Sample.class);

    assertTrue(sample.nullableField.isPresent());
    assertNull(sample.nullableField.get());
  }

  @Test
  void forAdsApi_deserializesPresentJsonNullable() throws Exception {
    Sample sample = mapper.readValue("{\"nullableField\":\"world\"}", Sample.class);

    assertTrue(sample.nullableField.isPresent());
    assertEquals("world", sample.nullableField.get());
  }

  // ---- NON_ABSENT inclusion ----

  @Test
  void forAdsApi_omitsNullReferenceFieldsFromSerialization() throws Exception {
    Sample sample = new Sample();
    sample.name = "only-this";
    // every other reference field remains null

    String json = mapper.writeValueAsString(sample);

    assertEquals("{\"name\":\"only-this\"}", json);
  }

  // ---- common scalar/collection types used in generated models ----

  @Test
  void forAdsApi_bigDecimalRoundTrip() throws Exception {
    Sample sample = new Sample();
    sample.amount = new BigDecimal("123.456");

    Sample round = mapper.readValue(mapper.writeValueAsString(sample), Sample.class);

    assertEquals(0, sample.amount.compareTo(round.amount));
  }

  @Test
  void forAdsApi_uuidRoundTrip() throws Exception {
    Sample sample = new Sample();
    sample.id = UUID.fromString("12345678-1234-1234-1234-123456789012");

    String json = mapper.writeValueAsString(sample);
    Sample round = mapper.readValue(json, Sample.class);

    assertTrue(json.contains("\"id\":\"12345678-1234-1234-1234-123456789012\""), json);
    assertEquals(sample.id, round.id);
  }

  @Test
  void forAdsApi_uriRoundTrip() throws Exception {
    Sample sample = new Sample();
    sample.link = URI.create("https://example.com/path");

    String json = mapper.writeValueAsString(sample);
    Sample round = mapper.readValue(json, Sample.class);

    assertTrue(json.contains("\"link\":\"https://example.com/path\""), json);
    assertEquals(sample.link, round.link);
  }

  @Test
  void forAdsApi_listAndMapRoundTrip() throws Exception {
    Sample sample = new Sample();
    sample.tags = List.of("a", "b", "c");
    sample.metadata = Map.of("k1", "v1", "k2", "v2");

    Sample round = mapper.readValue(mapper.writeValueAsString(sample), Sample.class);

    assertEquals(sample.tags, round.tags);
    assertEquals(sample.metadata, round.metadata);
  }

  @Test
  void forAdsApi_nestedObjectRoundTrip() throws Exception {
    Sample sample = new Sample();
    sample.nested = new Nested();
    sample.nested.label = "inner";
    sample.nested.value = 42;

    Sample round = mapper.readValue(mapper.writeValueAsString(sample), Sample.class);

    assertNotNull(round.nested);
    assertEquals("inner", round.nested.label);
    assertEquals(42, round.nested.value);
  }

  @Test
  void forAdsApi_respectsJsonPropertyRenames() throws Exception {
    Sample sample = new Sample();
    sample.wireName = "value";

    String json = mapper.writeValueAsString(sample);
    Sample round = mapper.readValue("{\"wire_name\":\"roundTrip\"}", Sample.class);

    assertTrue(json.contains("\"wire_name\":\"value\""), json);
    assertFalse(json.contains("wireName"), json);
    assertEquals("roundTrip", round.wireName);
  }

  // ------------------------------------------------------------------
  // Sample POJOs — intentionally mirror the annotation/type surface
  // of the generated model classes without depending on any of them.
  // ------------------------------------------------------------------

  static class Sample {
    public String name;
    public LocalDate date;
    public LocalDateTime localTimestamp;
    public BigDecimal amount;
    public UUID id;
    public URI link;
    public SampleEnum status;
    public JsonNullable<String> nullableField = JsonNullable.undefined();
    public List<String> tags;
    public Map<String, String> metadata;
    public Nested nested;
    @JsonProperty("wire_name")
    public String wireName;
  }

  static class Nested {
    public String label;
    public int value;
  }

  enum SampleEnum {
    ACTIVE("ACTIVE"),
    PAUSED("PAUSED");

    private final String value;

    SampleEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @JsonCreator
    public static SampleEnum fromValue(String value) {
      for (SampleEnum e : values()) {
        if (e.value.equals(value)) {
          return e;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }
}