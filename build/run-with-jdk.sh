#!/bin/bash

if [ -z "${BASH_VERSION:-}" ]; then
    exec bash "$0" "$@"
fi

set -e

DEFAULT_JDK8_HOME="/path/to/jdk8"
DEFAULT_JDK21_HOME="/path/to/jdk21"
JDK8_HOME="${JDK8_HOME:-$DEFAULT_JDK8_HOME}"
JDK21_HOME="${JDK21_HOME:-$DEFAULT_JDK21_HOME}"

usage() {
    echo "Usage: $0 {8|21} <script-path> [script-args ...]"
}

normalize_script_path() {
    local input_path="$1"

    if [[ "$input_path" == @* ]]; then
        input_path="${input_path#@}"
    fi

    input_path="${input_path//\\//}"

    if [[ "$input_path" = /* ]]; then
        echo "$input_path"
    else
        echo "$PWD/$input_path"
    fi
}

validate_jdk_home() {
    local jdk_name="$1"
    local jdk_home="$2"

    if [ ! -d "$jdk_home" ]; then
        echo "[ERROR] $jdk_name home does not exist: $jdk_home" >&2
        exit 1
    fi

    if [ ! -x "$jdk_home/bin/java" ]; then
        echo "[ERROR] java not found in $jdk_name home: $jdk_home/bin/java" >&2
        exit 1
    fi
}

main() {
    if [ $# -lt 2 ]; then
        usage
        exit 1
    fi

    local jdk_version="$1"
    shift

    local script_input="$1"
    shift

    local selected_jdk_home=""
    case "$jdk_version" in
        8)
            selected_jdk_home="$JDK8_HOME"
            ;;
        21)
            selected_jdk_home="$JDK21_HOME"
            ;;
        *)
            echo "[ERROR] Unsupported JDK version: $jdk_version" >&2
            usage
            exit 1
            ;;
    esac

    validate_jdk_home "JDK $jdk_version" "$selected_jdk_home"

    local script_path
    script_path="$(normalize_script_path "$script_input")"
    local script_dir
    script_dir="$(dirname "$script_path")"
    local script_name
    script_name="$(basename "$script_path")"

    if [ ! -f "$script_path" ]; then
        echo "[ERROR] Script file not found: $script_path" >&2
        exit 1
    fi

    if [ ! -x "$script_path" ]; then
        chmod +x "$script_path"
    fi

    export JAVA_HOME="$selected_jdk_home"
    export PATH="$JAVA_HOME/bin:$PATH"

    echo "[INFO] Switched to JDK $jdk_version"
    echo "[INFO] JAVA_HOME=$JAVA_HOME"

    (
        cd "$script_dir"
        bash "$script_name" "$@"
    )
}

main "$@"
