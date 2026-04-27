# 2026042625 fix seo database alias casing

## 背景

`/seo/dataSourceController/getDataBase` 在 PostgreSQL/Vastbase 下通过 MyBatis `resultType="java.util.Map"` 返回结果时，`SELECT *` 生成的键名会被折叠为小写，导致前端弹窗子表读取不到 `DBNAME`、`USERNAME`、`PASSWORD`、`STATE` 等字段。

## 本次修改

- 文件：`seo/src/main/resources/mybatis/seo/DataSourceMapper.xml`
- 范围：仅修改 `getDataBase`
- 变更：将 `SELECT *` 改为显式字段选择，并统一使用大写双引号别名：
  - `ID`
  - `SOURCE_ID`
  - `DBNAME`
  - `USERNAME`
  - `PASSWORD`
  - `STATE`
  - `DRIVERCLASS_NAME`
  - `DATASOURCE_URL`
  - `CREATE_TIME`
  - `CREATE_USER`

## 原因

前端 `DataSourceModal.vue` 的数据库列表和重复校验逻辑都直接依赖大写字段名。后端不显式控制别名时，PG/Vastbase 返回给 `Map` 的键名与页面契约不一致。

## 验证

- `DataSourceMapper.xml` 语法检查：无错误
- 模块编译：在 `seo` 目录执行 `mvn -DskipTests compile`
- 结果：`BUILD SUCCESS`

## 备注

如果本地 `9090` 上仍看到旧返回，需要重启或重新部署当前运行中的 `seo` 服务，使更新后的 mapper 生效。
