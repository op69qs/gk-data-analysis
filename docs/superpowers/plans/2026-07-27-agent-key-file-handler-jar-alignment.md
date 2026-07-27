# Agent Key File Handler JAR 对齐整改实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 在保留现有上传、归档、KEY、监控和页面骨架的基础上，将 TIMS 业务结果严格对齐 JAR，并补齐低资源流式处理、整周期原子替换、人工 ADM 和完整 Vastbase SQL。

**Architecture:** Excel 按 JAR 固定列序逐文件解析，原始 `D_ACCT` 与规范化字段写入批次私有二进制临时文件；全包解析成功后由一个全局租约保护的短业务事务删除同类型旧周期、分批写 STG、核对行数并更新原 `tims_file_pending` 与成功终态。ADM 保持独立人工任务，EDW 变更功能本期不暴露。

**Tech Stack:** Java 8、Spring Boot 2、MyBatis-Plus 3.1.2、Apache POI、Vastbase G100 V2.2、Vue 2 Options API、Ant Design Vue 1.4.9、JUnit 4、Mockito。

## Global Constraints

- JAR 固定列序、`D_ACCT` 原文本、`DATA_DATE=yyyyMM`、`BATCH_DATE=执行日yyyyMMdd`、固定 `TRE_CODE=2200000000` 和同类型整周期替换不得改变。
- 不写 `agent_key_file.tims_file_income/payout/stock`，不按登录用户国库裁剪 TIMS ZIP 内容，不拆分或重建原 pending。
- 上传后只自动解析和 STG 入库；ADM 只能由授权人员按批次账期人工调用。
- TIMS 一个工作线程；多个实例以 `agent_key_file.report_runtime_lock` 的租约和行锁串行。
- 解析阶段不持有业务数据库事务；删除、分批插入、行数核对、pending 成功状态和跟踪成功终态在同一事务提交。
- 所有生产数据库变更只通过明确 SQL 交付；程序启动不得执行 DDL。
- Java 8；前端保持 Vue 2 Options API 与现有组件模式；Node 14。

---

### Task 1: 固定列序与原始时间语义

**Files:**
- Modify: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/parser/TimsReportRecord.java`
- Modify: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/parser/TimsExcelParser.java`
- Modify: `org-tribe-system/src/test/java/org/jeecg/modules/reporting/parser/TimsExcelParserTest.java`

**Interfaces:**
- Produces: `TimsReportRecord#getDAcctText(): String` 与 `getPeriodKey(): String`；解析器按收入 9、支出 8、库存 7 个固定位置读取。

- [ ] **Step 1: 写失败测试**

```java
assertEquals("202511", income.getDAcctText());
assertEquals("2025-11-01", stock.getDAcctText());
assertEquals(new BigDecimal("10.00"), income.getCurrentAmount());
```

同时用“交换表头文字但数据位置不变”的工作簿证明解析器不按表头重排；用库存 7 列样例证明第 5～7 列固定映射借方、贷方、余额。

- [ ] **Step 2: 运行测试确认因当前月末归一和表头探测而失败**

Run: `mvn -Dtest=TimsExcelParserTest test`

- [ ] **Step 3: 最小实现固定列序和原始日期**

`TimsReportRecord` 保存 `String dAcctText`；解析器跳过首行、按类型定义固定索引，不调用 `ColumnLayout.detect`，金额按 JAR 两位小数语义归一。只校验字段可转换，不新增业务校验。

- [ ] **Step 4: 运行解析测试并提交**

Run: `mvn -Dtest=TimsExcelParserTest test`

Commit: `fix: align TIMS column and date semantics with jar`

### Task 2: 批次私有临时文件与有界内存

**Files:**
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/service/TimsSpool.java`
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/service/TimsSpoolCodec.java`
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/service/TimsReportPreparationService.java`
- Modify: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/parser/TimsExcelParser.java`
- Create: `org-tribe-system/src/test/java/org/jeecg/modules/reporting/service/TimsSpoolCodecTest.java`
- Create: `org-tribe-system/src/test/java/org/jeecg/modules/reporting/service/TimsReportPreparationServiceTest.java`

**Interfaces:**
- `TimsExcelParser#parse(Path, TimsBusinessType, Consumer<TimsReportRecord>)`
- `TimsReportPreparationService#prepare(Path extractRoot, Path workRoot, TimsBusinessType type, YearMonth period): TimsPreparationResult`
- `TimsSpool#readBatches(int batchSize, Consumer<List<TimsReportRecord>> consumer)`

- [ ] **Step 1: 写 spool 往返和多文件顺序失败测试**

测试包含制表符、换行、中文、负金额与空征收机关，要求二进制长度前缀编码往返不丢字段；三个工作簿解析时，回调观察到的最大内存记录列表不超过单批大小。

- [ ] **Step 2: 运行测试确认类不存在**

Run: `mvn -Dtest=TimsSpoolCodecTest,TimsReportPreparationServiceTest test`

- [ ] **Step 3: 实现顺序解析和私有 work 文件**

临时文件路径固定为 `<batch>/work/tims-<uuid>.spool`，创建后校验位于批次目录；写完记录数量和 SHA-256。解析错误时关闭并删除临时文件，成功对象实现 `AutoCloseable` 并在关闭时删除。

- [ ] **Step 4: 运行测试并提交**

Run: `mvn -Dtest=TimsSpoolCodecTest,TimsReportPreparationServiceTest,TimsExcelParserTest test`

Commit: `feat: spool TIMS rows outside database transaction`

### Task 3: STG 分批写入与整周期原子事务

**Files:**
- Modify: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/mapper/TimsReportMapper.java`
- Modify: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/mapper/xml/TimsReportMapper.xml`
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/service/TimsAtomicLoadService.java`
- Modify: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/service/TimsReportProcessingService.java`
- Modify: `org-tribe-system/src/test/java/org/jeecg/modules/reporting/mapper/TimsReportMapperXmlTest.java`
- Modify: `org-tribe-system/src/test/java/org/jeecg/modules/reporting/service/TimsReportProcessingServiceTest.java`
- Create: `org-tribe-system/src/test/java/org/jeecg/modules/reporting/service/TimsAtomicLoadServiceTest.java`

**Interfaces:**
- `TimsReportMapper#deleteStg*(String periodKey)`
- `TimsReportMapper#insertStg*(List<TimsReportRecord>, String periodKey, String batchDate)`
- `TimsReportMapper#countStg*(String periodKey)`
- `TimsAtomicLoadService#load(TimsPreparationResult, TimsLoadContext): long`，标注 `@Transactional(rollbackFor=Exception.class)`。

- [ ] **Step 1: 写失败测试**

断言 Mapper XML 不再引用三张 TIMS 中间表，收入/支出 `d_acct=#{row.dAcctText}`，库存同样保留具体日期；服务每批最多读取配置数量、插入返回数和最终计数必须等于 spool 总数，否则抛异常。

- [ ] **Step 2: 运行测试确认旧中间表 SQL 和整包 List 行为导致失败**

Run: `mvn -Dtest=TimsReportMapperXmlTest,TimsReportProcessingServiceTest,TimsAtomicLoadServiceTest test`

- [ ] **Step 3: 最小实现删除一次、分批插入、计数核对**

收入/支出删除 `data_date = yyyyMM`，库存删除 `data_date like yyyyMM || '%'`；`BATCH_DATE` 使用事务执行日。批量默认 400，可通过 `reporting.tims-batch-size` 配置为 1～1000。

- [ ] **Step 4: 运行测试并提交**

Run: `mvn -Dtest=TimsReportMapperXmlTest,TimsReportProcessingServiceTest,TimsAtomicLoadServiceTest test`

Commit: `feat: atomically replace TIMS STG period in bounded batches`

### Task 4: 全局单执行租约与 pending 同行更新

**Files:**
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/entity/ReportRuntimeLock.java`
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/mapper/ReportRuntimeLockMapper.java`
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/mapper/xml/ReportRuntimeLockMapper.xml`
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/service/ReportRuntimeLockService.java`
- Modify: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/service/LegacyPendingService.java`
- Modify: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/mapper/LegacyPendingMapper.java`
- Modify: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/mapper/xml/LegacyPendingMapper.xml`
- Create: `org-tribe-system/src/test/java/org/jeecg/modules/reporting/service/ReportRuntimeLockServiceTest.java`
- Modify: `org-tribe-system/src/test/java/org/jeecg/modules/reporting/service/LegacyPendingServiceTest.java`

**Interfaces:**
- `ReportRuntimeLockService#acquireTims(String owner): boolean`
- `ReportRuntimeLockService#assertOwnedForUpdate(String owner): void`
- `ReportRuntimeLockService#releaseTims(String owner): void`
- `LegacyPendingService#completeTims(ReportBatch, long committedRows, String userId)` 只更新 `id=batch.id`。

- [ ] **Step 1: 写失败测试**

验证活动租约拒绝第二执行者、过期租约可领取、失权执行者不能进入写事务；验证 TIMS 成功只调用一次 `updateTimsPending`，不调用 delete/insert/rebuild。

- [ ] **Step 2: 运行测试确认失败**

Run: `mvn -Dtest=ReportRuntimeLockServiceTest,LegacyPendingServiceTest test`

- [ ] **Step 3: 实现短租约与事务内 `FOR UPDATE` fencing**

领取使用带失效条件的原子 `UPDATE`；业务事务内执行 `SELECT lease_owner ... FOR UPDATE` 并核对令牌。finally 仅允许当前 owner 释放。

- [ ] **Step 4: 运行测试并提交**

Run: `mvn -Dtest=ReportRuntimeLockServiceTest,LegacyPendingServiceTest test`

Commit: `feat: serialize TIMS loading and preserve original pending row`

### Task 5: 工作流只自动到 STG，ADM 保持人工

**Files:**
- Modify: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/config/ReportingAsyncConfig.java`
- Modify: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/config/ReportingProperties.java`
- Modify: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/service/ReportWorkflowService.java`
- Modify: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/service/ReportTaskService.java`
- Modify: `org-tribe-system/src/test/java/org/jeecg/modules/reporting/service/ReportWorkflowServiceTest.java`
- Modify: `org-tribe-system/src/test/java/org/jeecg/modules/reporting/service/ReportTaskServiceTest.java`
- Modify: `org-tribe-system/src/test/java/org/jeecg/modules/reporting/job/ReportTaskJobTest.java`

**Interfaces:**
- 自动事件只接受 `PARSE`/`LOAD`；人工 API 可创建 `PROCESS` 任务。
- 入库成功：批次 `SUCCEEDED/100`，加工状态 `WAITING_MANUAL` 或 `DEPENDENCY_UNVERIFIED`。

- [ ] **Step 1: 写失败测试**

即使旧的两个 auto 开关为 true，TIMS 成功也不得创建或执行 PROCESS；人工 `queueRetry(batchId,"PROCESS",...)` 仍按批次月末日调用；异步执行器核心/最大线程均为 1。

- [ ] **Step 2: 运行测试确认当前会自动调用而失败**

Run: `mvn -Dtest=ReportWorkflowServiceTest,ReportTaskServiceTest,ReportTaskJobTest test`

- [ ] **Step 3: 实现自动停止、人工门禁和原子成功终态**

删除 `autoProcessEnabled` 分支；解析日志先显示“已解析/待提交”，只有 STG 事务成功后写“已提交”。PROCESS 租约过期继续只失败不自动重放。

- [ ] **Step 4: 运行测试并提交**

Run: `mvn -Dtest=ReportWorkflowServiceTest,ReportTaskServiceTest,ReportTaskJobTest test`

Commit: `fix: keep ADM processing manual by reporting period`

### Task 6: 页面状态、菜单和权限收口

**Files:**
- Modify: `org-tribe-view/src/views/reporting/ReportBatchList.vue`
- Modify: `org-tribe-view/src/views/reporting/ReportBatchDetail.vue`
- Modify: `org-tribe-view/src/views/reporting/components/ReportTaskTimeline.vue`
- Modify: `org-tribe-view/src/views/reporting/components/ReportUploadModal.vue`
- Delete: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/controller/ReportChangeRecordController.java`
- Modify: `org-tribe-system/src/test/java/org/jeecg/modules/reporting/service/ReportingWebContractTest.java`

**Component map:**
- `ReportBatchList.vue`: 查询和列表编排；打开上传/详情。
- `ReportBatchDetail.vue`: 展示批次、文件、任务和人工加工操作；不承载解析逻辑。
- `ReportTaskTimeline.vue`: 只根据 props 渲染状态及进度，向上发出刷新事件。
- `ReportUploadModal.vue`: 只收集来源、类型、账期和 ZIP；TIMS 不提交用户国库裁剪参数。

- [ ] **Step 1: 写后端失败契约测试并列出前端可观察状态**

测试确认 `/reporting/change/**` 不再注册；详情接口仍提供 processCalls，人工按钮仅对 TIMS 且 STG 成功显示。

- [ ] **Step 2: 运行失败测试**

Run: `mvn -Dtest=ReportingWebContractTest test`

- [ ] **Step 3: 调整 Vue 2 Options API 页面**

状态文案包含“排队、解析中、已解析待提交、入库中、已提交、待人工加工、依赖未核验、加工中/成功/失败”；移除变更页面菜单入口，不添加前端按钮级强依赖，保留 `reporting:batch:*` 编码。

- [ ] **Step 4: 验证并提交**

Run: `npm run lint`

Commit: `feat: expose committed TIMS progress and manual processing`

### Task 7: 完整 Vastbase SQL 与交付手册

**Files:**
- Modify: `org-tribe-system/src/main/resources/db/reporting/01_schema_object_inventory.sql`
- Modify: `org-tribe-system/src/main/resources/db/reporting/03_stg_structure_check.sql`
- Modify: `org-tribe-system/src/main/resources/db/reporting/05_report_tracking_tables.sql`
- Modify: `org-tribe-system/src/main/resources/db/reporting/06_report_indexes_constraints.sql`
- Modify: `org-tribe-system/src/main/resources/db/reporting/09_menu_permission_seed.sql`
- Modify: `org-tribe-system/src/main/resources/db/reporting/10_process_dependency_check.sql`
- Modify: `org-tribe-system/src/main/resources/db/reporting/11_rollback.sql`
- Create: `org-tribe-system/src/main/resources/db/reporting/12_stg_performance_check.sql`
- Modify: `org-tribe-system/src/test/java/org/jeecg/modules/reporting/entity/ReportingDatabaseScriptsTest.java`
- Modify: `docs/agent-key-file-handler-development-manual.md`

**SQL deliverables:**
- `05`: 七张新表完整建表 SQL，包含 `report_runtime_lock` 初始行。
- `06`: 所有七张表的新增列兼容、约束和索引 SQL；只改新模块技术表。
- `09`: 数据上报、批次、监控、代理国库菜单及全部预留按钮；明确删除旧“报送调整记录”菜单授权。
- `10`: ETL/ADM 只读核验，文字明确为人工调用前置。
- `11`: 按外键顺序删除七张新表和菜单，标注破坏性与审批要求。
- `12`: 只读输出三张 STG 字段、索引、分区和三条周期删除 `EXPLAIN`；不自动建 STG 索引。

- [ ] **Step 1: 写脚本行为失败测试**

测试脚本编号齐全、`05/06/11` 包含 runtime lock、`09` 不创建 changes 菜单、`10` 不含自动加工开关、`12` 只有只读查询/EXPLAIN。

- [ ] **Step 2: 运行测试确认失败**

Run: `mvn -Dtest=ReportingDatabaseScriptsTest test`

- [ ] **Step 3: 完成 SQL 和手册逐文件映射**

手册列明所有新增/修改文件、七张新表、零个既有业务表 DDL 修改、STG 只读性能建议、菜单路径、权限编码、部署顺序、配置默认值、内网未确认项和回滚步骤。

- [ ] **Step 4: 运行测试并提交**

Run: `mvn -Dtest=ReportingDatabaseScriptsTest test`

Commit: `docs: deliver complete Vastbase reporting migration SQL`

### Task 8: 全量验证与交付核对

**Files:**
- Modify only if verification exposes a defect; every defect first gets a failing regression test.

- [ ] **Step 1: 后端全量测试和编译**

Run: `mvn test`

Run: `mvn -DskipTests compile`

- [ ] **Step 2: 脱敏 ZIP 样例回归**

收入 4 个、支出 2 个、库存 3 个 Excel 全部解析；核对 `D_ACCT` 原文、文件数、行数和类型固定列序。测试不得连接或写生产数据库。

- [ ] **Step 3: 前端检查**

Run: `npm run lint`

Run: `npm run build`

- [ ] **Step 4: 最终差异与 SQL 安全检查**

Run: `git diff --check`

确认 SQL 没有修改三张 STG 的结构或索引、没有创建未确认 EDW/ETL/ADM 对象、没有写三张 TIMS 中间表。

- [ ] **Step 5: 更新开发手册验证结果并提交**

Commit: `chore: verify JAR-aligned reporting migration`
