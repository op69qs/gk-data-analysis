-- 目标：在主系统 Schema "jeecg-boot-os" 创建数据上报菜单与未来按钮权限点。
-- 可重复执行：是；按确定性 ID 和 NOT EXISTS 防重复。
-- 当前上线规则：只给角色分配 menu_type=0/1 的菜单行；menu_type=2 按钮行暂不授权、不启用接口拦截。

set search_path to "jeecg-boot-os", public;

insert into sys_permission
(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type,
 sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, description,
 create_by, create_time, update_by, update_time, del_flag, rule_flag, status)
select md5('sys_permission:/reporting'), '', '数据上报', '/reporting', 'layouts/RouteView',
       'ReportingRoot', '/reporting/batches', 0, null, '1', 850, 1, 'cloud-upload', 1, 0, 0, 0,
       '收入、支出、库存及 KEY 文件上报', 'admin', current_timestamp, 'admin', current_timestamp, 0, 0, '1'
where not exists (select 1 from sys_permission where id = md5('sys_permission:/reporting'));

insert into sys_permission
(id, parent_id, name, url, component, component_name, menu_type, perms, perms_type, sort_no,
 always_show, icon, is_route, is_leaf, keep_alive, hidden, description, create_by, create_time,
 update_by, update_time, del_flag, rule_flag, status)
select md5('sys_permission:/reporting/' || item.path), md5('sys_permission:/reporting'),
       item.name, '/reporting/' || item.path, item.component, item.component_name, 1, null, '1', item.sort_no,
       0, item.icon, 1, 1, 0, 0, item.description, 'admin', current_timestamp,
       'admin', current_timestamp, 0, 0, '1'
from (values
  ('batches', '上报批次', 'reporting/ReportBatchList', 'ReportBatchList', 1, 'unordered-list', '上传、执行进度、详情和重试'),
  ('monitoring', '上报监控', 'reporting/ReportMonitoring', 'ReportMonitoring', 2, 'dashboard', 'KEY/TIMS 完整性和异常监控'),
  ('agent-treasuries', '代理国库配置', 'reporting/AgentTreasuryConfig', 'AgentTreasuryConfig', 3, 'setting', '代理国库有效期和启停'),
  ('changes', '报送调整记录', 'reporting/ReportChangeRecord', 'ReportChangeRecord', 4, 'edit', '收入支出查询与人工调整记录')
) as item(path, name, component, component_name, sort_no, icon, description)
where not exists (select 1 from sys_permission p where p.id = md5('sys_permission:/reporting/' || item.path));

insert into sys_permission
(id, parent_id, name, url, component, component_name, menu_type, perms, perms_type, sort_no,
 always_show, is_route, is_leaf, keep_alive, hidden, description, create_by, create_time,
 update_by, update_time, del_flag, rule_flag, status)
select md5('sys_permission:button:' || item.perms),
       md5('sys_permission:/reporting/' || item.parent_path), item.name,
       null, null, null, 2, item.perms, '1', item.sort_no,
       0, 0, 1, 0, 1, '预留按钮权限；当前仅菜单控制',
       'admin', current_timestamp, 'admin', current_timestamp, 0, 0, '1'
from (values
  ('batches', '上传文件', 'reporting:batch:upload', 1),
  ('batches', '下载文件', 'reporting:file:download', 2),
  ('batches', '重新解析/入库', 'reporting:batch:retry', 3),
  ('batches', '再次加工', 'reporting:batch:process', 4),
  ('batches', '逻辑删除', 'reporting:batch:delete', 5),
  ('batches', '上报审核', 'reporting:batch:audit', 6),
  ('agent-treasuries', '新增代理国库', 'reporting:treasury:add', 1),
  ('agent-treasuries', '编辑代理国库', 'reporting:treasury:edit', 2),
  ('changes', '新增调整记录', 'reporting:change:add', 1),
  ('batches', '物理清理归档', 'reporting:archive:cleanup', 9)
) as item(parent_path, name, perms, sort_no)
where not exists (select 1 from sys_permission p where p.id = md5('sys_permission:button:' || item.perms));

select id, parent_id, name, url, component, menu_type, perms
from sys_permission where id = md5('sys_permission:/reporting')
   or parent_id = md5('sys_permission:/reporting')
   or parent_id in (select id from sys_permission where parent_id = md5('sys_permission:/reporting'))
order by menu_type, sort_no;
