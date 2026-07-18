package org.jeecg.modules.indexlib.service;

import org.jeecg.modules.util.PageData;

import java.util.List;
import java.util.Map;

public interface IndexRelationService {
    List<Map<String, String>> getBatchIndexInfo(PageData pageData);

    Map<String, Object> getIndexDetails(PageData pageData);
}
