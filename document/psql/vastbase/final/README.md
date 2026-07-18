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
13. `013_adm_tables_init.sql`

也可以直接执行：

- `000_run_all.sql`

## 说明

- `000_run_all.sql` 只纳入已完成目标库事务编译验证的脚本；同目录的后续编号脚本在验证完成前不纳入总入口。
- 所有脚本均面向 Vastbase `sql_compatibility = B` 环境。
- 过程脚本使用 `CREATE PROCEDURE ... BEGIN ... END;` 加独立 `/` 结束符，适配现场 `vsql -f` 执行；不要删除过程后的 `/`。
- `007_events_init.sql` 默认使用 `DISABLE` 创建 Event，待业务联调完成后再启用更稳妥。
- `001_etl_init.sql` 中的 `etl` 日志过程当前为迁移期编译解锁桩实现，用于承接大量过程中的日志调用依赖。
- `008` 到 `012` 根据新增的 `ucloud_结构.sql`、`upm_结构.sql` 转换，补齐 `ucloud` / `upm` schema、源表结构和被 `dwbi-system-docking` 调用的过程。为保证整包事务干跑可控，这批脚本保留主键，未落 MySQL 导出中的大量二级索引。
- `013` 根据生产 MySQL `adm.sql` 转换，包含 330 张表和 2 张视图；普通标识符统一使用 Vastbase 默认小写，只有保留字或非常规源字段名使用小写双引号保真。
- `014` 包含源库 90 个普通二级索引；当前已完成生成器回归验证，待目标库事务复验后再加入总入口。
- `015` 包含 117 个过程和 2 个函数，`016` 包含 5 个 Event；尚在目标库逐项编译验证，验证完成前不加入总入口，Event 默认以 `DISABLE` 状态交付。

## 验证结果

- 已使用以下顺序对本目录全部脚本做联合事务验证：
  - `BEGIN;`
  - 依次执行 `001 -> 012`
  - `ROLLBACK;`
- 验证结果：原 `001 -> 012` 整包可联合编译和创建，无残留半成品。
- `013_adm_tables_init.sql` 已在现有 `001 -> 012` 环境中单独执行完整事务创建并回滚，随后正式导入；实库核验结果为 330 张表、2 张视图。
- 重新生成后拆出的 `014_adm_indexes_init.sql`、`015_adm_routines_init.sql`、`016_adm_events_init.sql` 尚未导入目标库。
- `017_dynamic_refresh_run_log_init.sql` 是动态刷数运行记录的幂等增量脚本。现有数据库只执行 `017`，不得为增加该表而重跑会删除并重建 ADM 表的 `013_adm_tables_init.sql`。

## 建议执行方式

- 正式执行：
  - `vsql -h <host> -p <port> -U <user> -d <database> -W '<password>' -f 000_run_all.sql`
- 联合验证：
  - `vsql -h <host> -p <port> -U <user> -d <database> -W '<password>' -c 'BEGIN;' -f 000_run_all.sql -c 'ROLLBACK;'`
