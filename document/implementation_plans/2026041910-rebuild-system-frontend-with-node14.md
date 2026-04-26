# 使用固定 Node 14 重建并替换 system 前端静态包

## 背景

- `org-tribe-view` 源码中存在以下页面：
  - `statistics/dimensionTable.vue`
  - `report/industryReport.vue`
  - `report/enterpriseReport.vue`
  - `report/enterpriseRankReport.vue`
- 但 `org-tribe-system` 当前运行中的静态前端包缺少上述 4 个页面模块，导致菜单存在但点击无响应。
- 本机全局 Node 版本为 23，直接执行 `npm run build` 会触发 Webpack 4 / OpenSSL 兼容问题，不能用于该 Vue 2 老项目构建。

## 处理方式

- 复用 `org-tribe-system/pom.xml` 中已经固定好的前端工具链：
  - Node `v14.21.3`
  - npm `8.19.4`
- 实际使用目录为：`org-tribe-system/target/frontend/node`

## 实施过程

### 1. 使用固定 Node 14 安装依赖

- 先确认 `org-tribe-system/target/frontend/node/node.exe` 可用。
- 将该目录临时放到 PATH 最前面，避免 `npm` 子进程误用系统 Node 23。
- 重新执行：

```powershell
npm ci --registry=https://registry.npmmirror.com --legacy-peer-deps
```

### 2. 使用固定 Node 14 构建前端

- 在同样的 PATH 环境下执行：

```powershell
npm run build
```

- 构建成功，生成新的 `org-tribe-view/dist`。
- 构建过程中仅出现旧项目常见的 CSS 顺序和资源体积 warning，不影响产物输出。

### 3. 替换 system 静态前端包

- 将新的 `org-tribe-view/dist` 镜像覆盖到以下两个目录：
  - `org-tribe-system/src/main/resources/static`
  - `org-tribe-system/target/classes/static`

这样同时覆盖源码静态资源和当前类路径下的运行静态资源。

## 验证结果

- 新静态包中的 chunk 已包含以下模块：
  - `./views/statistics/dimensionTable.vue`
  - `./views/report/industryReport.vue`
  - `./views/report/enterpriseReport.vue`
  - `./views/report/enterpriseRankReport.vue`
- 对应文件已在以下位置验证命中：
  - `org-tribe-system/src/main/resources/static/js/chunk-b2a5147e.5bff8720.js`
  - `org-tribe-system/target/classes/static/js/chunk-b2a5147e.5bff8720.js`

## 结论

- 这次问题不是菜单配置错误，也不是源码缺页面，而是 `system` 运行中的静态前端包过旧。
- 现已使用仓库内固定的 Node 14 重新构建前端，并完成 `system` 静态资源替换。
- 若浏览器仍显示旧结果，优先执行强制刷新，必要时重启当前 `org-tribe-system` 进程以清掉旧资源缓存。

## 2026-04-19 补充修正

- 替换成新前端包后，系统布局恢复成了源码默认的顶部横向菜单。
- 进一步确认 `org-tribe-view/src/defaultSettings.js` 当前默认配置为 `layout: 'topmenu'`，这会让未命中旧缓存配置的浏览器直接以顶部导航启动。
- 同时，浏览器一旦按该默认值启动，`DEFAULT_LAYOUT_MODE` 会被写入本地存储，后续刷新仍会保持顶部导航。
- 因此追加两项最小修正：
  - 将默认布局从 `topmenu` 调整为 `sidemenu`；
  - 在 `main.js` 启动阶段把已缓存的 `topmenu` 自动纠正为 `sidemenu`，避免要求用户手动清理本地存储。
- 修正后需要重新构建 `org-tribe-view/dist`，并再次覆盖 `org-tribe-system` 的静态资源目录。

## 2026-04-20 菜单位置回退

- 用户要求将菜单位置改回之前的顶部横向布局。
- 因为 2026-04-19 的修正不仅改了默认值，还会在启动阶段把缓存中的 `topmenu` 强制改成 `sidemenu`，所以本次回退也需要成对处理：
  - 将默认布局从 `sidemenu` 调整回 `topmenu`；
  - 在 `main.js` 启动阶段把已缓存的 `sidemenu` 自动纠正为 `topmenu`，确保浏览器无需手动清理本地存储即可恢复顶部菜单。
- 回退后同样需要重新构建 `org-tribe-view/dist`，并重新覆盖 `org-tribe-system` 的静态资源目录。