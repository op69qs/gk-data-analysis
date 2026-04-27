# 月度快报详情接口 Vastbase 字段别名修复

## 背景

- 智能报告列表点击“查看”后，会调用 `/fixedReport/newsFlash/getMonthlyReport`。
- 后端日志显示 `ReportMapper.getMonthlyReport` 能查到 `report.t_month_report_fact` 数据，但 `NewsFlashController.getMonthlyReport` 在读取返回结果时触发空指针。

## 根因

- `fixedReport/src/main/resources/mybatis/fixedReport/ReportMapper.xml` 中的 `getMonthlyReport` 使用了 `SELECT *`。
- 在 Vastbase/PostgreSQL 驱动下，结果映射到 `Map` 时，未显式加双引号的列名会被折成小写键。
- `NewsFlashController` 继续按 `textList`、`tableParams2` 等既有大小写键调用 `allDataList.get(...).toString()`，取到 `null` 后触发 `NullPointerException`。

## 实现

- 将 `ReportMapper.xml` 中 `getMonthlyReport` 的 `SELECT *` 改成显式列清单。
- 对控制器使用到的字段统一增加双引号别名，例如 `c.textList AS "textList"`、`c.tableParams2 AS "tableParams2"`。
- 保持控制器现有读取逻辑不变，通过 SQL 返回稳定键名消除 Vastbase 下的键名折叠问题。

## 验证

- 使用 VS Code 诊断检查 `ReportMapper.xml`，确认无新增 XML 错误。
- 建议在 `fixedReport` 模块执行 Maven 编译，并重新请求 `/fixedReport/newsFlash/getMonthlyReport` 验证接口返回不再报空指针。