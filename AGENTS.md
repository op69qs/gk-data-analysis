# gk-data-analysis 工作指引

## 作用范围

本文件适用于仓库根目录下所有子模块。仓库根目录现已提供聚合 `pom.xml`，但大部分模块仍保留独立构建/启动方式，排查问题时优先进入具体模块定位。

## 先看模块，再动手

- `org-tribe-system`：主后端，JEECG 体系核心业务模块。
- `org-tribe-view`：主前端，Vue 2 + Ant Design Vue。
- `dwbi-system-docking`：系统对接与调度模块，依赖 Redis，运行前先看 [dwbi-system-docking/README.MD](dwbi-system-docking/README.MD)。
- `dwbi-statistical-analysis`、`fixedReport`、`indicatorsLibv-1.0`、`inspect`、`seo`、`kunMgk`：独立 Spring Boot 业务/报表模块，结构相近，很多实现存在复制痕迹，修改前先确认目标模块是否唯一。
- `jar`：已打包产物与外部化配置示例，不是 Maven 模块。

如果需求没有明确指定模块，默认先判断它属于：

1. 主系统页面或接口：优先看 `org-tribe-view` 和 `org-tribe-system`。
2. 数据对接、定时任务、外部系统调用：优先看 `dwbi-system-docking`。
3. 报表、文档导出、专题分析：优先看其对应独立模块，不要直接改主系统。

## 常用命令

### Java 模块

在具体模块目录执行：

```powershell
mvn clean test
mvn clean package -DskipTests
mvn spring-boot:run
```

补充约束：

- 运行环境以 Java 8 为准。
- 当前可在仓库根目录执行聚合构建；若只验证单模块问题，仍优先进入模块目录执行 Maven 命令。
- `org-tribe-system` 现已直接继承仓库根父工程，不再依赖本地桥接父模块解析。
- 多个模块的打包方式会把依赖复制到 `target/lib`，并把 `yml/properties` 复制到 `target` 根目录；排查“本地能跑、打包后不能跑”时先检查这个约定。
- 可以使用 admin Ysyyrps4 账号测试

### 前端模块

在 [org-tribe-view/README.md](org-tribe-view/README.md) 的基础上执行：

```powershell
cd org-tribe-view
npm run pre
npm run serve
npm run build
npm run lint
```

补充约束：

- 这是 Vue 2 旧项目，依赖较老，不要按 Vue 3 写法改代码。
- `package.json` 默认使用淘宝镜像安装依赖；离开该网络环境时，优先调整 registry，不要直接改业务代码。
- 前端接口封装集中在 `src/api`，优先复用现有 axios 调用模式。
- node 使用 node14进行编译

## 高价值入口文件

- [org-tribe-system/src/main/java/org/jeecg/JeecgApplication.java](org-tribe-system/src/main/java/org/jeecg/JeecgApplication.java)：主系统启动入口。
- [dwbi-system-docking/src/main/java/org/dockingProjects/SystemDockingApplication.java](dwbi-system-docking/src/main/java/org/dockingProjects/SystemDockingApplication.java)：对接模块启动入口。
- [org-tribe-view/src/api](org-tribe-view/src/api)：前端接口组织方式。
- [org-tribe-view/src/components/README.md](org-tribe-view/src/components/README.md)：前端组件约定。
- [org-tribe-view/src/router/README.md](org-tribe-view/src/router/README.md)：前端路由说明。

## 仓库特有坑点

### dwbi-system-docking

- Redis 必须先启动。
- 配置中至少关注 `fileSavePath`、`restartCount`、`applicationPath`。
- 该模块对扫描与调度配置较敏感，启动类需要显式保留 `@MapperScan`、`@ComponentScan`；如果新增定时任务，再确认是否需要 `@EnableScheduling`，并与 [dwbi-system-docking/README.MD](dwbi-system-docking/README.MD) 保持一致。

### org-tribe-system

- 技术栈带有 JEECG、Zuul、Eureka 等旧式 Spring Cloud 组件；升级或替换依赖前先确认是否影响现有网关/注册中心行为。
- 代码主要在 `org.jeecg.modules.*`，新增后端功能优先沿用现有包结构，不要平铺新顶级包。

### 多个报表模块

- `fixedReport`、`inspect`、`indicatorsLibv-1.0`、`seo`、`kunMgk` 之间存在大量相似实现；修复缺陷时先全文检索同类类名，避免只修一个副本。

## 修改策略

- 先在目标模块内搜索现有实现，再决定是否新增代码；这个仓库重复实现较多，直接新写常常会偏离既有模式。
- 优先做最小改动，不顺手清理无关旧代码。
- 涉及配置时，优先核对 `src/main/resources` 与 [jar/application.yml](jar/application.yml)、[jar/sys/application.yml](jar/sys/application.yml) 是否存在外部化配置差异。
- 如果只是补接口或页面，先沿用现有 controller/service/mapper 或 `src/api`/`views` 结构，不要引入新的架构层。

## 文档入口

- [dwbi-system-docking/README.MD](dwbi-system-docking/README.MD)
- [org-tribe-view/README.md](org-tribe-view/README.md)
- [org-tribe-view/src/components/README.md](org-tribe-view/src/components/README.md)
- [org-tribe-view/src/router/README.md](org-tribe-view/src/router/README.md)

这些文档已经覆盖模块使用说明。更新 AGENTS 时优先补充“代理容易踩坑但无法快速从代码里看出来的约束”，不要把现有 README 内容整段复制过来。