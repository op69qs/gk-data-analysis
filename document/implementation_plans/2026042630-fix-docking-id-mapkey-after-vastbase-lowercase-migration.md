# 2026042630 修复 dwbi-system-docking 在 Vastbase 字段小写化后的 ID 映射空指针

## 背景

`dwbi-system-docking` 启动时出现：

- `NullPointerException` at `SystemInformationUtil.saveProcessConfigToRedis` line 85。
- 现场说明 Vastbase 侧字段名已统一为小写 `id`。

前置异常已从 `column "id" does not exist` 变为 NPE，说明 SQL 可执行但 Java 取值键名不匹配。

## 根因分析

`SystemInformationUtil`、`ProcessConfig` 等逻辑长期按 `Map` 键 `ID` 读取主键值，例如：

- `processConfig.get("ID")`
- `systemConfig.get("ID")`

当库字段改为小写 `id` 后，MyBatis 对 `resultType=hashmap` 返回的键名变为 `id`，导致：

- `processConfig.get("ID") == null`
- 在 `.toString()` 或拼接 Redis key 时触发空指针。

## 变更策略

采用最小改动策略：在 MyBatis 查询中把主键列统一别名为 `"ID"`，保持 Java 既有读取逻辑不变，避免扩散修改。

## 修改文件

1. `dwbi-system-docking/src/main/resources/mybatis/mysql/ConfigMapper.xml`

- `getProcessConfig`: `id AS "ID"`
- `getProcessThreadConfig`: `id AS "ID"`
- `getSystemConfig`: `f.id AS "ID"`
- `getPlatformConfig`: `id AS "ID"`
- `getTargetConfig`: `id AS "ID"`
- `getResourcesConfig`: `id AS "ID"`
- `getResourcesIPConfig`: `id AS "ID"`

2. `dwbi-system-docking/src/main/resources/mybatis/mysql/MysqlMapper.xml`

- `findAll`: `id AS "ID"`

## 影响评估

- 仅影响返回 `Map` 的列标签，不改变 where/join 条件和业务语义。
- 对已使用 `ID` 键的 Java 逻辑保持兼容。
- 对下游 Redis key 命名保持不变（`PROCESS_...`、`THREAD_...`、`CRON_...`）。

## 后续建议

- 若计划彻底统一小写风格，可后续新增一轮重构：Java 侧统一改为读取 `id` 并做兼容降级；本次先保障启动恢复。
