# 修复门户权限命中但菜单为空

## 问题现象
- 日志显示门户权限同步已命中：`portalCodeCount=46`，但页面左侧菜单为空。
- 说明门户权限码已返回，但后端组装 `menu` 时没有得到可见菜单节点。

## 根因分析
- 原逻辑仅使用 `sys_permission.perms` 与门户权限码做精确匹配。
- 在实际数据中，很多菜单节点没有 `perms`，而是通过 `url` 参与权限定义；导致只匹配到少量按钮或非菜单权限。
- 前端只依赖 `/sys/permission/getUserPermissionByToken` 的 `result.menu` 渲染路由，`menu` 为空时页面就无菜单。

## 实施改动
文件：`org-tribe-system/src/main/java/org/jeecg/modules/system/controller/SysPermissionController.java`

1. 增强门户码匹配规则：
- 新增统一归一化方法：去除前导 `/` 后比较。
- 匹配字段从仅 `perms` 扩展为 `perms + url` 双通道。

2. 增加菜单兜底回退：
- 门户同步后统计命中的菜单数量（`menuType=0/1`）。
- 若命中为 0，则记录告警日志并回退到本地权限查询，避免前端空菜单。

## 影响范围
- 仅影响 OAuth 门户权限同步分支，不影响原本地权限分支。
- 当门户码与本地权限映射正常时，继续以门户权限为准。
- 当门户码无法映射出菜单时，系统自动回退本地权限，保证可用性。

## 验证建议
1. 门户登录后观察日志：
- 若映射正常，应出现 `matchedMenuCount>0`。
- 若映射异常，会出现“未匹配到可见菜单，回退本地权限”告警。
2. 抓取 `/sys/permission/getUserPermissionByToken` 响应：确认 `result.menu` 非空。
3. 刷新前端，确认左侧菜单正常渲染。
