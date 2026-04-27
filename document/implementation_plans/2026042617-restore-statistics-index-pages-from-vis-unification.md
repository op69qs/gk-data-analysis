# 恢复旧指标查询页与 vis 指标库方案页的分离

## 问题

- 当前主前端里，原有菜单“指标库/指标查询”与 vis 菜单“指标库方案”会打开同一内容页。
- 这与旧 vis 的原始页面语义不一致。旧 vis 的“指标库方案”对应的是 `BigScreen/ExhibitionSchemeList.vue`，不是 GK 原有的 `statistics/indexLibrary.vue`。

## 根因

本次问题不是旧 vis 本来就和指标查询同页，而是后续兼容改动把两条链路错误合并了：

1. `org-tribe-view/src/utils/util.js` 将 `statistics/indexLibrary`、`statistics/schemeIndex` 强制映射成 `vis/SchemeList`，导致后台即使返回旧统计页组件，也会在动态路由阶段被改写成 vis 页面。
2. `org-tribe-view/src/views/statistics/schemeIndex.vue` 被整体替换成了 `vis/SchemeList` 包装页。
3. `org-tribe-view/src/views/statistics/indexLibrary.vue` 的模板头部被误插入了 `vis/SchemeList` 包装片段，破坏了原始页面结构。

## 修改

- 移除 `util.js` 中对 `statistics/indexLibrary`、`statistics/schemeIndex` 的组件归一化。
- 移除 `util.js` 中对 `/statistics/indexLibrary`、`/statistics/schemeIndex` 到 `/vis/index-library` 的路径归一化。
- 移除 `generateLegacyRedirectRouters()` 中对 `/statistics/indexLibrary`、`/statistics/schemeIndex` 到 `/vis/index-library` 的强制重定向。
- 将 `schemeIndex.vue` 恢复为原始“公共方案查询”页面实现。
- 将 `indexLibrary.vue` 头部误插入的 vis 包装片段替换回原始维度/账期/指标树区域。
- 使用 `node node_modules/@vue/cli-service/bin/vue-cli-service.js build` 绕过损坏的 npm 环境重新构建前端，并把 `dist` 同步到 `org-tribe-system/src/main/resources/static` 与 `org-tribe-system/target/classes/static`。

## 验证

- `get_errors` 检查通过：
  - `org-tribe-view/src/utils/util.js`
  - `org-tribe-view/src/views/statistics/schemeIndex.vue`
  - `org-tribe-view/src/views/statistics/indexLibrary.vue`
- 全局搜索确认已不存在以下错误合并残留：
  - `statistics/indexLibrary -> vis/SchemeList`
  - `statistics/schemeIndex -> vis/SchemeList`
  - `<vis-scheme-list />` 出现在 `statistics/*` 页面中
- 额外尝试执行 `org-tribe-view` 的 `npm run lint`，但当前本机 Node/npm 环境损坏，报错 `Cannot find module ... npm-prefix.js`，因此未完成命令级 lint 验证。
- 进一步验证新构建产物 `app.b3e2ac11.js` 后确认，旧 `statistics/* -> vis` 的组件归一和路径归一已消失，仅保留 `/indexLibrary -> /vis/index-library` 这一条 vis 历史路径兼容。

## 结论

- 之前把两个入口做成同页是兼容逻辑和页面源码一起改错了，不是“旧 vis 本来就是这个内容”。
- 现在旧“指标库/指标查询”会回到自身统计页实现，vis 下“指标库方案”继续走 `vis/SchemeList`。