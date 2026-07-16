# 指标库方案生产契约重建设计

## 1. 背景与目标

“可视化大屏 → 指标库方案”当前实现由代理补写，只实现了方案分页、删除以及不经预览直接写入图库的简化转图流程。该实现没有完整反映现场生产行为。

本次以以下证据确定功能边界，优先级从高到低：

1. `document/指标库方案页面fix/jeecg-boot-module-system-2.3.0.jar`：生产后端契约和数据库访问边界。
2. `document/指标库方案页面fix/指标库方案-生产演示.mp4`：现场页面布局和操作流程。
3. 需求文档中“转图方案”和“方案查询”的描述。
4. `document/指标库方案页面fix/jeecg-boot-parent.zip`：仅用于补充可读源码，不引入生产 JAR 中不存在或现场页面未开放的功能。

目标是恢复现场的方案查询、删除、转图配置、真实数据预览和预览后保存流程，同时明确数据库同步要求。

## 2. 范围

### 2.1 本次包含

- 按方案名称模糊查询。
- 按创建日期开始、结束时间查询。
- 查询条件重置。
- 分页展示序号、名称、创建时间、创建人和操作。
- 删除指标方案。
- 将已有指标方案打开为转图配置。
- 回填方案名称、方案条件、指标、维度及单位等生产方案数据。
- 支持柱状图、折线图、饼图、柱状折线图四类现场图表。
- 使用生产查询接口生成真实数据预览。
- 仅在预览成功后允许保存到图库。
- 数据库对象核验和兼容同步脚本。

### 2.2 本次不包含

- 地图、条形图、BigNumber。
- 参考地区分支新增的 `IndexStripController`、`IndexSpecialController`、`AreaUnitController` 等扩展能力。
- 新建指标方案、编辑原指标方案或指标树管理。
- 新建自定义业务表。
- 与本页面无关的大屏编辑器或图库重构。

## 3. 总体方案

采用“生产契约重建”方案：前端页面使用现场字段和交互；后端接口、参数、返回值和持久化流程以生产 JAR 为准；参考分支只补齐生产类的可读实现。

不得继续使用当前 `/indexLibraryScheme/toGallery` 的推测式转换。该接口绕过数据查询和图表预览，且自行推导 `condition`、`query_path`、`time_type`，不符合生产流程。

### 3.1 生产接口映射

| 功能 | 生产控制器 | 生产方法 |
| --- | --- | --- |
| 方案分页 | `IndexSchemeController` | `selectSchemeTable` |
| 删除方案 | `IndexSchemeController` | `deleteScheme` |
| 获取指标信息 | `IndexSchemeController` | `getIndexInfo` |
| 柱状/折线/柱折预览 | `IndexBarLineController` | `getIndexBarLineData` |
| 柱状/折线/柱折保存 | `IndexBarLineController` | `saveIndexBarLine` |
| 饼图预览 | `IndexPieController` | `getIndexPieData` |
| 饼图保存 | `IndexPieController` | `saveIndexPie` |

接口在当前部署拓扑中的网关前缀可以保留现有 `/vis/api` 适配，但控制器语义、请求字段和返回结构必须与生产契约一致，前端不得依赖代理自定义字段。

## 4. 页面设计

### 4.1 方案列表

查询区包含：

- 方案名称：绑定 `name`，后端映射为 `schemeDescr` 进行模糊匹配。
- 创建日期：分别提交 `begin_time` 和 `end_time`，格式为 `YYYY-MM-DD`。
- 查询按钮：回到第一页并按当前条件加载。
- 重置按钮：清空名称和日期后回到第一页重新加载。

表格列与现场保持一致：

1. 序号。
2. 名称，对应 `SCHEME_DESCR`。
3. 创建时间，对应 `ADD_DATE`。
4. 创建人，对应 `realname`。
5. 操作：转图、删除。

分页请求使用 `pageNo` 和 `pageSize`；响应使用生产的 `result`、`rows` 和 `total`。

### 4.2 转图弹窗

点击“转图”后打开大尺寸弹窗。弹窗初始化时必须使用选中记录的生产字段，并解析 `SCHEME_CONDITON`；解析失败时提示方案数据异常，不进入可保存状态。

弹窗至少包含：

- 方案名称：只读。
- 图表标题：必填，可修改。
- 原方案筛选条件及时间范围。
- 指标信息。
- 维度配置。
- 单位值和单位。
- 图表类型图标。
- 图表预览区。
- 保存、取消按钮。

图表类型仅保留：

- `1`：柱状图。
- `2`：折线图。
- `3`：饼图。
- `4`：柱状折线图。

不展示地图、条形图或 BigNumber，也不向后端发送这些类型。

## 5. 数据流

### 5.1 列表与删除

1. 前端提交 `name`、`begin_time`、`end_time`、`pageNo`、`pageSize`。
2. `IndexSchemeController.selectSchemeTable` 计算分页偏移并查询方案总数和列表。
3. 列表查询从 `visual_screen.vs_lib_index_scheme` 读取方案，关联 `visual_screen.sys_user` 获取创建人。
4. 删除时前端提交 `schemeId`。
5. `IndexSchemeController.deleteScheme` 删除对应 `vs_lib_index_scheme` 记录，成功后刷新当前页；若当前页已空则回退一页。

### 5.2 转图初始化

1. 前端读取列表记录中的 `ID`、`SCHEME_DESCR`、`SCHEME_CONDITON` 和指标信息。
2. 解析方案条件，恢复时间粒度、时间范围、维度、单位及指标字段。
3. 调用 `IndexSchemeController.getIndexInfo` 获取生产指标元数据。
4. 未完整取得必要数据时禁用预览和保存，并显示明确错误。

### 5.3 预览

1. 用户选择图表类型并调整标题或配置。
2. 柱状图、折线图和柱状折线图调用 `getIndexBarLineData`。
3. 饼图调用 `getIndexPieData`。
4. 请求使用生产 `condition` 字段及控制器需要的方案、指标、时间、维度和单位参数。
5. 只有接口返回 `result=success` 且包含可渲染数据时才渲染预览并记录当前配置已通过预览。
6. 任一影响图表数据或样式的字段变化后，清除“已预览”状态，必须重新预览。

### 5.4 保存

1. 保存按钮在当前配置预览成功前保持禁用。
2. 柱状图、折线图和柱状折线图调用 `saveIndexBarLine`。
3. 饼图调用 `saveIndexPie`。
4. 保存请求沿用预览成功时的 `condition`，并携带图表内容和标题。
5. 生产控制器通过 `GalleryService.add` 写入 `visual_screen.vs_gallery_info`，同时保存方案关联字段。
6. 保存成功后关闭弹窗并提示“添加成功”；保存失败时保留配置和预览，允许重试。

## 6. 后端边界

后端重建时遵守以下约束：

- 生产 JAR 已存在的类、方法和请求字段优先原样恢复。
- 参考分支中同名生产类可作为源码基础，但必须逐项与 JAR 类签名、Mapper SQL 和字节码关键流程对照。
- 不移植生产 JAR 中不存在的扩展控制器。
- 列表查询保持生产返回字段，前端不再依赖 `name`、`create_time` 等代理别名。
- 图库写入统一经过 `GalleryService.add`，不在指标方案模块维护第二套图库插入 SQL。
- SQL 改写仅限 MySQL 到当前 Vastbase 的必要语法兼容，如分页、字符串拼接和日期处理；业务条件不改变。
- 对空方案 ID、无效条件 JSON、空指标、无数据源和查询失败分别返回可识别错误，不使用空指针或静默降级。

## 7. 数据库对象与同步结论

### 7.1 不新增业务表

生产实现直接依赖已有对象：

- `visual_screen.vs_lib_index_scheme`：指标方案。
- `visual_screen.vs_gallery_info`：保存后的图库组件。
- `visual_screen.sys_user`：创建人名称。
- `indicators_lib.lib_index_relation`：指标名称和元数据。
- `visual_screen.f_get_IndexName`：列表中的指标名称转换函数。

本次不设计任何新业务表。

### 7.2 必须提供结构核验/兼容同步脚本

当前仓库的 Vastbase 初始化脚本没有完整检出上述生产对象定义，同时当前 `GalleryMapper.xml` 仍存在按旧 11 列顺序执行无列名 `INSERT` 的实现，而生产 JAR 明确按以下字段保存图库：

- `id`
- `option`
- `query_path`
- `content`
- `type`
- `title`
- `sort`
- `state`
- `business_id`
- `time_type`
- `dimension_type`
- `dacct_radio`
- `title_old`
- `add_time`
- `add_user`
- `index_scheme_id`
- `index_scheme_name`
- `condition`

因此交付中需要包含幂等核验/同步脚本：

1. 对缺失的生产表或函数给出明确失败提示或按已确认的生产结构创建。
2. 对 `vs_gallery_info` 缺失的生产字段执行兼容补列。
3. 将图库插入改为显式列名，禁止依赖表列顺序。
4. 核验 `vs_lib_index_scheme` 至少包含生产查询、回填所需字段：`ID`、`SCHEME_DESCR`、`SCHEME_SQL`、`SCHEME_COLUMS`、`SCHEME_CONDITON`、`ADD_USERID`、`ADD_DATE`。
5. 核验 `f_get_IndexName` 位于 `visual_screen` schema；仓库现有脚本只发现了 `seo.f_get_IndexName`，不能视为已满足生产依赖。
6. 脚本必须幂等，并提供执行前后查询结果。

## 8. 错误处理

- 列表失败：保留查询条件，显示后端消息，表格退出加载态。
- 日期倒置：前端阻止查询并提示开始日期不能大于结束日期。
- 方案条件不可解析：阻止进入预览，提示方案数据异常。
- 预览无数据：展示空状态，不允许保存。
- 预览接口失败：保留用户配置，允许重新预览。
- 保存失败：不关闭弹窗，不清空预览。
- 删除失败：不移除本地行，显示后端错误。

## 9. 测试与验收

### 9.1 后端契约测试

- `selectSchemeTable` 正确传递名称、日期和分页条件。
- 列表字段与生产字段一致。
- `deleteScheme` 使用 `schemeId`。
- 四种图表类型映射到正确预览和保存接口。
- 无效图表类型被拒绝。
- 保存通过 `GalleryService.add` 写入完整生产字段。
- 数据库兼容 SQL 在 Vastbase 下可解析。

### 9.2 前端测试

- 名称、开始日期、结束日期查询和重置。
- 日期倒置校验。
- 分页和删除后的页码处理。
- 转图弹窗正确回填生产方案。
- 页面只出现四种图表类型。
- 各图表类型调用正确接口。
- 配置变化会使已预览状态失效。
- 未预览、预览失败、预览无数据时不能保存。
- 保存失败时配置不丢失。

### 9.3 浏览器验收

- 页面布局与生产演示视频一致。
- 四类图标、表单和预览区在目标分辨率下无错位。
- 查询、重置、转图、预览、保存、删除完整走通。
- 网络请求的 URL、请求体和响应处理符合生产契约。
- 控制台无新增错误或警告。
- 图库页可读取并展示新保存的图表。

## 10. 完成标准

- 当前简化直写图库流程已移除。
- 列表和删除对齐生产 `IndexSchemeController`。
- 四类预览和保存对齐生产 `IndexBarLineController`、`IndexPieController`。
- 未引入地图、条形图、BigNumber 和其他地区扩展能力。
- 不新增业务表，并交付数据库对象核验/兼容同步脚本。
- 自动化测试、构建和真实浏览器验收通过。
