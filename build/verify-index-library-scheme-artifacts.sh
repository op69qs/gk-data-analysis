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

absolute_artifact_path() {
  local artifact="$1"
  local artifact_dir
  local artifact_name

  if [[ "${artifact}" == /* ]]; then
    printf '%s\n' "${artifact}"
    return
  fi

  artifact_dir="$(dirname -- "${artifact}")"
  artifact_name="$(basename -- "${artifact}")"
  printf '%s/%s\n' "$(cd "${artifact_dir}" && pwd -P)" "${artifact_name}"
}

tmp_dir="$(mktemp -d "${TMPDIR:-/tmp}/index-scheme-artifacts.XXXXXX")"
trap 'rm -rf -- "${tmp_dir}"' EXIT

system_extract="${tmp_dir}/system"
vis_extract="${tmp_dir}/vis"
mkdir -p "${system_extract}" "${vis_extract}"

system_jar="$(absolute_artifact_path "${system_jar}")"
vis_jar="$(absolute_artifact_path "${vis_jar}")"

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

index_html="${system_extract}/static/index.html"
if [[ ! -f "${index_html}" ]]; then
  echo "system artifact has no static/index.html: ${system_jar}" >&2
  exit 1
fi

declare -a referenced_assets=()
declare -A seen_referenced_assets=()
html_js_attribute_pattern="(?i)(?:src|href)\\s*=\\s*(?:\"[^\"]+\\.js(?:\\?[^\"[:space:]]*)?\"|'[^']+\\.js(?:\\?[^'[:space:]]*)?'|[^[:space:]>\"']+\\.js(?:\\?[^[:space:]>\"']*)?)"

while IFS= read -r attribute; do
  reference="${attribute#*=}"
  reference="${reference#"${reference%%[![:space:]]*}"}"
  reference="${reference%"${reference##*[![:space:]]}"}"
  reference="${reference#\"}"
  reference="${reference%\"}"
  reference="${reference#\'}"
  reference="${reference%\'}"
  reference="${reference%%\?*}"
  reference="${reference%%\#*}"

  case "${reference}" in
    http://*|https://*|//*|data:*)
      continue
      ;;
  esac

  reference="${reference#/}"
  reference="${reference#./}"
  reference="${reference#static/}"

  case "/${reference}/" in
    */../*|*/./*)
      echo "index.html contains an unsafe JavaScript path: ${reference}" >&2
      exit 1
      ;;
  esac

  asset="${system_extract}/static/${reference}"
  if [[ ! -f "${asset}" ]]; then
    echo "index.html references a missing JavaScript asset: ${reference}" >&2
    exit 1
  fi

  if [[ -z "${seen_referenced_assets["${asset}"]+present}" ]]; then
    referenced_assets+=("${asset}")
    seen_referenced_assets["${asset}"]=1
  fi
done < <(rg -o -N --pcre2 "${html_js_attribute_pattern}" "${index_html}" || true)

if (( ${#referenced_assets[@]} == 0 )); then
  echo "system artifact index.html references no local JavaScript assets" >&2
  exit 1
fi

declare -a index_scheme_assets=()
for asset in "${referenced_assets[@]}"; do
  if rg -a -q -F 'indexLibraryScheme/toGallery' "${asset}"; then
    echo "referenced system asset still contains the removed toGallery endpoint: ${asset}" >&2
    exit 1
  fi

  is_index_scheme_asset=true
  for signature in \
    '生成图片' \
    'IndexBarLine/getIndexBarLineData' \
    'IndexPie/getIndexPieData' \
    '柱状图' \
    '折线图' \
    '饼图' \
    '柱状折线图'
  do
    if ! rg -a -q -F "${signature}" "${asset}"; then
      is_index_scheme_asset=false
      break
    fi
  done

  if [[ "${is_index_scheme_asset}" == true ]]; then
    index_scheme_assets+=("${asset}")
  fi
done

if (( ${#index_scheme_assets[@]} == 0 )); then
  echo "referenced assets do not contain the complete production index scheme feature" >&2
  exit 1
fi

map_option_pattern="[\"']?(value|chartType|chart_type)[\"']?\\s*:\\s*[\"']map[\"']|IndexMap"
for asset in "${index_scheme_assets[@]}"; do
  if rg -a -q -F '图库标题' "${asset}"; then
    echo "index scheme asset still contains the removed field text: 图库标题 (${asset})" >&2
    exit 1
  fi

  # Shared chunks legitimately contain ECharts series with type:"map".
  # Reject only signatures used by the removed index-scheme map option.
  if rg -a -q "${map_option_pattern}" "${asset}"; then
    echo "index scheme asset still contains an out-of-scope map option signature: ${asset}" >&2
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
