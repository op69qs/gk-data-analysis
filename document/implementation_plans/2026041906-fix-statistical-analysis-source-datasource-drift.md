# dwbi-statistical-analysis 源配置回写 Vastbase

## 问题现象

- 启动阶段出现 Druid 数据源初始化失败：`java.sql.SQLException: com.mysql.jdbc.Driver`。
- 报错位置在 MyBatis Plus 创建 `SqlSessionFactory` 时，说明 Spring 正在按当前生效配置初始化主数据源。

## 根因分析

- `dwbi-statistical-analysis/target/application.yml` 与 `deploy-package/config/dwbi-statistical-analysis/application.yml` 已切到 Vastbase / PostgreSQL。
- 但源码文件 `dwbi-statistical-analysis/src/main/resources/application.yml` 仍保留旧 MySQL 配置：
  - `jdbc:mysql://cui02:3308/dps...`
  - `com.mysql.jdbc.Driver`
  - `druid.stat.mergeSql=true`
- 该模块一旦从源码启动，或重新打包后由源码资源覆盖 `target`，就会重新按 MySQL 驱动初始化。
- 由于模块依赖已切到 PostgreSQL，类路径中没有 MySQL 驱动，于是直接抛出 `ClassNotFoundException: com.mysql.jdbc.Driver`。

## 本次修改

- 将 `dwbi-statistical-analysis/src/main/resources/application.yml` 的主数据源从 MySQL 改回 Vastbase：
  - URL 改为 `jdbc:postgresql://100.71.11.54:25432/gk_data_analysis?currentSchema=dps,public`
  - 用户改为 `vastbase_test`
  - 驱动改为 `org.postgresql.Driver`
- 同步移除 `druid.stat.mergeSql=true`，与仓库当前 Vastbase 基线保持一致，仅保留 `druid.stat.slowSqlMillis=5000`。

## 结论

- 这次报错不是“明明都改成 Vastbase 了还在报 MySQL”，而是 `dwbi-statistical-analysis` 的源码配置发生了回漂。
- 运行产物已经是 Vastbase，源码却还是 MySQL，所以一旦重新走源码资源链路，就会再次触发 `com.mysql.jdbc.Driver` 找不到。