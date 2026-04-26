# org-tribe-system JWT 运行时依赖修复

## 问题现象

- 登录接口执行到 `JwtUtil.sign` 时抛出 `NoClassDefFoundError: com/auth0/jwt/algorithms/Algorithm`。

## 根因分析

- `org.jeecg.common.system.util.JwtUtil` 在运行时依赖 `com.auth0:java-jwt`。
- `org-tribe-system` 当前 `pom.xml` 未显式声明 `java-jwt`，因此 `maven-dependency-plugin` 在打包到 `target/lib` 时不会复制该 jar。
- 仓库自带的 `jar/lib`、`jar/sys/lib` 已存在 `java-jwt-3.7.0.jar`，说明该项目的离线运行包本就需要这项依赖。
- 同时确认 `src/main/java` 下的 mapper XML 仍需作为资源复制，否则后续重打包会再次出现 MyBatis statement 缺失问题。

## 实施修改

- 在 `org-tribe-system/pom.xml` 中新增 `com.auth0:java-jwt:3.7.0`。
- 在 `org-tribe-system/pom.xml` 中恢复 `<build><resources>` 配置，把 `src/main/java` 下的 `**/*.xml` 复制到 `target/classes`。

## 预期结果

- 打包后 `target/lib/java-jwt-3.7.0.jar` 存在。
- 登录流程调用 `JwtUtil.sign` 时不再因 `Algorithm` 类缺失而失败。
- 重打包后 MyBatis mapper XML 仍保留在 `target/classes`。