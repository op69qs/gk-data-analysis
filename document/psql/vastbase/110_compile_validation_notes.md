# Vastbase 编译验证记录

## 已确认的语法边界

- 可用：
  - `CREATE PROCEDURE schema.proc() BEGIN ... END;`
- 不可直接使用：
  - `CREATE DEFINER=\`user\`@\`%\` PROCEDURE ...`
  - `DELIMITER $$ ... $$`

因此本轮新增了：

- `tools/normalize_vastbase_mysql_bundle.sh`
  - 用于把源过程直迁包转换为可执行版脚本。
- `100_etl_logging_stubs.sql`
  - 先补齐 `etl` 日志过程桩，解除大量业务过程的编译依赖。

## 事务编译验证方式

- 所有验证都通过 `psql -v ON_ERROR_STOP=1` 执行。
- 使用 `BEGIN; ... ROLLBACK;` 包裹，保证失败时不在目标库留下半成品。

## 当前结果

### 已编译通过

- `180_visual_screen_direct_dependencies_batch1_executable.sql`
  - 在带 `100_etl_logging_stubs.sql` 的事务中整包通过。
- `181_visual_screen_direct_dependencies_batch2_executable.sql`
  - 在带 `100_etl_logging_stubs.sql` 的事务中整包通过。
- `182_visual_screen_direct_dependencies_batch3_executable.sql`
  - 修复源脚本截断与残留尾部后，在带 `100_etl_logging_stubs.sql` 的事务中整包通过。
- `151_report_p_news_flash_month_text_number_rewrite.sql`
  - 已改写为集合化 SQL 版本，并完成事务编译验证。
- `171_edw_p_trs_budget_income_compare_rewrite.sql`
  - 已改写为集合化 SQL 版本，并完成事务编译验证。
- `161_visual_screen_task_rewrite.sql`
  - 已改写为 `P_task_vscreen_daily / P_task_vscreen_month_end + EXECUTE IMMEDIATE` 编排版本。
  - 与 `180 / 181 / 182` 依赖包、`100_etl_logging_stubs.sql` 联合事务编译通过。
- `021_indicators_lib_init_report02.sql`
  - 已单独完成事务编译验证。
- `024_indicators_lib_orchestration_rewrite.sql`
  - `p_exe_formula_hand / p_exe_formula / init_report01 / init_report03 / p_xunhuan_formula`
  - 已完成事务编译验证。
- `172_indicators_lib_p_trs_budget_income_compare_xin_rewrite.sql`
  - 已完成事务编译验证。
- `030_events.sql`
  - 在加载 `010 / 151 / 161 / 171 / 172 / 024` 等重写依赖后，7 个替换 Event 已完成事务创建验证。
- `report` 运行链联合验证
  - `P_MONTH_REPORT_TEXT`
  - `P_NEWS_FLASH_MONTH_REPORT_TEXT`
  - `P_NEWS_FLASH_MONTH_TEXT_NUMBER`（重写版 `151`）
  - `P_NEWS_FLASH_QUARTER_REPORT_TEXT`
  - `P_QUARTER_REPORT_TEXT`
  - 已与 `171 / 172 / 024` 在同一事务中联合编译通过。
- `vastbase/final/001 -> 007`
  - 已完成最终初始化包的联合事务验证。
  - 顺序为：
    - `001_etl_init.sql`
    - `002_ods_init.sql`
    - `003_report_init.sql`
    - `004_edw_init.sql`
    - `005_visual_screen_init.sql`
    - `006_indicators_lib_init.sql`
    - `007_events_init.sql`
  - 结果：
    - 全部过程脚本联合加载通过。
    - 7 个 Event 联合创建通过。
    - 事务末尾已正常 `ROLLBACK`。

说明：

- `visual_screen.P_task_vscreen*` 的三批直接依赖过程，目前已经具备“可编译落库”的基础。
- `report.P_NEWS_FLASH_MONTH_TEXT_NUMBER` 和 `edw.P_TRS_BUDGET_INCOME_COMPARE` 已有 Vastbase 可编译替代版本。
- `visual_screen.P_task_vscreen / P_task_vscreen1 / P_task_vscreen_new` 已有 Vastbase 可编译替代版本。
- `indicators_lib` 面向 Event 的主编排过程现在也已有可编译替代版本。
- `030_events.sql` 已从“语法准备态”进入“可事务创建验证通过”状态。
- `report` 被 `p_xunhuan_formula` 直接调用的核心月报/季报过程已完成联合编译验证。

### 部分通过后失败

- `150_report_core_executable.sql`
  - `report.P_MONTH_REPORT_TEXT` 通过
  - `report.P_NEWS_FLASH_MONTH_REPORT_TEXT` 通过
  - `report.P_NEWS_FLASH_MONTH_TEXT_NUMBER` 失败
  - 失败信息：
    - `subprogram body is not ended correctly at end of input`

说明：

- `report` 当前不是头部语法问题，而是第三个过程体内部控制流还需要进一步改写。

### 当前仍失败

- `170_edw_core_executable.sql`
  - 首个过程 `edw.P_TRS_BUDGET_INCOME_COMPARE` 仍会因原始 MySQL 控制流写法失败。
  - 当前应改用 `171_edw_p_trs_budget_income_compare_rewrite.sql`。
- `160_visual_screen_task_executable.sql`
  - 原始规范化版仍会因 `CURSOR + LOOP + IF ... END IF` 失败。
  - 当前应改用 `161_visual_screen_task_rewrite.sql`。

## 额外语法探针结论

- `SET done = CASE WHEN ... END`
  - 可编译
- `IF ... THEN ... END IF`
  - 不可编译
- `WHILE ... DO ... END WHILE`
  - 不可编译
- `label: LOOP ... END LOOP`
  - 不可编译

说明：

- 后续对 `report.P_NEWS_FLASH_MONTH_TEXT_NUMBER`、`edw.P_TRS_BUDGET_INCOME_COMPARE`、`visual_screen.P_task_vscreen*` 的处理，应优先改成集合化 SQL 或其它不依赖 MySQL 过程控制的写法。

## 当前最值得继续推进的方向

1. 先正式落 `100_etl_logging_stubs.sql`。
2. 再优先落 `180 / 181 / 182` 三批 `visual_screen` 直接依赖可执行版。
3. 将 `151_report_p_news_flash_month_text_number_rewrite.sql` 和 `171_edw_p_trs_budget_income_compare_rewrite.sql` 并入最终初始化包。
4. 开始整理最终交付所需的“按 schema 初始化脚本”分层清单，只保留已验证可用脚本进入最终包。
