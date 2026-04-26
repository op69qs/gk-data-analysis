# vis 页面块图库绑定与查询条件轻量恢复

## 背景

上一切片已经把 `PageEditorEntry.vue` 推进到“模板预设生成区块 + 轻量区块编辑”的阶段，但每个区块还只能改标题、内容和布局坐标，缺少旧 `AddTemplateDrawer.vue` 中更关键的一层：

- 绑定 `gallery_id`
- 记录 `query_path`
- 设置 `unit`
- 维护 `pageWhere` 查询条件数组

如果这层不恢复，页面块虽然能保存，但仍无法表达旧 vis 图表块的最小查询语义。

## 本次改动

1. 在 `PageEditorEntry.vue` 的区块编辑区补出以下字段：
   - `query_path`
   - `unit`
   - `option`
2. 把 `pageWhere` 从只在 payload 中透传，升级为页面内可直接编辑的轻量列表。
3. 每个区块现在都支持：
   - 手工添加普通查询条件
   - 一键添加时间条件（默认 `where_type=t`、`where_key=dacct`）
   - 删除单条条件
4. `normalizeBlock` 现在会统一规整 `pageWhere` 结构，确保保存时仍符合后端 `PageWhere` 的最小字段形状：
   - `where_type`
   - `where_key`
   - `where_value`

## 取舍

- 这次不恢复旧抽屉里的树选择器、时间选择器和图库列表选择弹窗。
- 这次只恢复“可表达查询语义的数据结构”，让编辑页能直接保存 `gallery_id/query_path/unit/pageWhere`。
- 后续如果要继续逼近旧体验，可以在这个基础上再把树控件和图库选择器逐步替换回来。

## 验证

- 需要通过 `PageEditorEntry.vue` 的问题检查。
- 运行态联调后，重点看编辑页保存后后端是否能正确接收 `pageWhere` 数组。 