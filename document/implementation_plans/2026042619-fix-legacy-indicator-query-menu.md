# 恢复生产库旧“指标查询”菜单到生产查询页

## 问题

- 主前端源码已经把旧统计页和 vis 方案页拆开，但运行态仍然出现“指标库/指标查询”和 vis“指标库方案”打开同一页面。

## 定位

- 直接查询生产库 `sys_permission` 后确认，现存旧 GK 菜单：
  - `id = cae8031ed1a7aeaed5625928a5ed74da`
  - `parent_id = aea6b487925d084dad182e09c95a6c79`（旧“指标库”根菜单）
  - `name = 指标查询`
- 这条菜单当前被错误改成：
  - `url = /vis/index-library`
  - `component = vis/SchemeList`
- 同时生产库中 vis 菜单 `vis_index_library_menu_20260423` 也存在并同样指向 `/vis/index-library + vis/SchemeList`。
- 因此当前两个入口同页的直接原因是：数据库里已经把旧 GK 菜单实改成了 vis 菜单，而不是只有前端兼容层错误。

## 修复判断

- `statistics/schemeIndex.vue` 是独立的公共方案列表页。
- `statistics/indexLibrary.vue` 是生产“指标查询”菜单直接打开的查询页。
- 旧“指标查询”菜单应恢复为：
  - `url = /statistics/indexLibrary`
  - `component = statistics/indexLibrary`
- vis“指标库方案”继续独立指向 `/vis/index-library`，本修复不修改它。

## 产出

- 新增 SQL：`2026072101-fix-legacy-indicator-query-entry.sql`
- 新增回退 SQL：`2026072101-fix-legacy-indicator-query-entry.rollback.sql`
- 该 SQL 仅更新一条旧 GK 菜单记录，不修改 vis 菜单树。

## 验证

- 使用事务包裹脚本并提供回读 SQL，只匹配指定菜单 ID、父菜单 ID 和名称。
- 预期修复结果：
  - `url = /statistics/indexLibrary`
  - `component = statistics/indexLibrary`
- 当前只产出现场执行脚本，未连接现场数据库执行，不声明实库已经更新。

## 说明

- 在应用该 SQL 前，前端源码侧已完成两项收口：
  - 取消 `util.js` 对 `statistics/indexLibrary` 与 `statistics/schemeIndex` 到 vis 的强制归一。
  - 恢复 `schemeIndex.vue` 与 `indexLibrary.vue` 的旧统计页实现。
- 因此数据库修复后，三个入口继续保持各自职责：
  - 旧“指标查询” -> `/statistics/indexLibrary`
  - 旧公共方案列表 -> `/statistics/schemeIndex`
  - vis“指标库方案” -> `/vis/index-library`
