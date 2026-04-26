# vis dev 默认 Eureka 地址修复与联调验证

## 本次问题

用户反馈 `org-tribe-system` 访问 `vis` 仍然出现：

- `Load balancer does not have available server for client: vis`

进一步排查时，当前工作区运行态还存在两个直接问题：

- `vis-screen-backend` 未启动时，Eureka 中没有 `VIS` 实例
- 即使启动 `vis-screen-backend`，旧配置也可能把虚拟/VPN 网卡地址注册到 Eureka，导致 Zuul/Ribbon 找到的是错误实例

## 本次修改

修改文件：

- `vis-screen-backend/jeecg-boot-module-system/src/main/resources/bootstrap.yml`

在已有 `VIS_EUREKA_IP` / `VIS_EUREKA_HOST` 覆盖能力基础上，进一步增加 dev 默认值：

- `vis.eureka.ip: ${VIS_EUREKA_IP:192.168.160.161}`
- `vis.eureka.host: ${VIS_EUREKA_HOST:${vis.eureka.ip}}`

并让 `eureka.instance.*` 全部统一引用 `vis.eureka.ip/host`。

这样在本机 dev 直接执行 `spring-boot:run` 时，即使不额外传环境变量，也会默认注册到业务网段 `192.168.160.161`，不再漂移到 Tailscale / wt0 / vEthernet。

## 验证过程

### 1. 编译验证

执行：

- `mvn -f jeecg-boot-module-system\pom.xml -DskipTests compile`

结果：

- 构建成功

### 2. 仅启动 vis-screen-backend

不传任何额外 Eureka 环境变量，直接启动：

- `mvn -f jeecg-boot-module-system\pom.xml -DskipTests spring-boot:run`

启动日志确认：

- `DiscoveryClient_VIS/192.168.160.161:vis:8081: registering service...`
- `registration status: 204`

### 3. Eureka 注册结果

读取 `http://192.168.160.244:8761/eureka/apps`，确认：

- `VIS` 唯一实例为 `192.168.160.161:vis:8081`
- `vipAddress` 为 `vis`
- 状态为 `UP`

### 4. 启动 org-tribe-system 并复测 Zuul

启动 `org-tribe-system` 后，对用户原问题链路发起请求：

- `GET http://127.0.0.1:9090/vis/api/schemeInfo/getAllPage?schemeId=1`
- Header: `X-Access-Token: invalid-probe-token`

实际返回：

- HTTP 500
- `{"message":"token非法无效!"...}`

这说明：

- 请求已经成功穿过 `org-tribe-system` 的 Zuul 路由
- Ribbon 已经拿到了 `vis` 的可用实例
- `Load balancer does not have available server for client: vis` 已不再复现
- 当前剩余失败点是 `vis` 自身鉴权，而不是服务发现

## 结论

本轮修复后，dev 环境下 `vis-screen-backend` 默认就会注册到正确业务 IP：

- `192.168.160.161:8081`

并且已通过真实启动两个模块验证：

- `org-tribe-system -> /vis/api/** -> vis-screen-backend` 路由链路恢复正常
- 原始的 Ribbon 无可用实例错误已被消除
- 后续若再失败，应继续沿 `vis` 的 token/JWT/Redis 鉴权链路排查，而不是再回头查 Eureka 服务发现
