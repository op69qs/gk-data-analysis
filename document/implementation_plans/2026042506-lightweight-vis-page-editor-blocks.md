# vis 页面编辑最小块编辑切片

## 背景

当前主仓已经恢复了 `/vis/bigscreen/pages/editor` 路由承接、页面基础信息保存，以及页面管理列表的新增/编辑/发布/删除主流程。

但 `PageEditorEntry.vue` 仍然只是“基础信息表单 + page_sub 只读列表”，离旧 `AddTemplate.vue` 至少还差一层“模板预设生成区块”和“轻量编辑区块内容”的最小可用能力。

## 本次改动

1. 从旧 `vis_screen` 的 `AddTemplate.vue` 中抽出模板 1-7 的默认区块布局预设，内嵌到主仓 `PageEditorEntry.vue`。
2. 在页面编辑承接页中，模板切换后可直接按预设生成 `page_sub`，不恢复旧拖拽器，只保留最小块列表生成能力。
3. 将 `page_sub` 从只读列表升级为轻量可编辑列表，支持修改：
   - 区块标题
   - 区块类型
   - 内容
   - 图库 ID
   - 时间类型/时间区间
   - 位置尺寸 `x/y/w/h`
4. 保存时继续复用已恢复好的 `/vis/api/pageInfo/add`、`/vis/api/pageInfo/edit` 和 `/vis/api/pageSub/getAll`，不新增后端接口。
5. 页面列表进入 editor 时，多带一个 `template` 查询参数，便于编辑页初始化模板编号。

## 取舍

- 这次不恢复旧 `vue-grid-layout` 拖拽布局，也不恢复图库模块设置抽屉。
- 这次只做“能生成区块、能改基础字段、能保存”的过渡态，先保证 editor 不再只是说明页。
- 真正的大屏可视化编辑器恢复可以后续直接替换 `PageEditorEntry.vue`，数据库菜单与动态路由不需要再改。

## 验证

- 需要至少通过 `PageEditorEntry.vue` 和相关 API 封装的编辑器问题检查。
- 如果本地运行环境允许，后续再补实际新增页面、编辑页面的接口联调验证。