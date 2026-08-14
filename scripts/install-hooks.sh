#!/usr/bin/env bash
#
# Install git hooks by symlinking from .git/hooks/ to scripts/hooks/.
# Symlinking (rather than copying) means hook updates in scripts/hooks/
# take effect immediately without re-running this script.

set -euo pipefail

REPO_ROOT="$(git rev-parse --show-toplevel)"
HOOKS_SRC="$REPO_ROOT/scripts/hooks"
HOOKS_DST="$REPO_ROOT/.git/hooks"

for hook_path in "$HOOKS_SRC"/*; do
  hook_name="$(basename "$hook_path")"
  dst="$HOOKS_DST/$hook_name"
  chmod +x "$hook_path"
  ln -sfn "../../scripts/hooks/$hook_name" "$dst"
  echo "Installed: $dst -> scripts/hooks/$hook_name"
done