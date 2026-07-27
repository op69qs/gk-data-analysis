# agentKeyFileHandler 迁移开发与部署手册

## 1. 交付结论

本次将 `agentKeyFileHandler-2.1.0.jar` 的上报能力并入主系统：

- 后端：`org-tribe-system` 中的 `org.jeecg.modules.reporting` 模块；
- 前端：`org-tribe-view` 中的 `src/views/reporting` 页面；
- 数据库：Vastbase G100 V2.2，Schema 与原 MySQL 一一对应；
- 部署形式：不新建微服务，复用主系统登录、菜单、数据范围和 Vue 2 页面能力。

自动链路严格停在“解析 + STG 入库”。`adm.p_guoku_lib_report_all` 不会被上传、任务扫描或恢复机制自动调用；只能由授权人员在页面按本批次账期手工调用。

## 2. 业务功能与 JAR 对齐结果

### 2.1 KEY 上报

1. 上传 ZIP，原件归档并计算 SHA-256。
2. 从 `k<业务日期>t<国库代码>.zip` 识别日期和国库，页面传值只做交叉校验。
3. 安全解压后按文件名中 `sr` / `zc` / `kc` / `tk` 识别收入、支出、库存、退库文本。
4. 按 JAR 固定字段顺序解析，按 ZIP 名替换对应 `agent_file_*` 明细。
5. 保留并更新原 `agent_keyfile_pending` 的文件名、行数、异常和处理状态。
6. 代理国库、KEY 监控、异常展示、文件下载、逻辑删除和按原批次重试均在主系统展示。

### 2.2 TIMS 上报

ZIP 内 Excel 数量不限于一个，Excel 可位于 ZIP 根目录或任意层级子目录。系统递归扫描小写 `.xls` 和 `.xlsx`，忽略目录和 macOS 附属文件。

| 类型 | Excel 固定列序（首行只跳过，不按表头文字重排） | STG 目标 |
|---|---|---|
| 收入 | 日期、国库代码、国库简称、征收机关、预算级次、科目代码、科目名称、本期执行数、年累计 | `stg.trs_tmis_budget_income` |
| 支出 | 日期、国库代码、国库简称、预算级次、科目代码、科目名称、本期执行数、年累计 | `stg.trs_tmis_budget_payout` |
| 库存 | 日期、所属国库代码、所属国库名称、预算级次、借方发生额、贷方发生额、余额 | `stg.trs_tmis_stock` |

时间和金额语义：

- 页面账期：`yyyy-MM`；
- STG `DATA_DATE`：`yyyyMM`；
- STG `BATCH_DATE`：实际入库执行日 `yyyyMMdd`；
- STG `D_ACCT`：Excel 第 1 列原文，收支可为 `202511`，库存的 `2025-11-01` 等具体日期不转月末；
- 金额：两位小数，`HALF_EVEN`，与 JAR 的两位格式结果对齐；
- TIMS pending 汇总国库固定为 `2200000000`，不用当前登录人国库替换。

迁移后只写真实定时链路的三张 STG，不写 `agent_key_file.tims_file_income` / `tims_file_payout` / `tims_file_stock`。原 JAR 中这三表属于未配置定时注解的另一中间路径，不是生产自动 STG 链路。

### 2.3 TIMS 重复周期和整包原子性

同类型同周期重复上报与 JAR 一致：后一次成功包整周期替换前一次。

- 收入/支出删除条件：`DATA_DATE = yyyyMM`；
- 库存删除条件：`DATA_DATE LIKE 'yyyyMM%'`；
- 收入、支出、库存三类互不影响；
- 不追加、不按国库合并、不按登录人国库裁剪 ZIP 内数据。

整包失败保护实现为：

1. 数据库事务外逐个 Excel 解析，顺序写入批次私有二进制 spool，内存中不保留整包记录。
2. 任意文件、工作表或行校验失败，不开启 STG 写事务，旧周期数据不变。
3. 全包校验成功后，一个业务事务内执行：校验全局租约 → 删除旧周期 → 每批最多 400 行插入 → 核对实际行数 → 更新原 `tims_file_pending` 同一行 → 更新成功跟踪状态。
4. 任一删除、插入、计数或状态更新失败，上述事务全部回滚，旧周期仍然完整。
5. spool 在成功或失败后删除；原 ZIP 和解压文件保留。

一个进程只有一个 TIMS 工作线程；多实例共用 `report_runtime_lock` 的 `TIMS_LOAD` 租约串行。因此多个几万行 Excel 不会同时把整包放入 JVM 或并发冲击 STG。

### 2.4 手工 ADM 加工

STG 成功后，批次整体为 `SUCCEEDED/100%`，下游状态为：

- `WAITING_MANUAL`：依赖已核验，可手工调用；
- `DEPENDENCY_UNVERIFIED`：ETL/ADM 尚未核验，按钮展示但禁用；
- `QUEUED` / `PROCESSING` / `SUCCEEDED` / `FAILED`：手工加工的排队、执行、成功和失败。

页面不提供账期输入；服务端从原批次取月末日，检查 `etl.guoku_lib_report_all_log.state='1'`，写调用记录后调用 `adm.p_guoku_lib_report_all(批次月末日)`。PROCESS 超时只标记失败，系统不自动重放外部过程；运维核对 ETL 实际结果后才可再次调用。

### 2.5 JAR 中存在但本期不启用的能力

JAR 中有收入/支出基线查询和人工调整代码，涉及：

- `edw.income_report_detail_stat`；
- `edw.payout_report_detail_stat`；
- `edw.reprot_update_record`。

原页面相关按钮已注释，且当前没有三个真实对象材料。本期已撤下控制器、前端页面、API 和菜单，不查询、不写入上述对象。只在对象盘点脚本中保留 `INACTIVE_*` 记录，待内网 DDL 和业务确认后单独启用。

## 3. 页面、菜单与权限

### 3.1 生产需配置的菜单

| 菜单 | URL | Vue 组件 | 业务用途 |
|---|---|---|---|
| 数据上报 | `/reporting` | `layouts/RouteView` | 根菜单 |
| 上报批次 | `/reporting/batches` | `reporting/ReportBatchList` | 上传、查询、进度、详情、重试、人工加工 |
| 上报监控 | `/reporting/monitoring` | `reporting/ReportMonitoring` | KEY/TIMS 齐全性和异常监控 |
| 代理国库配置 | `/reporting/agent-treasuries` | `reporting/AgentTreasuryConfig` | 代理国库有效期与启停 |

不配置“报送调整记录”。`09_menu_permission_seed.sql` 会主动删除旧版脚本可能已添加的调整菜单及角色关系。

### 3.2 状态跟踪

列表与详情每 3 秒异步刷新执行中批次。可见信息包括：批次、账期、来源/类型、当前阶段、百分比、成功/异常行、原 ZIP 和解压文件、每次任务、时间线、行级异常和每次 ADM 调用。

TIMS 中间进度依次为：排队 → 解析中 → 已解析待提交 → STG 入库中 → 已提交 → 待人工加工/依赖未核验。只有 STG 事务成功后才显示“已提交”。

### 3.3 预留按钮权限

当前上线只做菜单权限。SQL 预留以下 `menu_type=2` 编码，暂不分配角色、不作为接口拦截依赖：

| 角色方向 | 预留权限 |
|---|---|
| 上报人员 | `reporting:batch:upload`、`reporting:file:download`、`reporting:batch:retry` |
| 审核/运维 | `reporting:batch:process`、`reporting:batch:audit` |
| 管理员 | `reporting:batch:delete`、`reporting:treasury:add`、`reporting:treasury:edit`、`reporting:archive:cleanup` |

后续启用按钮权限时，需同时增加后端权限注解、前端按钮指令和角色授权，不能只隐藏按钮。

## 4. 数据库对象和明确 SQL

### 4.1 本次新建的七张表

全部位于 `agent_key_file`，完整 `CREATE TABLE` 在 `05_report_tracking_tables.sql`，索引与旧版兼容变更在 `06_report_indexes_constraints.sql`。

| 表 | 用途 |
|---|---|
| `report_batch` | 上报批次、账期、阶段、总进度、行数、下游状态 |
| `report_file` | 原 ZIP/解压文件、父子关系、路径、大小、SHA-256、行数 |
| `report_task` | PARSE/LOAD/PROCESS 每次尝试、租约、时间、结果 |
| `report_task_log` | 状态变迁、操作人、行数和时间线 |
| `report_parse_error` | 文件/工作表/行/列/原值/错误原因 |
| `report_process_call` | 每次人工 ADM 调用参数、外部日志 ID、状态和异常 |
| `report_runtime_lock` | 全局 `TIMS_LOAD` 租约，防止多实例同时替换 STG |

### 4.2 新表变更和索引 SQL 汇总

`06_report_indexes_constraints.sql` 明确包含：

- 兼容已执行旧版脚本：给 `report_task` 增加 `lease_owner`、`lease_until`；
- 将 `report_batch.auto_process_required` 默认值改为 `0`；该列仅作旧版兼容，程序不再据此自动加工；
- 兼容创建 `report_runtime_lock` 及 `TIMS_LOAD` 初始行；
- 批次号唯一索引、批次查询、创建人、文件父子/SHA-256、任务领取/尝试、时间线、行错误、过程调用和租约到期索引；
- `report_process_call` 对 `QUEUED/PROCESSING` 过程的部分唯一索引。

本次对原 JAR 业务表、STG、EDW、ETL 和 ADM 对象的 DDL 变更数量是 **0**。程序启动不会建表、改列或建索引。`12_stg_performance_check.sql` 只读查询 STG 列/索引/分区并执行 `EXPLAIN DELETE`，不创建 STG 索引。如需优化，必须由 DBA 根据该结果单独出具 SQL，不得根据推测上线。

### 4.3 会读写的已有对象

| Schema | 对象 | 当前路径 |
|---|---|---|
| `agent_key_file` | `agent_treatury_config` | 代理国库查询/新增/修改 |
| `agent_key_file` | `agent_keyfile_pending` | KEY 上传、完成、异常、监控 |
| `agent_key_file` | `agent_file_income/payout/stock/back` | KEY 明细替换 |
| `agent_key_file` | `tims_file_pending` | TIMS 上传时插入，成功/失败只更新原批次 ID 同一行 |
| `stg` | `trs_tmis_budget_income/payout/stock` | TIMS 同类型整周期原子替换 |
| `edw` | `cm_guoku_dimnsn` | 国库层级范围查询 |
| `etl` | `guoku_lib_report_all_log` | 手工 ADM 前检查运行中状态并写调用日志 |
| `adm` | `p_guoku_lib_report_all` | 仅页面手工调用 |

`tims_file_income/payout/stock` 和三个 EDW 调整对象是已识别但当前不访问的对象，详见 `01_schema_object_inventory.sql` 的 `INACTIVE_*`。

## 5. SQL 文件和执行顺序

| 顺序 | 脚本 | 性质 | 说明 |
|---|---|---|---|
| 1 | `01_schema_object_inventory.sql` | 只读 | JAR 已确定、本期非活动和待内网确认对象盘点 |
| 2 | `02_agent_key_file_structure_check.sql` | 只读 | 原 `agent_key_file` 表的列和约束 |
| 3 | `03_stg_structure_check.sql` | 只读 | 三张 STG 的列和约束 |
| 4 | `04_edw_etl_adm_dependency_check.sql` | 只读 | EDW/ETL/ADM 对象和过程参数 |
| 5 | `05_report_tracking_tables.sql` | DDL + 初始行 | 完整创建七张新表及 `TIMS_LOAD` 租约行 |
| 6 | `06_report_indexes_constraints.sql` | DDL + 初始行 | 兼容列、默认值和全部新表索引 |
| 7 | `07_mysql_vastbase_sql_compatibility_check.sql` | 只读 | Vastbase 函数、窗口和过程元数据 |
| 8 | `12_stg_performance_check.sql` | 只读/EXPLAIN | STG 索引、分区和整周期删除计划 |
| 9 | `09_menu_permission_seed.sql` | 菜单 DML | 创建 3 个子菜单和预留按钮，清理旧调整菜单 |
| 10 | `10_process_dependency_check.sql` | 只读 | 手工 ADM 调用门禁；通过后只开 `REPORTING_PROCESS_DEPENDENCIES_VERIFIED=true` |
| 11 | `08_data_reconciliation_check.sql` | 只读 | 按一个测试批次对账 |
| 回滚 | `11_rollback.sql` | 破坏性 | 停服、备份、审批后删菜单和七张新表 |

生产执行 `05` / `06` / `09` 前必须由 DBA 核对 Schema、表空间、主键/外键支持、部分唯一索引语法以及主系统 `sys_permission` 字段。

## 6. 代码文件与功能对应

### 6.1 后端

| 文件/类 | 职责 |
|---|---|
| `ReportBatchController` / `ReportBatchService` | ZIP 上传、业务参数校验、归档、批次/文件/初始任务、pending |
| `ReportArchiveService` / `SafeZipExtractor` | 固定根目录归档、SHA-256、防 Zip Slip、数量/单文件/解压总量限额 |
| `KeyFileParser` / `KeyReportProcessingService` / `KeyReportMapper.xml` | KEY 四类文本解析和明细替换 |
| `TimsExcelParser` / `TimsReportRecord` | TIMS 固定列序、原 `D_ACCT`、两位金额和行级错误 |
| `TimsReportPreparationService` / `TimsSpool*` | 事务外逐文件解析、私有二进制 spool、数量/SHA-256 校验和清理 |
| `TimsAtomicLoadService` / `TimsReportMapper.xml` | 事务内整周期删除、有界批插入、行数核对、保留原日期 |
| `ReportRuntimeLockService` / `ReportRuntimeLockMapper.xml` | 多实例 `TIMS_LOAD` 租约领取、续租、事务内 `FOR UPDATE` fencing、释放 |
| `LegacyPendingService` / `LegacyPendingMapper.xml` | 继续维护原 KEY/TIMS pending；TIMS 只更新原批次 ID 行 |
| `ReportWorkflowService` / `ReportTaskService` / `ReportTaskJob` | 单线程自动 PARSE/LOAD、租约恢复、中间进度、手工 PROCESS 任务 |
| `ReportProcessCallService` / `ReportWorkflowMapper.xml` | ETL 运行检查、人工调用记录和按批次月末 ADM 调用 |
| `ReportBatchQueryService` / `ReportFileAccessService` | 批次列表/详情、全时间线、文件下载、逻辑删除 |
| `AgentTreasuryService` / `ReportMonitoringService` | 代理国库配置和 KEY/TIMS 监控 |
| `ReportingAccessService` / `ReportingUserScopeService` | 登录人国库数据范围；不用于裁剪 TIMS ZIP |

### 6.2 前端

| 文件 | 职责 |
|---|---|
| `src/api/reporting.js` | 批次、上传、文件、重试、监控和代理国库 API |
| `ReportBatchList.vue` | 批次查询、上传入口、总进度、下游状态、3 秒轮询 |
| `ReportBatchDetail.vue` | 批次摘要、文件、任务、行错误、ADM 调用和按原周期再次调用 |
| `ReportUploadModal.vue` | KEY/TIMS、类型、账期和 ZIP；TIMS 不提交登录人国库裁剪参数 |
| `ReportTaskTimeline.vue` | 任务尝试、已解析待提交、入库、结果和时间线 |
| `ReportFileTable.vue` | 原件/解压文件、大小、摘要、行数和下载 |
| `ReportMonitoring.vue` | KEY/TIMS 齐全性、处理状态和异常 |
| `AgentTreasuryConfig.vue` | 代理国库查询、新增、修改、有效期和启停 |

## 7. 配置和资源边界

| 配置 | 生产默认 | 说明 |
|---|---:|---|
| `REPORTING_ARCHIVE_ROOT` | `/opt/gk-data-analysis/reporting` | 专用归档根目录 |
| `REPORTING_MAX_UPLOAD_BYTES` | 10 MiB | ZIP 上传限额 |
| `REPORTING_MAX_ZIP_ENTRIES` | 2000 | ZIP 条目数限额 |
| `REPORTING_MAX_UNCOMPRESSED_BYTES` | 500 MiB | 解压总量限额 |
| `REPORTING_MAX_ENTRY_BYTES` | 100 MiB | 单解压文件限额 |
| `REPORTING_RETENTION_DAYS` | 0 | 0 表示不物理清理，待保留期确认 |
| `REPORTING_TIMS_BATCH_SIZE` | 400 | STG 批插入大小，程序限制 1～1000 |
| `REPORTING_TIMS_LOCK_LEASE_MINUTES` | 60 | TIMS 全局租约 |
| `REPORTING_TASK_STALE_TIMEOUT_MINUTES` | 30 | PARSE/LOAD 任务租约失效阈值 |
| `REPORTING_PROCESS_STALE_TIMEOUT_MINUTES` | 360 | PROCESS 超时只失败、不自动重放 |
| `REPORTING_PROCESS_DEPENDENCIES_VERIFIED` | false | 只控制人工 ADM 是否可用，不产生自动加工 |

线程池核心和最大线程均为 1，队列 100。Excel 解析不占用数据库业务事务；数据库压力主要来自一次周期删除和每批 400 行插入。上线前必须用 `12_stg_performance_check.sql` 检查执行计划，并用典型最大 ZIP 在非高峰期压测。

## 8. 已确定和待内网确认

已确定：

- JAR 中的 Schema 和对象名称；
- TIMS 固定列序、原日期、固定汇总国库、整周期替换和手工月末调用规则；
- 不写三张 TIMS 中间表；
- 不暴露 EDW 调整功能；
- 七张新技术表全部归属 `agent_key_file`；
- 原业务/STG/EDW/ETL/ADM 对象不做 DDL 变更。

待内网确认：

1. 原 `agent_key_file` 各表真实 DDL、主键、字段类型和授权；
2. 三张 STG 的真实字段类型、字符长度、索引、分区和周期删除执行计划；
3. `etl.guoku_lib_report_all_log` 字段顺序、类型、状态回写和陈旧运行记录处理规则；
4. `adm.p_guoku_lib_report_all` 真实签名、参数类型、内部依赖、授权和幂等性；
5. `edw.cm_guoku_dimnsn` 的真实类型和国库层级口径；
6. 归档目录容量、服务账号权限和业务保留期；
7. KEY 四类和 TIMS 三类的最大规模脱敏样例与业务验收结果。

未确认项不会由程序或 SQL 猜测创建。

## 9. 部署和验收

1. 在内网 Vastbase 依次执行第 5 节脚本，保存每个查询结果。
2. 确认 `05` / `06` 仅新建或修改七张 `report_*` 表，无原业务表变更。
3. 执行 `09`，给生产角色只分配根菜单和三个子菜单，暂不分配按钮行。
4. 设置归档根目录并确认服务账号可写；保留期未确认时保持 `0`。
5. 部署后分别用 KEY 和 TIMS 脱敏包验证归档、解压、多 Excel、固定列、行数、异常和重试。
6. 对 TIMS 做一次成功同周期重报和一次中途故障，确认成功时后包整周期生效，失败时旧周期行数/金额不变。
7. 在 `REPORTING_PROCESS_DEPENDENCIES_VERIFIED=false` 时确认页面显示“依赖未核验”且无法调用。
8. 完成 ETL/ADM 核验后只开启 `REPORTING_PROCESS_DEPENDENCIES_VERIFIED=true`，手工调用一次并对照批次月末参数、外部日志和过程结果。
9. 验收列表/详情的全过程状态和文件下载，并演练受控回滚步骤。

## 10. 回滚边界

`11_rollback.sql` 仅删除本次菜单、角色菜单关系和七张新技术表。它不删除：

- 原 KEY/TIMS pending 和 KEY 明细表；
- 三张 STG；
- EDW、ETL、ADM 对象；
- 归档 ZIP 和解压文件。

回滚脚本具有破坏性，必须先停止服务、备份七张新表、取得审批后手工执行。
