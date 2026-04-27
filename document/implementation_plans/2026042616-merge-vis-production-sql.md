# vis 生产脚本合并

## 背景

用户要求把此前分散的 vis 菜单 SQL 合并成一份可直接用于生产的脚本，并要求：

1. 先查实库，不按历史文件猜当前状态。
2. URL 与 component 必须对照当前前端真实路由承接关系。
3. 解释 `vis_root_menu_20260423` 这种记录为什么会存在。

## 实库核对结果

已直接查询 `100.71.11.54:25432 / gk_data_analysis / "jeecg-boot-os"`：

- `vis_root_menu_20260423`
  - `url = /vis`
  - `component = layouts/RouteView`
  - `component_name = VisRoot`
  - `redirect = /vis/preview`
  - `sort_no = 1`
  - `always_show = 0`
- `vis_preview_menu_20260423` 仍存在，当前还挂在 vis 根菜单下。
- 生产里的页面编辑承接菜单真实 id 是 `vis_legacy_add_template_menu_20260423`，不是之前脚本里假定的 `1321380065238953985`。
- 该菜单当前实际是：
  - `url = /vis/bigscreen/pages/editor`
  - `component = vis/PageList`
  - `component_name = VisLegacyAddTemplate`
  - `hidden = 1`
- 实库中不存在旧路径残留记录：
  - `/gallery`
  - `/indexLibrary`
  - `/statistics/indexLibrary`
  - `/BigScreen`
  - `/bigScreen/TemplateList`
  - `/BigScreen/PageList`
  - `/BigScreen/ExhibitionSchemeList`
  - `/bigScreen/AddTemplate`
  - `/BigScreenPreview`
- 当前拥有 vis 菜单权限的角色是：
  - `admin`
  - `zhcx`

额外确认到一条不属于 vis 根菜单子树的兼容菜单：

- `aea6b487925d084dad182e09c95a6c79` 的 `url = /statisticss`
- 其子菜单 `cae8031ed1a7aeaed5625928a5ed74da` 指向 `/vis/index-library`

这条不是 `vis_root_menu_20260423` 子树的一部分，本次不动，避免误删其它业务入口。

## 前端对照结果

对照 `org-tribe-view/src/utils/util.js` 与相关页面：

- `/vis` 当前只是一条静态隐藏路由，承接组件仍是 `PreviewEntry.vue`。
- `/vis/preview` 仍可用于方案预览跳转，不应该因为删菜单就删除路由能力。
- `/vis/bigscreen/pages/editor` 的真实前端承接组件是 `vis/PageEditorEntry`。
- 旧 `BigScreen/AddTemplate` 在前端已被归一到 `vis/PageEditorEntry`。

因此生产脚本里：

- 只删 `vis_preview_menu_20260423` 菜单，不删 `/vis/preview` 能力。
- 需要把 editor 菜单组件修正成 `vis/PageEditorEntry`。

## `vis_root_menu_20260423` 为什么会是这种内容

这条记录不是业务页面，而是 JEECG 动态菜单里的“分组节点”：

- `url = /vis`：顶层菜单路径。
- `component = layouts/RouteView`：告诉前端它是一个承接子菜单的路由容器，不是具体业务页。
- `redirect = /vis/preview`：旧版本默认点根菜单时落到预览页。
- `always_show = 0`：避免顶栏 topmenu 把这个 RouteView 根节点渲染成空白单项，而是作为可展开分组显示。

也就是说，这种内容本身不是错，错的是它现在仍然把默认落点指到要删除的 `预览概览`，而且 `sort_no = 1` 让它排在过前位置。

## 本次输出

新增一份生产合并脚本：

- `2026042615-vis-production-menu-converge.sql`

这份脚本合并了此前分散在：

- `2026042303-gk-vis-menu-seed.sql`
- `2026042412-add-vis-system-management-menus.sql`
- `2026042613-vis-merge-role-init.sql`

中的可复用部分，并按实库修正了以下关键点：

1. `vis_root_menu_20260423` 改成 `redirect = /vis/gallery`、`sort_no = 999.00`
2. 删除 `vis_preview_menu_20260423` 及其角色授权
3. 保留并修正 `vis_legacy_add_template_menu_20260423`，改为 `vis/PageEditorEntry`
4. 保留现有 vis 角色授权链，同时补一个独立角色 `vis_screen_manager`
5. 不碰 `/statisticss -> /vis/index-library` 兼容菜单
6. 不再继续写入 `vis_*_20260423` 这类语义化主键；新脚本改为按 `url/component_name` 收敛现网记录，缺失记录再补 32 位 md5 风格 ID，以和并入后库里的主流 ID 形式保持一致

## 验证方式

文件级只做了静态校验与实库只读核对，没有在生产库直接执行写入。

建议正式执行前先在同库做：

1. `BEGIN;`
2. 执行 `2026042615-vis-production-menu-converge.sql`
3. 查询 `vis_root_menu_20260423`、`vis_preview_menu_20260423`、`vis_legacy_add_template_menu_20260423`
4. 确认后再 `COMMIT;`
