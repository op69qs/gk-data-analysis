# 将 vis-screen-backend 切换为 Eureka 注册版

## 背景

`org-tribe-system` 是 Zuul + Eureka 体系，网关按 `serviceId` 做服务发现。

而 `vis-screen-backend` 原先并没有真的接入这套发现链路：

- 启动配置里的应用名是 `jeecg-system`
- `bootstrap.yml` 写的是 Nacos 地址
- GK 网关期望的服务名却是 `visScreen`

结果就是即便网关按 `serviceId: visScreen` 查找，也找不到对应实例。

## 本次改动

### 1. vis-screen-backend 改为 Eureka 客户端

修改文件：

- `vis-screen-backend/jeecg-boot-module-system/pom.xml`
- `vis-screen-backend/jeecg-boot-module-system/src/main/resources/bootstrap.yml`
- `vis-screen-backend/jeecg-boot-module-system/target/bootstrap.yml`

处理内容：

- 新增 `spring-cloud-starter-netflix-eureka-client`
- 将 `spring.application.name` 改为 `visScreen`
- 去掉原有 Nacos 注册配置
- 新增 Eureka 默认地址：`http://192.168.160.244:8761/eureka/`

并保留环境变量覆盖口：

- `EUREKA_DEFAULT_ZONE`

### 2. GK 网关切回服务发现路由

修改文件：

- `org-tribe-system/src/main/resources/application-dev.yml`
- `org-tribe-system/target/classes/application-dev.yml`

处理内容：

- 将 `/visScreen/**` 从静态 `url` 路由改回 `serviceId: visScreen`

这意味着 GK 现在会直接向 Eureka 查询 `visScreen` 实例，而不是固定转发到 `127.0.0.1:9082`。

## 验证结果

### 已确认

- `bootstrap.yml` 与 GK 路由 YAML 均通过静态错误检查
- 关键字段已对齐：
  - `vis-screen-backend` 服务名为 `visScreen`
  - GK 路由为 `serviceId: visScreen`

### 当前阻塞

无法在当前环境完成可靠编译验证，原因是本机 Maven/VS Code Java 分析都在使用与该老项目不兼容的较高版本 JDK，触发了 Lombok/Javac 模块访问错误：

- `module jdk.compiler does not "opens com.sun.tools.javac.processing"`
- `Could not initialize class lombok.javac.Javac`

这不是 Eureka 改动本身导致的问题，而是当前 Java 运行环境与旧版 Lombok/编译链不兼容。

## 下一步

要真正看到注册成功，需要：

1. 用 Java 8 启动 `vis-screen-backend`
2. 启动后确认日志中出现 Eureka 注册成功信息
3. 再启动或重启 `org-tribe-system`
4. 访问 `/visScreen/pageInfo/getPage`，确认不再出现 `Load balancer does not have available server for client: visScreen`

## 备注

这次变更已经把“名称不一致”和“注册中心体系不一致”这两个核心问题改到位。
如果后续仍未注册成功，应优先检查：

- 实际启动使用的 JDK 是否为 1.8
- `192.168.160.244:8761` 是否可达
- Eureka 服务端是否允许该实例注册
