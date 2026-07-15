#!/usr/bin/env python3
"""Convert the production MySQL adm dump into Vastbase compatibility-B scripts."""

from __future__ import annotations

import argparse
import hashlib
import re
from pathlib import Path


OBJECT_MARKER = re.compile(r"(?m)^-- (?:Table|View|Procedure|Function|Event) structure for .+$")
RESERVED_WORDS = {
    "all", "analyse", "analyze", "and", "any", "array", "as", "asc", "asymmetric", "body",
    "both", "buckets", "case", "cast", "check", "checktable", "collate", "column", "constraint",
    "create", "current_catalog", "current_date", "current_role", "current_time", "current_timestamp",
    "current_user", "curtime", "dbcc", "dbtimezone", "default", "deferrable", "desc", "distinct",
    "distinctrow", "do", "else", "enum", "except", "excluded", "false", "fetch", "first",
    "flashback", "for", "foreign", "from", "grant", "group", "groupparent", "having", "in",
    "initially", "intersect", "into", "is", "json_mergepatch", "last_day", "leading", "less",
    "key", "limit", "localtime", "localtimestamp", "logical_data", "logical_file", "maxvalue", "minus",
    "modify", "nocycle", "not", "null", "off", "offset", "on", "only", "or", "order",
    "performance", "physical_only", "placing", "primary", "procedure", "references", "reject",
    "return", "returning", "select", "sessiontimezone", "session_user", "some", "symmetric",
    "sysdate", "table", "then", "to", "trailing", "true", "union", "unique", "using",
    "utc_date", "utc_time", "utc_timestamp", "variadic", "verify", "when", "where", "window", "with",
}

def _lower_unquote(sql: str) -> str:
    def replace_quoted(match: re.Match[str]) -> str:
        identifier = match.group(1).lower()
        requires_quotes = identifier in RESERVED_WORDS or re.fullmatch(r"[a-z_][a-z0-9_]*", identifier) is None
        escaped = identifier.replace('"', '""')
        return f'"{escaped}"' if requires_quotes else identifier

    sql = re.sub(r"`([^`]+)`", replace_quoted, sql)
    return re.sub(
        r"\b([A-Za-z_][A-Za-z0-9_]*)\.([A-Za-z_][A-Za-z0-9_]*)\b",
        lambda match: f"{match.group(1).lower()}.{match.group(2).lower()}",
        sql,
    )


def _split_arguments(arguments: str) -> list[str]:
    result: list[str] = []
    start = 0
    depth = 0
    quote: str | None = None
    index = 0
    while index < len(arguments):
        char = arguments[index]
        if quote:
            if char == quote:
                if index + 1 < len(arguments) and arguments[index + 1] == quote:
                    index += 1
                else:
                    quote = None
        elif char in ("'", '"'):
            quote = char
        elif char == "(":
            depth += 1
        elif char == ")":
            depth -= 1
        elif char == "," and depth == 0:
            result.append(arguments[start:index].strip())
            start = index + 1
        index += 1
    result.append(arguments[start:].strip())
    return result


def normalize_etl_log_calls(sql: str) -> str:
    pattern = re.compile(r"CALL\s+etl\.edw_proc_(error|trace)_log\((.*?)\);", re.I | re.S)

    def replace(match: re.Match[str]) -> str:
        kind = match.group(1).lower()
        arguments = _split_arguments(match.group(2))
        expected = 7 if kind == "error" else 6
        extras = len(arguments) - expected
        if extras > 0:
            del arguments[1 : 1 + extras]
        return f"CALL etl.edw_proc_{kind}_log({','.join(arguments)});"

    return pattern.sub(replace, sql)


def _strip_column_options(line: str) -> str:
    line = re.sub(r"\s+CHARACTER SET\s+\w+", "", line, flags=re.I)
    line = re.sub(r"\s+COLLATE\s+\w+", "", line, flags=re.I)
    line = re.sub(r"\s+COMMENT\s+'(?:''|[^'])*'", "", line, flags=re.I)
    line = re.sub(r"\s+ON UPDATE CURRENT_TIMESTAMP(?:\(\d+\))?", "", line, flags=re.I)
    line = re.sub(r"\b(tinyint|smallint|mediumint|int|integer|bigint)\(\d+\)", r"\1", line, flags=re.I)
    line = re.sub(r"\blongtext\b", "text", line, flags=re.I)
    if re.search(r"\bAUTO_INCREMENT\b", line, flags=re.I):
        line = re.sub(r"\bbigint\b", "bigserial", line, count=1, flags=re.I)
        line = re.sub(r"\b(?:tinyint|smallint|mediumint|int|integer)\b", "serial", line, count=1, flags=re.I)
        line = re.sub(r"\s+DEFAULT\s+[^,\s]+", "", line, flags=re.I)
        line = re.sub(r"\s+AUTO_INCREMENT\b", "", line, flags=re.I)
    return re.sub(r"[ \t]+", " ", line).rstrip()


def _index_name(table_name: str, source_name: str) -> str:
    source_name = source_name.strip('"')
    candidate = re.sub(r"[^a-z0-9_]+", "_", f"{table_name}_{source_name}".lower()).strip("_")
    if len(candidate) <= 63:
        return candidate
    digest = hashlib.sha1(candidate.encode("utf-8")).hexdigest()[:8]
    return f"{candidate[:54]}_{digest}"


def _convert_index(table_name: str, line: str) -> str | None:
    match = re.match(
        r'(?:INDEX|KEY)\s+("[^"]+"|[a-z_][a-z0-9_]*)\s*\((.*)\)\s*(?:USING\s+BTREE)?\s*,?$',
        line,
        flags=re.I,
    )
    if not match:
        return None
    columns = re.sub(r'("[^"]+"|[a-z_][a-z0-9_]*)\(\d+\)', r"\1", match.group(2), flags=re.I)
    index_name = _index_name(table_name, match.group(1))
    return f"CREATE INDEX {index_name} ON adm.{table_name} ({columns});"


def convert_table_block(block: str) -> str:
    block = _lower_unquote(block)
    name_match = re.search(r"CREATE TABLE\s+([a-zA-Z0-9_]+)", block, flags=re.I)
    if not name_match:
        raise ValueError("table block has no CREATE TABLE")
    name = name_match.group(1).lower()
    body_match = re.search(r"CREATE TABLE\s+\w+\s*\((.*)\)\s*ENGINE\s*=?.*?;", block, flags=re.I | re.S)
    if not body_match:
        raise ValueError(f"cannot parse table body: {name}")

    converted_lines: list[str] = []
    indexes: list[str] = []
    for raw_line in body_match.group(1).splitlines():
        line = raw_line.strip()
        if not line:
            continue
        if re.match(r'(?:INDEX|KEY)\s+(?:"[^"]+"|[a-z_][a-z0-9_]*)\s*\(', line, flags=re.I):
            converted_index = _convert_index(name, line)
            if converted_index is None:
                raise ValueError(f"cannot parse index in table {name}: {line}")
            indexes.append(converted_index)
            continue
        if re.match(r"UNIQUE\s+(?:INDEX|KEY)\s+\w+", line, flags=re.I):
            line = re.sub(r"UNIQUE\s+(?:INDEX|KEY)\s+\w+", "UNIQUE", line, count=1, flags=re.I)
        line = re.sub(r"\s+USING\s+BTREE", "", line, flags=re.I)
        converted_lines.append(_strip_column_options(line))

    for index in range(len(converted_lines) - 1):
        if not converted_lines[index].endswith(","):
            converted_lines[index] += ","
    if converted_lines:
        converted_lines[-1] = converted_lines[-1].rstrip(",")
    body = "\n".join(f"  {line}" for line in converted_lines)
    index_sql = "\n".join(indexes)
    suffix = f"\n{index_sql}" if index_sql else ""
    return f"DROP TABLE IF EXISTS adm.{name};\nCREATE TABLE adm.{name} (\n{body}\n);{suffix}\n"


def _qualify_view_sources(sql: str) -> str:
    return re.sub(
        r"\b(from|join)\s+([a-z_][a-z0-9_]*)\b",
        lambda match: f"{match.group(1)} adm.{match.group(2)}",
        sql,
        flags=re.I,
    )


def convert_view_block(block: str) -> str:
    sql = _lower_unquote(block)
    name_match = re.search(r"CREATE.*?\bVIEW\s+([a-zA-Z0-9_]+)\s+AS\s+", sql, flags=re.I | re.S)
    if not name_match:
        raise ValueError("view block has no CREATE VIEW")
    name = name_match.group(1).lower()
    select_sql = sql[name_match.end() :].strip()
    select_sql = select_sql[:-1] if select_sql.endswith(";") else select_sql
    select_sql = _qualify_view_sources(select_sql)
    return f"DROP VIEW IF EXISTS adm.{name};\nCREATE VIEW adm.{name} AS {select_sql};\n"


def _convert_routine_block(block: str, kind: str) -> str:
    sql = re.sub(r"(?im)^delimiter\s+;;\s*$|^delimiter\s+;\s*$", "", block)
    sql = re.sub(r"(?m)^;;\s*$", "", sql)
    sql = re.sub(r"(?m)^-- -+\s*$", "", sql)
    sql = re.sub(r"(?m)^(\s*)#+\s*", r"\1-- ", sql)
    sql = re.sub(r"CREATE\s+DEFINER\s*=\s*`[^`]+`@`[^`]+`\s+", "CREATE ", sql, flags=re.I)
    sql = _lower_unquote(sql)
    sql = re.sub(rf"DROP {kind} IF EXISTS\s+([a-zA-Z0-9_]+)", rf"DROP {kind} IF EXISTS adm.\1", sql, flags=re.I)
    sql = re.sub(rf"CREATE {kind}\s+([a-zA-Z0-9_]+)", rf"CREATE {kind} adm.\1", sql, count=1, flags=re.I)
    sql = re.sub(r"\s+CHARSET\s+\w+(?=\s*(?:BEGIN|COMMENT|DETERMINISTIC|$))", "", sql, flags=re.I)
    sql = sql.replace("@V_RETURN_CODE", "V_RETURN_CODE").replace("@V_ERROR_MSG", "V_ERROR_MSG")
    if "DECLARE EXIT HANDLER FOR SQLEXCEPTION" in sql and "DECLARE V_RETURN_CODE" not in sql:
        sql = re.sub(
            r"(^\s*DECLARE EXIT HANDLER FOR SQLEXCEPTION)",
            "    DECLARE V_RETURN_CODE TEXT;\n    DECLARE V_ERROR_MSG TEXT;\n\\1",
            sql,
            count=1,
            flags=re.M,
        )
    sql = normalize_etl_log_calls(sql)
    return sql.strip() + ";\n/\n"


def convert_event_block(block: str) -> str:
    block = re.sub(r"(?im)^SET\s+FOREIGN_KEY_CHECKS\s*=.*$", "", block)
    sql = re.sub(r"(?im)^delimiter\s+;;\s*$|^delimiter\s+;\s*$", "", block)
    sql = re.sub(r"(?m)^;;\s*$", "", sql)
    sql = re.sub(r"(?m)^-- -+\s*$", "", sql)
    sql = re.sub(r"CREATE\s+DEFINER\s*=\s*`[^`]+`@`[^`]+`\s+", "CREATE ", sql, flags=re.I)
    sql = _lower_unquote(sql)
    name_match = re.search(r"CREATE EVENT\s+([a-zA-Z0-9_]+)", sql, flags=re.I)
    if not name_match:
        raise ValueError("event block has no CREATE EVENT")
    source_name = name_match.group(1).lower()
    event_name = f"adm_{source_name}"
    sql = re.sub(r"DROP EVENT IF EXISTS\s+\w+", f"DROP EVENT IF EXISTS {event_name}", sql, flags=re.I)
    sql = re.sub(r"CREATE EVENT\s+\w+", f"CREATE EVENT {event_name}", sql, count=1, flags=re.I)
    sql = re.sub(r"EVERY\s+'(\d+)'\s+", r"EVERY \1 ", sql, flags=re.I)
    if not re.search(r"\b(?:ENABLE|DISABLE)\b", sql, flags=re.I):
        sql = re.sub(r"(?=\s*DO\s+CALL)", "\nDISABLE", sql, count=1, flags=re.I)
    return sql.strip() + ";\n"


def _blocks(source: str) -> list[tuple[str, str]]:
    matches = list(OBJECT_MARKER.finditer(source))
    result = []
    for index, match in enumerate(matches):
        end = matches[index + 1].start() if index + 1 < len(matches) else len(source)
        marker = match.group(0)
        kind = re.search(r"-- (Table|View|Procedure|Function|Event) structure", marker).group(1).lower()
        result.append((kind, source[match.end() : end]))
    return result


def convert_dump(source_path: Path, output_dir: Path) -> dict[str, int]:
    source = source_path.read_text(encoding="utf-8-sig")
    groups: dict[str, list[str]] = {key: [] for key in ("table", "view", "procedure", "function", "event")}
    for kind, block in _blocks(source):
        groups[kind].append(block)

    output_dir.mkdir(parents=True, exist_ok=True)
    table_blocks = [convert_table_block(block) for block in groups["table"]]
    indexes = [
        line
        for table_block in table_blocks
        for line in table_block.splitlines()
        if line.startswith("CREATE INDEX ")
    ]
    tables = [
        re.sub(r"(?m)^CREATE INDEX .*;\n?", "", table_block)
        for table_block in table_blocks
    ]
    views = [convert_view_block(block) for block in groups["view"]]
    routines = [_convert_routine_block(block, "PROCEDURE") for block in groups["procedure"]]
    routines += [_convert_routine_block(block, "FUNCTION") for block in groups["function"]]
    events = [convert_event_block(block) for block in groups["event"]]

    header = "-- Generated from document/psql/mysql/adm.sql; do not edit by hand.\nCREATE SCHEMA IF NOT EXISTS adm;\nSET search_path TO adm, public;\n\n"
    (output_dir / "013_adm_tables_init.sql").write_text(header + "\n".join(tables + views), encoding="utf-8")
    (output_dir / "014_adm_indexes_init.sql").write_text(header + "\n".join(indexes) + "\n", encoding="utf-8")
    (output_dir / "015_adm_routines_init.sql").write_text(header + "\n".join(routines), encoding="utf-8")
    (output_dir / "016_adm_events_init.sql").write_text(header + "\n".join(events), encoding="utf-8")
    return {
        "tables": len(tables),
        "views": len(views),
        "procedures": len(groups["procedure"]),
        "functions": len(groups["function"]),
        "events": len(events),
    }


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("source", type=Path)
    parser.add_argument("output_dir", type=Path)
    args = parser.parse_args()
    counts = convert_dump(args.source, args.output_dir)
    print(" ".join(f"{key}={value}" for key, value in counts.items()))


if __name__ == "__main__":
    main()
