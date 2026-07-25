# 数据分析平台 / 大数据可视化 / 电子登记簿 — 数据加工全量清单

> 本文档补全 `external_data_source_operation_test_manual.md` 未展开的逐条链路说明。  
> 依据仓库内 **`document/dataInterface`** Shell 脚本、**`document/psql`** MySQL 源导出与 Vastbase **`final/`** 交付脚本整理。  
> 数据库：`gk_data_analysis`（Vastbase，`sql_compatibility = B`）。

---

## 0. 分层架构总览

```
┌─────────────────────────────────────────────────────────────────────────────┐
│ 入口层                                                                       │
│  A. Linux crontab Shell（document/dataInterface/shell/）→ ods.* 贴源表       │
│  B. dwbi-system-docking（Redis CRON + HTTP 接口）→ ucloud/upm 动态表        │
│  C. stg.* TMIS 预算/库存数据（源脚本不在 dataInterface，见 §0.1 缺口）       │
└─────────────────────────────────────────────────────────────────────────────┘
                                      ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│ 加工层（存储过程）                                                            │
│  ods → edw → report / indicators_lib → visual_screen（大屏）                 │
│  ucloud/upm 汇总过程                                                         │
│  etl.* 仅为日志桩（EDW_PROC_TRACE_LOG / EDW_PROC_ERROR_LOG）                  │
└─────────────────────────────────────────────────────────────────────────────┘
                                      ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│ 调度层                                                                       │
│  Vastbase Event（7 个，final/007_events_init.sql，默认 DISABLE）              │
│  dwbi-system-docking ProcessConfig（Redis `CRON_<processId>`）               │
│  源 MySQL 另有 edw 层 2 个 Event（未纳入 final/007，见 §3.3）               │
└─────────────────────────────────────────────────────────────────────────────┘
                                      ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│ 展示层                                                                       │
│  org-tribe-system / fixedReport / indicatorsLibv-1.0 / seo                  │
│  vis-screen-backend（schema visual_screen，端口 9093）                        │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 0.1 关键缺口（执行前必读）

| 缺口 | 说明 |
|------|------|
| **`stg.*` 贴源** | 大屏/指标/报表核心过程读 `stg.trs_tmis_budget_income/payout/stock/provinces`，**不是** dataInterface Shell 直接写入；Shell 写的是 `ods.te_*`、`ods.tv_fin_income_detail`、`ods.PT_*` 等。`stg` 数据 historically 由 TMIS 接口或其它 ETL（如 `edw.p_trs_budget_new`）加载，**本仓库无对应 Shell**。 |
| **手册中的 `_1030` 脚本** | `external_data_source_operation_test_manual.md` 提到 `db2_to_mysql_1030.sh` 等，**当前仓库不存在**，仅有 §1 所列 8 个脚本。 |
| **触发器** | MySQL 源导出（`source_routines*.sql`）中 **无 CREATE TRIGGER**。 |
| **ER 存储过程 DDL** | 电子登记簿运行时 `CALL` 的过程定义 **不在本仓库**，仅在 Java/MyBatis 中引用（§5）。 |
| **Vastbase final 覆盖范围** | `final/` 仅含已验证可编译的子集；源 MySQL 另有 **100+** 过程未全部迁入（§4 全量清单）。 |

---

## 1. 全部 Shell 脚本（仓库现存 8 个）

路径根目录：`gk-data-analysis/document/dataInterface/shell/`  
表清单：`gk-data-analysis/document/dataInterface/table_list/payout.list`

### 1.1 生产脚本（需 crontab，建议每天 22:00）

#### 1.1.1 `db2_to_mysql.sh`

| 项 | 内容 |
|----|------|
| **源** | DB2 `tessdb.DB2TESS` @ `11.8.1.17`，用户 `bduser` |
| **方式** | 增量，条件 `S_ENTRUSTDATE='$DATA_DATE'` |
| **日期** | 默认 `DATA_DATE=$(date -d "-3 day" +%Y%m%d)` |
| **中间文件** | `$SHELL_HOME/data_db2/<表名>.txt`（`\|` 分隔） |
| **目标** | Vastbase `ods.<表名>`，先 `DELETE` 同日再 `\copy` |
| **表清单** | `payout.list`：`te_agentbankback_detail_query`、`te_agentbankpay_detail_query` |
| **下游过程** | 间接：`ods.te_*` →（经 TMIS/stg 链路，非本脚本直连）→ `edw`/`visual_screen`/`report` |
| **建议 crontab** | `0 22 * * * /opt/app/guoku/dataInterface/shell/db2_to_mysql.sh >> /var/log/guoku/db2_to_mysql.log 2>&1` |

**逐表链路：**

| Shell 步骤 | 源表 (DB2) | 目标表 (ods) | 后续存储过程/Event |
|------------|------------|--------------|-------------------|
| EXPORT + COPY | `DB2TESS.te_agentbankback_detail_query` | `ods.te_agentbankback_detail_query` | 源 MySQL 中 `edw.p_trs_budget_new` 读 `adm.trs_stat_agentbankpay_detail`（与 te 表不同链路）；集中支付明细供 `adm`/报表间接使用 |
| EXPORT + COPY | `DB2TESS.te_agentbankpay_detail_query` | `ods.te_agentbankpay_detail_query` | 同上 |

---

#### 1.1.2 `mysql_to_mysql.sh`

| 项 | 内容 |
|----|------|
| **源** | MySQL/PostgreSQL `gkdas.tv_fin_income_detail` @ `11.8.170.84:5432`，用户 `fdsp` |
| **方式** | 按 `s_intredate like '$DATE%'` 导出（脚本内 `DATE` 当前硬编码 `202103`，生产需改回 `date -d "-3 day" +%Y%m`） |
| **中间文件** | `/opt/app/guoku/dataInterface/shell/data/tv_fin_income_detail_$DATE.txt` |
| **目标** | `ods.tv_fin_income_detail`（`\copy`，TAB 分隔） |
| **下游过程** | `visual_screen.P_VS_INDUSTRY_TAX`、`P_VS_PILLAR_INDUSTRIES`、`P_VS_TAX_SUBJECT` 等读 `ODS.TV_FIN_INCOME_DETAIL`；`edw.P_trs_kyd_industry` 系列 |
| **建议 crontab** | `0 22 * * * /opt/app/guoku/dataInterface/shell/mysql_to_mysql.sh >> /var/log/guoku/mysql_to_mysql.log 2>&1` |

**逐表链路：**

| Shell | 源 | ods 目标 | 存储过程 | 输出表 | Event |
|-------|-----|----------|----------|--------|-------|
| mysql 导出 + vsql copy | `gkdas.tv_fin_income_detail` | `ods.tv_fin_income_detail` | `visual_screen.P_VS_INDUSTRY_TAX` | `visual_screen.vs_industry_tax` | `visual_screen_p_task_vs` → `P_task_vscreen` |
| 同上 | 同上 | 同上 | `visual_screen.P_VS_PILLAR_INDUSTRIES` | `visual_screen.vs_pillar_industries` | 同上 |
| 同上 | 同上 | 同上 | `visual_screen.P_VS_TAX_SUBJECT` | `visual_screen.vs_tax_subject` | 同上 |

---

#### 1.1.3 `oracle_to_mysql.sh`

| 项 | 内容 |
|----|------|
| **源** | Oracle `rhdzhtk.*` @ `9.16.20.178:1521/qcdzkt`，用户 `ZHTK_CX` |
| **方式** | 全量：sqlplus spool → TRUNCATE → `\copy` |
| **中间目录** | `/home/guoku/data_oracle/*.txt` |
| **目标** | `ods.<表名>`（与 txt 文件名一致，无扩展名） |
| **建议 crontab** | `0 22 * * * /opt/app/guoku/dataInterface/shell/oracle_to_mysql.sh >> /var/log/guoku/oracle_to_mysql.log 2>&1` |

**逐表链路（16 张 Oracle 表 → ods）：**

| # | Oracle 源表 | ods 目标表 | 后续存储过程 | 输出/用途 | 调度 |
|---|-------------|------------|--------------|-----------|------|
| 1 | `PT_TK_SQRXX` | `ods.PT_TK_SQRXX` | — | 退库申请主数据 | — |
| 2 | `PT_TK_ZSJGHSXX` | `ods.PT_TK_ZSJGHSXX` | — | 退库征收机关核实 | — |
| 3 | `PT_TK_SPMXXX` | `ods.PT_TK_SPMXXX` | — | 退库税票明细 | — |
| 4 | `PT_TK_TSMX` | `ods.PT_TK_TSMX` | — | 退库明细 | — |
| 5 | `PT_GY_JYLS` | `ods.PT_GY_JYLS` | — | 交易流水 | — |
| 6 | `CS_GY_XTCS` | `ods.CS_GY_XTCS` | — | 系统参数 | — |
| 7 | `DM_GKZT_YSKM_FZBZ` | `ods.DM_GKZT_YSKM_FZBZ` | — | 国库主体预算科目辅助标志 | — |
| 8 | `DM_GY_GKZT` | `ods.DM_GY_GKZT` | — | 国库主体维度 | — |
| 9 | `DM_GY_HSZT` | `ods.DM_GY_HSZT` | — | 核算主体维度 | — |
| 10 | `DM_GY_SPZT` | `ods.DM_GY_SPZT` | — | 审批状态码表 | — |
| 11 | `DM_GY_THYY` | `ods.DM_GY_THYY` | — | 退回原因码表 | — |
| 12 | `PT_GY_XWRZ` | `ods.PT_GY_XWRZ` | — | 行为日志 | — |
| 13 | `DM_GY_YHHH` | `ods.DM_GY_YHHH` | — | 银行行号 | — |
| 14 | `PT_YHXX` | `ods.PT_YHXX` | — | 用户信息 | — |
| 15 | `QX_YHLX_GNS` | `ods.QX_YHLX_GNS` | — | 用户类型功能 | — |
| 16 | （`PT_GY_FILES` 由过程处理，见下） | `ods.pt_gy_files` | **`ods.p_pt_gy_files_temp()`** | 刷新 `ods.pt_gy_files_temp` → `ods.pt_gy_files` | **`ods_pt_gy_files_task`** Event，每天 01:00 |

> `ods.p_pt_gy_files_temp`：从 `ods.pt_gy_files` 解析 FDFS 路径写入 `ods.pt_gy_files_temp`，再回写 `ods.pt_gy_files`。依赖 Oracle 侧文件元数据已入 `ods.pt_gy_files`（若该表由其它链路同步，本 Shell 未直接导出）。

---

### 1.2 辅助 / 测试脚本（不建议上 crontab）

| 脚本 | 用途 | 源 → 目标 |
|------|------|-----------|
| **`oracle_to.sh`** | Oracle 单表测试（仅 `PT_TK_ZSJGHSXX`） | Oracle → `/home/guoku/data_oracle/` → `ods.*` |
| **`oracle_test.sh`** | 单表导出测试（`PT_TK_SQRXX`），不入库 | Oracle → txt |
| **`to_mysql_test.sh`** | 单表入库测试（`PT_GY_XWRZ`） | txt → `ods.PT_GY_XWRZ` |
| **`tv.sh`** | 按纳税人名单 mysqldump 子集到 `data/tv.sql`（硬编码企业列表） | MySQL `gkdas` → SQL 文件，**非** Vastbase 管道 |
| **`xunhuan.sh`** | 日期循环占位脚本（语法有误，不可用） | 无 |

---

## 2. Vastbase 交付脚本中的存储过程与 Event（`final/`）

执行顺序见 `final/README.md`：`001_etl` → `012_ucloud_upm`。

### 2.1 触发器

**无。** 源库导出与 Vastbase 交付脚本均未包含业务触发器。

### 2.2 Event（定时任务，默认 DISABLE）

文件：`final/007_events_init.sql`（源自 `mysql/source_events.sql`）

| Event 名 | 调度 | 调用过程 | 参数 | 影响层级 |
|----------|------|----------|------|----------|
| `indicators_lib_p_init_report01` | 每天 21:00 | `indicators_lib.init_report01` | 上月末日期 | `indicators_lib.lib_*` 指标表 |
| `indicators_lib_p_init_report02` | 每天 23:00 | `indicators_lib.init_report02` | 上月末日期 | `indicators_lib.lib_indicators_*` |
| `indicators_lib_p_init_report03` | 每天 02:00 | `indicators_lib.init_report03` | 上月末日期 | 指标 + `edw.trs_budget_income_compare` |
| `indicators_lib_p_xunhuan_formula` | 每天 18:00 | `indicators_lib.p_xunhuan_formula` | 当天 `YYYYMMDD` | 全链路编排（edw/report/indicators） |
| `ods_pt_gy_files_task` | 每天 01:00 | `ods.p_pt_gy_files_temp` | 无 | `ods.pt_gy_files` / `_temp` |
| `seo_p_task_vs` | 每天 02:00 | `visual_screen.p_task_vscreen` | **昨天** `YYYYMMDD` | 全部 `visual_screen.vs_*` |
| `visual_screen_p_task_vs` | 每天 18:15 | `visual_screen.p_task_vscreen` | **当天** `YYYYMMDD` | 全部 `visual_screen.vs_*` |

启用示例（现场确认依赖已跑通后）：

```sql
ALTER EVENT visual_screen_p_task_vs ENABLE;
SHOW EVENTS;
```

### 2.3 源 MySQL 额外 Event（**未**纳入 `final/007`）

文件：`mysql/source_events_edw_etl.sql`

| Event | 调度 | 调用 | 说明 |
|-------|------|------|------|
| `edw.EVT_TRS_CALL_EDW_BUDGET_DATA` | 每天 22:30:01 | `edw.p_trs_budget_new()` | 从 `adm.trs_stat_agentbankpay_detail` 刷新 `edw.cm_guoku_bdgorg` |
| `edw.EVT_TRS_CALL_EDW_CP` | 每天 17:30 | `edw.proc_trs_guoku_cp()` | 国库 CP 统计（过程体在源库，未迁入 final） |

---

## 3. 逐条加工链路（Shell → 过程 → 表 → Event）

### 3.1 链路 A：DB2 集中支付 → ods →（间接）报表/大屏

```
crontab 22:00
  db2_to_mysql.sh
    → ods.te_agentbankback_detail_query
    → ods.te_agentbankpay_detail_query
      → [缺口: stg 层 TMIS 数据由其它入口加载]
      → edw.P_TRS_BUDGET_INCOME_COMPARE (读 stg)
      → indicators_lib.p_xunhuan_formula (Event 18:00)
      → visual_screen.P_task_vscreen (Event 18:15)
      → report.P_MONTH_REPORT_TEXT 等
```

### 3.2 链路 B：凯盈达收入明细 → ods → 大屏行业税

```
crontab 22:00
  mysql_to_mysql.sh
    → ods.tv_fin_income_detail
      → visual_screen.P_VS_INDUSTRY_TAX      → visual_screen.vs_industry_tax
      → visual_screen.P_VS_PILLAR_INDUSTRIES → visual_screen.vs_pillar_industries
      → visual_screen.P_VS_TAX_SUBJECT       → visual_screen.vs_tax_subject
        ↑ 由 P_task_vscreen / P_task_vscreen_month_end 在月末日调用
        ↑ Event: visual_screen_p_task_vs / seo_p_task_vs
```

### 3.3 链路 C：Oracle 全程电子退库 → ods → 文件过程

```
crontab 22:00
  oracle_to_mysql.sh
    → ods.PT_* / ods.DM_* / ods.QX_* (16 表)
      → [业务页面直接查 ods 或通过应用 SQL]
      → ods.p_pt_gy_files_temp (Event ods_pt_gy_files_task 01:00)
           读 ods.pt_gy_files → 写 ods.pt_gy_files_temp → 回写 ods.pt_gy_files
```

### 3.4 链路 D：指标库全链路编排 `p_xunhuan_formula`

**入口：** Event `indicators_lib_p_xunhuan_formula`（18:00）或手工 `CALL indicators_lib.p_xunhuan_formula('YYYYMMDD')`

**过程内步骤（`final/006_indicators_lib_init.sql`）：**

| 步骤 | 动作 | 源表 | 目标/调用 |
|------|------|------|-----------|
| 1-3 | 规范化日期 | `stg.trs_tmis_budget_income/payout/stock` | UPDATE stg |
| 4 | 预算比较 | stg | `CALL edw.P_TRS_BUDGET_INCOME_COMPARE` → `edw.trs_budget_income_compare` |
| 4 | 同上 | stg | `CALL indicators_lib.P_TRS_BUDGET_INCOME_COMPARE_XIN` → `edw.trs_budget_income_compare` |
| 5 | 日循环 | stg 游标 | `CALL indicators_lib.p_exe_formula_hand(日期, 指标ID)` → `indicators_lib.lib_*` |
| 6 | 月初 | — | `CALL indicators_lib.p_exe_formula`；`report.P_QUARTER_REPORT_TEXT`；`report.P_NEWS_FLASH_QUARTER_REPORT_TEXT` |
| 7 | 月循环 | — | `report.P_MONTH_REPORT_TEXT` → `report.report_text`；`P_NEWS_FLASH_*` → 快报文本表 |

### 3.5 链路 E：大屏编排 `P_task_vscreen`

**入口：** Event `visual_screen_p_task_vs`（18:15，当天）或 `seo_p_task_vs`（02:00，昨天）

```
CALL visual_screen.P_task_vscreen(V_BATCH_DATE)
  ├─ 读 stg.trs_tmis_budget_income / payout / stock / provinces 取 d_acct 列表
  ├─ CALL P_task_vscreen_daily(V_DATA_DATE)   -- 每个会计日都跑
  │    ├─ p_vs_inventory_analyze   → visual_screen.vs_inventory_analyze   ← stg.trs_tmis_stock
  │    ├─ p_vs_inventory_area      → visual_screen.vs_inventory_area
  │    ├─ p_vs_inventory_balance   → visual_screen.vs_inventory_balance
  │    ├─ P_vs_inventory_form      → visual_screen.vs_inventory_form
  │    ├─ p_vs_revenu_display      → visual_screen.vs_revenu_display       ← stg.provinces
  │    ├─ p_vs_amount_ranking_by_region → visual_screen.vs_amount_ranking_by_region
  │    ├─ p_vs_regional_taxation   → visual_screen.vs_regional_taxation
  │    ├─ p_vs_municipalities_directly → visual_screen.vs_municipalities_directly
  │    └─ p_vs_five_provinces_in_southwest_china → visual_screen.vs_five_provinces_in_southwest_china
  └─ CALL P_task_vscreen_month_end(V_DATA_DATE)  -- 仅 V_DATA_DATE = 月末日
       ├─ p_vs_gemini_structure … p_vs_transfer_income（28 个子过程，见 §3.6）
       └─ 读 stg / edw.cm_guoku_dimnsn / ods.tv_fin_income_detail / indicators_lib.lib_indicators_*
```

**展示：** `vis-screen-backend` 读 `visual_screen.vs_*` → 前端 `/vis/gallery`、大屏预览 API。

### 3.6 大屏子过程逐条清单（48 个，final/005）

| 存储过程 | 主要源表 | 输出表 | 调度入口 |
|----------|----------|--------|----------|
| `P_VS_AMOUNT_RANKING_BY_REGION` | `stg.trs_tmis_budget_income_provinces` | `visual_screen.vs_amount_ranking_by_region` | `P_task_vscreen_daily` |
| `p_vs_area_income` | `visual_screen.vs_three_budget_revenue` | `visual_screen.vs_area_income` | `P_task_vscreen_month_end` |
| `p_vs_area_pay` | `visual_screen.vs_economic_pay` | `visual_screen.vs_area_pay` | month_end |
| `P_VS_BUDGET_REVENUE` | `stg.trs_tmis_budget_income`, `edw.cm_guoku_dimnsn` | `visual_screen.vs_budget_revenue` | month_end |
| `P_VS_CHONGQING_ECONOMIC_ZONE` | `vs_three_budget_revenue` | `visual_screen.vs_chongqing_economic_zone` | month_end |
| `P_VS_CUSTOMS_IMPORT_DUTIES` | stg + edw | `visual_screen.vs_customs_import_duties` | month_end |
| `P_VS_CUSTOMS_IMPORT_VAT` | stg + edw | `visual_screen.vs_customs_import_vat` | month_end |
| `p_vs_customs_income_situation` | stg + edw | `visual_screen.vs_customs_income_situation` | month_end |
| `P_VS_CUSTOMS_NON_TAX` | stg + edw | `visual_screen.vs_customs_non_tax` | month_end |
| `P_VS_CUSTOMS_REVENUE` | stg + edw | `visual_screen.vs_customs_revenue` | month_end |
| `p_vs_economic_pay` | `stg.trs_tmis_budget_payout` | `visual_screen.vs_economic_pay` | month_end |
| `P_VS_ECONOMIC_TAXATION` | `vs_budget_revenue` | `visual_screen.vs_economic_taxation` | month_end |
| `P_VS_FIVE_PROVINCES_IN_SOUTHWEST_CHINA` | `stg.trs_tmis_budget_income_provinces` | `visual_screen.vs_five_provinces_in_southwest_china` | daily |
| `P_VS_GEMINI_STRUCTURE` | stg + edw | `visual_screen.vs_gemini_structure` | month_end |
| `P_VS_GROWTH_OF_TAX_REVENUE` | `indicators_lib.lib_indicators_000052` | `visual_screen.vs_growth_of_tax_revenue` | month_end |
| `p_vs_growth_public_budget` | stg + edw | `visual_screen.vs_growth_public_budget` | month_end |
| `P_VS_IMPORT_DUTY_ON_IMPORTED_ARTICLES` | stg + edw | `visual_screen.vs_import_duty_on_imported_articles` | month_end |
| `P_VS_INCOME_EXPENDITURE` | stg income+payout + edw | `visual_screen.vs_income_expenditure` | month_end |
| `P_VS_INDUSTRY_TAX` | **`ods.tv_fin_income_detail`** + edw | `visual_screen.vs_industry_tax` | month_end |
| `p_vs_inventory_analyze` | `stg.trs_tmis_stock` + edw | `visual_screen.vs_inventory_analyze` | daily |
| `p_vs_inventory_area` | `stg.trs_tmis_stock` | `visual_screen.vs_inventory_area` | daily |
| `p_vs_inventory_balance` | `stg.trs_tmis_stock` | `visual_screen.vs_inventory_balance` | daily |
| `P_vs_inventory_form` | stg.stock + edw | `visual_screen.vs_inventory_form` | daily |
| `P_VS_LAND_TRANSFER` | stg + edw | `visual_screen.vs_land_transfer` | month_end |
| `P_VS_LOCAL_FINANCIAL_RESOURCES` | stg + edw | `visual_screen.vs_local_financial_resources` | month_end |
| `P_VS_MUNICIPALITIES_DIRECTLY` | stg.provinces | `visual_screen.vs_municipalities_directly` | daily |
| `P_VS_PILLAR_INDUSTRIES` | **`ods.tv_fin_income_detail`** + edw | `visual_screen.vs_pillar_industries` | month_end |
| `P_VS_PUBLIC_BUDGET` | stg + edw | `visual_screen.vs_public_budget` | month_end |
| `p_vs_purpose_pay` | stg.payout + edw | `visual_screen.vs_purpose_pay` | month_end |
| `P_VS_REGIONAL_TAXATION` | stg.provinces | `visual_screen.vs_regional_taxation` | daily |
| `p_vs_region_pay` | stg.payout + edw | `visual_screen.vs_region_pay` | month_end |
| `p_vs_region_pay_20241014` | 同上（备用版本） | `visual_screen.vs_region_pay` | 手工 |
| `P_VS_REVENUE_EXPENDITURE` | `indicators_lib.lib_indicators_000008` | `visual_screen.vs_revenue_expenditure` | month_end |
| `P_VS_REVENU_DISPLAY` | stg.provinces | `visual_screen.vs_revenu_display` | daily |
| `p_vs_subject_pay` | stg.payout + edw | `visual_screen.vs_subject_pay` | month_end |
| `p_vs_subject_pay_sub` | stg.payout + edw | `visual_screen.vs_subject_pay_sub` | month_end |
| `P_VS_TAX_REVENUE` | stg + edw | `visual_screen.vs_tax_revenue` | month_end |
| `P_VS_TAX_SUBJECT` | **`ods.tv_fin_income_detail`** + edw | `visual_screen.vs_tax_subject` | month_end |
| `P_VS_THREE_BUDGET_REVENUE` | stg + edw | `visual_screen.vs_three_budget_revenue` | month_end |
| `P_VS_TRANSFER_INCOME` | stg + edw | `visual_screen.vs_transfer_income` | month_end |
| `trs_kyd_enterprise` | `edw.trs_kyd_industry` | `visual_screen.trs_kyd_enterprise` | 手工/扩展编排 |
| `trs_kyd_enterprise_rank` | `edw.trs_kyd_industry` | `visual_screen.trs_kyd_enterprise_rank` | 同上 |
| `trs_kyd_industry` | `edw.trs_kyd_industry` | `visual_screen.trs_kyd_industry` | 同上 |
| `P_task_vscreen_daily` | — | —（编排器） | `P_task_vscreen` |
| `P_task_vscreen_month_end` | — | —（编排器） | `P_task_vscreen` |
| `P_task_vscreen` | stg 四表 | —（编排器） | **Event** |
| `P_task_vscreen1` | 同 P_task_vscreen | — | 备用 |
| `P_task_vscreen_new` | stg.stock | — | 备用 |

### 3.7 链路 F：dwbi-system-docking（UCloud / UPM 接口）

**非 Shell**；应用内 Redis 调度 + HTTP 拉数 + 存储过程汇总。

| 阶段 | 组件 | 说明 |
|------|------|------|
| 调度 | `ProcessConfig` + Redis `CRON_<processId>` | 配置表 `system_docking.api_task_ins` / `api_task_thread_ins` |
| 取数 | `ApiTaskUtil` | HTTP → 写入 `ucloud.api_interface_*` / `upm.*` 动态分表 |
| 汇总 | `ConfigMapper.xml` CALL | 见下表 |
| 展示 | `dwbi-statistical-analysis` | 告警/性能/资源排行页面 |

| 存储过程 | 入参 | 输出表 |
|----------|------|--------|
| `ucloud.ucloud_api_interface_alarm_data` | 动态表名, 日期 | `ucloud.api_alarm_summary` |
| `ucloud.ucloud_api_interface_system_data` | 动态表名, 日期 | `ucloud.api_system_summary`（过程内动态 SQL） |
| `upm.upm_proc_api_alarm_summary_alarmlog` | 动态表名 | UPM 告警汇总 |
| `upm.upm_proc_api_alarm_summary_netper` | 动态表名 | 网络性能汇总 |
| `upm.upm_proc_api_alarm_summary_interface` | 动态表名 | 接口汇总 |

脚本位置：`final/012_ucloud_upm_procedures_init.sql`；表结构：`008`–`011`。

### 3.8 ods 层其它过程（final/002）

| 过程 | 源 | 目标 | 触发 |
|------|-----|------|------|
| `ods.p_pt_gy_files_temp` | `ods.pt_gy_files` | `ods.pt_gy_files_temp` → `ods.pt_gy_files` | Event `ods_pt_gy_files_task` |
| `ods.p_trs_info_enterprises_temporary` | `ods.trs_info_enterprises_temporary` | `ods.trs_info_enterprises` | 手工（企业信息去重入库） |
| `ods.p_trs_info_nonbusiness_temporary` | `_temporary` | `ods.trs_info_nonbusiness` | 手工 |
| `ods.p_trs_info_practice_cancel_temporary` | `_temporary` | `ods.trs_info_practice_cancel` | 手工 |

### 3.9 report / edw / indicators 交付过程（final/003–006）

| 过程 | 输出 | 触发 |
|------|------|------|
| `report.P_MONTH_REPORT_TEXT` | `report.report_text` | `p_xunhuan_formula` / init_report Event |
| `report.P_NEWS_FLASH_MONTH_REPORT_TEXT` | `report.news_flash_month_text` | 同上 |
| `report.P_NEWS_FLASH_MONTH_TEXT_NUMBER` | `report.news_flash_month_text_number` | 同上 |
| `report.P_QUARTER_REPORT_TEXT` | `report.quarter_report_text` | 同上（月初） |
| `report.P_NEWS_FLASH_QUARTER_REPORT_TEXT` | `report.news_flash_quarter_report_text` | 同上 |
| `edw.P_TRS_BUDGET_INCOME_COMPARE` | `edw.trs_budget_income_compare` | `p_xunhuan_formula` |
| `indicators_lib.init_report01/02/03` | `indicators_lib.lib_*` | Event 21:00/23:00/02:00 |
| `indicators_lib.p_exe_formula_hand` | 动态（读 `lib_index_formula`） | 被 init / xunhuan 调用 |
| `indicators_lib.p_exe_formula` | 同上 | 被 init_report01、p_xunhuan 调用 |
| `indicators_lib.P_TRS_BUDGET_INCOME_COMPARE_XIN` | `edw.trs_budget_income_compare` | init_report03、p_xunhuan |
| `indicators_lib.p_xunhuan_formula` | 多表（见 §3.4） | Event 18:00 |

---

## 4. 源 MySQL 全量过程清单（未全部迁入 Vastbase final）

导出文件：

- `document/psql/mysql/source_routines.sql` — schema: ods, report, edw(部分), visual_screen, indicators_lib, seo 等
- `document/psql/mysql/source_routines_edw_etl.sql` — schema: edw, etl

### 4.1 按 schema 统计

| Schema | 过程数（源 MySQL） | 已迁入 final | 备注 |
|--------|-------------------|--------------|------|
| `ods` | 14 | 4 | 含宏观数据 `P_FM_SUST_*` 等未迁入 |
| `edw` | 45 | 1 | 含 `P_TRS_FINANCE_TAX_STATIS`、`p_transDataFromOdsToEdw_main` 等 |
| `report` | 10 | 5 | 缺 `p_trs_income_payout_statistics` 等 |
| `indicators_lib` | 19 | 7 | 含多个 `p_xunhuan_formula_*` 临时版 |
| `visual_screen` | 51 | 48 | 基本覆盖 |
| `seo` | 34 | 0 | SEO 模块过程与 visual_screen 大量重名，由 `seo_p_task_vs` Event 转调 visual_screen |
| `ucloud/upm` | — | 7 | 由结构 SQL 转换，非 source_routines 导出 |

完整过程名列表见源文件 `DROP PROCEDURE IF EXISTS \`schema\`.\`name\`` 行。

---

## 5. 电子登记簿（dwbi-er-cq）

### 5.1 Shell 脚本

**无数据加工类 Shell。** 仓库仅有：

| 脚本 | 用途 |
|------|------|
| `scripts/service.sh` / `deploy-package/service.sh` | 启停 portal/register |
| `scripts/run-with-jdk.sh` | JDK 路径 |
| `scripts/sync-bes-license.sh` | BES 许可证同步 |
| `migration_bundle/run_*.sh` | **数据库迁移审计**（Python 3.7），非日常 ETL |
| `document/sql/2026070201-*.sql` | 机构名称同步 SQL（一次性维护） |

### 5.2 存储过程（运行时 CALL，DDL 不在仓库）

| 过程 | 调用位置 | 业务 | 触发方式 |
|------|----------|------|----------|
| `adm.pro_dw_aml_clue(clueId, OUT, OUT)` | `RegisterMapper.xml` / `ClueMapper.xml` | 线索多维自主分析 | **页面操作**触发 |
| `adm.pro_ana_clue_fz_cq(clueId, OUT, OUT)` | 同上 | 线索固定指标分析（重庆） | 页面操作 |
| `er_register.pro_ana_info_report(clueId, OUT, OUT)` | 同上 | 线索类型统计分析 | 页面操作 |
| `dmcode.p_org_code_all(...)` | `OrgServiceImpl.java` | 机构编码查询 | 页面/API |

> 以上过程定义需在 **ER 生产库**（schema `adm` / `er_register` / `dmcode`）查 `information_schema.routines`；本 Git 仓库**未包含** CREATE PROCEDURE 脚本。

### 5.3 定时任务（Java @Scheduled，非数据库存储过程）

| 类 | 调度 | 作用 |
|----|------|------|
| `SyncSchedulerService` | 增量：每 5 分钟；全量：cron `0 0 2 * * ?` | 从 GK-Nexus 同步用户/组织镜像到 ER 本地表 |
| `ErBizAuditRetryTask` | fixedDelay 5 分钟 | 业务审计失败重试 |
| `DataBakMysqlTask` | `*/5 * * * * ?` | **测试占位**（仅打日志） |

配置：`dwbi-portal/.../application.yml` → `sync.scheduler.*`

### 5.4 ER 数据流小结

```
GK-Nexus API (OAuth sync)
  → SyncMirrorService (Java 定时)
  → ER 本地镜像表 (portal sql 初始化)
  → 登记/线索业务页面
  → CALL adm.* / er_register.* (数据库存储过程，DDL 在生产库)
```

**与数据分析平台无 Shell/过程交叉**；共用 GK-Nexus 身份，数据库实例独立。

---

## 6. 建议执行顺序（首次落地）

1. **Vastbase 初始化：** `final/000_run_all.sql`（或 001→012 顺序）
2. **Shell 贴源（crontab 22:00）：** `db2_to_mysql.sh` → `mysql_to_mysql.sh` → `oracle_to_mysql.sh`
3. **确认 stg 层有数据**（若无，需向原运维索取 TMIS 加载脚本或接口配置）
4. **手工验证 CALL：** `ods.p_pt_gy_files_temp` → `indicators_lib.p_xunhuan_formula` → `visual_screen.P_task_vscreen`
5. **启动 dwbi-system-docking**（Redis + 接口配置）验证 ucloud/upm 链
6. **启用 Event：** 按 §2.2 逐个 `ALTER EVENT ... ENABLE`
7. **页面验证：** 固定报表、指标库、大屏 `/vis/gallery`

---

## 7. 相关文件索引

| 路径 | 内容 |
|------|------|
| `document/dataInterface/shell/*.sh` | 全部 Shell 源脚本 |
| `document/psql/vastbase/final/` | Vastbase 可执行交付脚本 |
| `document/psql/mysql/source_routines*.sql` | MySQL 源过程全量导出 |
| `document/psql/mysql/source_events*.sql` | MySQL 源 Event 导出 |
| `document/psql/vastbase/external_data_source_operation_test_manual.md` | 现场测试手册 |
| `document/psql/vastbase/040_missing_routines_inventory.md` | 缺失依赖说明 |
| `document/psql/vastbase/090_dwbi_system_docking_ucloud_upm_inventory.md` | 接口调度清单 |
| `document/psql/README.md` | 迁移目录总览 |

---

*文档生成依据仓库快照；若生产环境存在未入库脚本（如 `_1030` 变体、stg 加载脚本），需向原系统运维补充后追加到 §1。*
