# 旧 vis 路径重定向补齐

## 背景

- 旧菜单组件名已经能在动态菜单装配时映射到 `vis/SchemeList`。
- 但直接访问旧路径 `/statistics/indexLibrary`、`/statistics/schemeIndex` 仍然会失败。
- 前端补齐旧路径隐藏路由后，浏览器运行态进一步确认真正失败点不是 `/404`，而是后端 history deep-link 先被 Shiro `JwtFilter` 拦截，返回 `token为空!`。
- 这说明要同时补两层兼容：前端动态路由保留旧路径跳转入口，后端还要让这些旧入口先匿名放行并转发到 `index.html`。

## 本次修改

- 在 `org-tribe-view/src/utils/util.js` 的 `generateIndexRouter` 中新增 `generateLegacyRedirectRouters()`。
- 为以下旧路径补充隐藏重定向路由：
  - `/gallery` -> `/vis/gallery`
  - `/indexLibrary` -> `/vis/index-library`
  - `/statistics/indexLibrary` -> `/vis/index-library`
  - `/statistics/schemeIndex` -> `/vis/index-library`
  - `/BigScreen` -> `/vis/bigscreen`
  - `/bigScreen/TemplateList` -> `/vis/bigscreen/templates`
  - `/BigScreen/PageList` -> `/vis/bigscreen/pages`
  - `/BigScreen/ExhibitionSchemeList` -> `/vis/bigscreen/schemes`
  - `/bigScreen/AddTemplate` -> `/vis/bigscreen/pages/editor`
- 在 `org-tribe-system/src/main/java/org/jeecg/config/WebMvcConfiguration.java` 中，为同一批 legacy vis 深链接补充 `forward:/index.html`，把 history 模式旧地址交还给前端 SPA。
- 在 `org-tribe-system/src/main/java/org/jeecg/config/ShiroConfig.java` 中，为同一批 legacy vis 深链接补充 `anon` 放行，避免请求在进入 Spring MVC 前就被 `JwtFilter` 以 `token为空!` 拦截。

## 验证

- `get_errors` 检查 `org-tribe-view/src/utils/util.js` 无错误。
- `node C:\nvm4w\nodejs\node_modules\npm\bin\npm-cli.js run build` 构建通过，仅保留原有 CSS 顺序和体积 warning。
- `mvn -DskipTests compile` 通过，`WebMvcConfiguration.java` 改动已进入后端编译产物。
- `mvn -DskipTests compiler:compile` 通过，`ShiroConfig.java` 改动已进入后端编译产物。
- 使用 Java 8 启动 `org-tribe-system` 后，运行态确认 `/statistics/indexLibrary` 不再返回 Whitelabel 500 `token为空!`，而是进入 `http://127.0.0.1:9090/user/login?redirect=%2Fstatistics%2FindexLibrary`。
- 同样确认 `/statistics/schemeIndex` 会进入 `http://127.0.0.1:9090/user/login?redirect=%2Fstatistics%2FschemeIndex`。

## 结论

- 现在旧菜单路径不仅能在菜单装配阶段映射到新页面，也能在运行态先通过后端匿名放行与 SPA 转发，再由前端旧路径 redirect 进入 vis 新入口。
- 这样数据库菜单残留旧 path、手工收藏旧地址、或从旧链接进入时，都不会再直接落到 `/404` 或后端 `token为空!` 500。