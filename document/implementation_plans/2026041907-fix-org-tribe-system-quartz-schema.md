# 修复 org-tribe-system Quartz schema 定位错误

## 问题现象

- `org-tribe-system` 启动时在 Quartz 恢复阶段报错：`relation "qrtz_locks" does not exist on vastbase`。
- 报错随后被包装成 `current transaction is aborted`，但根因是 Quartz 在错误 schema 下查表。
- 当前库里的 Quartz 表实际位于 `jeecg-boot-os` schema，不在 `public`。

## 根因分析

- `org-tribe-system/src/main/resources/application-dev.yml` 中 `spring.datasource.url` 仍配置为 `currentSchema=public`。
- `org-tribe-system/src/main/resources/application-dev.yml`、`application-test.yml`、`application-prod.yml` 中动态主数据源 `master.url` 也都仍指向 `currentSchema=public`。
- `jeecg_database.properties` 与 `deploy-package/config/org-tribe-system` 下的外部化配置也保持同样的旧值，说明这是一次源码与部署配置同步漂移，不是单次运行环境偶发覆盖。
- Quartz 未显式声明带 schema 的 `tablePrefix`，因此默认按连接 `search_path` 去找 `qrtz_*` 表，最终命中 `public`。

## 修改内容

- 将 `org-tribe-system` 源码中的 PostgreSQL 连接串统一改为：
  - `currentSchema=%22jeecg-boot-os%22,public`
- 同步修改以下文件中的对应 URL：
  - `org-tribe-system/src/main/resources/application-dev.yml`
  - `org-tribe-system/src/main/resources/application-test.yml`
  - `org-tribe-system/src/main/resources/application-prod.yml`
  - `org-tribe-system/src/main/resources/jeecg/jeecg_database.properties`
  - `deploy-package/config/org-tribe-system/application-dev.yml`
  - `deploy-package/config/org-tribe-system/application-test.yml`
  - `deploy-package/config/org-tribe-system/application-prod.yml`
- 在三个 `application-*.yml` 中增加 Quartz 显式表前缀：
  - `spring.quartz.properties.org.quartz.jobStore.tablePrefix='"jeecg-boot-os".qrtz_'`

## 这样改的原因

- `currentSchema` 先放 `jeecg-boot-os`，再回退到 `public`，可覆盖主系统中依赖默认 schema 的未限定对象。
- `jeecg-boot-os` 含连字符，JDBC URL 中使用 `%22...%22` 编码双引号，避免 schema 名在连接参数里被错误解析。
- Quartz 再额外指定 `tablePrefix`，即使后续某个环境把默认 schema 改回 `public`，调度器仍会直接访问 `"jeecg-boot-os".qrtz_*`，不再依赖 `search_path`。

## 验证情况

- 编辑器静态检查：上述 YAML / properties 文件均无错误。
- 已触发 `mvn -pl org-tribe-system -am -DskipTests compile` 验证。
- 该模块构建链会先执行 `org-tribe-view` 的 `npm ci` 与 `npm run build`，因此编译耗时较长；最终结果以本轮终端输出为准。

## 边界说明

- 本次未改 Quartz 建表脚本，也未新增数据库对象；问题不在缺表，而在 schema 解析错误。
- 停机时的 `RxIoScheduler` 线程泄漏告警是应用启动失败后的清理告警，不是本次 Quartz 根因。