# 大屏数据字段契约修复设计

## 目标

以 `dwbi_vis_screen` 原有前端、控制器和 Mapper 的逐接口行为为准，修复 `gk-data-analysis` 大屏预览中“数据库存在数据但图表无数据”的问题，并保持迁移前的接口兼容性。

## 已确认的问题

方案 `3333` 会发出五个数据请求：

- `queryData/getStructure`：成功且有数据。
- `queryData/getThreeBudget`：失败。
- `queryData/getTaxRevenue`：失败。
- `queryData/getSubjectPay`：成功且有数据。
- `queryData/getPurposePay`：成功且有数据。

两个失败请求都在年维度查询中选择 `LEFT(a.DACCT, 4)`，却按 `a.DACCT` 排序。Vastbase 要求 `SELECT DISTINCT` 的排序表达式出现在选择结果中，因此拒绝执行。

同时，迁移后的 `QueryDataMapper.xml` 仅为部分查询添加了带双引号的大写别名。Vastbase 会把未加引号的结果列名折叠为小写；当源控制器继续通过 `DACCT`、`INDEX_VALUE` 等大写键读取 `Map` 时，会得到空值。

## 字段契约

不做机械的全局大小写转换。每个接口按以下顺序确定契约：

1. 以 `dwbi_vis_screen` 原前端实际读取的响应字段为准。
2. 以原控制器实际读取的数据库 `Map` 字段为准。
3. 以原 Mapper 的业务含义和查询结果为依据补齐 Vastbase 兼容语法。

因此：

- 前端响应继续使用源接口已有字段，例如 `data`、`x`、`amount`、`name`、`value`。
- 控制器读取数据库结果时继续使用源代码已有字段，例如 `DACCT`、`INDEX_VALUE`、`AREA_DSCR`。
- SQL 对需要保持大写的 `Map` 键使用带双引号的别名，例如 `AS "DACCT"`、`AS "INDEX_VALUE"`。
- 如果源前端或控制器本来使用小写字段，则保持小写，不为了形式统一而改名。

## 修复范围

### Mapper 契约审计

逐个检查 `QueryDataMapper.xml` 中所有返回 `java.util.Map` 的查询，并与源控制器的 `map.get(...)` 调用对应：

- 为控制器按大写读取的字段增加显式大写别名。
- 为控制器按小写读取的字段保留小写别名。
- 不修改对实体 `resultMap` 的字段映射，除非运行证据表明实体映射错误。

### Vastbase SQL 修复

年维度查询选择 `LEFT(a.DACCT, 4)` 时，选择、分组和排序使用相同表达式。非年维度继续按完整 `a.DACCT` 处理。

重点修复并验证：

- `getThreeBudgetRate`
- `getTaxRevenueRate`

审计中发现的相同模式一并修复，避免其他大屏方案重复出现同类错误。

### 前端

大屏组件保持 `dwbi_vis_screen` 原响应字段解析逻辑。只有在对照源代码确认迁移时写错字段名的情况下才调整前端，不增加大小写兜底转换。

## 数据流

1. 大屏页面根据 `query_path` 调用对应的 `queryData/*` 接口。
2. 控制器按源实现组织查询参数并调用 Mapper。
3. Mapper 使用 Vastbase 兼容 SQL，返回字段名与控制器读取键完全一致的 `Map`。
4. 控制器继续输出源前端约定的响应结构。
5. `BigScreenTabTemplate.vue` 将响应数据写入 ECharts 配置并渲染。

## 错误处理

- SQL 执行失败时接口保持现有失败响应，但验收不允许 3333 的任一数据请求失败。
- 查询成功但业务结果为空时保留源项目的 `nodata` 行为。
- 不把 SQL 错误或字段契约错误伪装成正常空数据。

## 测试与验收

1. 先增加可自动执行的 Mapper 契约检查，证明缺失别名和错误年维度排序会被发现。
2. 修改后运行后端相关测试或至少完成模块编译与 Mapper XML 解析。
3. 运行前端回归测试和生产构建，确认接口字段未破坏现有页面代码。
4. 从 `http://cui02:3000` 登录并通过门户进入大屏预览。
5. 验证 3333 的五个 `queryData/*` 请求全部返回成功。
6. 验证两页所有配置了数据源的图表均实际显示数据，左右按钮和键盘切换后数据仍存在。
7. 检查浏览器控制台和网络请求，不允许出现由本次修复导致的错误。

## 非目标

- 不重命名所有 REST JSON 字段。
- 不增加控制器大小写双读兼容层。
- 不批量修改数据库数据。
- 不改动与大屏数据字段契约无关的业务模块。
