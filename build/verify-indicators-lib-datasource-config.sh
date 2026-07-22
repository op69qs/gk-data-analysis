#!/usr/bin/env bash
set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
source_config="${repo_root}/indicatorsLibv-1.0/src/main/resources/application-dev.yml"
deploy_config="${repo_root}/deploy-package/config/indicatorsLibv-1.0/application-dev.yml"

master_value() {
  local file="$1"
  local key="$2"
  awk -v key="$key" '
    /^        master:[[:space:]]*$/ { in_master = 1; next }
    in_master && $0 !~ /^          / && $0 !~ /^[[:space:]]*$/ { exit }
    in_master && index($0, "          " key ":") == 1 {
      value = substr($0, index($0, ":") + 1)
      sub(/^[[:space:]]*/, "", value)
      sub(/[[:space:]]*#.*$/, "", value)
      quote = sprintf("%c", 39)
      first = substr(value, 1, 1)
      last = substr(value, length(value), 1)
      if ((first == quote && last == quote) || (first == "\"" && last == "\"")) {
        value = substr(value, 2, length(value) - 2)
      }
      print value
      exit
    }
  ' "$file"
}

for key in url username password driver-class-name; do
  source_value="$(master_value "$source_config" "$key")"
  deploy_value="$(master_value "$deploy_config" "$key")"

  if [[ -z "$source_value" ]]; then
    echo "FAIL: source master datasource is missing ${key}" >&2
    exit 1
  fi
  if [[ "$source_value" != "$deploy_value" ]]; then
    echo "FAIL: source and deploy master datasource differ for ${key}" >&2
    exit 1
  fi
done

if [[ "$(master_value "$source_config" url)" != jdbc:postgresql://* ]]; then
  echo "FAIL: master datasource URL is not PostgreSQL" >&2
  exit 1
fi

echo "PASS: indicatorsLib master datasource configuration is complete and synchronized"
