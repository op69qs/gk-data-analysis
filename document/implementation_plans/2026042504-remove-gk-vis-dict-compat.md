# GK 侧 vis 字典兼容收口

## 背景

- `/vis/system/dict` 已经改为通过 GK 网关调用 `vis-screen-backend` 的 `/vis/api/visDict/list` 与 `/vis/api/visDict/exportXls`。
- `vis-screen-backend` 已承接 old vis 字典白名单、补种、恢复逻辑，并完成运行态验证。
- `org-tribe-system` 中残留的 vis 字典兼容接口已经不再是正确边界，继续保留会让实现再次分叉。

## 本次处理

- 删除 `org-tribe-system` 的 `SysDictController` 中以下 vis 专用逻辑：
  - `visList`
  - `visExportXls`
  - old vis 字典分页排序逻辑
  - old vis 字典补种与逻辑删除恢复逻辑
- 删除 `org-tribe-system` 中仅服务于上述逻辑的两个工具类：
  - `VisLegacyDictScope`
  - `VisLegacyDictSeedSupport`

## 结果

- GK 主系统恢复为只承载通用 `sys/dict/*` 能力。
- old vis 字典的专属实现只保留在 `vis-screen-backend`，模块边界与网关路由保持一致。
- 后续若继续排查 vis 归属问题，优先以“是否走 `/vis/api/**`”作为判断标准。