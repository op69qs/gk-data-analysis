#!/usr/bin/env bash

set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
default_system_jar="${repo_root}/deploy-package/app/org-tribe-system-2.1.0.jar"
default_vis_jar="${repo_root}/deploy-package/app/vis-screen-2.3.0.jar"

if (( $# > 2 )); then
  echo "usage: $0 [org-tribe-system.jar] [vis-screen.jar]" >&2
  exit 2
fi

system_jar="${1:-${default_system_jar}}"
vis_jar="${2:-${default_vis_jar}}"

for artifact in "${system_jar}" "${vis_jar}"; do
  if [[ ! -f "${artifact}" ]]; then
    echo "artifact not found: ${artifact}" >&2
    exit 1
  fi
done

for command_name in jar rg; do
  if ! command -v "${command_name}" >/dev/null 2>&1; then
    echo "required command not found: ${command_name}" >&2
    exit 1
  fi
done

tmp_dir="$(mktemp -d "${TMPDIR:-/tmp}/index-scheme-artifacts.XXXXXX")"
trap 'rm -rf "${tmp_dir}"' EXIT

system_extract="${tmp_dir}/system"
vis_extract="${tmp_dir}/vis"
mkdir -p "${system_extract}" "${vis_extract}"

system_jar="$(realpath "${system_jar}")"
vis_jar="$(realpath "${vis_jar}")"

(
  cd "${system_extract}"
  jar xf "${system_jar}"
)
(
  cd "${vis_extract}"
  jar xf "${vis_jar}"
)

static_js="${system_extract}/static/js"
if [[ ! -d "${static_js}" ]]; then
  echo "system artifact has no static/js directory: ${system_jar}" >&2
  exit 1
fi

mapfile -t index_scheme_assets < <(rg -a -l '生成图片' "${static_js}" || true)
if (( ${#index_scheme_assets[@]} == 0 )); then
  echo "system artifact does not contain the production action text: 生成图片" >&2
  exit 1
fi

if rg -a -q '图库标题' "${static_js}"; then
  echo "system artifact still contains the removed field text: 图库标题" >&2
  exit 1
fi

if rg -a -q '/vis/api/indexLibraryScheme/toGallery' "${static_js}"; then
  echo "system artifact still contains the removed toGallery endpoint" >&2
  exit 1
fi

for asset in "${index_scheme_assets[@]}"; do
  if rg -a -q '地图' "${asset}"; then
    echo "index scheme asset still contains an out-of-scope map option: ${asset}" >&2
    exit 1
  fi
done

controller_dir="${vis_extract}/org/jeecg/modules/indexlib/controller"
for controller in \
  IndexSchemeController \
  IndexBarLineController \
  IndexPieController
do
  if [[ ! -f "${controller_dir}/${controller}.class" ]]; then
    echo "vis-screen artifact is missing ${controller}.class" >&2
    exit 1
  fi
done

echo "index scheme artifact verification passed"
