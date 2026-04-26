# fixedReport 移除 FORMAT 兼容初始化器

## 背景

- `fixedReport` 之前新增了 `VastbaseCompatibilityInitializer`，在启动时通过 `CREATE OR REPLACE FUNCTION public.format(...)` 为 Vastbase/PostgreSQL 补一个 MySQL 风格的 `FORMAT(value, digits)` 兼容函数。
- 这类做法虽然能短期兜底，但会把业务 SQL 迁移问题继续留在库里，并且会向 `public` schema 写入持久函数对象，不符合本轮“按 Vastbase 原生写法收口”的要求。

## 本次修改

### 1. 删除运行时兼容初始化器

- 删除 `fixedReport/src/main/java/org/fixedReport/config/VastbaseCompatibilityInitializer.java`。
- `fixedReport` 启动后不再自动创建或覆盖 `public.format(...)` 系列函数。

### 2. 将 fixedReport 中的 MySQL `FORMAT(...)` 全量改为 Vastbase 原生格式化

已处理文件：

- `fixedReport/src/main/resources/mybatis/fixedReport/KydReportMapper.xml`
- `fixedReport/src/main/resources/mybatis/fixedReport/ReportMapper.xml`
- `fixedReport/src/main/resources/mybatis/fixedReport/QuarterReportMapper.xml`
- `fixedReport/src/main/resources/mybatis/fixedReport/NewsFlashMapper.xml`
- `fixedReport/src/main/resources/mybatis/fixedReport/NewsFlashQuarterMapper.xml`

统一替换策略：

- `FORMAT(expr, 2)`
  - 改为 `TO_CHAR(ROUND(CAST(NULLIF(((expr))::text, '') AS NUMERIC), 2), 'FM999,999,999,999,999,999,999,999,990.00')`
- `FORMAT(expr, 0)`
  - 改为 `TO_CHAR(ROUND(CAST(NULLIF(((expr))::text, '') AS NUMERIC), 0), 'FM999,999,999,999,999,999,999,999,990')`

说明：

- 继续保留原 SQL 中已有的 `REPLACE(..., ',', '|')`、`COALESCE(...)`、`CONCAT(...)` 等报表展示逻辑。
- 对原先 `FORMAT((SELECT ...), n)`、`FORMAT(expr * 100, n)`、`FORMAT(expr / ${params.AMT_UNIT}, n)` 等形式一并做了原生转换。
- 额外使用 `NULLIF(((expr))::text, '')`，用于兼容原 SQL 中少量“子查询返回空串再参与格式化”的写法，避免直接 `CAST('' AS NUMERIC)` 失败。

## 目的

- 让 `fixedReport` 的报表 SQL 直接使用 Vastbase/PostgreSQL 原生能力，而不是依赖启动时补函数。
- 避免向 `public` schema 写入兼容性对象，减少对数据库环境的侵入。
- 让后续巡检结果能真实反映 fixedReport 的迁移完成度，而不是被运行时兜底掩盖。

## 静态结果

- 对 `fixedReport/src/main/resources/mybatis/fixedReport/*.xml` 复扫后，`FORMAT(` 已清空。
- `VastbaseCompatibilityInitializer` 代码文件已删除，fixedReport 代码侧不再引用该兼容方案。

## 后续验证

- 执行 `mvn -pl fixedReport -am -DskipTests compile`。
- 如接真实 Vastbase 数据源，重点回归：
  - 生成报告相关接口
  - 快报 / 季报 / 分行业报表
  - 依赖 `REPLACE(..., ',', '|')` 的文本拼接展示结果