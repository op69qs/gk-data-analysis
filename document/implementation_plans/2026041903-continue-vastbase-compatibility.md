# 继续推进 Vastbase 兼容收尾

## 本次修改

- 继续清理 `seo` 与 `dwbi-statistical-analysis` 中会直接阻塞 Vastbase 运行的 MySQL 残留。
- `seo` 模块补齐辅助元数据查询对 `Vastbase` / `PostgreSQL` 的支持，不再只支持 `Mysql`、`DB2`、`Clickhouse`。
- 修复 `seo` 中两个依赖 MySQL 宽松 `GROUP BY` 的树查询，避免 PostgreSQL/Vastbase 严格模式报错。
- 继续把统计分析模块中残留的 MySQL schema 反引号、`FORMAT(...)`、`CAST(... AS SIGNED)`、`LIMIT offset,size` 改为 PostgreSQL/Vastbase 可执行写法。

## 具体落点

### seo

- `DataTableMapper.xml`
  - 去掉 `seo` / `edw` 相关反引号引用。
  - `getDataSourceTree`、`getRelationTree` 中仅用于去重的 `GROUP BY` 改为 `SELECT DISTINCT`，避免在 Vastbase 下按主键分组却选择未聚合列。
- `DataAuxiliaryMapper.xml`
  - 去掉 `seo` / `edw` 反引号引用。
  - `jeecg-boot-os` schema 改为双引号引用。
  - 为 `getDataTableSelection`、`getDataTableComments` 新增 `Vastbase` / `PostgreSQL` 分支，使用 `information_schema`、`pg_class`、`pg_attribute`、`obj_description`、`col_description` 获取表与字段元数据。
- `ComprehensiveQueryMapper.xml`
  - 收掉残留的列级反引号 `a.name` 引用。

### dwbi-statistical-analysis

- `ForecastAnalysisMapper.xml`
  - 去掉 `system_docking` schema 的反引号引用。
  - `FORMAT(...)` 改为 `ROUND(CAST(... AS NUMERIC), 2)`。
- `KeyIndicatorsMonitorMapper.xml`
  - 去掉 `system_docking` schema 的反引号引用。
  - `CAST(b.content AS SIGNED)` 改为 `CAST(... AS NUMERIC(18,6))`，并对空字符串做 `NULLIF/COALESCE` 兜底。
- `KeyIndicatorsParameterMapper.xml`
  - 分页由 `LIMIT offset,size` 改为 `LIMIT size OFFSET offset`。
- `CreateAnalysisSQL.java`
  - 第一批动态 SQL 改造完成，优先处理了预测分析、阈值预测、重点指标序时分析中最常走的 MySQL 片段。
  - `GROUP_CONCAT` 改为 `string_agg(... ORDER BY ...)`
  - `IFNULL` 改为 `COALESCE`
  - `FROM DUAL` 改为 PostgreSQL 可直接执行的字面量 `SELECT`

### indicatorsLibv-1.0

- `indicatorsMineMapper.xml`
  - `SUBSTRING_INDEX + mysql.help_topic` 的逗号拆分改为 `regexp_split_to_table(...)`
  - `FIND_IN_SET` 改为 `= ANY(string_to_array(...))`
  - `IFNULL` 改为 `COALESCE`
  - `LIMIT offset,size` 改为 `LIMIT size OFFSET offset`
  - `jeecg-boot-os` schema 改为 PostgreSQL 双引号引用，其余 schema/table 反引号清理为 PostgreSQL/Vastbase 写法
- `IndexSchemeMapper.xml`
  - 清理 `visual_screen`、`indicators_lib` 相关反引号引用
  - 两处分页改为 `LIMIT size OFFSET offset`

### inspect

- `mvcConfigMapper.xml`
  - `jeecg-boot-os` schema 改为 PostgreSQL 双引号引用。
- `inspectionEvidenceRecordMapper.xml`
  - `inspection` schema 反引号引用改为 PostgreSQL/Vastbase 兼容写法。
  - 去掉 `getEvidenceRecordMainInfo` 中对主表 `SELECT *` 的无意义 `GROUP BY TASK_ID, PROVIDER`，避免 PostgreSQL 严格分组报错。
- `inspectionEvidenceNotificationMapper.xml`
  - `inspection` schema 反引号引用改为 PostgreSQL/Vastbase 兼容写法。
  - 修正 `getRecordSub` 中子表别名错误，避免条件仍引用不存在的 `a.RECORD_ID`。
- `InspectionDataCheckMapper.xml`
  - `inspection`、`dmcode` schema 的反引号引用改为 PostgreSQL/Vastbase 兼容写法。
  - 收掉列级反引号，降低在 Vastbase 下因标识符处理差异触发的 SQL 风险。
- `inspectionEnforceLawOpinionMapper.xml`
  - `inspection` schema 反引号引用改为 PostgreSQL/Vastbase 兼容写法。
- `inspectionEnforceLawWorkingPaperMapper.xml`
  - `inspection` schema 反引号引用改为 PostgreSQL/Vastbase 兼容写法。
- `inspectionEnforceLawReportMapper.xml`
  - `inspection` schema 反引号引用改为 PostgreSQL/Vastbase 兼容写法。
- `inspectionFindingsOfFactMapper.xml`
  - `inspection` schema 反引号引用改为 PostgreSQL/Vastbase 兼容写法。
- `inspectionCaseMapper.xml`
  - `enumset` schema 反引号引用改为 PostgreSQL/Vastbase 兼容写法。
  - 分页由 `LIMIT offset,size` 改为 `LIMIT size OFFSET offset`。
  - `QUESTION_ID` 条件补括号，避免 `AND/OR` 优先级在不同数据库下产生歧义。
- `inspectionApprovalMapper.xml`
  - `jeecg-boot-os` schema 改为 PostgreSQL 双引号引用。
  - 分页由 `LIMIT offset,size` 改为 `LIMIT size OFFSET offset`。
  - `CAST(... AS SIGNED)` 改为 `CAST(... AS NUMERIC)`，避免 MySQL 专属类型转换。
- `inspectionGroupMapper.xml`
  - 分页由 `LIMIT offset,size` 改为 `LIMIT size OFFSET offset`。
- `InspectionReportMapper.xml`
  - `inspection`、`enumset` schema 的 MySQL 风格引用改为 PostgreSQL/Vastbase 兼容写法。
  - `CAST(... AS SIGNED)` 改为 `CAST(... AS NUMERIC)`，避免工作组排序依赖 MySQL 专属类型。
- `inspectionRegisterBookMapper.xml`
  - `inspection`、`dmcode` schema 的反引号引用改为 PostgreSQL/Vastbase 兼容写法。
  - `LIMIT 0,1` 改为 `LIMIT 1`。
- `InspectionCashBondMapper.xml`
  - `inspection`、`dmcode` schema 的 MySQL 风格引用改为 PostgreSQL/Vastbase 兼容写法。
- `inspectionPostSVNoticeMapper.xml`
  - `inspection` schema 反引号引用改为 PostgreSQL/Vastbase 兼容写法。
- `inspectionProcessControlMapper.xml`
  - 流程控制相关 `inspection` 表引用统一改为 PostgreSQL/Vastbase 兼容写法，避免大量 MySQL 反引号 schema 写法在 Vastbase 下失效。
- `fixedReport`
  - 继续完成 `KydReportMapper.xml`、`QuarterReportMapper.xml`、`ReportMapper.xml`、`NewsFlashMapper.xml`、`NewsFlashQuarterMapper.xml`、`mvcConfigMapper.xml` 的兼容清理。
  - 已将剩余分页改为 `LIMIT size OFFSET offset`，将 `CAST(... AS SIGNED)` 改为 `CAST(... AS NUMERIC)`，并清理 `report` / `indicators_lib` / `jeecg-boot-os` 的 MySQL 风格引用。
  - `QuarterReportMapper.xml`、`ReportMapper.xml`、`NewsFlashQuarterMapper.xml` 的图形/文本聚合已改为 Vastbase/PostgreSQL 兼容写法。
  - 重新执行 `mvn -pl fixedReport -am -DskipTests compile`，结果持续 `BUILD SUCCESS`。
- `org-tribe-system` 短 XML 收口
  - `enumMapper.xml`：`CAST(ENUM_ID AS SIGNED)` 改为 `CAST(ENUM_ID AS NUMERIC)`，并清理 `enumset` 反引号引用。
  - `DetectionMapper.xml`：清理 `enumset.cm_guoku_detection` 的 MySQL 风格引用。
  - `talentPoolSysUserMapper.xml`：清理 `inspection` 反引号引用，并将 `jeecg-boot-os` 改为 PostgreSQL 双引号 schema 引用。
  - `LevyingBodiesMapper.xml`、`IndustryMapper.xml`：清理 `dmcode` 反引号引用，并补齐 PostgreSQL 严格 `GROUP BY` 所需列。
- `inspect` 短 XML 收口
  - `questionRuleMapper.xml`：分页改为 `LIMIT size OFFSET offset`，并为关键字检索补括号，避免 `AND/OR` 优先级歧义。
  - `inspectionTaskTemplateMapper.xml`：清理 `inspection` 反引号引用，并将 `CAST(... AS SIGNED)` 改为 `CAST(... AS NUMERIC)`。
- `inspect` 长 XML 收口
  - `inspectionUserMapper.xml`：分页改为 `LIMIT size OFFSET offset`，`GROUP_CONCAT` 改为 `string_agg`，并清理 `dmcode` / `jeecg-boot-os` 的 MySQL 风格引用。
  - `inspectionTaskMapper.xml`：清理 `enumset` / `dmcode` / `inspection` 反引号引用，`DATE_FORMAT` 改为 `to_char`，并将 `CAST(... AS SIGNED)` 改为 `CAST(... AS NUMERIC)`。
  - `InspectionPlanMapper.xml`：清理 `inspection` / `dmcode` / `enumset` 反引号引用，将 `IFNULL` 改为 `COALESCE`，`DATE_FORMAT` 改为 `to_char`，分页改为 `LIMIT size OFFSET offset`，并删除两大段已废弃的历史注释 SQL，避免复扫噪音。
- `inspect` 中型 XML 收口
  - `inspectionWorkingPaperMapper.xml`：清理 `inspection` / `enumset` / `dmcode` / `jeecg-boot-os` 的 MySQL 风格引用。
  - `inspectionSupplementMapper.xml`：清理 `inspection` 反引号引用，`DATE_FORMAT` 改为 `to_char`，分页改为 `LIMIT size OFFSET offset`。
  - `inspectionRoutinePeriodMapper.xml`：清理 `inspection` 反引号引用，并将 `CAST(... AS SIGNED)` 改为 `CAST(... AS NUMERIC)`。
  - `inspectionPostSVTaskMapper.xml`：清理 `inspection_task_postsv` / `enumset` 引用，分页改为 `LIMIT size OFFSET offset`，并将 `CAST(... AS SIGNED)` 改为 `CAST(... AS NUMERIC)`。
  - `InspectionReceiptMapper.xml`：清理 `inspection` / `dmcode` 的 MySQL 风格引用。
  - `inspectionCheckAccountSheetMapper.xml`：清理 `inspection` / `dmcode` / `jeecg-boot-os` 的 MySQL 风格引用。
  - `InspectionPlanMapper.xml`：将 2 处 `FORMAT(...)` 改为 Vastbase/PostgreSQL 原生 `TO_CHAR(...)`，其中计划统计表的整数展示改为 `TO_CHAR(ROUND(...), 'FM...990')`，任务完成率改为 `TO_CHAR(ROUND(..., 2), 'FM...990.00')`，并用 `NULLIF(aa.total, 0)` 避免分母为 0 时出错。
  - `inspectionStatisticsTableMapper.xml`：将统计表动态列汇总里的 `FORMAT(SUM(...), 0)` 改为 `TO_CHAR(ROUND(CAST(SUM(...) AS NUMERIC), 0), 'FM...990')`，补齐 `inspect` 模块最后一处主链路 `FORMAT(...)` 残留。
- `inspect` 核心台账/整改 XML 收口
  - `questionBankMapper.xml`：`GROUP_CONCAT` 改为 `string_agg`，`IFNULL` 改为 `COALESCE`，并修正关键字查询中的 `AND/OR` 优先级；同时删除废弃注释 SQL，避免后续误扫。
  - `inspectionSelfLedgerMapper.xml`：清理 `inspection` / `dmcode` / `jeecg-boot-os` 反引号引用，`GROUP_CONCAT(... SEPARATOR '\n')` 改为 `string_agg(..., E'\n')`，并补齐 PostgreSQL 严格 `GROUP BY` 所需列。
  - `inspectionPostSVLedgerMapper.xml`：清理 `inspection` / `enumset` / `jeecg-boot-os` 反引号引用，`IFNULL` 改为 `COALESCE`，`GROUP_CONCAT` 改为 `string_agg`，并补齐 PostgreSQL 严格 `GROUP BY` 所需列。
  - `inspectionReformMapper.xml`：清理整改相关 `inspection_question_ledger*`、`inspection_question_bank`、`inspection_question_rule*` 的 MySQL 风格引用，并去掉计数查询中错误携带的分页限制。
- `inspect` 剩余 XML 收口
  - `InspectionTypeClassMapper.xml`、`inspectionTemporarySumMapper.xml`、`inspectionStatisticsTableMapper.xml`、`inspectionReminderMapper.xml`、`inspectionNotificationPushMapper.xml`、`inspectionPostSVworkDiaryMapper.xml`、`inspectionPostSVReportMapper.xml`、`inspectionPostSVReformMapper.xml`：继续清理 `inspection` / `dmcode` 反引号引用，分页统一改为 `LIMIT size OFFSET offset`，并修正 `inspectionPostSVReformMapper.xml` 中计数查询误带分页的问题；静态检查与局部规则复扫均已通过。
  - `InspectionNationalDebtMapper.xml`：将 `GROUP_CONCAT(IFNULL(...))` 改为 `string_agg(COALESCE(...), ',')`，并同步改造最大检查次数汇总 SQL。
  - `inspectionIssueListMapper.xml`、`inspectionPostSVListMapper.xml`、`inspectionQuestionLedgerMapper.xml`、`inspectionSelfSummaryMapper.xml`：清理 `inspection` / `enumset` / `dmcode` / `jeecg-boot-os` 的 MySQL 风格引用，并补齐 PostgreSQL 严格聚合场景下的 `MAX(...)` / `GROUP BY` 写法。
  - 以上 5 个剩余 `inspect` mapper 再次静态检查通过，`inspect/src/main/resources/mybatis/inspect` 下针对 `GROUP_CONCAT`、`IFNULL`、`DATE_FORMAT`、MySQL 反引号 schema、`AS SIGNED`、旧分页语法的规则复扫已清空。

### Java 动态 SQL

- `inspect/InspectionNationalDebtServiceImpl.java`
  - 动态列聚合中的 `IFNULL(CONVERT(SUM(...),CHAR),'')` 改为 `COALESCE(CAST(SUM(...) AS TEXT),'')`，避免依赖 MySQL 的 `CONVERT(...,CHAR)`。
- `indicatorsLibv-1.0/IndicatorsMineController.java`
  - 账期模板表达式中的 `DATE_FORMAT(@DATA_DATE, ...)` 改为 `to_char(@DATA_DATE, ...)`，覆盖日、月、年三种周期拼接。
- `indicatorsLibv-1.0/CreateSchemeSQL.java`
  - 主查询聚合由 `IFNULL(FORMAT(SUM(IF(...)),2),'')` 改为 PostgreSQL 可执行的 `COALESCE(CAST(ROUND(SUM(CASE WHEN ...),2) AS TEXT),'')`。
  - 季度账期比较中的 `CONVERT(..., SIGNED)` 改为 `CAST(... AS NUMERIC)`，日期区间比较中的 `STR_TO_DATE` 改为 `TO_DATE`。
  - 结果集筛选中的数字比较同步去掉 MySQL `CONVERT(..., DECIMAL)`。
- `fixedReport/ReportCenterController.java`
  - 两处汇总 SQL 中的 `IFNULL(sum(F_PAYAMT),0)` 改为 `COALESCE(sum(F_PAYAMT),0)`。
- `dwbi-statistical-analysis/CreateAnalysisSQL.java`
  - 当前可见的一批监测分析 SQL 已继续把 `IFNULL` 改为 `COALESCE`，`GROUP_CONCAT` 改为 `string_agg`，并清理 `system_docking` 的 MySQL 反引号引用，优先覆盖资源排名、构成分析、序时分析、资源告警和发送接收分析等热点片段。

### fixedReport

- `fixedReport/src/main/java/org/fixedReport/config/VastbaseCompatibilityInitializer.java`
  - 新增 Vastbase/PostgreSQL 启动兼容初始化器。
  - 在检测到数据库为 PostgreSQL/Vastbase 时，自动创建 `public.format(numeric, integer)`、`public.format(double precision, integer)`、`public.format(text, integer)` 三个兼容函数。
  - 先兼容 `fixedReport` 四个核心报表 XML 中大批量残留的 MySQL `FORMAT(value, digits)` 调用，避免继续逐条手改前就被该函数整体阻塞。

- `ReportTndustryMapper.xml`
  - 三处分页由 `LIMIT offset,size` 改为 `LIMIT size OFFSET offset`。
- `ReportMapper.xml`
  - 前半段公共图形查询中的 `GROUP_CONCAT` 改为 `string_agg(... ORDER BY ...)`。
  - 对应的 `ifnull/IFNULL` 聚合与数值兜底改为 `COALESCE`。
  - 清理部分 `indicators_lib` schema 反引号引用，先把折线图、饼图等共用图形 SQL 拉到 PostgreSQL/Vastbase 可执行范围。
  - 继续把文本描述区和区域分析区的一批 `GROUP_CONCAT` 文本拼接改为 `string_agg`。
  - `report.lib_indicators_000437/000438` 的指标文本、`getAreaParams` 的地区/主题/占比聚合，已不再依赖 MySQL 聚合函数。

## 目的

- 让 SEO 的数据源辅助页面在 Vastbase / PostgreSQL 类型下可以继续枚举 schema、表、字段与注释。
- 消除 PostgreSQL/Vastbase 严格 `GROUP BY` 与 MySQL 专属数值函数造成的运行时错误。

## 验证计划

- 编译验证：
  - `mvn -pl seo,dwbi-statistical-analysis -am -DskipTests compile`
  - `mvn -pl fixedReport -am -DskipTests compile`
- 若后续接入真实 Vastbase 数据源，再补一轮页面级验证：
  - SEO 数据源配置页的数据表下拉与字段注释加载
  - 统计分析历史分析接口与重点指标资源排行接口

## 追加进展

### Java 动态 SQL 继续收口

- `fixedReport/ReportCenterController.java`
  - 三处运行时分页 SQL 由 `LIMIT offset,size` 改为 `LIMIT size OFFSET offset`，避免集中支付明细/退库查询在 Vastbase 下分页语法报错。
- `dwbi-statistical-analysis/ComprehensiveQueryController.java`
  - 综合查询分页与导出限流 SQL 改为 PostgreSQL/Vastbase 兼容的 `LIMIT size OFFSET offset` / `LIMIT size`。
- `dwbi-statistical-analysis/AlarmStatisticsController.java`
  - 告警统计分页参数中的 `limit` 片段改为 `LIMIT size OFFSET offset`。
- `dwbi-statistical-analysis/AlarmDetailsController.java`
  - 告警明细分页参数中的 `limit` 片段改为 `LIMIT size OFFSET offset`。
- `indicatorsLibv-1.0/IndexSchemeController.java`
  - 指标方案数据预览分页由 `LIMIT offset,size` 改为 `LIMIT size OFFSET offset`。
- `indicatorsLibv-1.0/IndexRelationController.java`
  - 指标关系数据预览分页由 `LIMIT offset,size` 改为 `LIMIT size OFFSET offset`。
- `indicatorsLibv-1.0/IndicatorsMineServiceImpl.java`
  - 试跑 SQL 的抽样限制由 `LIMIT 0,10` 改为 `LIMIT 10`。
- `indicatorsLibv-1.0/IndicatorsMineController.java`
  - 两处季度账期模板由 `CONCAT(YEAR(@DATA_DATE),'Q',QUARTER(@DATA_DATE))` 改为 `CONCAT(EXTRACT(YEAR FROM @DATA_DATE),'Q',EXTRACT(QUARTER FROM @DATA_DATE))`。
  - 新增指标时的动态删除 SQL 去掉 `indicators_lib` schema 反引号。
- `indicatorsLibv-1.0/IndexRelationController.java`
  - 指标表格结果列的动态 SQL 拼接中，3 处残留 `FORMAT(V.xxx,2)` 改为 `CAST(ROUND(CAST(V.xxx AS NUMERIC),2) AS TEXT)`。
  - 同时把空串判断改为 `COALESCE(CAST(V.xxx AS TEXT),'')=''`，避免 Vastbase/PostgreSQL 下把数值列直接与空串比较。
- `fixedReport/ReportMapper.xml`、`fixedReport/QuarterReportMapper.xml`、`fixedReport/NewsFlashQuarterMapper.xml`
  - 继续收口报表账期表达式，把 `SUBSTR('${params.ACCOUNT_PERIOD}',1,4)-1/-4` 这类“文本直接减数字”的写法改为 `CAST(SUBSTR(...,1,4) AS NUMERIC)-n`。
  - 该类语句在 MySQL 下依赖隐式转换，Vastbase/PostgreSQL 下需要显式数值化后再参与 `CONCAT` / `BETWEEN` 条件拼接。
- 配置与边界复核
  - 清理 `fixedReport`、`org-tribe-system`、`indicatorsLibv-1.0`、`inspect`、`seo` 的 `application-*.yml` 中残留的注释性 MySQL URL / driver 示例，减少后续全仓巡检噪音。
  - 继续保留 `seo` 中 `DataSourceController` / `DBHelper` 的 MySQL 连接测试能力；该部分用于外部数据源接入，不属于主库 Vastbase 迁移残留。
- `indicatorsLibv-1.0/CreateSchemeSQL.java`
  - 条件筛选里的 `CONVERT(REPLACE(...), DECIMAL)` 改为 `CAST(REPLACE(...) AS DECIMAL)`。

### 指标库与系统 XML 继续收口

- `indicatorsLibv-1.0/indicatorsMineMapper.xml`
  - 清理 `indicators_lib` schema 反引号。
  - 动态建表 DDL 去掉 MySQL 专属的 `ENGINE`、`CHARSET`、`COLLATE`、列级 `COMMENT`，改为 Vastbase/PostgreSQL 可执行的简化建表语句。
  - 手工跑批与历史跑批 `CALL` 语句改为无反引号 schema 调用。
- `org-tribe-system/SubjectImportMapper.xml`
  - `ods` schema 的增删改查与存储过程调用全部去掉 MySQL 反引号，统一成 PostgreSQL/Vastbase 兼容写法。

### fixedReport / inspect 追加收口

- `fixedReport/ReportTndustryMapper.xml`
  - 将 5 处基于 `@p/@s/@r` 的 MySQL 用户变量分组排名逻辑改为 `ROW_NUMBER() OVER (PARTITION BY INDUSTRY_NAME ORDER BY ...)`。
  - 清理 `adm.trs_mth_income_industry` 与 `dmcode.cm_report_title` 的 MySQL 反引号引用。
- `fixedReport/KydReportMapper.xml`
  - 两处 `IF(#{params.industryType} = TRUE, ..., LEV_4_DSCR)` 改为标准 `CASE WHEN`。
  - 继续清理一处列级反引号 `a.name`。
- `inspect/inspectionCaseMapper.xml`
  - 清理人才名称列 `d.NAME` 的 MySQL 反引号写法。
- `inspect/inspectionApprovalMapper.xml`
  - 清理审批/申请人名称列 `e.NAME`、`g.NAME` 的 MySQL 反引号写法。

### org-tribe-system / inspect 继续清理低成本尾项

- `org-tribe-system/enumMapper.xml`
  - 清理 `enumset.cm_guoku_enumeration` 的残余 MySQL 反引号引用。
- `org-tribe-system/AreaMapper.xml`
  - 清理 `DMCODE.CM_GUOKU_AREA_CODE` 上的残余反引号及列级标识符反引号。
- `inspect/InspectionCashBondMapper.xml`
  - 清理 `guoku_dscr`、`guoku_id` 的列级反引号。
- `inspect/inspectionProcSubMapper.xml`
  - 清理流程子表和主流程表关联条件上的列级/表级反引号。
- `inspect/inspectionPostSVProcSubMapper.xml`
  - 清理 PostSV 流程子表与主流程表关联条件上的列级反引号。
- `inspect/InspectionPlanMapper.xml`
  - 清理检查组名称、被查库、人员表和 `edw.cm_guoku_dimnsn` 的残余反引号引用。
- `inspect/InspectionReportMapper.xml`
  - 清理组员姓名、职责字段与 `GROUP BY` 中的列级反引号。
- `inspect/inspectionRegisterBookMapper.xml`
  - 清理 `TYPE_CODE`、`BOOK_ID`、`CONTENT_PID`、`INSPECTED_GUOKU_ID`、`INSPECTION_TASK_ID` 的列级反引号。
- `inspect/inspectionSupplementMapper.xml`
  - 清理 `LEDGER_ID`、`QUESTION_CONTENT`、`RULE_FILE_*` 的列级反引号。
- `inspect/inspectionTemporarySumMapper.xml`
  - 清理 `QUESTION_ID`、`QUESTION_ID_1`、`QUESTION_ID_2` 的列级反引号。
- `org-tribe-system/GuokuMapper.xml`
  - 清理 `cm_guoku_dimnsn`、`cm_guoku_bookorg`、`t_area_code` 相关 schema/table 反引号。
  - 同步清理 `guoku_id`、`guoku_pid`、`bookorgcodepid`、`AREA_NO_ID`、`AREA_DSCR`、`state` 等列级反引号。

### 本轮继续收口

- `inspect/inspectionQuestionLedgerMapper.xml`
  - 清理用户台账聚合查询末尾残留的列级反引号，覆盖 `QUESTION_ID_1`、`QUESTION_ID_2`、`QUESTION_ID`、`IS_LIST`、`IS_REPORT` 及对应拼接字段。
- `seo/DataSourceInfoMapper.java`

### 2026-04-19 配置统一切库

- 按最新要求，将源码和仓库内 `jar/`、`jar/sys/` 外部化配置中的主库数据源统一切到 `jdbc:postgresql://100.71.11.54:25432/gk_data_analysis`。
- 主库账号统一改为 `vastbase_test`，并同步更新对应密码。
- 各模块保留原有 `currentSchema` 设定，仅替换主机、端口、数据库名与账号密码；`dwbi-system-docking` 的 ClickHouse 数据源未改动。
- 同时清理配置文件里残留的旧库注释地址与旧账号注释，避免后续排查时混淆新旧环境。
  - 清理注解 SQL 中 `PASSWORD` 列的 MySQL 反引号写法。
- `indicatorsLibv-1.0/indicatorsMineMapper.xml`
  - 清理 `indicators_lib.lib_index_relation` 更新语句的表级反引号。
- `indicatorsLibv-1.0/IndexRelationMapper.xml`
  - 将 `FIND_IN_SET(#{category}, r.CATEGORY)` 改为 `#{category} = ANY(string_to_array(COALESCE(r.CATEGORY, ''), ','))`。
- `fixedReport/CentralizedPaymentMapper.xml`
  - 清理 `GUOKU_ID`、`GUOKU_LVL_ID_3`、`OLD_GUOKU_ID` 的列级反引号。
  - 为 `AND ... OR ...` 条件补括号，避免 Vastbase/PostgreSQL 下按默认优先级导致查询语义偏移。
- `dwbi-system-docking/mysql/ConfigMapper.xml`
  - 清理 `insertProcessRecords` 中 `system_docking` schema 的 MySQL 反引号。
  - 将运行期动态建表 SQL `createTable`、`createNetPerformanceEventLog`、`createAlarmLogAbnormalBehavior` 从 MySQL DDL 改写为 PostgreSQL/Vastbase 可执行语法，移除 `CHARACTER SET`、`COLLATE`、`ENGINE`、`ROW_FORMAT`、行内 `INDEX(...)`、列级 `COMMENT` 等 MySQL 专属定义。
- `dwbi-system-docking/clickhouse/ClickHouseConfigMapper.xml`
  - 明确识别为 ClickHouse 专用适配层，不纳入 Vastbase 主数据源收口范围。
  - 保持 ClickHouse 原有 SQL 逻辑不变，不对 `GROUP_CONCAT`、`MergeTree` 及其原始写法做 Vastbase 化处理。
- `fixedReport/ReportCenterController.java`、`seo/ComprehensiveQueryController.java`、`indicatorsLibv-1.0/IndexSchemeMapper.xml`
  - 清理注释中的历史 MySQL 片段，消除规则复扫假阳性。

### 本轮静态校验补充

- `ReportTndustryMapper.xml`、`KydReportMapper.xml`、`inspectionCaseMapper.xml`、`inspectionApprovalMapper.xml` 的 XML 静态检查均通过。
- 针对 `IF(@p=...)`、`@p:=`、`@s:=`、`adm.\`trs_mth_income_industry\``、`dmcode.\`cm_report_title\``、`\`NAME\`` 的定向复扫已清空。
- `enumMapper.xml`、`AreaMapper.xml`、`InspectionCashBondMapper.xml`、`inspectionProcSubMapper.xml`、`inspectionPostSVProcSubMapper.xml`、`InspectionPlanMapper.xml`、`InspectionReportMapper.xml`、`inspectionRegisterBookMapper.xml`、`inspectionSupplementMapper.xml`、`inspectionTemporarySumMapper.xml` 的 XML 静态检查均通过。
- 针对上述文件目标反引号关键字的定向复扫已清空。
- `GuokuMapper.xml` 的 XML 静态检查通过，针对 `cm_guoku_dimnsn` / `cm_guoku_bookorg` / `t_area_code` 及对应列级反引号的定向复扫已清空。
- `inspectionQuestionLedgerMapper.xml`、`inspectionPostSVReportMapper.xml`、`CentralizedPaymentMapper.xml` 的静态检查均通过。
- `inspect/src/main/resources/mybatis/inspect` 下针对反引号、`GROUP_CONCAT`、`IFNULL`、`DATE_FORMAT`、旧分页和 `AS SIGNED` 的规则复扫已清空。
- `fixedReport/CentralizedPaymentMapper.xml` 针对列级反引号的定向复扫已清空。
- `dwbi-system-docking/mysql/ConfigMapper.xml` 的 XML 静态检查通过，针对 `system_docking` 反引号及 MySQL DDL 关键字的定向复扫已清空。
- 排除 ClickHouse 专用层后，`org-tribe-system`、`inspect`、`fixedReport`、`dwbi-statistical-analysis`、`seo`、`indicatorsLibv-1.0`、`dwbi-system-docking/mybatis/mysql` 在本轮规则下已无真实业务命中。

### 本轮编译验证

- `cd indicatorsLibv-1.0 && mvn -DskipTests compile`：`BUILD SUCCESS`
- `cd dwbi-statistical-analysis && mvn -DskipTests compile`：`BUILD SUCCESS`
- `cd org-tribe-system && mvn -DskipTests compile`：`BUILD SUCCESS`
- `cd inspect && mvn -DskipTests compile`：`BUILD FAILURE`
  - 失败原因为当前执行环境触发 `jdk.compiler` 模块访问限制：`module jdk.compiler does not "opens com.sun.tools.javac.processing"`。
  - 这属于当前编译运行时/JDK 模块开放问题，不是本轮 XML 修改造成的业务编译错误；项目基线仍按 JDK8 约束看待。
- `cd org-tribe-system && mvn -DskipTests compile`（追加回归）：`BUILD SUCCESS`
- `cd seo && mvn -DskipTests compile`：`BUILD SUCCESS`
- `cd indicatorsLibv-1.0 && mvn -DskipTests compile`（追加回归）：`BUILD SUCCESS`
- `cd fixedReport && mvn -DskipTests compile`（追加回归）：`BUILD SUCCESS`
- `cd dwbi-system-docking && mvn -DskipTests compile`（追加回归）：`BUILD SUCCESS`

### 当前剩余热点判断

- 继续全仓规则复扫后，普通业务模块中的低成本 MySQL 残留已进一步收缩。
- 当前高密度命中主要集中在：
  - `dwbi-system-docking/src/main/resources/mybatis/clickhouse/ClickHouseConfigMapper.xml`
- 这些命中多数位于专用数据源 mapper、建表 DDL、存储过程/方言专属片段，不再属于“可安全机械替换的普通业务尾项”，后续需要按目标数据源职责逐段判断，而不是继续做全仓文本替换。
- 其中 `dwbi-system-docking/src/main/resources/mybatis/mysql/ConfigMapper.xml` 已完成运行期基础 DDL 收口，后续更高风险项主要转为 ClickHouse 专用 mapper 和存储过程链路。

### 当前收口结论

- 以 `GROUP_CONCAT`、`IFNULL`、`DATE_FORMAT`、`STR_TO_DATE`、`FIND_IN_SET`、`AS SIGNED`、旧分页和 MySQL 反引号为规则，对 Vastbase 主路径再次总复扫后，已无真实业务命中。
- 当前剩余命中仅为 `dwbi-system-docking/src/main/resources/mybatis/clickhouse/ClickHouseConfigMapper.xml` 中 3 处 `GROUP_CONCAT`，属于 ClickHouse 专用适配层原始保留内容，不构成 Vastbase 迁移未完成项。
- 因此本轮范围内“全项目 MySQL -> Vastbase 主路径适配”可视为完成，后续若继续推进，应转入 ClickHouse 侧专用方言治理或运行期联调验证，而不是继续按 Vastbase 规则做机械替换。