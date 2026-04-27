# 2026042626 修复 seo 维度主表新增列数不匹配

## 背景

维度主表新增接口 `/seo/dim/addMain` 在 PostgreSQL/Vastbase 下报错：

- `ERROR: INSERT has more target columns than expressions`

日志显示 `DimensionMapper.addMain` 执行的是：

```sql
INSERT INTO ods.seo_dimension_main VALUES (?, ?)
```

而目标表 `ods.seo_dimension_main` 实际有 3 列：

- `id`
- `name`
- `add_time`

因此 PostgreSQL/Vastbase 会按整表列数校验，导致 2 个值无法直接插入 3 列表。

## 修改

文件：`seo/src/main/resources/mybatis/seo/DimensionMapper.xml`

将 `addMain` 从无列名插入改为显式列名插入：

```sql
INSERT INTO ods.seo_dimension_main (id, name) VALUES (?, ?)
```

这样 `add_time` 由库表默认行为或空值处理，不再要求 mapper 额外传参。

## 验证

1. 查询 `information_schema.columns`，确认 `ods.seo_dimension_main` 存在额外列 `add_time`
2. `DimensionMapper.xml` 无语法错误
3. 在 `seo` 模块执行：

```powershell
mvn -DskipTests compile
```

结果：`BUILD SUCCESS`

## 影响范围

- 仅修复维度主表新增逻辑
- `seo_dimension_sub` 当前表结构与 `addSub` 传参数量一致，本次未改
