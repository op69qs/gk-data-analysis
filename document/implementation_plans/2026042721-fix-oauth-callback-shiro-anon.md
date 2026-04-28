# 2026-04-27 OAuth Callback Shiro Anonymous Access

## 背景

- 门户登录后跳转到 `http://localhost:9090/oauth/callback?code=...`。
- system 模块运行后，该地址被 Shiro JWT 过滤器拦截，返回 `token为空!`，导致前端回调页无法执行后续 `OAuthLogin`。

## 变更

- 文件：`org-tribe-system/src/main/java/org/jeecg/config/ShiroConfig.java`
- 在 Shiro 白名单新增：
  - `filterChainDefinitionMap.put("/oauth/callback", "anon");`
- 文件：`org-tribe-system/src/main/java/org/jeecg/config/WebMvcConfiguration.java`
- 在 `addViewControllers` 新增：
  - `registry.addViewController("/oauth/callback").setViewName("forward:/index.html");`

## 原因

- `/oauth/callback` 是前端 SPA 路由，不应要求已登录 token。
- 前端页面加载后会调用后端真实回调接口 `/sys/oauth/callback` 完成 code 换 token。
- 该路由还需要后端转发到 `index.html`，否则会返回 404，无法进入 Vue 回调页面逻辑。

## 验证思路

1. 启动 `org-tribe-system`。
2. 打开 `http://localhost:9090/oauth/callback?code=xxx&state=yyy`。
3. 预期不再出现 `token为空!` 的 500 页面，改为前端回调页面逻辑。
4. 再检查 `/sys/oauth/callback` 返回是否符合门户 token 交换结果。
