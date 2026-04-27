# 2026042701 — GK-Nexus OAuth2.0 单点登录接入

## 背景

依据 `document/subsystem_oauth_guide.md`，将 org-tribe-system / org-tribe-view 接入 GK-Nexus OAuth2.0 授权码模式单点登录。

---

## 修改文件清单

### 后端（org-tribe-system）

| 文件 | 类型 | 说明 |
|------|------|------|
| `src/main/java/org/jeecg/modules/oauth/controller/NexusOAuthController.java` | 新增 | OAuth 回调端点 `/sys/oauth/callback`，交换 code → token，查本地用户，生成 JEECG JWT |
| `src/main/java/org/jeecg/config/HttpClientConfig.java` | 新增 | 注册 `RestTemplate` Bean |
| `src/main/java/org/jeecg/config/ShiroConfig.java` | 修改 | 新增 `/sys/oauth/callback` 为 anon 规则（不拦截） |
| `src/main/resources/application-dev.yml` | 修改 | 新增 `gk-nexus.oauth.*` 配置段 |

### 前端（org-tribe-view）

| 文件 | 类型 | 说明 |
|------|------|------|
| `src/views/user/OAuthCallback.vue` | 新增 | 回调页：校验 state、dispatch OAuthLogin、处理多部门弹框、跳转 dashboard |
| `src/config/router.config.js` | 修改 | constantRouterMap 顶层新增 `/oauth/callback` 路由 |
| `src/permission.js` | 修改 | whiteList 增加 `/oauth/callback`（不强制跳转登录）|
| `src/store/modules/user.js` | 修改 | 新增 `OAuthLogin` action，调 GET `/sys/oauth/callback` |
| `src/api/login.js` | 修改 | 新增 `oauthCallback(code)` 函数 |
| `src/views/user/Login.vue` | 修改 | 在提交按钮下方新增"统一门户登录"按钮及 `handleNexusLogin` 方法 |
| `public/index.html` | 修改 | 新增 `nexusBffUrl` / `nexusClientId` 全局配置 |

---

## 实现要点

### 授权码流程

```
用户点击"统一门户登录"
  → 前端存 state 到 sessionStorage
  → window.location → GK-Nexus /api/auth/oauth/authorize?...
  → GK-Nexus 授权后回调 /oauth/callback?code=xxx&state=xxx
  → OAuthCallback.vue 校验 state（CSRF 防护）
  → dispatch OAuthLogin → GET /sys/oauth/callback?code=xxx
  → 后端 NexusOAuthController:
      1. POST GK-Nexus /api/auth/oauth/token 换取 access_token
      2. JWT.decode(accessToken) 取 username claim
      3. getUserByName + checkUserIsEffective
      4. JwtUtil.sign + Redis 缓存（同 CasClientController 逻辑）
      5. 返回 { token, userInfo, departs, multi_depart }
  → 前端存 ACCESS_TOKEN / USER_INFO 到 Vue.ls
  → 跳转 /dashboard/analysis
```

### 与 CAS 实现的对比

NexusOAuthController 完全镜像 CasClientController：
- 相同的返回结构（token / userInfo / departs / multi_depart）
- 相同的 Redis 缓存逻辑
- 差异：code→token 交换用 RestTemplate POST（取代 CAS p3/serviceValidate XML 解析）

### 安全决策

- `client_secret` 通过环境变量 `NEXUS_CLIENT_SECRET` 注入，不硬编码
- `state` 发起前存 sessionStorage，回调时比对后立即删除（CSRF 防护）
- JWT 不做 JWKS 验签（token 来自服务端直接换取，security boundary 由 HTTPS + client_secret 保障）
- redirect_uri 动态取 `window.location.origin + '/oauth/callback'`，无硬编码

---

## 配置项

`application-dev.yml` 新增（生产环境通过环境变量覆盖 client-secret）：

```yaml
gk-nexus:
  oauth:
    token-url: http://localhost:3000/api/auth/oauth/token
    client-id: gk-data-analysis
    client-secret: ${NEXUS_CLIENT_SECRET:changeme}
    redirect-uri: http://localhost:9090/oauth/callback
```

`public/index.html` 新增（可按环境修改）：

```js
window._CONFIG['nexusBffUrl'] = 'http://localhost:3000';
window._CONFIG['nexusClientId'] = 'gk-data-analysis';
```

---

## 联调验证步骤

1. 启动后端，`curl http://localhost:9090/sys/oauth/callback?code=test` 返回 JSON 错误（非 404 / Shiro 重定向）
2. 前端访问 `/user/login`，页面显示"统一门户登录"按钮
3. 点击按钮后 URL 跳转含正确 `client_id=gk-data-analysis`、`redirect_uri`、`state` 参数
4. 完成 GK-Nexus 授权 → 回调 `/oauth/callback?code=xxx` → 跳转 dashboard（需门户联调）
5. 测试 state 篡改：手动改 URL 的 state → 回调页显示"登录状态验证失败"，不执行登录
