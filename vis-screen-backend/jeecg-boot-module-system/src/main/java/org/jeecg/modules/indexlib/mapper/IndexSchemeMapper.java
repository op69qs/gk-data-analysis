package org.jeecg.modules.indexlib.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IndexSchemeMapper {
    String getAllTrsInfo();

    String getAllAreaInfo();

    List<Map<String, Object>> execSchemeSql(@Param("params") PageData pageData);

    Map<String, String> getSchemeInfoById(@Param("params") PageData pageData);

    int getSchemeCount(@Param("params") PageData pageData);

    List<Map<String, Object>> selectSchemeTable(@Param("params") PageData pageData);

    void deleteSchemeById(@Param("params") Map<String, Object> params);

    List<Map<String, String>> getIndexNames(@Param("params") PageData pageData);
}
