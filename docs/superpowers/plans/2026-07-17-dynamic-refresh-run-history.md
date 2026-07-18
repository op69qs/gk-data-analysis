# Dynamic Refresh Run History Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Fix the dynamic-refresh table layout, remove the unrelated report tab, persist every accepted execution with its final result, and replace the misleading success notice with an accepted-task notice.

**Architecture:** Add an application-owned `adm.exec_shell_task_run_log` table and a focused MyBatis mapper. `ErrorLogServiceImpl` commits the task-state transition and new run record through `TransactionTemplate` before submitting `(taskId, runId)` to the executor. The runner follows the production JAR procedure contract, writes one final task status and run result, and the Vue 2 page exposes paginated history through a read-only modal.

**Tech Stack:** Java 8, Spring Boot 2, Spring `TaskExecutor` and `TransactionTemplate`, MyBatis XML, Vastbase/PostgreSQL SQL, Vue 2.6, Ant Design Vue 1.4.

## Global Constraints

- Production JAR behavior is authoritative for the stored-procedure signature: `call schema.procedure(shell_param, task_id)`.
- Vastbase schema, table, column, and index names remain unquoted lowercase identifiers.
- Mapper and service integration tests use real Spring, MyBatis, and Vastbase; do not introduce Fake Mapper implementations.
- Existing menu path, Vue component path, portal permission attributes, CRUD API paths, and task data remain unchanged.
- `/callProc` success means accepted for asynchronous execution, not execution completed.
- Persist at most 64 KiB of process or exception output per run.
- Do not read or depend on an unknown production ETL log table.

---

## File Structure

**Create**

- `document/psql/vastbase/final/017_dynamic_refresh_run_log_init.sql`: idempotent live-database migration.
- `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/mapper/DynamicRefreshRunLogMapper.java`: run-history persistence interface.
- `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/mapper/xml/DynamicRefreshRunLogMapper.xml`: lowercase Vastbase SQL.
- `org-tribe-system/src/test/java/org/jeecg/modules/enumSetting/service/impl/DynamicRefreshExecutionVastbaseIT.java`: real asynchronous failure-path integration test.
- `org-tribe-view/src/views/manualCallReport/modules/runHistoryModal.vue`: paginated read-only history modal.

**Modify**

- `document/psql/vastbase/final/013_adm_tables_init.sql`: include the run-log table for clean installs.
- `document/psql/vastbase/final/README.md`: document incremental migration order and live verification.
- `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/mapper/ErrorLogMapper.java`
- `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/mapper/xml/ErrorLogMapper.xml`
- `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/service/ErrorLogService.java`
- `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/service/impl/ErrorLogServiceImpl.java`
- `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/service/impl/DynamicRefreshTaskRunner.java`
- `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/service/impl/DynamicRefreshStoredProcedureRunner.java`
- `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/service/impl/DynamicRefreshProcessRunner.java`
- `org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/controller/ErrorLogController.java`
- `org-tribe-system/src/test/java/org/jeecg/modules/enumSetting/mapper/ErrorLogMapperVastbaseIT.java`
- `org-tribe-system/src/test/java/org/jeecg/modules/enumSetting/service/impl/DynamicRefreshStoredProcedureRunnerTest.java`
- `org-tribe-system/src/test/java/org/jeecg/modules/enumSetting/service/impl/DynamicRefreshProcessRunnerTest.java`
- `org-tribe-view/src/views/manualCallReport/manualCallReportList.vue`

**Delete**

- `org-tribe-system/src/test/java/org/jeecg/modules/enumSetting/service/impl/ErrorLogServiceImplTest.java`
- `org-tribe-system/src/test/java/org/jeecg/modules/enumSetting/service/impl/DynamicRefreshTaskRunnerTest.java`

---

### Task 1: Add the Vastbase run-history schema and real mapper

**Interfaces**

- Produces: `DynamicRefreshRunLogMapper.add`, `getData`, `getCount`, `getById`, `complete`, and `deleteByTaskId`.
- Consumes: existing `PageData` and the configured `gk_data_analysis` datasource.

- [ ] **Step 1: Extend the real Mapper integration test first**

Add a transactional test to `ErrorLogMapperVastbaseIT` that inserts a task plus two run records, queries them by `task_id`, verifies newest-first ordering and lowercase keys, completes one record, and relies on transaction rollback.

```java
@Autowired
private DynamicRefreshRunLogMapper runLogMapper;

@Test
@Transactional
public void persistsAndQueriesRunHistoryThroughRealVastbase() {
    String taskId = uuid();
    assertEquals(1, errorLogMapper.add(task(taskId, "mapper-history-" + taskId)));
    assertEquals(1, runLogMapper.add(runLog("run-old-" + taskId, taskId, "2026-07-17 10:00:00")));
    assertEquals(1, runLogMapper.add(runLog("run-new-" + taskId, taskId, "2026-07-17 11:00:00")));

    PageData query = new PageData();
    query.put("task_id", taskId);
    query.put("page", 0);
    query.put("rows", 10);
    assertEquals(Integer.valueOf(2), runLogMapper.getCount(query));
    List<Map<String, Object>> rows = runLogMapper.getData(query);
    assertEquals("run-new-" + taskId, rows.get(0).get("id"));
    assertTrue(rows.get(0).containsKey("result_message"));

    assertEquals(1, runLogMapper.complete(
            "run-new-" + taskId, "500", "mapper integration failure"
    ));
    assertEquals("500", runLogMapper.getById("run-new-" + taskId).get("status"));
}
```

- [ ] **Step 2: Run the test and verify the missing mapper failure**

Run:

```bash
cd org-tribe-system
mvn -Dtest=ErrorLogMapperVastbaseIT test
```

Expected: compilation fails because `DynamicRefreshRunLogMapper` does not exist.

- [ ] **Step 3: Add clean-install and incremental lowercase DDL**

Add this table after `adm.exec_shell_task` in `013_adm_tables_init.sql`, and place the same `CREATE TABLE IF NOT EXISTS` plus indexes in `017_dynamic_refresh_run_log_init.sql`:

```sql
CREATE TABLE IF NOT EXISTS adm.exec_shell_task_run_log (
  id varchar(40) NOT NULL,
  task_id varchar(40) NOT NULL,
  task_name varchar(255),
  task_type varchar(2),
  shell_path varchar(255),
  shell_name varchar(255),
  shell_param varchar(1000),
  status varchar(3) NOT NULL,
  start_time timestamp(0) NOT NULL DEFAULT current_timestamp,
  end_time timestamp(0),
  result_message text,
  create_user varchar(128),
  PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_exec_shell_task_run_log_task_time
  ON adm.exec_shell_task_run_log (task_id, start_time DESC);
CREATE INDEX IF NOT EXISTS idx_exec_shell_task_run_log_status
  ON adm.exec_shell_task_run_log (status);
```

Document that `017` is the only script to run on an existing database and that re-running `013` is destructive.

- [ ] **Step 4: Add the focused mapper**

Create:

```java
@Repository
public interface DynamicRefreshRunLogMapper {
    int add(@Param("params") PageData pd);
    List<Map<String, Object>> getData(@Param("params") PageData pd);
    Integer getCount(@Param("params") PageData pd);
    Map<String, Object> getById(@Param("id") String id);
    int complete(@Param("id") String id,
                 @Param("status") String status,
                 @Param("resultMessage") String resultMessage);
    int deleteByTaskId(@Param("taskId") String taskId);
}
```

Implement explicit lowercase columns in XML. `getData` and `getCount` require `task_id`; `getData` orders by `start_time DESC, id DESC` and uses `LIMIT/OFFSET`. `complete` sets `status`, `end_time = CURRENT_TIMESTAMP`, and `result_message`.

- [ ] **Step 5: Apply the incremental DDL to the real database**

Run the repository’s configured Vastbase connection against:

```bash
psql -d gk_data_analysis -v ON_ERROR_STOP=1 \
  -f document/psql/vastbase/final/017_dynamic_refresh_run_log_init.sql
```

Expected: table and two indexes are created without quoted uppercase identifiers.

- [ ] **Step 6: Run the real mapper test**

Run:

```bash
cd org-tribe-system
mvn -Dtest=ErrorLogMapperVastbaseIT test
```

Expected: all mapper tests pass and no `mapper-history-*` rows remain after rollback.

- [ ] **Step 7: Commit the schema and mapper slice**

```bash
git add document/psql/vastbase/final/013_adm_tables_init.sql \
  document/psql/vastbase/final/017_dynamic_refresh_run_log_init.sql \
  document/psql/vastbase/final/README.md \
  org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/mapper/DynamicRefreshRunLogMapper.java \
  org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/mapper/xml/DynamicRefreshRunLogMapper.xml \
  org-tribe-system/src/test/java/org/jeecg/modules/enumSetting/mapper/ErrorLogMapperVastbaseIT.java
git commit -m "feat: persist dynamic refresh run history"
```

---

### Task 2: Restore the JAR procedure contract and capture execution results

**Interfaces**

- Consumes: `DynamicRefreshRunLogMapper.complete(id, status, resultMessage)`.
- Produces: `DynamicRefreshTaskRunner.run(String taskId, String runId)`.
- Produces: `DynamicRefreshProcessRunner.ProcessResult` with `exitCode` and bounded `output`.

- [ ] **Step 1: Change procedure tests before production code**

Update `DynamicRefreshStoredProcedureRunnerTest` to assert exactly two bound values:

```java
Map<String, Object> task = new HashMap<>();
task.put("id", "task-123");
task.put("shell_name", "adm.p_all_control");
task.put("shell_param", "202510");

ProcedureCall call = runner.buildCall(task);
assertEquals("{call adm.p_all_control(?,?)}", call.getSql());
assertEquals(Arrays.asList("202510", "task-123"), call.getArguments());
```

Update process-runner tests so a temporary executable that writes to stderr and exits `9` returns:

```java
ProcessResult result = runner.run(task);
assertEquals(9, result.getExitCode());
assertTrue(result.getOutput().contains("expected failure"));
```

- [ ] **Step 2: Run focused tests and verify failure**

```bash
cd org-tribe-system
mvn -Dtest=DynamicRefreshStoredProcedureRunnerTest,DynamicRefreshProcessRunnerTest test
```

Expected: procedure argument and process result assertions fail against the current implementation.

- [ ] **Step 3: Implement the exact two-argument procedure call**

Change `buildCall` so it requires `id`, treats `shell_param` as one string, and builds:

```java
String sql = "{call " + routineName + "(?,?)}";
List<String> arguments = Arrays.asList(parameter, taskId);
return new ProcedureCall(sql, arguments);
```

Keep the validated routine-name insertion and bind both values through `CallableStatement`.

- [ ] **Step 4: Capture bounded process output without pipe deadlock**

Change `DynamicRefreshProcessRunner.run` to redirect merged output to a temporary file, wait with the existing timeout, read at most 65,536 bytes, delete the file in `finally`, and return:

```java
static final class ProcessResult {
    private final int exitCode;
    private final String output;
    // constructor and getters
}
```

The command remains a `List<String>` and never uses `/bin/sh -c`.

- [ ] **Step 5: Make the task runner finalize both records**

Inject `DynamicRefreshRunLogMapper`. Change the entry point to:

```java
public void run(String taskId, String runId)
```

For stored procedures, reload the task after the call and preserve `500`; map `200` or unchanged `1` to success. For scripts, map exit code `0` to success and nonzero to failure with output. In every exception path build:

```text
<exception class>: <message>; root cause: <root class>: <root message>
```

Truncate the final message to 65,536 characters. In `finally`, update task status first and then complete the matching run record with `end_time`.

- [ ] **Step 6: Delete Fake Mapper tests**

Delete `ErrorLogServiceImplTest` and `DynamicRefreshTaskRunnerTest`. Retain pure procedure-name and process-command unit tests because they do not fake database behavior.

- [ ] **Step 7: Run focused runner tests**

```bash
cd org-tribe-system
mvn -Dtest=DynamicRefreshStoredProcedureRunnerTest,DynamicRefreshProcessRunnerTest test
```

Expected: all focused unit tests pass.

- [ ] **Step 8: Commit the execution slice**

```bash
git add org-tribe-system/src/main/java/org/jeecg/modules/enumSetting/service/impl \
  org-tribe-system/src/test/java/org/jeecg/modules/enumSetting/service/impl
git commit -m "fix: record dynamic refresh execution results"
```

---

### Task 3: Make task submission transactional and expose real history APIs

**Interfaces**

- Consumes: `DynamicRefreshTaskRunner.run(taskId, runId)`.
- Produces: `String ErrorLogService.callProc(PageData pd)`.
- Produces: `getRunRecords(PageData)` and `getRunRecordCount(PageData)`.
- Produces: `POST /errorLogController/getRunRecords`.

- [ ] **Step 1: Add a real asynchronous integration test**

Create `DynamicRefreshExecutionVastbaseIT` with injected real `ErrorLogService`, `ErrorLogMapper`, and `DynamicRefreshRunLogMapper`. Insert a unique task through the service with `shell_name = "invalid"`, call `callProc`, then poll the real run row for at most ten seconds:

```java
String runId = errorLogService.callProc(startParameters(taskId));
Map<String, Object> run = awaitCompletedRun(runId, 10, TimeUnit.SECONDS);
assertEquals("500", errorLogMapper.getTaskById(taskId).get("status"));
assertEquals("500", run.get("status"));
assertNotNull(run.get("end_time"));
assertTrue(String.valueOf(run.get("result_message"))
        .contains("存储过程名称不合法"));
```

Use `finally` to delete run rows and the task from the real database. Do not annotate this asynchronous test with `@Transactional`.

- [ ] **Step 2: Run the integration test and verify failure**

```bash
cd org-tribe-system
mvn -Dtest=DynamicRefreshExecutionVastbaseIT test
```

Expected: compilation fails because `callProc` does not return `runId` and the run-log lifecycle is not wired.

- [ ] **Step 3: Change the service contract**

Update `ErrorLogService`:

```java
String callProc(PageData pd);
List<Map<String, Object>> getRunRecords(PageData pd);
Integer getRunRecordCount(PageData pd);
```

Inject `DynamicRefreshRunLogMapper` and `PlatformTransactionManager` into `ErrorLogServiceImpl`; create a `TransactionTemplate`.

- [ ] **Step 4: Implement commit-before-submit**

Inside `callProc`, execute mark-running, task reload, and run-log insert inside `transactionTemplate.execute`. Return the generated run ID only after this transaction commits, then call:

```java
taskExecutor.execute(() -> taskRunner.run(taskId, runId));
```

If submission throws, update task and run record to `500` with the bounded exception summary and rethrow so the controller returns failure.

- [ ] **Step 5: Add paginated history controller behavior**

Add `POST /getRunRecords`, requiring `task_id`, parsing `pageNo/pageSize` exactly like `getData`, and returning `rows`, `total`, `result`, and `msg`.

Change `/callProc` to:

```java
String runId = errorLogService.callProc(pd);
result.put("msg", "任务已提交，请在运行记录中查看执行结果");
result.put("runId", runId);
result.put("result", "success");
```

Log caught submission exceptions and return `failed` without claiming execution success.

- [ ] **Step 6: Run the real asynchronous test**

```bash
cd org-tribe-system
mvn -Dtest=DynamicRefreshExecutionVastbaseIT test
```

Expected: the invalid routine creates one real run row, reaches `500`, contains the expected error, and cleanup leaves no test data.

- [ ] **Step 7: Run all dynamic-refresh backend tests**

```bash
cd org-tribe-system
mvn -Dtest='ErrorLogMapperVastbaseIT,DynamicRefreshExecutionVastbaseIT,DynamicRefreshStoredProcedureRunnerTest,DynamicRefreshProcessRunnerTest' test
```

Expected: all selected tests pass with zero failures and zero errors.

- [ ] **Step 8: Commit the service/API slice**

```bash
git add org-tribe-system/src/main/java/org/jeecg/modules/enumSetting \
  org-tribe-system/src/test/java/org/jeecg/modules/enumSetting/service/impl/DynamicRefreshExecutionVastbaseIT.java
git commit -m "feat: expose dynamic refresh run records"
```

---

### Task 4: Fix the Vue page and add the history modal

**Interfaces**

- Consumes: `POST /errorLogController/getRunRecords`.
- Consumes: `/callProc` accepted-task response with `runId`.
- Produces: direct dynamic-refresh content with no nested report tab.

- [ ] **Step 1: Add the history modal component**

Create `runHistoryModal.vue` with `show(record)`, local pagination, and:

```javascript
postAction('/errorLogController/getRunRecords', {
  task_id: this.task.id,
  pageNo: this.pagination.current,
  pageSize: this.pagination.pageSize
})
```

Columns are start time, end time, status, and result. Render status through the same `1/200/500` labels and render result as:

```html
<pre class="result-message">{{ text || '-' }}</pre>
```

The modal has only a close button and clears old rows before every new task query.

- [ ] **Step 2: Remove the unrelated report tab**

In `manualCallReportList.vue`, remove:

- the outer `<a-tabs>` and both `<a-tab-pane>` nodes;
- `activeTab`;
- the `ReportManagement` import and component registration.

Keep the card, query form, table, edit modal, and new run-history modal directly under the page.

- [ ] **Step 3: Fix layout and operation content**

Remove `:scroll="{ x: 1050 }"` and `fixed: 'right'`. Set `:tableLayout="'fixed'"`, add `ellipsis: true` to long business columns, and widen the non-fixed action column. Add:

```html
<a-divider type="vertical" />
<a @click="showRunHistory(record)">运行记录</a>
```

- [ ] **Step 4: Correct the accepted-task feedback**

On `/callProc` success:

```javascript
this.$message.info(res.msg || '任务已提交，请在运行记录中查看执行结果')
this.loadData()
```

Do not use `$message.success` and do not poll for final completion from the main page.

- [ ] **Step 5: Compile and lint the touched Vue files**

Run:

```bash
cd org-tribe-view
npm run lint -- --no-fix
npm run build
```

Expected: Vue templates compile, production assets build, and no new lint errors are introduced by the touched files.

- [ ] **Step 6: Commit the frontend slice**

```bash
git add org-tribe-view/src/views/manualCallReport
git commit -m "feat: show dynamic refresh run history"
```

---

### Task 5: Package, deploy, and verify the real system

**Interfaces**

- Consumes: completed backend, frontend, and live `017` migration.
- Produces: updated deploy-package JAR and verified browser behavior on port 9090.

- [ ] **Step 1: Run backend regression verification**

```bash
cd org-tribe-system
mvn test
```

Expected: all configured tests pass. If the full legacy suite contains unrelated known failures, record them and rerun the four dynamic-refresh classes with zero failures.

- [ ] **Step 2: Build the deployable artifact**

```bash
cd /root/work-project/project-02/gk-data-analysis
mvn -pl org-tribe-system -DskipTests package
```

Expected: `org-tribe-system/target/org-tribe-system-2.1.0.jar` is produced and contains `DynamicRefreshRunLogMapper.xml`, `runHistoryModal` in the frontend assets, and the new controller mapping.

- [ ] **Step 3: Update deploy-package and restart**

Copy the exact built JAR to:

```text
deploy-package/app/org-tribe-system-2.1.0.jar
```

Stop and start `org-tribe-system` through `deploy-package/service.sh`. Wait until logs show `Started JeecgApplication` and confirm port `9090` is listening.

- [ ] **Step 4: Verify API mappings and database objects**

Confirm:

- `adm.exec_shell_task_run_log` exists with lowercase columns.
- both indexes exist.
- `/errorLogController/getRunRecords` is mapped.
- existing `/getData`, `/add`, `/edit`, `/callProc`, and `/del` remain mapped.

- [ ] **Step 5: Run the real browser acceptance flow**

Use Chrome DevTools against the authenticated local application:

1. Open the dynamic-refresh menu.
2. Confirm no “报告管理” inner tab exists.
3. Confirm table headers, values, and operation links align.
4. Start the invalid test task.
5. Confirm the only immediate message says the task was submitted.
6. Open “运行记录”.
7. Confirm one real network request to `getRunRecords`.
8. Confirm the newest row reaches failure and displays “存储过程名称不合法”.
9. Confirm no new browser Console error and no unexpected Java exception escapes the runner.

- [ ] **Step 6: Perform final diff and artifact review**

Run:

```bash
git diff --check HEAD~4..HEAD
git status --short
jar tf deploy-package/app/org-tribe-system-2.1.0.jar | grep DynamicRefreshRunLogMapper
```

Expected: no whitespace errors, only intended workspace changes are tracked, and the deployed JAR contains the new mapper class and XML.

- [ ] **Step 7: Commit deployment artifact if tracked**

If `deploy-package/app/org-tribe-system-2.1.0.jar` is tracked:

```bash
git add deploy-package/app/org-tribe-system-2.1.0.jar
git commit -m "build: package dynamic refresh run history"
```

If it is ignored, leave the verified local deployment in place and report its checksum.
