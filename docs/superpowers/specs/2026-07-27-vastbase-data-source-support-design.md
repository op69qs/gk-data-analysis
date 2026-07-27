# Vastbase 数据源支持设计

日期：2026-07-27

## 目标

在不改变 Mysql、ClickHouse、DB2 现有行为的前提下，将 Vastbase 作为综合查询模块的一等数据源类型。Vastbase 沿用现有 Mysql 的业务流程：配置数据源、测试连接、维护数据表元数据、读取字段、生成查询条件并执行综合查询。

现场 Vastbase 采用“一个物理数据库、多个 Schema”的组织方式，驱动使用 PostgreSQL 兼容方案：

- Driver：`org.postgresql.Driver`
- URL：`jdbc:postgresql://<ip>:<port>/<database>?currentSchema=<schema>`

## 范围

本次包含：

1. 数据源维护增加 Vastbase 类型。
2. Vastbase 数据库名与 Schema 的配置、回显、编辑、重复校验和连接测试。
3. Vastbase Schema 下的数据表、字段和注释读取。
4. 数据表维护树挂载 Vastbase Schema 和数据表。
5. 综合查询对 Vastbase 的字段选择、条件过滤、聚合、分页和 SQL 执行。
6. 动态数据源初始化和健康检查兼容 Vastbase。
7. 数据库迁移脚本、自动化测试和运行态接口验证。

本次不改变：

- Mysql、ClickHouse、DB2 的配置字段、URL 和查询行为。
- 维度页面现有主库与 ClickHouse 同步逻辑。
- `edw.fm_trs_guoku_base_table` 的结构及其 `DATABASE_ID` 关联方式。
- 已保存综合查询方案的数据结构。

## 数据模型

在 `seo.seo_datasource_database` 增加一个可空字段：

```sql
ALTER TABLE seo.seo_datasource_database
ADD COLUMN schema_name varchar(100);
```

字段语义：

| 类型 | `DBNAME` | `SCHEMA_NAME` |
|---|---|---|
| Vastbase | 物理数据库名，如 `gk_data_analysis` | Schema，如 `edw` |
| Mysql | 原数据库名 | `NULL` |
| ClickHouse | 原数据库名 | `NULL` |
| DB2 | 原数据库名 | `NULL` |

每个 Vastbase Schema 对应一条 `seo_datasource_database` 记录，并保留独立 `ID`。数据表元数据继续通过 `edw.fm_trs_guoku_base_table.DATABASE_ID` 关联该记录。

同一个 Vastbase 数据源中，配置唯一性使用：

```text
SOURCE_ID + DBNAME + SCHEMA_NAME
```

非 Vastbase 类型继续使用原有 `SOURCE_ID + DBNAME` 规则。

`seo.seo_datasource_enum` 增加 Vastbase 枚举记录：

```text
DATASOURCE  = Vastbase
DRIVERCLASS = org.postgresql.Driver
URL         = jdbc:postgresql://ip:port/DBNAME?currentSchema=SCHEMA_NAME
```

迁移脚本必须幂等：字段或枚举已经存在时不重复创建；不更新已有 Mysql、ClickHouse、DB2 记录。

## 页面设计

数据源维护页面保持现有操作流程。

选择 Vastbase 时：

- 显示“数据库”输入框，写入 `DBNAME`。
- 显示“Schema”输入框，写入 `SCHEMA_NAME`，必填。
- 测试连接同时提交数据库名和 Schema。
- 编辑列表显示数据库和 Schema。
- 重复校验比较数据库名与 Schema 的组合。

选择其他数据库类型时：

- 不显示 Schema 输入框。
- `SCHEMA_NAME` 不提交或保存为 `NULL`。
- 原有表单行为保持不变。

数据表维护树中，Vastbase 第二级节点显示 `SCHEMA_NAME`；其他数据库类型仍显示 `DBNAME`。

## 后端连接与动态数据源

新增或编辑 Vastbase 配置时，生成：

```text
jdbc:postgresql://IP:PORT/DBNAME?currentSchema=SCHEMA_NAME
```

连接测试使用 PostgreSQL JDBC 驱动，并执行 `SELECT 1` 验证连接。动态数据源初始化根据驱动类型选择健康检查 SQL：

- DB2：`SELECT 1 FROM SYSIBM.SYSDUMMY1`
- Mysql、ClickHouse、Vastbase/PostgreSQL：`SELECT 1`
- 其他旧类型保留现有兼容行为

连接测试失败时返回明确错误，至少区分：数据库不可达、认证失败、数据库不存在、Schema 不存在。保存配置前必须验证 `DBNAME` 和 `SCHEMA_NAME` 非空，但不强制用户先成功测试连接，以保持现有页面使用方式。

## 元数据读取

Vastbase 数据表列表从 `information_schema.tables` 读取：

```sql
SELECT table_name
FROM information_schema.tables
WHERE table_schema = :schemaName
  AND table_type IN ('BASE TABLE', 'VIEW');
```

字段、表注释使用 `pg_namespace`、`pg_class`、`pg_attribute`、`obj_description` 和 `col_description`，过滤条件使用 `SCHEMA_NAME`，不再把 `DBNAME` 当作 Schema。

表名在元数据中继续保存为 `TABLE_SIGN`。实际查询 SQL 对 Vastbase 使用 Schema 限定名：

```text
SCHEMA_NAME.TABLE_SIGN
```

Mysql、ClickHouse、DB2 仍使用当前表名生成规则。

## 综合查询

Vastbase 复用 Mysql 的业务分支，包括字段类型、普通条件、数值条件、日期条件、核算主体、聚合和排序。仅在 SQL 方言有差异的位置使用 Vastbase/PostgreSQL 分支：

- 分页：`LIMIT ... OFFSET ...`
- 字符串和日期转换：使用 Vastbase/PostgreSQL 可执行语法
- 标识符：必要时使用 Schema 限定，不将用户输入直接拼接为任意 Schema
- 参数值：继续通过现有参数机制传递，避免扩大 SQL 注入面

附件查询沿用动态数据源执行路径，不增加独立 Vastbase 页面逻辑。

## 兼容与迁移

新增字段允许为空，因此已有数据无需回填。所有读取逻辑采用：

```text
Vastbase -> SCHEMA_NAME
其他类型 -> DBNAME
```

旧 Vastbase 试验配置如果没有 `SCHEMA_NAME`，页面显示“Schema 未配置”，连接测试和表读取返回可理解的校验错误，不静默把 `DBNAME` 同时当数据库和 Schema。

发布顺序：

1. 备份 `seo.seo_datasource_database` 和 `seo.seo_datasource_enum`。
2. 执行幂等迁移脚本。
3. 发布 SEO 后端。
4. 发布前端静态资源。
5. 新建 Vastbase 配置并完成端到端验证。

回滚代码时可保留 `SCHEMA_NAME` 字段和 Vastbase 枚举；旧版本会忽略新增字段。必要时只禁用或删除 Vastbase 配置，不删除字段，避免破坏已录入信息。

## 测试与验收

自动化测试至少覆盖：

1. Vastbase 枚举能从接口返回。
2. URL 使用物理数据库名和 Schema 正确生成。
3. Mysql、ClickHouse、DB2 URL 保持原结果。
4. Vastbase 重复校验使用 `DBNAME + SCHEMA_NAME`。
5. 动态数据源 Vastbase 健康检查为 `SELECT 1`。
6. Vastbase 表列表、字段名、表注释和字段注释可读取。
7. 数据表树显示 Schema，并能挂载表节点。
8. 综合查询能够执行选择、过滤、聚合、排序和分页。
9. 维度页面行为未因本次改动发生变化。

运行态验收使用本地 Vastbase 测试库，至少创建或使用两个 Schema，验证：

- 同一数据库下两个 Schema 可分别配置。
- 同名表在不同 Schema 下不会冲突。
- 数据表维护能分别展示两个 Schema 的表。
- 综合查询生成 Schema 限定 SQL 并返回正确数据。
- 浏览器控制台无新增异常，网络请求无 500。
