# vis_screen 并入 GK 执行记录 01

## 本轮已落地

### 1. 认证缓存语义对齐

已将 vis 后端认证链路中的 Redis token TTL 从 `JwtUtil.EXPIRE_TIME * 2 / 1000` 统一为 `JwtUtil.EXPIRE_TIME / 1000`，与 GK 当前实现保持一致。

涉及位置：
- `vis_screen/jeecg-boot/jeecg-boot-base-common/src/main/java/org/jeecg/config/shiro/ShiroRealm.java`
- `vis_screen/jeecg-boot/jeecg-boot-base-common/src/main/java/org/jeecg/common/util/TokenUtils.java`
- `vis_screen/jeecg-boot/jeecg-boot-module-system/src/main/java/org/jeecg/modules/system/controller/LoginController.java`
- `vis_screen/jeecg-boot/jeecg-boot-module-system/src/main/java/org/jeecg/modules/cas/controller/CasClientController.java`
- `vis_screen/jeecg-boot/jeecg-boot-module-system/src/main/java/org/jeecg/modules/nexus/controller/NexusOAuthCallbackController.java`（随后已整体删除）

结论：
- GK 与 vis 现在可以共享同一个 Redis 实例。
- `prefix_user_token_` 前缀策略可以继续复用。
- 共享前提中的主要实现差异已从源码层面收敛。

### 2. GK 网关新增 vis 开发态路由

已在 `org-tribe-system/src/main/resources/application-dev.yml` 中新增：
- 路由前缀：`/visScreen/**`
- 转发方式：Zuul `url` 路由
- 后端地址来源：`VIS_SCREEN_BACKEND_URL`
- 默认值：`http://127.0.0.1:9082`

选择 `url` 路由而不是 `serviceId` 的原因：
- GK 当前使用 Zuul + Eureka。
- vis 当前源码默认注册到 Nacos。
- 两边注册中心尚未统一，当前阶段先打通网关转发链路更稳妥。

### 19. 修正 GK 开发态 vis 网关默认端口

已完成：

- 将 `org-tribe-system/src/main/resources/application-dev.yml` 中 `VIS_SCREEN_BACKEND_URL` 的默认值从 `http://127.0.0.1:7001` 修正为 `http://127.0.0.1:9082`。

根因：

- `/visScreen/**` 当前是 Zuul 的地址路由，不走注册中心发现。
- GK 开发态默认把 vis 后端指向 `127.0.0.1:7001`，但 vis 实际开发配置 `vis_screen/jeecg-boot/jeecg-boot-module-system/src/main/resources/application-dev.yml` 的服务端口是 `9082`。
- 结果是只要未显式注入 `VIS_SCREEN_BACKEND_URL`，页面管理、方案管理、图库等所有经 `/visScreen/**` 访问的接口都会在 GK 侧报 `Connect to 127.0.0.1:7001 failed: Connection refused`，前端表现为列表空白或控制台 500。

本轮验证：

- 使用 `admin / Ysyyrps4` 登录后直调 `POST /visScreen/pageInfo/getPage`，修正前可稳定复现 500，错误为连接 `127.0.0.1:7001` 被拒绝。
- 修正后，开发态在未额外设置环境变量时会默认转发到 vis 的实际本地端口 `9082`，与当前 vis dev 配置保持一致。

### 3. 移除 vis 独立 Nexus OAuth SSO 后端能力

已删除：
- `vis_screen/jeecg-boot/jeecg-boot-module-system/src/main/java/org/jeecg/modules/nexus/config/NexusOAuthConfig.java`
- `vis_screen/jeecg-boot/jeecg-boot-module-system/src/main/java/org/jeecg/modules/nexus/controller/NexusOAuthCallbackController.java`
- `vis_screen/jeecg-boot/jeecg-boot-module-system/src/main/java/org/jeecg/modules/nexus/controller/NexusSyncHintController.java`
- `vis_screen/document/2026041401-vis-nexus-oauth-sso.md`

已同步移除：
- `vis_screen/jeecg-boot/jeecg-boot-module-system/src/main/resources/application-dev.yml` 中的 `gk-nexus.oauth` 配置段。

### 4. 封禁 vis 旧平台入口

已在 vis 的 Shiro 过滤链中新增 `LegacyPlatformBlockFilter`，对以下旧平台路径直接返回下线响应：

- `/sys/login`
- `/sys/logout`
- `/sys/mLogin`
- `/sys/phoneLogin`
- `/thirdLogin/**`
- `/sys/user/**`
- `/sys/role/**`
- `/sys/permission/**`
- `/sys/sysUserAgent/**`
- `/sys/sysDepartRole/**`
- `/sys/sysDepartPermission/**`

本次做法是从统一过滤入口收口，而不是逐个删除 controller，目的是先切断旧平台访问面，同时不影响 `visualScreen` 业务 controller。

### 5. 建立 GK 前端 vis 基础骨架

已新增：

- `org-tribe-view/src/api/visScreen.js`
- `org-tribe-view/src/views/vis/PreviewEntry.vue`

当前状态：

- 前端已具备通过 `/visScreen/**` 前缀访问 vis 后端的 API 封装。
- 已准备一个可被后续动态菜单直接挂载的页面组件路径 `vis/PreviewEntry`。
- 当前未添加静态路由，避免绕开后续页面级权限菜单接入。

### 6. 补充 GK 菜单权限种子 SQL

已新增：

- `document/implementation_plans/2026042303-gk-vis-menu-seed.sql`

当前状态：

- 该 SQL 提供最小 `sys_permission` 菜单树，包含根菜单 `可视化大屏` 与子菜单 `预览概览`。
- 子菜单组件路径已对齐 `org-tribe-view/src/views/vis/PreviewEntry.vue`，导入后即可走 GK 原有动态菜单机制。
- 仍未引入 vis 原有用户、角色、权限菜单数据，只接入页面级访问入口。
- 2026-04-23 已实际导入 GK 开发库 `gk_data_analysis` 的 `jeecg-boot-os.sys_permission`，回查结果为：
	- `vis_root_menu_20260423 | layouts/RouteView | 0`
	- `vis_preview_menu_20260423 | vis/PreviewEntry | 1`
- 导入过程中发现 GK 当前 `sys_permission` 表结构不包含 `internal_or_external` 字段，因此种子 SQL 已按 GK 实际 25 列结构收敛。

### 7. 落地首个真实预览页迁移切片

已新增或更新：

- `org-tribe-view/src/api/visScreen.js`
- `org-tribe-view/src/views/vis/PreviewEntry.vue`
- `org-tribe-view/src/views/vis/modules/VisScreenSchemePreview.vue`
- `org-tribe-view/src/views/vis/modules/VisScreenEcharts.vue`

当前状态：

- `PreviewEntry` 不再只是占位概览页，已经可以在 GK 前端内选择方案并加载 `/visScreen/schemeInfo/getAllPage` 的真实页面数据。
- 预览页已支持文本区块、表格区块和常见图表区块的本地渲染。
- 区块内 `query_path` 已统一在前端补齐到 `/visScreen/**` 前缀，避免继续走 vis 独立站点裸路径。
- 地图区块本轮先做显式降级提示，未在本轮引入整套 `areaMap` 数据与旧版地图组件实现。

### 8. 本轮验证结果

- 新增与修改文件的编辑器静态检查通过，无新增语法错误。
- `org-tribe-view` 构建已执行两轮：
	- 直接 `npm run build` 失败，原因是当前 Node 23 与 webpack 4 的 OpenSSL 兼容问题。
	- 加 `NODE_OPTIONS=--openssl-legacy-provider` 后继续构建，仍卡在旧依赖链：`css-loader` / `postcss` 与当前 Node 版本的 `exports` 兼容问题。
- 因此当前前端构建阻塞属于运行环境与老依赖版本不兼容，不是本轮 vis 预览页改动导致的语法性失败。

### 9. 恢复固定 Node 14 工具链并完成 GK 前端构建

已验证：

- 仓库内固定工具链 `org-tribe-system/target/frontend/node` 仍可用。
- 实际版本为：
	- Node `v14.21.3`
	- npm `8.19.4`

处理结果：

- 清理了当前终端残留的 `NODE_OPTIONS=--openssl-legacy-provider`，避免其反向干扰 Node 14。
- 使用固定 Node 14 在 `org-tribe-view` 下重新执行 `npm run build`，构建成功。
- 新产物已覆盖到：
	- `org-tribe-system/src/main/resources/static`
	- `org-tribe-system/target/classes/static`

### 10. 补齐本地 deploy-package 联调路由

已更新：

- `deploy-package/config/org-tribe-system/application-dev.yml`

处理结果：

- 本地 `deploy-package` 开发配置已补充 `/visScreen/**` 路由。
- 本地默认目标设为 `http://127.0.0.1:9082`，与 `vis_screen/deploy-package/config/application-dev.yml` 的本地端口保持一致。
- 这样在本机用 `deploy-package` 联调时，GK 网关可以直接转发到 vis 打包服务。

### 11. 确认联调依赖并切换可用 profile

已验证：

- `vis_screen` 打包配置依赖可达：
	- `192.168.160.244:25432` 可达
	- `armbian-ca01.local:6379` 可达
- `gk-data-analysis` 的 `deploy-package` 开发配置依赖不可达：
	- `127.0.0.1:25432` 不可达
	- `127.0.0.1:6379` 不可达
- `gk-data-analysis` 的 `test/prod` 依赖可达：
	- `100.71.11.54:25432` 可达
	- `192.168.160.30:6379` 可达

处理结果：

- 本地联调时，GK 打包服务将优先使用 `test` 或 `prod` profile，而不是本机空依赖的 `dev` profile。
- 已为 `deploy-package/config/org-tribe-system/application-test.yml` 与 `application-prod.yml` 补充 `visScreen` 路由，保证使用远端依赖 profile 时也能转发到本机 vis 服务。

### 12. 实机联调结果与菜单缺失根因

已验证：

- `vis_screen` 打包服务已用最新 `vis-screen.jar` 成功启动在 `9082`。
- `org-tribe-system` 源码态可通过复用 `vis_screen/bes` 目录中的有效 BES license 成功启动在 `9090`。
- 浏览器可打开 GK 登录页，并使用 `admin` 登录进入首页。
- `/sys/permission/getUserPermissionByToken` 返回的菜单树中没有 vis 节点。

根因：

- 之前导入的 `2026042303-gk-vis-menu-seed.sql` 只插入了 `sys_permission` 菜单记录。
- 远端 GK 数据库中 `vis_root_menu_20260423` 与 `vis_preview_menu_20260423` 已存在。
- 但 `sys_role_permission` 中对这两个菜单的授权记录为 `0` 行，因此管理员菜单树不会返回 vis 菜单。

修正：

- 已补充种子 SQL，使其在导入菜单后，自动给 `admin` 当前关联的角色补 `sys_role_permission` 授权。
- 这样后续再执行种子 SQL，不需要手工进角色菜单界面分配权限。

### 13. vis 网关接口 404 与 token 失效根因

- 初次访问 `vis/preview` 时，`/visScreen/schemeInfo/getAll` 等接口返回 `404`。
- 根因是源码态 `org-tribe-system` 运行时仍在使用 `target/classes` 中旧的 `application-dev.yml`，未带上 `visScreen` 路由；同步配置并重启后，`404` 已消失。
- 随后同一接口返回 `500: Token失效，请重新登录`。


修正：

	- `vis_screen/deploy-package/config/application-dev.yml`
	- `vis_screen/jeecg-boot/jeecg-boot-module-system/src/main/resources/application-dev.yml`
- 这样 vis 在开发/本地联调时会与 GK 共享同一套 token 存储。

- 远端 GK 数据库中的 `vis_root_menu_20260423`、子菜单和 `sys_role_permission` 授权均已存在。
- `/sys/permission/getUserPermissionByToken` 返回的 `permissionList` 中，`/vis` 根节点和五个子菜单都已正确返回。
- 浏览器顶栏最初只显示空白首项，不显示 `可视化大屏` 文本。

最终根因：
- GK 前端顶栏组件 `org-tribe-view/src/components/menu/index.js` 对 `alwaysShow=true` 的有子菜单节点会按普通 `menu-item` 渲染，而不是 `submenu`。
- `vis` 根节点本身没有直连页面内容，只负责承接子菜单，因此最终在顶栏表现成一个空白菜单项，肉眼看起来等同于“没有 vis 入口”。


- 已将 `document/implementation_plans/2026042303-gk-vis-menu-seed.sql` 中 vis 根菜单的 `always_show` 改为 `0`，并补充 `UPDATE sys_permission SET always_show = 0`。
- 已把远端 GK 开发库中 `vis_root_menu_20260423.always_show` 实际更新为 `0`，回查结果为：
	- `vis_root_menu_20260423 | 1 | 0`
- 同时将 `org-tribe-view/src/components/page/GlobalHeader.vue` 顶栏右侧保留宽度从 `360px` 收紧到 `240px`，给顶栏菜单释放更多可见宽度。
- 使用仓库固定 Node 14 工具链重新构建 `org-tribe-view` 并覆盖到 GK 静态目录。

- 直接访问 `/vis/gallery` 已可正常打开，标签页标题为 `图库`。
- `图库` 页已拉起分类 tabs 与列表数据，当前页面可见真实数据项，例如：`三本预算`、`汇报`、`国库是什么`。


- `org-tribe-view/src/views/vis/PageList.vue`

本轮处理：

- 将三条已有业务菜单更新为 BigScreen 子菜单：
	- `vis_template_menu_20260423 -> /vis/bigscreen/templates`
	- `vis_page_menu_20260423 -> /vis/bigscreen/pages`
	- `vis_scheme_menu_20260423 -> /vis/bigscreen/schemes`
- `vis_scheme_menu_20260423` 的显示名称同步改为更贴近原 vis 的 `展示方案管理`。
	- `vis_root_menu_20260423 | /vis | 1 | 0`
	- `vis_gallery_menu_20260423 | vis_root_menu_20260423 | /vis/gallery | 2 | 0`
	- `vis_bigscreen_menu_20260423 | vis_root_menu_20260423 | /vis/bigscreen | 3 | 0`
	- `vis_template_menu_20260423 | vis_bigscreen_menu_20260423 | /vis/bigscreen/templates | 1 | 0`
	- `vis_page_menu_20260423 | vis_bigscreen_menu_20260423 | /vis/bigscreen/pages | 2 | 0`
	- `vis_scheme_menu_20260423 | vis_bigscreen_menu_20260423 | /vis/bigscreen/schemes | 3 | 0`

浏览器验证结果：

- admin 登录后，顶栏 `可视化大屏` 的下拉菜单已显示为：`预览概览 / 图库 / 大屏设置`。
- `大屏设置` 二级菜单已显示为：`模板管理 / 页面管理 / 展示方案管理`。
- 点击 `页面管理` 后地址为 `/vis/bigscreen/pages`。
- 在编辑器点击 `返回列表` 后地址回到 `/vis/bigscreen/pages`，未再跳回旧的 `/vis/pages`。

### 16. 补回 vis 原“指标库方案”入口并做失败降级

已更新：

- `document/implementation_plans/2026042303-gk-vis-menu-seed.sql`
- `org-tribe-view/src/views/statistics/indexLibrary.vue`

### 18. 恢复 vis 原独立预览页语义

已更新：

- `org-tribe-view/src/config/router.config.js`
- `org-tribe-view/src/views/vis/PreviewFullscreen.vue`
- `org-tribe-view/src/views/vis/SchemeList.vue`
- `org-tribe-view/src/views/vis/PreviewEntry.vue`
- `org-tribe-view/src/views/vis/PageList.vue`
- `org-tribe-view/src/views/vis/modules/VisScreenSchemePreview.vue`

本轮处理：

- 为 `/BigScreenPreview` 新增前端静态路由，直接渲染独立预览页，不再落回 GK 后台壳页面。
- 同步新增 `/vis/preview/fullscreen`，作为 GK 新路径下的独立预览入口。
- `展示方案管理`、`预览概览`、`页面管理 -> 预览关联方案` 三处预览动作已补回 old vis 的确认流：
	- 先弹出“是否设置大屏自动轮播”确认框。
	- 再按选择携带 `autoSetting`、`interval`、`schemeId/info` 跳转到独立预览页。
- `VisScreenSchemePreview` 已补充 `carousel` 展示模式，支持：
	- 按方案轮播间隔自动播放
	- `ESC` 退出
	- 左右方向键切换页面
	- 关闭管理态顶部工具栏

验证结果：

- 使用仓库固定 Node 14 工具链重新执行 `org-tribe-view` 构建成功，并已覆盖到：
	- `org-tribe-system/src/main/resources/static`
	- `org-tribe-system/target/classes/static`
- 浏览器实测：
	- `http://127.0.0.1:9090/BigScreenPreview?...` 已进入新的独立预览页，不再显示后台导航壳。
	- `http://127.0.0.1:9090/vis/preview/fullscreen?...` 已进入新的独立预览页。
	- 独立预览页右上角 `返回` 按钮能正确回到：
		- old vis 路径：`/BigScreen/ExhibitionSchemeList`
		- GK 新路径：`/vis/bigscreen/schemes`
- 当前页面内仍可见 `500`，属于方案预览真实数据接口未成功返回，不再是路由或页面承接缺失。
本轮处理：

- 按 vis 原始菜单 `sys_permission.sql` 中的 `指标库方案 -> /indexLibrary -> target/targetScheme` 语义，在 GK 的 `可视化大屏` 下补回菜单入口。
- 新增菜单：
	- `vis_index_library_menu_20260423 | /vis/index-library | statistics/indexLibrary | VisIndexLibrary`
- 组件直接复用 GK 现有 `statistics/indexLibrary.vue`，避免重复搬运一套指标库方案页。
- 为避免与 GK 现有 `/statistics/indexLibrary` 动态路由撞名，新增菜单使用独立的 `component_name = VisIndexLibrary`。
- 考虑到当前环境下 `selectIndexRelationTree` 首屏请求会返回后端 `500 GENERAL`，已在 `indexLibrary.vue` 的两个初始化树请求上补 `catch` 降级；页面现在会以空树状态打开，而不是抛未捕获异常。

数据库回查结果：

- 远端 GK 开发库已存在：
	- `vis_index_library_menu_20260423 | vis_root_menu_20260423 | /vis/index-library | statistics/indexLibrary | VisIndexLibrary | 4`

浏览器验证结果：

- `可视化大屏` 顶栏下拉中已出现 `指标库方案`。
- 点击后可打开 `/vis/index-library`，标签页标题为 `指标库方案`。
- 刷新该页面后，`pageError` 已为 `0`；当前仍可观察到两个后端 `500` 响应，来源均为 `/indicatorsLib/indexRelationController/selectIndexRelationTree`。
- 由于已做前端降级，页面当前表现为：树区域显示 `暂无数据`，但页面主体可继续停留并操作，不再因未捕获 Promise 拒绝而中断。

### 17. 补齐 vis 旧路径的服务端直刷兼容

已更新：

- `org-tribe-system/src/main/java/org/jeecg/config/WebMvcConfiguration.java`
- `org-tribe-system/src/main/java/org/jeecg/config/ShiroConfig.java`

本轮处理：

- 将旧 vis history 模式路径补入 GK 的前端壳回退规则，新增 `forward:/index.html` 承接：
	- `/gallery`
	- `/indexLibrary`
	- `/BigScreenPreview`
	- `/BigScreen`
	- `/BigScreen/**`
	- `/bigScreen`
	- `/bigScreen/**`
- 同步将上述路径补入 Shiro `anon` 白名单，避免旧路径在进入前端壳前先被 `JwtFilter` 拦截并抛出 `token为空!`。
- 使用 Java 8 对 `org-tribe-system` 执行 `mvn -pl org-tribe-system -DskipTests compile`，编译通过。
- 重启本地 `9090` 的 GK 源码态服务后，旧 vis 路径已按预期落到前端壳页。

浏览器验证结果：

- `http://127.0.0.1:9090/BigScreen/PageList` 直刷后不再出现后端 Whitelabel 或 `token为空!`，页面标题已恢复为 GK 前端壳 `国库数据分析与综合应用平台`。
- `http://127.0.0.1:9090/BigScreenPreview` 直刷后不再出现后端 Whitelabel 或 `token为空!`。
- `http://127.0.0.1:9090/gallery`
  与 `http://127.0.0.1:9090/indexLibrary` 直刷后同样进入前端壳；当前控制台仍可见业务接口 `500`，但这些已属于页面初始化接口问题，不再是 server-side fallback 缺失。
- `http://127.0.0.1:9090/bigScreen/AddTemplate`
  与 `http://127.0.0.1:9090/BigScreen/ExhibitionSchemeList` 旧入口也已回到前端壳页。

### 20. 确认 GK -> vis 转发恢复并完成最小业务回归

已完成：

- 修正 `org-tribe-system/src/main/resources/application-dev.yml` 中开发态 `VIS_SCREEN_BACKEND_URL` 默认值，统一指向 vis 实际开发端口 `http://127.0.0.1:9082`。
- 修正 GK 开发态 `zuul.routes.visScreen.url` 的 YAML 缩进错误，并同步修正运行时 `org-tribe-system/target/classes/application-dev.yml`，保证当前源码态进程实际拿到 `/visScreen/** -> http://127.0.0.1:9082`。
- 重启本地 GK 源码态服务后，再次验证经 GK 转发访问 vis 业务接口。

根因拆分：

- 第一层根因是 GK 开发态默认把 vis 后端指向 `127.0.0.1:7001`，与 vis 当前开发配置 `9082` 不一致。
- 第二层根因是 `zuul.routes.visScreen.url` 被错误缩进到 `sensitiveHeaders` 下方，导致即使默认端口修正后，Zuul 路由 URL 仍未真正生效。

接口级验证结果：

- 使用 `admin / Ysyyrps4` 登录获取 GK token 后，直调 `POST http://127.0.0.1:9090/visScreen/pageInfo/getPage` 已可经 GK 网关成功返回真实页面数据。
- 同一请求直连 vis `http://127.0.0.1:9082/pageInfo/getPage` 与经 GK 转发返回结果一致，说明共享 token、请求头透传和 JSON body 都已恢复正常。

页面级回归结果：

- `http://127.0.0.1:9090/vis/bigscreen/pages` 页面管理已恢复，可正常展示真实页面卡片列表。
- `http://127.0.0.1:9090/vis/bigscreen/schemes` 展示方案管理已恢复，可正常展示真实方案列表。
- `http://127.0.0.1:9090/vis/gallery` 图库已恢复，可正常展示分类 tabs 与真实图库数据。

编辑保存回归结果：

- 已从页面管理进入现有页面编辑器，打开页面 `集成测试页面0423`（`id=dbc930a160374996a85b36091b419da1`）并执行一次“保存”，页面返回列表并提示 `修改成功`。
- 保存后该页面状态会临时变为 `未发布`，因此已立即再次执行一次“发布”，将其恢复为 `已发布`，避免给现有样例数据留下持续副作用。

结论：

- 当前 GK -> vis 的 `/visScreen/**` 网关转发链路已恢复。
- vis 并入 GK 后，页面管理、方案管理、图库三个主入口均已完成最小可用回归。
- implementation plan 中此前遗留的“编辑已有页面并提交保存”验证项已实际关闭。

## 已确认约束

- `BigScreenPreview` 继续保持登录后访问，不做匿名发布，不引入单独签名链接机制。
- vis 运行时查询接口当前没有额外的按用户或机构的数据权限要求。
- vis 的用户、角色、权限治理要继续向 GK 收口，vis 自身只保留业务域能力。

## 验证结果

### 通过

- 源码内 `EXPIRE_TIME * 2 / 1000` 认证写入点已清零。
- GK `application-dev.yml` 无格式错误。
- vis `application-dev.yml` 无格式错误。
- vis `src/main` 源码范围内已无 Nexus OAuth 相关 Java / YAML 引用。
- vis 已新增旧平台入口拦截过滤器，且相关 Java 文件无新增语法错误。

### 受环境阻塞

尝试执行：

```powershell
cd vis_screen/jeecg-boot
mvn -pl jeecg-boot-module-system -am -DskipTests compile
```

结果失败，失败原因不是本轮代码改动，而是当前终端使用的 JDK 与旧版 Lombok / Javac 组合不兼容，报错为模块开放问题：

- `module jdk.compiler does not "opens com.sun.tools.javac.processing"`

这说明当前环境不满足该模块既有的 Java 8 编译前提。后续要做完整 Maven 编译验证，需要切到 Java 8，或为当前 JDK 补 `--add-opens` 兼容参数。

## 下一步建议

1. 继续收口 vis 本地用户/角色/权限相关后端入口，只保留 visualScreen 业务接口。
2. 在 GK 前端内建立 vis API 封装和最小预览页切片，接 `/visScreen/**` 前缀。
3. 在具备 Java 8 的终端中重跑 vis 与 GK 的模块编译验证。
