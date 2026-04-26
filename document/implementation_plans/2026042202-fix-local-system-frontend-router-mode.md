# 修复本地信创版 system 前端路由模式

## 背景

- 本地信创版 `deploy-package` 启动后，`admin / Ysyyrps4` 可以登录，菜单权限接口也能正常返回。
- 但点击叶子菜单后，浏览器 URL 会落成 `/dashboard/analysis#/...`，页面主体只剩首页壳子，无法按基线版完成菜单级验证。

## 判定过程

### 1. 先排除菜单数据问题

- 直接调用本地 `http://127.0.0.1:9090/sys/permission/getUserPermissionByToken` 获取菜单树。
- 返回结果中的菜单 `path` 均为正常 history 路径，例如：
  - `/isystem/permission`
  - `/statistics/dataSource`
  - `/statistics/dimensionTable`
- 因此问题不在数据库中的 `sys_permission.url`，也不在后端返回的菜单树字段。

### 2. 再验证本地网关对 history 路径的处理

- 对以下地址直接做 HTTP 请求：
  - `http://127.0.0.1:9090/`
  - `http://127.0.0.1:9090/user/login`
  - `http://127.0.0.1:9090/isystem/permission`
  - `http://127.0.0.1:9090/dashboard/analysis`
- 结果只有根路径 `/` 返回前端首页；其余 history 路径全部直接返回：

```json
{"status":500,"message":"Token失效，请重新登录"}
```

- 说明当前本地 `org-tribe-system` 并没有对 Vue history 路由做前端 fallback，任何非根路径都会下沉到后端并被鉴权/异常处理拦截。

### 3. 源码定位

- 在 `org-tribe-view/src/router/index.js` 中确认前端被配置为：

```js
mode: 'history'
```

- 这与当前本地运行环境不兼容。

## 处理方式

### 1. 调整前端路由模式

- 将 `org-tribe-view/src/router/index.js` 中的路由模式从 `history` 改为 `hash`。
- 这样前端所有菜单跳转都会回到 `/#/...` 形式，不再要求后端支持 history fallback。

### 2. 调整生产静态资源根路径

- 原 `org-tribe-view/vue.config.js` 中 `publicPath` 为 `./`。
- 当浏览器地址被带到 `/user/login` 这类路径时，Webpack 懒加载 chunk 会被拼成：
  - `/user/js/...`
  - `/user/css/...`
- 浏览器实测已出现这类 404，因此将 `publicPath` 调整为 `/`，让生产资源统一走根路径绝对引用。

### 3. 使用固定 Node 14 重建前端

- 全局 Node 23 无法稳定构建这套旧 Vue 2 / Webpack 4 工程。
- 复用仓库内已验证过的固定工具链：
  - `org-tribe-system/target/frontend/node/node.exe`
  - Node `v14.21.3`
  - npm `8.19.4`
- 在该环境下执行 `npm run build`，构建成功。

### 4. 将新静态资源覆盖到 deploy-package 运行 jar

- 当前本地 9090 使用的是 `deploy-package/app/org-tribe-system-2.1.0.jar`。
- 检查 jar 结构后确认前端资源位于：
  - `static/index.html`
  - `static/css/**`
  - `static/js/**`
  - 以及其余 `static/**` 资源目录
- 使用 JDK8 自带 `jar uf` 将 `org-tribe-view/dist` 覆盖写入该 jar 的 `static/**` 目录，并重启本地 `deploy-package` 服务。
- 期间曾尝试直接用 .NET Zip 更新 jar，导致 `invalid LOC header (bad signature)`；已第一时间用备份 jar 恢复，再改用 `jar uf` 安全更新。

## 结论

- 本地信创版“点击菜单后只剩首页壳子”的根因不是菜单数据，也不是鉴权接口本身，而是：
  - 前端被打成了 `history` 路由；
  - 当前本地 `org-tribe-system` 运行环境没有为 history 路由提供前端 fallback。
- 同时，生产构建的 `publicPath=./` 也会让懒加载资源在 `/user/login` 这类地址下错误请求 `/user/js/...` 和 `/user/css/...`，需要一起改成根路径绝对引用。
- 修复后已验证：
  - 浏览器地址回到 `http://127.0.0.1:9090/?_ts=...#/dashboard/analysis` 这种纯 hash 形式；
  - 打开菜单管理后地址稳定为 `#/isystem/permission`；
  - 页面主体可以正常渲染表格，不再退化成只剩首页壳子。