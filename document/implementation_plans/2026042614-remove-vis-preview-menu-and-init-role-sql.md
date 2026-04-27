# vis 菜单收口与角色初始化 SQL

## 本次目标

用户要求做三件事：

1. 去掉 `可视化大屏` 下的 `预览概览`。
2. 保持 `可视化大屏` 根菜单排在最后。
3. 生成本次 vis 合并后可直接初始化的权限/角色 SQL。

## 本次修改

### 1. 收口 vis 菜单 seed

修改 `2026042303-gk-vis-menu-seed.sql`：

- 删除 `vis_preview_menu_20260423` 的插入逻辑。
- 增加对 `vis_preview_menu_20260423` 及其 `sys_role_permission` 的清理，避免旧库重复执行 seed 后仍保留该菜单。
- 将 `vis_root_menu_20260423` 的 `redirect` 从 `/vis/preview` 改为 `/vis/gallery`。
- 保持 `sort_no = 999.00`，继续确保 `可视化大屏` 作为顶层菜单落在最后。
- 管理员自动授权列表中移除 `vis_preview_menu_20260423`。

### 2. 补齐存量库迁移脚本

修改 `2026042411-vis-route-prefix-migration.sql`：

- 对已有 `vis_root_menu_20260423` 强制回写 `redirect = '/vis/gallery'`、`sort_no = 999.00`、`always_show = 0`。
- 删除已导入数据库中的 `vis_preview_menu_20260423` 及其授权记录。

### 3. 新增 vis 角色初始化 SQL

新增 `2026042613-vis-merge-role-init.sql`：

- 创建角色：`可视化大屏` / `vis_screen_manager`
- 授权当前 vis 合并后的主菜单权限：
  - `可视化大屏`
  - `图库`
  - `大屏设置`
  - `指标库方案`
  - `数据字典`
  - `业务类型`
  - `国库`
  - `模板管理`
  - `页面管理`
  - `页面编辑`
  - `展示方案管理`
- 默认把该角色绑定给 `admin`，方便导入后立即验证。

## 执行顺序

推荐顺序：

1. 执行 `2026042303-gk-vis-menu-seed.sql`
2. 执行 `2026042412-add-vis-system-management-menus.sql`
3. 执行 `2026042613-vis-merge-role-init.sql`

如果库里已经导入过旧 vis 菜单，再额外执行：

4. `2026042411-vis-route-prefix-migration.sql`

## 验证点

- `sys_permission` 中不再存在 `vis_preview_menu_20260423`
- `vis_root_menu_20260423.redirect = '/vis/gallery'`
- `vis_root_menu_20260423.sort_no = 999.00`
- `sys_role.role_code = 'vis_screen_manager'` 存在
- `sys_role_permission` 中该角色已关联 vis 合并后的菜单权限