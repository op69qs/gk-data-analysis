# 修正 visScreen 网关地址路由缩进

## 问题现象

GK 侧访问 `/visScreen/**` 时，Zuul 报错：

`Load balancer does not have available server for client: visScreen`

日志里还能看到：

- `Refreshing SpringClientFactory-visScreen`
- `DynamicServerListLoadBalancer ... current list of Servers=[]`

这说明当前请求没有按静态 URL 路由转发，而是退回到了 Ribbon/Eureka 服务发现链路。

## 根因

`org-tribe-system` 的开发态配置把 `zuul.routes.visScreen.url` 错误缩进到了 `sensitiveHeaders` 下方：

```yml
visScreen:
  path: /visScreen/**
  sensitiveHeaders:
    url: ${VIS_SCREEN_BACKEND_URL:http://127.0.0.1:9082}
```

Zuul 不会把这种写法识别成路由地址，因此会把路由 ID `visScreen` 当成服务名去走 Ribbon，最终在注册中心找不到可用实例。

## 本次修改

- 修正 `org-tribe-system/src/main/resources/application-dev.yml`
- 同步修正 `org-tribe-system/target/classes/application-dev.yml`

修正后结构为：

```yml
visScreen:
  path: /visScreen/**
  sensitiveHeaders:
  url: ${VIS_SCREEN_BACKEND_URL:http://127.0.0.1:9082}
```

这样 `/visScreen/**` 会直接转发到 `VIS_SCREEN_BACKEND_URL`，默认落到 `http://127.0.0.1:9082`。

## 已完成验证

- 两份 YAML 文件均通过编辑器错误检查，无语法错误。
- 当前工作区未发现额外的 `deploy-package` 外置 `visScreen` 路由配置覆盖本次修复。

## 后续验证

该修改属于启动期配置，当前已经运行的 `org-tribe-system` 进程不会自动热更新，仍会继续使用旧配置。需要：

1. 重启 `org-tribe-system`
2. 再次访问 `POST /visScreen/pageInfo/getPage`
3. 确认日志里不再出现 `SpringClientFactory-visScreen` 和 `Load balancer does not have available server for client: visScreen`
