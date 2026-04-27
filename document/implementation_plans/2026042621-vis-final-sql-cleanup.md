# vis 最终 SQL 清理说明

本次已将 vis 菜单合并相关的历史 SQL 收口为一份最终可执行脚本：

- 保留：`2026042615-vis-production-menu-converge.sql`

这份脚本现在已经同时包含：

1. vis 根菜单收口
2. 预览概览菜单删除
3. 页面编辑承接菜单修正为 `vis/PageEditorEntry`
4. vis 独立角色 `vis_screen_manager` 初始化与授权补齐
5. 旧 GK `指标库/指标查询` 菜单恢复到 `/statistics/schemeIndex`

已删除的历史 SQL：

- `2026042303-gk-vis-menu-seed.sql`
- `2026042411-vis-route-prefix-migration.sql`
- `2026042412-add-vis-system-management-menus.sql`
- `2026042613-vis-merge-role-init.sql`
- `2026042618-fix-legacy-indicator-query-menu.sql`

删除原因：

- 上述脚本的有效变更已被并入最终脚本。
- 继续保留多份 SQL 容易导致生产执行顺序混乱，或重复执行旧的语义化 ID 版本。
- 最终目录只保留一份可直接给生产使用的 vis 收口 SQL，减少误操作风险。

验证结果：

- 已在真实库 `100.71.11.54:25432 / gk_data_analysis / "jeecg-boot-os"` 上用 `BEGIN ... ROLLBACK` 执行过最终脚本，确认可执行且未落库。