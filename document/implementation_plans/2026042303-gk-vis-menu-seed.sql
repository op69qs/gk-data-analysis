-- GK 主系统接入 vis 页面级菜单的业务种子数据。
-- 导入后会生成一个根菜单“可视化大屏”，并按 vis 原菜单结构挂出预览概览、图库、大屏设置等入口。
-- 同时补 system/TreasuryList 和 system/BusinessTypeList 两个仍沿用“系统管理”父菜单的兼容入口。

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  'vis_root_menu_20260423', '', '可视化大屏', '/vis', 'layouts/RouteView', 'VisRoot', '/vis/preview',
  0, NULL, '1', 999.00, 0, 'dashboard', 1,
  0, 0, 0, 'vis_screen 并入 GK 后的页面级菜单根节点', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE id = 'vis_root_menu_20260423'
);

UPDATE sys_permission
SET sort_no = 999.00,
    always_show = 0,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = 'vis_root_menu_20260423';

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  'vis_preview_menu_20260423', 'vis_root_menu_20260423', '预览概览', '/vis/preview', 'vis/PreviewEntry', 'VisPreviewEntry', NULL,
  1, NULL, '1', 1.00, 0, NULL, 1,
  1, 0, 0, 'vis_screen 首个接入 GK 的承接页', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE id = 'vis_preview_menu_20260423'
);

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  'vis_gallery_menu_20260423', 'vis_root_menu_20260423', '图库', '/vis/gallery', 'vis/GalleryList', 'VisGalleryList', NULL,
  1, NULL, '1', 2.00, 0, NULL, 1,
  1, 0, 0, 'vis_screen 图库页', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE id = 'vis_gallery_menu_20260423'
);

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  'vis_bigscreen_menu_20260423', 'vis_root_menu_20260423', '大屏设置', '/vis/bigscreen', 'layouts/RouteView', 'VisBigScreenMenu', '/vis/bigscreen/templates',
  0, NULL, '1', 3.00, 0, 'tool', 1,
  0, 0, 0, 'vis_screen 原大屏设置菜单分组', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE id = 'vis_bigscreen_menu_20260423'
);

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  'vis_index_library_menu_20260423', 'vis_root_menu_20260423', '指标库方案', '/vis/index-library', 'vis/SchemeList', 'VisIndexLibrary', NULL,
  1, NULL, '1', 4.00, 0, NULL, 1,
  1, 0, 0, 'vis_screen 原指标库方案入口，映射到 vis 展示方案管理页', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE id = 'vis_index_library_menu_20260423'
);

UPDATE sys_permission
SET parent_id = 'vis_root_menu_20260423',
    name = '图库',
    url = '/vis/gallery',
    component = 'vis/GalleryList',
    component_name = 'VisGalleryList',
    sort_no = 2.00,
    always_show = 0,
    icon = NULL,
    is_leaf = 1,
    hidden = 0,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = 'vis_gallery_menu_20260423';

UPDATE sys_permission
SET parent_id = 'vis_root_menu_20260423',
    name = '大屏设置',
    url = '/vis/bigscreen',
    component = 'layouts/RouteView',
    component_name = 'VisBigScreenMenu',
    redirect = '/vis/bigscreen/templates',
    sort_no = 3.00,
    always_show = 0,
    icon = 'tool',
    is_leaf = 0,
    hidden = 0,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = 'vis_bigscreen_menu_20260423';

  UPDATE sys_permission
  SET parent_id = 'vis_root_menu_20260423',
    name = '指标库方案',
    url = '/vis/index-library',
    component = 'vis/SchemeList',
    component_name = 'VisIndexLibrary',
    sort_no = 4.00,
    always_show = 0,
    icon = NULL,
    is_leaf = 1,
    hidden = 0,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
  WHERE id = 'vis_index_library_menu_20260423';

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  'vis_dict_menu_20260424', 'vis_root_menu_20260423', '数据字典', '/vis/system/dict', 'system/DictList', 'VisDictList', NULL,
  1, NULL, '1', 5.00, 0, NULL, 1,
  1, 0, 0, 'vis_screen 数据字典入口', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE id = 'vis_dict_menu_20260424'
);

UPDATE sys_permission
SET parent_id = 'vis_root_menu_20260423',
    name = '数据字典',
    url = '/vis/system/dict',
    component = 'system/DictList',
    component_name = 'VisDictList',
    sort_no = 5.00,
    always_show = 0,
    icon = NULL,
    is_leaf = 1,
    hidden = 0,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = 'vis_dict_menu_20260424';

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  'vis_business_type_menu_20260424', 'vis_root_menu_20260423', '业务类型', '/vis/system/business-type', 'system/BusinessTypeList', 'VisBusinessTypeList', NULL,
  1, NULL, '1', 6.00, 0, NULL, 1,
  1, 0, 0, 'vis_screen 业务类型入口', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE id = 'vis_business_type_menu_20260424'
);

UPDATE sys_permission
SET parent_id = 'vis_root_menu_20260423',
    name = '业务类型',
    url = '/vis/system/business-type',
    component = 'system/BusinessTypeList',
    component_name = 'VisBusinessTypeList',
    sort_no = 6.00,
    always_show = 0,
    icon = NULL,
    is_leaf = 1,
    hidden = 0,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = 'vis_business_type_menu_20260424';

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  'vis_treasury_menu_20260424', 'vis_root_menu_20260423', '国库', '/vis/system/treasury', 'system/TreasuryList', 'VisTreasuryList', NULL,
  1, NULL, '1', 7.00, 0, NULL, 1,
  1, 0, 0, 'vis_screen 国库入口', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE id = 'vis_treasury_menu_20260424'
);

UPDATE sys_permission
SET parent_id = 'vis_root_menu_20260423',
    name = '国库',
    url = '/vis/system/treasury',
    component = 'system/TreasuryList',
    component_name = 'VisTreasuryList',
    sort_no = 7.00,
    always_show = 0,
    icon = NULL,
    is_leaf = 1,
    hidden = 0,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = 'vis_treasury_menu_20260424';

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  'vis_scheme_menu_20260423', 'vis_bigscreen_menu_20260423', '展示方案管理', '/vis/bigscreen/schemes', 'vis/SchemeList', 'VisSchemeList', NULL,
  1, NULL, '1', 3.00, 0, NULL, 1,
  1, 0, 0, 'vis_screen 方案管理页', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE id = 'vis_scheme_menu_20260423'
);

UPDATE sys_permission
SET parent_id = 'vis_bigscreen_menu_20260423',
    name = '展示方案管理',
    url = '/vis/bigscreen/schemes',
    component = 'vis/SchemeList',
    component_name = 'VisSchemeList',
    sort_no = 3.00,
    always_show = 0,
    icon = NULL,
    is_leaf = 1,
    hidden = 0,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = 'vis_scheme_menu_20260423';

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  'vis_page_menu_20260423', 'vis_bigscreen_menu_20260423', '页面管理', '/vis/bigscreen/pages', 'vis/PageList', 'VisPageList', NULL,
  1, NULL, '1', 4.00, 0, NULL, 1,
  1, 0, 0, 'vis_screen 页面管理页', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE id = 'vis_page_menu_20260423'
);

UPDATE sys_permission
SET parent_id = 'vis_bigscreen_menu_20260423',
    name = '页面管理',
    url = '/vis/bigscreen/pages',
    component = 'vis/PageList',
    component_name = 'VisPageList',
    sort_no = 2.00,
    always_show = 0,
    icon = NULL,
    is_leaf = 1,
    hidden = 0,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = 'vis_page_menu_20260423';

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  '1321380065238953985', 'vis_bigscreen_menu_20260423', '页面编辑', '/vis/bigscreen/pages/editor', 'vis/PageEditorEntry', 'VisPageEditor', NULL,
  1, NULL, '1', 2.50, 0, NULL, 1,
  1, 0, 1, 'vis_screen 页面编辑过渡入口，承接旧 AddTemplate 路由', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE id = '1321380065238953985'
);

UPDATE sys_permission
SET parent_id = 'vis_bigscreen_menu_20260423',
    name = '页面编辑',
    url = '/vis/bigscreen/pages/editor',
  component = 'vis/PageEditorEntry',
    component_name = 'VisPageEditor',
    sort_no = 2.50,
    always_show = 0,
    icon = NULL,
    is_leaf = 1,
    hidden = 1,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = '1321380065238953985';

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  'vis_template_menu_20260423', 'vis_bigscreen_menu_20260423', '模板管理', '/vis/bigscreen/templates', 'vis/TemplateList', 'VisTemplateList', NULL,
  1, NULL, '1', 5.00, 0, NULL, 1,
  1, 0, 0, 'vis_screen 模板管理页', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE NOT EXISTS (
  SELECT 1 FROM sys_permission WHERE id = 'vis_template_menu_20260423'
);

UPDATE sys_permission
SET parent_id = 'vis_bigscreen_menu_20260423',
    name = '模板管理',
    url = '/vis/bigscreen/templates',
    component = 'vis/TemplateList',
    component_name = 'VisTemplateList',
    sort_no = 1.00,
    always_show = 0,
    icon = NULL,
    is_leaf = 1,
    hidden = 0,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = 'vis_template_menu_20260423';

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  '1320623776118910977',
  COALESCE(
    (SELECT id FROM sys_permission WHERE id = 'd7d6e2e4e2934f2c9385a623fd98c6f3' LIMIT 1),
    (SELECT id FROM sys_permission WHERE url = '/isystem' LIMIT 1),
    (SELECT id FROM sys_permission WHERE name = '系统管理' AND component = 'layouts/RouteView' LIMIT 1)
  ),
  '业务类型', '/isystem/BusinessTypeList', 'system/BusinessTypeList', 'VisSystemBusinessTypeList', NULL,
  1, NULL, '1', 6.00, 0, NULL, 1,
  1, 0, 0, 'vis_screen 系统管理兼容入口: 业务类型', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE COALESCE(
  (SELECT id FROM sys_permission WHERE id = 'd7d6e2e4e2934f2c9385a623fd98c6f3' LIMIT 1),
  (SELECT id FROM sys_permission WHERE url = '/isystem' LIMIT 1),
  (SELECT id FROM sys_permission WHERE name = '系统管理' AND component = 'layouts/RouteView' LIMIT 1)
) IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE id = '1320623776118910977'
  );

UPDATE sys_permission
SET parent_id = COALESCE(
      (SELECT id FROM sys_permission WHERE id = 'd7d6e2e4e2934f2c9385a623fd98c6f3' LIMIT 1),
      (SELECT id FROM sys_permission WHERE url = '/isystem' LIMIT 1),
      (SELECT id FROM sys_permission WHERE name = '系统管理' AND component = 'layouts/RouteView' LIMIT 1)
    ),
    name = '业务类型',
    url = '/isystem/BusinessTypeList',
    component = 'system/BusinessTypeList',
    component_name = 'VisSystemBusinessTypeList',
    sort_no = 6.00,
    always_show = 0,
    icon = NULL,
    is_leaf = 1,
    hidden = 0,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = '1320623776118910977';

INSERT INTO sys_permission (
  id, parent_id, name, url, component, component_name, redirect,
  menu_type, perms, perms_type, sort_no, always_show, icon, is_route,
  is_leaf, keep_alive, hidden, description, create_by, create_time,
  update_by, update_time, del_flag, rule_flag, status
)
SELECT
  '1320624219096133633',
  COALESCE(
    (SELECT id FROM sys_permission WHERE id = 'd7d6e2e4e2934f2c9385a623fd98c6f3' LIMIT 1),
    (SELECT id FROM sys_permission WHERE url = '/isystem' LIMIT 1),
    (SELECT id FROM sys_permission WHERE name = '系统管理' AND component = 'layouts/RouteView' LIMIT 1)
  ),
  '国库', '/isystem/TreasuryList', 'system/TreasuryList', 'VisSystemTreasuryList', NULL,
  1, NULL, '1', 6.10, 0, NULL, 1,
  1, 0, 0, 'vis_screen 系统管理兼容入口: 国库', 'admin', CURRENT_TIMESTAMP,
  'admin', CURRENT_TIMESTAMP, 0, 0, '1'
WHERE COALESCE(
  (SELECT id FROM sys_permission WHERE id = 'd7d6e2e4e2934f2c9385a623fd98c6f3' LIMIT 1),
  (SELECT id FROM sys_permission WHERE url = '/isystem' LIMIT 1),
  (SELECT id FROM sys_permission WHERE name = '系统管理' AND component = 'layouts/RouteView' LIMIT 1)
) IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM sys_permission WHERE id = '1320624219096133633'
  );

UPDATE sys_permission
SET parent_id = COALESCE(
      (SELECT id FROM sys_permission WHERE id = 'd7d6e2e4e2934f2c9385a623fd98c6f3' LIMIT 1),
      (SELECT id FROM sys_permission WHERE url = '/isystem' LIMIT 1),
      (SELECT id FROM sys_permission WHERE name = '系统管理' AND component = 'layouts/RouteView' LIMIT 1)
    ),
    name = '国库',
    url = '/isystem/TreasuryList',
    component = 'system/TreasuryList',
    component_name = 'VisSystemTreasuryList',
    sort_no = 6.10,
    always_show = 0,
    icon = NULL,
    is_leaf = 1,
    hidden = 0,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = '1320624219096133633';

INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids)
SELECT
  md5(r.role_id || ':' || p.permission_id),
  r.role_id,
  p.permission_id,
  ''
FROM (
  SELECT ur.role_id
  FROM sys_user_role ur
  JOIN sys_user u ON u.id = ur.user_id
  WHERE u.username = 'admin'
) r
CROSS JOIN (
  SELECT 'vis_root_menu_20260423' AS permission_id
  UNION ALL
  SELECT 'vis_preview_menu_20260423' AS permission_id
  UNION ALL
  SELECT 'vis_gallery_menu_20260423' AS permission_id
  UNION ALL
  SELECT 'vis_bigscreen_menu_20260423' AS permission_id
  UNION ALL
  SELECT 'vis_index_library_menu_20260423' AS permission_id
  UNION ALL
  SELECT 'vis_dict_menu_20260424' AS permission_id
  UNION ALL
  SELECT 'vis_business_type_menu_20260424' AS permission_id
  UNION ALL
  SELECT 'vis_treasury_menu_20260424' AS permission_id
  UNION ALL
  SELECT 'vis_scheme_menu_20260423' AS permission_id
  UNION ALL
  SELECT 'vis_page_menu_20260423' AS permission_id
  UNION ALL
  SELECT '1321380065238953985' AS permission_id
  UNION ALL
  SELECT 'vis_template_menu_20260423' AS permission_id
  UNION ALL
  SELECT '1320623776118910977' AS permission_id
  UNION ALL
  SELECT '1320624219096133633' AS permission_id
) p
WHERE NOT EXISTS (
  SELECT 1
  FROM sys_role_permission srp
  WHERE srp.role_id = r.role_id
    AND srp.permission_id = p.permission_id
);