-- 问题 5：恢复旧“指标库 / 指标查询”菜单的生产查询页。
-- 本脚本只更新指定旧菜单；不会修改 /vis/index-library 及其权限记录。

BEGIN;

UPDATE "jeecg-boot-os".sys_permission
SET url = '/statistics/indexLibrary',
    component = 'statistics/indexLibrary',
    component_name = NULL,
    redirect = NULL,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = 'cae8031ed1a7aeaed5625928a5ed74da'
  AND parent_id = 'aea6b487925d084dad182e09c95a6c79'
  AND name = '指标查询';

-- 回读旧指标查询菜单及独立 vis 菜单，确认两条链路没有被合并。
SELECT id, parent_id, name, url, component, component_name, redirect
FROM "jeecg-boot-os".sys_permission
WHERE id = 'cae8031ed1a7aeaed5625928a5ed74da'
   OR url = '/vis/index-library'
ORDER BY id;

COMMIT;
