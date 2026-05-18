# gk-data-analysis MySQL -> Vastbase 迁移清单

## 目录说明

- `mysql/source_routines.sql`
  - 从 `cui02:3308` 导出的原始 MySQL routine DDL。
  - 本次导出覆盖 schema: `system_docking, ods, ucloud, upm, indicators_lib, approval, visual_screen, report, seo`。
- `mysql/source_events.sql`
  - 从 `cui02:3308` 导出的原始 MySQL event DDL。
- `mysql/source_routines_edw_etl.sql`
  - 从 `cui02:3308` 额外导出的 `edw/etl` 原始 MySQL routine DDL。
- `mysql/source_events_edw_etl.sql`
  - 从 `cui02:3308` 额外导出的 `edw/etl` 原始 MySQL event DDL。
- `vastbase/010_ods_procedures.sql`
  - 第一批已改写的 `ods` 过程脚本。
- `vastbase/020_indicators_lib_orchestration.sql`
  - 第一批已改写的 `indicators_lib` 编排过程脚本。
- `vastbase/021_indicators_lib_init_report02.sql`
  - `indicators_lib.init_report02` 的源过程直迁版脚本，先保持 B 兼容方言，后续再按实测结果收敛细节。
- `vastbase/030_events.sql`
  - 用于替换 7 个 MySQL Event 的 Vastbase Event 脚本。
- `vastbase/040_missing_routines_inventory.md`
  - 解释“目标 Vastbase 当前仍缺 routine”具体指什么，并列出当前脚本受影响的依赖。
- `vastbase/050_report_core_source_port.sql`
  - `report` 关键文本报表过程的源过程直迁包。
- `vastbase/060_visual_screen_task_source_port.sql`
  - `visual_screen` 关键大屏编排过程的源过程直迁包。
- `vastbase/070_edw_core_source_port.sql`
  - `edw` 关键统计过程的源过程直迁包。
- `vastbase/080_visual_screen_direct_dependencies_batch1.sql`
  - `visual_screen.P_task_vscreen*` 直接依赖过程第一批源过程直迁包。
- `vastbase/081_visual_screen_direct_dependencies_batch2.sql`
  - `visual_screen.P_task_vscreen*` 直接依赖过程第二批源过程直迁包。
- `vastbase/082_visual_screen_direct_dependencies_batch3.sql`
  - `visual_screen.P_task_vscreen*` 直接依赖过程第三批源过程直迁包。
- `vastbase/090_dwbi_system_docking_ucloud_upm_inventory.md`
  - `dwbi-system-docking` 中 `ucloud/upm` 表与过程调用点清单。
- `vastbase/100_etl_logging_stubs.sql`
  - `etl.EDW_PROC_TRACE_LOG / etl.EDW_PROC_ERROR_LOG` 的编译解锁桩脚本。
- `vastbase/110_compile_validation_notes.md`
  - Vastbase 事务编译验证记录与当前卡点。
- `tools/normalize_vastbase_mysql_bundle.sh`
  - 将源过程直迁包规范化为 Vastbase B 兼容可执行脚本的小工具。
- `vastbase/150_report_core_executable.sql`
  - `050_report_core_source_port.sql` 的规范化可执行版。
- `vastbase/160_visual_screen_task_executable.sql`
  - `060_visual_screen_task_source_port.sql` 的规范化可执行版。
- `vastbase/170_edw_core_executable.sql`
  - `070_edw_core_source_port.sql` 的规范化可执行版。
- `vastbase/180_visual_screen_direct_dependencies_batch1_executable.sql`
  - `080_visual_screen_direct_dependencies_batch1.sql` 的规范化可执行版。
- `vastbase/181_visual_screen_direct_dependencies_batch2_executable.sql`
  - `081_visual_screen_direct_dependencies_batch2.sql` 的规范化可执行版。
- `vastbase/182_visual_screen_direct_dependencies_batch3_executable.sql`
  - `082_visual_screen_direct_dependencies_batch3.sql` 的规范化可执行版。
- `vastbase/final/`
  - 面向最终交付的 Vastbase 初始化脚本目录，只保留已完成事务编译验证的成品脚本。
  - `001_etl_init.sql`
    - `etl` 日志桩过程初始化脚本。
  - `002_ods_init.sql`
    - `ods` 过程初始化脚本。
  - `003_report_init.sql`
    - `report` 已验证过程初始化脚本。
  - `004_edw_init.sql`
    - `edw` 已验证过程初始化脚本。
  - `005_visual_screen_init.sql`
    - `visual_screen` 已验证依赖过程和编排过程初始化脚本。
  - `006_indicators_lib_init.sql`
    - `indicators_lib` 已验证过程初始化脚本。
  - `007_events_init.sql`
    - 7 个 Vastbase 替代 Event 初始化脚本。
  - `README.md`
    - 最终交付脚本的执行顺序与说明。

## 已核实的源库现状

- MySQL `cui02:3308`
  - routine 导出文件行数: `20388`
  - event 导出文件行数: `53`
  - `edw/etl` 追加 routine 导出文件行数: `11407`
  - `edw/etl` 追加 event 导出文件行数: `20`
  - 已确认 event 共 7 个:
    - `indicators_lib.p_init_report01`
    - `indicators_lib.p_init_report02`
    - `indicators_lib.p_init_report03`
    - `indicators_lib.p_xunhuan_formula`
    - `ods.pt_gy_files_task`
    - `seo.P_task_vs`
    - `visual_screen.P_task_vs`
- Vastbase `cui02:25432/gk_data_analysis`
  - `sql_compatibility = B`
  - `enable_prevent_job_task_startup = off`
- 当前 `SHOW EVENTS;` 结果为空
- 业务 schema 当前无任何 routine
- 已实测可接受的目标过程语法为 MySQL 兼容风格:
  - `CREATE PROCEDURE schema.proc() BEGIN ... END`
  - 不接受此前试探的 `LANGUAGE plpgsql` 风格
- 已实测可接受的目标 Event 语法包括:
  - `CREATE EVENT ... ON SCHEDULE EVERY 1 DAY DISABLE DO ...`
  - `STARTS 'yyyy-mm-dd hh24:mi:ss'`
  - `COMMENT '...'`

## 本轮迁移策略

- `ods`
  - 直接改写为 Vastbase 过程，优先处理搬运型过程。
- `indicators_lib`
  - 优先改写调度/编排层过程。
  - `p_exe_formula_hand` 采用 Vastbase 上实际存在的 `blob::text` 方式解码公式 SQL。
  - 保留 MySQL B 兼容函数用法，如 `DATE_FORMAT / CURDATE / QUARTER / YEAR`，以降低改写风险。
- `event`
  - 目标库支持 Event，但同一 database 下不支持同名 Event。
  - 因此将源库两个同名 `P_task_vs` 事件改为带 schema 前缀的唯一名称。
  - 由于目标库目前仍缺少一批被调用 routine，本轮 event 脚本默认使用 `DISABLE`，待依赖过程迁入后再启用。

## 仍需补齐的对象

- `indicators_lib.init_report02`
  - 已生成源过程直迁版脚本，但还未做逐段兼容校验。
- `indicators_lib.p_exe_formula_history_hand`
  - 已生成源过程直迁版脚本，但仍需做事务与动态 SQL 行为校验。
- `report / visual_screen / edw`
  - 已补充关键源过程直迁包，下一步应按调用顺序逐个在 Vastbase 实测收敛。
- `visual_screen.P_task_vscreen*` 直接调用链
  - 已拆出三批直接依赖源过程直迁包，覆盖 `p_vs_*`、`P_VS_*` 以及 `trs_kyd_*` 等被编排过程直接调用对象。
- `visual_screen` 直接依赖链
  - 第一批、第二批规范化可执行版已完成事务编译验证，当前可作为后续正式落库候选。
- `report.* / visual_screen.* / edw.* / etl.*`
  - 当前 Vastbase 只有表，无对应 routine。
  - `p_xunhuan_formula` 和 7 个 event 的最终启用依赖这批过程先完成迁移。

## 当前交付状态

- `vastbase/final/` 已形成最终初始化脚本目录。
- 已使用 `psql -v ON_ERROR_STOP=1` 对 `001 -> 007` 全部脚本执行 `BEGIN; ... ROLLBACK;` 联合事务验证。
- 当前整包结果：
  - 过程脚本可联合加载。
  - 7 个 Event 可联合创建。
  - 事务末尾已正常 `ROLLBACK`，验证过程未在目标库留下半成品。

## ucloud / upm 补查结论

- 当前 MySQL `cui02:3308` 的 `information_schema.schemata` 中不存在 `ucloud`、`upm` schema。
- 同机 `information_schema.tables` 中也不存在 `ucloud`、`upm` 表。
- 仓库内仅发现 `dwbi-system-docking/src/main/resources/mybatis/mysql/ConfigMapper.xml` 中 5 处调用点:
  - `ucloud.ucloud_api_interface_alarm_data`
  - `ucloud.ucloud_api_interface_system_data`
  - `upm.upm_proc_api_alarm_summary_alarmlog`
  - `upm.upm_proc_api_alarm_summary_netper`
  - `upm.upm_proc_api_alarm_summary_interface`
- 结论:
  - `ucloud/upm` 不是“当前源库里存在但尚未迁移”的状态。
  - 它们的真实源库位置仍未落在当前仓库和当前 MySQL 实例里，后续需要单独补充来源实例或历史 SQL 包。
