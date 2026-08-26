# Vastbase 生产初始化脚本

## 交付结论

本目录以 `all_event.sql`（2026-07-29 现场提供的 MySQL 全库 Event 和
routine 导出）为唯一业务过程基线。筛选规则不是“迁移所有过程”，而是从
全部 Event 的 `DO CALL` 开始，递归保留直接和间接调用到的过程/函数。

静态闭包审计结果：

- MySQL Event：15 个，目标缺失 0、额外 0。
- MySQL routine 总数：361 个；Event 可达闭包：76 个。
- Event 闭包目标：76 个全部覆盖，另保留 2 个 `visual_screen` 编排适配器，
  共 77 个过程、1 个函数。
- 生产初始化链另包含 `dwbi-system-docking` 直接调用的 7 个 ucloud/upm
  过程；它们不属于 Event 闭包。整包合计 84 个过程、1 个函数。
- Event 调用与调度语义不一致 0；参数模式不一致 0；未解释的调用边差异 0；
  未解释的 DML 目标差异 0。
- ADM 不再全量搬运：5 个 Event 最终只需要 13 个 ADM routine
  （12 个过程、1 个函数）和 5 个 EDW 下游依赖过程。

闭包按源 schema 分布：

| schema | Event | Event 可达 routine |
| --- | ---: | ---: |
| adm | 5 | 13 |
| edw | 2 | 8 |
| etl | 2 | 4 |
| indicators_lib | 4 | 7 |
| ods | 1 | 1 |
| report | 0 | 5 |
| visual_screen | 1 | 38 |
| 合计 | 15 | 76 |

## 生产执行范围

生产只执行 `000_run_all.sql`。入口全部使用相对 `\ir`，现场只需把本目录
中的文件原样放在同一个目录，不依赖开发机路径。入口依次加载 19 个脚本：

1. `001_etl_init.sql`：真实 ETL 过程跟踪/错误日志表及两个日志过程。
2. `001a_etl_event_routines_init.sql`：两个 ETL Event 入口过程及运行日志表。
3. `002_ods_init.sql`：ODS 闭包过程。
4. `003_report_init.sql`：Report 闭包过程。
5. `004_edw_init.sql`：EDW 闭包过程。
6. `005_visual_screen_init.sql`：38 个源闭包过程及 2 个任务拆分适配器。
7. `006_indicators_lib_init.sql`：指标库闭包过程。
8. `006a_event_dependency_routines_init.sql`：EDW Event 的两个入口过程。
9. `007_events_init.sql`：ETL、EDW、指标库、ODS、大屏共 10 个 Event。
10. `008_ucloud_tables_init.sql`：ucloud 相关表。
11. `009_upm_alarmlog_tables_init.sql`：UPM 告警日志表。
12. `010_upm_system_data_tables_init.sql`：UPM 系统数据表。
13. `011_upm_netperformance_tables_init.sql`：UPM 性能数据表。
14. `012_ucloud_upm_procedures_init.sql`：`dwbi-system-docking` 使用的 5 个业务
    入口过程及 2 个历史辅助过程。
15. `013_adm_tables_init.sql`：331 张 ADM 表，已包含动态刷新日志表及索引。
16. `014_adm_indexes_init.sql`：90 个 ADM 索引。
17. `014a_adm_dependency_routines_init.sql`：ADM 闭包需要的 5 个 EDW 过程。
18. `015_adm_routines_init.sql`：12 个 ADM 过程、1 个 ADM 函数。
19. `016_adm_events_init.sql`：5 个 ADM Event。

`012_ucloud_upm_procedures_init.sql` 不在 Event 可达闭包内，但属于应用直接调用链，
因此作为显式生产扩展保留；其 5 个业务入口为
`ucloud.ucloud_api_interface_alarm_data`、`ucloud.ucloud_api_interface_system_data`、
`upm.upm_proc_api_alarm_summary_alarmlog`、
`upm.upm_proc_api_alarm_summary_netper` 和
`upm.upm_proc_api_alarm_summary_interface`。
`017_dynamic_refresh_run_log_init.sql` 只供已有库增量升级；新库所需定义已经合并
进 `013_adm_tables_init.sql`，因此不在总入口重复执行。

## 是否支持多次执行

当前脚本已按“对象级幂等”改造，可在同一库重复执行 `000_run_all.sql`：

| 对象类型 | 策略 | 说明 |
| --- | --- | --- |
| Schema | `CREATE SCHEMA IF NOT EXISTS` | 可重复 |
| 表 | `CREATE TABLE IF NOT EXISTS` | 可重复；**不会删表、不会改已有列结构、不会清数据** |
| 索引 | `CREATE INDEX IF NOT EXISTS` | 可重复 |
| 过程/函数 | `DROP ... IF EXISTS` + `CREATE` | 可重复；每次以脚本定义覆盖 |
| Event | `DROP EVENT IF EXISTS` + `CREATE EVENT IF NOT EXISTS` | 可重复；若现场不支持 Event，该项需改外部调度 |

注意：

1. 多次执行是“补齐缺失对象 / 覆盖过程定义”，不是“重建空库”。
2. 若要把表结构改到与脚本完全一致，需要单独的变更脚本或先备份后手工 `DROP` 再跑初始化。
3. 过程体内的 `DROP TABLE`（如 ETL 临时表）属于运行时逻辑，不影响初始化幂等。

## 执行成功校验

对象级校验使用同目录 `000_verify_init.sql`：

```bash
cd <final目录>
vsql -h <host> -p <port> -U <user> -d <database> -W '<password>' \
  -v ON_ERROR_STOP=1 -f 000_verify_init.sql
```

也可使用 `000_run_all_full.sql`（先跑 19 个初始化脚本，再自动 `\ir 000_verify_init.sql`）。

校验项包含：12 个 schema、84 个过程分 schema 计数、关键业务入口过程、
`adm.num_char` 函数、ucloud/upm/adm 表数量、关键日志表、ADM 索引数量、15 个 Event。
输出列 `pass=Y/N`；最后一节只列出未通过项。

## 前提与正式执行

- 目标数据库为 Vastbase MySQL 兼容模式：`sql_compatibility = B`。
- 执行账号有创建 schema、表、索引、过程、函数和 Event 的权限。
- 19 个入口脚本和 `000_run_all.sql` 位于同一目录。
- 生产执行前建议先全局关闭 Event 调度，脚本成功后再按变更窗口开启。

```bash
cd <final目录>
vsql -h <host> -p <port> -U <user> -d <database> -W '<password>' \
  -v ON_ERROR_STOP=1 -f 000_run_all.sql
```

脚本中的过程使用 Vastbase 支持的独立 `/` 结束符。若现场命令名是 `psql`，
只有该客户端确实是 Vastbase 兼容客户端并支持独立 `/` 时，才能把上述命令的
`vsql` 替换成 `psql`；标准 PostgreSQL `psql` 不能直接执行这套脚本。

## Dry run

`BEGIN`、入口和 `ROLLBACK` 必须在同一个连接中，不能分别执行三个命令：

```bash
cd <final目录>
printf '%s\n' 'BEGIN;' '\ir 000_run_all.sql' 'ROLLBACK;' | \
  vsql -h <host> -p <port> -U <user> -d <database> -W '<password>' \
  -v ON_ERROR_STOP=1
```

这只验证建表和过程/Event 的解析、依赖及创建，不会提交对象，也不会用生产数据
实际跑 15 条业务任务。上线后仍须按下文逐个手工调用并核查业务结果和日志。

## Event 行为回归（仅隔离测试库）

除生产初始化脚本外，仓库提供两类开发测试工具：

- `../../tests/event_runtime_fixtures.sql`：2026-07 测试快照的最小业务数据，包含
  ODS 消费、国库维度、指标同比、文件路径、企业调查、代理银行和可持续金融数据。
- `../../tools/verify_event_runtime.sh`：每个 Event 前重新加载夹具，逐一手工执行
  15 个 `DO CALL`，检查调用退出码、过程内部错误日志和目标表确定值。

夹具会删除/重建部分测试源表并写入带 `EVT-` 标记的数据，**严禁在生产库执行**。
只允许在从目标环境复制出来的独立验证库运行：

```bash
cd <document/psql目录>
tools/verify_event_runtime.sh \
  <独立验证数据库> \
  tests/event_runtime_fixtures.sql \
  /tmp/gk_event_runtime_verify
```

2026-07-30 在 `cui02-t` / `g100` 的独立数据库
`gk_event_validation_20260730a` 完整执行结果为 15/15 PASS：

| Event | 业务断言 |
| --- | --- |
| `etl_evt_etl_dimnsn_data` | 分表 2 行、维度视图 2 行 |
| `etl_evt_etl_ods_to_dm` | 临时源被消费并删除、月表金额 66.66、stamp 清除、维度补入、无错误日志 |
| `edw_evt_trs_call_edw_budget_data` | 复合预算单位键为 `500001EV01`，无 CHAR 填充空格 |
| `edw_evt_trs_call_edw_cp` | 前一日支付 123.45、退款 23.45 各 1 行 |
| `indicators_lib_p_init_report01` | 535 条公式全部执行，内部错误 0 |
| `indicators_lib_p_init_report02` | 两个维度指标合计 246.90，内部错误 0 |
| `indicators_lib_p_init_report03` | 收入同比均为 0.2、排名均为 1，内部错误 0 |
| `indicators_lib_p_xunhuan_formula` | 命中 batch 数据，日期规范为 `2026-06-01`，535 条公式及报告链完成，内部错误 0 |
| `ods_pt_gy_files_task` | FastDFS 路径转换为 `/usr/data/fdfs/storage/170/data2/data/...` |
| `visual_screen_p_task_vs` | 大屏目标表产生当期数据，内部错误 0 |
| `adm_enterprise_survey_1` | 企业调查临时表和最终表各 1 行、金额 1 |
| `adm_enterprise_survey_2` | 按 22:00/23:00 调度前后依赖串联后最终表保持 1 行 |
| `adm_e_sust_update` | 连续执行两次后仍只有 1 行、贷款余额 321.45 |
| `adm_p_trs_stat_agentbankpay_back_detail` | 前两个月退款 18.88、1 行 |
| `adm_p_trs_stat_agentbankpay_detail` | 前一个月支付 88.88、1 行 |

此次一致性基线是现场提供的 `all_event.sql` 中 MySQL Event/routine 定义，并对
Vastbase 目标结果做了确定值验证。由于没有使用现场 MySQL 运行账号，本次不包含
“同一夹具同时写入 MySQL 和 Vastbase”的在线双库差分；如需在线差分，必须使用
经授权的独立 MySQL 测试库，不能在生产源库造数。

## Event 全局和单独控制

`enable_prevent_job_task_startup` 的含义是“阻止任务启动”，所以值与启停语义相反：

```sql
-- 查看全局状态
SHOW enable_prevent_job_task_startup;

-- 全局关闭所有 Event 调度
ALTER SYSTEM SET enable_prevent_job_task_startup = on;

-- 全局允许所有已 ENABLE 的 Event 调度
ALTER SYSTEM SET enable_prevent_job_task_startup = off;
```

修改后再次 `SHOW` 确认。该参数在验证环境中是 reload 级别；若现场集群没有自动
刷新，由 DBA 按现场集群方式 reload。

单独启停 Event：

```sql
ALTER EVENT adm_enterprise_survey_1 DISABLE;
ALTER EVENT adm_enterprise_survey_1 ENABLE;
SHOW EVENTS;
```

`ENABLE` 只允许按计划触发，不代表立即执行。手工执行某个 Event 时，直接复制
`007_events_init.sql` 或 `016_adm_events_init.sql` 中该 Event 的 `DO CALL` 内容。
例如：

```sql
CALL etl.entrance_merge_dimnsn_data();
CALL etl.entrance_merge_t_jrtj_dim_value_data();
CALL edw.p_trs_budget_new();
CALL edw.proc_trs_guoku_cp();
CALL indicators_lib.init_report01(
  DATE_FORMAT(LAST_DAY(DATE_ADD(CURDATE(), INTERVAL -1 MONTH)), '%Y-%m-%d')
);
CALL ods.p_pt_gy_files_temp();
CALL visual_screen.p_task_vscreen(DATE_FORMAT(CURDATE(), '%Y%m%d'));
CALL adm.p_ana_sust_mth_enterprise_survey_temp(DATE_FORMAT(NOW(), '%Y%m'));
CALL adm.p_ana_sust_mth_enterprise_survey(DATE_FORMAT(NOW(), '%Y%m'), '', '');
CALL adm.p_ana_sust_update(CURDATE());
CALL adm.p_trs_stat_agentbankpay_back_detail();
CALL adm.p_trs_stat_agentbankpay_detail();
```

## 调用日志与上线核查

`SHOW EVENTS` 只能看 Event 定义和启停状态，不能代替业务调用日志。本次恢复了
源库真实日志对象，不再使用编译占位桩：

```sql
-- 有埋点过程的步骤成功记录；同一日期/过程/步骤只保留最新一条
SELECT *
  FROM etl.edw_proc_trace_log
 ORDER BY end_time DESC, proc_name, step_id;

-- 有异常埋点过程的失败记录
SELECT *
  FROM etl.edw_proc_error_log
 ORDER BY end_time DESC, proc_name, step_id;

-- 两个 ETL Event 入口的成功、抢锁失败或异常记录
SELECT *
  FROM etl.t_run_log
 ORDER BY run_time DESC;
```

这些表只覆盖源过程本来带日志调用的链路，以及本次迁移的两个 ETL 入口；不能据此
推断所有过程都已执行。上线验收建议先全局关调度，逐个手工 `CALL`，同时记录开始
时间，检查上述日志、目标表数据日期/行数和数据库服务端错误日志，确认后再开启调度。

## 语义适配说明

以下差异经过人工核对并在审计工具中显式登记，不属于遗漏：

1. 源 `indicators_lib.p_xunhuan_formula` 调用了源文件中不存在的
   `edw.p_trs_budget_income_compare_xin`。目标改为调用源文件中真实存在的
   `indicators_lib.p_trs_budget_income_compare_xin`，否则 Event 必然运行失败。
2. `visual_screen.p_task_vscreen` 的 37 个叶子调用拆到
   `p_task_vscreen_daily` 和 `p_task_vscreen_month_end` 两个适配器，入口 Event 不变，
   两个适配器合并后的业务调用集合与源入口一致。
3. 指标库 `init_report01 -> p_exe_formula -> p_exe_formula_hand` 使用包装过程承接
   循环和日志，最终业务执行及日志仍落在同一调用链。
4. MySQL 的 schema 限定 `TEMPORARY TABLE` 无法直接映射到 Vastbase；
   `etl.entrance_merge_t_jrtj_dim_value_data` 改用 `ods_temp` 下 UNLOGGED 工作表，
   并用 advisory lock 保证单实例执行，保留原数据变换、清理和运行日志语义。
5. 源 `edw.p_trs_budget_income_compare` 异常分支引用了未声明的 `TABLE_NAME`；
   目标记录实际过程名，避免错误处理器自身再次报错。
6. MySQL 的 `EVERY 1 WEEK` 在 Vastbase 不被接受，等价改写为 `EVERY 7 DAY`。
7. 指标公式目录中 MySQL 的数字字符串参数、`@变量 :=` 排名、错拼占位符和两处
   源公式字段/`WHERE` 缺失已在公式执行器中做定向转换；排名改为等价的
   `ROW_NUMBER()`。执行器逐条调用并记录总数，防止批量动态 SQL 中途结束却无法发现。
8. 公式依赖但旧脚本遗漏的 `lib_indicators_000527` 已补建；全量 535 条公式执行时
   `etl.edw_proc_error_log` 为 0。
9. ADM 企业调查动态 SQL 的双引号已改为字符串单引号；7 张 DATE 类型目标表的月度
   删除条件改为 `YYYY-MM-01`，避免删除失效后每次 Event 重复累加。
10. EDW 预算单位复合键拼接前对 CHAR 字段执行 `RTRIM`，与 MySQL 默认读取 CHAR
    时去掉右侧填充的表现一致。
11. `003_report_init.sql` 为季度快报过程补齐 `DROP PROCEDURE`，生产入口可重复执行。

## 可重复闭包审计

开发侧可用同一份 `all_event.sql` 重新执行静态审计：

```bash
python3 ../../tools/audit_event_closure.py \
  /path/to/all_event.sql . --json /tmp/all_event_closure.json
```

命令返回 0 的条件包括：Event/routine 无缺失、无未登记的额外对象、Event 的调用、
周期、起始时间、保留策略和启停状态一致、参数模式一致，且调用边和 DML 目标不存在
未解释差异。ucloud/upm 的 7 个应用直调过程已作为生产扩展显式登记，不计为未登记
对象。`filter_event_closure_bundle.py` 是按闭包重新裁剪脚本的机械工具，不是生产执行入口。

## 已完成验证

- 静态基线：15 Event、361 源 routine、76 个 Event 可达 routine；缺失 0，
  未解释差异 0，审计退出码 0。
- 环境：`cui02-t` 的 `g100` Vastbase 容器；源业务库未改动，行为测试使用独立克隆
  `gk_event_validation_20260730a`。
- 2026-07-30 生产入口 dry-run：同一连接执行
  `BEGIN -> \ir 000_run_all.sql -> ROLLBACK`，`ON_ERROR_STOP=1`；当时的 18/18
  Event 闭包脚本完成，`ERROR/FATAL/PANIC` 为 0，最终正常 `ROLLBACK`。
- 行为回归：15/15 Event PASS，断言失败 0；详细日志位于测试容器
  `/tmp/gk_event_validation_20260730a.verify_final/`。
- 静态闭包审计再次退出 0：Event 缺失 0、routine 缺失 0、未解释调用边/DML 差异 0。
- 测试期间全局调度被暂停；完成后已恢复测试前配置
  `enable_prevent_job_task_startup=off` 并由新连接确认。
