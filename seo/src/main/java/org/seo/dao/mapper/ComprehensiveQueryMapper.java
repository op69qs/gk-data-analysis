package org.seo.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.seo.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ComprehensiveQueryMapper {

    List<Map<String,Object>> getTableName(@Param("params") PageData pd);

    List<Map<String,Object>> getColumn(@Param("params") PageData pd);

    Map<String,String> getType(@Param("params") PageData pd);

    List<Map<String,Object>> getSchemeMain(@Param("params") PageData pd);

    List<Map<String,Object>> checkScheme(@Param("params") PageData pd);

    List<Map<String,Object>> getSchemeMainPage(@Param("params") PageData pd);

    void addSchemeMain(@Param("params") PageData pd);

    void editSchemeMain(@Param("params") PageData pd);

    void delSchemeMain(@Param("params") PageData pd);

    List<Map<String,Object>> getSchemeTable(@Param("params") PageData pd);

    void addSchemeTable(@Param("params") PageData pd);

    void delSchemeTable(@Param("params") PageData pd);

    List<Map<String,Object>> getSchemeColumn(@Param("params") PageData pd);

    void addSchemeColumn(@Param("params") PageData pd);

    void delSchemeColumn(@Param("params") PageData pd);

    List<Map<String,Object>> getSchemeWhere(@Param("params") PageData pd);

    void addSchemeWhere(@Param("params") PageData pd);

    void delSchemeWhere(@Param("params") PageData pd);

    List<Map<String,Object>> getSchemeOrder(@Param("params") PageData pd);

    void addSchemeOrder(@Param("params") PageData pd);

    void delSchemeOrder(@Param("params") PageData pd);

    Integer countMain(@Param("params") PageData pd);

    List<Map<String,Object>> getRelationMain(@Param("params") PageData pd);

    void addRelationMain(@Param("params") PageData pd);

    void delRelationMain(@Param("params") PageData pd);

    List<Map<String,Object>> getRelationSub(@Param("params") PageData pd);

    void addRelationSub(@Param("params") PageData pd);

    void delRelationSub(@Param("params") PageData pd);

    List<Map<String,Object>> getSchemeIndication(@Param("params") PageData pd);

    void addSchemeIndication(@Param("params") PageData pd);

    void delSchemeIndication(@Param("params") PageData pd);

    List<Map<String,Object>> executeSql(@Param("params") PageData pd);

    Integer countSql(@Param("params") PageData pd);
}
