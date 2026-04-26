# 2026042413 修复 visScreen 网关路由与指标库方案旧菜单兼容

## 问题

- `POST /visScreen/schemeInfo/getPage` 与 `POST /visScreen/pageInfo/getPage` 在 GK 9090 侧不可用。
- “指标库方案”菜单在部分环境下仍落到旧路径，导致没有进入当前 vis 方案管理标签页。
- 现要求进一步统一为所有 vis 相关接口请求都挂在 `/vis/` 下，并将网关路由名也收敛为 `vis`。

## 定位

- 浏览器登录 9090 后，左侧菜单“可视化大屏 -> 指标库方案”实际仍来自旧菜单路径兼容链；当前静态 bundle 不包含 `/statistics/indexLibrary -> /vis/index-library` 时，会落到旧入口而不是站内标签页。
- 本机 vis 后端实际监听在 `9082`，并且直接访问 `http://127.0.0.1:9082/schemeInfo/getPage` 可正常返回分页数据，说明后端本身可用。
- `org-tribe-system` 当前 `/visScreen/**` 路由配置使用 `serviceId: visScreen`，但本地联调场景实际需要直连 `9082`，否则 9090 侧请求会继续落到错误的转发链上，浏览器中表现为 `404` 或 `token非法无效!`。
- `org-tribe-view/src/utils/util.js` 已兼容 `/indexLibrary -> /vis/index-library`，但没有兼容仍可能残留在 `sys_permission` 里的 `/statistics/indexLibrary`。
- 新一轮排查确认 `/vis/api/**` 不会被 `WebMvcConfiguration` 中的 `/vis/** -> forward:/index.html` 抢走；直接访问 `POST /vis/api/schemeInfo/getPage` 与 `POST /vis/api/pageInfo/getPage` 返回的是鉴权错误 `token为空!`，不是页面 HTML。
- 真正导致浏览器里仍然访问旧 `/visScreen/**` 的原因，不是源码未改，而是 `mvn spring-boot:run` 只会在 `generate-resources` 阶段构建 `org-tribe-view/dist`，不会执行 `prepare-package` 阶段的静态资源复制，导致运行中的 9090 仍然服务旧的 `static` 目录内容。
- 本地启动若使用非 Java 8，BES 9.5.5 在停止或初始化阶段会触发额外兼容问题；本次联调必须显式切到 Java 8。

## 修改

- 将 `org-tribe-system/src/main/resources/application-dev.yml` 与当前 `org-tribe-system/target/application-dev.yml` 中的 `zuul.routes.visScreen` 改为 `url: ${VIS_SCREEN_BACKEND_URL:http://127.0.0.1:9082}`，让 GK 9090 直接转发到本机正在运行的 vis 后端。
- 扩展前端动态菜单归一化：`/statistics/indexLibrary` 也收敛到 `/vis/index-library`，继续复用 `vis/SchemeList`。
- 将 GK 开发网关路由改为 `vis.path=/vis/api/**`，并直连 `${VIS_SCREEN_BACKEND_URL:http://127.0.0.1:9082}`。
- 将 vis 前端接口封装、页面内硬编码 URL、开发代理统一迁移到 `/vis/api/**`，不再使用 `/visScreen/**`。
- 调整 `ShiroConfig`，将 `/vis/api/**` 从原有匿名 `/vis/**` 页面规则中单独分流，保证 API 走 `jwt`，少数预览查询接口继续保留匿名访问。
- 使用固定 Node 14 重新构建 `org-tribe-view`，并将产物覆盖到 `org-tribe-system/src/main/resources/static` 与 `org-tribe-system/target/classes/static`。
- 本地启动 `vis-screen-backend` 与 `org-tribe-system` 时显式切换到 Java 8，并关闭 Eureka 自动注册，避免本机无注册中心时阻塞联调。

## 结果

- 浏览器实测登录 9090 后，“可视化大屏 -> 指标库方案” 已进入站内路由 `/vis/index-library`，说明菜单旧路径兼容与标签页打开方式已经恢复。
- `vis-screen-backend` 已在本机 `9082` 成功启动，`org-tribe-system` 已在本机 `9090` 成功启动。
- 直连 `POST /vis/api/schemeInfo/getPage` 与 `POST /vis/api/pageInfo/getPage` 均返回 `token为空!`，证明请求已命中后端接口链路，而不是被 SPA 页面转发吞掉。
- 浏览器刷新 `http://127.0.0.1:9090/vis/index-library` 后，请求已变为 `/vis/api/schemeInfo/getPage` 与 `/vis/api/pageInfo/getPage`，并能正常展示方案数据。
- 浏览器抽样验证 `http://127.0.0.1:9090/vis/gallery` 时，请求已变为 `/vis/api/gallery/getPage` 与 `/vis/api/bussType/getAll`，说明相邻 vis 页面也已经切到新前缀。

## 额外说明

- 如果继续使用 `mvn spring-boot:run` 方式联调 `org-tribe-system`，前端每次重新 build 后仍需要把 `org-tribe-view/dist` 手动同步到 `org-tribe-system/src/main/resources/static` 与 `org-tribe-system/target/classes/static`，否则浏览器会继续命中旧 bundle。