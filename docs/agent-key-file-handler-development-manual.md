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
| 3 | KEY 收入/支出/库存/退库解析 | 已提交：`f07011f` | 已按 JAR 字节码复写；仍等待四类样例做日期格式和数据值最终对照 |
| 4 | TIMS 收入/支出/库存解析和 STG 写入 | 已提交：`7d22e10` | 脱敏收入两份 XLS 已通过真实解析验证；支出、库存待补样例 |
| 5 | 异步任务链、自动加工、详情、下载和重试 | 核心链路已实现，待提交 | 代理国库、监控和变更记录接口继续开发；真实过程和日志表须内网核对 |
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

## 6. 第 4 批文件与功能对应

| 新文件 | 功能 | 业务逻辑 |
|---|---|---|
| `.../parser/TimsBusinessType.java` | TIMS 三类业务 | 固定 `1=收入、2=支出、3=库存`，与原页面和待处理表一致 |
| `.../parser/TimsExcelParser.java` | XLS/XLSX 解析 | 读取全部工作表、跳过每张表第一行表头、按表头确认字段；支持 JAR 中间表与自动 STG 任务的两套列布局 |
| `.../parser/TimsReportRecord.java` | TIMS 统一记录 | 收入/支出保存科目与金额，库存保存可选账户及借贷余额；同时保留文件、工作表、行号 |
| `.../parser/TimsExcelParseError.java`、`TimsExcelParseResult.java` | 解析异常 | 表头、日期、必填值、金额和账期不一致均可定位到文件/工作表/行/列/原值 |
| `.../service/TimsReportProcessingService.java` | 双目标处理 | 递归汇总 Excel；先准备并验证全批数据，再按原范围覆盖 TIMS 中间表和 STG 目标表 |
| `.../mapper/TimsReportMapper.java`、`.../mapper/xml/TimsReportMapper.xml` | Vastbase 写入 | 目标表固定白名单，绑定全部值参数；禁止原 JAR 的动态表名和 `${}` SQL |
| `.../mapper/TimsReportMapperVastbaseIT.java` | 内网对象检查 | 默认明确跳过；内网设置开关与连接环境变量后，只读检查六张目标表是否存在 |

### 6.1 JAR 中同时存在的两条 TIMS 路径

JAR 不是只有一套 TIMS 解析：

1. `TimsFileParseConfig` 是未配置定时注解的中间表解析器。收入/支出读取 8 列：`D_ACCT,TRECODE,TERNAME,LEVEL,SUBJECT_CODE,SUBJECT_NAME,F_AMT,YEAR_AMT`；库存读取 8 列：`D_ACCT,TRECODE,TERNAME,LEVEL,ACCOUNT,DEBIT_AMOUNT,CREDIT_AMOUNT,BALANCE`，写入 `agent_key_file.tims_file_*`。
2. `TimsAllFileConfig` 才是按 `SCHEDULED_CRON` 每分钟执行的自动任务。它按页面所选 `yyyy-MM` 账期删除 STG 旧数据，再读取收入 9 列、支出 8 列、库存 7 列，写入 `stg.trs_tmis_*`。收入比中间表多“征收机关”列；库存自动任务没有“账户”列。

新实现保留两套目标结果，但只解析一次：通过表头识别收入是否含“征收机关”、库存是否含“账户”，然后分别映射中间表和 STG 所需列。`.xls` 和 `.xlsx` 均支持，且不再受 JAR 自动任务只能用 `HSSFWorkbook` 读取旧 `.xls` 的限制。

### 6.2 脱敏收入 XLS 实际核验

用户提供的 `收入.zip` 中两份文件均为旧二进制 `.xls`，各有一个名为“收入数据”的工作表；第 1 行是表头，第 2 行是数据。有效表头共 9 列：`日期、国库代码、国库简称、征收机关、预算级次、科目代码、科目名称、本期执行数、年累计`，末尾另有空占位列。日期为 `202511`，两份文件的国库代码分别为 `2200000000`、`2200100000`。

2026-07-26 使用迁移后的解析器直接只读验证两份原始 `.xls`：各解析 1 行、0 异常，账期均规范为 `2025-11-30`，国库代码与文件原值一致。样例仅在本机临时目录解压并验证，没有复制进仓库、没有写入数据库、没有上传到外部服务。

### 6.3 覆盖范围、原值格式与失败保护

中间表继续采用 JAR 的范围语义：按本批次出现的 `D_ACCT` 集合与 `TRECODE` 集合删除旧行，再批量插入。STG 继续采用 JAR 的整账期语义：收入/支出按 `DATA_DATE=yyyyMM` 替换，库存按同账期前缀替换；`BATCH_DATE=yyyyMMdd`、`D_ACCT=yyyyMM`。模拟 DDL 据此将三者定义为 `varchar(8)`，真实类型仍以内网 `03_stg_structure_check.sql` 结果为准。

原 JAR 在读取 Excel 前就删除 STG 旧账期数据，文件损坏时仍可能把待处理状态写成完成。迁移实现要求所有文件表头、日期、金额和所选账期全部通过后，才在一个事务中替换中间表和 STG；有任一错误则旧数据不变，批次进入失败/部分成功状态，页面展示行级原因，也不会自动调用下游加工。

## 7. 状态与页面展示规范

### 7.1 第 5 批当前文件与功能对应

| 新文件 | 功能 | 业务逻辑 |
|---|---|---|
| `.../config/ReportingAsyncConfig.java`、`.../job/ReportTaskJob.java` | 后台执行 | 上传事务提交后才投递；使用独立有界线程池，页面关闭不影响处理 |
| `.../service/ReportWorkflowService.java` | 任务编排 | 串联解析、业务表/暂存表入库和 TIMS 自动加工；错误批次在解析阶段停止，不调用过程 |
| `.../service/ReportTaskService.java` | 分阶段重试 | 仅允许 `PARSE/LOAD/PROCESS`；新建尝试并关联原任务，账期和范围只从原批次读取 |
| `.../service/ReportProcessCallService.java`、`.../mapper/xml/ReportWorkflowMapper.xml` | 原加工调用 | 检查 `etl.guoku_lib_report_all_log.STATE='1'` 和本模块运行记录，写原日志并调用 `adm.p_guoku_lib_report_all(月末)` |
| `.../service/ReportBatchQueryService.java`、`.../controller/ReportBatchQueryController.java` | 列表与完整详情 | 一次返回批次、文件、任务、时间线、行错误和过程调用；普通删除仅逻辑删除 |
| `.../service/ReportFileAccessService.java`、`.../controller/ReportFileController.java` | 留存文件下载 | 只按数据库文件 ID 下载，并再次校验真实路径位于专用归档根目录，阻止越权路径读取 |
| `.../service/LegacyPendingService.java`、`.../mapper/xml/LegacyPendingMapper.xml` | 原十表兼容 | 同步维护 JAR 的 `agent_keyfile_pending` 和 `tims_file_pending`，保留旧监控查询依赖的状态、文件名、数量和异常字段 |

生产配置默认自动调用下游过程，严格符合已确认要求；仅允许运维在故障隔离时通过 `REPORTING_AUTO_PROCESS_ENABLED=false` 临时停用。开发、测试环境仍默认关闭真实过程调用，避免误触内网加工。自动调用是否可用不会被隐瞒：过程失败或依赖缺失时，批次与 `PROCESS` 任务均显示失败，可由授权人员在同批次再次加工。

统一执行状态为：`QUEUED`（等待）、`PROCESSING`（执行中）、`SUCCEEDED`（成功）、`PARTIALLY_SUCCEEDED`（部分成功）、`FAILED`（失败）、`CANCELLED`（取消）、`LOGICALLY_DELETED`（逻辑删除）。

页面当前阶段由任务类型单独表达：上传归档、解压、解析、入库、下游加工。每一阶段必须展示等待、开始、进行中、成功或失败，并提供开始时间、结束时间、耗时、当前文件、文件数、处理行数、异常行数和错误摘要。页面关闭不影响后台执行。

完成任务不原地改回等待状态。授权人员执行“重新解析、重新入库、再次加工”时，新建任务并通过 `retry_of_task_id` 关联原任务；调用账期和国库范围从原批次读取，不能由页面改成其他周期。

## 8. 数据库归属与执行顺序

JAR 明确依赖对象仍使用原 Schema：`agent_key_file`、`stg`、`edw`、`etl`、`adm`。本模块新建六张跟踪表也放在 `agent_key_file`，与业务归属一致。当前没有任何对象归属到 `ods`、`dmcode`、`comm_sys` 或 `dps`；若主系统菜单、用户范围接口间接依赖这些 Schema，后续作为“主系统集成依赖”单列，不能说成 JAR 依赖。

建议逐一执行和核对：

1. 执行 `01_schema_object_inventory.sql`，确认 19 个对象的存在状态。
2. 执行 `02`、`03`、`04`，导出真实字段、约束和过程参数。
3. 将结果与模拟 DDL 和本手册对照；不一致项以真实 DDL 为准并修改 Mapper。
4. 仅在确认 `agent_key_file` 可创建新表后执行 `05`、`06`。
5. 开发/测试环境在未确认 `etl.guoku_lib_report_all_log` 和 `adm.P_GUOKU_LIB_REPORT_ALL` 前保持自动加工关闭；生产配置按业务要求默认自动，部署前必须执行依赖核验，失败会在页面明确显示而不会伪装完成。

## 9. 已确定与待内网确认

已确定：JAR 直接引用的 Schema、19 个对象名称、KEY 四类文件、TIMS 三类文件、TIMS 三张 STG 目标表、月末账期调用规则、运行中互斥语义、自动调用和按原批次重试要求。

仍待确认：19 个对象在内网 Vastbase 的真实 DDL/类型/授权，ADM 过程完整定义及依赖，ETL 日志表列定义，EDW 对象表或视图类型，国库树与当前用户范围接口，文件保留期和容量规则，KEY/支出/库存/退库完整脱敏样例。未确认项不会以猜测 SQL 接入生产对象。

## 10. 验证基线与已知问题

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

第 4 批当前执行 6 个仓库用例：脱敏收入同结构 9 列解析、两套库存列布局、表头/金额错误定位、多层目录下中间表与 STG 双写、错误批次不删除旧数据、Mapper 固定表与绑定参数检查；全部通过。另对用户提供的两份原始脱敏收入 XLS 执行 1 次只读核验，两份均为 1 行成功、0 行异常。内网 Vastbase 集成检查已验证为“未配置时明确跳过”，待配置连接后执行真实六对象检查。

第 5 批核心链路当前执行 10 个新增定向用例，连同前四批共 33 个本模块测试全部通过：月末计算、原批次重试、加工互斥、自动任务链、解析失败阻断过程、完整详情、逻辑删除、安全下载、原待处理表 SQL 白名单、过程 SQL 白名单。真实 ADM 过程结果和 ETL 日志状态回写仍以内网核验为准。
