#!/usr/bin/env bash
set -euo pipefail

if [ $# -ne 2 ]; then
  echo "Usage: $0 <spec-version> <path-to-openapi-spec>"
  exit 1
fi

SPEC_VERSION="$1"
SPEC_FILE="$2"

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROOT_DIR="$(dirname "$SCRIPT_DIR")"

[ -d "$ROOT_DIR/.generated" ] && rm -rf "$ROOT_DIR/.generated"
mkdir -p "$ROOT_DIR/.generated"

rm -rf "$ROOT_DIR/src/main/java/com/apple/ads/model"
rm -rf "$ROOT_DIR/src/main/java/com/apple/ads/api"

openapi-generator-cli generate \
  -i "$SPEC_FILE" \
  -g java \
  -o "$ROOT_DIR/.generated" \
  -t "$ROOT_DIR/generator/templates" \
  -p apiPackage=com.apple.ads.api \
  -p containerDefaultToNull=true \
  -p disallowAdditionalPropertiesIfNotPresent=false \
  -p hideGenerationTimestamp=true \
  -p library=retrofit2 \
  -p modelPackage=com.apple.ads.model \
  -p serializationLibrary=jackson \
  -p useJakartaEe=true \
  -p useOneOfInterfaces=true \
  -p useBeanValidation=true \
  -p dateLibrary=java8-localdatetime \
  -p enumUnknownDefaultCase=true \
  --inline-schema-options SKIP_SCHEMA_REUSE=true \

mkdir -p "$ROOT_DIR/src/main/java/com/apple/ads"
cp -a "$ROOT_DIR/.generated/src/main/java/com/apple/ads/model" "$ROOT_DIR/src/main/java/com/apple/ads"
cp -a "$ROOT_DIR/.generated/src/main/java/com/apple/ads/api" "$ROOT_DIR/src/main/java/com/apple/ads"

PREV_SPEC_VERSION=$(cat "$ROOT_DIR/spec.version" 2>/dev/null || true)
echo "${SPEC_VERSION##*-}" > "$ROOT_DIR/spec.version"
if [ "${SPEC_VERSION##*-}" != "${PREV_SPEC_VERSION##*-}" ]; then
  echo "0" > "$ROOT_DIR/patch.version" # reset patch version upon updates to spec version
fi

rm -rf "$ROOT_DIR/.generated"