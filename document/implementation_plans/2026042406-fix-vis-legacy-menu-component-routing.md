# vis 旧菜单组件名兼容修复

## 背景

在继续收口 vis 菜单并入 `org-tribe-view` 的过程中，发现 vis 原始菜单 SQL 仍大量使用旧组件名：

- `BigScreen/TemplateList`
- `BigScreen/PageList`
- `BigScreen/ExhibitionSchemeList`
- `BigScreen/AddTemplate`

而主前端已经把对应页面统一落在 `src/views/vis/` 下。

主系统动态菜单路由解析代码位于 `org-tribe-view/src/utils/util.js`，原逻辑会直接把后台返回的 `item.component` 拼接为：

- `views/${item.component}.vue`
- 或 `components/${item.component}.vue`

这意味着如果数据库里仍然保留旧的 `BigScreen/*` 组件名，动态菜单点击后会尝试解析不存在的 `views/BigScreen/*.vue`，从而导致菜单页面打不开。

## 根因判断

本次采取的是本地可证伪的最小判断：

1. `src/views/vis/` 中已经具备 vis 页面文件。
2. 动态路由解析器没有任何旧组件名到新组件目录的映射。
3. 旧 vis SQL 仍存在 `BigScreen/*` 组件名。

因此可以得到局部结论：

> 即使页面源码已经迁入主工程，只要菜单数据还残留旧组件名，主前端动态路由仍可能解析失败。

## 代码修改

修改文件：`org-tribe-view/src/utils/util.js`

新增 `normalizeLegacyComponent(component)`，对 vis 遗留菜单组件名做归一化映射：

- `BigScreen/TemplateList -> vis/TemplateList`
- `BigScreen/PageList -> vis/PageList`
- `BigScreen/ExhibitionSchemeList -> vis/SchemeList`
- `BigScreen/AddTemplate -> vis/PageList`

并在 `generateChildRouters()` 中，不再直接使用 `item.component`，而是先使用归一化后的 `normalizedComponent` 再拼接动态 `require()` 路径。

这样即使数据库菜单还未完全切换到新的 `vis/*` 组件名，也可以继续解析到已经迁入主工程的页面组件。

## 为什么先做代码兼容

当前 9090 本地运行态在本轮排查时未监听，无法立即通过登录接口与 `/sys/permission/getUserPermissionByToken` 在线确认数据库中的实际菜单组件名。

在这种情况下，先做前端兼容层是更小且更稳妥的收口方式：

1. 不依赖数据库已经完成清洗。
2. 不阻塞现有 vis 页面继续在主系统菜单中打开。
3. 后续即使数据库再把组件名改成 `vis/*`，本次兼容层也不会影响新菜单解析。

## 验证情况

已完成：

1. `org-tribe-view/src/utils/util.js` 静态错误检查通过，无新增报错。
2. 已确认归一化映射函数和调用点已落入源码。

未完成：

1. 9090 运行态菜单接口验证。
2. 将本次源码改动同步到运行中的 `target/classes/static` 前端包。
3. 基于真实菜单点击做页面级联调验证。

## 后续建议

下一步应优先做以下两项：

1. 恢复 9090 服务，登录后拉取实际菜单，确认数据库当前是否仍返回 `BigScreen/*`。
2. 重新构建或同步前端静态资源，使本次 `util.js` 兼容逻辑进入运行态，再验证旧菜单能否正常打开 vis 页面。