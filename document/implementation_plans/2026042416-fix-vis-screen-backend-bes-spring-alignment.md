# vis-screen-backend BES / Spring 版本对齐记录

## 原始异常

启动 `org.jeecg.VISSystemApplication` 时出现：

```text
Exception in thread "main" java.lang.IllegalArgumentException: Cannot instantiate interface org.springframework.context.ApplicationContextInitializer : com.bes.enterprise.springboot.autoconfigure.BesMvcConfigContextInitializer
Caused by: java.lang.NoSuchMethodError: org.springframework.core.KotlinDetector.isKotlinReflectPresent()Z
```

## 根因

`vis-screen-backend` 继承仓库根父工程后，运行时类路径出现了多组版本漂移：

1. Spring Boot 组件部分仍停留在根父工程的 `2.0.6.RELEASE`
2. Spring Framework 组件部分已经被 `vis-screen-backend` 自身的 JEECG 依赖管理拉到 `5.1.5.RELEASE`
3. BES 使用的 `bes-lite-spring-boot-2.x-starter` 期望的是 Spring Boot 2.1.x / Spring 5.1.x 这一组兼容组合

结果就是：

- `spring-beans` 调用了 `spring-core` 中较新版本才有的方法，触发 `KotlinDetector.isKotlinReflectPresent()` 缺失
- 后续继续暴露出 `snakeyaml`、`lettuce-core` 等由根父工程旧版本残留导致的二进制兼容问题

## 本次修改

在 `vis-screen-backend/pom.xml` 中新增并显式钉住以下兼容版本：

- `spring-boot.compat.version=2.1.3.RELEASE`
- `spring-framework.compat.version=5.1.5.RELEASE`
- `snakeyaml.compat.version=1.23`
- `lettuce.compat.version=5.1.4.RELEASE`
- `commons-pool2.compat.version=2.6.1`

并在 `dependencyManagement` 中显式覆盖了 `vis-screen-backend` 实际使用到的关键依赖，包括：

- `spring-boot`
- `spring-boot-autoconfigure`
- `spring-boot-loader`
- 各类 `spring-boot-starter-*`
- `spring-core` / `spring-beans` / `spring-context` / `spring-expression` / `spring-web`
- `org.yaml:snakeyaml`
- `io.lettuce:lettuce-core`
- `org.apache.commons:commons-pool2`

同时通过 `pluginManagement` 将 `spring-boot-maven-plugin` 版本也固定到 `2.1.3.RELEASE`，避免 `spring-boot:run` 仍沿用根父工程的 `2.0.6.RELEASE`。

## 验证结果

### 1. Spring 依赖树已对齐

`jeecg-boot-module-system` 当前已解析到：

- Spring Boot `2.1.3.RELEASE`
- Spring Framework `5.1.5.RELEASE`
- SnakeYAML `1.23`
- Lettuce `5.1.4.RELEASE`
- Commons Pool2 `2.6.1`

### 2. 原始异常已消失

重新执行：

```powershell
mvn --% -f vis-screen-backend/jeecg-boot-module-system/pom.xml spring-boot:run -DskipTests -Dspring-boot.run.mainClass=org.jeecg.VISSystemApplication
```

应用已经能够输出：

- `Jeecg Boot Version: 2.3`
- `Spring Boot Version: 2.1.3.RELEASE`
- active profile `dev`

说明已经越过了最初的 BES 初始化异常点。

### 3. 当前剩余阻塞已变更为环境/配置层

启动现在失败在：

```text
ApplicationContextException: Failed to start bean 'eurekaAutoServiceRegistration'
Caused by: java.lang.NullPointerException
```

这说明当前阻塞点已经不是 Spring/BES 类路径兼容，而是 Eureka 注册或对应环境配置问题。

## 额外观察

停止 BES 时还出现了：

```text
InaccessibleObjectException: module java.base does not "opens java.io" to unnamed module
```

仓库约束本身要求 Java 8 运行；若在更高版本 JDK 上调试，BES 9.5.5.025 的停机清理逻辑可能触发这个反射限制。它不是本次启动主因，但说明本模块本地验证仍应优先使用 Java 8。