# 收支存（指标库收入/支出/库存）→ 大屏联调链路评估

依据：`document/psql/vastbase/final`（005 过程 + 007 Event）+ 现有 TIMS 上报实现 + vis-screen-backend。

## 总判（设计层，假定生产已执行 final）

| 业务 | 上报写 STG | 日编排发现 | 叶子过程写 vs_* | 大屏读 vs_* | 设计是否完整 |
|------|------------|------------|-----------------|-------------|--------------|
| **库存 STOCK** | `stg.trs_tmis_stock` | `BATCH_DATE` + `D_ACCT` | **日跑** `p_task_vscreen_daily` 内 4 个库存过程 | 有（如 `vs_inventory_*`） | **完整** |
| **收入 INCOME** | `stg.trs_tmis_budget_income` | 同上 | 多数在 **月末** `p_task_vscreen_month_end` | 有（如 `vs_budget_revenue` 等） | **条件完整**（见下） |
| **支出 PAYOUT** | `stg.trs_tmis_budget_payout` | 同上 | 多数在 **月末** `month_end` | 有（如 `vs_region_pay` 等） | **条件完整**（见下） |

**结论：final 下三条上报→展示链路在对象上是齐的；库存可按日刷新；收入/支出大屏结果依赖「账期日 = 月末日」才走进 month_end。**

---

## 调用链（与快报共用编排入口）

```text
数据上报页 TIMS（INCOME/PAYOUT/STOCK）异步入库
  → STG（batch_date=当天 yyyyMMdd，d_acct=Excel 日期原文，data_date=账期）
  → Event visual_screen_p_task_vs
  → CALL p_task_vscreen(当天)
       按 BATCH_DATE 从四张 STG 取 D_ACCT（收入/支出/库存/快报 provinces）
  → 每个 d_acct：
       必调 p_task_vscreen_daily(d_acct)     ← 库存 + 快报 provinces
       仅当 d_acct = LAST_DAY(d_acct) 时
            再调 p_task_vscreen_month_end(d_acct)  ← 收入/支出类大屏
  → visual_screen.vs_* → vis-screen-backend → 前端
```

日期规范化（005）：

- `D_ACCT` 能格式化为 `%Y-%m-%d` → 直接用
- 否则 `CONCAT(D_ACCT,'01')`（例如 Excel 填 `202511` → `2025-11-01`）

因此：若收入/支出 Excel 日期只填 `yyyyMM`，会变成**月初**，**不会**触发 month_end，收入/支出相关大屏可能不更新；应填**该月最后一天**（或能解析为月末的日期），或手工 `CALL p_task_vscreen_month_end('yyyy-mm-dd')`。

---

## STG → 过程 → vs_* → 前端（摘要）

### 库存（日跑，联调优先验证）

| 过程 | 结果表（示例） | 前端 |
|------|----------------|------|
| `p_vs_inventory_analyze/area/balance/form` | `vs_inventory_*` | QueryTableData 等读 `vs_inventory_area` |

### 收入（月末）

| 过程（节选） | 结果表 | 前端 |
|--------------|--------|------|
| `p_vs_budget_revenue` | `vs_budget_revenue` | QueryTableData / QueryMapData |
| `p_vs_tax_revenue` / `three_budget` / `public_budget` / `growth_public_budget` 等 | 对应 `vs_*` | 同上 |

### 支出（月末）

| 过程（节选） | 结果表 | 前端 |
|--------------|--------|------|
| `p_vs_region_pay` / `subject_pay` / `purpose_pay` / `economic_pay` / `area_pay` | 对应 `vs_*` | QueryTableData / QueryMapData |

### 快报收入（日跑，另见 01–04）

| 过程 | 结果表 |
|------|--------|
| `p_vs_revenu_display` 等 5 个 | `vs_revenu_display` 等 |

---

## 应用侧（已实现，与 final 衔接点）

- 异步：与收支存同一套批次/任务/解析错误告知
- `batch_date`：上传当天 `yyyyMMdd`（`TimsReportProcessingService`）→ 对齐 Event 入参
- 收入/支出：`d_acct` = Excel 日期原文（决定能否进 month_end）
- 库存：`d_acct` = Excel 日期；日跑即可出库存大屏

---

## 联调检查脚本（本目录）

| 脚本 | 用途 |
|------|------|
| [05_ips_chain_precheck.sql](05_ips_chain_precheck.sql) | 上报前：三张 STG、编排过程、Event 时间 |
| [06_ips_chain_post_upload_check.sql](06_ips_chain_post_upload_check.sql) | 上报后：STG/批次/`vs_*`/是否满足月末条件 |
| [04_manual_trigger_vscreen.sql](04_manual_trigger_vscreen.sql) | 共用：手工触发 `p_task_vscreen` |

反馈要求同 [README.md](README.md)：带服务器时间、`pg_job` 上次成功时间、批次号、账期、Excel 中 `d_acct` 样例。
