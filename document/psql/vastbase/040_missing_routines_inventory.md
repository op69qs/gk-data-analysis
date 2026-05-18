# 目标 Vastbase 缺失 routine 说明

## 这句话具体是什么意思

“`report / edw / visual_screen / etl` 在目标 Vastbase 当前仍缺 routine” 的意思是：

- 目标库 `gk_data_analysis` 里这些 schema 的表多数已经存在。
- 但 `information_schema.routines` 中还没有这些 schema 下的 `procedure/function`。
- 所以当前我们已经生成的某些过程脚本和事件脚本，虽然语法上可以创建，但一旦执行到 `CALL report...`、`CALL edw...`、`CALL visual_screen...`、`CALL etl...`，就会因为被调用对象不存在而失败。

本轮实查结果：

- 目标 Vastbase:
  - `report`: `0` routines
  - `edw`: `0` routines
  - `visual_screen`: `0` routines
  - `etl`: `0` routines
- 源 MySQL:
  - `edw`: 存在大量过程，例如：
    - `P_TRS_BUDGET_INCOME_COMPARE`
    - `P_TRS_BUDGET_INCOME_COMPARE_NEW`
    - `P_TRS_FINANCE_TAX_STATIS`
    - `P_TRS_FINANCE_TAX_STATIS_NEW`
  - `report`: 已确认存在：
    - `P_MONTH_REPORT_TEXT`
    - `P_NEWS_FLASH_MONTH_REPORT_TEXT`
    - `P_NEWS_FLASH_MONTH_TEXT_NUMBER`
    - `P_NEWS_FLASH_QUARTER_REPORT_TEXT`
    - `P_QUARTER_REPORT_TEXT`
    - `p_trs_income_payout_statistics`
  - `visual_screen`: 已确认存在：
    - `P_task_vscreen`
    - `P_task_vscreen_new`
    - 大量 `p_vs_*` 过程
- `etl`:
    - 当前迁移链里主要是被 `indicators_lib`、`report`、`visual_screen`、`edw` 过程用于日志记录的 `ETL.EDW_PROC_TRACE_LOG` / `ETL.EDW_PROC_ERROR_LOG`
    - 本轮追加导出 `edw/etl` 后，确认 `edw` DDL 已拿到；`etl` 过程定义仍未在追加导出文件中落到可用过程体，后续需要继续补源。

## 当前已生成脚本受哪些缺失对象影响

### `020_indicators_lib_orchestration.sql`

直接依赖但目标库尚不存在的 routine：

- `indicators_lib.p_trs_budget_income_compare_xin`
- `indicators_lib.p_exe_formula`
- `edw.p_trs_budget_income_compare`
- `edw.p_trs_budget_income_compare_xin`
- `report.p_quarter_report_text`
- `report.p_news_flash_quarter_report_text`
- `report.p_month_report_text`
- `report.p_news_flash_month_report_text`
- `report.p_news_flash_month_text_number`

说明：

- `p_exe_formula_hand`
  - 当前脚本可独立继续收敛，因为它主要依赖 `indicators_lib.lib_index_formula` 和其动态 SQL 内容。
- `init_report01`
  - 只依赖 `p_exe_formula_hand`，是当前最接近可单独落库验证的一批。
- `init_report03`
  - 还依赖 `indicators_lib.p_trs_budget_income_compare_xin`。
- `p_xunhuan_formula`
  - 依赖面最广，涉及 `edw/report/indicators_lib` 三层过程。

### `030_events.sql`

7 个 Event 中，理论上只有一个最接近可独立启用：

- `ods_pt_gy_files_task`
  - 只依赖 `ods.p_pt_gy_files_temp`

其余 Event 当前仍受依赖缺失影响：

- `indicators_lib_p_init_report01`
  - 依赖 `indicators_lib.init_report01 -> indicators_lib.p_exe_formula_hand`
- `indicators_lib_p_init_report02`
  - 依赖 `indicators_lib.init_report02`
- `indicators_lib_p_init_report03`
  - 依赖 `indicators_lib.init_report03 -> indicators_lib.p_trs_budget_income_compare_xin`
- `indicators_lib_p_xunhuan_formula`
  - 依赖 `indicators_lib.p_xunhuan_formula -> edw/report/...`
- `seo_p_task_vs`
  - 依赖 `visual_screen.p_task_vscreen`
- `visual_screen_p_task_vs`
  - 依赖 `visual_screen.p_task_vscreen`

## 建议迁移顺序

1. 先落并验证 `ods` 过程。
2. 再落 `indicators_lib.p_exe_formula_hand` 与 `init_report01`。
3. 再补 `indicators_lib.init_report02 / p_exe_formula_history_hand`。
4. 补 `report` 关键 5 个文本报表过程。
5. 补 `edw` 中被 `indicators_lib` 直接调用的 2 到 4 个关键过程。
6. 补 `visual_screen.p_task_vscreen` 及其直接调用链。
7. 最后再启用 `030_events.sql` 中除 `ods_pt_gy_files_task` 外的事件。

## 本轮已补充的源过程直迁包

- `050_report_core_source_port.sql`
  - 包含：
    - `report.P_MONTH_REPORT_TEXT`
    - `report.P_NEWS_FLASH_MONTH_REPORT_TEXT`
    - `report.P_NEWS_FLASH_MONTH_TEXT_NUMBER`
    - `report.P_NEWS_FLASH_QUARTER_REPORT_TEXT`
    - `report.P_QUARTER_REPORT_TEXT`
    - `report.p_trs_income_payout_statistics`
- `060_visual_screen_task_source_port.sql`
  - 包含：
    - `visual_screen.P_task_vscreen`
    - `visual_screen.P_task_vscreen1`
    - `visual_screen.P_task_vscreen_new`
- `080_visual_screen_direct_dependencies_batch1.sql`
  - 包含第一批直接依赖，例如：
    - `visual_screen.P_VS_AMOUNT_RANKING_BY_REGION`
    - `visual_screen.p_vs_area_income`
    - `visual_screen.p_vs_area_pay`
    - `visual_screen.P_VS_BUDGET_REVENUE`
    - `visual_screen.P_VS_CHONGQING_ECONOMIC_ZONE`
    - `visual_screen.P_VS_CUSTOMS_IMPORT_DUTIES`
    - `visual_screen.P_VS_CUSTOMS_IMPORT_VAT`
    - `visual_screen.p_vs_customs_income_situation`
    - `visual_screen.P_VS_CUSTOMS_NON_TAX`
    - `visual_screen.P_VS_CUSTOMS_REVENUE`
    - `visual_screen.p_vs_economic_pay`
    - `visual_screen.P_VS_ECONOMIC_TAXATION`
    - `visual_screen.P_VS_FIVE_PROVINCES_IN_SOUTHWEST_CHINA`
    - `visual_screen.P_VS_GEMINI_STRUCTURE`
    - `visual_screen.p_vs_growth_public_budget`
    - `visual_screen.P_VS_IMPORT_DUTY_ON_IMPORTED_ARTICLES`
    - `visual_screen.P_VS_INCOME_EXPENDITURE`
    - `visual_screen.P_VS_INDUSTRY_TAX`
- `081_visual_screen_direct_dependencies_batch2.sql`
  - 包含第二批直接依赖，例如：
    - `visual_screen.p_vs_inventory_analyze`
    - `visual_screen.p_vs_inventory_area`
    - `visual_screen.p_vs_inventory_balance`
    - `visual_screen.P_vs_inventory_form`
    - `visual_screen.P_VS_LAND_TRANSFER`
    - `visual_screen.P_VS_LOCAL_FINANCIAL_RESOURCES`
    - `visual_screen.P_VS_MUNICIPALITIES_DIRECTLY`
    - `visual_screen.P_VS_PILLAR_INDUSTRIES`
    - `visual_screen.P_VS_PUBLIC_BUDGET`
    - `visual_screen.p_vs_purpose_pay`
    - `visual_screen.P_VS_REGIONAL_TAXATION`
    - `visual_screen.p_vs_region_pay`
    - `visual_screen.P_VS_REVENUE_EXPENDITURE`
    - `visual_screen.P_VS_REVENU_DISPLAY`
    - `visual_screen.p_vs_subject_pay`
    - `visual_screen.p_vs_subject_pay_sub`
    - `visual_screen.P_VS_TAX_REVENUE`
    - `visual_screen.P_VS_TAX_SUBJECT`
    - `visual_screen.P_VS_THREE_BUDGET_REVENUE`
- `082_visual_screen_direct_dependencies_batch3.sql`
  - 包含第三批直接依赖：
    - `visual_screen.P_VS_TRANSFER_INCOME`
    - `visual_screen.trs_kyd_enterprise`
    - `visual_screen.trs_kyd_enterprise_rank`
    - `visual_screen.trs_kyd_industry`
- `070_edw_core_source_port.sql`
  - 包含：
    - `edw.P_TRS_BUDGET_INCOME_COMPARE`
    - `edw.P_TRS_BUDGET_INCOME_COMPARE_NEW`
    - `edw.P_TRS_FINANCE_TAX_STATIS`
    - `edw.P_TRS_FINANCE_TAX_STATIS_NEW`
