# vis 菜单数据库实库更新记录

## 本次操作

- 目标库：`100.71.11.54:25432/gk_data_analysis`
- 账号：`vastbase_test`
- schema：`"jeecg-boot-os", public`
- 目标表：`sys_permission`

## 实际执行内容

- 执行 `2026042411-vis-route-prefix-migration.sql` 对应的菜单路径收敛逻辑。
- 额外把实库中发现的 `/statistics/indexLibrary` 也统一更新为 `/vis/index-library`，组件改为 `vis/SchemeList`。
- 删除兼容残留菜单：
  - `vis_legacy_gallery_menu_20260423`
  - `vis_legacy_index_library_menu_20260423`
  - `vis_legacy_preview_menu_20260423`
- 删除与正式菜单重复的 legacy 子菜单：
  - `vis_legacy_template_menu_20260423`
  - `vis_legacy_page_menu_20260423`
  - `vis_legacy_scheme_menu_20260423`

## 保留项

- 保留 `vis_legacy_add_template_menu_20260423`。
- 原因：当前库里没有单独的正式 editor 菜单记录，这条记录是 `/vis/bigscreen/pages/editor` 的唯一入口定义，删除会丢失编辑态动态路由。

## 执行后结果

- `sys_permission` 中旧路径 `/gallery`、`/indexLibrary`、`/statistics/indexLibrary`、`/BigScreen*`、`/bigScreen*` 已清零。
- `statistics/indexLibrary` 旧组件残留已清零。
- 库中 vis 相关菜单只保留正式 `/vis/...` 路径和必要的 editor/preview 入口。

## 风险与说明

- 本次为直接实库更新，未通过应用启动迁移。
- 若其他环境也存在同批旧菜单数据，需要同步执行更新版迁移脚本。