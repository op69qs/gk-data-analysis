# 修复 vis 模板管理与图库展示

## 问题

- 模板管理页使用了当前仓库另一组 `1.png/2.png/...` 资源，和旧 vis 的模板预览图不一致。
- 模板管理页顶部额外提示文案没有保留必要。
- 图库页标题被单行省略，且标题与“编辑”操作视觉上挤在中间，不符合当前页面预期。
- 数据字典页排查后确认当前 `/vis/system/dict` 是复用 GK 通用 `system/DictList`，不是独立 vis 页面；这项若要继续调整，需要在“保留别名入口”与“切换到其他 GK 页面/口径”之间再做业务确认。

## 定位结论

- `org-tribe-view/src/views/vis/TemplateList.vue` 里模板数据是前端硬编码，本次错误直接来自资源引用写成 `@/assets/1.png` 等文件。
- 旧 vis 对应资源位于 `vis_screen/ant-design-vue-jeecg/src/assets/template1.png` 到 `template7.png`。
- `org-tribe-view/src/views/vis/GalleryList.vue` 当前用 `white-space: nowrap` 和 `text-overflow: ellipsis` 压缩标题，导致长标题无法完整显示。
- 图库动作区本身就是单个 actions 插槽，问题集中在标题与操作链接的布局样式，不需要改接口和数据结构。

## 修改

- 将旧 vis 模板预览图 `template1.png` 到 `template7.png` 复制到 `org-tribe-view/src/assets/`。
- 更新 `TemplateList.vue`：
  - 删除顶部提示条。
  - 模板数据源切回 `template1.png` 到 `template7.png`。
  - 预览图改为 `object-fit: contain`，避免图片被裁切。
- 更新 `GalleryList.vue`：
  - 标题改为多行自然换行，不再省略。
  - 标题显式左对齐。
  - “编辑”链接固定靠右，不随标题长度挤到中间。
  - 补充 `ant-card-actions` 层级样式，避免动作区默认居中影响布局。

## 验证

- 对 `TemplateList.vue`、`GalleryList.vue` 运行编辑器诊断，未发现语法错误。
- 尝试执行 `npm run lint -- src/views/vis/TemplateList.vue src/views/vis/GalleryList.vue`，脚本返回文件被 ignore 的警告，未形成有效 lint 结果。
