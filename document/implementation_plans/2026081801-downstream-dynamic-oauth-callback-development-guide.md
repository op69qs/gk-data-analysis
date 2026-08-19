# 下游系统动态 OAuth 回调开发指导

## 1. 适用范围

本文用于指导门户下游系统接入“按客户端来源网域选择下游入口”的 SSO 场景。

下游系统不再把某个入口 IP 或固定 `redirect-uri` 写死在应用配置中，而是在收到 OAuth 回调后，根据本次 HTTP 请求的外部协议、Host、端口和应用上下文路径生成回调地址，并使用同一个地址向 GK-Nexus 换取授权码对应的令牌。

本文范本已在数据分析平台 `org-tribe-system` 中落地，核心类为：

```text
org.jeecg.modules.oauth.NexusOAuthRedirectUriResolver
```

## 2. 总体链路

```text
客户端来源 IP
    -> GK-Nexus 按 source_cidr 选择下游入口
    -> 门户发起 authorize，redirect_uri 指向所选下游入口
    -> 浏览器访问下游前端 /oauth/callback
    -> 前端调用下游后端 /sys/oauth/callback?code=...
    -> 后端从当前 HTTP 请求生成同一个 redirect_uri
    -> 后端向 GK-Nexus token 接口换取 access_token
```

注意：前端浏览器回调路由和后端换票接口可以不同。例如数据分析平台的前端回调路由是 `/oauth/callback`，后端换票接口是 `/sys/oauth/callback`。生成的 OAuth `redirect_uri` 必须是前端浏览器回调路由，不能直接使用后端接口的 `requestURI`。

## 3. GK-Nexus 侧配置

客户端来源网段、下游入口和 OAuth 回调地址仍由 GK-Nexus 管理。每一个可访问入口都要有对应的精确回调地址：

| 客户端来源网段 | 下游入口 | `oauth_redirect_uri` |
|---|---|---|
| `9.16.20.0/24` | DMZ 入口 1 | `https://<DMZ入口1>/oauth/callback` |
| `9.17.20.0/24` | DMZ 入口 2 | `https://<DMZ入口2>/oauth/callback` |
| 其他 `9.*` | 金融机构入口 | `https://<金融入口>/oauth/callback` |
| `11.0.0.0/8` | 人行全辖入口 | `https://<人行入口>/oauth/callback` |

实际网段以现场网络部门提供的 CIDR 为准。更具体的网段必须优先于更宽的网段，例如 `9.16.20.0/24`、`9.17.20.0/24` 必须优先于 `9.0.0.0/8`。

同时，在 GK-Nexus 的 OAuth 客户端 `oauth_client.redirect_uris` 中精确登记全部合法回调地址。协议、Host、端口和路径必须完全一致，不使用通配符，不添加查询参数。

下游系统不需要复制维护 `sso_target_endpoint` 的来源网段映射，也不需要配置 `NEXUS_REDIRECT_URIS`。

## 4. 下游系统实现要求

### 4.1 解析规则

实现一个独立的回调地址解析器，输入至少包括：

- `HttpServletRequest`；
- 可信代理 CIDR 配置；
- 当前应用的前端 OAuth 回调路径。

解析优先级如下：

1. 如果请求对端地址不在可信代理 CIDR 中，忽略所有 `X-Forwarded-*` 请求头，使用 Servlet 请求的 `scheme`、`serverName`、`serverPort`。
2. 如果请求对端地址命中可信代理 CIDR，并且 `X-Forwarded-Proto`、`X-Forwarded-Host` 合法，则使用代理传递的外部协议和 Host。
3. `X-Forwarded-Host` 没有端口时，可使用合法的 `X-Forwarded-Port` 补充端口。
4. 只允许 `http` 和 `https`，拒绝用户信息、Fragment、查询参数和非法端口。
5. 将应用上下文路径与前端回调路径拼接，例如 `/jeecg-boot` + `/oauth/callback`。

最终格式为：

```text
scheme://host[:port][context-path]/oauth/callback
```

### 4.2 换票必须使用解析结果

后端 OAuth 回调收到 `code` 后，必须在换票前解析当前请求地址，并把解析结果作为 token 请求的 `redirect_uri`：

```java
String callbackRedirectUri = redirectUriResolver.resolve(request, trustedProxyCidrs);
params.add("redirect_uri", callbackRedirectUri);
```

不要再从 `application.yml` 读取以下固定配置：

```yaml
gk-nexus:
  oauth:
    redirect-uri: http://固定地址/oauth/callback
```

### 4.3 配置模板

保留可信代理配置，默认只信任本机：

```yaml
gk-nexus:
  oauth:
    trusted-proxy-cidrs: "${NEXUS_TRUSTED_PROXY_CIDRS:127.0.0.1/32,::1/128}"
```

如果下游系统前面没有 Nginx、WAF 或负载均衡，保持默认值即可。此时应用使用直接到达应用服务器的请求地址，并忽略客户端提交的 `X-Forwarded-*`。

如果确实存在反向代理，只能填写代理实际连接下游系统的源地址，例如：

```bash
NEXUS_TRUSTED_PROXY_CIDRS=127.0.0.1/32,::1/128,192.168.10.10/32
```

这里填写的是代理地址，不是客户端网段。不能填写 `9.0.0.0/8`、`11.0.0.0/8` 或 `0.0.0.0/0`。

代理需要正确传递外部访问地址：

```nginx
proxy_set_header Host $host;
proxy_set_header X-Forwarded-Proto $scheme;
proxy_set_header X-Forwarded-Host $host;
proxy_set_header X-Forwarded-Port $server_port;
```

Nginx 不是本方案的前置条件；没有 Nginx 时按直连规则工作。

## 5. 安全要求

- 不要使用浏览器传入的 `X-Forwarded-Host` 直接拼接回调地址。
- `request.getRemoteAddr()` 表示当前 TCP 直接对端，只用于判断是否为可信代理，不能用它作为下游入口 Host。
- 只信任明确的代理 IP/CIDR，可信代理配置不能使用全网段。
- OAuth 服务端仍必须对 `redirect_uri` 做精确白名单校验。动态生成不等于允许任意 Host。
- 不允许回调地址带查询参数、Fragment、用户信息或非法端口。
- 不通过统一反向代理把不同安全域全部暴露给所有用户。客户端来源网段到入口的访问控制仍由 GK-Nexus 和现场网络策略共同保证。
- 如果不同安全域使用不同入口，必须分别登记回调地址并分别验证，不能用一个“万能入口”代替网络隔离。

## 6. 测试要求

至少覆盖以下单元测试：

1. 直连请求使用 `scheme + Host + port`。
2. 可信代理请求使用合法的 `X-Forwarded-Proto/Host/Port`。
3. 非可信请求伪造转发头时，仍使用直连地址。
4. 应用有 context path 时，回调路径包含 context path。
5. 非法 Host、协议、端口、查询参数和 Fragment 被拒绝或不被采用。
6. token 换票请求中的 `redirect_uri` 与解析结果完全一致。

现场验收建议分别从以下来源网域发起登录：

```text
9.16.20.*  -> DMZ 入口 1
9.17.20.*  -> DMZ 入口 2
其他 9.*   -> 金融机构入口
11.*       -> 人行全辖入口
```

检查三处地址完全一致：

```text
GK-Nexus authorize 请求 redirect_uri
= GK-Nexus sso_target_endpoint.oauth_redirect_uri
= 下游 token 换票请求 redirect_uri
```

数据分析平台测试命令：

```bash
mvn -pl org-tribe-system \
  -Dskip.frontend.build=true \
  -Dtest=NexusOAuthRedirectUriResolverTest,NexusOAuthControllerTest \
  -Dsurefire.failIfNoSpecifiedTests=false test
```

## 7. 开发交付清单

- [ ] 增加动态回调地址解析器。
- [ ] 后端换票使用当前请求解析结果。
- [ ] 删除固定 `redirect-uri`、`redirect-uris`、`redirect-uri-mode` 和 `NEXUS_REDIRECT_URIS` 配置。
- [ ] 增加 `trusted-proxy-cidrs`，默认只信任本机。
- [ ] 只在确有代理时配置代理源地址，并同步配置代理转发头。
- [ ] 在 GK-Nexus 精确登记所有合法 `oauth_redirect_uri`。
- [ ] 补齐直连、可信代理、非可信转发头和非法地址测试。
- [ ] 现场验证四类来源网域的授权请求和换票请求。
