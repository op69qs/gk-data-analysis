# 直接使用门户 permissions 组装本地权限对象

## 背景
在 `SysPermissionController.getUserPermissionByToken` 调试中发现，原逻辑主要依赖：
1. 先拿 `grantedLocalPermissionCodes`
2. 再全量查询本地权限定义并逐条匹配

当本地权限量较大或匹配链路复杂时，该过程存在明显卡顿风险。

## 目标
- 优先使用门户同步接口返回的 `data.permissions` 直接构建 `SysPermission` 列表。
- 保持“门户优先、无本地回退授权”的原则不变。
- 在门户仅返回 code、未返回 permissions 明细时，保留原有 code 匹配逻辑作为兼容兜底。

## 实现变更

### 1. 调整入口流程
文件：`org-tribe-system/src/main/java/org/jeecg/modules/system/controller/SysPermissionController.java`

`getUserPermissionByToken` 改为：
1) 拉取门户 `data`
2) 提取 `grantedLocalPermissionCodes`（用于日志与兜底）
3) 优先调用 `buildPermissionListByPortalPermissions(data)`
4) 若结果为空，再调用 `buildPermissionListByPortalCodes(codes)`

### 2. 新增门户 payload 解析方法
- `fetchPortalPermissionData(String localJwtToken)`：返回 `data` JSON，而不是仅返回 code 集合。
- `extractPortalPermissionCodes(JSONObject data)`：从 `grantedLocalPermissionCodes/permissionCodes` 提取 code 集合。
- `buildPermissionListByPortalPermissions(JSONObject data)`：
  - 直接读取 `data.permissions`
  - 使用 `localPermissionCode` 作为本地 `id`
  - 使用 `localParentPermissionCode` 作为 `parentId`（缺失时回退 portal id 映射）
  - 通过 `type` 映射菜单类型：DIRECTORY->0, MENU->1, BUTTON->2
  - 根据父子关系计算 `leaf`
- `convertPortalType(String portalType)` 与 `firstNonEmpty(...)` 辅助方法。

### 3. 性能收益
- 避免每次鉴权都全量读取本地权限定义并执行匹配扫描。
- 在门户返回完整 permissions 场景下，菜单构建路径更短、更稳定。

## 验证
- 已完成 `org-tribe-system` 编译验证：
  - `mvn -DskipTests compile`

## 风险与兼容
- 门户 payload 若缺少 `permissions`，仍保留 code 匹配逻辑兼容。
- `permsType` 在门户未显式返回时设置为默认值 `"1"`，与现有按钮权限结构兼容。