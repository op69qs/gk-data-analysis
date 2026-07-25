# Agent Key File Handler Migration Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 在主系统完整复写 agentKeyFileHandler 的 KEY/TIMS 上报、解析、归档、监控、变更和自动加工能力，并适配 Vastbase。

**Architecture:** 后端在 `org.jeecg.modules.reporting` 建立独立模块，以批次、文件、任务、日志和过程调用统一编排；KEY/TIMS 解析器只处理文件格式，写入器对接 JAR 已明确的表。前端新增数据上报菜单域，展示批次全流程和详细进度。

**Tech Stack:** Java 8、Spring Boot 2、MyBatis-Plus 3.1.2、Quartz、Vastbase、Vue 2、Ant Design Vue 1.4.9。

## Global Constraints

- Vastbase Schema 与原 MySQL Schema 一一对应；所有数仓对象使用 `schema.object`。
- 未取得 DDL 时仅执行核对脚本，不创建、覆盖或猜测既有数仓对象。
- ZIP 原件和解压文件可追溯；普通删除只能逻辑删除。
- 自动加工按批次账期、类型和国库范围执行，且相同范围互斥。
- 当前仅菜单权限上线；上传、下载、重试、加工、删除、审核、配置和清理按钮权限均预留后端校验点。
- Java 使用 Java 8；前端保持 Vue 2；Node 使用 Node 14。

---

## File Structure

| 路径 | 责任 |
|---|---|
| `org-tribe-system/src/main/java/org/jeecg/modules/reporting/**` | 实体、服务、解析器、任务、控制器 |
| `org-tribe-system/src/main/resources/mapper/reporting/**` | Vastbase 查询与对象核对 Mapper |
| `org-tribe-system/src/main/resources/db/reporting/**` | 结构核对、模块 DDL、权限、回滚脚本 |
| `org-tribe-system/src/test/java/org/jeecg/modules/reporting/**` | 文件、状态机、Mapper、流程测试 |
| `org-tribe-view/src/api/reporting.js` | API 封装 |
| `org-tribe-view/src/views/reporting/**` | 批次、详情、监控、配置、变更页面 |

## Task 1: 对象核对与模块跟踪模型

**Files:**
- Create: `org-tribe-system/src/main/resources/db/reporting/01_schema_object_inventory.sql`
- Create: `org-tribe-system/src/main/resources/db/reporting/02_agent_key_file_structure_check.sql`
- Create: `org-tribe-system/src/main/resources/db/reporting/03_stg_structure_check.sql`
- Create: `org-tribe-system/src/main/resources/db/reporting/04_edw_etl_adm_dependency_check.sql`
- Create: `org-tribe-system/src/main/resources/db/reporting/05_report_tracking_tables.sql`
- Create: `org-tribe-system/src/main/resources/db/reporting/06_report_indexes_constraints.sql`
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/entity/{ReportBatch,ReportFile,ReportTask,ReportTaskLog,ReportParseError,ReportProcessCall}.java`
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/enums/ReportStatus.java`
- Test: `org-tribe-system/src/test/java/org/jeecg/modules/reporting/entity/ReportStatusTest.java`

- [ ] 写失败测试：`PROCESSING -> SUCCEEDED` 合法，`SUCCEEDED -> QUEUED` 非法；脚本必须包含 `agent_key_file` 十表、`stg` 三表、`edw` 四对象、`etl.guoku_lib_report_all_log`、`adm.P_GUOKU_LIB_REPORT_ALL`。
- [ ] 执行 `cd org-tribe-system && mvn -Dtest=ReportStatusTest test`，确认失败。
- [ ] 实现批次、文件、任务、日志、行错误、过程调用实体及状态机；DDL 仅创建本模块跟踪表，既有数仓对象只读核对。
- [ ] 再次执行相同测试，确认通过。
- [ ] 提交：`git add org-tribe-system/src/main/resources/db/reporting org-tribe-system/src/main/java/org/jeecg/modules/reporting org-tribe-system/src/test/java/org/jeecg/modules/reporting && git commit -m "feat: add reporting tracking model"`。

## Task 2: 文件归档、安全解压与上传 API

**Files:**
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/config/ReportingProperties.java`
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/util/SafeZipExtractor.java`
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/service/{ReportArchiveService,ReportBatchService}.java`
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/controller/ReportBatchController.java`
- Modify: `org-tribe-system/src/main/resources/application-{dev,test,prod}.yml`
- Test: `org-tribe-system/src/test/java/org/jeecg/modules/reporting/util/SafeZipExtractorTest.java`

- [ ] 写失败测试：从“ZIP -> 收入目录 -> 收入1.xls/收入2.xls”递归找到两份 XLS；忽略 `__MACOSX`、`.DS_Store`、`._*`；路径穿越 ZIP 被拒绝。
- [ ] 执行 `cd org-tribe-system && mvn -Dtest=SafeZipExtractorTest test`，确认失败。
- [ ] 实现 SHA-256、服务端归档目录、容量限制、解压防护、`ReportFile` 登记和 `POST /reporting/batches/upload`；上传仅接受 ZIP，创建批次后提交后台任务。
- [ ] 运行测试并以 MockMvc 验证上传返回批次 ID 和初始状态，确认通过。
- [ ] 提交：`git add org-tribe-system && git commit -m "feat: add reporting upload and archive"`。

## Task 3: KEY 文件处理器

**Files:**
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/parser/{KeyFileParser,KeyFileType}.java`
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/service/KeyReportProcessingService.java`
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/mapper/KeyReportMapper.java`
- Create: `org-tribe-system/src/main/resources/mapper/reporting/KeyReportMapper.xml`
- Test: `org-tribe-system/src/test/java/org/jeecg/modules/reporting/parser/KeyFileParserTest.java`

- [ ] 写失败测试：`sr/zc/kc/tk` 文件名分别映射收入、支出、库存、退库；字段不足时生成含文件和行号的错误。
- [ ] 运行 `cd org-tribe-system && mvn -Dtest=KeyFileParserTest test`，确认失败。
- [ ] 实现 JAR 定义的四类字段映射，分别写入 `agent_key_file.agent_file_income`、`agent_file_payout`、`agent_file_stock`、`agent_file_back`；同 ZIP 重跑先替换旧明细，分别记录 SR/ZC/KC/TK 行数与异常。
- [ ] 实现 `agent_key_file.agent_keyfile_pending` 的待处理、已处理、异常、逻辑删除和文件关联。
- [ ] 运行解析测试及 Mapper 集成测试，确认通过；提交 `feat: add key report parser`。

## Task 4: TIMS Excel 处理器与 STG 写入

**Files:**
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/parser/{TimsExcelParser,TimsBusinessType}.java`
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/service/TimsReportProcessingService.java`
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/mapper/TimsReportMapper.java`
- Create: `org-tribe-system/src/main/resources/mapper/reporting/TimsReportMapper.xml`
- Test: `org-tribe-system/src/test/java/org/jeecg/modules/reporting/parser/TimsExcelParserTest.java`
- Test: `org-tribe-system/src/test/java/org/jeecg/modules/reporting/mapper/TimsReportMapperVastbaseIT.java`

- [ ] 写失败测试：收入多层 ZIP 中的 XLS 解析出收入行；库存解析出账户、借方、贷方、余额；Excel 表头/日期/金额不合格时产生行错误。
- [ ] 执行 `cd org-tribe-system && mvn -Dtest=TimsExcelParserTest test`，确认失败。
- [ ] 实现收入/支出字段 `D_ACCT,TRECODE,TERNAME,LEVEL,SUBJECT_CODE,SUBJECT_NAME,F_AMT,YEAR_AMT` 与库存字段 `D_ACCT,TRECODE,TERNAME,LEVEL,ACCOUNT,DEBIT_AMOUNT,CREDIT_AMOUNT,BALANCE` 解析。
- [ ] 以类型、账期、国库范围替换 `agent_key_file.tims_file_*` 中间明细，并以白名单方式替换 `stg.trs_tmis_budget_income`、`stg.trs_tmis_budget_payout`、`stg.trs_tmis_stock`；禁止动态表名拼接。
- [ ] 运行单元与 Vastbase 集成测试；缺失真实对象时，集成测试必须明确跳过并提示先执行核对脚本。提交 `feat: add tims report processing`。

## Task 5: 异步编排、自动加工、监控与重试

**Files:**
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/service/{ReportTaskService,ReportProcessCallService,ReportMonitoringService}.java`
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/job/ReportTaskJob.java`
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/controller/{ReportFileController,ReportMonitoringController,AgentTreasuryController,ReportChangeRecordController}.java`
- Create: `org-tribe-system/src/main/resources/mapper/reporting/{ReportProcessMapper,ReportingMonitoringMapper}.xml`
- Test: `org-tribe-system/src/test/java/org/jeecg/modules/reporting/service/ReportTaskServiceTest.java`

- [ ] 写失败测试：`2026-07` 转为 `2026-07-31`；同账期范围已运行时再次调用抛出业务繁忙；重试生成新任务但沿用原批次账期和范围。
- [ ] 执行 `cd org-tribe-system && mvn -Dtest=ReportTaskServiceTest test`，确认失败。
- [ ] 实现“归档→解压→解析→中间表/STG→自动加工”的依赖任务链，过程调用为 `adm.P_GUOKU_LIB_REPORT_ALL(月末账期)`；读写 `etl.guoku_lib_report_all_log` 以保持原 JAR 的运行中互斥语义。
- [ ] 实现批次详情、时间线、文件下载、行错误、重试、再次加工、代理国库配置、KEY/TIMS 监控、收入/支出变更记录接口；所有接口使用 JEECG `Result`、当前用户和 `@AutoLog`。
- [ ] 将 MySQL `IFNULL`、`DATE_FORMAT`、`LIMIT offset,size`、`GROUP_CONCAT/FIND_IN_SET` 改为 Vastbase SQL；所有查询参数绑定，禁止 `${}`。
- [ ] 运行服务和 Mapper 测试，确认通过；提交 `feat: add reporting workflow and monitoring`。

## Task 6: Vue 2 页面、菜单权限、保留清理与验收

**Files:**
- Create: `org-tribe-view/src/api/reporting.js`
- Create: `org-tribe-view/src/views/reporting/{ReportBatchList,ReportBatchDetail,ReportMonitoring,AgentTreasuryConfig,ReportChangeRecord}.vue`
- Create: `org-tribe-view/src/views/reporting/components/{ReportUploadModal,ReportTaskTimeline,ReportFileTable}.vue`
- Create: `org-tribe-system/src/main/resources/db/reporting/{08_data_reconciliation_check,09_menu_permission_seed,10_process_dependency_check,11_rollback}.sql`
- Create: `org-tribe-system/src/main/java/org/jeecg/modules/reporting/job/ReportArchiveCleanupJob.java`
- Test: `org-tribe-system/src/test/java/org/jeecg/modules/reporting/e2e/ReportingFlowIT.java`

- [ ] 写失败渲染测试：任务状态 `PARSE/PROCESSING` 显示“解析中”；详情页显示上传、解压、解析、入库、加工的时间线和结果。
- [ ] 实现 Options API 页面：上传、批次列表、每 3 秒轮询未完成批次、详情、文件、异常、下载、重试、再次加工、KEY/TIMS 监控、代理国库配置和变更记录。
- [ ] 菜单脚本创建上报、监控、配置、变更菜单和上传、下载、重试、加工、删除、审核、配置、清理权限编码；当前只授予菜单权限，后端保留按钮校验。
- [ ] 清理任务仅删除超过保留期且已逻辑删除的归档；过程依赖脚本在启用自动加工前核验 ADM、ETL、STG 对象。
- [ ] 使用脱敏 `收入.zip` 进行端到端验证，并在补充 KEY/支出/库存/退库样例后逐类核对文件数、行数、金额、账期、国库和过程结果。
- [ ] 运行 `cd org-tribe-system && mvn test` 及 `cd org-tribe-view && npm run lint && npm run build`，确认通过；提交 `test: verify reporting migration flows`。

## Preconditions and Gates

实施前由业务/数据库侧提供并执行核对：`agent_key_file` 十张表、`stg` 三表、`edw` 四对象、`etl.guoku_lib_report_all_log`、`adm.P_GUOKU_LIB_REPORT_ALL` 的 DDL/对象检查结果。缺少这些材料时，Task 1、2 和归档部分可做；涉及既有数仓表写入、过程调用与最终数据对照的步骤必须停在对象核对门禁。
