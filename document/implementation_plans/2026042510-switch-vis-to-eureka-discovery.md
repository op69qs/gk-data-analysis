# vis-screen-backend 切换到 Eureka 服务发现

## 目标

- `vis-screen-backend` 通过 Eureka 注册服务，注册名统一为 `vis`
- `org-tribe-system` 通过 Eureka + Zuul 的 `serviceId` 发现 `vis`，不再依赖写死的 `url` 转发

## 本次修改

### 1. vis-screen-backend

文件：`vis-screen-backend/jeecg-boot-module-system/src/main/resources/bootstrap.yml`

- 将 `spring.application.name` 从 `visScreen` 调整为 `vis`
- 将 `eureka.client.enabled` 默认值从 `false` 调整为 `true`
- 将 `register-with-eureka`、`fetch-registry` 的默认值同步调整为 `true`

这样在未显式覆写环境变量时，`vis-screen-backend` 会默认作为 `vis` 注册到 Eureka。

### 2. org-tribe-system

文件：`org-tribe-system/src/main/resources/application-dev.yml`

- 保留 `/vis/api/**` 与 `/visScreen/**` 两条 Zuul 路由入口
- 将两条路由的目标从 `url: ${VIS_SCREEN_BACKEND_URL:...}` 改为 `serviceId: vis`

这样 `org-tribe-system` 会通过 Eureka 查找 `vis` 实例，而不是直连固定地址。

## 影响与约束

- 当前改动只覆盖 `application-dev.yml`，因为仓库内仅该 profile 存在 vis 的 Zuul 路由配置
- 启动联调时需要保证 Eureka 中心可达，且 `vis-screen-backend` 成功注册为 `vis`
- 如果后续部署环境仍想禁用 Eureka，可继续通过 `EUREKA_CLIENT_ENABLED=false` 覆盖默认值

## 校验思路

- 检查 `org-tribe-system` 的 vis 路由是否只剩 `serviceId: vis`
- 检查仓库内不再依赖 `VIS_SCREEN_BACKEND_URL` 作为 GK 发现 vis 的主路径
- 启动后在 Eureka 控制台确认存在服务名 `VIS` 或 `vis` 的注册实例