-- 脚本名: 2026071302-02-fix-jeecg-vis-index-library-component.sql
-- 系统: gk-data-analysis (jeecg-boot-os.sys_permission)
-- 批次: 2026071302
-- 用途: 下游分析平台本地菜单「指标库方案」组件改为 vis/IndexLibraryList，与门户一致
-- 现场执行顺序: 02（接在 01 门户脚本之后；库为 gk_data_analysis）
-- 前置: 库内已存在 url=/vis/index-library 的 VisIndexLibrary 菜单
-- 后置: 重新登录分析平台刷新动态路由
-- 副作用注意: 幂等可重跑；不删菜单

BEGIN;

SELECT
    'BEFORE_JEECG_INDEX_LIBRARY_COMPONENT_FIX' AS marker,
    id,
    name,
    url,
    component,
    component_name
FROM "jeecg-boot-os".sys_permission
WHERE url = '/vis/index-library'
   OR component_name = 'VisIndexLibrary'
ORDER BY id;

UPDATE "jeecg-boot-os".sys_permission
SET component = 'vis/IndexLibraryList',
    description = 'vis_screen 原指标库方案入口，独立页(转图/删除)',
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE (url = '/vis/index-library' OR component_name = 'VisIndexLibrary')
  AND COALESCE(component, '') <> 'vis/IndexLibraryList';

SELECT
    'AFTER_JEECG_INDEX_LIBRARY_COMPONENT_FIX' AS marker,
    id,
    name,
    url,
    component,
    component_name
FROM "jeecg-boot-os".sys_permission
WHERE url = '/vis/index-library'
   OR component_name = 'VisIndexLibrary'
ORDER BY id;

COMMIT;
