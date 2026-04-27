# 2026042628 修复企业排名 Druid CAST 兼容并排查分企业源表

## 背景

接口：

- `/fixedReport/kydReportController/getEnterprise`
- `/fixedReport/kydReportController/getEnterpriseRanking`

现象分为两类：

1. `getEnterprise` 报 `schema "adm" does not exist`
2. `getEnterpriseRanking` 除了同样依赖 `adm` 之外，还被 Druid 拦截：
   - `ROW_NUMBER() OVER (...)::text` 触发 parser 报错

## 本次修改

文件：`fixedReport/src/main/resources/mybatis/fixedReport/KydReportMapper.xml`

将：

```sql
ROW_NUMBER() OVER (ORDER BY a.F_AMT_year DESC)::text AS rank
```

改为：

```sql
CAST(ROW_NUMBER() OVER (ORDER BY a.F_AMT_year DESC) AS TEXT) AS rank
```

目的：避免老版本 Druid 对 PostgreSQL `::text` 简写 cast 的语法误判。

## 数据源排查结论

### 1. 代码当前仍依赖旧对象

`KydReportController` 中写死了：

- `adm.trs_kyd_enterprise`
- `adm.trs_kyd_enterprise_rank`

### 2. 当前 Vastbase 实际对象

库里存在：

- `visual_screen.trs_kyd_enterprise`
- `visual_screen.trs_kyd_enterprise_rank`

但字段结构为：

- `dacct`
- `guoku_id`
- `guoku_dscr`
- `area_code`
- `area_dscr`
- `period_flag`
- `index_code`
- `index_name`
- `index_value`
- `index_value_year`（仅 enterprise）

### 3. 不能直接替换 schema

当前 `KydReportMapper.xml` 依赖的字段包括：

- `PRONAME`
- `PROCODE`
- `S_TRECODE`
- `LEV_1_ID`
- `LEV_2_ID`
- `LEV_4_DSCR`
- `F_AMT_101` 到 `F_AMT_99999`
- `mark`
- `F_AMT`
- `F_AMT_year`

这些字段在 `visual_screen.trs_kyd_enterprise*` 中都不存在，因此不能把 `adm` 机械替换成 `visual_screen`。

## 验证

- `KydReportMapper.xml` 语法检查：无错误
- 模块编译：`fixedReport` 目录执行 `mvn -DskipTests compile`
- 结果：`BUILD SUCCESS`

## 结论

本次已修复 `getEnterpriseRanking` 中一个确定存在的 Druid 解析兼容问题。

但 `getEnterprise` / `getEnterpriseRanking` 的主路径仍被旧宽表数据源缺失阻塞：

- 代码要的旧对象在 `adm` schema
- 当前库只有 `visual_screen` 下的同名但不同结构表
- 在缺少旧对象 DDL 或字段映射规则前，不能安全重写为现有表
