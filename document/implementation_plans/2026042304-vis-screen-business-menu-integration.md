# vis_screen 业务菜单并入 GK 实施记录 02

## 本轮目标

将 vis 原有业务菜单对应的界面继续收口到 GK `org-tribe-view`，优先接入可以直接复用的业务页：

- 方案管理
- 页面管理
- 模板管理

本轮不继续扩散到 vis 原有 JEECG 平台管理页，这部分已在前序收口中明确下线。

## 本轮落地

### 1. 扩展 vis 前端 API 封装

已在 `org-tribe-view/src/api/visScreen.js` 新增：

- `addSchemeInfo`
- `editSchemeInfo`
- `deleteSchemeInfo`
- `addPageInfo`
- `editPageInfo`
- `deletePageInfo`
- `editPageInfoState`
- `getPageSubAll`
- `getGalleryAll`
- `getOrgTree`
- `getNewAreaTree`
- `getGuokuTree`
- `getKeMuTreeName`
- `getSubjectTree`
- `bussTypeGetAll`

作用：

- 让 GK 前端可以直接承接 vis 的方案管理与页面管理操作，不再依赖旧站的 `@/api/api`。

### 2. 新增 GK 侧方案管理页

已新增：

- `org-tribe-view/src/views/vis/SchemeList.vue`
- `org-tribe-view/src/views/vis/modules/VisSchemeModal.vue`

当前行为：

- 支持按方案名称查询。
- 支持新增、编辑、删除方案。
- 支持通过 `schemeInfo/getAllRel` 回显方案与页面关系。
- 支持拖拽调整方案内页面轮播顺序。
- 预览动作直接跳转 GK 中的 `/vis/preview`，并携带 `schemeId`。

### 3. 新增 GK 侧页面管理页

已新增：

- `org-tribe-view/src/views/vis/PageList.vue`
- `org-tribe-view/src/views/vis/PageEditor.vue`
- `org-tribe-view/src/views/vis/modules/VisPageBlockDrawer.vue`
- `org-tribe-view/src/views/vis/modules/VisQuarterPicker.vue`
- `org-tribe-view/src/views/vis/modules/VisYearRangePicker.vue`

当前行为：

- 支持按页面名称、状态查询。
- 支持在 `/vis/pages?editor=add` 打开新增编辑器。
- 支持在 `/vis/pages?editor=edit&id=...` 打开编辑器并回显页面布局。
- 支持模板选择、背景图/背景色配置、标题背景设置。
- 支持打开图库模块设置弹窗，配置时间、维度与文本块内容。
- 支持保存草稿与直接发布，提交到 vis 后端的 `pageInfo/add`、`pageInfo/edit`。
- 支持页面删除。
- 支持发布/撤回状态切换。
- 支持跳转到已有预览页做联动预览。

说明：

- 已将 `vue-grid-layout`、`vue-color`、`dom-to-image` 补入 `org-tribe-view/package.json`，并通过固定 Node14 工具链完成安装与构建验证。
- 当前采用“不新增菜单路由”的方式，在现有 `/vis/pages` 页面内通过查询参数切换列表态与编辑态，避免继续改菜单 SQL。

### 4. 新增 GK 侧模板管理页

已新增：

- `org-tribe-view/src/views/vis/TemplateList.vue`

当前行为：

- 在 GK 中展示 vis 原有模板总览。
- 作为后续页面编辑器迁移时的模板选型对照页。

### 5. 预览页支持按方案跳转

已更新：

- `org-tribe-view/src/views/vis/PreviewEntry.vue`

当前行为：

- 支持读取路由参数 `schemeId`。
- 从方案管理页点击“预览”后，会在 GK 预览页中自动选中对应方案，而不是始终落到第一条方案。

### 6. 扩展 GK 菜单种子 SQL

已更新：

- `document/implementation_plans/2026042303-gk-vis-menu-seed.sql`

新增菜单：

- `方案管理` -> `vis/SchemeList`
- `页面管理` -> `vis/PageList`
- `模板管理` -> `vis/TemplateList`

并同步扩展 `sys_role_permission` 自动授权逻辑，确保管理员角色导入后即可看到新增菜单。

## 当前收口状态

已并入 GK 的 vis 业务菜单页：

- 预览概览
- 方案管理
- 页面管理
- 模板管理

仍待下一批继续迁移：

- 因静态图片资源缺失导致的部分区块 404 展示问题
- 编辑器更深层的模板/图库管理关联页
- 更深一层的发布、撤回、删除、预览联动等业务动作回归

## 验证重点

本轮完成后应继续验证：

1. 执行 `2026042303-gk-vis-menu-seed.sql` 后，GK 动态菜单是否返回新增三个 vis 子菜单。
2. `org-tribe-view` 在固定 Node 14 工具链下能否通过构建。
3. 方案管理页的新增、编辑、删除是否经 GK 网关成功落到 vis 后端。
4. 页面管理页的状态切换、删除、新增、编辑是否成功。

## 本轮补充修复

### 7. 修复 `/vis/**` 深链直刷的两段问题

已完成：

- GK 后端增加 `/vis` 与 `/vis/**` 的匿名放行与 `index.html` 转发。
- `org-tribe-view/vue.config.js` 将生产构建 `publicPath` 改为绝对路径 `/`。

根因拆分：

- 第一段问题是后端未为 Vue Router history 模式提供 `/vis/**` fallback，导致直刷时落到 Shiro/JWT 检查并报 `token为空!`。
- 第二段问题是前端生产包原先使用相对资源路径 `./`，即使后端已回退到 `index.html`，深链地址 `/vis/schemes` 仍会把脚本解析成 `/vis/js/*`，随后再次被 fallback 成 HTML，浏览器报 `Unexpected token '<'`。

本机验证结果：

- `http://127.0.0.1:9090/vis/schemes` 直刷后不再出现后端 Whitelabel 或 `token为空!`。
- `http://127.0.0.1:9090/vis/pages` 直刷后正常进入登录守卫：`/user/login?redirect=%2Fvis%2Fpages`。
- `http://127.0.0.1:9090/vis/templates` 直刷后正常进入登录守卫：`/user/login?redirect=%2Fvis%2Ftemplates`。

说明：

- vis 业务页面本来就要求登录后访问，因此未登录状态下跳转登录页属于符合预期的结果。

### 8. 接入 `/vis/pages` 内页编辑器首个可用切片

已完成：

- 页面管理“添加/编辑”按钮不再停留在提示文案，而是切到 GK 内的新编辑器页。
- 新编辑器沿用 vis 原有页面模型与后端接口，不改 vis 服务端控制器。
- 通过查询参数复用现有菜单路径：
	- `/vis/pages?editor=add`
	- `/vis/pages?editor=edit&id=<pageId>`

本机验证结果：

- 使用仓库指引中的测试账号 `admin / Ysyyrps4` 登录 GK 成功。
- 打开 `http://127.0.0.1:9090/vis/pages` 后，“添加”按钮可进入新增编辑态。
- 选择模板后，页面栅格画布可正常渲染，不再停留在“迁移中”占位提示。
- `org-tribe-view` 通过嵌入式 Node14 完成依赖安装与生产构建，产物已同步覆盖到 GK 静态目录。

当前限制：

- 当时页面管理列表数据尚未正常回显，因此这一阶段只验证到“新增编辑器可进入并渲染模板画布”；后续已在第 9、10 节完成列表修复与“编辑已有页面并提交保存”的在线回归。
- 登录后控制台仍有系统既有的 WebSocket 连接超时日志，与本次 vis 页面编辑器接入无直接关系。

### 9. 修复页面管理列表空白但实际已保存的问题

已完成：

- 修复 `org-tribe-view/src/views/vis/PageList.vue` 对 `pageInfo/getPage` 的调用参数。

根因：

- `pageInfo/add` 实际保存成功，新增页面已能从 `pageInfo/getAll` 查询到。
- 页面管理列表之所以一直显示“暂无页面数据”，不是保存失败，而是前端列表调用 `pageInfo/getPage` 时未传 `pageNo`、`pageSize`。
- vis 后端该分页接口在缺少分页参数时返回 `result=success`，但 `msg=操作失败` 且没有 `rows`，从而被前端当成空列表渲染。

本机验证结论：

- 使用 `admin / Ysyyrps4` 在 `/vis/pages?editor=add` 新增页面 `集成测试页面0423` 后，接口实际已落库。
- 浏览器侧带 token 重放 `pageInfo/getPage`：
	- 不带分页参数时，无 `rows`。
	- 带 `pageNo=1&pageSize=10` 时，可返回包含 `集成测试页面0423` 在内的分页数据。

说明：

- 这次修复只收敛在列表查询参数，不改编辑器保存逻辑。

### 10. 完成“编辑已有页面并提交保存”的在线回归

已完成：

- 在 GK 前端登录态下进入 `http://127.0.0.1:9090/vis/bigscreen/pages`，页面管理列表已可正常展示真实页面数据。
- 进一步验证 `http://127.0.0.1:9090/vis/bigscreen/schemes` 与 `http://127.0.0.1:9090/vis/gallery`，确认展示方案管理和图库入口也均已恢复。
- 从页面管理列表进入现有页面 `集成测试页面0423`（`id=dbc930a160374996a85b36091b419da1`）的编辑器，执行一次“保存”，页面返回列表并提示 `修改成功`。

本轮观察：

- 保存已有页面后，vis 原有业务语义会把页面状态切成 `未发布`，这不是保存失败，而是原始页面状态流转的一部分。
- 为避免对现有样例数据造成持续影响，保存成功后已立刻再次执行一次“发布”，将该页面恢复为 `已发布`。

结论：

- 页面管理不再只停留在“可进入编辑器”，而是已经完成“编辑已有页面并实际提交保存”的在线闭环验证。
- 本文档前序遗留的“登录后对编辑已有页面并实际提交的更完整业务回归”现已关闭。