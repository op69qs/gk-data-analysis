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
| 5 | 异步任务链、自动加工、详情、下载和重试 | 已提交：`95d7df0` | 真实过程和日志表须内网核对 |
| 6 | 代理国库、监控、变更、Vastbase 运维脚本 | 已提交：`d629bd5` | 保留原 JAR 待处理表兼容及菜单/按钮权限扩展点 |
| 7 | Vue 2 页面与详细进度展示 | 已提交：`a6e76b0` | 新增 4 个菜单页、1 个详情抽屉和 3 个复用组件 |

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
| `.../config/ReportingAsyncConfig.java`、`.../job/ReportTaskJob.java` | 后台执行 | 上传事务提交后低延迟投递；数据库定时扫描排队任务；每次领取生成唯一执行令牌，业务事务持有任务行锁并在提交终态时再次核验令牌；解析/入库进程中断后重新排队，加工中断则明确失败并等待人工核查 |
| `.../service/ReportWorkflowService.java` | 任务编排 | 串联解析、业务表/暂存表入库和 TIMS 自动加工；错误批次在解析阶段停止，不调用过程 |
| `.../service/ReportTaskService.java` | 分阶段重试 | 仅允许 `PARSE/LOAD/PROCESS`；在批次行锁内新建尝试，同阶段已排队/执行时拒绝重复提交，账期和范围只从原批次读取 |
| `.../service/ReportProcessCallService.java`、`.../mapper/xml/ReportWorkflowMapper.xml` | 原加工调用 | 检查 `etl.guoku_lib_report_all_log.STATE='1'` 和本模块运行记录，写原日志并调用 `adm.p_guoku_lib_report_all(月末)` |
| `.../service/ReportBatchQueryService.java`、`.../controller/ReportBatchQueryController.java` | 列表与完整详情 | 一次返回批次、文件、任务、时间线、行错误和过程调用；执行中禁止删除，删除时移除 JAR 旧监控记录，新批次及原件仍逻辑留存 |
| `.../service/ReportingAccessService.java`、`.../controller/ReportFileController.java` | 数据范围与留存文件下载 | 所有批次、详情、重试、删除、文件下载均按当前登录人国库范围校验；文件只按数据库 ID 下载，并再次校验真实路径位于专用归档根目录 |
| `.../service/LegacyPendingService.java`、`.../mapper/xml/LegacyPendingMapper.xml` | 原十表兼容 | 同步维护 JAR 的 `agent_keyfile_pending` 和 `tims_file_pending`，保留旧监控查询依赖的状态、文件名、数量和异常字段 |
| `.../service/LegacyKeyFileName.java` | KEY 原命名规则 | 始终从 `k<业务日期>t<国库>.zip` 派生关键元数据；页面传值仅作交叉校验，不能绕过命名规则；按 ZIP 基名拒绝重复上传 |
| `.../service/ReportingUserScopeService.java` | 用户国库范围 | 从服务端 `sys_user.guoku_id` 读取当前登录人范围；监控、国库配置和收支调整不信任前端传入的 `guokuId` |
| `.../service/AgentTreasuryService.java`、`AgentTreasuryController.java` | 代理国库 | 保留代码/名称/起止日期/状态查询、当前用户国库范围、新增、修改、启停和有效期校验；不新增 JAR 未提供的删除功能 |
| `.../service/ReportMonitoringService.java`、`ReportMonitoringController.java` | KEY/TIMS 监控 | 以代理国库为基线，展示四类 KEY 和三类 TIMS 齐全性、处理状态、行数及异常 |
| `.../service/ReportChangeService.java`、`ReportChangeRecordController.java` | 收入/支出调整 | 读取 EDW 基线明细，叠加最新调整展示；新增时由服务端重读原金额并重算差额 |
| `.../mapper/xml/ReportingBusinessMapper.xml` | Vastbase 业务 SQL | 固定 `agent_key_file`/`edw` 对象；用 `coalesce`、窗口函数、`offset/limit` 替代 MySQL 专有语法，全部条件绑定参数 |
| `.../service/ReportArchiveCleanupService.java`、`.../job/ReportArchiveCleanupJob.java` | 留存清理 | 仅当保留天数大于 0 时，删除已逻辑删除且超期的本模块归档；再次校验归档根目录 |

### 7.2 原待处理表的严格兼容点

- KEY 待处理记录保留四类文件名、文件名状态、行数、异常、ZIP 路径和解压目录；重复 ZIP 检查沿用 JAR 的“ZIP 基名包含匹配”。
- JAR 上传 TIMS 时先新增一条 `TRE_CODE=2200000000` 的 ZIP 汇总待处理记录；解析成功后，再按 Excel 中的“业务日期 + 真实国库 + 业务类型”删除并重建明细待处理记录，使旧监控页的每个国库都得到正确已报结果。新明细主键使用“批次 ID + 序号”，删除/重试只清理本批次派生记录，不再按可重复文件名跨批次删除。
- JAR 的 TIMS 列名含义与字面相反：`FILE_PATH` 存 ZIP 文件路径，`ZIP_FILE_PATH` 存解压目录；兼容表写入继续按该含义执行。
- 迁移后额外保留六张跟踪表，用于表达归档、解压、解析、入库、加工、行错误和每次重试；不改变原十表字段。

### 7.3 自动执行、状态和同批次重试

代码实现了“入库成功后自动调用原过程”的严格业务链，但生产默认处于安全门禁：必须先执行 `10_process_dependency_check.sql`、确认真实 ETL 日志表列顺序/类型及 ADM 过程契约，再同时设置 `REPORTING_AUTO_PROCESS_ENABLED=true` 和 `REPORTING_PROCESS_DEPENDENCIES_VERIFIED=true`。门禁未通过时，TIMS 批次页面显示“部分完成/等待加工”及 `WAITING_CONFIGURATION`，不会将未调过过程的批次误报为整体成功。

任务事件携带唯一 `taskId`，每次领取生成“实例 + 随机值”的唯一执行令牌并写入租约。解析、业务表/STG、原 pending、文件状态、任务终态和批次状态处于同一事务；事务先凭令牌更新并锁住任务行，提交终态时再次凭令牌更新。租约转移后，旧执行者不能提交业务写入、覆盖状态或清除新执行者租约。应用重启后的排队任务会继续执行；解析/入库进程中断并释放数据库事务后可重新排队，加工任务超时只置为失败，不能自动重复调用外部过程。同阶段重试与删除共用批次行锁，避免并发竞态。同一过程的加工任务由 Vastbase 全局活动状态部分唯一索引互斥，并保留 JAR 的 ETL `STATE='1'` 检查。

原 JAR 对 `etl.guoku_lib_report_all_log` 使用不带列名的插入，现有材料无法证明其字段顺序、运行记录超时字段及安全接管规则。迁移实现因此不猜测外部锁，也不自动清理 `STATE='1'`：内部加工租约超时后页面明确失败，运维须先核查 ADM/ETL 实际执行结果和日志，再按同一上报周期人工重试。取得真实 DDL 后，应将插入改为显式列名，并由数据库负责人确认外部陈旧状态恢复规则。

统一执行状态为：`QUEUED`（等待）、`PROCESSING`（执行中）、`SUCCEEDED`（成功）、`PARTIALLY_SUCCEEDED`（部分成功）、`FAILED`（失败）、`CANCELLED`（取消）、`LOGICALLY_DELETED`（逻辑删除）。

页面当前阶段由任务类型单独表达：上传归档、解压、解析、入库、下游加工。每一阶段展示等待、开始、进行中、成功或失败，并提供开始/结束时间、耗时、文件、处理/成功/异常行数、错误摘要和存储过程调用结果。列表和详情对未完成批次每 3 秒异步轮询；页面关闭不影响后台执行。

完成任务不原地改回等待状态。授权人员执行“重新解析、重新入库、再次加工”时，新建任务并通过 `retry_of_task_id` 关联原任务；调用账期和国库范围从原批次读取，不能由页面改成其他周期。

### 7.4 前端文件与业务对应

| 文件 | 页面/组件 | 对应能力 |
|---|---|---|
| `org-tribe-view/src/api/reporting.js` | API 封装 | 批次、文件、重试、监控、代理国库和调整接口 |
| `.../ReportBatchList.vue` | 上报批次 | 上传入口、筛选、总进度、成功/异常行、3 秒轮询、逻辑删除 |
| `.../ReportBatchDetail.vue` | 执行详情 | 任务阶段、状态事件、文件/行数、行异常、过程调用、下载和按原批次重试 |
| `.../ReportMonitoring.vue` | 上报监控 | KEY 四类和 TIMS 三类齐全性、执行异常及文件状态 |
| `.../AgentTreasuryConfig.vue` | 代理国库 | 查询、新增、编辑、有效期、启用/停用 |
| `.../ReportChangeRecord.vue` | 收入/支出调整 | EDW 基线+最新调整查询、调整历史、新增调整 |
| `.../components/ReportUploadModal.vue` | 上传弹窗 | KEY/TIMS、类型、账期、国库、ZIP；提交后自动执行 |
| `.../components/ReportTaskTimeline.vue` | 详细时间线 | 每次尝试、状态事件、操作人、文件和行数 |
| `.../components/ReportFileTable.vue` | 文件明细 | 原 ZIP/解压文件、SHA-256、解析状态、行数和下载 |

### 7.5 菜单与未来按钮权限

`09_menu_permission_seed.sql` 创建“数据上报”根菜单及上报批次、上报监控、代理国库配置、报送调整记录 4 个页面菜单。当前只需给角色分配 `menu_type=0/1` 的菜单行，不分配 `menu_type=2` 按钮行，接口也暂不启用 Shiro 按钮拦截。

已预留 `reporting:batch:upload`、`reporting:file:download`、`reporting:batch:retry`、`reporting:batch:process`、`reporting:batch:delete`、`reporting:batch:audit`、`reporting:treasury:add`、`reporting:treasury:edit`、`reporting:change:add`、`reporting:archive:cleanup`。`audit` 目前仅为后续审核流程预留权限码，不伪装成已实现的 JAR 功能。后续区分上报人员、审核/运维人员和管理员时，只需启用控制器权限注解、页面按钮指令并分配已预留编码，无需改接口和任务模型。

## 8. 数据库归属与执行顺序

JAR 明确依赖对象仍使用原 Schema：`agent_key_file`、`stg`、`edw`、`etl`、`adm`。本模块新建六张跟踪表也放在 `agent_key_file`，与业务归属一致。当前没有任何对象归属到 `ods`、`dmcode`、`comm_sys` 或 `dps`；若主系统菜单、用户范围接口间接依赖这些 Schema，后续作为“主系统集成依赖”单列，不能说成 JAR 依赖。

| 脚本 | 目标库/Schema | 用途 | 是否写数据 |
|---|---|---|---|
| `01_schema_object_inventory.sql` | 当前数据库 | 19 个 JAR 对象存在性盘点 | 否 |
| `02_agent_key_file_structure_check.sql` | `agent_key_file` | 原十表字段、约束和索引 | 否 |
| `03_stg_structure_check.sql` | `stg` | TIMS 三张 STG 表结构 | 否 |
| `04_edw_etl_adm_dependency_check.sql` | `edw`/`etl`/`adm` | EDW 对象、日志表、过程及参数 | 否 |
| `05_report_tracking_tables.sql` | `agent_key_file` | 创建六张新跟踪表 | DDL |
| `06_report_indexes_constraints.sql` | `agent_key_file` | 新跟踪表索引和唯一约束 | DDL |
| `07_mysql_vastbase_sql_compatibility_check.sql` | 当前数据库/`adm` | Vastbase 函数、窗口和过程元数据检查 | 否 |
| `08_data_reconciliation_check.sql` | `agent_key_file`/`stg`/`edw`/`etl` | 上线前后行数、金额和异常对账 | 否 |
| `09_menu_permission_seed.sql` | `"jeecg-boot-os"` | 菜单和未来按钮权限编码 | 是，可重复 |
| `10_process_dependency_check.sql` | `stg`/`etl`/`adm` | 生产自动加工前置检查 | 否 |
| `11_rollback.sql` | `"jeecg-boot-os"`/`agent_key_file` | 受控回滚菜单与六张新表 | 是，破坏性 |

建议逐一执行和核对：

1. 执行 `01_schema_object_inventory.sql`，确认 19 个对象的存在状态。
2. 执行 `02`、`03`、`04`，导出真实字段、约束和过程参数。
3. 将结果与模拟 DDL 和本手册对照；不一致项以真实 DDL 为准并修改 Mapper。
4. 仅在确认 `agent_key_file` 可创建新表后执行 `05`、`06`。
5. 执行 `07_mysql_vastbase_sql_compatibility_check.sql`，确认函数、窗口函数和过程元数据可读。
6. 执行 `08_data_reconciliation_check.sql`，记录各类待处理数、明细数、金额、STG 数和调整数，作为新旧对照基线。
7. 在主系统 Schema 为 `"jeecg-boot-os"` 且 `sys_permission` 字段与脚本一致时执行 `09_menu_permission_seed.sql`；当前只授权菜单行。
8. 生产启用自动加工前执行 `10_process_dependency_check.sql`；开发/测试环境在未确认 `etl.guoku_lib_report_all_log` 和 `adm.P_GUOKU_LIB_REPORT_ALL` 前保持自动加工关闭。
9. `11_rollback.sql` 只在停服、备份和人工审批后执行；它只回滚本模块菜单和六张跟踪表，不删原 JAR 十表、STG、EDW、ETL、ADM 对象或 ZIP 原件。

## 9. 已确定与待内网确认

已确定：JAR 直接引用的 Schema、19 个对象名称、KEY 四类文件、TIMS 三类文件、TIMS 三张 STG 目标表、月末账期调用规则、运行中互斥语义、自动调用和按原批次重试要求。主系统已有 `Login_Userinfo.guokuId`，页面传入该值，后端按 JAR 原规则从 `edw.cm_guoku_dimnsn` 计算层级前缀并限定代理国库和监控数据范围。

仍待确认：19 个对象在内网 Vastbase 的真实 DDL/类型/授权，ADM 过程完整定义及依赖，ETL 日志表列定义，EDW 对象是表还是视图，文件保留期和容量规则，KEY 收入/支出/库存/退库、TIMS 支出/库存脱敏样例。脱敏 TIMS 收入样例已确认，但未连接内网库，因此只完成解压和真实 XLS 解析对照，未声称完成生产入库/过程验收。未确认项不会以猜测 SQL 接入生产对象。

## 10. 验证基线与已知问题

2026-07-25 在隔离分支执行基线验证：

- 后端可以编译并启动 Spring 测试上下文。
- 原有 `SampleTest` 存在 3 个与本迁移无关的失败：固定期望 5 行但环境返回 22 行、测试字符串 `hello` 转数字失败、日志测试出现空对象。
- 测试环境尝试连接 `192.168.160.244:8761` 注册中心，当前不可达并导致等待；这不是本次改动引入。
- 2026-07-26 最后一次全模块测试还在 Spring `dev` 上下文启动阶段因 `100.71.11.54:25432` 开发库不可达而停止；因此全库门禁未过，与已独立通过的 61 个上报测试分开记录。
- 前端构建引用的 13 个图片资源在原工作区和隔离工作区均不存在，因此基线构建失败；本功能不会隐式修改这些历史页面。
- 原有 `application-test.yml` 从第 1 行起存在历史缩进错误，标准 YAML 解析失败；本次新增 `reporting` 配置块缩进独立且正确，但没有扩大范围修复原配置。

开发期间每批至少执行本模块定向测试和后端编译。最终交付时将本功能验证结果与上述仓库历史问题分开报告。

本模块测试在当前旧版 Surefire 2.21.0 环境使用 `-DforkCount=0` 执行，避免该运行器的子进程误判 Maven 父进程退出。该参数只影响测试进程，不影响生产代码和打包产物。

第 2 批共执行 9 个定向用例：根目录 Excel、多层目录 Excel、macOS 附属文件忽略、附属文件仍纳入解压容量限制、ZIP 路径穿越拒绝、解压阶段失败识别、归档与 SHA-256、非 ZIP 拒绝、上传接口和批次/文件/任务持久化；全部通过。

第 3 批当前执行 4 个定向用例：四类文件名识别、四类字段顺序、坏行不丢弃正确行、任意目录深度扫描及按 ZIP 覆盖四表；全部通过。Vastbase 真实表结构与 KEY 样例对照仍属于内网验收项。

第 4 批当前执行 6 个仓库用例：脱敏收入同结构 9 列解析、两套库存列布局、表头/金额错误定位、多层目录下中间表与 STG 双写、错误批次不删除旧数据、Mapper 固定表与绑定参数检查；全部通过。另对用户提供的两份原始脱敏收入 XLS 执行 1 次只读核验，两份均为 1 行成功、0 行异常。内网 Vastbase 集成检查已验证为“未配置时明确跳过”，待配置连接后执行真实六对象检查。

最终定向回归共执行 61 个上报模块测试，全部通过：覆盖空 KEY/TIMS 包、无 Excel/只有表头阻断、分国库 pending 重建、KEY 命名及行内国库不可绕过、批次派生记录隔离、唯一领取令牌与事务 fencing、旧执行者失权后禁止写入、活动重试/删除互斥、加工全局互斥，以及批次、文件、监控、配置和调整接口的服务端国库范围验证。后端 `compile` 成功。

前端新增 8 个 Vue 文件和 1 个 API 文件已用本项目 `vue-template-compiler` 与 Babel 全部解析通过，并按 Ant Design Vue 1.4.9 修正了月份值与组件兼容。`npm run build` 仍只因仓库历史页面缺失 13 个图片资源失败，错误清单与改造前基线一致，未出现上报页面编译错误。项目未配置 ESLint 文件，因此不声称 `npm run lint` 成功。

真实 ADM 过程结果、ETL 日志状态回写、Vastbase 真实 DDL 适配和缺失样例仍属内网/业务验收门禁。
