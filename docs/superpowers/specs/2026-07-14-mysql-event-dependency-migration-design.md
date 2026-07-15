# MySQL Event Dependency Migration Design

## Goal

Migrate every MySQL Event currently defined on `cui02:3308` and the complete
routine dependency closure needed by those Events into the Vastbase
`gk_data_analysis` database. Do not migrate unrelated MySQL system, test, or
historical routines merely because they exist in `information_schema.ROUTINES`.

## Source Baseline

The source of truth is the live MySQL instance `cui02:3308`, cross-checked
against the existing exports under `document/psql/mysql/`.

The live instance contains ten Events:

| Source Event | Schedule | Entry routine |
| --- | --- | --- |
| `dmcode.evt_dmcode_drop_temp_tables` | Every day, 23:30 | `dmcode.p_drop_temp_tables()` |
| `edw.EVT_TRS_CALL_EDW_BUDGET_DATA` | Every day, 22:30:01 | `edw.p_trs_budget_new()` |
| `edw.EVT_TRS_CALL_EDW_CP` | Every day, 17:30 | `edw.proc_trs_guoku_cp()` |
| `indicators_lib.p_init_report01` | Every day, 21:00 | `indicators_lib.init_report01(previous_month_end)` |
| `indicators_lib.p_init_report02` | Every day, 23:00 | `indicators_lib.init_report02(previous_month_end)` |
| `indicators_lib.p_init_report03` | Every day, 02:00 | `indicators_lib.init_report03(previous_month_end)` |
| `indicators_lib.p_xunhuan_formula` | Every day, 18:00 | `indicators_lib.p_xunhuan_formula(current_day)` |
| `ods.pt_gy_files_task` | Every day, 01:00 | `ods.p_pt_gy_files_temp()` |
| `seo.P_task_vs` | Every day, 02:00 | `visual_screen.p_task_vscreen(previous_day)` |
| `visual_screen.P_task_vs` | Every day, 18:15 | `visual_screen.p_task_vscreen(current_day)` |

The MySQL Event definitions are marked `ENABLED`, while the inspected test
instance has `event_scheduler=OFF`. This is an environment-level execution
gate and does not change the enabled state stored on each Event.

## Scope Selection

Routine scope is derived from the ten Events, not from a full export of all
business schemas:

1. Start from each Event body and record its entry routine.
2. Recursively follow schema-qualified and unqualified `CALL` statements.
3. Treat an unqualified call as belonging to the caller's schema.
4. Inspect dynamic SQL strings for generated `CALL` statements.
5. Check references to source-defined business functions separately because
   function calls do not use the `CALL` keyword.
6. Stop when a routine has no further routine or business-function dependency.

The current static analysis identifies 58 `CALL` targets. One target,
`edw.P_TRS_BUDGET_INCOME_COMPARE_XIN`, does not exist in the live MySQL
`information_schema.ROUTINES` catalog. The source-defined closure therefore
contains 57 routines. The existing `document/psql/vastbase/final/` scripts
contain 54 of them.

The three missing source-defined routines are:

| Routine | Current source or candidate | Required action |
| --- | --- | --- |
| `dmcode.p_drop_temp_tables` | Retrieved live with `SHOW CREATE PROCEDURE` | Convert and add to the final bundle |
| `edw.p_trs_budget_new` | Converted candidate in `170_edw_core_executable.sql` | Validate and promote into the final bundle |
| `edw.proc_trs_guoku_cp` | Source definition in `source_routines_edw_etl.sql` | Convert, validate, and add to the final bundle |

The closure count is an assertion to be regenerated during implementation,
not a reason to ignore newly discovered dynamic or function dependencies.

### Dangling MySQL call

The live MySQL catalog contains `edw.P_TRS_BUDGET_INCOME_COMPARE` but does not
contain `edw.P_TRS_BUDGET_INCOME_COMPARE_XIN`. The exported
`indicators_lib.p_xunhuan_formula` nevertheless calls both names. This is a
source-side dangling call that remained hidden while the MySQL Event Scheduler
was globally off.

The existing Vastbase `final/006_indicators_lib_init.sql` already replaces the
dangling second call with
`indicators_lib.P_TRS_BUDGET_INCOME_COMPARE_XIN`, a real migrated procedure
that performs the `_XIN` processing. The implementation preserves this
documented correction and does not invent an `edw` wrapper for a procedure
that does not exist in the source catalog.

## Source Drift Check

The original routine and Event migration commit was created on
2026-05-18. A live `information_schema.ROUTINES.LAST_ALTERED` check found only
seven `trs-test` procedures changed after that timestamp. None belongs to the
Event dependency closure.

For each of the three missing routines and all ten Events, implementation still
captures a fresh `SHOW CREATE` definition before conversion. This ensures the
final scripts are based on the live definition rather than only on repository
history.

## Vastbase Routine Packaging

The new routine definitions follow the existing Vastbase B-compatibility
style and are placed before Event creation in the final execution order.

The preferred packaging is:

- Add a focused final routine script for the three missing dependency routines.
- Keep the existing `001` through `006` scripts unchanged unless validation
  proves an existing dependency definition is wrong.
- Load the focused routine script immediately before `007_events_init.sql`.
- Update `000_run_all.sql` and `final/README.md` to make the order explicit.

This isolates the dependency-gap repair from previously validated routines and
keeps the change reviewable.

## Event Conversion

`007_events_init.sql` becomes the complete ten-Event deliverable.

Vastbase requires Event names to be unique within a database, whereas MySQL
allows the same Event name in different schemas. Every target Event therefore
uses a schema-prefixed name:

| Source Event | Vastbase Event name |
| --- | --- |
| `dmcode.evt_dmcode_drop_temp_tables` | `dmcode_evt_dmcode_drop_temp_tables` |
| `edw.EVT_TRS_CALL_EDW_BUDGET_DATA` | `edw_evt_trs_call_edw_budget_data` |
| `edw.EVT_TRS_CALL_EDW_CP` | `edw_evt_trs_call_edw_cp` |
| `indicators_lib.p_init_report01` | `indicators_lib_p_init_report01` |
| `indicators_lib.p_init_report02` | `indicators_lib_p_init_report02` |
| `indicators_lib.p_init_report03` | `indicators_lib_p_init_report03` |
| `indicators_lib.p_xunhuan_formula` | `indicators_lib_p_xunhuan_formula` |
| `ods.pt_gy_files_task` | `ods_pt_gy_files_task` |
| `seo.P_task_vs` | `seo_p_task_vs` |
| `visual_screen.P_task_vs` | `visual_screen_p_task_vs` |

Each target Event preserves the source interval, start time, completion
behavior, and call arguments. Each definition explicitly uses `ENABLE` so the
object state is the same in test and production.

## Environment-Level Execution Gate

The same Event DDL is deployed to test and production. Environment parameters,
not divergent SQL files, control whether jobs execute.

Test environment:

```text
Event state: ENABLE
enable_prevent_job_task_startup: on
```

Production environment:

```text
Event state: ENABLE
enable_prevent_job_task_startup: off
job_queue_processes: greater than 0
```

The parameter name has inverse semantics: `on` prevents the job thread from
starting; `off` allows it to start. Event deployment must check these values
before creating enabled Events.

## Validation

Validation is divided into non-executing and executing phases.

### Non-executing validation

1. Regenerate the dependency closure and confirm every routine resolves to a
   final Vastbase definition.
2. Confirm the final bundle contains ten uniquely named Events.
3. Compare schedule, start time, completion behavior, and call body against the
   live MySQL definitions.
4. On the Vastbase test database, verify
   `enable_prevent_job_task_startup=on` before loading Event DDL.
5. Run the final SQL bundle with `psql -v ON_ERROR_STOP=1` inside the existing
   `BEGIN`/`ROLLBACK` compile-validation flow.
6. Use `SHOW EVENTS` to verify that all ten Events exist and report enabled
   object state while the global job thread remains blocked.

### Executing validation

Executing procedures may commit internally and mutate business tables, so it
is not part of automatic compile validation. It requires a controlled test
window, known input dates, table-level before/after evidence, and explicit
authorization. Event scheduling remains globally blocked until those checks
finish.

## Operational Safety

- Do not enable the Vastbase job thread during migration or compile testing.
- Do not call data-processing entry routines automatically.
- Preserve all pre-existing uncommitted repository changes.
- Do not store database passwords in newly generated scripts or documentation.
- The configured `vastbase_test` account currently reports as locked after
  failed authentication attempts. Target-database validation waits for account
  unlock; no further login retries are made while it remains locked.

## Completion Criteria

The migration is complete when:

- all ten live MySQL Events have source snapshots and Vastbase definitions;
- all routines and business functions in their recursive dependency closure
  resolve to final Vastbase definitions;
- the three currently missing routines compile in the target compatibility mode;
- the final bundle passes transactional compile validation;
- `SHOW EVENTS` lists ten enabled Events with matching schedules and calls;
- test and production parameter instructions clearly preserve the intended
  environment-level execution gate.
