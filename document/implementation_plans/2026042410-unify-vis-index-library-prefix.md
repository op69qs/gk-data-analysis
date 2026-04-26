# vis 指标库入口统一到 /vis 前缀

## 背景

`/indexLibrary` 是原始 vis 的历史路径，`/vis/index-library` 是并入 GK 后新增的统一路径。
两者之前都直接加载同一个页面，导致代码层面看起来像是维护了两套 vis 入口。

## 本次处理

1. 前端常量路由中保留 `/vis/index-library` 作为正式入口。
2. `/indexLibrary` 不再直接挂载页面，改为重定向到 `/vis/index-library`，并保留 query/hash。
3. 动态菜单路由生成阶段增加旧路径归一：
   - `/indexLibrary` 统一转为 `/vis/index-library`
4. 保留后端对 `/indexLibrary` 的匿名放行和 SPA 转发，仅用于兼容历史外链和旧收藏。

## 修改文件

- `org-tribe-view/src/config/router.config.js`
- `org-tribe-view/src/utils/util.js`
- `org-tribe-system/target/classes/static/js/app.f0e92721.js`
- `org-tribe-system/target/classes/static/js/chunk-vendors.6cfa37ad.js`
- `org-tribe-system/src/main/resources/static/js/app.d7cee0c2.js`
- `org-tribe-system/src/main/resources/static/js/chunk-vendors.6cfa37ad.js`

## 结果

- 代码语义上，vis 指标库统一到 `/vis/index-library`。
- 历史地址 `/indexLibrary` 仍可访问，但只承担兼容跳转职责。
- 未登录直接访问旧地址时，登录页 `redirect` 已收敛为 `/vis/index-library`。

## 验证

- `get_errors` 检查：`router.config.js`、`util.js` 无错误。
- 浏览器验证：
  - 已登录场景下，`/vis/index-library` 正常进入 `VisSchemeList`
  - 旧地址 `/indexLibrary` 已不再作为独立页面入口保留
