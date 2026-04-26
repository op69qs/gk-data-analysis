# Implementation Plan: vis_screen 以微服务形式并入 GK 主系统

## Overview

本计划用于将 vis_screen 改造成 gk-data-analysis 体系下的业务微服务与前端业务模块。目标状态是：前端代码直接并入 org-tribe-view，后端保留独立 vis 服务；vis 不再保留独立登录、注册、用户、角色、权限管理能力，也不再保留独立 Nexus OAuth SSO 能力；认证与用户身份完全复用 gk-data-analysis 的现有平台能力；权限按页面级接入 GK 主系统菜单树；现有图库、页面、方案、预览、运行时查询等业务能力和数据结构继续保留。

## Architecture Decisions

- 前端并入 org-tribe-view，而不是保留独立前端并做菜单跳转。
  理由：你已经明确要直接并入代码；这样可以彻底消除双入口、双登录页、双路由守卫。
- vis 后端保留为独立微服务，经 GK 的 Zuul 网关转发访问。
  理由：满足“微服务形式”要求，同时不把 vis 业务域重新塞回 org-tribe-system 单体。
- vis 服务直接校验 GK token，但用户、角色、权限的权威数据源改为 gk-data-analysis，不再依赖 vis 本地 sys_user、sys_role、sys_permission；Redis 可以共享实例，但必须统一 `prefix_user_token_` 前缀与 token TTL 语义。
  理由：两个系统当前用户/权限/角色是独立的，本次整合要求废弃 vis 侧相关逻辑并统一使用分析平台能力，因此 vis 只能保留业务域，不再保留本地平台身份治理职责；当前两边头名、签名算法、token key 前缀一致，差异主要在 vis 侧历史上使用了 `2 * EXPIRE_TIME` 的 Redis TTL，需要先收敛到 GK 基线。
- 权限粒度按页面级接入 GK 主系统 sys_permission 菜单体系。
  理由：这是当前需求已明确的权限颗粒度，优先完成边界收口，避免首期把按钮级权限一并放大。
- vis 后端彻底收口平台能力，只保留 visualScreen 业务域接口，并移除 NexusOAuthConfig、NexusOAuthCallbackController、NexusSyncHintController 这条独立 SSO 线。
  理由：这次需求不是隐藏页面，而是取消 vis 自己的平台治理职责；独立 SSO 与独立用户体系都会与目标边界冲突。

## Dependency Graph

```text
GK token / GK 用户权限权威源对齐
    |
    +- Zuul 路由转发 vis 服务
    |    |
  |    +- vis 服务认证适配
    |    |    |
  |    |    +- vis 废弃本地用户/权限/角色逻辑
  |    |    +- vis 业务接口可被 GK 前端访问
    |    |
  |    +- Nexus OAuth 独立链路移除
  |    |
    |    +- 页面级权限菜单接入
    |
    +- org-tribe-view 路由/API 合并
         |
         +- 预览页迁入
         +- 配置页迁入
         +- 独立登录与权限页面删除/下线
```

实施顺序遵循“先切换认证权威源并移除独立 SSO，再打通网关与最小接口，再迁最小前端切片，再扩展业务页面，最后清理旧入口”的顺序。

## Task List

### Phase 1: Foundation

- [ ] Task 1: 确认 GK token 与 vis 服务认证兼容基线
- [ ] Task 2: 新增 vis 网关路由并打通最小受保护接口

## Task 1: 确认 GK token 与 vis 服务认证兼容基线

**Description:**
梳理并固定 GK 与 vis 的认证公共前提，包括 X-Access-Token 请求头、JwtUtil 签名规则、Redis token 生命周期，以及 vis 后续不再依赖本地 sys_user、sys_role、sys_permission 作为认证授权来源后的新权威边界。输出结论是：vis 服务后续直接校验 GK token 时，依赖 gk-data-analysis 的哪些用户与权限能力，哪些实现必须统一，哪些本地逻辑必须废弃。

**Acceptance criteria:**
- [ ] 明确 GK 与 vis 当前 JwtUtil、ShiroRealm、Redis token key 规则是否可直接兼容。
- [ ] 明确 vis 服务切换到 gk-data-analysis 作为用户/权限权威源后的实现路径。
- [ ] 形成一份认证兼容结论，作为后续实现约束。

**Current findings:**
- [x] `X-Access-Token` 请求头在 GK 与 vis 中保持一致。
- [x] `JwtUtil` 签名算法与 `EXPIRE_TIME` 常量在 GK 与 vis 中保持一致。
- [x] `CommonConstant.PREFIX_USER_TOKEN` 在 vis 中为 `prefix_user_token_`，与 GK 当前使用方式兼容。
- [x] vis 历史差异点为 Redis token TTL 使用 `JwtUtil.EXPIRE_TIME * 2 / 1000`；执行计划时需先统一到 GK 的 `JwtUtil.EXPIRE_TIME / 1000`。
- [x] GK 当前网关基于 Zuul + Eureka，vis 当前源码默认注册到 Nacos；在注册中心统一前，Task 2 先采用 Zuul `url` 路由打通链路，而不是直接依赖 `serviceId` 发现。

**Verification:**
- [ ] 自检：对照 GK 与 vis 的 ShiroRealm、ShiroConfig、JwtUtil 使用链路。
- [ ] 自检：确认请求头仍统一使用 X-Access-Token。
- [ ] 自检结论：确认“不信任网关透传、由 vis 直接验 token，且认证授权权威源切到 GK”作为固定方案。
- [ ] 构建成功：`mvn -pl vis_screen/jeecg-boot/jeecg-boot-module-system -am -DskipTests compile`

**Dependencies:** None

**Files likely touched:**
- `org-tribe-system/src/main/java/org/jeecg/config/ShiroConfig.java`
- `org-tribe-system/src/main/java/org/jeecg/modules/shiro/authc/ShiroRealm.java`
- `vis_screen/jeecg-boot/jeecg-boot-base-common/src/main/java/org/jeecg/config/shiro/ShiroConfig.java`
- `vis_screen/jeecg-boot/jeecg-boot-base-common/src/main/java/org/jeecg/config/shiro/ShiroRealm.java`

**Estimated scope:** Small: 1-2 files for implementation, plus validation reading

## Task 2: 新增 vis 网关路由并打通最小受保护接口

**Description:**
在 GK 网关侧为 vis 服务增加明确的 Zuul 路由前缀，并选取一个最小 visualScreen 业务接口完成端到端通路验证。该任务只解决“请求能经 org-tribe-view -> Zuul -> vis-service 到达，并由 vis 服务基于 GK token 成功识别用户，且不再依赖 vis 本地用户权限逻辑”。

**Acceptance criteria:**
- [ ] org-tribe-system 新增 vis 服务转发路由，且头信息不会丢失。
- [ ] vis 服务最小受保护业务接口可通过 GK token 成功访问。
- [ ] token 无效或缺失时，vis 服务返回与 GK 体系一致的未授权行为。
- [ ] vis 服务的最小认证链路不再要求 vis 本地用户/角色/权限数据参与鉴权。

**Current findings:**
- [x] GK 源码内当前只有 `application-dev.yml` 存在 Zuul 路由段，开发态可先直接接入 `/visScreen/**`。
- [x] vis 最小可验证业务接口可优先选用 `POST /pageInfo/getAll` 或 `POST /schemeInfo/getAll` 这类 visualScreen controller。
- [x] 在注册中心未统一前，`/visScreen/**` 先使用地址路由，实际后端地址通过 `VIS_SCREEN_BACKEND_URL` 注入。

**Verification:**
- [ ] 构建成功：`mvn -pl org-tribe-system -DskipTests compile`
- [ ] 构建成功：`mvn -pl vis_screen/jeecg-boot/jeecg-boot-module-system -am -DskipTests compile`
- [ ] 自检验证：使用已登录 GK 会话访问一个 vis 业务接口，经网关返回 200；移除 token 后返回未授权。

**Dependencies:** Task 1

**Files likely touched:**
- `org-tribe-system/src/main/resources/application-dev.yml`
- `vis_screen/jeecg-boot/jeecg-boot-base-common/src/main/java/org/jeecg/config/shiro/ShiroRealm.java`
- `vis_screen/jeecg-boot/jeecg-boot-base-common/src/main/java/org/jeecg/config/shiro/filters/JwtFilter.java`

说明：`application-test.yml` 与 `application-prod.yml` 当前源码中未包含对应 Zuul 段，后续需要结合外部化配置一起补齐，不在本次最小切片里盲改。

**Estimated scope:** Medium: 3-5 files

### Checkpoint: Foundation

- [ ] GK 网关能够把带 X-Access-Token 的请求稳定转发到 vis 服务。
- [ ] vis 服务能够直接校验 GK token，而不是依赖自有登录接口与本地用户权限数据。
- [ ] 基础编译通过，后续前端迁入可以在此基础上展开。

### Phase 2: Core Integration

- [ ] Task 3: 收口 vis 后端平台能力入口
- [ ] Task 4: 在 org-tribe-view 建立 vis 前端基础骨架
- [ ] Task 5: 迁入大屏预览最小纵向切片

## Task 3: 收口 vis 后端平台能力入口

**Description:**
把 vis 后端里与登录、注册、用户、角色、权限管理相关的平台能力从对外边界中移除或禁用，同时删除 vis 中新增的 Nexus OAuth 相关功能，确保 vis 服务只保留 visualScreen 业务域能力。该任务的目标不是动业务数据结构，而是切断 vis 对“平台治理职责”的暴露。

**Acceptance criteria:**
- [ ] vis 对外不再保留独立登录与注册入口作为正式访问路径。
- [ ] vis 不再对外提供独立用户/角色/权限管理入口用于业务使用。
- [ ] `NexusOAuthConfig.java`、`NexusOAuthCallbackController.java`、`NexusSyncHintController.java` 及其相关配置、白名单、文档入口被移除。
- [ ] visualScreen 相关 controller 保持可访问，系统平台 controller 不作为 vis 入口继续承接业务。

**Verification:**
- [ ] 构建成功：`mvn -pl vis_screen/jeecg-boot/jeecg-boot-module-system -am -DskipTests compile`
- [ ] 自检验证：旧登录相关 URL 与 Nexus OAuth 相关 URL 不再作为正式入口可用。
- [ ] 自检验证：visualScreen 相关业务接口访问不受影响。

**Dependencies:** Task 2

**Files likely touched:**
- `vis_screen/jeecg-boot/jeecg-boot-base-common/src/main/java/org/jeecg/config/shiro/ShiroConfig.java`
- `vis_screen/jeecg-boot/jeecg-boot-module-system/src/main/java/org/jeecg/modules/**/controller/*.java`
- `vis_screen/jeecg-boot/jeecg-boot-module-system/src/main/java/org/jeecg/modules/nexus/config/NexusOAuthConfig.java`
- `vis_screen/jeecg-boot/jeecg-boot-module-system/src/main/java/org/jeecg/modules/nexus/controller/NexusOAuthCallbackController.java`
- `vis_screen/jeecg-boot/jeecg-boot-module-system/src/main/java/org/jeecg/modules/nexus/controller/NexusSyncHintController.java`
- `vis_screen/document/2026041401-vis-nexus-oauth-sso.md`

**Estimated scope:** Medium: 3-5 files

## Task 4: 在 org-tribe-view 建立 vis 前端基础骨架

**Description:**
在 org-tribe-view 中建立 vis 业务模块的基础目录、API 封装、路由挂载点和菜单落位方式，但先不整体迁移所有页面。重点是让 vis 功能成为 GK 前端中的一等模块，而不是外挂 iframe 或外链，同时不再依赖 vis 独立前端中的用户态入口与平台 store。

**Acceptance criteria:**
- [ ] org-tribe-view 中新增 vis 业务目录与 API 组织方式。
- [ ] vis 业务路由能够通过 GK 主系统动态菜单机制挂载。
- [ ] 不再依赖 vis 独立前端的 `/user/login`、`permission.js`、独立 store 作为入口。

**Verification:**
- [ ] 构建成功：`npm run build`（在 org-tribe-view 目录）
- [ ] 自检验证：GK 菜单中可出现一个 vis 模块入口，点击后进入 org-tribe-view 内部路由。
- [ ] 自检验证：控制台无基础路由加载错误。

**Dependencies:** Task 2

**Files likely touched:**
- `org-tribe-view/src/config/router.config.js`
- `org-tribe-view/src/api/**`
- `org-tribe-view/src/views/**`
- `org-tribe-view/src/permission.js`
- `org-tribe-view/src/store/**`

**Estimated scope:** Medium: 3-5 files

## Task 5: 迁入大屏预览最小纵向切片

**Description:**
优先迁移一个完整可运行的预览切片，而不是先搬完所有配置端页面。建议首批选择 `BigScreenPreview` 及其最小依赖组件，完成从 org-tribe-view 路由到网关转发 vis 业务接口的完整路径，验证前端并入方案是成立的。

**Acceptance criteria:**
- [ ] `BigScreenPreview` 页面可以在 org-tribe-view 中访问。
- [ ] 预览页所需最小 API 能通过网关从 vis 服务获取数据。
- [ ] 预览页不依赖 vis 独立登录页和独立前端全局状态。

**Verification:**
- [ ] 构建成功：`npm run build`（在 org-tribe-view 目录）
- [ ] 自检验证：登录 GK 后可进入预览页并渲染出页面/方案数据。
- [ ] 自检验证：刷新页面后不跳转到 vis 旧登录页。

**Dependencies:** Task 4

**Files likely touched:**
- `vis_screen/ant-design-vue-jeecg/src/views/BigScreen/BigScreenPreview.vue`
- `vis_screen/ant-design-vue-jeecg/src/views/BigScreen/modules/*.vue`
- `org-tribe-view/src/views/vis/**`
- `org-tribe-view/src/api/**`

**Estimated scope:** Medium: 3-5 files

### Checkpoint: Core Features

- [ ] GK 前端内已经存在 vis 路由入口。
- [ ] 至少一个 vis 页面可在 org-tribe-view 中跑通到 vis 服务。
- [ ] vis 后端已不再依赖独立登录入口、独立 SSO 与本地权限逻辑才能访问业务域。

### Phase 3: Business Migration

- [ ] Task 6: 分批迁入图库、页面、方案配置页
- [ ] Task 7: 接入 GK 页面级权限菜单
- [ ] Task 8: 下线 vis 独立前端与遗留入口

## Task 6: 分批迁入图库、页面、方案配置页

**Description:**
按业务纵向切片分批迁入 vis 的配置端页面，优先级建议为：方案管理、页面管理、图库管理。每一批都要包含页面、相关 API、最小共用组件，不一次性搬运全部 BigScreen 目录，避免范围失控。

**Acceptance criteria:**
- [ ] 至少完成方案管理与页面管理两个核心配置页迁移。
- [ ] 每个迁移页面都仅依赖 org-tribe-view 公共布局、公共组件和新的 vis API 封装。
- [ ] vis 独立前端中的同名页面不再作为生产入口。

**Verification:**
- [ ] 构建成功：`npm run build`（在 org-tribe-view 目录）
- [ ] 自检验证：方案、页面相关列表/新增/编辑至少各完成一条核心流程。
- [ ] 自检验证：页面中无对 vis 独立 store/user 模块的残留引用。

**Dependencies:** Task 5

**Files likely touched:**
- `vis_screen/ant-design-vue-jeecg/src/views/BigScreen/ExhibitionSchemeList.vue`
- `vis_screen/ant-design-vue-jeecg/src/views/BigScreen/PageList.vue`
- `vis_screen/ant-design-vue-jeecg/src/views/BigScreen/TemplateList.vue`
- `org-tribe-view/src/views/vis/**`
- `org-tribe-view/src/api/**`

**Estimated scope:** Large: 5+ files

## Task 7: 接入 GK 页面级权限菜单

**Description:**
在 GK 主系统中为 vis 建立页面级菜单树，把访问控制统一纳入 sys_permission 和前端动态路由生成逻辑。该任务不追求按钮级细化，只解决“哪些 vis 页面谁能进”的主边界问题，同时明确 vis 不再维护自己独立的用户、角色、权限治理逻辑。

**Acceptance criteria:**
- [ ] GK 中存在 vis 页面级菜单树定义。
- [ ] 无权限用户无法访问对应 vis 页面路由。
- [ ] 页面权限校验只来自 GK 主系统，不再从 vis 自己的权限管理页面维护。
- [ ] vis 本地用户/角色/权限相关前后端逻辑被标记为废弃或移除，不再作为运行依赖。

**Verification:**
- [ ] 构建成功：`mvn -pl org-tribe-system -DskipTests compile`
- [ ] 构建成功：`npm run build`（在 org-tribe-view 目录）
- [ ] 自检验证：两个不同权限账号对 vis 页面呈现不同可见范围。

**Dependencies:** Task 4, Task 6

**Files likely touched:**
- `org-tribe-system/src/main/java/org/jeecg/modules/system/controller/SysPermissionController.java`
- `org-tribe-view/src/permission.js`
- `org-tribe-view/src/utils/util.js`
- `org-tribe-view/src/views/system/PermissionList.vue`

**Estimated scope:** Medium: 3-5 files

## Task 8: 下线 vis 独立前端与遗留入口

**Description:**
在主流程跑通后，正式下线 vis 独立前端作为生产入口，清理旧路由守卫、旧登录页、旧发布入口说明和部署脚本中的默认访问方式，确保系统对外只暴露 GK 主前端入口与 vis 后端微服务接口。同时确认 Nexus OAuth 文档与部署入口已经一起下线。

**Acceptance criteria:**
- [ ] vis 独立前端不再作为生产入口使用。
- [ ] 所有用户侧访问路径统一从 GK 主前端进入。
- [ ] 部署文档和运行说明反映新的入口与调用链路。

**Verification:**
- [ ] 自检验证：访问旧独立前端地址不会再成为正式业务入口。
- [ ] 自检验证：从 GK 主前端可覆盖主要 vis 业务访问路径。
- [ ] 自检检查：部署说明、访问说明与新架构一致。

**Dependencies:** Task 6, Task 7

**Files likely touched:**
- `vis_screen/ant-design-vue-jeecg/src/permission.js`
- `vis_screen/ant-design-vue-jeecg/src/config/router.config.js`
- `vis_screen/document/*.md`
- `gk-data-analysis/document/implementation_plans/*.md`

**Estimated scope:** Medium: 3-5 files

### Checkpoint: Complete

- [ ] 所有 vis 用户侧入口都已统一到 GK 主前端。
- [ ] vis 服务只保留业务域能力，不再承担独立平台认证、独立 SSO 与权限治理职责。
- [ ] 页面级权限在 GK 主系统内生效。
- [ ] 主流程可完成：登录 GK -> 进入 vis -> 访问预览或配置页 -> 调用 vis 微服务。

## Risks and Mitigations

| Risk | Impact | Mitigation |
|------|--------|------------|
| GK 与 vis 的 Redis token 规则不完全兼容 | Medium | 当前已确认签名、头名、key 前缀兼容，优先统一 vis 历史 TTL 差异，再进入网关与接口联调 |
| 前端页面迁入时公共组件和依赖版本冲突 | High | 先做预览最小切片，按页面逐批迁，不一次性整体搬运 BigScreen 目录 |
| vis 业务接口中隐含依赖本地权限表 | High | 在 Task 3 和 Task 6 期间自检 controller/service，优先修复强依赖点 |
| Nexus OAuth 残留配置或白名单导致仍存在第二登录入口 | High | 在 Task 3 明确删除 3 个 Java 文件、Shiro 白名单、配置项与文档说明 |
| 页面级权限不够覆盖预览页特殊访问方式 | Medium | 先明确预览页是否需要匿名发布模式，若需要则后续追加只读发布机制 |
| 旧独立前端残留入口导致用户继续走旧路径 | Medium | Task 8 统一下线旧入口并更新部署/访问文档 |

## Confirmed Constraints

- vis 与 GK 可以共享同一个 Redis 实例，并统一沿用 `prefix_user_token_` 前缀策略；前提是 vis token TTL 先与 GK 收敛到同一语义。
- `BigScreenPreview` 不需要匿名访问，继续保持登录后访问，不引入单独签名链接机制。
- vis 运行时查询接口当前不存在额外的按用户或机构的数据权限要求，不增加页面级权限之外的二级权限设计。

## Parallelization Opportunities

- 可并行：前端页面目录迁移预研、后端 visualScreen controller 盘点、Nexus OAuth 残留清单整理、部署文档整理。
- 必须串行：token 兼容方案、权威用户源切换、网关路由、页面级权限接入。
- 需要先约定接口再并行：org-tribe-view 的 vis API 封装与 vis 服务对外路径命名。

## Verification

- [ ] 每个任务都包含 acceptance criteria。
- [ ] 每个任务都包含 verification。
- [ ] 任务顺序符合依赖关系，先认证与转发，再前端迁入，再权限与下线。
- [ ] 高风险项被前置到 Phase 1 和 Phase 2，而不是拖到收尾。
- [ ] 该计划已经可以直接作为后续分批实施的执行清单。
