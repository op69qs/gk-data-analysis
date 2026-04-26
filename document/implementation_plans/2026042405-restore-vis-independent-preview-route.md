# 2026042405 恢复 vis 独立预览路由并打通本地预览闭环

## 本次处理

- 将 GK 前端内 vis 的 3 个预览入口统一切回 `/BigScreenPreview`，恢复独立预览路由语义，同时保留 `returnUrl` 便于从预览返回当前 GK 页面。
- 同步修补 `org-tribe-system/target/classes/static/js` 中当前正在被 9090 服务的 runtime chunk，避免源码已改但 live bundle 仍继续跳 `/vis/preview/fullscreen`。
- 排查并确认 `mvn compile` 不会覆盖 `target/classes/static`，但 `mvn package` 会在 `prepare-package` 阶段先删后拷前端静态资源，因此手工 runtime patch 会被重新打包覆盖。
- 重新拉起本地 `vis-screen-backend` 与 `org-tribe-system` 源码态服务，统一使用 JDK 8。
- 修正 GK 开发态 `zuul.routes.visScreen` 回到 URL 直连模式：`http://127.0.0.1:9082`，避免本地预览继续因 Ribbon 找不到 `visScreen` 实例而返回 500。

## 关键结论

- 独立预览路由 `/BigScreenPreview` 已恢复，不再被前端交互默认导向页面级 `/vis/preview/fullscreen`。
- 当前仓库里的 `gk-data-analysis/document/bes` 目录只有 `bes.lic.txt` 与 `.installtime`，不足以让 BES 通过 license 校验；本地启动 `org-tribe-system` 需要临时复用 `vis_screen/bes` 下的有效 license 目录。
- `vis-screen-backend/jeecg-boot-module-system/target/jeecg-boot-module-system-2.3.0.jar` 不是可执行 fat jar，manifest 无 `Main-Class`，本地应改用 `java -cp target/classes;target/lib/* org.jeecg.JeecgSystemApplication` 方式启动。

## 验证

- 源码修改后，`SchemeList.vue`、`PageList.vue`、`PreviewEntry.vue` 的语法检查通过。
- 9090 日志确认此前 500 的直接原因是 `Load balancer does not have available server for client: visScreen`，不是预览路由本身未生效。
- 9082 已通过类路径方式成功启动，日志显示 `Server listening on port(s): 9082 (http)`。
- 9090 已通过类路径方式成功启动，日志显示 `BES initialized with port(s): 9090 (http)`。

## 剩余状态

- 下一次浏览器复验应基于已修正的 `org-tribe-system` 开发态 Zuul URL 直连配置进行；若仍有 500，再继续看下游 vis 接口本身返回，而不是回到预览路由层排查。