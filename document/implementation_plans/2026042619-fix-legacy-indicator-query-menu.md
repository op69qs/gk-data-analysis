# 恢复生产库旧“指标查询”菜单到统计页

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

- 该菜单父节点仍是旧 GK 的“指标库”，同组兄弟菜单为：
  - `编辑指标`
  - `新增指标`
  - `指标管理`
  - `公共指标管理`
- 结合页面职责：
  - `statistics/schemeIndex.vue` 是旧“方案列表/公共方案查询”入口页。
  - `statistics/indexLibrary.vue` 是从方案列表点击“执行”后进入的实际查询页。
- 因此旧“指标查询”菜单应恢复为：
  - `url = /statistics/schemeIndex`
  - `component = statistics/schemeIndex`

## 产出

- 新增 SQL：`2026042618-fix-legacy-indicator-query-menu.sql`
- 该 SQL 仅更新一条旧 GK 菜单记录，不修改 vis 菜单树。

## 验证

- 使用事务包裹脚本进行预演，确认只会更新一条记录，并将其恢复为：
  - `url = /statistics/schemeIndex`
  - `component = statistics/schemeIndex`
- 随后已直接执行到生产库并回读确认，当前实库该菜单已恢复为旧统计页入口。

## 说明

- 在应用该 SQL 前，前端源码侧已完成两项收口：
  - 取消 `util.js` 对 `statistics/indexLibrary` 与 `statistics/schemeIndex` 到 vis 的强制归一。
  - 恢复 `schemeIndex.vue` 与 `indexLibrary.vue` 的旧统计页实现。
- 因此数据库修复后，这两个入口会重新分离：
  - 旧“指标查询” -> `/statistics/schemeIndex`
  - vis“指标库方案” -> `/vis/index-library`