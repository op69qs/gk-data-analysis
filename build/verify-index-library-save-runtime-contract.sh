#!/usr/bin/env bash

set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
source_config="${repo_root}/org-tribe-system/src/main/resources/application-dev.yml"
deploy_config="${repo_root}/deploy-package/config/org-tribe-system/application-dev.yml"

verify_timeout_config() {
  local config="$1"

  if [[ ! -f "${config}" ]]; then
    echo "timeout config not found: ${config}" >&2
    return 1
  fi

  if ! awk '
    /^vis:$/ { in_vis = 1; next }
    in_vis && /^[^[:space:]]/ { in_vis = 0 }
    in_vis && /^  ribbon:$/ { in_vis_ribbon = 1; next }
    in_vis_ribbon && /^    ReadTimeout: 300000$/ { found = 1 }
    END { exit(found ? 0 : 1) }
  ' "${config}"; then
    echo "VIS-specific Ribbon ReadTimeout must be 300000: ${config}" >&2
    return 1
  fi

  if ! awk '
    /^ribbon:$/ { in_global_ribbon = 1; next }
    in_global_ribbon && /^[^[:space:]]/ { in_global_ribbon = 0 }
    in_global_ribbon && /^  ReadTimeout: 60000$/ { found = 1 }
    END { exit(found ? 0 : 1) }
  ' "${config}"; then
    echo "global Ribbon ReadTimeout must remain 60000: ${config}" >&2
    return 1
  fi
}

verify_timeout_config "${source_config}"
verify_timeout_config "${deploy_config}"

echo "index-library save runtime contract verification passed"
