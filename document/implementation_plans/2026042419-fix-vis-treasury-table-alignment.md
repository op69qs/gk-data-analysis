# vis 国库表格对齐修复

## 问题

- 页面 `http://localhost:9090/vis/system/treasury` 的表头被全局样式居中。
- `TreasuryList.vue` 里的大多数列没有显式 `align`，数据单元格保持默认左对齐。
- 结果是表头居中、内容左对齐，视觉上看起来“没有对齐”。

## 定位结论

- 运行态检查确认：
  - `th` 的 `text-align` 为 `center`
  - 第一列 `td` 的 `text-align` 为 `left`
- 同页只有“操作”列显式配置了 `align: 'center'`，其余列没有显式对齐规则。

## 修改

- 在 `org-tribe-view/src/views/system/TreasuryList.vue` 中为非操作列显式补上 `align: 'left'`。
- 在同文件补充表头样式覆盖，纠正页面现有全局 `th { text-align:center !important; }` 对 Treasury 表头的误覆盖。
- 保留“操作”列为 `align: 'center'`，避免影响按钮区展示。

## 预期结果

- 国库表格的表头和单元格按照同一列对齐规则渲染。
- 文本列维持左对齐，可读性不变。
- 页面不再出现“表头居中、内容偏左”的错位感。