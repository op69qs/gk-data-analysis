# org-tribe-system JwtFilter 空 token 空指针修复

## 背景

- 前端出现 WebSocket 连接报错，同时后端日志显示 `JwtFilter.executeLogin` 抛出空指针。
- 实际问题不是 IP 本身异常，而是请求未携带 `X-Access-Token` 时，过滤器仍直接执行 `token.endsWith("#/")`。

## 根因

- 文件：`org-tribe-system/src/main/java/org/jeecg/modules/shiro/authc/aop/JwtFilter.java`
- 逻辑先从 header 或 parameter 读取 token。
- 当 token 为 `null` 或空串时，没有提前拦截，直接调用字符串方法，触发 `NullPointerException`。
- 外层把该异常包装为 `AuthenticationException("Token失效，请重新登录")`，最终表现为 500，掩盖了真实原因。

## 修改内容

- 在 `executeLogin` 中增加 token 非空校验。
- 对缺失 token 直接抛出 `AuthenticationException("token为空!")`，避免空指针。
- 在 `isAccessAllowed` 中单独透传 `AuthenticationException`，避免把明确的认证异常再次包装成泛化错误。

## 预期效果

- 请求未携带 token 时，不再触发 `NullPointerException`。
- 后续会进入原有 Shiro 认证失败流程，日志和问题定位更准确。
- WebSocket 报错不再被这个空指针误导，便于继续排查前端连接配置问题。

## 验证建议

- 未登录或 token 过期状态下访问任意受保护接口，确认后台不再出现 `JwtFilter.executeLogin` 空指针。
- 登录后刷新页面，确认普通接口与通知 WebSocket 至少不会再因为该空指针导致误判。