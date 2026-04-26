# vis 模块接入 GK 平台联调整理

## 变更目标

- vis-screen-backend 使用 Eureka 注册服务，注册名保持为 `vis`。
- org-tribe-system 通过 Eureka 发现 vis 服务，不再依赖写死转发目标地址。
- GK 前端保持所有 vis 访问路径挂载在 `/vis/**` 下。
- vis 后端兼容 GK 平台签发的 `X-Access-Token`，并复用平台登录态。

## 已完成改造

### 1. 服务发现与网关路由

- org-tribe-system 在 `application-dev.yml` 中保留 `/vis/api/**`、`/visScreen/**` 的手写路由，并改为 `serviceId: vis`。
- 增加 `zuul.ignored-services: [vis, VIS]`，避免 Eureka 自动路由抢占 `/vis/**` 的前端深链。
- vis-screen-backend 在 `bootstrap.yml` 中显式按 IP 注册 Eureka：
  - `prefer-ip-address: true`
  - `hostname/ip-address` 使用 `spring.cloud.client.ip-address`
  - `instance-id` 使用 `ip:service:port`

### 2. 前端 vis 路径与静态资源

- org-tribe-system 增加 `/vis`、`/vis/**` 到 `index.html` 的 forward，确保 `/vis/preview` 这类深链能落到 SPA。
- org-tribe-view 生产构建静态资源前缀从相对路径改为根路径，避免 `/vis/js/...` 命中 HTML。
- 前端域名配置改为 same-origin，所有 vis 请求继续走 `/vis/api/**`。

### 3. 统一认证兼容

- vis-screen-backend 的 `ShiroRealm` 增加平台 JWT fallback：
  - Redis token 校验失败时，不再直接拒绝。
  - 回退为仅校验 JWT 过期时间与用户存在性。
- vis-screen-backend Redis 配置改为只读取 `VIS_SPRING_REDIS_PASSWORD`，避免被机器全局 `SPRING_REDIS_PASSWORD` 污染。
- 本地启动 vis 时通过 JVM 参数清空 Redis 密码，确认共享 Redis 不再出现 `ERR AUTH`。

## 运行验证

### 1. 服务启动

- vis-screen-backend 本地成功监听 `8081`，并以 `100.118.113.100:vis:8081` 注册到 Eureka。
- org-tribe-system 本地成功监听 `9090`，并通过 Zuul 挂出 `/vis/api/**` 路由。

### 2. 网关接口验证

- `/vis/api/schemeInfo/getAllPage` 在清理坏 Eureka 实例后可连续返回 `200`。
- Ribbon 最终只保留正确的 vis IP 实例，不再出现坏主机名导致的 `400/200` 交替。

### 3. 真实浏览器验证

- 通过浏览器访问 `http://127.0.0.1:9090/`，进入 GK 登录页。
- 使用 `admin / Ysyyrps4` 登录成功后，正常进入首页 `dashboard/analysis`。
- 在同一登录态下访问 `http://127.0.0.1:9090/vis/preview?schemeId=1`，页面可正常进入，未复现“登录后立即退出”。

## 本轮确认到的额外线索

### 1. 直刷登录页地址异常

- 直接访问 `http://127.0.0.1:9090/user/login?redirect=/vis/preview?schemeId=1` 会返回后端 Whitelabel 500。
- org-tribe-system 日志显示根因是 Shiro `JwtFilter` 对该请求抛出 `AuthenticationException: token为空!`。
- 这说明该地址不是一个可稳定直刷的后端入口，实际应通过 SPA 正常入口触发登录页渲染。

### 2. 登录后前端仍有错误请求

- 登录完成后浏览器出现一次错误请求：`/user/undefined/login?...`，返回 `404`。
- 同时还有 WebSocket `ws://127.0.0.1:9090/websocket/...` 握手 `404`。
- 这两个问题在本轮未导致退出登录，但属于后续应继续收敛的前端运行态噪音。

## 当前结论

- vis 已完成通过 Eureka 被 GK 网关发现，并可通过 `/vis/api/**` 访问。
- vis 与 GK 平台的登录态已打通，本轮真实浏览器未复现“登录完立刻退出”。
- 当前更值得继续排查的是：
  - `/user/login?redirect=...` 直刷路径为什么会落入 JWT 过滤链。
  - 登录后为什么会出现 `/user/undefined/login` 错误请求。

## 联调收尾说明

- 本轮浏览器联调结束后，已按要求停止本次启动的 Java 进程，避免残留本地运行态影响下一轮排查。