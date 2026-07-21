-- 问题 5 回退：将旧“指标查询”菜单恢复为修复前的公共方案列表入口。
-- 本脚本只回退指定旧菜单；不会修改 /vis/index-library 及其权限记录。

BEGIN;

UPDATE "jeecg-boot-os".sys_permission
SET url = '/statistics/schemeIndex',
    component = 'statistics/schemeIndex',
    component_name = NULL,
    redirect = NULL,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = 'cae8031ed1a7aeaed5625928a5ed74da'
  AND parent_id = 'aea6b487925d084dad182e09c95a6c79'
  AND name = '指标查询';

SELECT id, parent_id, name, url, component, component_name, redirect
FROM "jeecg-boot-os".sys_permission
WHERE id = 'cae8031ed1a7aeaed5625928a5ed74da';

COMMIT;
