# vis 后端并入 GK 仓库并统一打包启动

## 目标

本轮目标不是继续做页面迁移，而是把 vis 后端源码真正复制进 `gk-data-analysis` 仓库内，让它满足三个工程化要求：

- 能参与 GK 根仓库统一 Maven reactor 构建
- 能进入 GK 的统一 `deploy-package` 装配产物
- 能被统一启动脚本和统一 BES 目录结构管理

## 设计选择

### 1. 采用子聚合，不强行改成继承 GK 根父工程

已在 GK 根聚合 `pom.xml` 中新增：

- `vis-screen-backend`

但 `vis-screen-backend` 自己仍保留独立父 `pom` 和原有 dependencyManagement 体系。

原因：

- GK 主系统当前是 Spring Boot `2.0.6.RELEASE`
- vis 后端当前是 Spring Boot `2.1.3.RELEASE`
- 两边还带着各自的 Spring Cloud / BES 依赖约束

如果强行让 vis 模块直接继承 GK 根父工程，版本冲突会扩散到依赖管理层，后续很难做最小收口。当前做法是：

- GK 根仓库负责统一聚合和统一打包
- vis 子聚合负责维持自己可运行的版本闭包

这样既能一起构建，也不会把两套版本体系粗暴压成一套。

### 2. 只复制最小后端闭包

本轮复制进仓库的模块是：

- `vis-screen-backend/jeecg-boot-base-common`
- `vis-screen-backend/jeecg-boot-base-api`
- `vis-screen-backend/jeecg-boot-module-system`

同时做了两项裁剪：

- `jeecg-boot-base-api` 只保留 `jeecg-system-local-api`
- 不再把 `jeecg-cloud-module`、`jeecg-deploy`、旧前端构建链带进来

原因：

- 当前浏览器只访问 GK 前端，vis 旧前端已经不再是目标产物
- 当前后端集成走 GK 前端直连 GK 后端，再由 GK 网关转发 `/visScreen/**`
- vis 独立 cloud 部分和独立 deploy-package 在本阶段都不是最小必要闭包

## 构建改造

### 1. vis 后端主模块改成纯后端 fat jar

`vis-screen-backend/jeecg-boot-module-system` 已移除以下构建耦合：

- 旧前端 `npm install` / `npm build`
- 将产物复制到 vis 自己 deploy-package 的 antrun 逻辑
- 对旧前端 dist 的打包依赖

保留内容：

- `spring-boot-maven-plugin` repackage
- 配置资源复制到 `target`

结果：

- 该模块现在能在 GK 仓库内独立产出 fat jar
- 产物名称为 `jeecg-boot-module-system-*.jar`

### 2. Maven 验证结果

已验证命令：

```powershell
mvn -pl vis-screen-backend/jeecg-boot-module-system -am -DskipTests "-Dexec.skip=true" package
```

结果：

- `jeecg-boot-base-common` 成功
- `jeecg-system-local-api` 成功
- `jeecg-boot-module-system` 成功
- 整体 `BUILD SUCCESS`

这说明复制进 GK 仓库内的 vis 最小闭包已经具备独立可构建性。

## 统一装配

### 1. deploy-package-assembly 新增 vis 产物

`deploy-package-assembly/pom.xml` 已把 vis 纳入统一装配，新增产物包括：

- `deploy-package/app/jeecg-boot-module-system-2.3.0.jar`
- `deploy-package/app/vis-screen-2.3.0.jar`
- `deploy-package/config/vis-screen/*.yml`
- `deploy-package/config/vis-screen/*.properties`
- `deploy-package/config/vis-screen/bootstrap.yml`
- `deploy-package/config/vis-screen/logback-spring.xml`
- `deploy-package/config/vis-screen/banner.txt`
- `deploy-package/bes/vis-screen/**`
- `deploy-package/bin/start-all.sh`
- `deploy-package/bin/start-all.ps1`

这里与原有模块不同的点在于：

- 其他 GK 模块大多是 `app + lib/<module>` 的 classpath 启动模式
- vis 使用的是单 fat jar + 独立 `config/vis-screen` + 独立 `bes/vis-screen`

补充说明：

- 初版装配直接保留了上游原始 jar 名 `jeecg-boot-module-system-*.jar`
- 这样虽然功能上可用，但在 `deploy-package/app` 中不直观，容易被误判为“只有 config 有 vis 内容”
- 当前已改为在装配阶段显式重命名为 `vis-screen-*.jar`，与目录名和启动脚本模块名保持一致

### 2. assembly 验证结果

已验证命令：

```powershell
mvn -pl deploy-package-assembly -DskipTests package
```

结果：

- 装配成功
- `deploy-package/app` 共复制 7 个 jar，其中包含显式命名的 vis fat jar `vis-screen-*.jar`
- `deploy-package/config/vis-screen` 共复制 8 个配置文件
- `deploy-package/bes/vis-screen` 成功复制 vis BES 目录
- `deploy-package/bin` 成功复制 `start-all.sh` 与 `start-all.ps1`

## 统一启动

### 1. Linux 启动脚本支持两种模块模式

`build/start-all.sh` 已从固定 classpath 启动表，改成：

- `module | jar_pattern | main_class | launch_mode`

其中：

- 既有模块继续使用 `classpath`
- `vis-screen` 新增为 `fatjar`

vis 启动方式为：

- jar pattern: `vis-screen-*.jar`
- main class: `org.jeecg.JeecgSystemApplication`
- launch mode: `fatjar`

fat jar 分支会额外带上：

- `-Dserver.bes.basedir=deploy-package/bes/vis-screen`
- `--spring.config.additional-location=file:deploy-package/config/vis-screen/`

### 2. Windows 启动脚本已补齐

本轮新增：

- `build/start-all.ps1`

它与 Linux 脚本对齐了同样的模块表语义，并新增：

- `Resolve-Jar`，按通配符查找 jar
- `LaunchMode` 区分 `classpath` 和 `fatjar`
- `vis-screen` 使用 fat jar 启动分支

### 3. Windows 启动脚本验证结果

最初验证时，`start-all.ps1` 在 Windows PowerShell 下因为 UTF-8 无 BOM 与中文提示串组合，导致解析失败。

已修正为：

- 脚本提示语全部改为 ASCII
- 文件按 UTF-8 BOM 输出后再参与 assembly

最终验证命令：

```powershell
powershell -ExecutionPolicy Bypass -File .\deploy-package\bin\start-all.ps1 status
```

结果：

- 脚本可正常执行
- 7 个模块状态均可被统一输出
- `vis-screen` 已纳入统一状态管理

输出结果为：

```text
[STOPPED] dwbi-statistical-analysis
[STOPPED] dwbi-system-docking
[STOPPED] fixedReport
[STOPPED] indicatorsLibv-1.0
[STOPPED] org-tribe-system
[STOPPED] seo
[STOPPED] vis-screen
```

## 当前结论

本轮结束后，vis 后端已经从“仓库外单独项目”变成 GK 仓库内受统一管理的后端子聚合，并且已经具备：

- 一起构建
- 一起装配
- 一起启动脚本管理

当前没有再把 vis 旧前端、旧 deploy-package、cloud 模块一并拖入，是刻意的最小收口策略，不是遗漏。

这样做的收益是：

- 满足“方便一起打包、统一启动管理”的直接目标
- 避免把不再需要的旧前端和 cloud 依赖重新引回主仓库
- 后续如果要继续做更深层的配置统一或启动顺序治理，可以在这个稳定闭包上继续推进