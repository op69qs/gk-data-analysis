#!/usr/bin/env python3
"""Audit a MySQL dump's Event-rooted routine closure against Vastbase final SQL."""

from __future__ import annotations

import argparse
import json
import re
from collections import defaultdict, deque
from dataclasses import dataclass
from pathlib import Path


DB_RE = re.compile(r"^-- Current Database: `([^`]+)`")
ROUTINE_RE = re.compile(
    r"^CREATE\s+DEFINER=.*?\s+(PROCEDURE|FUNCTION)\s+`?([A-Za-z_][A-Za-z0-9_]*)`?\s*\(",
    re.I,
)
FINAL_ROUTINE_RE = re.compile(
    r"^CREATE(?:\s+OR\s+REPLACE)?\s+(PROCEDURE|FUNCTION)\s+"
    r"([A-Za-z_][A-Za-z0-9_]*)\.([A-Za-z_][A-Za-z0-9_]*)\s*\(",
    re.I | re.M,
)
EVENT_RE = re.compile(r"\bEVENT\s+`([^`]+)`", re.I)
FINAL_EVENT_SOURCE_RE = re.compile(r"COMMENT\s+'source mysql event ([^.']+)\.([^']+)'", re.I)
FINAL_EVENT_RE = re.compile(r"^CREATE\s+EVENT\b", re.I | re.M)
CALL_RE = re.compile(
    r"\bCALL\s+"
    r"(?:(?:`([^`]+)`|([A-Za-z_][A-Za-z0-9_]*))\s*\.\s*)?"
    r"(?:`([^`]+)`|([A-Za-z_][A-Za-z0-9_]*))",
    re.I,
)
DML_RE = re.compile(
    r"\b(INSERT\s+(?:IGNORE\s+)?INTO|REPLACE\s+INTO|UPDATE|DELETE\s+FROM|"
    r"TRUNCATE(?:\s+TABLE)?|CREATE\s+(?:TEMPORARY\s+)?TABLE|"
    r"DROP\s+(?:TEMPORARY\s+)?TABLE(?:\s+IF\s+EXISTS)?)\s+"
    r"((?:`?[A-Za-z_][A-Za-z0-9_]*`?\s*\.\s*)?`?[A-Za-z_][A-Za-z0-9_]*`?)",
    re.I,
)

# The source dump itself contains one broken cross-schema CALL.  The target
# redirects it to the routine that actually exists in the same source dump.
DANGLING_REDIRECTS = {
    "edw.p_trs_budget_income_compare_xin": "indicators_lib.p_trs_budget_income_compare_xin",
}

# Two target-only wrappers split the long visual-screen task into daily and
# month-end branches.  They do not add business work; together they preserve
# the leaf-call set of the source entry point.
ALLOWED_EXTRA_ROUTINES = {
    "visual_screen.p_task_vscreen_daily",
    "visual_screen.p_task_vscreen_month_end",
    # Operational entry points used by dwbi-system-docking. They are outside
    # the Event closure but belong to the production initialization chain.
    "ucloud.ucloud_api_interface_alarm_data",
    "ucloud.ucloud_api_interface_alarm_data_copy1",
    "ucloud.ucloud_api_interface_system_data",
    "ucloud.untitled",
    "upm.upm_proc_api_alarm_summary_alarmlog",
    "upm.upm_proc_api_alarm_summary_interface",
    "upm.upm_proc_api_alarm_summary_netper",
}

# These routines have deliberately changed only at the orchestration layer:
# wrapper delegation, the broken CALL redirect above, or task splitting.
ACCEPTED_EDGE_ADAPTATIONS = {
    "indicators_lib.init_report01",
    "indicators_lib.p_exe_formula",
    "indicators_lib.p_xunhuan_formula",
    "visual_screen.p_task_vscreen",
}

# MySQL schema-qualified TEMPORARY tables cannot be represented directly in
# Vastbase.  The target uses schema-qualified UNLOGGED work tables protected by
# an advisory lock, while retaining the same transformation statements.
ACCEPTED_DML_ADAPTATIONS = {
    "etl.entrance_merge_t_jrtj_dim_value_data",
}


@dataclass(frozen=True, order=True)
class Key:
    schema: str
    name: str

    @classmethod
    def make(cls, schema: str, name: str) -> "Key":
        return cls(schema.lower(), name.lower())

    def __str__(self) -> str:
        return f"{self.schema}.{self.name}"


@dataclass
class Routine:
    key: Key
    kind: str
    sql: str


@dataclass
class Event:
    key: Key
    sql: str


def strip_sql_comments(sql: str) -> str:
    sql = re.sub(r"/\*(?!\!).*?\*/", " ", sql, flags=re.S)
    sql = re.sub(r"(?m)--(?=\s).*?$", " ", sql)
    sql = re.sub(r"(?m)#.*?$", " ", sql)
    return sql


def calls_in(sql: str, default_schema: str) -> set[Key]:
    result = set()
    for match in CALL_RE.finditer(strip_sql_comments(sql)):
        schema = match.group(1) or match.group(2) or default_schema
        name = match.group(3) or match.group(4)
        result.add(Key.make(schema, name))
    return result


def parse_mysql_dump(path: Path) -> tuple[dict[Key, Routine], dict[Key, Event]]:
    routines: dict[Key, Routine] = {}
    events: dict[Key, Event] = {}
    current_db = ""
    section = ""
    routine_lines: list[str] | None = None
    routine_kind = ""
    routine_name = ""
    event_lines: list[str] | None = None
    event_name = ""

    for line in path.read_text(errors="ignore").splitlines(keepends=True):
        db_match = DB_RE.match(line)
        if db_match:
            current_db = db_match.group(1)
        if line.startswith("-- Dumping events for database"):
            section = "events"
        elif line.startswith("-- Dumping routines for database"):
            section = "routines"

        if routine_lines is not None:
            if line.startswith("DELIMITER ;"):
                key = Key.make(current_db, routine_name)
                routines[key] = Routine(key, routine_kind.upper(), "".join(routine_lines))
                routine_lines = None
            else:
                routine_lines.append(line)
            continue

        if event_lines is not None:
            event_lines.append(line)
            if "*/ ;;" in line:
                key = Key.make(current_db, event_name)
                events[key] = Event(key, "".join(event_lines))
                event_lines = None
            continue

        if section == "routines":
            routine_match = ROUTINE_RE.match(line)
            if routine_match:
                routine_kind, routine_name = routine_match.groups()
                routine_lines = [line]
        elif section == "events" and "/*!50106 CREATE*/" in line:
            event_match = EVENT_RE.search(line)
            if event_match:
                event_name = event_match.group(1)
                event_lines = [line]
                if "*/ ;;" in line:
                    key = Key.make(current_db, event_name)
                    events[key] = Event(key, line)
                    event_lines = None

    return routines, events


def function_refs_in(sql: str, default_schema: str, routines: dict[Key, Routine]) -> set[Key]:
    cleaned = strip_sql_comments(sql)
    refs: set[Key] = set()
    functions = [routine for routine in routines.values() if routine.kind == "FUNCTION"]
    for routine in functions:
        if routine.key.schema == default_schema:
            pattern = rf"(?<![A-Za-z0-9_.`])`?{re.escape(routine.key.name)}`?\s*\("
            if re.search(pattern, cleaned, re.I):
                refs.add(routine.key)
        qualified = (
            rf"`?{re.escape(routine.key.schema)}`?\s*\.\s*"
            rf"`?{re.escape(routine.key.name)}`?\s*\("
        )
        if re.search(qualified, cleaned, re.I):
            refs.add(routine.key)
    return refs


def parameter_modes(sql: str) -> tuple[str, ...]:
    start = sql.find("(")
    if start < 0:
        return ()
    depth = 0
    quote = ""
    end = -1
    for i in range(start, len(sql)):
        char = sql[i]
        if quote:
            if char == quote:
                quote = ""
            continue
        if char in "'\"`":
            quote = char
        elif char == "(":
            depth += 1
        elif char == ")":
            depth -= 1
            if depth == 0:
                end = i
                break
    if end < 0 or not sql[start + 1:end].strip():
        return ()
    params = []
    current = []
    depth = 0
    for char in sql[start + 1:end]:
        if char == "(":
            depth += 1
        elif char == ")":
            depth -= 1
        if char == "," and depth == 0:
            params.append("".join(current))
            current = []
        else:
            current.append(char)
    params.append("".join(current))
    modes = []
    for param in params:
        match = re.match(r"\s*(INOUT|IN|OUT)\b", param, re.I)
        modes.append(match.group(1).upper() if match else "IN")
    return tuple(modes)


def dml_targets(sql: str, default_schema: str) -> set[str]:
    targets = set()
    for match in DML_RE.finditer(strip_sql_comments(sql)):
        operation = re.sub(r"\s+", " ", match.group(1).upper())
        identifier = re.sub(r"[`\s]", "", match.group(2)).lower()
        if "." not in identifier:
            identifier = f"{default_schema.lower()}.{identifier}"
        targets.add(f"{operation} {identifier}")
    return targets


def build_closure(
    routines: dict[Key, Routine], events: dict[Key, Event]
) -> tuple[set[Key], set[Key], dict[Key, set[Key]], dict[Key, set[Key]]]:
    event_edges = {key: calls_in(event.sql, key.schema) for key, event in events.items()}
    routine_edges: dict[Key, set[Key]] = {}
    for key, routine in routines.items():
        refs = calls_in(routine.sql, key.schema)
        refs |= function_refs_in(routine.sql, key.schema, routines)
        refs.discard(key)
        routine_edges[key] = refs

    closure: set[Key] = set()
    dangling: set[Key] = set()
    queue = deque(sorted({target for targets in event_edges.values() for target in targets}))
    while queue:
        key = queue.popleft()
        if key in closure or key in dangling:
            continue
        if key not in routines:
            dangling.add(key)
            continue
        closure.add(key)
        queue.extend(sorted(routine_edges[key] - closure - dangling))
    return closure, dangling, event_edges, routine_edges


def parse_final(final_dir: Path) -> tuple[dict[Key, list[str]], set[Key]]:
    routines: dict[Key, list[str]] = defaultdict(list)
    events: set[Key] = set()
    for path in sorted(final_dir.glob("*.sql")):
        sql = path.read_text(errors="ignore")
        for match in FINAL_ROUTINE_RE.finditer(sql):
            _, schema, name = match.groups()
            routines[Key.make(schema, name)].append(path.name)
        for match in FINAL_EVENT_SOURCE_RE.finditer(sql):
            events.add(Key.make(match.group(1), match.group(2)))
    return dict(routines), events


def parse_final_bodies(final_dir: Path) -> dict[Key, Routine]:
    routines: dict[Key, Routine] = {}
    for path in sorted(final_dir.glob("*.sql")):
        sql = path.read_text(errors="ignore")
        matches = list(FINAL_ROUTINE_RE.finditer(sql))
        for index, match in enumerate(matches):
            kind, schema, name = match.groups()
            end = matches[index + 1].start() if index + 1 < len(matches) else len(sql)
            key = Key.make(schema, name)
            routines[key] = Routine(key, kind.upper(), sql[match.start():end])
    return routines


def parse_final_event_bodies(final_dir: Path) -> dict[Key, str]:
    events: dict[Key, str] = {}
    for path in sorted(final_dir.glob("*.sql")):
        sql = path.read_text(errors="ignore")
        matches = list(FINAL_EVENT_RE.finditer(sql))
        for index, match in enumerate(matches):
            end = matches[index + 1].start() if index + 1 < len(matches) else len(sql)
            body = sql[match.start():end]
            source = FINAL_EVENT_SOURCE_RE.search(body)
            if source:
                events[Key.make(source.group(1), source.group(2))] = body
    return events


def event_schedule(sql: str) -> tuple[object, ...]:
    """Return a comparable schedule; normalize MySQL WEEK to Vastbase DAY."""
    interval = re.search(
        r"\bEVERY\s+(\d+)\s+(SECOND|MINUTE|HOUR|DAY|WEEK|MONTH|YEAR)\b",
        sql,
        re.I,
    )
    starts = re.search(r"\bSTARTS\s+'([^']+)'", sql, re.I)
    completion = re.search(r"\bON\s+COMPLETION\s+(NOT\s+)?PRESERVE\b", sql, re.I)
    status = re.search(r"\b(ENABLE|DISABLE)\b", sql, re.I)
    number = int(interval.group(1)) if interval else None
    unit = interval.group(2).upper() if interval else None
    if unit == "WEEK":
        number *= 7
        unit = "DAY"
    return (
        number,
        unit,
        starts.group(1) if starts else None,
        bool(completion and completion.group(1)),
        status.group(1).upper() if status else None,
    )


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("mysql_dump", type=Path)
    parser.add_argument("final_dir", type=Path)
    parser.add_argument("--json", type=Path)
    args = parser.parse_args()

    routines, events = parse_mysql_dump(args.mysql_dump)
    closure, dangling, event_edges, routine_edges = build_closure(routines, events)
    final_routines, final_events = parse_final(args.final_dir)
    final_bodies = parse_final_bodies(args.final_dir)
    final_event_bodies = parse_final_event_bodies(args.final_dir)
    missing = sorted(closure - final_routines.keys())
    extra = sorted(final_routines.keys() - closure)
    unexpected_extra = [key for key in extra if str(key) not in ALLOWED_EXTRA_ROUTINES]
    missing_events = sorted(events.keys() - final_events)
    extra_events = sorted(final_events - events.keys())
    signature_mismatches = []
    edge_mismatches = []
    dml_mismatches = []
    event_semantic_mismatches = []
    for key in sorted(events.keys() & final_event_bodies.keys()):
        source_calls = sorted(calls_in(events[key].sql, key.schema))
        target_calls = sorted(calls_in(final_event_bodies[key], key.schema))
        source_schedule = event_schedule(events[key].sql)
        target_schedule = event_schedule(final_event_bodies[key])
        if source_calls != target_calls or source_schedule != target_schedule:
            event_semantic_mismatches.append(
                {
                    "event": str(key),
                    "source_calls": [str(value) for value in source_calls],
                    "target_calls": [str(value) for value in target_calls],
                    "source_schedule": source_schedule,
                    "target_schedule": target_schedule,
                }
            )
    for key in sorted(closure & final_bodies.keys()):
        source = routines[key]
        target = final_bodies[key]
        source_modes = parameter_modes(source.sql)
        target_modes = parameter_modes(target.sql)
        if source_modes != target_modes:
            signature_mismatches.append(
                {"routine": str(key), "source": source_modes, "target": target_modes}
            )
        source_edges = routine_edges[key]
        target_edges = calls_in(target.sql, key.schema)
        target_edges |= function_refs_in(target.sql, key.schema, routines)
        target_edges.discard(key)
        missing_edges = sorted(source_edges - target_edges)
        extra_edges = sorted(target_edges - source_edges)
        if missing_edges or extra_edges:
            edge_mismatches.append(
                {
                    "routine": str(key),
                    "missing": [str(value) for value in missing_edges],
                    "extra": [str(value) for value in extra_edges],
                }
            )
        source_dml = dml_targets(source.sql, key.schema)
        target_dml = dml_targets(target.sql, key.schema)
        if source_dml != target_dml:
            dml_mismatches.append(
                {
                    "routine": str(key),
                    "missing": sorted(source_dml - target_dml),
                    "extra": sorted(target_dml - source_dml),
                }
            )

    resolved_dangling = {
        key: Key.make(*DANGLING_REDIRECTS[str(key)].split(".", 1))
        for key in dangling
        if str(key) in DANGLING_REDIRECTS
        and Key.make(*DANGLING_REDIRECTS[str(key)].split(".", 1)) in final_routines
    }
    unresolved_dangling = sorted(dangling - resolved_dangling.keys())
    accepted_edge_mismatches = [
        value for value in edge_mismatches
        if value["routine"] in ACCEPTED_EDGE_ADAPTATIONS
    ]
    unresolved_edge_mismatches = [
        value for value in edge_mismatches
        if value["routine"] not in ACCEPTED_EDGE_ADAPTATIONS
    ]
    accepted_dml_mismatches = [
        value for value in dml_mismatches
        if value["routine"] in ACCEPTED_DML_ADAPTATIONS
    ]
    unresolved_dml_mismatches = [
        value for value in dml_mismatches
        if value["routine"] not in ACCEPTED_DML_ADAPTATIONS
    ]

    payload = {
        "source_events": [str(key) for key in sorted(events)],
        "source_routines_total": len(routines),
        "closure": [str(key) for key in sorted(closure)],
        "dangling": [str(key) for key in sorted(dangling)],
        "resolved_dangling": {
            str(key): str(value) for key, value in sorted(resolved_dangling.items())
        },
        "unresolved_dangling": [str(key) for key in unresolved_dangling],
        "missing_final_routines": [str(key) for key in missing],
        "extra_final_routines": [str(key) for key in extra],
        "unexpected_extra_final_routines": [str(key) for key in unexpected_extra],
        "missing_final_events": [str(key) for key in missing_events],
        "extra_final_events": [str(key) for key in extra_events],
        "signature_mismatches": signature_mismatches,
        "event_semantic_mismatches": event_semantic_mismatches,
        "edge_mismatches": edge_mismatches,
        "accepted_edge_adaptations": accepted_edge_mismatches,
        "unresolved_edge_mismatches": unresolved_edge_mismatches,
        "dml_mismatches": dml_mismatches,
        "accepted_dml_adaptations": accepted_dml_mismatches,
        "unresolved_dml_mismatches": unresolved_dml_mismatches,
        "event_edges": {
            str(key): [str(target) for target in sorted(targets)]
            for key, targets in sorted(event_edges.items())
        },
        "routine_edges": {
            str(key): [str(target) for target in sorted(routine_edges[key])]
            for key in sorted(closure)
        },
        "final_locations": {
            str(key): final_routines[key] for key in sorted(closure & final_routines.keys())
        },
    }
    if args.json:
        args.json.write_text(json.dumps(payload, ensure_ascii=False, indent=2) + "\n")

    print(f"SOURCE_EVENTS={len(events)}")
    print(f"SOURCE_ROUTINES={len(routines)}")
    print(f"CLOSURE_ROUTINES={len(closure)}")
    print(f"DANGLING={len(dangling)}")
    print(f"RESOLVED_DANGLING={len(resolved_dangling)}")
    print(f"UNRESOLVED_DANGLING={len(unresolved_dangling)}")
    print(f"FINAL_MISSING_ROUTINES={len(missing)}")
    print(f"FINAL_EXTRA_ROUTINES={len(extra)}")
    print(f"UNEXPECTED_EXTRA_ROUTINES={len(unexpected_extra)}")
    print(f"FINAL_MISSING_EVENTS={len(missing_events)}")
    print(f"FINAL_EXTRA_EVENTS={len(extra_events)}")
    print(f"SIGNATURE_MISMATCHES={len(signature_mismatches)}")
    print(f"EVENT_SEMANTIC_MISMATCHES={len(event_semantic_mismatches)}")
    print(f"EDGE_MISMATCHES={len(edge_mismatches)}")
    print(f"UNRESOLVED_EDGE_MISMATCHES={len(unresolved_edge_mismatches)}")
    print(f"DML_MISMATCHES={len(dml_mismatches)}")
    print(f"UNRESOLVED_DML_MISMATCHES={len(unresolved_dml_mismatches)}")
    for label, values in (
        ("DANGLING", dangling),
        ("MISSING_ROUTINE", missing),
        ("MISSING_EVENT", missing_events),
        ("EXTRA_EVENT", extra_events),
    ):
        for value in values:
            print(f"{label} {value}")
    failed = (
        missing
        or missing_events
        or extra_events
        or unexpected_extra
        or unresolved_dangling
        or signature_mismatches
        or event_semantic_mismatches
        or unresolved_edge_mismatches
        or unresolved_dml_mismatches
    )
    return 1 if failed else 0


if __name__ == "__main__":
    raise SystemExit(main())
