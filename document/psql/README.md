# gk-data-analysis MySQL -> Vastbase SQL 说明

生产交付脚本位于 `vastbase/final/`，现场只需要把该目录中的文件放到同一个目录，
执行 `000_run_all.sql`。完整的基线、筛选方法、脚本清单、Event 启停、手工调用、
日志查询、语义差异和 dry-run 结果见 `vastbase/final/README.md`。

本轮以 2026-07-29 提供的 `all_event.sql` 为基线：15 个 MySQL Event、361 个
routine；从 Event 入口递归得到 76 个可达 routine。最终 Event 缺失 0、闭包
routine 缺失 0；ADM 已从原来的全量搬运收敛为 5 个 Event 实际需要的 13 个
ADM routine 和 5 个 EDW 依赖过程。

`mysql/` 和 `vastbase/` 下除 `final/` 外的文件是历史导出、中间迁移批次和排查资料，
不属于生产执行入口。不要把旧的 `030_events.sql`、`100_etl_logging_stubs.sql` 或
各批次 executable SQL 与 `final/000_run_all.sql` 混用。

开发侧闭包审计工具：

- `tools/audit_event_closure.py`：解析 MySQL Event/routine，递归计算调用闭包并与
  `final/` 比较签名、调用边和 DML 目标。
- `tools/filter_event_closure_bundle.py`：按审计闭包机械裁剪迁移脚本，仅供重新生成
  候选包，不用于现场执行。
- `tools/run_event_runtime_smoke.sh`：逐个调用 15 个 Event 入口，只检查调用层错误。
- `tools/verify_event_runtime.sh`：在独立测试库加载 `tests/event_runtime_fixtures.sql`，
  同时检查内部错误日志和目标表确定值；夹具会造数和重建测试源表，严禁用于生产库。

2026-07-30 的隔离 Vastbase 行为回归为 15/15 PASS，最新 18 脚本事务 dry-run
也已通过。逐 Event 的断言和本轮发现的语义修复见 `vastbase/final/README.md`。
