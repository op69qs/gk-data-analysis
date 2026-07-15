-- vis 菜单与角色生产收口脚本。
-- 目标：
-- 1. 删除“预览概览”菜单，但保留 /vis/preview 预览能力。
-- 2. 将“可视化大屏”根菜单固定到最后，并把默认跳转改到 /vis/gallery。
-- 3. 修正页面编辑承接菜单到前端真实组件 vis/PageEditorEntry。
-- 4. 保留生产现有 vis 角色授权，并补一条独立角色 vis_screen_manager。
-- 5. 不处理 /statisticss 下那条 /vis/index-library 兼容菜单，它不属于当前 vis 根菜单子树。
-- 说明：
-- - 新增记录统一使用 md5 形式的哈希 ID，和并入后库中大量 32 位 ID 风格保持一致。
-- - 现网已经存在的 vis_* 语义 ID 不在这里直接改主键，避免生产联动风险；脚本通过 url/component_name 做兼容更新。

SET search_path TO "jeecg-boot-os", public;

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  md5('sys_permission:/vis'), '', '可视化大屏', '/vis', 'layouts/RouteView', 'VisRoot', '/vis/gallery',
  0, NULL, '1', 999.00, 0, 'dashboard', 1,
  0, 0, 0, 'vis_screen 并入 GK 后的页面级菜单根节点', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE url = '/vis' AND component_name = 'VisRoot'
);

UPDATE sys_permission
SET parent_id = '',
    name = '可视化大屏',
    url = '/vis',
    component = 'layouts/RouteView',
    component_name = 'VisRoot',
    redirect = '/vis/gallery',
    menu_type = 0,
    sort_no = 999.00,
    always_show = 0,
    icon = 'dashboard',
    is_route = 1,
    is_leaf = 0,
    keep_alive = 0,
    hidden = 0,
    description = 'vis_screen 并入 GK 后的页面级菜单根节点',
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP,
    del_flag = 0,
    rule_flag = 0,
    status = '1'
WHERE url = '/vis' AND component_name = 'VisRoot';

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  md5('sys_permission:/vis/gallery'), COALESCE((SELECT id FROM sys_permission WHERE url = '/vis' AND component_name = 'VisRoot' LIMIT 1), md5('sys_permission:/vis')), '图库', '/vis/gallery', 'vis/GalleryList', 'VisGalleryList', NULL,
  1, NULL, '1', 2.00, 0, NULL, 1,
  1, 0, 0, 'vis_screen 图库页', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE url = '/vis/gallery' AND component_name = 'VisGalleryList'
);

UPDATE sys_permission
SET parent_id = (SELECT id FROM sys_permission WHERE url = '/vis' AND component_name = 'VisRoot' LIMIT 1),
    name = '图库',
    url = '/vis/gallery',
    component = 'vis/GalleryList',
    component_name = 'VisGalleryList',
    redirect = NULL,
    menu_type = 1,
    sort_no = 2.00,
    always_show = 0,
    icon = NULL,
    is_route = 1,
    is_leaf = 1,
    keep_alive = 0,
    hidden = 0,
    description = 'vis_screen 图库页',
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP,
    del_flag = 0,
    rule_flag = 0,
    status = '1'
WHERE url = '/vis/gallery' AND component_name = 'VisGalleryList';

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  md5('sys_permission:/vis/bigscreen'), COALESCE((SELECT id FROM sys_permission WHERE url = '/vis' AND component_name = 'VisRoot' LIMIT 1), md5('sys_permission:/vis')), '大屏设置', '/vis/bigscreen', 'layouts/RouteView', 'VisBigScreenMenu', '/vis/bigscreen/templates',
  0, NULL, '1', 3.00, 0, 'tool', 1,
  0, 0, 0, 'vis_screen 原大屏设置菜单分组', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE url = '/vis/bigscreen' AND component_name = 'VisBigScreenMenu'
);

UPDATE sys_permission
SET parent_id = (SELECT id FROM sys_permission WHERE url = '/vis' AND component_name = 'VisRoot' LIMIT 1),
    name = '大屏设置',
    url = '/vis/bigscreen',
    component = 'layouts/RouteView',
    component_name = 'VisBigScreenMenu',
    redirect = '/vis/bigscreen/templates',
    menu_type = 0,
    sort_no = 3.00,
    always_show = 0,
    icon = 'tool',
    is_route = 1,
    is_leaf = 0,
    keep_alive = 0,
    hidden = 0,
    description = 'vis_screen 原大屏设置菜单分组',
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP,
    del_flag = 0,
    rule_flag = 0,
    status = '1'
WHERE url = '/vis/bigscreen' AND component_name = 'VisBigScreenMenu';

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  md5('sys_permission:/vis/index-library:vis-root'), COALESCE((SELECT id FROM sys_permission WHERE url = '/vis' AND component_name = 'VisRoot' LIMIT 1), md5('sys_permission:/vis')), '指标库方案', '/vis/index-library', 'vis/IndexLibraryList', 'VisIndexLibrary', NULL,
  1, NULL, '1', 4.00, 0, NULL, 1,
  1, 0, 0, 'vis_screen 原指标库方案入口，指标方案', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE url = '/vis/index-library' AND component_name = 'VisIndexLibrary'
);

UPDATE sys_permission
SET parent_id = (SELECT id FROM sys_permission WHERE url = '/vis' AND component_name = 'VisRoot' LIMIT 1),
    name = '指标库方案',
    url = '/vis/index-library',
    component = 'vis/IndexLibraryList',
    component_name = 'VisIndexLibrary',
    redirect = NULL,
    menu_type = 1,
    sort_no = 4.00,
    always_show = 0,
    icon = NULL,
    is_route = 1,
    is_leaf = 1,
    keep_alive = 0,
    hidden = 0,
    description = 'vis_screen 指标库方案',
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP,
    del_flag = 0,
    rule_flag = 0,
    status = '1'
WHERE url = '/vis/index-library' AND component_name = 'VisIndexLibrary';

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  md5('sys_permission:/vis/system/dict'), COALESCE((SELECT id FROM sys_permission WHERE url = '/vis' AND component_name = 'VisRoot' LIMIT 1), md5('sys_permission:/vis')), '数据字典', '/vis/system/dict', 'system/DictList', 'VisDictList', NULL,
  1, NULL, '1', 5.00, 0, NULL, 1,
  1, 0, 0, 'vis_screen 数据字典入口', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE url = '/vis/system/dict' AND component_name = 'VisDictList'
);

UPDATE sys_permission
SET parent_id = (SELECT id FROM sys_permission WHERE url = '/vis' AND component_name = 'VisRoot' LIMIT 1),
    name = '数据字典',
    url = '/vis/system/dict',
    component = 'system/DictList',
    component_name = 'VisDictList',
    redirect = NULL,
    menu_type = 1,
    sort_no = 5.00,
    always_show = 0,
    icon = NULL,
    is_route = 1,
    is_leaf = 1,
    keep_alive = 0,
    hidden = 0,
    description = 'vis_screen 数据字典入口',
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP,
    del_flag = 0,
    rule_flag = 0,
    status = '1'
WHERE url = '/vis/system/dict' AND component_name = 'VisDictList';

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  md5('sys_permission:/vis/system/business-type'), COALESCE((SELECT id FROM sys_permission WHERE url = '/vis' AND component_name = 'VisRoot' LIMIT 1), md5('sys_permission:/vis')), '业务类型', '/vis/system/business-type', 'system/BusinessTypeList', 'VisBusinessTypeList', NULL,
  1, NULL, '1', 6.00, 0, NULL, 1,
  1, 0, 0, 'vis_screen 业务类型入口', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE url = '/vis/system/business-type' AND component_name = 'VisBusinessTypeList'
);

UPDATE sys_permission
SET parent_id = (SELECT id FROM sys_permission WHERE url = '/vis' AND component_name = 'VisRoot' LIMIT 1),
    name = '业务类型',
    url = '/vis/system/business-type',
    component = 'system/BusinessTypeList',
    component_name = 'VisBusinessTypeList',
    redirect = NULL,
    menu_type = 1,
    sort_no = 6.00,
    always_show = 0,
    icon = NULL,
    is_route = 1,
    is_leaf = 1,
    keep_alive = 0,
    hidden = 0,
    description = 'vis_screen 业务类型入口',
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP,
    del_flag = 0,
    rule_flag = 0,
    status = '1'
WHERE url = '/vis/system/business-type' AND component_name = 'VisBusinessTypeList';

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  md5('sys_permission:/vis/system/treasury'), COALESCE((SELECT id FROM sys_permission WHERE url = '/vis' AND component_name = 'VisRoot' LIMIT 1), md5('sys_permission:/vis')), '国库', '/vis/system/treasury', 'system/TreasuryList', 'VisTreasuryList', NULL,
  1, NULL, '1', 7.00, 0, NULL, 1,
  1, 0, 0, 'vis_screen 国库入口', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE url = '/vis/system/treasury' AND component_name = 'VisTreasuryList'
);

UPDATE sys_permission
SET parent_id = (SELECT id FROM sys_permission WHERE url = '/vis' AND component_name = 'VisRoot' LIMIT 1),
    name = '国库',
    url = '/vis/system/treasury',
    component = 'system/TreasuryList',
    component_name = 'VisTreasuryList',
    redirect = NULL,
    menu_type = 1,
    sort_no = 7.00,
    always_show = 0,
    icon = NULL,
    is_route = 1,
    is_leaf = 1,
    keep_alive = 0,
    hidden = 0,
    description = 'vis_screen 国库入口',
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP,
    del_flag = 0,
    rule_flag = 0,
    status = '1'
WHERE url = '/vis/system/treasury' AND component_name = 'VisTreasuryList';

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  md5('sys_permission:/vis/bigscreen/templates'), COALESCE((SELECT id FROM sys_permission WHERE url = '/vis/bigscreen' AND component_name = 'VisBigScreenMenu' LIMIT 1), md5('sys_permission:/vis/bigscreen')), '模板管理', '/vis/bigscreen/templates', 'vis/TemplateList', 'VisTemplateList', NULL,
  1, NULL, '1', 1.00, 0, NULL, 1,
  1, 0, 0, 'vis_screen 模板管理页', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE url = '/vis/bigscreen/templates' AND component_name = 'VisTemplateList'
);

UPDATE sys_permission
SET parent_id = (SELECT id FROM sys_permission WHERE url = '/vis/bigscreen' AND component_name = 'VisBigScreenMenu' LIMIT 1),
    name = '模板管理',
    url = '/vis/bigscreen/templates',
    component = 'vis/TemplateList',
    component_name = 'VisTemplateList',
    redirect = NULL,
    menu_type = 1,
    sort_no = 1.00,
    always_show = 0,
    icon = NULL,
    is_route = 1,
    is_leaf = 1,
    keep_alive = 0,
    hidden = 0,
    description = 'vis_screen 模板管理页',
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP,
    del_flag = 0,
    rule_flag = 0,
    status = '1'
WHERE url = '/vis/bigscreen/templates' AND component_name = 'VisTemplateList';

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  md5('sys_permission:/vis/bigscreen/pages'), COALESCE((SELECT id FROM sys_permission WHERE url = '/vis/bigscreen' AND component_name = 'VisBigScreenMenu' LIMIT 1), md5('sys_permission:/vis/bigscreen')), '页面管理', '/vis/bigscreen/pages', 'vis/PageList', 'VisPageList', NULL,
  1, NULL, '1', 2.00, 0, NULL, 1,
  1, 0, 0, 'vis_screen 页面管理页', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE url = '/vis/bigscreen/pages' AND component_name = 'VisPageList'
);

UPDATE sys_permission
SET parent_id = (SELECT id FROM sys_permission WHERE url = '/vis/bigscreen' AND component_name = 'VisBigScreenMenu' LIMIT 1),
    name = '页面管理',
    url = '/vis/bigscreen/pages',
    component = 'vis/PageList',
    component_name = 'VisPageList',
    redirect = NULL,
    menu_type = 1,
    sort_no = 2.00,
    always_show = 0,
    icon = NULL,
    is_route = 1,
    is_leaf = 1,
    keep_alive = 0,
    hidden = 0,
    description = 'vis_screen 页面管理页',
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP,
    del_flag = 0,
    rule_flag = 0,
    status = '1'
WHERE url = '/vis/bigscreen/pages' AND component_name = 'VisPageList';

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  md5('sys_permission:/vis/bigscreen/schemes'), COALESCE((SELECT id FROM sys_permission WHERE url = '/vis/bigscreen' AND component_name = 'VisBigScreenMenu' LIMIT 1), md5('sys_permission:/vis/bigscreen')), '展示方案管理', '/vis/bigscreen/schemes', 'vis/SchemeList', 'VisSchemeList', NULL,
  1, NULL, '1', 3.00, 0, NULL, 1,
  1, 0, 0, 'vis_screen 方案管理页', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE url = '/vis/bigscreen/schemes' AND component_name = 'VisSchemeList'
);

UPDATE sys_permission
SET parent_id = (SELECT id FROM sys_permission WHERE url = '/vis/bigscreen' AND component_name = 'VisBigScreenMenu' LIMIT 1),
    name = '展示方案管理',
    url = '/vis/bigscreen/schemes',
    component = 'vis/SchemeList',
    component_name = 'VisSchemeList',
    redirect = NULL,
    menu_type = 1,
    sort_no = 3.00,
    always_show = 0,
    icon = NULL,
    is_route = 1,
    is_leaf = 1,
    keep_alive = 0,
    hidden = 0,
    description = 'vis_screen 方案管理页',
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP,
    del_flag = 0,
    rule_flag = 0,
    status = '1'
WHERE url = '/vis/bigscreen/schemes' AND component_name = 'VisSchemeList';

UPDATE sys_permission
SET parent_id = (SELECT id FROM sys_permission WHERE url = '/vis/bigscreen' AND component_name = 'VisBigScreenMenu' LIMIT 1),
    name = '页面编辑',
    url = '/vis/bigscreen/pages/editor',
    component = 'vis/PageEditorEntry',
    component_name = 'VisPageEditor',
    redirect = NULL,
    menu_type = 1,
    sort_no = 95.00,
    always_show = 0,
    icon = NULL,
    is_route = 1,
    is_leaf = 1,
    keep_alive = 0,
    hidden = 1,
    description = 'vis_screen 页面编辑过渡入口，承接旧 AddTemplate 路由',
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP,
    del_flag = 0,
    rule_flag = 0,
    status = '1'
WHERE url = '/vis/bigscreen/pages/editor'
  OR url = '/bigScreen/AddTemplate';

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  md5('sys_permission:/vis/bigscreen/pages/editor'), COALESCE((SELECT id FROM sys_permission WHERE url = '/vis/bigscreen' AND component_name = 'VisBigScreenMenu' LIMIT 1), md5('sys_permission:/vis/bigscreen')), '页面编辑', '/vis/bigscreen/pages/editor', 'vis/PageEditorEntry', 'VisPageEditor', NULL,
  1, NULL, '1', 95.00, 0, NULL, 1,
  1, 0, 1, 'vis_screen 页面编辑过渡入口，承接旧 AddTemplate 路由', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1
  FROM sys_permission
  WHERE url = '/vis/bigscreen/pages/editor'
);

DELETE FROM sys_role_permission
WHERE permission_id IN (
  SELECT id FROM sys_permission WHERE url = '/vis/preview' AND component_name = 'VisPreviewEntry'
);

DELETE FROM sys_permission
WHERE url = '/vis/preview' AND component_name = 'VisPreviewEntry';

-- 恢复旧 GK 菜单“指标库/指标查询”的原始统计页入口。
-- 这条菜单属于旧“指标库”菜单树，不属于 vis 菜单树，不应继续指向 /vis/index-library。
UPDATE sys_permission
SET url = '/statistics/schemeIndex',
    component = 'statistics/schemeIndex',
    component_name = NULL,
    redirect = NULL,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = 'cae8031ed1a7aeaed5625928a5ed74da'
  AND parent_id = 'aea6b487925d084dad182e09c95a6c79'
  AND name = '指标查询';

INSERT INTO sys_role (
  id, role_name, role_code, description,
  create_by, create_time, update_by, update_time
)
SELECT
  md5('sys_role:vis_screen_manager'), '可视化大屏', 'vis_screen_manager', 'vis_screen 并入 GK 后的业务角色',
  'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM sys_role WHERE role_code = 'vis_screen_manager'
);

UPDATE sys_role
SET role_name = '可视化大屏',
    role_code = 'vis_screen_manager',
    description = 'vis_screen 并入 GK 后的业务角色',
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE role_code = 'vis_screen_manager' OR id = md5('sys_role:vis_screen_manager');

WITH vis_root AS (
  SELECT id
  FROM sys_permission
  WHERE url = '/vis' AND component_name = 'VisRoot'
  LIMIT 1
),
vis_bigscreen AS (
  SELECT id
  FROM sys_permission
  WHERE url = '/vis/bigscreen' AND component_name = 'VisBigScreenMenu'
  LIMIT 1
),
vis_permissions AS (
  SELECT sp.id AS permission_id
  FROM sys_permission sp
  WHERE sp.id = (SELECT id FROM vis_root)
     OR sp.id = (SELECT id FROM vis_bigscreen)
     OR (sp.parent_id = (SELECT id FROM vis_root)
         AND sp.url IN ('/vis/gallery', '/vis/index-library', '/vis/system/dict', '/vis/system/business-type', '/vis/system/treasury'))
     OR (sp.parent_id = (SELECT id FROM vis_bigscreen)
         AND sp.url IN ('/vis/bigscreen/templates', '/vis/bigscreen/pages', '/vis/bigscreen/schemes', '/vis/bigscreen/pages/editor'))
)
INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids)
SELECT
  md5(vr.role_id || ':' || p.permission_id),
  vr.role_id,
  p.permission_id,
  ''
FROM (
  SELECT DISTINCT role_id
  FROM sys_role_permission
  WHERE permission_id IN (SELECT permission_id FROM vis_permissions)
  UNION
  SELECT id AS role_id
  FROM sys_role
  WHERE role_code = 'vis_screen_manager' OR id = md5('sys_role:vis_screen_manager')
) vr
CROSS JOIN vis_permissions p
WHERE NOT EXISTS (
    SELECT 1
    FROM sys_role_permission srp
    WHERE srp.role_id = vr.role_id
      AND srp.permission_id = p.permission_id
  );