# 修复 vis 图库续载、图片路径与指标库旧菜单收口

## 问题

用户反馈本轮仍存在以下问题：

1. `/vis/gallery` 图片加载异常，且相较旧 vis 实现缺少大量内容。
2. `/vis/bigscreen/pages` 页面管理缩略图仍然加载失败。
3. 原有“指标库/指标查询”与 vis 下“指标库方案”未稳定收敛到同一内容页。
4. vis 后台日志出现 `Request method 'GET' not supported`。

## 根因

### 图库内容缺失

- 现有 [org-tribe-view/src/views/vis/GalleryList.vue](org-tribe-view/src/views/vis/GalleryList.vue) 只固定请求第一页 12 条数据。
- 旧 vis 的 [vis_screen/ant-design-vue-jeecg/src/views/gallery/GalleryList.vue](vis_screen/ant-design-vue-jeecg/src/views/gallery/GalleryList.vue) 使用滚动触底续载。
- 因此页面会表现成“少很多内容”，不是后端没返回，而是前端没继续取。

### 后台 GET/POST 报错

- 前端 `getBusinessTypeList()` 之前使用 `GET /vis/api/bussType/getAll`。
- 后端 [vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/controller/BussTypeController.java](vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/controller/BussTypeController.java) 中 `getAll` 仅支持 `POST`。
- 这正对应日志中的 `Request method 'GET' not supported`。

### 图片路径错误

- vis 后端图库与页面缩略图字段 `content` / `thumbnail` 在 mapper 中是原样返回，不会自动补全访问前缀。
- 这些文件实际应通过 vis 网关 ` /vis/api/** ` 转发到后端，再由 `/sys/common/static/**` 提供下载。
- 现有前端直接拼接 `window._CONFIG['domianURL'] + /相对路径`，会把 vis 资源错误地打到 GK 根路径下，导致图片 404。

### 指标库旧菜单未完全收口

- 之前只兼容了旧路径 `/indexLibrary`、`/statistics/indexLibrary`。
- 对旧组件名 `target/targetScheme`、`statistics/indexLibrary`、`statistics/schemeIndex` 的归一还不完整。
- 数据库菜单若残留这些旧 component 值，仍可能落到错误页面或旧页面。

## 修改

1. 新增 [org-tribe-view/src/utils/visMedia.js](org-tribe-view/src/utils/visMedia.js)
   - 统一解析 vis 图库和页面缩略图地址。
   - 相对路径一律归一到 `/vis/api/sys/common/static/...`。
   - 已带 `/sys/common/static/` 的历史值，会自动重写为 vis 网关路径。

2. 更新 [org-tribe-view/src/views/vis/GalleryList.vue](org-tribe-view/src/views/vis/GalleryList.vue)
   - 改为复用 `resolveVisMediaUrl`。
   - 恢复滚动触底续载行为。
   - 切换业务类型时重置列表并重新从第一页加载。

3. 更新 [org-tribe-view/src/views/vis/PageList.vue](org-tribe-view/src/views/vis/PageList.vue)
   - 页面缩略图改为复用同一套 vis 资源路径归一逻辑。

4. 更新 [org-tribe-view/src/api/visScreen.js](org-tribe-view/src/api/visScreen.js)
   - `getBusinessTypeList` 从 `GET` 改为 `POST`，与后端控制器保持一致。

5. 更新 [org-tribe-view/src/utils/util.js](org-tribe-view/src/utils/util.js)
   - 增加旧菜单组件 `target/targetScheme`、`statistics/indexLibrary`、`statistics/schemeIndex` 到 `vis/SchemeList` 的兼容映射。
   - 增加旧路径 `/statistics/schemeIndex` 到 `/vis/index-library` 的兼容映射。

## 验证

- 需要重新执行前端构建，确认：
  - 语法与打包通过。
  - 图库页不再触发 `bussType/getAll` 的 GET 请求报错。
  - 页面管理与图库图片请求路径改为 `/vis/api/sys/common/static/...`。

## 关于 Redis 警告

- 当前日志中的 Redis 连接失败来自 `192.168.160.30:6379` 不可达。
- 这类日志目前属于“先尝试 Redis，再回退 JWT 验签”的降级路径，不再直接把请求判成登录失效。
- 若要彻底消除该 warning，需要恢复 Redis 连通性，或单独调整 vis backend 当前环境的 Redis 配置。