# Vastbase 最终初始化脚本

## 执行顺序

1. `001_etl_init.sql`
2. `002_ods_init.sql`
3. `003_report_init.sql`
4. `004_edw_init.sql`
5. `005_visual_screen_init.sql`
6. `006_indicators_lib_init.sql`
7. `007_events_init.sql`

也可以直接执行：

- `000_run_all.sql`

## 说明

- 本目录只保留已完成事务编译验证的最终交付脚本。
- 所有脚本均面向 Vastbase `sql_compatibility = B` 环境。
- `007_events_init.sql` 默认使用 `DISABLE` 创建 Event，待业务联调完成后再启用更稳妥。
- `001_etl_init.sql` 中的 `etl` 日志过程当前为迁移期编译解锁桩实现，用于承接大量过程中的日志调用依赖。

## 验证结果

- 已使用以下顺序对本目录全部脚本做联合事务验证：
  - `BEGIN;`
  - 依次执行 `001 -> 007`
  - `ROLLBACK;`
- 验证结果：整包可联合编译和创建，无残留半成品。

## 建议执行方式

- 正式执行：
  - `psql -v ON_ERROR_STOP=1 'postgresql://vastbase_test:***@cui02:25432/gk_data_analysis' -f 000_run_all.sql`
- 联合验证：
  - `psql -v ON_ERROR_STOP=1 'postgresql://vastbase_test:***@cui02:25432/gk_data_analysis' -c 'BEGIN;' -f 000_run_all.sql -c 'ROLLBACK;'`
