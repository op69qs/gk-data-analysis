#!/usr/bin/env bash

set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
source_base_config="${repo_root}/org-tribe-system/src/main/resources/application.yml"
deploy_base_config="${repo_root}/deploy-package/config/org-tribe-system/application.yml"
source_dev_config="${repo_root}/org-tribe-system/src/main/resources/application-dev.yml"
deploy_dev_config="${repo_root}/deploy-package/config/org-tribe-system/application-dev.yml"

verify_base_timeout_config() {
  local config="$1"

  if [[ ! -f "${config}" ]]; then
    echo "timeout config not found: ${config}" >&2
    return 1
  fi

  if ! awk '
    /^[^[:space:]]/ {
      in_vis = ($0 == "vis:")
      in_vis_ribbon = 0
      next
    }
    in_vis && /^  [^[:space:]]/ {
      in_vis_ribbon = ($0 == "  ribbon:")
      next
    }
    in_vis && in_vis_ribbon && /^    ReadTimeout: 300000$/ { found++ }
    END { exit(found == 1 ? 0 : 1) }
  ' "${config}"; then
    echo "VIS-specific Ribbon ReadTimeout must be 300000: ${config}" >&2
    return 1
  fi

  if ! awk '
    /^[^[:space:]]/ {
      in_spring = ($0 == "spring:")
      in_profiles = 0
      next
    }
    in_spring && /^  [^[:space:]]/ {
      in_profiles = ($0 == "  profiles:")
      next
    }
    in_spring && in_profiles && /^    active: [^[:space:]]+$/ { found = 1 }
    END { exit(found ? 0 : 1) }
  ' "${config}"; then
    echo "base config must retain an active profile independently of VIS timeout: ${config}" >&2
    return 1
  fi
}

verify_dev_timeout_config() {
  local config="$1"

  if [[ ! -f "${config}" ]]; then
    echo "timeout config not found: ${config}" >&2
    return 1
  fi

  if awk '
    /^[^[:space:]]/ {
      in_vis = ($0 == "vis:")
      in_vis_ribbon = 0
      next
    }
    in_vis && /^  [^[:space:]]/ {
      in_vis_ribbon = ($0 == "  ribbon:")
      next
    }
    in_vis && in_vis_ribbon && /^    ReadTimeout:/ { found = 1 }
    END { exit(found ? 0 : 1) }
  ' "${config}"; then
    echo "VIS-specific timeout must live in base config, not dev profile: ${config}" >&2
    return 1
  fi

  if ! awk '
    /^[^[:space:]]/ {
      in_global_ribbon = ($0 == "ribbon:")
      next
    }
    in_global_ribbon && /^  ReadTimeout: 60000$/ { found = 1 }
    END { exit(found ? 0 : 1) }
  ' "${config}"; then
    echo "global Ribbon ReadTimeout must remain 60000: ${config}" >&2
    return 1
  fi
}

verify_base_timeout_config "${source_base_config}"
verify_base_timeout_config "${deploy_base_config}"
verify_dev_timeout_config "${source_dev_config}"
verify_dev_timeout_config "${deploy_dev_config}"

echo "index-library save runtime contract verification passed"
