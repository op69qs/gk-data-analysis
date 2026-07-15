# vis 鉴权改为 OAuth 信息优先

## 背景

用户反馈：OAuth 登录后访问 vis 菜单时，权限验证不应依赖 vis 本地用户，而应以 OAuth/token 携带信息为主。

## 调整目标

将 vis 后端 Shiro 逻辑调整为：

1. 认证（Authentication）优先依赖 token 会话与 token 声明。
2. 授权（Authorization）优先从 token claims 解析角色与权限。
3. 本地用户仅作为补充信息（如状态、实名），不再是前置依赖。

## 代码改动

文件：

- `vis-screen-backend/jeecg-boot-base-common/src/main/java/org/jeecg/config/shiro/ShiroRealm.java`

### 1) 认证阶段改造（OAuth-first）

- 在 `checkUserTokenIsEffect` 中：
  - 先校验 token 是否有效（过期 + Redis 会话存在）；
  - 再基于 token 声明构造 `LoginUser`；
  - 如本地用户存在，仅补充状态与展示信息；
  - 本地用户不存在时不再直接抛 `用户不存在`。

### 2) 授权阶段改造（claims-first）

- 在 `doGetAuthorizationInfo` 中：
  - 优先读取请求头 token；
  - 解析 claims：`roles/role`、`permissions/perms/authorities/scope`；
  - 若 claims 可用，直接写入 `SimpleAuthorizationInfo` 并返回；
  - 仅当 claims 缺失时，回退 `commonAPI.queryUserRoles/queryUserAuths`。

### 3) token 有效性策略调整

- `jwtTokenRefresh` 改为会话优先：
  - 必须 token 未过期；
  - 并且 Redis 中存在 `PREFIX_USER_TOKEN + token`；
  - Redis 异常时回退过期时间校验（保证可用性）。

## 影响与兼容性

- OAuth 场景：不再因 vis 本地无用户而失败。
- 旧逻辑：当 token 未提供权限 claims 时，仍可回退到原有 commonAPI 查询。
- 安全边界：未放开匿名访问，仍要求 token 且需会话有效。

## 验证建议

1. OAuth 登录后访问 `/vis/api/**`，确认不再出现 `用户不存在`。
2. 若 token 带权限 claims，验证 `@RequiresPermissions` 生效。
3. 人为构造过期 token 或清理 Redis 会话，确认仍会被拒绝。