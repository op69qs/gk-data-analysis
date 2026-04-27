# 智能报告列表后端别名修复

## 背景

- 智能报告“报告管理”页面调用 `/fixedReport/newsFlash/getReportAll` 可以返回总数与列表数据，但列表内容未显示。
- 前端已有既定字段约定，按 `REPORT_NAME`、`REPORT_TYPE_ID`、`MODIFY_DATE` 等大写键读取列表行和后续详情参数。

## 根因

- `fixedReport` 的 `NewsFlashMapper.xml` 在列表查询中使用了 `SELECT *`。
- 在 Vastbase/PostgreSQL 风格驱动下，未加双引号的大写列名最终落到 `Map` 中会变成小写键，导致前端按既有大写字段取值失败。

## 实现

- 在 `fixedReport/src/main/resources/mybatis/fixedReport/NewsFlashMapper.xml` 的 `getReportAll` 查询中，去掉 `SELECT *`。
- 将列表页和详情弹窗依赖的字段改成显式列清单，并统一写成 `AS "REPORT_NAME"` 这类双引号大写别名。
- 撤回前端临时兼容逻辑，保持前端按既有大写字段消费后端返回值。

## 验证

- 使用 VS Code 诊断检查修改后的 Mapper 与页面文件，确认没有新增编辑器级错误。
- 当前环境中的 Node 为 14.21.3，符合该仓库“Node 14 编译”的约束；此前 lint 失败是项目缺少 ESLint 配置，不是 Node 版本问题。
- 后续可在运行中的页面刷新“智能报告 -> 报告管理”，确认列表内容、查看和删除参数恢复正常。