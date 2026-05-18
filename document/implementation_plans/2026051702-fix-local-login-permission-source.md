# 修正本地登录与门户登录的权限来源分流

## 背景
- 当前 `org-tribe-view` 前端统一调用 `/sys/permission/getUserPermissionByToken` 拉取菜单与按钮权限。
- 之前为了强制门户权限生效，后端移除了本地权限回退，导致本地登录用户也被错误套用门户权限同步逻辑。
- 直接表现为：本地 `admin` 账号虽然仍有本地角色和权限，但页面菜单为空。

## 根因
- `org-tribe-system` 的 `SysPermissionController#getUserPermissionByToken` 无论 token 来源如何，都会先尝试读取门户上下文并按门户权限构造菜单。
- 本地登录 `/sys/login` 不会写入：
  - `PREFIX_NEXUS_PORTAL_USER_ID_{token}`
  - `PREFIX_NEXUS_PORTAL_ACCESS_TOKEN_{token}`
- 因此本地登录命中“门户权限码为空，不回退本地权限”分支，最终返回空菜单。

## 修正原则
- 本地登录：只使用本地角色/权限。
- 门户 OAuth 登录：只使用门户同步过来的权限，不回退本地。

## 本次实现
文件：
- `org-tribe-system/src/main/java/org/jeecg/modules/system/controller/SysPermissionController.java`

改动点：
1. 新增门户上下文判断
- 仅当 Redis 中同时存在门户用户 ID 与门户 access token 时，认定当前 token 来自门户 OAuth 登录。

2. 调整权限分流
- 有门户上下文：
  - 继续走门户权限同步与本地菜单映射逻辑。
  - 保持“不回退本地权限”的严格策略。
- 无门户上下文：
  - 直接调用 `sysPermissionService.queryByUser(username)` 读取本地权限。

3. 增补日志
- 本地登录输出 `本地登录权限结果` 日志。
- 门户登录继续输出 `门户权限同步结果` 日志。

## 预期结果
- 本地 `admin` 登录后恢复按本地角色显示菜单。
- 门户 OAuth 进入系统时仍严格按门户下发权限显示菜单，不混入本地角色权限。

## 验证建议
1. 使用本地 `admin` 登录，确认左侧菜单恢复。
2. 观察日志：
- 本地登录应出现 `本地登录权限结果`
- 门户登录应出现 `门户权限同步结果`
3. 再次验证 OAuth 登录用户，确认仍不会回退到本地角色菜单。
