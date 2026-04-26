# 修正 visScreen 开发态网关默认端口

## 背景

继续收口 vis 并入 GK 的未完成事项时，实际打开 GK 的“页面管理”页发现浏览器控制台报 500，页面列表退化为“暂无页面数据”。

这不是前端列表渲染问题，而是 `/visScreen/**` 网关链路在开发态直接转发失败。

## 根因

排查链路如下：

1. 页面管理页首屏只会调用 `POST /visScreen/pageInfo/getPage`。
2. 使用 GK 登录 token 直调该接口，可稳定复现 500：`Connect to 127.0.0.1:7001 failed: Connection refused`。
3. 检查 GK 网关配置发现 `org-tribe-system/src/main/resources/application-dev.yml` 中 `VIS_SCREEN_BACKEND_URL` 默认值仍是 `http://127.0.0.1:7001`。
4. 检查 vis 实际开发配置 `vis_screen/jeecg-boot/jeecg-boot-module-system/src/main/resources/application-dev.yml`，其服务端口为 `9082`。

因此，当前问题的根因是 GK 开发态 vis 网关默认端口配置错误，而不是页面管理代码本身。

后续复测又发现一个叠加问题：`visScreen` 路由块里 `url` 一度被误缩进到 `sensitiveHeaders` 子节点下，导致 Zuul 即便读取到 `9082` 默认值，也没有按预期把它当作路由地址生效。

## 处理

将 GK 开发态配置改为：

- `VIS_SCREEN_BACKEND_URL` 默认值：`http://127.0.0.1:9082`
- `zuul.routes.visScreen.url` 与 `path`、`sensitiveHeaders` 保持同级 YAML 缩进

这样在本地未额外注入环境变量时，`/visScreen/**` 会直接落到 vis 的实际 dev 端口。

## 影响范围

这次修复会同时恢复所有依赖 `/visScreen/**` 的 GK 内嵌 vis 页面：

- 页面管理
- 方案管理
- 图库管理
- 预览相关查询
- 页面编辑器中的图库/树形维度查询

## 验证计划

1. 重启或重新加载 GK 开发服务配置。
2. 使用 `admin / Ysyyrps4` 登录 GK。
3. 重新访问：
   - `/vis/bigscreen/pages`
   - `/vis/bigscreen/schemes`
   - `/vis/gallery`
4. 确认不再出现 `127.0.0.1:7001 connection refused`。
5. 确认 `/visScreen/pageInfo/getPage` 不再因 Zuul 路由配置层级错误返回 `500 GENERAL`。
6. 如 vis 后端已启动，再继续补“编辑已有页面并提交保存”的在线回归。