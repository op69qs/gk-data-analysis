# vis 集成重开范围审计

## 审计目标

在继续恢复历史工作前，先确认 `gk-data-analysis` 当前代码树里哪些 vis 集成实现已经丢失，哪些仍然可以直接从同工作区的 `vis_screen` 仓库复用。

本次不直接恢复代码，只做代码面审计和重开顺序收口，避免后续从错误的断点开始重做。

## 已确认现状

### 1. `gk-data-analysis` 当前不存在 `vis-screen-backend`

- 当前仓库根目录只有：
  - `dwbi-statistical-analysis`
  - `dwbi-system-docking`
  - `fixedReport`
  - `indicatorsLibv-1.0`
  - `inspect`
  - `org-tribe-system`
  - `org-tribe-view`
  - `seo`
- 不存在历史实施文档中反复提到的 `vis-screen-backend/` 目录。

这说明：

- `2026042305-vis-screen-backend-in-repo-packaging.md` 记录的“vis 后端并入 GK 主仓”这部分实现，目前没有体现在实际代码树里。

### 2. 根聚合 `pom.xml` 未接入 vis 子聚合

当前根 `pom.xml` 的 `<modules>` 只有：

- `dwbi-statistical-analysis`
- `dwbi-system-docking`
- `fixedReport`
- `indicatorsLibv-1.0`
- `org-tribe-system`
- `seo`
- `deploy-package-assembly`

但当前仓库根目录同样不存在 `deploy-package-assembly/` 目录，也不存在 `vis-screen-backend/`。

这说明：

- 聚合层和统一装配层都处于“不一致状态”。
- 当前代码面不能支撑历史文档里描述的“vis 一起构建、一起装配、一起启动”。

### 3. `org-tribe-view` 当前没有 vis 迁移页面

当前 `org-tribe-view/src/views/` 下不存在：

- `vis/`

同时当前 `org-tribe-view/src/api/` 下不存在：

- `visScreen.js`

当前 `org-tribe-view/src/utils/util.js` 仍是未加 vis 旧组件名兼容的基线版本：

- 没有 `normalizeLegacyComponent(...)`
- 没有 `BigScreen/* -> vis/*` 的组件归一逻辑
- 没有旧 `/indexLibrary`、`/gallery`、`/BigScreen*` 到 `/vis/...` 的前端路径收口

这说明：

- 历史上已经迁入 GK 的 `PreviewEntry`、`SchemeList`、`PageList`、`TemplateList`、`GalleryList`、预览页和系统管理兼容页，目前都不在 `gk-data-analysis` 代码里。

### 4. `org-tribe-system` 当前也没有 vis 网关接入痕迹

当前 `org-tribe-system/src/main/resources/application-dev.yml` 的 Zuul 路由里仍只有：

- `talentpool`
- `infoActive`
- `inspection`
- `indicatorsLib`
- `seo`
- `smartLibrary`
- `fixedReport`

没有：

- `/visScreen/**`
- `/vis/api/**`
- `VIS_SCREEN_BACKEND_URL`

这说明：

- 历史文档里记录的 `visScreen` 或 `vis/api` 网关转发链路，目前没有落在源码配置里。

### 5. 同工作区 `vis_screen` 仓库仍保留原始可复用来源

`vis_screen` 当前仍存在：

- `vis_screen/ant-design-vue-jeecg/src/views/BigScreen/`
- `vis_screen/ant-design-vue-jeecg/src/views/gallery/`
- `vis_screen/ant-design-vue-jeecg/src/views/system/BusinessTypeList.vue`
- `vis_screen/ant-design-vue-jeecg/src/views/system/TreasuryList.vue`
- `vis_screen/jeecg-boot/jeecg-boot-base-api/`
- `vis_screen/jeecg-boot/jeecg-boot-base-common/`
- `vis_screen/jeecg-boot/jeecg-boot-module-system/`

这说明：

- 当前不是“历史实现完全无源可追”。
- vis 并入 GK 主仓这条线，可以直接以 `vis_screen` 当前仓库为代码来源重新抽取，而不是从零手写。

## 审计结论

当前最值得重开的不是主仓通用 BES 基线，而是 vis 集成整条实现链。

按代码层优先级，应分成四个重开阶段：

### 阶段 1: 恢复后端聚合与装配骨架

目标：

- 重新把 `vis-screen-backend` 引入 `gk-data-analysis`
- 恢复 `deploy-package-assembly` 目录与装配逻辑
- 让根聚合重新具备“能一起构建 vis”的能力

优先依据：

- `2026042305-vis-screen-backend-in-repo-packaging.md`
- `2026042413-fix-vis-screen-backend-parent-pom.md`
- `2026042414-fix-vis-screen-backend-autopoi-resolution.md`
- `2026042415-fix-vis-screen-backend-mybatis-plus-version.md`
- `2026042416-fix-vis-screen-backend-bes-spring-alignment.md`
- `2026042417-fix-vis-screen-backend-dev-eureka-startup.md`

### 阶段 2: 恢复 GK 网关与前端 vis 入口

目标：

- 恢复 `org-tribe-system` 中的 vis 网关路由
- 恢复 `org-tribe-view` 中的 `src/views/vis/*` 和 `src/api/visScreen.js`
- 恢复前端对旧 vis 组件名和旧路径的兼容归一

优先依据：

- `2026042302-vis-screen-integration-execution-log.md`
- `2026042304-vis-screen-business-menu-integration.md`
- `2026042406-fix-vis-legacy-menu-component-routing.md`
- `2026042413-fix-visscreen-route-and-index-menu.md`

### 阶段 3: 恢复 `/vis/...` 菜单与数据库迁移脚本

目标：

- 恢复 GK 菜单 seed
- 恢复旧路径向 `/vis/...` 的实库迁移脚本
- 收口 BigScreen / gallery / indexLibrary 历史路径

优先依据：

- `2026042303-gk-vis-menu-seed.sql`
- `2026042411-remove-vis-legacy-routes.md`
- `2026042411-vis-route-prefix-migration.sql`
- `2026042412-add-vis-system-management-menus.sql`
- `2026042412-live-db-vis-route-update.md`

### 阶段 4: 恢复 vis 字典边界

目标：

- 恢复 old vis 数据字典白名单、补种和排序
- 明确这部分逻辑留在 `vis-screen-backend`，不再散落在 GK 主系统里

优先依据：

- `2026042501-filter-vis-legacy-dicts.md`
- `2026042502-restore-vis-legacy-dict-seeds.md`
- `2026042503-migrate-vis-dict-to-vis-screen-backend.md`
- `2026042504-remove-gk-vis-dict-compat.md`

## 建议的实际重开顺序

1. 先恢复 `vis-screen-backend` 和 `deploy-package-assembly` 的目录与聚合关系。
2. 再恢复 `org-tribe-system` 的 vis 网关配置。
3. 再恢复 `org-tribe-view/src/views/vis/*`、`src/api/visScreen.js`、`src/utils/util.js` 的兼容逻辑。
4. 然后补回菜单 SQL 和 `/vis/...` 路由迁移脚本。
5. 最后收口 vis 字典和本地 dev 启动细节。

## 本次审计的直接结论

- `gk-data-analysis` 当前代码树里，vis 集成实现基本可以视为“未落地”。
- 历史实施文档已恢复，可作为恢复顺序和边界依据。
- `vis_screen` 工作区仍保留原始前后端源码，是下一步恢复代码的直接来源。