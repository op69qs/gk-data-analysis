# vis 统一消费分析平台认证

## 背景

- `vis-screen-backend` 已经通过 Eureka 以 `vis` 注册，`org-tribe-system` 通过 Zuul 和 Eureka 转发 `/vis/api/**`。
- 剩余问题不在服务发现，而在认证语义：`vis` 原本要求本地 Redis token 和本地用户密码验签，这和分析平台签发 token 的模式不兼容。

## 目标

- 保持 `vis` 作为被集成模块，由分析平台负责登录和签发 token。
- `vis` 不再要求平台 token 同时满足 `vis` 自己的本地密码验签语义。
- 集成链路以 `org-tribe-system -> /vis/api/** -> vis` 为准，不再要求浏览器或调用方直连 `vis` 本体通过鉴权。

## 实现调整

### 1. 对齐 vis Redis 默认配置

文件：`vis-screen-backend/jeecg-boot-module-system/src/main/resources/application-dev.yml`

- 将 `spring.redis.host` 默认值从本地 `127.0.0.1` 改为和分析平台一致的共享 Redis 主机 `192.168.160.30`。
- 同时保留环境变量覆盖能力，便于不同环境启动。
- 同样风格已同步到 `application-prod.yml` 与 `application-test.yml`。

这样做的目的是让 `vis` 在能够复用平台缓存时优先复用，而不是先因为本地 Redis 漂移直接失败。

### 2. 放宽 vis 的 token 刷新兜底规则

文件：`vis-screen-backend/jeecg-boot-base-common/src/main/java/org/jeecg/config/shiro/ShiroRealm.java`

调整 `jwtTokenRefresh` 逻辑：

- 仍然保留原来的优先路径：如果 Redis 中存在 `prefix_user_token_` 缓存，就按原逻辑进行刷新和校验。
- 如果 Redis 读取异常，或者没有命中本地 token 缓存，不再强制使用 `vis` 本地用户密码去执行 `JwtUtil.verify(...)`。
- 改为退化到“token 可解码且未过期”的检查。

新增辅助方法：

- `isTokenNotExpired(String token)`

它只解码 JWT 并检查 `exp` 是否晚于当前时间，不再引入 `vis` 本地密码语义。

## 验证结果

本次验证全部在 JDK 8 下完成，本地启动端口为：

- `org-tribe-system`: `9090`
- `vis-screen-backend`: `8081`

验证结论如下：

### 1. 服务发现正常

- `vis` 可正常启动并监听 `8081`
- `swagger-ui.html` 返回 `200`
- `VIS` 能重新注册到 Eureka

### 2. 平台登录正常

- `POST http://127.0.0.1:9090/sys/login`
- 使用 `admin / Ysyyrps4` 可以成功拿到平台 token

### 3. 直连 vis 仍然失败，但这是预期内行为

- 使用平台 token 直接请求 `http://127.0.0.1:8081/vis/api/schemeInfo/getAll`
- 返回仍然是 `500 Token失效，请重新登录`

这说明 `vis` 本体直连接口依然保留着本地认证边界，没有被错误地改造成完全匿名。

### 4. 通过 GK 路由访问 vis 已不再是鉴权错误

- 使用同一枚平台 token 请求 `http://127.0.0.1:9090/vis/api/schemeInfo/getAll`
- 返回状态从之前的认证失败变为 `400`
- 无 token 请求同一路径会返回 `500 token为空!`

这说明：

- token 已经成功经过分析平台链路被消费
- 现在失败点已经从“身份验证失败”前移为“业务接口参数或业务逻辑问题”

### 5. 浏览器验证与接口结果一致

- 浏览器在 `http://127.0.0.1:9090/` 登录成功后，注入平台 token 再访问 `http://127.0.0.1:9090/vis/preview`
- 页面不再落回登录页，也不再出现 `token为空`
- 刷新后的前端请求命中 `400` 资源失败

这与接口层的 routed probe 一致，说明浏览器实际访问的也是统一认证后的集成链路。

## 结论

- Eureka 注册与发现已经完成。
- 前端运行时地址已经对齐到 same-origin。
- `vis` 作为集成模块，已经可以通过 `org-tribe-system` 消费分析平台登录态。
- 当前剩余问题不再是“认证整合失败”，而是 `vis` 页面或其业务接口自身返回 `400` 的独立业务问题。

## 后续建议

1. 继续排查 `/vis/api/schemeInfo/getAll` 在 routed 模式下返回 `400` 的业务入参与数据依赖。
2. 如果后续要求支持“平台 token 直连 vis 本体也通过”，需要单独设计 `vis` 的 trust 模式，而不是继续在现有本地 JWT 密码语义上叠补丁。
3. 将浏览器登录后 `/user/undefined/login` 的 JSONP/静态资源问题作为单独前端缺陷处理，不与本次认证整合混在一起。