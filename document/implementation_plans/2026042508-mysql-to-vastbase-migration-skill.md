# MySQL 迁移 Vastbase 注意事项 skill 整理

## 本次目标

- 基于前序 MySQL -> Vastbase 改造记录，整理一份可复用的 workspace skill。
- 将之前分散在多份实施记录中的经验，抽象成后续可重复执行的迁移注意事项，而不是继续依赖零散对话上下文。

## 归纳来源

本次 skill 主要归纳自以下既有记录：

- `document/implementation_plans/2026041901-mysql-to-vastbase-baseline.md`
- `document/implementation_plans/2026041903-continue-vastbase-compatibility.md`
- `document/implementation_plans/2026041904-remove-fixedreport-format-initializer.md`
- `document/implementation_plans/2026041906-fix-statistical-analysis-source-datasource-drift.md`
- `document/implementation_plans/2026041907-fix-org-tribe-system-quartz-schema.md`
- `/memories/repo/fixedreport-vastbase-format-compat.md`

## 本次产物

- 新增 workspace skill：`.github/skills/mysql-to-vastbase-migration/SKILL.md`

## skill 内容结构

- 适用场景
- 基线认知与迁移顺序
- 配置层注意事项
- SQL 改造优先规则
- DDL 与元数据查询注意事项
- 明确不要做的事
- 高风险区处理原则
- 建议验证方式
- 本仓库复用方式与快速检查清单

## 归纳时刻意强调的点

- Vastbase 在本项目里按 PostgreSQL 兼容思路处理，不按 MySQL 思路继续兜底。
- 配置漂移是实际发生过的问题，不能只改运行产物，不改源码配置。
- `public.format(...)` 这种数据库侧兼容函数只能作为短期兜底，不应作为最终迁移完成态。
- ClickHouse 专用 mapper、外部 MySQL 数据源接入能力，不应误算为 Vastbase 主库迁移尾项。
- 存储过程、动态 SQL 生成器、报表展示 SQL 属于高风险区，必须结合真实对象和结果验证。

## 目的

- 让后续再做 MySQL -> Vastbase 收口时，先按统一规则分辨“可以规则化处理的内容”和“必须联调验证的内容”。
- 降低再次出现配置回漂、schema 定位错误、宽松 `GROUP BY` 误用、机械替换误伤专用方言的概率。