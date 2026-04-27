# 登录页 user chunk 缓存规避

## 背景

- `Login.vue` 和 `org-tribe-system/src/main/resources/static/js/user.1cdef70e.js` 已经加了 `domianRunURL` 判空保护。
- 但浏览器真实登录时仍持续请求 `/user/undefined/login?...`。
- 直接打开 `http://localhost:9090/js/user.1cdef70e.js?v=...` 可看到服务端已经返回了新逻辑，说明旧问题来自浏览器继续命中同名静态资源缓存。

## 处理

- 将首页静态预取中的 `user` chunk 路径从 `user.1cdef70e.js` 切换为新文件名。
- 同步修改 `app` chunk 的 webpack 映射，并切换首页引用到新的 `app` 文件名，让运行时按新文件名加载 `user` 异步包。
- 保留 `user.1cdef70e.js` 内容修复，同时新增同内容的新文件名副本，强制浏览器拉取未缓存的登录 chunk。

## 验证点

- 登录页应加载新的 `user` chunk URL，而不是继续使用旧缓存的 `user.1cdef70e.js`。
- 使用测试账号重新登录后，不再出现 `/user/undefined/login?...` 的 JSONP 请求。