#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
if [[ "$(basename "${SCRIPT_DIR}")" == "bin" ]]; then
  APP_HOME="$(cd "${SCRIPT_DIR}/.." && pwd)"
else
  APP_HOME="${SCRIPT_DIR}"
fi

APP_DIR="${APP_HOME}/app"
CONFIG_DIR="${APP_HOME}/config"
LIB_DIR="${APP_HOME}/lib"
RUNTIME_DIR="${APP_HOME}/runtime"
LOG_DIR="${RUNTIME_DIR}/logs"
PID_DIR="${RUNTIME_DIR}/pids"
BES_DIR="${APP_HOME}/bes"

REQUIRED_JAVA_MAJOR="${REQUIRED_JAVA_MAJOR:-8}"
SPRING_PROFILE="${SPRING_PROFILES_ACTIVE:-dev}"
JAVA_BIN="${JAVA_HOME:+${JAVA_HOME}/bin/}java"
DEFAULT_SERVICE_XMX="${SERVICE_XMX_DEFAULT:-}"

SERVICES=$(cat <<'EOF'
dwbi-statistical-analysis|dwbi-statisticalAnalysis-*.jar|org.triber.analysis.StatisticalAnalysisApplication|classpath
dwbi-system-docking|dwbi-system-docking-*.jar|org.dockingProjects.SystemDockingApplication|classpath
fixedReport|fixedReport-*.jar|org.fixedReport.FixedReportApplication|classpath
indicatorsLibv-1.0|indicatorsLib-*.jar|org.indicatorsLib.IndicatorsLibApplication|classpath
org-tribe-system|org-tribe-system-*.jar|org.jeecg.JeecgApplication|classpath
seo|seo-*.jar|org.seo.SEOComprehensiveQuery|classpath
vis-screen|vis-screen-*.jar|org.jeecg.VISSystemApplication|classpath|bes
EOF
)

usage() {
  cat <<'EOF'
Usage:
  ./service.sh start [all|module...] [xmx=256m]
  ./service.sh stop [all|module...]
  ./service.sh restart [all|module...] [xmx=256m]
  ./service.sh status [all|module...]
EOF
}

parse_major_version() {
  local version="$1"
  local major

  major=$(echo "$version" | sed -E 's/^v?([0-9]+).*/\1/')
  if [[ -z "$major" || ! "$major" =~ ^[0-9]+$ ]]; then
    return 1
  fi

  if [[ "$major" == "1" ]]; then
    major=$(echo "$version" | sed -E 's/^v?1\.([0-9]+).*/\1/')
    if [[ -z "$major" || ! "$major" =~ ^[0-9]+$ ]]; then
      return 1
    fi
  fi

  printf '%s\n' "$major"
}

require_java_version() {
  if ! command -v "$JAVA_BIN" >/dev/null 2>&1; then
    echo "[ERROR] java command not found: $JAVA_BIN" >&2
    return 1
  fi

  local java_line version major
  java_line=$("$JAVA_BIN" -version 2>&1 | head -n 1)
  version=$(echo "$java_line" | sed -E 's/.*version "([^"]+)".*/\1/')
  if [[ -z "$version" || "$version" == "$java_line" ]]; then
    echo "[ERROR] Failed to parse Java version from: $java_line" >&2
    return 1
  fi

  major=$(parse_major_version "$version") || {
    echo "[ERROR] Failed to parse Java major version from: $version" >&2
    return 1
  }

  if (( major < REQUIRED_JAVA_MAJOR )); then
    echo "[ERROR] Java ${REQUIRED_JAVA_MAJOR}+ is required, current: $version" >&2
    return 1
  fi
}

resolve_modules() {
  if [[ $# -eq 0 ]]; then
    printf '%s\n' all
    return
  fi

  printf '%s\n' "$@"
}

is_all_targeted() {
  local target
  for target in "$@"; do
    if [[ "$target" == "all" ]]; then
      return 0
    fi
  done
  return 1
}

is_selected() {
  local module="$1"
  shift || true

  if [[ $# -eq 0 ]] || is_all_targeted "$@"; then
    return 0
  fi

  local target
  for target in "$@"; do
    if [[ "$target" == "$module" ]]; then
      return 0
    fi
  done
  return 1
}

resolve_jar() {
  local pattern="$1"
  local match

  match=$(compgen -G "${APP_DIR}/${pattern}" | head -n 1 || true)
  if [[ -z "$match" ]]; then
    return 1
  fi

  printf '%s\n' "$match"
}

module_env_suffix() {
  printf '%s\n' "$1" | tr '[:lower:]-.' '[:upper:]__'
}

configured_xmx() {
  local module="$1"
  local xmx_override="${2:-}"
  local env_name="SERVICE_XMX_$(module_env_suffix "$module")"
  local module_xmx="${!env_name:-}"

  if [[ -n "$xmx_override" ]]; then
    printf '%s|%s\n' "$xmx_override" "cli"
    return 0
  fi

  if [[ -n "$module_xmx" ]]; then
    printf '%s|%s\n' "$module_xmx" "$env_name"
    return 0
  fi

  if [[ -n "$DEFAULT_SERVICE_XMX" ]]; then
    printf '%s|%s\n' "$DEFAULT_SERVICE_XMX" "SERVICE_XMX_DEFAULT"
    return 0
  fi

  printf '%s|%s\n' "" "jvm-default"
}

pid_file() {
  printf '%s\n' "${PID_DIR}/$1.pid"
}

get_pid() {
  local module="$1"
  local file pid

  file=$(pid_file "$module")
  if [[ -f "$file" ]]; then
    pid=$(cat "$file")
    if kill -0 "$pid" >/dev/null 2>&1; then
      printf '%s\n' "$pid"
      return 0
    fi
    rm -f "$file"
  fi

  return 1
}

status_module() {
  local module="$1"
  if pid=$(get_pid "$module"); then
    echo "[RUNNING] ${module} pid=${pid}"
  else
    echo "[STOPPED] ${module}"
  fi
}

start_module() {
  local module="$1"
  local pattern="$2"
  local main_class="$3"
  local launch_mode="$4"
  local extra_mode="${5:-}"
  local xmx_override="${6:-}"
  local jar_path log_dir stdout_log stderr_log pid config_dir classpath xmx_info configured_heap xmx_source

  if pid=$(get_pid "$module"); then
    echo "[SKIP] ${module} already running, pid=${pid}"
    return 0
  fi

  jar_path=$(resolve_jar "$pattern") || {
    echo "[ERROR] Missing jar for ${module}: ${pattern}" >&2
    return 1
  }

  config_dir="${CONFIG_DIR}/${module}"
  if [[ ! -d "$config_dir" ]]; then
    echo "[ERROR] Missing config directory for ${module}: ${config_dir}" >&2
    return 1
  fi

  log_dir="${LOG_DIR}/${module}"
  stdout_log="${log_dir}/console.log"
  stderr_log="${log_dir}/console.err.log"
  mkdir -p "$PID_DIR" "$log_dir"

  echo "[START] ${module}"
  classpath="${jar_path}"
  if [[ -d "${LIB_DIR}/${module}" ]]; then
    classpath="${classpath}:${LIB_DIR}/${module}/*"
  fi
  if [[ -d "${LIB_DIR}/common" ]]; then
    classpath="${classpath}:${LIB_DIR}/common/*"
  fi
  xmx_info=$(configured_xmx "$module" "$xmx_override")
  configured_heap="${xmx_info%%|*}"
  xmx_source="${xmx_info#*|}"

  if [[ "$launch_mode" == "fatjar" ]]; then
    local java_args=()
    if [[ -n "$configured_heap" ]]; then
      java_args+=("-Xmx${configured_heap}")
    fi
    java_args+=("-Dserver.bes.basedir=${BES_DIR}/${module}")
    nohup "$JAVA_BIN" \
      "${java_args[@]}" \
      -jar "$jar_path" \
      --spring.profiles.active="${SPRING_PROFILE}" \
      --spring.config.additional-location="file:${config_dir}/" \
      >"$stdout_log" 2>"$stderr_log" &
  else
    local java_args=()
    if [[ "$extra_mode" == "bes" ]]; then
      java_args+=("-Dserver.bes.basedir=${BES_DIR}/${module}")
    fi
    if [[ -n "$configured_heap" ]]; then
      java_args+=("-Xmx${configured_heap}")
    fi
    nohup "$JAVA_BIN" \
      "${java_args[@]}" \
      -cp "$classpath" \
      "$main_class" \
      --spring.profiles.active="${SPRING_PROFILE}" \
      --spring.config.additional-location="file:${config_dir}/" \
      >"$stdout_log" 2>"$stderr_log" &
  fi

  pid=$!
  printf '%s\n' "$pid" > "$(pid_file "$module")"
  if [[ -n "$configured_heap" ]]; then
    echo "[OK] ${module} started, pid=${pid}, Xmx=${configured_heap} (${xmx_source})"
  else
    echo "[OK] ${module} started, pid=${pid}, Xmx=jvm-default"
  fi
}

stop_module() {
  local module="$1"
  local pid file

  file=$(pid_file "$module")
  if ! pid=$(get_pid "$module"); then
    echo "[SKIP] ${module} is not running"
    return 0
  fi

  echo "[STOP] ${module}, pid=${pid}"
  kill "$pid" >/dev/null 2>&1 || true

  for _ in 1 2 3 4 5 6 7 8 9 10; do
    if ! kill -0 "$pid" >/dev/null 2>&1; then
      rm -f "$file"
      echo "[OK] ${module} stopped"
      return 0
    fi
    sleep 1
  done

  kill -9 "$pid" >/dev/null 2>&1 || true
  rm -f "$file"
  echo "[OK] ${module} stopped"
}

main() {
  if [[ $# -lt 1 ]]; then
    usage
    exit 1
  fi

  local action="$1"
  shift || true
  local xmx_override=""
  local targets=()
  local arg
  for arg in "$@"; do
    if [[ "$arg" == xmx=* ]]; then
      xmx_override="${arg#xmx=}"
      if [[ -z "$xmx_override" ]]; then
        echo "[ERROR] Empty xmx value" >&2
        exit 1
      fi
      continue
    fi
    targets+=("$arg")
  done
  if [[ ${#targets[@]} -eq 0 ]]; then
    targets=(all)
  fi

  local module pattern main_class launch_mode extra_mode
  while IFS='|' read -r module pattern main_class launch_mode extra_mode; do
    [[ -z "$module" ]] && continue
    if ! is_selected "$module" "${targets[@]}"; then
      continue
    fi

    case "$action" in
      start)
        require_java_version
        start_module "$module" "$pattern" "$main_class" "$launch_mode" "$extra_mode" "$xmx_override"
        ;;
      stop)
        stop_module "$module"
        ;;
      restart)
        require_java_version
        stop_module "$module"
        start_module "$module" "$pattern" "$main_class" "$launch_mode" "$extra_mode" "$xmx_override"
        ;;
      status)
        status_module "$module"
        ;;
      *)
        usage
        exit 1
        ;;
    esac
  done <<< "$SERVICES"
}

main "$@"
