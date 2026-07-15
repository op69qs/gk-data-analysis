# Dynamic Data Refresh Recovery Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Restore the dynamic data refresh page, CRUD APIs, Vastbase task table, stored-procedure execution, and server-script execution from the production JAR behavior.

**Architecture:** Keep the existing Vue 2 `ListMixin` POST/`rows` contract and the existing `ErrorLogController -> ErrorLogService -> ErrorLogMapper` backend chain. Add one focused asynchronous task runner and one process runner so execution and path validation can be tested without starting Spring or a real process.

**Tech Stack:** Vue 2.6, Ant Design Vue 1.4, Spring Boot 2.0, Java 8, MyBatis XML, JUnit 4, Mockito, Vastbase/PostgreSQL compatibility mode B.

## Global Constraints

- Production JAR and screenshots define all user-visible behavior unless this plan explicitly fixes a confirmed defect.
- Vastbase schema, table, and column identifiers are unquoted lowercase identifiers.
- The provisional `adm.exec_shell_task` DDL must be marked as inferred and revisited when the production MySQL DDL becomes available.
- Task types are `1` for stored procedures and `2` for server scripts.
- Statuses are `0` not run, `1` running, `200` success, and `500` failure.
- Keep the five POST endpoint paths: `getData`, `add`, `edit`, `callProc`, and `del` under `/errorLogController`.
- Do not invoke shell commands through `/bin/sh -c`; pass the script parameter as one `ProcessBuilder` argument.
- Use Java 8-compatible syntax and the repository's existing Vue 2 style.

---

## File Structure

- `document/psql/vastbase/final/013_adm_dynamic_refresh_task_init.sql`: provisional lowercase Vastbase DDL.
- `document/psql/vastbase/final/000_run_all.sql`: include the new DDL in the final bundle.
- `document/psql/vastbase/final/README.md`: document the provisional table and execution order.
- `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/controller/ErrorLogController.java`: HTTP contract and validation boundary.
- `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/service/ErrorLogService.java`: persistence operations used by the controller and runner.
- `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/service/impl/ErrorLogServiceImpl.java`: mapper delegation.
- `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/service/DynamicRefreshTaskRunner.java`: asynchronous state machine and type dispatch.
- `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/service/DynamicRefreshProcessRunner.java`: safe executable path resolution and process exit handling.
- `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/config/DynamicRefreshTaskConfig.java`: bounded Spring task executor.
- `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/mapper/ErrorLogMapper.java`: mapper signatures.
- `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/mapper/xml/ErrorLogMapper.xml`: parameterized Vastbase SQL and procedure call.
- `org-tribe-system/src/main/resources/application.yml`: allowed shell roots setting.
- `org-tribe-system/src/test/java/org/jeecg/modules/enumSetting/controller/ErrorLogControllerTest.java`: API success/failure behavior.
- `org-tribe-system/src/test/java/org/jeecg/modules/enumSetting/service/DynamicRefreshTaskRunnerTest.java`: status transitions and dispatch.
- `org-tribe-system/src/test/java/org/jeecg/modules/enumSetting/service/DynamicRefreshProcessRunnerTest.java`: path validation and process command construction.
- `org-tribe-view/src/api/dynamicRefresh.js`: focused endpoint wrappers.
- `org-tribe-view/src/views/manualCallReport/manualCallReportList.vue`: production-equivalent list page.
- `org-tribe-view/src/views/manualCallReport/modules/callModal.vue`: add/edit modal.

---

### Task 1: Provisional Vastbase Task Table

**Files:**
- Create: `document/psql/vastbase/final/013_adm_dynamic_refresh_task_init.sql`
- Modify: `document/psql/vastbase/final/000_run_all.sql`
- Modify: `document/psql/vastbase/final/README.md`

**Interfaces:**
- Produces: lowercase table `adm.exec_shell_task` with the 12 JAR-derived columns used by Task 2.

- [ ] **Step 1: Add the provisional DDL**

```sql
-- Provisional definition inferred from org-tribe-system-2.1.0.jar.
-- Reconcile lengths, defaults, indexes, and comments with the production MySQL DDL when supplied.
create schema if not exists adm;

create table if not exists adm.exec_shell_task (
  id varchar(32) primary key,
  task_name varchar(200) not null,
  shell_path varchar(500) not null,
  shell_name varchar(300) not null,
  shell_param varchar(1000) not null,
  cron_id varchar(32),
  status varchar(3) not null default '0',
  create_time timestamp without time zone not null default current_timestamp,
  create_user varchar(32),
  update_time timestamp without time zone,
  update_user varchar(32),
  task_type varchar(1) not null,
  constraint ck_exec_shell_task_status check (status in ('0', '1', '200', '500')),
  constraint ck_exec_shell_task_type check (task_type in ('1', '2'))
);

create index if not exists idx_exec_shell_task_status on adm.exec_shell_task (status);
create index if not exists idx_exec_shell_task_name on adm.exec_shell_task (task_name);
```

- [ ] **Step 2: Add `013` to the final runner and README**

Change the runner counters to `1/13` through `13/13`, append:

```sql
\echo [13/13] loading 013_adm_dynamic_refresh_task_init.sql
\i /root/work-project/project-02/gk-data-analysis/document/psql/vastbase/final/013_adm_dynamic_refresh_task_init.sql
```

Document that `013` is provisional and derived from the JAR.

- [ ] **Step 3: Validate the DDL without leaving objects behind**

Run against the configured Vastbase test connection:

```bash
psql -h 100.71.11.54 -p 25432 -U vastbase_test -d gk_data_analysis -W -v ON_ERROR_STOP=1 -c 'begin' \
  -f document/psql/vastbase/final/013_adm_dynamic_refresh_task_init.sql \
  -c "select column_name from information_schema.columns where table_schema='adm' and table_name='exec_shell_task' order by ordinal_position" \
  -c 'rollback'
```

Expected: 12 lowercase column names and `ROLLBACK` with no persistent table change. If no test connection is available, parse-check the script and record the environmental limitation.

- [ ] **Step 4: Commit**

```bash
git add document/psql/vastbase/final
git commit -m "feat: add provisional dynamic refresh task table"
```

---

### Task 2: Persistence and CRUD API

**Files:**
- Modify: `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/controller/ErrorLogController.java`
- Modify: `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/service/ErrorLogService.java`
- Modify: `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/service/impl/ErrorLogServiceImpl.java`
- Modify: `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/mapper/ErrorLogMapper.java`
- Modify: `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/mapper/xml/ErrorLogMapper.xml`
- Create: `org-tribe-system/src/test/java/org/jeecg/modules/enumSetting/controller/ErrorLogControllerTest.java`

**Interfaces:**
- Produces: `getData(PageData)`, `getCount(PageData)`, `getDataById(String)`, `add(PageData)`, `edit(PageData)`, `deleteById(String)`, `claimForExecution(String)`, `updateStatus(String,String)` and `callProc(PageData)`.
- Consumes later: `DynamicRefreshTaskRunner.start(String id)` from Task 3.

- [ ] **Step 1: Write failing controller tests**

Use JUnit 4, Mockito, and `ReflectionTestUtils` to inject mocks. Cover these exact assertions:

```java
@Test
public void addForcesServerOwnedFields() {
    JSONObject input = new JSONObject();
    input.put("task_name", "refresh");
    input.put("task_type", "1");
    input.put("shell_path", "/home/app/dwbi/");
    input.put("shell_name", "adm.p_refresh");
    input.put("shell_param", "202510");
    input.put("status", "500");

    Map<String, Object> result = controller.add(input);

    assertEquals("success", result.get("result"));
    ArgumentCaptor<PageData> captor = ArgumentCaptor.forClass(PageData.class);
    verify(service).add(captor.capture());
    assertEquals("0", captor.getValue().getString("status"));
    assertEquals(32, captor.getValue().getString("id").length());
}

@Test
public void editFailureIsNotOverwrittenBySuccess() {
    doThrow(new RuntimeException("db failed")).when(service).edit(any(PageData.class));
    Map<String, Object> result = controller.edit(new JSONObject());
    assertEquals("failed", result.get("result"));
}

@Test
public void deleteUsesOnlyId() {
    JSONObject input = new JSONObject();
    input.put("id", "task-1");
    input.put("shell_name", "must-not-be-used");
    Map<String, Object> result = controller.del(input);
    assertEquals("success", result.get("result"));
    verify(service).deleteById("task-1");
}
```

- [ ] **Step 2: Run the tests and verify red**

```bash
mvn -pl org-tribe-system -Dtest=ErrorLogControllerTest test
```

Expected: compilation failures because the restored CRUD signatures do not yet exist.

- [ ] **Step 3: Restore service and mapper contracts**

Use integer row counts for mutations:

```java
Map<String, Object> getDataById(String id);
int add(PageData pd);
int edit(PageData pd);
int deleteById(String id);
int claimForExecution(String id);
int updateStatus(String id, String status);
void callProc(PageData pd);
```

Controller methods must return immediately on exceptions and must never append a success result after a catch block.

- [ ] **Step 4: Replace the mapper XML with lowercase Vastbase SQL**

Use explicit columns, `concat('%', #{params.task_name}, '%')`, `limit #{params.rows} offset #{params.page}`, `delete ... where id = #{id}`, and conditional claims:

```sql
update adm.exec_shell_task
set status = '1', update_time = current_timestamp
where id = #{id} and status <> '1'
```

The procedure statement must bind values and interpolate only the separately validated routine name:

```xml
<select id="callProc" statementType="CALLABLE">
  {call ${params.shell_name}(#{params.shell_param}, #{params.id})}
</select>
```

- [ ] **Step 5: Run controller tests and backend compile**

```bash
mvn -pl org-tribe-system -Dtest=ErrorLogControllerTest test
mvn -pl org-tribe-system -DskipTests compile
```

Expected: tests pass and compile exits `0`.

- [ ] **Step 6: Commit**

```bash
git add org-tribe-system/src/main/java/org/jeecg/modules/enumSetting org-tribe-system/src/test/java/org/jeecg/modules/enumSetting
git commit -m "feat: restore dynamic refresh task CRUD"
```

---

### Task 3: Asynchronous Stored Procedure and Script Execution

**Files:**
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/service/DynamicRefreshTaskRunner.java`
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/service/DynamicRefreshProcessRunner.java`
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/config/DynamicRefreshTaskConfig.java`
- Modify: `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/controller/ErrorLogController.java`
- Modify: `org-tribe-system/src/main/resources/application.yml`
- Create: `org-tribe-system/src/test/java/org/jeecg/modules/enumSetting/service/DynamicRefreshTaskRunnerTest.java`
- Create: `org-tribe-system/src/test/java/org/jeecg/modules/enumSetting/service/DynamicRefreshProcessRunnerTest.java`

**Interfaces:**
- Produces: `boolean DynamicRefreshTaskRunner.start(String id)` and `int DynamicRefreshProcessRunner.run(Map<String,Object> task)`.
- Consumes: persistence methods from Task 2.

- [ ] **Step 1: Write failing task-runner tests**

Use a synchronous `TaskExecutor` (`Runnable::run`) and mocked service/process runner. Cover stored procedure dispatch, script dispatch, duplicate claim rejection, and failure status:

```java
@Test
public void storedProcedureTaskMovesFromRunningToSuccess() {
    Map<String, Object> task = task("1");
    when(service.getDataById("id-1")).thenReturn(task);
    when(service.claimForExecution("id-1")).thenReturn(1);

    assertTrue(runner.start("id-1"));

    verify(service).callProc(any(PageData.class));
    verify(service).updateStatus("id-1", "200");
}

@Test
public void exceptionMarksTaskFailed() {
    Map<String, Object> task = task("1");
    when(service.getDataById("id-1")).thenReturn(task);
    when(service.claimForExecution("id-1")).thenReturn(1);
    doThrow(new RuntimeException("failed")).when(service).callProc(any(PageData.class));

    assertTrue(runner.start("id-1"));
    verify(service).updateStatus("id-1", "500");
}
```

- [ ] **Step 2: Write failing process-runner tests**

Extract command creation into package-visible `List<String> buildCommand(Map<String,Object>)` and assert:

```java
assertEquals(Arrays.asList("/home/app/dwbi/run.sh", "2025-10"), runner.buildCommand(task));
```

Also assert that `../outside.sh`, absolute `shell_name`, and an executable outside `/home/app/dwbi` throw `IllegalArgumentException`.

- [ ] **Step 3: Run both tests and verify red**

```bash
mvn -pl org-tribe-system -Dtest=DynamicRefreshTaskRunnerTest,DynamicRefreshProcessRunnerTest test
```

Expected: compilation failures because both classes are absent.

- [ ] **Step 4: Implement the bounded executor and runners**

Create a `ThreadPoolTaskExecutor` bean named `dynamicRefreshTaskExecutor` with core size `2`, max size `4`, queue capacity `50`, thread prefix `dynamic-refresh-`, and graceful shutdown waiting `30` seconds.

`DynamicRefreshTaskRunner.start` must:

1. Reject blank IDs and missing tasks.
2. Validate type `1` or `2`.
3. Validate type-1 names with `^[A-Za-z_][A-Za-z0-9_]*\.[A-Za-z_][A-Za-z0-9_]*$`.
4. Atomically claim the task.
5. Submit execution.
6. Write `200` after success and `500` after any exception/non-zero script exit.

`DynamicRefreshProcessRunner` must normalize `Paths.get(shellPath).resolve(shellName)`, require it to stay under one configured allowed root, and call:

```java
new ProcessBuilder(executable.toString(), shellParam).redirectErrorStream(true).start();
```

Drain the merged output stream before `waitFor()` to prevent a full process buffer from blocking.

- [ ] **Step 5: Wire the start endpoint**

`POST /callProc` reads only `id`, calls `taskRunner.start(id)`, and returns the JAR message `正在调用中,请稍后查看` only when submission succeeds.

Add this lower-case, comma-separated setting to `application.yml`:

```yaml
dynamic-refresh:
  allowed-shell-roots: /home/app/dwbi
```

- [ ] **Step 6: Run execution tests and all focused backend tests**

```bash
mvn -pl org-tribe-system -Dtest=ErrorLogControllerTest,DynamicRefreshTaskRunnerTest,DynamicRefreshProcessRunnerTest test
```

Expected: all focused tests pass.

- [ ] **Step 7: Commit**

```bash
git add org-tribe-system/src/main org-tribe-system/src/test/java/org/jeecg/modules/enumSetting
git commit -m "feat: execute dynamic refresh tasks asynchronously"
```

---

### Task 4: Vue 2 Dynamic Refresh Page

**Files:**
- Create: `org-tribe-view/src/api/dynamicRefresh.js`
- Create: `org-tribe-view/src/views/manualCallReport/manualCallReportList.vue`
- Create: `org-tribe-view/src/views/manualCallReport/modules/callModal.vue`

**Interfaces:**
- Consumes: five POST endpoints from Tasks 2 and 3.
- Produces: backend-menu-loadable component `system/manualCallReportList`.

- [ ] **Step 1: Add focused API wrappers**

```javascript
import { postAction } from '@/api/manage'

export const addTask = params => postAction('/errorLogController/add', params)
export const editTask = params => postAction('/errorLogController/edit', params)
export const startTask = id => postAction('/errorLogController/callProc', { id })
```

List and delete stay in the page's `url` object so the existing `ListMixin` handles POST pagination and `rows` responses.

- [ ] **Step 2: Restore the modal from the JAR structure**

Use Ant Design Vue form decorators for the five fields. All are required; `shell_param` has `{ max: 1000 }`. `handleOk` chooses `addTask` or `editTask`, emits `ok` only on `result === 'success'`, and leaves the modal open on failure so the user can correct input.

- [ ] **Step 3: Restore the list page from the JAR structure**

Use `ListMixin`, `callModal`, and these URLs:

```javascript
url: {
  list: '/errorLogController/getData',
  delete: '/errorLogController/del'
}
```

Bind `:pagination="ipagination"`, map task types/statuses exactly, pass only `record.id` to `startTask`, and disable edit/start/delete while `record.status === '1'`.

- [ ] **Step 4: Lint the restored source**

```bash
env PATH=/root/.local/share/fnm/node-versions/v22.22.2/installation/bin:/usr/bin:/bin npm run lint -- --no-fix
```

Run from `org-tribe-view`. Expected: no lint errors in the three new files. If the repository-wide legacy lint fails elsewhere, run ESLint directly on these files and record both results.

- [ ] **Step 5: Build the frontend**

```bash
env PATH=/root/.local/share/fnm/node-versions/v22.22.2/installation/bin:/usr/bin:/bin npm run build
```

Expected: Vue build exits `0` and the generated bundle contains `manualCallReportList` and `/errorLogController/getData`.

- [ ] **Step 6: Commit**

```bash
git add org-tribe-view/src/api/dynamicRefresh.js org-tribe-view/src/views/manualCallReport
git commit -m "feat: restore dynamic refresh task page"
```

---

### Task 5: Integrated Verification and Documentation

**Files:**
- Modify only if verification finds a defect in files introduced by Tasks 1-4.

**Interfaces:**
- Consumes: all previous deliverables.
- Produces: evidence that source, database script, backend, and browser behavior match the approved design.

- [ ] **Step 1: Run all focused backend tests and compile**

```bash
mvn -pl org-tribe-system -Dtest=ErrorLogControllerTest,DynamicRefreshTaskRunnerTest,DynamicRefreshProcessRunnerTest test
mvn -pl org-tribe-system -DskipTests compile
```

Expected: both commands exit `0`.

- [ ] **Step 2: Re-run frontend lint and build**

Run the Task 4 commands. Expected: new files lint clean and build exits `0`.

- [ ] **Step 3: Verify SQL identifier casing mechanically**

```bash
rg -n '"[A-Za-z_][A-Za-z0-9_]*"|`' \
  document/psql/vastbase/final/013_adm_dynamic_refresh_task_init.sql \
  org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/mapper/xml/ErrorLogMapper.xml
```

Expected: no quoted or backtick identifiers.

- [ ] **Step 4: Run the application and verify in Chrome DevTools**

Log in with the repository test account, open the dynamic refresh menu, and verify:

1. The page renders rather than showing an empty route.
2. Task-name and status filters send the expected POST payload.
3. Add/edit modal matches the screenshots and validates required fields.
4. Add, edit, start, and delete refresh the table.
5. Starting sets status to running and prevents duplicate actions.
6. Console has no new errors and network responses use `result`, `msg`, `rows`, and `total`.

- [ ] **Step 5: Review the final diff for scope and secrets**

```bash
git diff HEAD~4 --stat
git diff HEAD~4 | rg -n -i 'password|secret|api[_-]?key|token'
git status --short
```

Expected: only dynamic-refresh source, tests, DDL, design, and plan changes; no new secrets; unrelated pre-existing untracked files remain untouched.

- [ ] **Step 6: Commit verification-only fixes if needed**

Stage only the specific files changed while correcting a verification failure, inspect the staged diff, then commit them with `git commit -m "fix: address dynamic refresh verification findings"`. Skip this commit when verification requires no code changes.
