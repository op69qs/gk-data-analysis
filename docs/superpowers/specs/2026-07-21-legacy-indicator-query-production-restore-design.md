# 旧“指标查询”生产页面恢复设计

## 背景

数据分析平台问题 5 表现为：旧“指标库 / 指标查询”菜单在信创环境打开“方案描述 / 包含指标”列表页，而生产环境打开的是包含维度、账期、公共指标、我的指标和查询结果的指标查询页。

旧统计菜单与 `vis` 下“指标库方案”是两套独立菜单。本次修复不得修改 `/vis/index-library`、`vis/IndexLibraryList` 及其菜单权限。

## 已确认根因

1. 旧“指标查询”菜单记录被写成：
   - `url = /statistics/schemeIndex`
   - `component = statistics/schemeIndex`
2. `schemeIndex.vue` 的职责是公共方案列表；用户点击“执行”后，它才跳转到 `/statistics/indexLibrary`。
3. 生产截图对应 `statistics/indexLibrary.vue` 的原始查询页面。
4. 当前 `indexLibrary.vue` 在历史 vis 整合过程中只做了不完整恢复，相比原始生产实现少 372 行模板内容，缺少公共指标、我的指标、查询条件及结果区域。
5. 现场 `jeecg-boot-module-system-2.3.0.jar` 的有效静态资源不包含旧 `schemeIndex` 页面特征；该 JAR 中的 `org.jeecg.modules.indexlib` 也不是旧查询页使用的完整 `indicatorsLib` 服务，因此不能把该 JAR 的页面结构当作问题 5 的生产页面来源。
6. 旧查询页依赖的 `selectIndexRelationTree`、`selectPublicScheme`、`selectSchemeData` 等接口仍由仓库中的 `indicatorsLibv-1.0` 提供。

## 目标行为

- 点击旧“指标库 / 指标查询”后直接打开 `/statistics/indexLibrary`。
- 页面恢复生产布局和能力：
  - 维度及账期选择；
  - 公共指标树；
  - 我的指标树；
  - 查询条件、结果表格；
  - 保存方案、查看方案、图表和下载等原有操作。
- `statistics/schemeIndex.vue` 保持独立，继续作为公共方案列表页；其“执行”操作仍可携带方案参数进入 `/statistics/indexLibrary`。
- `vis` 菜单、路由、组件和接口均不发生变化。

## 实施设计

### 1. 回归测试

新增轻量静态回归测试，首先在当前代码上复现失败，并验证以下生产契约：

- 旧菜单修复 SQL 的目标必须是 `/statistics/indexLibrary + statistics/indexLibrary`。
- `indexLibrary.vue` 必须包含“公共指标”“我的指标”及查询结果相关生产结构。
- `schemeIndex.vue` 仍跳转到 `/statistics/indexLibrary`。
- 修复文件不得把旧统计入口归一到 `/vis/index-library`。

### 2. 页面恢复

以仓库初始生产版本 `84e5e01` 中的 `org-tribe-view/src/views/statistics/indexLibrary.vue` 为基线，完整恢复页面，而不是继续拼补当前残缺模板。

不借此修改接口、字段、交互或页面样式，避免引入超出生产行为的设计。

### 3. 菜单修复

新增一份独立、可重复执行的现场 SQL：

- 仅匹配指定旧菜单 ID、父菜单 ID 和名称；
- 将 URL、组件恢复为 `statistics/indexLibrary`；
- 不更新任何 `vis` 菜单记录；
- 执行后提供目标菜单及 vis 菜单的回读查询；
- 提供对应回退 SQL，将该记录恢复为执行前的 `statistics/schemeIndex` 值。

同时修正尚可能被新环境执行的菜单汇总 SQL，避免重新初始化时再次写入错误入口。

### 4. 文档纠偏

修正此前将 `schemeIndex` 判断为旧“指标查询”正确入口的实现记录，明确：

- `schemeIndex` 是公共方案列表；
- `indexLibrary` 是菜单直接打开的生产查询页；
- 两者与 `vis` 指标库方案均保持独立。

## 验证方案

1. 运行新增回归测试，确认修改前失败、修改后通过。
2. 使用 `vue-template-compiler` 编译恢复后的单文件组件模板，确认无模板编译错误。
3. 执行前端生产构建，确认 `indexLibrary.vue` 被正常打包。
4. 检查构建产物包含“公共指标”“我的指标”和旧查询接口特征。
5. 对 SQL 做静态检查，确认只更新目标旧菜单，不包含对 vis 菜单的更新。
6. 若现场数据库可连接，在事务中预演 SQL 并回读受影响行；未获得现场数据库连接时，不声明数据库已执行。

## 不在本次范围

- 不修改 `/vis/index-library`。
- 不合并 `statistics` 与 `vis` 页面。
- 不重构旧指标查询业务逻辑。
- 不修改指标库表结构。
- 不用现场 JAR 中的新 `indexlib` 页面替代旧 `indicatorsLib` 查询页面。

## 验收标准

- 旧“指标查询”菜单直接进入生产指标查询页。
- 公共方案列表页仍可独立访问并执行方案。
- vis 指标库方案页面行为不变。
- 前端构建和回归测试通过。
- 提供可直接在现场执行、可回读、可回退的菜单修复 SQL。
