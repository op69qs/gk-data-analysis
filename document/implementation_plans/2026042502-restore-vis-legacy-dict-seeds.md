# vis 旧数据字典补种与顺序恢复

## 问题

- `http://127.0.0.1:9090/vis/system/dict` 当前只显示 9 条字典。
- 旧 vis 页面显示 13 条，且第一页顺序为旧系统既有顺序。
- 已确认白名单代码无误，差异来自当前库缺少旧 vis 的 4 条字典种子：`enable_status`、`yn`、`status`、`user_type`。

## 处理

- 在后端新增 `VisLegacyDictSeedSupport`，内置这 4 条旧 vis 字典及其字典项种子。
- `SysDictController` 的 `/sys/dict/visList` 与 `/sys/dict/visExportXls` 在查询前自动补齐缺失种子，并清理字典缓存。
- `VisLegacyDictScope` 增加旧 vis 页面展示顺序，vis 专用列表改为按该顺序分页返回，避免依赖数据库默认返回顺序。
- 新增单测，锁定旧 vis 字典顺序和缺失补种集合。

## 验证

- 运行 `VisLegacyDictScopeTest` 与 `VisLegacyDictSeedSupportTest`，验证白名单顺序和补种集合。
- 修复后再次访问 `vis/system/dict`，预期总数恢复到 13 条，第一页顺序与旧 vis 截图一致。