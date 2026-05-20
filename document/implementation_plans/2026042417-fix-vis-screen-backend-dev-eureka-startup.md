# vis-screen-backend dev 环境 Eureka 启动修复记录

## 现象

在清理完 BES / Spring 版本兼容问题后，`jeecg-boot-module-system` 启动继续失败：

```text
ApplicationContextException: Failed to start bean 'eurekaAutoServiceRegistration'
Caused by: java.lang.NullPointerException
```

## 根因

`bootstrap.yml` 中把 Eureka 客户端在本地 `dev` 环境下也默认开启了：

- `register-with-eureka: true`
- `fetch-registry: true`
- `defaultZone` 默认指向固定内网地址 `http://192.168.160.244:8761/eureka/`

本地调试场景并不一定具备该注册中心，因此应用在启动完成前会卡死在自动注册阶段。

## 本次修改

在 `jeecg-boot-module-system/src/main/resources/bootstrap.yml` 中调整为环境变量控制，并让本地默认关闭：

```yml
eureka:
  client:
    enabled: ${EUREKA_CLIENT_ENABLED:false}
    register-with-eureka: ${EUREKA_REGISTER_WITH_EUREKA:${EUREKA_CLIENT_ENABLED:false}}
    fetch-registry: ${EUREKA_FETCH_REGISTRY:${EUREKA_CLIENT_ENABLED:false}}
```

这样处理后：

- 本地 `dev` 直接启动时默认不依赖 Eureka
- 如果部署环境仍需要接入 Eureka，只要显式传入环境变量即可恢复原行为

## 验证

### 1. Eureka 空指针已消失

重新启动后，不再出现 `eurekaAutoServiceRegistration` 相关空指针。

### 2. 应用可完整启动

由于本机 `9082` 已被其他 Java 进程占用，本次使用临时端口验证：

```powershell
mvn --% -f vis-screen-backend/jeecg-boot-module-system/pom.xml spring-boot:run -DskipTests -Dspring-boot.run.mainClass=org.jeecg.VISSystemApplication -Dspring-boot.run.arguments=--server.port=19082
```

启动日志已出现：

- `BES initialized with port(s): 19082 (http)`
- `Server listening on port(s): 19082 (http) with context path ''`
- `Started JeecgSystemApplication`

说明当前 `dev` 环境已经可以在不依赖 Eureka 的前提下正常启动。

## 补充说明

- 本机 `9082` 当时已被另一 Java 进程占用，因此直接使用默认端口会得到 `Connector configured to listen on port 9082 failed to start`
- 这不是应用配置错误，而是本地端口占用问题