# vis-screen-backend Vastbase 适配记录

## 范围

- 模块：`vis-screen-backend/jeecg-boot-module-system`
- 目标：把 `visualScreen` 主路径查询与数据源基线从 MySQL 收口到 Vastbase/PostgreSQL 兼容写法

## 本次调整

- 数据源统一切到 `jdbc:postgresql://100.71.11.54:25432/gk_data_analysis?currentSchema=visual_screen,public`
- 驱动统一切到 `org.postgresql.Driver`
- Druid `validationQuery` 改为 `SELECT 1`
- 去掉 `druid.stat.mergeSql=true`，仅保留慢 SQL 阈值配置
- `jeecg_database.properties` 同步到 Vastbase 基线
- `jeecg-cloud-module/config/jeecg-cloud-application-beta.yml` 的活动主数据源也同步到同一套 Vastbase 基线
- `CommonUtils`、`SysBaseApiImpl`、`SqlUtils` 将 `vastbase` 识别为 PostgreSQL 兼容类型，避免运行时数据库产品名识别失败
- 移除 `jeecg-boot-base-common` 中未被当前源码直接使用的 `hibernate-re` 依赖，规避其内置 `OnlAuthDataMapper.xml` 在 fat jar 启动时触发的重复 MyBatis `resultMap` 冲突
- `visualScreen` 关键 mapper 收口：
  - `IFNULL`/`ifnull` -> `COALESCE`
  - MySQL 日期回退表达式 -> `TO_CHAR`/`TO_DATE`/`INTERVAL`
  - MySQL 分页 `LIMIT offset,size` -> `LIMIT size OFFSET offset`
  - MySQL 行号变量 `@rownum` -> `ROW_NUMBER() OVER (...)`
  - 同比/环比分母统一加 `NULLIF(..., 0)`
  - 修正 PostgreSQL 下会报错的聚合语义，例如缺失 `GROUP BY`、分组列与选择列不一致

## 重点文件

- `vis-screen-backend/jeecg-boot-module-system/src/main/resources/application-dev.yml`
- `vis-screen-backend/jeecg-boot-module-system/src/main/resources/application-test.yml`
- `vis-screen-backend/jeecg-boot-module-system/src/main/resources/application-prod.yml`
- `vis-screen-backend/jeecg-boot-module-system/src/main/resources/jeecg/jeecg_database.properties`
- `vis-screen-backend/jeecg-cloud-module/config/jeecg-cloud-application-beta.yml`
- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/mapper/xml/QueryTableDataMapper.xml`
- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/mapper/xml/QueryMapDataMapper.xml`
- `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/mapper/xml/QueryDataMapper.xml`

## 验证

- 执行：`mvn -pl jeecg-boot-module-system -am -DskipTests compile`
- 结果：`BUILD SUCCESS`
- 执行：`mvn --% -f vis-screen-backend/pom.xml -pl jeecg-boot-module-system -am -Dmaven.test.skip=true package`
- 结果：`BUILD SUCCESS`
- 执行：在 `jeecg-boot-module-system/target` 下运行 `java -jar jeecg-boot-module-system-2.3.0.jar`，关闭 Eureka 注册
- 结果：应用越过原 `OnlAuthDataMapper` 重复加载异常，已完成 Vastbase 数据源初始化并继续进入 Quartz/Swagger/任务线程池启动阶段
- 复扫 `visualScreen/mapper/xml` 后，未再命中本轮关注的 MySQL 方言关键字：`IFNULL`、`DATE_FORMAT`、`DATE_SUB`、`@rownum`、`cast(... as signed)`、`LIMIT offset,size`
- 扩大到 `vis-screen-backend` 源码与配置复扫后，活动配置中的 MySQL 数据源已清除；剩余命中主要是 `SysLogMapper.xml` 的 `MYSQL` 兼容分支和个别注释/target 产物

## 说明

- 当前验证以编译和静态复扫为主；仓库默认测试未覆盖这些 SQL 的运行态。
- 如果后续要做联调，优先验证 `visualScreen` 页面里的同比/环比卡片、地图分区排行、库存区域排行和方案分页列表。