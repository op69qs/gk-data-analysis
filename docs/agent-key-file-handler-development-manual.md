# Agent Key File Handler 迁移开发手册

## 1. 文档用途

本文记录原 JAR 功能、新代码文件、数据库对象、页面能力和业务规则之间的对应关系。开发过程中持续更新；未实现项不得因为出现在设计中而标记为已完成。

配套文档：

- 总体设计：`docs/superpowers/specs/2026-07-25-agent-key-file-handler-migration-design.md`
- 实施计划：`docs/superpowers/plans/2026-07-25-agent-key-file-handler-migration.md`
- Vastbase 开发环境模拟脚本：`docs/superpowers/specs/2026-07-25-agent-key-file-handler-vastbase-simulation.sql`

## 2. 当前实施状态

| 批次 | 范围 | 状态 | 说明 |
|---|---|---|---|
| 1 | 状态模型、跟踪实体、对象核验和跟踪表脚本 | 已实现，待提交 | 不写 JAR 既有数仓对象 |
| 2 | ZIP 上传、归档、安全解压 | 未开始 | 将核验收入 ZIP 的多层目录 |
| 3 | KEY 收入/支出/库存/退库解析 | 未开始 | 等待四类样例做最终对照 |
| 4 | TIMS 收入/支出/库存解析和 STG 写入 | 未开始 | 当前仅有脱敏收入样例 |
| 5 | 异步任务链、自动加工、监控和重试 | 未开始 | 真实过程和日志表须内网核对 |
| 6 | Vue 2 页面、菜单权限和端到端验收 | 未开始 | 页面显示全过程详细状态 |

## 3. 第 1 批文件与功能对应

| 新文件 | 功能 | 业务逻辑 |
|---|---|---|
| `org-tribe-system/src/main/java/org/jeecg/modules/reporting/enums/ReportStatus.java` | 统一状态机 | 任务完成后不能退回排队；重试必须新建任务，保留原执行记录 |
| `.../entity/ReportBatch.java` | 上报批次总览 | 保存来源域、类型、账期、国库、当前阶段、总进度和总体结果 |
| `.../entity/ReportFile.java` | 原 ZIP 与解压文件跟踪 | 保存父子文件、路径、大小、SHA-256、归档/解压/解析状态、行数和保留状态 |
| `.../entity/ReportTask.java` | 后台执行步骤 | 保存归档、解压、解析、入库、加工各任务及每次重试 |
| `.../entity/ReportTaskLog.java` | 页面时间线 | 每次状态变化记录阶段、文件、进度、成功/异常行数、操作人和详细信息 |
| `.../entity/ReportParseError.java` | 行级异常 | 定位到批次、文件、任务、工作表、行、列、原值和错误原因 |
| `.../entity/ReportProcessCall.java` | 下游加工调用 | 保存批次账期、国库范围、过程参数、等待/运行/完成状态、耗时和异常 |
| `db/reporting/01_schema_object_inventory.sql` | 全对象盘点 | 一次列出 JAR 明确依赖的 19 个对象及是否存在 |
| `db/reporting/02_agent_key_file_structure_check.sql` | JAR 十表核对 | 只读导出字段和约束，不修改表 |
| `db/reporting/03_stg_structure_check.sql` | STG 三表核对 | 只读导出 TIMS 自动写入目标结构 |
| `db/reporting/04_edw_etl_adm_dependency_check.sql` | EDW/ETL/ADM 核对 | 核对 4 个 EDW 对象、1 个 ETL 日志表、1 个 ADM 过程和参数 |
| `db/reporting/05_report_tracking_tables.sql` | 创建新跟踪表 | 仅创建本模块六表，Schema 明确为 `agent_key_file` |
| `db/reporting/06_report_indexes_constraints.sql` | 新表索引 | 支持批次列表、任务调度、时间线、异常和过程互斥范围查询 |

## 4. 状态与页面展示规范

统一执行状态为：`QUEUED`（等待）、`PROCESSING`（执行中）、`SUCCEEDED`（成功）、`PARTIALLY_SUCCEEDED`（部分成功）、`FAILED`（失败）、`CANCELLED`（取消）、`LOGICALLY_DELETED`（逻辑删除）。

页面当前阶段由任务类型单独表达：上传归档、解压、解析、入库、下游加工。每一阶段必须展示等待、开始、进行中、成功或失败，并提供开始时间、结束时间、耗时、当前文件、文件数、处理行数、异常行数和错误摘要。页面关闭不影响后台执行。

完成任务不原地改回等待状态。授权人员执行“重新解析、重新入库、再次加工”时，新建任务并通过 `retry_of_task_id` 关联原任务；调用账期和国库范围从原批次读取，不能由页面改成其他周期。

## 5. 数据库归属与执行顺序

JAR 明确依赖对象仍使用原 Schema：`agent_key_file`、`stg`、`edw`、`etl`、`adm`。本模块新建六张跟踪表也放在 `agent_key_file`，与业务归属一致。当前没有任何对象归属到 `ods`、`dmcode`、`comm_sys` 或 `dps`；若主系统菜单、用户范围接口间接依赖这些 Schema，后续作为“主系统集成依赖”单列，不能说成 JAR 依赖。

建议逐一执行和核对：

1. 执行 `01_schema_object_inventory.sql`，确认 19 个对象的存在状态。
2. 执行 `02`、`03`、`04`，导出真实字段、约束和过程参数。
3. 将结果与模拟 DDL 和本手册对照；不一致项以真实 DDL 为准并修改 Mapper。
4. 仅在确认 `agent_key_file` 可创建新表后执行 `05`、`06`。
5. 未确认 `etl.guoku_lib_report_all_log` 和 `adm.P_GUOKU_LIB_REPORT_ALL` 前，自动加工开关必须关闭。

## 6. 已确定与待内网确认

已确定：JAR 直接引用的 Schema、19 个对象名称、KEY 四类文件、TIMS 三类文件、TIMS 三张 STG 目标表、月末账期调用规则、运行中互斥语义、自动调用和按原批次重试要求。

仍待确认：19 个对象在内网 Vastbase 的真实 DDL/类型/授权，ADM 过程完整定义及依赖，ETL 日志表列定义，EDW 对象表或视图类型，国库树与当前用户范围接口，文件保留期和容量规则，KEY/支出/库存/退库完整脱敏样例。未确认项不会以猜测 SQL 接入生产对象。

## 7. 验证基线与已知问题

2026-07-25 在隔离分支执行基线验证：

- 后端可以编译并启动 Spring 测试上下文。
- 原有 `SampleTest` 存在 3 个与本迁移无关的失败：固定期望 5 行但环境返回 22 行、测试字符串 `hello` 转数字失败、日志测试出现空对象。
- 测试环境尝试连接 `192.168.160.244:8761` 注册中心，当前不可达并导致等待；这不是本次改动引入。
- 前端构建引用的 13 个图片资源在原工作区和隔离工作区均不存在，因此基线构建失败；本功能不会隐式修改这些历史页面。

开发期间每批至少执行本模块定向测试和后端编译。最终交付时将本功能验证结果与上述仓库历史问题分开报告。

本模块测试在当前旧版 Surefire 2.21.0 环境使用 `-DforkCount=0` 执行，避免该运行器的子进程误判 Maven 父进程退出。该参数只影响测试进程，不影响生产代码和打包产物。
