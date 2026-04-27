# 2026042627 排查 fixedReport 分行业报表缺失数据源

## 背景

接口：`/fixedReport/kydReportController/getIndustry`

报错：

- `ERROR: schema "adm" does not exist`
- 出错 SQL 位于 `fixedReport/mybatis/fixedReport/KydReportMapper.xml`

## 排查结论

这次问题不是单纯的 SQL 语法或字段别名问题，而是当前 Vastbase 库中缺失该报表依赖的数据对象，且现有同名/近名表也无法直接替代。

### 1. 源码中的实际依赖

- `KydReportController` 把 `tableName` 写死为 `adm.trs_kyd_industry`
- `KydReportMapper` 还直接引用了：
  - `adm.trs_kyd_industry`
  - `adm.trs_kyd_industry_mb`

### 2. 数据库实际情况

对当前库 `gk_data_analysis` 检查后：

- `adm` schema 不存在
- `trs_kyd_industry_mb` 对象不存在
- 库中存在的近名对象：
  - `edw.trs_kyd_industry`
  - `edw.trs_kyd_industry_YYYYMM_guoku`
  - `visual_screen.trs_kyd_industry`

但这些对象都不能直接支撑当前报表 SQL：

- `edw.trs_kyd_industry*` 仅有明细型字段，如 `d_acct`、`s_trecode`、`lev_1_id`、`f_amt`
- 不存在当前报表 SQL 依赖的宽表字段：
  - `PRONAME`
  - `F_AMT_101`
  - `F_AMT_1010101`
  - `mark`
- `visual_screen.trs_kyd_industry` 字段结构也完全不同

### 3. 现有替代链路也不可用

仓库中另一个同类实现 `ReportTndustryMapper.xml` 依赖 `adm.trs_mth_income_industry`，但当前库中该对象也不存在。

### 4. 数据现状

检查 `edw.trs_kyd_industry` 及多个月份分表的行数估计，当前均为 0。

## 结论

`/fixedReport/kydReportController/getIndustry` 当前失败的根因是：

1. 代码仍依赖旧的 `adm` schema 宽表对象
2. 当前 Vastbase 中未迁移这些对象
3. 现有候选表既无对应字段，也基本无数据，无法在不明确业务映射规则的前提下安全重写

## 后续需要的外部条件

要真正修复该接口，至少需要以下任一条件：

1. 提供旧对象定义或迁移脚本：
   - `adm.trs_kyd_industry`
   - `adm.trs_kyd_industry_mb`
   - 如有：`adm.trs_mth_income_industry`
2. 提供新的数据来源与字段映射规则，允许基于现库对象重写该报表逻辑

在缺少上述信息前，不建议机械替换 schema 或表名，否则会从 `schema 不存在` 变成 `table/column 不存在`，无法恢复功能。
