package org.seo.service;

import org.seo.util.PageData;
import java.util.List;
import java.util.Map;

public interface ComprehensiveQueryService {

    List<Map<String,Object>> getTableName(PageData pd);

    List<Map<String,Object>> getColumn(PageData pd);

    Map<String,String> getType(PageData pd);

    List<Map<String,Object>> getSchemeMain(PageData pd);

    List<Map<String,Object>> checkScheme(PageData pd);

    List<Map<String,Object>> getSchemeMainPage(PageData pd);

    void addSchemeMain(PageData pd);

    void editSchemeMain(PageData pd);

    void delSchemeMain(PageData pd);

    List<Map<String,Object>> getSchemeTable(PageData pd);

    void addSchemeTable(PageData pd);

    void delSchemeTable(PageData pd);

    List<Map<String,Object>> getSchemeColumn(PageData pd);

    void addSchemeColumn(PageData pd);

    void delSchemeColumn(PageData pd);

    List<Map<String,Object>> getSchemeWhere(PageData pd);

    void addSchemeWhere(PageData pd);

    void delSchemeWhere(PageData pd);

    List<Map<String,Object>> getSchemeOrder(PageData pd);

    void addSchemeOrder(PageData pd);

    void delSchemeOrder(PageData pd);

    Integer countMain(PageData pd);

    List<Map<String,Object>> getRelationMain(PageData pd);

    void addRelationMain(PageData pd);

    void delRelationMain(PageData pd);

    List<Map<String,Object>> getRelationSub(PageData pd);

    void addRelationSub(PageData pd);

    void delRelationSub(PageData pd);

    List<Map<String,Object>> getSchemeIndication(PageData pd);

    void addSchemeIndication(PageData pd);

    void delSchemeIndication(PageData pd);

    List<Map<String,Object>> executeSql(PageData pd,String dataSource_id);

    Integer countSql(PageData pd,String dataSource_id);

    Map<String, Object> getIndicatorScheme(String schemeId);
}
