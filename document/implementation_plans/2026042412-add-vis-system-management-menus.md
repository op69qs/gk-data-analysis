# vis 系统管理菜单补齐

## 背景

合并 vis_screen 后，`可视化大屏` 根菜单下当前只落了预览概览、图库、大屏设置、指标库方案四项。

对照原始 `vis_screen/visual_screen-sql-data/sys_permission.sql`，原 vis 的系统管理下除了已经复用主系统的用户管理、角色管理、菜单管理外，还保留了三项业务入口：

- 数据字典
- 业务类型
- 国库

## 本次处理

1. 从原 vis SQL 中确认缺口仅为上述三项。
2. 新增 `2026042412-add-vis-system-management-menus.sql`，补齐三条 `/vis/system/*` 菜单。
3. 同步补齐 `sys_role_permission`，将新菜单授权给当前已经拥有 vis 菜单的角色。

## 预期结果

- `vis_root_menu_20260423` 下新增：
  - `/vis/system/dict`
  - `/vis/system/business-type`
  - `/vis/system/treasury`
- 已有 vis 菜单角色刷新权限后可见上述菜单。