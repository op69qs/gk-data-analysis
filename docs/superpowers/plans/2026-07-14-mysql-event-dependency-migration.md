# MySQL Event Dependency Migration Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Deliver ten Vastbase Events and every source-defined routine in their recursive dependency closure, while preserving enabled Event state and using the environment-level job-thread switch as the execution gate.

**Architecture:** Treat the ten live MySQL Events as graph roots, verify the 58 call targets against the live routine catalog, and preserve the existing correction for the one dangling MySQL call. Add one focused routine-gap script immediately before the existing Event script, expand the Event script from seven disabled Events to ten enabled Events, and validate the complete bundle without executing business procedures.

**Tech Stack:** MySQL/MariaDB client, Vastbase G100 in B compatibility mode, `psql`/`vsql`, Perl for static dependency auditing, SQL migration files, Bash verification commands.

## Global Constraints

- The source of truth is live MySQL `cui02:3308`, cross-checked with `document/psql/mysql/`.
- Scope is the ten Events and their recursive routine/business-function dependency closure, not all routines in the server.
- Preserve the existing correction from nonexistent `edw.P_TRS_BUDGET_INCOME_COMPARE_XIN` to real `indicators_lib.P_TRS_BUDGET_INCOME_COMPARE_XIN`.
- All ten Vastbase Event objects must be explicitly `ENABLE`.
- Test must have `enable_prevent_job_task_startup=on`; production must use `off` and `job_queue_processes > 0`.
- Do not execute data-processing entry routines during automatic verification.
- Do not store passwords in scripts, documentation, shell history, or commits.
- Preserve unrelated tracked and untracked workspace changes.
- Do not retry the target Vastbase login until `vastbase_test` has been unlocked.

---

## File Structure

- Create `document/psql/tools/verify_event_dependency_closure.pl`: reproducible static dependency-closure and final-coverage audit.
- Create `document/psql/mysql/source_routines_dmcode.sql`: raw live MySQL DDL for `dmcode.p_drop_temp_tables`.
- Create `document/psql/mysql/source_events_dmcode.sql`: raw live MySQL DDL for `dmcode.evt_dmcode_drop_temp_tables`.
- Create `document/psql/vastbase/event_dependency_migration_inventory.md`: source Event, target Event, entry routine, closure, dangling-call, and delivery status evidence.
- Create `document/psql/vastbase/final/006a_event_dependency_routines_init.sql`: the three missing source-defined routines.
- Modify `document/psql/vastbase/final/007_events_init.sql`: ten explicitly enabled Events.
- Modify `document/psql/vastbase/final/000_run_all.sql`: load `006a` before `007` and update progress counters.
- Modify `document/psql/vastbase/final/README.md`: execution order, enabled-object/global-gate model, and validation instructions.
- Modify `document/psql/README.md`: correct source and target counts and index the new artifacts.

### Task 1: Make dependency scope reproducible and capture missing source DDL

**Files:**
- Create: `document/psql/tools/verify_event_dependency_closure.pl`
- Create: `document/psql/mysql/source_routines_dmcode.sql`
- Create: `document/psql/mysql/source_events_dmcode.sql`
- Create: `document/psql/vastbase/event_dependency_migration_inventory.md`
- Modify: `document/psql/README.md`

**Interfaces:**
- Consumes: `source_routines.sql`, `source_routines_edw_etl.sql`, `source_events.sql`, `source_events_edw_etl.sql`, and live `SHOW CREATE` output.
- Produces: deterministic summary `CALL_TARGETS=58`, `SOURCE_DEFINED=57`, `FINAL_PRESENT=54`, `SOURCE_DEFINED_MISSING=3`, plus the dangling-call name.

- [ ] **Step 1: Write the dependency-audit script with failing expectations**

The script must parse routine sections introduced by `-- PROCEDURE` or
`-- FUNCTION`, resolve unqualified `CALL` names to the caller schema, seed the
nine unique Event entry routines, add `dmcode.p_drop_temp_tables` as the tenth
Event's entry routine, and compare the closure with `final/*.sql` definitions.

Its required terminal assertions are:

```perl
die "expected 58 CALL targets\n" unless keys(%seen) == 58;
die "expected dangling edw call\n"
    unless $dangling{'edw.p_trs_budget_income_compare_xin'};
die "expected 57 source-defined routines\n"
    unless grep($known{$_}, keys(%seen)) == 57;
```

Before adding the dmcode source snapshot and final gap script, its report must
identify:

```text
SOURCE_DEFINED_MISSING dmcode.p_drop_temp_tables
SOURCE_DEFINED_MISSING edw.p_trs_budget_new
SOURCE_DEFINED_MISSING edw.proc_trs_guoku_cp
DANGLING edw.p_trs_budget_income_compare_xin
```

- [ ] **Step 2: Run the audit and verify the baseline fails final coverage**

Run:

```bash
perl document/psql/tools/verify_event_dependency_closure.pl
```

Expected: exit nonzero because the three source-defined routines are absent
from `final/`, while the report still distinguishes the dangling `edw` call.

- [ ] **Step 3: Capture the dmcode raw source definitions**

Use the live `SHOW CREATE PROCEDURE` and `SHOW CREATE EVENT` results already
verified in the investigation. Store MySQL syntax unchanged, including
`DEFINER`, delimiters, schedule, `ON COMPLETION PRESERVE`, and `ENABLE`.

In the same read-only pass, refresh `SHOW CREATE` for `edw.p_trs_budget_new`,
`edw.proc_trs_guoku_cp`, and all nine previously exported Events. Diff those
live definitions against `source_routines_edw_etl.sql`, `source_events.sql`,
and `source_events_edw_etl.sql`. If a live definition differs, update the raw
source snapshot before converting it; do not silently prefer the old export.

The Event snapshot must contain:

```sql
DROP EVENT IF EXISTS `dmcode`.`evt_dmcode_drop_temp_tables`;
DELIMITER $$
CREATE DEFINER=`root`@`%` EVENT `evt_dmcode_drop_temp_tables`
ON SCHEDULE EVERY 1 DAY STARTS '2017-08-31 23:30:00'
ON COMPLETION PRESERVE ENABLE
DO BEGIN
    CALL dmcode.p_drop_temp_tables();
END
$$
DELIMITER ;
```

The routine snapshot must preserve the cursor over
`information_schema.TABLES`, the `new_tree_temp%`/length-29 filter, and the
dynamic `DROP TABLE dmcode.<name>` behavior.

- [ ] **Step 4: Write the dependency inventory**

Document all ten source/target Event names, nine unique entry routines, 58
call targets, 57 real source routines, 54 existing final routines, three
missing routines, and the nonexistent `edw..._XIN` call corrected by
`final/006_indicators_lib_init.sql`.

- [ ] **Step 5: Re-run the audit**

Expected: the dmcode source definition is now known, but final coverage still
fails on exactly three routines.

- [ ] **Step 6: Commit the source and audit increment**

```bash
git add document/psql/tools/verify_event_dependency_closure.pl \
  document/psql/mysql/source_routines_dmcode.sql \
  document/psql/mysql/source_events_dmcode.sql \
  document/psql/vastbase/event_dependency_migration_inventory.md \
  document/psql/README.md
git commit -m "docs: capture event dependency migration scope"
```

### Task 2: Add the three missing Vastbase routines

**Files:**
- Create: `document/psql/vastbase/final/006a_event_dependency_routines_init.sql`
- Test: `document/psql/tools/verify_event_dependency_closure.pl`

**Interfaces:**
- Consumes: the live dmcode DDL, `170_edw_core_executable.sql:728`, and `source_routines_edw_etl.sql:132`.
- Produces: `dmcode.p_drop_temp_tables()`, `edw.p_trs_budget_new()`, and `edw.proc_trs_guoku_cp()`.

- [ ] **Step 1: Confirm the audit currently reports the three routines missing**

Run:

```bash
perl document/psql/tools/verify_event_dependency_closure.pl
```

Expected: nonzero with exactly the three `SOURCE_DEFINED_MISSING` lines listed
in Task 1.

- [ ] **Step 2: Create the focused routine-gap script**

Use this structure and preserve the complete DML bodies from the identified
source blocks:

```sql
-- Missing routine dependencies required by the ten migrated MySQL Events.

SET search_path TO dmcode, public;

DROP PROCEDURE IF EXISTS dmcode.p_drop_temp_tables;
CREATE PROCEDURE dmcode.p_drop_temp_tables()
BEGIN
    DECLARE v_flag INT DEFAULT 0;
    DECLARE v_tname VARCHAR(50);
    DECLARE v_sql TEXT;
    DECLARE c_tname CURSOR FOR
        SELECT t.table_name
        FROM information_schema.TABLES t
        WHERE t.TABLE_SCHEMA = 'dmcode'
          AND t.TABLE_NAME LIKE 'new_tree_temp%'
          AND LENGTH(t.TABLE_NAME) = 29;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_flag = 1;

    OPEN c_tname;
    tn_lp: LOOP
        FETCH c_tname INTO v_tname;
        IF v_flag = 1 THEN
            LEAVE tn_lp;
        END IF;
        SET v_sql = CONCAT('DROP TABLE dmcode.', v_tname);
        EXECUTE IMMEDIATE v_sql;
    END LOOP;
    CLOSE c_tname;
END
;
/

SET search_path TO edw, public;

DROP PROCEDURE IF EXISTS edw.p_trs_budget_new;
CREATE PROCEDURE edw.p_trs_budget_new()
BEGIN
    DELETE FROM edw.cm_guoku_bdgorg;
    INSERT INTO edw.cm_guoku_bdgorg (
        S_BDGORGTRECODE, S_TRECODE, S_BDGORGCODE, S_BDGORGNAME
    )
    SELECT DISTINCT
        CONCAT(S_TRECODE, S_BDGORGCODE),
        S_TRECODE,
        S_BDGORGCODE,
        S_BDGORGNAME
    FROM adm.trs_stat_agentbankpay_detail;
    COMMIT;
END
;
/

DROP PROCEDURE IF EXISTS edw.proc_trs_guoku_cp;
CREATE PROCEDURE edw.proc_trs_guoku_cp()
BEGIN
    DELETE FROM adm.trs_stat_agentbankpay_back_detail
    WHERE STR_TO_DATE(CONCAT_WS('-', LEFT(S_ENTRUSTDATE, 4),
        SUBSTR(S_ENTRUSTDATE, 5, 2), RIGHT(S_ENTRUSTDATE, 2)), '%Y-%m-%d')
        = DATE_SUB(CURDATE(), INTERVAL 1 DAY);
    DELETE FROM adm.trs_stat_agentbankpay_detail
    WHERE STR_TO_DATE(CONCAT_WS('-', LEFT(S_ENTRUSTDATE, 4),
        SUBSTR(S_ENTRUSTDATE, 5, 2), RIGHT(S_ENTRUSTDATE, 2)), '%Y-%m-%d')
        = DATE_SUB(CURDATE(), INTERVAL 1 DAY);
    INSERT INTO adm.trs_stat_agentbankpay_detail
    SELECT * FROM ods.trs_stat_agentbankpay_detail
    WHERE STR_TO_DATE(CONCAT_WS('-', LEFT(S_ENTRUSTDATE, 4),
        SUBSTR(S_ENTRUSTDATE, 5, 2), RIGHT(S_ENTRUSTDATE, 2)), '%Y-%m-%d')
        = DATE_SUB(CURDATE(), INTERVAL 1 DAY);
    INSERT INTO adm.trs_stat_agentbankpay_back_detail
    SELECT * FROM ods.trs_stat_agentbankpay_back_detail
    WHERE STR_TO_DATE(CONCAT_WS('-', LEFT(S_ENTRUSTDATE, 4),
        SUBSTR(S_ENTRUSTDATE, 5, 2), RIGHT(S_ENTRUSTDATE, 2)), '%Y-%m-%d')
        = DATE_SUB(CURDATE(), INTERVAL 1 DAY);
END
;
/
```

- [ ] **Step 3: Run static coverage verification**

Run:

```bash
perl document/psql/tools/verify_event_dependency_closure.pl
```

Expected: exit 0 with `SOURCE_DEFINED_MISSING=0`, `FINAL_PRESENT=57`, and one
documented dangling call.

- [ ] **Step 4: Run SQL text hygiene checks**

```bash
rg -n 'DEFINER|DELIMITER|@[A-Za-z_]' \
  document/psql/vastbase/final/006a_event_dependency_routines_init.sql
```

Expected: no matches. `EXECUTE IMMEDIATE` replaces the MySQL session-variable
`PREPARE` sequence.

- [ ] **Step 5: Commit the routine gap**

```bash
git add document/psql/vastbase/final/006a_event_dependency_routines_init.sql
git commit -m "feat: add event dependency routines"
```

### Task 3: Expand the Vastbase Event bundle from seven to ten enabled Events

**Files:**
- Modify: `document/psql/vastbase/final/007_events_init.sql`
- Modify: `document/psql/vastbase/030_events.sql`
- Test: `document/psql/tools/verify_event_dependency_closure.pl`

**Interfaces:**
- Consumes: the ten live MySQL Event definitions and all 57 delivered routines.
- Produces: ten globally unique Vastbase Event names, each in object state `ENABLE`.

- [ ] **Step 1: Add failing static Event assertions to the audit**

The audit must require exactly ten `CREATE EVENT` statements, ten `ENABLE`
clauses, zero `DISABLE` clauses, and these three new names:

```text
dmcode_evt_dmcode_drop_temp_tables
edw_evt_trs_call_edw_budget_data
edw_evt_trs_call_edw_cp
```

Run the audit and expect failure against the current seven disabled Events.

- [ ] **Step 2: Change the existing seven Event definitions to explicit ENABLE**

Replace each standalone `DISABLE` clause with `ENABLE` in both the working
script and final script. Keep schedules, dates, arguments, and completion
behavior unchanged.

- [ ] **Step 3: Add the three missing Event definitions**

Add these blocks to both Event scripts:

```sql
SET search_path TO dmcode, public;

DROP EVENT IF EXISTS dmcode_evt_dmcode_drop_temp_tables;
CREATE EVENT IF NOT EXISTS dmcode_evt_dmcode_drop_temp_tables
ON SCHEDULE EVERY 1 DAY STARTS '2017-08-31 23:30:00'
ON COMPLETION PRESERVE
ENABLE
COMMENT 'source mysql event dmcode.evt_dmcode_drop_temp_tables'
DO CALL dmcode.p_drop_temp_tables();

SET search_path TO edw, public;

DROP EVENT IF EXISTS edw_evt_trs_call_edw_budget_data;
CREATE EVENT IF NOT EXISTS edw_evt_trs_call_edw_budget_data
ON SCHEDULE EVERY 1 DAY STARTS '2018-05-21 22:30:01'
ON COMPLETION PRESERVE
ENABLE
COMMENT 'source mysql event edw.EVT_TRS_CALL_EDW_BUDGET_DATA'
DO CALL edw.p_trs_budget_new();

DROP EVENT IF EXISTS edw_evt_trs_call_edw_cp;
CREATE EVENT IF NOT EXISTS edw_evt_trs_call_edw_cp
ON SCHEDULE EVERY 1 DAY STARTS '2018-12-06 17:30:00'
ON COMPLETION PRESERVE
ENABLE
COMMENT 'source mysql event edw.EVT_TRS_CALL_EDW_CP'
DO CALL edw.proc_trs_guoku_cp();
```

- [ ] **Step 4: Run Event parity checks**

Run:

```bash
perl document/psql/tools/verify_event_dependency_closure.pl
```

Expected: exit 0 and report `EVENTS=10 ENABLED=10 DISABLED=0`.

- [ ] **Step 5: Commit the Event bundle**

```bash
git add document/psql/vastbase/030_events.sql \
  document/psql/vastbase/final/007_events_init.sql \
  document/psql/tools/verify_event_dependency_closure.pl
git commit -m "feat: migrate all mysql events"
```

### Task 4: Wire the routine gap into the final bundle and update operations documentation

**Files:**
- Modify: `document/psql/vastbase/final/000_run_all.sql`
- Modify: `document/psql/vastbase/final/README.md`
- Modify: `document/psql/README.md`
- Modify: `document/psql/vastbase/event_dependency_migration_inventory.md`

**Interfaces:**
- Consumes: `006a_event_dependency_routines_init.sql` and the ten-Event `007_events_init.sql`.
- Produces: deterministic final execution order and environment-safe runbook.

- [ ] **Step 1: Add `006a` immediately before Event creation**

In `000_run_all.sql`, insert:

```sql
\echo [7/13] loading 006a_event_dependency_routines_init.sql
\i /root/work-project/project-02/gk-data-analysis/document/psql/vastbase/final/006a_event_dependency_routines_init.sql

\echo [8/13] loading 007_events_init.sql
```

Update all earlier counters to `/13` and all later display positions by one;
do not rename the existing `008` through `012` files.

- [ ] **Step 2: Update final README semantics**

Document the order `001` through `006`, `006a`, `007`, then `008` through
`012`. Replace the old “Events default DISABLE” statement with:

```text
All ten Event objects are created ENABLE. Before loading 007_events_init.sql,
test must have enable_prevent_job_task_startup=on. Production uses off and
job_queue_processes > 0 after controlled procedure validation.
```

Do not include literal passwords in example commands; use environment-variable
or prompt-based placeholders.

- [ ] **Step 3: Update migration inventory and root README**

Replace outdated statements that claim seven Events were exported or
converted. Record ten live Events, 58 call targets, 57 real source routines,
the one dangling MySQL call, and three added routines.

- [ ] **Step 4: Run documentation consistency checks**

```bash
rg -n '7 个 Event|seven Events|默认使用 `DISABLE`|007_events_init.sql.*DISABLE' \
  document/psql/README.md \
  document/psql/vastbase/final/README.md \
  document/psql/vastbase/event_dependency_migration_inventory.md
```

Expected: no stale claims.

- [ ] **Step 5: Commit final wiring and documentation**

```bash
git add document/psql/vastbase/final/000_run_all.sql \
  document/psql/vastbase/final/README.md \
  document/psql/README.md \
  document/psql/vastbase/event_dependency_migration_inventory.md
git commit -m "docs: document enabled event deployment"
```

### Task 5: Verify the final bundle against Vastbase after account unlock

**Files:**
- Verify: `document/psql/vastbase/final/000_run_all.sql`
- Verify: `document/psql/vastbase/final/006a_event_dependency_routines_init.sql`
- Verify: `document/psql/vastbase/final/007_events_init.sql`
- Modify only if evidence requires it: the failing SQL file and inventory evidence.

**Interfaces:**
- Consumes: unlocked `vastbase_test` credentials parsed from the matching JDBC data-source block without retaining YAML quote characters.
- Produces: compile evidence, routine catalog evidence, and ten-row `SHOW EVENTS` evidence without running business DML.

- [ ] **Step 1: Verify the account is unlocked with one connection attempt**

Parse username/password from the same `25432/gk_data_analysis` data-source
block. Strip matching outer YAML single or double quotes, pass the value as
`PGPASSWORD="$pw"`, and never echo it. If the server still reports locked,
stop immediately without retrying.

- [ ] **Step 2: Assert the global job thread is blocked**

Run:

```sql
SHOW enable_prevent_job_task_startup;
SHOW job_queue_processes;
```

Expected in test: `enable_prevent_job_task_startup=on`. If it is `off`, do not
load enabled Events; report the operational blocker.

- [ ] **Step 3: Run transactional compile validation**

Run `psql -v ON_ERROR_STOP=1` with:

```sql
BEGIN;
\i /root/work-project/project-02/gk-data-analysis/document/psql/vastbase/final/000_run_all.sql
SHOW EVENTS;
ROLLBACK;
```

Expected: every script loads, ten Events are listed and enabled, and rollback
leaves no migration artifacts from the validation transaction.

- [ ] **Step 4: Verify routine and Event catalogs inside the transaction**

Assert that the three routines exist and that `SHOW EVENTS` contains all ten
target names. Do not issue any `CALL` against the entry routines.

- [ ] **Step 5: Re-run all static verification**

```bash
perl document/psql/tools/verify_event_dependency_closure.pl
git diff --check -- document/psql docs/superpowers
git status --short
```

Expected: audit passes. Any whitespace failures outside the touched paths are
pre-existing and must not be edited as part of this migration.

- [ ] **Step 6: Record evidence and commit any evidence-only update**

Update the inventory with the date, target parameters, routine count, Event
count, and rollback result. Do not record credentials.

```bash
git add document/psql/vastbase/event_dependency_migration_inventory.md
git commit -m "test: verify event migration bundle"
```

## Plan Self-Review Findings

- All ten Events are represented with exact target names and schedules.
- The plan distinguishes 58 call targets from 57 real source-defined routines.
- The nonexistent `edw..._XIN` procedure is documented as a dangling source
  call and is not fabricated in Vastbase.
- The three real routine gaps have exact source locations and complete target
  SQL bodies.
- Enabled Event objects are safe in test only when the global job thread is
  blocked; the verification step fails closed when that condition is absent.
- No step executes a business entry routine automatically.
