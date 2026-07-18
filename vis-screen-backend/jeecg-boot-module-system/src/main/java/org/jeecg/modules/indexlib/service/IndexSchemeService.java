package org.jeecg.modules.indexlib.service;

import org.jeecg.modules.util.PageData;

import java.util.List;
import java.util.Map;

public interface IndexSchemeService {
    String getAllTrsInfo();

    String getAllAreaInfo();

    List<Map<String, Object>> execSchemeSql(PageData pageData);

    Map<String, String> getSchemeInfoById(PageData pageData);

    int getSchemeCount(PageData pageData);

    List<Map<String, Object>> selectSchemeTable(PageData pageData);

    void deleteSchemeById(Map<String, Object> params);

    List<Map<String, String>> getIndexNames(PageData pageData);
}
