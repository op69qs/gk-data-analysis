package org.jeecg.modules.enumSetting.service;

import org.jeecg.modules.util.PageData;

import java.util.List;
import java.util.Map;

public interface ErrorLogService {

    List<Map<String, Object>> getData(PageData pd);

    String callProc(PageData pd);

    Integer getCount(PageData pd);

    List<Map<String, Object>> getRunRecords(PageData pd);

    Integer getRunRecordCount(PageData pd);

    void add(PageData pd);

    void edit(PageData pd);

    void del(PageData pd);
}
