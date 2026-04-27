# 2026042702 migrate vis-screen-backend to bes

## 背景

将 vis-screen-backend 按仓库现有模块改造方式，从内嵌 Tomcat 切换为宝兰德 BES 中间件，保持 Java 8 + Spring Boot 2.1.3 兼容。

## 修改范围

1. 依赖层（POM）
- vis-screen-backend/jeecg-boot-base-common/pom.xml
  - spring-boot-starter-web 排除 spring-boot-starter-tomcat
  - spring-boot-starter-websocket 排除 spring-boot-starter-tomcat
  - 新增 javax.servlet-api（provided）
  - 新增 commons-fileupload:1.4（用于替换 Tomcat FileItemStream）
- vis-screen-backend/jeecg-boot-module-system/pom.xml
  - 新增 com.bes.appserver:bes-lite-spring-boot-2.x-starter
  - 新增 com.bes.appserver:bes-websocket
  - 新增 org.springframework.boot:spring-boot-loader（compile）

2. 代码层（Java）
- vis-screen-backend/jeecg-boot-base-common/src/main/java/org/jeecg/config/WebSocketConfig.java
  - 移除 ServerEndpointExporter Bean（BES + bes-websocket 下不需要手动注册）
- vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/JeecgSystemApplication.java
  - 移除 TomcatServletWebServerFactory 及 org.apache.catalina/org.apache.tomcat 相关 import
- vis-screen-backend/jeecg-boot-base-common/src/main/java/org/jeecg/common/util/oss/OssBootUtil.java
  - FileItemStream import 从 org.apache.tomcat.util.http.fileupload 切换到 org.apache.commons.fileupload

3. 配置层（YAML）
- vis-screen-backend/jeecg-boot-module-system/src/main/resources/application-dev.yml
- vis-screen-backend/jeecg-boot-module-system/src/main/resources/application-prod.yml
- vis-screen-backend/jeecg-boot-module-system/src/main/resources/application-test.yml
  - 删除 server.tomcat.max-swallow-size
  - 新增 server.bes.basedir: bes
  - 新增 server.store-lic-interval: 0

## 验证

在 vis-screen-backend 根目录执行：

```powershell
mvn -pl jeecg-boot-module-system -am -DskipTests compile
```

结果：BUILD SUCCESS。

## 注意事项

1. 启动目录需能解析到 bes 目录（basedir=bes），即运行目录下存在 bes/license/bes.lic 等授权文件。
2. jeecg-cloud-module/config/jeecg-cloud-application-beta.yml 仍存在 server.tomcat 配置，该文件属于 cloud 配置，不在本次 vis-screen-backend 单体 BES 改造范围内。
3. 若后续用 java -jar 验证运行，建议在 jeecg-boot-module-system 模块目录启动，避免 basedir 相对路径偏移。
