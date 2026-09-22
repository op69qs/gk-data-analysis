# 20260923 快报收入上报链路（DDL + 检查）

## 用途

1. 在目标库创建 `stg.trs_tmis_budget_income_provinces`
2. 检查「上报 → STG → Event/过程 → vs_* → 大屏」链路是否通顺
3. 将检查结果（含时间字段）反馈后统一评估

快报收入与指标库收支存共用「数据上报」页，**异步处理、完成/异常状态沿用原 TIMS 批次逻辑**：

- 上传弹窗选择「快报收入」时可下载原 DIS「快报_收入数据.xls」模板（`GET /reporting/templates/FLASH_INCOME`）
- 上传成功仅表示文件已接收，后台继续 PARSE/LOAD
- 完成：列表状态成功，可看成功行数
- 异常：列表失败/部分失败，详情可看 `report_parse_error` 与任务 `result_summary`
- 关闭页面不中断后台任务（与收支存相同）

## 执行顺序

| 顺序 | 脚本 | 何时执行 |
|------|------|----------|
| 1 | [01_create_stg_trs_tmis_budget_income_provinces.sql](01_create_stg_trs_tmis_budget_income_provinces.sql) | 快报：部署前建表 |
| 2 | [02_chain_precheck.sql](02_chain_precheck.sql) | 快报：上报前 |
| 3 | 应用上报（TIMS 快报收入 / 或收支存） | 异步 |
| 4 | [03_chain_post_upload_check.sql](03_chain_post_upload_check.sql) | 快报：上报后 |
| 5 | （可选）[04_manual_trigger_vscreen.sql](04_manual_trigger_vscreen.sql) | 手工触发日编排 |
| — | [00_ips_chain_assessment.md](00_ips_chain_assessment.md) | **收支存链路评估说明** |
| 6 | [05_ips_chain_precheck.sql](05_ips_chain_precheck.sql) | **收支存：上报前** |
| 7 | [06_ips_chain_post_upload_check.sql](06_ips_chain_post_upload_check.sql) | **收支存：上报后** |

## 收支存注意

收入/支出大屏多在 `p_task_vscreen_month_end`，仅当 Excel `d_acct` 规范化后等于**月末日**才自动跑。详见 [00_ips_chain_assessment.md](00_ips_chain_assessment.md)。

## 反馈时请一并提供

- 执行库名、执行人、开始/结束时间（服务器时间）
- 每个脚本完整结果集（或截图/导出）
- 上报批次号 `batch_id`、所选账期、上传完成时间
- 若有异常：批次状态、`report_task` 摘要、`report_parse_error` 内容

## 关键约定

- STG 写入：`data_date = 账期 yyyyMM`，`batch_date = 上传当天 yyyyMMdd`
- 日编排：`visual_screen_p_task_vs` → `CALL p_task_vscreen(当天)`，按 `BATCH_DATE` 发现 provinces
