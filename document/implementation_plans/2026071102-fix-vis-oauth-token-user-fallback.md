# 修复 OAuth 后 vis 鉴权“用户不存在”问题

## 现象

门户 OAuth 登录后访问 vis 菜单，vis 后端（9093）报错：

- `AuthenticationException: Token失效，请重新登录`
- 根因栈内出现：`ShiroRealm.checkUserTokenIsEffect -> 用户不存在!`

## 根因分析

vis 后端 `ShiroRealm` 会先从 token 取 `username`，然后调用 `commonAPI.getUserByName(username)` 查询本地用户。

在门户 OAuth 场景下，token 可能来自分析平台主系统（或门户映射身份），而 vis 本地用户库并不一定存在同名用户，导致直接抛 `用户不存在!`。

## 实施内容

修改文件：

- `vis-screen-backend/jeecg-boot-base-common/src/main/java/org/jeecg/config/shiro/ShiroRealm.java`

改动点：

1. 在 `checkUserTokenIsEffect` 中增加回退逻辑
- 当本地用户不存在时，不再立即抛错。
- 改为尝试从 JWT 声明构造临时 `LoginUser`（`username`、`subject->id`、`name->realname`、`status=1`）。
- 前提是 token 未过期。

2. 在 `jwtTokenRefresh` 中增加空密码分支
- 若 `passWord` 为空（临时用户场景），不走基于密码的验签刷新。
- 直接按 token 过期时间判断有效性。

3. 新增私有方法 `buildLoginUserFromToken`
- 用于统一封装“基于 token 声明构建登录态”的逻辑。

## 兼容性与安全性

- 未放开 `/vis/api/**` 的匿名访问，仍保留 Shiro 鉴权入口。
- 仅在“本地用户缺失”且“token 未过期”时走声明回退。
- 对已有本地用户流程无影响。

## 验证建议

1. 门户 OAuth 登录后进入 vis 菜单，确认不再出现 `用户不存在!`。
2. 继续访问 vis 业务接口，确认 9093 不再抛 token 失效异常。
3. 使用明显过期 token 复测，确认仍会被拒绝（安全边界未放宽）。