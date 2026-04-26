# 2026042001 各模块切换 BES 改造

## 目标

- 将当前仓库后端模块从嵌入式 Tomcat 切换到 BES。
- 统一根 POM 的 BES 依赖管理。
- 统一各模块 thin JAR 场景下的 BES 运行依赖和配置。
- 按当前用户约束，证书先使用 `bes.lic.txt`，不追加二进制 license 文件。

## 本次改动

1. 根 `pom.xml` 新增 `bes.version=9.5.5.025`，并在 `dependencyManagement` 中登记：
   - `com.bes.appserver:bes-lite-spring-boot-2.x-starter`
   - `com.bes.appserver:bes-websocket`
   - `com.bes.appserver:bes-jasper`
2. 后端模块 `org-tribe-system`、`dwbi-statistical-analysis`、`dwbi-system-docking`、`fixedReport`、`indicatorsLibv-1.0`、`seo`、`inspect`：
   - `spring-boot-starter-web` 统一排除 `spring-boot-starter-tomcat`
   - 新增 `bes-lite-spring-boot-2.x-starter`
   - 新增 `javax.servlet-api` provided
   - 新增 `spring-boot-loader` compile，适配当前 thin JAR + target/lib 打包方式
3. `org-tribe-system` 额外处理：
   - `spring-boot-starter-websocket` 排除 Tomcat
   - 新增 `bes-websocket`
   - 删除 `WebSocketConfig` 中的 `ServerEndpointExporter`
4. 各模块 `application*.yml`：
   - 删除 `server.tomcat.max-swallow-size`
   - 新增 `server.bes.basedir: ../document/bes`
   - 新增 `server.store-lic-interval: 0`
5. 在 `document/bes/license/` 下补齐 `bes.lic.txt` 供 BES 按目录读取。

## 配置约定

- 当前仓库没有单独的模块级 `bes/` 目录，统一复用根目录下的 `document/bes`。
- 默认按“从模块目录执行 `java -jar target/...jar`”推导相对路径，因此配置为 `../document/bes`。
- 证书按当前用户要求先只落 `bes.lic.txt`。

## 验证说明

- 已核对本机 Maven 仓库存在 `com.bes.appserver:bes-lite-spring-boot-2.x-starter:9.5.5.025`。
- 尚未做完整 Maven 打包验证；后续应在 JDK 8 下按模块执行编译或打包确认依赖解析与 BES 启动行为。