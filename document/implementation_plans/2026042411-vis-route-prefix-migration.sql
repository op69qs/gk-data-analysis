-- 将现有 sys_permission 中的 vis 历史菜单路径统一收敛到 /vis/... 前缀。
-- 执行前请先备份 sys_permission。

UPDATE sys_permission
SET url = '/vis/gallery',
    component = 'vis/GalleryList',
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = '1320627091170963458'
   OR (url = '/gallery' AND component IN ('gallery/GalleryList', 'vis/GalleryList'));

UPDATE sys_permission
SET url = '/vis/bigscreen',
    component = 'layouts/RouteView',
    redirect = '/vis/bigscreen/templates',
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = '1320628439870386178'
   OR url = '/BigScreen';

UPDATE sys_permission
SET url = '/vis/bigscreen/templates',
    component = 'vis/TemplateList',
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = '1320628710990196737'
   OR url = '/bigScreen/TemplateList'
   OR component = 'BigScreen/TemplateList';

UPDATE sys_permission
SET url = '/vis/bigscreen/pages',
    component = 'vis/PageList',
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = '1320628838421540865'
   OR url = '/BigScreen/PageList'
   OR component = 'BigScreen/PageList';

UPDATE sys_permission
SET url = '/vis/bigscreen/schemes',
    component = 'vis/SchemeList',
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = '1320628986648244225'
   OR url = '/BigScreen/ExhibitionSchemeList'
   OR component = 'BigScreen/ExhibitionSchemeList';

UPDATE sys_permission
SET url = '/vis/bigscreen/pages/editor',
   component = 'vis/PageEditorEntry',
   component_name = 'VisPageEditor',
   name = '页面编辑',
   hidden = 1,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = '1321380065238953985'
   OR url = '/bigScreen/AddTemplate'
   OR component = 'BigScreen/AddTemplate';

UPDATE sys_permission
SET url = '/vis/index-library',
    component = 'vis/SchemeList',
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = '1450704195020578817'
   OR url = '/indexLibrary'
   OR url = '/statistics/indexLibrary'
   OR (url = '/vis/index-library' AND component = 'statistics/indexLibrary');

DELETE FROM sys_permission
WHERE id IN (
       'vis_legacy_gallery_menu_20260423',
       'vis_legacy_index_library_menu_20260423',
       'vis_legacy_preview_menu_20260423',
       'vis_legacy_template_menu_20260423',
       'vis_legacy_page_menu_20260423',
       'vis_legacy_scheme_menu_20260423'
     )
    OR url IN (
          '/gallery',
          '/indexLibrary',
          '/BigScreenPreview',
          '/bigScreen/TemplateList',
          '/BigScreen/PageList',
          '/BigScreen/ExhibitionSchemeList'
       );
