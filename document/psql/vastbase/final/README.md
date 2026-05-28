# Vastbase 最终初始化脚本

## 执行顺序

1. `001_etl_init.sql`
2. `002_ods_init.sql`
3. `003_report_init.sql`
4. `004_edw_init.sql`
5. `005_visual_screen_init.sql`
6. `006_indicators_lib_init.sql`
7. `007_events_init.sql`
8. `008_ucloud_tables_init.sql`
9. `009_upm_alarmlog_tables_init.sql`
10. `010_upm_system_data_tables_init.sql`
11. `011_upm_netperformance_tables_init.sql`
12. `012_ucloud_upm_procedures_init.sql`

也可以直接执行：

- `000_run_all.sql`

## 说明

- 本目录只保留已完成事务编译验证的最终交付脚本。
- 所有脚本均面向 Vastbase `sql_compatibility = B` 环境。
- 过程脚本使用 `CREATE PROCEDURE ... BEGIN ... END;` 加独立 `/` 结束符，适配现场 `vsql -f` 执行；不要删除过程后的 `/`。
- `007_events_init.sql` 默认使用 `DISABLE` 创建 Event，待业务联调完成后再启用更稳妥。
- `001_etl_init.sql` 中的 `etl` 日志过程当前为迁移期编译解锁桩实现，用于承接大量过程中的日志调用依赖。
- `008` 到 `012` 根据新增的 `ucloud_结构.sql`、`upm_结构.sql` 转换，补齐 `ucloud` / `upm` schema、源表结构和被 `dwbi-system-docking` 调用的过程。为保证整包事务干跑可控，这批脚本保留主键，未落 MySQL 导出中的大量二级索引。

## 验证结果

- 已使用以下顺序对本目录全部脚本做联合事务验证：
  - `BEGIN;`
  - 依次执行 `001 -> 012`
  - `ROLLBACK;`
- 验证结果：整包可联合编译和创建，无残留半成品。

## 建议执行方式

- 正式执行：
  - `vsql -h <host> -p <port> -U <user> -d <database> -W '<password>' -f 000_run_all.sql`
- 联合验证：
  - `vsql -h <host> -p <port> -U <user> -d <database> -W '<password>' -c 'BEGIN;' -f 000_run_all.sql -c 'ROLLBACK;'`
