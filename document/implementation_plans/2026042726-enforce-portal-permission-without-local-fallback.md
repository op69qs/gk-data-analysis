# 强制门户权限生效（移除本地回退）

## 需求背景
- 用户明确要求：门户登录用户必须完全按门户权限控制，不允许回退到本地角色权限。
- 当前日志显示：门户权限码有返回，但未匹配到可见菜单，导致前端无菜单。

## 根因定位
- 门户接口文档中，推荐字段为 `grantedLocalPermissionCodes`，其语义是 `localPermissionCode`。
- 现有映射仅按本地 `sys_permission.perms` 与 `sys_permission.url` 匹配，覆盖范围不足。
- 因此出现 `portalCodeCount` 很大但 `matchedMenuCount=0` 的情况。

## 本次改动
文件：`org-tribe-system/src/main/java/org/jeecg/modules/system/controller/SysPermissionController.java`

1. 移除本地权限回退
- 删除“门户未命中或无菜单时回退 `sysPermissionService.queryByUser(username)`”逻辑。
- 统一按门户权限映射结果返回（允许为空，不再回退本地）。

2. 扩展门户码匹配键
- 匹配候选从 `perms + url` 扩展为：
  - `id`
  - `perms`
  - `url`
  - `componentName`
- 兼容门户 `localPermissionCode` 可能映射到本地不同字段的场景。

3. 增强归一化与诊断日志
- 权限码归一化增加尾斜杠清理。
- 当门户码为空、归一化后为空、完全无匹配、映射后无菜单时，输出样本日志，便于快速定位门户配置问题。

## 预期行为
- 门户用户菜单与按钮严格由门户权限决定。
- 若门户配置不正确，将表现为无菜单，同时日志给出门户码样本与匹配结果，不再被本地权限掩盖。

## 验证建议
1. 门户登录后检查 `/sys/permission/getUserPermissionByToken` 返回的 `menu` 与 `auth`。
2. 观察日志字段：
- `portalCodeCount`
- `localPermissionCount`
- `matchedMenuCount`
3. 如仍无菜单，依据 `portalCodeSample` 核对门户侧该系统的 `localPermissionCode` 配置。