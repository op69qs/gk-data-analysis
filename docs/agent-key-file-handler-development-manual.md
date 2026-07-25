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
| 1 | 状态模型、跟踪实体、对象核验和跟踪表脚本 | 已提交：`c05b05c` | 不写 JAR 既有数仓对象 |
| 2 | ZIP 上传、归档、安全解压 | 已提交：`5244e25` | 已按脱敏收入 ZIP 核验多层目录和 macOS 附属文件 |
| 3 | KEY 收入/支出/库存/退库解析 | 已实现，待提交 | 已按 JAR 字节码复写；仍等待四类样例做日期格式和数据值最终对照 |
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

## 4. 第 2 批文件与功能对应

| 新文件 | 功能 | 业务逻辑 |
|---|---|---|
| `.../config/ReportingProperties.java` | 上报参数 | 配置归档根目录、上传/解压容量上限、条目上限、保留期和自动加工门禁 |
| `.../util/SafeZipExtractor.java` | 安全解压和递归找文件 | 阻止路径穿越、绝对路径、重复输出、超大条目和解压膨胀；忽略 `__MACOSX`、`.DS_Store`、`._*` |
| `.../service/ReportArchiveService.java` | 原件归档 | 只接受 ZIP，服务端固定命名为 `source.zip`，计算 SHA-256 后解压到本批次独立目录 |
| `.../service/ReportBatchService.java` | 创建上传批次 | TIMS 账期转当月末；记录 ZIP、解压子文件、归档/解压成功任务和等待解析任务；失败仍保留批次状态 |
| `.../controller/ReportBatchController.java` | 上传接口 | `POST /reporting/batches/upload` 返回批次 ID、批次号、当前阶段、状态和进度 |
| `.../mapper/Report*Mapper.java` | 跟踪记录持久化 | 使用 MyBatis-Plus 写入第 1 批六表中的批次、文件、任务和时间线表 |
| `.../vo/ReportUploadCommand.java` | 上传参数 | 来源域、业务类型、账期和国库；TIMS 必须选择收入/支出/库存和 `yyyy-MM` |
| `application-{dev,test,prod}.yml` | 环境配置 | 归档目录由环境配置；保留期未确认时为 0，不执行物理清理 |

### 4.1 脱敏收入 ZIP 核验结论

2026-07-25 对用户提供的脱敏 `收入.zip` 只读检查后确认，其实际结构为：ZIP 根目录下有“收入”文件夹，文件夹中有 `收入1.xls`、`收入2.xls`；包内同时存在 `__MACOSX`、`.DS_Store`、`._收入1.xls`、`._收入2.xls` 等 macOS 附属内容。

新实现不把“收入文件夹”作为硬性要求。合法 Excel 可以直接位于 ZIP 根目录，也可以位于收入文件夹或更深子目录；程序递归识别 `.xls/.xlsx`，因此后续支出、库存样例即使目录层级不同也不需要改上传和解压逻辑。业务类型与 Excel 内容的对应校验在解析批次实现。

### 4.2 归档目录和接口约定

归档路径由服务端组成：`<reporting.archive-root>/<key|tims>/<yyyy-MM|pending>/<batch-id>/archive/source.zip`，解压目录为同批次下的 `extracted/`。用户原文件名只保存为业务元数据，不参与服务器路径拼接。

上传接口当前随“数据上报”菜单授权访问。按钮权限编码已固定为 `reporting:batch:upload`，但未启用按钮拦截，确保现有系统只有菜单权限时仍可使用；后续区分上报人员与审核/运维人员时，只启用该权限点并调整角色授权，不改变接口路径和业务流程。

## 5. 第 3 批文件与功能对应

| 新文件 | 功能 | 业务逻辑 |
|---|---|---|
| `.../parser/KeyFileType.java` | KEY 文件分类 | 严格保留 JAR 的大小写规则：只识别小写 `.txt`，名称包含 `sr`、`zc`、`kc`、`tk` 时依次对应收入、支出、库存、退库 |
| `.../parser/KeyFileParser.java` | KEY 文本解析 | UTF-8 逐行读取，TAB 分隔，不跳过首行；分别按 8、8、6、9 个字段解析，金额使用十进制类型 |
| `.../parser/KeyReportRecord.java` | 四类记录模型 | 保留 JAR 字段顺序及 `ACOUNT_CODE`、`BCKREASON` 等原字段含义，不自行更名数据库列 |
| `.../parser/KeyFileParseResult.java`、`KeyFileParseError.java` | 解析结果和行级异常 | 正确行继续处理；异常精确记录文件名、行号、原文和原因，供页面详细跟踪 |
| `.../service/KeyReportProcessingService.java` | 递归处理和覆盖入库 | 查找任意目录深度的 KEY 文本；每类数据按 `KEY_ZIP_NAME` 先删除旧数据，再批量写入本次正确记录 |
| `.../service/KeyReportProcessingResult.java` | 四类统计 | 分别返回 SR/ZC/KC/TK 文件数、成功行数、异常行数，并给出批次合计 |
| `.../mapper/KeyReportMapper.java`、`.../mapper/xml/KeyReportMapper.xml` | Vastbase 持久化 | 固定写入 `agent_key_file` 下四张 JAR 原表；全程绑定参数，不使用动态 Schema 或 `${}`；日期用 Vastbase 可执行的 `cast(... as date)` |

### 5.1 从 JAR 已核实的 KEY 原始规则

原 JAR 的 `FileUtil.importFileContent` 明确使用 UTF-8，逐行读取所有内容；`parseKeyFileData` 使用 TAB 分隔且没有表头跳过逻辑。`KeyFileParseConfig` 只查找小写 `.txt`，并用文件名是否包含小写 `sr`、`zc`、`kc`、`tk` 判断类型。

四类字段顺序如下，迁移代码逐位保持：

| 类型 | JAR 字段顺序 |
|---|---|
| 收入 SR | `D_ACCT,TRECODE,SUBJECT_CODE,TAXORGCODE,BUDGET_TYPE,LEVEL,F_AMT,YEAR_AMT` |
| 支出 ZC | `D_ACCT,TRECODE,SUBJECT_CODE,TAXORGCODE,CODE_TYPE,LEVEL,F_AMT,YEAR_AMT` |
| 库存 KC | `D_ACCT,TRECODE,LEVEL,ACOUNT_CODE,F_BAL,YEAR_INIT_BAL` |
| 退库 TK | `D_ACCT,TRECODE,SUBJECT_CODE,BUDGET_TYPE,LEVEL,TAXORGCODE,BCKREASON,F_AMT,YEAR_AMT` |

JAR 对每一类执行“按 `KEY_ZIP_NAME` 删除旧明细，再插入新明细”，并将 ZIP 名和添加时间补入每行。新实现保留该覆盖语义；若同一 ZIP 内出现多个同类型文本，新实现先合并这些文本再覆盖一次，避免 JAR 在罕见多文件情况下只保留最后一个文件。此项属于可追踪的数据丢失缺陷修正，不改变单文件正常结果。

### 5.2 KEY 尚待样例确认项

JAR 把第一列原值交给数据库的日期函数，字节码不能证明生产文件实际使用 `yyyy-MM-dd`、`yyyyMMdd` 或其他形式。目前 Vastbase Mapper 按标准日期文本转换；在收到 KEY 脱敏样例后，必须逐类确认 `D_ACCT` 原始格式并补充明确的格式校验。未收到样例前，不宣称这一项已完成生产数据验收。

## 6. 状态与页面展示规范

统一执行状态为：`QUEUED`（等待）、`PROCESSING`（执行中）、`SUCCEEDED`（成功）、`PARTIALLY_SUCCEEDED`（部分成功）、`FAILED`（失败）、`CANCELLED`（取消）、`LOGICALLY_DELETED`（逻辑删除）。

页面当前阶段由任务类型单独表达：上传归档、解压、解析、入库、下游加工。每一阶段必须展示等待、开始、进行中、成功或失败，并提供开始时间、结束时间、耗时、当前文件、文件数、处理行数、异常行数和错误摘要。页面关闭不影响后台执行。

完成任务不原地改回等待状态。授权人员执行“重新解析、重新入库、再次加工”时，新建任务并通过 `retry_of_task_id` 关联原任务；调用账期和国库范围从原批次读取，不能由页面改成其他周期。

## 7. 数据库归属与执行顺序

JAR 明确依赖对象仍使用原 Schema：`agent_key_file`、`stg`、`edw`、`etl`、`adm`。本模块新建六张跟踪表也放在 `agent_key_file`，与业务归属一致。当前没有任何对象归属到 `ods`、`dmcode`、`comm_sys` 或 `dps`；若主系统菜单、用户范围接口间接依赖这些 Schema，后续作为“主系统集成依赖”单列，不能说成 JAR 依赖。

建议逐一执行和核对：

1. 执行 `01_schema_object_inventory.sql`，确认 19 个对象的存在状态。
2. 执行 `02`、`03`、`04`，导出真实字段、约束和过程参数。
3. 将结果与模拟 DDL 和本手册对照；不一致项以真实 DDL 为准并修改 Mapper。
4. 仅在确认 `agent_key_file` 可创建新表后执行 `05`、`06`。
5. 未确认 `etl.guoku_lib_report_all_log` 和 `adm.P_GUOKU_LIB_REPORT_ALL` 前，自动加工开关必须关闭。

## 8. 已确定与待内网确认

已确定：JAR 直接引用的 Schema、19 个对象名称、KEY 四类文件、TIMS 三类文件、TIMS 三张 STG 目标表、月末账期调用规则、运行中互斥语义、自动调用和按原批次重试要求。

仍待确认：19 个对象在内网 Vastbase 的真实 DDL/类型/授权，ADM 过程完整定义及依赖，ETL 日志表列定义，EDW 对象表或视图类型，国库树与当前用户范围接口，文件保留期和容量规则，KEY/支出/库存/退库完整脱敏样例。未确认项不会以猜测 SQL 接入生产对象。

## 9. 验证基线与已知问题

2026-07-25 在隔离分支执行基线验证：

- 后端可以编译并启动 Spring 测试上下文。
- 原有 `SampleTest` 存在 3 个与本迁移无关的失败：固定期望 5 行但环境返回 22 行、测试字符串 `hello` 转数字失败、日志测试出现空对象。
- 测试环境尝试连接 `192.168.160.244:8761` 注册中心，当前不可达并导致等待；这不是本次改动引入。
- 前端构建引用的 13 个图片资源在原工作区和隔离工作区均不存在，因此基线构建失败；本功能不会隐式修改这些历史页面。
- 原有 `application-test.yml` 从第 1 行起存在历史缩进错误，标准 YAML 解析失败；本次新增 `reporting` 配置块缩进独立且正确，但没有扩大范围修复原配置。

开发期间每批至少执行本模块定向测试和后端编译。最终交付时将本功能验证结果与上述仓库历史问题分开报告。

本模块测试在当前旧版 Surefire 2.21.0 环境使用 `-DforkCount=0` 执行，避免该运行器的子进程误判 Maven 父进程退出。该参数只影响测试进程，不影响生产代码和打包产物。

第 2 批共执行 9 个定向用例：根目录 Excel、多层目录 Excel、macOS 附属文件忽略、附属文件仍纳入解压容量限制、ZIP 路径穿越拒绝、解压阶段失败识别、归档与 SHA-256、非 ZIP 拒绝、上传接口和批次/文件/任务持久化；全部通过。

第 3 批当前执行 4 个定向用例：四类文件名识别、四类字段顺序、坏行不丢弃正确行、任意目录深度扫描及按 ZIP 覆盖四表；全部通过。Vastbase 真实表结构与 KEY 样例对照仍属于内网验收项。
