# vis 字典迁移到 vis-screen-backend

## 背景

- 现有 `/vis/system/dict` 菜单虽然属于 vis 入口，但前端复用了 GK 的 `system/DictList`，并且请求落到 `org-tribe-system` 自己的 `/sys/dict/visList`。
- 按当前边界约束，原 vis 负责的内容应由合并后的 `vis-screen-backend` 承接，GK 只保留壳和网关。

## 本次调整

- 在 `vis-screen-backend` 的 `SysDictController` 新增：
  - `/sys/dict/visList`
  - `/sys/dict/visExportXls`
- 将旧 vis 字典白名单、展示顺序、缺失种子补齐逻辑下沉到 `vis-screen-backend`：
  - `VisLegacyDictScope`
  - `VisLegacyDictSeedSupport`
- 前端 `DictList.vue` 仅在 `/vis/system/dict` 路由下切到 `/vis/api/sys/dict/visList` 与 `/vis/api/sys/dict/visExportXls`。

## 影响范围

- 普通 GK 数据字典页仍走 `/sys/dict/list`，不变。
- vis 数据字典页改为通过 GK 网关访问 `vis-screen-backend`，符合 `/vis/api/**` 的服务边界。

## 验证目标

- `/vis/system/dict` 请求应命中 `/vis/api/sys/dict/visList`。
- 返回总数应恢复为 old vis 的 13 条。
- 首页顺序应保持：
  - `menu_type`
  - `enable_status`
  - `valid_status`
  - `dict_item_status`
  - `yn`
  - `global_perms_type`
  - `perms_type`
  - `send_status`
  - `user_status`
  - `status`