# vis 路径前缀彻底统一

## 目标

把 vis 相关菜单、直刷路径、预览跳转和 SQL 种子尽量收敛到 `/vis/...` 前缀，同时明确当前恢复态仍保留少量必要的旧入口兼容。

## 本次调整

1. 前端主跳转与菜单目标统一改成 `/vis/...`；当前恢复态仍保留 `/BigScreenPreview -> /vis/preview` 的兼容承接，避免旧链接和硬刷新失效。
2. 前端组件内部跳转改为只写 `/vis/...`，包括方案预览和页面关联方案预览。
3. 动态菜单仍保留一层旧数据归一：如果数据库还没执行迁移，旧 `/gallery`、`/BigScreen*`、`/indexLibrary` 菜单会在前端被收敛成新的 `/vis/...` 路径，不再对外暴露旧地址。
4. 后端已移除大部分旧 vis 路径的公开放行与 SPA forward；当前恢复态额外保留 `/BigScreenPreview` 及其子路径的匿名放行和 SPA forward，用于承接旧预览入口。
5. `vis_screen/sql/sys_permission.sql`、`vis_screen/visual_screen-sql-data/sys_permission.sql` 和 GK 侧菜单 seed 已同步改成 `/vis/...`。
6. 新增 `2026042411-vis-route-prefix-migration.sql`，用于修正现有数据库里已经导入过的旧菜单记录。
7. 原 `AddTemplate` 对应的隐藏编辑路由不会删除，而是收敛为 `/vis/bigscreen/pages/editor`，因为当前它仍是页面编辑跳转的数据库侧承接入口。

## 验证

- `org-tribe-view` 本次改动涉及的 Vue/JS 文件已通过问题检查，无新增语法错误。
- `org-tribe-system` Java 文件的问题检查仍受本地 Lombok/JDK 语言服务环境影响，属于既有环境问题，不是本次改动引入。
- 运行态静态 bundle 需要同步替换后才能让 9090 立即反映源码变化。

## 备注

当前恢复态不再扩散旧公开入口，但为了避免现网旧菜单、旧书签和数据库尚未迁移时直接失效，仍保留了两类过渡兼容：动态菜单里的“旧路径归一到新路径”内部转换，以及 `/BigScreenPreview` 的预览别名承接。数据库执行完 `2026042411-vis-route-prefix-migration.sql`、旧预览链接完成替换后，这两层都可以继续收缩。

补充说明：`/bigScreen/AddTemplate` 不再作为公开路径保留，但对应菜单记录需要继续存在并迁到 `/vis/bigscreen/pages/editor`，否则页面编辑跳转会失去数据库侧动态路由定义。