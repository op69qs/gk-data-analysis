#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
APP_HOME="$(cd "$SCRIPT_DIR/.." && pwd)"
RUN_DIR="$APP_HOME/run"
LOG_DIR="$APP_HOME/logs"
SPRING_PROFILE="${SPRING_PROFILES_ACTIVE:-dev}"
JAVA_CMD="${JAVA_HOME:+$JAVA_HOME/bin/}java"

MODULES=$(cat <<'EOF'
dwbi-statistical-analysis|dwbi-statisticalAnalysis-*.jar|org.triber.analysis.StatisticalAnalysisApplication|classpath
dwbi-system-docking|dwbi-system-docking-*.jar|org.dockingProjects.SystemDockingApplication|classpath
fixedReport|fixedReport-*.jar|org.fixedReport.FixedReportApplication|classpath
indicatorsLibv-1.0|indicatorsLib-*.jar|org.indicatorsLib.IndicatorsLibApplication|classpath
org-tribe-system|org-tribe-system-*.jar|org.jeecg.JeecgApplication|classpath
seo|seo-*.jar|org.seo.ComprehensiveQuery|classpath
vis-screen|vis-screen-*.jar|org.jeecg.JeecgSystemApplication|fatjar
EOF
)

resolve_jar() {
  local pattern="$1"
  local match
  match=$(compgen -G "$APP_HOME/app/$pattern" | head -n 1 || true)
  if [[ -z "$match" ]]; then
    return 1
  fi
  basename "$match"
}

module_pid_file() {
  echo "$RUN_DIR/$1.pid"
}

module_is_running() {
  local module="$1"
  local pid_file
  pid_file=$(module_pid_file "$module")
  if [[ ! -f "$pid_file" ]]; then
    return 1
  fi
  local pid
  pid=$(cat "$pid_file")
  if kill -0 "$pid" >/dev/null 2>&1; then
    return 0
  fi
  rm -f "$pid_file"
  return 1
}

start_module() {
  local module="$1"
  local pattern="$2"
  local main_class="$3"
  local launch_mode="$4"
  local jar_name

  if module_is_running "$module"; then
    echo "[RUNNING] $module"
    return 0
  fi

  jar_name=$(resolve_jar "$pattern") || {
    echo "[MISSING] $module jar not found for pattern $pattern"
    return 1
  }

  mkdir -p "$RUN_DIR" "$LOG_DIR/$module"
  local stdout_log="$LOG_DIR/$module/console.log"
  local stderr_log="$LOG_DIR/$module/console.err.log"

  if [[ "$launch_mode" == "fatjar" ]]; then
    nohup "$JAVA_CMD" \
      -Dserver.bes.basedir="$APP_HOME/bes/$module" \
      -jar "$APP_HOME/app/$jar_name" \
      --spring.profiles.active="$SPRING_PROFILE" \
      --spring.config.additional-location="file:$APP_HOME/config/$module/" \
      >"$stdout_log" 2>"$stderr_log" &
  else
    nohup "$JAVA_CMD" \
      -cp "$APP_HOME/app/$jar_name:$APP_HOME/lib/$module/*" \
      "$main_class" \
      --spring.profiles.active="$SPRING_PROFILE" \
      --spring.config.additional-location="file:$APP_HOME/config/$module/" \
      >"$stdout_log" 2>"$stderr_log" &
  fi

  echo $! > "$(module_pid_file "$module")"
  echo "[STARTED] $module"
}

stop_module() {
  local module="$1"
  local pid_file
  pid_file=$(module_pid_file "$module")

  if [[ ! -f "$pid_file" ]]; then
    echo "[STOPPED] $module"
    return 0
  fi

  local pid
  pid=$(cat "$pid_file")
  if kill -0 "$pid" >/dev/null 2>&1; then
    kill "$pid" >/dev/null 2>&1 || true
  fi
  rm -f "$pid_file"
  echo "[STOPPED] $module"
}

status_module() {
  local module="$1"
  if module_is_running "$module"; then
    echo "[RUNNING] $module"
  else
    echo "[STOPPED] $module"
  fi
}

ACTION="${1:-status}"

while IFS='|' read -r module pattern main_class launch_mode; do
  [[ -z "$module" ]] && continue
  case "$ACTION" in
    start)
      start_module "$module" "$pattern" "$main_class" "$launch_mode"
      ;;
    stop)
      stop_module "$module"
      ;;
    restart)
      stop_module "$module"
      start_module "$module" "$pattern" "$main_class" "$launch_mode"
      ;;
    status)
      status_module "$module"
      ;;
    *)
      echo "Usage: $0 {start|stop|restart|status}"
      exit 1
      ;;
  esac
done <<< "$MODULES"