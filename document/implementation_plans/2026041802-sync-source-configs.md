# 源码配置同步检查与修正

## 背景

在后端模块排障过程中，已经确认多个模块长期从 `target` 目录启动。此时如果只修复 `target` 下的配置，而源码 `src/main/resources` 中的配置未同步，下次重新打包或直接从源码启动时，会把旧配置再次带回运行环境。

## 本次检查范围

- `org-tribe-system`
- `fixedReport`
- `dwbi-statistical-analysis`
- `dwbi-system-docking`
- `seo`
- `indicatorsLibv-1.0`
- `inspect`

## 检查方法

1. 对比各模块 `src/main/resources` 与 `target` 下的 `application*.yml`、`application.properties`。
2. 重点核对数据库、Redis、Eureka，以及 `TEMPLATE_FILE_PATH`、`fileSavePath`、`restartCount` 等启动敏感项。
3. 对已经发现漂移的模块，直接把源码配置同步回当前可用环境。

## 结果

### fixedReport

- 源码 `application-dev.yml` 曾回退到 `localhost:3306`、`root/root`。
- 已同步为 `jdbc:mysql://cui02:3308/jeecg-boot-os`、`root/1i&Lutjh2#RtC&`。

### 其余已存在 target 配置的模块

- `org-tribe-system`
- `dwbi-statistical-analysis`
- `dwbi-system-docking`
- `seo`
- `indicatorsLibv-1.0`

以上模块当前源码与 `target` 关键运行配置一致，未发现新的源码/产物漂移。

### inspect

- `inspect/target` 当前没有完整的外置配置文件可供比对，说明尚未形成同样的打包产物状态。
- 但源码 `application-dev.yml` 中存在活动配置 `http://localhost:8762/eureka/`，与当前统一运行环境不一致。
- 已将其收敛为 `http://192.168.160.244:8761/eureka/`，避免后续启动 `inspect` 时误连本机 Eureka。

## 结论

本轮检查后，源码侧剩余需要同步的关键配置已处理完。后续如果再次出现“`target` 能跑、源码重启失败”的情况，应优先同时核对源码和 `target` 下的配置文件，而不是只看当前运行日志。