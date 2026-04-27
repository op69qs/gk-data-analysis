# 修复 vis-screen-backend 开发环境 Redis 默认地址

## 背景

- `vis-screen-backend` 的 `jeecg-boot-module-system` 默认激活 `dev` 配置，并在本地使用 8081 端口。
- 当前运行日志持续出现 `Unable to connect to 192.168.160.30:6379`，随后在 `ShiroRealm` 中打印 `Redis token校验失败，回退为JWT验签`。
- 代码排查确认：JWT 回退逻辑本身仍然成立，真正的问题是 `application-dev.yml` 中 `SPRING_REDIS_HOST` 的默认值仍然指向历史内网地址 `192.168.160.30`。

## 本次修改

- 将 `vis-screen-backend/jeecg-boot-module-system/src/main/resources/application-dev.yml` 中的 Redis 默认地址从 `192.168.160.30` 改为 `127.0.0.1`。
- 保留 `SPRING_REDIS_HOST` / `SPRING_REDIS_PORT` 环境变量覆盖能力，不影响显式配置的环境。
- 未修改 `application-test.yml` 和 `application-prod.yml`，避免对部署环境的既有默认行为造成额外扰动。

## 结论

- 这次日志里的 `Redis token校验失败` 根因已经定位，不是 Shiro 回退逻辑失效，而是开发环境默认 Redis 地址漂移到了不可达的历史 IP。
- 配置改动生效后，本地若已启动 Redis，将不再继续访问 `192.168.160.30:6379`；若本机没有 Redis，则日志会改为反映本地 Redis 未启动，而不是误连旧内网地址。
