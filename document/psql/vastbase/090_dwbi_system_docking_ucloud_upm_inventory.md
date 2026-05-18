# dwbi-system-docking 中 ucloud / upm 调用点清单

## 结论

- 当前 MySQL `cui02:3308` 上没有 `ucloud`、`upm` schema。
- 但 `dwbi-system-docking` 代码中明确存在这两个 schema 的表写入和过程调用。
- 这说明 `ucloud/upm` 不是“没被代码使用”，而是“代码仍依赖，但真实源库位置不在本次提供的 MySQL 实例中”。

## 应用调度链

- `dwbi-system-docking/src/main/java/org/dockingProjects/config/ProcessConfig.java`
  - `run(...)` 会从 Redis 读取 `CRON_<processId>`，然后通过 `CronTrigger` 拉起子进程任务。
- `dwbi-system-docking/src/main/java/org/dockingProjects/utils/ApiTaskUtil.java`
  - 在接口取数和入库完成后，会拼接 `procedurMap`，再调用 `mysqlConfigService.callProcedure(procedurMap)`。
- `dwbi-system-docking/src/main/java/org/dockingProjects/service/impl/ConfigServiceImpl.java`
  - `callProcedure(...)` 会分发到 `ConfigMapper` 中的 5 个可调用存储过程入口。

## MyBatis 中已确认的过程调用点

文件：
- `dwbi-system-docking/src/main/resources/mybatis/mysql/ConfigMapper.xml`

已确认 `CALL` 目标：

- `ucloud.ucloud_api_interface_alarm_data`
- `ucloud.ucloud_api_interface_system_data`
- `upm.upm_proc_api_alarm_summary_alarmlog`
- `upm.upm_proc_api_alarm_summary_netper`
- `upm.upm_proc_api_alarm_summary_interface`

对应 SQL 片段：

- `CALL ucloud.ucloud_api_interface_alarm_data(#{params.tableName},#{params.date}, #{params.returnVal,mode=OUT,jdbcType=VARCHAR});`
- `CALL ucloud.ucloud_api_interface_system_data(#{params.tableName},#{params.date}, #{params.returnVal,mode=OUT,jdbcType=VARCHAR});`
- `CALL upm.upm_proc_api_alarm_summary_alarmlog(#{params.tableName}, #{params.returnVal,mode=OUT,jdbcType=VARCHAR});`
- `CALL upm.upm_proc_api_alarm_summary_netper(#{params.tableName}, #{params.returnVal,mode=OUT,jdbcType=VARCHAR});`
- `CALL upm.upm_proc_api_alarm_summary_interface(#{params.tableName}, #{params.returnVal,mode=OUT,jdbcType=VARCHAR});`

## 代码中已确认的表依赖

同一份 `ConfigMapper.xml` 还直接写入了 `upm` 表：

- `upm.netperformanceeventlog_${params.tableMonth}`
- `upm.alarmlogabnormalbehavior_${params.tableMonth}`

并且存在建表语句：

- `CREATE TABLE IF NOT EXISTS upm.netperformanceeventlog_${params.tableMonth} (...)`
- `CREATE TABLE IF NOT EXISTS upm.alarmlogAbnormalbehavior_${params.tableMonth} (...)`

另外还存在对 `ucloud.api_interface_alarm_data` 的直接写入。

## 对迁移的影响

- `dwbi-system-docking` 的调度逻辑不只是“调用缺失过程”，还依赖 `ucloud/upm` 业务表。
- 即使把当前已知 `report / edw / visual_screen / indicators_lib / ods` 迁完，`ucloud/upm` 真实源对象未补齐时，`UClOUD_PROCESS` 和 `UPM_PROCESS` 这两条链依然无法在 Vastbase 上完整闭环。

## 后续建议

1. 在历史数据库实例、备份 SQL 包或部署机上继续检索 `ucloud`、`upm` schema 的 DDL。
2. 优先找这 5 个被代码直接 `CALL` 的过程定义。
3. 同时补齐 `upm.netperformanceeventlog_*`、`upm.alarmlogabnormalbehavior_*`、`ucloud.api_interface_alarm_data`、`ucloud.api_interface_system_data*` 的表结构来源。
