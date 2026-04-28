# 修复 OAuth 回调被旧 token 拦截

## 问题现象
- 从门户跳转到 `/oauth/callback?code=...` 后，前端回到登录页。
- 后端日志出现 `Token失效，请重新登录!`，随后有 `/sys/logout` 成功日志。

## 根因
- 前端 `router.beforeEach` 在本地存在 token 时，会先执行 `GetPermissionList`。
- 当本地 token 已过期时，`GetPermissionList` 失败，触发 `Logout`，导致还没执行 OAuth 回调换 token 就被踢回登录页。
- 虽然 `/oauth/callback` 在白名单中，但白名单逻辑仅在“无 token”分支生效；“有 token”分支仍会走权限拉取。

## 修改内容
- 文件：`org-tribe-view/src/permission.js`
- 在 `router.beforeEach` 最前面增加特殊分支：
  - 当 `to.path === '/oauth/callback'` 时直接 `next()` 并 `return`。

## 影响范围
- 仅影响 OAuth 回调页进入流程。
- 普通登录、权限初始化、其他受保护路由流程不变。

## 验证建议
1. 清理浏览器缓存后，先手工写入一个过期 token（或使用历史 token）。
2. 从门户点击跳转到 `/oauth/callback?code=...`。
3. 预期：不再先触发 `GetPermissionList` 导致登出；回调页可继续调用 `/sys/oauth/callback` 完成 code 换 token。
