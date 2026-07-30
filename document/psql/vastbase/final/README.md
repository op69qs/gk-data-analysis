# Vastbase 生产初始化脚本

## 生产执行范围

生产只执行 `000_run_all.sql`。入口使用相对 `\ir`，因此现场只需保证下列文件位于同一目录，不依赖开发机绝对路径。

入口按顺序加载 18 个脚本：

1. `001_etl_init.sql`
2. `002_ods_init.sql`
3. `003_report_init.sql`
4. `004_edw_init.sql`
5. `005_visual_screen_init.sql`
6. `006_indicators_lib_init.sql`
7. `006a_event_dependency_routines_init.sql`
8. `007_events_init.sql`
9. `008_ucloud_tables_init.sql`
10. `009_upm_alarmlog_tables_init.sql`
11. `010_upm_system_data_tables_init.sql`
12. `011_upm_netperformance_tables_init.sql`
13. `012_ucloud_upm_procedures_init.sql`
14. `013_adm_tables_init.sql`
15. `014_adm_indexes_init.sql`
16. `014a_adm_dependency_routines_init.sql`
17. `015_adm_routines_init.sql`
18. `016_adm_events_init.sql`

`017_dynamic_refresh_run_log_init.sql` 是已有数据库的增量补丁，不在新库总入口重复执行；它的表和两个索引已经合并在 `013_adm_tables_init.sql` 中。

ADM 覆盖内容：

- `013`：331 张 ADM 表，包含 `dynamic_refresh_run_log`。
- `014`：90 个 ADM 索引。
- `014a`：ADM 过程调用所需的 9 个 EDW、Report、Indicators 依赖过程。
- `015`：ADM 的 117 个存储过程和 2 个函数。
- `016`：ADM 的 5 个 Event。

整包合计创建或重建 203 个存储过程、2 个函数和 15 个 Event。

## 前提

- 目标数据库必须是 Vastbase MySQL 兼容模式：`sql_compatibility = B`。
- 所有 SQL 文件必须放在同一个目录内。
- 过程使用 Vastbase 支持的 `CREATE PROCEDURE ... AS ... BEGIN ... END; /` 格式，独立 `/` 不能删除。
- `007` 和 `016` 中的 15 个 Event 均为 `ENABLE`，与 MySQL 源对象状态一致；是否允许调度由实例参数 `enable_prevent_job_task_startup` 统一控制。

## 正式执行

先进入 SQL 所在目录，再执行入口：

```bash
cd <final目录>
vsql -h <host> -p <port> -U <user> -d <database> -W '<password>' \
  -v ON_ERROR_STOP=1 -f 000_run_all.sql
```

脚本依赖 `vsql` 对独立 `/` 终止符的处理，生产整包不要改用标准 PostgreSQL `psql`。

## Dry run

`BEGIN`、入口和 `ROLLBACK` 必须位于同一个连接：

```bash
cd <final目录>
printf '%s\n' 'BEGIN;' '\ir 000_run_all.sql' 'ROLLBACK;' | \
  vsql -h <host> -p <port> -U <user> -d <database> -W '<password>' \
  -v ON_ERROR_STOP=1
```

## Event 控制

参数含义是“阻止任务启动”，因此值与开关语义相反。以下命令由数据库管理员执行：

```sql
-- 查看全局状态
SHOW enable_prevent_job_task_startup;

-- 全局关闭所有 Event 调度
ALTER SYSTEM SET enable_prevent_job_task_startup = on;

-- 全局允许 Event 调度
ALTER SYSTEM SET enable_prevent_job_task_startup = off;
```

该参数在当前 Vastbase 环境中为 `sighup` 级别。修改后重新执行 `SHOW` 确认；若现场集群未自动刷新，由 DBA 按集群运维方式 reload 配置。

单独启停某个 Event：

```sql
ALTER EVENT adm_enterprise_survey_1 DISABLE;
ALTER EVENT adm_enterprise_survey_1 ENABLE;
SHOW EVENTS;
```

`ENABLE` 只是允许它按计划调度，不会立即执行。需要立刻手工跑某个 Event 时，直接执行它的 `DO CALL` 对应过程。例如：

```sql
CALL adm.p_ana_sust_mth_enterprise_survey_temp(DATE_FORMAT(NOW(), '%Y%m'));
CALL adm.p_ana_sust_mth_enterprise_survey(DATE_FORMAT(NOW(), '%Y%m'), '', '');
CALL adm.p_ana_sust_update(CURDATE());
CALL adm.p_trs_stat_agentbankpay_back_detail();
CALL adm.p_trs_stat_agentbankpay_detail();
```

## 验证结果

- 环境：`cui02-t` 的 `g100` Vastbase 容器，数据库 `gk_data_analysis`。
- 方式：同一连接执行 `BEGIN -> \ir 000_run_all.sql -> ROLLBACK`，启用 `ON_ERROR_STOP=1`，不使用任何依赖占位。
- 结果：18/18 脚本完成，输出无 `ERROR`，最终 `ROLLBACK`，总耗时 24622 ms。
- 测试事务已回滚，测试库未保留本次 dry-run 对象变更。
