-- 补齐 vis 合并后遗漏的系统管理菜单。
-- 范围仅包含原 vis_screen 中除用户管理/角色管理/菜单管理外仍需保留的三个入口：
-- 数据字典、业务类型、国库。

SET search_path TO "jeecg-boot-os", public;

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
WHERE EXISTS (
  SELECT 1 FROM sys_permission WHERE id = 'vis_root_menu_20260423'
)
  AND NOT EXISTS (
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
    is_route = 1,
    is_leaf = 1,
    keep_alive = 0,
    hidden = 0,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP,
    del_flag = 0,
    rule_flag = 0,
    status = '1'
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
WHERE EXISTS (
  SELECT 1 FROM sys_permission WHERE id = 'vis_root_menu_20260423'
)
  AND NOT EXISTS (
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
    is_route = 1,
    is_leaf = 1,
    keep_alive = 0,
    hidden = 0,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP,
    del_flag = 0,
    rule_flag = 0,
    status = '1'
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
WHERE EXISTS (
  SELECT 1 FROM sys_permission WHERE id = 'vis_root_menu_20260423'
)
  AND NOT EXISTS (
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
    is_route = 1,
    is_leaf = 1,
    keep_alive = 0,
    hidden = 0,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP,
    del_flag = 0,
    rule_flag = 0,
    status = '1'
WHERE id = 'vis_treasury_menu_20260424';

INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids)
SELECT
  md5(vr.role_id || ':' || p.permission_id),
  vr.role_id,
  p.permission_id,
  ''
FROM (
  SELECT DISTINCT role_id
  FROM sys_role_permission
  WHERE permission_id IN (
    'vis_root_menu_20260423',
    'vis_preview_menu_20260423',
    'vis_gallery_menu_20260423',
    'vis_bigscreen_menu_20260423',
    'vis_index_library_menu_20260423'
  )
) vr
CROSS JOIN (
  SELECT 'vis_dict_menu_20260424' AS permission_id
  UNION ALL
  SELECT 'vis_business_type_menu_20260424' AS permission_id
  UNION ALL
  SELECT 'vis_treasury_menu_20260424' AS permission_id
) p
WHERE EXISTS (
  SELECT 1 FROM sys_permission sp WHERE sp.id = p.permission_id
)
  AND NOT EXISTS (
    SELECT 1
    FROM sys_role_permission srp
    WHERE srp.role_id = vr.role_id
      AND srp.permission_id = p.permission_id
  );