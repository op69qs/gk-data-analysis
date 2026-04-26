# vis 数据字典按旧 vis 范围过滤

## 背景

- `可视化大屏 -> 数据字典` 当前复用的是 GK 主系统通用页面 `system/DictList`。
- 旧 vis 并没有独立的“数据字典前端页面 + 专用后端控制器”组合，旧入口本质上仍然查看 `sys_dict` 数据。
- 因此当前问题不在于“页面接错了”，而在于当前 GK 库里的通用字典数据范围大于旧 vis 当时实际可见的范围。

## 旧 vis 数据来源确认

本轮对以下位置做了交叉核对：

- `vis_screen/ant-design-vue-jeecg/src/views/system/DictList.vue`
- `vis_screen/jeecg-boot/jeecg-boot-module-system/src/main/java/org/jeecg/modules/system/controller/SysDictController.java`
- `vis_screen/sql/sys_dict.sql`
- `vis_screen/sql/sys_dict_item.sql`

确认结论：

- 旧 vis 前端也是调用通用 `/sys/dict/list`。
- 旧 vis 后端也没有额外的 vis 专用 dict controller。
- 真正决定“旧 vis 能看到哪些字典”的，是旧库 `sys_dict` 种子数据里 `del_flag=0` 的那一批 `dict_code`。

## 本次实现

### 1. 固化旧 vis 字典白名单

新增文件：

- `org-tribe-system/src/main/java/org/jeecg/modules/system/util/VisLegacyDictScope.java`

当前白名单来自旧 `vis_screen/sql/sys_dict.sql` 中逻辑未删除的数据，包含：

- `enable_status`
- `perms_type`
- `del_flag`
- `sex`
- `global_perms_type`
- `dict_item_status`
- `valid_status`
- `user_type`
- `status`
- `send_status`
- `yn`
- `menu_type`
- `user_status`

实现方式：

- 通过 `VisLegacyDictScope.apply(...)` 统一给 `QueryWrapper<SysDict>` 增加 `dict_code in (...)` 条件。

### 2. 在通用字典控制器中增加 vis 专用入口

修改文件：

- `org-tribe-system/src/main/java/org/jeecg/modules/system/controller/SysDictController.java`

新增接口：

- `GET /sys/dict/visList`
- `GET /sys/dict/visExportXls`

处理方式：

- 通用列表仍走 `/sys/dict/list`。
- vis 列表与导出在进入分页/导出前先套用 `VisLegacyDictScope`。
- 这样不会影响 GK 原有系统管理中的通用数据字典页。

### 3. 仅在 vis 菜单入口切换到专用接口

修改文件：

- `org-tribe-view/src/views/system/DictList.vue`

实现方式：

- 组件初始化阶段判断当前路由是否为 `/vis/system/dict`。
- 若命中 vis 入口，则：
  - `url.list = /sys/dict/visList`
  - `url.exportXlsUrl = sys/dict/visExportXls`
- 若不是 vis 入口，则继续保持原：
  - `url.list = /sys/dict/list`
  - `url.exportXlsUrl = sys/dict/exportXls`

这样做的原因是：

- `JeecgListMixin` 会在 `created()` 中立刻执行 `loadData()`。
- 如果等组件创建后再改接口地址，首屏请求仍会先打到通用接口。
- 因此接口分流必须前移到 `data()` 初始化阶段完成。

## 影响范围

- `可视化大屏 -> 数据字典` 现在只显示旧 vis 范围内的字典。
- 主系统原有 `系统管理 -> 数据字典` 不受影响，仍显示 GK 通用字典数据。
- vis 字典导出也会同步限制在同一批白名单内。

## 验证

- 新增单测：
  - `org-tribe-system/src/test/java/org/jeecg/modules/system/util/VisLegacyDictScopeTest.java`
- 已验证：
  - `VisLegacyDictScopeTest` 通过。
  - Maven 聚焦执行 `-Dtest=org.jeecg.modules.system.util.VisLegacyDictScopeTest test` 最终 `BUILD SUCCESS`。
- 编辑器静态检查：
  - `DictList.vue` 无新增错误。
  - `VisLegacyDictScope.java` 无新增错误。

## 说明

- 这次没有为 vis 单独复制一套新的字典页面，而是保留现有 GK 页面入口，改为后端按旧 vis 数据范围过滤。
- 这样改动最小，且能保证 vis 和 GK 通用系统页的行为边界清晰。