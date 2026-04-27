# 修复 org-tribe-system websocket 端点未注册问题

## 现象

- GK 主平台 9090 已能正常承载 vis 页面，但登录后页面顶部通知模块持续报 websocket 握手失败。
- 浏览器运行态错误为：`ws://127.0.0.1:9090/websocket/{userId}` 握手返回 `404`。
- 该问题不会阻塞 vis 页面展示，但会让消息通知链路失效，并持续产生日志噪音。

## 排查结论

- 前端 [org-tribe-view/src/components/tools/HeaderNotice.vue](d:/document/c-project/project-code/project-02/gk-data-analysis/org-tribe-view/src/components/tools/HeaderNotice.vue) 使用的连接地址是 `/websocket/{userId}`。
- 后端 [org-tribe-system/src/main/java/org/jeecg/modules/message/websocket/WebSocket.java](d:/document/c-project/project-code/project-02/gk-data-analysis/org-tribe-system/src/main/java/org/jeecg/modules/message/websocket/WebSocket.java) 也声明了 `@ServerEndpoint("/websocket/{userId}")`。
- 但 `org-tribe-system` 中缺少 `ServerEndpointExporter` 注册器，因此 `@ServerEndpoint` 并没有被 Spring Boot / BES 容器真正注册成可握手端点。
- 对比 `vis-screen-backend` 可见，其在 `jeecg-boot-base-common` 中已有 [vis-screen-backend/jeecg-boot-base-common/src/main/java/org/jeecg/config/WebSocketConfig.java](d:/document/c-project/project-code/project-02/gk-data-analysis/vis-screen-backend/jeecg-boot-base-common/src/main/java/org/jeecg/config/WebSocketConfig.java) 做同类注册。

## 修改

- 在 `org-tribe-system` 新增 `org.jeecg.config.WebSocketConfig`。
- 通过 `ServerEndpointExporter` 将 `@ServerEndpoint` 标注的 websocket 端点注册进容器。
- 本次只补注册器，不改前端 websocket 地址，不改消息业务逻辑。

## 验证目标

1. `org-tribe-system` 编译通过。
2. 重新启动 9090 后，`/websocket/{userId}` 不再返回 `404`。
3. 浏览器顶部通知模块不再出现 websocket 握手 `404`。