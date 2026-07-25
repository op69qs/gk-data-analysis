# 数据分析平台外部数据源迁移现场操作与测试手册

## 1. 适用范围

本文用于现场验证数据分析平台迁移到 Vastbase 后，原有外部数据源是否仍能接入、数据加工流程是否能正常运行、页面展示数据是否可信。

> **全量逐条清单**（全部 Shell、存储过程、Event、Shell→过程→表链路、ER 登记簿）：见同目录 [`data_processing_pipeline_full_inventory.md`](data_processing_pipeline_full_inventory.md)。

本手册覆盖三类验证：

1. 所有数据入口测试：外部库、Shell 抽数、接口调度、数据源配置、接口配置、动态分表写入。
2. 所有数据调用过程测试：Vastbase 初始化脚本、存储过程、Event、`dwbi-system-docking` 子进程调度、过程调用链。
3. 所有数据展示验证：统计分析、固定报表、指标库、可视化大屏、SEO 数据查询、运维监控类页面。

现场测试原则：

- 每条链路必须形成证据：源端数量、目标落库数量、过程执行结果、展示页面截图或接口返回。
- `adm` 视为展示层，不作为最终数据来源；需要继续向下追到 `edw`、`ods`、`stg` 或外部接口/外部库。
- Vastbase Event 当前按交付脚本默认 `DISABLE` 创建，必须先完成依赖过程和数据校验，再按需启用。
- 测试中不要只看页面是否打开，必须同时核对页面指标值与后台 SQL 查询结果。



### 4.3 对象数量核对

```sql
select n.nspname as schema_name, count(*) as table_count
from pg_class c
join pg_namespace n on n.oid = c.relnamespace
where c.relkind in ('r','p')
  and n.nspname in ('etl','ods','report','edw','visual_screen','indicators_lib','ucloud','upm','system_docking')
group by n.nspname
order by n.nspname;

select routine_schema, routine_type, count(*) as routine_count
from information_schema.routines
where routine_schema in ('etl','ods','report','edw','visual_screen','indicators_lib','ucloud','upm')
group by routine_schema, routine_type
order by routine_schema, routine_type;

show events;
```

通过标准：

- 上述 schema 已存在。
- `report`、`edw`、`visual_screen`、`indicators_lib`、`ods`、`etl` 至少能查到已迁移 routine。
- Event 能查到 7 个替代事件，初始状态允许为 `DISABLE`。

## 5. 数据入口测试

### 5.1 Shell 抽数入口

老抽数脚本位于 `document/dataInterface/shell/`，现场需要按真实外部库逐项测试。

| 入口 | 源端 | 目标层 | 脚本 | 频率/方式 | 验证重点 |
| --- | --- | --- | --- | --- | --- |
| 集中支付 | DB2 `tessdb.DB2TESS` | `ods.*` | `db2_to_mysql.sh`、`db2_to_mysql_1030.sh` | 每日 22:00，增量 | `payout.list` 中每张表源端和目标端数量一致 |
| 凯盈达收入明细 | MySQL `gkdas.tv_fin_income_detail` | `ods.tv_fin_income_detail` | `mysql_to_mysql.sh`、`mysql_to_mysql_1030.sh`、`tv.sh` | 按日/月 | `s_intredate` 日期范围一致，分区/月份数据能被后续过程读取 |
| 全程电子退库 | Oracle `rhdzhtk.*` | `ods.PT_*`、`ods.DM_*`、`ods.QX_*` | `oracle_to_mysql.sh`、`oracle_to_mysql_1030.sh`、`oracle_to.sh` | 每日 22:00，全量 | TXT 导出行数、目标表行数、关键日期字段格式 |

现场测试步骤：

1. 不直接改生产脚本，复制一份到测试目录，将日期固定为一个可核验日期。
2. 源端执行 `count(*)`，记录源表数量。
3. 执行脚本。
4. 目标 Vastbase 执行 `count(*)` 和关键字段抽样。
5. 对比源端、文件行数、目标端数量。

证据模板：

| 数据入口 | 表名 | 测试日期 | 源端行数 | 文件行数 | 目标行数 | 结果 | 证据文件 |
| --- | --- | --- | ---: | ---: | ---: | --- | --- |
| DB2 支付 |  |  |  |  |  |  |  |
| MySQL 收入明细 | `ods.tv_fin_income_detail` |  |  |  |  |  |  |
| Oracle 退库 |  |  |  |  |  |  |  |

### 5.2 `dwbi-system-docking` 接口入口

源码确认该模块从 Redis 读取进程、线程、接口配置，并调用外部接口后写入 `ucloud` / `upm` 动态表，完成后再调用汇总过程。

配置表检查：

```sql
select id, task_code, task_name, is_enable
from system_docking.api_task_ins
where is_enable = '1'
order by tesk_sort;

select task_id, id, task_thread_desc, task_thread_cron, is_enable
from system_docking.api_task_thread_ins
where is_enable = '1'
order by task_id, tesk_thread_sort;

select h.id, h.api_contract, h.api_type, h.api_method, h.api_index_code, h.is_enable
from system_docking.api_system_api_info h
where h.is_enable = '1'
order by h.id;
```

接口连通性检查：

```bash
curl -sS -m 10 "$UCLOUD_TEST_URL" 2>&1 | tee evidence/10_ucloud_api_connect.log
curl -sS -m 10 "$UPM_TEST_URL" 2>&1 | tee evidence/11_upm_api_connect.log
```

进程调度检查：

- Redis 中应存在 `PROCESS_CONFIG`、`PROCESS_LIST`、`SYSTEM_CONFIG_<processId>`、`INTERFACE_CONFIG_<processId>`、`CRON_<processId>`。
- 子进程启动后日志应出现 `executor start`、接口调用日志、`执行存储过程进行数据汇总`、`executor end`。
- 执行完成后应写入接口记录表和业务数据表。

数据表检查：

```sql
select count(*) from ucloud.api_interface_alarm_data;
select count(*) from ucloud.api_interface_system_data;
select count(*) from upm.api_interface_system_data;

select table_schema, table_name
from information_schema.tables
where table_schema in ('ucloud','upm')
  and (table_name like 'api_interface_system_data%' or table_name like 'netperformanceeventlog_%' or table_name like 'alarmlogabnormalbehavior_%')
order by table_schema, table_name;
```

过程调用检查：

```sql
call ucloud.ucloud_api_interface_alarm_data('ucloud.api_interface_alarm_data', '<测试日期>', @ret);
call ucloud.ucloud_api_interface_system_data('ucloud.api_interface_system_data<YYYYMM>', '<测试日期>', @ret);
call upm.upm_proc_api_alarm_summary_alarmlog('upm.alarmlogabnormalbehavior_<YYYYMMDD>', @ret);
call upm.upm_proc_api_alarm_summary_netper('upm.netperformanceeventlog_<YYYYMMDD>', @ret);
call upm.upm_proc_api_alarm_summary_interface('upm.api_interface_system_data<YYYYMMDD>', @ret);
```

如果现场 Vastbase 不支持 `@ret` 变量写法，按实际过程签名改为 `call ...` 并在日志表/目标汇总表验证结果。

通过标准：

- 配置表能查到启用的进程、线程、接口。
- 外部 API 返回 200 或业务成功码。
- 原始数据表有新增数据。
- 汇总过程执行无异常。
- 汇总表行数或指标值发生符合预期的变化。

## 6. 数据调用过程测试

### 6.1 过程分层和推荐执行顺序

按依赖从下往上测试：

1. `etl`：日志桩过程可调用。
2. `ods`：贴源层搬运过程。
3. `ucloud` / `upm`：接口数据汇总过程。
4. `edw`：核心统计过程。
5. `report`：月报、季报、快报文本和统计表过程。
6. `visual_screen`：大屏 `P_task_vscreen` 及其直接依赖。
7. `indicators_lib`：指标公式和 `p_xunhuan_formula` 编排。
8. Event：只在上述手工过程均通过后启用。

### 6.2 核心过程测试清单

| 层级 | 过程 | 输入参数 | 目标表/结果 | 验证方式 |
| --- | --- | --- | --- | --- |
| `etl` | `EDW_PROC_TRACE_LOG`、`EDW_PROC_ERROR_LOG` | 过程内日志参数 | 不阻断业务过程 | 手工调用或间接调用无报错 |
| `ods` | `p_pt_gy_files_temp` | 无 | 退库/文件相关 ODS 表 | 执行前后行数和日期字段 |
| `edw` | `P_TRS_BUDGET_INCOME_COMPARE`、`P_TRS_BUDGET_INCOME_COMPARE_NEW` | 日期 | 预算收支比较结果 | 结果表行数、金额汇总 |
| `edw` | `P_TRS_FINANCE_TAX_STATIS`、`P_TRS_FINANCE_TAX_STATIS_NEW` | 日期 | `edw.trs_finance_tax_statis` | `DATA_DATE + AREA_NO + SERVICE_TYPE + ROWS_ID` 分组结果 |
| `report` | `p_trs_income_payout_statistics` | 日期 | `report.TRS_INCOME_PAYOUT_STATISTICS*` | 收入/支出统计和页面一致 |
| `report` | `P_MONTH_REPORT_TEXT`、`P_QUARTER_REPORT_TEXT`、`P_NEWS_FLASH_*` | 日期/月季 | 月报、季报、快报文本表 | 文本表有对应期别数据 |
| `visual_screen` | `p_task_vscreen` | `YYYYMMDD` | 大屏各 `p_vs_*` 结果表 | 大屏接口和后台数一致 |
| `indicators_lib` | `init_report01/02/03` | 月末日期 | 指标计算结果 | 指标页面可查到对应期别 |
| `indicators_lib` | `p_xunhuan_formula` | `YYYYMMDD` | 月报/季报/快报/指标结果 | 全链路编排成功 |

### 6.3 手工执行模板

每个过程都按“执行前快照 -> 调用 -> 执行后快照 -> 页面/接口验证”记录。

```sql
-- 1. 执行前
select count(*) as before_count from <schema>.<target_table> where <date_column> = '<测试日期>';

-- 2. 调用过程
call <schema>.<procedure_name>('<测试日期>');

-- 3. 执行后
select count(*) as after_count from <schema>.<target_table> where <date_column> = '<测试日期>';
select * from <schema>.<target_table> where <date_column> = '<测试日期>' limit 20;
```

异常判定：

- 过程报缺表：先确认 `document/psql/vastbase/final` 是否已执行，再确认对象 schema 是否正确。
- 过程报缺函数/过程：回到 `040_missing_routines_inventory.md` 对照依赖链，不能直接启用上层 Event。
- 过程成功但目标无数据：先查源表对应日期是否有数据，再查过程过滤条件。
- 目标有数据但页面无展示：进入第 7 章做接口和页面验证。

### 6.4 Event 启用前检查

Event 脚本包含 7 个替代事件：

- `indicators_lib_p_init_report01`
- `indicators_lib_p_init_report02`
- `indicators_lib_p_init_report03`
- `indicators_lib_p_xunhuan_formula`
- `ods_pt_gy_files_task`
- `seo_p_task_vs`
- `visual_screen_p_task_vs`

启用前必须满足：

- 对应 `CALL` 手工执行通过。
- 目标表有对应日期数据。
- 应用页面或接口可展示。
- 现场确认调度时间不会与人工补数、旧调度、外部 Shell 重叠。

启用模板：

```sql
alter event indicators_lib_p_init_report01 enable;
alter event indicators_lib_p_init_report02 enable;
alter event indicators_lib_p_init_report03 enable;
alter event indicators_lib_p_xunhuan_formula enable;
alter event ods_pt_gy_files_task enable;
alter event seo_p_task_vs enable;
alter event visual_screen_p_task_vs enable;
show events;
```

## 7. 数据展示验证

### 7.1 展示验证总规则

每个页面至少保留三份证据：

1. 页面截图：筛选条件、表格/图表、时间区间必须可见。
2. 浏览器接口返回：URL、状态码、请求参数、返回数据条数。
3. 后台 SQL：同口径查询结果，与页面核心指标一致。

页面可打开但数据为空不等于通过，必须确认后台同口径也是空，并说明源端是否确实无数据。

### 7.2 页面清单

| 模块 | 页面/功能 | 后台链路 | 验证重点 |
| --- | --- | --- | --- |
| 首页/分析首页 | `dashboard/analysis` | `org-tribe-system` 菜单和聚合接口 | 登录、菜单、核心指标卡片 |
| 固定报表 | 报告管理、月报、季报、快报 | `fixedReport` + `report.*` | 报表期别、文本、表格金额 |
| 指标库 | 指标查询、指标管理、公共指标管理 | `indicatorsLibv-1.0` + `indicators_lib.*` | 指标公式、指标值、期别 |
| 行业/企业报表 | 分行业报表、企业报表、企业排名 | `fixedReport` / `adm` / `edw` / `ods` | `adm` 展示值继续追到 `edw/ods` |
| 动态刷数 | 动态刷数页面 | `indicators_lib.p_xunhuan_formula` | 手工触发后过程和结果表变化 |
| 统计分析 | 告警、预测、资源排行 | `dwbi-statistical-analysis` + `system_docking/ucloud/upm` | 外部接口数据能被页面消费 |
| 可视化大屏 | `/vis/gallery`、大屏预览、模板/页面 | `visual_screen.*` | `p_task_vscreen` 后大屏指标刷新 |
| SEO 数据查询 | 数据源维护、数据表维护、数据查询、维度表 | `seo` | 数据源枚举、表树、查询结果、维度数据 |

### 7.3 SQL 对账模板

固定报表/指标库：

```sql
select count(*) from report.t_month_report_fact where <period_column> = '<期别>';
select * from report.t_month_report_fact where <period_column> = '<期别>' limit 20;

select count(*) from indicators_lib.<指标结果表> where <date_column> = '<期别>';
```

财政税收链路：

```sql
select data_date, area_no, service_type, rows_id, count(*), sum(<金额字段>)
from edw.trs_finance_tax_statis
where data_date = '<测试日期>'
group by data_date, area_no, service_type, rows_id
order by area_no, service_type, rows_id
limit 50;
```

收入明细链路：

```sql
select count(*) from ods.tv_fin_income_detail where s_intredate like '<YYYYMM>%';
select count(*) from ods.trs_info_enterprises;
```

UCloud/UPM 链路：

```sql
select count(*) from ucloud.api_interface_alarm_data;
select count(*) from ucloud.api_interface_system_data;
select count(*) from upm.api_interface_system_data;
```

可视化大屏：

```sql
select table_schema, table_name
from information_schema.tables
where table_schema = 'visual_screen'
order by table_name;
```

## 8. 总体验收矩阵

| 链路 | 数据入口通过 | 过程调用通过 | 展示验证通过 | 备注 |
| --- | --- | --- | --- | --- |
| DB2 支付 -> `ods` -> 报表/指标 |  |  |  |  |
| MySQL 凯盈达 -> `ods.tv_fin_income_detail` -> `edw/adm` -> 行业/企业报表 |  |  |  |  |
| Oracle 退库 -> `ods.PT_*` -> `ods.p_pt_gy_files_temp` -> 页面 |  |  |  |  |
| UCloud API -> `ucloud` -> 汇总过程 -> 统计分析/告警页面 |  |  |  |  |
| UPM API -> `upm` -> 汇总过程 -> 统计分析/性能页面 |  |  |  |  |
| `report` 月报/季报/快报过程 -> 固定报表页面 |  |  |  |  |
| `indicators_lib` 指标过程 -> 指标库页面 |  |  |  |  |
| `visual_screen.p_task_vscreen` -> 可视化大屏 |  |  |  |  |
| 7 个 Vastbase Event 自动调度 |  |  |  |  |

验收结论只能填：

- `通过`：入口、过程、展示三段证据齐全。
- `有条件通过`：功能可用，但存在明确数据空表、源端无数据、历史基线同样异常等说明。
- `不通过`：任一关键环节失败，且不能用源端无数据或历史基线问题解释。

## 9. 常见问题判定

| 现象 | 优先判断 | 处理方式 |
| --- | --- | --- |
| Shell 执行成功但目标无数据 | 日期条件或目标 schema 不一致 | 固定日期复跑，核对源端 count 和目标表名 |
| `dwbi-system-docking` 不启动子进程 | Redis 配置未加载或 `ProcessConfig` 未启用 | 查 `PROCESS_CONFIG`、`PROCESS_LIST`、`CRON_<processId>` 和应用日志 |
| 接口记录表有失败状态 | 外部 API 不通、认证失败、参数错误 | 保留请求 URL、状态码、返回体，找外部系统确认 |
| `CALL ucloud/upm` 报错 | 动态分表或过程依赖缺失 | 先确认 `008` 到 `012` 初始化脚本已执行，再查动态表 |
| Event 不执行 | Event 未启用或全局开关阻止 | 查 `show events` 和 `enable_prevent_job_task_startup` |
| 过程成功但页面无数据 | 页面接口条件与过程日期口径不一致 | 浏览器抓接口参数，按同参数查后台 SQL |
| 页面 500 | 后台 SQL、缺表、缺过程、权限或网关路由 | 以接口日志和模块日志为准，不只看前端提示 |
| 页面数据为空 | 可能是源端本来无数据 | 必须提供源端 count、目标 count、过程日志三份证据 |

## 10. 现场证据目录建议

```text
evidence/
  00_connectivity/
  01_vastbase_init/
  02_shell_sources/
    db2/
    oracle/
    mysql/
  03_docking_api/
    redis/
    ucloud/
    upm/
    logs/
  04_procedure_calls/
    ods/
    edw/
    report/
    indicators_lib/
    visual_screen/
    ucloud_upm/
  05_event_scheduler/
  06_pages/
    fixedReport/
    indicatorsLib/
    statisticalAnalysis/
    vis/
    seo/
  99_summary/
```

最终提交材料至少包含：

- `summary.xlsx` 或同等表格：按第 8 章验收矩阵填写。
- 每条失败项的错误日志和 SQL 证据。
- 每条通过项的页面截图、接口返回和后台 SQL 结果。
- Event 是否启用、启用时间、启用后的第一次执行结果。

## 11. 最终结论模板

```text
本次现场验证范围：<模块/链路范围>
Vastbase 初始化：通过/不通过
外部数据入口：通过 <N> 项，有条件通过 <N> 项，不通过 <N> 项
数据加工过程：通过 <N> 项，有条件通过 <N> 项，不通过 <N> 项
页面展示验证：通过 <N> 项，有条件通过 <N> 项，不通过 <N> 项
Event 自动调度：已启用/未启用，原因：<原因>

总体结论：
<通过 / 有条件通过 / 不通过>

遗留问题：
1. <问题、影响、证据、责任系统、建议处理人>
2. <问题、影响、证据、责任系统、建议处理人>
```
