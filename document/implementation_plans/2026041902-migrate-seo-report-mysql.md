# 2026041902 从 cui01 迁移 seo/report 到 cui02 MySQL

## 目标

将源服务器 `cui01` 上 MySQL `localhost:3306` 的 `seo`、`report` 两个库，迁移到目标服务器 `cui02:3308` 的同名库中。

## 实施过程

### 1. 连通性确认

- 初始阶段，`cui01` 不能解析 `cui02`，曾临时使用 SSH 反向隧道验证目标 MySQL 可达。
- 用户将源服务器加入 Tailscale 后，`cui01` 已可直接解析并访问 `cui02`。
- 最终迁移采用直连方式：
  - 源库：`localhost:3306`
  - 目标库：`cui02:3308`

### 2. 源库与目标库状态确认

- 源端对象数：
  - `seo`: 21 张表
  - `report`: 22 张表
- 目标端最终对象数：
  - `seo`: 21 张表
  - `report`: 22 张表
  - `report` 视图数：0

### 3. seo 库迁移

- 已在目标实例创建同名库 `seo`。
- `seo` 已完成导入。
- 最终对象数核对：源端 21，目标端 21。

### 4. report 库迁移

- 曾尝试整库流式导入，但源端 `mysqldump` 在大表导出过程中多次断开：
  - `lib_indicators_000144`
  - `lib_indicators_000437`
  - `t_sys_report_guoku`
- 最终采用以下策略重跑 `report`：
  - 先重建目标库 `report`
  - 先导入结构 `--no-data`
  - 再导入数据
  - 对带 `ACCOUNT_PERIOD` 字段的表按期间分块导入
  - 对其他表按整表导入
- 过程中有一版脚本在 `v_kpi_id` 上因 `CHUNK_COUNT=0` 触发 `periods[@]: unbound variable`，但该问题发生在绝大多数表已导入完成之后。
- 后续已单独核对 `v_kpi_id`，源端与目标端均存在，且均为 `BASE TABLE`，行数均为 0。

## 最终校验

### seo

- 源端表数：21
- 目标端表数：21

### report

- 源端表数：22
- 目标端表数：22
- `v_kpi_id`：
  - 源端：`BASE TABLE`，0 行
  - 目标端：`BASE TABLE`，0 行

### report 关键表精确行数核对

以下精确 `COUNT(*)` 结果源端与目标端一致：

- `area_sort`: 43
- `indicators_lib_create`: 144
- `indicators_lib_create_all`: 240
- `inspection_report`: 0
- `lib_indicators_000144`: 37503
- `lib_indicators_000437`: 28195
- `lib_indicators_000438`: 28135
- `news_flash_month_text`: 100
- `news_flash_month_text_number`: 596
- `news_flash_quarter_report_text`: 35
- `quarter_report_text_20210303`: 21
- `qut_area2`: 16
- `qut_area3`: 15
- `qut_talbe`: 43
- `report_options`: 5
- `report_table`: 0
- `report_text`: 100
- `t_month_report_fact`: 120
- `t_sys_report_guoku`: 122
- `trs_income_payout_statistics`: 99906
- `trs_income_payout_statistics_index`: 99906
- `v_kpi_id`: 0

## 备份与回滚信息

- 迁移过程中已生成一份目标端导入前备份目录：
  - `/tmp/mysql-migrate-seo-report-20260419-114255`
- 其中包含早期迁移阶段生成的目标库备份文件。
- 直连重跑 `report` 时也生成了新的临时工作目录，但当前会话未稳定回显目录名；如需进一步清理，可在 `cui01` 上按 `/tmp/report-migrate-direct-*` 搜索。

## 结论

本次迁移已经完成，目标实例 `cui02:3308` 中已存在并完成同步：

- `seo`
- `report`

最终以对象数和关键表精确行数完成校验。
