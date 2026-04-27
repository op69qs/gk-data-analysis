# indicatorsLib 指标方案列表缺失函数修复说明

本次修复针对 `indicatorsLibv-1.0` 模块中指标方案列表查询在 Vastbase 下报错：

- `ERROR: function indicators_lib.f_get_indexname(text) does not exist`

## 根因

- `indicators_lib.lib_index_scheme.scheme_colums` 在目标库中的真实类型为 `text`。
- 目标库 `indicators_lib` schema 下不存在 `f_get_indexname(...)` 函数定义。
- 仓库内也没有该函数的建库脚本，说明当前链路依赖了历史库对象，迁移后未被补齐。

## 本次改动

- 修改 `indicatorsLibv-1.0/src/main/resources/mybatis/indicatorsLib/IndexSchemeMapper.xml`
- 将两处 `indicators_lib.f_get_IndexName(s.SCHEME_COLUMS)` 替换为内联 SQL：
  - 直接关联 `indicators_lib.lib_index_relation`
  - 用 `POSITION(CONCAT(',', o.INDEX_ID, ',') IN CONCAT(',', COALESCE(s.SCHEME_COLUMS, ''), ',')) > 0` 判断指标 ID 是否在方案字段列表中
  - 用 `string_agg(...)` 聚合为页面需要的 `INDEX_NAME`
  - 保留原有 `INDEX_TYPE = '1'` 时追加 `(%)` 的展示语义

## 为什么这样改

- 不再依赖数据库中缺失的历史函数对象，避免环境差异反复触发同类问题。
- 第一版 `unnest(string_to_array(...)) AS col(index_id)` 虽然数据库可执行，但会被当前 Druid Wall 的 PostgreSQL 解析器拦截。
- 最终改成 `POSITION + string_agg` 的保守写法，兼容当前 Druid 拦截器，同时仍然满足展示需求。
- 只改当前报错查询路径，范围最小，不影响 controller 和 service 层。

## 验证

1. 在真实库执行查询排查：确认 `scheme_colums` 类型为 `text`，且 `f_get_indexname` 函数不存在。
2. 在真实库试跑最终兼容 SQL：可以正常执行，不再依赖缺失函数。
3. 模块级编译验证：

```powershell
cd indicatorsLibv-1.0
mvn -DskipTests compile
```

结果：`BUILD SUCCESS`

## 未做事项

- 未给数据库补建 `f_get_indexname` 函数。
- 未顺手清理同文件中其他历史动态 SQL 风险点，本次只处理当前报错链路。