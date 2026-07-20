# 指标库方案生产界面纠偏设计

## 背景

`master` 已恢复生产 JAR 中的指标方案列表、删除、图表预览和保存接口，
但当前转图页面采用了自行设计的左右双栏界面，没有完整还原生产演示。
同时，现有 `deploy-package` 仍打包着功能提交前的旧简化弹窗，因此现场页面
仍显示“方案名称 / 图库标题 / 图表类型 / 确定”。

本次纠偏同时解决两个问题：

1. 以生产演示视频还原转图界面的布局和操作顺序；
2. 重新生成并核验最终前后端部署 JAR，避免只提交源码、不更新运行产物。

## 权威来源与优先级

发生冲突时按以下顺序裁决：

1. `document/指标库方案页面fix/jeecg-boot-module-system-2.3.0.jar`
   决定后端接口、请求字段和功能边界；
2. `document/指标库方案页面fix/指标库方案-生产演示.mp4`
   决定现场可见布局、控件和交互顺序；
3. 需求文档中的“转图方案”和“方案查询”描述；
4. `document/指标库方案页面fix/jeecg-boot-parent.zip` 中的
   `TurnMap.vue` 仅用于补充生产视频中可辨认的实现细节。

参考分支独有功能不得进入本次实现。

## 功能边界

### 保留

- 方案名称和创建日期区间查询、重置、分页；
- 删除方案；
- 柱状图、折线图、饼图、柱状折线图；
- 根据当前配置请求真实预览数据；
- 生成图片后保存到图库；
- 图表颜色、渐变和比率等生产演示中出现的配置；
- 柱折图的指标方向配置。

### 排除

- 地图、条形图、BigNumber、标准地图；
- 参考地区分支新增的 Controller 或 API；
- `/vis/api/indexLibraryScheme/toGallery`；
- 客户端提交 `schemeSql`；
- 为满足参考源码而新增生产 JAR 中不存在的业务表或 Controller。

## 转图弹窗设计

### 布局

- 使用生产演示中的 `80%` 宽单列纵向弹窗，不采用左右双栏。
- 弹窗标题使用“方案名称 - 转图”，方案名称为空时回退为“转图”。
- 顶部依次展示四个图标单选：柱状图、折线图、饼图、柱状折线图。
- 图标下面是生产顺序的公共配置和图型专属配置。
- “生成图片”成功后，在表单下方显示深色 ECharts 预览。
- 弹窗底部为“关闭”和“确定”；未成功生成当前配置时禁止确定保存。

### 公共配置

- 图表标题：可编辑、必填；
- 指标：按方案回显、只读；
- 维度：按方案回显、禁用；
- 周期：按方案回显、禁用；
- 单位：按方案回显、禁用；
- 时间类型：严格根据方案的 `dacct_radio` / `timeType` 显示可选项；
- 选择时间：根据日、月、季、年显示对应日期控件，不使用自由文本框。

### 图型专属配置

柱状图、折线图、柱状折线图：

- 横轴显示可选“账期”或当前维度名称（国库/地区）；
- 横轴为账期时选择一个国库或地区；
- 横轴为维度时选择具体账期；
- 生成成功后允许选择图表颜色和是否渐变；
- 柱状折线图额外显示指标方向表，只允许柱状或折线，并可删除指标行。

饼图：

- 统计方向可选“指标”或“维度”；
- 按方向选择国库/地区或指标；
- 显示“是否包含比率”；
- 生成成功后允许选择图表颜色；
- 不显示渐变开关。

### 状态规则

- 打开弹窗时从原始方案条件初始化，不猜测 `dimension_type`；
- 任意影响请求或图表 option 的配置变化，都使已生成状态失效；
- “生成图片”只调用生产预览接口；
- 只有最新一次预览请求可以更新画面，过期响应必须丢弃；
- 保存使用最后一次成功生成时冻结的条件和 `data:image` 缩略图；
- 关闭、切换方案和重复打开时，清理上一条方案的异步状态。

## 接口设计

列表、删除和指标信息：

- `POST /vis/api/indexSchemeController/selectSchemeTable`
- `POST /vis/api/indexSchemeController/deleteScheme`
- `POST /vis/api/indexSchemeController/getIndexInfo`

柱状、折线和柱折：

- `POST /vis/api/IndexBarLine/getIndexBarLineData`
- `POST /vis/api/IndexBarLine/saveIndexBarLine`

饼图：

- `POST /vis/api/IndexPie/getIndexPieData`
- `POST /vis/api/IndexPie/saveIndexPie`

界面所需的候选项优先使用方案条件和上述接口返回的数据。若生产 JAR
没有提供额外候选项接口，界面只能显示方案已限定的候选项，不得引入参考
地区分支的 `getDateInterval`、`getDimensionSelect` 或树接口作为新依赖。

## 组件边界

- `IndexLibraryList.vue`：查询、列表、分页、删除、打开转图；
- `IndexLibraryConvertModal.vue`：弹窗状态机、预览/保存请求、竞态控制；
- `IndexLibraryConvertForm.vue`：生产顺序表单和图型专属控件；
- `IndexLibraryChartPreview.vue`：生产深色 ECharts option 和截图；
- `indexLibraryScheme.js`：条件规范化、校验、预览/保存载荷纯函数；
- `indexLibraryScheme.test.mjs`：请求契约、状态规则和模板契约回归测试。

## 数据库结论

不新增业务表。继续使用既有：

- `visual_screen.vs_lib_index_scheme`
- `visual_screen.vs_gallery_info`
- `visual_screen.sys_user`
- `indicators_lib.lib_index_relation`
- `edw.cm_guoku_dimnsn`

现有同步脚本只负责校验这些表、补齐允许缺失的图库字段并创建或替换
`visual_screen.f_get_IndexName`；不得创建整张业务表。

## 验证与交付

1. 测试先行增加生产布局和交互的失败断言；
2. 运行指标方案前端契约测试和 Vue 模板编译；
3. 运行后端指标方案 JUnit 测试；
4. 完整构建 `org-tribe-view/dist`；
5. 重新打包 `org-tribe-system-2.1.0.jar`；
6. 重新打包包含 `indexlib` 的 `vis-screen-2.3.0.jar`；
7. 刷新 `deploy-package/app`；
8. 从最终 JAR 内检查：
   - 存在“生成图片”和生产弹窗结构；
   - 不存在“图库标题”和 `/indexLibraryScheme/toGallery`；
   - `vis-screen` JAR 中存在三个生产 Controller；
9. 使用真实浏览器验证列表和四类转图流程。没有现场登录凭据时，
   浏览器验证必须明确标记为生产形状 API mock，不能冒充真实接口验证；
10. 实际运行环境只有在替换 JAR、重启并核验静态资源哈希后才可宣称更新。
