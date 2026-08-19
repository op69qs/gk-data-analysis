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

## 8. 交付清单对应样例代码

下面的代码以 Java + Spring Boot 下游系统为例。其他语言或框架必须保持相同的安全规则：直连不信任转发头，只有可信代理可以提供外部协议和 Host，最终回调地址必须通过 GK-Nexus 白名单校验。

### 8.1 动态回调地址解析器

对应清单：

```text
[ ] 增加动态回调地址解析器
```

下游系统可以直接复制数据分析平台的完整实现：

```text
org-tribe-system/src/main/java/org/jeecg/modules/oauth/NexusOAuthRedirectUriResolver.java
```

核心调用样例：

```java
@Component
public class NexusOAuthRedirectUriResolver {

    private static final String CALLBACK_PATH = "/oauth/callback";

    public String resolve(HttpServletRequest request, String trustedProxyCidrs) {
        String scheme = request.getScheme();
        String authority = authority(request.getServerName(), request.getServerPort(), scheme);

        // 只有当前 TCP 对端是可信代理时，才读取 X-Forwarded-*。
        if (isTrustedProxy(request.getRemoteAddr(), trustedProxyCidrs)) {
            String forwardedProto = firstHeader(request.getHeader("X-Forwarded-Proto"));
            String forwardedHost = firstHeader(request.getHeader("X-Forwarded-Host"));
            if (isHttpScheme(forwardedProto) && isSafeAuthority(forwardedHost)) {
                scheme = forwardedProto;
                authority = forwardedHost;
                String forwardedPort = firstHeader(request.getHeader("X-Forwarded-Port"));
                if (!hasExplicitPort(authority) && isPort(forwardedPort)) {
                    authority = authority + ":" + forwardedPort;
                }
            }
        }

        if (!isHttpScheme(scheme) || !isSafeAuthority(authority)) {
            throw new IllegalArgumentException("OAuth callback request address is invalid");
        }

        String contextPath = request.getContextPath();
        String callbackPath = (contextPath == null || contextPath.isEmpty()
                || "/".equals(contextPath))
                ? CALLBACK_PATH
                : contextPath + CALLBACK_PATH;
        return new URI(scheme.toLowerCase(), authority, callbackPath, null, null).toString();
    }

    // isTrustedProxy 必须按 IP/CIDR 做真实匹配，不能只判断配置是否非空。
    // authority、isSafeAuthority、isPort 等校验方法应一并保留，参见本仓库完整实现。
}
```

不要把下面这种写法作为实现：

```java
// 错误：客户端可以伪造 X-Forwarded-Host，导致回调地址被任意篡改。
String redirectUri = request.getHeader("X-Forwarded-Proto")
        + "://" + request.getHeader("X-Forwarded-Host") + "/oauth/callback";
```

### 8.2 后端换票使用动态结果

对应清单：

```text
[ ] 后端换票使用当前请求解析结果
```

控制器样例：

```java
@RestController
@RequestMapping("/sys/oauth")
public class OAuthCallbackController {

    @Autowired
    private NexusOAuthRedirectUriResolver redirectUriResolver;

    @Value("${gk-nexus.oauth.trusted-proxy-cidrs:127.0.0.1/32,::1/128}")
    private String trustedProxyCidrs;

    @GetMapping("/callback")
    public Object callback(@RequestParam("code") String code,
                           HttpServletRequest request) {
        String redirectUri = redirectUriResolver.resolve(request, trustedProxyCidrs);
        return exchangeCodeForToken(code, redirectUri);
    }

    private Object exchangeCodeForToken(String code, String redirectUri) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        return restTemplate.postForObject(tokenUrl, new HttpEntity<>(params), JSONObject.class);
    }
}
```

数据分析平台实际代码在换票前执行：

```java
String callbackRedirectUri = resolveCallbackRedirectUri(request);
accessToken = exchangeCodeForToken(normalizedCode, callbackRedirectUri);
```

### 8.3 删除固定回调配置

对应清单：

```text
[ ] 删除固定 redirect-uri、redirect-uris、redirect-uri-mode 和 NEXUS_REDIRECT_URIS 配置
```

修改前：

```yaml
gk-nexus:
  oauth:
    redirect-uri: http://localhost:9090/oauth/callback
```

修改后：

```yaml
gk-nexus:
  oauth:
    token-url: http://gk-nexus/auth/oauth/token
    client-id: GK_DATA_ANALYSIS
    client-secret: ${NEXUS_CLIENT_SECRET}
    # 不再配置 redirect-uri
```

同时检查环境变量和部署脚本：

```bash
rg -n "redirect-uri|redirect-uris|redirect-uri-mode|NEXUS_REDIRECT_URIS" \
  src deploy-package bin service.sh
```

命令没有输出固定回调配置才算清理完成。GK-Nexus 的 `oauth_client.redirect_uris` 不属于下游固定配置，仍必须保留。

### 8.4 增加可信代理配置

对应清单：

```text
[ ] 增加 trusted-proxy-cidrs，默认只信任本机
```

`application.yml` 样例：

```yaml
gk-nexus:
  oauth:
    trusted-proxy-cidrs: "${NEXUS_TRUSTED_PROXY_CIDRS:127.0.0.1/32,::1/128}"
```

无代理时不需要设置环境变量：

```bash
unset NEXUS_TRUSTED_PROXY_CIDRS
```

有代理时只填写代理源地址：

```bash
export NEXUS_TRUSTED_PROXY_CIDRS="127.0.0.1/32,::1/128,192.168.10.10/32"
```

不要把客户端来源网段写入此配置：

```bash
# 错误：这些是客户端来源网段，不是可信代理地址
NEXUS_TRUSTED_PROXY_CIDRS="9.0.0.0/8,11.0.0.0/8"
```

### 8.5 代理转发头配置

对应清单：

```text
[ ] 只在确有代理时配置代理源地址，并同步配置代理转发头
```

Nginx 样例：

```nginx
location / {
    proxy_pass http://data-analysis-backend;
    proxy_set_header Host $host;
    proxy_set_header X-Forwarded-Proto $scheme;
    proxy_set_header X-Forwarded-Host $host;
    proxy_set_header X-Forwarded-Port $server_port;
}
```

没有 Nginx 时，不需要增加任何代理配置，应用直接使用：

```java
request.getScheme();
request.getServerName();
request.getServerPort();
```

同时，代理的实际连接地址必须与 `NEXUS_TRUSTED_PROXY_CIDRS` 中的地址一致；否则应用会忽略转发头并使用内部地址，最终换票会因回调地址不一致而失败。

### 8.6 GK-Nexus 端点和回调白名单

对应清单：

```text
[ ] 在 GK-Nexus 精确登记所有合法 oauth_redirect_uri
```

现场配置 SQL 样例：

```sql
BEGIN;

INSERT INTO gk_nexus.sso_target_endpoint
    (sys_code, endpoint_name, source_cidr, base_url,
     oauth_redirect_uri, priority, is_active)
VALUES
    ('GK_DATA_ANALYSIS', 'DMZ-9.16.20', '9.16.20.0/24',
     'https://<DMZ入口1>:9090',
     'https://<DMZ入口1>:9090/oauth/callback', 24, 1),
    ('GK_DATA_ANALYSIS', 'DMZ-9.17.20', '9.17.20.0/24',
     'https://<DMZ入口2>:9090',
     'https://<DMZ入口2>:9090/oauth/callback', 24, 1),
    ('GK_DATA_ANALYSIS', '金融机构', '9.0.0.0/8',
     'https://<金融入口>:9090',
     'https://<金融入口>:9090/oauth/callback', 8, 1),
    ('GK_DATA_ANALYSIS', '人行全辖', '11.0.0.0/8',
     'https://<人行入口>:9090',
     'https://<人行入口>:9090/oauth/callback', 8, 1)
ON CONFLICT (sys_code, source_cidr) DO UPDATE
SET endpoint_name = EXCLUDED.endpoint_name,
    base_url = EXCLUDED.base_url,
    oauth_redirect_uri = EXCLUDED.oauth_redirect_uri,
    priority = EXCLUDED.priority,
    is_active = EXCLUDED.is_active,
    updated_at = CURRENT_TIMESTAMP;

UPDATE gk_nexus.oauth_client
SET redirect_uris = concat_ws(',',
    'https://<DMZ入口1>:9090/oauth/callback',
    'https://<DMZ入口2>:9090/oauth/callback',
    'https://<金融入口>:9090/oauth/callback',
    'https://<人行入口>:9090/oauth/callback')
WHERE client_id = 'GK_DATA_ANALYSIS';

COMMIT;
```

`<DMZ入口1>` 等占位符必须替换成现场实际地址。不要将本 SQL 直接带占位符执行。

### 8.7 测试样例

对应清单：

```text
[ ] 补齐直连、可信代理、非可信转发头和非法地址测试
```

JUnit 样例：

```java
@Test
public void ignoresForwardedHostFromUntrustedClient() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setScheme("http");
    request.setServerName("10.20.8.20");
    request.setServerPort(9090);
    request.setRemoteAddr("192.168.1.20");
    request.addHeader("X-Forwarded-Proto", "https");
    request.addHeader("X-Forwarded-Host", "attacker.example.test:443");

    assertEquals(
        "http://10.20.8.20:9090/oauth/callback",
        resolver.resolve(request, "10.0.0.0/8"));
}

@Test
public void usesForwardedAddressFromTrustedProxy() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setScheme("http");
    request.setServerName("127.0.0.1");
    request.setServerPort(8080);
    request.setRemoteAddr("10.0.0.5");
    request.addHeader("X-Forwarded-Proto", "https");
    request.addHeader("X-Forwarded-Host", "10.20.8.20:9443");

    assertEquals(
        "https://10.20.8.20:9443/oauth/callback",
        resolver.resolve(request, "10.0.0.0/8"));
}
```

换票请求也必须校验 `redirect_uri`：

```java
server.expect(requestTo(tokenUrl))
    .andExpect(content().string(containsString(
        "redirect_uri=https%3A%2F%2F10.20.8.20%3A9443%2Foauth%2Fcallback")))
    .andRespond(withSuccess("{\"access_token\":\"token\"}",
        MediaType.APPLICATION_JSON));
```

### 8.8 现场验收样例

对应清单：

```text
[ ] 现场验证四类来源网域的授权请求和换票请求
```

可在下游日志中临时增加以下脱敏日志，不记录授权码和客户端密钥：

```java
log.info("OAuth callback route={}, remotePeer={}, selectedRedirectUri={}",
        request.getRequestURI(), request.getRemoteAddr(), callbackRedirectUri);
```

分别从四类网域访问并检查日志：

```text
9.16.20.*  -> https://<DMZ入口1>:9090/oauth/callback
9.17.20.*  -> https://<DMZ入口2>:9090/oauth/callback
其他 9.*   -> https://<金融入口>:9090/oauth/callback
11.*       -> https://<人行入口>:9090/oauth/callback
```

GK-Nexus 和下游日志查询样例：

```bash
# GK-Nexus：查看授权请求中的 redirect_uri
rg -n "authorize.*redirect_uri|redirect_uri" logs/gk-nexus/*.log

# 下游：查看换票时最终采用的 redirect_uri
rg -n "selectedRedirectUri|redirectUri" logs/org-tribe-system/*.log
```

自动化测试命令：

```bash
mvn -pl org-tribe-system \
  -Dskip.frontend.build=true \
  -Dtest=NexusOAuthRedirectUriResolverTest,NexusOAuthControllerTest \
  -Dsurefire.failIfNoSpecifiedTests=false test
```
