# vis 系统管理页继续迁移

## 本轮目标

在已经完成 `预览/图库/模板/页面/方案` 迁移的基础上，继续把 vis 原前端里仍出现在菜单中的系统管理页面并入 GK 前端。优先级选择为：

- `system/TreasuryList`
- `system/BusinessTypeList`

这两个页面满足三个条件：

1. 用户当前反馈截图里仍能看到对应菜单入口。
2. GK `org-tribe-view` 里此前没有同名组件文件，点击后只能落到缺页或空白。
3. vis 后端对应接口仍然保留，且已经通过 `/visScreen/**` 网关接入 GK。

## 为什么这一轮只迁这两个系统页

vis 原 `src/views/system` 目录里还有 `UserList`、`RoleList`、`PermissionList`、`DictList` 等 JEECG 标准系统页，但这些能力在 GK 主系统里原本就有，且用户此前已经明确要求收口 vis 自己的登录、用户、角色、权限能力。因此这一轮不再重复迁一套系统壳，只补 vis 业务仍依赖、且菜单里确实还在暴露的两个页面。

## 已完成改动

前端代码：

- 在 `org-tribe-view/src/api/visScreen.js` 增补业务类型和国库页面所需的 `/visScreen/**` 代理接口。
- 新增 `org-tribe-view/src/views/system/TreasuryList.vue`。
- 新增 `org-tribe-view/src/views/system/modules/TreasuryModal.vue`。
- 新增 `org-tribe-view/src/views/system/BusinessTypeList.vue`。
- 新增 `org-tribe-view/src/views/system/modules/BusinessTypeListModal.vue`。

菜单 SQL：

- 在 `2026042303-gk-vis-menu-seed.sql` 追加 `业务类型` 和 `国库` 两个兼容菜单。
- 菜单路径沿用 vis 原路径：`/isystem/BusinessTypeList`、`/isystem/TreasuryList`。
- 组件路径使用当前 GK 工作区真实文件：`system/BusinessTypeList`、`system/TreasuryList`。
- 父菜单不再硬编码依赖某一个环境，只优先寻找已有 `系统管理` 根节点。

## 验证情况

已完成：

- 新增文件的编辑器静态错误检查通过。

受环境限制未完成：

- `npm run lint` 无法执行，原因是当前项目缺 ESLint 配置。
- `npm run build` 在当前机器默认 `Node.js v23` 下会遇到旧版 Vue 2 / webpack 4 依赖链兼容问题；补 `NODE_OPTIONS=--openssl-legacy-provider` 后又在旧依赖的 `postcss/css-loader` 子依赖导出规则上失败。

结论：

- 当前未发现这次新增页面自身的语法或导入错误。
- 若要完成运行级页面回归，应在仓库指引建议的 `Node 14` 环境下重新执行前端构建或启动 dev server，再验证菜单点击与 CRUD 主链路。