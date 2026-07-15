# 分地区预算收入与大屏键盘可靠性修复设计

## 目标

修复“总行调研02”中“一般公共预算收入分地区”无数据的问题，并让大屏左右键在按下时立即、稳定地切换页面。

## 数据查询

`getBudgetRevenueTitle` 和 `getBudgetRevenueRateTitle` 已通过 `GROUP BY AREA_DSCR, AREA_CODE` 保证分组唯一，不需要额外使用 `DISTINCT`。Vastbase/PostgreSQL 要求 `SELECT DISTINCT` 查询的 `ORDER BY` 表达式必须出现在选择列中，当前 `ORDER BY AREA_CODE` 因此报错。

修复方式是在这两个查询中删除多余的 `DISTINCT`，保留：

- `AREA_DSCR`、`INDEX_VALUE` 的大写引用别名，继续满足后台 Controller 的字段契约；
- `GROUP BY AREA_DSCR, AREA_CODE`，保持原有分组结果；
- `ORDER BY AREA_CODE`，保持重庆市、两江新区、高新区的稳定顺序。

Mapper 契约测试将同时约束字段大小写和上述 Vastbase 语法要求。

## 键盘导航

新增一个无 DOM 依赖的键盘事件协调器，由预览页注册 `keydown`、`keyup` 和 `blur`：

- `keydown`：在按键按下时立即触发左、右或 ESC 动作；忽略 `event.repeat`，避免长按快速翻过多页。
- `keyup`：如果同一按键已经由 `keydown` 处理，只负责清除状态；如果运行终端只产生 `keyup`，则作为兜底触发一次动作。
- `blur`：清空按键状态，避免窗口失焦导致按键被永久视为按下。
- 页面销毁时移除全部监听器，避免重复进入预览页后产生多个处理器。

协调器只决定一次事件序列是否应触发动作，实际轮播仍调用 Element UI Carousel 的 `prev()` / `next()`。Element UI 会在手动切换时自动重置轮播定时器。

## 回归验证

1. Mapper 测试先证明两个标题查询不能保留 `SELECT DISTINCT`，再验证修复通过。
2. 键盘单元测试覆盖：正常 keydown/keyup 只切一次、keydown 立即切换、长按 repeat 不重复切换、keyup-only 终端兜底、失焦后可以再次响应。
3. 运行后端 Mapper 契约测试、前端测试和前端生产构建。
4. 部署后在“总行调研02”验证接口返回 `data`、`titleArea`，并连续使用左右键切换。

## 范围

只修改预算收入地图的两个标题 SQL、对应 Mapper 测试、键盘协调器、预览页接入和前端测试。不调整其他大屏查询、轮播间隔或页面样式。
