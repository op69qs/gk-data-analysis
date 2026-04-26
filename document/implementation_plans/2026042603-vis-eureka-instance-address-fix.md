# vis Eureka 实例地址修复记录

## 背景

`org-tribe-system` 通过 Zuul 转发 `/vis/api/**` 时出现：

- `Load balancer does not have available server for client: vis`

Eureka 页面同时可见 `VIS`，说明问题不在于完全没注册，而在于运行时可用实例与实际转发链路不稳定。

## 根因

`vis-screen-backend` 在当前 Windows 环境存在多张可用网卡：

- `192.168.160.161`：业务网段 Wi-Fi
- `100.118.113.100`：Tailscale
- `100.75.77.248`：`wt0`
- 以及多张 `vEthernet`

原配置直接使用 `spring.cloud.client.ip-address` 生成 Eureka 实例地址。实际运行中会在这些虚拟/VPN 网卡之间漂移，导致：

- Eureka 中注册出非业务地址实例
- Zuul / Ribbon 在不同时间命中到不同地址
- 路由链路表现为间歇性“找不到可用实例”或落到错误实例

## 修改

修改文件：

- `vis-screen-backend/jeecg-boot-module-system/src/main/resources/bootstrap.yml`

本次改动分两部分：

1. 增加 `spring.cloud.inetutils` 约束，优先业务网段并忽略常见虚拟接口。
2. 增加显式覆盖参数：
   - `VIS_EUREKA_IP`
   - `VIS_EUREKA_HOST`
   - `VIS_EUREKA_INSTANCE_ID`

这样在自动选网卡不可靠时，可以直接通过启动环境把 Eureka 注册地址钉死。

## 验证

### 1. 本地启动 `vis-screen-backend`

使用 Java 8 启动 `jeecg-boot-module-system`，并显式传入：

- `VIS_EUREKA_IP=192.168.160.161`
- `VIS_EUREKA_HOST=192.168.160.161`

启动日志确认注册行为：

- `DiscoveryClient_VIS/192.168.160.161:vis:8081: registering service...`
- `registration status: 204`

### 2. Eureka 注册结果

查询 `http://192.168.160.244:8761/eureka/apps/VIS`，可见新实例：

- `192.168.160.161:vis:8081`

同时还可见旧的 `100.118.113.100:vis:8081` 实例。该实例不是本轮启动产生的最新地址，需后续确认是否为旧续约或其他进程残留。

### 3. Zuul 路由探测

对以下地址发起直接请求：

- `http://127.0.0.1:9090/vis/api/schemeInfo/getAllPage?schemeId=1`

返回结果为：

- HTTP 500
- `{"message":"token非法无效!"...}`

这说明请求已经成功经由 Zuul 转发到了 `vis-screen-backend`，当前失败点已经从“服务发现找不到实例”切换为 `vis` 自身鉴权。

## 结论

本轮已确认并修复的点：

- `vis-screen-backend` 的 Eureka 注册地址可以通过显式参数稳定控制
- `org-tribe-system -> /vis/api/** -> vis-screen-backend` 的服务发现链路已经恢复
- 原始的 `Load balancer does not have available server for client: vis` 在本轮验证路径下不再出现

仍需注意的点：

- Eureka 中仍存在旧的 `100.118.113.100` 实例，需要后续清理或确认来源，否则可能继续参与负载均衡
- 当前 `/vis/api/**` 返回的是 `vis` 鉴权错误，不是路由错误，后续应继续沿认证链路排查
