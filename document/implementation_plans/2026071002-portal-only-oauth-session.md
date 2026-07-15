# 数据分析平台门户用户免本地账户登录

## 问题

门户用户 `lj_001` 可以完成 OAuth 授权码换取，但数据分析平台回调强制查询本地 `sys_user`。本地用户缺失时，门户授权码已被一次性消费，分析平台随后返回“该用户不存在”。

## 实现

- OAuth 回调在本地用户不存在时，根据门户 JWT 的 `sub`、`username`、`name` 构造内存用户，不写入分析平台数据库。
- 门户用户的本地会话 token、门户 userId、门户 access token 和内存用户均使用相同 TTL 缓存到 Redis。
- ShiroRealm 命中门户用户会话缓存时直接恢复 `LoginUser`，不再查询本地 `sys_user` 或校验本地密码。
- 已有 `SysPermissionController` 使用门户 access token 拉取权限节点，因此门户会话继续从门户获取权限和菜单。

## 边界

- 已存在的本地用户继续执行原有状态校验和本地 JWT 签名路径。
- 门户用户会话不创建、更新或同步本地 `sys_user`、角色和菜单记录。
- 退出后 Redis 会话失效，门户用户无法再通过本地 token 访问。