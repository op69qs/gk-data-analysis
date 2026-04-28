# 门户用户权限改为从门户同步（菜单+按钮）

## 背景
门户 OAuth 登录后，系统原逻辑仍按本地 `sys_permission` 角色关系查询用户菜单，导致门户账号进入后菜单/按钮权限不准确。

## 本次改动

### 1. OAuth 回调阶段缓存门户上下文
文件：`org-tribe-system/src/main/java/org/jeecg/modules/oauth/controller/NexusOAuthController.java`

- 在生成本地 JWT 后，新增 Redis 缓存：
  - `PREFIX_NEXUS_PORTAL_USER_ID_{localJwt}` -> 门户 userId（来自 access_token 的 `sub`）
  - `PREFIX_NEXUS_PORTAL_ACCESS_TOKEN_{localJwt}` -> 门户 access_token
- 过期时间与本地 JWT 保持一致。

### 2. 获取用户权限时优先走门户接口
文件：`org-tribe-system/src/main/java/org/jeecg/modules/system/controller/SysPermissionController.java`

- 在 `getUserPermissionByToken` 中新增优先逻辑：
  1. 根据本地 JWT 从 Redis 获取门户 userId 和门户 access_token；
  2. 调用门户接口：`/api/sync/users/{userId}/permissions?sysCode=...`；
  3. 读取 `data.grantedLocalPermissionCodes`（兼容 `permissionCodes`）；
  4. 用门户返回的权限编码过滤本地权限定义，并补齐父级菜单链路；
  5. 继续复用现有 `menu/auth/allAuth` 组装逻辑返回前端。
- 若门户信息缺失或门户调用失败：自动回退到原本地权限逻辑，保证可用性。

### 3. 新增配置项
文件：`org-tribe-system/src/main/resources/application-dev.yml`

```yaml
gk-nexus:
  sync:
    user-permissions-url: http://localhost:3000/api/sync/users/{userId}/permissions
    sys-code: GK_DATA_ANALYSIS
```

## 使用影响

- 对门户 OAuth 登录用户：菜单权限与按钮权限以门户账号权限为准。
- 对非门户登录用户：保持原逻辑（本地角色权限）不变。

## 风险与回退

- 门户接口不可用时自动回退本地权限，不阻断登录。
- 若需要快速回退，只需移除/屏蔽 Redis 门户上下文写入或权限接口优先分支即可恢复原行为。

## 验证建议

1. 门户登录后访问系统首页。
2. 抓取 `/sys/permission/getUserPermissionByToken` 响应：
   - `menu` 应与门户分配菜单一致；
   - `auth` 应与门户分配按钮编码一致。
3. 使用同账号验证：
   - 左侧菜单是否按门户分配显示；
   - `v-has` 控制按钮是否按门户分配显示/隐藏；
   - 表单字段禁用策略是否按权限生效。
